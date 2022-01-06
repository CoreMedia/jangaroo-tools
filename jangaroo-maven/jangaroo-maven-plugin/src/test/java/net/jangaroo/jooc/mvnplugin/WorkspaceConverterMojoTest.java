package net.jangaroo.jooc.mvnplugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import junit.framework.TestCase;
import net.jangaroo.jooc.mvnplugin.converter.JangarooConfig;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class WorkspaceConverterMojoTest extends TestCase {

  public void testUnquoteIdentifierKeys() {
    assertEquals("{\n  identifier: \"{value1}\",\n  \"non-identifier\": \"value2\"\n}", WorkspaceConverterMojo.unquoteIdentifierKeys("{\n  \"identifier\": \"{value1}\",\n  \"non-identifier\": \"value2\"\n}"));
  }

  public void testConvertJangarooConfig() throws JsonProcessingException {
    WorkspaceConverterMojo workspaceConverterMojo = new WorkspaceConverterMojo();

    JangarooConfig jangarooConfig = new JangarooConfig();
    jangarooConfig.setType("code");
    jangarooConfig.setAutoLoad(Arrays.asList("init.js", "config-init.js"));
    Map<String, Object> senchaConfig = new LinkedHashMap<>();
    senchaConfig.put("namespace", "com.acme.sencha");
    senchaConfig.put("custom-key", "${project.dir} some/other/path");
    jangarooConfig.setSencha(senchaConfig);
    assertEquals("{\n" +
            "  type: \"code\",\n" +
            "  sencha: {\n" +
            "    namespace: \"com.acme.sencha\",\n" +
            "    \"custom-key\": \"${project.dir} some/other/path\",\n" +
            "    \n" +
            "  },\n" +
            "  autoLoad: [\n" +
            "    \"init.js\",\n" +
            "    \"config-init.js\",\n" +
            "    \n" +
            "  ],\n" +
            "  \n" +
            "}",
            workspaceConverterMojo.getPrettyPrintedJangarooConfig(jangarooConfig)
                    .replace(System.getProperty("line.separator"), "\n")
    );
  }
}
