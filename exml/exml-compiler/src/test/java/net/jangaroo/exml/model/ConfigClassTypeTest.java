package net.jangaroo.exml.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ConfigClassTypeTest {
   @Test
    public void testPlugin() {
     ConfigClassType plugin = ConfigClassType.valueOf("PLUGIN");
     assertEquals(ConfigClassType.PLUGIN, plugin);
     assertEquals("ptype", plugin.getExtTypeAttribute());
   }
}
