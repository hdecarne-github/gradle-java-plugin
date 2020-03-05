/*
 * Copyright (c) 2018-2020 Holger de Carne and contributors, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.carne.gradle.plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.JacksonFeature;

import de.carne.boot.Exceptions;
import de.carne.boot.logging.Log;
import de.carne.gradle.plugin.util.GitHubApi.ReleaseAssetInfo;
import de.carne.util.Late;

/**
 * GitHub Repository client.
 */
public class GitHubRepo implements AutoCloseable {

	private static final Log LOG = new Log();

	private static final Pattern GITHUB_REMOTE_URL_PATTERN = Pattern.compile("https://github.com/(.+)/(.+).git");

	private static final String GITHUB_API_BASE_URI = "https://api.github.com";

	private final File dir;
	private final String owner;
	private final String repo;
	private final String branch;
	private final String token;
	private final boolean dirty;
	private Late<Client> clientHolder = new Late<>();
	private Late<GitHubApi> apiHolder = new Late<>();

	/**
	 * Constructs a new {@linkplain GitHubRepo} instance.
	 *
	 * @param dir the local GitHub repository directory.
	 * @param token the GitHub access token.
	 * @throws IOException if an I/O error occurs while accessing the repository.
	 */
	public GitHubRepo(String dir, String token) throws IOException {
		this(new File(dir), token);
	}

	/**
	 * Constructs a new {@linkplain GitHubRepo} instance.
	 *
	 * @param dir the local GitHub repository directory.
	 * @param token the GitHub access token.
	 * @throws IOException if an I/O error occurs while accessing the repository.
	 */
	public GitHubRepo(File dir, String token) throws IOException {
		this.dir = dir.getAbsoluteFile();

		LOG.info("Accessing GitHub repository at ''{0}''...", this.dir);

		try (Git git = Objects.requireNonNull(Git.open(dir))) {
			String remoteUrl = git.getRepository().getConfig().getString("remote", "origin", "url");

			LOG.debug("  Remote URL: {0}", remoteUrl);

			Matcher remoteUrlMatcher = GITHUB_REMOTE_URL_PATTERN.matcher(remoteUrl);

			if (!remoteUrlMatcher.matches()) {
				throw new IOException("Unexpected remote url: " + remoteUrl);
			}
			this.owner = remoteUrlMatcher.group(1);
			this.repo = remoteUrlMatcher.group(2);
			this.branch = Objects.requireNonNull(git.getRepository().getBranch());

			LOG.debug("  GitHub owner : {0}", this.owner);
			LOG.debug("  GitHub repo  : {0}", this.repo);
			LOG.debug("  GitHub branch: {0}", this.branch);

			Status gitStatus = git.status().call();

			this.dirty = gitStatus.hasUncommittedChanges() || !gitStatus.getUntracked().isEmpty();
		} catch (GitAPIException e) {
			throw new IOException("A Git exception has occurred", e);
		}
		this.token = token;
	}

	/**
	 * Gets the repository directory.
	 *
	 * @return the repository directory.
	 */
	public File dir() {
		return this.dir;
	}

	/**
	 * Gets the dirty status of the repository.
	 *
	 * @return {@code true} if any uncommitted changes or untracked files exist for this repository.
	 */
	public boolean isDirty() {
		return this.dirty;
	}

	/**
	 * Queries a release by name.
	 *
	 * @param name the name of the release to query.
	 * @return the queried release's info or {@code null} if the release does not exit.
	 * @throws IOException if an I/O error occurs while performing the request.
	 */
	public GitHubApi.@Nullable ReleaseInfo queryRelease(String name) throws IOException {
		setupClientIfNeeded();

		LOG.info("Querying release ''{0}/{1}/{2}''...", this.owner, this.repo, name);

		GitHubApi.ReleaseInfo result = null;

		try {
			int page = 1;

			while (true) {
				List<GitHubApi.ReleaseInfo> releaseInfos = this.apiHolder.get().listReleases(this.owner, this.repo,
						page);

				for (GitHubApi.ReleaseInfo releaseInfo : releaseInfos) {
					if (name.equals(releaseInfo.name)) {
						result = releaseInfo;
						break;
					}
				}
				if (result != null || releaseInfos.isEmpty()) {
					break;
				}
				page++;
			}
		} catch (ClientErrorException e) {
			throw new IOException("Failed to query release '" + name + "'", e);
		}
		return result;
	}

	/**
	 * Drafts a new release.
	 *
	 * @param name the name of the new release.
	 * @param body the release description to use.
	 * @return the newly drafted release's info.
	 * @throws IOException if an I/O error occurs while performing the request.
	 */
	public GitHubApi.ReleaseInfo draftRelease(String name, String body) throws IOException {
		setupClientIfNeeded();

		LOG.info("Drafting new release ''{0}/{1}/{2}@{3}''...", this.owner, this.repo, name, this.branch);

		GitHubApi.ReleaseInfo release;

		try {
			GitHubApi.CreateOrEditReleaseRequest request = new GitHubApi.CreateOrEditReleaseRequest();

			request.tagName = name;
			request.targetCommitish = this.branch;
			request.name = name;
			request.body = body;
			request.draft = true;
			request.prerelease = name.endsWith("-SNAPSHOT");
			release = this.apiHolder.get().createRelease(this.owner, this.repo, request);
		} catch (ClientErrorException e) {
			throw new IOException("Failed to draft new release '" + name + "'", e);
		}
		return release;
	}

	/**
	 * Uploads a release asset.
	 *
	 * @param uploadUrl the upload URL of the release.
	 * @param assetFile the asset file to upload.
	 * @return the uploaded asset's info.
	 * @throws IOException if an I/O error occurs while performing the request.
	 */
	public GitHubApi.ReleaseAssetInfo uploadReleaseAsset(String uploadUrl, File assetFile) throws IOException {
		return uploadReleaseAsset(uploadUrl, assetFile, assetFile.getName(), null);
	}

	/**
	 * Uploads a release asset.
	 *
	 * @param uploadUrl the upload URL of the release.
	 * @param assetFile the asset file to upload.
	 * @param name the name of the uploaded asset.
	 * @return the uploaded asset's info.
	 * @throws IOException if an I/O error occurs while performing the request.
	 */
	public GitHubApi.ReleaseAssetInfo uploadReleaseAsset(String uploadUrl, File assetFile, String name)
			throws IOException {
		return uploadReleaseAsset(uploadUrl, assetFile, name, null);
	}

	/**
	 * Uploads a release asset.
	 *
	 * @param uploadUrl the upload URL of the release.
	 * @param assetFile the asset file to upload.
	 * @param name the name of the uploaded asset.
	 * @param label the label of the uploaded asset (may be {@code null}).
	 * @return the uploaded asset's info.
	 * @throws IOException if an I/O error occurs while performing the request.
	 */
	public GitHubApi.ReleaseAssetInfo uploadReleaseAsset(String uploadUrl, File assetFile, String name,
			@Nullable String label) throws IOException {
		setupClientIfNeeded();

		LOG.info("Uploading release asset ''{0} -> {1}''...", assetFile, uploadUrl);

		UriBuilder targetUri = UriBuilder.fromUri(uploadUrl).queryParam("name", name);

		if (label != null) {
			targetUri.queryParam("label", label);
		}

		WebTarget target = this.clientHolder.get().target(targetUri);
		ReleaseAssetInfo assetInfo;

		try (InputStream assetStream = new FileInputStream(assetFile);
				Response response = target.request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(assetStream, fileType(assetFile)))) {
			assetInfo = response.readEntity(ReleaseAssetInfo.class);
		}
		return assetInfo;
	}

	/**
	 * Deletes a repository release.
	 *
	 * @param releaseId the id of the release to delete.
	 * @throws IOException if an I/O error occurs while performing the request.
	 */
	public void deleteRelease(String releaseId) throws IOException {
		setupClientIfNeeded();

		LOG.info("Deleting release ''{0}/{1}/{2}''...", this.owner, this.repo, releaseId);

		try {
			this.apiHolder.get().deleteRelease(this.owner, this.repo, releaseId);
		} catch (NotFoundException e) {
			Exceptions.ignore(e);
		} catch (ClientErrorException e) {
			throw new IOException("Failed to delete release '" + releaseId + "'", e);
		}
	}

	@Override
	public synchronized void close() {
		this.clientHolder.getOptional().ifPresent(Client::close);
	}

	private synchronized void setupClientIfNeeded() throws IOException {
		if (!this.clientHolder.getOptional().isPresent()) {
			try {
				ClientBuilder clientBuilder = ClientBuilder.newBuilder().register(JacksonFeature.class)
						.register(ApiVersionV3.class).register(ApiAuthorization.class);
				Client client = this.clientHolder.set(clientBuilder.build());

				client.property(ApiAuthorization.class.getName(), "token " + this.token);

				GitHubApi api = WebResourceFactory.newResource(GitHubApi.class,
						client.target(new URI(GITHUB_API_BASE_URI)));

				this.apiHolder.set(api);
			} catch (URISyntaxException e) {
				throw new IOException("Invalid GitHub API Base URI: " + GITHUB_API_BASE_URI, e);
			}
		}
	}

	private static class ApiVersionV3 implements ClientRequestFilter {

		@Override
		public void filter(ClientRequestContext requestContext) throws IOException {
			requestContext.getHeaders().add("Accept", "application/vnd.github.v3+json");
		}

	}

	private static class ApiAuthorization implements ClientRequestFilter {

		@Override
		public void filter(ClientRequestContext requestContext) throws IOException {
			requestContext.getHeaders().add("Authorization",
					requestContext.getClient().getConfiguration().getProperty(ApiAuthorization.class.getName()));
		}

	}

	private static final String[] FILE_TYPE_MAP = new String[] {
			// *.exe
			".exe", "application/vnd.microsoft.portable-executable",
			// *.gz
			".gz", "application/gzip",
			// *.html
			".html", "text/html",
			// *.jar
			".jar", "application/zip",
			// *.md
			".md", "text/markdown",
			// *.xml
			".xml", "xml/xml",
			// *.zip
			".zip", "application/zip",
			// *
			"", "application/binary" };

	private static MediaType fileType(File file) {
		String fileName = file.getName();
		int fileTypeIndex = 0;
		String fileType = null;

		while (fileType == null) {
			if (fileName.endsWith(FILE_TYPE_MAP[fileTypeIndex])) {
				fileType = FILE_TYPE_MAP[fileTypeIndex + 1];
			}
			fileTypeIndex += 2;
		}
		return MediaType.valueOf(fileType);
	}

}
