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

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

class DependencyKey implements Comparable<DependencyKey> {

	private final String projectName;
	private final String configurationName;
	private final String dependencyGroup;
	private final String dependencyName;
	private final String dependencyVersion;

	DependencyKey(String projectName, String configurationName, String dependencyGroup, String dependencyName,
			String dependencyVersion) {
		this.projectName = projectName;
		this.configurationName = configurationName;
		this.dependencyGroup = dependencyGroup;
		this.dependencyName = dependencyName;
		this.dependencyVersion = dependencyVersion;
	}

	public String getProject() {
		return this.projectName;
	}

	public String getConfiguration() {
		return this.configurationName;
	}

	public ArtifactId getArtifactId() {
		return new ArtifactId(this.dependencyGroup, this.dependencyName);
	}

	public ArtifactVersionId getArtifactVersionId() {
		return new ArtifactVersionId(this.dependencyGroup, this.dependencyName, this.dependencyVersion);
	}

	@Override
	@SuppressWarnings("squid:S1066")
	public int compareTo(@NonNull DependencyKey o) {
		int comparison = 0;

		if ((comparison = this.projectName.compareTo(o.projectName)) == 0) {
			if ((comparison = this.configurationName.compareTo(o.configurationName)) == 0) {
				if ((comparison = this.dependencyGroup.compareTo(o.dependencyGroup)) == 0) {
					if ((comparison = this.dependencyName.compareTo(o.dependencyName)) == 0) {
						comparison = this.dependencyVersion.compareTo(o.dependencyVersion);
					}
				}
			}
		}
		return comparison;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.projectName, this.configurationName, this.dependencyGroup, this.dependencyName,
				this.dependencyVersion);
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return this == obj || (obj instanceof DependencyKey && compareTo((DependencyKey) obj) == 0);
	}

	@Override
	public String toString() {
		return this.projectName + ":" + this.configurationName + ":" + this.dependencyGroup + ":" + this.dependencyName
				+ ":" + this.dependencyVersion;
	}

}
