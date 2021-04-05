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

import java.io.File;
import java.io.IOException;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskExecutionException;

import de.carne.gradle.plugin.java.ext.JavaToolsExtension;
import de.carne.gradle.plugin.java.ext.Node;
import de.carne.gradle.plugin.java.util.NpmWrapper;
import de.carne.gradle.plugin.java.util.Plugins;

/**
 * Base class for node based tasks.
 */
public abstract class NodeTask extends DefaultTask implements JavaToolsTask {

	protected static final String PACKAGE_JSON_NAME = "package.json";
	protected static final String PACKAGE_LOCK_JSON_NAME = "package-lock.json";
	protected static final String NODE_MODULES_NAME = "node_modules";

	@Override
	public void afterEvaluate(Project project) {
		Node node = project.getExtensions().getByType(JavaToolsExtension.class).getNode();
		boolean enabled = node.isEnabled();

		setEnabled(enabled);
		if (enabled) {
			Plugins.checkJavaApplied(project);
		}
	}

	@Internal
	protected NpmWrapper getNpmWrapperInstance() throws IOException, InterruptedException {
		Node node = getProject().getExtensions().getByType(JavaToolsExtension.class).getNode();
		NpmWrapper npmWrapper = NpmWrapper.getInstance(node.getNodeProjectDir(), node.getNpmCommand());
		String npmVersion = npmWrapper.npmVersion();
		String requiredNpmVersion = node.getNpmVersion();

		if (!npmVersion.matches(node.getNpmVersion())) {
			throw new TaskExecutionException(this, new IllegalArgumentException(
					"Invalid npm version: " + npmVersion + " (required: " + requiredNpmVersion + ")"));
		}
		return npmWrapper;
	}

	@Internal
	protected File getNodeProjectFile(String name) {
		return new File(getProject().getExtensions().getByType(JavaToolsExtension.class).getNode().getNodeProjectDir(),
				name);
	}

	@Internal
	protected File getTaskOutFile() {
		return new File(getProject().getBuildDir(), getName() + ".out");
	}

}
