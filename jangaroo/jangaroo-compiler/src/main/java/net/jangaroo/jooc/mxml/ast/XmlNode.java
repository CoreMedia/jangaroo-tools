package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.NodeImplBase;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;

/**
 * Common API of XmlElement and XmlAttribute.
 */
public abstract class XmlNode extends NodeImplBase {
  public abstract String getLocalName();

  public abstract String getPrefix();

  XmlElement parent;

  private TypedIdeDeclaration propertyDeclaration;
  private Annotation event;

  void assignPropertyDeclarationOrEvent(XmlElement parentElement) {
    ClassDeclaration type = parentElement.getType();
    String localName = getLocalName();
    propertyDeclaration = MxmlToModelParser.findPropertyModel(type, localName);
    if (propertyDeclaration == null) {
      event = MxmlToModelParser.findEvent(type, localName);
    }
  }

  TypedIdeDeclaration getPropertyDeclaration() {
    return propertyDeclaration;
  }

  Annotation getEvent() {
    return event;
  }
}
