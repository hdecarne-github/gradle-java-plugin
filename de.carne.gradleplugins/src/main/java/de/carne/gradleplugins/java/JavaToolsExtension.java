/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.gradleplugins.java;

import org.gradle.api.tasks.SourceSet;

/**
 * Plugin extension object.
 */
public class JavaToolsExtension {

	/**
	 * Extension name.
	 */
	public static final String EXTENSION_NAME = "javatools";

	private String genI18NSourceSet = SourceSet.MAIN_SOURCE_SET_NAME;

	private String genI18NInclude = "**/*I18N.properties";

	private String genI18NKeyFilter = "^STR_.*";

	private String genDir = "src/main/java";

	/**
	 * @return the genI18NSourceSet
	 */
	public String getGenI18NSourceSet() {
		return this.genI18NSourceSet;
	}

	/**
	 * @param genI18NSourceSet the genI18NSourceSet to set
	 */
	public void setGenI18NSourceSet(String genI18NSourceSet) {
		this.genI18NSourceSet = genI18NSourceSet;
	}

	/**
	 * @return the genI18NInclude
	 */
	public String getGenI18NInclude() {
		return this.genI18NInclude;
	}

	/**
	 * @param genI18NInclude the genI18NInclude to set
	 */
	public void setGenI18NInclude(String genI18NInclude) {
		this.genI18NInclude = genI18NInclude;
	}

	/**
	 * @return the genI18NKeyFilter
	 */
	public String getGenI18NKeyFilter() {
		return this.genI18NKeyFilter;
	}

	/**
	 * @param genI18NKeyFilter the genI18NKeyFilter to set
	 */
	public void setGenI18NKeyFilter(String genI18NKeyFilter) {
		this.genI18NKeyFilter = genI18NKeyFilter;
	}

	/**
	 * @return the genDir
	 */
	public String getGenDir() {
		return this.genDir;
	}

	/**
	 * @param genDir the genDir to set
	 */
	public void setGenDir(String genDir) {
		this.genDir = genDir;
	}

}
