package net.jangaroo.extxml;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * From a List of ComponentSuites, find the one defining the given xtype or class name.
 */
public class ComponentSuites {

  private List<ComponentSuite> componentSuites = new ArrayList<ComponentSuite>();

  public ComponentSuites(List<File> xsds) {
    for (File importedXsd : xsds) {
      componentSuites.add(new XsdScanner(importedXsd).scan());
    }
  }

  public void addComponentSuite(ComponentSuite componentSuite) {
    componentSuites.add(componentSuite);
  }

  public ComponentSuite getComponentSuiteDefiningXtype(String xtype) {
    for (ComponentSuite importedComponentSuite : componentSuites) {
      if (importedComponentSuite.getComponentClassByXtype(xtype) != null) {
        return importedComponentSuite;
      }
    }
    return null;
  }

  public ComponentClass getComponentClassByXtype(String xtype) {
    ComponentSuite componentSuite = getComponentSuiteDefiningXtype(xtype);
    if (componentSuite != null) {
      return componentSuite.getComponentClassByXtype(xtype);
    }
    return null;
  }

  public ComponentSuite getComponentSuiteDefiningClassName(String className) {
    for (ComponentSuite importedComponentSuite : componentSuites) {
      if (importedComponentSuite.getComponentClassByClassName(className) != null) {
        return importedComponentSuite;
      }
    }
    return null;
  }


}
