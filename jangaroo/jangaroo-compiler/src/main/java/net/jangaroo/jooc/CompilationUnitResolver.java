package net.jangaroo.jooc;

import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;

import javax.annotation.Nonnull;

/**
 * Base class for CompilationUniteModelResolvers with some convenience methods.
 */
public abstract class CompilationUnitResolver {

  @Nonnull
  public abstract CompilationUnit resolveCompilationUnit(@Nonnull String qName);

  public boolean implementsInterface(CompilationUnit classCompilationUnit, String anInterface) {
    return classCompilationUnit != null && implementsInterface((ClassDeclaration) classCompilationUnit.getPrimaryDeclaration(), anInterface);
  }

  public boolean implementsInterface(ClassDeclaration classDeclaration, String anInterface) {
    return classDeclaration.isAssignableTo((ClassDeclaration) resolveCompilationUnit(anInterface).getPrimaryDeclaration());
  }

  public CompilationUnit getSuperclassCompilationUnit(ClassDeclaration classModel) {
    ClassDeclaration superclass = classModel.getSuperTypeDeclaration();
    return superclass == null ? null : superclass.getCompilationUnit();
  }

}
