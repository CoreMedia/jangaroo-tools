package net.jangaroo.jooc.types;

import net.jangaroo.jooc.ast.TypeDeclaration;

/**
 * The type of an expression.
 */
public class ExpressionType {

  private MetaType metaType;
  private TypeDeclaration declaration;

  public ExpressionType(MetaType metaType, TypeDeclaration declaration) {
    this.metaType = metaType;
    this.declaration = declaration;
  }

  public MetaType getMetaType() {
    return metaType;
  }

  public TypeDeclaration getDeclaration() {
    return declaration;
  }

  public enum MetaType {
    INSTANCE,
    CLASS,
    FUNCTION
  }
}
