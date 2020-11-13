package net.jangaroo.jooc.types;

import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeWithTypeParam;
import net.jangaroo.jooc.ast.Type;
import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.utils.AS3Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The type of an expression.
 */
public class ExpressionType {

  private Type type;
  private AS3Type as3Type;
  private TypeDeclaration declaration;
  private ExpressionType typeParameter;
  private boolean isConfigType;

  public ExpressionType(@Nonnull Type type) {
    this.type = type;
  }

  public ExpressionType(@Nonnull TypeDeclaration declaration) {
    this(declaration, null);
  }

  public ExpressionType(@Nonnull TypeDeclaration declaration, @Nullable ExpressionType typeParameter) {
    this.declaration = declaration;
    this.typeParameter = typeParameter;
  }

  public boolean isConfigType() {
    return isConfigType;
  }

  public void markAsConfigTypeIfPossible() {
    if (getDeclaration() instanceof ClassDeclaration && ((ClassDeclaration) getDeclaration()).hasAnyExtConfigOrBindable()) {
      isConfigType = true;
    }
  }

  @Nullable
  public Type getType() {
    return type;
  }

  public ExpressionType getEvalType() {
    return this;
  }

  @Nonnull
  public AS3Type getAS3Type() {
    if (as3Type == null) {
      String typeQName = getDeclaration().getQualifiedNameStr();
      as3Type = AS3Type.typeByName(typeQName);
      if (as3Type == null) {
        as3Type = AS3Type.OBJECT;
      }
    }
    return as3Type;
  }

  @Nonnull
  public TypeDeclaration getDeclaration() {
    if (declaration == null) {
      declaration = type.getDeclaration();
    }
    return declaration;
  }

  @Nullable
  public ExpressionType getTypeParameter() {
    if (typeParameter == null && type != null && type.getIde() instanceof IdeWithTypeParam) {
      typeParameter = new ExpressionType(((IdeWithTypeParam) type.getIde()).getType());
    }
    return typeParameter;
  }

  public boolean isArrayLike() {
    return getAS3Type() == AS3Type.ARRAY || getAS3Type() == AS3Type.VECTOR;
  }

  public IdeDeclaration resolvePropertyDeclaration(String memberName) {
    if (getAS3Type() == AS3Type.CLASS && getTypeParameter() != null) {
      return getTypeParameter().getDeclaration().getStaticMemberDeclaration(memberName);
    } else {
      return getDeclaration().resolvePropertyDeclaration(memberName);
    }
  }

  /**
   * Return whether the given expression type is more general than this expression type,
   * so that anything of this expression type could be assigned to a variable of the
   * given expression type.
   * @param toCheck the expression type of the variable to assign
   * @return whether the assignment to an expression of this type is valid
   */
  public boolean isAssignableTo(@Nonnull ExpressionType toCheck) {

    if (AS3Type.ANY.equals(getAS3Type()) ||  AS3Type.BOOLEAN.equals(getAS3Type())) {
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

    if (!getAS3Type().equals(AS3Type.OBJECT) && getAS3Type().equals(expectedAS3Type)) {
      return true;
    }

    if (isNumber(expectedAS3Type) && isNumber(getAS3Type())) {
      return true;
    }

    if (AS3Type.VECTOR.equals(expectedAS3Type)) {
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
      return getAS3Type().equals(expectedAS3Type);
    }

    ClassDeclaration currentDeclaration = (ClassDeclaration) getDeclaration();
    if (AS3Type.CLASS.equals(getAS3Type()) && getTypeParameter() != null) {
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
    TypeDeclaration declaration = getDeclaration();
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpressionType that = (ExpressionType) o;
    return declaration.equals(that.getDeclaration()) && (getTypeParameter() != null ? getTypeParameter().equals(that.getTypeParameter()) : that.typeParameter == null);

  }

  @Override
  public int hashCode() {
    int result = getDeclaration().hashCode();
    result = 31 * result + (getTypeParameter() != null ? getTypeParameter().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    String name = getDeclaration().getQualifiedNameStr();
    ExpressionType typeParameter = getTypeParameter();
    if (typeParameter != null) {
      name += "<" + typeParameter.toString() + ">";
    }
    return name;
  }

  public static boolean isNumber(AS3Type type) {
    return AS3Type.NUMBER.equals(type) || AS3Type.INT.equals(type) || AS3Type.UINT.equals(type);
  }

  public static String toString(ExpressionType type) {
    return type == null ? "*" : type.toString();
  }
}
