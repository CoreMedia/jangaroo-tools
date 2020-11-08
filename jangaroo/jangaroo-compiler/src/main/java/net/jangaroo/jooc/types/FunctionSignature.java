package net.jangaroo.jooc.types;

import net.jangaroo.jooc.ast.TypeDeclaration;
import net.jangaroo.jooc.model.MethodType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * A special expression type for functions / methods.
 */
public class FunctionSignature extends ExpressionType {

  private final MethodType methodType;
  private final int minArgumentCount;
  private final boolean hasRest;
  private final List<ExpressionType> parameterTypes;

  public FunctionSignature(@Nonnull TypeDeclaration functionType,
                           MethodType methodType,
                           int minArgumentCount, boolean hasRest,
                           List<ExpressionType> parameterTypes,
                           ExpressionType returnType) {
    super(functionType, returnType);
    this.methodType = methodType;
    this.minArgumentCount = minArgumentCount;
    this.hasRest = hasRest;
    this.parameterTypes = parameterTypes;
  }

  public MethodType getMethodType() {
    return methodType;
  }

  public int getMinArgumentCount() {
    return minArgumentCount;
  }

  public boolean hasRest() {
    return hasRest;
  }

  public List<ExpressionType> getParameterTypes() {
    return parameterTypes;
  }

  @Override
  public ExpressionType getEvalType() {
    return methodType == MethodType.SET ? parameterTypes.get(0)  // use setter's param type
            : methodType == MethodType.GET ? getTypeParameter()  // use getter's return type
            : this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    FunctionSignature that = (FunctionSignature) o;
    return methodType == that.methodType
            && minArgumentCount == that.minArgumentCount
            && hasRest == that.hasRest
            && parameterTypes.equals(that.parameterTypes);

  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), methodType, minArgumentCount, hasRest, parameterTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (methodType != null) {
      sb.append(methodType.name().toLowerCase());
    }
    sb.append("(");
    boolean isFirst = true;
    for (ExpressionType parameterType : parameterTypes) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(", ");
      }
      sb.append(toString(parameterType));
    }
    sb.append("):");
    sb.append(toString(getTypeParameter()));
    return sb.toString();
  }
}
