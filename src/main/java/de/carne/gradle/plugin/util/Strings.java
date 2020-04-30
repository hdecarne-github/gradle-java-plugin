/*
 * Copyright (c) 2016-2020 Holger de Carne and contributors, All Rights Reserved.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class providing {@linkplain String} related functions.
 */
public final class Strings {

	private Strings() {
		// Prevent instantiation
	}

	/**
	 * Ellipsis string.
	 */
	public static final String ELLIPSIS = "\\u2026";

	/**
	 * Checks whether a {@linkplain String} is empty.
	 * <p>
	 * A {@linkplain String} is considered empty if it is either {@code null} or of length {@code 0}.
	 *
	 * @param s the {@linkplain String} to check.
	 * @return {@code true} if the string is empty.
	 */
	public static boolean isEmpty(@Nullable String s) {
		return s == null || s.length() == 0;
	}

	/**
	 * Checks whether a {@linkplain String} is not empty.
	 * <p>
	 * A {@linkplain String} is considered empty if it is either {@code null} or of length {@code 0}.
	 *
	 * @param s the {@linkplain String} to check.
	 * @return {@code true} if the string is not empty.
	 */
	public static boolean notEmpty(@Nullable String s) {
		return s != null && s.length() > 0;
	}

	/**
	 * Gets the string representation of the submitted object and never returns {@code null}.
	 *
	 * @param o the object to get the string representation of.
	 * @return the string representation of the submitted object (never {@code null}).
	 */
	@SuppressWarnings("null")
	public static String valueOf(@Nullable Object o) {
		return String.valueOf(String.valueOf(o));
	}

	/**
	 * Makes sure a {@linkplain String} is not {@code null}.
	 *
	 * @param s the {@linkplain String} to check.
	 * @return the submitted {@linkplain String} or {@code ""} if {@code null} was submitted.
	 */
	public static String safe(@Nullable String s) {
		return (s != null ? s : "");
	}

	/**
	 * Makes sure a {@linkplain String} is not {@code null} and trimmed.
	 *
	 * @param s the {@linkplain String} to trim.
	 * @return the submitted {@linkplain String} in trimmed form or {@code ""} if {@code null} was submitted.
	 */
	public static String safeTrim(@Nullable String s) {
		return (s != null ? s.trim() : "");
	}

	/**
	 * Makes sure a {@linkplain String} is either {@code null} or trimmed.
	 *
	 * @param s the {@linkplain String} to trim.
	 * @return the submitted {@linkplain String} in trimmed form or {@code null} if {@code null} was submitted.
	 */
	public static @Nullable String trim(@Nullable String s) {
		return (s != null ? s.trim() : s);
	}

	/**
	 * Splits a {@linkplain String} according to a given delimiter.
	 *
	 * @param s the {@linkplain String} to split.
	 * @param delim the delimiter character to split at.
	 * @param all whether to split at all occurrences of the delimiter character ({@code true}) or only at the first one
	 * ({@code false}).
	 * @return the splitted sub-strings. The actual number of sub-strings depends on the actual occurrences of the
	 * delimiter character as well as the {@code all} flag.
	 */
	@SuppressWarnings("null")
	public static @NonNull String[] split(String s, char delim, boolean all) {
		List<String> splits = new ArrayList<>();
		int splitIndex = -1;

		while (true) {
			int nextSplitIndex = (splitIndex == -1 || all ? s.indexOf(delim, splitIndex + 1) : -1);

			if (nextSplitIndex < 0) {
				splits.add(s.substring(splitIndex + 1));
				break;
			}
			splits.add(s.substring(splitIndex + 1, nextSplitIndex));
			splitIndex = nextSplitIndex;
		}
		return splits.toArray(new @Nullable String[splits.size()]);
	}

	/**
	 * Joins the {@linkplain String} representation of multiple {@linkplain Object}s into a single {@linkplain String}.
	 * <p>
	 * The {@linkplain Object#toString()} is used to retrieve the Objects' {@linkplain String} representation.
	 *
	 * @param objects the {@linkplain Objects}s to join.
	 * @param delim the delimiter to use for joining.
	 * @return the joined {@linkplain String}.
	 */
	public static String join(Object[] objects, String delim) {
		return join(Arrays.asList(objects), delim, Integer.MAX_VALUE, ELLIPSIS);
	}

	/**
	 * Joins the {@linkplain String} representation of multiple {@linkplain Object}s into a single {@linkplain String}.
	 * <p>
	 * The {@linkplain Object#toString()} is used to retrieve the Objects' {@linkplain String} representation.
	 *
	 * @param objects the {@linkplain Objects}s to join.
	 * @param delim the delimiter to use for joining.
	 * @return the joined {@linkplain String}.
	 */
	public static String join(Iterable<?> objects, String delim) {
		return join(objects, delim, Integer.MAX_VALUE, ELLIPSIS);
	}

	/**
	 * Joins the {@linkplain String} representation of multiple {@linkplain Object}s into a single {@linkplain String}.
	 * <p>
	 * The {@linkplain Object#toString()} is used to retrieve the Objects' {@linkplain String} representation.
	 *
	 * @param objects the {@linkplain String}s to join.
	 * @param delim the delimiter to use for joining.
	 * @param limit the maximum length of the joined {@linkplain String}.
	 * @return the joined {@linkplain String}.
	 */
	public static String join(Object[] objects, String delim, int limit) {
		return join(Arrays.asList(objects), delim, limit, ELLIPSIS);
	}

	/**
	 * Joins the {@linkplain String} representation of multiple {@linkplain Object}s into a single {@linkplain String}.
	 * <p>
	 * The {@linkplain Object#toString()} is used to retrieve the Objects' {@linkplain String} representation.
	 *
	 * @param objects the {@linkplain String}s to join.
	 * @param delim the delimiter to use for joining.
	 * @param limit the maximum length of the joined {@linkplain String}.
	 * @return the joined {@linkplain String}.
	 */
	public static String join(Iterable<?> objects, String delim, int limit) {
		return join(objects, delim, limit, ELLIPSIS);
	}

	/**
	 * Joins the {@linkplain String} representation of multiple {@linkplain Object}s into a single {@linkplain String}.
	 * <p>
	 * The {@linkplain Object#toString()} is used to retrieve the Objects' {@linkplain String} representation.
	 *
	 * @param objects the {@linkplain Objects}s to join.
	 * @param delim the delimiter to use for joining.
	 * @param limit the maximum length of the joined {@linkplain String}.
	 * @param ellipsis the ellipsis to place at the end of the joined string in case {@code limit} is reached.
	 * @return the joined {@linkplain String}.
	 */
	public static String join(Object[] objects, String delim, int limit, String ellipsis) {
		return join(Arrays.asList(objects), delim, limit, ellipsis);
	}

	/**
	 * Joins the {@linkplain String} representation of multiple {@linkplain Object}s into a single {@linkplain String}.
	 * <p>
	 * The {@linkplain Object#toString()} is used to retrieve the Objects' {@linkplain String} representation.
	 *
	 * @param objects the {@linkplain Objects}s to join.
	 * @param delim the delimiter to use for joining.
	 * @param limit the maximum length of the joined {@linkplain String}.
	 * @param ellipsis the ellipsis to place at the end of the joined string in case {@code limit} is reached.
	 * @return the joined {@linkplain String}.
	 */
	public static String join(Iterable<?> objects, String delim, int limit, String ellipsis) {
		StringBuilder joined = new StringBuilder();
		boolean limitReached = false;

		for (Object object : objects) {
			if (joined.length() > 0) {
				limitReached = joinLimit(joined, delim, limit);
			}
			limitReached = limitReached || joinLimit(joined, valueOf(object), limit);
			if (limitReached) {
				break;
			}
		}
		if (limitReached) {
			int joinedLength = joined.length();
			int replaceLength = Math.min(ellipsis.length(), joinedLength);
			int replaceStart = joinedLength - replaceLength;

			for (int replaceIndex = 0; replaceIndex < replaceLength; replaceIndex++) {
				joined.setCharAt(replaceStart + replaceIndex, ellipsis.charAt(replaceIndex));
			}
		}
		return joined.toString();
	}

	private static boolean joinLimit(StringBuilder joined, String s, int limit) {
		int maxAppend = limit - joined.length();
		boolean limitReached = s.length() > maxAppend;

		joined.append(!limitReached ? s : s.substring(0, maxAppend));
		return limitReached;
	}

	private static char[] hexCharsUpper = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/**
	 * Encodes a {@linkplain CharSequence} to a pure ASCII representation by quoting non printable characters.
	 *
	 * @param chars the {@linkplain CharSequence} to encode.
	 * @return the encoded characters.
	 */
	public static String encode(CharSequence chars) {
		return encode(new StringBuilder(), chars).toString();
	}

	/**
	 * Encodes a {@linkplain CharSequence} to a pure ASCII representation by quoting non printable characters.
	 *
	 * @param buffer the {@linkplain StringBuilder} to encode into.
	 * @param chars the {@linkplain CharSequence} to encode.
	 * @return the encoded characters.
	 */
	public static StringBuilder encode(StringBuilder buffer, CharSequence chars) {
		chars.chars().forEach(c -> {
			if (c == '\\') {
				buffer.append("\\\\");
			} else if (c == '"') {
				buffer.append("\\\"");
			} else if (32 <= c && c <= 126) {
				buffer.append((char) c);
			} else {
				switch (c) {
				case 0:
					buffer.append("\\0");
					break;
				case 8:
					buffer.append("\\b");
					break;
				case 9:
					buffer.append("\\t");
					break;
				case 10:
					buffer.append("\\n");
					break;
				case 12:
					buffer.append("\\f");
					break;
				case 13:
					buffer.append("\\r");
					break;
				default:
					buffer.append("\\u").append(hexCharsUpper[(c >> 12) & 0xf]).append(hexCharsUpper[(c >> 8) & 0xf])
							.append(hexCharsUpper[(c >> 4) & 0xf]).append(hexCharsUpper[c & 0xf]);
				}
			}
		});
		return buffer;
	}

	/**
	 * Decodes a {@linkplain CharSequence} previously encoded via {@linkplain #encode(CharSequence)}.
	 *
	 * @param chars the {@linkplain CharSequence} to decode.
	 * @return the decoded characters.
	 */
	public static String decode(CharSequence chars) {
		CharDecoder decoder = new CharDecoder();

		chars.chars().forEach(decoder::decode);
		return decoder.getDecoded();
	}

	private static class CharDecoder {

		private final StringBuilder buffer = new StringBuilder();
		private boolean quoted = false;
		private int encodeIndex = 0;
		private int decodedC = 0;

		CharDecoder() {
			// To make it accessible to the outer class
		}

		public void decode(int c) {
			if (!this.quoted) {
				decodeUnquoted(c);
			} else if (this.encodeIndex == 0) {
				decodeQuoted(c);
			} else {
				decodeQuotedEncoded(c);
			}
		}

		private void decodeUnquoted(int c) {
			if (c != '\\') {
				this.buffer.append((char) c);
			} else {
				this.quoted = true;
			}
		}

		private void decodeQuoted(int c) {
			if (c != 'u') {
				this.quoted = false;
				switch (c) {
				case '\\':
					this.buffer.append('\\');
					break;
				case '0':
					this.buffer.append('\0');
					break;
				case 'b':
					this.buffer.append('\b');
					break;
				case 't':
					this.buffer.append('\t');
					break;
				case 'n':
					this.buffer.append('\n');
					break;
				case 'f':
					this.buffer.append('\f');
					break;
				case 'r':
					this.buffer.append('\r');
					break;
				case '"':
					this.buffer.append('"');
					break;
				default:
					throw new IllegalArgumentException("Unexpected quoted character: " + ((char) c));
				}
			} else {
				this.encodeIndex = 1;
				this.decodedC = 0;
			}
		}

		private void decodeQuotedEncoded(int c) {
			this.encodeIndex = (this.encodeIndex + 1) % 5;
			this.quoted = this.encodeIndex != 0;
			this.decodedC <<= 4;
			if ('0' <= c && c <= '9') {
				this.decodedC |= c - '0';
			} else if ('a' <= c && c <= 'f') {
				this.decodedC |= c - 'a' + 10;
			} else if ('A' <= c && c <= 'F') {
				this.decodedC |= c - 'A' + 10;
			} else {
				throw new IllegalArgumentException("Unexpected encoded character: " + ((char) c));
			}
			if (!this.quoted) {
				this.buffer.append((char) this.decodedC);
			}
		}

		public String getDecoded() {
			return this.buffer.toString();
		}

	}

	/**
	 * Encodes a {@linkplain CharSequence} to a HTML conform representation by quoting special characters.
	 *
	 * @param chars the {@linkplain CharSequence} to encode.
	 * @return the encoded characters.
	 */
	public static String encodeHtml(CharSequence chars) {
		return encodeHtml(new StringBuilder(), chars).toString();
	}

	/**
	 * Encodes a {@linkplain CharSequence} to a HTML conform representation by quoting special characters.
	 *
	 * @param buffer the {@linkplain StringBuilder} to encode into.
	 * @param chars the {@linkplain CharSequence} to encode.
	 * @return the encoded characters.
	 */
	public static StringBuilder encodeHtml(StringBuilder buffer, CharSequence chars) {
		chars.chars().forEach(c -> {
			switch (c) {
			case '<':
				buffer.append("&lt;");
				break;
			case '>':
				buffer.append("&gt;");
				break;
			case '&':
				buffer.append("&amp;");
				break;
			case '"':
				buffer.append("&quot;");
				break;
			case '\'':
				buffer.append("&apos;");
				break;
			default:
				if (32 <= c && c <= 126) {
					buffer.append((char) c);
				} else {
					buffer.append("&#").append(c).append(';');
				}
			}
		});
		return buffer;
	}

}
