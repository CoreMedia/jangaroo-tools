package net.jangaroo.extxml.generation;

import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.model.ComponentSuite;

public class ConfigClassModel {
  private ComponentSuite componentSuite;
  private String className;
  private ComponentClass componentClass;

  public ConfigClassModel(ComponentClass componentClass, ComponentSuite componentSuite, String className) {
    this.componentSuite = componentSuite;
    this.className = className;
    this.componentClass = componentClass;
  }

  public ComponentClass getComponentClass() {
    return componentClass;
  }

  public ComponentSuite getComponentSuite() {
    return componentSuite;
  }

  public String getClassName() {
    return className;
  }

  public String getExtendsPhrase() {
    ComponentClass superComponentClass = componentSuite.findComponentClassByFullClassName(componentClass.getSuperClassName());
    if (superComponentClass == null) {
      return " extends joo.JavaScriptObject";
    }
    return " extends " + superComponentClass.getSuite().getConfigClassPackage() + "." + superComponentClass.getLastXtypeComponent();
  }

  public String getImportSuperClassPhrase() {
    ComponentClass superComponentClass = componentSuite.findComponentClassByFullClassName(componentClass.getSuperClassName());
    if (superComponentClass == null) {
      return "import joo.JavaScriptObject;";
    }
    return "import " + superComponentClass.getSuite().getConfigClassPackage() + "." + superComponentClass.getLastXtypeComponent() + ";";
  }
}
