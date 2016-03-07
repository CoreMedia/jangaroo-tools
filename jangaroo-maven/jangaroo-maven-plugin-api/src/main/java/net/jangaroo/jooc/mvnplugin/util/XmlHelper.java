package net.jangaroo.jooc.mvnplugin.util;

import org.apache.maven.model.Dependency;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlHelper {

  private XmlHelper() {

  }

  protected static Node createDependencyNode(Document document, Dependency dependency) {
    return createDependencyNode(document, dependency.getArtifactId(), dependency.getGroupId(), dependency.getVersion(), dependency.getType());
  }

  protected static Node createDependencyNode(Document document, String artifactId, String groupId, String version, String type) {
    Element dependencyNode = document.createElement("dependency");
    Element artifactIdTag = createElement(document, "artifactId", artifactId);
    Element groupIdTag = createElement(document, "groupId", groupId);
    Element versionTag = createElement(document, "version", version);
    Element scopeTag = createElement(document, "type", type);

    dependencyNode.appendChild(artifactIdTag);
    dependencyNode.appendChild(groupIdTag);
    dependencyNode.appendChild(versionTag);
    dependencyNode.appendChild(scopeTag);

    return dependencyNode;
  }

  private static Element createElement(Document document, String tagname, String textContent) {
    Element element = document.createElement(tagname);
    element.setTextContent(textContent);
    return element;
  }
}
