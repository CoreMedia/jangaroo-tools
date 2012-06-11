package net.jangaroo.jooc.model;

import net.jangaroo.utils.CompilerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A model of an ActionScript compilation unit.
 */
public class CompilationUnitModel implements ActionScriptModel {

  private String packageName = "";
  private List<String> imports = new ArrayList<String>();
  private NamedModel primaryDeclaration;

  public CompilationUnitModel() {
  }

  public CompilationUnitModel(String packageName) {
    this.packageName = packageName;
  }

  public CompilationUnitModel(String packageName, NamedModel primaryDeclaration) {
    this(packageName);
    this.primaryDeclaration = primaryDeclaration;
  }

  public String getPackage() {
    return packageName;
  }

  public void setPackage(String packageName) {
    this.packageName = packageName;
  }

  public String getQName() {
    return CompilerUtils.qName(packageName, primaryDeclaration.getName());
  }

  public void setQName(String qName) {
    packageName = CompilerUtils.packageName(qName);
    primaryDeclaration.setName(CompilerUtils.className(qName));
  }

  public NamedModel getPrimaryDeclaration() {
    return primaryDeclaration;
  }

  public void setPrimaryDeclaration(NamedModel primaryDeclaration) {
    this.primaryDeclaration = primaryDeclaration;
  }

  public void addImport(String qName) {
    if (qName != null && qName.contains(".") && !qName.contains("<") && !imports.contains(qName) && !getPackage().equals(CompilerUtils.packageName(qName))) {
      imports.add(qName);
    }
  }

  public List<String> getImports() {
    return Collections.unmodifiableList(imports);
  }

  public void addImplicitImports() {
    visit(new ModelVisitor() {
      @Override
      public void visitCompilationUnit(CompilationUnitModel compilationUnitModel) {
        compilationUnitModel.getPrimaryDeclaration().visit(this);
      }

      @Override
      public void visitClass(ClassModel classModel) {
        addImport(classModel.getSuperclass());
        for (String anInterface : classModel.getInterfaces()) {
          addImport(anInterface);
        }
        for (MemberModel memberModel : classModel.getMembers()) {
          memberModel.visit(this);
        }
      }

      @Override
      public void visitField(FieldModel fieldModel) {
        addImport(fieldModel.getType());
      }

      @Override
      public void visitProperty(PropertyModel propertyModel) {
        addImport(propertyModel.getType());
      }

      @Override
      public void visitMethod(MethodModel methodModel) {
        for (ParamModel paramModel : methodModel.getParams()) {
          paramModel.visit(this);
        }
        methodModel.getReturnModel().visit(this);
      }

      @Override
      public void visitParam(ParamModel paramModel) {
        addImport(paramModel.getType());
      }

      @Override
      public void visitReturn(ReturnModel returnModel) {
        addImport(returnModel.getType());
      }

      @Override
      public void visitNamespace(NamespaceModel namespaceModel) {
      }

      @Override
      public void visitAnnotation(AnnotationModel annotationModel) {
      }

      @Override
      public void visitAnnotationProperty(AnnotationPropertyModel annotationPropertyModel) {
      }
    });
  }

  @Override
  public void visit(ModelVisitor visitor) {
    visitor.visitCompilationUnit(this);
  }

  public ClassModel getClassModel() {
    return primaryDeclaration instanceof ClassModel ? (ClassModel)primaryDeclaration : null;
  }

}
