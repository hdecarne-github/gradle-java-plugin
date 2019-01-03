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

import de.carne.gradle.plugin.task.GenerateI18NTask;

/**
 * Test {@linkplain GenerateI18NTask} class.
 */
class GenerateI18NTaskTest {

	private static File TEST_BASE_DIR = new File(
			System.getProperty(GenerateI18NTaskTest.class.getPackage().getName(), "build/testProjects"))
					.getAbsoluteFile();

	@Test
	void testProjectGenerateI18NTaskTest1() {
		File testProjectDir = new File(TEST_BASE_DIR, "GenerateI18NTaskTest1");
		List<String> cleanAssembleArguments = Arrays.asList("-s", "-i", "clean", "assemble");

		BuildResult cleanAssembleResult = GradleRunner.create().withProjectDir(testProjectDir)
				.withTestKitDir(TEST_BASE_DIR).withPluginClasspath().withArguments(cleanAssembleArguments).build();

		System.out.println(cleanAssembleResult.getOutput());

		assertTaskOutcome(cleanAssembleResult, ":generateI18N", TaskOutcome.SUCCESS);
		assertTaskOutcome(cleanAssembleResult, ":assemble", TaskOutcome.SUCCESS);

		List<String> assembleArguments = Arrays.asList("-s", "-i", "assemble");

		BuildResult assembleResult = GradleRunner.create().withProjectDir(testProjectDir).withTestKitDir(TEST_BASE_DIR)
				.withPluginClasspath().withArguments(assembleArguments).build();

		System.out.println(assembleResult.getOutput());

		assertTaskOutcome(assembleResult, ":generateI18N", TaskOutcome.UP_TO_DATE);
		assertTaskOutcome(assembleResult, ":assemble", TaskOutcome.UP_TO_DATE);
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
