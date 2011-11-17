package net.jangaroo.exml.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ConfigClassTypeTest {
   @Test
    public void testPlugin() {
     ConfigClassType plugin = ConfigClassType.fromExtConfigAttribute("ptype");
     assertEquals(ConfigClassType.PTYPE, plugin);
     assertEquals("ptype", plugin.getExtTypeAttribute());
     assertEquals("ptype", plugin.getType());

     plugin = ConfigClassType.fromExtConfigAttribute("gctype");
     assertEquals(ConfigClassType.GCTYPE, plugin);
     assertEquals("xtype", plugin.getExtTypeAttribute());
     assertEquals("gctype", plugin.getType());
   }
}
