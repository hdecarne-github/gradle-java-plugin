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
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * Abstract base class for all kinds of code generators.
 */
public abstract class Generator {

	private Date generationTimestamp;

	private DateFormat generationTimestampFormat;

	/**
	 * Construct {@code Generator}.
	 *
	 * @param generationTimestamp The generation timestamp to use. If
	 *        {@code null}, the current time is used.
	 * @param generationTimestampFormat The format to use for formating the
	 *        generation timestamp. If {@code null} the default format is used.
	 */
	protected Generator(Date generationTimestamp, DateFormat generationTimestampFormat) {
		this.generationTimestamp = (generationTimestamp != null ? generationTimestamp : new Date());
		this.generationTimestampFormat = (generationTimestampFormat != null ? generationTimestampFormat
				: DateFormat.getDateTimeInstance());
	}

	/**
	 * Get the generation timestamp.
	 *
	 * @return The generation timestamp.
	 */
	public Date getGenerationTimestamp() {
		return this.generationTimestamp;
	}

	/**
	 * Get the generation timestamp format.
	 *
	 * @return The generation timestamp format.
	 */
	public DateFormat getGenerationTimestampFormat() {
		return this.generationTimestampFormat;
	}

	/**
	 * Format the generation timestamp using the set format.
	 *
	 * @return The formatted generation timestamp.
	 */
	public String formatGenerationTimestamp() {
		return this.generationTimestampFormat.format(this.generationTimestamp);
	}

	/**
	 * Perform generation.
	 *
	 * @param ctx The generation context.
	 * @param in The {@code Reader} to read the input data from.
	 * @param out The {@code Writer} to write the output data to.
	 * @throws IOException if an I/O error occurs during generation.
	 */
	public abstract void generate(Map<String, String> ctx, Reader in, Writer out) throws IOException;

	/**
	 * Get context value.
	 *
	 * @param ctx The context to evaluate.
	 * @param key The key to retrieve.
	 * @return The found value.
	 * @throws IllegalArgumentException if the key is not defined in the
	 *         context.
	 */
	protected String getContextString(Map<String, String> ctx, String key) {
		String value = ctx.get(key);

		if (value == null) {
			throw new IllegalArgumentException("Undefined context paramter: " + key);
		}
		return value;
	}

	/**
	 * Get context value.
	 *
	 * @param ctx The context to evaluate.
	 * @param key The key to retrieve.
	 * @param defaultValue The default value to return in case the key is not
	 *        defined in the context.
	 * @return The found value or the default value in case the key is not
	 *         defined in the context.
	 */
	protected String getContextString(Map<String, String> ctx, String key, String defaultValue) {
		return ctx.getOrDefault(key, defaultValue);
	}

	/**
	 * Format and write output data.
	 *
	 * @param out The {@code Writer} to write the output data to.
	 * @param template The template to use for formatting.
	 * @param arguments The arguments to use for formatting.
	 * @throws IOException if an I/O error occurs while writing the output data.
	 */
	protected void write(Writer out, MessageFormat template, Object... arguments) throws IOException {
		String outData = template.format(arguments);

		out.write(outData);
	}

}
