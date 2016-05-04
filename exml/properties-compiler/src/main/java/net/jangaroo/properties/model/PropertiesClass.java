/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties.model;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesClass {

  private static final Pattern AS3_IDENTIFIER_PATTERN = Pattern.compile("(\\p{Alpha}|[$_])(\\p{Alnum}|[$_])*");
  private static final Pattern RESOURCE_REFERENCE_PATTERN = Pattern.compile(
          "^\\s*Resource\\s*\\(\\s*(key|bundle)\\s*=\\s*['\"]([^'\"]*)['\"]\\s*,\\s*(key|bundle)\\s*=\\s*['\"]([^'\"]*)['\"]\\s*\\)\\s*$"
  );

  private ResourceBundleClass resourceBundle;
  private Locale locale;
  private PropertiesConfiguration properties;
  private File srcFile;

  public PropertiesClass(ResourceBundleClass resourceBundle, Locale locale, PropertiesConfiguration properties, File srcFile) {
    this.resourceBundle = resourceBundle;
    resourceBundle.addLocaleProperties(locale, this);

    this.locale = locale;
    this.properties = properties;
    this.srcFile = srcFile;
  }

  public ResourceBundleClass getResourceBundle() {
    return resourceBundle;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getComment() {
    return adjustComment(properties.getLayout().getCanonicalHeaderComment(true));
  }

  public List<Property> getProps() {
    PropertiesConfigurationLayout layout = properties.getLayout();
    List<Property> props = new ArrayList<Property>();
    Iterator keys = properties.getKeys();
    while (keys.hasNext()) {
      String key = (String)keys.next();
      String value = properties.getString(key);
      Matcher matcher = RESOURCE_REFERENCE_PATTERN.matcher(value);
      boolean valueIsResourceReference = matcher.find();
      if (valueIsResourceReference) {
        boolean bundleFirst = "bundle".equals(matcher.group(1));
        String referenceBundleKey = matcher.group(!bundleFirst ? 2 : 4);
        String referenceBundleFullClassName = matcher.group(bundleFirst ? 2 : 4);
        value = referenceBundleFullClassName + "_properties.INSTANCE";
        if (isIdentifier(referenceBundleKey)) {
          value += "." + referenceBundleKey;
        } else {
          value += "[\"" + referenceBundleKey + "\"]";
        }
      }
      props.add(new Property(adjustComment(layout.getCanonicalComment(key, true)), key, isIdentifier(key), value, !valueIsResourceReference));
    }
    return props;
  }

  private String adjustComment(String comment) {
    if (comment == null) {
      return null;
    }
    // take care for directives:
    String result = comment.replaceAll("(^|\\n)#([^]]*])", "\n*/ $1 /*");
    result = result.replaceAll("(^|\\n)#", "\n *");
    return result;
  }

  private static boolean isIdentifier(String str) {
    return AS3_IDENTIFIER_PATTERN.matcher(str).matches();
  }

  public File getSrcFile() {
    return srcFile;
  }

  public Set<String> getImports() {
    Set<String> result = new HashSet<String>();
    Iterator keys = properties.getKeys();
    while (keys.hasNext()) {
      String key = (String)keys.next();
      String value = properties.getString(key);
      Matcher matcher = RESOURCE_REFERENCE_PATTERN.matcher(value);
      if (matcher.find()) {
        String bundle = "bundle".equals(matcher.group(1)) ? matcher.group(2) : matcher.group(4);
        String referenceBundleFullClassName = bundle + "_properties";
        result.add(referenceBundleFullClassName);
      }
    }
    return result;
  }
}
