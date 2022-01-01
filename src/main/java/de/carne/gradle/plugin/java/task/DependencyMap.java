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
package de.carne.gradle.plugin.java.task;

import java.util.Objects;
import java.util.TreeMap;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExternalDependency;
import org.gradle.api.initialization.dsl.ScriptHandler;

/**
 *
 */
class DependencyMap extends TreeMap<DependencyKey, DependencyHolder> {

	// Serialization support
	private static final long serialVersionUID = -3996197953181906688L;

	private DependencyMap(Project project, ScriptHandler buildscript) {
		addConfigurations(project, buildscript.getConfigurations());
	}

	private DependencyMap(Project project) {
		addConfigurations(project, project.getConfigurations());
	}

	public static DependencyMap fromProject(Project project) {
		return new DependencyMap(project);
	}

	public static DependencyMap fromBuildscript(Project project) {
		return new DependencyMap(project, project.getBuildscript());
	}

	private void addConfigurations(Project project, ConfigurationContainer configurations) {
		configurations.forEach(configuration -> addConfiguration(project, Objects.requireNonNull(configuration)));
	}

	private void addConfiguration(Project project, Configuration configuration) {
		configuration.getDependencies()
				.forEach(dependency -> addDependency(project, configuration, Objects.requireNonNull(dependency)));
	}

	private void addDependency(Project project, Configuration configuration, Dependency dependency) {
		if (dependency instanceof ExternalDependency) {
			DependencyHolder dependencyHolder = new DependencyHolder(project, configuration,
					(ExternalDependency) dependency);

			put(dependencyHolder, dependencyHolder);
		}
	}

}
