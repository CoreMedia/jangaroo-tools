package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: fwienber Date: 27.02.11 Time: 17:08 To change this template use File | Settings |
 * File Templates.
 */
public class IdeWithTypeParam extends Ide {

  private JooSymbol originalIde;
  private JooSymbol symDotLt;
  private Type type;
  private JooSymbol symGt;

  public IdeWithTypeParam(JooSymbol ide, JooSymbol symDotLt, Type type, JooSymbol symGt) {
    super(withTypeParam(ide, type));
    this.originalIde = ide;
    this.symDotLt = symDotLt;
    this.type = type;
    this.symGt = symGt;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return makeChildren(super.getChildren(), type);
  }

  public JooSymbol getOriginalIde() {
    return originalIde;
  }

  public JooSymbol getSymDotLt() {
    return symDotLt;
  }

  public Type getType() {
    return type;
  }

  public JooSymbol getSymGt() {
    return symGt;
  }

  @SuppressWarnings({"UnusedParameters"})
  private static JooSymbol withTypeParam(JooSymbol ide, Type type) { // NOSONAR
    return ide.replacingSymAndTextAndJooValue(ide.sym,
            ide.getText() + "$object", // TODO: depending on type, use the right suffix
            ide.getJooValue());
  }

  @Override
  public void scope(Scope scope) {
    super.scope(scope);
    type.scope(scope);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitIdeWithTypeParam(this);
  }

  public void addPublicApiDependency() {
    super.addPublicApiDependency();
    type.getIde().addPublicApiDependency();
  }
}
