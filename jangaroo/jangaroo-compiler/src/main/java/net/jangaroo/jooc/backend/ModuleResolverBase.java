package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.Annotation;

import java.util.Map;
import java.util.Objects;

public abstract class ModuleResolverBase {

  protected final CompilationUnitResolver compilationUnitModelResolver;

  public ModuleResolverBase(CompilationUnitResolver compilationUnitModelResolver) {
    this.compilationUnitModelResolver = compilationUnitModelResolver;
  }

  protected String getNativeAnnotationRequireValue(Annotation nativeAnnotation) {
    return (String) getAnnotationParameterValue(nativeAnnotation, Jooc.NATIVE_ANNOTATION_REQUIRE_PROPERTY, "");
  }

  public static String getNativeAnnotationValue(Annotation nativeAnnotation) {
    return (String) getAnnotationParameterValue(nativeAnnotation, null, null);
  }

  protected static Object getAnnotationParameterValue(Annotation nativeAnnotation, String name,
                                                      Object defaultValue) {
    Map<String, Object> propertiesByName = nativeAnnotation.getPropertiesByName();
    for (Map.Entry<String, Object> entry : propertiesByName.entrySet()) {
      if (Objects.equals(entry.getKey(), name)) {
        String stringValue = (String) entry.getValue();
        return stringValue == null ? defaultValue : stringValue;
      }
    }
    return null;
  }
}
