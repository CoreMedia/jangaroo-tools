package net.jangaroo.exml.tools;

import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.StdOutCompileLog;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.backend.ApiModelGenerator;
import net.jangaroo.jooc.config.ParserOptions;
import net.jangaroo.jooc.config.SemicolonInsertionMode;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;
import net.jangaroo.jooc.model.CompilationUnitModel;
import net.jangaroo.jooc.model.CompilationUnitModelRegistry;
import net.jangaroo.jooc.util.MessageFormat;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A tool to getParser a given Ext AS API and build a model of it.
 */
public class ExtAsApi {

  private final ApiModelGenerator apiModelGenerator;
  private JangarooParser jangarooParser;
  private CompilationUnitModelRegistry compilationUnitModelRegistry;

  public ExtAsApi(String jangarooRuntimeVersion, String jangarooLibsVersion) {
    jangarooParser = getParser(jangarooRuntimeVersion, jangarooLibsVersion);
    compilationUnitModelRegistry = new CompilationUnitModelRegistry();
    apiModelGenerator = new ApiModelGenerator(false);
  }

  private static JangarooParser getParser(String jangarooRuntimeVersion, String jangarooLibsVersion) {
    return getParser(Arrays.asList(
                    getMavenArtifact("net.jangaroo", "jangaroo-runtime", jangarooRuntimeVersion, null),
                    getMavenArtifact("net.jangaroo", "jangaroo-browser", jangarooLibsVersion, null)
            ),
            getMavenArtifact("net.jangaroo", "ext-as", jangarooLibsVersion, "sources"),
            new StdOutCompileLog());
  }

  public CompilationUnitModel getCompilationUnitModel(String qName) {
    CompilationUnitModel compilationUnitModel = compilationUnitModelRegistry.resolveCompilationUnit(qName);
    if (compilationUnitModel == null) {
      CompilationUnit compilationUnit = jangarooParser.getCompilationUnit(qName);
      if (compilationUnit != null && compilationUnit.getSource().isInSourcePath()) {
        compilationUnit.analyze(null);
        try {
          compilationUnitModel = apiModelGenerator.generateModel(compilationUnit);
          compilationUnitModelRegistry.register(compilationUnitModel);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return compilationUnitModel;
  }

  private boolean isQualifiedName(String qName) {
    return compilationUnitModelRegistry.resolveCompilationUnit(qName) != null ||
            jangarooParser.getCompilationUnit(qName) != null;
  }

  public String resolveQualifiedName(CompilationUnitModel context, String name) {
    if (name == null) {
      return null;
    }
    if (name.contains(".")) {
      return name;
    }
    // try imports:
    List<String> imports = new ArrayList<String>(context.getImports());
    // same package is an implicit import:
    if (context.getPackage().length() > 0) {
      imports.add(context.getPackage() + ".*");
    }
    // all top-level classes are imported implicitly:
    imports.add("*");

    for (String anImport : imports) {
      String unqualified = CompilerUtils.className(anImport);
      if (unqualified.equals("*")) {
        String qName = CompilerUtils.qName(CompilerUtils.packageName(anImport), name);
        if (isQualifiedName(qName)) {
          return qName;
        }
      } else if (unqualified.equals(name)) {
        return anImport;
      }
    }
    return name;
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
