package net.jangaroo.jooc.json;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class JsonTest {

  @Test
  public void testJsonObject() {
    List<String> undeclaredDependencies = Arrays.asList("packages/my_package_1", "packages/my_package_2");
    List<String> unusedDependencies = Arrays.asList("packages/my_package_3", "packages/my_package_4");
    JsonObject jsonObject = new JsonObject(
            "undeclared", new JsonArray(undeclaredDependencies.toArray()),
            "unused", new JsonArray(unusedDependencies.toArray())
    );
    Assert.assertEquals("{\n" +
            "  undeclared: [\n" +
            "    \"packages/my_package_1\",\n" +
            "    \"packages/my_package_2\"\n" +
            "  ],\n" +
            "  unused: [\n" +
            "    \"packages/my_package_3\",\n" +
            "    \"packages/my_package_4\"\n" +
            "  ]\n" +
            "}", jsonObject.toString(2).replaceAll(JsonObject.LINE_SEPARATOR, "\n")); 
  }

  @Test
  public void testJsonObjectWithQuotedKeys() {
    List<String> undeclaredDependencies = Arrays.asList("packages/my_package_1", "packages/my_package_2");
    List<String> unusedDependencies = Arrays.asList("packages/my_package_3", "packages/my_package_4");
    JsonObject jsonObject = new JsonObject(
            "undeclared", new JsonArray(undeclaredDependencies.toArray()),
            "unused", new JsonArray(unusedDependencies.toArray())
    );
    Assert.assertEquals("{\n" +
            "  \"undeclared\": [\n" +
            "    \"packages/my_package_1\",\n" +
            "    \"packages/my_package_2\"\n" +
            "  ],\n" +
            "  \"unused\": [\n" +
            "    \"packages/my_package_3\",\n" +
            "    \"packages/my_package_4\"\n" +
            "  ]\n" +
            "}", jsonObject.stringify().replaceAll(JsonObject.LINE_SEPARATOR, "\n")); 
  }
}
