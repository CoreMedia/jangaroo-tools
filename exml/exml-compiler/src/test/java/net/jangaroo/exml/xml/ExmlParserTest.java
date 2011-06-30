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
  public void testParseAllElements() throws Exception{
    FileInputSource sourcePathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    FileInputSource classpathInputSource = new FileInputSource(new File(getClass().getResource("/").toURI()));
    ConfigClassRegistry registry = new ConfigClassRegistry(sourcePathInputSource, classpathInputSource, "exmlparser.config", outputFolder.getRoot());
    ExmlParser exmlParser = new ExmlParser(registry);

    InputStream inputStream = getClass().getResourceAsStream("/exmlparser/AllElements.exml");
    ExmlModel model = exmlParser.parse(inputStream);
    Assert.assertEquals(new HashSet<String>(Arrays.asList("ext.config.panel", "ext.config.button", "ext.config.menuitem")),
            model.getImports());
    Assert.assertEquals("ext.config.panel", model.getParentClassName());

    JsonObject expectedJsonObject = new JsonObject(
            "layout", "{config.myLayout}",
            "title", "I am a panel",
            "defaults", new JsonObject("layout","border"),
            "layoutConfig", new JsonObject(
                    "bla", "blub",
                    "anchor", new JsonObject("style", "test"),
                    "border", new JsonObject("type", "solid")
            ),
            "items", new JsonArray(
                    new JsonObject(
                            "text", "Save"
                    ).settingWrapperClass("ext.config.button"),
                    "{{xtype: \"editortreepanel\"}}"
            ),
            "menu", new JsonArray(
                    new JsonObject(
                            "text", "juhu1"
                    ).settingWrapperClass("ext.config.menuitem"),
                    new JsonObject(
                            "text", "juhu2"
                    ).settingWrapperClass("ext.config.menuitem"),
                    new JsonObject(
                            "text", "juhu3"
                    ).settingWrapperClass("ext.config.menuitem")
            ),
            "tools", new JsonArray(
                     new JsonObject(
                         "handler", "{function(x){return ''+x;}}",
                         "id", "gear"
                     )
            )
    );
    Assert.assertEquals(expectedJsonObject.toString(2), model.getJsonObject().toString(2));
  }
}
