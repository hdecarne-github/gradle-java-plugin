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
package de.carne.gradle.plugin.test.util;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

class SystemOutLogger implements Logger {

	private static final String TRACE_NAME = "TRACE";
	private static final String LIFECYCLE_NAME = "LIFECYCLE";

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
	public void trace(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
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
	public void debug(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
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
	public void info(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
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
	public void warn(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
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
	public void error(@Nullable Marker marker, @Nullable String format, @Nullable Object arg1, @Nullable Object arg2) {
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
	public boolean isLifecycleEnabled() {
		return true;
	}

	@Override
	public void debug(@Nullable String message, Object @Nullable... objects) {
		log(LogLevel.DEBUG.name(), message, objects);
	}

	@Override
	public void lifecycle(@Nullable String message) {
		log(LIFECYCLE_NAME, message);
	}

	@Override
	public void lifecycle(@Nullable String message, Object @Nullable... objects) {
		log(LIFECYCLE_NAME, message, objects);
	}

	@Override
	public void lifecycle(@Nullable String message, @Nullable Throwable throwable) {
		log(LIFECYCLE_NAME, message, throwable);
	}

	@Override
	public boolean isQuietEnabled() {
		return true;
	}

	@Override
	public void quiet(@Nullable String message) {
		log(LogLevel.QUIET.name(), message);
	}

	@Override
	public void quiet(@Nullable String message, Object @Nullable... objects) {
		log(LogLevel.QUIET.name(), message, objects);
	}

	@Override
	public void info(@Nullable String message, Object @Nullable... objects) {
		log(LogLevel.INFO.name(), message, objects);
	}

	@Override
	public void quiet(@Nullable String message, @Nullable Throwable throwable) {
		log(LogLevel.QUIET.name(), message, throwable);
	}

	@Override
	public boolean isEnabled(@Nullable LogLevel level) {
		return true;
	}

	@Override
	public void log(@Nullable LogLevel level, @Nullable String message) {
		log(Objects.toString(level), message);
	}

	@Override
	public void log(@Nullable LogLevel level, @Nullable String message, Object @Nullable... objects) {
		log(Objects.toString(level), MessageFormatter.arrayFormat(message, objects).getMessage());
	}

	@Override
	public void log(@Nullable LogLevel level, @Nullable String message, @Nullable Throwable throwable) {
		log(Objects.toString(level), message, throwable);
	}

	public void log(String level, @Nullable String message) {
		System.out.println(level + ": " + message);
	}

	public void log(String level, @Nullable String message, Object @Nullable... objects) {
		log(level, MessageFormatter.arrayFormat(message, objects).getMessage());
	}

	public void log(String level, @Nullable String message, @Nullable Throwable throwable) {
		log(level, message);
		if (throwable != null) {
			throwable.printStackTrace(System.out);
		}
	}

}
