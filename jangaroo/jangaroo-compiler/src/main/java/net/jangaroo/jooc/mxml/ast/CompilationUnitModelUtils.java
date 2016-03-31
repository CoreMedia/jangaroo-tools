package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.CompilationUnitModelResolver;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.ParamModel;
import net.jangaroo.jooc.mxml.MxmlUtils;

import java.util.Iterator;

class CompilationUnitModelUtils {
  static boolean constructorSupportsConfigOptionsParameter(String classQName, CompilationUnitModelResolver resolver) {
    CompilationUnitModel compilationUnitModel = getCompilationUnitModel(classQName, resolver);
    ClassModel classModel = compilationUnitModel.getClassModel();
    if (classModel != null) {
      MethodModel constructorModel = classModel.getConstructor();
      if (constructorModel != null) {
        Iterator<ParamModel> constructorParams = constructorModel.getParams().iterator();
        if (constructorParams.hasNext()) {
          ParamModel firstParam = constructorParams.next();
          if (MxmlUtils.CONFIG.equals(firstParam.getName())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  static CompilationUnitModel getCompilationUnitModel(String fullClassName, CompilationUnitModelResolver resolver) {
    return resolver.resolveCompilationUnit(fullClassName);
  }
}
