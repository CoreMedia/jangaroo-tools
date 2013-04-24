package net.jangaroo.jooc.config;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 * Test CompilerConfigParser.
 */
public class CompilerConfigParserTest {

  @Test
  public void testParse() {
    JoocConfiguration joocConfiguration = new JoocConfiguration();
    InputStream compilerConfig = getClass().getResourceAsStream("/config.xml");
    new CompilerConfigParser(joocConfiguration).parse(compilerConfig);
    Assert.assertEquals(joocConfiguration.getNamespaces().size(), 2);

    NamespaceConfiguration expectedNamespace1 = joocConfiguration.getNamespaces().get(0);
    Assert.assertEquals("exml:ext.config", expectedNamespace1.getUri());
    Assert.assertEquals("manifest.xml", expectedNamespace1.getManifest().replace(File.separatorChar, '/'));

    NamespaceConfiguration expectedNamespace2 = joocConfiguration.getNamespaces().get(1);
    Assert.assertEquals("exml:com.acme.config", expectedNamespace2.getUri());
    Assert.assertEquals("com/acme/config/manifest.xml", expectedNamespace2.getManifest().replace(File.separatorChar, '/'));
  }

}
