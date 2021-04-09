/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties.model;

import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.jangaroo.jooc.json.JsonObject.isIdentifier;

public class PropertiesClass {

  private static final Pattern RESOURCE_REFERENCE_PATTERN = Pattern.compile(
          "^\\s*Resource\\s*\\(\\s*(key|bundle)\\s*=\\s*['\"]([^'\"]*)['\"]\\s*,\\s*(key|bundle)\\s*=\\s*['\"]([^'\"]*)['\"]\\s*\\)\\s*$"
  );
  private static final Pattern AS3_ANNOTATION_PATTERN = Pattern.compile("\\s*\\*\\s*(\\[[^]]*])(\n|$)");

  private final ResourceBundleClass resourceBundle;
  private final Locale locale;
  private final PropertiesConfiguration properties;

  public PropertiesClass(ResourceBundleClass resourceBundle, Locale locale, PropertiesConfiguration properties) {
    this.resourceBundle = resourceBundle;
    this.locale = locale;
    this.properties = properties;
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

  /**
   * takes special care of AS3 annotations
   */
  public String getAnnotations() {
    final String comment = getComment();
    if (comment != null) {
      // extract all annotations:
      Matcher matcher = AS3_ANNOTATION_PATTERN.matcher(comment);
      List<String> annotations = new ArrayList<>();
      while (matcher.find()) {
        annotations.add(matcher.group(1));
      }
      if (!annotations.isEmpty()) {
        return String.join("\n", annotations);
      }
    }
    return null;
  }

  /**
   * takes special care of AS3 annotations
   */
  public String getAs3Comment() {
    final String comment = getComment();
    if (comment != null) {
      // remove all annotations:
      String withoutAnnotations = AS3_ANNOTATION_PATTERN.matcher(comment).replaceAll("");
      if (!withoutAnnotations.isEmpty()) {
        return withoutAnnotations;
      }
    }
    return null;
  }

  public List<Property> getProps() {
    return getProps(true, true);
  }

  private List<Property> getProps(boolean includeStrings, boolean includeReferences) {
    PropertiesConfigurationLayout layout = properties.getLayout();
    List<Property> props = new ArrayList<>();
    Iterator<String> keys = properties.getKeys();
    while (keys.hasNext()) {
      String key = keys.next();
      String value = properties.getString(key);
      Matcher matcher = RESOURCE_REFERENCE_PATTERN.matcher(value);
      boolean valueIsResourceReference = matcher.find();
      if (valueIsResourceReference && includeReferences) {
        boolean bundleFirst = "bundle".equals(matcher.group(1));
        String referenceBundleKey = matcher.group(!bundleFirst ? 2 : 4);
        String referenceBundleFullClassName = matcher.group(bundleFirst ? 2 : 4);
        value = referenceBundleFullClassName + CompilerUtils.PROPERTIES_CLASS_SUFFIX + ".INSTANCE";
        if (isIdentifier(referenceBundleKey)) {
          value += "." + referenceBundleKey;
        } else {
          value += "[\"" + referenceBundleKey + "\"]";
        }
      }
      if (valueIsResourceReference ? includeReferences : includeStrings) {
        props.add(new Property(adjustComment(layout.getCanonicalComment(key, true)), key, isIdentifier(key), value, valueIsResourceReference));
      }
    }
    return props;
  }

  public List<Property> getStringProps() {
    return getProps(true, false);
  }

  public List<Property> getReferenceProps() {
    return getProps(false, true);
  }

  private String adjustComment(String comment) {
    if (comment == null) {
      return null;
    }
    return comment.replaceAll("(^|\\n)#", "$1 *").replaceAll("(^|\\n)#", "$1 *");
  }

  public Set<String> getImports() {
    Set<String> result = new HashSet<>();
    Iterator<String> keys = properties.getKeys();
    while (keys.hasNext()) {
      String key = keys.next();
      String value = properties.getString(key);
      Matcher matcher = RESOURCE_REFERENCE_PATTERN.matcher(value);
      if (matcher.find()) {
        String bundle = "bundle".equals(matcher.group(1)) ? matcher.group(2) : matcher.group(4);
        String referenceBundleFullClassName = bundle + CompilerUtils.PROPERTIES_CLASS_SUFFIX;
        result.add(referenceBundleFullClassName);
      }
    }
    return result;
  }

}
