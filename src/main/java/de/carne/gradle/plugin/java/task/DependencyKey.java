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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import de.carne.gradle.plugin.java.util.Strings;

class DependencyKey implements Comparable<DependencyKey> {

	private final String projectName;
	private final String configurationName;
	private final String dependencyGroup;
	private final String dependencyName;
	private final String dependencyVersion;
	private final List<Object> sortKey = new ArrayList<>();

	DependencyKey(String projectName, String configurationName, String dependencyGroup, String dependencyName,
			@Nullable String dependencyVersion) {
		this.projectName = projectName;
		this.configurationName = configurationName;
		this.dependencyGroup = dependencyGroup;
		this.dependencyName = dependencyName;
		this.dependencyVersion = Strings.safe(dependencyVersion);
		initSortKey();
	}

	private static final Pattern DEPENDENCY_VERSION_TOKEN_PATTERN = Pattern.compile("(\\d*)(.*)");

	private void initSortKey() {
		this.sortKey.add(this.projectName);
		this.sortKey.add(this.configurationName);
		this.sortKey.add(this.dependencyGroup);
		this.sortKey.add(this.dependencyName);

		StringTokenizer tokens = new StringTokenizer(this.dependencyVersion, ".", false);

		while (tokens.hasMoreElements()) {
			String token = tokens.nextToken();
			Matcher tokenMatcher = DEPENDENCY_VERSION_TOKEN_PATTERN.matcher(token);

			if (tokenMatcher.lookingAt()) {
				String tokenDigits = Objects.requireNonNull(tokenMatcher.group(1));
				String tokenSuffix = Objects.requireNonNull(tokenMatcher.group(2));

				try {
					Integer tokenValue = Integer.parseInt(tokenDigits);

					this.sortKey.add(tokenValue);
					this.sortKey.add(tokenSuffix);
				} catch (@SuppressWarnings("unused") NumberFormatException e) {
					this.sortKey.add(token);
					this.sortKey.add("");
				}
			} else {
				this.sortKey.add(token);
				this.sortKey.add("");
			}
		}
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
	public int compareTo(@NonNull DependencyKey o) {
		Iterator<Object> thisTokens = this.sortKey.iterator();
		Iterator<Object> oTokens = o.sortKey.iterator();
		int comparison = 0;

		while (comparison == 0 && (thisTokens.hasNext() || oTokens.hasNext())) {
			Object thisToken = (thisTokens.hasNext() ? thisTokens.next() : "");
			Object oToken = (oTokens.hasNext() ? oTokens.next() : "");

			if (thisToken instanceof Integer && oToken instanceof Integer) {
				comparison = ((Integer) thisToken).compareTo((Integer) oToken);
			} else {
				comparison = thisToken.toString().compareTo(oToken.toString());
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
