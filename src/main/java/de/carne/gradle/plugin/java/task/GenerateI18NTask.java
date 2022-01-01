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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

import de.carne.gradle.plugin.java.ext.GenerateI18N;
import de.carne.gradle.plugin.java.ext.JavaToolsExtension;
import de.carne.gradle.plugin.java.util.JavaOutput;
import de.carne.gradle.plugin.java.util.OutputWriter;
import de.carne.gradle.plugin.java.util.Plugins;
import de.carne.gradle.plugin.java.util.ProjectLogger;
import de.carne.gradle.plugin.java.util.Strings;

/**
 * GenerateI18NTask - Create/update I18N helper classes.
 */
public class GenerateI18NTask extends DefaultTask implements JavaToolsTask {

	private static final ResourceBundle TEMPLATES = ResourceBundle.getBundle(GenerateI18NTask.class.getName());

	private static final String GENERATE_I18N_TASK_GROUP = LifecycleBasePlugin.BUILD_GROUP;
	private static final String GENERATE_I18N_TASK_NAME = "generateI18N";
	private static final String GENERATE_I18N_TASK_DESCRIPTION = "Create/update I18N helper classes.";

	/**
	 * Creates the {@linkplain GenerateI18NTask}.
	 *
	 * @param project the {@linkplain Project} to create the task for.
	 * @return the created {@linkplain GenerateI18NTask} object.
	 */
	public static GenerateI18NTask create(Project project) {
		project.getLogger().info("Creating task " + GENERATE_I18N_TASK_NAME);
		return project.getTasks().create(GENERATE_I18N_TASK_NAME, GenerateI18NTask.class);
	}

	@Override
	public void apply(Project project) {
		setGroup(GENERATE_I18N_TASK_GROUP);
		setDescription(GENERATE_I18N_TASK_DESCRIPTION);
	}

	@Override
	public void afterEvaluate(Project project) {
		GenerateI18N generateI18N = project.getExtensions().getByType(JavaToolsExtension.class).getGenerateI18N();
		boolean enabled = generateI18N.isEnabled();

		setEnabled(enabled);
		if (enabled) {
			Plugins.checkJavaApplied(project);
			getInputs().files(generateI18N.getBundles());
			getOutputs().dir(generateI18N.getGenDir());
			processBundleFiles(generateI18N.getBundles(), (srcDir, bundleFile) -> {
				File javaFile = getAbsoluteFile(generateI18N.getGenDir(), getJavaFile(bundleFile));

				project.getLogger().info("{} output file {}", GENERATE_I18N_TASK_NAME, javaFile);
				getOutputs().file(javaFile);
			});
			Plugins.setTasksDependsOn(project, JavaCompile.class, this);
		}
	}

	/**
	 * Executes {@linkplain GenerateI18NTask}.
	 */
	@TaskAction
	public void executeGenerateI18N() {
		Project project = getProject();

		ProjectLogger.enterProject(project);
		try {
			GenerateI18N generateI18N = project.getExtensions().getByType(JavaToolsExtension.class).getGenerateI18N();
			Pattern keyFilter = Pattern.compile(generateI18N.getKeyFilter());

			processBundleFiles(generateI18N.getBundles(), (srcDir, bundleFile) -> {
				try {
					generateJavaFile(srcDir, bundleFile, generateI18N.getGenDir(), keyFilter,
							generateI18N.getEncoding(), generateI18N.getLineSeparator());
				} catch (IOException e) {
					throw new TaskExecutionException(this, e);
				}
			});
		} finally {
			ProjectLogger.leaveProject();
		}
	}

	private void processBundleFiles(ConfigurableFileTree fileTree, BiConsumer<File, File> consumer) {
		File srcDir = fileTree.getDir();

		fileTree.forEach(
				bundleFile -> consumer.accept(srcDir, getRelativeFile(srcDir, Objects.requireNonNull(bundleFile))));
	}

	private File getRelativeFile(File baseDir, File file) {
		Path baseDirPath = baseDir.toPath();
		Path filePath = file.toPath();

		return baseDirPath.relativize(filePath).toFile();
	}

	private File getAbsoluteFile(File baseDir, File file) {
		Path baseDirPath = baseDir.toPath();
		Path filePath = file.toPath();

		return baseDirPath.resolve(filePath).toAbsolutePath().toFile();
	}

	private File getJavaFile(File bundleFile) {
		File bundleFileParent = bundleFile.getParentFile();
		String bundleFileName = bundleFile.getName();
		int extensionIndex = bundleFileName.lastIndexOf('.');
		String javaFileName = (0 < extensionIndex && extensionIndex < bundleFileName.length()
				? bundleFileName.substring(0, extensionIndex) + ".java"
				: bundleFileName);

		return new File(bundleFileParent, javaFileName);
	}

	private void generateJavaFile(File srcDir, File bundleFile, File genDir, Pattern keyFilter, String encoding,
			String lineSeparator) throws IOException {
		File absoluteBundleFile = getAbsoluteFile(srcDir, bundleFile);
		File javaFile = getJavaFile(bundleFile);
		File absoluteJavaFile = getAbsoluteFile(genDir, javaFile);

		Files.createDirectories(absoluteJavaFile.toPath().getParent());
		try (Reader bundleReader = new FileReader(absoluteBundleFile);
				OutputWriter javaWriter = new OutputWriter(absoluteJavaFile, false, encoding, lineSeparator)) {
			Properties bundle = new Properties();

			bundle.load(bundleReader);

			List<String> bundleKeys = new ArrayList<>(bundle.stringPropertyNames());

			bundleKeys.sort(String::compareTo);
			generateJavaHeader(javaWriter, bundleFile, javaFile);
			for (String bundleKey : bundleKeys) {
				if (keyFilter.matcher(bundleKey).matches()) {
					String bundleString = Objects.requireNonNull(bundle.getProperty(bundleKey));

					generateJavaBody(javaWriter, bundleKey, bundleString);
				}
			}
			generateJavaFooter(javaWriter);
		}
	}

	private void generateJavaHeader(OutputWriter javaWriter, File bundleFile, File javaFile) throws IOException {
		javaWriter.write(TEMPLATES.getString("FILE_HEADER"));

		String javaPackage = Strings.safe(javaFile.getParent()).replace('/', '.').replace('\\', '.');
		String javaClass = javaFile.getName().replaceAll("\\..*", "");
		String normalizedBundleFile = bundleFile.toString().replace('\\', '/');

		if (Strings.notEmpty(javaPackage)) {
			javaWriter.write(MessageFormat.format(TEMPLATES.getString("PACKAGE_STATEMENT"), javaPackage));
		}
		javaWriter.write(MessageFormat.format(TEMPLATES.getString("CLASS_START"), normalizedBundleFile, javaClass));
	}

	private void generateJavaBody(OutputWriter javaWriter, String bundleKey, String bundleString) throws IOException {
		String mangledBundleKey = JavaOutput.mangleBundleKey(bundleKey);
		String encodedBundleString = JavaOutput.encodeBundleString(bundleString);

		javaWriter.write(MessageFormat.format(TEMPLATES.getString("CLASS_BODY"), bundleKey, mangledBundleKey,
				encodedBundleString));
	}

	private void generateJavaFooter(OutputWriter javaWriter) throws IOException {
		javaWriter.write(MessageFormat.format(TEMPLATES.getString("CLASS_END"), (Object) new Object[0]));
	}

}
