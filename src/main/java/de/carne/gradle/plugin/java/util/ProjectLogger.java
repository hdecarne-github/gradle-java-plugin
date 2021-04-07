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

import org.eclipse.jdt.annotation.Nullable;
import org.gradle.api.Project;
import org.gradle.api.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

/**
 * Utility class providing access to the currently executing project's logger.
 */
public final class ProjectLogger {

	private static final ThreadLocal<@Nullable Logger> LOGGER_HOLDER = new ThreadLocal<>();

	private static final Logger DEFAULT_LOGGER = new Logger() {

		private static final String TRACE_NAME = "TRACE";

		@Override
		public String getName() {
			return getClass().getSimpleName();
		}

		@Override
		public boolean isTraceEnabled() {
			return true;
		}

		@Override
		public void trace(@Nullable String msg) {
			log(TRACE_NAME, msg);
		}

		@Override
		public void trace(@Nullable String format, @Nullable Object arg) {
			log(TRACE_NAME, format, arg);
		}

		@Override
		public void trace(@Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
			log(TRACE_NAME, format, arg1, arg2);
		}

		@Override
		public void trace(@Nullable String format, Object @Nullable... arguments) {
			log(TRACE_NAME, format, arguments);
		}

		@Override
		public void trace(@Nullable String msg, @Nullable Throwable t) {
			log(TRACE_NAME, msg, t);
		}

		@Override
		public boolean isTraceEnabled(@Nullable Marker marker) {
			return true;
		}

		@Override
		public void trace(@Nullable Marker marker, @Nullable String msg) {
			log(TRACE_NAME, msg);
		}

		@Override
		public void trace(@Nullable Marker marker, @Nullable String format, @Nullable Object arg) {
			log(TRACE_NAME, format, arg);
		}

		@Override
		public void trace(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1,
				@Nullable Object arg2) {
			log(TRACE_NAME, format, arg1, arg2);
		}

		@Override
		public void trace(@Nullable Marker marker, @Nullable String format, Object @Nullable... argArray) {
			log(TRACE_NAME, format, argArray);
		}

		@Override
		public void trace(@Nullable Marker marker, @Nullable String msg, @Nullable Throwable t) {
			log(TRACE_NAME, msg, t);
		}

		@Override
		public boolean isDebugEnabled() {
			return true;
		}

		@Override
		public void debug(@Nullable String msg) {
			log(LogLevel.DEBUG.name(), msg);
		}

		@Override
		public void debug(@Nullable String format, @Nullable Object arg) {
			log(LogLevel.DEBUG.name(), format, arg);
		}

		@Override
		public void debug(@Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
			log(LogLevel.DEBUG.name(), format, arg1, arg2);
		}

		@Override
		public void debug(@Nullable String msg, @Nullable Throwable t) {
			log(LogLevel.DEBUG.name(), msg, t);
		}

		@Override
		public boolean isDebugEnabled(@Nullable Marker marker) {
			return true;
		}

		@Override
		public void debug(@Nullable Marker marker, @Nullable String msg) {
			log(LogLevel.DEBUG.name(), msg);
		}

		@Override
		public void debug(@Nullable Marker marker, @Nullable String format, @Nullable Object arg) {
			log(LogLevel.DEBUG.name(), format, arg);
		}

		@Override
		public void debug(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1,
				@Nullable Object arg2) {
			log(LogLevel.DEBUG.name(), format, arg1, arg2);
		}

		@Override
		public void debug(@Nullable Marker marker, @Nullable String format, Object @Nullable... arguments) {
			log(LogLevel.DEBUG.name(), format, arguments);
		}

		@Override
		public void debug(@Nullable Marker marker, @Nullable String msg, @Nullable Throwable t) {
			log(LogLevel.DEBUG.name(), msg, t);
		}

		@Override
		public boolean isInfoEnabled() {
			return true;
		}

		@Override
		public void info(@Nullable String msg) {
			log(LogLevel.INFO.name(), msg);
		}

		@Override
		public void info(@Nullable String format, @Nullable Object arg) {
			log(LogLevel.INFO.name(), format, arg);
		}

		@Override
		public void info(@Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
			log(LogLevel.INFO.name(), format, arg1, arg2);
		}

		@Override
		public void info(@Nullable String msg, @Nullable Throwable t) {
			log(LogLevel.INFO.name(), msg, t);
		}

		@Override
		public boolean isInfoEnabled(@Nullable Marker marker) {
			return true;
		}

		@Override
		public void info(@Nullable Marker marker, @Nullable String msg) {
			log(LogLevel.INFO.name(), msg);
		}

		@Override
		public void info(@Nullable Marker marker, @Nullable String format, @Nullable Object arg) {
			log(LogLevel.INFO.name(), format, arg);
		}

		@Override
		public void info(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1,
				@Nullable Object arg2) {
			log(LogLevel.INFO.name(), format, arg1, arg2);
		}

		@Override
		public void info(@Nullable Marker marker, @Nullable String format, Object @Nullable... arguments) {
			log(LogLevel.INFO.name(), format, arguments);
		}

		@Override
		public void info(@Nullable Marker marker, @Nullable String msg, @Nullable Throwable t) {
			log(LogLevel.INFO.name(), msg, t);
		}

		@Override
		public boolean isWarnEnabled() {
			return true;
		}

		@Override
		public void warn(@Nullable String msg) {
			log(LogLevel.WARN.name(), msg);
		}

		@Override
		public void warn(@Nullable String format, @Nullable Object arg) {
			log(LogLevel.WARN.name(), format, arg);
		}

		@Override
		public void warn(@Nullable String format, Object @Nullable... arguments) {
			log(LogLevel.WARN.name(), format, arguments);
		}

		@Override
		public void warn(@Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
			log(LogLevel.WARN.name(), format, arg1, arg2);
		}

		@Override
		public void warn(@Nullable String msg, @Nullable Throwable t) {
			log(LogLevel.WARN.name(), msg, t);
		}

		@Override
		public boolean isWarnEnabled(@Nullable Marker marker) {
			return true;
		}

		@Override
		public void warn(@Nullable Marker marker, @Nullable String msg) {
			log(LogLevel.WARN.name(), msg);
		}

		@Override
		public void warn(@Nullable Marker marker, @Nullable String format, @Nullable Object arg) {
			log(LogLevel.WARN.name(), format, arg);
		}

		@Override
		public void warn(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1,
				@Nullable Object arg2) {
			log(LogLevel.WARN.name(), format, arg1, arg2);
		}

		@Override
		public void warn(@Nullable Marker marker, @Nullable String format, Object @Nullable... arguments) {
			log(LogLevel.WARN.name(), format, arguments);
		}

		@Override
		public void warn(@Nullable Marker marker, @Nullable String msg, @Nullable Throwable t) {
			log(LogLevel.WARN.name(), msg, t);
		}

		@Override
		public boolean isErrorEnabled() {
			return true;
		}

		@Override
		public void error(@Nullable String msg) {
			log(LogLevel.ERROR.name(), msg);
		}

		@Override
		public void error(@Nullable String format, @Nullable Object arg) {
			log(LogLevel.ERROR.name(), format, arg);
		}

		@Override
		public void error(@Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
			log(LogLevel.ERROR.name(), format, arg1, arg2);
		}

		@Override
		public void error(@Nullable String format, Object @Nullable... arguments) {
			log(LogLevel.ERROR.name(), format, arguments);
		}

		@Override
		public void error(@Nullable String msg, @Nullable Throwable t) {
			log(LogLevel.ERROR.name(), msg, t);
		}

		@Override
		public boolean isErrorEnabled(@Nullable Marker marker) {
			return true;
		}

		@Override
		public void error(@Nullable Marker marker, @Nullable String msg) {
			log(LogLevel.ERROR.name(), msg);
		}

		@Override
		public void error(@Nullable Marker marker, @Nullable String format, @Nullable Object arg) {
			log(LogLevel.ERROR.name(), format, arg);
		}

		@Override
		public void error(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1,
				@Nullable Object arg2) {
			log(LogLevel.ERROR.name(), format, arg1, arg2);
		}

		@Override
		public void error(@Nullable Marker marker, @Nullable String format, Object @Nullable... arguments) {
			log(LogLevel.ERROR.name(), format, arguments);
		}

		@Override
		public void error(@Nullable Marker marker, @Nullable String msg, @Nullable Throwable t) {
			log(LogLevel.ERROR.name(), msg, t);
		}

		@Override
		public void debug(@Nullable String message, Object @Nullable... objects) {
			log(LogLevel.DEBUG.name(), message, objects);
		}

		@Override
		public void info(@Nullable String message, Object @Nullable... objects) {
			log(LogLevel.INFO.name(), message, objects);
		}

		@SuppressWarnings("java:S106")
		public void log(String level, @Nullable String message) {
			System.out.println(level + ": " + message);
		}

		public void log(String level, @Nullable String message, Object @Nullable... objects) {
			log(level, MessageFormatter.arrayFormat(message, objects).getMessage());
		}

		@SuppressWarnings("java:S106")
		public void log(String level, @Nullable String message, @Nullable Throwable throwable) {
			log(level, message);
			if (throwable != null) {
				throwable.printStackTrace(System.out);
			}
		}
	};

	private ProjectLogger() {
		// Prevent instantiation
	}

	/**
	 * Activates a project context for the current thread.
	 *
	 * @param project the currently executing project.
	 * @see #leaveProject()
	 */
	public static void enterProject(Project project) {
		LOGGER_HOLDER.set(project.getLogger());
	}

	/**
	 * Deactivates a previously set project context.
	 *
	 * @see #enterProject(Project)
	 */
	public static void leaveProject() {
		LOGGER_HOLDER.remove();
	}

	/**
	 * Logs a message on error level.
	 *
	 * @param format the message format string.
	 * @param arguments the message format arguments.
	 */
	public static void error(String format, Object... arguments) {
		getLogger().error(format, arguments);
	}

	/**
	 * Logs an exception message on error level.
	 *
	 * @param message the message to log.
	 * @param exception the exception to log.
	 */
	public static void error(String message, Throwable exception) {
		getLogger().error(message, exception);
	}

	/**
	 * Logs a message on warn level.
	 *
	 * @param format the message format string.
	 * @param arguments the message format arguments.
	 */
	public static void warn(String format, Object... arguments) {
		getLogger().warn(format, arguments);
	}

	/**
	 * Logs an exception message on warn level.
	 *
	 * @param message the message to log.
	 * @param exception the exception to log.
	 */
	public static void warn(String message, Throwable exception) {
		getLogger().warn(message, exception);
	}

	/**
	 * Logs a message on info level.
	 *
	 * @param format the message format string.
	 * @param arguments the message format arguments.
	 */
	public static void info(String format, Object... arguments) {
		getLogger().info(format, arguments);
	}

	/**
	 * Logs an exception message on info level.
	 *
	 * @param message the message to log.
	 * @param exception the exception to log.
	 */
	public static void info(String message, Throwable exception) {
		getLogger().info(message, exception);
	}

	/**
	 * Logs a message on debug level.
	 *
	 * @param format the message format string.
	 * @param arguments the message format arguments.
	 */
	public static void debug(String format, Object... arguments) {
		getLogger().debug(format, arguments);
	}

	/**
	 * Logs an exception message on debug level.
	 *
	 * @param message the message to log.
	 * @param exception the exception to log.
	 */
	public static void debug(String message, Throwable exception) {
		getLogger().debug(message, exception);
	}

	/**
	 * Logs a message on trace level.
	 *
	 * @param format the message format string.
	 * @param arguments the message format arguments.
	 */
	public static void trace(String format, Object... arguments) {
		getLogger().trace(format, arguments);
	}

	/**
	 * Logs an exception message on trace level.
	 *
	 * @param message the message to log.
	 * @param exception the exception to log.
	 */
	public static void trace(String message, Throwable exception) {
		getLogger().trace(message, exception);
	}

	private static Logger getLogger() {
		Logger logger = LOGGER_HOLDER.get();

		return (logger != null ? logger : DEFAULT_LOGGER);
	}

}
