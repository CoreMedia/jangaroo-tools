package net.jangaroo.jooc.types;

import net.jangaroo.jooc.ast.TypeDeclaration;

import javax.annotation.Nonnull;

/**
 * The type of an expression.
 */
public class ExpressionType {

  public static final ExpressionType CLASS = new ExpressionType(MetaType.CLASS, null);
  public static final ExpressionType FUNCTION = new ExpressionType(MetaType.FUNCTION, null);

  private final MetaType metaType;
  private final TypeDeclaration declaration;

  public static ExpressionType create(MetaType metaType, @Nonnull TypeDeclaration declaration) {
    if (metaType == MetaType.INSTANCE && declaration != null) {
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
    }
    return newMetaType;
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

  public enum MetaType {
    INSTANCE,
    CLASS,
    FUNCTION
  }
}
