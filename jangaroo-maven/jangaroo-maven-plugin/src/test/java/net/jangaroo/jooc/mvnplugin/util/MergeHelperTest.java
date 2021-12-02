package net.jangaroo.jooc.mvnplugin.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class MergeHelperTest extends TestCase {

  public void testMergeMapIntoBaseMap() {
    Map<String, Object> baseMap = new HashMap<>(ImmutableMap.of(
            "hello", "world",
            "overridden", false,
            "js", Lists.newArrayList("one")
    ));
    Map<String, Object> additionalMap = ImmutableMap.of(
            "new", "value",
            "overridden", ImmutableList.of("yes"),
            "js", ImmutableList.of("two"),
            "css", ImmutableList.of("two")
    );
    MergeHelper.mergeMapIntoBaseMap(baseMap, additionalMap, new MergeHelper.MergeOptions(MergeHelper.ListStrategy.APPEND, MergeHelper.MapStrategy.MERGE));
    assertEquals(ImmutableMap.of(
            "hello", "world",
            "overridden", ImmutableList.of("yes"),
            "js", ImmutableList.of("one", "two"),
            "new", "value",
            "css", ImmutableList.of("two")
    ), baseMap);
  }
}