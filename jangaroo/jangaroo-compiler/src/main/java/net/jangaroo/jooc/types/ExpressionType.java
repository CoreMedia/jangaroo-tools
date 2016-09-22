package net.jangaroo.jooc.types;

import net.jangaroo.jooc.ast.ClassDeclaration;
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

  public boolean isAssignableTo(@Nonnull ExpressionType toCheck) {

    if (as3Type == null || AS3Type.ANY.equals(as3Type) ||  AS3Type.BOOLEAN.equals(as3Type)) {
      // this expression type can be anything
      return true;
    }

    ClassDeclaration toCheckClassDeclaration = toCheck.getDeclaration() instanceof ClassDeclaration ?
            (ClassDeclaration) toCheck.getDeclaration() : null;

    if (toCheckClassDeclaration != null && toCheckClassDeclaration.isObject()) {
      // everything can be an object
      return true;
    }
    AS3Type expectedAS3Type = toCheck.getAS3Type();

    if (isNumber(expectedAS3Type) && isNumber(as3Type)) {
      return true;
    }

    if (AS3Type.VECTOR.equals(toCheck.getAS3Type())) {
      // cannot handle vectors yet
      return true;
    }
    // special case reg exp, not required any more for some reason
    /* if (AS3Type.REG_EXP.equals(expectedAS3Type) && sym.REGEXP_LITERAL == actualSym) {
      return true;
    }*/

    // what about simple types here? currently handled in Visitor

    if (toCheckClassDeclaration == null || !(getDeclaration() instanceof ClassDeclaration)) {
      // this is either a void declaration, cannot be any as this was already checked
      return as3Type.equals(expectedAS3Type);
    }

    ClassDeclaration currentDeclaration = (ClassDeclaration) getDeclaration();
    if (AS3Type.CLASS.equals(as3Type) && getTypeParameter() != null) {
      TypeDeclaration typeDeclaration = getTypeParameter().getDeclaration();
      if (typeDeclaration instanceof ClassDeclaration) {
        currentDeclaration = (ClassDeclaration) typeDeclaration;
      } else {
        // can this even happen?
        return true;
      }
    }
    return currentDeclaration.isAssignableTo((ClassDeclaration) toCheck.getDeclaration());
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

  public static boolean isNumber(AS3Type type) {
    return AS3Type.NUMBER.equals(type) || AS3Type.INT.equals(type) || AS3Type.UINT.equals(type);
  }

}
