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

import org.eclipse.jdt.annotation.Nullable;
import org.gradle.api.Project;

class CheckDependencyVersionsReport {

	private final Project project;
	private final String title;
	private @Nullable String lastProjectName = null;
	private @Nullable String lastConfigurationName = null;

	public CheckDependencyVersionsReport(Project project, String title) {
		this.project = project;
		this.title = title;
	}

	public void reportDependencyMismatch(String projectName, String configurationName, ArtifactVersionId actual,
			ArtifactVersionId found) {
		writeContext(projectName, configurationName);
		write("      Dependency mismatch: %1$s -> %2$s", actual, found);
	}

	private void writeContext(String projectName, String configurationName) {
		if (!projectName.equals(this.lastProjectName)) {
			if (this.lastProjectName == null) {
				write(this.title);
			}
			this.lastProjectName = projectName;
			this.lastConfigurationName = null;
			write("  Project %1$s", this.lastProjectName);
		}
		if (!configurationName.equals(this.lastConfigurationName)) {
			this.lastConfigurationName = configurationName;
			write("    Configuration %1$s", this.lastConfigurationName);
		}
	}

	private String write(String format, Object... args) {
		String message = String.format(format, args);

		this.project.getLogger().warn(message);
		return message;
	}

}
