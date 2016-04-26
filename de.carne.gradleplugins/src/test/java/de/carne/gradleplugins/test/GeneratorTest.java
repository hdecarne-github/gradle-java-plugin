/*
 * Copyright (c) 2016 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.gradleplugins.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import de.carne.gradleplugins.generate.JavaI18NGenerator;

/**
 *
 */
public class GeneratorTest {

	private static final Date TEST_TIMESTAMP = new Date(1461360290992l);

	/**
	 * Test {@code JavaI18NGenerator}.
	 */
	@Test
	public void testJavaI18NGenerator() {
		JavaI18NGenerator i18nGenerator = new JavaI18NGenerator(TEST_TIMESTAMP);
		HashMap<String, String> ctx = new HashMap<>();

		ctx.put(JavaI18NGenerator.KEY_I18N_PACKAGE, "test");
		ctx.put(JavaI18NGenerator.KEY_I18N_CLASS, "I18N");
		ctx.put(JavaI18NGenerator.KEY_I18N_KEY_FILTER, "^TEXT_.*");
		try (Reader in = new InputStreamReader(GeneratorTest.class.getResourceAsStream("I18N.properties"))) {
			StringWriter out = new StringWriter();

			i18nGenerator.generate(ctx, in, out);
			Assert.assertEquals(getStringFromResource("testJavaI18NGenerator.txt"), out.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	private String getStringFromResource(String name) throws IOException, URISyntaxException {
		Path resourcePath = Paths.get(GeneratorTest.class.getResource(name).toURI());
		byte[] resourceBytes = Files.readAllBytes(resourcePath);

		return new String(resourceBytes, StandardCharsets.UTF_8);
	}

}
