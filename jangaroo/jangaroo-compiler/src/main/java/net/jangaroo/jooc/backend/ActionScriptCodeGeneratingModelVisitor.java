package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.FieldModel;
import net.jangaroo.jooc.model.MemberModel;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.ModelVisitor;
import net.jangaroo.jooc.model.ParamModel;
import net.jangaroo.jooc.model.PropertyModel;
import net.jangaroo.jooc.model.ReturnModel;
import net.jangaroo.jooc.model.TypedModel;
import net.jangaroo.jooc.model.ValuedModel;
import net.jangaroo.jooc.model.Visibility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A ClassModel visitor that generates ActionScript (API) code.
 */
public class ActionScriptCodeGeneratingModelVisitor implements ModelVisitor {
  private final PrintWriter output;
  private CompilationUnitModel compilationUnitModel;
  private boolean skipAsDoc;

  public ActionScriptCodeGeneratingModelVisitor(Writer writer) {
    output = writer instanceof PrintWriter ? (PrintWriter)writer : new PrintWriter(writer);
  }

  public ActionScriptCodeGeneratingModelVisitor(Writer writer, boolean skipAsDoc) {
    this(writer);
    this.skipAsDoc = skipAsDoc;
  }

  public void setCompilationUnitModel(CompilationUnitModel compilationUnitModel) {
    this.compilationUnitModel = compilationUnitModel;
  }

  @Override
  public void visitCompilationUnit(CompilationUnitModel compilationUnitModel) {
    setCompilationUnitModel(compilationUnitModel);
    output.printf("package %s {%n", compilationUnitModel.getPackage());
    for (String anImport : compilationUnitModel.getImports()) {
      output.printf("import %s;%n", anImport);
    }
    output.println();
    compilationUnitModel.getPrimaryDeclaration().visit(this);
    output.print("}");
    output.close();
  }

  @Override
  public void visitClass(ClassModel classModel) {
    printAsdoc(classModel.getAsdoc(), "");
    for (AnnotationModel annotation : classModel.getAnnotations()) {
      annotation.visit(this);
    }
    output.printf("%s %s %s", classModel.getVisibility(), classModel.isInterface() ? "interface" : "class", classModel.getName());
    if (!classModel.isInterface() && !isEmpty(classModel.getSuperclass())) {
      output.printf(" extends %s", classModel.getSuperclass());
    }
    String separator = classModel.isInterface() ? " extends" : " implements";
    for (String anInterface : classModel.getInterfaces()) {
      output.printf("%s %s", separator, anInterface);
      separator = ", ";
    }
    output.print(" {");
    for (MemberModel member : classModel.getMembers()) {
      output.println();
      member.visit(this);
    }
    output.println("}");
  }

  public void flush() {
    output.flush();
  }

  @Override
  public void visitField(FieldModel fieldModel) {
    printAsdoc(fieldModel.getAsdoc(), "  ");
    output.print("  ");
    generateVisibility(fieldModel);
    generateStatic(fieldModel);
    output.printf("%s %s", fieldModel.isConst() ? "const" : "var", fieldModel.getName());
    generateType(fieldModel);
    generateValue(fieldModel);
    output.println(";");
  }

  @Override
  public void visitProperty(PropertyModel propertyModel) {
    if (propertyModel.isReadable()) {
      visitMethod(propertyModel.getGetter());
    }
    if (propertyModel.isWritable()) {
      if (propertyModel.isReadable()) {
        // an empty line between getter and setter:
        output.println();
      }
      visitMethod(propertyModel.getSetter());
    }
  }

  private static List<String> PARAM_SUPPRESSING_ASDOC_TAGS = Arrays.asList("@inheritDoc", "@private");

  @Override
  public void visitMethod(MethodModel methodModel) {
    StringBuilder asdoc = new StringBuilder();
    if (methodModel.getAsdoc() != null) {
      asdoc.append(methodModel.getAsdoc());
    }
    if (!PARAM_SUPPRESSING_ASDOC_TAGS.contains(asdoc.toString())) {
      for (ParamModel paramModel : methodModel.getParams()) {
        if (!isEmpty(paramModel.getAsdoc())) {
          asdoc.append("\n  @param ").append(paramModel.getName()).append(" ").append(paramModel.getAsdoc());
        }
      }
      String returnAsDoc = methodModel.getReturnModel().getAsdoc();
      if (!isEmpty(returnAsDoc)) {
        asdoc.append("\n  @return ").append(returnAsDoc);
      }
    }
    printAsdoc(asdoc.toString(), "  ");
    output.print("  ");
    generateOverride(methodModel.isOverride());
    generateVisibility(methodModel);
    generateStatic(methodModel);
    output.print("function ");
    if (methodModel.getMethodType() != null) {
      output.printf("%s ", methodModel.getMethodType());
    }
    output.printf("%s(", methodModel.getName());
    String separator = "";
    for (ParamModel paramModel : methodModel.getParams()) {
      output.print(separator);
      visitParam(paramModel);
      separator = ", ";
    }
    output.print(")");
    methodModel.getReturnModel().visit(this);
    if (hasBody(methodModel)) {
      output.printf(" {%n    %s%n  }%n", methodModel.getBody());
    } else {
      output.println(";");
    }
  }

  private void generateOverride(boolean override) {
    if (override) {
      output.print("override ");
    }
  }

  @Override
  public void visitParam(ParamModel paramModel) {
    if (paramModel.isRest()) {
      output.print("...");
    }
    output.print(paramModel.getName());
    generateType(paramModel);
    if (!paramModel.isRest()) {
      generateValue(paramModel);
    }
  }

  @Override
  public void visitReturn(ReturnModel returnModel) {
    generateType(returnModel);
  }

  @Override
  public void visitAnnotation(AnnotationModel annotationModel) {
    output.print("[" + annotationModel.getName());
    if (!annotationModel.getProperties().isEmpty()) {
      output.print("(");
      List<AnnotationPropertyModel> properties = annotationModel.getProperties();
      boolean first = true;
      for (AnnotationPropertyModel property : properties) {
        if (first) {
          first = false;
        } else {
          output.write(", ");
        }
        property.visit(this);
      }
      output.print(")");
    }
    output.println("]");
  }

  @Override
  public void visitAnnotationProperty(AnnotationPropertyModel annotationPropertyModel) {
    if (isEmpty(annotationPropertyModel.getName())) {
      output.print(annotationPropertyModel.getValue());
    } else {
      output.printf("%s = %s", annotationPropertyModel.getName(), annotationPropertyModel.getValue());
    }
  }

  private void printAsdoc(String asdoc, String indent) {
    if (!skipAsDoc && asdoc != null && asdoc.trim().length() > 0) {
      output.println(indent + "/**");
      output.println(indent + " * " + asdoc);
      output.println(indent + " */");
    }
  }

  private void generateStatic(MemberModel memberModel) {
    if (memberModel.isStatic()) {
      output.print("static ");
    }
  }

  private void generateVisibility(MemberModel memberModel) {
    if (!(compilationUnitModel.getPrimaryDeclaration() instanceof ClassModel
      && ((ClassModel)compilationUnitModel.getPrimaryDeclaration()).isInterface())) {
      output.print(memberModel.getVisibility() + " ");
      if (!memberModel.isField() && !hasBody(memberModel)) {
        output.print("native ");
      }
    }
  }

  private boolean hasBody(MemberModel memberModel) {
    return memberModel.isMethod() && ((MethodModel)memberModel).getBody() != null;
  }

  private void generateType(TypedModel typedModel) {
    if (!isEmpty(typedModel.getType())) {
      output.print(":" + typedModel.getType());
    }
  }

  private void generateValue(ValuedModel valuedModel) {
    if (!isEmpty(valuedModel.getValue())) {
      output.print(" = " + valuedModel.getValue());
    }
  }

  private static boolean isEmpty(String string) {
    return string == null || string.trim().length() == 0;
  }

  public static void main(String[] args) {
    ClassModel classModel = new ClassModel("com.acme.Foo");
    classModel.setAsdoc("This is the Foo class.");

    AnnotationModel annotation = new AnnotationModel("ExtConfig",
      new AnnotationPropertyModel("target", "'foo.Bar'"));
    classModel.addAnnotation(annotation);

    FieldModel field = new FieldModel("FOOBAR");
    field.setType("String");
    field.setConst(true);
    field.setStatic(true);
    field.setVisibility(Visibility.PRIVATE);
    field.setAsdoc("A constant for foo bar.");
    field.setValue("'foo bar baz'");
    classModel.addMember(field);

    MethodModel method = new MethodModel();
    method.setName("doFoo");
    method.setAsdoc("Some method.");
    method.setBody("trace('foo');");
    ParamModel param = new ParamModel();
    param.setName("foo");
    param.setType("com.acme.sub.Bar");
    param.setValue("null");
    method.setParams(Collections.singletonList(param));
    method.setType("int");
    classModel.addMember(method);

    PropertyModel propertyModel = new PropertyModel();
    propertyModel.setName("baz");
    propertyModel.setType("String");
    propertyModel.setAsdoc("The baz is a string.");
    classModel.addMember(propertyModel);

    StringWriter stringWriter = new StringWriter();
    classModel.visit(new ActionScriptCodeGeneratingModelVisitor(stringWriter));
    System.out.println("Result:\n" + stringWriter);
  }
}
