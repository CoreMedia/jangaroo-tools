package net.jangaroo.jooc.types;

import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.utils.AS3Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The type of an expression.
 */
public class ExpressionType {

  private final AS3Type as3Type;
  private final TypeDeclaration declaration;
  private final ExpressionType typeParameter;

  public ExpressionType(@Nonnull TypeDeclaration declaration) {
    this(declaration, null);
  }

  public ExpressionType(@Nonnull TypeDeclaration declaration, @Nullable ExpressionType typeParameter) {
    this.as3Type = computeAS3Type(declaration);
    this.declaration = declaration;
    this.typeParameter = typeParameter;
  }

  @Nonnull
  private static AS3Type computeAS3Type(TypeDeclaration declaration) {
    String typeQName = declaration.getQualifiedNameStr();
    AS3Type as3Type = AS3Type.typeByName(typeQName);
    return as3Type != null ? as3Type : AS3Type.OBJECT;
  }

  public AS3Type getAS3Type() {
    return as3Type;
  }

  @Nonnull
  public TypeDeclaration getDeclaration() {
    return declaration;
  }

  @Nullable
  public ExpressionType getTypeParameter() {
    return typeParameter;
  }

  public boolean isArrayLike() {
    return as3Type == AS3Type.ARRAY || as3Type == AS3Type.VECTOR;
  }

  public IdeDeclaration resolvePropertyDeclaration(String memberName) {
    if (as3Type == AS3Type.CLASS && typeParameter != null) {
      return typeParameter.getDeclaration().getStaticMemberDeclaration(memberName);
    } else {
      return getDeclaration().resolvePropertyDeclaration(memberName);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpressionType that = (ExpressionType) o;
    return declaration.equals(that.declaration) && (typeParameter != null ? typeParameter.equals(that.typeParameter) : that.typeParameter == null);

  }

  @Override
  public int hashCode() {
    int result = declaration.hashCode();
    result = 31 * result + (typeParameter != null ? typeParameter.hashCode() : 0);
    return result;
  }
}
