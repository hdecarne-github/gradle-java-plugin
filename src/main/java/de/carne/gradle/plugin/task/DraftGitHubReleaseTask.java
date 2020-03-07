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
package de.carne.gradle.plugin.task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;

import de.carne.gradle.plugin.ext.GitHubRelease;
import de.carne.gradle.plugin.ext.JavaToolsExtension;
import de.carne.gradle.plugin.util.GitHubApi;
import de.carne.gradle.plugin.util.GitHubRepo;
import de.carne.io.IOUtil;

/**
 * DraftGitHubReleaseTask - Draft new GitHub release.
 */
public class DraftGitHubReleaseTask extends DefaultTask implements JavaToolsTask {

	private static final String DRAFT_GITHUB_RELEASE_TASK_GROUP = "distribute";
	private static final String DRAFT_GITHUB_RELEASE_TASK_NAME = "draftGitHubRelease";
	private static final String DRAFT_GITHUB_RELEASE_TASK_DESCRIPTION = "Draft new GitHub release.";

	/**
	 * Creates the {@linkplain DraftGitHubReleaseTask}.
	 *
	 * @param project the {@linkplain Project} to create the task for.
	 * @return the created {@linkplain DraftGitHubReleaseTask} object.
	 */
	public static DraftGitHubReleaseTask create(Project project) {
		project.getLogger().info("Creating task " + DRAFT_GITHUB_RELEASE_TASK_NAME);
		return project.getTasks().create(DRAFT_GITHUB_RELEASE_TASK_NAME, DraftGitHubReleaseTask.class);
	}

	@Override
	public void apply(Project project) {
		setGroup(DRAFT_GITHUB_RELEASE_TASK_GROUP);
		setDescription(DRAFT_GITHUB_RELEASE_TASK_DESCRIPTION);
	}

	@Override
	public void afterEvaluate(Project project) {
		GitHubRelease githubRelease = project.getExtensions().getByType(JavaToolsExtension.class).getGithubRelease();

		setEnabled(githubRelease.isEnabled());
	}

	/**
	 * Executes {@linkplain DraftGitHubReleaseTask}.
	 */
	@TaskAction
	public void executeDraftGitHubRelease() {
		Project project = getProject();
		File repoDir = getRepoDir(project.getProjectDir());
		GitHubRelease githubRelease = project.getExtensions().getByType(JavaToolsExtension.class).getGithubRelease();
		String releaseName = githubRelease.getReleaseName();

		getLogger().lifecycle("Drafting release {} for repo '{}'...", releaseName, repoDir);

		try (GitHubRepo repo = new GitHubRepo(repoDir, githubRelease.getGithubToken())) {
			checkDirty(repo, githubRelease);
			checkOverwrite(repo, githubRelease);

			String releaseNotes = readReleaseNotes(githubRelease);

			GitHubApi.ReleaseInfo draft = repo.draftRelease(githubRelease.getReleaseName(), releaseNotes);
			ConfigurableFileTree releaseAssets = githubRelease.getReleaseAssets();

			for (File releaseAsset : releaseAssets.getFiles()) {
				getLogger().lifecycle("Uploading release asset '{}'...", releaseAsset);

				repo.uploadReleaseAsset(Objects.requireNonNull(draft.uploadUrl), releaseAsset);
			}
		} catch (IOException e) {
			throw new TaskExecutionException(this, e);
		}
	}

	private File getRepoDir(File projectDir) {
		File repoDir = projectDir;

		while (repoDir != null && !(new File(repoDir, ".git").isDirectory())) {
			repoDir = repoDir.getParentFile();
		}
		return (repoDir != null ? repoDir : projectDir);
	}

	private void checkDirty(GitHubRepo repo, GitHubRelease githubRelease) throws IOException {
		if (!githubRelease.isIgnoreDirty() && repo.isDirty()) {
			throw new IOException("Repo at '" + repo.dir()
					+ "' has uncomitted or untracked changes; either clean repo or set ignoreDiry");
		}
	}

	private void checkOverwrite(GitHubRepo repo, GitHubRelease githubRelease) throws IOException {
		String releaseName = githubRelease.getReleaseName();
		GitHubApi.ReleaseInfo releaseInfo = repo.queryRelease(releaseName);

		if (releaseInfo != null) {
			if (!githubRelease.isOverwrite()) {
				throw new IOException("Release '" + releaseName + "' already exists");
			} else if (!releaseInfo.draft) {
				throw new IOException("Release '" + releaseName + "' already published");
			}
			repo.deleteRelease(Objects.requireNonNull(releaseInfo.id));
		}
	}

	private String readReleaseNotes(GitHubRelease githubRelease) throws IOException {
		return new String(IOUtil.readAllBytes(githubRelease.getReleaseNotes()), StandardCharsets.UTF_8);
	}

}
