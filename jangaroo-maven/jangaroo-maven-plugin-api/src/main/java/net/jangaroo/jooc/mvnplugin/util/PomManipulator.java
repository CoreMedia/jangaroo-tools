package net.jangaroo.jooc.mvnplugin.util;


import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * Provides methods to manipulate pom files.
 */
public class PomManipulator {

  private static final String DEPENDENCIES_QUERY = "/project/dependencies";

  private PomManipulator() {
  }

  /**
   * Add dependencies to <u>dependency management</u> and set the given scope to those dependencies.
   * After adding the dependencies the pom file will be rewritten.
   *
   * @param pom - the pom file to manipulate
   * @param dependencies - the dependencies which should be added
   * @param log -Maven Logger to Log what happens
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws XPathExpressionException
   * @throws TransformerException
   */
  public static void addDependencies(File pom, List<Dependency> dependencies, Log log) throws MojoExecutionException { //NOSONAR
    Document doc = initDocument(pom);

    NodeList nodes = queryForDependencies(doc);
    addDependencies(doc, nodes, dependencies, log);
    writeUpdatedPom(pom, doc);
  }

  public static void addDependency(File pom, Dependency dependency, Log log) throws MojoExecutionException { //NOSONAR
    addDependencies(pom, Collections.singletonList(dependency), log);
  }

  private static void writeUpdatedPom(File pom, Document doc) throws MojoExecutionException {
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      DOMSource source = new DOMSource(doc);
      StreamResult streamResult = new StreamResult(pom);
      transformer.transform(source, streamResult);
    } catch (TransformerException e) {
      throw new MojoExecutionException("Cannot write updated pom", e);
    }
  }


  private static NodeList queryForDependencies(Document doc) throws MojoExecutionException {
    return performXPathQuery(doc, DEPENDENCIES_QUERY);
  }

  private static NodeList performXPathQuery(Document doc, String query) throws MojoExecutionException {
    try {
      XPath xpath = XPathFactory.newInstance().newXPath();
      return (NodeList) xpath.evaluate(query,
              doc, XPathConstants.NODESET);
    } catch (XPathExpressionException e) {
      throw new MojoExecutionException("Cannot parse pom", e);
    }
  }

  private static Document initDocument(File pom) throws MojoExecutionException {
    try {
      String xml = FileUtils.readFileToString(pom);

      DocumentBuilderFactory xmlFact =
              DocumentBuilderFactory.newInstance();
      // there is only one default namespace so we can disable it
      // notice: namespaces break xpath expressions
      // either disable them or provide a NamespaceContext
      xmlFact.setNamespaceAware(false);
      DocumentBuilder builder = xmlFact.newDocumentBuilder();
      return builder.parse(
              new java.io.ByteArrayInputStream(
                      xml.getBytes(Charset.forName("UTF-8"))));
    } catch (ParserConfigurationException e) {
      throw new MojoExecutionException("Cannot read pom because of parser exception", e);
    }  catch (SAXException e) {
      throw new MojoExecutionException("Cannot read pom because of xml errors", e);
    }  catch (IOException e) {
      throw new MojoExecutionException("Cannot read pom because of IO", e);
    }
  }

  private static void addDependencies(Document document, NodeList nodes, List<Dependency> dependencies, Log log) { //NOSONAR
    //Only one dependencies tag allowed in maven, so we can assume we want this node.
    Node dependencyNodes = nodes.item(0);
    for (Dependency dependency: dependencies) {
      Node dependencyNode = XmlHelper.createDependencyNode(document, dependency);
      dependencyNodes.appendChild(dependencyNode);
      log.info("Append dependency to dependency management: " + dependency);
    }
  }

  private static void setDependencies(Document document, NodeList nodes, List<Dependency> dependencies, Log log) { //NOSONAR
    //Only one dependencies tag allowed in maven, so we can assume we want this node.
    Node dependencyNodes = nodes.item(0);
    // TODO dependencyNodes.remve all
    for (Dependency dependency: dependencies) {
      Node dependencyNode = XmlHelper.createDependencyNode(document, dependency);
      dependencyNodes.appendChild(dependencyNode);
      log.info("Append dependency to dependency management: " + dependency);
    }
  }
}
