package net.jangaroo.dependencies;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CyclicRequiredDependencies {

  public static final String PATH_PREFIX = "/target/classes/META-INF/resources/joo/classes/";
  public static final String PATH_SUFFIX = ".js";

  public static void main(String[] args) throws IOException {
    String baseDir = args[0];
    List<File> files = new ArrayList<>();
    fillInCompiledClassFiles(new File(baseDir), files);

    Multimap<String,String> requires = HashMultimap.create();
    for (File file : files) {
      fillInRequires(file, requires);
    }

    Set<String> classesInCycles = getNodesInCycles(requires);
    requires.keySet().retainAll(classesInCycles);
    requires.values().retainAll(classesInCycles);

    File outFile = new File(args[1]);
    try (PrintWriter writer = new PrintWriter(new FileWriter(outFile))) {
      writeGraph(writer, requires);
    }
  }

  private static void fillInCompiledClassFiles(File baseDir, List<File> files) {
    for (File file : baseDir.listFiles()) {
      if (file.isDirectory()) {
        fillInCompiledClassFiles(file, files);
      } else {
        String path = file.getPath();
        if (path.contains(PATH_PREFIX) && path.endsWith(PATH_SUFFIX)) {
          files.add(file);
        }
      }
    }
  }

  private static void fillInRequires(File file, Multimap<String, String> requires) throws IOException {
    String path = file.getPath();
    String className = path
            .substring(path.indexOf(PATH_PREFIX) + PATH_PREFIX.length(), path.length() - PATH_SUFFIX.length())
            .replace('/', '.');
    List<String> lines = Files.readLines(file, Charsets.UTF_8);

    int lastBrace = -1;
    for (int i = 0; i < lines.size(); i++) {
      if (lines.get(i).indexOf('}') != -1) {
        lastBrace = i;
      }
    }
    if (lastBrace == -1) {
      return;
    }
    String descriptorLine = lines.get(lastBrace);

    int startPos = descriptorLine.lastIndexOf('[');
    int endPos = descriptorLine.lastIndexOf(']');
    String dependenciesString = descriptorLine.substring(startPos + 1, endPos);
    String[] dependencies = dependenciesString.split("[\",]+");
    for (String dependency : dependencies) {
      if (!dependency.isEmpty()) {
        requires.put(className, dependency);
      }
    }
  }

  private static Set<String> getNodesInCycles(Multimap<String, String> edges) {
    Multiset<String> classNames = edges.keys();
    Set<String> result = new HashSet<>();
    for (String className : classNames) {
      if (getAllSuccessors(className, edges).contains(className)) {
        result.add(className);
      }
    }
    return result;
  }

  private static Set<String> getAllSuccessors(String nodeId, Multimap<String, String> edges) {
    Set<String> result = new HashSet<>();
    fillInSuccessors(nodeId, edges, result);
    return result;
  }

  private static void fillInSuccessors(String nodeId, Multimap<String, String> edges, Set<String> result) {
    Collection<String> successors = edges.get(nodeId);
    for (String successor : successors) {
      if (result.add(successor)) {
        fillInSuccessors(successor, edges, result);
      }
    }
  }

  private static void writeGraph(PrintWriter writer, Multimap<String, String> requires) {
    writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:java=\"http://www.yworks.com/xml/yfiles-common/1.0/java\" xmlns:sys=\"http://www.yworks.com/xml/yfiles-common/markup/primitives/2.0\" xmlns:x=\"http://www.yworks.com/xml/yfiles-common/markup/2.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xmlns:yed=\"http://www.yworks.com/xml/yed/3\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\">");
    writer.println("<key for=\"node\" id=\"d6\" yfiles.type=\"nodegraphics\"/>");
    for (String nodeId : requires.keys()) {
      writer.println("    <node id=\"" + nodeId + "\">");
      writer.println("      <data key=\"d6\">");
      writer.println("        <y:ShapeNode>");
      writer.println("          <y:Geometry height=\"20.0\" width=\"" + 8 * nodeId.length() + "\" x=\"765.099609375\" y=\"-442.9166666666667\"/>\n");
      writer.println("          <y:NodeLabel>" + nodeId + "</y:NodeLabel>");
      writer.println("        </y:ShapeNode>");
      writer.println("      </data>");
      writer.println("    </node>");
    }
    for (Map.Entry<String, String> entry : requires.entries()) {
      writer.println("    <edge source=\"" + entry.getKey() + "\" target=\"" + entry.getValue() + "\"/>");
    }
    writer.println("</graphml>");
  }
}
