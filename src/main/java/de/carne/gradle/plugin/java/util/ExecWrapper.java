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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;

abstract class ExecWrapper {

	private final File directory;
	private final String command;

	protected ExecWrapper(File directory, String command) {
		this.directory = directory;
		this.command = command;
	}

	protected int executeCommand(ExecOut out, @Nullable String... args) throws IOException, InterruptedException {
		List<@Nullable String> processCommand = new ArrayList<>(1 + args.length);

		processCommand.add(this.command);
		processCommand.addAll(Arrays.asList(args));

		ProjectLogger.info("Executing command: {}", String.join(" ", processCommand));
		ProjectLogger.info("Working directory: {}", this.directory);

		Process process = new ProcessBuilder(processCommand).directory(this.directory).start();

		try (BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String processLine;

			while ((processLine = processReader.readLine()) != null) {
				out.accept(processLine);
			}
		}
		return process.waitFor();
	}

	static String executeVersionCommand(File directory, String command, @Nullable String... args)
			throws IOException, InterruptedException {
		ExecWrapper wrapper = new ExecWrapper(directory, command) {
			// nothing to do here
		};
		VersionOut version = new VersionOut();

		wrapper.executeCommand(version, args);
		return version.get();
	}

	private static class VersionOut implements ExecOut, Supplier<String> {

		@Nullable
		private String value = null;

		VersionOut() {
			// nothing to do here
		}

		@Override
		public void accept(String processLine) throws IOException {
			if (this.value != null) {
				throw new IOException("Unexpected process output: " + processLine);
			}
			this.value = processLine;
		}

		@Override
		public String get() {
			return Objects.requireNonNull(this.value);
		}

	}

}
