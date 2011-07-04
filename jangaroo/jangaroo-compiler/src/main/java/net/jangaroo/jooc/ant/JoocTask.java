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

package net.jangaroo.jooc.ant;

import net.jangaroo.jooc.Jooc;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

import java.io.File;
import java.util.Vector;

/**
 * @author Andreas Gawecki
 */
public class JoocTask extends MatchingTask {

  private static final String FAIL_MSG
      = "Compile failed; see the compiler error output for details.";

  private Path src;
  private String classpath;
  private File destDir;
  private String sourcepath;
  private File apiDestDir;
  private boolean debug = false;
  private String debugLevel = null;
  private boolean enableAssertions = false;
  private boolean verbose = false;
  protected boolean failOnError = true;
  private boolean allowduplicatelocalvariables;
  protected File[] compileList = new File[0];
  private String autoSemicolon = null;

  public boolean getEnableassertions() {
    return enableAssertions;
  }

  public void setEnableassertions(boolean enableAssertions) {
    this.enableAssertions = enableAssertions;
  }

  public File getApiDestDir() {
    return apiDestDir;
  }

  public void setApiDestDir(final File apiDestDir) {
    this.apiDestDir = apiDestDir;
  }

  /**
   * Adds a path for source compilation.
   *
   * @return a nested src element.
   */
  public Path createSrc() {
    if (src == null) {
      src = new Path(getProject());
    }
    return src.createPath();
  }

  /**
   * Recreate src.
   *
   * @return a nested src element.
   */
  protected Path recreateSrc() {
    src = null;
    return createSrc();
  }

  /**
   * Set the source directories to find the source joo files.
   */
  public void setSrcdir(Path srcDir) {
    if (src == null) {
      src = srcDir;
    } else {
      src.append(srcDir);
    }
  }

  /**
   * Get the source dirs to find the source java files.
   */
  public Path getSrcdir() {
    return src;
  }

  /**
   * Set the classpath to find the joo library files.
   */

  public void setClasspath(final String classpath) {
    this.classpath = classpath;
  }

  /**
   * Get the classpath to find joo library files.
   */
  public String getClasspath() {
    return classpath;
  }

  /**
   * Set the destination directory into which the Java source
   * files should be compiled.
   */
  public void setDestdir(File destDir) {
    this.destDir = destDir;
  }

  /**
   * Get the destination directory into which the java source files
   * should be compiled.
   */
  public File getDestdir() {
    return destDir;
  }

  /**
   * Indicates whether the build will continue
   * even if there are compilation errors; defaults to true.
   */
  public void setFailonerror(boolean fail) {
    failOnError = fail;
  }

  /**
   * Gets the failonerror flag.
   */
  public boolean getFailonerror() {
    return failOnError;
  }

  /**
   * Gets the allowduplicatelocalvariables flag.
   */
  public boolean isAllowduplicatelocalvariables() {
    return allowduplicatelocalvariables;
  }

  /**
   * Sets the allowduplicatelocalvariables flag.
   */
  public void setAllowduplicatelocalvariables(final boolean allowduplicatelocalvariables) {
    this.allowduplicatelocalvariables = allowduplicatelocalvariables;
  }

  /**
   * Indicates whether source should be compiled
   * with debug information; defaults to off.
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  /**
   * Gets the debug flag.
   */
  public boolean getDebug() {
    return debug;
  }

  public String getDebugLevel() {
    return debugLevel;
  }

  public void setDebugLevel(String debugLevel) {
    this.debugLevel = debugLevel;
  }

  /**
   * If true, asks the compiler for verbose output.
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /**
   * Gets the verbose flag.
   */
  public boolean getVerbose() {
    return verbose;
  }

  public String getAutoSemicolon() {
    return autoSemicolon;
  }

  public void setAutoSemicolon(final String autoSemicolon) {
    this.autoSemicolon = autoSemicolon;
  }

  /**
   * Executes the task.
   */
  public void execute() throws BuildException {
    checkParameters();
    resetFileLists();
    sourcepath = "";
    // scan source directories and dest directory to build up
    // compile lists
    String[] list = src.list();
    for (String aList : list) {
      File srcDir = getProject().resolveFile(aList);
      if (!srcDir.exists()) {
        throw new BuildException("srcdir \""
            + srcDir.getPath()
            + "\" does not exist!", getLocation());
      }
      if (!sourcepath.isEmpty()) {
        sourcepath += File.pathSeparator;
      }
      sourcepath += srcDir.getAbsolutePath();
      DirectoryScanner ds = this.getDirectoryScanner(srcDir);
      String[] files = ds.getIncludedFiles();
      scanDir(srcDir, destDir != null ? destDir : srcDir, files);
    }
    compile();
  }

  /**
   * Clear the list of files to be compiled and copied..
   */
  protected void resetFileLists() {
    compileList = new File[0];
  }

  /**
   * Scans the directory looking for source files to be compiled.
   * The results are returned in the class variable compileList
   */
  protected void scanDir(File srcDir, File destDir, String[] files) {
    GlobPatternMapper m = new GlobPatternMapper();
    m.setFrom("*" + Jooc.INPUT_FILE_SUFFIX);
    m.setTo("*.js");
    SourceFileScanner sfs = new SourceFileScanner(this);
    File[] newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);

    if (newFiles.length > 0) {
      File[] newCompileList = new File[compileList.length +
          newFiles.length];
      System.arraycopy(compileList, 0, newCompileList, 0,
          compileList.length);
      System.arraycopy(newFiles, 0, newCompileList,
          compileList.length, newFiles.length);
      compileList = newCompileList;
    }
  }

  /**
   * Gets the list of files to be compiled.
   */
  public File[] getFileList() {
    return compileList;
  }

  /**
   * Check that all required attributes have been set and nothing
   * silly has been entered.
   *
   * @since Ant 1.5
   */
  protected void checkParameters() throws BuildException {
    if (src == null) {
      throw new BuildException("srcdir attribute must be set!",
          getLocation());
    }
    if (src.size() == 0) {
      throw new BuildException("srcdir attribute must be set!",
          getLocation());
    }

    if (destDir != null && !destDir.isDirectory()) {
      throw new BuildException("destination directory \""
          + destDir
          + "\" does not exist "
          + "or is not a directory", getLocation());
    }
  }

  /**
   * Perform the compilation.
   *
   * @since Ant 1.5
   */
  protected void compile() {
    if (compileList.length > 0) {
      log("Compiling " + compileList.length +
          " joo source file"
          + (compileList.length == 1 ? "" : "s")
          + (destDir != null ? " to " + destDir : ""));

      String[] joocArgs = getJoocArgs();
      log("configured AntCompileLog");
      if (verbose) {
        StringBuffer cmdLine = new StringBuffer(100);
        cmdLine.append("jooc ");
        for (String joocArg : joocArgs) {
          cmdLine.append(" ");
          cmdLine.append(joocArg);
        }
        getProject().log(cmdLine.toString());
      }
      if (Jooc.run(joocArgs, new AntCompileLog(getProject())) != 0) {
        if (failOnError) {
          throw new BuildException(FAIL_MSG, getLocation());
        } else {
          log(FAIL_MSG, Project.MSG_ERR);
        }
      }
    } else {
      log("no *" + Jooc.INPUT_FILE_SUFFIX + " files to compile");
    }
  }

  protected String[] getJoocArgs() {
    Vector<String> args = new Vector<String>(compileList.length + 10);
    if (debug) {
      args.add("-g");
      if (debugLevel != null) {
        args.add(debugLevel);
      }
    }
    if (autoSemicolon != null) {
      args.add("-autosemicolon");
      args.add(autoSemicolon);
    }
    if (verbose) {
      args.add("-v");
    }
    if (enableAssertions) {
      args.add("-ea");
    }
    if (allowduplicatelocalvariables) {
      args.add("-ad");
    }
    if (destDir != null) {
      args.add("-d");
      args.add(destDir.getAbsolutePath());
    }
    if (apiDestDir != null) {
      args.add("-api");
      args.add(apiDestDir.getAbsolutePath());
    }
    args.add("-sourcepath");
    args.add(sourcepath);
    if (classpath != null && !classpath.isEmpty()) {
      args.add("-classpath");
      args.add(classpath);
    }
    for (File aCompileList : compileList) {
      String filename = aCompileList.getAbsolutePath();
      args.add(filename);
    }
    return args.toArray(new String[args.size()]);
  }
}
