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
package de.carne.gradle.plugin.java.util;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.UnknownPluginException;

import de.carne.gradle.plugin.java.JavaToolsPlugin;

/**
 * Utility class providing {@linkplain Plugin} related functions.
 */
public final class Plugins {

	private Plugins() {
		// Prevent instantiation
	}

	/**
	 * Checks whether the {@code java} plugin is applied to the submitted project.
	 *
	 * @param project the {@linkplain Project} to check.
	 * @throws GradleException if {@code java} plugin is not applied.
	 */
	public static void checkJavaApplied(Project project) {
		try {
			project.getPlugins().getPlugin("java");
		} catch (UnknownPluginException e) {
			String message = "Unable to apply plugin " + JavaToolsPlugin.JAVA_TOOLS_PLUGIN_NAME
					+ "; please apply java or java-library plugin first";

			project.getLogger().error(message);
			throw new GradleException(message, e);
		}
	}

	/**
	 * Adds a task dependency to a specific type of tasks.
	 *
	 * @param project the {@linkplain Project} to add the dependency for.
	 * @param taskType the {@linkplain Task} type to add the dependency to.
	 * @param dependency the {@linkplain Task} to add as a dependency.
	 */
	public static void setTasksDependsOn(Project project, Class<? extends Task> taskType, Task dependency) {
		project.getTasks().stream().filter(task -> taskType.isAssignableFrom(task.getClass()))
				.forEach(task -> task.dependsOn(dependency));
	}

	/**
	 * Adds a task dependency to a named task.
	 *
	 * @param project the {@linkplain Project} to add the dependency for.
	 * @param taskName the name of the {@linkplain Task} to add the dependency to.
	 * @param dependency the {@linkplain Task} to add as a dependency.
	 */
	public static void setTaskDependsOn(Project project, String taskName, Task dependency) {
		project.getTasks().stream().filter(task -> taskName.equals(task.getName()))
				.forEach(task -> task.dependsOn(dependency));
	}

}
