package net.jangaroo.jooc.mxml;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A global registry for MXML namespaces that map elements to ActionScript class names.
 */
public class MxmlComponentRegistry {

  /**
   * namespace -> local element name -> fully qualified AS3 class name
   */
  private Map<String, ComponentPackageModel> registry = new HashMap<String, ComponentPackageModel>();
  private Map<String, List<QName>> qNamesByClassName = new HashMap<String, List<QName>>();

  public void registerElement(String namespace, String localName, String classQName) {
    ComponentPackageModel componentPackageModel = registry.get(namespace);
    if (componentPackageModel == null) {
      componentPackageModel = new ComponentPackageModel(namespace);
      registry.put(namespace, componentPackageModel);
    }
    componentPackageModel.addElementToClassNameMapping(localName, classQName);
    List<QName> qNames = qNamesByClassName.get(classQName);
    if (qNames == null) {
      qNames = new ArrayList<QName>();
      qNamesByClassName.put(classQName, qNames);
    }
    qNames.add(new QName(namespace, localName));
  }

  public String getClassName(String namespace, String localName) {
    ComponentPackageModel componentPackageModel = registry.get(namespace);
    return componentPackageModel != null ? componentPackageModel.getClassName(localName) : null;
  }

  public void add(ComponentPackageModel componentPackageModel) {
    String namespace = componentPackageModel.getNamespace();
    for (Map.Entry<String, String> entry : componentPackageModel.entrySet()) {
      registerElement(namespace, entry.getKey(), entry.getValue());
    }
  }

  public Collection<ComponentPackageModel> getComponentPackageModels() {
    return registry.values();
  }

  public ComponentPackageModel getComponentPackageModel(String namespace) {
    return registry.get(namespace);
  }

  public List<QName> getQNamesByClassName(String className) {
    return qNamesByClassName.get(className);
  }
}
