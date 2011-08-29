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
package net.jangaroo.properties.ant;

import net.jangaroo.properties.compiler.PropertiesCompiler;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank Wienberg
 */
public class PropcTask extends MatchingTask {

  private static final String FAIL_MSG
          = "Compile failed; see the compiler error output for details.";

  private Path src;
  private File destDir;
  private String sourcepath;
  private boolean verbose = false;
  private boolean failOnError = true;
  private File[] compileList = new File[0];

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
   *
   * @param srcDir the source directories
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
   *
   * @return the source directories
   */
  public Path getSrcdir() {
    return src;
  }

  /**
   * Set the destination directory into which the Java source
   * files should be compiled.
   *
   * @param destDir the destination directory
   */
  public void setDestdir(File destDir) {
    this.destDir = destDir;
  }

  /**
   * Get the destination directory into which the java source files
   * should be compiled.
   *
   * @return the destination directory
   */
  public File getDestdir() {
    return destDir;
  }

  /**
   * Indicates whether the build will continue
   * even if there are compilation errors; defaults to true.
   *
   * @param fail whether to fail on errors
   */
  public void setFailonerror(boolean fail) {
    failOnError = fail;
  }

  /**
   * Gets the failonerror flag.
   *
   * @return whether to fail on errors
   */
  public boolean getFailonerror() {
    return failOnError;
  }


  /**
   * If true, asks the compiler for verbose output.
   *
   * @param verbose the verbose state
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /**
   * Gets the verbose flag.
   *
   * @return the verbose state
   */
  public boolean getVerbose() {
    return verbose;
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
   *
   * @param srcDir  the source directory
   * @param destDir the destination directory
   * @param files   the files to scan
   */
  protected void scanDir(File srcDir, File destDir, String[] files) {
    GlobPatternMapper m = new GlobPatternMapper();
    m.setFrom("*.properties");
    m.setTo("*.as");
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
   *
   * @return the list of files to be compiled
   */
  public File[] getFileList() {
    return compileList.clone();
  }

  /**
   * Check that all required attributes have been set and nothing
   * silly has been entered.
   *
   * @throws org.apache.tools.ant.BuildException
   *          when a build error occurred
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
              " properties source file"
              + (compileList.length == 1 ? "" : "s")
              + (destDir != null ? " to " + destDir : ""));

      String[] joocArgs = getArgs();
      if (PropertiesCompiler.run(joocArgs) != 0) {
        if (failOnError) {
          throw new BuildException(FAIL_MSG, getLocation());
        } else {
          log(FAIL_MSG, Project.MSG_ERR);
        }
      }
    } else {
      log("no *.properties files to compile");
    }
  }

  protected String[] getArgs() {
    List<String> args = new ArrayList<String>(compileList.length + 10);
    if (destDir != null) {
      args.add("-d");
      args.add(destDir.getAbsolutePath());
    }
    args.add("-sourcepath");
    args.add(sourcepath);
   
    for (File aCompileList : compileList) {
      String filename = aCompileList.getAbsolutePath();
      args.add(filename);
    }
    return args.toArray(new String[args.size()]);
  }
}
