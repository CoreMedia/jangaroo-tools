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
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.Parameters;
import net.jangaroo.jooc.ast.PropertyDeclaration;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.AS3Type;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.jangaroo.jooc.mxml.ast.MxmlToModelParser.InstantiationMode.*;

final class MxmlToModelParser {

  private static final String EXT_CONFIG_CREATE_FLAG = "create";
  private static final String EXT_CONFIG_EXTRACT_XTYPE_PARAMETER = "extractXType";

  private static final String CONFIG_MODE_AT_SUFFIX = "$at";
  private static final String CONFIG_MODE_ATTRIBUTE_NAME = "mode";
  private static final Map<String, String> CONFIG_MODE_TO_AT_VALUE = new HashMap<>();

  private static final String UNTYPED_MARKER = "__UNTYPED__";

  static {
    CONFIG_MODE_TO_AT_VALUE.put("append", "net.jangaroo.ext.Exml.APPEND");
    CONFIG_MODE_TO_AT_VALUE.put("prepend", "net.jangaroo.ext.Exml.PREPEND");
  }

  private final JangarooParser jangarooParser;
  private final MxmlParserHelper mxmlParserHelper;
  private MxmlRootModel mxmlRootModel;
  private List<MxmlModel> mxmlModelsWithId;

  MxmlToModelParser(JangarooParser jangarooParser, MxmlParserHelper mxmlParserHelper) {
    this.jangarooParser = jangarooParser;
    this.mxmlParserHelper = mxmlParserHelper;
  }

  MxmlRootModel parse(XmlElement objectNode) {
    mxmlRootModel = new MxmlRootModel();
    mxmlModelsWithId = new ArrayList<>();
    fillObjectModel(mxmlRootModel, objectNode, getCompilationUnitModel(objectNode));
    mxmlModelsWithId.removeAll(mxmlRootModel.getDeclarations().getElements());
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
      members.addAll(createMemberModelsFromAttribute(objectNode, type, attribute));
    }
    TypedIdeDeclaration defaultPropertyModel = findDefaultPropertyModel((ClassDeclaration) type.getPrimaryDeclaration());
    List<XmlElement> defaultValues = defaultPropertyModel != null ? new ArrayList<>() : null;
    boolean hasDefaultPropertyModel = defaultValues != null;
    for (XmlElement element : objectNode.getElements()) {
      if (element.isBuiltInElement()) {
        if (MxmlUtils.MXML_DECLARATIONS.equals(element.getLocalName())) {
          if (objectModel == mxmlRootModel) {
            mxmlRootModel.declarations = createArrayModel(element.getElements());
          } else {
            jangarooParser.getLog().error(element.getSymbol(), "Declarations are only allowed directly inside the MXML root element.");
          }
        }
      } else {
        List<MxmlMemberModel> memberModels = createMemberModelsFromElement(objectNode, type, hasDefaultPropertyModel, element);
        if (memberModels.isEmpty()) {
          if (hasDefaultPropertyModel) {
            defaultValues.add(element);
          } else {
            jangarooParser.getLog().error(objectNode.getSymbol(), "Element not allowed here.");
          }
        }
        members.addAll(memberModels);
      }
    }
    if (hasDefaultPropertyModel && !defaultValues.isEmpty()) {
      members.add(new MxmlPropertyModel(null, defaultPropertyModel, createPropertyValue(objectNode, defaultPropertyModel.getName(), defaultValues)));
    }
    objectModel.members = members;
  }

  private List<MxmlMemberModel> createMixinsPropertyModels(MxmlPropertyModel memberModel) {
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
      jangarooParser.getLog().error(memberModel.getSourceNode().getSymbol(),
              "MXML mixins property must contain a list of sub-elements.");
    }
    return members;
  }

  private List<MxmlPropertyModel> createExtractedXTypePropertyModel(MxmlPropertyModel propertyModel) {
    String extractXTypePropertyName = propertyModel.getExtractXTypePropertyName();
    if (extractXTypePropertyName != null && !extractXTypePropertyName.isEmpty()) {
      MxmlModel propertyValueModel = propertyModel.getValue();
      ClassDeclaration classDeclaration = propertyModel.getPropertyDeclaration().getClassDeclaration();
      TypedIdeDeclaration extractXTypePropertyModel = classDeclaration.getMemberDeclaration(extractXTypePropertyName);
      if (extractXTypePropertyModel == null) {
        jangarooParser.getLog().error(String.format("Annotation [ExtConfig(extractXType=\"%s\")] in class %s refers to a non-existing property.", extractXTypePropertyName, classDeclaration.getQualifiedNameStr()));
      } else {
        if (propertyValueModel.getType() != null) {
          String extractedXTypeCode = MxmlUtils.createBindingExpression(propertyValueModel.getType().getQualifiedNameStr() + "['prototype'].xtype");
          MxmlValueModel extratedXTypeValue = new MxmlValueModel(propertyValueModel.getSourceElement(), new JooSymbol(extractedXTypeCode));
          return Collections.singletonList(new MxmlPropertyModel(propertyModel.getSourceNode(), extractXTypePropertyModel, extratedXTypeValue));
        }
      }
    }
    return Collections.emptyList();
  }

  @Nonnull
  private List<MxmlPropertyModel> createArrayAtPropertyModel(XmlElement element, MxmlPropertyModel propertyModel) {
    if ("Array".equals(getPropertyType(propertyModel.getPropertyDeclaration()))) {
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
        return Collections.singletonList(new MxmlPropertyModel(propertyModel.getSourceNode(), propertyModel.getPropertyDeclaration(), atValueModel) {
          @Override
          String getConfigOptionName() {
            return super.getConfigOptionName() + CONFIG_MODE_AT_SUFFIX;
          }
        });
      }
    }
    return Collections.emptyList();
  }

  private MxmlArrayModel createArrayModel(List<XmlElement> elements) {
    return new MxmlArrayModel(elements.stream().map(this::createModel).collect(Collectors.toList()));
  }

  private MxmlValueModel createValueModel(@Nonnull XmlNode sourceNode, @Nonnull JooSymbol textContent) {
    return new MxmlValueModel(sourceNode, textContent);
  }

  private List<MxmlMemberModel> createPropertyModels(XmlNode sourceNode, TypedIdeDeclaration propertyDeclaration) {
    List<MxmlMemberModel> properties = new ArrayList<>();
    MxmlPropertyModel propertyModel = createPropertyModel(sourceNode, propertyDeclaration);
    if (MxmlUtils.EXML_MIXINS_PROPERTY_NAME.equals(propertyModel.getConfigOptionName())) {
      properties.addAll(createMixinsPropertyModels(propertyModel));
    } else {
      if (sourceNode instanceof XmlElement) {
        properties.addAll(createArrayAtPropertyModel((XmlElement) sourceNode, propertyModel));
        properties.addAll(createExtractedXTypePropertyModel(propertyModel));
      }
      properties.add(propertyModel);
    }
    return properties;
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
    return new MxmlPropertyModel(sourceNode, propertyDeclaration, value);
  }

  private MxmlModel createPropertyValue(XmlNode sourceNode, String propertyType, List<XmlElement> elements) {
    MxmlModel value;
    if ("Array".equals(propertyType) || elements.size() > 1 && (propertyType == null || "*".equals(propertyType) || "Object".equals(propertyType))) {
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
    return new MxmlEventHandlerModel(sourceNode, eventType, handlerCode);
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

  private List<MxmlMemberModel> createMemberModelsFromAttribute(XmlElement objectNode, CompilationUnit type, XmlAttribute attribute) {
    if (!RootElementProcessor.alreadyProcessed(attribute)) {
      String attributeNamespaceUri = objectNode.getNamespaceUri(attribute.getPrefix());
      boolean isUntypedAccess = MxmlUtils.EXML_UNTYPED_NAMESPACE.equals(attributeNamespaceUri);
      if (!isIdAttributePredicate().test(attribute)) {
        String propertyName = attribute.getLocalName();
        if (attribute.getPrefix() == null || isUntypedAccess) {
          ClassDeclaration classModel = type == null ? null : (ClassDeclaration) type.getPrimaryDeclaration();
          TypedIdeDeclaration propertyModel = null;
          if (!isUntypedAccess && classModel != null) {
            propertyModel = findPropertyModel(classModel, propertyName);
            if (propertyModel == null) {
              Annotation eventModel = findEvent(classModel, propertyName);
              if (eventModel != null) {
                return Collections.singletonList(createEventHandlerModel(attribute, eventModel));
              }
            }
          }
          if (propertyModel == null) {
            propertyModel = createDynamicPropertyModel(objectNode, type, propertyName, isUntypedAccess);
          }
          return createPropertyModels(attribute, propertyModel);
        }
      }
    }
    return Collections.emptyList();
  }

  @Nonnull
  private List<MxmlMemberModel> createMemberModelsFromElement(XmlElement objectNode, CompilationUnit type, boolean hasDefaultPropertyModel, XmlElement element) {
    TypedIdeDeclaration propertyModel = null;
    String propertyName = element.getLocalName();
    ClassDeclaration classModel = type == null ? null : (ClassDeclaration) type.getPrimaryDeclaration();
    if (objectNode.getNamespaceURI().equals(element.getNamespaceURI())) {
      if (classModel != null) {
        propertyModel = findPropertyModel(classModel, propertyName);
        if (propertyModel == null) {
          Annotation eventModel = findEvent(classModel, propertyName);
          if (eventModel != null) {
            return Collections.singletonList(createEventHandlerModel(element, eventModel));
          }
        }
      }
      if (propertyModel == null && !hasDefaultPropertyModel) {
        propertyModel = createDynamicPropertyModel(element, type, propertyName, false);
      }
    }
    if (propertyModel != null) {
      return createPropertyModels(element, propertyModel);
    }
    // if propertyDeclaration is still null, item will be used as value for default property!
    return Collections.emptyList();
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
  static JooSymbol getTextContent(XmlElement element) {
    return element.getTextNodes().stream().findFirst().orElse(new JooSymbol(""));
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

  enum InstantiationMode {
    MXML,
    PLAIN,
    EXT_CONFIG,
    EXT_CREATE;

    static InstantiationMode from(boolean useConfigObjects) {
      return useConfigObjects ? EXT_CONFIG : EXT_CREATE;
    }

    public boolean isExt() {
      return this != MXML;
    }

    public boolean isConfig() {
      return this == EXT_CONFIG || this == PLAIN;
    }
  }

  class MxmlModel {
    private XmlElement sourceElement;
    private String id;
    private CompilationUnit type;
    private InstantiationMode instantiationMode;

    XmlElement getSourceElement() {
      return sourceElement;
    }

    String getId() {
      return id;
    }

    CompilationUnit getType() {
      return type;
    }

    void setUseConfigObjects(Boolean useConfigObjects) {
      if (useConfigObjects != null && instantiationMode == null) {
        instantiationMode = InstantiationMode.from(useConfigObjects);
      }
    }

    @Nonnull
    InstantiationMode getInstantiationMode() {
      if (instantiationMode == null) {
        instantiationMode = type == null || !CompilationUnitUtils.constructorSupportsConfigOptionsParameter(type) ? MXML
                : id == null ? InstantiationMode.from(useConfigObjects(type)) : EXT_CREATE;
      } else {
        if (id != null && instantiationMode == EXT_CONFIG) {
          instantiationMode = EXT_CREATE;
        }
      }
      return instantiationMode;
    }

    private void doUsePlainObjects() {
      instantiationMode = PLAIN;
    }
  }

  private static <T, S> Stream<S> filterByType(List<T> list, Class<S> type) {
    return list.stream()
            .map(member -> type.isInstance(member) ? type.cast(member) : null)
            .filter(Objects::nonNull);
  }

  class MxmlObjectModel extends MxmlModel {
    List<MxmlMemberModel> members;

    List<MxmlMemberModel> getMembers() {
      return members;
    }

    Stream<MxmlPropertyModel> getProperties() {
      return filterByType(members, MxmlPropertyModel.class);
    }

    Stream<MxmlEventHandlerModel> getEventHandlers() {
      return filterByType(members, MxmlEventHandlerModel.class);
    }
  }

  class MxmlRootModel extends MxmlObjectModel {
    MxmlArrayModel declarations = new MxmlArrayModel(Collections.emptyList());
    List<MxmlModel> references = Collections.emptyList();

    MxmlArrayModel getDeclarations() {
      return declarations;
    }

    List<MxmlModel> getReferences() {
      return references;
    }
  }

  class MxmlArrayModel extends MxmlModel {
    List<MxmlModel> elements;

    MxmlArrayModel(List<MxmlModel> elements) {
      this.elements = elements;
    }

    List<MxmlModel> getElements() {
      return elements;
    }

    @Override
    void setUseConfigObjects(Boolean useConfigObjects) {
      super.setUseConfigObjects(useConfigObjects);
      elements.forEach(element -> element.setUseConfigObjects(useConfigObjects));
    }
  }

  class MxmlValueModel extends MxmlModel {
    private final XmlNode sourceNode;
    private final JooSymbol value;

    MxmlValueModel(@Nonnull XmlNode sourceNode, @Nonnull JooSymbol value) {
      this.sourceNode = sourceNode;
      this.value = value;
    }

    @Nonnull
    XmlNode getSourceNode() {
      return sourceNode;
    }

    @Nonnull
    JooSymbol getValue() {
      return value;
    }
  }

  abstract class MxmlMemberModel {
    private final XmlNode sourceNode;

    MxmlMemberModel(XmlNode sourceNode) {
      this.sourceNode = sourceNode;
    }

    XmlNode getSourceNode() {
      return sourceNode;
    }
  }

  class MxmlPropertyModel extends MxmlMemberModel {
    private final TypedIdeDeclaration propertyDeclaration;
    private final MxmlModel value;
    private final String extractXTypePropertyName;

    MxmlPropertyModel(XmlNode sourceNode, TypedIdeDeclaration propertyDeclaration, @Nonnull MxmlModel value) {
      super(sourceNode);
      this.propertyDeclaration = propertyDeclaration;
      this.value = value;
      Boolean useConfigObjects = useConfigObjects(propertyDeclaration);
      if (useConfigObjects != null) {
        value.setUseConfigObjects(useConfigObjects);
      }
      extractXTypePropertyName = MxmlToModelParser.getExtractXTypePropertyName(propertyDeclaration);
      if (extractXTypePropertyName != null) {
        value.doUsePlainObjects();
      }
    }

    TypedIdeDeclaration getPropertyDeclaration() {
      return propertyDeclaration;
    }

    String getConfigOptionName() {
      return MxmlToModelParser.getConfigOptionName(getPropertyDeclaration());
    }

    MxmlModel getValue() {
      return value;
    }

    String getExtractXTypePropertyName() {
      return extractXTypePropertyName;
    }
  }

  class MxmlEventHandlerModel extends MxmlMemberModel {
    JooSymbol handlerCode;
    private String eventName;
    private String eventTypeStr;

    MxmlEventHandlerModel(XmlNode sourceNode, Annotation eventType, JooSymbol handlerCode) {
      super(sourceNode);
      this.handlerCode = handlerCode;
      eventName = MxmlToModelParser.getEventName(eventType);
      eventTypeStr = MxmlToModelParser.getEventTypeStr(eventType);
    }

    String getFlexEventName() {
      return eventName;
    }
 
    String getExtEventName() {
      if (eventName.startsWith("on")) {
        return eventName.substring(2).toLowerCase();
      }
      return eventName.toLowerCase();
    }

    String getEventTypeStr() {
      return eventTypeStr;
    }

    JooSymbol getHandlerCode() {
      return handlerCode;
    }
  }

  private static String getEventName(@Nonnull Annotation event) {
    Object eventNameModel = event.getPropertiesByName().get("name");
    return (String) (eventNameModel != null ? eventNameModel : event.getPropertiesByName().get(null));
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


}
