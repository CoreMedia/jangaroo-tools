package net.jangaroo.jooc.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A model of a field of an ActionScript class.
 */
public class AnnotationModel extends DocumentedModel {

  private List<AnnotationPropertyModel> properties = new ArrayList<AnnotationPropertyModel>();

  public AnnotationModel() {
  }

  public AnnotationModel(String name) {
    super(name);
  }

  public AnnotationModel(String name, AnnotationPropertyModel... properties) {
    super(name);
    this.properties = Arrays.asList(properties);
  }

  public List<AnnotationPropertyModel> getProperties() {
    return Collections.unmodifiableList(properties);
  }

  public Map<String, AnnotationPropertyModel> getPropertiesByName() {
    LinkedHashMap<String, AnnotationPropertyModel> result = new LinkedHashMap<String, AnnotationPropertyModel>();
    for (AnnotationPropertyModel property : properties) {
      result.put(property.getName(), property);
    }
    return Collections.unmodifiableMap(result);
  }

  public void setProperties(List<AnnotationPropertyModel> properties) {
    this.properties = properties;
  }

  public void addProperty(AnnotationPropertyModel annotationProperty) {
    properties.add(annotationProperty);
  }

  public void visit(ModelVisitor visitor) {
    visitor.visitAnnotation(this);
  }
}
