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

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

class ArtifactVersionId implements Comparable<ArtifactVersionId> {

	private final ArtifactId artifactId;
	private final String version;

	public ArtifactVersionId(String group, String name, String version) {
		this.artifactId = new ArtifactId(group, name);
		this.version = version;
	}

	public ArtifactId getArtifactId() {
		return this.artifactId;
	}

	public boolean isSnapshot() {
		return this.version.endsWith("-SNAPSHOT");
	}

	public boolean isQualified() {
		return this.version.contains("-");
	}

	public boolean isCandidate(ArtifactVersionId artifactVersionId) {
		return this.artifactId.equals(artifactVersionId.artifactId) && (!artifactVersionId.isSnapshot() || isSnapshot())
				&& (!artifactVersionId.isQualified() || isQualified());
	}

	@Override
	public int compareTo(ArtifactVersionId o) {
		int comparison = 0;

		if ((comparison = this.artifactId.compareTo(o.artifactId)) == 0) {
			comparison = this.version.compareTo(o.version);
		}
		return comparison;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.artifactId, this.version);
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return this == obj || (obj instanceof ArtifactVersionId && compareTo((ArtifactVersionId) obj) == 0);
	}

	@Override
	public String toString() {
		return this.artifactId + ":" + this.version;
	}

}
