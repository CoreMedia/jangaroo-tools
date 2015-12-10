package net.jangaroo.jooc.mvnplugin;

import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.Compiler;
import net.jangaroo.jooc.config.JoocConfiguration;
import net.jangaroo.jooc.config.NamespaceConfiguration;
import net.jangaroo.utils.CompilerUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.IOUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Mojo to compile Jangaroo sources during the compile phase.
 * Then, apply compression on all generated JavaScript files (using Google Closure Compiler),
 * aggregating them into a single <code>&#42;.lib.js</code> file.
 *
 * @goal compile
 * @phase compile
 * @requiresDependencyResolution compile
 * @threadSafe
 */
@SuppressWarnings({"UnusedDeclaration", "UnusedPrivateField"})
public class CompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory into whose META-INF/resources/amd/as3 sub-directory compiled classes are generated.
   * This property is used for <code>jangaroo</code> packaging type as {@link #getOutputDirectory}.
   *
   * @parameter expression="${project.build.outputDirectory}"
   */
  private File outputDirectory;

  /**
   * Location of Jangaroo resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. This property is used for <code>war</code> packaging type (actually, all packaging types
   * but <code>jangaroo</code>) as {@link #getOutputDirectory}.
   * Defaults to ${project.build.directory}/jangaroo-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-output/"
   */
  private File packageSourceDirectory;

  /**
   * Temporary output directory for compiled classes to be packaged into a single *.js file.
   *
   * @parameter expression="${project.build.directory}/temp/jangaroo-output/classes"
   */
  private File tempClassesOutputDirectory;

  /**
   * A list of inclusion filters for the compiler.
   *
   * @parameter
   */
  private Set<String> includes = new HashSet<String>();
  /**
   * A list of exclusion filters for the compiler.
   *
   * @parameter
   */
  private Set<String> excludes = new HashSet<String>();

  /**
   * This parameter specifies the path and name of the output file containing all
   * compiled classes, relative to the outputDirectory.
   *
   * @parameter expression="joo/${project.groupId}.${project.artifactId}.classes.js"
   */
  private String moduleClassesJsFile;

  /**
   * Output directory for generated API stubs, relative to the outputDirectory.
   *
   * @parameter expression="${project.build.outputDirectory}/META-INF/joo-api"
   */
  private String apiOutputDirectory;

  /**
   * A list of custom MXML component namespaces.
   *
   * @parameter
   */
  @SuppressWarnings("MismatchedReadAndWriteOfArray")
  private NamespaceConfiguration[] namespaces;

  /**
   * Define if plugin must stop/fail on warnings.
   *
   * @parameter expression="${jangaroo.compile.failOnWarning}" default-value="false"
   */
  private boolean failOnWarning;

  /**
   * Read the input file using "encoding".
   *
   * @parameter expression="${file.encoding}" default-value="UTF-8"
   */
  private String encoding;

  public File getApiOutputDirectory() {
    return isJangarooPackaging() ? new File(apiOutputDirectory) : null;
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    return getMavenPluginHelper().getActionScriptClassPath(false);
  }

  protected List<File> getCompileSourceRoots() {
    return Arrays.asList(sourceDirectory,getGeneratedSourcesDirectory());
  }

  protected File getOutputDirectory() {
    return isJangarooPackaging() ? new File(outputDirectory, "META-INF/resources") : packageSourceDirectory;
  }

  protected File getTempClassesOutputDirectory() {
    return tempClassesOutputDirectory;
  }

  @Override
  protected Set<String> getIncludes() {
    return includes;
  }

  @Override
  protected Set<String> getExcludes() {
    return excludes;
  }

  public String getModuleClassesJsFileName() {
    return moduleClassesJsFile;
  }

  @Override
  protected JoocConfiguration createJoocConfiguration() throws MojoExecutionException, MojoFailureException {
    JoocConfiguration joocConfiguration = super.createJoocConfiguration();
    if (namespaces != null) {
      joocConfiguration.setNamespaces(Arrays.asList(namespaces));
    }
    return joocConfiguration;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    super.execute();
    try {
      processAllJsFiles();
    } catch (RuntimeException exc) {
      throw exc;
    } catch (MojoFailureException exc) {
      throw exc;
    } catch (MojoExecutionException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new MojoExecutionException("wrap: " + exc.getMessage(), exc);
    }
  }

  private void processAllJsFiles() throws Exception {
    File aggregationOutputBaseDir = new File(getOutputDirectory(), "amd");
    File sourceDirectory = new File(aggregationOutputBaseDir, "as3");
    if (true || !sourceDirectory.exists()) {
      getLog().debug("generate-lib: source directory " + sourceDirectory + " does not exist; skipping *.lib.js generation.");
      return;
    }
    File aggregationOutputFile = new File(aggregationOutputBaseDir, GenerateModuleAMDMojo.computeAMDName(getProject().getGroupId(), getProject().getArtifactId()) + ".lib.js");
    //noinspection ResultOfMethodCallIgnored
    aggregationOutputFile.getParentFile().mkdirs();
    DirectoryScanner scanner = new DirectoryScanner();
    scanner.setBasedir(sourceDirectory);
    scanner.setIncludes(new String[]{"**/*.js"});
    scanner.setExcludes(new String[]{"**/*_properties_*.js"});
    scanner.scan();
    String[] sources = scanner.getIncludedFiles();
    getLog().debug("start generating compressed library");
    com.google.javascript.jscomp.Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    final MessageFormatter messageFormatter = options.errorFormat.toFormatter(compiler, false);
    compiler.setErrorManager(new BasicErrorManager() {
      private int suppressedWarningsCount = 0;

      @Override
      public void println(CheckLevel level, JSError error) {
        if (level == CheckLevel.ERROR) {
          getLog().error(error.format(level, messageFormatter));
        } else if (level == CheckLevel.WARNING) {
          // ignore certain JSDoc warnings (see below):
          if ("JSC_PARSE_ERROR".equals(error.getType().key) && error.description.contains("Non-JSDoc comment has annotations")) {
            ++suppressedWarningsCount;
          } else {
            getLog().warn(error.format(level, messageFormatter));
          }
        }
      }

      @Override
      protected void printSummary() {
        getLog().info(String.format("GCC: %d error(s), %d warning(s)%n",
                getErrorCount(), getWarningCount() - suppressedWarningsCount));
      }
    });
    // Advanced mode not used here, because we write standard JavaScript, not Google's DSL.
    CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
    // does not seem to have the desired effect, so we disable all warnings (see above):
    options.setWarningLevel(DiagnosticGroups.NON_STANDARD_JSDOC, CheckLevel.OFF);
    // since we use AMD, no global variables exist:
    options.variableRenaming = VariableRenamingPolicy.ALL;
    if (isGenerateSourceMaps()) {
      options.sourceMapFormat = SourceMap.Format.V3;
      options.sourceMapOutputPath = aggregationOutputFile.getParent();
      options.sourceMapDetailLevel = SourceMap.DetailLevel.ALL;
      options.sourceMapLocationMappings = new ArrayList<SourceMap.LocationMapping>();
      String sourcePath = sourceDirectory.getCanonicalPath().replace(File.separatorChar, '/');
      String relativePath = CompilerUtils.getRelativePath(aggregationOutputFile.getParentFile(), sourceDirectory, false).replace(File.separatorChar, '/');
      options.sourceMapLocationMappings.add(new SourceMap.LocationMapping(sourcePath, relativePath));
    }
    options.setOutputCharset(encoding);

    Charset charset = Charset.forName(encoding);
    ArrayList<SourceFile> inputs = new ArrayList<SourceFile>();
    for (String source : sources) {
      inputs.add(SourceFile.fromFile(new File(sourceDirectory, source), charset));
    }
    Result result = compiler.compile(Collections.<SourceFile>emptyList(), inputs, options);

    if (failOnWarning && result.warnings.length > 0) {
      throw new MojoFailureException("warnings while generating lib => failure! See log: "
        + result.debugLog);
    }

    if (!result.success) {
      throw new MojoFailureException("errors while generating lib => failure!");
    }
    // The compiler is responsible for generating the compiled code; it is not
    // accessible via the Result.
    getLog().info("writing library file " + aggregationOutputFile.getCanonicalPath());
    OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(aggregationOutputFile), encoding);
    writer.write(compiler.toSource());
    if (isGenerateSourceMaps()) {
      writer.write("\n//# sourceMappingURL=" + aggregationOutputFile.getName() + ".map");
    }
    IOUtil.close(writer);
    if (isGenerateSourceMaps()) {
      getLog().info("projecting library source map...");
      StringWriter sourceMapWriter = new StringWriter();
      result.sourceMap.appendTo(sourceMapWriter, aggregationOutputFile.getName());
      JSONObject sourceMapJson = new JSONObject(sourceMapWriter.toString());
      JSONArray jsSources = (JSONArray) sourceMapJson.get("sources");
      getLog().info("projecting " + jsSources.length() + " *.js source names to *.as.");
      List<String> asSources = new ArrayList<String>(jsSources.length());
      for (int i = 0; i < jsSources.length(); i++) {
        String source = (String) jsSources.get(i);
        source = source.replaceFirst(".js$", ".as");
        asSources.add(source);
      }
      sourceMapJson.put("sources", new JSONArray(asSources));

      String libSourceMapFileName = aggregationOutputFile.getCanonicalPath() + ".map";
      getLog().info("writing library source map " + libSourceMapFileName);
      OutputStreamWriter sourceMapFileWriter = new OutputStreamWriter(new FileOutputStream(libSourceMapFileName), encoding);
      sourceMapFileWriter.write(sourceMapJson.toString());
      IOUtil.close(sourceMapFileWriter);
    }
    getLog().debug("end generating compressed library");
  }
}
