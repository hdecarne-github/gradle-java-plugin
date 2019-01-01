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
package de.carne.gradle.plugin.util;

/**
 * Utility class providing Java code generation related functions.
 */
public final class JavaOutput {

	private JavaOutput() {
		// Prevent instantiation
	}

	/**
	 * Mangles a resource bundle key to standard Java function name.
	 *
	 * @param bundleKey the name to mangle.
	 * @return the mangled name.
	 */
	public static String mangleBundleKey(String bundleKey) {
		StringBuilder mangled = new StringBuilder();
		int bundleKeyLength = bundleKey.length();
		boolean nextUpperCase = false;

		for (int chIndex = 0; chIndex < bundleKeyLength; chIndex++) {
			char ch = bundleKey.charAt(chIndex);

			if (ch == '_') {
				nextUpperCase = true;
			} else if (nextUpperCase) {
				nextUpperCase = false;
				mangled.append(Character.toUpperCase(ch));
			} else {
				mangled.append(Character.toLowerCase(ch));
			}
		}
		return mangled.toString();
	}

	/**
	 * Encodes a resource bundle string for Javadoc output.
	 *
	 * @param bundleString the {@linkplain String} to encode.
	 * @return the encoded string data.
	 */
	public static String encodeBundleString(String bundleString) {
		StringBuilder encoded = new StringBuilder();

		bundleString.chars().forEachOrdered(code -> {
			switch (code) {
			case '\r':
				break;
			case '\n':
				encoded.append("<br>");
				break;
			case '<':
				encoded.append("&lt;");
				break;
			case '>':
				encoded.append("&gt;");
				break;
			case '/':
				encoded.append("&frasl;");
				break;
			case '@':
			case '*':
				encoded.append("&#" + Integer.toString(code) + ";");
				break;
			default:
				encoded.append((char) code);
			}
		});
		return encoded.toString();
	}

}
