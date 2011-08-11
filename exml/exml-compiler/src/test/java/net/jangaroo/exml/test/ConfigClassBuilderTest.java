package net.jangaroo.exml.test;

import net.jangaroo.exml.as.ConfigClassBuilder;
import net.jangaroo.exml.model.ConfigAttribute;
import net.jangaroo.exml.model.ConfigClass;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.StdOutCompileLog;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
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
    ConfigClass configClass = buildConfigClass("/testNamespace/config/testComponent.as");
    Assert.assertEquals("testNamespace.config", configClass.getPackageName());
    Assert.assertEquals("testComponent", configClass.getName());
    Assert.assertEquals("testPackage.TestComponent", configClass.getComponentClassName());
    Assert.assertEquals("This is a TestComponent with panel as baseclass. <p>This class serves as a typed config object for constructor of the component class <code>testPackage.TestComponent</code>. Instantiating this class for the first time also registers the corresponding component class under the xtype \"testNamespace.config.testComponent\" with ExtJS.</p> @see testPackage.TestComponent", configClass.getDescription());
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
    Assert.assertEquals(new HashSet<String>(Arrays.asList("propertyOne", "propertyTwo", "propertyThree", "propertyFour")), attributeNames);
  }

  @Test
  public void testBuildNonConfigClass() throws Exception {
    ConfigClass configClass = buildConfigClass("/testNamespace/config/NonConfig.as");
    Assert.assertNull(configClass);
  }

  @Test(expected = CompilerError.class)
  public void testBadAnnotationParameter() throws Exception {
    buildConfigClass("/testNamespace/config/badConfig1.as");
  }

  @Test(expected = CompilerError.class)
  public void testMissingAnnotationParameter() throws Exception {
    buildConfigClass("/testNamespace/config/badConfig2.as");
  }

  private ConfigClass buildConfigClass(String resourceName) throws URISyntaxException {
    File sourceFile = new File(getClass().getResource(resourceName).toURI());
    InputSource inputSource = new FileInputSource(sourceFile);
    CompilationUnit compilationUnit = Jooc.doParse(inputSource, new StdOutCompileLog(), SemicolonInsertionMode.QUIRKS);
    ConfigClassBuilder configClassBuilder = new ConfigClassBuilder(compilationUnit);
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
