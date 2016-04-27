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
package de.carne.gradleplugins.generate;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Date;
import java.util.ResourceBundle;

import de.carne.util.Strings;

/**
 * {@code Generator} class used for all kinds of Java code generators.
 */
public abstract class JavaGenerator extends Generator {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(JavaGenerator.class.getName());

	private static final MessageFormat TEMPLATE_FILE_COMMENT = new MessageFormat(
			BUNDLE.getString("TEMPLATE_FILE_COMMENT"));

	/**
	 * Construct {@code JavaGenerator}.
	 *
	 * @param generationTimestamp See {@linkplain Generator#Generator(Date)}
	 */
	protected JavaGenerator(Date generationTimestamp) {
		super(generationTimestamp);
	}

	/**
	 * Encode string data to be inserted into a java comment.
	 *
	 * @param data The data to encode.
	 * @return The encoded data.
	 */
	protected String encodeComment(String data) {
		String[] dataLines = Strings.splitLines(data);
		StringBuilder buffer = new StringBuilder();

		for (String dataLine : dataLines) {
			if (buffer.length() > 0) {
				buffer.append("<br/>");
			}
			for (int dataLineIndex = 0; dataLineIndex < dataLine.length(); dataLineIndex++) {
				char c = dataLine.charAt(dataLineIndex);

				switch (c) {
				case '<':
					buffer.append("&lt;");
					break;
				case '>':
					buffer.append("&gt;");
					break;
				case '/':
					buffer.append("&frasl;");
					break;
				case '@':
					buffer.append("&#" + Integer.toString(c) + ";");
					break;
				default:
					buffer.append(c);
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * Generate a Java file comment.
	 *
	 * @param out The {@code Writer} to write the output to.
	 * @param timestamp The timestamp to use for comment generation.
	 * @param title The title to use for comment generation.
	 * @throws IOException if an I/O error occurs during generation.
	 */
	protected void generateFileComment(Writer out, Date timestamp, String title) throws IOException {
		write(out, TEMPLATE_FILE_COMMENT, timestamp, title);
	}

}
