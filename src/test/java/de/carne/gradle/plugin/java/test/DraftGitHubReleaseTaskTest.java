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
package de.carne.gradle.plugin.java.test;

import java.util.Objects;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import de.carne.gradle.plugin.java.task.DraftGitHubReleaseTask;

/**
 * Test {@linkplain DraftGitHubReleaseTask} class.
 */
class DraftGitHubReleaseTaskTest extends TestProjectRunner {

	private static final String GITHUB_TOKEN_PROPERTY = "GITHUB_TOKEN";

	@EnabledIfSystemProperty(named = GITHUB_TOKEN_PROPERTY, matches = "true")
	@Test
	void testDraftGitHubReleaseTask() {
		String githubToken = Objects.requireNonNull(System.getenv(GITHUB_TOKEN_PROPERTY));

		BuildResult draftGitHubReleaseResult = run("-s", "-i", "-PgithubToken=" + githubToken, "clean",
				"draftGitHubRelease");

		assertTaskOutcome(draftGitHubReleaseResult, ":draftGitHubRelease", TaskOutcome.SUCCESS);
	}

}
