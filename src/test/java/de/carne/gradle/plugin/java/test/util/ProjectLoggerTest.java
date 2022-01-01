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
package de.carne.gradle.plugin.java.test.util;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.gradle.plugin.java.util.ProjectLogger;

/**
 * Test {@linkplain ProjectLogger} class.
 */
class ProjectLoggerTest {

	@Test
	void testLogger() {
		PrintStream savedOut = System.out;
		PrintStreamFilter testOut = new PrintStreamFilter(savedOut);

		System.setOut(testOut);
		ProjectLogger.trace("traceMessage: {}", getClass().getName());
		ProjectLogger.trace("traceException", new Exception());
		ProjectLogger.debug("debugMessage: {}", getClass().getName());
		ProjectLogger.debug("debugException", new Exception());
		ProjectLogger.info("infoMessage: {}", getClass().getName());
		ProjectLogger.info("infoException", new Exception());
		ProjectLogger.warn("warnMessage: {}", getClass().getName());
		ProjectLogger.warn("warnException", new Exception());
		ProjectLogger.error("errorMessage: {}", getClass().getName());
		ProjectLogger.error("errorException", new Exception());
		System.setOut(savedOut);

		int printlnCount = testOut.printlnCount();

		Assertions.assertEquals(10, printlnCount);
	}

	private static class PrintStreamFilter extends PrintStream {

		private int printlnCount = 0;

		PrintStreamFilter(OutputStream out) {
			super(out, true);
		}

		@Override
		public void println(@Nullable String s) {
			this.printlnCount++;
			super.println(s);
		}

		public int printlnCount() {
			return this.printlnCount;
		}

	}

}
