/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties.model;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class PropertiesClass {

  private static final Pattern AS3_IDENTIFIER_PATTERN = Pattern.compile("(\\p{Alpha}|[$_])(\\p{Alnum}|[$_])*");
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
      props.add(new Property(adjustComment(layout.getCanonicalComment(key, true)), key, isIdentifier(key), properties.getString(key)));
    }
    return props;
  }

  private String adjustComment(String canonicalComment) {
    return canonicalComment == null ? null : canonicalComment.replaceAll("(^|\\n)#", "\n *");
  }

  private static boolean isIdentifier(String str) {
    return AS3_IDENTIFIER_PATTERN.matcher(str).matches();
  }

  public File getSrcFile() {
    return srcFile;
  }
}
