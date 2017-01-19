package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;

class CompilationUnitUtils {
  static boolean constructorSupportsConfigOptionsParameter(String classQName, CompilationUnitResolver resolver) {
    CompilationUnit compilationUnitModel = resolver.resolveCompilationUnit(classQName);
    ClassDeclaration classModel = (ClassDeclaration) compilationUnitModel.getPrimaryDeclaration();
    while (classModel != null) {
      if ("ext.Base".equals(classModel.getQualifiedNameStr())) {
        return true;
      }
      // Even though in ActionScript, no constructor means default constructor, we have the special
      // MXML semantics that a config-param-constructor is by default inherited.
      // Thus, continue to look into the superclass:
      classModel = classModel.getSuperTypeDeclaration();
    }
    return false;
  }

}
