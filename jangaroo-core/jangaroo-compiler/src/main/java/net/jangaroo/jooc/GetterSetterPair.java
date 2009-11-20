package net.jangaroo.jooc;

import java.io.IOException;

/**
 * An object aggregating the getter and the setter {@link MethodDeclaration} of the same property.
 * Used only as a synthesized node returned by {@link net.jangaroo.jooc.Scope#getIdeDeclaration(String)} and
 * {@link net.jangaroo.jooc.Scope#getIdeDeclaration(Ide)}.
 */
public class GetterSetterPair extends NodeImplBase {
  private MethodDeclaration getter, setter;

  GetterSetterPair(MethodDeclaration getter, MethodDeclaration setter) {
    this.getter = getter;
    this.setter = setter;
  }

  public JooSymbol getSymbol() {
    return getter.getSymbol();
  }

  public MethodDeclaration getGetter() {
    return getter;
  }

  public MethodDeclaration getSetter() {
    return setter;
  }

  public void generateCode(JsWriter out) throws IOException {
    throw new IllegalStateException("GetterSetterPair#generateCode() should never be called!");
  }
}
