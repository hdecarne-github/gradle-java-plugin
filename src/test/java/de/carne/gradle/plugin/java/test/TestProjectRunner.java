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
package de.carne.gradle.plugin.java.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Assertions;

abstract class TestProjectRunner {

	protected GradleRunner runner() {
		File baseDir = new File("build/testProjects").getAbsoluteFile();
		File projectDir = new File(baseDir, getClass().getSimpleName());

		return GradleRunner.create().withProjectDir(projectDir).withTestKitDir(baseDir).withPluginClasspath();
	}

	protected BuildResult run(@NonNull String... arguments) {
		List<String> argumentsList = Arrays.asList(arguments);

		return runner().withArguments(argumentsList).build();
	}

	protected void assertTaskOutcome(BuildResult buildResult, String taskPath, TaskOutcome taskOutcome) {
		boolean status = false;

		for (BuildTask task : buildResult.getTasks()) {
			if (taskPath.equals(task.getPath())) {
				Assertions.assertEquals(taskOutcome, task.getOutcome());
				status = true;
			}
		}
		Assertions.assertTrue(status, "No TaskOutcome for task " + taskPath);
	}

	protected void assertOutputLine(BuildResult buildResult, String line) {
		boolean match = false;

		try (BufferedReader outputLines = new BufferedReader(new StringReader(buildResult.getOutput()))) {
			String outputLine;

			while (!match && (outputLine = outputLines.readLine()) != null) {
				if (outputLine.equals(line)) {
					match = true;
				}
			}
		} catch (IOException e) {
			Assertions.fail(e);
		}
		Assertions.assertTrue(match, "No output line: " + line);
	}

}
