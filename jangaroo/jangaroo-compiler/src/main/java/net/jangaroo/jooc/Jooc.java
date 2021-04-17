/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompilationResult;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.TransitiveAstVisitor;
import net.jangaroo.jooc.backend.CompilationUnitSink;
import net.jangaroo.jooc.backend.CompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.JsCodeGenerator;
import net.jangaroo.jooc.backend.MergedOutputCompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.SingleFileCompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.TypeScriptCodeGenerator;
import net.jangaroo.jooc.backend.TypeScriptModuleResolver;
import net.jangaroo.jooc.cli.CommandLineParseException;
import net.jangaroo.jooc.cli.JoocCommandLineParser;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.config.NamespaceConfiguration;
import net.jangaroo.jooc.config.PublicApiViolationsMode;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;
import net.jangaroo.jooc.input.ZipEntryInputSource;
import net.jangaroo.jooc.mxml.CatalogComponentsParser;
import net.jangaroo.jooc.mxml.CatalogGenerator;
import net.jangaroo.jooc.mxml.ComponentPackageManifestParser;
import net.jangaroo.jooc.mxml.ComponentPackageModel;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Jangaroo AS3-to-JS Compiler's main class.
 *
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class Jooc extends JangarooParser implements net.jangaroo.jooc.api.Jooc {

  public static final String PUBLIC_API_EXCLUSION_ANNOTATION_NAME = "ExcludeClass";
  public static final String PUBLIC_API_INCLUSION_ANNOTATION_NAME = "PublicApi";
  public static final String DEPRECATED_ANNOTATION_NAME = "Deprecated";
  public static final String NATIVE_ANNOTATION_NAME = "Native";
  public static final String NATIVE_ANNOTATION_REQUIRE_PROPERTY = "require";
  public static final String RENAME_ANNOTATION_NAME = "Rename";
  public static final String MIXIN_ANNOTATION_NAME = "Mixin";
  public static final String MIXIN_HOOK_ANNOTATION_NAME = "MixinHook";
  public static final String MIXIN_HOOK_ANNOTATION_EXTENDED_ATTRIBUTE_NAME = "extended";
  public static final String MIXIN_HOOK_ANNOTATION_DEFAULT_ATTRIBUTE_NAME = "on";
  public static final Set<String> MIXIN_HOOK_ANNOTATION_ATTRIBUTE_NAMES = new HashSet<>(Arrays.asList("before", "after",
          MIXIN_HOOK_ANNOTATION_DEFAULT_ATTRIBUTE_NAME, MIXIN_HOOK_ANNOTATION_EXTENDED_ATTRIBUTE_NAME));
  public static final String BINDABLE_ANNOTATION_NAME = "Bindable";
  public static final String EXT_CONFIG_ANNOTATION_NAME = "ExtConfig";
  public static final String EXT_PRIVATE_ANNOTATION_NAME = "ExtPrivate";
  public static final String EMBED_ANNOTATION_NAME = "Embed";
  public static final String EMBED_ANNOTATION_SOURCE_PROPERTY = "source";
  public static final String RESOURCE_BUNDLE_ANNOTATION_NAME = "ResourceBundle";
  public static final String ARRAY_ELEMENT_TYPE_ANNOTATION_NAME = "ArrayElementType";
  public static final String EVENT_ANNOTATION_NAME = "Event";
  public static final String EVENT_ANNOTATION_NAME_ATTRIBUTE_NAME = "name";
  public static final String LAZY_ANNOTATION_NAME = "Lazy";
  public static final String PARAMETER_ANNOTATION_NAME = "Parameter";
  public static final String PARAMETER_ANNOTATION_REQUIRED_PROPERTY = "required";
  public static final String PARAMETER_ANNOTATION_COERCE_TO_PROPERTY = "coerceTo";
  public static final String COERCE_TO_VALUE_PROPERTIES_CLASS = "PropertiesClass";
  public static final String COERCE_TO_VALUE_KEYOF_PREFIX = "keyof ";
  public static final String RETURN_ANNOTATION_NAME = "Return";

  public static final List<String> ANNOTATIONS_FOR_COMPILER_ONLY = Arrays.asList(
          Jooc.NATIVE_ANNOTATION_NAME,
          Jooc.RENAME_ANNOTATION_NAME,
          Jooc.EMBED_ANNOTATION_NAME,
          Jooc.BINDABLE_ANNOTATION_NAME,
          Jooc.ARRAY_ELEMENT_TYPE_ANNOTATION_NAME,
          Jooc.EXT_CONFIG_ANNOTATION_NAME,
          Jooc.RESOURCE_BUNDLE_ANNOTATION_NAME,
          Jooc.MIXIN_ANNOTATION_NAME,
          Jooc.MIXIN_HOOK_ANNOTATION_NAME,
          Jooc.EXT_PRIVATE_ANNOTATION_NAME,
          Jooc.PUBLIC_API_INCLUSION_ANNOTATION_NAME,
          Jooc.PUBLIC_API_EXCLUSION_ANNOTATION_NAME,
          Jooc.DEPRECATED_ANNOTATION_NAME,
          Jooc.EVENT_ANNOTATION_NAME,
          Jooc.LAZY_ANNOTATION_NAME,
          Jooc.PARAMETER_ANNOTATION_NAME,
          Jooc.RETURN_ANNOTATION_NAME
  );
  public static final String TS_SUFFIX = ".ts";
  public static final String D_TS_SUFFIX = ".d" + TS_SUFFIX;

  private static final String TEST_DEPENDENCY_NAME_PREFIX = "test-";
  private static final String USED_UNDECLARED_DEPENDENCIES_WARNING = "Used undeclared %sdependencies found:";
  private static final String UNDECLARED_DEPENDENCY_USED_BY_FORMAT = "Undeclared %sdependency %s was used by:";
  private static final String UNUSED_DECLARED_DEPENDENCIES_WARNING = "Unused declared dependencies found:";

  private final DependencyWarningsManager dependencyWarningsManager;

  public static String getOutputSuffix(boolean isMigrateToTypeScript) {
    return isMigrateToTypeScript ? TS_SUFFIX : OUTPUT_FILE_SUFFIX;
  }

  private final List<FileInputSource> compileQueue = new ArrayList<>();

  public Jooc() {
    this(new JoocConfiguration());
  }

  public Jooc(JoocConfiguration config) {
    this(config, new StdOutCompileLog());
  }

  public Jooc(JoocConfiguration config, CompileLog log) {
    super(config, log);
    dependencyWarningsManager = new DependencyWarningsManager();
  }

  @Override
  public JoocConfiguration getConfig() {
    return (JoocConfiguration) super.getConfig();
  }

  @Override
  public void setConfig(JoocConfiguration config) {
    super.setConfig(config);
  }

  public String getOutputSuffix() {
    return getOutputSuffix(getConfig().isMigrateToTypeScript());
  }

  @Override
  public CompilationResult run() {
    try {
      return run1();
    } catch (CompilerError e) {
      logCompilerError(e);
      return new CompilationResultImpl(CompilationResult.RESULT_CODE_COMPILATION_FAILED);
    } catch (Exception e) {
      e.printStackTrace(); // NOSONAR something serious happened and we cannot get it into the ordinary log
      logCompilerError(e);
      return new CompilationResultImpl(CompilationResult.RESULT_CODE_INTERNAL_COMPILER_ERROR);
    }
  }

  private void logCompilerError(Throwable e) {
    boolean causedBy = false;
    for (Throwable current = e; current != null; current = current.getCause()) {
      String message = current.getMessage();
      if (causedBy) {
        message = "Caused by: " + message;
      }
      if (current instanceof CompilerError && ((CompilerError) current).getSymbol() != null) {
        log.error(((CompilerError) current).getSymbol(), message);
      } else {
        log.error(message);
      }
      causedBy = true;
    }
  }

  private void findUnusedDependencies(CompilationUnit compilationUnit, InputSource classPathInputSource) {
    dependencyWarningsManager.loadInputSource(classPathInputSource);
    dependencyWarningsManager.updateUsedCompileDependencies(compilationUnit.getRuntimeDependencies().stream()
            .map(this::findSource)
            .map(dependencyWarningsManager::convertInputSourceToDependency)
            .distinct()
            .collect(Collectors.toList()));
  }

  private static final List<String> IGNORE_DEPENDENCIES = Arrays.asList(
          "Boolean.as",
          "String.as",
          "Number.as",
          "Object.as",
          "RegExp.as",
          "int",
          "uint",
          "Date.as",
          "Array.as",
          "Error.as",
          "Vector.as",
          "Class.as",
          "Function.as",
          "XML.as"
  );

  private void checkUndeclaredDependencies(CompilationUnit compilationUnit) {

    List<String> usedUndeclaredDependencies = compilationUnit.getRuntimeDependencies().stream()
            .map(this::findSource)
            .filter(inputSource -> !IGNORE_DEPENDENCIES.contains(inputSource.getName()))
            .filter(inputSource -> !(inputSource.isInSourcePath() || inputSource.isInCompilePath()))
            .map(dependencyWarningsManager::convertInputSourceToDependency)
            .filter(Objects::nonNull)
            .distinct()
            .filter(dependency -> !dependency.contains("jangaroo-runtime") && !dependency.contains("jangaroo-browser"))
            .collect(Collectors.toList());

    usedUndeclaredDependencies.forEach(dependency ->
            dependencyWarningsManager.addDependencyWarning(dependency, compilationUnit.getQualifiedNameStr())
    );
  }

  private CompilationResult run1() {
    InputSource sourcePathInputSource;
    InputSource classPathInputSource;
    File propertiesOutputDirectory;
    if (getConfig().isMigrateToTypeScript()) {
      propertiesOutputDirectory = getConfig().getOutputDirectory();
    } else {
      propertiesOutputDirectory = getConfig().getLocalizedOutputDirectory();
      if (propertiesOutputDirectory == null) {
        // temporary fix until the new configuration option can be used by IDEA Plugin:
        propertiesOutputDirectory = new File(getConfig().getOutputDirectory().getParentFile(), "locale");
      }
    }
    try {
      String extNamespace = getConfig().getExtNamespace();
      sourcePathInputSource = PathInputSource.fromFiles(getConfig().getSourcePath(), new String[]{""}, true, extNamespace);
      classPathInputSource = PathInputSource.createCompilePathAwareClassPath(getConfig().getClassPath(), new String[]{"", JOO_API_IN_SWC_DIRECTORY_PREFIX}, extNamespace, getConfig().getCompilePath());
    } catch (IOException e) {
      throw new CompilerError("IO Exception occurred", e);
    }

    setUp(sourcePathInputSource, classPathInputSource);

    HashMap<File, File> outputFileMap = new HashMap<>();
    try {
      setUpMxmlComponentRegistry(sourcePathInputSource, classPathInputSource);
      for (File sourceFile : getConfig().getSourceFiles()) {
        processSource(sourceFile);
      }

      CompilationUnitSinkFactory codeSinkFactory = createSinkFactory(getConfig(), null);
      CompilationUnitSinkFactory dTsSinkFactory = createSinkFactory(getConfig(), D_TS_SUFFIX);
      CompilationUnitSinkFactory apiSinkFactory = createSinkFactory(getConfig(), AS_SUFFIX);
      ImplementedMembersAnalyzer implementedMembersAnalyzer = new ImplementedMembersAnalyzer(this);
      for (InputSource inputSource : compileQueue) {
        CompilationUnit unit = importSource(inputSource);
        if (unit != null) {
          checkValidFileName(unit);
          unit.analyze(null);
          if (getConfig().isFindUnusedDependencies() && getConfig().isSourcesAreTests()) {
            findUnusedDependencies(unit, classPathInputSource);
          }
          checkUndeclaredDependencies(unit);
          if (getConfig().getPublicApiViolationsMode() != PublicApiViolationsMode.ALLOW) {
            reportPublicApiViolations(unit);
          }

          implementedMembersAnalyzer.analyzeImplementedMembers(unit);

          TypeChecker typeChecker = new TypeChecker(log);
          unit.visit(new TransitiveAstVisitor(typeChecker));
        }
      }

      if (!dependencyWarningsManager.getDependencyWarnings().isEmpty()) {
        String dependencyNamePrefix = getConfig().isSourcesAreTests() ? TEST_DEPENDENCY_NAME_PREFIX : "";
        List<String> lines = new ArrayList<>();
        lines.add(String.format(USED_UNDECLARED_DEPENDENCIES_WARNING, dependencyNamePrefix));
        dependencyWarningsManager.getDependencyWarnings()
                .forEach(dependencyWarning -> {
                  lines.add(String.format(UNDECLARED_DEPENDENCY_USED_BY_FORMAT, dependencyNamePrefix, dependencyWarning.getDependency()));
                  dependencyWarning.getUsages().forEach(s -> lines.add("    " + s));
                  lines.add("Add the following to your pom:");
                  lines.addAll(dependencyWarning.createUsedUndeclaredDependencyWarning(getConfig().isSourcesAreTests()));
                });
        getLog().error(String.join("\n", lines));
      }

      if (!getConfig().getCompilePath().isEmpty() &&
              !dependencyWarningsManager.getUnusedDeclaredDependencies().isEmpty()) {
        getLog().warning(UNUSED_DECLARED_DEPENDENCIES_WARNING);
        dependencyWarningsManager.getUnusedDeclaredDependencies()
                .forEach(unusedDependency -> getLog().warning("    " + unusedDependency));
      }

      for (InputSource source : compileQueue) {
        File sourceFile = ((FileInputSource)source).getFile();
        File outputFile = null;
        try {
          String sourceName = source.getName();
          boolean isPropertiesSource = sourceName.endsWith(PROPERTIES_SUFFIX);
          CompilationUnit unit = importSource(source);
          if (unit != null) {
            final IdeDeclaration primaryDeclaration = unit.getPrimaryDeclaration();
            CompilationUnitSinkFactory currentCodeSinkFactory = codeSinkFactory;
            boolean generatesCode;
            if (!getConfig().isMigrateToTypeScript()) {
              generatesCode = JsCodeGenerator.generatesCode(primaryDeclaration);
            } else {
              generatesCode = TypeScriptCodeGenerator.generatesCode(primaryDeclaration);
              if (generatesCode && TypeScriptModuleResolver.getNonRequireNativeName(primaryDeclaration) != null) {
                currentCodeSinkFactory = dTsSinkFactory;
              }
            }
            if (generatesCode) {
              outputFile = writeOutput(sourceFile, unit, currentCodeSinkFactory, getConfig().isVerbose());
            }
            if (getConfig().isGenerateApi()) {
              writeOutput(sourceFile, unit, apiSinkFactory, getConfig().isVerbose());
              if (isPropertiesSource && isDefaultLocale(sourceName)) {
                // copy default locale properties to joo-api so Idea can find them
                File apiOutputDirectory = getConfig().getApiOutputDirectory();
                String relativeSourcePath = source.getRelativePath();
                File apiFile = new File(apiOutputDirectory, relativeSourcePath);
                Files.copy(sourceFile.toPath(), apiFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
              }
            }
          }
        } catch (IOException e) {
          getLog().error(fileToSymbol(sourceFile), e.getClass().getName() + ": " + e.getMessage());
        }
        outputFileMap.put(sourceFile, outputFile); // always map source file, even if output file is null!
      }

      compileQueue.clear();

      copySassFiles();

      int result = log.hasErrors() ? CompilationResult.RESULT_CODE_COMPILATION_FAILED : CompilationResult.RESULT_CODE_OK;
      return new CompilationResultImpl(result, outputFileMap);
    } catch (IOException e) {
      throw new CompilerError(e.getClass().getName() + ": " + e.getMessage(), e);
    } finally {
      tearDown();
    }
  }

  private boolean isDefaultLocale(String sourceName) {
    return sourceName.indexOf('_') < 0;
  }

  private void checkValidFileName(final CompilationUnit unit) {
    InputSource source = getInputSource(unit);
    if (!source.getName().endsWith(AS_SUFFIX)) {
      // Only check *.as file names.
      // For *.properties and *.mxml, the class name is derived from the file name, anyway!
      return;
    }
    // check valid file name for qname
    String path = source.getRelativePath();
    if (path != null) {
      String qname = unit.getPrimaryDeclaration().getQualifiedNameStr();
      String expectedPath = CompilerUtils.fileNameFromQName(qname, File.separatorChar, AS_SUFFIX);
      if (!expectedPath.equals(path)) {
        warning(unit.getSymbol(),
                String.format("expected '%s' as the file name for %s, found: '%s'. -sourcepath not set (correctly)?",
                        expectedPath,
                        qname,
                        path));
      }
    }
  }

  private void setUpMxmlComponentRegistry(InputSource sourcePathInputSource, InputSource classPathInputSource)
          throws IOException {
    // scan classpath for catalog.xml-s:
    List<InputSource> children = classPathInputSource.getChildren("catalog.xml");
    CatalogComponentsParser catalogParser =
            new CatalogComponentsParser(getMxmlComponentRegistry());
    for (InputSource child : children) {
      catalogParser.parse(child.getInputStream());
    }

    // find manifest.xml for this module's component definitions:
    MxmlComponentRegistry localMxmlComponentRegistry = new MxmlComponentRegistry();
    List<NamespaceConfiguration> namespaces = getConfig().getNamespaces();
    for (NamespaceConfiguration namespace : namespaces) {
      File componentPackageManifest = namespace.getManifest();
      InputSource componentPackageManifestInputSource;
      if (componentPackageManifest == null) {
        // look for default manifest file:
        componentPackageManifestInputSource = sourcePathInputSource.getChild("manifest.xml");
      } else {
        componentPackageManifestInputSource = new FileInputSource(componentPackageManifest, false);
      }
      if (componentPackageManifestInputSource != null) {
        InputStream manifestInputStream = componentPackageManifestInputSource.getInputStream();
        ComponentPackageModel componentPackageModel =
                new ComponentPackageManifestParser(namespace.getUri()).parse(manifestInputStream);
        getMxmlComponentRegistry().add(componentPackageModel);
        localMxmlComponentRegistry.add(componentPackageModel);
      }
    }
    File catalogOutputDirectory = getConfig().getCatalogOutputDirectory();
    if (catalogOutputDirectory != null && !localMxmlComponentRegistry.getComponentPackageModels().isEmpty()) {
      //noinspection ResultOfMethodCallIgnored
      catalogOutputDirectory.mkdirs();
      new CatalogGenerator(localMxmlComponentRegistry).generateCatalog(new File(catalogOutputDirectory, "catalog.xml"));
    }
  }

  private void reportPublicApiViolations(CompilationUnit unit) {
    Set<String> dependenciesForPublicAPICheck = new HashSet<>(unit.getRuntimeDependencies(true));
    dependenciesForPublicAPICheck.addAll(unit.getPublicApiDependencies());
    for (String qName : dependenciesForPublicAPICheck) {
      CompilationUnit compilationUnit = getCompilationUnit(qName);
      if (getInputSource(compilationUnit) instanceof ZipEntryInputSource
        && compilationUnit.getPackageDeclaration().getAnnotation(PUBLIC_API_EXCLUSION_ANNOTATION_NAME) != null) {
        String msg = "PUBLIC API VIOLATION: " + compilationUnit.getPrimaryDeclaration().getQualifiedNameStr();
        File sourceFile = new File(unit.getSymbol().getFileName());
        if (getConfig().getPublicApiViolationsMode() == PublicApiViolationsMode.WARN) {
          JangarooParser.warning(msg, sourceFile);
        } else {
          throw JangarooParser.error(msg, sourceFile);
        }
      }
    }
  }

  public File writeOutput(File sourceFile,
                          CompilationUnit compilationUnit,
                          CompilationUnitSinkFactory writerFactory,
                          boolean verbose) throws CompilerError {
    CompilationUnitSink sink = writerFactory.createSink(
            compilationUnit.getPackageDeclaration(), compilationUnit.getPrimaryDeclaration(),
            sourceFile, verbose);

    return sink.writeOutput(compilationUnit);
  }

  private CompilationUnitSinkFactory createSinkFactory(JoocConfiguration config, String suffix) {
    CompilationUnitSinkFactory codeSinkFactory = null;

    if (suffix == null) {
      suffix = getOutputSuffix();
    }
    boolean generateTypeScriptIndexDTS = D_TS_SUFFIX.equals(suffix);
    boolean generateActionScriptApi = AS_SUFFIX.equals(suffix);

    if (OUTPUT_FILE_SUFFIX.equals(suffix) && config.isMergeOutput()
            || generateTypeScriptIndexDTS && config.isMigrateToTypeScript()) {
      File outputFile = config.isMergeOutput() ? config.getOutputFile()
              : new File(getConfig().getOutputDirectory(), "index" + D_TS_SUFFIX);
      codeSinkFactory = new MergedOutputCompilationUnitSinkFactory(config, outputFile, this, this);
    } else if (!(generateActionScriptApi && !config.isGenerateApi()) && !generateTypeScriptIndexDTS) {
      File outputDirectory = generateActionScriptApi ? config.getApiOutputDirectory() : config.getOutputDirectory();
      final String nativeSuffix = !generateActionScriptApi && config.isMigrateToTypeScript() ? D_TS_SUFFIX : null;
      codeSinkFactory = new SingleFileCompilationUnitSinkFactory(config, outputDirectory, generateActionScriptApi, suffix, nativeSuffix, this, this);
    }
    return codeSinkFactory;
  }

  public static String getResultCodeDescription(int resultCode) {
    switch (resultCode) {
      case CompilationResult.RESULT_CODE_OK:
        return "ok";
      case CompilationResult.RESULT_CODE_COMPILATION_FAILED:
        return "compilation failed";
      case CompilationResult.RESULT_CODE_INTERNAL_COMPILER_ERROR:
        return "internal compiler error";
      case CompilationResult.RESULT_CODE_UNRECOGNIZED_OPTION:
        return "unrecognized option";
      case CompilationResult.RESULT_CODE_MISSING_OPTION_ARGUMENT:
        return "missing option argument";
      case CompilationResult.RESULT_CODE_ILLEGAL_OPTION_VALUE:
        return "illegal option value";
      default:
        return "unknown result code";
    }
  }

  protected void processSource(File file) throws IOException {
    if (file.isDirectory()) {
      throw error("Input file is a directory.", file);
    }
    File sourceDir = getConfig().findSourceDir(file);
//    String qName = CompilerUtils.qNameFromFile(sourceDir, file);
//    InputSource canonicalInputSource = findSource(qName);
//    if (canonicalInputSource != null && !canonicalInputSource.getPath().equals(file.getPath())) {
//      if (canonicalInputSource.getName().endsWith(PROPERTIES_SUFFIX)) {
//        // ignore classes generated from properties files!
//        return;
//      }
//      throw Jooc.error(String.format("Compilation unit %s defined in %s is redeclared in %s.", qName, canonicalInputSource.getPath(), file.getPath()), file);
//    }
    FileInputSource inputSource = new FileInputSource(sourceDir, file, true, getConfig().getExtNamespace());
    compileQueue.add(inputSource);
    importSource(inputSource);
  }

  protected void copySassFiles() throws IOException {
    String extSassNamespace = getConfig().getExtSassNamespace();
    for (String sassSourceSubFolderName : getConfig().getSassSourceFilesByType().keySet()) {
      File sassSourceSubFolder = getConfig().getSassSourcePathByType().get(sassSourceSubFolderName);
      File sassTargetSubFolder = getConfig().getSassOutputDirectoryByType().get(sassSourceSubFolderName);
      if (sassSourceSubFolder == null) {
        getLog().error("No sass source folder registered for folder type: " + sassSourceSubFolderName);
        continue;
      }
      if (sassTargetSubFolder == null) {
        getLog().error("No sass output folder registered for folder type: " + sassSourceSubFolderName);
        continue;
      }
      for (File sassSourceFile : getConfig().getSassSourceFilesByType().get(sassSourceSubFolderName)) {
        File sassTargetFile;
        Path relativeSrcPath = sassSourceSubFolder.toPath().relativize(sassSourceFile.toPath());
        String srcFileName = sassSourceFile.getName();
        if (!srcFileName.endsWith(SCSS_SUFFIX)) {
          getLog().warning("Cannot handle unknown extension in sass directory for file: " + sassSourceFile);
          sassTargetFile = sassTargetSubFolder.toPath().resolve(relativeSrcPath).toFile();
        } else {
          String fqn = CompilerUtils.qNameFromFile(sassSourceSubFolder, sassSourceFile);
          // apply possible renaming
          CompilationUnit compilationUnit = getCompilationUnit(fqn);
          String newFqn;
          if (compilationUnit != null) {
            newFqn = compilationUnit.getPrimaryDeclaration().getTargetQualifiedNameStr();
          } else {
            // do not rename
            newFqn = fqn;
            getLog().warning(String.format("Could not find compilation unit %s for SASS file %s", fqn, srcFileName));
          }
          // apply extSassNamespace
          if (!extSassNamespace.isEmpty()) {
            if (!newFqn.startsWith(extSassNamespace) || extSassNamespace.equals(newFqn)) {
              getLog().error(String.format("Invalid extSassNamespace configuration %s! The following file location would be transformed: %s", extSassNamespace, sassSourceFile));
              continue;
            }
            newFqn = newFqn.substring(extSassNamespace.length() + 1);
          }
          sassTargetFile = CompilerUtils.fileFromQName(newFqn, sassTargetSubFolder, SCSS_SUFFIX);
        }
        //noinspection ResultOfMethodCallIgnored
        sassTargetFile.getParentFile().mkdirs();
        try {
          Files.copy(sassSourceFile.toPath(),
                  sassTargetFile.toPath(),
                  StandardCopyOption.REPLACE_EXISTING,
                  StandardCopyOption.COPY_ATTRIBUTES,
                  LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
          getLog().error(String.format("Could not copy file %s to %s", sassSourceFile, sassTargetFile));
        }
      }
    }
  }

  public static int run(String[] argv, CompileLog log) {
    try {
      JoocCommandLineParser commandLineParser = new JoocCommandLineParser();
      JoocConfiguration config = commandLineParser.parse(argv);
      if (config != null) {
        return new Jooc(config, log).run().getResultCode();
      }
    } catch (CommandLineParseException e) {
      System.out.println(e.getMessage()); // NOSONAR this is a commandline tool
      return e.getExitCode();
    }
    return CompilationResult.RESULT_CODE_OK;
  }

  public static void main(String[] argv) {
    int result = run(argv, new StdOutCompileLog());
    if (result != 0) {
      System.exit(result);
    }
  }

}
