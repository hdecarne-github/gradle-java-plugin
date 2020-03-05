/*
 * Copyright (c) 2018-2020 Holger de Carne and contributors, All Rights Reserved.
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

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

import de.carne.gradle.plugin.task.GenerateI18NTask;

/**
 * Test {@linkplain GenerateI18NTask} class.
 */
class GenerateI18NTaskTest1 extends TestProjectRunner {

	@Test
	void testProjectGenerateI18NTask() {
		BuildResult cleanAssembleResult = run("-s", "-i", "clean", "assemble");

		assertTaskOutcome(cleanAssembleResult, ":generateI18N", TaskOutcome.SUCCESS);
		assertTaskOutcome(cleanAssembleResult, ":assemble", TaskOutcome.SUCCESS);

		BuildResult assembleResult = run("-s", "-i", "assemble");

		assertTaskOutcome(assembleResult, ":generateI18N", TaskOutcome.UP_TO_DATE);
		assertTaskOutcome(assembleResult, ":assemble", TaskOutcome.UP_TO_DATE);
	}

}
