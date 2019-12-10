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
package de.carne.gradle.plugin.test.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.gradle.plugin.util.JavaOutput;

/**
 * Test {@linkplain JavaOutput} class.
 */
class JavaOutputTest {

	@Test
	void testMangleBundleKey() {
		Assertions.assertEquals("keyTest", JavaOutput.mangleBundleKey("KEY_TEST"));
	}

	@Test
	void testEncodeBundleString() {
		Assertions.assertEquals(
				"It is ok to have special chars like &frasl;&#42; &commat;&#228;&#246;&#252; &#42;&frasl;<br>in a bundle string.",
				JavaOutput.encodeBundleString("It is ok to have special chars like /* @äöü */\r\nin a bundle string."));
	}

}
