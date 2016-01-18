package net.jangaroo.jooc.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphUtilTest {

  @Test
  public void testKosarajuSortEmpty() throws Exception {
    Map<String, Set<String>> graph = graph();
    List<String> sorted = kosarajuSort(graph);
    Assert.assertEquals(Collections.emptyList(), sorted);
  }

  @Test
  public void testKosarajuSortSingle() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a"));
    List<String> sorted = kosarajuSort(graph);
    Assert.assertEquals(Collections.singletonList("a"), sorted);
  }

  @Test
  public void testKosarajuSortSimple() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b"));
    List<String> sorted = kosarajuSort(graph);
    Assert.assertEquals(Arrays.asList("a", "b"), sorted);
  }

  @Test
  public void testKosarajuSortChain() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b", "c"), successors("c"));
    List<String> sorted = kosarajuSort(graph);
    Assert.assertEquals(Arrays.asList("a", "b", "c"), sorted);
  }

  @Test
  public void testKosarajuSortCycle() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b", "c"), successors("c", "a"));
    List<String> sorted = kosarajuSort(graph);
    Assert.assertEquals(new HashSet<String>(Arrays.asList("a", "b", "c")),
            new HashSet<String>(sorted));
  }

  @Test
  public void testKosarajuSortTadpole() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b", "c"), successors("c", "b"));
    List<String> sorted = kosarajuSort(graph);
    Assert.assertEquals("a", sorted.get(0));
    Assert.assertEquals(new HashSet<String>(Arrays.asList("b", "c")), new HashSet<String>(sorted.subList(1, 3)));
  }

  @Test
  public void testKosarajuSortInverseTadpole() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b", "a", "c"), successors("c"));
    List<String> sorted = kosarajuSort(graph);
    Assert.assertEquals(new HashSet<String>(Arrays.asList("a", "b")), new HashSet<String>(sorted.subList(0, 2)));
    Assert.assertEquals("c", sorted.get(2));
  }

  private List<String> kosarajuSort(Map<String, Set<String>> graph) {
    return new ArrayList<String>(GraphUtil.kosarajuSort(graph));
  }

  @Test
  public void testStronglyConnectedComponentEmpty() throws Exception {
    Map<String, Set<String>> graph = graph();
    Collection<Set<String>> partitioned = stronglyConnectedComponent(graph);
    Assert.assertEquals(set(), partitioned);
  }

  @Test
  public void testStronglyConnectedComponentSingle() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a"));
    Set<Set<String>> partitioned = stronglyConnectedComponent(graph);
    Assert.assertEquals(set(set("a")), partitioned);
  }

  @Test
  public void testStronglyConnectedComponentSimple() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b"));
    Collection<Set<String>> partitioned = stronglyConnectedComponent(graph);
    Assert.assertEquals(set(set("a"), set("b")), partitioned);
  }

  @Test
  public void testStronglyConnectedComponentChain() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b", "c"), successors("c"));
    Collection<Set<String>> partitioned = stronglyConnectedComponent(graph);
    Assert.assertEquals(set(set("a"), set("b"), set("c")), partitioned);
  }

  @Test
  public void testStronglyConnectedComponentCycle() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b", "c"), successors("c", "a"));
    Collection<Set<String>> partitioned = stronglyConnectedComponent(graph);
    Assert.assertEquals(set(set("a", "b", "c")), partitioned);
  }

  @Test
  public void testStronglyConnectedComponentTadpole() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b", "c"), successors("c", "b"));
    Collection<Set<String>> partitioned = stronglyConnectedComponent(graph);
    Assert.assertEquals(set(set("a"), set("b", "c")), partitioned);
  }

  @Test
  public void testStronglyConnectedComponentInverseTadpole() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b"), successors("b", "a", "c"), successors("c"));
    Collection<Set<String>> partitioned = stronglyConnectedComponent(graph);
    Assert.assertEquals(set(set("a", "b"), set("c")), partitioned);
  }

  @Test
  public void testStronglyConnectedComponentComplex() throws Exception {
    Map<String, Set<String>> graph = graph(
            successors("a", "b", "c", "d"),
            successors("b", "c", "e"),
            successors("c", "d", "e"),
            successors("d", "b", "e"),
            successors("e"));
    Collection<Set<String>> partitioned = stronglyConnectedComponent(graph);
    Assert.assertEquals(set(set("a"), set("b", "c", "d"), set("e")), partitioned);
  }

  private Set<Set<String>> stronglyConnectedComponent(Map<String, Set<String>> graph) {
    return new HashSet<Set<String>>(GraphUtil.stronglyConnectedComponent(graph));
  }

  @Test
  public void testNoCycle() throws Exception {
    Map<String, Set<String>> graph = graph(
            successors("a", "b"));
    List<String> cycle = GraphUtil.findCycle(graph, "a");
    Assert.assertNull(cycle);
  }

  @Test
  public void testCycleSimple() throws Exception {
    Map<String, Set<String>> graph = graph(
            successors("a", "b"),
            successors("b", "a"));
    List<String> cycle = GraphUtil.findCycle(graph, "a");
    Assert.assertEquals(Arrays.asList("a", "b"), cycle);
  }

  @Test
  public void testCycleComplex() throws Exception {
    Map<String, Set<String>> graph = graph(
            successors("a", "b", "c", "d"),
            successors("b", "c", "e"),
            successors("c", "d", "e"),
            successors("d", "b", "e"),
            successors("e"));
    List<String> cycle = GraphUtil.findCycle(graph, "c");
    Assert.assertEquals(Arrays.asList("c", "d", "b"), cycle);
  }

  private <T> Set<T> set(T ... nodes) {
    return new HashSet<T>(Arrays.asList(nodes));
  }

  private Map<String, Set<String>> graph(Map<String, Set<String>> ... parts) {
    Map<String, Set<String>> result = new HashMap<String, Set<String>>();
    for (Map<String, Set<String>> part : parts) {
      result.putAll(part);
    }
    return result;
  }
  
  private Map<String, Set<String>> successors(String node, String ... successors) {
    return Collections.<String,Set<String>>singletonMap(node, new HashSet<String>(Arrays.asList(successors)));
  }

  @Test
  public void testReverse() throws Exception {
    Map<String, Set<String>> graph = graph(successors("a", "b", "c"), successors("b"), successors("c"));
    Map<String, Set<String>> reversed = GraphUtil.reverse(graph);
    Assert.assertEquals(graph(successors("a"), successors("b", "a"), successors("c", "a")), reversed);
  }
}