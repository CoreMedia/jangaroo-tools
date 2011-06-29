package net.jangaroo.exml.as;

import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigClassBuilderTest {
  @Test
  public void testBuildConfigClass() throws Exception {
    ConfigClass configClass = buildConfigClass("/testNamespace/config/TestComponent.as");
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("TestComponent", configClass.getName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentName());
    Assert.assertEquals("This is a TestComponent with panel as baseclass. " +
            "<b>Do not edit. This is an auto-generated class.</b> " +
            "@see testPackage.TestComponent", configClass.getDescription());
    Set<String> attributeNames = new HashSet<String>();
    for (ConfigAttribute configAttribute : configClass.getCfgs()) {
      attributeNames.add(configAttribute.getName());
      if ("propertyOne".equals(configAttribute.getName())) {
        Assert.assertEquals("Boolean", configAttribute.getType());
        Assert.assertEquals("Some Boolean property", configAttribute.getDescription());
      }
      if ("propertyTwo".equals(configAttribute.getName())) {
        Assert.assertEquals("Number", configAttribute.getType());
        Assert.assertEquals("Some Number property", configAttribute.getDescription());
      }
    }
    Assert.assertEquals(new HashSet<String>(Arrays.asList("propertyOne", "propertyTwo")), attributeNames);
  }

  @Test
  public void testBuildNonConfigClass() throws Exception {
    ConfigClass configClass = buildConfigClass("/testNamespace/config/NonConfig.as");
    Assert.assertNull(configClass);
  }

  @Test(expected = Jooc.CompilerError.class)
  public void testBadAnnotationParameter() throws Exception {
    buildConfigClass("/testNamespace/config/BadConfig1.as");
  }

  @Test(expected = Jooc.CompilerError.class)
  public void testMissingAnnotationParameter() throws Exception {
    buildConfigClass("/testNamespace/config/BadConfig2.as");
  }

  private ConfigClass buildConfigClass(String resourceName) throws URISyntaxException {
    File sourceFile = new File(getClass().getResource(resourceName).toURI());
    InputSource inputSource = new FileInputSource(null, sourceFile);
    ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(inputSource);
    return configClassBuilder.buildConfigClass();
  }

  @Test
  public void testParseAsDoc() {
    Assert.assertEquals("text", ConfigClassBuilder.parseAsDocComment("text"));
    Assert.assertEquals("text", ConfigClassBuilder.parseAsDocComment("\r* text"));
    Assert.assertEquals("text text2", ConfigClassBuilder.parseAsDocComment("\r* text\r* text2"));
    Assert.assertEquals("text text2", ConfigClassBuilder.parseAsDocComment("\r* text\r*\r* text2"));
    Assert.assertEquals("text", ConfigClassBuilder.parseAsDocComment("\n* text"));
    Assert.assertEquals("text", ConfigClassBuilder.parseAsDocComment("\r\n* text"));
    Assert.assertEquals("text*", ConfigClassBuilder.parseAsDocComment("\n*text*"));
    Assert.assertEquals("te xt", ConfigClassBuilder.parseAsDocComment("te\nxt"));
    Assert.assertEquals("te xt", ConfigClassBuilder.parseAsDocComment("te\r\n      xt"));
  }
}
