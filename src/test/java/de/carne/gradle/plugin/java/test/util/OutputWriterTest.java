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

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.gradle.plugin.java.util.OutputWriter;

/**
 * Test {@linkplain OutputWriter} class.
 */
class OutputWriterTest {

	@Test
	void testOutputWriterCrLf() throws IOException {
		try (StringWriter stringWriter = new StringWriter();
				OutputWriter outputWriter = new OutputWriter(stringWriter, "\r\n")) {
			outputWriter.write("\r\n \n \r");
			outputWriter.flush();
			Assertions.assertEquals("\r\n \r\n \r\n", stringWriter.toString());
		}
	}

	@Test
	void testOutputWriterLf() throws IOException {
		try (StringWriter stringWriter = new StringWriter();
				OutputWriter outputWriter = new OutputWriter(stringWriter, "\n")) {
			outputWriter.write("\r\n \n \r");
			outputWriter.flush();
			Assertions.assertEquals("\n \n \n", stringWriter.toString());
		}
	}

	@Test
	void testOutputWriterCr() throws IOException {
		try (StringWriter stringWriter = new StringWriter();
				OutputWriter outputWriter = new OutputWriter(stringWriter, "\r")) {
			outputWriter.write("\r\n \n \r");
			outputWriter.flush();
			Assertions.assertEquals("\r \r \r", stringWriter.toString());
		}
	}

}
