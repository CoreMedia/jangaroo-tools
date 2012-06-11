package net.jangaroo.jooc.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: fwienber Date: 11.06.12 Time: 12:22 To change this template use File | Settings |
 * File Templates.
 */
public interface AnnotatedModel extends ActionScriptModel {
  List<AnnotationModel> getAnnotations();

  void setAnnotations(List<AnnotationModel> annotations);

  void addAnnotation(AnnotationModel annotation);
}
