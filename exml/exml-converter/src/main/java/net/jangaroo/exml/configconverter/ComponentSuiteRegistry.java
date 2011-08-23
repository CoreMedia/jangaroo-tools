package net.jangaroo.exml.configconverter;

import net.jangaroo.exml.configconverter.model.ComponentClass;
import net.jangaroo.exml.configconverter.model.ComponentSuite;
import net.jangaroo.utils.log.Log;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A registry for ComponentSuites, which returns a ComponentSuite given its namespace URI.
 * Is the ComponentSuite is not yet registered, it uses a ComponentSuiteResolver to construct it and
 * caches it for later access.
 */
public final class ComponentSuiteRegistry {

  private final Map<String, ComponentSuite> componentSuitesByNamespaceUri = new LinkedHashMap<String, ComponentSuite>(10);

  public void add(ComponentSuite componentSuite) {
    componentSuitesByNamespaceUri.put(componentSuite.getNamespace(), componentSuite);
    componentSuite.setRegistry(this);
  }

  public ComponentSuite getComponentSuite(String namespaceUri) {
    ComponentSuite componentSuite = componentSuitesByNamespaceUri.get(namespaceUri);
    if (componentSuite == null) {
      Log.i("Component suite for namespace URI "+namespaceUri+" not found in registry.");
    }
    return componentSuite;
  }

  public ComponentClass getComponentClass(String namespaceUri, String localName) {
    ComponentSuite componentSuite = getComponentSuite(namespaceUri);
    return componentSuite != null ? componentSuite.getComponentClassByLocalName(localName) : null;
  }

  public ComponentClass findComponentClassByXtype(String xtype) {
    // now, search all known component suites for xtype:
    for (ComponentSuite suite : componentSuitesByNamespaceUri.values()) {
      ComponentClass componentClass = suite.getComponentClassByXtype(xtype);
      if (componentClass != null) {
        return componentClass;
      }
    }
    return null;
  }

  public ComponentClass findComponentClassByFullClassName(String fullComponentClassName) {
    // now, search all known component suites for fully qualified component class name:
    for (ComponentSuite suite : componentSuitesByNamespaceUri.values()) {
      ComponentClass componentClass = suite.getComponentClassByFullClassName(fullComponentClassName);
      if (componentClass != null) {
        return componentClass;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (ComponentSuite suite : componentSuitesByNamespaceUri.values()) {
      sb.append(suite.getNamespace()).append(", ");
    }
    return sb.toString();
  }

  public void reset() {
    componentSuitesByNamespaceUri.clear();
  }
}
