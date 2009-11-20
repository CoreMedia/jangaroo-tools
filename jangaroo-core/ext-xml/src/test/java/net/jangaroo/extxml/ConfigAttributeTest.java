/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ConfigAttributeTest {

  @Test
  public void testHashCode() {
    ConfigAttribute one = new ConfigAttribute("one","boolean");
    ConfigAttribute two = new ConfigAttribute("two","boolean");

    assertFalse(one.hashCode() == two.hashCode());

    ConfigAttribute two2 = new ConfigAttribute("two","boolean");

    assertTrue(two.hashCode() == two2.hashCode());

    Set<ConfigAttribute> set = new HashSet<ConfigAttribute>();
    set.add(one);
    set.add(two);
    set.add(two2);

    assertTrue(set.size() == 2);

  }

  @Test
  public void testEquals() {
    ConfigAttribute one = new ConfigAttribute("one","boolean");
    ConfigAttribute two = new ConfigAttribute("two","boolean");

    assertFalse(one.equals(two));

    ConfigAttribute two2 = new ConfigAttribute("two","boolean");

    assertTrue(two.equals(two2));
  }
}
