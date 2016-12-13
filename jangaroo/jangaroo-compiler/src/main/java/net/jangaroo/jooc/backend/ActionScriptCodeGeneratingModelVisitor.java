package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.model.ActionScriptModel;
import net.jangaroo.jooc.model.AnnotatedModel;
import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.FieldModel;
import net.jangaroo.jooc.model.MemberModel;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.ModelVisitor;
import net.jangaroo.jooc.model.NamespaceModel;
import net.jangaroo.jooc.model.NamespacedModel;
import net.jangaroo.jooc.model.ParamModel;
import net.jangaroo.jooc.model.PropertyModel;
import net.jangaroo.jooc.model.ReturnModel;
import net.jangaroo.jooc.model.TypedModel;
import net.jangaroo.jooc.model.ValuedModel;
import net.jangaroo.utils.CompilerUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A ClassModel visitor that generates ActionScript (API) code.
 */
public class ActionScriptCodeGeneratingModelVisitor implements ModelVisitor {
  public static final Pattern LEADING_ASDOC_WHITESPACE_PATTERN = Pattern.compile("\\s*\\* ?(.*)");
  private final PrintWriter output;
  private CompilationUnitModel compilationUnitModel;
  private boolean skipAsDoc;
  private String indent = "";

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
    indent = "";
    for (String anImport : compilationUnitModel.getImports()) {
      output.printf("import %s;%n", anImport);
    }
    output.println();
    for (String aDependency : compilationUnitModel.getDependenciesInModule()) {
      output.printf("[%s(\"%s\")]%n", Jooc.USES_ANNOTATION_NAME, aDependency);
    }
    compilationUnitModel.getPrimaryDeclaration().visit(this);
    indent = "";
    output.print("}");
    output.close();
  }

  @Override
  public void visitClass(ClassModel classModel) {
    visitAnnotations(classModel);
    output.print(classModel.getAnnotationCode());
    printAsdoc(classModel.getAsdoc());
    printToken(classModel.getNamespace());
    printTokenIf(classModel.isFinal(), "final");
    printTokenIf(classModel.isDynamic(), "dynamic");
    printToken(classModel.isInterface(), "interface", "class");
    printToken(classModel.getName());
    if (!classModel.isInterface() && !isEmpty(classModel.getSuperclass())) {
      output.printf("extends %s ", classModel.getSuperclass());
    }
    if (!classModel.getInterfaces().isEmpty()) {
      printToken(classModel.isInterface(), "extends", "implements");
      List<String> tokens = classModel.getInterfaces();
      output.print(tokens.get(0));
      for (String token : tokens.subList(1, tokens.size())) {
        output.print(", ");
        output.print(token);
      }
      output.print(" ");
    }
    output.print("{");
    output.print(classModel.getBodyCode());
    indent = "  ";
    for (MemberModel member : classModel.getMembers()) {
      output.println();
      member.visit(this);
    }
    indent = "";
    output.println("}");
  }

  @Override
  public void visitNamespace(NamespaceModel namespaceModel) {
    visitAnnotations(namespaceModel);
    printAsdoc(namespaceModel.getAsdoc());
    printToken(namespaceModel.getNamespace());
    printToken("namespace");
    output.print(namespaceModel.getName());
    generateValue(namespaceModel);
    output.println(";");
  }

  private void visitAnnotations(AnnotatedModel annotatedModel) {
    for (AnnotationModel annotation : annotatedModel.getAnnotations()) {
      annotation.visit(this);
    }
  }

  private void printParameterList(List<? extends ActionScriptModel> models) {
    output.print("(");
    boolean first = true;
    for (ActionScriptModel model : models) {
      if (first) {
        first = false;
      } else {
        output.print(", ");
      }
      model.visit(this);
    }
    output.print(")");
  }

  private void printTokenIf(boolean flag, String token) {
    if (flag) {
      printToken(token);
    }
  }

  private void printToken(boolean flag, String trueToken, String falseToken) {
    printToken(flag ? trueToken : falseToken);
  }

  private void printToken(String token) {
    output.printf("%s ", token);
  }

  private void indent() {
    output.print(indent);
  }

  public void flush() {
    output.flush();
  }

  @Override
  public void visitField(FieldModel fieldModel) {
    visitAnnotations(fieldModel);
    printAsdoc(fieldModel.getAsdoc());
    indent();
    printToken(fieldModel.getNamespace());
    printTokenIf(fieldModel.isStatic(), "static");
    printToken(fieldModel.isConst(), "const", "var");
    output.print(fieldModel.getName());
    generateType(fieldModel);
    generateValue(fieldModel);
    output.println(";");
  }

  @Override
  public void visitProperty(PropertyModel propertyModel) {
    throw new IllegalStateException("PropertyModel should not be visited by code generator.");
  }

  private static List<String> PARAM_SUPPRESSING_ASDOC_TAGS = Arrays.asList("@inheritDoc", "@private");

  @Override
  public void visitMethod(MethodModel methodModel) {
    visitAnnotations(methodModel);
    StringBuilder asdoc = new StringBuilder();
    if (methodModel.getAsdoc() != null) {
      asdoc.append(methodModel.getAsdoc());
    }
    if (!PARAM_SUPPRESSING_ASDOC_TAGS.contains(asdoc.toString())) {
      for (ParamModel paramModel : methodModel.getParams()) {
        if (!isEmpty(paramModel.getAsdoc())) {
          asdoc.append("\n@param ").append(paramModel.getName()).append(" ").append(paramModel.getAsdoc());
        }
      }
      String returnAsDoc = methodModel.getReturnModel().getAsdoc();
      if (!isEmpty(returnAsDoc)) {
        asdoc.append("\n@return ").append(returnAsDoc);
      }
    }
    printAsdoc(asdoc.toString());
    indent();
    printTokenIf(methodModel.isOverride(), "override");
    String methodBody = methodModel.getBody();
    if (!isPrimaryDeclarationAnInterface()) {
      printToken(methodModel.getNamespace());
      printTokenIf(methodModel.isStatic(), "static");
      printTokenIf(methodModel.isFinal(), "final");
      printTokenIf(methodBody == null, "native");
    }
    printToken("function");
    if (methodModel.getMethodType() != null) {
      printToken(methodModel.getMethodType().toString());
    }
    output.print(methodModel.getName());
    printParameterList(methodModel.getParams());
    methodModel.getReturnModel().visit(this);
    if (methodBody != null) {
      output.printf(" {%n    %s%n  }%n", methodModel.getBody());
    } else {
      output.println(";");
    }
  }

  @Override
  public void visitParam(ParamModel paramModel) {
    if (paramModel.isRest()) {
      output.print("...");
    }
    output.print(paramModel.getName());
    if (!paramModel.isRest()) {
      generateType(paramModel);
      generateValue(paramModel);
    }
  }

  @Override
  public void visitReturn(ReturnModel returnModel) {
    generateType(returnModel);
  }

  @Override
  public void visitAnnotation(AnnotationModel annotationModel) {
    printAsdoc(annotationModel.getAsdoc());
    indent(); output.print("[" + annotationModel.getName());
    if (!annotationModel.getProperties().isEmpty()) {
      printParameterList(annotationModel.getProperties());
    }
    output.println("]");
  }

  @Override
  public void visitAnnotationProperty(AnnotationPropertyModel annotationPropertyModel) {
    String name = annotationPropertyModel.getName();
    String value = annotationPropertyModel.getValue();
    String unquoted = CompilerUtils.unquote(value);
    if (unquoted != null) {
      String quote = value.substring(0, 1);
      // escape "<" and "'" (single quote), or asdoc tool will fail:
      value = quote + unquoted.replaceAll("<", "&lt;").replaceAll("'", "&quot;") + quote;
    }
    if (isEmpty(name)) {
      output.print(value);
    } else if (isEmpty(value)) {
      output.print(name);
    } else {
      output.printf("%s = %s", name, value);
    }
  }

  private void printAsdoc(String asdoc) {
    if (!skipAsDoc && asdoc != null && asdoc.trim().length() > 0) {
      indent(); output.println("/**");
      for (String line : asdoc.split("\n")) {
        Matcher matcher = LEADING_ASDOC_WHITESPACE_PATTERN.matcher(line);
        if (matcher.matches()) {
          line = matcher.group(1);
        }
        // asdoc tool does not like "@" that is not followed by a directive.
        // Thus, we escape all "@"s not following white-space or "*" (so that /**@private*/ still works):
        line = line.replaceAll("([^\\s*])@", "$1&#64;");
        indent(); output.println(" " + ("* " + line).trim());
      }
      indent(); output.println(" */");
    }
  }

  private boolean isPrimaryDeclarationAnInterface() {
    return compilationUnitModel.getPrimaryDeclaration() instanceof ClassModel
      && ((ClassModel)compilationUnitModel.getPrimaryDeclaration()).isInterface();
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
    // TODO: move to unit test!
    ClassModel classModel = new ClassModel("com.acme.Foo");
    classModel.setAsdoc("This is the Foo class.");

    AnnotationModel annotation = new AnnotationModel("ExtConfig",
      new AnnotationPropertyModel("target", "'foo.Bar'"));
    classModel.addAnnotation(annotation);

    FieldModel field = new FieldModel("FOOBAR");
    field.setType("String");
    field.setConst(true);
    field.setStatic(true);
    field.setNamespace(NamespacedModel.PRIVATE);
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
