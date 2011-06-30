package net.jangaroo.exml.xml;

import net.jangaroo.exml.json.JsonArray;
import net.jangaroo.exml.json.JsonObject;
import net.jangaroo.exml.model.ConfigClassRegistry;
import net.jangaroo.exml.model.ExmlModel;
import net.jangaroo.jooc.input.FileInputSource;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;

public class ExmlParserTest {
  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();

  @Test
  public void testParseTestComponent() throws Exception{
    FileInputSource sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    FileInputSource classpathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    ConfigClassRegistry registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, "testNamespace.config", outputFolder.getRoot());
    ExmlParser exmlParser = new ExmlParser(registry);

    InputStream inputStream = getClass().getResourceAsStream("/testPackage/TestComponent.exml");
    ExmlModel model = exmlParser.parse(inputStream);
    Assert.assertEquals(new HashSet<String>(Arrays.asList("ext.config.panel", "ext.config.label")),
            model.getImports());
    Assert.assertEquals("ext.config.panel", model.getParentClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "title", "I am inside a package!",
            "items", new JsonArray(
                    new JsonObject(
                            "text", "bla"
                    ).settingWrapperClass("ext.config.label")
            )
    );
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }
}
