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
package de.carne.gradle.plugin;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.UnknownPluginException;
import org.gradle.api.tasks.compile.JavaCompile;

import de.carne.gradle.plugin.ext.JavaToolsExtension;
import de.carne.gradle.plugin.task.CheckDependencyVersionsTask;
import de.carne.gradle.plugin.task.DraftGitHubReleaseTask;
import de.carne.gradle.plugin.task.GenerateI18NTask;
import de.carne.gradle.plugin.util.Late;

/**
 * JavaToolsPlugin providing java related build functions and tasks.
 */
public class JavaToolsPlugin implements Plugin<Project> {

	/**
	 * The plugin name.
	 */
	public static final String JAVA_TOOLS_PLUGIN_NAME = JavaToolsPlugin.class.getPackage().getName() + "java-tools";

	private final Late<GenerateI18NTask> generateI18NTaskHolder = new Late<>();
	private final Late<CheckDependencyVersionsTask> checkDependencyVersionsTaskHolder = new Late<>();
	private final Late<DraftGitHubReleaseTask> draftGitHubReleaseTaskHolder = new Late<>();

	@Override
	public void apply(@Nullable Project project) {
		if (project != null) {
			// Create extension object
			JavaToolsExtension.create(project);
			// Create task objects
			this.generateI18NTaskHolder.set(GenerateI18NTask.create(project)).apply(project);
			this.checkDependencyVersionsTaskHolder.set(CheckDependencyVersionsTask.create(project)).apply(project);
			this.draftGitHubReleaseTaskHolder.set(DraftGitHubReleaseTask.create(project)).apply(project);
			// Finish setup after evaluate
			project.afterEvaluate(this::afterEvaluate);
		}
	}

	private void afterEvaluate(@Nullable Project project) {
		Objects.requireNonNull(project);

		JavaToolsExtension javaTools = project.getExtensions().getByType(JavaToolsExtension.class);

		// Check prerequisites
		if (javaTools.getGenerateI18N().isEnabled()) {
			checkJavaApplied(project);
		}

		// Set standard dependencies
		if (javaTools.getGenerateI18N().isEnabled()) {
			setTasksDependsOn(project, JavaCompile.class, this.generateI18NTaskHolder.get());
		}

		// Finalize tasks
		this.generateI18NTaskHolder.get().afterEvaluate(project);
		this.checkDependencyVersionsTaskHolder.get().afterEvaluate(project);
		this.draftGitHubReleaseTaskHolder.get().afterEvaluate(project);
	}

	private void checkJavaApplied(Project project) {
		try {
			project.getPlugins().getPlugin("java");
		} catch (UnknownPluginException e) {
			String message = "Unable to apply plugin " + JAVA_TOOLS_PLUGIN_NAME
					+ "; please apply java or java-library plugin first";

			project.getLogger().error(message);
			throw new GradleException(message, e);
		}
	}

	private void setTasksDependsOn(Project project, Class<? extends Task> taskType, Task dependency) {
		project.getTasks().stream().filter(task -> taskType.isAssignableFrom(task.getClass()))
				.forEach(task -> task.dependsOn(dependency));
	}

}
