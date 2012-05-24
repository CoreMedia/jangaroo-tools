package net.jangaroo.exml.generator;

import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.exml.model.ConfigClassToNewExmlElementAdapter;
import net.jangaroo.exml.model.ConfigClassToOldExmlElementAdapter;
import net.jangaroo.exml.model.ExmlElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ExmlConfigPackage {

  private static final Comparator<ExmlElement> EXML_ELEMENT_BY_NAME_COMPARATOR = new Comparator<ExmlElement>() {
    @Override
    public int compare(ExmlElement ee1, ExmlElement ee2) {
      return ee1.getName().compareTo(ee2.getName());
    }
  };

  private List<ExmlElement> exmlElements;
  private Map<String,String> usedNamespaces;
  private String packageName;
  private String ns;

  public ExmlConfigPackage(Collection<ConfigClass> cl, String packageName) {
    this.packageName = packageName;
    ns = calcNsFromPackageName(packageName, 1);
    exmlElements = new ArrayList<ExmlElement>(cl.size());
    boolean useTargetClassNames = !packageName.endsWith(".config");
    for (ConfigClass configClass : cl) {
      exmlElements.add(useTargetClassNames
        ? new ConfigClassToNewExmlElementAdapter(configClass)
        : new ConfigClassToOldExmlElementAdapter(configClass));
    }
    computeShortNamespaces();
    Collections.sort(exmlElements, EXML_ELEMENT_BY_NAME_COMPARATOR);
  }

  private void computeShortNamespaces() {
    usedNamespaces = new HashMap<String,String>();
    for (ExmlElement ee : exmlElements) {
      calcNsFromPackage(ee);
      ExmlElement superElement = ee.getSuperElement();
      if (superElement != null) {
        calcNsFromPackage(superElement);
      }
    }
  }

  private void calcNsFromPackage(ExmlElement exmlElement) {
    String packageName = exmlElement.getPackage();
    String shortNamespace;
    if (this.packageName.equals(packageName)) {
      shortNamespace = ns; // do not add to usedNamespaces, as freemarker template cares about this separately!
    } else {
      shortNamespace = usedNamespaces.get(packageName);
      if (shortNamespace == null) {
        if (packageName.length() == 0) {
          shortNamespace = "top"; // should only occur in tests or demo code!
        } else {
          for (int n = 1; ; n++) {
            shortNamespace = calcNsFromPackageName(packageName, n);
            if (!usedNamespaces.values().contains(shortNamespace)) {
              break;
            }
          }
        }
        usedNamespaces.put(packageName, shortNamespace);
      }
    }
    exmlElement.setNs(shortNamespace);
  }

  private String calcNsFromPackageName(String packageName, int n) {
    if (packageName.length() == 0) {
      return "t";
    }
    String shortNamespace;
    String[] parts = packageName.split("\\.");
    StringBuilder ns = new StringBuilder();
    for (String part : parts) {
      ns.append(part.substring(0, n));
    }
    shortNamespace = ns.toString();
    return shortNamespace;
  }

  public String getNs() {
    return ns;
  }

  public String getPackageName() {
    return packageName;
  }

  public List<ExmlElement> getExmlElements() {
    return exmlElements;
  }

  public Map<String,String> getUsedNamespaces() {
    return usedNamespaces;
  }
}
