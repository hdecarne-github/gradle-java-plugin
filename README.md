### Gradle Java-Tools plugin
[![Download](https://api.bintray.com/packages/hdecarne/maven/java-gradle-plugins/images/download.svg)](https://bintray.com/hdecarne/maven/java-gradle-plugins/_latestVersion)
[![Build Status](https://travis-ci.com/hdecarne/java-gradle-plugins.svg?branch=master)](https://travis-ci.com/hdecarne/java-gradle-plugins)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=de.carne%3Ajava-gradle-plugins&metric=coverage)](https://sonarcloud.io/dashboard/index/de.carne:java-gradle-plugins)  

This project collects custom Gradle plugins used for the development of [my private projects](https://github.com/hdecarne/).

#### Plugin de.carne.java-tools
See [plugins.gradle.org](https://plugins.gradle.org/plugin/de.carne.java-tools) for how enable the plugin in your build script.

The fastest way is the Gradle plugin mechanism:
```Gradle
plugins {
	id 'de.carne.java-tools' version 'latest.version'
}
```
Check the badge above to determine the latest version of the plugin.

#### Task generateI18N
This task runs automatically before the __compileJava__ task, scans the source set for existing resource
bundles and generates access classes for them. The following default settings are used by this task.
```Gradle
javatools {
	generateI18N {
		enabled = true
		keyFilter = "^I18N_.*"
		genDir = file("src/main/java")
		bundles = fileTree("src/main/resources") {
			include "**/*I18N.properties"
		}
}
```
 * __enabled__: Set this to false to disable the task.
 * __keyFilter__: Java regular expression pattern identifying the resource keys to be evaluated by the task. Only resource keys matching this pattern are accessible via the generated class.
 * __genDir__: The target directory for the generated files.
 * __bundles__: The file tree object defining the resource bundles to be evaluated by the task.

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
Note that not for all keys access code have been created due to the task's __keyFilter__ property.
