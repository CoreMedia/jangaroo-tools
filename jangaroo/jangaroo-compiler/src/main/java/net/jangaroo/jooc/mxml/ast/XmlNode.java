package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.ast.NodeImplBase;

/**
 * Common API of XmlElement and XmlAttribute.
 */
public abstract class XmlNode extends NodeImplBase {
  public abstract String getLocalName();

  public abstract String getPrefix();
}
