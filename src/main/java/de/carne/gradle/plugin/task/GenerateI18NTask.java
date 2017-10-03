/*
 * Copyright (c) 2016-2017 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.gradle.plugin.task;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;

import de.carne.gradle.plugin.ext.GenerateI18N;
import de.carne.gradle.plugin.ext.JavaToolsExtension;
import de.carne.gradle.plugin.util.JavaOutput;
import de.carne.util.Strings;

/**
 * GenerateI18NTask - Create/update I18N helper classes.
 */
public class GenerateI18NTask extends DefaultTask implements JavaToolsTask {

	private static final ResourceBundle TEMPLATES = ResourceBundle.getBundle(GenerateI18NTask.class.getName());

	/**
	 * The task name.
	 */
	public static final String GENERATE_I18N_TASK_NAME = "generateI18N";

	private static final String GENERATE_I18N_TASK_DESCRIPTION = "Create/update I18N helper classes.";

	/**
	 * Create the {@linkplain GenerateI18NTask}.
	 *
	 * @param project The {@linkplain Project} to create the task for.
	 * @return The created {@linkplain GenerateI18NTask} object.
	 */
	public static GenerateI18NTask create(Project project) {
		return project.getTasks().create(GENERATE_I18N_TASK_NAME, GenerateI18NTask.class);
	}

	@Override
	public void apply(Project project) {
		setDescription(GENERATE_I18N_TASK_DESCRIPTION);
	}

	@Override
	public void afterEvaluate(Project project) {
		GenerateI18N generateI18N = project.getExtensions().getByType(JavaToolsExtension.class).getGenerateI18N();

		setEnabled(generateI18N.isEnabled());
		getInputs().files(generateI18N.getBundles());
		getOutputs().dir(generateI18N.getBundles().getDir());
		processBundleFiles(generateI18N.getBundles(), (srcDir, bundleFile) -> {
			File javaFile = getJavaFile(bundleFile);

			getOutputs().file(getAbsoluteFile(srcDir, javaFile));
		});
	}

	/**
	 * Execute {@linkplain GenerateI18NTask}.
	 */
	@TaskAction
	public void executeGenerateI18N() {
		Project project = getProject();
		GenerateI18N generateI18N = project.getExtensions().getByType(JavaToolsExtension.class).getGenerateI18N();
		Pattern keyFilter = Pattern.compile(generateI18N.getKeyFilter());

		processBundleFiles(generateI18N.getBundles(), (srcDir, bundleFile) -> {
			try {
				generateJavaFile(srcDir, bundleFile, generateI18N.getGenDir(), keyFilter);
			} catch (IOException e) {
				throw new TaskExecutionException(this, e);
			}
		});
	}

	private void processBundleFiles(ConfigurableFileTree fileTree, BiConsumer<File, File> consumer) {
		File srcDir = fileTree.getDir();

		fileTree.forEach(bundleFile -> consumer.accept(srcDir, getRelativeFile(srcDir, bundleFile)));
	}

	private File getRelativeFile(File baseDir, File file) {
		Path baseDirPath = baseDir.toPath();
		Path filePath = file.toPath();

		return baseDirPath.relativize(filePath).toFile();
	}

	private File getAbsoluteFile(File baseDir, File file) {
		Path baseDirPath = baseDir.toPath();
		Path filePath = file.toPath();

		return baseDirPath.resolve(filePath).toFile();
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

	private void generateJavaFile(File srcDir, File bundleFile, File genDir, Pattern keyFilter) throws IOException {
		File absoluteBundleFile = getAbsoluteFile(srcDir, bundleFile);
		File javaFile = getJavaFile(bundleFile);
		File absoluteJavaFile = getAbsoluteFile(genDir, javaFile);

		Files.createDirectories(absoluteJavaFile.toPath().getParent());
		try (Reader bundleReader = new FileReader(absoluteBundleFile);
				PrintWriter javaWriter = new PrintWriter(new FileWriter(absoluteJavaFile, false))) {
			Properties bundle = new Properties();

			bundle.load(bundleReader);

			List<String> bundleKeys = new ArrayList<>(bundle.stringPropertyNames());

			bundleKeys.sort(String::compareTo);
			generateJavaHeader(javaWriter, bundleFile, javaFile);
			for (String bundleKey : bundleKeys) {
				if (keyFilter.matcher(bundleKey).matches()) {
					String bundleString = bundle.getProperty(bundleKey);

					generateJavaBody(javaWriter, bundleKey, bundleString);
				}
			}
			generateJavaFooter(javaWriter, bundleFile, javaFile);
		}
	}

	private void generateJavaHeader(PrintWriter javaWriter, File bundleFile, File javaFile) {
		javaWriter.print(TEMPLATES.getString("FILE_HEADER"));

		String javaPackage = Strings.safe(javaFile.getParent()).replace('/', '.');
		String javaClass = javaFile.getName().replaceAll("\\..*", "");

		if (Strings.notEmpty(javaPackage)) {
			javaWriter.print(MessageFormat.format(TEMPLATES.getString("PACKAGE_STATEMENT"), javaPackage));
		}
		javaWriter.print(MessageFormat.format(TEMPLATES.getString("CLASS_START"), bundleFile, javaClass));
	}

	private void generateJavaBody(PrintWriter javaWriter, String bundleKey, String bundleString) {
		String mangledBundleKey = JavaOutput.mangleBundleKey(bundleKey);
		String encodedBundleString = JavaOutput.encodeBundleString(bundleString);

		javaWriter.print(MessageFormat.format(TEMPLATES.getString("CLASS_BODY"), bundleKey, mangledBundleKey,
				encodedBundleString));
	}

	private void generateJavaFooter(PrintWriter javaWriter, File bundleFile, File javaFile) {
		javaWriter.print(MessageFormat.format(TEMPLATES.getString("CLASS_END"), bundleFile, javaFile));
	}

}
