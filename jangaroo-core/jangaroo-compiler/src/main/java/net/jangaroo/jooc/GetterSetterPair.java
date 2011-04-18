package net.jangaroo.jooc;

import java.io.IOException;

/**
 * An object aggregating the getter and the setter {@link FunctionDeclaration} of the same property.
 * Used only as a synthesized node returned by {@link Scope#lookupDeclaration(Ide)}. Therefore, scope and analyze methods should do nothing as the component
 * methods getter and setter are already scoped and analyzed.
 */
public class GetterSetterPair extends IdeDeclaration {
  private FunctionDeclaration getter, setter;

  GetterSetterPair(FunctionDeclaration getter, FunctionDeclaration setter) {
    super(getter.getIde());
    this.getter = getter;
    this.setter = setter;
  }

  public JooSymbol getSymbol() {
    return getter.getSymbol();
  }

  public FunctionDeclaration getGetter() {
    return getter;
  }

  public FunctionDeclaration getSetter() {
    return setter;
  }

  @Override
  public void scope(final Scope scope) {
  }

  protected void generateJsCode(JsWriter out) throws IOException {
    throw new IllegalStateException("GetterSetterPair#generateCode() should never be called!");
  }
}
