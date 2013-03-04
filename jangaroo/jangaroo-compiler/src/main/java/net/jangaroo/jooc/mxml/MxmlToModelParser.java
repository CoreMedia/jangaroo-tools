package net.jangaroo.jooc.mxml;

import net.jangaroo.jooc.JangarooParser;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class MxmlToModelParser {

  public static final String MXML_DECLARATIONS = "Declarations";
  public static final String MXML_SCRIPT = "Script";
  public static final String MXML_METADATA = "Metadata";
  public static final String MXML_ID_ATTRIBUTE = "id";
  public static final String RESOURCE_MANAGER_QNAME = "mx.resources.ResourceManager";
  public static final Pattern AT_RESOURCE_PATTERN = Pattern.compile("^\\s*@Resource\\s*\\(\\s*bundle\\s*=\\s*['\"]([a-zA-Z0-9_$]+)['\"]\\s*,\\s*key\\s*=\\s*['\"]([a-zA-Z0-9_$]+)['\"]\\s*\\)\\s*$");
  public static final String RESOURCE_ACCESS_CODE = "{%s.getInstance().getString(\"%s\",\"%s\")}";
  private final JangarooParser jangarooParser;
  private int methodIndex;

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
    CompilationUnitModel model = new CompilationUnitModel(CompilerUtils.packageName(qName),
            new ClassModel(CompilerUtils.className(qName)));

    BufferedInputStream inputStream = null;
    try {
      inputStream = new BufferedInputStream(in.getInputStream());
      parse(inputStream, model);
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }

    return model;
  }

  /**
   * Parse the input stream content into a model.
   * Close the input stream after reading.
   *
   * @param inputStream the input stream
   * @param model the model
   * @throws java.io.IOException  if the input stream could not be read
   * @throws org.xml.sax.SAXException if the XML was not well-formed
   */
  private void parse(InputStream inputStream, CompilationUnitModel model) throws IOException, SAXException {
    Document document = buildDom(inputStream);
    Element objectNode = document.getDocumentElement();
    String superClassName = createClassNameFromNode(objectNode);

    if (superClassName.equals(model.getQName())) {
      jangarooParser.getLog().error("Cyclic inheritance error: super class and this component are the same!. There is something wrong!"); // TODO: MXML file position!
    }
    ClassModel classModel = model.getClassModel();
    classModel.setSuperclass(superClassName);
    model.addImport(superClassName);

    for (Element element : MxmlUtils.getChildElements(objectNode)) {
      if (MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) {
        String elementName = element.getLocalName();
        if (MXML_DECLARATIONS.equals(elementName)) {
          for (Element declaration : MxmlUtils.getChildElements(element)) {
            String fieldName = declaration.getAttribute(MXML_ID_ATTRIBUTE);
            String type = createClassNameFromNode(declaration);
            PropertyModel fieldModel = new PropertyModel(fieldName, type);
            fieldModel.addGetter();
            fieldModel.addSetter();
            classModel.addMember(fieldModel);
          }
        } else if (MXML_SCRIPT.equals(elementName)) {
          classModel.addBodyCode(getTextContent(element));
        } else if (MXML_METADATA.equals(elementName)) {
          classModel.addAnnotationCode(getTextContent(element));
        } else {
          jangarooParser.getLog().error("Unknown MXML element: " + elementName);
        }
      }
    }

    MethodModel constructorModel = classModel.createConstructor();
    StringBuilder code = new StringBuilder();
    code.append("super();");

    for (Element element : MxmlUtils.getChildElements(objectNode)) {
      if (MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) {
        String elementName = element.getLocalName();
        if (MXML_DECLARATIONS.equals(elementName)) {
          for (Element declaration : MxmlUtils.getChildElements(element)) {
            String fieldName = declaration.getAttribute("id");
            String type = createClassNameFromNode(declaration);
            createPropertyAssigmentCode(model, code, declaration, "this", fieldName, getCompilationUnitModel(type));
          }
        }
      }
    }

    createPropertyAssignmentsCode(model, objectNode, "this", code);

    constructorModel.setBody(code.toString());
  }

  private CompilationUnitModel getCompilationUnitModel(String fullClassName) throws IOException {
    CompilationUnit compilationUnit = jangarooParser.getCompilationsUnit(fullClassName);
    if (compilationUnit == null) {
      jangarooParser.getLog().warning("*** not found: " + fullClassName);
      return null;
    } else {
      return new ApiModelGenerator(false).generateModel(compilationUnit); // TODO: cache!
    }
  }

  private String createClassNameFromNode(Node objectNode) {
    String name = objectNode.getLocalName();
    String uri = objectNode.getNamespaceURI();
    String packageName = uri == null ? null : MxmlUtils.parsePackageFromNamespace(uri);
    if (packageName == null) {
      jangarooParser.getLog().warning("namespace '" + uri + "' of element '" + name + "' in EXML file does not denote a config package, using unqualified name."); // TODO: MXML file position!
      return name;
    }
    return CompilerUtils.qName(packageName, name);
  }

  private void createPropertyAssignmentsCode(CompilationUnitModel compilationUnitModel, Element objectNode,
                                             String variable, StringBuilder code) throws IOException {
    CompilationUnitModel type = getCompilationUnitModel(createClassNameFromNode(objectNode));
    NamedNodeMap attributes = objectNode.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Attr attribute = (Attr) attributes.item(i);
      String propertyName = attribute.getLocalName();
      if (attribute.getNamespaceURI() != null) {
        // allow custom namespace URIs like "config" to access sub-properties:
        propertyName = attribute.getNamespaceURI() + "." + propertyName;
      }
      if (!MXML_ID_ATTRIBUTE.equals(propertyName)) {
        createPropertyAssigmentCode(compilationUnitModel, code, variable, type, propertyName, attribute.getValue());
      }
    }

    List<Element> childNodes = MxmlUtils.getChildElements(objectNode);
    for (Element element : childNodes) {
      if (MxmlUtils.isMxmlNamespace(element.getNamespaceURI())) {
        // ignore; has been handled before.
        continue;
      }
      createPropertyAssigmentCode(compilationUnitModel, code, element, variable, element.getLocalName(), type);
    }
  }

  private void createPropertyAssigmentCode(CompilationUnitModel compilationUnitModel, StringBuilder code,
                                           Element propertyElement, String variable, String propertyName, CompilationUnitModel objectType) throws IOException {
    MemberModel propertyModel = objectType == null ? null : objectType.getClassModel().getMember(propertyName);
    boolean isPropertyTypeArray = propertyModel != null && "Array".equals(propertyModel.getType());
    // it seems to be an array or an object
    List<Element> childElements = MxmlUtils.getChildElements(propertyElement);
    if (childElements.isEmpty()) {
      createPropertyAssigmentCode(compilationUnitModel, code, variable, objectType, propertyName, getTextContent(propertyElement));
      return;
    }

    List<String> methodNames = createAuxVarsForChildObjects(compilationUnitModel, childElements, code);
    code.append("\n    ").append(variable).append(".").append(propertyName).append(" = ");
    if (methodNames.size() > 1 || isPropertyTypeArray) {
      // TODO: Check for type violation
      // We must write an array.
      code.append("[");
      for (Iterator<String> iterator = methodNames.iterator(); iterator.hasNext(); ) {
        String methodName = iterator.next();
        code.append(methodName);
        if (iterator.hasNext()) {
          code.append(", ");
        }
      }
      code.append("]");
    } else if (methodNames.size() == 1) {
      // The property is either unspecified, untyped, or object-typed
      // and it contains at least one child element. Use the first element as the
      // property value.
      code.append(methodNames.get(0));
    } else {
      jangarooParser.getLog().error("Non-array property must not have multiple MXML child elements."); // TODO: MXML file position!
      code.append("undefined");
    }
    code.append(";");
  }

  private static String getTextContent(Element element) {
    return element.getChildNodes().getLength() == 1 && element.getFirstChild().getNodeType() == Node.TEXT_NODE ? ((Text) element.getFirstChild()).getData() : "";
  }

  private void createPropertyAssigmentCode(CompilationUnitModel compilationUnitModel, StringBuilder code, String variable, CompilationUnitModel type, String propertyName, String value) throws IOException {
    MemberModel propertyModel = type == null ? null : type.getClassModel().getMember(propertyName);
    if (propertyModel == null && type != null) {
      // is it an event?
      AnnotationModel event = getEvent(type, propertyName);
      if (event != null) {
        AnnotationPropertyModel eventType = event.getPropertiesByName().get("type");
        String eventTypeStr = eventType == null ? "Object" : eventType.getStringValue();
        compilationUnitModel.addImport(eventTypeStr);
        if (propertyName.startsWith("on")) {
          propertyName = propertyName.substring(2);
        }
        String eventHandlerName = "___on_" + propertyName + (++methodIndex);
        MethodModel eventHandler = new MethodModel(eventHandlerName, "void",
                new ParamModel("event", eventTypeStr));
        eventHandler.setBody(value);
        compilationUnitModel.getClassModel().addMember(eventHandler);
        compilationUnitModel.addImport("joo.addEventListener");
        code.append("\n    ").append("joo.addEventListener(").append(variable)
                .append(", '").append(propertyName).append("', ").append(eventTypeStr).append(", ")
                .append(eventHandlerName).append(");");
        return;
      }
    }
    Matcher resourceBundleMatcher = AT_RESOURCE_PATTERN.matcher(value);
    if (resourceBundleMatcher.matches()) {
      String bundle = resourceBundleMatcher.group(1);
      String key = resourceBundleMatcher.group(2);
      value = String.format(RESOURCE_ACCESS_CODE, RESOURCE_MANAGER_QNAME, bundle, key);
      compilationUnitModel.addImport(RESOURCE_MANAGER_QNAME);
      MxmlUtils.addResourceBundleAnnotation(compilationUnitModel.getClassModel(), bundle);
    }
    Object attributeValue = MxmlUtils.getAttributeValue(value,
            propertyModel == null ? null : propertyModel.getType());
    code.append("\n    ").append(variable).append(".").append(propertyName).append(" = ")
            .append(MxmlUtils.valueToString(attributeValue)).append(";");
  }

  private AnnotationModel getEvent(CompilationUnitModel type, String propertyName) throws IOException {
    ClassModel classModel = type.getClassModel();
    AnnotationModel event = classModel.getEvent(propertyName);
    if (event == null && classModel.getSuperclass() != null) {
      CompilationUnitModel superCUM = getCompilationUnitModel(classModel.getSuperclass());
      return getEvent(superCUM, propertyName);
    }
    return event;
  }


  private List<String> createAuxVarsForChildObjects(CompilationUnitModel model, List<Element> elements, StringBuilder code) throws IOException {
    List<String> auxVarNames = new ArrayList<String>();
    for (Element arrayItemNode : elements) {
      String arrayItemClassName = createClassNameFromNode(arrayItemNode);
      model.addImport(arrayItemClassName);
      String auxVarName = "$$" + (++methodIndex);
      code.append("\n    var ").append(auxVarName).append(":").append(arrayItemClassName);
      String id = arrayItemNode.getAttribute(MXML_ID_ATTRIBUTE);
      if (id.length() > 0) {
        model.getClassModel().addMember(new FieldModel(id, arrayItemClassName));
        code.append(" = this.").append(id);
      }
      code.append(" = ")
              .append("new ").append(arrayItemClassName).append("();");
      createPropertyAssignmentsCode(model, arrayItemNode, auxVarName, code);
      auxVarNames.add(auxVarName);
    }
    return auxVarNames;
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
