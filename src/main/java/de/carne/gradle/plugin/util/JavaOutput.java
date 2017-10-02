/*
 * Copyright (c) 2016-2017 Holger de Carne and contributors, All Rights Reserved.
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
	 * Encode string data for Javadoc output.
	 *
	 * @param string The {@linkplain String} to encode.
	 * @return The encoded string data.
	 */
	public static String encodeJavadoc(String string) {
		StringBuilder encoded = new StringBuilder();

		string.chars().forEachOrdered(code -> {
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
