/*
 * Copyright (c) 2018-2022 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.gradle.plugin.java;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import de.carne.gradle.plugin.java.ext.JavaToolsExtension;
import de.carne.gradle.plugin.java.task.NpmBuildTask;
import de.carne.gradle.plugin.java.task.CheckDependencyVersionsTask;
import de.carne.gradle.plugin.java.task.DraftGitHubReleaseTask;
import de.carne.gradle.plugin.java.task.GenerateI18NTask;
import de.carne.gradle.plugin.java.task.NpmInstallTask;
import de.carne.gradle.plugin.java.task.NpmTestTask;
import de.carne.gradle.plugin.java.util.Late;

/**
 * JavaToolsPlugin providing java related build functions and tasks.
 */
public class JavaToolsPlugin implements Plugin<Project> {

	/**
	 * The plugin name.
	 */
	public static final String JAVA_TOOLS_PLUGIN_NAME = "de.carne.java-tools";

	private final Late<GenerateI18NTask> generateI18NTaskHolder = new Late<>();
	private final Late<CheckDependencyVersionsTask> checkDependencyVersionsTaskHolder = new Late<>();
	private final Late<DraftGitHubReleaseTask> draftGitHubReleaseTaskHolder = new Late<>();
	private final Late<NpmInstallTask> npmInstallTaskHolder = new Late<>();
	private final Late<NpmBuildTask> npmBuildTaskHolder = new Late<>();
	private final Late<NpmTestTask> npmTestTaskHolder = new Late<>();

	@Override
	public void apply(@Nullable Project project) {
		Objects.requireNonNull(project);

		// Create extension object
		JavaToolsExtension.create(project);
		// Create task objects
		this.generateI18NTaskHolder.set(GenerateI18NTask.create(project)).apply(project);
		this.checkDependencyVersionsTaskHolder.set(CheckDependencyVersionsTask.create(project)).apply(project);
		this.draftGitHubReleaseTaskHolder.set(DraftGitHubReleaseTask.create(project)).apply(project);
		this.npmInstallTaskHolder.set(NpmInstallTask.create(project)).apply(project);
		this.npmBuildTaskHolder.set(NpmBuildTask.create(project)).apply(project);
		this.npmTestTaskHolder.set(NpmTestTask.create(project)).apply(project);
		// Finish setup after evaluate
		project.afterEvaluate(this::afterEvaluate);
	}

	private void afterEvaluate(@Nullable Project project) {
		Objects.requireNonNull(project);

		// Finalize tasks setup
		this.generateI18NTaskHolder.get().afterEvaluate(project);
		this.checkDependencyVersionsTaskHolder.get().afterEvaluate(project);
		this.draftGitHubReleaseTaskHolder.get().afterEvaluate(project);
		this.npmInstallTaskHolder.get().afterEvaluate(project);
		this.npmBuildTaskHolder.get().afterEvaluate(project);
		this.npmTestTaskHolder.get().afterEvaluate(project);
	}

}
