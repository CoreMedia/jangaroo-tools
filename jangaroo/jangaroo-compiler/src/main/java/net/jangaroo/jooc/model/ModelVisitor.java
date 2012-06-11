package net.jangaroo.jooc.model;

/**
 * A visitor for ActionScript models.
 */
public interface ModelVisitor {

  void visitClass(ClassModel classModel);

  void visitNamespace(NamespaceModel namespaceModel);

  void visitField(FieldModel fieldModel);

  void visitProperty(PropertyModel propertyModel);

  void visitMethod(MethodModel methodModel);

  void visitParam(ParamModel paramModel);

  void visitReturn(ReturnModel returnModel);

  void visitAnnotation(AnnotationModel annotationModel);

  void visitAnnotationProperty(AnnotationPropertyModel annotationPropertyModel);

  void visitCompilationUnit(CompilationUnitModel compilationUnitModel);
}
