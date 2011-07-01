package net.jangaroo.exml.as;

import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.StdOutCompileLog;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Extends;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.config.JoocOptions;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.sym;

import java.io.IOException;

public class ConfigClassBuilder extends AstVisitorBase {
  private static final String EXT_CONFIG_META_NAME = "ExtConfig";
  private static final String TARGET_ANNOTATION_PARAMETER_NAME = "target";
  private static final String AS3_ANY_TYPE = "*";

  private static final String COMMENT_START = "/*";
  private static final String COMMENT_END = "*/";
  private static final String LINE_COMMENT_START = "//";
  private static final String ASDOC_COMMENT_START = "/**";


  private InputSource inputSource;
  private ConfigClass configClass;

  public ConfigClassBuilder(InputSource inputSource) {
    this.inputSource = inputSource;
  }

  public ConfigClass buildConfigClass() {
    CompilationUnit compilationUnit = Jooc.doParse(inputSource, new StdOutCompileLog(), JoocOptions.SemicolonInsertionMode.QUIRKS);
    try {
      compilationUnit.visit(this);
    } catch (IOException e) {
      throw new IllegalStateException("should not happen, because the ConfigClassBuilder does not do I/O", e);
    }
    return configClass.getComponentName() == null ? null : configClass;
  }

  @Override
  public void visitCompilationUnit(CompilationUnit compilationUnit) throws IOException {
    configClass = new ConfigClass();
    compilationUnit.getPackageDeclaration().visit(this);
    compilationUnit.getPrimaryDeclaration().visit(this);
  }

  @Override
  public void visitPackageDeclaration(PackageDeclaration packageDeclaration) throws IOException {
    String packageName = packageDeclaration.getQualifiedNameStr();
    configClass.setPackageName(packageName);
  }

  @Override
  public void visitClassDeclaration(ClassDeclaration classDeclaration) throws IOException {
    String className = classDeclaration.getName();
    configClass.setName(className);
    String description = parseDescription(classDeclaration.getSymClass(), classDeclaration.getSymModifiers());
    configClass.setDescription(description);
    if (classDeclaration.getOptExtends() != null) {
      classDeclaration.getOptExtends().visit(this);
    }
    for (AstNode node : classDeclaration.getDirectives()) {
      node.visit(new ClassAnnotationsVisitor());
    }
    classDeclaration.getBody().visit(this);
  }

  @Override
  public void visitExtends(Extends anExtends) throws IOException {
    String superClassName = anExtends.getSuperClass().getQualifiedNameStr();
    configClass.setSuperClassName(superClassName);
  }

  @Override
  public void visitClassBody(ClassBody classBody) throws IOException {
    for (AstNode node : classBody.getDirectives()) {
      node.visit(new ClassBodyVisitor());
    }
  }

  private class ClassAnnotationsVisitor extends AstVisitorBase {
    @Override
    public void visitAnnotation(Annotation annotation) throws IOException {
      detectAsDoc(annotation);
      detectExtConfigAnnotation(annotation);
    }

    private void detectAsDoc(Annotation annotation) {
      if (configClass.getDescription() == null) {
        String description = parseDescription(annotation.getLeftBracket(), new JooSymbol[0]);
        configClass.setDescription(description);
      }
    }

    private void detectExtConfigAnnotation(Annotation annotation) {
      if (EXT_CONFIG_META_NAME.equals(annotation.getMetaName())) {
        if (configClass.getComponentName() != null) {
          throw new CompilerError(annotation.getSymbol(), "Only one [" + EXT_CONFIG_META_NAME + "] annotation may be given.");
        }

        CommaSeparatedList<AnnotationParameter> annotationParameters = annotation.getOptAnnotationParameters();
        String target = null;
        while (annotationParameters != null) {
          AnnotationParameter annotationParameter = annotationParameters.getHead();
          Ide optName = annotationParameter.getOptName();
          if (optName != null && TARGET_ANNOTATION_PARAMETER_NAME.equals(optName.getName())) {
            JooSymbol symbol = annotationParameter.getValue().getSymbol();
            if (symbol.sym != sym.STRING_LITERAL) {
              throw new CompilerError(symbol, "The " + TARGET_ANNOTATION_PARAMETER_NAME + " parameter of an [" + EXT_CONFIG_META_NAME + "] annotation must be a string literal.");
            }
            target = (String) symbol.getJooValue();
            break;
          }
          annotationParameters = annotationParameters.getTail();
        }
        if (target == null) {
          throw new CompilerError(annotation.getSymbol(), "A " + TARGET_ANNOTATION_PARAMETER_NAME + " parameter must be provided for an [" + EXT_CONFIG_META_NAME + "] annotation.");
        }
        configClass.setComponentName(target);
      }
    }
  }

  private class ClassBodyVisitor extends AstVisitorBase {
    @Override
    public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
      if (functionDeclaration.isGetter()) {
        String name = functionDeclaration.getName();
        String type = parseTypeDeclaration(functionDeclaration);
        String description = parseDescription(functionDeclaration.getSymbol(), functionDeclaration.getSymModifiers());
        configClass.addCfg(new ConfigAttribute(name, type, description));
      }
    }

    private String parseTypeDeclaration(FunctionDeclaration functionDeclaration) {
      TypeRelation optTypeRelation = functionDeclaration.getFun().getOptTypeRelation();
      String type;
      if (optTypeRelation != null) {
        type = (String) optTypeRelation.getType().getSymbol().getJooValue();
      } else {
        type = AS3_ANY_TYPE;
      }
      return type;
    }
  }

  public static String parseDescription(JooSymbol symbol, JooSymbol[] symModifiers) {
    JooSymbol firstSymbol = symbol;
    if (symModifiers.length > 0) {
      firstSymbol = symModifiers[0];
    }
    String whitespace = firstSymbol.getWhitespace();
    int pos = 0;
    String lastAsDocComment = null;
    while (true) {
      int commentStart = whitespace.indexOf(COMMENT_START, pos);
      int lineCommentStart = whitespace.indexOf(LINE_COMMENT_START, pos);
      if (commentStart < 0) {
        break;
      }
      if (lineCommentStart >= 0 && lineCommentStart < commentStart) {
        pos = findLineCommentEnd(whitespace, lineCommentStart) + 1;
      } else {
        int endPos = findCommentEndPos(whitespace, commentStart);
        if (whitespace.substring(commentStart).startsWith(ASDOC_COMMENT_START)) {
          String comment = whitespace.substring(commentStart + ASDOC_COMMENT_START.length(), endPos);
          lastAsDocComment = parseAsDocComment(comment);
        }
        pos = endPos + COMMENT_END.length();
      }
    }
    return lastAsDocComment;
  }

  public static int findCommentEndPos(String whitespace, int commentStart) {
    int endPos = whitespace.indexOf(COMMENT_END, commentStart + COMMENT_START.length());
    if (endPos < 0) {
      throw new IllegalStateException("unterminated comment found; this should be detected by the lexer");
    }
    return endPos;
  }

  public static int findLineCommentEnd(String whitespace, int lineCommentStart) {
    int returnPos = whitespace.indexOf('\r', lineCommentStart);
    int lineFeedPos = whitespace.indexOf('\n', lineCommentStart);
    int endPos = returnPos < 0 ? lineFeedPos : lineFeedPos < 0 ? returnPos : Math.min(returnPos, lineFeedPos);
    if (endPos < 0) {
      throw new IllegalStateException("unterminated line comment found; this should be detected by the lexer");
    }
    return endPos;
  }

  public static String parseAsDocComment(String comment) {
    String lastAsDocComment;
    comment = comment.replaceAll("\\s*[\\r\\n]\\s*\\*[ \\t\u000B\\f]*", " ");
    comment = comment.replaceAll("\\s+", " ");
    lastAsDocComment = comment.trim();
    return lastAsDocComment;
  }
}
