package net.jangaroo.jooc.mvnplugin.test;

import net.jangaroo.jooc.mvnplugin.WorkspaceConverterMojo;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

public class WorkspaceConverterMojoTest {

  @Test
  public void test() throws Exception {
    File file = new File("/home/fwellers/dev/cms/apps/studio-client/pom.xml");

    WorkspaceConverterMojo workspaceConverterMojo = new WorkspaceConverterMojo();
    HashMap<String, String> pluginContext = new HashMap<>();
    pluginContext.put("studio.npm.maven.root", "/home/fwellers/dev/cms/apps/studio-client/pom.xml");
    workspaceConverterMojo.setPluginContext(pluginContext);
    workspaceConverterMojo.execute();

  }
}
