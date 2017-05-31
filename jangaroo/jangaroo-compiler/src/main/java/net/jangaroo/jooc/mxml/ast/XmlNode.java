package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.NodeImplBase;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;

/**
 * Common API of XmlElement and XmlAttribute.
 */
public abstract class XmlNode extends NodeImplBase {
  public abstract String getLocalName();

  public abstract String getPrefix();

  private Scope scope;
  XmlElement parent;

  private TypedIdeDeclaration propertyDeclaration;
  private Annotation event;
  InstantiationMode instantiationMode;
  private String extractXTypePropertyName;

  @Override
  public void scope(Scope scope) {
    this.scope = scope;
    scope(getChildren(), scope);
  }

  public Scope getScope() {
    return scope;
  }

  boolean assignPropertyDeclarationOrEvent(XmlElement parentElement) {
    ClassDeclaration type = parentElement.getType();
    String localName = getLocalName();
    propertyDeclaration = MxmlToModelParser.findPropertyModel(type, localName);
    if (propertyDeclaration == null) {
      event = MxmlToModelParser.findEvent(type, localName);
      if (event == null) {
        if (!isUntypedAccess() && !type.isDynamic()) {
          return false;
        }
        propertyDeclaration = new VariableDeclaration(new JooSymbol("var"), new Ide(localName), new TypeRelation(new JooSymbol("*")));
      }
    } else {
      extractXTypePropertyName = MxmlToModelParser.getExtractXTypePropertyName(propertyDeclaration);
      instantiationMode = extractXTypePropertyName != null
              ? InstantiationMode.PLAIN
              : InstantiationMode.from(MxmlToModelParser.useConfigObjects(propertyDeclaration));
    }
    parentElement.addMember(this);
    return true;
  }

  boolean isUntypedAccess() {
    return false;
  }

  TypedIdeDeclaration getPropertyDeclaration() {
    return propertyDeclaration;
  }

  String getConfigOptionName() {
    return propertyDeclaration == null ? null : MxmlToModelParser.getConfigOptionName(propertyDeclaration);
  }

  boolean isProperty() {
    return getPropertyDeclaration() != null;
  }

  Annotation getEvent() {
    return event;
  }

  public boolean isEvent() {
    return event != null;
  }

  /**
   * If this XmlNode represents a property (isProperty()), its property value can be any of the following:
   * <ul>
   *   <li>a JooSymbol containing its textual value, which can also be a binding expression,</li>
   *   <li>its only child XmlElement, or</li>
   *   <li>a List of its child XmlElements.</li>
   * </ul>
   * @return this XmlNodes property value, if it represents a property, null otherwise
   */
  abstract Object getPropertyValue();

  public XmlAttribute getConfigModeAttribute() {
    return null;
  }

  public String getConfigMode() {
    return "";
  }

  public String getExtractXTypePropertyName() {
    return extractXTypePropertyName;
  }

  enum InstantiationMode {
    MXML,
    PLAIN,
    EXT_CONFIG,
    EXT_CREATE;

    static InstantiationMode from(Boolean useConfigObjects) {
      return useConfigObjects == null ? null : useConfigObjects ? EXT_CONFIG : EXT_CREATE;
    }

    public boolean isExt() {
      return this != MXML;
    }

    public boolean isConfig() {
      return this == EXT_CONFIG || this == PLAIN;
    }
  }
}
