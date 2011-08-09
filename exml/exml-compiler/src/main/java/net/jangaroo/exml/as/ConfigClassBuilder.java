package net.jangaroo.exml.as;

import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassType;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
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


  private ConfigClass configClass;
  private CompilationUnit compilationUnit;

  public ConfigClassBuilder(CompilationUnit compilationUnit) {
    this.compilationUnit = compilationUnit;
  }

  public ConfigClass buildConfigClass() {
    try {
      compilationUnit.visit(this);
    } catch (IOException e) {
      throw new IllegalStateException("should not happen, because the ConfigClassBuilder does not do I/O", e);
    }
    return configClass.getComponentClassName() == null ? null : configClass;
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
    String name = classDeclaration.getName();
    configClass.setName(name);
    ClassDeclaration superTypeDeclaration = classDeclaration.getSuperTypeDeclaration();
    //Ignore superclass if its object or null
    String superClassName = superTypeDeclaration == null ? null : "Object".equals(superTypeDeclaration.getQualifiedNameStr()) ? null : superTypeDeclaration.getQualifiedNameStr();
    configClass.setSuperClassName(superClassName);
    String description = parseDescription(classDeclaration.getSymClass(), classDeclaration.getSymModifiers());
    configClass.setDescription(description);
    for (AstNode node : classDeclaration.getDirectives()) {
      node.visit(new ClassAnnotationsVisitor());
    }
    classDeclaration.getBody().visit(this);
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
        if (configClass.getComponentClassName() != null) {
          throw new CompilerError(annotation.getSymbol(), "Only one [" + EXT_CONFIG_META_NAME + "] annotation may be given.");
        }

        CommaSeparatedList<AnnotationParameter> annotationParameters = annotation.getOptAnnotationParameters();
        while (annotationParameters != null) {
          AnnotationParameter annotationParameter = annotationParameters.getHead();
          Ide optNameIde = annotationParameter.getOptName();
          if (optNameIde != null) {
            String parameterName = optNameIde.getName();
            LiteralExpr annotationParameterValue = annotationParameter.getValue();
            String parameterValue = null;
            if (annotationParameterValue != null) {
              JooSymbol symbol = annotationParameterValue.getSymbol();
              if (symbol.sym != sym.STRING_LITERAL) {
                throw new CompilerError(symbol, "The " + parameterName + " parameter of an [" + EXT_CONFIG_META_NAME + "] annotation must be a string literal.");
              }
              parameterValue = (String) symbol.getJooValue();
            }
            if (TARGET_ANNOTATION_PARAMETER_NAME.equals(parameterName)) {
              if (parameterValue == null) {
                throw new CompilerError(optNameIde.getSymbol(), "The " + parameterName + " parameter of an [" + EXT_CONFIG_META_NAME + "] annotation must have a value.");
              }
              configClass.setComponentClassName(parameterValue);
            } else {
              try {
                configClass.setType(ConfigClassType.fromExtTypeAttribute(parameterName));
              } catch (IllegalArgumentException e) {
                throw new CompilerError(optNameIde.getSymbol(), "'" + parameterName + "' is not a valid parameter of an [" + EXT_CONFIG_META_NAME + "] annotation (only 'xtype', 'ptype', 'type' are allowed).", e);
              }
              if (parameterValue == null) {
                // default: use fully qualified config class name as the xtype/ptype/type
                parameterValue = configClass.getFullName();
              }
              configClass.setTypeValue(parameterValue);
            }
          }
          annotationParameters = annotationParameters.getTail();
        }
        if (configClass.getComponentClassName() == null) {
          throw new CompilerError(annotation.getSymbol(), "A " + TARGET_ANNOTATION_PARAMETER_NAME + " parameter must be provided for an [" + EXT_CONFIG_META_NAME + "] annotation.");
        }
      }
    }
  }

  private class ClassBodyVisitor extends AstVisitorBase {
    @Override
    public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
      if (functionDeclaration.isGetter() && !functionDeclaration.isStatic()) {
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
        type = optTypeRelation.getType().getSymbol().getText();
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
