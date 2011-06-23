package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.JsWriter;
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * An object aggregating the getter and the setter {@link FunctionDeclaration} of the same property.
 * Used only as a synthesized node returned by {@link net.jangaroo.jooc.Scope#lookupDeclaration(Ide)}. Therefore, scope and analyze methods should do nothing as the component
 * methods getter and setter are already scoped and analyzed.
 */
public class GetterSetterPair extends IdeDeclaration {
  private FunctionDeclaration getter, setter;

  public GetterSetterPair(FunctionDeclaration getter, FunctionDeclaration setter) {
    super(getter.getIde());
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public void visit(AstVisitor visitor) {
    visitor.visitGetterSetterPair(this);
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

  public void generateJsCode(JsWriter out) throws IOException {
    throw new IllegalStateException("GetterSetterPair#generateCode() should never be called!");
  }
}
