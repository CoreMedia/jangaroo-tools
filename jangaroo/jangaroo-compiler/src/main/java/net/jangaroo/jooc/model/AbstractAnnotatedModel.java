package net.jangaroo.jooc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for AnnotatedModels.
 */
public abstract class AbstractAnnotatedModel extends DocumentedModel implements AnnotatedModel {
  private List<AnnotationModel> annotations = new ArrayList<AnnotationModel>();

  protected AbstractAnnotatedModel() {
  }

  protected AbstractAnnotatedModel(String name) {
    super(name);
  }

  protected AbstractAnnotatedModel(String name, String asdoc) {
    super(name, asdoc);
  }

  @Override
  public List<AnnotationModel> getAnnotations() {
    return Collections.unmodifiableList(annotations);
  }

  @Override
  public void setAnnotations(List<AnnotationModel> annotations) {
    this.annotations = annotations;
  }

  @Override
  public void addAnnotation(AnnotationModel annotation) {
    annotations.add(annotation);
  }
}
