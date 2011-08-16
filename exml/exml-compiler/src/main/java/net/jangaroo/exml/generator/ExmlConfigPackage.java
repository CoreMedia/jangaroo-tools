package net.jangaroo.exml.generator;

import net.jangaroo.exml.model.ConfigClass;

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

  private static final Comparator<ConfigClass> CONFIG_CLASS_BY_NAME_COMPARATOR = new Comparator<ConfigClass>() {
    @Override
    public int compare(ConfigClass cc1, ConfigClass cc2) {
      return cc1.getName().compareTo(cc2.getName());
    }
  };

  private List<ConfigClass> configClasses;
  private Map<String,String> usedNamespaces;
  private String packageName;

  public ExmlConfigPackage(Collection<ConfigClass> cl, String packageName) {
    this.packageName = packageName;
    this.configClasses = new ArrayList<ConfigClass>(cl);
    Collections.sort(configClasses, CONFIG_CLASS_BY_NAME_COMPARATOR);
    this.usedNamespaces = findDistinctPackageNames(configClasses);
  }

  private Map<String,String> findDistinctPackageNames(Collection<ConfigClass> configClasses) {
    Map<String,String> names = new HashMap<String,String>();
    for(ConfigClass cl : configClasses) {
      if(cl.getSuperClass() != null && !packageName.equals(cl.getSuperClass().getPackageName()) && !names.containsValue(cl.getSuperClass().getPackageName())) {
        names.put(calcNsFromPackage(cl.getSuperClass().getPackageName()), cl.getSuperClass().getPackageName());
      }
    }
    return names;
  }

  private String calcNsFromPackage(String theName) {
    String[] parts = theName.split("\\.");
    StringBuilder ns = new StringBuilder();
    for (String part : parts) {
      ns.append(part.charAt(0));
    }
    return ns.toString();
  }

  public String getNs() {
    return calcNsFromPackage(packageName);
  }

  public String getPackageName() {
    return packageName;
  }

  public List<ConfigClass> getConfigClasses() {
    return configClasses;
  }

  public Map<String,String> getUsedNamespaces() {
    return usedNamespaces;
  }
}
