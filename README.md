### Gradle Java-Tools plugin
[![Publication](https://img.shields.io/maven-central/v/de.carne/gradle-java-plugin)](https://search.maven.org/artifact/de.carne/gradle-java-plugin)
[![Build Status](https://travis-ci.com/hdecarne/gradle-java-plugin.svg?branch=master)](https://travis-ci.com/hdecarne/gradle-java-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=de.carne%3Agradle-java-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=de.carne%3Agradle-java-plugin)  

This project provides a custom made Gradle plugin used for the development of [my private Java based projects](https://github.com/hdecarne/).

The fastest way to apply this plugin to a project is the Gradle plugin mechanism:
```Gradle
plugins {
	id 'de.carne.java-tools' version 'latest.version'
}
```
Check the badge above to determine the latest version of the plugin.

See [plugins.gradle.org](https://plugins.gradle.org/plugin/de.carne.java-tools) for other ways to enable the plugin in your build script.

### Task generateI18N
This task runs automatically before the __compileJava__ task, scans the source set for existing resource bundles and generates access classes for them.
```Gradle
javatools {
	generateI18N {
		enabled = true // default: false
		keyFilter = "^I18N_.*" // default
		genDir = file("${buildDir}/generated-src/i18n/main/java") // default
		bundles = fileTree("src/main/resources") {
			include "**/*I18N.properties"
		} // default
		lineSeparator = "\n" // default: System.getProperty("line.separator")
		encoding = "ISO-8859-1" // default: System.getProperty("file.encoding","UTF-8")
	}

sourceSets {
	main {
		java {
			srcDir javatools.generateI18N.genDir
		}
	}
}

```
* __enabled__: Set this to false to disable the task.
* __keyFilter__: Java regular expression pattern identifying the resource keys to be evaluated by the task. Only resource keys matching this pattern are accessible via the generated class.
* __genDir__: The target directory for the generated files.
* __bundles__: The file tree object defining the resource bundles to be evaluated by the task.
* __lineSeparator__: The line separator to use for code generation.
* __encoding__: The encoding to use for code generation.

The __generateI18N__ task scans the source set for any resource bundle matching the defined file pattern. For every found resource bundle it creates a Java class with same name as the resource bundle which can be used to access and format the resource strings. For example the resource bundle file:
```INI
I18N_CERT_EXPORT_TITLE = Export certificate
_STR_EXPORT_BUTTON = Export
```
results in the Java code
```Java
/*
 * I18N resource strings
 *
 * Generated on May 1, 2016 3:00:58 PM
 */
package de.carne.certmgr.jfx.certexport;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Package localization resources.
 */
public final class I18N {

	/**
	 * The BUNDLE represented by this class.
	 */
	public static final ResourceBundle BUNDLE = ResourceBundle.getBundle(I18N.class.getName());

	private static String format(String key, Object... arguments) {
		String pattern = BUNDLE.getString(key);

		return (arguments.length > 0 ? MessageFormat.format(pattern, arguments) : pattern);
	}

	/**
	 * Resource key {@code STR_CERT_EXPORT_TITLE}
	 * <p>
	 * Export certificate
	 * </p>
	 */
	public static final String STR_CERT_EXPORT_TITLE = "STR_CERT_EXPORT_TITLE";

	/**
	 * Resource string {@code STR_CERT_EXPORT_TITLE}
	 * <p>
	 * Export certificate
	 * </p>
	 *
	 * @param arguments Format arguments.
	 * @return The formated string.
	 */
	public static String formatSTR_CERT_EXPORT_TITLE(Object... arguments) {
		return format(STR_CERT_EXPORT_TITLE, arguments);
	}

}
```
Note that not for all keys access code has been created due to the task's __keyFilter__ property.

### Task checkDependencyVersions
By running this task one can check whether any of the configured dependencies has a newer version available.
No configuration is needed to run this task. SNAPSHOT-versions are only considered as an update if the current version
is also a SNAPSOT-version.

### Task draftGitHubRelease
This tasks prepares a new GitHub release by uploading a configured set of artifacts as well as the accompanying release notes.
```Gradle
javatools {
	githubRelease {
		enabled = true // default: false
		releaseName = "v${project.version}" // default
		releaseNotes = file("./RELEASE-v${project.version}.md") // default
		releaseAssets = fileTree("build/libs") {
			include("*")
		} // default: undefined
		overwrite = true // default: false
		githubToken = project.findProperty('githubToken') // default: undefined
		ignoreDirty = true // default: false
	}
}

draftGitHubRelease.dependsOn(assemble)

```
* __enabled__: Set this to false to disable the task.
* __releaseName__: The release name to use.
* __releaseNotes__: The release notes to upload.
* __releaseAssets__: The release assets to upload.
* __overwrite__: Whether to overwrite an existing release with the same name. If set to false the task will fail if an identically named release already exists.
* __githubToken__: The GitHub API token to use to access the GitHub API.
* __ignoreDirty__: Whether to ignore a dirty workspace (contains uncommitted changes). If set to false the task will fail if there are any uncommitted changes in the repository.

The release is created in draft state and still has to be published afterwards e.g. via the GitHub web site.
