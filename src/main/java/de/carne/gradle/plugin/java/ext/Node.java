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
package de.carne.gradle.plugin.java.ext;

import java.io.File;

import org.gradle.api.Project;

import de.carne.gradle.plugin.java.task.NodeTask;

/**
 * Configuration object for {@linkplain NodeTask} derived tasks.
 * <p>
 * build.gradle:
 *
 * <pre>
 * javatools {
 *  node {
 *   ...
 *  }
 * }
 * </pre>
 */
public class Node {

	private final Project project;

	private boolean enabledParam = false;
	private String npmCommandParam = "npm";
	private String npmVersionParam = ".*";
	private File nodeProjectDirParam;
	private File nodeDistDirParam;
	private String buildScriptsParam = "build";
	private String testScriptsParam = "test";

	/**
	 * Constructs {@linkplain Node}.
	 *
	 * @param project the owning {@linkplain Project}.
	 */
	public Node(Project project) {
		this.project = project;
		this.nodeProjectDirParam = this.project.file("node");
		this.nodeDistDirParam = new File(this.project.getBuildDir(), "node/dist");
	}

	/**
	 * Checks whether node integration is enabled.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  enabled = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @return {@code true} if node integration is enabled.
	 */
	public boolean isEnabled() {
		return this.enabledParam;
	}

	/**
	 * Enables/disables node integration.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  enabled = true|false // default: false
	 * }
	 * </pre>
	 *
	 * @param enabled whether to enable or disable the generation of I18N helper classes.
	 */
	public void setEnabled(boolean enabled) {
		this.enabledParam = enabled;
	}

	/**
	 * Gets the npm command string to execute.
	 * <p>
	 * If the npm executable is not inside the path or specific version has to be invoked, a fully qualified command
	 * including path has to be defined here.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  npmCommand = "npm" // default: "npm"
	 * }
	 * </pre>
	 *
	 * @return the npm command string to execute.
	 */
	public String getNpmCommand() {
		return this.npmCommandParam;
	}

	/**
	 * Sets the nom command string to execute.
	 * <p>
	 * If the npm executable is not inside the path or specific version has to be invoked, a fully qualified command
	 * including path has to be defined here.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  npmCommand = "npm" // default: "npm"
	 * }
	 * </pre>
	 *
	 * @param npmCommand the npm command string to execute.
	 */
	public void setNpmCommand(String npmCommand) {
		this.npmCommandParam = npmCommand;
	}

	/**
	 * Gets the required npm version pattern.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  npmVersion = ".*" // default: ".*"
	 * }
	 * </pre>
	 *
	 * @return the required npm version pattern.
	 */
	public String getNpmVersion() {
		return this.npmVersionParam;
	}

	/**
	 * Sets the required npm version pattern.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  npmVersion = ".*" // default: ".*"
	 * }
	 * </pre>
	 *
	 * @param npmVersion the required npm version pattern.
	 */
	public void setNpmVersion(String npmVersion) {
		this.npmVersionParam = npmVersion;
	}

	/**
	 * Gets the node project directory.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  nodeProjectDir = file(...) // default: file("${projectDir}/node")
	 * }
	 * </pre>
	 *
	 * @return the node project directory.
	 */
	public File getNodeProjectDir() {
		return this.nodeProjectDirParam;
	}

	/**
	 * Sets the node project directory.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  nodeProjectDir = file(...) // default: file("${projectDir}/node")
	 * }
	 * </pre>
	 *
	 * @param dir the node project directory.
	 */
	public void setNodeProjectDir(File dir) {
		this.nodeProjectDirParam = dir;
	}

	/**
	 * Gets the node project's dist directory.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  nodeDistDir = file(...) // default: file("${projectDir}/node/dist")
	 * }
	 * </pre>
	 *
	 * @return the node project's dist directory.
	 */
	public File getNodeDistDir() {
		return this.nodeDistDirParam;
	}

	/**
	 * Sets the node project's dist directory.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  nodeDistDir = file(...) // default: file("${projectDir}/node/dist")
	 * }
	 * </pre>
	 *
	 * @param dir the node project's dist directory.
	 */
	public void setNodeDistDir(File dir) {
		this.nodeDistDirParam = dir;
	}

	/**
	 * Sets the build scripts to execute.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  buildScripts = "build" // default: "build"
	 * }
	 * </pre>
	 *
	 * @param buildScripts the comma separated list of build scripts to execute.
	 */
	public void setBuildScripts(String buildScripts) {
		this.buildScriptsParam = buildScripts;
	}

	/**
	 * Gets the build scripts to execute.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  buildScripts = "build" // default: "build"
	 * }
	 * </pre>
	 *
	 * @return the comma separated list of build scripts to execute.
	 */
	public String getBuildScripts() {
		return this.buildScriptsParam;
	}

	/**
	 * Sets the test scripts to execute.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  testScripts = "test" // default: "test"
	 * }
	 * </pre>
	 *
	 * @param testScripts the comma separated list of test scripts to execute.
	 */
	public void setTestScripts(String testScripts) {
		this.testScriptsParam = testScripts;
	}

	/**
	 * Gets the test scripts to execute.
	 * <p>
	 * build.gradle:
	 *
	 * <pre>
	 * node {
	 *  testScripts = "test" // default: "test"
	 * }
	 * </pre>
	 *
	 * @return the comma separated list of test scripts to execute.
	 */
	public String getTestScripts() {
		return this.testScriptsParam;
	}

}
