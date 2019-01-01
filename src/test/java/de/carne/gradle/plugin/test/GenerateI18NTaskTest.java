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
import java.util.ArrayList;
import java.util.List;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
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
		List<String> arguments1 = new ArrayList<>();

		arguments1.add("-s");
		arguments1.add("clean");
		arguments1.add("assemble");

		GradleRunner gradleRunner = GradleRunner.create().withProjectDir(testProjectDir).withTestKitDir(TEST_BASE_DIR)
				.withPluginClasspath();
		BuildResult cleanAssembleResult = gradleRunner.withArguments(arguments1).build();

		System.out.println(cleanAssembleResult.getOutput());

		List<String> arguments2 = new ArrayList<>();

		arguments2.add("-s");
		arguments2.add("assemble");

		BuildResult assembleResult = gradleRunner.withArguments(arguments2).build();

		System.out.println(assembleResult.getOutput());
	}

}
