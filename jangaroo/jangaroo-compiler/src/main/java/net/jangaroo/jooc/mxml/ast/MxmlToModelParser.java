package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.exml.api.Exmlc;
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
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.PropertyDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.json.Json;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.jooc.json.JsonValue;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
  private MxmlRootModel mxmlRootModel;
  private List<MxmlModel> mxmlModelsWithId;

  private final Collection<Directive> constructorBodyDirectives = new LinkedList<>();
  private final Collection<Directive> classBodyDirectives = new LinkedList<>();
  String additionalDeclarations = "";

  MxmlToModelParser(JangarooParser jangarooParser, MxmlParserHelper mxmlParserHelper, MxmlCompilationUnit mxmlCompilationUnit) {
    this.jangarooParser = jangarooParser;
    this.mxmlParserHelper = mxmlParserHelper;
    this.compilationUnit = mxmlCompilationUnit;
  }

  MxmlRootModel parse(XmlElement objectNode) {
    mxmlRootModel = new MxmlRootModel();
    mxmlModelsWithId = new ArrayList<>();
    fillObjectModel(mxmlRootModel, objectNode, getCompilationUnitModel(objectNode));
    mxmlModelsWithId.removeAll(mxmlRootModel.getDeclarations());
    mxmlRootModel.references = Collections.unmodifiableList(mxmlModelsWithId);
    return mxmlRootModel;
  }

  private MxmlModel createModel(XmlElement objectNode) {
    CompilationUnit type = getCompilationUnitModel(objectNode);
    MxmlModel model;
    if (isCodeContainer(objectNode)) {
      model = createValueModel(objectNode, getTextContent(objectNode));
    } else if ("Array".equals(type.getQualifiedNameStr())) {
      model = createArrayModel(objectNode.getElements());
    } else {
      model = createObjectModel(objectNode, type);
    }
    model.id = getId(objectNode);
    model.sourceElement = objectNode;
    model.type = type;
    if (model.id != null) {
      mxmlModelsWithId.add(model);
    }
    return model;
  }

  private MxmlObjectModel createObjectModel(XmlElement objectNode, CompilationUnit type) {
    MxmlObjectModel objectModel = new MxmlObjectModel();
    fillObjectModel(objectModel, objectNode, type);
    return objectModel;
  }

  private void fillObjectModel(MxmlObjectModel objectModel, XmlElement objectNode, CompilationUnit type) {
    List<MxmlMemberModel> members = new ArrayList<>();
    for (XmlAttribute attribute : objectNode.getAttributes()) {
      Object propertyModelOrEventAnnotation = findPropertyModelOrEventAnnotation(objectNode, type, attribute);
      MxmlMemberModel memberModel = createMemberModel(attribute, propertyModelOrEventAnnotation);
      if (memberModel != null) {
        members.add(memberModel);
      }
    }
    TypedIdeDeclaration defaultPropertyModel = findDefaultPropertyModel((ClassDeclaration) type.getPrimaryDeclaration());
    List<XmlElement> defaultValues = new ArrayList<>();
    for (XmlElement element : objectNode.getElements()) {
      if (element.isBuiltInElement()) {
        if (MxmlUtils.MXML_DECLARATIONS.equals(element.getLocalName())) {
          if (objectModel == mxmlRootModel) {
            mxmlRootModel.declarations = element.getElements().stream().map(this::createModel).collect(Collectors.toList());
          } else {
            jangarooParser.getLog().error(element.getSymbol(), "Declarations are only allowed directly inside the MXML root element.");
          }
        }
      } else {
        Object propertyModelOrEventAnnotation = findPropertyModelOrEventAnnotation(objectNode, type, defaultPropertyModel != null, element);
        MxmlMemberModel memberModel = createMemberModel(element, propertyModelOrEventAnnotation);
        if (memberModel != null) {
          if (memberModel instanceof MxmlPropertyModel && MxmlUtils.EXML_MIXINS_PROPERTY_NAME.equals(memberModel.getConfigOptionName())) {
            members.addAll(collectMixinsProperties((MxmlPropertyModel) memberModel));
          } else {
            members.add(memberModel);
            if (memberModel instanceof MxmlPropertyModel) {
              MxmlPropertyModel atMember = getArrayAtPropertyModel(element, (MxmlPropertyModel) memberModel);
              if (atMember != null) {
                members.add(atMember);
              }
            }
          }
        } else if (defaultPropertyModel != null) {
          defaultValues.add(element);
        } else {
          jangarooParser.getLog().error(objectNode.getSymbol(), "Element not allowed here.");
        }
      }
    }
    if (defaultPropertyModel != null && !defaultValues.isEmpty()) {
      members.add(new MxmlPropertyModel(defaultPropertyModel, createPropertyValue(objectNode, defaultPropertyModel.getName(), defaultValues)));
    }
    objectModel.members = members;
  }

  private List<MxmlMemberModel> collectMixinsProperties(MxmlPropertyModel memberModel) {
    List<MxmlMemberModel> members = new ArrayList<>();
    MxmlModel propertyValueModel = memberModel.getValue();
    if (propertyValueModel instanceof MxmlArrayModel) {
      for (MxmlModel mixin : ((MxmlArrayModel) propertyValueModel).getElements()) {
        if (mixin instanceof MxmlObjectModel) {
          members.addAll(((MxmlObjectModel) mixin).getMembers());
        } else {
          jangarooParser.getLog().error(mixin.sourceElement.getSymbol(),
                  "MXML mixins property must only contain sub-elements.");
        }
      }
    } else {
      jangarooParser.getLog().error(memberModel.sourceNode.getSymbol(),
              "MXML mixins property must contain a list of sub-elements.");
    }
    return members;
  }

  private MxmlPropertyModel getArrayAtPropertyModel(XmlElement element, MxmlPropertyModel propertyModel) {
    if (!"Array".equals(getPropertyType(propertyModel.getPropertyDeclaration()))) {
      return null;
    }
    XmlAttribute configModeAttribute = element.getAttributeNodeNS(Exmlc.EXML_NAMESPACE_URI, CONFIG_MODE_ATTRIBUTE_NAME);
    String configMode = configModeAttribute != null
            ? (String) configModeAttribute.getValue().getJooValue()
            : getConfigMode(propertyModel.getPropertyDeclaration());
    String atValue = CONFIG_MODE_TO_AT_VALUE.get(configMode);
    if (atValue != null) {
      String atValueCode = MxmlUtils.createBindingExpression(atValue);
      MxmlValueModel atValueModel = configModeAttribute != null
              ? createValueModel(configModeAttribute, configModeAttribute.getValue().replacingSymAndTextAndJooValue(sym.STRING_LITERAL, atValueCode, atValueCode))
              : createValueModel(element, new JooSymbol(atValueCode));
      return new MxmlPropertyModel(propertyModel.getPropertyDeclaration(), atValueModel) {
        @Override
        String getConfigOptionName() {
          return super.getConfigOptionName() + CONFIG_MODE_AT_SUFFIX;
        }
      };
    }
    return null;
  }

  private MxmlArrayModel createArrayModel(List<XmlElement> elements) {
    return new MxmlArrayModel(elements.stream().map(this::createModel).collect(Collectors.toList()));
  }

  private MxmlValueModel createValueModel(@Nonnull XmlNode sourceNode, @Nonnull JooSymbol textContent) {
    return new MxmlValueModel(sourceNode, textContent);
  }

  private MxmlMemberModel createMemberModel(XmlNode sourceNode, Object propertyModelOrEventAnnotation) {
    MxmlMemberModel memberModel;
    if (propertyModelOrEventAnnotation instanceof TypedIdeDeclaration) {
      memberModel = createPropertyModel(sourceNode, (TypedIdeDeclaration) propertyModelOrEventAnnotation);
    } else if (propertyModelOrEventAnnotation instanceof Annotation) {
      memberModel = createEventHandlerModel(sourceNode, (Annotation) propertyModelOrEventAnnotation);
    } else {
      return null;
    }
    memberModel.sourceNode = sourceNode;
    return memberModel;
  }

  private MxmlPropertyModel createPropertyModel(@Nonnull XmlNode sourceNode, @Nonnull TypedIdeDeclaration propertyDeclaration) {
    String propertyType = getPropertyType(propertyDeclaration);
    MxmlModel value;
    if (sourceNode instanceof XmlElement) {
      XmlElement sourceElement = (XmlElement) sourceNode;
      value = isCodeContainer(sourceElement)
              ? createValueModel(sourceElement, getTextContent(sourceElement))
              : createPropertyValue(sourceElement, propertyType, sourceElement.getElements());
    } else {
      value = createValueModel(sourceNode, ((XmlAttribute) sourceNode).getValue());
    }
    return new MxmlPropertyModel(propertyDeclaration, value);
  }

  private MxmlModel createPropertyValue(XmlNode sourceNode, String propertyType, List<XmlElement> elements) {
    MxmlModel value;
    if ("Array".equals(propertyType)) {
      value = createArrayModel(elements);
      value.sourceElement = (XmlElement) sourceNode;
    } else {
      Iterator<XmlElement> elementIterator = elements.iterator();
      value = elementIterator.hasNext() ? createModel(elementIterator.next()) : createValueModel(sourceNode, new JooSymbol(MxmlUtils.createBindingExpression(AS3Type.getDefaultValue(propertyType))));
      if (elementIterator.hasNext()) {
        jangarooParser.getLog().error(elementIterator.next().getSymbol(), "Non-array property may only have at most one sub-element.");
      }
    }
    return value;
  }

  private MxmlEventHandlerModel createEventHandlerModel(XmlNode sourceNode, Annotation eventType) {
    JooSymbol handlerCode = (sourceNode instanceof XmlElement ? getTextContent((XmlElement) sourceNode)
            : ((XmlAttribute) sourceNode).getValue());
    return new MxmlEventHandlerModel(eventType, handlerCode);
  }

  private String getId(XmlElement node) {
    return node.getAttributes().stream()
            .filter(isIdAttributePredicate())
            .map(attribute -> (String) attribute.getValue().getJooValue())
            .findFirst()
            .orElse(null);
  }

  private static Predicate<XmlAttribute> isIdAttributePredicate() {
    return attribute -> attribute.getPrefix() == null && MxmlUtils.MXML_ID_ATTRIBUTE.equals(attribute.getLocalName());
  }

  private boolean isCodeContainer(XmlElement node) {
    return node.getElements().isEmpty() && !getTextContent(node).getText().trim().isEmpty() && node.getAttributes().stream().noneMatch(isIdAttributePredicate().negate());
  }

  Json modelToJson(@Nonnull MxmlModel mxmlModel) {
    Json json;
    CompilationUnit type = mxmlModel.getType();
    String typeName = type == null ? "*" : type.getQualifiedNameStr();
    if (mxmlModel instanceof MxmlArrayModel) {
      json = arrayModelToJsonArray((MxmlArrayModel) mxmlModel);
    } else if (mxmlModel instanceof MxmlObjectModel) {
      JsonObject objectModel = objectModelToJsonObject((MxmlObjectModel) mxmlModel);
      Object defaultValue = getDefaultValue(typeName);
      if (defaultValue != null) {
        json = new JsonValue(defaultValue);
      } else {
        if (!"Object".equals(typeName) && !mxmlModel.isUsePlainObjects()) {
          objectModel.settingWrapperClass(typeName);
          objectModel.setInstantiationMode(
                  (mxmlModel.isUseConfigObjects() != null ? mxmlModel.isUseConfigObjects() : false)
                          ? JsonObject.InstantiationMode.EXT_CONFIG
                          : CompilationUnitUtils.constructorSupportsConfigOptionsParameter(typeName, jangarooParser)
                          ? JsonObject.InstantiationMode.EXT_CREATE
                          : JsonObject.InstantiationMode.MXML
          );
        }
        json = objectModel;
      }
    } else if (mxmlModel instanceof MxmlValueModel){
      MxmlValueModel valueModel = (MxmlValueModel) mxmlModel;
      json = new JsonValue(getValue(typeName, valueModel));
    } else {
      throw new IllegalStateException("Unknown MxmlModel subclass " + mxmlModel.getClass());
    }
    json.setId(mxmlModel.getId());
    return json;
  }

  private Object getDefaultValue(String typeName) {
    AS3Type as3Type = AS3Type.typeByName(typeName);
    if (as3Type != null) {
      switch (as3Type) {
        case INT:
        case UINT:
        case NUMBER:
          return 0;
        case BOOLEAN:
          return false;
        case STRING:
          return "";
      }
    }
    return null;
  }

  private Object getValue(String typeName, MxmlValueModel valueModel) {
    JooSymbol valueSymbol = valueModel.getValue();
    Object value = MxmlUtils.getAttributeValue((String) valueSymbol.getJooValue(), typeName);
    if (value instanceof String && MxmlUtils.isBindingExpression((String) value)) {
      value = JsonObject.code(MxmlUtils.getBindingExpression((String) value));
    }
    return value;
  }

  private JsonArray arrayModelToJsonArray(MxmlArrayModel objectNode) {
    return new JsonArray(objectNode.getElements().stream().map(this::modelToJson).toArray());
  }

  JsonObject objectModelToJsonObject(MxmlObjectModel objectModel) {
     return objectModelToJsonObject(new JsonObject(), objectModel);
  }

  private JsonObject objectModelToJsonObject(JsonObject model, MxmlObjectModel objectModel) {
    for (MxmlMemberModel member : objectModel.getMembers()) {
      if (member instanceof MxmlPropertyModel) {
        MxmlPropertyModel propertyModel = (MxmlPropertyModel) member;
        String configOptionName = propertyModel.getConfigOptionName();
        MxmlModel propertyValueModel = propertyModel.getValue();
        Json configOptionValue = modelToJson(propertyValueModel);
        model.set(configOptionName, configOptionValue);
        String extractXTypePropertyName = propertyModel.getExtractXTypePropertyName();
        if (extractXTypePropertyName != null && !extractXTypePropertyName.isEmpty()) {
          model.set(extractXTypePropertyName, JsonObject.code(propertyValueModel.getType().getQualifiedNameStr() + "['prototype'].xtype"));
        }
      } else if (member instanceof MxmlEventHandlerModel) {
        MxmlEventHandlerModel eventHandlerModel = (MxmlEventHandlerModel) member;
        JsonObject listeners = (JsonObject) model.get("listeners");
        if (listeners == null) {
          listeners = new JsonObject();
          model.set("listeners", listeners);
        }
        String eventHandlerMethodName = createEventHandlerMethod(eventHandlerModel);
        listeners.set(eventHandlerModel.getConfigOptionName(), JsonObject.code(eventHandlerMethodName));
      }
    }
    return model;
  }

  private void renderConfigAuxVar(@Nonnull Ide ide, Ide type) {
    constructorBodyDirectives.add(MxmlAstUtils.createVariableDeclaration(ide, type));
  }

  private Object findPropertyModelOrEventAnnotation(XmlElement objectNode, CompilationUnit type, XmlAttribute attribute) {
    if (RootElementProcessor.alreadyProcessed(attribute)) {
      return null;
    }
    String attributeNamespaceUri = objectNode.getNamespaceUri(attribute.getPrefix());
    boolean isUntypedAccess = MxmlUtils.EXML_UNTYPED_NAMESPACE.equals(attributeNamespaceUri);
    if (isIdAttributePredicate().test(attribute)) {
      return null;
    }
    String propertyName = attribute.getLocalName();
    if (attribute.getPrefix() == null || isUntypedAccess) {
      ClassDeclaration classModel = type == null ? null : (ClassDeclaration) type.getPrimaryDeclaration();
      TypedIdeDeclaration propertyModel = null;
      if (!isUntypedAccess && classModel != null) {
        propertyModel = findPropertyModel(classModel, propertyName);
        if (propertyModel == null) {
          Annotation eventModel = findEvent(classModel, propertyName);
          if (eventModel != null) {
            return eventModel;
          }
        }
      }
      if (propertyModel == null) {
        propertyModel = createDynamicPropertyModel(objectNode, type, propertyName, isUntypedAccess);
      }
      return propertyModel;
    }
    return null;
  }

  private Object findPropertyModelOrEventAnnotation(XmlElement objectNode, CompilationUnit type, boolean hasDefaultPropertyModel, XmlElement element) {
    TypedIdeDeclaration propertyModel = null;
    String propertyName = element.getLocalName();
    ClassDeclaration classModel = type == null ? null : (ClassDeclaration) type.getPrimaryDeclaration();
    if (objectNode.getNamespaceURI().equals(element.getNamespaceURI())) {
      if (classModel != null) {
        propertyModel = findPropertyModel(classModel, propertyName);
        if (propertyModel == null) {
          Annotation eventModel = findEvent(classModel, propertyName);
          if (eventModel != null) {
            return eventModel;
          }
        }
      }
      if (propertyModel == null && !hasDefaultPropertyModel) {
        propertyModel = createDynamicPropertyModel(element, type, propertyName, false);
      }
    }
    // if propertyDeclaration is still null, item will be used as value for default property!
    return propertyModel;
  }

  private void createPropertyAssignmentCodeWithBindings(Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig, @Nonnull JooSymbol value, @Nonnull MxmlPropertyModel propertyModel) {
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

  void processAttributesAndChildNodes(MxmlObjectModel objectModel, Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig) {
    if (!objectModel.getMembers().isEmpty()) {
      processMembers(objectModel, configVariable, targetVariable, generatingConfig);
    }
  }

  private void processMembers(MxmlObjectModel objectModel, Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig) {
    Ide variable = generatingConfig ? configVariable : targetVariable;
    for (MxmlMemberModel member : objectModel.getMembers()) {
      if (member instanceof MxmlEventHandlerModel) {
        MxmlEventHandlerModel eventHandlerModel = (MxmlEventHandlerModel) member;
        createAttachEventHandlerCode(variable, eventHandlerModel);
      } else if (member instanceof MxmlPropertyModel) {
        MxmlPropertyModel propertyModel = (MxmlPropertyModel) member;
        MxmlModel propertyValue = propertyModel.getValue();
        if (propertyValue instanceof MxmlValueModel) {
          createPropertyAssignmentCodeWithBindings(configVariable, targetVariable, generatingConfig, ((MxmlValueModel) propertyValue).getValue(), propertyModel);
        } else {
          createChildElementsPropertyAssignmentCode(propertyValue, variable, propertyModel, generatingConfig);
        }
      }
    }
  }

  private static String getConfigMode(TypedIdeDeclaration propertyModel) {
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
    return "";
  }

  private void createChildElementsPropertyAssignmentCode(MxmlModel childElements, Ide variable,
                                                         MxmlPropertyModel propertyModel, boolean generatingConfig) {
    String value = childElements instanceof MxmlArrayModel
            ? createArrayCodeFromChildElements(((MxmlArrayModel)childElements).getElements())
            : createValueCodeFromElement(null, childElements);

    String extractXTypeToProperty = propertyModel.getExtractXTypePropertyName();
    if (extractXTypeToProperty != null) {
      StringBuilder constructorCode = new StringBuilder();
      if (!extractXTypeToProperty.isEmpty()) {
        constructorCode.append("    ")
                .append(getPropertyAssignmentCode(variable, extractXTypeToProperty, value + "['xtype']"));
      }
      constructorCode.append(String.format(DELETE_OBJECT_PROPERTY_CODE, value, "xtype"));
      constructorCode.append(String.format(DELETE_OBJECT_PROPERTY_CODE, value, "xclass"));
      constructorBodyDirectives.addAll(mxmlParserHelper.parseConstructorBody(constructorCode.toString()));
    }
    createPropertyAssignmentCode(variable, propertyModel, new JooSymbol(MxmlUtils.createBindingExpression(value)), generatingConfig);
  }

  private String createArrayCodeFromChildElements(List<MxmlModel> childElements) {
    List<String> arrayItems = new ArrayList<>();
    for (MxmlModel arrayItemNode : childElements) {
      String itemValue = createValueCodeFromElement(null, arrayItemNode);
      arrayItems.add(itemValue);
    }
    return "[" + join(arrayItems, ", ") + "]";
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
  String createValueCodeFromElement(@Nullable Ide configVar, MxmlModel objectModel) {
    XmlElement objectElement = objectModel.sourceElement;
    CompilationUnit type = objectModel.getType();
    final String className = type.getQualifiedNameStr();
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

    if (Boolean.TRUE.equals(objectModel.isUseConfigObjects()) ||
            CompilationUnitUtils.constructorSupportsConfigOptionsParameter(className, jangarooParser)) {
      // if class supports a config options parameter, create a config options object and assign properties to it:
      configVariable = createAuxVar(objectElement, id);
      renderConfigAuxVar(configVariable, typeIde != null ? typeIde : new Ide(className));
      if (targetVariableName == null) {
        targetVariableName = createAuxVar(objectElement).getName();
      }
      if (objectModel instanceof MxmlObjectModel) {
        // process attributes and children, using a forward reference to the object to build inside bindings:
        processAttributesAndChildNodes((MxmlObjectModel) objectModel, configVariable, new Ide(targetVariableName), true);
      } else {
        // TODO!
      }
    }

    String value = createValueCodeFromElement(objectModel, className, configVariable);

    StringBuilder constructorCode = new StringBuilder();
    if (null != id) {
      constructorCode.append("    ").append(targetVariableName);
    } else if (configVariable == null) {
      // no config object was built: create variable for object to build now:
      targetVariableName = createAuxVar(objectElement).getName();
      constructorCode.append("    ")
              .append("var ").append(targetVariableName).append(":").append(className);
    } else if (objectModel.isUseConfigObjects()) {
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
      if (objectModel instanceof MxmlObjectModel) {
        processAttributesAndChildNodes((MxmlObjectModel) objectModel, null, ide, false);
      } else {
        // TODO!
      }
    }
    return targetVariableName;
  }

  private String createValueCodeFromElement(MxmlModel objectModel, String className, Ide configVariable) {
    String value;
    JooSymbol textContentSymbol = getTextContent(objectModel.getSourceElement());
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
    } else if (objectModel instanceof MxmlArrayModel) {
      value = createArrayCodeFromChildElements(((MxmlArrayModel)objectModel).getElements());
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

  private boolean useConfigObjects(CompilationUnit compilationUnit) {
    ClassDeclaration classModel = (ClassDeclaration) compilationUnit.getPrimaryDeclaration();
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

  private String getEventHandlerName(@Nonnull MxmlEventHandlerModel event) {
    JooSymbol value = event.getHandlerCode();
    String eventName = event.getConfigOptionName();
    return "$on_" + eventName.replace('-', '_') + "_" + value.getLine() + "_" + value.getColumn();
  }

  private String createEventHandlerMethod(@Nonnull MxmlEventHandlerModel eventHandlerModel) {
    JooSymbol value = eventHandlerModel.getHandlerCode();
    String eventHandlerName = getEventHandlerName(eventHandlerModel);
    String eventTypeStr = eventHandlerModel.getEventTypeStr();
    compilationUnit.addImport(eventTypeStr);
    StringBuilder classBodyCode = new StringBuilder();
    classBodyCode
            .append("private function ").append(eventHandlerName)
            .append(" (").append("event").append(':').append(eventTypeStr).append(") :void {\n")
            .append("\n    ").append(value.getJooValue())
            .append('}');
    classBodyDirectives.addAll(mxmlParserHelper.parseClassBody(new JooSymbol(classBodyCode.toString())).getDirectives());
    return eventHandlerName;
  }

  private void createAttachEventHandlerCode(@Nonnull Ide ide, @Nonnull MxmlEventHandlerModel event) {
    String eventName = event.getConfigOptionName();
    String eventTypeStr = event.getEventTypeStr();
    String variable = ide.getName();
    String eventNameConstant = (eventName.substring(0, 1) + eventName.substring(1).replaceAll("([A-Z])", "_$1")).toUpperCase();
    String eventHandlerName = getEventHandlerName(event);
    StringBuilder constructorCode = new StringBuilder();
    constructorCode.append("    ").append(variable).append("." + MxmlUtils.ADD_EVENT_LISTENER_METHOD_NAME + "(").append(eventTypeStr)
            .append(".").append(eventNameConstant)
            .append(", ")
            .append(eventHandlerName)
            .append(");");
    constructorBodyDirectives.addAll(mxmlParserHelper.parseConstructorBody(constructorCode.toString()));
  }

  private static String getEventName(@Nonnull Annotation event) {
    Object eventNameModel = event.getPropertiesByName().get("name");
    String eventName = (String) (eventNameModel != null ? eventNameModel : event.getPropertiesByName().get(null));
    if (eventName.startsWith("on")) {
      eventName = eventName.substring(2);
    }
    return eventName;
  }

  private static String getEventTypeStr(@Nonnull Annotation event) {
    Object eventType = event.getPropertiesByName().get("type");
    String eventTypeStr;
    if (eventType instanceof String) {
      eventTypeStr = (String) eventType;
    } else {
      eventTypeStr = "Object";
    }
    return eventTypeStr;
  }

  private void createPropertyAssignmentCode(@Nonnull Ide variable, @Nonnull MxmlPropertyModel propertyModel, @Nonnull JooSymbol value, boolean generatingConfig) {
    Directive propertyAssignment = createPropertyAssignment(variable, propertyModel, value, generatingConfig);
    constructorBodyDirectives.add(propertyAssignment);
  }

  @Nonnull
  private Directive createPropertyAssignment(@Nonnull Ide variable, @Nonnull MxmlPropertyModel propertyModel, @Nonnull JooSymbol value, boolean generatingConfig) {
    String attributeValueAsString = getAttributeValueAsString(propertyModel.getPropertyDeclaration(), value);

    String propertyName = generatingConfig ? propertyModel.getConfigOptionName() : propertyModel.getPropertyDeclaration().getName();
    boolean untypedAccess = true; // untyped || !propertyName.equals(propertyDeclaration.getName());

    Expr rightHandSide = mxmlParserHelper.parseExpression(value.replacingSymAndTextAndJooValue(value.sym, attributeValueAsString, attributeValueAsString));
    return MxmlAstUtils.createPropertyAssignment(variable, rightHandSide, propertyName, untypedAccess);
  }

  private static String getAttributeValueAsString(@Nonnull TypedIdeDeclaration propertyModel, @Nonnull JooSymbol value) {
    return MxmlUtils.valueToString(getAttributeValue(propertyModel, value));
  }

  private static Object getAttributeValue(@Nonnull TypedIdeDeclaration propertyModel, @Nonnull JooSymbol value) {
    TypeRelation typeRelation = propertyModel.getOptTypeRelation();
    String propertyType = typeRelation == null ? null : typeRelation.getType().getIde().getQualifiedNameStr();
    return MxmlUtils.getAttributeValue((String) value.getJooValue(), propertyType);
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
    try {
      String fullClassName = mxmlParserHelper.getClassNameForElement(jangarooParser, element);
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
    return element.getTextNodes().stream().findFirst().orElse(new JooSymbol(""));
  }

  Collection<Directive> getConstructorBodyDirectives() {
    return constructorBodyDirectives;
  }

  Collection<Directive> getClassBodyDirectives() {
    return classBodyDirectives;
  }

  private String getPropertyType(TypedIdeDeclaration propertyModel) {
    TypeRelation optTypeRelation;
    if (propertyModel.isMethod() && ((FunctionDeclaration) propertyModel).isSetter()) {
      Parameters params = ((FunctionDeclaration) propertyModel).getParams();
      optTypeRelation = params.getHead() == null ? null : params.getHead().getOptTypeRelation();
    } else {
      optTypeRelation = propertyModel.getOptTypeRelation();
    }
    return optTypeRelation == null ? null : optTypeRelation.getType().getIde().getQualifiedNameStr();
  }

  class MxmlModel {
    XmlElement sourceElement;
    String id;
    CompilationUnit type;
    Boolean useConfigObjects;
    boolean usePlainObjects;

    XmlElement getSourceElement() {
      return sourceElement;
    }

    public String getId() {
      return id;
    }

    public CompilationUnit getType() {
      return type;
    }

    public void setUseConfigObjects(Boolean useConfigObjects) {
      this.useConfigObjects = useConfigObjects;
    }

    Boolean isUseConfigObjects() {
      if (useConfigObjects == null) {
        useConfigObjects = useConfigObjects(type);
      }
      return useConfigObjects;
    }

    public boolean isUsePlainObjects() {
      return usePlainObjects;
    }

    public void setUsePlainObjects(boolean usePlainObjects) {
      this.usePlainObjects = usePlainObjects;
    }
  }

  class MxmlObjectModel extends MxmlModel {
    List<MxmlMemberModel> members;

    public List<MxmlMemberModel> getMembers() {
      return members;
    }

  }

  class MxmlRootModel extends MxmlObjectModel {
    List<MxmlModel> declarations = Collections.emptyList();
    List<MxmlModel> references = Collections.emptyList();

    public List<MxmlModel> getDeclarations() {
      return declarations;
    }

    public List<MxmlModel> getReferences() {
      return references;
    }
  }

  private class MxmlArrayModel extends MxmlModel {
    List<MxmlModel> elements;

    MxmlArrayModel(List<MxmlModel> elements) {
      this.elements = elements;
    }

    public List<MxmlModel> getElements() {
      return elements;
    }

    @Override
    public void setUseConfigObjects(Boolean useConfigObjects) {
      super.setUseConfigObjects(useConfigObjects);
      elements.forEach(element -> element.setUseConfigObjects(useConfigObjects));
    }
  }

  private class MxmlValueModel extends MxmlModel {
    XmlNode sourceNode;
    JooSymbol value;

    MxmlValueModel(@Nonnull XmlNode sourceNode, @Nonnull JooSymbol value) {
      this.sourceNode = sourceNode;
      this.value = value;
    }

    @Nonnull
    public XmlNode getSourceNode() {
      return sourceNode;
    }

    @Nonnull
    public JooSymbol getValue() {
      return value;
    }
  }

  private abstract class MxmlMemberModel {
    XmlNode sourceNode;
    abstract String getConfigOptionName();
  }

  private class MxmlPropertyModel extends MxmlMemberModel {
    private final TypedIdeDeclaration propertyDeclaration;
    private final MxmlModel value;
    private final String extractXTypePropertyName;

    MxmlPropertyModel(TypedIdeDeclaration propertyDeclaration, @Nonnull MxmlModel value) {
      this.propertyDeclaration = propertyDeclaration;
      this.value = value;
      Boolean useConfigObjects = useConfigObjects(propertyDeclaration);
      if (useConfigObjects != null) {
        value.setUseConfigObjects(useConfigObjects);
      }
      extractXTypePropertyName = MxmlToModelParser.getExtractXTypePropertyName(propertyDeclaration);
      if (extractXTypePropertyName != null) {
        value.setUsePlainObjects(true);
      }
    }

    TypedIdeDeclaration getPropertyDeclaration() {
      return propertyDeclaration;
    }

    String getConfigOptionName() {
      return MxmlToModelParser.getConfigOptionName(getPropertyDeclaration());
    }

    public MxmlModel getValue() {
      return value;
    }

    Boolean isForcingConfigObjects() {
      return useConfigObjects(propertyDeclaration);
    }

    String getExtractXTypePropertyName() {
      return extractXTypePropertyName;
    }
  }

  private class MxmlEventHandlerModel extends MxmlMemberModel {
    Annotation eventType;
    JooSymbol handlerCode;
    private String eventName;
    private String eventTypeStr;

    MxmlEventHandlerModel(Annotation eventType, JooSymbol handlerCode) {
      this.eventType = eventType;
      this.handlerCode = handlerCode;
      eventName = getEventName(eventType);
      eventTypeStr = MxmlToModelParser.getEventTypeStr(eventType);
    }

    @Override
    String getConfigOptionName() {
      return eventName;
    }

    Annotation getEventType() {
      return eventType;
    }

    String getEventTypeStr() {
      return eventTypeStr;
    }

    JooSymbol getHandlerCode() {
      return handlerCode;
    }
  }
}
