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

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.carne.gradle.plugin.util.GitHubApi;
import de.carne.gradle.plugin.util.GitHubRepo;

/**
 * Test {@linkplain GitHubRepo} class.
 */
class GitHubRepoTest {

	private static final String TEST_TOKEN = Objects
			.requireNonNull(System.getProperty(GitHubRepoTest.class.getName() + ".TEST_TOKEN", ""));
	private static final boolean DIRTY_REPO = Boolean
			.valueOf(System.getProperty(GitHubRepoTest.class.getName() + ".DIRTY_REPO", "false")).booleanValue();

	private static final String TEST_RELASE_NAME = "v0.0." + System.currentTimeMillis() + "-SNAPSHOT";
	private static final File TEST_ASSET_FILE = new File("README.md");
	private static final String TEST_ASSET_NAME = TEST_ASSET_FILE.getName();
	private static final String TEST_ASSET_LABEL = "Release README";

	@Test
	void testReleaseApi() throws IOException {
		try (GitHubRepo githubRepo = new GitHubRepo(new SystemOutLogger(), ".", TEST_TOKEN)) {
			Assertions.assertEquals(DIRTY_REPO, githubRepo.isDirty());

			if (!"".equals(TEST_TOKEN)) {
				Assertions.assertNull(githubRepo.queryRelease(TEST_RELASE_NAME));

				GitHubApi.ReleaseInfo releaseInfo1 = githubRepo.draftRelease(TEST_RELASE_NAME,
						"<put description here>");
				GitHubApi.ReleaseInfo releaseInfo2 = githubRepo.queryRelease(TEST_RELASE_NAME);

				Assertions.assertEquals(releaseInfo1.id, releaseInfo2.id);
				Assertions.assertNotNull(releaseInfo1.id);
				Assertions.assertNotNull(releaseInfo1.uploadUrl);
				Assertions.assertTrue(releaseInfo1.draft);
				Assertions.assertTrue(releaseInfo1.prerelease);

				GitHubApi.ReleaseAssetInfo assetInfo = githubRepo.uploadReleaseAsset(
						Objects.requireNonNull(releaseInfo1.uploadUrl), TEST_ASSET_FILE, TEST_ASSET_NAME,
						TEST_ASSET_LABEL);

				Assertions.assertEquals(TEST_ASSET_NAME, assetInfo.name);
				Assertions.assertEquals(TEST_ASSET_LABEL, assetInfo.label);

				githubRepo.deleteRelease(Objects.requireNonNull(releaseInfo1.id));

				Assertions.assertNull(githubRepo.queryRelease(TEST_RELASE_NAME));
			}
		}
	}

}
