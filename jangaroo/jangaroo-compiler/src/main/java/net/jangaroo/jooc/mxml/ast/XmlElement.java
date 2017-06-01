package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.CompilerUtils;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.jangaroo.jooc.mxml.MxmlParserHelper.parsePackageFromNamespace;

public class XmlElement extends XmlNode {

  private static final String CONFIG_MODE_ATTRIBUTE_NAME = "mode";

  private final List<XmlElement> elements = new LinkedList<>();

  private final XmlTag openingMxmlTag;
  private final List<Object> children;
  private final XmlTag closingMxmlTag;

  private String classQName;
  private ClassDeclaration type;
  private TypedIdeDeclaration defaultPropertyModel;
  private List<XmlNode> members = new ArrayList<>();
  private List<XmlElement> defaultPropertyValues = new ArrayList<>();

  public XmlElement(@Nonnull XmlTag openingMxmlTag, @Nullable List children, @Nullable XmlTag closingMxmlTag) {
    this.openingMxmlTag = openingMxmlTag;
    this.children = children != null ? children : Collections.emptyList();
    this.closingMxmlTag = closingMxmlTag;
    initChildren();
    openingMxmlTag.setElement(this);
  }

  private static TypedIdeDeclaration findDefaultPropertyModel(ClassDeclaration classModel) {
    for (ClassDeclaration current = classModel; current != null; current = current.getSuperTypeDeclaration()) {
      TypedIdeDeclaration defaultPropertyModel = findPropertyWithAnnotation(current, MxmlUtils.MXML_DEFAULT_PROPERTY_ANNOTATION);
      if (defaultPropertyModel != null) {
        return defaultPropertyModel;
      }
    }
    return null;
  }

  private static TypedIdeDeclaration findPropertyWithAnnotation(ClassDeclaration current, String annotation) {
    for (TypedIdeDeclaration member : current.getMembers()) {
      if (!member.getAnnotations(annotation).isEmpty()) {
        return member;
      }
    }
    return null;
  }

  private void initChildren() {
    for (XmlAttribute attribute : openingMxmlTag.getAttributes()) {
      attribute.parent = this;
    }
    for (Object child : children) {
      if (child instanceof XmlElement) {
        XmlElement xmlElement = (XmlElement) child;
        elements.add(xmlElement);
        xmlElement.parent = this;
      }
    }
  }

  public String getName() {
    return openingMxmlTag.getLocalName();
  }

  @Override
  public String getPrefix() {
    return openingMxmlTag.getPrefix();
  }

  JooSymbol getTextContent() {
    JooSymbol jooSymbol = getTextNodes().stream().findFirst().orElse(new JooSymbol(""));
    String textContent = ((String) jooSymbol.getJooValue()).trim();
    boolean hasTextContent = !textContent.isEmpty();
    if (hasTextContent) {
      if (getElements().isEmpty() && getAttributes().stream().noneMatch(((Predicate<XmlAttribute>) XmlAttribute::isId).negate())) {
        return jooSymbol;
      }
      throw Jooc.error(getTextNodes().get(0), String.format("Unexpected text inside MXML element: '%s'.", textContent));
    }
    return null;
  }

  @Override
  Object getPropertyValue() {
    JooSymbol jooSymbol = getTextContent();
    if (jooSymbol != null) {
      return jooSymbol;
    }
    if (isArrayPropertyValue(getPropertyTypeName(), elements)) {
      return elements;
    }
    return getSinglePropertyValue(elements);
  }

  private static boolean isArrayPropertyValue(String propertyType, List<XmlElement> elements) {
    return "Array".equals(propertyType) || elements.size() > 1 && (propertyType == null || "*".equals(propertyType) || "Object".equals(propertyType));
  }

  private static XmlElement getSinglePropertyValue(List<XmlElement> elements) {
    if (elements.isEmpty()) {
      return null;
    }
    if (elements.size() > 1) {
      throw Jooc.error(elements.get(1).getSymbol(), "Non-array property may only have at most one sub-element.");
    }
    return elements.get(0);
  }

  @Override
  public JooSymbol getSymbol() {
    return openingMxmlTag.getSymbol();
  }

  public JooSymbol getClosingSymbol() {
    return closingMxmlTag != null ? closingMxmlTag.getSymbol() : openingMxmlTag.getClosingSymbol();
  }

  @Override
  public List<? extends AstNode> getChildren() {
    return children.stream()
            .filter(AstNode.class::isInstance)
            .map(AstNode.class::cast)
            .collect(Collectors.toList());
  }

  List<JooSymbol> getTextNodes() {
    return children.stream()
            .filter(JooSymbol.class::isInstance)
            .map(link -> (JooSymbol) link)
            .collect(Collectors.toList());
  }

  public List<XmlElement> getElements() {
    return elements;
  }

  @Override
  public void scope(Scope scope) {
    super.scope(scope);
    scope(openingMxmlTag.getAttributes(), scope);
  }

  @Override
  public void analyze(AstNode parentNode) {
    if (parentNode instanceof MxmlCompilationUnit) {
      // we are the root node!
      initObjectElement();
    } else if (!isBuiltInElement()) {
      XmlElement parentElement = (XmlElement) parentNode;
      if (parentElement.getType() != null && !parentElement.isArray()) {
        // Are we a property element?
        boolean isPropertyOrEvent = getNamespaceURI().equals(parentElement.getNamespaceURI())
                && assignPropertyDeclarationOrEvent(parentElement);
        if (!isPropertyOrEvent) {
          if (parentElement.getDefaultPropertyName() != null) {
            // try whether this is an object element first:
            initObjectElement();
            parentElement.addDefaultPropertyValue(this);
          } else {
            // dynamic property of a non-dynamic class: error!
            getScope().getCompiler().getLog().error(parentElement.getSymbol(), "MXML: property " + getLocalName() + " not found in class " + parentElement.getType().getQualifiedNameStr() + ".");
          }
        }
      } else {
        //  We are a value!
        initObjectElement();
      }
    }
    analyze(this, openingMxmlTag.getAttributes());
    analyze(this, getChildren());
    members = Collections.unmodifiableList(members); // no more modifications after analysing finished please!
  }

  public ClassDeclaration getType() {
    return type;
  }

  void addMember(XmlNode member) {
    members.add(member);
  }

  List<XmlNode> getMembers() {
    return members;
  }

  Stream<XmlNode> getProperties() {
    return members.stream().filter(XmlNode::isProperty);
  }

  Stream<XmlNode> getEventHandlers() {
    return members.stream().filter(XmlNode::isEvent);
  }

  public String getDefaultPropertyName() {
    return defaultPropertyModel == null ? null : defaultPropertyModel.getName();
  }

  private void addDefaultPropertyValue(XmlElement objectElement) {
    defaultPropertyValues.add(objectElement);
  }

  public boolean isArrayDefaultPropertyValue() {
    return isArrayPropertyValue(getPropertyTypeName(defaultPropertyModel), defaultPropertyValues);
  }

  public XmlElement getSingleDefaultPropertyValue() {
    return getSinglePropertyValue(defaultPropertyValues);
  }

  public List<XmlElement> getDefaultPropertyValues() {
    return defaultPropertyValues;
  }

  boolean isArray() {
    return getType() != null && "Array".equals(getType().getQualifiedNameStr());
  }

  @Override
  public AstNode getParentNode() {
    return parent;
  }

  @Override
  public XmlAttribute getConfigModeAttribute() {
    return getAttributeNodeNS(Exmlc.EXML_NAMESPACE_URI, CONFIG_MODE_ATTRIBUTE_NAME);
  }

  @Override
  public String getConfigMode() {
    XmlAttribute configModeAttribute = getConfigModeAttribute();
    return configModeAttribute != null
            ? (String) configModeAttribute.getValue().getJooValue()
            : getConfigMode(getPropertyDeclaration());
  }

  private static String getConfigMode(TypedIdeDeclaration propertyDeclaration) {
    Annotation extConfigAnnotation = propertyDeclaration.getAnnotation(Jooc.EXT_CONFIG_ANNOTATION_NAME);
    if (extConfigAnnotation != null) {
      CommaSeparatedList<AnnotationParameter> annotationParameters = extConfigAnnotation.getOptAnnotationParameters();
      while (annotationParameters != null) {
        Ide name = annotationParameters.getHead().getOptName();
        if (name != null && CONFIG_MODE_ATTRIBUTE_NAME.equals(name.getName())) {
          AstNode value = annotationParameters.getHead().getValue();
          if (value instanceof LiteralExpr) {
            Object jooValue = value.getSymbol().getJooValue();
            if (jooValue instanceof String) {
              return (String) jooValue;
            }
          }
        }
        annotationParameters = annotationParameters.getTail();
      }
    }
    return "";
  }

  private void initObjectElement() {
    if (classQName == null) {
      String name = getLocalName();
      if (getPrefix() != null) {
        name = getPrefix() + ":" + name;
      }
      throw Jooc.error(this, "Could not resolve class from MXML node <" + name + "/>");
    }

    type = getScope().getClassDeclaration(classQName);
    if (type == null || type.isInterface()) {
      throw Jooc.error(getSymbol(), "Class not found: " + classQName);
    }
    ((MxmlCompilationUnit)getScope().getCompilationUnit()).addImport(classQName);
    instantiationMode = computeInstantiationMode();
    defaultPropertyModel = findDefaultPropertyModel(type);
  }

  private InstantiationMode computeInstantiationMode() {
    if (parent == null) {
      return InstantiationMode.PLAIN;
    }
    if (type == null || !CompilationUnitUtils.constructorSupportsConfigOptionsParameter(type)) {
       return InstantiationMode.MXML;
    }
    String id = getId();
    if (id != null) {
      return InstantiationMode.EXT_CREATE;
    }
    InstantiationMode parentInstantiationMode = parent != null && parent.isProperty() ? parent.instantiationMode : null;
    if (parentInstantiationMode != null) {
      return parentInstantiationMode;
    }
    return InstantiationMode.from(XmlNode.useConfigObjects(type));
  }

  InstantiationMode getInstantiationMode() {
    return instantiationMode;
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(openingMxmlTag);
    if (null != children) {
      for (Object child : children) {
        if (child instanceof JooSymbol) {
          child = ((JooSymbol)child).getSourceCode();
        }
        builder.append(child);
      }
    }
    if(null != closingMxmlTag) {
      builder.append(closingMxmlTag);
    }
    return builder.toString();
  }

  public List<XmlAttribute> getAttributes() {
    return openingMxmlTag.getAttributes();
  }

  @Nullable
  XmlAttribute getAttribute(String name) {
    return openingMxmlTag.getAttribute(name);
  }

  @Nullable
  JooSymbol getIdSymbol() {
    return openingMxmlTag.getIdSymbol();
  }

  @Nullable
  public String getId() {
    JooSymbol idSymbol = getIdSymbol();
    return idSymbol == null ? null : (String) idSymbol.getJooValue();
  }

  public String getLocalName() {
    return openingMxmlTag.getLocalName();
  }

  public String getNamespaceURI() {
    return getNamespaceUri(getPrefix());
  }

  public boolean isBuiltInElement() {
    return MxmlUtils.isMxmlNamespace(getNamespaceURI()) && MxmlUtils.BUILT_IN_ELEMENT_NAMES.contains(getLocalName());
  }


  String getNamespaceUri(@Nullable String prefix) {
    String localResult = openingMxmlTag.getNamespaceUri(prefix);
    if(null != localResult) {
      return localResult;
    }
    if(null != parent) {
      return parent.getNamespaceUri(prefix);
    }
    return null;
  }

  /**
   * @see Element#getAttributeNS(String, String)
   */
  String getAttributeNS(String namespaceUri, String localName) {
    XmlAttribute attribute = getAttributeNodeNS(namespaceUri, localName);
    return null != attribute ? (String) attribute.getValue().getJooValue() : "";
  }

  /**
   * @see Element#getAttributeNodeNS(String, String)
   */
  XmlAttribute getAttributeNodeNS(String namespaceUri, String localName) {
    return openingMxmlTag.getAttribute(namespaceUri, localName);
  }

  String resolveClass(MxmlComponentRegistry mxmlComponentRegistry) {
    if (classQName == null) {
      String prefix = getPrefix();
      String name = getLocalName();
      String uri = getNamespaceURI();
      if (uri == null) {
        throw Jooc.error(getSymbol(),"Undeclared namespace URI for prefix '" + prefix + "'.");
      } else {
        String packageName = parsePackageFromNamespace(uri);
        if (packageName != null) {
          classQName = CompilerUtils.qName(packageName, name);
        } else {
          classQName = mxmlComponentRegistry.getClassName(uri, name);
        }
      }
    }
    return classQName;
  }

  public String getClassQName() {
    return classQName;
  }
}
