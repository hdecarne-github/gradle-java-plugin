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
package de.carne.gradle.plugin.java.ext;

import java.io.File;

import org.eclipse.jdt.annotation.Nullable;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;

/**
 * Configuration object for {@linkplain de.carne.gradle.plugin.java.task.DraftGitHubReleaseTask}.
 * <p>
 * build.gradle:
 *
 * <pre>
 * javatools {
 *  githubRelease {
 *   ...
 *  }
 * }
 * </pre>
 */
public class GitHubRelease {

	private final Project project;

	private boolean enabledParam = false;
	@Nullable
	private String releaseNameParam;
	@Nullable
	private File releaseNotesParam;
	@Nullable
	private ConfigurableFileTree releaseAssetsParam;
	private boolean overwriteParam = false;
	@Nullable
	private String githubTokenParam;
	private boolean ignoreDirtyParam = false;

	/**
	 * Constructs a new {@linkplain GitHubRelease} instance.
	 *
	 * @param project the owning {@linkplain Project}.
	 */
	public GitHubRelease(Project project) {
		this.project = project;

		Object versionProperty = this.project.getProperties().get("version");

		if (versionProperty != null) {
			this.releaseNameParam = "v" + versionProperty;
			this.releaseNotesParam = this.project.file("RELEASE-" + this.releaseNameParam + ".md");
		}
	}

	/**
	 * Checks whether the GitHub release tasks are enabled.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  enabled = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @return {@code true} if the GitHub release tasks are enabled.
	 */
	public boolean isEnabled() {
		return this.enabledParam;
	}

	/**
	 * Enables/disables the GitHub release tasks.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  enabled = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @param enabled whether to enable or disable the GitHub release tasks.
	 */
	public void setEnabled(boolean enabled) {
		this.enabledParam = enabled;
	}

	/**
	 * Gets the GitHub release name.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  releaseName = "..." // default: "v${project.version}"
	 * }
	 * </pre>
	 *
	 * @return the GitHub release name.
	 */
	public String getReleaseName() {
		String checkedReleaseName = this.releaseNameParam;

		if (checkedReleaseName == null) {
			throw new GradleException("Property releaseName not set");
		}
		return checkedReleaseName;
	}

	/**
	 * Sets the GitHub release name.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  releaseName = "..." // default: "v${project.version}"
	 * }
	 * </pre>
	 *
	 * @param releaseName the release name to set.
	 */
	public void setReleaseName(String releaseName) {
		this.releaseNameParam = releaseName;
	}

	/**
	 * Gets the release notes file.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  releaseNotes = file(...) // default: file("RELEASE-v${project.version}.md");
	 * }
	 * </pre>
	 *
	 * @return the release notes file.
	 */
	public File getReleaseNotes() {
		File checkedReleaseNotes = this.releaseNotesParam;

		if (checkedReleaseNotes == null) {
			throw new GradleException("Property releaseNotes not set");
		}
		return checkedReleaseNotes;
	}

	/**
	 * Sets the release notes file.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  releaseNotes = file(...) // default: file("RELEASE-v${project.version}.md");
	 * }
	 * </pre>
	 *
	 * @param releaseNotes the release notes file to set.
	 */
	public void setReleaseNotes(File releaseNotes) {
		this.releaseNotesParam = releaseNotes;
	}

	/**
	 * Gets the release assets to upload.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  releaseAssets = fileTree(...) // default: &lt;undefined&gt;
	 * }
	 * </pre>
	 *
	 * @return the release assets to upload.
	 */
	public ConfigurableFileTree getReleaseAssets() {
		ConfigurableFileTree checkedReleaseAssets = this.releaseAssetsParam;

		if (checkedReleaseAssets == null) {
			throw new GradleException("Property releaseAssets not set");
		}
		return checkedReleaseAssets;
	}

	/**
	 * Sets the release assets to upload.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  releaseAssets = fileTree(...) // default: &lt;undefined&gt;
	 * }
	 * </pre>
	 *
	 * @param releaseAssets the release assets to set.
	 */
	public void setReleaseAssets(ConfigurableFileTree releaseAssets) {
		this.releaseAssetsParam = releaseAssets;
	}

	/**
	 * Checks whether release overwriting is enabled.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  overwrite = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @return {@code true} if release overwriting is enabled.
	 */
	public boolean isOverwrite() {
		return this.overwriteParam;
	}

	/**
	 * Enables/disables release overwriting.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  overwrite = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @param overwrite whether to overwrite an already existing release.
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwriteParam = overwrite;
	}

	/**
	 * Gets the GitHub access token.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  githubToken = "..." // default: &lt;undefined&gt;
	 * }
	 * </pre>
	 *
	 * @return the GitHub access token.
	 */
	public String getGithubToken() {
		String checkedGithubtoken = this.githubTokenParam;

		if (checkedGithubtoken == null) {
			throw new GradleException("Property githubToken not set");
		}
		return checkedGithubtoken;
	}

	/**
	 * Sets the GitHub access token.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  githubToken = "..." // default: &lt;undefined&gt;
	 * }
	 * </pre>
	 *
	 * @param githubToken the GitHub access token to use.
	 */
	public void setGithubToken(String githubToken) {
		this.githubTokenParam = githubToken;
	}

	/**
	 * Checks whether a dirty state of the local repository is ignored.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  ignoreDirty = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @return {@code true} if a dirty state of the local repository is ignored.
	 */
	public boolean isIgnoreDirty() {
		return this.ignoreDirtyParam;
	}

	/**
	 * Enables/disables ignoring of the dirty state of the local repository.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * githubRelease {
	 *  overwrite = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @param ignoreDirty whether to ignore a dirty state of the local repository.
	 */
	public void setIgnoreDirty(boolean ignoreDirty) {
		this.ignoreDirtyParam = ignoreDirty;
	}

}
