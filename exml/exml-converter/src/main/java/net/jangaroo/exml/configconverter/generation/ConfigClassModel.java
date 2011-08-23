package net.jangaroo.exml.configconverter.generation;

import net.jangaroo.exml.configconverter.model.ComponentClass;
import net.jangaroo.exml.configconverter.model.ComponentSuite;
import org.codehaus.plexus.util.StringUtils;

public class ConfigClassModel {
  private ComponentSuite componentSuite;
  private String className;
  private ComponentClass componentClass;
  private String type;

  public ConfigClassModel(ComponentClass componentClass, ComponentSuite componentSuite, String className, String type) {
    this.componentSuite = componentSuite;
    this.className = className;
    this.componentClass = componentClass;
    this.type = type;
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

  public String getType() {
    return type;
  }

  public String getExtendsPhrase() {
    ComponentClass superComponentClass = componentSuite.findComponentClassByFullClassName(componentClass.getSuperClassName());
    if (superComponentClass == null) {
      return " extends joo.JavaScriptObject";
    }
    return " extends " + superComponentClass.getSuite().getConfigClassPackage() + "." + StringUtils.uncapitalise(superComponentClass.getLastXtypeComponent());
  }

  public String getImportSuperClassPhrase() {
    ComponentClass superComponentClass = componentSuite.findComponentClassByFullClassName(componentClass.getSuperClassName());
    if (superComponentClass == null) {
      return "import joo.JavaScriptObject;";
    }
    return "import " + superComponentClass.getSuite().getConfigClassPackage() + "." + StringUtils.uncapitalise(superComponentClass.getLastXtypeComponent()) + ";";
  }
}
