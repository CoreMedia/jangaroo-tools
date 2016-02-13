package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DefaultSenchaApplicationConfigurerTest {

  @Test
  public void testConfigure() throws Exception {
    Map<String, Object> config = new HashMap<String, Object>();
    DefaultSenchaApplicationConfigurer configurer = new DefaultSenchaApplicationConfigurer();
    configurer.configure(config);
    assertNotNull(config.get(DefaultSenchaApplicationConfigurer.ID));
  }
}