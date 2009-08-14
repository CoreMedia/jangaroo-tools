/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.mojo;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 *
 */
public class JooUnitMojoTest extends AbstractMojoTestCase {

  protected void setUp() throws Exception {
    // required for mojo lookups to work
    super.setUp();
  }

  protected void tearDown()
      throws Exception {
    // required
    super.tearDown();
  }


  /**
   * tests the proper discovery and configuration of the mojo
   *
   * @throws Exception
   */
  public void testJooUnitMojoTestEnvironment() throws Exception {
    File testPom = new File(getBasedir(), "/target/test-classes/joounit-config.xml");
    JooUnitMojo mojo = (JooUnitMojo) lookupMojo("test", testPom);
    assertNotNull(mojo);
  }
}
