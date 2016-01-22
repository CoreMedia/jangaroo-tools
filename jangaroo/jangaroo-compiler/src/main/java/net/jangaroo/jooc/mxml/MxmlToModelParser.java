package net.jangaroo.jooc.mxml;

import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.api.FilePosition;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.model.AnnotationModel;
import net.jangaroo.jooc.model.AnnotationPropertyModel;
import net.jangaroo.jooc.model.ClassModel;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.FieldModel;
import net.jangaroo.jooc.model.MemberModel;
import net.jangaroo.jooc.model.MethodModel;
import net.jangaroo.jooc.model.NamespaceModel;
import net.jangaroo.jooc.model.ParamModel;
import net.jangaroo.jooc.model.PropertyModel;
import net.jangaroo.jooc.util.PreserveLineNumberHandler;
import net.jangaroo.utils.CompilerUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.jangaroo.jooc.util.PreserveLineNumberHandler.getColumnNumber;
import static net.jangaroo.jooc.util.PreserveLineNumberHandler.getLineNumber;


public final class MxmlToModelParser {

  public static final String MXML_UNTYPED_NAMESPACE = "mxml:untyped";
  public static final String MXML_DECLARATIONS = "Declarations";
  public static final String MXML_SCRIPT = "Script";
  public static final String MXML_METADATA = "Metadata";
  public static final String MXML_ID_ATTRIBUTE = "id";
  public static final String MXML_DEFAULT_PROPERTY_ANNOTATION = "DefaultProperty";
  public static final String EXML_MIXINS_PROPERTY_NAME = "__mixins__";

  public static final String ALLOW_CONSTRUCTOR_PARAMETERS_ANNOTATION = "AllowConstructorParameters";
  private static final String EXT_CONFIG_META_NAME = "ExtConfig";

  private static final String CONFIG_MODE_AT_SUFFIX = "$at";
  private static final String CONFIG_MODE_ATTRIBUTE_NAME = "mode";
  private static final Map<String, String> CONFIG_MODE_TO_AT_VALUE = new HashMap<String, String>(); static {
    CONFIG_MODE_TO_AT_VALUE.put("append", "net.jangaroo.ext.Exml.APPEND");
    CONFIG_MODE_TO_AT_VALUE.put("prepend", "net.jangaroo.ext.Exml.PREPEND");
  }


  private static Pattern EXML_VAR_PATTERN = Pattern.compile("(private|protected|internal|public)\\s+var\\s+([A-Za-z_][A-Za-z_0-9]*)");
  private static Pattern INITIALIZE_METHOD_PATTERN = Pattern.compile("private\\s+function\\s+__initialize__\\s*\\(");

  private final JangarooParser jangarooParser;
  private InputSource inputSource;
  private CompilationUnitModel compilationUnitModel;
  private ParamModel configParamModel;
  private Map<String, String> varsDeclaredInScripts;
  private Pattern nativeConstructorPattern;

  private int auxVarIndex;
  private StringBuilder code;
  private boolean hasInitializeMethod;

  /**
   * 
   * @param jangarooParser the Jangaroo parser to use to look up compilation units
   * @param compilationUnitModel the "naked" compilation unit model to fill
   */
  public MxmlToModelParser(JangarooParser jangarooParser, CompilationUnitModel compilationUnitModel) {
    this.jangarooParser = jangarooParser;
    this.compilationUnitModel = compilationUnitModel;
  }

  /**
   * Parses the MXML file into a CompilationUnitModel
   * @param in the input source to parse
   * @throws java.io.IOException if the input stream could not be read
   * @throws org.xml.sax.SAXException if the XML was not well-formed
   */
  public void parse(InputSource in) throws IOException, SAXException {
    inputSource = in;
    String className = compilationUnitModel.getClassModel().getName();
    nativeConstructorPattern = Pattern.compile("(\\s*public)\\s+native(\\s+function\\s+" + className + "\\s*\\(\\s*([A-Za-z_][A-Za-z_0-9]*)\\s*:\\s*([A-Za-z_][A-Za-z_0-9.]*)\\s*=\\s*null\\s*\\)\\s*); *\\n");
    varsDeclaredInScripts = new HashMap<String, String>();
    auxVarIndex = 0;
    code = new StringBuilder();

    BufferedInputStream inputStream = null;
    try {
      inputStream = new BufferedInputStream(in.getInputStream());
      parse(inputStream);
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
  }

  /**
   * Parse the input stream content into a model.
   * Close the input stream after reading.
   *
   * @param inputStream the input stream
   * @throws java.io.IOException  if the input stream could not be read
   * @throws org.xml.sax.SAXException if the XML was not well-formed
   */
  private void parse(InputStream inputStream) throws IOException, SAXException {
    Document document = buildDom(inputStream);
    Element objectNode = document.getDocumentElement();
    String superClassName = createClassNameFromNode(objectNode);

    if (superClassName == null) {
      throw Jooc.error(position(objectNode), "Could not resolve super class from node " + objectNode.getNamespaceURI() + ":" + objectNode.getLocalName());
    }
    String classQName = compilationUnitModel.getQName();
    if (superClassName.equals(classQName)) {
      throw Jooc.error(position(objectNode), "Cyclic inheritance error: super class and this component are the same!. There is something wrong!");
    }
    ClassModel classModel = compilationUnitModel.getClassModel();
    classModel.setSuperclass(superClassName);
    compilationUnitModel.addImport(superClassName);

    processScriptsAndMetadata(objectNode);

    String superConfigVar = null;
    if (constructorSupportsConfigOptionsParameter(superClassName)) {
      superConfigVar = createAuxVar();
      renderConfigAuxVar(superConfigVar, classQName, true);
    }

    if (configParamModel == null || superConfigVar == null) {
      createFields(superConfigVar == null ? "" : superConfigVar, objectNode);
    } else {
      String defaultsConfigVar = createAuxVar();
      renderConfigAuxVar(defaultsConfigVar, classQName, false);
      createFields(defaultsConfigVar, objectNode);
      compilationUnitModel.addImport("net.jangaroo.ext.Exml");
      code.append(MessageFormat.format("\n    {1} = net.jangaroo.ext.Exml.apply({0}, {1});",
              defaultsConfigVar, configParamModel.getName()));
    }

    if (hasInitializeMethod) {
      code.append(String.format("%n    this.__initialize__(%s);",
              configParamModel == null ? "" : configParamModel.getName()));
    }

    processAttributesAndChildNodes(objectNode, superConfigVar, "this", superConfigVar != null);
    MethodModel constructorModel = classModel.createConstructor();
    if (superConfigVar != null) {
      // also let the generated class support a config constructor parameter:
      if (configParamModel == null) {
        configParamModel = new ParamModel("config", classQName, "null");
      }
      constructorModel.addParam(configParamModel);
      compilationUnitModel.addImport("net.jangaroo.ext.Exml");
      code.append(MessageFormat.format("\n    net.jangaroo.ext.Exml.apply({0}, {1});"
                      + "\n    super({0});",
              superConfigVar, configParamModel.getName()));
    }

    constructorModel.setBody(code.toString());
  }

  private void renderConfigAuxVar(String configAuxVar, String type, boolean useCast) {
    code.append(String.format("\n    var %s:%s = %s;", configAuxVar, type, useCast ? type + "({})" : "{}"));
  }

  private boolean constructorSupportsConfigOptionsParameter(String classQName) throws IOException {
    CompilationUnitModel compilationUnitModel = getCompilationUnitModel(classQName);
    if (compilationUnitModel != null) {
      ClassModel classModel = compilationUnitModel.getClassModel();
      if (classModel != null) {
        MethodModel constructorModel = classModel.getConstructor();
        if (constructorModel != null) {
          Iterator<ParamModel> constructorParams = constructorModel.getParams().iterator();
          if (constructorParams.hasNext()) {
            ParamModel firstParam = constructorParams.next();
            if ("config".equals(firstParam.getName())) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  private void processScriptsAndMetadata(Element objectNode) throws IOException {
    ClassModel classModel = compilationUnitModel.getClassModel();
    for (Element element : MxmlUtils.getChildElements(objectNode)) {
      if (MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) {
        String elementName = element.getLocalName();
        if (MXML_SCRIPT.equals(elementName)) {
          String scriptCode = getTextContent(element);
          if (configParamModel == null) {
            Matcher constructorMatcher = nativeConstructorPattern.matcher(scriptCode);
            if (constructorMatcher.find()) {
              configParamModel = new ParamModel(
                      constructorMatcher.group(3),
                      constructorMatcher.group(4),
                      "null");
              scriptCode = scriptCode.substring(0, constructorMatcher.start())
                      + scriptCode.substring(constructorMatcher.end());
            }
          }
          if (!hasInitializeMethod) {
            hasInitializeMethod = INITIALIZE_METHOD_PATTERN.matcher(scriptCode).find();
          }
          Matcher varDeclarationMatcher = EXML_VAR_PATTERN.matcher(scriptCode);
          while (varDeclarationMatcher.find()) {
            varsDeclaredInScripts.put(varDeclarationMatcher.group(2), varDeclarationMatcher.group(1));
          }
          classModel.addBodyCode(scriptCode);
        } else if (MXML_METADATA.equals(elementName)) {
          classModel.addAnnotationCode(getTextContent(element));
        } else if (!MXML_DECLARATIONS.equals(elementName)) {
          throw Jooc.error(position(element), "Unknown MXML element: " + elementName);
        }
      }
    }
  }

  private void createFields(String configVar, Element objectNode) throws IOException {
    for (Element element : MxmlUtils.getChildElements(objectNode)) {
      if (MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) {
        String elementName = element.getLocalName();
        if (MXML_DECLARATIONS.equals(elementName)) {
          for (Element declaration : MxmlUtils.getChildElements(element)) {
            createValueCodeFromElement(configVar, declaration, false, false);
          }
        }
      }
    }
  }

  private boolean processAttributes(Element objectNode, CompilationUnitModel type, String configVariable, String targetVariable, boolean generatingConfig) throws IOException {
    String variable = generatingConfig ? configVariable : targetVariable;
    ClassModel classModel = type == null ? null : type.getClassModel();
    NamedNodeMap attributes = objectNode.getAttributes();
    boolean hasBindings = false;
    boolean hasIdAttribute = attributes.getNamedItem(MXML_ID_ATTRIBUTE) != null;
    for (int i = 0; i < attributes.getLength(); i++) {
      Attr attribute = (Attr) attributes.item(i);
      String propertyName = attribute.getLocalName();
      if (attribute.getNamespaceURI() == null && !MXML_ID_ATTRIBUTE.equals(propertyName) ||
              MXML_UNTYPED_NAMESPACE.equals(attribute.getNamespaceURI())) {
        String value = attribute.getValue();
        MemberModel propertyModel = null;
        if (classModel != null) {
          propertyModel = findPropertyModel(classModel, propertyName);
          if (propertyModel == null) {
            if (generatingConfig && hasIdAttribute) {
              // Only generate config-event-listeners when config has no ID, because otherwise it will be instantiated
              // immediately, anyway, and it is safer to attach event listeners to the actual object.
              hasBindings = true;
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
          propertyModel = createDynamicPropertyModel(objectNode, type, propertyName, MXML_UNTYPED_NAMESPACE.equals(attribute.getNamespaceURI()));
        }
        if (createPropertyAssignmentCodeWithBindings(configVariable, targetVariable, generatingConfig, value, propertyModel)) {
          hasBindings = true;
        }
      }
    }
    return hasBindings;
  }

  private boolean createPropertyAssignmentCodeWithBindings(String configVariable, String targetVariable, boolean generatingConfig, String value, MemberModel propertyModel) {
    String variable = generatingConfig ? configVariable : targetVariable;
    if (false && MxmlUtils.isBindingExpression(value) && hasSetter(propertyModel)) {
      if (generatingConfig) {
        createPropertyAssignmentCode(configVariable, propertyModel,
                MxmlUtils.createBindingExpression(getOrCreateExpressionMethod(targetVariable, propertyModel, value) + "()"), true);
      }
      return true;
    } else {
      // skip property assignment to target object if it was already contained in config object: 
      if (generatingConfig || configVariable == null) {
        // default: create a normal property assignment:
        createPropertyAssignmentCode(variable, propertyModel, value, generatingConfig);
      }
      return false;
    }
  }

  private static boolean hasSetter(MemberModel memberModel) {
    return hasAnnotationAtSetter(memberModel, Jooc.BINDABLE_ANNOTATION_NAME);
  }

  private static boolean hasAnnotationAtSetter(MemberModel memberModel, String annotationName) {
    MemberModel setter = null;
    if (memberModel instanceof PropertyModel) {
      setter = ((PropertyModel) memberModel).getSetter();
    } else if (memberModel instanceof FieldModel && !((FieldModel) memberModel).isConst()) {
      setter = memberModel;
    }
    return setter != null && !setter.getAnnotations(annotationName).isEmpty();
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

  private boolean processAttributesAndChildNodes(Element objectNode, String configVariable, String targetVariable, boolean generatingConfig) throws IOException {
    CompilationUnitModel type = getCompilationUnitModel(objectNode);
    boolean hasBindings = processAttributes(objectNode, type, configVariable, targetVariable, generatingConfig);
    if (processChildNodes(objectNode, type, configVariable, targetVariable, generatingConfig)) {
      hasBindings = true;
    }
    return hasBindings;
  }

  private boolean processChildNodes(Element objectNode, CompilationUnitModel type, String configVariable, String targetVariable, boolean generatingConfig) throws IOException {
    String variable = generatingConfig ? configVariable : targetVariable;
    boolean hasBindings = false;
    ClassModel classModel = type == null ? null : type.getClassModel();
    List<Element> childNodes = MxmlUtils.getChildElements(objectNode);
    MemberModel defaultPropertyModel = findDefaultPropertyModel(classModel);
    List<Element> defaultPropertyValues = new ArrayList<Element>();
    for (Element element : childNodes) {
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
            } else if (EXML_MIXINS_PROPERTY_NAME.equals(getConfigOptionName(propertyModel))) {
              List<Element> exmlMixins = MxmlUtils.getChildElements(element);
              for (Element exmlMixin : exmlMixins) {
                processAttributesAndChildNodes(exmlMixin, configVariable, targetVariable, generatingConfig);
              }
              continue;
            }
          }
        }
        if (propertyModel == null && defaultPropertyModel != null && createClassNameFromNode(element) != null) {
          // collect item to add it to the default property later:
          defaultPropertyValues.add(element);
        } else {
          if (propertyModel == null) {
            propertyModel = createDynamicPropertyModel(element, type, propertyName, false);
          }
          List<Element> childElements = MxmlUtils.getChildElements(element);
          if (childElements.isEmpty()) {
            if (createPropertyAssignmentCodeWithBindings(configVariable, targetVariable, generatingConfig, getTextContent(element), propertyModel)) {
              hasBindings = true;
            }
          } else {
            createChildElementsPropertyAssignmentCode(childElements, variable, propertyModel, generatingConfig);
          }
          String configMode = "Array".equals(propertyModel.getType()) ? element.getAttributeNS(Exmlc.EXML_NAMESPACE_URI, CONFIG_MODE_ATTRIBUTE_NAME) : "";
          String atValue = CONFIG_MODE_TO_AT_VALUE.get(configMode);
          if (atValue != null) {
            String atPropertyName = generatingConfig ? getConfigOptionName(propertyModel) : propertyModel.getName();
            code.append(String.format("\n    %s.%s = %s;", variable, atPropertyName + CONFIG_MODE_AT_SUFFIX, atValue));
          }
        }
      }
    }
    if (!defaultPropertyValues.isEmpty()) {
      createChildElementsPropertyAssignmentCode(defaultPropertyValues, variable, defaultPropertyModel, generatingConfig);
    }
    return hasBindings;
  }

  private void createChildElementsPropertyAssignmentCode(List<Element> childElements, String variable,
                                                         MemberModel propertyModel, boolean generatingConfig) throws IOException {
    boolean forceArray = "Array".equals(propertyModel.getType());
    AnnotationModel allowConstructorAnnotation = getAnnotationAtSetter(propertyModel, ALLOW_CONSTRUCTOR_PARAMETERS_ANNOTATION);
    boolean allowConstructorParameters = allowConstructorAnnotation != null;
    boolean suppressType = allowConstructorParameters && allowConstructorAnnotation.getPropertiesByName().get("suppressType") != null;
    String value = createArrayCodeFromChildElements(childElements, forceArray, allowConstructorParameters, suppressType);
    createPropertyAssignmentCode(variable, propertyModel, MxmlUtils.createBindingExpression(value), generatingConfig);
  }

  private String createArrayCodeFromChildElements(List<Element> childElements, boolean forceArray, boolean allowConstructorParameters, boolean suppressType) throws IOException {
    List<String> arrayItems = new ArrayList<String>();
    for (Element arrayItemNode : childElements) {
      String itemValue = createValueCodeFromElement("", arrayItemNode, allowConstructorParameters, suppressType);
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

  private String createValueCodeFromElement(String configVar, Element objectElement, boolean allowConstructorParameters, boolean suppressType) throws IOException {
    String className = createClassNameFromNode(objectElement);
    if (className == null) {
      throw Jooc.error(position(objectElement), "Could not resolve class from MXML node " + objectElement.getNamespaceURI() + ":" + objectElement.getLocalName());
    }
    compilationUnitModel.addImport(className);
    String targetVariable = null;   // name of the variable holding the object to build
    String id = objectElement.getAttribute(MXML_ID_ATTRIBUTE);
    if (!id.isEmpty()) {
      if (configParamModel != null && id.equals(configParamModel.getName())) {
        return "";
      }

      String scriptDeclarationNamespace = varsDeclaredInScripts.get(id);
      String qualifier = configVar;
      if (scriptDeclarationNamespace != null) {
        if (!NamespaceModel.PUBLIC.equals(scriptDeclarationNamespace)) {
          qualifier = ""; // corresponds to "this."
        }
      } else {
        FieldModel fieldModel = new FieldModel(id, className);
        fieldModel.addAnnotation(new AnnotationModel(Jooc.BINDABLE_ANNOTATION_NAME));
        if (constructorSupportsConfigOptionsParameter(className)) {
          fieldModel.addAnnotation(new AnnotationModel(ALLOW_CONSTRUCTOR_PARAMETERS_ANNOTATION));
        }
        compilationUnitModel.getClassModel().addMember(fieldModel);
      }
      targetVariable = CompilerUtils.qName(qualifier, id);
    }

    String configVariable = null; // name of the variable holding the config object to use in the constructor
    boolean hasBindings = false;
    if (constructorSupportsConfigOptionsParameter(className)) {
      // if class supports a config options parameter, create a config options object and assign properties to it:
      configVariable = createAuxVar();
      renderConfigAuxVar(configVariable, className, !suppressType);
      if (targetVariable == null) {
        targetVariable = createAuxVar();
      }
      // process attributes and children, using a forward reference to the object to build inside bindings:
      hasBindings = processAttributesAndChildNodes(objectElement, configVariable, targetVariable, true);
    }

    String value;
    if ("String".equals(className)) {
      String stringValue = getTextContent(objectElement);
      value = CompilerUtils.quote(stringValue);
    } else if ("int".equals(className) || "uint".equals(className) || "Number".equals(className)) {
      value = getTextContent(objectElement);
      if (MxmlUtils.isBindingExpression(value)) {
        value = MxmlUtils.getBindingExpression(value);
      } else if (value.isEmpty()) {
        value = null;
      }
    } else if ("Object".equals(className)) {
      value = "{}";
    } else if ("Array".equals(className)) {
      value = createArrayCodeFromChildElements(MxmlUtils.getChildElements(objectElement), true, allowConstructorParameters, suppressType);
    } else {
      StringBuilder valueBuilder = new StringBuilder();
      valueBuilder.append("new ").append(className).append("(");
      if (configVariable != null) {
        valueBuilder.append(configVariable);
      }
      valueBuilder.append(")");
      value = valueBuilder.toString();
    }
    
    if (id.length() > 0) {
      if (!configVar.isEmpty() // it is a declaration...
              && objectElement.getAttributes().getLength() == 1 // ...with only an id attribute...
              && objectElement.getChildNodes().getLength() == 0 // ...and no sub-elements or text content!
              ) {
        // prevent assigning a default value for such an empty declaration:
        return null;
      }
      code.append("\n    ").append(targetVariable);
    } else if (configVariable == null /*|| hasBindings*/) {
      if (targetVariable == null) {
        // no config object was built: create variable for object to build now:
        targetVariable = createAuxVar();
      }
      code.append("\n    ").append("var ").append(targetVariable).append(":").append(className);
    } else if (allowConstructorParameters) {
      return configVariable;
    } else {
      return value; // no aux var necessary
    }
    code.append(" = ").append(value).append(";");

    if (configVariable == null && !"Array".equals(className) /*|| hasBindings*/) {
      // no config object was built or event listeners or bindings have to be added:
      // process attribute and children and assign properties directly on the target object
      processAttributesAndChildNodes(objectElement, configVariable, targetVariable, false);
    }
    return targetVariable;
  }

  private String createAuxVar() {
    return "$_" + (++auxVarIndex);
  }

  private void createEventHandlerCode(String variable, String value, AnnotationModel event) {
    AnnotationPropertyModel eventType = event.getPropertiesByName().get("type");
    String eventTypeStr = eventType == null ? "Object" : eventType.getStringValue();
    compilationUnitModel.addImport(eventTypeStr);
    AnnotationPropertyModel eventNameModel = event.getPropertiesByName().get("name");
    String eventName = (eventNameModel != null ? eventNameModel : event.getPropertiesByName().get(null)).getStringValue();
    if (eventName.startsWith("on")) {
      eventName = eventName.substring(2);
    }
    String eventHandlerName = "$on_" + variable + "_" + eventName.replace('-', '_');
    MethodModel eventHandler = new MethodModel(eventHandlerName, "void",
            new ParamModel("event", eventTypeStr));
    eventHandler.setNamespace(NamespaceModel.PRIVATE);
    eventHandler.setBody(value);
    compilationUnitModel.getClassModel().addMember(eventHandler);
    compilationUnitModel.addImport("joo.addEventListener");
    code.append("\n    ").append("joo.addEventListener(").append(variable).append(", ")
            .append(CompilerUtils.quote(eventName)).append(", ")
            .append(eventHandlerName).append(", ")
            .append(eventTypeStr).append(");");
  }

  private String getOrCreateExpressionMethod(String variable, MemberModel propertyModel, String value) {
    String expressionMethodName = "$bind_" + variable + "_" + propertyModel.getName();
    MethodModel bindingMethod = compilationUnitModel.getClassModel().getMethod(null, expressionMethodName);
    if (bindingMethod == null) {
      bindingMethod = new MethodModel(expressionMethodName, propertyModel.getType());
      bindingMethod.setNamespace(NamespaceModel.PRIVATE);
      bindingMethod.setBody("return " + MxmlUtils.getBindingExpression(value) + ";");
      compilationUnitModel.getClassModel().addMember(bindingMethod);
    }
    return expressionMethodName;
  }

  private void createPropertyAssignmentCode(String variable, MemberModel propertyModel, String value, boolean generatingConfig) {
    String attributeValueAsString = MxmlUtils.valueToString(getPropertyValue(propertyModel, value));
    String propertyName = generatingConfig ? getConfigOptionName(propertyModel) : propertyModel.getName();
    code.append("\n    ").append(getPropertyAssignmentCode(variable, propertyName, attributeValueAsString));
  }

  private static String getPropertyAssignmentCode(String variable, String propertyName, String attributeValueAsString) {
    String assignment = MessageFormat.format("{0} = {1};", propertyName, attributeValueAsString);
    return "this".equals(variable)
            ? assignment
            : MessageFormat.format("{0}.{1}", variable, assignment);
  }

  private static String getConfigOptionName(MemberModel propertyModel) {
    if (propertyModel instanceof PropertyModel) {
      MethodModel setter = ((PropertyModel) propertyModel).getSetter();
      if (setter != null) { // should actually be there, or the assignment would not work!
        Iterator<AnnotationModel> configOptionAnnotations = setter.getAnnotations(EXT_CONFIG_META_NAME).iterator();
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

  private CompilationUnitModel getCompilationUnitModel(String fullClassName) throws IOException {
    if (fullClassName == null) {
      return null;
    }
    return jangarooParser.getCompilationUnitModel(fullClassName);
  }

  private CompilationUnitModel getCompilationUnitModel(Element element) throws IOException {
    String fullClassName = createClassNameFromNode(element);
    if (fullClassName == null) {
      return null;
    }
    try {
      return getCompilationUnitModel(fullClassName);
    } catch (CompilerError e) {
      // ugly to catch-and-rethrow, I know, but we need to add position information here...
      throw Jooc.error(position(element), e.getMessage());
    }
  }

  private MemberModel findPropertyModel(ClassModel classModel, String propertyName) throws IOException {
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

  private AnnotationModel findEvent(ClassModel classModel, String propertyName) throws IOException {
    for (ClassModel current = classModel; current != null; current = getSuperClassModel(current)) {
      AnnotationModel eventModel = current.getEvent(propertyName);
      if (eventModel != null) {
        return eventModel;
      }
    }
    return null;
  }

  private MemberModel findDefaultPropertyModel(ClassModel classModel) throws IOException {
    for (ClassModel current = classModel; current != null; current = getSuperClassModel(current)) {
      MemberModel defaultPropertyModel = current.findPropertyWithAnnotation(false, MXML_DEFAULT_PROPERTY_ANNOTATION);
      if (defaultPropertyModel != null) {
        return defaultPropertyModel;
      }
    }
    return null;
  }

  private MemberModel createDynamicPropertyModel(Node element, CompilationUnitModel compilationUnitModel, String name, boolean allowAnyProperty) {
    if (!allowAnyProperty && compilationUnitModel != null && compilationUnitModel.getClassModel() != null && !compilationUnitModel.getClassModel().isDynamic()) {
      // dynamic property of a non-dynamic class: warn!
      Jooc.warning(position(element), "MXML: property " + name + " not found in class " + compilationUnitModel.getQName() + ".");
    }
    return new FieldModel(name, "*");
  }

  private ClassModel getSuperClassModel(ClassModel classModel) throws IOException {
    String superclass = classModel.getSuperclass();
    if (superclass != null) {
      CompilationUnitModel superCompilationUnitModel = getCompilationUnitModel(superclass);
      if (superCompilationUnitModel != null && superCompilationUnitModel.getPrimaryDeclaration() instanceof ClassModel) {
        return superCompilationUnitModel.getClassModel();
      }
    }
    return null;
  }

  private Object getPropertyValue(MemberModel propertyModel, String value) {
    return MxmlUtils.getAttributeValue(value, propertyModel == null ? null : propertyModel.getType());
  }

  private String createClassNameFromNode(Node objectNode) {
    String name = objectNode.getLocalName();
    String uri = objectNode.getNamespaceURI();
    if (uri != null) {
      String packageName = MxmlUtils.parsePackageFromNamespace(uri);
      if (packageName != null) {
        String qName = CompilerUtils.qName(packageName, name);
        if (qName.equals(compilationUnitModel.getQName())) {
          return qName;
        }
        CompilationUnit compilationsUnit = jangarooParser.getCompilationUnit(qName);
        if (compilationsUnit != null && compilationsUnit.getPrimaryDeclaration() instanceof ClassDeclaration) {
          return qName;
        }
      } else {
        return jangarooParser.getMxmlComponentRegistry().getClassName(uri, name);
      }
    }
    return null;
  }

  private static String getTextContent(Element element) {
    return element.getChildNodes().getLength() == 1 && element.getFirstChild().getNodeType() == Node.TEXT_NODE ? ((Text) element.getFirstChild()).getData() : "";
  }

  private Document buildDom(InputStream inputStream) throws SAXException, IOException {
    SAXParser parser;
    final Document doc;
    try {
      final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
      saxFactory.setNamespaceAware(true);
      parser = saxFactory.newSAXParser();
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(false);
      doc = factory.newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("a default dom builder should be provided", e);
    }
    PreserveLineNumberHandler handler = new PreserveLineNumberHandler(doc);
    parser.parse(inputStream, handler);
    return doc;
  }

  private FilePosition position(Node node) {
    final int lineNumber = getLineNumber(node);
    final int columnNumber = getColumnNumber(node);
    return new FilePosition() {
      @Override
      public String getFileName() {
        return inputSource.getPath();
      }

      @Override
      public int getLine() {
        return lineNumber;
      }

      @Override
      public int getColumn() {
        return columnNumber;
      }
    };
  }

}
