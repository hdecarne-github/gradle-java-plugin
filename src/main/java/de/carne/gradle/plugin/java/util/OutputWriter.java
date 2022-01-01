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
package de.carne.gradle.plugin.java.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class used to write output using a specific line separator and encoding.
 */
public class OutputWriter extends Writer {

	private final BufferedWriter out;
	private final char[] lineSeparator;

	/**
	 * Constructs {@linkplain OutputWriter}.
	 *
	 * @param file the {@linkplain File} to write to.
	 * @param append whether to append the output to the file.
	 * @param encoding the encoding to use.
	 * @param lineSeparator the line separator to use.
	 * @throws IOException if an I/O error occurs.
	 */
	@SuppressWarnings("resource")
	public OutputWriter(File file, boolean append, String encoding, String lineSeparator) throws IOException {
		this(new OutputStreamWriter(new FileOutputStream(file, append), Charset.forName(encoding)), lineSeparator);
	}

	/**
	 * Constructs {@linkplain OutputWriter}.
	 *
	 * @param out the {@linkplain Writer} to write to.
	 * @param lineSeparator the line separator to use.
	 * @throws IOException if an I/O error occurs.
	 */
	public OutputWriter(Writer out, String lineSeparator) throws IOException {
		this.out = (out instanceof BufferedWriter ? (BufferedWriter) out : new BufferedWriter(out));
		this.lineSeparator = lineSeparator.toCharArray();
	}

	@Override
	public void write(char @Nullable [] cbuf, int off, int len) throws IOException {
		Objects.requireNonNull(cbuf);

		int lastOff = off;
		int nextOff = off;
		int nextOffLimit = off + len;

		while (nextOff < nextOffLimit) {
			if (nextOff + 1 < nextOffLimit && cbuf[nextOff] == '\r' && cbuf[nextOff + 1] == '\n') {
				writeln(cbuf, lastOff, nextOff - lastOff);
				nextOff += 2;
				lastOff = nextOff;
			} else if (cbuf[nextOff] == '\n' || cbuf[nextOff] == '\r') {
				writeln(cbuf, lastOff, nextOff - lastOff);
				nextOff++;
				lastOff = nextOff;
			} else {
				nextOff++;
			}
		}
		this.out.write(cbuf, lastOff, nextOff - lastOff);
	}

	private void writeln(char[] cbuf, int off, int len) throws IOException {
		if (len > 0) {
			this.out.write(cbuf, off, len);
		}
		this.out.write(this.lineSeparator);
	}

	@Override
	public void flush() throws IOException {
		this.out.flush();
	}

	@Override
	public void close() throws IOException {
		this.out.close();
	}

}
