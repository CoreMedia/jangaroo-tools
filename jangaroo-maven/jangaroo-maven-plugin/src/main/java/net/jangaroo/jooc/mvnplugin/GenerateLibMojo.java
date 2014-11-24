package net.jangaroo.jooc.mvnplugin;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.VariableRenamingPolicy;
import net.jangaroo.utils.CompilerUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.IOUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Apply compression on generated JS (using Google Closure Compiler),
 * aggregating into a single "lib" file.
 *
 * @author Frank Wienberg
 * @goal generate-lib
 * @phase process-classes
 * @threadSafe
 */
// @SuppressWarnings("unchecked")
public class GenerateLibMojo extends JangarooMojo {

  /**
   * Generated JavaScript AMD sources directory: the base directory for compression.
   *
   * @parameter expression="${project.build.outputDirectory}/META-INF/resources/amd/as3"
   */
  private File sourceDirectory;

  /**
   * @parameter expression="${project.build.outputDirectory}/META-INF/resources/amd"
   */
  private File aggregationOutputBaseDir;

  /**
   * Whether to skip execution.
   *
   * @parameter expression="${jangaroo.lib.skip}" default-value="false"
   */
  private boolean skip;

  /**
   * define if plugin must stop/fail on warnings.
   *
   * @parameter expression="${jangaroo.lib.failOnWarning}" default-value="false"
   */
  private boolean failOnWarning;

  /**
   * Read the input file using "encoding".
   *
   * @parameter expression="${file.encoding}" default-value="UTF-8"
   */
  private String encoding;

  /**
   * @parameter expression="${project}"
   * @readonly
   * @required
   */
  private MavenProject project;

  @Override
  public MavenProject getProject() {
    return project;
  }

  @SuppressWarnings("unchecked")
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      if (skip) {
        getLog().debug("run of generate-lib goal of jangaroo-maven-plugin skipped");
        return;
      }
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
    if (sourceDirectory == null || !sourceDirectory.exists()) {
      return;
    }
    if (aggregationOutputBaseDir == null) {
      throw new MojoFailureException("destination lib base dir is null");
    }
    File aggregationOutputFile = new File(aggregationOutputBaseDir, GenerateModuleAMDMojo.computeAMDName(project.getGroupId(), project.getArtifactId()) + ".lib.js");
    aggregationOutputFile.getParentFile().mkdirs();
    DirectoryScanner scanner = new DirectoryScanner();
    scanner.setBasedir(sourceDirectory);
    scanner.setIncludes(new String[]{"**/*.js"});
    scanner.scan();
    String[] sources = scanner.getIncludedFiles();
    getLog().debug("start generating compressed library");
    Compiler compiler = new Compiler();
     
    CompilerOptions options = new CompilerOptions();
    // Advanced mode not used here, because we write standard JavaScript, not Google's DSL.
    CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
    options.setWarningLevel(DiagnosticGroups.NON_STANDARD_JSDOC, CheckLevel.OFF);
    // since we use AMD, no global variables exist:
    options.variableRenaming = VariableRenamingPolicy.ALL;
    options.sourceMapFormat = SourceMap.Format.V3;
    options.sourceMapOutputPath = aggregationOutputFile.getParent();
    options.sourceMapDetailLevel = SourceMap.DetailLevel.ALL;
    options.sourceMapLocationMappings = new ArrayList<SourceMap.LocationMapping>();
    String sourcePath = sourceDirectory.getCanonicalPath().replace(File.separatorChar, '/');
    String relativePath = CompilerUtils.getRelativePath(aggregationOutputFile.getParentFile(), sourceDirectory, false).replace(File.separatorChar, '/');
    options.sourceMapLocationMappings.add(new SourceMap.LocationMapping(sourcePath, relativePath));

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
    writer.write("\n//# sourceMappingURL=" + aggregationOutputFile.getName() + ".map");
    IOUtil.close(writer);
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
    getLog().debug("end generating compressed library");
  }

}
