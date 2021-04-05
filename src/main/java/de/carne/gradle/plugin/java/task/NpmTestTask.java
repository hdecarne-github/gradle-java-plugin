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
package de.carne.gradle.plugin.java.task;

import java.io.File;
import java.io.IOException;

import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

import de.carne.gradle.plugin.java.ext.JavaToolsExtension;
import de.carne.gradle.plugin.java.ext.Node;
import de.carne.gradle.plugin.java.util.NpmWrapper;
import de.carne.gradle.plugin.java.util.Plugins;
import de.carne.gradle.plugin.java.util.ProjectLogger;

/**
 * NpmTestTask - Run npm test script.
 */
public class NpmTestTask extends NodeTask {

	private static final String NPM_TEST_TASK_GROUP = LifecycleBasePlugin.VERIFICATION_GROUP;
	private static final String NPM_TEST_TASK_NAME = "testNode";
	private static final String NPM_TEST_TASK_DESCRIPTION = "Execute npm test script.";

	/**
	 * Creates the {@linkplain NpmTestTask}.
	 *
	 * @param project the {@linkplain Project} to create the task for.
	 * @return the created {@linkplain NpmTestTask} object.
	 */
	public static NpmTestTask create(Project project) {
		project.getLogger().info("Creating task {}", NPM_TEST_TASK_NAME);
		return project.getTasks().create(NPM_TEST_TASK_NAME, NpmTestTask.class);
	}

	@Override
	public void apply(Project project) {
		setGroup(NPM_TEST_TASK_GROUP);
		setDescription(NPM_TEST_TASK_DESCRIPTION);
	}

	@Override
	public void afterEvaluate(Project project) {
		super.afterEvaluate(project);

		Node node = project.getExtensions().getByType(JavaToolsExtension.class).getNode();
		boolean enabled = node.isEnabled();

		if (enabled) {
			getInputs().dir(node.getNodeProjectDir());
			getOutputs().file(taskOutFile());
			Plugins.setTaskDependsOn(project, LifecycleBasePlugin.CHECK_TASK_NAME, this);
		}
	}

	/**
	 * Executes {@linkplain NpmTestTask}.
	 */
	@TaskAction
	public void executeNpmTest() {
		Project project = getProject();

		ProjectLogger.enterProject(project);
		try {
			Node node = project.getExtensions().getByType(JavaToolsExtension.class).getNode();
			NpmWrapper npmWrapper = npmWrapperInstance();
			File logFile = taskOutFile();

			npmWrapper.executeNpm(logFile, "run", node.getTestScript());
		} catch (IOException e) {
			throw new TaskExecutionException(this, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new TaskExecutionException(this, e);
		} finally {
			ProjectLogger.leaveProject();
		}
	}

}
