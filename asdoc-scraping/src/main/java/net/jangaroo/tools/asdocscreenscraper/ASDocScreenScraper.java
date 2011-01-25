package net.jangaroo.tools.asdocscreenscraper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A tool to screen-scrape ASDoc XHTML and thus reverse-engineer AS3 API source code.
 */
public class ASDocScreenScraper {
  private static final Tidy TIDY;
  private static XPathFactory xPathFactory = XPathFactory.newInstance();
  private static final Pattern RELATIVE_TYPE_URL_PATTERN = Pattern.compile("(?:\\.\\./)*(.*)\\.html(#.*)?");
  private Set<String> imports = new LinkedHashSet<String>();
  private static final String ADOBE_FLASH_PLATFORM_REFERENCE_BASE_URL = "http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/";

  private static final String IGNORE_UNTIL = "ignore-until:";
  private static final String IGNORE_UNTIL_NEXT_AT_MODE = IGNORE_UNTIL + "next-atMode";
  private static final String IGNORE_UNTIL_BR = IGNORE_UNTIL + "br";

  static {
    TIDY = new Tidy();
    TIDY.setCharEncoding(Configuration.UTF8);
    TIDY.setAltText("");
    TIDY.setDropEmptyParas(true);
    TIDY.setDropFontTags(true);
    TIDY.setFixComments(true);
    TIDY.setHideEndTags(false);
    TIDY.setIndentAttributes(true);
    TIDY.setMakeClean(true);
    TIDY.setQuiet(true);
    TIDY.setQuoteAmpersand(true);
    TIDY.setShowWarnings(false);
    TIDY.setXHTML(true);
    TIDY.setXmlOut(true);
    TIDY.setXmlSpace(false);
    TIDY.setXmlPi(false);
  }

  public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, TransformerException, URISyntaxException {
    if (args.length > 0) {
      new ASDocScreenScraper(ADOBE_FLASH_PLATFORM_REFERENCE_BASE_URL + args[0].replaceAll("\\.", "/") + ".html").scrape();
      return;
    }
    Document classSummaryDocument = loadAndParse(new URI(ADOBE_FLASH_PLATFORM_REFERENCE_BASE_URL + "class-summary.html"));
    XPath xpath = xPathFactory.newXPath();
    XPathExpression classNodesExpression = xpath.compile("//*[@class='summaryTable']//*[name()='tr'][not(@product)][contains(@runtime,'Flash::9')]//*[name()='a']/@href");
    NodeList classNodes = (NodeList)classNodesExpression.evaluate(classSummaryDocument, XPathConstants.NODESET);
    System.out.println("Hits: " + classNodes.getLength());
    for (int i = 0; i < classNodes.getLength(); i++) {
      String relativeClassUrl = classNodes.item(i).getNodeValue();
      if (!relativeClassUrl.endsWith("package-detail.html")) {
        System.out.println(relativeClassUrl);
        new ASDocScreenScraper(ADOBE_FLASH_PLATFORM_REFERENCE_BASE_URL + relativeClassUrl).scrape();
      }
    }
  }

  private URI url;
  private Document document;
  private String packageName;
  private String className;
  private boolean isInterface;

  public ASDocScreenScraper(String url) throws URISyntaxException {
    this.url = new URI(url);
  }

  public void scrape() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, TransformerException {
    Document doc = loadAndParse(url);
    this.document = doc;

    XPath xpath = xPathFactory.newXPath();
    XPathExpression packageNameExpression
      = xpath.compile("//*[@id='packageName']/text()");

    Node packageNameNode = (Node)packageNameExpression.evaluate(doc, XPathConstants.NODE);
    packageName = htmlTrim(packageNameNode.getNodeValue());
    if ("Top Level".equals(packageName)) {
      packageName = "";
    }

    String packageDirs = "../joo/" + packageName.replaceAll("\\.", "/");
    File packageDirsFile = new File(packageDirs);
    if (!packageDirsFile.mkdirs()) {
      System.out.println("[INFO] Package directory " + packageDirsFile.getAbsolutePath() + " already exists.");
    }
    className = htmlTrim(packageNameNode.getParentNode().getNextSibling().getNextSibling().getNodeValue());
    File classFile = new File(packageDirsFile, className + ".as");
    System.out.println("Writing file " + classFile.getCanonicalPath());
    PrintWriter writer = new PrintWriter(classFile, "UTF-8");

    writer.println("package " + packageName + " {");

    XPathExpression classDeclarationExpression
      = xpath.compile("/*[name()='html']/*[name()='body']/*[name()='div']/*[name()='div'][@id='content']/*[name()='div'][1]/*[name()='div'][1]/*[name()='table'][1]/*[name()='tr'][2]/*[name()='td'][2]/text()");
    Node classDeclarationNode = (Node)classDeclarationExpression.evaluate(doc, XPathConstants.NODE);
    String classDeclaration = classDeclarationNode.getNodeValue();
    isInterface = classDeclaration.indexOf("interface") != -1; // TODO: more exact match (Pattern) needed?
    while (classDeclarationNode.getNextSibling() != null) {
      classDeclarationNode = classDeclarationNode.getNextSibling();
      classDeclaration += "a".equals(classDeclarationNode.getNodeName())
        ? toType(classDeclarationNode)
        : classDeclarationNode.getNodeValue();
    }

    XPathExpression extendsExpression
      = xpath.compile("//*[@class='classHeaderTableLabel'][text()='Inheritance']/following-sibling::*/*[name()='a']");
    Node extendsClassNode = (Node)extendsExpression.evaluate(doc, XPathConstants.NODE);
    String extendsClause = "";
    if (extendsClassNode != null) {
      String extendsClass = toType(extendsClassNode);
      if (!"Object".equals(extendsClass)) {
        extendsClause = " extends " + extendsClass;
      }
    }

    XPathExpression implementsExpression
      = xpath.compile("//*[@class='classHeaderTableLabel'][text()='Implements']/following-sibling::*/*[name()='a']");
    NodeList implementsNodes = (NodeList)implementsExpression.evaluate(doc, XPathConstants.NODESET);
    String implementsClause = getImplementsClause(implementsNodes);

    XPathExpression propertyDeclarations = xpath.compile("//*[@class='content']//*[name()='span'][not(@product)][contains(@runtime,'Flash::9')] | //*[@class='content']/*[@class='MainContent']/*[name()='span'][not(@product)][not(@runtime)]");
    //XPathExpression propertyDeclarations = xpath.compile("//*[@class='MainContent'][2]/*[name()='div'][@class='detailBody']");
    NodeList propertyDeclarationNodes = (NodeList)propertyDeclarations.evaluate(doc, XPathConstants.NODESET);

    XPathExpression eventNameExpression = xPathFactory.newXPath().compile(".//*[name()='h2']/text()");

    StringBuilder eventCode = new StringBuilder();
    StringBuilder memberCode = new StringBuilder();

    for (int i = 0; i < propertyDeclarationNodes.getLength(); i++) {
      Node propertyDeclarationNode = propertyDeclarationNodes.item(i);
      Node propertyDeclarationHeader = propertyDeclarationNode.getNextSibling();
      propertyDeclarationNode = propertyDeclarationHeader.getNextSibling();
      if (!(propertyDeclarationNode != null && propertyDeclarationNode instanceof Element && "detailBody".equals(((Element)propertyDeclarationNode).getAttribute("class")))) {
        System.out.println("[WARN] Property declaration not followed by <div class='detailBody'>.");
      } else {
        NodeList eventNodes = (NodeList)xpath.evaluate("*[name()='a']/*[name()='code']/text()", propertyDeclarationNode, XPathConstants.NODESET);
        Node implementationNode = (Node)xpath.evaluate("*[name()='span'][@class='label'][text()='Implementation']", propertyDeclarationNode, XPathConstants.NODE);

        NodeList docNodes = propertyDeclarationNode.getChildNodes();
        if (eventNodes.getLength() == 2) {
          eventCode.append(getASDoc(docNodes, eventNodes.item(1)));
          String eventName = ((String)eventNameExpression.evaluate(propertyDeclarationHeader, XPathConstants.STRING)).trim();
          eventCode.append("[Event(name=\"").append(eventName)
            .append("\", type=\"").append(eventNodes.item(0).getNodeValue()).append("\")]\n");
        } else {
          memberCode.append(getASDoc(docNodes, null));
          memberCode.append(unparseCode(implementationNode == null ? propertyDeclarationNode.getFirstChild() : implementationNode.getNextSibling()));
        }
      }
    }

    writer.println(getImports());

    writer.println(eventCode);

    writer.print(getClassDoc());

    writer.println(classDeclaration + extendsClause + implementsClause + " {");

    writer.print(memberCode);

    writer.println("}");
    writer.println("}");
    writer.flush();
    writer.close();
  }

  private String getImplementsClause(NodeList implementsNodes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < implementsNodes.getLength(); i++) {
      sb.append(i == 0 ? " implements " : ", ");
      sb.append(toType(implementsNodes.item(i)));
    }
    return sb.toString();
  }

  private static Document loadAndParse(URI url) throws TransformerException, ParserConfigurationException, SAXException, IOException {
    Document document = TIDY.parseDOM(new BufferedInputStream(url.toURL().openStream()), null);
    DOMSource domSource = new DOMSource(document.getDocumentElement());
    Transformer serializer = TransformerFactory.newInstance().newTransformer();
    serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD XHTML 1.0 Transitional//EN");
    //serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
    String localXHtmlDoctype = new File(".").getAbsoluteFile().toURI().toString() + "xhtml1/DTD/xhtml1-transitional.dtd";
    serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, localXHtmlDoctype);
    StringWriter result = new StringWriter();
    serializer.transform(domSource, new StreamResult(result));

    String xhtmlText = result.toString();
    // filter out duplicate IDs:
    xhtmlText = xhtmlText.replaceAll(" id=\"(pageFilter|propertyDetail)\"", "");
    /*
    xhtmlText = xhtmlText.replaceAll(" runtime=\"[^\"]*\"", "");
    xhtmlText = xhtmlText.replaceAll(" target=\"\"", " target=\"_self\"");
    xhtmlText = xhtmlText.replaceAll(" href=\"\"", " href=\"#\"");
    xhtmlText = xhtmlText.replaceAll(" nowrap=\"true\"", " nowrap=\"nowrap\"");
    xhtmlText = xhtmlText.replaceAll("class=\"searchFormION\"", "class=\"searchFormION\" action=\"#\"");
    xhtmlText = xhtmlText.replaceAll(" xmlns:xd=\"http://www.pnp-software.com/XSLTdoc\"", "");
    String oldXHtmlText;
    do {
      oldXHtmlText = xhtmlText;
      xhtmlText = xhtmlText.replaceAll(" (id|name)=\"([^(),\"]*)(\\(|\\)|,)", " $1=\"$2_");
    } while (!oldXHtmlText.equals(xhtmlText));
*/
    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    domFactory.setValidating(false);
    domFactory.setNamespaceAware(true); // never forget this!
    DocumentBuilder builder = domFactory.newDocumentBuilder();
    return builder.parse(new InputSource(new StringReader(xhtmlText)));
  }

  private String getImports() {
    StringBuilder sb = new StringBuilder();
    for (String importType : imports) {
      sb.append("import ").append(importType).append(";\n");
    }
    return sb.toString();
  }

  private String unparseCode(Node codeNode) {
    StringBuilder sb = new StringBuilder();
    while (true) {
      while (codeNode != null && "br".equals(codeNode.getNodeName())) { // skip <br>s
        codeNode = codeNode.getNextSibling();
      }
      if (codeNode == null || !"code".equals(codeNode.getNodeName())) {
        // no following <code> node
        break;
      }
      if (sb.length() > 0) {
        // use the same ASDoc for setter as for getter:
        sb.append("/**\n * @private\n */\n");
      }
      sb.append("  ");
      NodeList childNodes = codeNode.getChildNodes();
      for (int j = 0; j < childNodes.getLength(); j++) {
        Node childNode = childNodes.item(j);
        String nodeValue = toType(childNode);
        if (nodeValue == null) {
          nodeValue = "#text".equals(childNode.getNodeName())
            ? childNode.getNodeValue()
            : childNode.getFirstChild().getNodeValue();
        }
        if (isInterface) {
          nodeValue = nodeValue.replaceFirst("\\bpublic ", "");
        }
        sb.append(nodeValue);
      }
      if (!isInterface && sb.indexOf("function ") != -1) {
        sb.append(" {\n      throw new Error('not implemented'); // TODO: implement!\n    }\n");
      } else {
        sb.append(";\n");
      }
      codeNode = codeNode.getNextSibling();
    }
    return sb.toString();
  }

  private String toType(Node aNode) {
    String href = getAttributeValueIfMatches(aNode, "a", "href");
    if (href != null) {
      if (href.indexOf("specialTypes.html#") != -1) {
        return aNode.getFirstChild().getNodeValue();
      }
      // types are represented as URLs relative to this class' package URL, but have to be package.Type.
      // ASDoc's habit of going back to the top level package using ".."s makes things easier:
      Matcher matcher = RELATIVE_TYPE_URL_PATTERN.matcher(href);
      if (matcher.matches()) {
        String typeName = matcher.group(1).replaceAll("/", ".");
        if (typeName.endsWith(".package-detail")) {
          return typeName.substring(0, typeName.length() - ".package-detail".length());
        }
        addImport(typeName);
        String memberName = matcher.group(2);
        if (memberName != null) {
          return className.equals(typeName) ? memberName : typeName + memberName;
        }
        return typeName;
      }
    }
    return null;
  }

  private void addImport(String fullyQualifiedTypeName) {
    int dotPos = fullyQualifiedTypeName.lastIndexOf('.');
    if (dotPos != -1) {
      String packageName = fullyQualifiedTypeName.substring(0, dotPos);
      if (!packageName.equals(this.packageName)) {
        imports.add(fullyQualifiedTypeName);
      }
    }
  }

  private String getClassDoc() throws XPathExpressionException {
    XPath xpath = xPathFactory.newXPath();
    XPathExpression classDoc
      = xpath.compile("//*[@id='content']//*[name()='div'][@class='MainContent']");
    Node classDocNode = (Node)classDoc.evaluate(document, XPathConstants.NODE);
    if (classDocNode != null) {
      NodeList classDocNodes = classDocNode.getChildNodes();
      return getASDoc(classDocNodes, null);
    }
    return "";
  }

  private String getASDoc(NodeList classDocNodes, Node eventNode) throws XPathExpressionException {
    if (classDocNodes.getLength() > 0) {
      StringBuilder writer = new StringBuilder();
      writer.append("/**\n");
      writer.append(" * ");
      boolean first = true;
      String atMode = null;
      XPathExpression atModeExpression = xPathFactory.newXPath().compile("*[name()='span'][@class='label' or @class='classHeaderTableLabel']");
      XPathExpression runtimeOrLanguageVersionExpression = xPathFactory.newXPath().compile(".//*[name()='td']/*[name()='b'][contains(text(),'Language Version')] | .//*[name()='td']/*[name()='b'][contains(text(), 'Runtime Version')]");
      for (int i = 0; i < classDocNodes.getLength(); i++) {
        Node node = classDocNodes.item(i);
        if (first) {
          // skip initial <code> element:
          if ("code".equals(node.getNodeName())) {
            // also skip following text node:
            if (node.getNextSibling() != null && "#text".equals(node.getNextSibling().getNodeName())) {
              ++i;
            }
            continue;
          } else if (isP(node) && node.getFirstChild() == null) {
            // skip all initial empty <p>s:
            continue;
          }
        }
        if (IGNORE_UNTIL_BR.equals(atMode)) {
          if ("br".equals(node.getNodeName())) {
            atMode = null;
          }
        } else if ("@see".equals(atMode)) {
          appendSeeAlsos(writer, node);
          atMode = null;
        } else if ("@example".equals(atMode)) {
          writer.append("\n * @example ").append(unparse(node, true));
          atMode = null;
        } else if ("@param".equals(atMode) || "@return".equals(atMode) || "@throws".equals(atMode)) {
          if ("table".equals(node.getNodeName())) {
            appendParamsOrReturnOrThrows(atMode, writer, node);
            atMode = null;
          }
        } else {
          if ("classHeaderTable".equals(getAttributeValueIfMatches(node, "table", "class"))) {
            continue;
          }
          Node runtimeOrLanguageVersionNode = (Node)runtimeOrLanguageVersionExpression.evaluate(node, XPathConstants.NODE);
          if (runtimeOrLanguageVersionNode != null) {
            atMode = null;
            // so far, skip runtime and language version information.
            continue;
          }
          Node atModeNode = (Node)atModeExpression.evaluate(node, XPathConstants.NODE);
          String newAtMode = determineAtMode(atModeNode == null ? node : atModeNode);
          if (newAtMode != null) {
            atMode = newAtMode;
          } else if (!(atMode != null && atMode.startsWith(IGNORE_UNTIL))) {
            if (eventNode != null && !first && "#text".equals(node.getNodeName())) {
              // detected copied ASDoc from event type constant, see http://livedocs.adobe.com/flex/3/html/help.html?content=asdoc_4.html "documenting ... events":
              break;
            }
            String nodeValue = unparse(node, first && isP(node));
            if (nodeValue.length() > 0) {
              first = false;
              writer.append(nodeValue);
            }
          }
        }
      }
      if (eventNode != null) {
        writer.append("\n * @eventType ").append(eventNode.getNodeValue());
      }
      writer.append("\n */\n");
      return writer.toString();
    }
    return "";
  }

  private static String determineAtMode(Node atModeNode) {
    String pStyleClass = getAttributeValueIfMatches(atModeNode, "span", "class");
    if ("label".equals(pStyleClass) || "classHeaderTableLabel".equals(pStyleClass)) {
      String label = atModeNode.getFirstChild().getNodeValue().trim();
      return "Parameters".equals(label) ? "@param"
        : "Returns".equals(label) ? "@return"
        : "See also".equals(label) ? "@see"
        : "Throws".equals(label) ? "@throws"
        : "Implementation".equals(label) ? IGNORE_UNTIL_NEXT_AT_MODE
        : "Event Object Type:".equals(label) || label.matches("property (.*)\\.type =") ? IGNORE_UNTIL_BR
        : label.startsWith("Example") ? "@example"
        : null;
    }
    return null;
  }

  private void appendSeeAlsos(StringBuilder writer, Node node) {
    NodeList seeNodes = node.getChildNodes();
    for (int j = 0; j < seeNodes.getLength(); j++) {
      Node seeNode = seeNodes.item(j);
      if ("a".equals(seeNode.getNodeName())) {
        String hrefText = seeNode.getAttributes().getNamedItem("href").getNodeValue();
        if (hrefText.indexOf("specialTypes.html#") != -1 || hrefText.indexOf("statements.html#") != -1) {
          hrefText = url.resolve(hrefText).toString();
        }
        String seeText;
        if (hrefText.startsWith("http://")) {
          seeText = hrefText + " " + seeNode.getFirstChild().getNodeValue();
        } else {
          seeText = toType(seeNode);
        }
        writer.append("\n * @see ").append(seeText);
      }
    }
    writer.append("\n * ");
  }

  private void appendParamsOrReturnOrThrows(String atMode, StringBuilder writer, Node node) throws XPathExpressionException {
    XPathExpression paramNameExpression = xPathFactory.newXPath().compile(".//*[name()='span'][@class='label']");
    XPathExpression errorTypeExpression = xPathFactory.newXPath().compile(".//*[name()='a']");
    NodeList paramRowNodes = node.getChildNodes();
    for (int i = 0; i < paramRowNodes.getLength(); i++) {
      Node paramRowNode = paramRowNodes.item(i);
      String firstTdClass = getAttributeValueIfMatches(paramRowNode.getFirstChild(), "td", "class");
      if ("paramSpacer".equals(firstTdClass)) {
        continue;
      }
      String key = "@param".equals(atMode) ? ((String)paramNameExpression.evaluate(paramRowNode, XPathConstants.STRING)).trim()
        : "@throws".equals(atMode) ? toType((Node)errorTypeExpression.evaluate(paramRowNode, XPathConstants.NODE))
        : "";
      String text = unparse(paramRowNode.getChildNodes().item(1), true); // unparse second td (first is only a spacer)
      int mdashIndex = text.indexOf("— ");
      if (mdashIndex != -1) {
        text = text.substring(mdashIndex + "— ".length());
      }
      writer.append("\n * ").append(atMode).append(" ");
      if (key.length() > 0) {
        writer.append(key).append(' ');
      }
      writer.append(text);
    }
    writer.append("\n * ");
  }

  private static boolean isP(Node node) {
    return "p".equals(node.getNodeName());
  }

  private String unparse(Node node, boolean suppressOuterElement) {
    String tag = node.getNodeName();
    if ("#text".equals(tag)) {
      return node.getNodeValue();
    }
    if ("br".equals(tag) || "hr".equals(tag)) {
      return "";
    }
    String divClass = getAttributeValueIfMatches(node, "div", "class");
    if ("listing".equals(divClass)) {
      tag = "listing";
    }
    String childrenUnparsed;
    if ("listing".equals(tag)) {
      childrenUnparsed = node.getFirstChild().getFirstChild().getNodeValue();
      childrenUnparsed = ("\n" + childrenUnparsed).replaceAll("\n", "\n * ");
    } else {
      StringBuilder sb = new StringBuilder();
      NodeList childNodes = node.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node childNode = childNodes.item(i);
        sb.append(unparse(childNode, false));
      }
      childrenUnparsed = sb.toString();
    }

    if ("span".equals(tag) || "tbody".equals(tag) || suppressOuterElement || (!"img".equals(tag) && childrenUnparsed.length() == 0)) {
      return childrenUnparsed;
    }
    boolean isBlockElement = "p".equals(tag) || "div".equals(tag) || "ul".equals(tag) || "li".equals(tag)
      || "listing".equals(tag) || "pre".equals(tag) || "table".equals(tag) || "tr".equals(tag) || "td".equals(tag);
    StringBuilder sb = new StringBuilder();
    if (isBlockElement) {
      sb.append("\n * ");
    }
    sb.append('<').append(tag);
    String href = getAttributeValueIfMatches(node, "a", "href");
    if (href != null) {
      sb.append(" href=\"").append(url.resolve(href)).append("\"");
    }
    String src = getAttributeValueIfMatches(node, "img", "src");
    if (src != null) {
      sb.append(" src=\"").append(url.resolve(src)).append("\" />");
      return sb.toString();
    }
    sb.append('>');
    sb.append(childrenUnparsed);
    sb.append("</").append(tag).append('>');
    return sb.toString();
  }

  private static String getAttributeValueIfMatches(Node node, String nodeName, String attributeName) {
    if (nodeName.equals(node.getNodeName())) {
      Node attributeNode = node.getAttributes().getNamedItem(attributeName);
      if (attributeNode != null) {
        return attributeNode.getNodeValue();
      }
    }
    return null;
  }

  private static String htmlTrim(String str) {
    return str.replaceAll("\u00A0", " ").trim();
  }

}
