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
package de.carne.gradleplugins.generate;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * {@code Generator} class for generating I18N resource string classes from an
 * existing {@code ResourceBundle}.
 */
public class JavaI18NGenerator extends JavaGenerator {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(JavaI18NGenerator.class.getName());

	private static final MessageFormat TEMPLATE_I18NCLASS_BEGIN = new MessageFormat(
			BUNDLE.getString("TEMPLATE_I18NCLASS_BEGIN"));

	private static final MessageFormat TEMPLATE_I18NCLASS_ENTRY = new MessageFormat(
			BUNDLE.getString("TEMPLATE_I18NCLASS_ENTRY"));

	private static final MessageFormat TEMPLATE_I18NCLASS_END = new MessageFormat(
			BUNDLE.getString("TEMPLATE_I18NCLASS_END"));

	private static final String FILE_TITLE = "I18N resource strings";

	/**
	 * The package name to use for generation.
	 */
	public static final String KEY_I18N_PACKAGE = "I18N_PACKAGE";

	/**
	 * The class name to use for generation.
	 */
	public static final String KEY_I18N_CLASS = "I18N_CLASS";

	/**
	 * The pattern to use for key filtering.
	 */
	public static final String KEY_I18N_KEY_FILTER = "I18N_KEY_FILTER";

	/**
	 * Construct {@code JavaI18NGenerator}.
	 *
	 * @param generationTimestamp See {@linkplain Generator#Generator(Date)}
	 */
	public JavaI18NGenerator(Date generationTimestamp) {
		super(generationTimestamp);
	}

	/*
	 * (non-Javadoc)
	 * @see de.carne.gradleplugins.generate.Generator#generate(java.util.Map,
	 * java.io.Reader, java.io.Writer)
	 */
	@Override
	public void generate(Map<String, String> ctx, Reader in, Writer out) throws IOException {
		String i18nPackage = getContextString(ctx, KEY_I18N_PACKAGE);
		String i18nClass = getContextString(ctx, KEY_I18N_CLASS);
		Pattern i18nKeyFilter = Pattern.compile(getContextString(ctx, KEY_I18N_KEY_FILTER));
		Properties i18nBundle = new Properties();

		i18nBundle.load(in);
		generateFileComment(out, getGenerationTimestamp(), FILE_TITLE);
		write(out, TEMPLATE_I18NCLASS_BEGIN, i18nPackage, i18nClass);
		for (Map.Entry<Object, Object> entry : i18nBundle.entrySet()) {
			String i18nKey = entry.getKey().toString();

			if (i18nKeyFilter.matcher(i18nKey).matches()) {
				String i18nValue = entry.getValue().toString();

				write(out, TEMPLATE_I18NCLASS_ENTRY, i18nKey, encodeComment(i18nValue));
			}
		}
		write(out, TEMPLATE_I18NCLASS_END);
	}

}
