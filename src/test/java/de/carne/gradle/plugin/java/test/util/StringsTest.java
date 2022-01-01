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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.gradle.plugin.java.util.Strings;

/**
 * Test {@linkplain Strings} class.
 */
class StringsTest {

	private static final String NOT_EMPTY_STRING = StringsTest.class.getName();

	@Test
	void testEmpty() {
		Assertions.assertTrue(Strings.isEmpty(null));
		Assertions.assertTrue(Strings.isEmpty(""));
		Assertions.assertFalse(Strings.isEmpty(NOT_EMPTY_STRING));
	}

	@Test
	void testNotEmpty() {
		Assertions.assertFalse(Strings.notEmpty(null));
		Assertions.assertFalse(Strings.notEmpty(""));
		Assertions.assertTrue(Strings.notEmpty(NOT_EMPTY_STRING));
	}

	@Test
	void testSafe() {
		Assertions.assertEquals("", Strings.safe(null));
		Assertions.assertSame("", Strings.safe(""));
		Assertions.assertSame(NOT_EMPTY_STRING, Strings.safe(NOT_EMPTY_STRING));
	}

}
