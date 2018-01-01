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
package de.carne.gradle.plugin.test;

import java.io.File;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Test;

import de.carne.gradle.plugin.task.GenerateI18NTask;

/**
 * Test {@linkplain GenerateI18NTask}.
 */
public class GenerateI18NTaskTest {

	private static File TEST_BASE_DIR = new File(
			System.getProperty(GenerateI18NTaskTest.class.getPackage().getName(), "build/testProjects"))
					.getAbsoluteFile();

	/**
	 * Test building project GenerateI18NTaskTest1.
	 */
	@Test
	public void testProjectGenerateI18NTaskTest1() {
		File testProjectDir = new File(TEST_BASE_DIR, "GenerateI18NTaskTest1");
		GradleRunner gradleRunner = GradleRunner.create().withProjectDir(testProjectDir).withTestKitDir(TEST_BASE_DIR)
				.withPluginClasspath();
		BuildResult cleanAssembleResult = gradleRunner.withArguments("-s", "clean", "assemble").build();

		System.out.println(cleanAssembleResult.getOutput());

		BuildResult assembleResult = gradleRunner.withArguments("-s", "assemble").build();

		System.out.println(assembleResult.getOutput());
	}

}
