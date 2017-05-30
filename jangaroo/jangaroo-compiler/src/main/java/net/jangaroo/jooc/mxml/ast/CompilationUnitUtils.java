package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Parameter;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.mxml.MxmlUtils;

import javax.annotation.Nonnull;

class CompilationUnitUtils {
  static boolean constructorSupportsConfigOptionsParameter(String classQName, CompilationUnitResolver resolver) {
    CompilationUnit compilationUnitModel = resolver.resolveCompilationUnit(classQName);
    return constructorSupportsConfigOptionsParameter(compilationUnitModel);
  }

  static boolean constructorSupportsConfigOptionsParameter(@Nonnull CompilationUnit compilationUnit) {
    return constructorSupportsConfigOptionsParameter((ClassDeclaration) compilationUnit.getPrimaryDeclaration());
  }

  static boolean constructorSupportsConfigOptionsParameter(@Nonnull ClassDeclaration classModel) {
    if (classModel != null) {
      FunctionDeclaration constructorModel = classModel.getConstructor();
      if (constructorModel != null) {
        Parameters constructorParams = constructorModel.getParams();
        if (constructorParams != null) {
          Parameter firstParam = constructorParams.getHead();
          if (MxmlUtils.CONFIG.equals(firstParam.getName())) {
            return true;
          }
        }
      }
    }
    return false;
  }

}
