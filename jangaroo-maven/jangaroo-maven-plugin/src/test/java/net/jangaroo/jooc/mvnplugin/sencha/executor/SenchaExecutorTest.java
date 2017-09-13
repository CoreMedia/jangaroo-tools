package net.jangaroo.jooc.mvnplugin.sencha.executor;

import junit.framework.TestCase;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class SenchaExecutorTest extends TestCase {

  public void testParseVersion() throws Exception {
    assertArrayEquals(new int[]{6, 2, 2, 28}, SenchaCmdExecutor.parseVersion("   6.2.2.28"));
    assertArrayEquals(new int[]{7, 5, 3}, SenchaCmdExecutor.parseVersion("   7.5.3"));
    assertArrayEquals(new int[]{6, 2, 2, 28}, SenchaCmdExecutor.parseVersion("Sencha Cmd 6.2.2.28"));
    assertArrayEquals(new int[]{6, 5, 0}, SenchaCmdExecutor.parseVersion("Sencha Cmd v6.5.0"));
    try {
      SenchaCmdExecutor.parseVersion("Sencha Cmd 6.2"); // too short
      fail("Sencha Cmd version must have at least three parts.");
    } catch (IOException e) {
      // alright
    }
  }
}
