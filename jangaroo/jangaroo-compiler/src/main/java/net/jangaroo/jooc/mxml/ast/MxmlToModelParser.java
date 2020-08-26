package net.jangaroo.jooc.mxml.ast;

import com.google.common.collect.Iterables;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.PropertyDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit.NET_JANGAROO_EXT_EXML;

final class MxmlToModelParser {

  private static final String EXML_NAMESPACE_URI = "http://www.jangaroo.net/exml/0.8";

  private static final String EXT_CONFIG_CREATE_FLAG = "create";
  private static final String EXT_CONFIG_EXTRACT_XTYPE_PARAMETER = "extractXType";

  private static final String CONFIG_MODE_AT_SUFFIX = "$at";
  private static final String CONFIG_MODE_ATTRIBUTE_NAME = "mode";
  private static final Map<String, String> CONFIG_MODE_TO_AT_VALUE = new HashMap<>();

  private static final String UNTYPED_MARKER = "__UNTYPED__";
  public static final List<String> PRIMITIVE_TYPE_NAMES = Arrays.asList("int", "uint", "Number", "Boolean");

  static {
    CONFIG_MODE_TO_AT_VALUE.put("append", "net.jangaroo.ext.Exml.APPEND");
    CONFIG_MODE_TO_AT_VALUE.put("prepend", "net.jangaroo.ext.Exml.PREPEND");
  }

  private final JangarooParser jangarooParser;
  private final MxmlParserHelper mxmlParserHelper;
  private final MxmlCompilationUnit compilationUnit;

  private final Collection<Directive> constructorBodyDirectives = new LinkedList<>();
  private final Collection<Directive> classBodyDirectives = new LinkedList<>();

  MxmlToModelParser(JangarooParser jangarooParser, MxmlParserHelper mxmlParserHelper, MxmlCompilationUnit mxmlCompilationUnit) {
    this.jangarooParser = jangarooParser;
    this.mxmlParserHelper = mxmlParserHelper;
    this.compilationUnit = mxmlCompilationUnit;
  }

  private List<ObjectField> processAttributes(List<ObjectField> listenerFields, XmlElement objectNode, CompilationUnit type) {
    ClassDeclaration classModel = type == null ? null : (ClassDeclaration) type.getPrimaryDeclaration();
    boolean hasIdAttribute = false;
    List<ObjectField> fields = new ArrayList<>();
    for (XmlAttribute attribute : objectNode.getAttributes()) {
      if (RootElementProcessor.alreadyProcessed(attribute)) {
        continue;
      }
      String propertyName = attribute.getLocalName();
      boolean noPrefix = attribute.getPrefix() == null;
      hasIdAttribute |= noPrefix && MxmlUtils.MXML_ID_ATTRIBUTE.equals(propertyName);
      String attributeNamespaceUri = objectNode.getNamespaceUri(attribute.getPrefix());
      boolean isUntypedAccess = MxmlUtils.EXML_UNTYPED_NAMESPACE.equals(attributeNamespaceUri);
      if (noPrefix && !MxmlUtils.MXML_ID_ATTRIBUTE.equals(propertyName) ||
              isUntypedAccess) {
        JooSymbol value = attribute.getValue();
        TypedIdeDeclaration propertyModel = null;
        if (!isUntypedAccess && classModel != null) {
          propertyModel = findPropertyModel(classModel, propertyName);
          if (propertyModel == null) {
            Annotation eventModel = findEvent(classModel, propertyName);
            if (eventModel != null) {
              listenerFields.add(createEventHandlerCode(value, eventModel));
              continue;
            }
          }
        }
        if (propertyModel == null) {
          propertyModel = createDynamicPropertyModel(objectNode, type, propertyName, isUntypedAccess);
        }
        String className = getTypeAsClassName(propertyModel);
        Expr valueExpr = createValueExprFromTextSymbol(value, className);
        if (valueExpr != null) {
          fields.add(createPropertyAssignmentCode(propertyModel, valueExpr));
        }
      }
    }
    return fields;
  }

  private static String getTypeAsClassName(TypedIdeDeclaration propertyModel) {
    TypeRelation typeRelation = propertyModel.getOptTypeRelation();
    String className = typeRelation == null ? "*" : typeRelation.getType().getIde().getQualifiedNameStr();
    if (UNTYPED_MARKER.equals(className)) {
      className = "*";
    }
    return className;
  }


  private static Annotation getAnnotationAtSetter(TypedIdeDeclaration memberModel, String annotationName) {
    TypedIdeDeclaration setter = null;
    if (memberModel instanceof PropertyDeclaration) {
      setter = ((PropertyDeclaration) memberModel).getSetter();
    } else if (memberModel instanceof VariableDeclaration && !((VariableDeclaration) memberModel).isConst()) {
      setter = memberModel;
    }
    if (setter != null) {
      Iterator<Annotation> annotations = setter.getAnnotations(annotationName).iterator();
      if (annotations.hasNext()) {
        return annotations.next();
      }
    }
    return null;
  }

  ObjectLiteral createObjectLiteralForAttributesAndChildNodes(XmlElement objectNode) {
    return MxmlAstUtils.createObjectLiteral(createObjectFieldsForAttributesAndChildNodes(objectNode));
  }

  private List<ObjectField> createObjectFieldsForAttributesAndChildNodes(XmlElement objectNode) {
    List<ObjectField> fields = new ArrayList<>();
    List<ObjectField> listenerFields = new ArrayList<>();
    if (!objectNode.getAttributes().isEmpty() || !objectNode.getElements().isEmpty()) {
      CompilationUnit type = getCompilationUnitModel(objectNode);
      fields.addAll(processAttributes(listenerFields, objectNode, type));
      fields.addAll(processChildNodes(listenerFields, objectNode, type));
    }
    if (!listenerFields.isEmpty()) {
      fields.add(MxmlAstUtils.createObjectField("listeners",
              MxmlAstUtils.createObjectLiteral(listenerFields)));
    }
    return fields;
  }

  private List<ObjectField> processChildNodes(List<ObjectField> listenerFields, XmlElement objectNode, CompilationUnit type) {
    ClassDeclaration classModel = type == null ? null : (ClassDeclaration) type.getPrimaryDeclaration();
    List<XmlElement> childNodes = objectNode.getElements();
    TypedIdeDeclaration defaultPropertyModel = findDefaultPropertyModel(classModel);
    List<XmlElement> defaultPropertyValues = new ArrayList<>();
    List<ObjectField> fields = new ArrayList<>();
    for (XmlElement element : childNodes) {
      if (!MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) { // ignore MXML namespace; has been handled before.
        TypedIdeDeclaration propertyModel = null;
        String propertyName = element.getLocalName();
        if (objectNode.getNamespaceURI().equals(element.getNamespaceURI())) {
          if (classModel != null) {
            propertyModel = findPropertyModel(classModel, propertyName);
            if (propertyModel == null) {
              Annotation eventModel = findEvent(classModel, propertyName);
              if (eventModel != null) {
                JooSymbol textContent = getTextContent(element);
                listenerFields.add(createEventHandlerCode(textContent, eventModel));
                continue;
              }
            }
          }
        }
        if (propertyModel == null && defaultPropertyModel != null) {
          // collect item to add it to the default property later:
          defaultPropertyValues.add(element);
        } else {
          if (propertyModel == null) {
            propertyModel = createDynamicPropertyModel(element, type, propertyName, false);
          }
          List<XmlElement> childElements = element.getElements();
          if (childElements.isEmpty()) {
            Expr valueExpr = createValueExprFromTextSymbol(getTextContent(element), getTypeAsClassName(propertyModel));
            if (valueExpr == null && hasArrayLikeType(propertyModel)) { // TODO: what about empty Object etc.?
              valueExpr = MxmlAstUtils.createArrayLiteral(Collections.emptyList());
            }
            if (valueExpr != null) {
              fields.add(createPropertyAssignmentCode(propertyModel, valueExpr));
            }
          } else {
            if (MxmlUtils.EXML_MIXINS_PROPERTY_NAME.equals(getConfigOptionName(propertyModel))) {
              for (XmlElement arrayItemNode : childElements) {
                List<ObjectField> mixinFields = createObjectFieldsForAttributesAndChildNodes(arrayItemNode);
                fields.addAll(mixinFields);
              }
            } else {
              Annotation extConfigAnnotation = getAnnotationAtSetter(propertyModel, Jooc.EXT_CONFIG_ANNOTATION_NAME);
              Boolean useConfigObjects = extConfigAnnotation == null ? null : useConfigObjects(extConfigAnnotation, null);
              fields.add(createPropertyAssignmentCode(propertyModel, createArrayExprFromChildElements(childElements, hasArrayLikeType(propertyModel), useConfigObjects)));
            }
          }
          String configMode = getConfigMode(element, propertyModel);
          String atValue = CONFIG_MODE_TO_AT_VALUE.get(configMode);
          if (atValue != null) {
            String atPropertyName = getConfigOptionName(propertyModel);
            fields.add(MxmlAstUtils.createObjectField(atPropertyName + CONFIG_MODE_AT_SUFFIX, new IdeExpr(mxmlParserHelper.parseIde(atValue))));
          }
        }
      }
    }
    if (!defaultPropertyValues.isEmpty()) {
      fields.addAll(createChildElementsPropertyAssignmentCode(defaultPropertyValues, defaultPropertyModel));
    }
    return fields;
  }

  private static String getConfigMode(XmlElement element, TypedIdeDeclaration propertyModel) {
    if (hasArrayLikeType(propertyModel)) {
      String configMode = element.getAttributeNS(EXML_NAMESPACE_URI, CONFIG_MODE_ATTRIBUTE_NAME);
      if (!configMode.isEmpty()) {
        return configMode;
      }
      Annotation extConfigAnnotation = propertyModel.getAnnotation(Jooc.EXT_CONFIG_ANNOTATION_NAME);
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
    }
    return "";
  }

  private List<ObjectField> createChildElementsPropertyAssignmentCode(List<XmlElement> childElements,
                                                                      TypedIdeDeclaration propertyModel) {
    boolean forceArray = hasArrayLikeType(propertyModel); // TODO: improve Array detection!
    Annotation extConfigAnnotation = getAnnotationAtSetter(propertyModel, Jooc.EXT_CONFIG_ANNOTATION_NAME);
    Boolean useConfigObjects = extConfigAnnotation == null ? null : useConfigObjects(extConfigAnnotation, null);
    Expr value = createArrayExprFromChildElements(childElements, forceArray, useConfigObjects);
    List<ObjectField> fields = new ArrayList<>();
    if (extConfigAnnotation != null) {
      Map<String, Object> propertiesByName = extConfigAnnotation.getPropertiesByName();
      if (propertiesByName.containsKey(EXT_CONFIG_EXTRACT_XTYPE_PARAMETER)) {
        Object extractXType = propertiesByName.get(EXT_CONFIG_EXTRACT_XTYPE_PARAMETER);
        if (extractXType == null || extractXType instanceof String) {
          String extractXTypeToProperty = (String) extractXType;
          if (extractXTypeToProperty != null) {
            fields.add(MxmlAstUtils.createObjectField(extractXTypeToProperty, MxmlAstUtils.createArrayIndexExpr(value, "xtype")));
          }
        }
      }
    }
    createPropertyAssignmentCode(propertyModel, value);
    return fields;
  }

  private static boolean hasArrayLikeType(TypedIdeDeclaration propertyModel) {
    return propertyModel.getIde().getScope().getExpressionType(propertyModel.getOptTypeRelation()).isArrayLike();
  }

  private Expr createArrayExprFromChildElements(List<XmlElement> childElements, boolean forceArray, Boolean useConfigObjects) {
    List<Expr> arrayItems = new ArrayList<>();
    for (XmlElement arrayItemNode : childElements) {
      Expr itemValue = createExprFromElement(arrayItemNode, useConfigObjects, true);
      arrayItems.add(itemValue);
    }
    Expr value;
    if (arrayItems.size() > 1 || forceArray) {
      // We must create an array.
      value = MxmlAstUtils.createArrayLiteral(arrayItems);
    } else {
      // The property is either unspecified, untyped, or object-typed
      // and it contains at least one child element. Use the first element as the
      // property value.
      value = arrayItems.isEmpty() ? MxmlAstUtils.createNullLiteral() : arrayItems.get(0);
    }
    return value;
  }

  @Nullable
  Expr createExprFromElement(XmlElement objectElement, Boolean defaultUseConfigObjects, boolean generatingConfig) {
    String className;
    try {
      className = mxmlParserHelper.getClassNameForElement(jangarooParser, objectElement);
    } catch (CompilerError e) {
      // rewrite compiler error so that the source is the current symbol
      throw JangarooParser.error(objectElement.getSymbol(), e.getMessage(), e.getCause());
    }
    Ide typeIde = compilationUnit.addImport(className);
    XmlAttribute idAttribute = objectElement.getAttribute(MxmlUtils.MXML_ID_ATTRIBUTE);
    String id = null;
    String additionalDeclaration;
    if (null != idAttribute) {
      JooSymbol idSymbol = idAttribute.getValue();
      id = (String) idSymbol.getJooValue();
      if (id.equals(compilationUnit.getConstructorParamName())) {
        return null;
      }

      Ide.verifyIdentifier(id, idSymbol);

      VariableDeclaration variableDeclaration = compilationUnit.getVariables().get(id);
      if (null == variableDeclaration) {
        String asDoc = MxmlUtils.toASDoc(objectElement.getSymbol().getWhitespace());
        int i = asDoc.lastIndexOf('\n');
        additionalDeclaration = asDoc +
                '[' + Jooc.BINDABLE_ANNOTATION_NAME + ']' +
                (i < 0 ? "\n" : asDoc.substring(i)) +
                "public var " + id + ':' + className + ';';
        Collection<Directive> directives = mxmlParserHelper.parseClassBody(new JooSymbol(additionalDeclaration)).getDirectives();
        Iterator<Directive> directiveIterator = directives.iterator();
        if (!directiveIterator.hasNext()) {
          throw new IllegalStateException("MXML: parsing generated field declaration with name '"+ id + "' failed.");
        }
        variableDeclaration = (VariableDeclaration) directiveIterator.next();
        classBodyDirectives.add(variableDeclaration);
      }
      if (generatingConfig && variableDeclaration.isExtConfig() && isMxmlDeclarations(objectElement.getParentNode())) {
        // default values are applied through the config object, not directly on 'this':
        id = null;
      }
    }

    Expr valueExpr;
    JooSymbol textContentSymbol = getTextContent(objectElement);
    String textContent = textContentSymbol.getText().trim();
    if (textContent.isEmpty()) {
      // suppress fields with only an id attribute (no other attributes, no sub-elements, no text content):
      if (idAttribute != null && objectElement.getElements().isEmpty() && objectElement.getAttributes().size() == 1) {
        return null;
      }
      if ("Object".equals(className)) {
        valueExpr = createObjectLiteralForAttributesAndChildNodes(objectElement);
      } else if ("Array".equals(className)) {
        valueExpr = createArrayExprFromChildElements(objectElement.getElements(), true, defaultUseConfigObjects);
      } else  {
        // process attributes and children:
        ObjectLiteral configObjectLiteral = createObjectLiteralForAttributesAndChildNodes(objectElement);
        valueExpr = MxmlAstUtils.createCastExpr(typeIde, configObjectLiteral);
        if (idAttribute != null || !useConfigObjects(defaultUseConfigObjects, className)) {
          valueExpr = MxmlAstUtils.createNewExpr(typeIde, valueExpr);
        }
      }
    } else {
      valueExpr = createValueExprFromTextSymbol(textContentSymbol, className);
      if (valueExpr == null) {
        valueExpr = MxmlAstUtils.createNewExpr(typeIde);
      } else {
        if (!(objectElement.getElements().isEmpty() && objectElement.getAttributes().size() == (idAttribute == null ? 0 : 1))) {
          throw Jooc.error(textContentSymbol, String.format("Unexpected text inside MXML element: '%s'.", textContent));
        }
        if (valueExpr instanceof IdeExpr && ("undefined".equals(((IdeExpr) valueExpr).getIde().getName()))) {
          return null;
        }
      }
    }
    if (id != null && valueExpr != null) {
      valueExpr = MxmlAstUtils.createAssignmentOpExpr(MxmlAstUtils.createDotExpr(
              new Ide(new JooSymbol(Ide.THIS).withWhitespace(MxmlAstUtils.INDENT_4)), id), valueExpr);
    }
    return valueExpr;
  }

  private static boolean isMxmlDeclarations(AstNode mxmlNode) {
    return mxmlNode instanceof XmlElement && isMxmlDeclarations((XmlElement) mxmlNode);
  }

  private static boolean isMxmlDeclarations(XmlElement mxmlNode) {
    return MxmlUtils.isMxmlNamespace(mxmlNode.getNamespaceURI())
            && MxmlUtils.MXML_DECLARATIONS.equals(mxmlNode.getName());
  }

  private Expr createValueExprFromTextSymbol(JooSymbol textContentSymbol, String className) {
    String textContent = ((String) textContentSymbol.getJooValue()).trim();
    if (textContent.isEmpty()) {
      if (PRIMITIVE_TYPE_NAMES.contains(className)) {
        return new IdeExpr(new Ide("undefined"));
      }
      return null;
    }
    String value = MxmlUtils.valueToString(MxmlUtils.getAttributeValue(textContent, className));
    Expr valueExpr = mxmlParserHelper.parseExpression(value.equals(textContentSymbol.getText())
            ? textContentSymbol
            : textContentSymbol.replacingSymAndTextAndJooValue(textContentSymbol.sym, value, value));
    // special case: String properties auto-cast any right-hand-side into a String:
    if ("String".equals(className) && !(valueExpr instanceof LiteralExpr && ((LiteralExpr)valueExpr).getValue().getJooValue() instanceof String)) {
      valueExpr = MxmlAstUtils.createApplyExpr(
              MxmlAstUtils.createDotExpr(compilationUnit.addImport(NET_JANGAROO_EXT_EXML), "asString"),
              valueExpr);
    }
    return valueExpr;
  }

  private boolean useConfigObjects(Boolean defaultUseConfigObjects, String className) {
    if (defaultUseConfigObjects != null) {
      return defaultUseConfigObjects;
    }
    ClassDeclaration classModel = (ClassDeclaration) jangarooParser.resolveCompilationUnit(className).getPrimaryDeclaration();
    // special case Plugin (avoid having to check all interfaces):
    CompilationUnit extPluginCompilationUnit = jangarooParser.getCompilationUnit("ext.Plugin");
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

  private ObjectField createEventHandlerCode(@Nonnull JooSymbol value, @Nonnull Annotation event) {
    Map<String, Object> eventPropertiesByName = event.getPropertiesByName();
    Object eventType = eventPropertiesByName.get("type");
    Ide eventTypeIde = compilationUnit.addImport(eventType instanceof String ? (String) eventType : "Object");
    Object eventNameModel = eventPropertiesByName.get(Jooc.EVENT_ANNOTATION_NAME_ATTRIBUTE_NAME);
    String eventName = (String) (eventNameModel != null ? eventNameModel : eventPropertiesByName.get(null));
    if (eventName.startsWith("on")) {
      eventName = eventName.substring(2);
    }
    String eventNameConstant = (eventName.substring(0, 1) + eventName.substring(1).replaceAll("([A-Z])", "_$1")).toUpperCase();
    String eventHandlerName = "$on_" + eventName.replace('-', '_')
            + "_" + value.getLine() + "_" + value.getColumn();
    String classBodyCode = "private function " + eventHandlerName +
            " (" + "event" + ':' + eventTypeIde.getQualifiedNameStr() + ") :void {\n" +
            "    " + value.getJooValue() +
            "}";
    classBodyDirectives.addAll(mxmlParserHelper.parseClassBody(new JooSymbol(classBodyCode)).getDirectives());

    return MxmlAstUtils.createObjectField(eventName,
            MxmlAstUtils.createApplyExpr(
                    MxmlAstUtils.createDotExpr(compilationUnit.addImport(NET_JANGAROO_EXT_EXML), "eventHandler"),
                    MxmlAstUtils.createDotExpr(eventTypeIde, eventNameConstant),
                    new IdeExpr(eventTypeIde),
                    new IdeExpr(new Ide(eventHandlerName))
            ));
  }

  private ObjectField createPropertyAssignmentCode(@Nonnull TypedIdeDeclaration propertyModel, @Nonnull Expr value) {
    return MxmlAstUtils.createObjectField(getConfigOptionName(propertyModel), value);
  }

  @Nonnull
  private Directive createPropertyAssigment(@Nonnull Ide variable, @Nonnull TypedIdeDeclaration propertyModel, @Nonnull JooSymbol value, boolean generatingConfig) {
    TypeRelation typeRelation = propertyModel.getOptTypeRelation();
    String propertyType = typeRelation == null ? null : typeRelation.getType().getIde().getName();
    boolean untyped = UNTYPED_MARKER.equals(propertyType);
    String attributeValueAsString = MxmlUtils.valueToString(MxmlUtils.getAttributeValue((String) value.getJooValue(), untyped ? null : propertyType));

    String propertyName = generatingConfig ? getConfigOptionName(propertyModel) : propertyModel.getName();
    boolean untypedAccess = untyped || !propertyName.equals(propertyModel.getName());

    Expr rightHandSide = mxmlParserHelper.parseExpression(value.replacingSymAndTextAndJooValue(value.sym, attributeValueAsString, attributeValueAsString));
    // special case: String properties auto-cast any right-hand-side into a String:
    if ("String".equals(propertyType) && !(rightHandSide instanceof LiteralExpr && ((LiteralExpr)rightHandSide).getValue().getJooValue() instanceof String)) {
      rightHandSide = MxmlAstUtils.createApplyExpr(
              MxmlAstUtils.createDotExpr(compilationUnit.addImport(NET_JANGAROO_EXT_EXML), "asString"),
              rightHandSide);
    }
    return MxmlAstUtils.createPropertyAssignment(variable, rightHandSide, propertyName, untypedAccess);
  }

  private static String getConfigOptionName(TypedIdeDeclaration propertyModel) {
    TypedIdeDeclaration setter = propertyModel instanceof PropertyDeclaration ? ((PropertyDeclaration) propertyModel).getSetter() : propertyModel;
    if (setter != null) { // should actually be there, or the assignment would not work!
      Iterator<Annotation> configOptionAnnotations = setter.getAnnotations(Jooc.EXT_CONFIG_ANNOTATION_NAME).iterator();
      if (configOptionAnnotations.hasNext()) {
        Object configOption = configOptionAnnotations.next().getPropertiesByName().get(null);
        if (configOption instanceof String) {
          return (String) configOption;
        }
      }
    }
    return propertyModel.getName();
  }

  // ======================================== auxiliary methods ========================================

  @Nonnull
  private CompilationUnit getCompilationUnitModel(XmlElement element) {
    String fullClassName = mxmlParserHelper.getClassNameForElement(jangarooParser, element);
    try {
      return jangarooParser.resolveCompilationUnit(fullClassName);
    } catch (CompilerError e) {
      // ugly to catch-and-rethrow, I know, but we need to add position information here...
      throw JangarooParser.error(element, e.getMessage());
    }
  }

  private TypedIdeDeclaration findPropertyModel(ClassDeclaration classModel, String propertyName) {
    for (ClassDeclaration currentClassModel = classModel;
         currentClassModel != null;
         currentClassModel = currentClassModel.getSuperTypeDeclaration()) {
      TypedIdeDeclaration memberModel = currentClassModel.getMemberDeclaration(propertyName);
      if (memberModel != null && !memberModel.isPrivate() && memberModel.isWritable()) {
        return memberModel;
      }
    }
    return null;
  }

  private Annotation findEvent(ClassDeclaration classModel, String propertyName) {
    for (ClassDeclaration current = classModel; current != null; current = current.getSuperTypeDeclaration()) {
      Annotation eventModel = getEvent(current, propertyName);
      if (eventModel != null) {
        return eventModel;
      }
    }
    return null;
  }

  private Annotation getEvent(ClassDeclaration classDeclaration, String propertyName) {
    for (Annotation eventAnnotation : classDeclaration.getAnnotations(Jooc.EVENT_ANNOTATION_NAME)) {
      CommaSeparatedList<AnnotationParameter> annotationParameters = eventAnnotation.getOptAnnotationParameters();
      while (annotationParameters != null) {
        Ide name = annotationParameters.getHead().getOptName();
        if (name != null && Jooc.EVENT_ANNOTATION_NAME_ATTRIBUTE_NAME.equals(name.getName())) {
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

  private TypedIdeDeclaration findDefaultPropertyModel(ClassDeclaration classModel) {
    for (ClassDeclaration current = classModel; current != null; current = current.getSuperTypeDeclaration()) {
      TypedIdeDeclaration defaultPropertyModel = findPropertyWithAnnotation(current, MxmlUtils.MXML_DEFAULT_PROPERTY_ANNOTATION);
      if (defaultPropertyModel != null) {
        return defaultPropertyModel;
      }
    }
    return null;
  }

  private TypedIdeDeclaration findPropertyWithAnnotation(ClassDeclaration current, String annotation) {
    for (TypedIdeDeclaration member : current.getMembers()) {
      if (!member.getAnnotations(annotation).isEmpty()) {
        return member;
      }
    }
    return null;
  }

  @Nonnull
  private TypedIdeDeclaration createDynamicPropertyModel(XmlElement element, CompilationUnit compilationUnitModel, String name, boolean allowAnyProperty) {
    if (!allowAnyProperty && compilationUnitModel != null && compilationUnitModel.getPrimaryDeclaration() != null && !compilationUnitModel.getPrimaryDeclaration().isDynamic()) {
      // dynamic property of a non-dynamic class: warn!
      jangarooParser.getLog().error(element.getSymbol(), "MXML: property " + name + " not found in class " + compilationUnitModel.getQualifiedNameStr() + ".");
    }
    return new VariableDeclaration(new JooSymbol("var"), new Ide(name), new TypeRelation(new JooSymbol(allowAnyProperty ? UNTYPED_MARKER : "*")));
  }

  @Nonnull
  private static JooSymbol getTextContent(XmlElement element) {
    //noinspection ConstantConditions
    return Iterables.getFirst(element.getTextNodes(), new JooSymbol(""));
  }

  Collection<Directive> getConstructorBodyDirectives() {
    return constructorBodyDirectives;
  }

  Collection<Directive> getClassBodyDirectives() {
    return classBodyDirectives;
  }
}
