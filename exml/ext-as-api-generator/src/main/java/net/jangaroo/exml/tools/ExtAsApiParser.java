package net.jangaroo.exml.tools;

import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.StdOutCompileLog;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.config.ParserOptions;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;
import net.jangaroo.jooc.util.MessageFormat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A tool to getParser a given Ext AS API and build a model of it.
 */
public class ExtAsApiParser {

  public static JangarooParser getParser(String jangarooRuntimeVersion, String jangarooLibsVersion) {
    return getParser(Arrays.asList(
                    getMavenArtifact("net.jangaroo", "jangaroo-runtime", jangarooRuntimeVersion, null),
                    getMavenArtifact("net.jangaroo", "jangaroo-browser", jangarooLibsVersion, null)
            ),
            getMavenArtifact("net.jangaroo", "ext-as", jangarooLibsVersion, "sources"),
            new StdOutCompileLog());
  }

  private static File getMavenArtifact(String groupId, String artifactId, String version, String classifier) {
    String mavenRepository = System.getenv("HOME") + "/.m2/repository/";
    return new File(mavenRepository + groupId.replace('.', '/') + new MessageFormat("/{0}/{1}/{0}-{2}.jar")
            .format(artifactId, version, classifier == null ? version : version + "-" + classifier));
  }

  private static JangarooParser getParser(List<File> classPath, File sourceRoot, CompileLog log) {
    JangarooParser jangarooParser = new JangarooParser(new ParserOptions() {
      @Override
      public SemicolonInsertionMode getSemicolonInsertionMode() {
        return SemicolonInsertionMode.ERROR;
      }

      @Override
      public boolean isVerbose() {
        return false;
      }
    }, log);
    InputSource sourcePathInputSource;
    InputSource classPathInputSource;
    try {
      sourcePathInputSource = PathInputSource.fromFiles(Collections.singletonList(sourceRoot), new String[]{""}, true);
      classPathInputSource = PathInputSource.fromFiles(classPath, new String[]{"", JangarooParser.JOO_API_IN_JAR_DIRECTORY_PREFIX}, false);
    } catch (IOException e) {
      throw new CompilerError("IO Exception occurred", e);
    }
    jangarooParser.setUp(sourcePathInputSource, classPathInputSource);

    //parseDirectory(jangarooParser, sourcePathInputSource.getChild("ext"));
    return jangarooParser;
  }

  private static void parseDirectory(JangarooParser jangarooParser, InputSource sourcePathInputSource) {
    for (InputSource inputSource : sourcePathInputSource.list()) {
      if (inputSource.isDirectory()) {
        parseDirectory(jangarooParser, inputSource);
      } else {
        CompilationUnit compilationUnit = jangarooParser.importSource(inputSource);
        System.out.println("Parsed: " + compilationUnit.getPrimaryDeclaration().getQualifiedNameStr());
      }
    }
  }

  public static void main(String[] argv) {
    getParser(argv[0], argv[1]);
  }
}
