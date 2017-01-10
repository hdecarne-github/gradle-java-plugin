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

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
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

	private String genSourceSet = "generated";

	private String genDir = "generated/main/java";

	/**
	 * @return the genI18NSourceSet property name.
	 */
	public static String getGenI18NSourceSetProperty() {
		return "genI18NSourceSet";
	}

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
	 * @return the genI18NInclude property name.
	 */
	public static String getGenI18NIncludeProperty() {
		return "genI18NInclude";
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
	 * @return the genI18NKeyFilter property name.
	 */
	public static String getGenI18NKeyFilterProperty() {
		return "genI18NKeyFilter";
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
	 * @return the genSourceSet property name.
	 */
	public static String getGenSourceSetProperty() {
		return "genSourceSet";
	}

	/**
	 * @return the genSourceSet
	 */
	public String getGenSourceSet() {
		return this.genSourceSet;
	}

	/**
	 * @param genSourceSet the genSourceSet to set
	 */
	public void setGenSourceSet(String genSourceSet) {
		this.genSourceSet = genSourceSet;
	}

	/**
	 * @return the genDir property name.
	 */
	public static String getGenDirProperty() {
		return "genDir";
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

	/**
	 * Get the project's extension object.
	 *
	 * @param project The project to get the extension object for.
	 * @return The extension object.
	 */
	public static JavaToolsExtension get(Project project) {
		return project.getExtensions().getByType(JavaToolsExtension.class);
	}

	/**
	 * Log extension parameters for debug purposes.
	 *
	 * @param logger The logger to use.
	 */
	public void log(Logger logger) {
		if (logger.isDebugEnabled()) {
			logger.debug("{} extension values:", EXTENSION_NAME);
			logger.debug(" genI18NSourceSet = '{}'", this.genI18NSourceSet);
			logger.debug(" genI18NInclude = '{}'", this.genI18NInclude);
			logger.debug(" genI18NKeyFilter = '{}'", this.genI18NKeyFilter);
			logger.debug(" genSourceSet = ''{}''", this.genSourceSet);
			logger.debug(" genDir = ''{}''", this.genDir);
		}
	}

}
