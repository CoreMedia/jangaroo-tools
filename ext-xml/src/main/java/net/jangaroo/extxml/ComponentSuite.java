package net.jangaroo.extxml;

import java.util.Map;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.io.File;

/**
 * A set of Ext JS components bundled under the same namespace.
 * An XML schema can be defined that contains element definition for all component classes.
 */
public class ComponentSuite {

  public ComponentSuite(File xsd) {
    this(null, xsd, null, Collections.<File>emptyList());
  }

  public ComponentSuite(String namespace, File xsd, File rootDir, List<File> importedXsds) {
    this.namespace = namespace;
    this.xsd = xsd;
    this.rootDir = rootDir;
    importedComponentSuites = new ComponentSuites(importedXsds);
    usedComponentSuites = new LinkedHashMap<String, ComponentSuite>(importedXsds.size());
  }

  public Map<String, ComponentSuite> getUsedComponentSuites() {
    return usedComponentSuites;
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
    return ns;
  }

  public String getPrefix() {
    return ns == null || ns.length() == 0 ? "" : ns + ":";
  }

  public File getXsd() {
    return xsd;
  }

  public File getRootDir() {
    return rootDir;
  }

  public void addComponentClass(ComponentClass cc) {
    cc.setSuite(this);
    componentClassesByXtype.put(cc.getXtype(), cc);
    componentClassesByClassName.put(cc.getClassName(), cc);
  }

  public Collection<ComponentClass> getComponentClasses() {
    return componentClassesByXtype.values();
  }

  public ComponentClass getComponentClassByXtype(String xtype) {
    ComponentClass componentClass = componentClassesByXtype.get(xtype);
    if (componentClass==null) {
      ComponentSuite importedComponentSuite = importedComponentSuites.getComponentSuiteDefiningXtype(xtype);
      if (importedComponentSuite != null) {
        componentClass = importedComponentSuite.getComponentClassByXtype(xtype);
        if (componentClass != null) {
          updateUsedComponentSuites(importedComponentSuite);
        }
      }
    }
    return componentClass;
  }

  public ComponentClass getComponentClassByClassName(String className) {
    ComponentClass componentClass = componentClassesByClassName.get(className);
    if (componentClass==null) {
      ComponentSuite importedComponentSuite = importedComponentSuites.getComponentSuiteDefiningClassName(className);
      if (importedComponentSuite != null) {
        componentClass = importedComponentSuite.getComponentClassByClassName(className);
        if (componentClass != null) {
          updateUsedComponentSuites(importedComponentSuite);
        }
      }
    }
    return componentClass;
  }

  private void updateUsedComponentSuites(ComponentSuite importedComponentSuite) {
    if (!usedComponentSuites.containsValue(importedComponentSuite)) {
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
      .append("xsd file:  ").append(xsd.getAbsolutePath()).append("\n")
      .append("src root:  ").append(rootDir).append("\n\n");
    for (ComponentClass cc : getComponentClasses()) {
      builder.append(cc).append("\n\n");
      //builder.append(cc.getXtype()).append(": ").append(cc.getClassName()).append("\n");
    }
    return builder.toString();
  }

  private String ns;
  private String namespace;
  private File xsd;
  private File rootDir;
  private Map<String, ComponentClass> componentClassesByXtype = new LinkedHashMap<String, ComponentClass>();
  private Map<String, ComponentClass> componentClassesByClassName = new LinkedHashMap<String, ComponentClass>();
  private ComponentSuites importedComponentSuites;
  private Map<String, ComponentSuite> usedComponentSuites;

}
