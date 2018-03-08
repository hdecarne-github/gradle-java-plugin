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
package de.carne.gradle.plugin.ext;

import de.carne.util.Platform;

/**
 * Configuration object providing build platform related informations.
 * <p>
 * build.gradle:
 *
 * <pre>
 * test {
 *  enabled = javatools.platform.swtToolkit.equals("cocoa-macosx-x86_64")
 *   ...
 * }
 * </pre>
 */
public class PlatformInfo {

	/**
	 * Checks whether the currently executing build platform is of type Linux.
	 *
	 * @return {@code true} if the currently executing build platform is of type Linux.
	 */
	public boolean isIsLinux() {
		return Platform.IS_LINUX;
	}

	/**
	 * Checks whether the currently executing build platform is of type macOS.
	 *
	 * @return {@code true} if the currently executing build platform is of type macOS.
	 */
	public boolean isIsMacos() {
		return Platform.IS_MACOS;
	}

	/**
	 * Checks whether the currently executing build platform is of type Windows.
	 *
	 * @return {@code true} if the currently executing build platform is of type Windows.
	 */
	public boolean isIsWindows() {
		return Platform.IS_WINDOWS;
	}

	/**
	 * Gets the SWT toolkit suitable for the currently executing build platform.
	 *
	 * @return the SWT toolkit suitable for the currently executing build platform.
	 */
	public String getSwtToolkit() {
		StringBuilder toolkit = new StringBuilder();

		if (Platform.IS_LINUX) {
			toolkit.append("gtk-linux");
		} else if (Platform.IS_MACOS) {
			toolkit.append("cocoa-macosx");
		} else if (Platform.IS_WINDOWS) {
			toolkit.append("win32-win32");
		} else {
			// Do not fail in case of unknown toolkit
			toolkit.append("unknown-unknown");
		}
		if ("x86".equals(Platform.SYSTEM_OS_ARCH) || "x86_32".equals(Platform.SYSTEM_OS_ARCH)) {
			toolkit.append("-x86");
		} else if ("x86_64".equals(Platform.SYSTEM_OS_ARCH)) {
			toolkit.append("-x86_64");
		} else {
			// Do not fail in case of unknown toolkit
			toolkit.append("uknown");
		}
		return toolkit.toString();
	}

}
