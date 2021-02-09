/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.properties.model;

import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.backend.TypeScriptModuleResolver;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.jangaroo.jooc.json.JsonObject.isIdentifier;

public class PropertiesClass {

  private static final Pattern RESOURCE_REFERENCE_PATTERN = Pattern.compile(
          "^\\s*Resource\\s*\\(\\s*(key|bundle)\\s*=\\s*['\"]([^'\"]*)['\"]\\s*,\\s*(key|bundle)\\s*=\\s*['\"]([^'\"]*)['\"]\\s*\\)\\s*$"
  );
  private static final String AS3_ANNOTATION_PATTERN = "(^|\\n)\\s*\\*\\s*(\\[[^]]*])";
  private static final String AS3_ANNOTATION_REPLACEMENT = "$1*/ $2 /*";

  private final ResourceBundleClass resourceBundle;
  private final Locale locale;
  private final PropertiesConfiguration properties;
  private final CompilationUnitResolver compilationUnitResolver;

  public PropertiesClass(ResourceBundleClass resourceBundle, Locale locale, PropertiesConfiguration properties, CompilationUnitResolver compilationUnitResolver) {
    this.resourceBundle = resourceBundle;
    this.locale = locale;
    this.properties = properties;
    this.compilationUnitResolver = compilationUnitResolver;
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
  public String getAs3Comment() {
    final String comment = getComment();
    if (comment == null) {
      return null;
    }
    return comment.replaceAll(AS3_ANNOTATION_PATTERN, AS3_ANNOTATION_REPLACEMENT);
  }

  /**
   * takes special care of AS3 annotations
   */
  public String getTsComment() {
    // for now the normal comment as we did not yet introduce decorators in TS
    return getComment();
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
      String tsValue = properties.getString(key);
      Matcher matcher = RESOURCE_REFERENCE_PATTERN.matcher(value);
      boolean valueIsResourceReference = matcher.find();
      if (valueIsResourceReference && includeReferences) {
        boolean bundleFirst = "bundle".equals(matcher.group(1));
        String referenceBundleKey = matcher.group(!bundleFirst ? 2 : 4);
        String referenceBundleFullClassName = matcher.group(bundleFirst ? 2 : 4);
        int dotPos = referenceBundleFullClassName.lastIndexOf('.');
        String className = dotPos > -1 ? referenceBundleFullClassName.substring(dotPos + 1) : referenceBundleFullClassName;
        value = referenceBundleFullClassName + CompilerUtils.PROPERTIES_CLASS_SUFFIX;
        tsValue = className + CompilerUtils.PROPERTIES_CLASS_SUFFIX;
        if (isIdentifier(referenceBundleKey)) {
          value += "." + referenceBundleKey;
          tsValue += "." + referenceBundleKey;
        } else {
          value += "[\"" + referenceBundleKey + "\"]";
          tsValue += "[\"" + referenceBundleKey + "\"]";
        }
      }
      if (valueIsResourceReference ? includeReferences : includeStrings) {
        props.add(new Property(adjustComment(layout.getCanonicalComment(key, true)), key, isIdentifier(key), value, tsValue, valueIsResourceReference));
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

  public Map<String, String> getTsImports() {
    Set<String> imports = getImports();
    if (locale != null) {
      // in TypeScript every module has to be imported, even if it is in the same folder/"package"
      imports.add(resourceBundle.getFullClassName());
    }
    TypeScriptModuleResolver typeScriptModuleResolver = new TypeScriptModuleResolver(compilationUnitResolver);
    return typeScriptModuleResolver.getDefaultImports(resourceBundle.getFullClassName(), imports).stream()
            .collect(Collectors.toMap(im -> im.localName, im -> im.source));
  }
}
