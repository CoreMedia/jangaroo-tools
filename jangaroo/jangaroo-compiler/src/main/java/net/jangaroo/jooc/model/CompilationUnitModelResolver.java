package net.jangaroo.jooc.model;

import javax.annotation.Nonnull;

/**
 * Base class for CompilationUniteModelResolvers with some convenience methods.
 */
public abstract class CompilationUnitModelResolver {

  @Nonnull
  public abstract CompilationUnitModel resolveCompilationUnit(@Nonnull String qName);

  public boolean implementsInterface(CompilationUnitModel classCompilationUnitModel, String anInterface) {
    if (classCompilationUnitModel == null) {
      return false;
    }
    ClassModel classModel = classCompilationUnitModel.getClassModel();
    if (classModel.isInterface() && anInterface.equals(classCompilationUnitModel.getQName())) {
      return true;
    }
    for (String interfaceName : classModel.getInterfaces()) {
      CompilationUnitModel compilationUnitModel = resolveCompilationUnit(interfaceName);
      if (compilationUnitModel == null) {
        throw new NullPointerException("AS3 compilation unit not found: " + interfaceName);
      }
      if (implementsInterface(compilationUnitModel, anInterface)) {
        return true;
      }
    }
    return implementsInterface(getSuperclassCompilationUnit(classModel), anInterface);
  }

  public CompilationUnitModel getSuperclassCompilationUnit(ClassModel classModel) {
    String superclass = classModel.getSuperclass();
    return superclass == null ? null : resolveCompilationUnit(superclass);
  }

  public ClassModel getSuperclass(ClassModel classModel) {
    CompilationUnitModel superclassCompilationUnit = getSuperclassCompilationUnit(classModel);
    return superclassCompilationUnit == null ? null : superclassCompilationUnit.getClassModel();
  }
}
