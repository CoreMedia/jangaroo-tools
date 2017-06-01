package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.NodeImplBase;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;

import java.util.Iterator;
import java.util.Map;

/**
 * Common API of XmlElement and XmlAttribute.
 */
public abstract class XmlNode extends NodeImplBase {
  private static final String EXT_CONFIG_CREATE_FLAG = "create";
  private static final String EXT_CONFIG_EXTRACT_XTYPE_PARAMETER = "extractXType";

  private static TypedIdeDeclaration findPropertyModel(ClassDeclaration classModel, String propertyName) {
    TypedIdeDeclaration propertyModel = null;
    ClassDeclaration superClassModel = classModel.getSuperTypeDeclaration();
    if (superClassModel != null) {
      propertyModel = findPropertyModel(superClassModel, propertyName);
    }
    if (propertyModel == null) {
      TypedIdeDeclaration memberModel = classModel.getMemberDeclaration(propertyName);
      if (memberModel != null && !memberModel.isPrivate() && memberModel.isWritable()) {
        propertyModel = memberModel;
      }
    }
    return propertyModel;
  }

  private static Annotation findEvent(ClassDeclaration classModel, String propertyName) {
    for (ClassDeclaration current = classModel; current != null; current = current.getSuperTypeDeclaration()) {
      Annotation eventModel = getEvent(current, propertyName);
      if (eventModel != null) {
        return eventModel;
      }
    }
    return null;
  }

  private static Annotation getEvent(ClassDeclaration classDeclaration, String propertyName) {
    for (Annotation eventAnnotation : classDeclaration.getAnnotations("Event")) {
      CommaSeparatedList<AnnotationParameter> annotationParameters = eventAnnotation.getOptAnnotationParameters();
      while (annotationParameters != null) {
        Ide name = annotationParameters.getHead().getOptName();
        if (name != null && "name".equals(name.getName())) {
          AstNode value = annotationParameters.getHead().getValue();
          if (value instanceof LiteralExpr && propertyName.equals(value.getSymbol().getJooValue())) {
            return eventAnnotation;
          }
        }
        annotationParameters = annotationParameters.getTail();
      }
      
    }
    return null;
  }

  private static Annotation getAnnotationAtSetter(TypedIdeDeclaration memberModel, String annotationName) {
    TypedIdeDeclaration setter = memberModel.getSetter();
    return setter == null ? null : setter.getAnnotation(annotationName);
  }

  static boolean useConfigObjects(ClassDeclaration classModel) {
    // special case Plugin (avoid having to check all interfaces):
    CompilationUnit extPluginCompilationUnit = classModel.getIde().getScope().getCompiler().getCompilationUnit("ext.Plugin");
    if (extPluginCompilationUnit != null &&
            classModel.isAssignableTo((ClassDeclaration) extPluginCompilationUnit.getPrimaryDeclaration())) {
      return true;
    }
    for (ClassDeclaration current = classModel; current != null; current = current.getSuperTypeDeclaration()) {
      Iterator<Annotation> extConfigAnnotations = current.getAnnotations(Jooc.EXT_CONFIG_ANNOTATION_NAME).iterator();
      if (extConfigAnnotations.hasNext()) {
        Annotation extConfigAnnotation = extConfigAnnotations.next();
        return useConfigObjects(extConfigAnnotation, true);
      }
    }
    return false;
  }

  private static Boolean useConfigObjects(TypedIdeDeclaration propertyDeclaration) {
    Annotation extConfigAnnotation = getAnnotationAtSetter(propertyDeclaration, Jooc.EXT_CONFIG_ANNOTATION_NAME);
    return extConfigAnnotation == null ? null : useConfigObjects(extConfigAnnotation, null);
  }

  private static String getExtractXTypePropertyName(TypedIdeDeclaration propertyDeclaration) {
    Annotation extConfigAnnotation = getAnnotationAtSetter(propertyDeclaration, Jooc.EXT_CONFIG_ANNOTATION_NAME);
    if (extConfigAnnotation != null) {
      Map<String, Object> propertiesByName = extConfigAnnotation.getPropertiesByName();
      if (propertiesByName.containsKey(EXT_CONFIG_EXTRACT_XTYPE_PARAMETER)) {
        Object extractXType = propertiesByName.get(EXT_CONFIG_EXTRACT_XTYPE_PARAMETER);
        return extractXType instanceof String ? (String) extractXType : "";
      }
    }
    return null;
  }

  private static Boolean useConfigObjects(Annotation extConfigAnnotation, Boolean defaultValue) {
    CommaSeparatedList<AnnotationParameter> annotationParameters = extConfigAnnotation.getOptAnnotationParameters();
    while (annotationParameters != null) {
      AnnotationParameter annotationParameter = annotationParameters.getHead();
      Ide annotationParameterName = annotationParameter.getOptName();
      if (annotationParameterName != null) {
        if (EXT_CONFIG_CREATE_FLAG.equals(annotationParameterName.getName())) {
          AstNode value = annotationParameter.getValue();
          return value instanceof LiteralExpr && Boolean.FALSE.equals(((LiteralExpr) value).getValue().getJooValue());
        } else if (EXT_CONFIG_EXTRACT_XTYPE_PARAMETER.equals(annotationParameterName.getName())) {
          // a given "extractXType" parameter implies to use config objects (create=false):
          return true;
        }
      }
      annotationParameters = annotationParameters.getTail();
    }
    return defaultValue;
  }

  private static String getConfigOptionName(TypedIdeDeclaration propertyModel) {
    Annotation extConfigAnnotation = getAnnotationAtSetter(propertyModel, Jooc.EXT_CONFIG_ANNOTATION_NAME);
    if (extConfigAnnotation != null) {
      Object configOption = extConfigAnnotation.getPropertiesByName().get(null);
      if (configOption instanceof String) {
        return (String) configOption;
      }
    }
    return propertyModel.getName();
  }

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
    propertyDeclaration = findPropertyModel(type, localName);
    if (propertyDeclaration == null) {
      event = findEvent(type, localName);
      if (event == null) {
        if (!isUntypedAccess() && !type.isDynamic()) {
          return false;
        }
        propertyDeclaration = new VariableDeclaration(new JooSymbol("var"), new Ide(localName), new TypeRelation(new JooSymbol("*")));
      }
    } else {
      extractXTypePropertyName = getExtractXTypePropertyName(propertyDeclaration);
      instantiationMode = extractXTypePropertyName != null
              ? InstantiationMode.PLAIN
              : InstantiationMode.from(useConfigObjects(propertyDeclaration));
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

  String getPropertyTypeName() {
    return getPropertyTypeName(propertyDeclaration);
  }

  static String getPropertyTypeName(TypedIdeDeclaration propertyModel) {
    TypeRelation optTypeRelation;
    if (propertyModel.isMethod() && ((FunctionDeclaration) propertyModel).isSetter()) {
      Parameters params = ((FunctionDeclaration) propertyModel).getParams();
      optTypeRelation = params.getHead() == null ? null : params.getHead().getOptTypeRelation();
    } else {
      optTypeRelation = propertyModel.getOptTypeRelation();
    }
    return optTypeRelation == null ? null : optTypeRelation.getType().getIde().getQualifiedNameStr();
  }

  String getConfigOptionName() {
    return propertyDeclaration == null ? null : getConfigOptionName(propertyDeclaration);
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

  String getExtractXTypePropertyName() {
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
