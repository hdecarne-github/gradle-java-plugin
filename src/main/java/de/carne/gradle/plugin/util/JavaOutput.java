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
package de.carne.gradle.plugin.util;

import java.util.HashMap;
import java.util.Map;

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

	private static final Map<Character, String> ENCODE_BUNDLE_STRING_MAP = new HashMap<>();

	static {
		ENCODE_BUNDLE_STRING_MAP.put('\r', "");
		ENCODE_BUNDLE_STRING_MAP.put('\n', "<br>");
		ENCODE_BUNDLE_STRING_MAP.put('<', "&lt;");
		ENCODE_BUNDLE_STRING_MAP.put('>', "&gt;");
		ENCODE_BUNDLE_STRING_MAP.put('&', "&amp;");
		ENCODE_BUNDLE_STRING_MAP.put('"', "&quot;");
		ENCODE_BUNDLE_STRING_MAP.put('\'', "&#" + (int) '\'' + ";");
		ENCODE_BUNDLE_STRING_MAP.put('/', "&frasl;");
		ENCODE_BUNDLE_STRING_MAP.put('@', "&commat;");
		ENCODE_BUNDLE_STRING_MAP.put('*', "&#" + (int) '*' + ";");
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
			String mappedCode = ENCODE_BUNDLE_STRING_MAP.get(Character.valueOf((char) code));

			if (mappedCode != null) {
				encoded.append(mappedCode);
			} else if (32 <= code && code <= 126) {
				encoded.append((char) code);
			} else {
				encoded.append("&#").append(code).append(';');
			}
		});
		return encoded.toString();
	}

}
