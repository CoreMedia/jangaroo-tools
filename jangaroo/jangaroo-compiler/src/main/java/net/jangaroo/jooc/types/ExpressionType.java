package net.jangaroo.jooc.types;

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.Typed;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * The type of an expression.
 */
public class ExpressionType {

  private static final String ARRAY_ELEMENT_TYPE_ANNOTATION_NAME = "ArrayElementType";
  private final MetaType metaType;
  private final TypeDeclaration declaration;

  public static ExpressionType create(MetaType metaType, @Nonnull TypeDeclaration declaration) {
    if (metaType == MetaType.INSTANCE) {
      MetaType newMetaType = computePredefinedType(declaration.getQualifiedNameStr());
      if (newMetaType != null) {
        return new ExpressionType(newMetaType, declaration.getIde().getScope().getCompiler().getAnyType());
      }
    }
    return new ExpressionType(metaType, declaration);
  }

  public static MetaType computePredefinedType(String typeQName) {
    MetaType newMetaType = null;
    if ("Class".equals(typeQName)) {
      newMetaType = MetaType.CLASS;
    } else if ("Function".equals(typeQName)) {
      newMetaType = MetaType.FUNCTION;
    } else if ("Array".equals(typeQName)) {
      newMetaType = MetaType.ARRAY;
    } else if ("Vector$object".equals(typeQName)) {
      newMetaType = MetaType.VECTOR;
    }
    return newMetaType;
  }

  public static ExpressionType create(IdeDeclaration declaration) {
    if (declaration instanceof TypeDeclaration) {
      return create(MetaType.CLASS, (TypeDeclaration) declaration);
    }
    if (declaration instanceof Typed) {
      TypeRelation typeRelation = ((Typed) declaration).getOptTypeRelation();
      if (typeRelation != null) {
        TypeDeclaration memberTypeDeclaration = typeRelation.getType().getDeclaration();
        if (memberTypeDeclaration != null) {
          if ("Array".equals(memberTypeDeclaration.getQualifiedNameStr())) {
            TypeDeclaration typeDeclaration = findArrayElementType(declaration);
            if (typeDeclaration != null) {
              return create(MetaType.ARRAY, typeDeclaration);
            }
          }
          return create(declaration instanceof FunctionDeclaration && !((FunctionDeclaration) declaration).isGetterOrSetter()
                          ? ExpressionType.MetaType.FUNCTION
                          : ExpressionType.MetaType.INSTANCE,
                  memberTypeDeclaration);
        }
      }
    }
    return null;
  }

  private static TypeDeclaration findArrayElementType(IdeDeclaration declaration) {
    // find [ArrayElementType("...")] annotation:
    Annotation annotation = findAnnotation(declaration, ARRAY_ELEMENT_TYPE_ANNOTATION_NAME);
    if (annotation != null) {
      JangarooParser compiler = declaration.getIde().getScope().getCompiler();
      CommaSeparatedList<AnnotationParameter> annotationParameters = annotation.getOptAnnotationParameters();
      if (annotationParameters == null) {
        compiler.getLog().error(declaration.getSymbol(), "[ArrayElementType] must provide a class reference.");
      } else {
        AnnotationParameter firstParameter = annotationParameters.getHead();
        Object elementType = firstParameter.getValue().getSymbol().getJooValue();
        if (elementType instanceof String) {
          CompilationUnit compilationUnit = compiler.getCompilationUnit((String) elementType);
          if (compilationUnit == null) {
            compiler.getLog().error(firstParameter.getSymbol(), String.format("[ArrayElementType] class reference '%s' not found.", elementType));
          } else {
            IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
            if (!(primaryDeclaration instanceof TypeDeclaration)) {
              compiler.getLog().error(firstParameter.getSymbol(), String.format("[ArrayElementType] references '%s', which is not a class.", elementType));
            } else {
              return  (TypeDeclaration) primaryDeclaration;
            }
          }
        }
      }
    }
    return null;
  }

  private static Annotation findAnnotation(AstNode declaration, String annotationName) {
    AstNode parentNode = declaration.getParentNode();
    if (parentNode == null) {
      return null;
    }
    List<? extends AstNode> children = parentNode.getChildren();
    int declarationIndex = children.indexOf(declaration);
    for (int index = declarationIndex - 1; index > 0; --index) {
      AstNode astNode = children.get(index);
      if (!(astNode instanceof Annotation)) {
        return null;
      }
      Annotation annotation = (Annotation) astNode;
      if (annotation.getMetaName().equals(annotationName)) {
        return annotation;
      }
    }
    return null;
  }

  private ExpressionType(MetaType metaType, TypeDeclaration declaration) {
    this.metaType = metaType;
    this.declaration = declaration;
  }

  @Nonnull
  public MetaType getMetaType() {
    return metaType;
  }

  @Nonnull
  public TypeDeclaration getDeclaration() {
    return declaration;
  }

  public boolean isArray() {
    return metaType == MetaType.ARRAY || metaType == MetaType.VECTOR;
  }

  public enum MetaType {
    INSTANCE,
    ARRAY,
    VECTOR,
    CLASS,
    FUNCTION
  }
}
