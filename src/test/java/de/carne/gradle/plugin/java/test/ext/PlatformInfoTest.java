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
package de.carne.gradle.plugin.java.test.ext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import de.carne.gradle.plugin.java.ext.PlatformInfo;

/**
 * Test {@linkplain PlatformInfo} class.
 */
class PlatformInfoTest {

	@EnabledOnOs(OS.LINUX)
	@Test
	void testLinux() {
		PlatformInfo platformInfo = new PlatformInfo();

		Assertions.assertTrue(platformInfo.isIsLinux());
		Assertions.assertFalse(platformInfo.isIsMacos());
		Assertions.assertFalse(platformInfo.isIsWindows());
		Assertions.assertEquals("gtk-linux-x86_64", platformInfo.getSwtToolkit());
	}

	@EnabledOnOs(OS.MAC)
	@Test
	void testMac() {
		PlatformInfo platformInfo = new PlatformInfo();

		Assertions.assertFalse(platformInfo.isIsLinux());
		Assertions.assertTrue(platformInfo.isIsMacos());
		Assertions.assertFalse(platformInfo.isIsWindows());
		Assertions.assertEquals("cocoa-macosx-x86_64", platformInfo.getSwtToolkit());
	}

	@EnabledOnOs(OS.WINDOWS)
	@Test
	void testWindows() {
		PlatformInfo platformInfo = new PlatformInfo();

		Assertions.assertFalse(platformInfo.isIsLinux());
		Assertions.assertFalse(platformInfo.isIsMacos());
		Assertions.assertTrue(platformInfo.isIsWindows());
		Assertions.assertEquals("win32-win32-x86_64", platformInfo.getSwtToolkit());
	}

}
