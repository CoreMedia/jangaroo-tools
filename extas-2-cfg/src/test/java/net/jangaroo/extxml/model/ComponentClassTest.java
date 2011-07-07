/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml.model;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ComponentClassTest {

  @Test
  public void testGetAllCfgs() {
    ComponentSuite suite = new ComponentSuite("test", "ts", null, null, null);

    ComponentClass cc = new ComponentClass("cc","TestClass");
    suite.addComponentClass(cc);
    cc.addCfg(new ConfigAttribute("one","boolean"));
    cc.addCfg(new ConfigAttribute("two","boolean"));
    cc.addCfg(new ConfigAttribute("two","boolean"));

    assertTrue(cc.getAllCfgs().size() == 2);


    ComponentClass supercc = new ComponentClass("supercc","SuperTestClass");
    suite.addComponentClass(supercc);
    supercc.addCfg(new ConfigAttribute("two","boolean"));

    cc.setSuperClassName("SuperTestClass");

    assertTrue(cc.getAllCfgs().size() == 2);

    supercc.addCfg(new ConfigAttribute("three","boolean"));

    assertTrue(cc.getAllCfgs().size() == 3);
  }
}
