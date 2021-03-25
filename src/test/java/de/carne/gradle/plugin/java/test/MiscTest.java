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

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Test miscellaneous plugin functions.
 */
class MiscTest extends TestProjectRunner {

	@Test
	@EnabledOnOs(OS.LINUX)
	void testOsFlagsLinux() {
		testOsFlags(true, false, false);
	}

	@Test
	@EnabledOnOs(OS.MAC)
	void testOsFlagsMac() {
		testOsFlags(false, true, false);
	}

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testOsFlagsWindow() {
		testOsFlags(false, false, true);
	}

	private void testOsFlags(boolean isLinux, boolean isMacos, boolean isWindows) {
		BuildResult cleanAssembleResult = run("-s", "-i", "clean", "assemble");

		assertOutputLine(cleanAssembleResult, "javatools.platform.isLinux: " + isLinux);
		assertOutputLine(cleanAssembleResult, "javatools.platform.isMacos: " + isMacos);
		assertOutputLine(cleanAssembleResult, "javatools.platform.isWindows: " + isWindows);
	}

}
