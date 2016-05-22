## GradlePlugins
This project collects custom Gradle plugins used for the development of [my private projects](https://github.com/hdecarne/).

### Plugin de.carne.gradleplugins.java
Use this plugin with your java projects. It requires the java plugin to be applied first.
```Gradle
apply plugin: 'java'
apply plugin: 'de.carne.gradleplugins.java'
```
#### Task genI18N
This task runs automatically before the __compileJava__ task, scans the source set for existing resource bundles and generates access classes for them. The following default settings are used by this task.
```Gradle
javatools {
  genI18NSourceSet = 'main'
  genI18NInclude = '**/*I18N.properties'
  genI18NKeyFilter = '^STR_.*'
  genDir = 'src/main/java'
}
```
 * __genI18NSourceSet__: The name of the source set to scan for resource bundles.
 * __genI18NInclude__: The file pattern identifying the resource bundles to be evaluated by the task. (Set this to empty to disable the task.)
 * __genI18NKeyFilter__: Java regular expression pattern identifying the resource keys to be evaluated by the task. Only resource keys matching this pattern are accessible via the generated class.
 * __genDir__: The target directory for the generated files.

The __genI18N__ task scans the source set for any resource bundle matching the defined file pattern. For every found resource bundle it creates a Java class with same name as the resource bundle which can be used to access and format the resource strings. For example the resource bundle file:
```INI
STR_CERT_EXPORT_TITLE = Export certificate
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
Note that not for all keys access code has been created due to the task's __genI18NKeyFilter__ property.
