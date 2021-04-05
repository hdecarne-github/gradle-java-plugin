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
 * NpmInstallTask - Run npm install.
 */
public class NpmInstallTask extends NodeTask {

	private static final String NPM_INSTALL_TASK_GROUP = LifecycleBasePlugin.BUILD_GROUP;
	private static final String NPM_INSTALL_TASK_NAME = "npmInstall";
	private static final String NPM_INSTALL_TASK_DESCRIPTION = "Execute npm install.";

	/**
	 * Creates the {@linkplain NpmInstallTask}.
	 *
	 * @param project the {@linkplain Project} to create the task for.
	 * @return the created {@linkplain NpmInstallTask} object.
	 */
	public static NpmInstallTask create(Project project) {
		project.getLogger().info("Creating task {}", NPM_INSTALL_TASK_NAME);
		return project.getTasks().create(NPM_INSTALL_TASK_NAME, NpmInstallTask.class);
	}

	@Override
	public void apply(Project project) {
		setGroup(NPM_INSTALL_TASK_GROUP);
		setDescription(NPM_INSTALL_TASK_DESCRIPTION);
	}

	@Override
	public void afterEvaluate(Project project) {
		super.afterEvaluate(project);

		Node node = project.getExtensions().getByType(JavaToolsExtension.class).getNode();
		boolean enabled = node.isEnabled();

		if (enabled) {
			getInputs().file(getNodeProjectFile(PACKAGE_JSON_NAME));
			getInputs().file(getNodeProjectFile(PACKAGE_LOCK_JSON_NAME));
			getOutputs().file(getTaskOutFile());
			getOutputs().dir(getNodeProjectFile(NODE_MODULES_NAME));
			Plugins.setTasksDependsOn(project, NpmBuildTask.class, this);
		}
	}

	/**
	 * Executes {@linkplain NpmInstallTask}.
	 */
	@TaskAction
	public void executeNpmInstall() {
		Project project = getProject();

		ProjectLogger.enterProject(project);
		try {
			NpmWrapper npmWrapper = getNpmWrapperInstance();
			File logFile = getTaskOutFile();

			npmWrapper.executeNpm(logFile, "install");
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
