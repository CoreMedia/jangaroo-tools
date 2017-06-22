package net.jangaroo.jooc.mvnplugin.sencha.executor;

import junit.framework.TestCase;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class SenchaExecutorTest extends TestCase {

  public void testParseVersion() throws Exception {
    assertArrayEquals(new int[]{6, 2, 2, 28}, SenchaCmdExecutor.parseVersion("   6.2.2.28"));
    assertArrayEquals(new int[]{7, 5, 3}, SenchaCmdExecutor.parseVersion("   7.5.3"));
    try {
      SenchaCmdExecutor.parseVersion("6.2"); // too short
      fail("Sencha Cmd version must have at least three parts.");
    } catch (IOException e) {
      // alright
    }
    try {
      SenchaCmdExecutor.parseVersion("6.2.5a"); // non-numeric
      fail("Sencha Cmd version must contain only dot-separated numbers.");
    } catch (IOException e) {
      // alright
    }
  }
}
