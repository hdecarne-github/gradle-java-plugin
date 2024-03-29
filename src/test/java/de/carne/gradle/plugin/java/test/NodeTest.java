/*
 * Copyright (c) 2018-2022 Holger de Carne and contributors, All Rights Reserved.
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

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

import de.carne.gradle.plugin.java.task.NodeTask;

/**
 * Test {@linkplain NodeTask} based tasks.
 */
class NodeTest extends TestProjectRunner {

	@Test
	void testBuildAndTestNode() {
		BuildResult cleanBuildResult = run("-s", "-i", "clean", "build");

		assertTaskOutcome(cleanBuildResult, ":npmInstall", TaskOutcome.SUCCESS);
		assertTaskOutcome(cleanBuildResult, ":npmBuild", TaskOutcome.SUCCESS);
		assertTaskOutcome(cleanBuildResult, ":npmTest", TaskOutcome.SUCCESS);

		BuildResult rebuildResult = run("-s", "-i", "build");

		assertTaskOutcome(rebuildResult, ":npmInstall", TaskOutcome.UP_TO_DATE);
		assertTaskOutcome(rebuildResult, ":npmBuild", TaskOutcome.UP_TO_DATE);
		assertTaskOutcome(rebuildResult, ":npmTest", TaskOutcome.UP_TO_DATE);
	}

}
