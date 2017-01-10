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
package de.carne.gradleplugins.java;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.DirectoryTree;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;
import org.gradle.api.tasks.TaskInputs;
import org.gradle.api.tasks.TaskOutputs;
import org.gradle.api.tasks.util.PatternSet;

import de.carne.gradleplugins.generate.JavaI18NGenerator;

/**
 * Generate I18N resource access classes from bundles.
 */
public class GenI18NTask extends DefaultTask {

	/**
	 * Task name.
	 */
	public static final String GEN_I18N_TASK_NAME = "genI18N";

	/**
	 * Task description.
	 */
	public static final String GEN_I18N_TASK_DESCRIPTION = "Generate I18N resource string classes";

	/**
	 * Helper class holding all necessary task context information.
	 */
	private class TaskContext {

		private JavaToolsExtension extension;
		private File genDir;
		private SourceSet sourceSet;
		private PatternSet pattern;

		TaskContext() {
			Project project = getProject();

			this.extension = JavaToolsExtension.get(project);
			this.genDir = new File(project.getBuildDir(), this.extension.getGenDir());
			this.sourceSet = JavaToolsPlugin.getSourceSet(project, this.extension.getGenI18NSourceSet());
			this.pattern = new PatternSet();
			this.pattern.include(this.extension.getGenI18NInclude());
		}

		public JavaToolsExtension extension() {
			return this.extension;
		}

		public File genDir() {
			return this.genDir;
		}

		public SourceSet sourceSet() {
			return this.sourceSet;
		}

		public PatternSet pattern() {
			return this.pattern;
		}

	}

	/**
	 * Prepare source dependencies.
	 */
	public void prepare() {
		TaskContext ctx = new TaskContext();

		ctx.sourceSet().getJava().srcDirs(new File(getProject().getBuildDir(), ctx.extension().getGenDir()));
	}

	/**
	 * Register inputs and outputs for incremental build support.
	 */
	public void prepareAfterEvaluate() {
		TaskContext ctx = new TaskContext();
		Map<File, File> genMap = determineGenMap(ctx);
		TaskInputs inputs = getInputs();
		TaskOutputs outputs = getOutputs();

		inputs.property(JavaToolsExtension.getGenDirProperty(), ctx.extension().getGenDir());
		inputs.property(JavaToolsExtension.getGenI18NSourceSetProperty(), ctx.extension().getGenI18NSourceSet());
		inputs.property(JavaToolsExtension.getGenI18NIncludeProperty(), ctx.extension().getGenI18NInclude());
		inputs.property(JavaToolsExtension.getGenI18NKeyFilterProperty(), ctx.extension().getGenI18NKeyFilter());
		outputs.dir(ctx.genDir());
		for (Map.Entry<File, File> genMapEntry : genMap.entrySet()) {
			File dstFile = genMapEntry.getKey();
			File srcFile = genMapEntry.getValue();

			inputs.file(srcFile);
			outputs.file(dstFile);
		}
	}

	/**
	 * Task entry point.
	 *
	 * @throws TaskExecutionException if an error occurs during task execution.
	 */
	@TaskAction
	public void runGenI18NTask() throws TaskExecutionException {
		TaskContext ctx = new TaskContext();
		Map<File, File> genMap = determineGenMap(ctx);
		JavaI18NGenerator generator = new JavaI18NGenerator(null, null);

		for (Map.Entry<File, File> genMapEntry : genMap.entrySet()) {
			HashMap<String, String> generatorCtx = new HashMap<>();
			File dstFile = genMapEntry.getKey();
			File srcFile = genMapEntry.getValue();

			if (!dstFile.exists() || dstFile.lastModified() <= srcFile.lastModified()) {
				getProject().getLogger().debug("Processing source file {}", srcFile);

				generatorCtx.put(JavaI18NGenerator.KEY_I18N_PACKAGE, getPackageFromFile(dstFile, ctx.genDir()));
				generatorCtx.put(JavaI18NGenerator.KEY_I18N_CLASS, getClassFromFile(dstFile));
				generatorCtx.put(JavaI18NGenerator.KEY_I18N_KEY_FILTER, ctx.extension().getGenI18NKeyFilter());

				try {
					Files.createDirectories(Paths.get(dstFile.getParent()));
				} catch (IOException e) {
					throw new TaskExecutionException(this, e);
				}
				try (FileReader in = new FileReader(srcFile); FileWriter out = new FileWriter(dstFile)) {
					generator.generate(generatorCtx, in, out);
				} catch (IOException e) {
					throw new TaskExecutionException(this, e);
				}
			} else {
				getProject().getLogger().debug("Skipping unchanged source file {}", srcFile);
			}
		}
	}

	private Map<File, File> determineGenMap(TaskContext ctx) {
		HashMap<File, File> genMap = new HashMap<>();
		Path genDirPath = ctx.genDir().toPath();

		for (DirectoryTree srcDirTree : ctx.sourceSet().getResources().getSrcDirTrees()) {
			File srcDir = getProject().file(srcDirTree.getDir());
			Path srcDirPath = srcDir.toPath();
			Set<File> srcFiles = getProject().fileTree(srcDir).matching(srcDirTree.getPatterns())
					.matching(ctx.pattern()).getFiles();

			for (File srcFile : srcFiles) {
				Path relSrcFilePath = srcDirPath.relativize(srcFile.toPath());
				File dstFile = genDirPath.resolve(relSrcFilePath.toString().replaceFirst("\\.properties$", ".java"))
						.toFile();

				if (!genMap.containsKey(dstFile)) {
					genMap.put(dstFile, srcFile);
				} else {
					getProject().getLogger().warn("Ignoring additional source file {} for {}:{}", srcFile, dstFile,
							genMap.get(dstFile));
				}
			}
		}
		return genMap;
	}

	private String getPackageFromFile(File file, File genDir) {
		Path relPackagePath = genDir.toPath().relativize(file.toPath().getParent());

		return relPackagePath.toString().replaceAll("/|\\\\", ".");
	}

	private String getClassFromFile(File file) {
		return file.getName().replaceFirst("\\..*$", "");
	}

}
