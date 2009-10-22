package net.jangaroo.extxml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A registry for ComponentSuites, which returns a ComponentSuite given its namespace URI.
 * Is the ComponentSuite is not yet registered, it uses a ComponentSuiteResolver to construct it and
 * caches it for later access.
 * This class, more precise method {@link #getComponentSuite}, is not thread-safe!
 */
public class ComponentSuiteRegistry {

  private static final ComponentSuiteResolver NO_COMPONENT_SUITE_RESOLVER = new ComponentSuiteResolver() {
    public InputStream resolveComponentSuite(String namespaceUri) throws IOException {
      throw new IOException("No ComponentSuiteResolver installed.");
    }
  };

  private final Map<String, ComponentSuite> componentSuitesByNamespaceUri = new LinkedHashMap<String, ComponentSuite>(10);
  private final ComponentSuiteResolver componentSuiteResolver;
  private ErrorHandler errorHandler = new StandardOutErrorHandler();

  public ComponentSuiteRegistry() {
    this(NO_COMPONENT_SUITE_RESOLVER);
  }

  public ComponentSuiteRegistry(ComponentSuiteResolver componentSuiteResolver) {
    this.componentSuiteResolver = componentSuiteResolver;
  }

  public void setErrorHandler(ErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
  }

  public ErrorHandler getErrorHandler() {
    return errorHandler;
  }

  public void add(ComponentSuite componentSuite) {
    componentSuitesByNamespaceUri.put(componentSuite.getNamespace(), componentSuite);
  }

  public ComponentSuite getComponentSuite(String namespaceUri) {
    ComponentSuite componentSuite = componentSuitesByNamespaceUri.get(namespaceUri);
    if (componentSuite == null) {
      try {
        InputStream xsdInputStream = componentSuiteResolver.resolveComponentSuite(namespaceUri);
        if (xsdInputStream == null) {
          errorHandler.error("No XSD registered for namespace URI " + namespaceUri);
        } else {
          componentSuite = new XsdScanner(this).scan(xsdInputStream);
          assert namespaceUri.equals(componentSuite.getNamespace());
        }
      } catch (IOException e) {
        errorHandler.error("Could not resolve XSD for namespace URI " + namespaceUri, e);
      }
    }
    return componentSuite;
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
}
