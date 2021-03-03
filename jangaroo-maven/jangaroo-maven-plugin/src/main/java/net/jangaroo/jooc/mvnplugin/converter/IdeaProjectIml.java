package net.jangaroo.jooc.mvnplugin.converter;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class IdeaProjectIml {
  private List<String> excludePaths;
  private String projectIml;
  private String studioNpmTarget;
  private File projectImlPath;


  public IdeaProjectIml(String studioNpmTarget, File projectImlPath) {
    this.studioNpmTarget = studioNpmTarget;
    this.projectImlPath = projectImlPath;
    this.excludePaths = new ArrayList<>();
    StringJoiner stringJoiner = new StringJoiner("\n");
    stringJoiner.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    stringJoiner.add("<module type=\"WEB_MODULE\" version=\"4\">");
    stringJoiner.add("  <component name=\"NewModuleRootManager\" inherit-compiler-output=\"true\">");
    stringJoiner.add("    <exclude-output />");
    stringJoiner.add("    <content url=\"file://$MODULE_DIR$\">");
    stringJoiner.add("%s");
    stringJoiner.add("    </content>");
    stringJoiner.add("  </component>");
    stringJoiner.add("</module>");
    stringJoiner.add("");
    this.projectIml = stringJoiner.toString();
  }

  public void readProjectIml() {
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    SAXParser saxParser = null;
    try {
      saxParser = saxParserFactory.newSAXParser();
      IdeaProjectImlHandler ideaProjectImlHandler = new IdeaProjectImlHandler();
      saxParser.parse(projectImlPath, ideaProjectImlHandler);
      this.excludePaths = ideaProjectImlHandler.getExcludePaths();
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  public void writeProjectIml(List<String> excludeFolderPaths) {
    if (projectImlPath.exists()) {
      readProjectIml();
    }
    StringJoiner stringJoiner = new StringJoiner("\n");
    excludeFolderPaths.stream()
            .map(path -> Paths.get(studioNpmTarget).relativize(Paths.get(path)).toString())
            .filter(path -> !this.excludePaths.contains(path))
            .forEach(excludePaths::add);
    this.excludePaths.stream()
            .sorted(Comparator.naturalOrder())
            .map(path -> String.format("      <excludeFolder url=\"file://$MODULE_DIR$/%s\" />", path))
            .forEach(stringJoiner::add);
    try {
      FileUtils.write(projectImlPath, String.format(projectIml, stringJoiner.toString()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
