package net.jangaroo.jooc.mvnplugin.util;


import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
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
 * Copied and modified from blueprint-extension-maven-plugin project
 *
 */
public class PomManipulator {

  private static final String DEPENDENCIES_QUERY = "/project/dependencies";

  private PomManipulator() {
    // hide constructor
  }

  /**
   * Add the given dependency to the <u>dependencies</u> of the given project.
   * After adding the dependencies the pom file will be rewritten.
   *
   * @param project - the project to manipulate
   * @param dependency - the dependency which should be added
   * @param log -Maven Logger to Log what happens
   */
  public static void addDependency(@Nonnull MavenProject project, @Nonnull Dependency dependency, @Nonnull Log log)
          throws MojoExecutionException { //NOSONAR

    addDependencies(project, Collections.singletonList(dependency), log);
  }

  /**
   * Add the given dependencies to the <u>dependencies</u> of the given project.
   * After adding the dependencies the pom file will be rewritten.
   *
   * @param project - the project to manipulate
   * @param dependencies - the dependencies which should be added
   * @param log -Maven Logger to Log what happens
   */
  public static void addDependencies(@Nonnull MavenProject project, List<Dependency> dependencies, Log log)
          throws MojoExecutionException { //NOSONAR

    Document doc = initDocument(project.getFile());

    NodeList nodes = queryForDependencies(doc);
    addDependencies(doc, nodes, dependencies, log);
    writeUpdatedPom(project.getFile(), doc);
  }

  /**
   * Sets the dependencies of the project.
   * After adding the dependencies the pom file will be rewritten.
   *
   * @param project - the project to manipulate
   * @param dependencies - the dependencies which should be added
   * @param log -Maven Logger to Log what happens
   */
  public static void updateDependencies(@Nonnull MavenProject project, List<Dependency> dependencies, Log log)
          throws MojoExecutionException { //NOSONAR

    Document doc = initDocument(project.getFile());

    NodeList nodes = queryForDependencies(doc);
    setDependencies(doc, nodes, dependencies, log);
    writeUpdatedPom(project.getFile(), doc);
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
      DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
      // there is only one default namespace so we can disable it
      // notice: namespaces break xpath expressions
      // either disable them or provide a NamespaceContext
      xmlFact.setNamespaceAware(false);
      DocumentBuilder builder = xmlFact.newDocumentBuilder();
      return builder.parse(new java.io.ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
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
      Node dependencyNode = createDependencyNode(document, dependency);
      dependencyNodes.appendChild(dependencyNode);
      log.info("Append dependency to dependency management: " + dependency);
    }
  }

  private static void setDependencies(Document document, NodeList nodes, List<Dependency> dependencies, Log log) { //NOSONAR
    //Only one dependencies tag allowed in maven, so we can assume we want this node.
    Node dependenciesNode = nodes.item(0);

    // XmlHelper.removeEmptyText(dependenciesNode, log);

    // add all the new dependencies
    for (Dependency dependency: dependencies) {
      Node dependencyNode = createDependencyNode(document, dependency);
      dependenciesNode.appendChild(dependencyNode);
      log.info("Append dependency to dependency management: " + dependency);
    }
  }

  private static Node createDependencyNode(Document document, Dependency dependency) {
    return createDependencyNode(document, dependency.getArtifactId(), dependency.getGroupId(), dependency.getVersion(), dependency.getType(), dependency.getScope());
  }

  private static Node createDependencyNode(Document document, String artifactId, String groupId, String version, String type, String scope) {

    Element dependencyNode = createElement(document, "dependency", null);

    Element groupIdTag = createElement(document, "groupId", groupId);
    Element artifactIdTag = createElement(document, "artifactId", artifactId);
    dependencyNode.appendChild(groupIdTag);
    dependencyNode.appendChild(artifactIdTag);

    if (version != null) {
      Element versionTag = createElement(document, "version", version);
      dependencyNode.appendChild(versionTag);
    }

    if (type != null) {
      Element typeTag = createElement(document, "type", type);
      dependencyNode.appendChild(typeTag);
    }

    if (scope != null) {
      Element scopeTag = createElement(document, "scope", scope);
      dependencyNode.appendChild(scopeTag);
    }

    return dependencyNode;
  }

  private static Element createElement(Document document, String tagname, String textContent) {
    Element element = document.createElement(tagname);
    element.setTextContent(textContent);
    return element;
  }
}
