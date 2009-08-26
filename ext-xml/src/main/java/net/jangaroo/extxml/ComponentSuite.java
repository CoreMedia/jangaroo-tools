package net.jangaroo.extxml;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.io.File;

/**
 * A set of Ext JS components bundled under the same namespace.
 * An XML schema can be defined that contains element definition for all component classes.
 */
public class ComponentSuite {

  public ComponentSuite(File xsd) {
    this(null, xsd, null);
  }

  public ComponentSuite(String namespace, File xsd, File rootDir) {
    this.namespace = namespace;
    this.xsd = xsd;
    this.rootDir = rootDir;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getNamespace() {
    return namespace;
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
    return componentClassesByXtype.get(xtype);
  }

  public ComponentClass getComponentClassByClassName(String className) {
    return componentClassesByClassName.get(className);
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

  private String namespace;
  private File xsd;
  private File rootDir;
  private Map<String,ComponentClass> componentClassesByXtype = new HashMap<String,ComponentClass>();
  private Map<String,ComponentClass> componentClassesByClassName = new HashMap<String,ComponentClass>();
}
