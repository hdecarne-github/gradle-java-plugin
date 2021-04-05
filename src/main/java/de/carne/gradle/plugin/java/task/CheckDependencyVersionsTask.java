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
package de.carne.gradle.plugin.java.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ComponentSelection;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.LenientConfiguration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.tasks.TaskAction;

import de.carne.gradle.plugin.java.util.ProjectLogger;

/**
 * CheckDependencyVersionsTask - Check for dependency version updates.
 */
public class CheckDependencyVersionsTask extends DefaultTask implements JavaToolsTask {

	private static final String CHECK_DEPENDENCY_VERSIONS_TASK_GROUP = "help";
	private static final String CHECK_DEPENDENCY_VERSIONS_TASK_NAME = "checkDependencyVersions";
	private static final String CHECK_DEPENDENCY_VERSIONS_TASK_DESCRIPTION = "Check for dependency version updates.";

	private static final String CHECK_BUILDSCRIPT_DEPENDENCY_VERSIONS_CONFIGURATION_NAME = "checkBuildscriptDependencyVersions";
	private static final String CHECK_PROJECT_DEPENDENCY_VERSIONS_CONFIGURATION_NAME = "checkProjectDependencyVersions";

	private static final String CHECK_BUILDSCRIPT_DEPENDENCY_VERSIONS_REPORT_TITLE = "Buildscript dependency version check result:";
	private static final String CHECK_PROJECT_DEPENDENCY_VERSIONS_REPORT_TITLE = "Project dependency version check result:";

	/**
	 * Creates the {@linkplain CheckDependencyVersionsTask}.
	 *
	 * @param project the {@linkplain Project} to create the task for.
	 * @return the created {@linkplain CheckDependencyVersionsTask} object.
	 */
	public static CheckDependencyVersionsTask create(Project project) {
		project.getLogger().info("Creating task " + CHECK_DEPENDENCY_VERSIONS_TASK_NAME);
		return project.getTasks().create(CHECK_DEPENDENCY_VERSIONS_TASK_NAME, CheckDependencyVersionsTask.class);
	}

	@Override
	public void apply(Project project) {
		setGroup(CHECK_DEPENDENCY_VERSIONS_TASK_GROUP);
		setDescription(CHECK_DEPENDENCY_VERSIONS_TASK_DESCRIPTION);
		project.getBuildscript().getConfigurations().create(CHECK_BUILDSCRIPT_DEPENDENCY_VERSIONS_CONFIGURATION_NAME)
				.setVisible(false).setTransitive(false);
		project.getConfigurations().create(CHECK_PROJECT_DEPENDENCY_VERSIONS_CONFIGURATION_NAME).setVisible(false)
				.setTransitive(false);
	}

	/**
	 * Executes {@linkplain CheckDependencyVersionsTask}.
	 */
	@TaskAction
	public void executeCheckDependencyVersions() {
		Project project = getProject();

		ProjectLogger.enterProject(project);
		try {
			executeCheckDependencyVersions(DependencyMap.fromBuildscript(project),
					Objects.requireNonNull(project.getBuildscript().getConfigurations()
							.findByName(CHECK_BUILDSCRIPT_DEPENDENCY_VERSIONS_CONFIGURATION_NAME)),
					new CheckDependencyVersionsReport(project, CHECK_BUILDSCRIPT_DEPENDENCY_VERSIONS_REPORT_TITLE));
			executeCheckDependencyVersions(DependencyMap.fromProject(project),
					Objects.requireNonNull(project.getConfigurations()
							.findByName(CHECK_PROJECT_DEPENDENCY_VERSIONS_CONFIGURATION_NAME)),
					new CheckDependencyVersionsReport(project, CHECK_PROJECT_DEPENDENCY_VERSIONS_REPORT_TITLE));
		} finally {
			ProjectLogger.leaveProject();
		}
	}

	private void executeCheckDependencyVersions(DependencyMap dependencyMap, Configuration cdvConfiguration,
			CheckDependencyVersionsReport report) {
		for (Map.Entry<DependencyKey, DependencyHolder> dependencyMapEntry : dependencyMap.entrySet()) {
			DependencyKey dependencyMapEntryKey = dependencyMapEntry.getKey();
			ArtifactVersionId dependencyArtifactVersionId = dependencyMapEntryKey.getArtifactVersionId();

			String dependencyArtifactIdString = dependencyArtifactVersionId.getArtifactId().toString();

			getProject().getLogger().info("Checking latest version for dependency {}...", dependencyArtifactIdString);

			String artifactDependencyString = dependencyArtifactIdString
					+ (dependencyArtifactVersionId.isSnapshot() ? ":latest.integration" : ":latest.release");
			Dependency artifactDependency = getProject().getDependencies().create(artifactDependencyString);

			cdvConfiguration.getDependencies().add(artifactDependency);
		}
		cdvConfiguration.resolutionStrategy(resolutionStrategy -> resolutionStrategy
				.componentSelection(componentSelectionRules -> componentSelectionRules
						.all(componentSelection -> filterComponentenSelection(dependencyMap, componentSelection))));

		LenientConfiguration resolvedCdvConfiguration = cdvConfiguration.getResolvedConfiguration()
				.getLenientConfiguration();
		Map<ArtifactId, ResolvedArtifact> resolvedArtifactsMap = buildResolvedArtifactsMap(
				resolvedCdvConfiguration.getArtifacts());

		evalDependencyVersions(dependencyMap, resolvedArtifactsMap, report);
	}

	private void evalDependencyVersions(DependencyMap dependencyMap,
			Map<ArtifactId, ResolvedArtifact> resolvedArtifactsMap, CheckDependencyVersionsReport report) {
		for (Map.Entry<DependencyKey, DependencyHolder> dependencyMapEntry : dependencyMap.entrySet()) {
			DependencyKey dependencyMapEntryKey = dependencyMapEntry.getKey();
			ArtifactVersionId dependencyArtifactVersionId = dependencyMapEntryKey.getArtifactVersionId();
			ResolvedArtifact resolvedArtifact = resolvedArtifactsMap.get(dependencyArtifactVersionId.getArtifactId());

			if (resolvedArtifact != null) {
				ArtifactVersionId resolvedArtifactVersionId = getResolvedArtifactVersionId(resolvedArtifact);

				if (dependencyArtifactVersionId.compareTo(resolvedArtifactVersionId) != 0) {
					report.reportDependencyMismatch(dependencyMapEntryKey.getProject(),
							dependencyMapEntryKey.getConfiguration(), dependencyArtifactVersionId,
							resolvedArtifactVersionId);
				}
			}
		}
	}

	private void filterComponentenSelection(DependencyMap dependencyMap, ComponentSelection componentSelection) {
		ArtifactVersionId candidateArtifactVersionId = getCandidateArtifactVersionId(componentSelection.getCandidate());

		if (dependencyMap.keySet().stream().map(DependencyKey::getArtifactVersionId).noneMatch(
				dependencyArtifactVersionId -> dependencyArtifactVersionId.isCandidate(candidateArtifactVersionId))) {
			componentSelection.reject("Ignoring candidate " + candidateArtifactVersionId);
		}
	}

	private Map<ArtifactId, ResolvedArtifact> buildResolvedArtifactsMap(Set<ResolvedArtifact> artifacts) {
		Map<ArtifactId, ResolvedArtifact> resolvedArtifactsMap = new HashMap<>();

		for (ResolvedArtifact artifact : artifacts) {
			resolvedArtifactsMap.put(getResolvedArtifactId(artifact), artifact);
		}
		return resolvedArtifactsMap;
	}

	private ArtifactId getResolvedArtifactId(ResolvedArtifact artifact) {
		ModuleVersionIdentifier artifactIdentifier = artifact.getModuleVersion().getId();

		return new ArtifactId(artifactIdentifier.getGroup(), artifactIdentifier.getName());
	}

	private ArtifactVersionId getResolvedArtifactVersionId(ResolvedArtifact artifact) {
		ModuleVersionIdentifier artifactIdentifier = artifact.getModuleVersion().getId();

		return new ArtifactVersionId(artifactIdentifier.getGroup(), artifactIdentifier.getName(),
				artifactIdentifier.getVersion());
	}

	private ArtifactVersionId getCandidateArtifactVersionId(ModuleComponentIdentifier candidate) {
		return new ArtifactVersionId(candidate.getGroup(), candidate.getModule(), candidate.getVersion());
	}

}
