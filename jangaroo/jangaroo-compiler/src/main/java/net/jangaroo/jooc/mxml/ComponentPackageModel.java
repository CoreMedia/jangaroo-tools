package net.jangaroo.jooc.mxml;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * An MXML component package consists of a namespace and a map from local element name to
 * fully qualified ActionScript class name.
 */
public class ComponentPackageModel {

  private String namespace;
  private Map<String, String> element2class = new LinkedHashMap<String, String>();

  public ComponentPackageModel(String namespace) {
    this.namespace = namespace;
  }

  public String getNamespace() {
    return namespace;
  }

  public void addElementToClassNameMapping(String localName, String classQName) {
    element2class.put(localName, classQName);
  }

  public String getClassName(String localName) {
    return element2class.get(localName);
  }

  public Set<Map.Entry<String, String>> entrySet() {
    return element2class.entrySet();
  }
}
