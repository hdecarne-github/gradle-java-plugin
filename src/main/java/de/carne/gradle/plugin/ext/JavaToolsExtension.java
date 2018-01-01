/*
 * Copyright (c) 2016-2018 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.gradle.plugin.ext;

import org.gradle.api.Action;
import org.gradle.api.Project;

/**
 * Extension object for {@linkplain de.carne.gradle.plugin.JavaToolsPlugin} configuration.
 */
public class JavaToolsExtension {

	private static final String JAVA_TOOLS_EXTENSION_NAME = "javatools";

	/**
	 * Create the {@linkplain JavaToolsExtension}.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * javatools {
	 *  ...
	 * }
	 * </pre>
	 *
	 * @param project The {@linkplain Project} to create the extions for.
	 * @return The created {@linkplain JavaToolsExtension} object.
	 */
	public static JavaToolsExtension create(Project project) {
		return project.getExtensions().create(JAVA_TOOLS_EXTENSION_NAME, JavaToolsExtension.class, project);
	}

	private final Project project;

	private final GenerateI18N generateI18NConfig;

	/**
	 * Construct {@linkplain JavaToolsExtension}.
	 *
	 * @param project The owning {@linkplain Project}.
	 */
	public JavaToolsExtension(Project project) {
		this.project = project;
		this.generateI18NConfig = new GenerateI18N(this.project);
	}

	/**
	 * Get the I18N helper class generation config. build.gradle:
	 *
	 * <pre>
	 * javatools {
	 *  generateI18N {
	 *   ...
	 *  }
	 * }
	 * </pre>
	 *
	 * @return The I18N helper class generation config.
	 */
	public GenerateI18N getGenerateI18N() {
		return this.generateI18NConfig;
	}

	/**
	 * Execute {@linkplain #generateI18N} configuration action.
	 *
	 * @param configuration The configuration action.
	 */
	public void generateI18N(Action<? super GenerateI18N> configuration) {
		this.generateI18NConfig.setEnabled(false);
		configuration.execute(this.generateI18NConfig);
	}

}
