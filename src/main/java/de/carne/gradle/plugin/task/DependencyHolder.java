/*
 * Copyright (c) 2018-2019 Holger de Carne and contributors, All Rights Reserved.
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

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ExternalDependency;

@SuppressWarnings("squid:S2160")
class DependencyHolder extends DependencyKey {

	private final Project project;
	private final Configuration configuration;
	private final ExternalDependency dependency;

	DependencyHolder(Project project, Configuration configuration, ExternalDependency dependency) {
		super(project.getName(), configuration.getName(), dependency.getGroup(), dependency.getName(),
				dependency.getVersion());
		this.project = project;
		this.configuration = configuration;
		this.dependency = dependency;
	}

}
