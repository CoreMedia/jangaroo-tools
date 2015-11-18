package net.jangaroo.jooc.mxml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A global registry for MXML namespaces that map elements to ActionScript class names.
 */
public class MxmlComponentRegistry {

  /**
   * namespace -> local element name -> fully qualified AS3 class name
   */
  private Map<String, ComponentPackageModel> registry = new HashMap<String, ComponentPackageModel>();

  public void registerElement(String namespace, String localName, String classQName) {
    ComponentPackageModel componentPackageModel = registry.get(namespace);
    if (componentPackageModel == null) {
      componentPackageModel = new ComponentPackageModel(namespace);
      registry.put(namespace, componentPackageModel);
    }
    componentPackageModel.addElementToClassNameMapping(localName, classQName);
  }

  public String getClassName(String namespace, String localName) {
    ComponentPackageModel componentPackageModel = registry.get(namespace);
    return componentPackageModel != null ? componentPackageModel.getClassName(localName) : null;
  }

  public void add(ComponentPackageModel componentPackageModel) {
    String namespace = componentPackageModel.getNamespace();
    ComponentPackageModel existingComponentPackageModel = registry.get(namespace);
    if (existingComponentPackageModel != null) {
      existingComponentPackageModel.add(componentPackageModel);
    } else {
      registry.put(namespace, componentPackageModel);
    }
  }

  public Collection<ComponentPackageModel> getComponentPackageModels() {
    return registry.values();
  } 
}
