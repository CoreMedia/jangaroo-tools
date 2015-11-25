package net.jangaroo.exml.mojo.pom;

import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION;
import static javax.xml.xpath.XPathConstants.NODE;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.lang.StringUtils.repeat;

public class PomConverter {
  /**
   * Replaces exml-maven-plugin configuration by jangaroo-maven-plugin configuration in a POM within the given
   * directory.
   */
  public static void removeExmlPlugin(File projectBaseDir) throws MojoExecutionException {
    Document document = readPom(projectBaseDir);
    removeExmlPlugin(document);
    writePom(document, projectBaseDir);
  }

  /**
   * Parses a POM from a given directory into a DOM document.
   */
  private static Document readPom(File projectBaseDir) throws MojoExecutionException {
    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      return documentBuilder.parse(new File(projectBaseDir, "pom.xml"));
    } catch (IOException e) {
      throw new MojoExecutionException("error while generating modified POM", e);
    } catch (ParserConfigurationException e) {
      throw new MojoExecutionException("error while generating modified POM", e);
    } catch (SAXException e) {
      throw new MojoExecutionException("error while generating modified POM", e);
    }
  }

  /**
   * Serializes the given DOM document into a POM file within the given directory.
   */
  private static void writePom(Document document, File projectBaseDir) throws MojoExecutionException {
    try {
      File pomFile = new File(projectBaseDir, "pom.xml");

      // keep trailing whitespace because it's not reproduced by the transformer and we want to keep the diff small
      String pom = readFileToString(pomFile);
      String trailingWhitespace = pom.substring(pom.lastIndexOf('>') + 1);

      PrintWriter pomWriter = new PrintWriter(pomFile);
      Transformer transformer = TransformerFactory.newInstance().newTransformer();

      // the transformer does not reproduce the new line after the XML declaration, so we do it on our own
      // see https://bugs.openjdk.java.net/browse/JDK-7150637
      transformer.setOutputProperty(OMIT_XML_DECLARATION, "yes");
      if (document.getXmlEncoding() != null) {
        pomWriter.print("<?xml version=\"");
        pomWriter.print(document.getXmlVersion());
        pomWriter.print("\" encoding=\"");
        pomWriter.print(document.getXmlEncoding());
        pomWriter.println("\"?>");
      }

      transformer.transform(new DOMSource(document), new StreamResult(pomWriter));
      pomWriter.write(trailingWhitespace);
      pomWriter.close();
    } catch (IOException e) {
      throw new MojoExecutionException("error while generating modified POM", e);
    } catch (TransformerException e) {
      throw new MojoExecutionException("error while generating modified POM", e);
    }
  }

  /**
   * Replaces exml-maven-plugin configuration by jangaroo-maven-plugin configuration within
   * {@code /project/build/plugins} and {@code /project/build/pluginManagement/plugins}.
   */
  private static void removeExmlPlugin(Document document) throws MojoExecutionException {
    try {
      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xPath = xPathFactory.newXPath();

      Node pluginsNode = (Node) xPath.evaluate("/project/build/plugins", document, NODE);
      removeExmlPlugin(pluginsNode);
      pluginsNode = (Node) xPath.evaluate("/project/build/pluginManagement/plugins", document, NODE);
      removeExmlPlugin(pluginsNode);
    } catch (XPathException e) {
      throw new MojoExecutionException("error while generating modified POM", e);
    }
  }

  /**
   * Replaces exml-maven-plugin configuration by jangaroo-maven-plugin configuration within the given {@code plugins}
   * element.
   */
  private static void removeExmlPlugin(Node pluginsNode) throws XPathException {
    if (pluginsNode == null) {
      return;
    }

    Document document = pluginsNode.getOwnerDocument();
    XPathFactory xPathFactory = XPathFactory.newInstance();
    XPath xPath = xPathFactory.newXPath();

    Node exmlPluginNode = (Node) xPath.evaluate("plugin[artifactId='exml-maven-plugin']", pluginsNode, NODE);

    if (exmlPluginNode == null) {
      return;
    }

    Node jangarooPluginNode = (Node) xPath.evaluate("plugin[artifactId='jangaroo-maven-plugin']", pluginsNode, NODE);
    Node exmlVersionNode = (Node) xPath.evaluate("version", exmlPluginNode, NODE);
    Node exmlExtensionNode = (Node) xPath.evaluate("extensions", exmlPluginNode, NODE);
    Node configClassPackageNode = (Node) xPath.evaluate("configuration/configClassPackage", exmlPluginNode, NODE);

    if (jangarooPluginNode == null) {
      jangarooPluginNode = document.createElement("plugin");
      insertChildWithWhitespace(pluginsNode, jangarooPluginNode, exmlPluginNode);

      Node jangarooPluginGroupIdNode = document.createElement("groupId");
      jangarooPluginGroupIdNode.setTextContent("net.jangaroo");
      insertChildWithWhitespace(jangarooPluginNode, jangarooPluginGroupIdNode, null);

      Node jangarooPluginArtifactIdNode = document.createElement("artifactId");
      jangarooPluginArtifactIdNode.setTextContent("jangaroo-maven-plugin");
      insertChildWithWhitespace(jangarooPluginNode, jangarooPluginArtifactIdNode, null);

      if (exmlVersionNode != null) {
        Node jangarooPluginVersionNode = document.createElement("version");
        jangarooPluginVersionNode.setTextContent(exmlVersionNode.getTextContent());
        insertChildWithWhitespace(jangarooPluginNode, jangarooPluginVersionNode, null);
      }
    }
    Node jangarooConfigurationNode = (Node) xPath.evaluate("configuration", jangarooPluginNode, NODE);
    Node jangarooExtensionsNode = (Node) xPath.evaluate("extensions", jangarooPluginNode, NODE);

    removeChildWithWhitespace(pluginsNode, exmlPluginNode);
    if (exmlExtensionNode != null) {
      if (jangarooExtensionsNode == null) {
        insertChildWithWhitespace(jangarooPluginNode, exmlExtensionNode, jangarooConfigurationNode);
      } else {
        jangarooExtensionsNode.setTextContent(exmlExtensionNode.getTextContent());
      }
    }
    if (configClassPackageNode != null) {
      if (jangarooConfigurationNode == null) {
        jangarooConfigurationNode = document.createElement("configuration");
        insertChildWithWhitespace(jangarooPluginNode, jangarooConfigurationNode, null);
      }
      Node namespacesNode = document.createElement("namespaces");
      insertChildWithWhitespace(jangarooConfigurationNode, namespacesNode, null);
      Node namespaceNode = document.createElement("namespace");
      insertChildWithWhitespace(namespacesNode, namespaceNode, null);
      Node uriNode = document.createElement("uri");
      uriNode.setTextContent("exml:" + configClassPackageNode.getTextContent());
      insertChildWithWhitespace(namespaceNode, uriNode, null);
    }
  }

  /**
   * Removes a child node. Also removed preceding whitespace to preserve correct indentation.
   */
  private static void removeChildWithWhitespace(Node parent, Node child) {
    Node previousSibling = child.getPreviousSibling();
    if (previousSibling.getNodeType() == Node.TEXT_NODE) {
      parent.removeChild(previousSibling);
    }
    parent.removeChild(child);
  }

  /**
   * Insert a child node at a given position. Adds whitespace before and/or after the child if necessary to preserve
   * correct indentation..
   */
  private static void insertChildWithWhitespace(Node parent, Node child, Node refChild) {
    if (refChild == null) {
      // trying to append a child
      if (parent.getLastChild() != null && parent.getLastChild().getNodeType() == Node.TEXT_NODE) {
        // append before the last child node which contains the indentation for the closing tag
        refChild = parent.getLastChild();
      }
    } else if (refChild.getPreviousSibling() != null && refChild.getPreviousSibling().getNodeType() == Node.TEXT_NODE) {
      refChild = refChild.getPreviousSibling();
    }
    parent.insertBefore(child, refChild);
    // insert whitespace before child for correct indentation
    parent.insertBefore(getIndent(child), child);
    if (parent.getChildNodes().getLength() == 2) {
      // child was the first child, insert whitespace between child and closing tag for indentation of closing tag
      parent.insertBefore(getIndent(parent), null);
    }
  }

  /**
   * Creates a text node containing the necessary whitespace to correctly indent the given node.
   */
  private static Node getIndent(Node node) {
    int depth = getDepth(node);
    String whitespace = "\n" + repeat(" ", (depth - 1) * guessIndent(node.getOwnerDocument()));
    return node.getOwnerDocument().createTextNode(whitespace);
  }

  /**
   * Calculates the depth of the given node within the document.
   */
  private static int getDepth(Node node) {
    if (node.getParentNode() == null) {
      return 0;
    }
    return getDepth(node.getParentNode()) + 1;
  }

  /**
   * Tries to guess the document's indentation from the first child element of the document element.
   */
  private static int guessIndent(Document document) {
    // find first child element
    Node firstElement = document.getDocumentElement().getFirstChild();
    while (firstElement != null && firstElement.getNodeType() != Node.ELEMENT_NODE) {
      firstElement = firstElement.getNextSibling();
    }

    if (firstElement != null) {
      // the node before the first element contains the indenting whitespace
      Node whitespaceNode = firstElement.getPreviousSibling();
      if (whitespaceNode.getNodeType() == Node.TEXT_NODE) {
        String text = whitespaceNode.getTextContent();
        int pos = Math.max(text.lastIndexOf("\n"), text.lastIndexOf("\r"));
        return text.substring(pos + 1).length();
      }
    }
    return 2;
  }
}
