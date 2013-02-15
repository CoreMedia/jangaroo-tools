package net.jangaroo.jooc.model;

import net.jangaroo.jooc.backend.ActionScriptCodeGeneratingModelVisitor;
import net.jangaroo.utils.AS3Type;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A registry of all known classes/interfaces. Lookup by name.
 */
public class CompilationUnitModelRegistry {
  private Map<String,CompilationUnitModel> registry = new LinkedHashMap<String, CompilationUnitModel>(500);
  private final ActionScriptCodeGeneratingModelVisitor DEBUG_CODE_GENERATOR = new ActionScriptCodeGeneratingModelVisitor(new PrintWriter(System.err), true);

  public void register(CompilationUnitModel compilationUnitModel) {
    String qName = compilationUnitModel.getQName();
    if (registry.containsKey(qName)) {
      throw new IllegalArgumentException("Attempt to redefine " + qName);
    }
    registry.put(qName, compilationUnitModel);
  }

  public Collection<CompilationUnitModel> getCompilationUnitModels() {
    return registry.values();
  }

  // TODO: add built-in classes to registry's "classpath"!
  private static ClassModel createErrorClass() {
    ClassModel errorClass = new ClassModel("Error");
    errorClass.addMember(new MethodModel("Error", "Error", new ParamModel("msg", "String", "null")));
    errorClass.addMember(new MethodModel("toString", "String"));
    return errorClass;
  }
  private static final CompilationUnitModel ERROR_COMPILATION_UNIT = new CompilationUnitModel("", createErrorClass());

  public CompilationUnitModel resolveCompilationUnit(String qName) {
    if ("Error".equals(qName)) {
      return ERROR_COMPILATION_UNIT;
    }
    return registry.get(qName);
  }

  public MethodModel resolveConstructor(ClassModel classModel) {
    return resolveMethod(classModel, null, null);
  }

  private CompilationUnitModel resolveDefiningType(CompilationUnitModel classModel, MethodType methodType, String name) {
    CompilationUnitModel definingType = null;
    if (classModel != null) {
      definingType = resolveDefiningInterface(classModel, methodType, name);
      if (definingType == null) {
        definingType = resolveDefiningClass(classModel, methodType, name);
      }
    }
    return definingType;
  }

  private CompilationUnitModel resolveDefiningInterface(CompilationUnitModel compilationUnitModel, MethodType methodType, String methodName) {
    CompilationUnitModel definingInterface = null;
    ClassModel classModel = compilationUnitModel.getClassModel();
    CompilationUnitModel superclass = getSuperclassCompilationUnit(classModel);
    if (superclass != null) {
      definingInterface = resolveDefiningInterface(superclass, methodType, methodName);
    }
    // look in current class's interfaces:
    for (String interfaceName : classModel.getInterfaces()) {
      CompilationUnitModel anInterface = resolveCompilationUnit(interfaceName);
      if (anInterface == null) {
        System.err.println("CompilationUnitModelRegistry#resolveDefiningInterface: compilation unit for " + interfaceName + " not found.");
        continue;
      }
      CompilationUnitModel recursionResult = resolveDefiningInterface(anInterface, methodType, methodName);
      if (recursionResult != null && (definingInterface == null || implementsInterface(definingInterface.getClassModel(), recursionResult.getQName()))) {
        // found more general interface that defines the method:
        definingInterface = recursionResult;
      }
    }
    // only if nothin found yet, and current class is actually an interface, we bother to look locally:
    if (definingInterface == null && classModel.isInterface() && classModel.getMethod(methodType, methodName) != null) {
      definingInterface = compilationUnitModel;
    }
    return definingInterface;
  }

  private CompilationUnitModel resolveDefiningClass(CompilationUnitModel classCompilationUnit, MethodType methodType, String methodName) {
    CompilationUnitModel definingClass = null;
    CompilationUnitModel currentCompilationUnit = classCompilationUnit;
    while (currentCompilationUnit != null) {
      ClassModel classModel = currentCompilationUnit.getClassModel();
      MethodModel method = classModel.getMethod(methodType, methodName == null ? classModel.getName() : methodName);
      if (method != null) {
        definingClass = currentCompilationUnit;
        // don't return immediately, but search for root definition!
      }
      currentCompilationUnit = getSuperclassCompilationUnit(classModel);
    }
    return definingClass;
  }

  public MethodModel resolveMethod(ClassModel classModel, MethodType methodType, String methodName) {
    ClassModel currentClass = classModel;
    while (currentClass != null) {
      MethodModel method = currentClass.getMethod(methodType, methodName == null ? currentClass.getName() : methodName);
      if (method != null) {
        return method;
      }
      currentClass = getSuperclass(currentClass);
    }
    return null;
  }

  public void complementOverrides() {
    // add all missing implementations of interface methods:
    for (CompilationUnitModel compilationUnitModel : getCompilationUnitModels()) {
      ClassModel classModel = compilationUnitModel.getClassModel();
      if (classModel != null && !classModel.isInterface()) {
        ClassModel superclass = getSuperclass(classModel);
        for (String anInterface : classModel.getInterfaces()) {
          if (!implementsInterface(superclass, anInterface)) {
            CompilationUnitModel interfaceModel = resolveCompilationUnit(anInterface);
            for (MemberModel memberModel : interfaceModel.getClassModel().getMembers()) {
              if (memberModel.isMethod()) {
                addImplementingMethod(classModel, (MethodModel)memberModel);
              } else {
                PropertyModel propertyModel = (PropertyModel)memberModel;
                if (propertyModel.isReadable()) {
                  addImplementingMethod(classModel, propertyModel.getGetter());
                }
                if (propertyModel.isWritable()) {
                  addImplementingMethod(classModel, propertyModel.getSetter());
                }
              }
            }
          }
        }
      }
    }
    // add constructors and "override" flags where neccessary, and correct overriding methods' signatures:
    for (CompilationUnitModel compilationUnitModel : getCompilationUnitModels()) {
      ClassModel classModel = compilationUnitModel.getClassModel();
      if (classModel != null && getSuperclass(classModel) != null) {
        MethodModel constructor = complementConstructor(classModel);
        for (MemberModel memberModel : classModel.getMembers()) {
          if (memberModel != constructor && !memberModel.isField() && !memberModel.isStatic()) {
            if (memberModel.isMethod()) {
              complementOverridingMethod(compilationUnitModel, (MethodModel)memberModel);
            } else {
              complementOverridingProperty(compilationUnitModel, (PropertyModel)memberModel);
            }
          }
        }
      }
    }
  }

  private void addImplementingMethod(ClassModel classModel, MethodModel methodModel) {
    String memberName = methodModel.getName();
    if (resolveMethod(classModel, methodModel.getMethodType(), memberName) == null) {
      MethodModel implementingMethod = methodModel.duplicate();
      implementingMethod.setAsdoc("@inheritDoc");
      classModel.addMember(implementingMethod);
    }
  }

  private boolean implementsInterface(ClassModel classModel, String anInterface) {
    if (classModel == null) {
      return false;
    }
    if (classModel.isInterface() && anInterface.equals(classModel.getName())) {
      return true;
    }
    for (String interfaceName : classModel.getInterfaces()) {
      if (implementsInterface(resolveCompilationUnit(interfaceName).getClassModel(), anInterface)) {
        return true;
      }
    }
    return implementsInterface(getSuperclass(classModel), anInterface);
  }

  private MethodModel complementConstructor(ClassModel classModel) {
    if (classModel.isInterface()) {
      return null;
    }
    ClassModel superclass = getSuperclass(classModel);
    MethodModel constructor = classModel.getConstructor();
    MethodModel superclassConstructor = resolveConstructor(superclass);
    if (superclassConstructor != null) {
      List<ParamModel> params = superclassConstructor.getParams();
      StringBuilder superCallValues = new StringBuilder();
      for (ParamModel superParam : params) {
        if (superParam.isOptional()) {
          break;
        }
        if (superCallValues.length() > 0) {
          superCallValues.append(", ");
        }
        superCallValues.append(AS3Type.getDefaultValue(superParam.getType()));
      }
      if (constructor == null) {
        constructor = classModel.createConstructor();
        constructor.setAsdoc("@inheritDoc");
        // Usually, if there is no explicit constructor, all parameters are handed on to the super constructor.
        // However, since we also want to support using an empty parameter list, we set all parameters to "optional".
        for (ParamModel superParam : params) {
          ParamModel paramModel = superParam.duplicate();
          paramModel.setOptional(true);
          constructor.addParam(paramModel);
        }
      }
      constructor.setBody("super(" + superCallValues + ");");
    }
    if (constructor != null && constructor.getBody() == null) {
      // no super constructor found, generate simple super call body:
      constructor.setBody("super();");
    }
    return constructor;
  }

  private void complementOverridingMethod(CompilationUnitModel compilationUnitModel, MethodModel methodModel) {
    ClassModel classModel = compilationUnitModel.getClassModel();
    if (classModel == null) {
      return;
    }
    String methodName = methodModel.getName();
    MethodModel superclassMethod = resolveMethod(getSuperclass(classModel), methodModel.getMethodType(), methodName);
    if (superclassMethod != null) {
      methodModel.setOverride(true);
    }
    CompilationUnitModel definingType = resolveDefiningType(compilationUnitModel, methodModel.getMethodType(), methodName);
    if (!definingType.equals(compilationUnitModel)) {
      MethodModel methodSignature = definingType.getClassModel().getMethod(methodModel.getMethodType(), methodName);
      if (!methodSignature.equals(methodModel)) {
        // correct method signature:
        logMethodSignatureCorrection(definingType, methodSignature, compilationUnitModel, methodModel);
        methodModel.setParams(methodSignature.getParams()); // TODO: pull up optional parameters/non-void return type into signature?
        methodModel.setType(methodSignature.getType());
      }
    }
  }

  private void complementOverridingProperty(CompilationUnitModel compilationUnitModel, PropertyModel propertyModel) {
    if (propertyModel.isReadable()) {
      complementOverridingProperty(compilationUnitModel, propertyModel, MethodType.GET);
    }
    if (propertyModel.isWritable()) {
      complementOverridingProperty(compilationUnitModel, propertyModel, MethodType.SET);
    }
  }

  private void complementOverridingProperty(CompilationUnitModel compilationUnitModel, PropertyModel propertyModel, MethodType methodType) {
    ClassModel classModel = compilationUnitModel.getClassModel();
    String propertyName = propertyModel.getName();
    MethodModel superclassAccessor = resolveMethod(getSuperclass(classModel), methodType, propertyName);
    MethodModel accessor = propertyModel.getMethod(methodType);
    if (superclassAccessor != null) {
      accessor.setOverride(true);
    }
    CompilationUnitModel definingType = resolveDefiningType(compilationUnitModel, methodType, propertyName);
    if (!definingType.equals(compilationUnitModel)) {
      PropertyModel superPropertyModel = definingType.getClassModel().getProperty(propertyModel.isStatic(), propertyName);
      if (!accessor.getType().equals(superPropertyModel.getType())) {
        // correct property type:
        logMethodSignatureCorrection(definingType, definingType.getClassModel().getMethod(methodType, propertyName), compilationUnitModel, accessor);
        propertyModel.setType(superPropertyModel.getType());
      }
    }
  }

  private void logMethodSignatureCorrection(CompilationUnitModel definingType, MethodModel methodSignature, CompilationUnitModel classModel, MethodModel methodModel) {
    // skip logging if the following conditions hold (obviously, return type/params have just been forgotten in overriding method):
    // * return type matches OR is void while defined to be something else AND
    // * parameters are empty
    if ((methodModel.getType().equals(methodSignature.getType()) || "void".equals(methodModel.getType()))
      && methodModel.getParams().isEmpty()) {
      return;
    }
    System.err.println("*** corrected ERROR in " + methodSignature.getMethodType() + " signature of " + classModel.getQName() + "." + methodSignature.getName() + " deviates from definition in " + definingType.getQName() + ".");
    System.err.printf(" %s:%n", definingType.getQName());
    DEBUG_CODE_GENERATOR.setCompilationUnitModel(definingType);
    methodSignature.visit(DEBUG_CODE_GENERATOR);
    DEBUG_CODE_GENERATOR.flush();
    System.err.printf(" %s:%n", classModel.getQName());
    DEBUG_CODE_GENERATOR.setCompilationUnitModel(classModel);
    methodModel.visit(DEBUG_CODE_GENERATOR);
    DEBUG_CODE_GENERATOR.flush();
  }

  public CompilationUnitModel getSuperclassCompilationUnit(ClassModel classModel) {
    return resolveCompilationUnit(classModel.getSuperclass());
  }

  public ClassModel getSuperclass(ClassModel classModel) {
    CompilationUnitModel superclassCompilationUnit = getSuperclassCompilationUnit(classModel);
    return superclassCompilationUnit == null ? null : superclassCompilationUnit.getClassModel();
  }

  public void complementImports() {
    for (CompilationUnitModel compilationUnitModel : getCompilationUnitModels()) {
      compilationUnitModel.addImplicitImports();
    }
  }
}
