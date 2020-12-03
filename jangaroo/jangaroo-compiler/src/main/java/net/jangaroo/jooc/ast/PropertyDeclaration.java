package net.jangaroo.jooc.ast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Virtual AST node that is created to aggregate a complementing get and set accessor.
 */
public class PropertyDeclaration extends TypedIdeDeclaration {

  private FunctionDeclaration getter;
  private FunctionDeclaration setter;

  PropertyDeclaration(FunctionDeclaration getter, FunctionDeclaration setter) {
    super(computeAnnotationsAndModifiers(getter, setter), getter.getIde(), getter.getOptTypeRelation());
    this.getter = getter;
    this.setter = setter;
  }

  private static AnnotationsAndModifiers computeAnnotationsAndModifiers(FunctionDeclaration getter, FunctionDeclaration setter) {
    ArrayList<Annotation> allAnnotations = new ArrayList<>(getter.getAnnotations());
    allAnnotations.addAll(setter.getAnnotations());
    return new AnnotationsAndModifiers(allAnnotations, Arrays.asList(getter.getSymModifiers()));
  }

  @Override
  public int getModifiers() {
    return getGetter() != null ? getGetter().getModifiers() : getSetter().getModifiers();
  }

  public FunctionDeclaration getGetter() {
    return getter;
  }

  public FunctionDeclaration getSetter() {
    return setter;
  }

  public FunctionDeclaration getAccessor(boolean returnSetter) {
    return returnSetter ? setter : getter;
  }

  public Iterable<FunctionDeclaration> getMethods() {
    return Arrays.asList(getter, setter);
  }

  @Override
  public boolean isWritable() {
    return getSetter() != null;
  }

  @Override
  public boolean isClassMember() {
    return getter.isClassMember();
  }

  @Override
  public ClassDeclaration getClassDeclaration() {
    return getter.getClassDeclaration();
  }

  @Override
  public boolean isExtConfig() {
    return getGetter() != null && super.isExtConfig(); // must have getter and setter
  }

  @Override
  public boolean isBindable() {
    return getGetter() != null && super.isBindable(); // must have getter and setter
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    throw new IllegalStateException("PropertyDeclaration is virtual and must not appear in AST.");
  }

  static TypedIdeDeclaration addDeclaration(FunctionDeclaration getterOrSetter, IdeDeclaration additionalDeclaration) {
    if (additionalDeclaration instanceof PropertyDeclaration) {
      PropertyDeclaration additionalPropertyDeclaration = (PropertyDeclaration) additionalDeclaration;
      additionalDeclaration = getterOrSetter.isGetter() ? additionalPropertyDeclaration.getSetter()
              : additionalPropertyDeclaration.getGetter();
    }
    if (additionalDeclaration instanceof FunctionDeclaration) {
      FunctionDeclaration additionalFunctionDeclaration = (FunctionDeclaration) additionalDeclaration;
      if (additionalFunctionDeclaration.isGetter()) {
        if (getterOrSetter.isSetter()) {
          return new PropertyDeclaration(additionalFunctionDeclaration, getterOrSetter);
        } else {
          return getterOrSetter;
        }
      } else if (additionalFunctionDeclaration.isSetter()) {
        if (getterOrSetter.isGetter()) {
          return new PropertyDeclaration(getterOrSetter, additionalFunctionDeclaration);
        } else {
          return getterOrSetter;
        }
      }
    }
    return null;
  }
}
