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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.jangaroo.jooc.api.CompilationResult;
import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.DotExpr;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.QualifiedIde;
import net.jangaroo.jooc.ast.TransitiveAstVisitor;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.backend.CompilationUnitSink;
import net.jangaroo.jooc.backend.CompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.MergedOutputCompilationUnitSinkFactory;
import net.jangaroo.jooc.backend.SingleFileCompilationUnitSinkFactory;
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
import net.jangaroo.jooc.util.GraphUtil;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Jangaroo AS3-to-JS Compiler's main class.
 *
 * @author Andreas Gawecki
 * @author Frank Wienberg
 */
public class Jooc extends JangarooParser implements net.jangaroo.jooc.api.Jooc {

  public static final String CLASS_LOADER_NAME = "classLoader";
  public static final String CLASS_LOADER_PACKAGE_NAME = "joo";
  public static final String CLASS_LOADER_FULLY_QUALIFIED_NAME = CLASS_LOADER_PACKAGE_NAME + "." + CLASS_LOADER_NAME;
  public static final String PUBLIC_API_EXCLUSION_ANNOTATION_NAME = "ExcludeClass";
  public static final String PUBLIC_API_INCLUSION_ANNOTATION_NAME = "PublicApi";
  public static final String NATIVE_ANNOTATION_NAME = "Native";
  public static final String NATIVE_ANNOTATION_REQUIRE_PROPERTY = "require";
  public static final String BINDABLE_ANNOTATION_NAME = "Bindable";
  public static final String EXT_CONFIG_ANNOTATION_NAME = "ExtConfig";

  private List<CompilationUnit> compileQueue = new ArrayList<CompilationUnit>();

  public Jooc() {
  }

  public Jooc(JoocConfiguration config) {
    this(config, new StdOutCompileLog());
  }

  public Jooc(JoocConfiguration config, CompileLog log) {
    super(config, log);
  }

  public JoocConfiguration getConfig() {
    return (JoocConfiguration) super.getConfig();
  }

  @Override
  public void setConfig(JoocConfiguration config) {
    super.setConfig(config);
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

  private CompilationResult run1() {
    InputSource sourcePathInputSource;
    InputSource classPathInputSource;
    try {
      sourcePathInputSource = PathInputSource.fromFiles(getConfig().getSourcePath(), new String[]{""}, true);
      classPathInputSource = PathInputSource.fromFiles(getConfig().getClassPath(), new String[]{"", JOO_API_IN_JAR_DIRECTORY_PREFIX}, false);
    } catch (IOException e) {
      throw new CompilerError("IO Exception occurred", e);
    }

    setUp(sourcePathInputSource, classPathInputSource);

    HashMap<File, File> outputFileMap = new HashMap<File, File>();
    try {
      setUpMxmlComponentRegistry(sourcePathInputSource, classPathInputSource);
      for (File sourceFile : getConfig().getSourceFiles()) {
        processSource(sourceFile);
      }

      CompilationUnitSinkFactory codeSinkFactory = createSinkFactory(getConfig(), false);
      CompilationUnitSinkFactory apiSinkFactory = null;
      if (getConfig().isGenerateApi()) {
        apiSinkFactory = createSinkFactory(getConfig(), true);
      }
      for (CompilationUnit unit : compileQueue) {
        unit.analyze(null);
        if (getConfig().getPublicApiViolationsMode() != PublicApiViolationsMode.ALLOW) {
          reportPublicApiViolations(unit);
        }
      }

      analyzeDependencyGraph();

      for (CompilationUnit unit : compileQueue) {
        File sourceFile = ((FileInputSource)unit.getSource()).getFile();
        File outputFile = null;
        // only generate JavaScript if [Native] annotation and 'native' modifier on primary compilationUnit are not present:
        if (unit.getAnnotation(NATIVE_ANNOTATION_NAME) == null && !unit.getPrimaryDeclaration().isNative()) {
          outputFile = writeOutput(sourceFile, unit, codeSinkFactory, getConfig().isVerbose());
        }
        outputFileMap.put(sourceFile, outputFile); // always map source file, even if output file is null!
        if (getConfig().isGenerateApi()) {
          writeOutput(sourceFile, unit, apiSinkFactory, getConfig().isVerbose());
        }
      }
      int result = log.hasErrors() ? CompilationResult.RESULT_CODE_COMPILATION_FAILED : CompilationResult.RESULT_CODE_OK;
      return new CompilationResultImpl(result, outputFileMap);
    } catch (IOException e) {
      throw new CompilerError("IO Exception occurred", e);
    } finally {
      tearDown();
    }
  }

  private enum Level {
    DYNAMIC(""),
    STATIC(".static"),
    INIT(".init");

    String suffix;

    Level(String suffix) {
      this.suffix = suffix;
    }
  }

  private static class Dependent {
    private CompilationUnit compilationUnit;
    private Level level;

    public Dependent(CompilationUnit compilationUnit, Level level) {
      this.compilationUnit = compilationUnit;
      this.level = level;
    }

    public CompilationUnit getCompilationUnit() {
      return compilationUnit;
    }

    public Level getLevel() {
      return level;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Dependent dependent = (Dependent) o;

      if (level != dependent.level) {
        return false;
      }
      return compilationUnit.equals(dependent.compilationUnit);
    }

    @Override
    public int hashCode() {
      int result = compilationUnit.hashCode();
      result = 31 * result + level.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return getCompilationUnitName(compilationUnit) + level.suffix;
    }
  }

  private void analyzeDependencyGraph() throws IOException {
    final Multimap<Dependent, Dependent> dependencyGraph = HashMultimap.create();
    Set<CompilationUnit> processedCompilationUnits = new HashSet<CompilationUnit>();
    Set<CompilationUnit> unprocessedCompilationUnits = new HashSet<CompilationUnit>(getCompilationUnits());
    while (!unprocessedCompilationUnits.isEmpty()) {
      for (final CompilationUnit compilationUnit : unprocessedCompilationUnits) {
        if (processedCompilationUnits.add(compilationUnit) && compilationUnit.getSource().isInSourcePath()) {
          // Add conceptual dependencies: DYNAMIC -> STATIC -> INIT.
          dependencyGraph.put(new Dependent(compilationUnit, Level.DYNAMIC), new Dependent(compilationUnit, Level.STATIC));
          dependencyGraph.put(new Dependent(compilationUnit, Level.STATIC), new Dependent(compilationUnit, Level.INIT));

          // Analyze dependencies in detail.
          final IdeDeclaration primaryDeclaration = compilationUnit.getPrimaryDeclaration();
          final Object classBody = primaryDeclaration instanceof ClassDeclaration ?
                  ((ClassDeclaration) primaryDeclaration).getBody() :
                  "noBody";

          // key null: references from static code
          // other keys: references from static methods
          final Multimap<FunctionDeclaration, FunctionDeclaration> internalUses = HashMultimap.create();
          final Multimap<FunctionDeclaration, Dependent> nonFunctionUses = HashMultimap.create();
          final FunctionDeclaration[] currentDeclaration = {null};
          compilationUnit.visit(new TransitiveAstVisitor(new AstVisitorBase()) {
            @Override
            public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) throws IOException {
              // Ignore instance methods.
              if (functionDeclaration.isStatic()) {
                // For static methods, register all nested ides as dependencies of the method.
                boolean isTopLevel = currentDeclaration[0] == null &&
                        classBody.equals(functionDeclaration.getParentNode());
                if (isTopLevel) {
                  currentDeclaration[0] = functionDeclaration;
                }
                super.visitFunctionDeclaration(functionDeclaration);
                if (isTopLevel) {
                  currentDeclaration[0] = null;
                }
              }
            }

            @Override
            public void visitVariableDeclaration(VariableDeclaration variableDeclaration) throws IOException {
              // Ignore instance fields.
              if (variableDeclaration.isStatic()) {
                // For static fields, register all nested ides as init dependencies.
                super.visitVariableDeclaration(variableDeclaration);
              }
            }

            private void declarationReferenced(IdeDeclaration ideDeclaration, boolean isNew) {
              if (ideDeclaration != null) {
                CompilationUnit ideCompilationUnit = ideDeclaration.getCompilationUnit();
                if (ideCompilationUnit != null && ideCompilationUnit.getSource().isInSourcePath()) {
                  if (isNew) {
                    nonFunctionUses.put(currentDeclaration[0], new Dependent(ideDeclaration.getCompilationUnit(), Level.DYNAMIC));
                  } else if (!ideCompilationUnit.equals(compilationUnit)) {
                    // The identifier is defined in a different compilation unit in the same module.
                    // The dependency must be analysed, because it might have to be
                    // strengthened into a required dependency.
                    boolean isStatic = ideDeclaration.isStatic();
                    // Ignore ordinary method calls: The called class must have been initialized,
                    // because an instance has already been created.
                    if (isStatic) {
                      nonFunctionUses.put(currentDeclaration[0],
                              new Dependent(ideDeclaration.getCompilationUnit(), Level.STATIC));
                    }
                  } else if (ideDeclaration instanceof FunctionDeclaration) {
                    FunctionDeclaration functionDeclaration = (FunctionDeclaration) ideDeclaration;
                    if (functionDeclaration.isMethod() && functionDeclaration.isStatic()) {
                      // A local constructor call or a local static call.
                      internalUses.put(currentDeclaration[0], functionDeclaration);
                    }
                  }
                }
              }
            }

            @Override
            public void visitIdeExpression(IdeExpr ideExpr) throws IOException {
              Expr normalizedExpr = ideExpr.getNormalizedExpr();
              if (normalizedExpr != ideExpr) {
                normalizedExpr.visit(this);
              } else {
                declarationReferenced(ideExpr.getIde().getDeclaration(false),
                        ideExpr.getParentNode() instanceof NewExpr);
              }
            }

            @Override
            public void visitDotExpr(DotExpr dotExpr) throws IOException {
              boolean isNew = dotExpr.getParentNode() instanceof NewExpr;

              Ide ide = dotExpr.getIde();
              declarationReferenced(ide.getDeclaration(false), isNew);

              Expr arg = dotExpr.getArg();
              arg.visit(this);

              // In some cases, the ide is not properly scoped, but the arg
              // is known to refer to a class declaration, which can be used
              // to resolve static members.
              if (arg instanceof IdeExpr) {
                IdeDeclaration argDeclaration = ((IdeExpr) arg).getIde().getDeclaration(false);
                if (argDeclaration instanceof ClassDeclaration) {
                  TypedIdeDeclaration staticMemberDeclaration = ((ClassDeclaration) argDeclaration).getStaticMemberDeclaration(ide.getName());
                  if (staticMemberDeclaration != null) {
                    declarationReferenced(staticMemberDeclaration, isNew);
                  }
                }
              }
            }

            @Override
            public void visitQualifiedIde(QualifiedIde qualifiedIde) throws IOException {
              visitIde(qualifiedIde);
            }
          });

          // Compute dependencies of the INIT level.
          Set<Dependent> initDependencies = new HashSet<Dependent>();

          // Dependencies directly in initializers.
          Collection<Dependent> nonFunctionDependencies = nonFunctionUses.get(null);
          if (nonFunctionDependencies != null) {
            initDependencies.addAll(nonFunctionDependencies);
          }

          // Dependencies in methods called directly or indirectly from initializers.
          Set<FunctionDeclaration> done = new HashSet<FunctionDeclaration>();
          Deque<FunctionDeclaration> todo = new LinkedList<FunctionDeclaration>();
          todo.addAll(internalUses.get(null));
          while (!todo.isEmpty()) {
            FunctionDeclaration ideDeclaration = todo.removeLast();
            if (done.add(ideDeclaration)) {
              if (ideDeclaration.isConstructor()) {
                // Constructor call: typical for singleton pattern.
                // Depend on level DYNAMIC, which ensures that all dependencies apply.
                initDependencies.add(new Dependent(compilationUnit, Level.DYNAMIC));
              } else {
                // Add dependencies.
                Collection<Dependent> localDependencies = nonFunctionUses.get(ideDeclaration);
                if (localDependencies != null) {
                  initDependencies.addAll(localDependencies);
                }
                if (internalUses.containsKey(ideDeclaration)) {
                  todo.addAll(internalUses.get(ideDeclaration));
                }
              }
            }
          }

          for (Dependent dependent : initDependencies) { // TODO: ignore null key!
            dependencyGraph.put(new Dependent(compilationUnit, Level.INIT), dependent);
          }

          // Dependencies for level STATIC.
          for (Dependent dependent : nonFunctionUses.values()) {
            dependencyGraph.put(new Dependent(compilationUnit, Level.STATIC), dependent);
          }

          // Required dependencies are registered at the INIT level,
          // other dependencies are registered at the STATIC level.
          for (CompilationUnit dependency : compilationUnit.getDependenciesAsCompilationUnits()) {
            boolean isRequired = compilationUnit.isRequiredDependency(dependency);
            dependencyGraph.put(new Dependent(compilationUnit, isRequired ? Level.INIT : Level.DYNAMIC),
                    new Dependent(dependency, Level.DYNAMIC));
          }
        }
      }

      unprocessedCompilationUnits = new HashSet<CompilationUnit>(getCompilationUnits());
      unprocessedCompilationUnits.removeAll(processedCompilationUnits);
    }

    // Process each strongly connected component of the dependency graph.
    Collection<Set<Dependent>> sccs = GraphUtil.stronglyConnectedComponent(dependencyGraph.asMap());
    Collection<Set<Dependent>> errorSCCs = new ArrayList<Set<Dependent>>();
    for (Set<Dependent> scc : sccs) {
      // Each scc must contain at most one initializer.
      List<Dependent> initializingDependents = new ArrayList<Dependent>();
      for (Dependent dependent : scc) {
        if (dependent.getLevel() == Level.INIT) {
          initializingDependents.add(dependent);
        }
      }

      // We assume that callbacks into a partially initialized class
      // are acceptable if they are triggered by the class initialization itself.
      // Therefore one initializer per cycle is ok. For more than one
      // initialized class, there might be nondeterministic effects
      // as one of the classes has to be initialized first.
      if (initializingDependents.size() > 1) {
        errorSCCs.add(scc);
      }

      // Initialized compilation units require all transitive dependents.
      // Uses dependencies are not enough.
      if (initializingDependents.size() == 1) {
        Dependent initializer = initializingDependents.get(0);
        CompilationUnit compilationUnit = initializer.getCompilationUnit();
        Deque<Dependent> todo = new LinkedList<Dependent>();
        todo.add(initializer);
        Set<Dependent> visited = new HashSet<Dependent>();
        while (!todo.isEmpty()) {
          Dependent dependent = todo.removeLast();
          if (visited.add(dependent)) {
            todo.addAll(dependencyGraph.get(dependent));

            CompilationUnit dependency = dependent.getCompilationUnit();
            if (!compilationUnit.equals(dependency)) {
              compilationUnit.addRequiredDependency(dependency);
            }
          }
        }
      }
    }

    if (!errorSCCs.isEmpty()) {
      Multimap<String, String> requires = HashMultimap.create();
      Set<String> allInitializedNames = new HashSet<String>();
      for (Map.Entry<Dependent, Collection<Dependent>> entry : dependencyGraph.asMap().entrySet()) {
        Dependent dependent = entry.getKey();
        String dependentName = dependent.toString();
        if (dependent.getLevel() == Level.INIT) {
          allInitializedNames.add(dependentName);
        }
        Collection<Dependent> dependencies = entry.getValue();
        for (Dependent dependency : dependencies) {
          requires.put(dependentName, dependency.toString());
        }
      }

      File reportOutputDirectory = getConfig().getReportOutputDirectory();
      File dependencyGraphFile = new File(reportOutputDirectory, "cycles.graphml");
      if (reportOutputDirectory != null) {
        try {
          Set<String> allDependents = new HashSet<String>();
          for (Set<Dependent> errorSCC : errorSCCs) {
            for (Dependent dependent : errorSCC) {
              allDependents.add(dependent.toString());
            }
          }
          DependencyGraphFile.writeDependencyFile(requires, allDependents, allInitializedNames, dependencyGraphFile);
        } catch (IOException e) {
          logCompilerError(e);
          // Do not throw again. A worse error is logged presently.
        }
      }

      // Complain.
      Set<Dependent> errorSCC = errorSCCs.iterator().next();
      List<Dependent> initializedDependents = new ArrayList<Dependent>();
      for (Dependent dependent : errorSCC) {
        if (dependent.getLevel() == Level.INIT) {
          initializedDependents.add(dependent);
        }
      }
      Dependent dependent1 = initializedDependents.get(0);
      Dependent dependent2 = initializedDependents.get(1);

      List<Dependent> path12 = new ArrayList<Dependent>(GraphUtil.findPath(dependencyGraph.asMap(), dependent1, dependent2));
      List<Dependent> path21 = new ArrayList<Dependent>(GraphUtil.findPath(dependencyGraph.asMap(), dependent2, dependent1));
      List<Dependent> cycle = new ArrayList<Dependent>();
      cycle.addAll(path12);
      cycle.addAll(path21.subList(1, path21.size()));

      StringBuilder message = new StringBuilder();
      message.append("The compilation units ");
      message.append(getCompilationUnitName(dependent1.getCompilationUnit()));
      message.append(" and ");
      message.append(getCompilationUnitName(dependent2.getCompilationUnit()));
      message.append(" contain static initializers");
      message.append(" (for example code blocks or static variables with a complex initializer)");
      message.append(" and the static initializers are mutually dependent: ");
      for (int i = 0; i < cycle.size(); i++) {
        if (i > 0) {
          message.append(" -> ");
        }
        message.append(cycle.get(i));
      }
      message.append(". You can either remove a static initializer or break the dependency cycle");
      message.append(" to make this module compile. (Other dependency cycles might exist, though.)");
      if (reportOutputDirectory != null) {
        message.append(" A dependency graph of the module has been written to ");
        message.append(dependencyGraphFile);
        message.append('.');
      }

      throw error(message.toString());
    }
  }

  private static String getCompilationUnitName(CompilationUnit compilationUnit) {
    return compilationUnit.getPrimaryDeclaration().getIde().getIde().getText();
  }

  @Override
  protected void keepGeneratedActionScript(InputSource in, String code) {
    File keepGeneratedActionScriptDirectory = getConfig().getKeepGeneratedActionScriptDirectory();
    if (keepGeneratedActionScriptDirectory != null) {
      File outputPackageDir = new File(keepGeneratedActionScriptDirectory, in.getParent().getRelativePath());
      //noinspection ResultOfMethodCallIgnored
      outputPackageDir.mkdirs();
      File generatedActionScriptFile =
              new File(outputPackageDir, CompilerUtils.removeExtension(in.getName()) + AS_SUFFIX);
      FileWriter fileWriter = null;
      try {
        fileWriter = new FileWriter(generatedActionScriptFile);
        fileWriter.write(code);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (fileWriter != null) {
          try {
            fileWriter.close();
          } catch (IOException e) {
            // ignore
          }
        }
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
    for (CompilationUnit compilationUnit : unit.getDependenciesAsCompilationUnits()) {
      if (compilationUnit.getSource() instanceof ZipEntryInputSource
        && compilationUnit.getAnnotation(PUBLIC_API_EXCLUSION_ANNOTATION_NAME) != null) {
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

  private CompilationUnitSinkFactory createSinkFactory(JoocConfiguration config, final boolean generateActionScriptApi) {
    CompilationUnitSinkFactory codeSinkFactory;

    if (!generateActionScriptApi && config.isMergeOutput()) {
      codeSinkFactory = new MergedOutputCompilationUnitSinkFactory(
              config, config.getOutputFile()
      );
    } else {
      File outputDirectory = generateActionScriptApi ? config.getApiOutputDirectory() : config.getOutputDirectory();
      final String suffix = generateActionScriptApi ? AS_SUFFIX : OUTPUT_FILE_SUFFIX;
      codeSinkFactory = new SingleFileCompilationUnitSinkFactory(config, outputDirectory, generateActionScriptApi, suffix);
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
    CompilationUnit unit = importSource(new FileInputSource(getConfig().findSourceDir(file), file, true));
    if (unit != null) {
      compileQueue.add(unit);
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
