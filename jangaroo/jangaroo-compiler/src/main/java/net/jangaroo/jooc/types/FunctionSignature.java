package net.jangaroo.jooc.types;

import net.jangaroo.jooc.ast.TypeDeclaration;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A special expression type for functions / methods.
 */
public class FunctionSignature extends ExpressionType {

  private final int minArgumentCount;
  private final boolean hasRest;
  private final List<ExpressionType> parameterTypes;

  public FunctionSignature(@Nonnull TypeDeclaration functionType, int minArgumentCount, boolean hasRest,
                           List<ExpressionType> parameterTypes,
                           ExpressionType returnType) {
    super(functionType, returnType);
    this.minArgumentCount = minArgumentCount;
    this.hasRest = hasRest;
    this.parameterTypes = parameterTypes;
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
    return minArgumentCount == that.minArgumentCount
            && hasRest == that.hasRest
            && parameterTypes.equals(that.parameterTypes);

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + minArgumentCount;
    result = 31 * result + (hasRest ? 1 : 0);
    result = 31 * result + parameterTypes.hashCode();
    return result;
  }

}
