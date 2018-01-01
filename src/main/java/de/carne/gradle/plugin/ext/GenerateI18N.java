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

import java.io.File;

import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;

/**
 * Configuration object for {@linkplain de.carne.gradle.plugin.task.GenerateI18NTask}.
 * <p>
 * build.gradle:
 *
 * <pre>
 * javatools {
 *  generateI18N {
 *   ...
 *  }
 * }
 * </pre>
 */
public class GenerateI18N {

	private final Project project;

	private boolean enabledParam = true;
	private String keyFilterParam = "^I18N_.*";
	private File genDirParam;
	private ConfigurableFileTree bundlesParam;

	/**
	 * Construct {@linkplain GenerateI18N}.
	 *
	 * @param project The owning {@linkplain Project}.
	 */
	public GenerateI18N(Project project) {
		this.project = project;
		this.genDirParam = this.project.file("src/main/java");
		this.bundlesParam = getBundlesDefault(this.project, "src/main/resources", "**/*I18N.properties");
	}

	private static ConfigurableFileTree getBundlesDefault(Project project, String srcDir, String include) {
		ConfigurableFileTree bundles = project.fileTree(srcDir);

		bundles.include(include);
		return bundles;
	}

	/**
	 * Check whether the generation of I18N helper classes is enabled.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * generateI18N {
	 *  enabled = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @return {@code true} if the generation of I18N helper classes is enabled.
	 */
	public boolean isEnabled() {
		return this.enabledParam;
	}

	/**
	 * Enable/disable the generation of I18N helper classes.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * generateI18N {
	 *  enabled = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @param enabled Whether to enable or disable the generation of I18N helper classes.
	 */
	public void setEnabled(boolean enabled) {
		this.enabledParam = enabled;
	}

	/**
	 * Get the pattern string identifying the resource bundle keys to be processed during generation.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * generateI18N {
	 *  keyFilter = "..." // default: "^I18N_.&#42;"
	 * }
	 * </pre>
	 *
	 * @return The pattern string identifying the resource bundle keys to be processed during generation.
	 */
	public String getKeyFilter() {
		return this.keyFilterParam;
	}

	/**
	 * Set the pattern string identifying the resource bundle keys to be processed during generation.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * generateI18N {
	 *  keyFilter = "..." // default: "^I18N_.&#42;"
	 * }
	 * </pre>
	 *
	 * @param keyFilter The pattern string identifying the resource bundle keys to be processed during generation.
	 */
	public void setKeyFilter(String keyFilter) {
		this.keyFilterParam = keyFilter;
	}

	/**
	 * Get the target folder for the generated I18N helper classes.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * generateI18N {
	 *  genDir = file(...) // default: file("src/main/java")
	 * }
	 * </pre>
	 *
	 * @return The target folder for the generated I18N helper classes.
	 */
	public File getGenDir() {
		return this.genDirParam;
	}

	/**
	 * Set the target folder for the generated I18N helper classes.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * generateI18N {
	 *  genDir = file(...) // default: file(/src/main/java)
	 * }
	 * </pre>
	 *
	 * @param genDir The target folder for the generated I18N helper classes.
	 */
	public void setGenDir(File genDir) {
		this.genDirParam = genDir;
	}

	/**
	 * Get the resource bundles to process.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * generateI18N {
	 *  bundles = fileTree(...) // default: fileTree("src/main/resources").include("&#42;&#42;/&#42;I18N.properties")
	 * }
	 * </pre>
	 *
	 * @return The resource bundles to process.
	 */
	public ConfigurableFileTree getBundles() {
		return this.bundlesParam;
	}

	/**
	 * Set the resource bundles to process.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * generateI18N {
	 *  bundles = fileTree(...) // default: fileTree("src/main/resources").include("&#42;&#42;/&#42;I18N.properties")
	 * }
	 * </pre>
	 *
	 * @param bundles The resource bundles to process.
	 */
	public void setBundles(ConfigurableFileTree bundles) {
		this.bundlesParam = bundles;
	}

}
