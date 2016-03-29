package net.jangaroo.jooc.mxml.ast;

import com.google.common.collect.Iterables;
import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.FieldModel;
import net.jangaroo.jooc.model.MemberModel;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.PropertyModel;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

  private final StringBuilder constructorCode = new StringBuilder();
  private final StringBuilder classBodyCode = new StringBuilder();

  MxmlToModelParser(JangarooParser jangarooParser, MxmlParserHelper mxmlParserHelper, MxmlCompilationUnit mxmlCompilationUnit) {
    this.jangarooParser = jangarooParser;
    this.mxmlParserHelper = mxmlParserHelper;
    this.compilationUnit = mxmlCompilationUnit;
  }

  private void renderConfigAuxVar(@Nonnull Ide ide, String type, boolean useCast) {
    constructorCode.append(String.format("\n    var %s:%s = %s;", ide.getName(), type, useCast ? type + "({})" : "{}"));
  }

  private void processAttributes(XmlElement objectNode, CompilationUnitModel type, @Nonnull Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig) {
    Ide variable = generatingConfig ? configVariable : targetVariable;
    ClassModel classModel = type == null ? null : type.getClassModel();
    boolean hasIdAttribute = false;
    for (XmlAttribute attribute : objectNode.getAttributes()) {
      String propertyName = attribute.getLocalName();
      boolean noPrefix = attribute.getPrefix() == null;
      hasIdAttribute |= noPrefix && MxmlUtils.MXML_ID_ATTRIBUTE.equals(propertyName);
      String attributeNamespaceUri = objectNode.getNamespaceUri(attribute.getPrefix());
      boolean isUntypedAccess = MxmlUtils.EXML_UNTYPED_NAMESPACE.equals(attributeNamespaceUri);
      if (noPrefix && !MxmlUtils.MXML_ID_ATTRIBUTE.equals(propertyName) ||
              isUntypedAccess) {
        String value = attribute.getValue().getText();
        MemberModel propertyModel = null;
        if (!isUntypedAccess && classModel != null) {
          propertyModel = findPropertyModel(classModel, propertyName);
          if (propertyModel == null) {
            if (generatingConfig && hasIdAttribute) {
              // Only generate config-event-listeners when config has no ID, because otherwise it will be instantiated
              // immediately, anyway, and it is safer to attach event listeners to the actual object.
              continue;
            }
            AnnotationModel eventModel = findEvent(classModel, propertyName);
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

  private void createPropertyAssignmentCodeWithBindings(Ide configVariable, Ide targetVariable, boolean generatingConfig, String value, MemberModel propertyModel) {
    Ide variable = generatingConfig ? configVariable : targetVariable;
    // skip property assignment to target object if it was already contained in config object:
    if (generatingConfig || configVariable == null) {
      // default: create a normal property assignment:
      createPropertyAssignmentCode(variable, propertyModel, value, generatingConfig);
    }
  }


  private static AnnotationModel getAnnotationAtSetter(MemberModel memberModel, String annotationName) {
    MemberModel setter = null;
    if (memberModel instanceof PropertyModel) {
      setter = ((PropertyModel) memberModel).getSetter();
    } else if (memberModel instanceof FieldModel && !((FieldModel) memberModel).isConst()) {
      setter = memberModel;
    }
    if (setter != null) {
      Iterator<AnnotationModel> annotations = setter.getAnnotations(annotationName).iterator();
      if (annotations.hasNext()) {
        return annotations.next();
      }
    }
    return null;
  }

  void processAttributesAndChildNodes(XmlElement objectNode, Ide configVariable, Ide targetVariable, boolean generatingConfig) {
    CompilationUnitModel type = getCompilationUnitModel(objectNode);
    processAttributes(objectNode, type, configVariable, targetVariable, generatingConfig);
    processChildNodes(objectNode, type, configVariable, targetVariable, generatingConfig);
  }

  private void processChildNodes(XmlElement objectNode, CompilationUnitModel type, Ide configVariable, Ide targetVariable, boolean generatingConfig) {
    Ide variable = generatingConfig ? configVariable : targetVariable;
    ClassModel classModel = type == null ? null : type.getClassModel();
    List<XmlElement> childNodes = objectNode.getElements();
    MemberModel defaultPropertyModel = findDefaultPropertyModel(classModel);
    List<XmlElement> defaultPropertyValues = new ArrayList<>();
    for (XmlElement element : childNodes) {
      if (!MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) { // ignore MXML namespace; has been handled before.
        MemberModel propertyModel = null;
        String propertyName = element.getLocalName();
        if (objectNode.getNamespaceURI().equals(element.getNamespaceURI())) {
          if (classModel != null) {
            propertyModel = findPropertyModel(classModel, propertyName);
            if (propertyModel == null) {
              AnnotationModel eventModel = findEvent(classModel, propertyName);
              if (eventModel != null) {
                String value = getTextContent(element);
                createEventHandlerCode(variable, value, eventModel);
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
            createPropertyAssignmentCodeWithBindings(configVariable, targetVariable, generatingConfig, getTextContent(element), propertyModel);
          } else {
            createChildElementsPropertyAssignmentCode(childElements, variable, propertyModel, generatingConfig);
          }
          String configMode = "Array".equals(propertyModel.getType()) ? element.getAttributeNS(Exmlc.EXML_NAMESPACE_URI, CONFIG_MODE_ATTRIBUTE_NAME) : "";
          String atValue = CONFIG_MODE_TO_AT_VALUE.get(configMode);
          if (atValue != null) {
            String atPropertyName = generatingConfig ? getConfigOptionName(propertyModel) : propertyModel.getName();
            constructorCode.append(String.format("\n    %s.%s = %s;", variable, atPropertyName + CONFIG_MODE_AT_SUFFIX, atValue));
          }
        }
      }
    }
    if (!defaultPropertyValues.isEmpty()) {
      createChildElementsPropertyAssignmentCode(defaultPropertyValues, variable, defaultPropertyModel, generatingConfig);
    }
  }

  private void createChildElementsPropertyAssignmentCode(List<XmlElement> childElements, Ide variable,
                                                         MemberModel propertyModel, boolean generatingConfig) {
    boolean forceArray = "Array".equals(propertyModel.getType());
    AnnotationModel extConfigAnnotation = getAnnotationAtSetter(propertyModel, Jooc.EXT_CONFIG_ANNOTATION_NAME);
    Boolean useConfigObjects = extConfigAnnotation == null ? null : useConfigObjects(extConfigAnnotation, null);
    String value = createArrayCodeFromChildElements(childElements, forceArray, useConfigObjects);
    if (extConfigAnnotation != null) {
      AnnotationPropertyModel extractXType = extConfigAnnotation.getPropertiesByName().get(EXT_CONFIG_EXTRACT_XTYPE_PARAMETER);
      if (extractXType != null) {
        String extractXTypeToProperty = extractXType.getStringValue();
        if (extractXTypeToProperty != null) {
          constructorCode.append("\n    ").append(getPropertyAssignmentCode(variable, extractXTypeToProperty, value + "['xtype']"));
        }
        constructorCode.append(String.format(DELETE_OBJECT_PROPERTY_CODE, value, "xtype"));
        constructorCode.append(String.format(DELETE_OBJECT_PROPERTY_CODE, value, "xclass"));
      }
    }
    createPropertyAssignmentCode(variable, propertyModel, MxmlUtils.createBindingExpression(value), generatingConfig);
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
    String className = mxmlParserHelper.getClassNameForElement(jangarooParser, objectElement);
    compilationUnit.addImport(className);
    Boolean useConfigObjects = defaultUseConfigObjects;
    if (useConfigObjects == null) {
      // let the class decide:
      useConfigObjects = useConfigObjects(jangarooParser.resolveCompilationUnit(className).getClassModel());
    }
    String targetVariableName = null;   // name of the variable holding the object to build
    String id = objectElement.getAttribute(MxmlUtils.MXML_ID_ATTRIBUTE);
    if (!id.isEmpty()) {
      if (id.equals(compilationUnit.getConstructorParamName())) {
        return null;
      }

      VariableDeclaration variableDeclaration = compilationUnit.getVariables().get(id);
      String qualifier = null != configVar ? configVar.getName() : "";
      if (null != variableDeclaration) {
        if (!variableDeclaration.isPublic()) {
          qualifier = ""; // corresponds to "this."
        }
      } else {
        String asDoc = MxmlUtils.toASDoc(objectElement.getSymbol().getWhitespace());
        int i = asDoc.lastIndexOf('\n');
        classBodyCode.append('\n')
                .append(asDoc)
                .append('[').append(Jooc.BINDABLE_ANNOTATION_NAME).append(']')
                .append(i < 0 ? "\n" : asDoc.substring(i))
                .append("public var ").append(id).append(':').append(className).append(';');
      }
      targetVariableName = CompilerUtils.qName(qualifier, id);
    }

    Ide configVariable = null; // name of the variable holding the config object to use in the constructor

    if (CompilationUnitModelUtils.constructorSupportsConfigOptionsParameter(className, jangarooParser)) {
      // if class supports a config options parameter, create a config options object and assign properties to it:
      configVariable = createAuxVar(objectElement, id);
      renderConfigAuxVar(configVariable, className, true);
      if (targetVariableName == null) {
        targetVariableName = createAuxVar(objectElement).getName();
      }
      // process attributes and children, using a forward reference to the object to build inside bindings:
      processAttributesAndChildNodes(objectElement, configVariable, new Ide(targetVariableName), true);
    }

    String value = createValueCodeFromElement(objectElement, defaultUseConfigObjects, className, configVariable);

    if (id.length() > 0) {
      if (null != configVar // it is a declaration...
              && objectElement.getAttributes().size() == 1 // ...with only an id attribute...
              && objectElement.getChildren().isEmpty() && objectElement.getTextNodes().isEmpty() // ...and no sub-elements or text content!
              ) {
        // prevent assigning a default value for such an empty declaration:
        return null;
      }
      constructorCode.append("\n    ").append(targetVariableName);
    } else if (configVariable == null) {
      if (targetVariableName == null) {
        // no config object was built: create variable for object to build now:
        targetVariableName = createAuxVar(objectElement).getName();
      }
      constructorCode.append("\n    ").append("var ").append(targetVariableName).append(":").append(className);
    } else if (useConfigObjects) {
      return configVariable.getName();
    } else {
      return value; // no aux var necessary
    }
    constructorCode.append(" = ").append(value).append(";");

    if (configVariable == null && !"Array".equals(className)) {
      // no config object was built or event listeners or bindings have to be added:
      // process attribute and children and assign properties directly on the target object
      Ide ide = null != targetVariableName ? new Ide(targetVariableName) : null;
      processAttributesAndChildNodes(objectElement, null, ide, false);
    }
    return targetVariableName;
  }

  private String createValueCodeFromElement(XmlElement objectElement, Boolean defaultUseConfigObjects, String className, Ide configVariable) {
    String value;
    String textContent = getTextContent(objectElement);
    if (MxmlUtils.isBindingExpression(textContent)) {
      return MxmlUtils.getBindingExpression(textContent);
    } else if ("String".equals(className)) {
      return CompilerUtils.quote(textContent);
    } else if ("int".equals(className) || "uint".equals(className) || "Number".equals(className) || "Boolean".equals(className)) {
      return textContent.isEmpty() ? null : textContent;
    }

    if (!textContent.trim().isEmpty()) {
      throw Jooc.error(objectElement, String.format("Unexpected text inside MXML element: '%s'.", textContent));
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

  private boolean useConfigObjects(ClassModel classModel) {
    for (ClassModel current = classModel; current != null; current = getSuperClassModel(current)) {
      Iterator<AnnotationModel> extConfigAnnotations = current.getAnnotations(Jooc.EXT_CONFIG_ANNOTATION_NAME).iterator();
      if (extConfigAnnotations.hasNext()) {
        AnnotationModel extConfigAnnotation = extConfigAnnotations.next();
        return useConfigObjects(extConfigAnnotation, true);
      }
      // special case Plugin (avoid having to check all interfaces):
      if (current.getInterfaces().contains("ext.Plugin") || current.getInterfaces().contains("Plugin")) {
        return true;
      }
    }
    return false;
  }

  private static Boolean useConfigObjects(AnnotationModel extConfigAnnotation, Boolean defaultValue) {
    Map<String, AnnotationPropertyModel> propertiesByName = extConfigAnnotation.getPropertiesByName();
    AnnotationPropertyModel flag = propertiesByName.get(EXT_CONFIG_CREATE_FLAG);
    if (flag == null) {
      // a given "extractXType" parameter implies to use config objects (create=false):
      if (propertiesByName.containsKey(EXT_CONFIG_EXTRACT_XTYPE_PARAMETER)) {
        return true;
      }
      return defaultValue;
    }
    return "false".equals(flag.getValue());
  }

  private Ide createAuxVar(XmlElement element) {
    JooSymbol symbol = element.getSymbol();
    String prefix = element.getName();
    return createAuxVar(symbol, prefix);
  }

  private Ide createAuxVar(XmlElement element, String idAttributeValue) {
    JooSymbol symbol = element.getSymbol();
    StringBuilder name = new StringBuilder();
    if (idAttributeValue.isEmpty()) {
      String prefix = element.getPrefix();
      if(null != prefix) {
        name.append(prefix).append('_');
      }
      name.append(element.getLocalName());
    } else {
      name.append(idAttributeValue);
      if(!Ide.IDE_PATTERN.matcher(idAttributeValue).matches()) {
        throw JangarooParser.error(symbol, "invalid action script identifier");
      }
    }
    return createAuxVar(symbol, name.toString());
  }

  @Nonnull
  private Ide createAuxVar(@Nonnull JooSymbol symbol, @Nonnull String prefix) {
    String preferredName = CompilerUtils.uncapitalize(prefix.replaceAll("-", "\\$")) + '_' + symbol.getLine() + '_' + symbol.getColumn();
    return compilationUnit.createAuxVar(preferredName);
  }

  private void createEventHandlerCode(@Nonnull Ide ide, String value, AnnotationModel event) {
    AnnotationPropertyModel eventType = event.getPropertiesByName().get("type");
    String eventTypeStr;
    if (eventType == null) {
      eventTypeStr = "Object";
    } else {
      eventTypeStr = eventType.getStringValue();
      compilationUnit.addImport(eventTypeStr);
    }
    AnnotationPropertyModel eventNameModel = event.getPropertiesByName().get("name");
    String eventName = (eventNameModel != null ? eventNameModel : event.getPropertiesByName().get(null)).getStringValue();
    if (eventName.startsWith("on")) {
      eventName = eventName.substring(2);
    }
    String variable = ide.getName();
    String eventHandlerName = "$on_" + variable + "_" + eventName.replace('-', '_');
    classBodyCode.append('\n')
            .append("private function ").append(eventHandlerName)
            .append(" (").append("event").append(':').append(eventTypeStr).append(") :void {\n")
            .append("\n    ").append(value)
            .append('}');
    compilationUnit.addImport("joo.addEventListener");
    constructorCode.append("\n    ").append("joo.addEventListener(").append(variable).append(", ")
            .append(CompilerUtils.quote(eventName)).append(", ")
            .append(eventHandlerName).append(", ")
            .append(eventTypeStr).append(");");
  }

  private void createPropertyAssignmentCode(@Nonnull Ide variable, MemberModel propertyModel, String value, boolean generatingConfig) {
    String attributeValueAsString = MxmlUtils.valueToString(getPropertyValue(propertyModel, value));
    String propertyName = generatingConfig ? getConfigOptionName(propertyModel) : propertyModel.getName();
    constructorCode.append("\n    ");
    String propertyAssignmentCode =
            UNTYPED_MARKER.equals(propertyModel.getType()) || !propertyName.equals(propertyModel.getName())
                    ? getUntypedPropertyAssignmentCode(variable, propertyName, attributeValueAsString)
                    : getPropertyAssignmentCode(variable, propertyName, attributeValueAsString);
    constructorCode.append(propertyAssignmentCode);
  }

  @Nonnull
  private static String getPropertyAssignmentCode(@Nonnull Ide variable, String propertyName, String attributeValueAsString) {
    String leftHandSide = Ide.THIS.equals(variable.getName()) ? propertyName : MessageFormat.format("{0}.{1}", variable, propertyName);
    return getAssignmentCode(leftHandSide, attributeValueAsString);
  }

  @Nonnull
  private static String getUntypedPropertyAssignmentCode(@Nonnull Ide variable, String propertyName, String attributeValueAsString) {
    String leftHandSide = MessageFormat.format("{0}[\"{1}\"]", variable.getName(), propertyName);
    return getAssignmentCode(leftHandSide, attributeValueAsString);
  }

  private static String getAssignmentCode(String leftHandSide, String rightHandSide) {
    return MessageFormat.format("{0} = {1};", leftHandSide, rightHandSide);
  }

  private static String getConfigOptionName(MemberModel propertyModel) {
    if (propertyModel instanceof PropertyModel) {
      MethodModel setter = ((PropertyModel) propertyModel).getSetter();
      if (setter != null) { // should actually be there, or the assignment would not work!
        Iterator<AnnotationModel> configOptionAnnotations = setter.getAnnotations(Jooc.EXT_CONFIG_ANNOTATION_NAME).iterator();
        if (configOptionAnnotations.hasNext()) {
          AnnotationPropertyModel configOption = configOptionAnnotations.next().getPropertiesByName().get(null);
          if (configOption != null) {
            return configOption.getStringValue();
          }
        }
      }
    }
    return propertyModel.getName();
  }

  // ======================================== auxiliary methods ========================================

  @Nonnull
  private CompilationUnitModel getCompilationUnitModel(XmlElement element) {
    String fullClassName = mxmlParserHelper.getClassNameForElement(jangarooParser, element);
    try {
      return CompilationUnitModelUtils.getCompilationUnitModel(fullClassName, jangarooParser);
    } catch (CompilerError e) {
      // ugly to catch-and-rethrow, I know, but we need to add position information here...
      throw JangarooParser.error(element, e.getMessage());
    }
  }

  private MemberModel findPropertyModel(ClassModel classModel, String propertyName) {
    MemberModel propertyModel = null;
    ClassModel superClassModel = getSuperClassModel(classModel);
    if (superClassModel != null) {
      propertyModel = findPropertyModel(superClassModel, propertyName);
    }
    if (propertyModel == null) {
      MemberModel memberModel = classModel.getMember(propertyName);
      if (memberModel != null && memberModel.isWritable()) {
        propertyModel = memberModel;
      }
    }
    return propertyModel;
  }

  private AnnotationModel findEvent(ClassModel classModel, String propertyName) {
    for (ClassModel current = classModel; current != null; current = getSuperClassModel(current)) {
      AnnotationModel eventModel = current.getEvent(propertyName);
      if (eventModel != null) {
        return eventModel;
      }
    }
    return null;
  }

  private MemberModel findDefaultPropertyModel(ClassModel classModel) {
    for (ClassModel current = classModel; current != null; current = getSuperClassModel(current)) {
      MemberModel defaultPropertyModel = current.findPropertyWithAnnotation(false, MxmlUtils.MXML_DEFAULT_PROPERTY_ANNOTATION);
      if (defaultPropertyModel != null) {
        return defaultPropertyModel;
      }
    }
    return null;
  }

  private MemberModel createDynamicPropertyModel(XmlElement element, CompilationUnitModel compilationUnitModel, String name, boolean allowAnyProperty) {
    if (!allowAnyProperty && compilationUnitModel != null && compilationUnitModel.getClassModel() != null && !compilationUnitModel.getClassModel().isDynamic()) {
      // dynamic property of a non-dynamic class: warn!
      JangarooParser.warning(element.getSymbol(), "MXML: property " + name + " not found in class " + compilationUnitModel.getQName() + ".");
    }
    return new FieldModel(name, allowAnyProperty ? UNTYPED_MARKER : "*");
  }

  private ClassModel getSuperClassModel(ClassModel classModel) {
    String superclass = classModel.getSuperclass();
    if (superclass != null) {
      CompilationUnitModel superCompilationUnitModel = jangarooParser.resolveCompilationUnit(superclass);
      return superCompilationUnitModel.getClassModel();
    }
    return null;
  }

  private Object getPropertyValue(MemberModel propertyModel, String value) {
    return MxmlUtils.getAttributeValue(value, propertyModel == null || UNTYPED_MARKER.equals(propertyModel.getType()) ? null : propertyModel.getType());
  }

  private static String getTextContent(XmlElement element) {
    JooSymbol textNode = Iterables.getFirst(element.getTextNodes(), null);
    return null != textNode ? textNode.getText() : "";
  }

  String consumeConstructorCode() {
    String result = constructorCode.toString();
    constructorCode.setLength(0);
    return result;
  }

  String getClassBodyCode() {
    return classBodyCode.toString();
  }
}
