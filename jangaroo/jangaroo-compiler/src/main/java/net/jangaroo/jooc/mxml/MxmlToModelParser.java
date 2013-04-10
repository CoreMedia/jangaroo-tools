package net.jangaroo.jooc.mxml;

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.backend.ApiModelGenerator;
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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class MxmlToModelParser {

  public static final String MXML_UNTYPED_NAMESPACE = "mxml:untyped";
  public static final String MXML_DECLARATIONS = "Declarations";
  public static final String MXML_SCRIPT = "Script";
  public static final String MXML_METADATA = "Metadata";
  public static final String MXML_ID_ATTRIBUTE = "id";
  public static final String MXML_DEFAULT_PROPERTY_ANNOTATION = "DefaultProperty";
  public static final String RESOURCE_MANAGER_QNAME = "mx.resources.ResourceManager";
  public static final Pattern AT_RESOURCE_PATTERN = Pattern.compile("^\\s*@Resource\\s*\\(\\s*bundle\\s*=\\s*['\"]([a-zA-Z0-9_$]+)['\"]\\s*,\\s*key\\s*=\\s*['\"]([a-zA-Z0-9_$]+)['\"]\\s*\\)\\s*$");
  public static final String RESOURCE_ACCESS_CODE = "{%s.getInstance().getString(\"%s\",\"%s\")}";
  private final JangarooParser jangarooParser;

  private CompilationUnitModel compilationUnitModel;
  private int auxVarIndex;
  private StringBuilder code;

  public MxmlToModelParser(JangarooParser jangarooParser) {
    this.jangarooParser = jangarooParser;
  }

  /**
   * Parses the MXML file into a CompilationUnitModel
   * @param in the input source to parse
   * @return the parsed model
   * @throws java.io.IOException if the input stream could not be read
   * @throws org.xml.sax.SAXException if the XML was not well-formed
   */
  public CompilationUnitModel parse(InputSource in) throws IOException, SAXException {
    String qName = CompilerUtils.qNameFromRelativPath(in.getRelativePath());
    compilationUnitModel = new CompilationUnitModel(CompilerUtils.packageName(qName),
            new ClassModel(CompilerUtils.className(qName)));
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

    return compilationUnitModel;
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
      throw Jooc.error("Could not resolve super class from node " + objectNode.getNamespaceURI() + ":" + objectNode.getLocalName());
    }
    if (superClassName.equals(compilationUnitModel.getQName())) {
      throw Jooc.error("Cyclic inheritance error: super class and this component are the same!. There is something wrong!");
    }
    ClassModel classModel = compilationUnitModel.getClassModel();
    classModel.setSuperclass(superClassName);
    compilationUnitModel.addImport(superClassName);

    MethodModel constructorModel = classModel.createConstructor();

    String superConfigVar = null;
    boolean hasBindings = false;
    FieldModel bindingsField = new FieldModel("$bindings", "Array");
    bindingsField.setNamespace(NamespaceModel.PRIVATE);
    classModel.addMember(bindingsField);
    code.append("\n    $bindings = [];");
    if (constructorSupportsConfigOptionsParameter(superClassName)) {
      // also let the generated class support a config constructor parameter:
      String configVar = createAuxVar();
      constructorModel.addParam(new ParamModel(configVar, "Object", "null"));
      superConfigVar = createAuxVar();
      renderConfigAuxVar(superConfigVar);
      createFields(objectNode);
      hasBindings = processAttributesAndChildNodes(objectNode, superConfigVar, "this", true);
      String keyVar = createAuxVar();
      code.append(MessageFormat.format("\n    if ({1}) for (var {0}:String in {1}) {2}[{0}] = {1}[{0}];"
                                     + "\n    super({2});",
              keyVar, configVar, superConfigVar));
    } else {
      createFields(objectNode);
    }
    if (superConfigVar == null || hasBindings) {
      processAttributesAndChildNodes(objectNode, superConfigVar, "this", false);
    }
    code.append("\n    for each (var $binding in $bindings) $binding.execute();");

    constructorModel.setBody(code.toString());
  }

  private void renderConfigAuxVar(String configAuxVar) {
    code.append("\n    var ").append(configAuxVar).append(":Object = {};");
  }

  private boolean constructorSupportsConfigOptionsParameter(String classQName) throws IOException {
    CompilationUnitModel compilationUnitModel = getCompilationUnitModel(classQName);
    if (compilationUnitModel != null) {
      ClassModel classModel = compilationUnitModel.getClassModel();
      if (classModel != null) {
        MethodModel constructorModel = classModel.getConstructor();
        if (constructorModel != null) {
          Iterator<ParamModel> constructorParams = constructorModel.getParams().iterator();
          if (constructorParams.hasNext() && "Object".equals(constructorParams.next().getType())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private void createFields(Element objectNode) throws IOException {
    ClassModel classModel = compilationUnitModel.getClassModel();
    for (Element element : MxmlUtils.getChildElements(objectNode)) {
      if (MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) {
        String elementName = element.getLocalName();
        if (MXML_DECLARATIONS.equals(elementName)) {
          for (Element declaration : MxmlUtils.getChildElements(element)) {
            createValueCodeFromElement(declaration);
          }
        } else if (MXML_SCRIPT.equals(elementName)) {
          classModel.addBodyCode(getTextContent(element));
        } else if (MXML_METADATA.equals(elementName)) {
          classModel.addAnnotationCode(getTextContent(element));
        } else {
          throw Jooc.error("Unknown MXML element: " + elementName);
        }
      }
    }
  }

  private boolean processAttributes(Element objectNode, CompilationUnitModel type, String configVariable, String targetVariable, boolean generatingConfig) throws IOException {
    String variable = generatingConfig ? configVariable : targetVariable;
    ClassModel classModel = type == null ? null : type.getClassModel();
    NamedNodeMap attributes = objectNode.getAttributes();
    boolean hasBindings = false;
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
            AnnotationModel eventModel = findEvent(classModel, propertyName);
            if (eventModel != null) {
              createEventHandlerCode(variable, value, eventModel);
              continue;
            }
          }
        }
        if (propertyModel == null) {
          propertyModel = createDynamicPropertyModel(type, propertyName, MXML_UNTYPED_NAMESPACE.equals(attribute.getNamespaceURI()));
        }
        if (MxmlUtils.isBindingExpression(value)) {
          hasBindings = true;
          if (generatingConfig) {
            createPropertyAssignmentCode(configVariable, propertyModel,
                    CompilerUtils.createCodeExpression(getOrCreateExpressionMethod(targetVariable, propertyModel, value) + "()"));
          } else {
            if (hasSetter(propertyModel)) {
              createBindingMethodCode(targetVariable, propertyModel, value);
            } else {
              createPropertyAssignmentCode(targetVariable, propertyModel, value);
            }
          }
        } else {
          if (generatingConfig || configVariable == null) {
            createPropertyAssignmentCode(variable, propertyModel, value);
          }
        }
      }
    }
    return hasBindings;
  }

  private static boolean hasSetter(MemberModel memberModel) {
    if (memberModel instanceof PropertyModel) {
      MethodModel setter = ((PropertyModel) memberModel).getSetter();
      return setter != null && !setter.getAnnotations(Jooc.ACCESSOR_ANNOTATION_NAME).isEmpty();
    }
    return false;
  }

  private boolean processAttributesAndChildNodes(Element objectNode, String configVariable, String targetVariable, boolean generatingConfig) throws IOException {
    CompilationUnitModel type = getCompilationUnitModel(objectNode);
    boolean hasBindings = processAttributes(objectNode, type, configVariable, targetVariable, generatingConfig);
    processChildNodes(objectNode, type, generatingConfig ? configVariable : targetVariable);
    return hasBindings;
  }

  private void processChildNodes(Element objectNode, CompilationUnitModel type, String variable) throws IOException {
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
            }
          }
        }
        if (propertyModel == null && defaultPropertyModel != null && createClassNameFromNode(element) != null) {
          // collect item to add it to the default property later:
          defaultPropertyValues.add(element);
        } else {
          if (propertyModel == null) {
            propertyModel = createDynamicPropertyModel(type, propertyName, false);
          }
          List<Element> childElements = MxmlUtils.getChildElements(element);
          if (childElements.isEmpty()) {
            createPropertyAssignmentCode(variable, propertyModel, getTextContent(element));
          } else {
            createChildElementsPropertyAssignmentCode(childElements, variable, propertyModel);
          }
        }
      }
    }
    if (!defaultPropertyValues.isEmpty()) {
      createChildElementsPropertyAssignmentCode(defaultPropertyValues, variable, defaultPropertyModel);
    }
  }

  private void createChildElementsPropertyAssignmentCode(List<Element> childElements, String variable,
                                                         MemberModel propertyModel) throws IOException {
    boolean forceArray = "Array".equals(propertyModel.getType());
    String value = createArrayCodeFromChildElements(childElements, forceArray);
    createPropertyAssignmentCode(variable, propertyModel, CompilerUtils.createCodeExpression(value));
  }

  private String createArrayCodeFromChildElements(List<Element> childElements, boolean forceArray) throws IOException {
    List<String> arrayItems = new ArrayList<String>();
    for (Element arrayItemNode : childElements) {
      String itemValue = createValueCodeFromElement(arrayItemNode);
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

  private String createValueCodeFromElement(Element objectElement) throws IOException {
    String className = createClassNameFromNode(objectElement);
    if (className == null) {
      throw Jooc.error("Could not resolve class from node " + objectElement.getNamespaceURI() + ":" + objectElement.getLocalName());
    }
    compilationUnitModel.addImport(className);
    String targetVariable = null;   // name of the variable holding the object to build
    String id = objectElement.getAttribute(MXML_ID_ATTRIBUTE);
    if (id.length() > 0) {
      PropertyModel fieldModel = new PropertyModel(id, className);
      fieldModel.addGetter();
      fieldModel.addSetter();
      compilationUnitModel.getClassModel().addMember(fieldModel);
      targetVariable = id;
    }

    String configVariable = null; // name of the variable holding the config object to use in the constructor
    boolean hasBindings = false;
    if (constructorSupportsConfigOptionsParameter(className)) {
      // if class supports a config options parameter, create a config options object and assign properties to it:
      configVariable = createAuxVar();
      renderConfigAuxVar(configVariable);
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
    } else if ("Object".equals(className)) {
      value = "{}";
    } else if ("Array".equals(className)) {
      value = createArrayCodeFromChildElements(MxmlUtils.getChildElements(objectElement), true);
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
      code.append("\n    ").append(targetVariable);
    } else if (configVariable == null || hasBindings) {
      if (targetVariable == null) {
        // no config object was built: create variable for object to build now:
        targetVariable = createAuxVar();
      }
      code.append("\n    ").append("var ").append(targetVariable).append(":").append(className);
    } else {
      return value; // no aux var neccessary
    }
    code.append(" = ").append(value).append(";");

    if (configVariable == null || hasBindings) {
      // no config object was built or bindings have to be added:
      // process attribute and children and assign properties directly on the target object
      processAttributesAndChildNodes(objectElement, configVariable, targetVariable, false);
    }
    return targetVariable;
  }

  private String createAuxVar() {
    return "$$" + (++auxVarIndex);
  }

  private void createEventHandlerCode(String variable, String value, AnnotationModel event) {
    AnnotationPropertyModel eventType = event.getPropertiesByName().get("type");
    String eventTypeStr = eventType == null ? "Object" : eventType.getStringValue();
    compilationUnitModel.addImport(eventTypeStr);
    String eventName = event.getPropertiesByName().get("name").getStringValue();
    if (eventName.startsWith("on")) {
      eventName = eventName.substring(2);
    }
    String eventHandlerName = "$on_" + variable + "_" + eventName;
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

  private void createBindingMethodCode(String variable, MemberModel propertyModel, String value) {
    String expressionMethodName = getOrCreateExpressionMethod(variable, propertyModel, value);
    compilationUnitModel.addImport("joo.binding.Binding");
    code    .append("\n    $bindings.push(new joo.binding.Binding(").append(expressionMethodName).append(", function($value){")
            .append("\n      ").append(getPropertyAssignmentCode(variable, propertyModel, "$value"))
            .append("\n    }));");
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

  private void createPropertyAssignmentCode(String variable, MemberModel propertyModel, String value) {
    String attributeValueAsString = MxmlUtils.valueToString(getPropertyValue(propertyModel, value));
    code.append("\n    ").append(getPropertyAssignmentCode(variable, propertyModel, attributeValueAsString));
  }

  private static String getPropertyAssignmentCode(String variable, MemberModel propertyModel, String attributeValueAsString) {
    String assignment = MessageFormat.format("{0} = {1};", propertyModel.getName(), attributeValueAsString);
    return "this".equals(variable)
            ? assignment
            : MessageFormat.format("{0}.{1}", variable, assignment);
  }

  // ======================================== auxiliary methods ========================================

  private CompilationUnitModel getCompilationUnitModel(String fullClassName) throws IOException {
    if (fullClassName == null) {
      return null;
    }
    CompilationUnit compilationUnit = jangarooParser.getCompilationsUnit(fullClassName);
    if (compilationUnit == null) {
      throw Jooc.error("Undefined type: " + fullClassName);
    }
    return new ApiModelGenerator(false).generateModel(compilationUnit); // TODO: cache!
  }

  private CompilationUnitModel getCompilationUnitModel(Element element) throws IOException {
    return getCompilationUnitModel(createClassNameFromNode(element));
  }

  private MemberModel findPropertyModel(ClassModel classModel, String propertyName) throws IOException {
    for (ClassModel current = classModel; current != null; current = getSuperClassModel(current)) {
      MemberModel propertyModel = current.getMember(propertyName);
      if (propertyModel != null && (propertyModel.isField() || propertyModel.isProperty())) {
        return propertyModel;
      }
    }
    return null;
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

  private MemberModel createDynamicPropertyModel(CompilationUnitModel compilationUnitModel, String name, boolean allowAnyProperty) {
    if (!allowAnyProperty && compilationUnitModel != null && compilationUnitModel.getClassModel() != null && !compilationUnitModel.getClassModel().isDynamic()) {
      // dynamic property of a non-dynamic class: error!
      throw Jooc.error("MXML: property " + name + " not found in class " + compilationUnitModel.getQName() + ".");
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
    Matcher resourceBundleMatcher = AT_RESOURCE_PATTERN.matcher(value);
    if (resourceBundleMatcher.matches()) {
      String bundle = resourceBundleMatcher.group(1);
      String key = resourceBundleMatcher.group(2);
      value = String.format(RESOURCE_ACCESS_CODE, RESOURCE_MANAGER_QNAME, bundle, key);
      compilationUnitModel.addImport(RESOURCE_MANAGER_QNAME);
      MxmlUtils.addResourceBundleAnnotation(compilationUnitModel.getClassModel(), bundle);
    }
    return MxmlUtils.getAttributeValue(value, propertyModel == null ? null : propertyModel.getType());
  }

  private String createClassNameFromNode(Node objectNode) {
    String name = objectNode.getLocalName();
    String uri = objectNode.getNamespaceURI();
    if (uri != null) {
      String packageName = MxmlUtils.parsePackageFromNamespace(uri);
      if (packageName != null) {
        String qName = CompilerUtils.qName(packageName, name);
        CompilationUnit compilationsUnit = jangarooParser.getCompilationsUnit(qName);
        if (compilationsUnit != null && compilationsUnit.getPrimaryDeclaration() instanceof ClassDeclaration) {
          return qName;
        }
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
      doc = factory.newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException("a default dom builder should be provided", e);
    }
    PreserveLineNumberHandler handler = new PreserveLineNumberHandler(doc);
    parser.parse(inputStream, handler);
    return doc;
  }
}
