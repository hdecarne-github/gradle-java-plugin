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
package de.carne.gradle.plugin.java.task;

import org.gradle.api.Project;

/**
 * Common interface for this plugin's tasks.
 */
public interface JavaToolsTask {

	/**
	 * Performs the necessary apply actions.
	 *
	 * @param project the {@linkplain Project} running the task.
	 * @see org.gradle.api.Plugin#apply(Object)
	 */
	void apply(Project project);

	/**
	 * Performs the necessary afterEvalute actions.
	 *
	 * @param project the {@linkplain Project} running the task.
	 * @see Project#afterEvaluate(org.gradle.api.Action)
	 */
	default void afterEvaluate(Project project) {
		// Do nothing by default
	}

}
