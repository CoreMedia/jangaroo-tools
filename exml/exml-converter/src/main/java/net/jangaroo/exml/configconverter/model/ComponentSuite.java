package net.jangaroo.exml.configconverter.model;

import net.jangaroo.exml.configconverter.ComponentSuiteRegistry;
import net.jangaroo.utils.log.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of Ext JS components bundled under the same namespace.
 * An XML schema can be defined that contains element definition for all component classes.
 */
public final class ComponentSuite {

  private String ns;
  private String namespace;
  private File rootDir;
  private File as3OutputDir;
  private Map<String, ComponentClass> componentClassesByXtype = new HashMap<String, ComponentClass>();
  private Map<String, ComponentClass> componentClassesByLocalName = new HashMap<String, ComponentClass>();
  private Map<String, ComponentClass> componentClassesByFullClassName = new HashMap<String, ComponentClass>();
  private Map<String, ComponentSuite> usedComponentSuites;
  private ComponentSuiteRegistry registry;

  private static final Comparator<ComponentClass> COMPONENT_CLASS_BY_ELEMENT_NAME_COMPARATOR = new Comparator<ComponentClass>() {
    @Override
    public int compare(ComponentClass cc1, ComponentClass cc2) {
      return cc1.getElementName().compareTo(cc2.getElementName());
    }
  };
  private String configClassPackage;

  public ComponentSuite() {
    this(new ComponentSuiteRegistry(), null, null, null, null, null);
  }

  public ComponentSuiteRegistry getRegistry() {
    return registry;
  }

  public void setRegistry(ComponentSuiteRegistry registry) {
    this.registry = registry;
  }

  public ComponentSuite(ComponentSuiteRegistry registry, String namespace, String namespacePrefix, File rootDir, File as3OutputDir, String configClassPackage) {
    this.namespace = namespace;
    this.ns = namespacePrefix;
    this.rootDir = rootDir;
    this.as3OutputDir = as3OutputDir;
    this.configClassPackage = configClassPackage;
    registry.add(this);
    usedComponentSuites = new LinkedHashMap<String, ComponentSuite>();
  }


  public void addImportedComponentSuite(ComponentSuite importedSuite) {
    if(importedSuite != null) {
      registry.add(importedSuite);
    }
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

  public void setRootDir(File rootDir) {
    this.rootDir = rootDir;
  }

  public File getRootDir() {
    return rootDir;
  }

  public void setAs3OutputDir(File as3OutputDir) {
    this.as3OutputDir = as3OutputDir;
  }

  public File getAs3OutputDir() {
    return as3OutputDir;
  }

  public void addComponentClass(ComponentClass cc) {
    cc.setSuite(this);
    componentClassesByXtype.put(cc.getXtype(), cc);
    componentClassesByLocalName.put(cc.getElementName(), cc);
    componentClassesByFullClassName.put(cc.getFullClassName(), cc);
  }

  public List<ComponentClass> getSortedComponentClasses() {
    List<ComponentClass> components = new ArrayList<ComponentClass>(getComponentClasses());
    Collections.sort(components, COMPONENT_CLASS_BY_ELEMENT_NAME_COMPARATOR);
    return components;
  }

  public Collection<ComponentClass> getComponentClasses() {
    return componentClassesByXtype.values();
  }

  public ComponentClass getComponentClassByXtype(String xtype) {
    return componentClassesByXtype.get(xtype);
  }

  public ComponentClass getComponentClassByLocalName(String localName) {
    return componentClassesByLocalName.get(localName);
  }

  public ComponentClass getComponentClassByNamespaceAndLocalName(String namespaceUri, String localName) {
    ComponentClass componentClass = registry.getComponentClass(namespaceUri, localName);
    updateUsedComponentSuites(componentClass);
    return componentClass;
  }

  public ComponentClass findComponentClassByXtype(String xtype) {
    ComponentClass componentClass = registry.findComponentClassByXtype(xtype);
    updateUsedComponentSuites(componentClass);
    return componentClass;
  }

  public ComponentClass getComponentClassByFullClassName(String className) {
    return componentClassesByFullClassName.get(className);
  }

  public ComponentClass findComponentClassByFullClassName(String className) {
    ComponentClass componentClass = registry.findComponentClassByFullClassName(className);
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
      String suiteNs = importedComponentSuite.getNs();
      if (suiteNs == null || suiteNs.length() == 0 || usedComponentSuites.containsKey(suiteNs)) {
        // create a new unique prefix:
        int index = 1;
        while (usedComponentSuites.containsKey(suiteNs = "cs" + index)) {
          ++index;
        }
        importedComponentSuite.setNs(suiteNs);
      }
      usedComponentSuites.put(suiteNs, importedComponentSuite);
    }
  }

  public void resolveSuperClasses() {
    for (ComponentClass cc : getComponentClasses()) {
      if (cc.getSuperClass() == null && cc.getSuperClassName() != null) {
        Log.w("Super component class '" + cc.getSuperClassName() + "' not found.");
      } else if (cc.getSuperClassName() == null && cc.getSuperClassNamespaceUri() != null && cc.getSuperClassLocalName() != null)  {
        ComponentClass supercl = this.getComponentClassByNamespaceAndLocalName(cc.getSuperClassNamespaceUri(), cc.getSuperClassLocalName());
        if(supercl != null) {
          cc.setSuperClassName(supercl.getFullClassName());
        } else {
          Log.e(String.format("Super component class with element name '%s' not found in component suite '%s'", cc.getSuperClassLocalName(), cc.getSuperClassNamespaceUri()));
        }
      }
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

  public String getConfigClassPackage() {
    return configClassPackage;
  }

  public void setConfigClassPackage(String configClassPackage) {
    this.configClassPackage = configClassPackage;
  }
}
