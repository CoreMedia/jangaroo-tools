package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.mxml.MxmlUtils;

class CompilationUnitUtils {
  static boolean constructorSupportsConfigOptionsParameter(String classQName, CompilationUnitResolver resolver) {
    CompilationUnit compilationUnitModel = resolver.resolveCompilationUnit(classQName);
    ClassDeclaration classModel = (ClassDeclaration) compilationUnitModel.getPrimaryDeclaration();
    while (classModel != null) {
      FunctionDeclaration constructorModel = classModel.getConstructor();
      if (constructorModel != null) {
        Parameters constructorParams = constructorModel.getParams();
        if (constructorParams != null) {
          Parameter firstParam = constructorParams.getHead();
          return MxmlUtils.CONFIG.equals(firstParam.getName());
        }
      }
      // Even though in ActionScript, no constructor means default constructor, we have the special
      // MXML semantics that a config-param-constructor is by default inherited.
      // Thus, continue to look into the superclass:
      classModel = classModel.getSuperTypeDeclaration();
    }
    return false;
  }

}
