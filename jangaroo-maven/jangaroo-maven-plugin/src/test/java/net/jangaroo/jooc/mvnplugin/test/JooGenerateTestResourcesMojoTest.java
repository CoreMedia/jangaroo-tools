package net.jangaroo.jooc.mvnplugin.test;

import junit.framework.TestCase;

import java.util.*;

/**
 * 
 */
public class JooGenerateTestResourcesMojoTest extends TestCase {
  JooGenerateTestResourcesMojo jooGenerateTestResourcesMojo;

  @Override
  protected void setUp() throws Exception {
    jooGenerateTestResourcesMojo = new JooGenerateTestResourcesMojo();
  }

  public void testLinearizeDependencies() {
    Map<String, List<String>> artifact2directDependencies = new HashMap<String, List<String>>();
    Set<String> all = new HashSet<String>();



    artifact2directDependencies.put("net.jangaroo:jangaroo-browser", new LinkedList<String>(Arrays.asList("net.jangaroo:jangaroo-runtime")));
    artifact2directDependencies.put("net.jangaroo:jooflash-native", new LinkedList<String>(Arrays.asList(new String[]{})));
    artifact2directDependencies.put("net.jangaroo:jangaroo-runtime", new LinkedList<String>(Arrays.asList(new String[]{})));
    artifact2directDependencies.put("net.jangaroo:tiny_mce", new LinkedList<String>(Arrays.asList("net.jangaroo:ext-all")));
    artifact2directDependencies.put("net.jangaroo:ext-base", new LinkedList<String>(Arrays.asList(new String[]{})));
    artifact2directDependencies.put("com.coremedia.ui:ui-client", new LinkedList<String>(Arrays.asList("net.jangaroo:ext-as", "net.jangaroo:joounit")));
    artifact2directDependencies.put("net.jangaroo:xmlhttprequest-as", new LinkedList<String>(Arrays.asList("net.jangaroo:jangaroo-browser", "net.jangaroo:xmlhttprequest")));
    artifact2directDependencies.put("net.jangaroo:miframe", new LinkedList<String>(Arrays.asList(new String[]{})));
    artifact2directDependencies.put("net.jangaroo:ext-as", new LinkedList<String>(Arrays.asList("net.jangaroo:jangaroo-runtime", "net.jangaroo:jangaroo-browser", "net.jangaroo:ext-base", "net.jangaroo:ext-as-aliases", "net.jangaroo:ext-all")));
    artifact2directDependencies.put("net.jangaroo:jooflexframework", new LinkedList<String>(Arrays.asList(new String[]{})));
    artifact2directDependencies.put("net.jangaroo:ext-as-aliases", new LinkedList<String>(Arrays.asList("net.jangaroo:ext-base")));
    artifact2directDependencies.put("net.jangaroo:joounit", new LinkedList<String>(Arrays.asList("net.jangaroo:jooflash", "net.jangaroo:jooflexframework")));
    artifact2directDependencies.put("net.jangaroo:ext-all", new LinkedList<String>(Arrays.asList("net.jangaroo:ext-base")));
    artifact2directDependencies.put("net.jangaroo:tiny_mce-ext", new LinkedList<String>(Arrays.asList("net.jangaroo:ext-as", "net.jangaroo:miframe", "net.jangaroo:tiny_mce")));
    artifact2directDependencies.put("net.jangaroo:xmlhttprequest", new LinkedList<String>(Arrays.asList(new String[]{})));
    artifact2directDependencies.put("net.jangaroo:jooflash", new LinkedList<String>(Arrays.asList("net.jangaroo:jangaroo-browser", "net.jangaroo:jooflash-native", "net.jangaroo:xmlhttprequest-as")));

    for (String s : artifact2directDependencies.keySet()) {
      all.add(s);
    }
    for (List<String> strings : artifact2directDependencies.values()) {
      for (String s : strings) {
        all.add(s);
      }
    }
    List<String> sorted = JooGenerateTestResourcesMojo.sort(new HashMap<String, List<String>>(artifact2directDependencies));
    assertFalse(all.addAll(sorted));

  }
}
