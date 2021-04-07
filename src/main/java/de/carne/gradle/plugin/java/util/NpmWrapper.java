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
package de.carne.gradle.plugin.java.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Utility class used to invoke the npm executable.
 */
public final class NpmWrapper extends ExecWrapper {

	private final String version;

	private NpmWrapper(File directory, String command, String version) {
		super(directory, command);
		this.version = version;
	}

	/**
	 * Gets a {@linkplain NpmWrapper} instance for the given parameters.
	 *
	 * @param directory the directory where to run npm.
	 * @param command the actual npm command to use.
	 * @return the created {@linkplain NpmWrapper} instance.
	 * @throws IOException if an I/O error occurs while probing the npm command.
	 * @throws InterruptedException if the thread is interrupted while probing the npm command.
	 */
	public static NpmWrapper getInstance(File directory, String command) throws IOException, InterruptedException {
		String version = ExecWrapper.executeVersionCommand(directory, command, "-v");

		return new NpmWrapper(directory, command, version);
	}

	/**
	 * Gets the wrapped npm version.
	 *
	 * @return the wrapped npm version.
	 */
	public String npmVersion() {
		return this.version;
	}

	/**
	 * Executes npm using the given arguments.
	 *
	 * @param logFile the {@linkplain File} to log the command output into.
	 * @param arguments the npm arguments to use.
	 * @return the command exit status.
	 * @throws IOException if an I/O error occurs while executing the npm command.
	 * @throws InterruptedException if the thread is interrupted while executing the npm command.
	 */
	public int executeNpm(File logFile, @Nullable String... arguments) throws IOException, InterruptedException {
		int status;

		try (NpmLog npmLog = new NpmLog(logFile)) {
			status = executeCommand(npmLog, arguments);
			npmLog.accept(">> status: " + status);
		}
		return status;
	}

	private static class NpmLog implements ExecOut, AutoCloseable {

		private final PrintStream log;

		NpmLog(File logFile) throws IOException {
			this.log = new PrintStream(Files.newOutputStream(logFile.toPath(), StandardOpenOption.WRITE,
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
		}

		@Override
		public void accept(String processLine) throws IOException {
			this.log.println(processLine);
		}

		@Override
		public void close() throws IOException {
			this.log.close();
		}

	}

}
