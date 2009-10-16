package net.jangaroo.extxml;

import java.io.File;
import java.util.*;

/**
 * A set of Ext JS components bundled under the same namespace.
 * An XML schema can be defined that contains element definition for all component classes.
 */
public class ComponentSuite {

  private String ns;
  private String namespace;
  private File rootDir;
  private File as3OutputDir;
  private Map<String, ComponentClass> componentClassesByXtype = new LinkedHashMap<String, ComponentClass>();
  private Map<String, ComponentClass> componentClassesByFullClassName = new LinkedHashMap<String, ComponentClass>();
  private ComponentSuiteRegistry componentSuiteRegistry;
  private Map<String, ComponentSuite> usedComponentSuites;

  public ComponentSuite() {
    this(new ComponentSuiteRegistry(), null, null, null, null);
  }

  public ComponentSuite(String namespace, String namespacePrefix, File rootDir, File as3OutputDir) {
    this(new ComponentSuiteRegistry(), namespace, namespacePrefix, rootDir, as3OutputDir);
  }

  public ComponentSuite(ComponentSuiteRegistry componentSuiteRegistry, String namespace, String namespacePrefix, File rootDir, File as3OutputDir) {
    this.componentSuiteRegistry = componentSuiteRegistry;
    this.namespace = namespace;
    this.ns = namespacePrefix;
    this.rootDir = rootDir;
    this.as3OutputDir = as3OutputDir;
    componentSuiteRegistry.add(this);
    usedComponentSuites = new LinkedHashMap<String, ComponentSuite>();
  }

  public ComponentSuiteRegistry getComponentSuiteRegistry() {
    return componentSuiteRegistry;
  }

  public void addImportedComponentSuite(ComponentSuite importedSuite) {
    if(importedSuite != null)
      componentSuiteRegistry.add(importedSuite);
  }

  public Map<String, ComponentSuite> getUsedComponentSuitesByNs() {
    return usedComponentSuites;
  }

  public Collection<ComponentSuite> getUsedComponentSuites() {
    return usedComponentSuites.values();
  }

  public String getUsedComponentSuiteNamespaces() {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, ComponentSuite> usedComponentSuiteEntry : usedComponentSuites.entrySet()) {
      builder
          .append(" xmlns:")
          .append(usedComponentSuiteEntry.getKey())
          .append("='")
          .append(usedComponentSuiteEntry.getValue().getNamespace())
          .append("'");
    }
    return builder.toString();
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNs(String ns) {
    this.ns = ns;
  }

  public String getNs() {
    return ns == null ? "" : ns;
  }

  public String getPrefix() {
    return ns == null || ns.length() == 0 ? "" : ns + ":";
  }

  public File getRootDir() {
    return rootDir;
  }

  public File getAs3OutputDir() {
    return as3OutputDir;
  }

  public void addComponentClass(ComponentClass cc) {
    cc.setSuite(this);
    componentClassesByXtype.put(cc.getXtype(), cc);
    componentClassesByFullClassName.put(cc.getFullClassName(), cc);
  }

  public Collection<ComponentClass> getComponentClasses() {
    return componentClassesByXtype.values();
  }

  public ComponentClass getComponentClassByXtype(String xtype) {
    return componentClassesByXtype.get(xtype);
  }

  public ComponentClass getComponentClassByNamespaceAndLocalName(String namespaceUri, String localName) {
    ComponentSuite componentSuite = componentSuiteRegistry.getComponentSuite(namespaceUri);
    ComponentClass componentClass = null;
    if (componentSuite != null) {
      componentClass = componentSuite.getComponentClassByXtype(localName);
      updateUsedComponentSuites(componentClass);
    } else {
      System.out.println("*** compontentSuiteRegistry: "+componentSuiteRegistry.toString());
    }
    return componentClass;
  }

  public ComponentClass findComponentClassByXtype(String xtype) {
    ComponentClass componentClass = componentSuiteRegistry.findComponentClassByXtype(xtype);
    updateUsedComponentSuites(componentClass);
    return componentClass;
  }

  public ComponentClass getComponentClassByFullClassName(String className) {
    return componentClassesByFullClassName.get(className);
  }

  public ComponentClass findComponentClassByFullClassName(String className) {
    ComponentClass componentClass = componentSuiteRegistry.findComponentClassByFullClassName(className);
    updateUsedComponentSuites(componentClass);
    return componentClass;
  }

  public List<ComponentClass> getComponentClassesByType(ComponentType type) {
    ArrayList<ComponentClass> result = new ArrayList<ComponentClass>();
    for (ComponentClass clazz : componentClassesByXtype.values()) {
      if (type.equals(clazz.getType())) {
        result.add(clazz);
      }
    }
    return result;
  }

  private void updateUsedComponentSuites(ComponentClass componentClass) {
    if (componentClass == null) {
      return;
    }
    ComponentSuite importedComponentSuite = componentClass.getSuite();
    if (importedComponentSuite!=this && !usedComponentSuites.containsValue(importedComponentSuite)) {
      String ns = importedComponentSuite.getNs();
      if (ns == null || ns.length() == 0 || usedComponentSuites.containsKey(ns)) {
        // create a new unique prefix:
        int index = 1;
        while (usedComponentSuites.containsKey(ns = "cs" + index)) {
          ++index;
        }
        importedComponentSuite.setNs(ns);
      }
      usedComponentSuites.put(ns, importedComponentSuite);
    }
  }

  void resolveSuperClasses() {
    for (ComponentClass cc : getComponentClasses()) {
      cc.getSuperClass();
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder
        .append("namespace: ").append(namespace).append("\n")
        .append("src root:  ").append(rootDir).append("\n\n");
    for (ComponentClass cc : getComponentClasses()) {
      builder.append(cc).append("\n\n");
      //builder.append(cc.getXtype()).append(": ").append(cc.getClassName()).append("\n");
    }
    return builder.toString();
  }

}
