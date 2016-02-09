package net.jangaroo.mxmlc.ast;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.NodeImplBase;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class XmlTag extends NodeImplBase {

  private final JooSymbol lt;
  private final Ide tagName;
  private final List<XmlAttribute> attributes;
  private final JooSymbol gt;

  public XmlTag(JooSymbol lt, Ide tagName, List<XmlAttribute> attributes, JooSymbol gt) {
    this.lt = lt;
    this.tagName = tagName;
    this.attributes = attributes;
    this.gt = gt;
  }

  @Override
  public JooSymbol getSymbol() {
    return lt;
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return Collections.emptyList();
  }

  @Override
  public void scope(Scope scope) {

  }

  @Override
  public void analyze(AstNode parentNode) {

  }

  @Override
  public AstNode getParentNode() {
    return null;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {

  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(lt.getText()).append(tagName.getName());
    if (attributes != null) {
      for (XmlAttribute attribute : attributes) {
        builder.append(" ").append(attribute);
      }
    }
    builder.append(gt.getText());
    return builder.toString();
  }

  public String getName() {
    return tagName.getName();
  }

  public XmlAttribute getAttribute(final String name) {
    return Iterables.getFirst(
            Iterables.filter(attributes, new Predicate<XmlAttribute>() {
              @Override
              public boolean apply(@Nullable XmlAttribute input) {
                return null != input && Objects.equals(name, input.getSymbol().getText());
              }
            }), null);
  }

  public List<XmlAttribute> getAttributes() {
    return attributes;
  }
}
