package net.jangaroo.jooc.mvnplugin;

import junit.framework.TestCase;

public class WorkspaceConverterMojoTest extends TestCase {
  public void testConvertJangarooConfig() {
    assertEquals("{\n  hello: \"{world}\",\n}", WorkspaceConverterMojo.convertJangarooConfig("{\n  \"hello\": \"{world}\"\n}"));
  }
}
