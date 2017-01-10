/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.gradleplugins.java;

import java.util.Set;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.UnknownPluginException;
import org.gradle.api.tasks.SourceSet;

/**
 * JavaToolsPlugin providing various Java related tasks.
 */
public class JavaToolsPlugin implements Plugin<Project> {

	/**
	 * Plugin name.
	 */
	public static final String JAVA_TOOL_PLUGIN_NAME = JavaToolsPlugin.class.getPackage().getName();

	private class AfterEvaluateAction implements Action<Project> {

		private GenI18NTask genI18NTask;

		AfterEvaluateAction(GenI18NTask genI18NTask) {
			this.genI18NTask = genI18NTask;
		}

		@Override
		public void execute(Project project) {
			JavaToolsExtension extension = JavaToolsExtension.get(project);

			extension.log(project.getLogger());
			this.genI18NTask.prepareAfterEvaluate();
		}

	}

	@Override
	public void apply(Project project) {
		checkPrerequisites(project);

		// Register extension object
		project.getExtensions().create(JavaToolsExtension.EXTENSION_NAME, JavaToolsExtension.class);

		// Create and pre-init tasks
		GenI18NTask genI18NTask = project.getTasks().create(GenI18NTask.GEN_I18N_TASK_NAME, GenI18NTask.class);

		genI18NTask.setDescription(GenI18NTask.GEN_I18N_TASK_DESCRIPTION);
		initTaskDependsOn(project, JavaPlugin.COMPILE_JAVA_TASK_NAME, genI18NTask);
		genI18NTask.prepare();

		// Queue post-init for tasks
		project.afterEvaluate(new AfterEvaluateAction(genI18NTask));
	}

	private void checkPrerequisites(Project project) {
		try {
			project.getPlugins().getPlugin("java");
		} catch (UnknownPluginException e) {
			String message = "Unable to apply plugin " + JAVA_TOOL_PLUGIN_NAME + "; please apply java plugin first";

			project.getLogger().error(message);
			throw new GradleException(message, e);
		}
	}

	private void initTaskDependsOn(Project project, String name, Object... dependencies) {
		Set<Task> taskSet = project.getTasksByName(name, false);

		for (Task task : taskSet) {
			task.dependsOn(dependencies);
		}
	}

	/**
	 * Lookup a source set.
	 *
	 * @param project The project to get the source set for.
	 * @param name The name of the source set to get.
	 * @return The found source set.
	 */
	public static SourceSet getSourceSet(Project project, String name) {
		return project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().getByName(name);
	}

}
