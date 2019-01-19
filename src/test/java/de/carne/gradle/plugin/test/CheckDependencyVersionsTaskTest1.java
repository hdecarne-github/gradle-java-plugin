/*
 * Copyright (c) 2018-2019 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.gradle.plugin.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.gradle.plugin.task.CheckDependencyVersionsTask;

/**
 * Test {@linkplain CheckDependencyVersionsTask} class.
 */
class CheckDependencyVersionsTaskTest1 {

	@SuppressWarnings("null")
	private static File TEST_BASE_DIR = new File(
			System.getProperty(CheckDependencyVersionsTaskTest1.class.getPackage().getName(), "build/testProjects"))
					.getAbsoluteFile();

	@Test
	void testProjectCheckDependencyVersionsTaskTest1() {
		File testProjectDir = new File(TEST_BASE_DIR, "CheckDependencyVersionsTaskTest1");
		List<String> runArguments = Arrays.asList("-s", "-i", "checkDependencyVersions");

		BuildResult run1Result = GradleRunner.create().withProjectDir(testProjectDir).withTestKitDir(TEST_BASE_DIR)
				.withPluginClasspath().withArguments(runArguments).build();

		System.out.println(run1Result.getOutput());

		assertTaskOutcome(run1Result, ":checkDependencyVersions", TaskOutcome.SUCCESS);
	}

	private void assertTaskOutcome(BuildResult buildResult, String taskPath, TaskOutcome taskOutcome) {
		boolean status = false;

		for (BuildTask task : buildResult.getTasks()) {
			if (taskPath.equals(task.getPath())) {
				Assertions.assertEquals(taskOutcome, task.getOutcome());
				status = true;
			}
		}
		Assertions.assertTrue(status, "No TaskOutcome for task " + taskPath);
	}

}
