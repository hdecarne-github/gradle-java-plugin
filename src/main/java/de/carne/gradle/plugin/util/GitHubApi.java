/*
 * Copyright (c) 2018-2021 Holger de Carne and contributors, All Rights Reserved.
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

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GitHub API interface.
 */
public interface GitHubApi {

	/**
	 * Lists all releases.
	 *
	 * @param owner the owner of the release to get.
	 * @param repo the repository of the release to get.
	 * @param page the result page to get.
	 * @return the retrieved release.
	 */
	@GET
	@Path("/repos/{owner}/{repo}/releases")
	@Produces(MediaType.APPLICATION_JSON)
	List<ReleaseInfo> listReleases(@PathParam("owner") String owner, @PathParam("repo") String repo,
			@QueryParam("page") int page);

	/**
	 * Gets a release.
	 *
	 * @param owner the owner of the release to get.
	 * @param repo the repository of the release to get.
	 * @param releaseId the id of the release to get.
	 * @return the retrieved release.
	 */
	@GET
	@Path("/repos/{owner}/{repo}/releases/{releaseId}")
	@Produces(MediaType.APPLICATION_JSON)
	ReleaseInfo getRelease(@PathParam("owner") String owner, @PathParam("repo") String repo,
			@PathParam("releaseId") String releaseId);

	/**
	 * Deletes a release.
	 *
	 * @param owner the owner of the release to delete.
	 * @param repo the repository of the release to delete.
	 * @param releaseId the id of the release to delete.
	 */
	@DELETE
	@Path("/repos/{owner}/{repo}/releases/{releaseId}")
	void deleteRelease(@PathParam("owner") String owner, @PathParam("repo") String repo,
			@PathParam("releaseId") String releaseId);

	/**
	 * Creates a release.
	 *
	 * @param owner the owner of the release to delete.
	 * @param repo the repository of the release to delete.
	 * @param request the create request object.
	 * @return the created release.
	 */
	@POST
	@Path("/repos/{owner}/{repo}/releases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	ReleaseInfo createRelease(@PathParam("owner") String owner, @PathParam("repo") String repo,
			CreateOrEditReleaseRequest request);

	/**
	 * See <a href= "https://developer.github.com/v3/">https://developer.github.com/v3/</a>
	 */
	@SuppressWarnings("javadoc")
	public static class ResponseStatus {
		@Nullable
		@JsonProperty("message")
		public String message;
		@Nullable
		@JsonProperty("documentation_url")
		public String documentationUrl;
	}

	/**
	 * See
	 * <a href= "https://developer.github.com/v3/repos/releases/">https://developer.github.com/v3/repos/releases/</a>
	 */
	@SuppressWarnings("javadoc")
	public static class ReleaseInfo {
		@Nullable
		@JsonProperty("url")
		public String url;
		@Nullable
		@JsonProperty("html_url")
		public String htmlUrl;
		@Nullable
		@JsonProperty("assets_url")
		public String assetsUrl;
		@Nullable
		@JsonProperty("upload_url")
		public String uploadUrl;
		@Nullable
		@JsonProperty("tarball_url")
		public String tarballUrl;
		@Nullable
		@JsonProperty("zipball_url")
		public String zipballUrl;
		@Nullable
		@JsonProperty("id")
		public String id;
		@Nullable
		@JsonProperty("node_id")
		public String nodeId;
		@Nullable
		@JsonProperty("tag_name")
		public String tagName;
		@Nullable
		@JsonProperty("target_commitish")
		public String targetCommitish;
		@Nullable
		@JsonProperty("name")
		public String name;
		@Nullable
		@JsonProperty("body")
		public String body;
		@JsonProperty("draft")
		public boolean draft;
		@JsonProperty("prerelease")
		public boolean prerelease;
		@Nullable
		@JsonProperty("created_at")
		public String createdAt;
		@Nullable
		@JsonProperty("published_at")
		public String publishedAt;
		@Nullable
		@JsonProperty("author")
		public ReleaseUserInfo author;
		@Nullable
		@JsonProperty("assets")
		public List<ReleaseAssetInfo> assets = null;
	}

	/**
	 * See
	 * <a href= "https://developer.github.com/v3/repos/releases/">https://developer.github.com/v3/repos/releases/</a>
	 */
	@SuppressWarnings("javadoc")
	public static class ReleaseUserInfo {
		@Nullable
		@JsonProperty("login")
		public String login;
		@Nullable
		@JsonProperty("id")
		public String id;
		@Nullable
		@JsonProperty("node_id")
		public String nodeId;
		@Nullable
		@JsonProperty("avatar_url")
		public String avatarUrl;
		@Nullable
		@JsonProperty("gravatar_id")
		public String gravatarId;
		@Nullable
		@JsonProperty("url")
		public String url;
		@Nullable
		@JsonProperty("html_url")
		public String htmlUrl;
		@Nullable
		@JsonProperty("followers_url")
		public String followerUrl;
		@Nullable
		@JsonProperty("following_url")
		public String followingUrl;
		@Nullable
		@JsonProperty("gists_url")
		public String gistsUrl;
		@Nullable
		@JsonProperty("starred_url")
		public String starredUrl;
		@Nullable
		@JsonProperty("subscriptions_url")
		public String subscriptionsUrl;
		@Nullable
		@JsonProperty("organizations_url")
		public String organizationsUrl;
		@Nullable
		@JsonProperty("repos_url")
		public String reposUrl;
		@Nullable
		@JsonProperty("events_url")
		public String eventsUrl;
		@Nullable
		@JsonProperty("received_events_url")
		public String receivedEventsUrl;
		@Nullable
		@JsonProperty("type")
		public String type;
		@JsonProperty("site_admin")
		public boolean siteAdmin;
	}

	/**
	 * See
	 * <a href= "https://developer.github.com/v3/repos/releases/">https://developer.github.com/v3/repos/releases/</a>
	 */
	@SuppressWarnings("javadoc")
	public static class ReleaseAssetInfo {
		@Nullable
		@JsonProperty("url")
		public String url;
		@Nullable
		@JsonProperty("browser_download_url")
		public String browserDownloadUrl;
		@Nullable
		@JsonProperty("id")
		public String id;
		@Nullable
		@JsonProperty("node_id")
		public String nodeId;
		@Nullable
		@JsonProperty("name")
		public String name;
		@Nullable
		@JsonProperty("label")
		public String label;
		@Nullable
		@JsonProperty("state")
		public String state;
		@Nullable
		@JsonProperty("content_type")
		public String contentType;
		@JsonProperty("size")
		public long size;
		@JsonProperty("download_count")
		public int downloadCount;
		@Nullable
		@JsonProperty("created_at")
		public String createdAt;
		@Nullable
		@JsonProperty("updated_at")
		public String updatedAt;
		@Nullable
		@JsonProperty("uploader")
		public ReleaseUserInfo uploader;
	}

	/**
	 * See
	 * <a href= "https://developer.github.com/v3/repos/releases/">https://developer.github.com/v3/repos/releases/</a>
	 */
	@SuppressWarnings("javadoc")
	public static class CreateOrEditReleaseRequest {
		@Nullable
		@JsonProperty("tag_name")
		public String tagName;
		@Nullable
		@JsonProperty("target_commitish")
		public String targetCommitish;
		@Nullable
		@JsonProperty("name")
		public String name;
		@Nullable
		@JsonProperty("body")
		public String body;
		@JsonProperty("draft")
		public boolean draft;
		@JsonProperty("prerelease")
		public boolean prerelease;
	}

}
