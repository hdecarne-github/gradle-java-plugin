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

import org.eclipse.jdt.annotation.Nullable;

class ArtifactId implements Comparable<ArtifactId> {

	private final String group;
	private final String name;

	public ArtifactId(String group, String name) {
		this.group = group;
		this.name = name;
	}

	@Override
	public int compareTo(ArtifactId o) {
		int comparison = 0;

		if ((comparison = this.group.compareTo(o.group)) == 0) {
			comparison = this.name.compareTo(o.name);
		}
		return comparison;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.group, this.name);
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return this == obj || (obj instanceof ArtifactId && compareTo((ArtifactId) obj) == 0);
	}

	@Override
	public String toString() {
		return this.group + ":" + this.name;
	}

}
