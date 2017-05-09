package net.jangaroo.jooc.mxml.ast;

import com.google.common.collect.Iterables;
import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.ArrayIndexExpr;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.PropertyDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

final class MxmlToModelParser {

  private static final String EXT_CONFIG_CREATE_FLAG = "create";
  private static final String EXT_CONFIG_EXTRACT_XTYPE_PARAMETER = "extractXType";

  private static final String CONFIG_MODE_AT_SUFFIX = "$at";
  private static final String CONFIG_MODE_ATTRIBUTE_NAME = "mode";
  private static final Map<String, String> CONFIG_MODE_TO_AT_VALUE = new HashMap<>();

  private static final String DELETE_OBJECT_PROPERTY_CODE = "\n    delete %s['%s'];";
  private static final String UNTYPED_MARKER = "__UNTYPED__";

  static {
    CONFIG_MODE_TO_AT_VALUE.put("append", "net.jangaroo.ext.Exml.APPEND");
    CONFIG_MODE_TO_AT_VALUE.put("prepend", "net.jangaroo.ext.Exml.PREPEND");
  }

  private final JangarooParser jangarooParser;
  private final MxmlParserHelper mxmlParserHelper;
  private final MxmlCompilationUnit compilationUnit;

  private final Collection<Directive> constructorBodyDirectives = new LinkedList<>();
  private final Collection<Directive> classBodyDirectives = new LinkedList<>();
  String additionalDeclarations = "";

  MxmlToModelParser(JangarooParser jangarooParser, MxmlParserHelper mxmlParserHelper, MxmlCompilationUnit mxmlCompilationUnit) {
    this.jangarooParser = jangarooParser;
    this.mxmlParserHelper = mxmlParserHelper;
    this.compilationUnit = mxmlCompilationUnit;
  }

  private void renderConfigAuxVar(@Nonnull Ide ide, Ide type) {
    constructorBodyDirectives.add(MxmlAstUtils.createVariableDeclaration(ide, type));
  }

  private void processAttributes(XmlElement objectNode, CompilationUnit type, Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig) {
    Ide variable = generatingConfig ? configVariable : targetVariable;
    ClassDeclaration classModel = type == null ? null : (ClassDeclaration) type.getPrimaryDeclaration();
    boolean hasIdAttribute = false;
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
              createEventHandlerCode(variable, value, eventModel);
              continue;
            }
          }
        }
        if (propertyModel == null) {
          propertyModel = createDynamicPropertyModel(objectNode, type, propertyName, isUntypedAccess);
        }
        createPropertyAssignmentCodeWithBindings(configVariable, targetVariable, generatingConfig, value, propertyModel);
      }
    }
  }

  private void createPropertyAssignmentCodeWithBindings(Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig, @Nonnull JooSymbol value, @Nonnull TypedIdeDeclaration propertyModel) {
    Ide variable = generatingConfig ? configVariable : targetVariable;
    // skip property assignment to target object if it was already contained in config object:
    if (generatingConfig || configVariable == null) {
      // default: create a normal property assignment:
      createPropertyAssignmentCode(variable, propertyModel, value, generatingConfig);
    }
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

  void processAttributesAndChildNodes(XmlElement objectNode, Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig) {
    if (!objectNode.getAttributes().isEmpty() || !objectNode.getElements().isEmpty()) {
      CompilationUnit type = getCompilationUnitModel(objectNode);
      processAttributes(objectNode, type, configVariable, targetVariable, generatingConfig);
      processChildNodes(objectNode, type, configVariable, targetVariable, generatingConfig);
    }
  }

  private void processChildNodes(XmlElement objectNode, CompilationUnit type, Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig) {
    Ide variable = generatingConfig ? configVariable : targetVariable;
    ClassDeclaration classModel = type == null ? null : (ClassDeclaration) type.getPrimaryDeclaration();
    List<XmlElement> childNodes = objectNode.getElements();
    TypedIdeDeclaration defaultPropertyModel = findDefaultPropertyModel(classModel);
    List<XmlElement> defaultPropertyValues = new ArrayList<>();
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
                createEventHandlerCode(variable, textContent, eventModel);
                continue;
              }
            } else if (MxmlUtils.EXML_MIXINS_PROPERTY_NAME.equals(getConfigOptionName(propertyModel))) {
              List<XmlElement> exmlMixins = element.getElements();
              for (XmlElement exmlMixin : exmlMixins) {
                processAttributesAndChildNodes(exmlMixin, configVariable, targetVariable, generatingConfig);
              }
              continue;
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
            JooSymbol textContent = getTextContent(element);
            createPropertyAssignmentCodeWithBindings(configVariable, targetVariable, generatingConfig, textContent, propertyModel);
          } else {
            createChildElementsPropertyAssignmentCode(childElements, variable, propertyModel, generatingConfig);
          }
          String configMode = getConfigMode(element, propertyModel);
          String atValue = CONFIG_MODE_TO_AT_VALUE.get(configMode);
          if (atValue != null) {
            String atPropertyName = generatingConfig ? getConfigOptionName(propertyModel) : propertyModel.getName();
            Expr dotExpr = new ArrayIndexExpr(new IdeExpr(new Ide(variable.getIde().withWhitespace("\n    "))), MxmlAstUtils.SYM_LBRACK, new LiteralExpr(new JooSymbol(CompilerUtils.quote(atPropertyName + CONFIG_MODE_AT_SUFFIX))), MxmlAstUtils.SYM_RBRACK);
            IdeExpr ideExpr = new IdeExpr(mxmlParserHelper.parseIde(" " + atValue));
            constructorBodyDirectives.add(MxmlAstUtils.createSemicolonTerminatedStatement(new AssignmentOpExpr(dotExpr, MxmlAstUtils.SYM_EQ.withWhitespace(" "), ideExpr)));
          }
        }
      }
    }
    if (!defaultPropertyValues.isEmpty()) {
      createChildElementsPropertyAssignmentCode(defaultPropertyValues, variable, defaultPropertyModel, generatingConfig);
    }
  }

  private static String getConfigMode(XmlElement element, TypedIdeDeclaration propertyModel) {
    if ("Array".equals(propertyModel.getOptTypeRelation().getType().getIde().getName())) {
      String configMode = element.getAttributeNS(Exmlc.EXML_NAMESPACE_URI, CONFIG_MODE_ATTRIBUTE_NAME);
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

  private void createChildElementsPropertyAssignmentCode(List<XmlElement> childElements, Ide variable,
                                                         TypedIdeDeclaration propertyModel, boolean generatingConfig) {
    boolean forceArray = "Array".equals(propertyModel.getOptTypeRelation().getType().getIde().getName()); // TODO: improve Array detection!
    Annotation extConfigAnnotation = getAnnotationAtSetter(propertyModel, Jooc.EXT_CONFIG_ANNOTATION_NAME);
    Boolean useConfigObjects = extConfigAnnotation == null ? null : useConfigObjects(extConfigAnnotation, null);
    String value = createArrayCodeFromChildElements(childElements, forceArray, useConfigObjects);
    if (extConfigAnnotation != null) {
      Map<String, Object> propertiesByName = extConfigAnnotation.getPropertiesByName();
      if (propertiesByName.containsKey(EXT_CONFIG_EXTRACT_XTYPE_PARAMETER)) {
        Object extractXType = propertiesByName.get(EXT_CONFIG_EXTRACT_XTYPE_PARAMETER);
        if (extractXType == null || extractXType instanceof String) {
          String extractXTypeToProperty = (String) extractXType;
          StringBuilder constructorCode = new StringBuilder();
          if (extractXTypeToProperty != null) {
            constructorCode.append("    ")
                    .append(getPropertyAssignmentCode(variable, extractXTypeToProperty, value + "['xtype']"));
          }
          constructorCode.append(String.format(DELETE_OBJECT_PROPERTY_CODE, value, "xtype"));
          constructorCode.append(String.format(DELETE_OBJECT_PROPERTY_CODE, value, "xclass"));
          constructorBodyDirectives.addAll(mxmlParserHelper.parseConstructorBody(constructorCode.toString()));
        }
      }
    }
    createPropertyAssignmentCode(variable, propertyModel, new JooSymbol(MxmlUtils.createBindingExpression(value)), generatingConfig);
  }

  private String createArrayCodeFromChildElements(List<XmlElement> childElements, boolean forceArray, Boolean useConfigObjects) {
    List<String> arrayItems = new ArrayList<>();
    for (XmlElement arrayItemNode : childElements) {
      String itemValue = createValueCodeFromElement(null, arrayItemNode, useConfigObjects);
      arrayItems.add(itemValue);
    }
    String value;
    if (arrayItems.size() > 1 || forceArray) {
      // We must create an array.
      value = "[" + join(arrayItems, ", ") + "]";
    } else {
      // The property is either unspecified, untyped, or object-typed
      // and it contains at least one child element. Use the first element as the
      // property value.
      value = arrayItems.isEmpty() ? "null" : arrayItems.get(0);
    }
    return value;
  }

  private static String join(List<String> array, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.size(); ++i) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(array.get(i));
    }
    return sb.toString();
  }

  @Nullable
  String createValueCodeFromElement(@Nullable Ide configVar, XmlElement objectElement, Boolean defaultUseConfigObjects) {
    String className;
    try {
      className = mxmlParserHelper.getClassNameForElement(jangarooParser, objectElement);
    } catch (CompilerError e) {
      // rewrite compiler error so that the source is the current symbol
      throw JangarooParser.error(objectElement.getSymbol(), e.getMessage(), e.getCause());
    }
    Ide typeIde = compilationUnit.addImport(className);
    String targetVariableName = null;   // name of the variable holding the object to build
    XmlAttribute idAttribute = objectElement.getAttribute(MxmlUtils.MXML_ID_ATTRIBUTE);
    String id = null;
    String additionalDeclaration = null;
    if (null != idAttribute) {
      JooSymbol idSymbol = idAttribute.getValue();
      id = (String) idSymbol.getJooValue();
      if (id.equals(compilationUnit.getConstructorParamName())) {
        return null;
      }

      Ide.verifyIdentifier(id, idSymbol);

      VariableDeclaration variableDeclaration = compilationUnit.getVariables().get(id);
      String qualifier = null != configVar ? configVar.getName() : "";
      if (null != variableDeclaration) {
        if (!variableDeclaration.isPublic()) {
          qualifier = ""; // corresponds to "this."
        }
      } else {
        String asDoc = MxmlUtils.toASDoc(objectElement.getSymbol().getWhitespace());
        int i = asDoc.lastIndexOf('\n');
        StringBuilder classBodyCode = new StringBuilder();
        classBodyCode
                .append(asDoc)
                .append('[').append(Jooc.BINDABLE_ANNOTATION_NAME).append(']')
                .append(i < 0 ? "\n" : asDoc.substring(i))
                .append("public var ").append(id).append(':').append(className).append(';');
        additionalDeclaration = classBodyCode.toString();
        classBodyDirectives.addAll(mxmlParserHelper.parseClassBody(new JooSymbol(additionalDeclaration)).getDirectives());
      }
      targetVariableName = CompilerUtils.qName(qualifier, id);
    }

    if (id != null && configVar != null // it is a declaration...
            && objectElement.getAttributes().size() == 1 // ...with only an id attribute...
            && objectElement.getChildren().isEmpty() && objectElement.getTextNodes().isEmpty()) {
      // prevent assigning a default value for such an empty declaration:
      additionalDeclarations += additionalDeclaration;
      return null;
    }

    Ide configVariable = null; // name of the variable holding the config object to use in the constructor

    if (Boolean.TRUE.equals(defaultUseConfigObjects) ||
            CompilationUnitUtils.constructorSupportsConfigOptionsParameter(className, jangarooParser)) {
      // if class supports a config options parameter, create a config options object and assign properties to it:
      configVariable = createAuxVar(objectElement, id);
      renderConfigAuxVar(configVariable, typeIde != null ? typeIde : new Ide(className));
      if (targetVariableName == null) {
        targetVariableName = createAuxVar(objectElement).getName();
      }
      // process attributes and children, using a forward reference to the object to build inside bindings:
      processAttributesAndChildNodes(objectElement, configVariable, new Ide(targetVariableName), true);
    }

    String value = createValueCodeFromElement(objectElement, defaultUseConfigObjects, className, configVariable);

    StringBuilder constructorCode = new StringBuilder();
    if (null != id) {
      constructorCode.append("    ").append(targetVariableName);
    } else if (configVariable == null) {
      // no config object was built: create variable for object to build now:
      targetVariableName = createAuxVar(objectElement).getName();
      constructorCode.append("    ")
              .append("var ").append(targetVariableName).append(":").append(className);
    } else if (useConfigObjects(defaultUseConfigObjects, className)) {
      return configVariable.getName();
    } else {
      return value; // no aux var necessary
    }
    constructorCode.append(" = ").append(value).append(";");
    constructorBodyDirectives.addAll(mxmlParserHelper.parseConstructorBody(constructorCode.toString()));

    if (configVariable == null && !"Array".equals(className)) {
      // no config object was built or event listeners or bindings have to be added:
      // process attribute and children and assign properties directly on the target object
      Ide ide = null != targetVariableName ? new Ide(targetVariableName) : null;
      if(null == ide) {
        throw new IllegalStateException("potential NPE ahead!");
      }
      processAttributesAndChildNodes(objectElement, null, ide, false);
    }
    return targetVariableName;
  }

  private String createValueCodeFromElement(XmlElement objectElement, Boolean defaultUseConfigObjects, String className, Ide configVariable) {
    String value;
    JooSymbol textContentSymbol = getTextContent(objectElement);
    String textContent = ((String) textContentSymbol.getJooValue()).trim();
    if (MxmlUtils.isBindingExpression(textContent)) {
      return MxmlUtils.getBindingExpression(textContent);
    } else if ("String".equals(className)) {
      return CompilerUtils.quote(textContent);
    } else if ("int".equals(className) || "uint".equals(className) || "Number".equals(className) || "Boolean".equals(className)) {
      return textContent.isEmpty() ? null : textContent;
    }

    if (!textContent.isEmpty()) {
      throw Jooc.error(textContentSymbol, String.format("Unexpected text inside MXML element: '%s'.", textContent));
    }
    if ("Object".equals(className)) {
      value = "{}";
    } else if ("Array".equals(className)) {
      value = createArrayCodeFromChildElements(objectElement.getElements(), true, defaultUseConfigObjects);
    } else {
      StringBuilder valueBuilder = new StringBuilder();
      valueBuilder.append("new ").append(className).append("(");
      if (configVariable != null) {
        valueBuilder.append(configVariable);
      }
      valueBuilder.append(")");
      value = valueBuilder.toString();
    }
    return value;
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
    for (ClassDeclaration current = classModel; current != null; current = getSuperClassModel(current)) {
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

  private Ide createAuxVar(XmlElement element) {
    JooSymbol symbol = element.getSymbol();
    String prefix = element.getName();
    return createAuxVar(symbol, prefix);
  }

  @Nonnull
  private Ide createAuxVar(@Nonnull XmlElement element, @Nullable String idAttributeValue) {
    JooSymbol symbol = element.getSymbol();
    StringBuilder name = new StringBuilder();
    if (StringUtils.isEmpty(idAttributeValue)) {
      String prefix = element.getPrefix();
      if(null != prefix) {
        name.append(prefix).append('_');
      }
      name.append(element.getLocalName());
    } else {
      name.append(idAttributeValue);
      Ide.verifyIdentifier(idAttributeValue, symbol);
    }
    return createAuxVar(symbol, name.toString());
  }

  @Nonnull
  private Ide createAuxVar(@Nonnull JooSymbol symbol, @Nonnull String prefix) {
    String preferredName = CompilerUtils.uncapitalize(prefix.replaceAll("-", "\\$")) + '_' + symbol.getLine() + '_' + symbol.getColumn();
    return compilationUnit.createAuxVar(preferredName);
  }

  private void createEventHandlerCode(@Nonnull Ide ide, @Nonnull JooSymbol value, @Nonnull Annotation event) {
    Map<String, Object> eventPropertiesByName = event.getPropertiesByName();
    Object eventType = eventPropertiesByName.get("type");
    String eventTypeStr;
    if (eventType instanceof String) {
      eventTypeStr = (String) eventType;
      compilationUnit.addImport(eventTypeStr);
    } else {
      eventTypeStr = "Object";
    }
    Object eventNameModel = eventPropertiesByName.get("name");
    String eventName = (String) (eventNameModel != null ? eventNameModel : eventPropertiesByName.get(null));
    if (eventName.startsWith("on")) {
      eventName = eventName.substring(2);
    }
    String eventNameConstant = (eventName.substring(0, 1) + eventName.substring(1).replaceAll("([A-Z])", "_$1")).toUpperCase();
    String variable = ide.getName();
    String eventHandlerName = "$on_" + variable + "_" + eventName.replace('-', '_');
    StringBuilder classBodyCode = new StringBuilder();
    classBodyCode
            .append("private function ").append(eventHandlerName)
            .append(" (").append("event").append(':').append(eventTypeStr).append(") :void {\n")
            .append("\n    ").append(value.getJooValue())
            .append('}');
    classBodyDirectives.addAll(mxmlParserHelper.parseClassBody(new JooSymbol(classBodyCode.toString())).getDirectives());

    StringBuilder constructorCode = new StringBuilder();
    constructorCode.append("    ").append(variable).append("." + MxmlUtils.ADD_EVENT_LISTENER_METHOD_NAME + "(").append(eventTypeStr)
            .append(".").append(eventNameConstant)
            .append(", ")
            .append(eventHandlerName)
            .append(");");
    constructorBodyDirectives.addAll(mxmlParserHelper.parseConstructorBody(constructorCode.toString()));
  }

  private void createPropertyAssignmentCode(@Nonnull Ide variable, @Nonnull TypedIdeDeclaration propertyModel, @Nonnull JooSymbol value, boolean generatingConfig) {
    Directive propertyAssignment = createPropertyAssigment(variable, propertyModel, value, generatingConfig);
    constructorBodyDirectives.add(propertyAssignment);
  }

  @Nonnull
  private Directive createPropertyAssigment(@Nonnull Ide variable, @Nonnull TypedIdeDeclaration propertyModel, @Nonnull JooSymbol value, boolean generatingConfig) {
    TypeRelation typeRelation = propertyModel.getOptTypeRelation();
    String propertyType = typeRelation == null ? null : typeRelation.getType().getIde().getName();
    boolean untyped = UNTYPED_MARKER.equals(propertyType);
    String attributeValueAsString = MxmlUtils.valueToString(MxmlUtils.getAttributeValue((String) value.getJooValue(), untyped ? null : propertyType));

    String propertyName = generatingConfig ? getConfigOptionName(propertyModel) : propertyModel.getName();
    boolean untypedAccess = true; // untyped || !propertyName.equals(propertyModel.getName());

    Expr rightHandSide = mxmlParserHelper.parseExpression(value.replacingSymAndTextAndJooValue(value.sym, attributeValueAsString, attributeValueAsString));
    return MxmlAstUtils.createPropertyAssignment(variable, rightHandSide, propertyName, untypedAccess);
  }

  @Nonnull
  private static String getPropertyAssignmentCode(@Nonnull Ide variable, String propertyName, String attributeValueAsString) {
    String leftHandSide = MessageFormat.format("{0}[{1}]", variable.getName(), CompilerUtils.quote(propertyName));
    return MessageFormat.format("{0} = {1};", leftHandSide, attributeValueAsString);
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
    TypedIdeDeclaration propertyModel = null;
    ClassDeclaration superClassModel = getSuperClassModel(classModel);
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

  private Annotation findEvent(ClassDeclaration classModel, String propertyName) {
    for (ClassDeclaration current = classModel; current != null; current = getSuperClassModel(current)) {
      Annotation eventModel = getEvent(current, propertyName);
      if (eventModel != null) {
        return eventModel;
      }
    }
    return null;
  }

  private Annotation getEvent(ClassDeclaration classDeclaration, String propertyName) {
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

  private TypedIdeDeclaration findDefaultPropertyModel(ClassDeclaration classModel) {
    for (ClassDeclaration current = classModel; current != null; current = getSuperClassModel(current)) {
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

  private ClassDeclaration getSuperClassModel(ClassDeclaration classModel) {
    return classModel.getSuperTypeDeclaration();
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
