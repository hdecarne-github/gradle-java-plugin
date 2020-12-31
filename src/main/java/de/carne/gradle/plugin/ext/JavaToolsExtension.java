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
package de.carne.gradle.plugin.ext;

import org.gradle.api.Action;
import org.gradle.api.Project;

/**
 * Root extension object for the {@linkplain de.carne.gradle.plugin.JavaToolsPlugin}.
 */
public class JavaToolsExtension {

	private static final String JAVA_TOOLS_EXTENSION_NAME = "javatools";

	/**
	 * Creates a new {@linkplain JavaToolsExtension} object.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * javatools {
	 *  ...
	 * }
	 * </pre>
	 *
	 * @param project the {@linkplain Project} to create the extension object for.
	 * @return the created {@linkplain JavaToolsExtension} object.
	 */
	public static JavaToolsExtension create(Project project) {
		return project.getExtensions().create(JAVA_TOOLS_EXTENSION_NAME, JavaToolsExtension.class, project);
	}

	private final Project project;

	private final PlatformInfo platformInfo = new PlatformInfo();
	private final GenerateI18N generateI18NConfig;
	private final GitHubRelease githubRelease;

	/**
	 * Constructs {@linkplain JavaToolsExtension}.
	 *
	 * @param project the owning {@linkplain Project}.
	 */
	public JavaToolsExtension(Project project) {
		this.project = project;
		this.generateI18NConfig = new GenerateI18N(this.project);
		this.githubRelease = new GitHubRelease(this.project);
	}

	/**
	 * Gets the Platform Info object.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * test {
	 *  enabled = javatools.platform.swtToolkit.equals("cocoa-macos-x86_64")
	 *  ...
	 * }
	 * </pre>
	 *
	 * @return the Platform Info object.
	 */
	public PlatformInfo getPlatform() {
		return this.platformInfo;
	}

	/**
	 * Gets the generateI18N configuration object.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * javatools {
	 *  generateI18N {
	 *   ...
	 *  }
	 * }
	 * </pre>
	 *
	 * @return the generateI18N configuration object.
	 */
	public GenerateI18N getGenerateI18N() {
		return this.generateI18NConfig;
	}

	/**
	 * Gets the githubRelease configuration object.
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
	 *
	 * @return the githubRelease configuration object.
	 */
	public GitHubRelease getGithubRelease() {
		return this.githubRelease;
	}

	/**
	 * Executes a {@linkplain #generateI18N} configuration action.
	 *
	 * @param configuration the configuration action to execute.
	 */
	public void generateI18N(Action<? super GenerateI18N> configuration) {
		this.generateI18NConfig.setEnabled(false);
		configuration.execute(this.generateI18NConfig);
	}

	/**
	 * Executes a {@linkplain #githubRelease} configuration action.
	 *
	 * @param configuration the configuration action to execute.
	 */
	public void githubRelease(Action<? super GitHubRelease> configuration) {
		this.githubRelease.setEnabled(false);
		configuration.execute(this.githubRelease);
	}

}
