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

package net.jangaroo.exml.ant;

import net.jangaroo.exml.ExmlcException;
import net.jangaroo.exml.compiler.Exmlc;
import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.utils.log.AntLogHandler;
import net.jangaroo.utils.log.Log;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Frank Wienberg
 */
@SuppressWarnings({"UnusedDeclaration"})
public class ExmlcTask extends MatchingTask {

  private static final String FAIL_MSG
    = "Compile failed; see the compiler error output for details.";

  private String targetNamespace;
  private String namespacePrefix;
  private File xsdOutputFile;
  private Path src;
  private File destDir;
  private List<Path> importedXsds = new ArrayList<Path>();
  private boolean verbose = false;
  private boolean failOnError = true;

  /**
   * Indicates whether the build will continue
   * even if there are compilation errors; defaults to true.
   * @param fail whether the build will fail on error or not
   */
  public void setFailonerror(boolean fail) {
    failOnError = fail;
  }

  /**
   * Gets the failonerror flag.
   * @return whether the build will fail on error or not
   */
  public boolean getFailonerror() {
    return failOnError;
  }

  public void setTargetNamespace(String targetNamespace) {
    this.targetNamespace = targetNamespace;
  }

  public void setNamespacePrefix(String namespacePrefix) {
    this.namespacePrefix = namespacePrefix;
  }

  public void setXsdOutputFile(File xsdOutputFile) {
    this.xsdOutputFile = xsdOutputFile;
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
   * Set the source directories to find the source EXML files.
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
   * @return the source directories
   */
  public Path getSrcdir() {
    return src;
  }

  public void setDestDir(File destDir) {
    this.destDir = destDir;
  }

  public void addImportedXsds(Path importedXsdFile) {
    importedXsds.add(importedXsdFile);
  }

  /**
   * If true, asks the compiler for verbose output.
   * @param verbose the verbose status
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /**
   * Gets the verbose flag.
   * @return the verbose status
   */
  public boolean getVerbose() {
    return verbose;
  }

  /**
   * Executes the task.
   */
  public void execute() throws BuildException {
    checkParameters();
    compile();
  }

  /**
   * Check that all required attributes have been set and nothing silly has been entered.
   * @throws org.apache.tools.ant.BuildException if a build error occurred
   */
  protected void checkParameters() {
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
   */
  protected void compile() {
    log("Compiling *.exml from " + src +
      " to *.as "
      + (destDir != null ? " at " + destDir : ""));

    String[] exmlcArgs = getExmlcArgs();
    Log.setLogHandler(new AntLogHandler(getProject()));
    log("configured AntCompileLog");
    if (verbose) {
      StringBuilder cmdLine = new StringBuilder(100);
      cmdLine.append("exmlc ");
      for (String exmlcArg : exmlcArgs) {
        cmdLine.append(" ");
        cmdLine.append(exmlcArg);
      }
      getProject().log(cmdLine.toString());
    }
    ExmlConfiguration config = new ExmlConfiguration();
    try {
      new Exmlc(config).generateAllConfigClasses();
    } catch (ExmlcException e) {
      if (failOnError) {
        log(e.getMessage(), Project.MSG_ERR);
        throw new BuildException(FAIL_MSG, e, getLocation());
      } else {
        e.printStackTrace(); // NOSONAR sorry, this is Ant, we do not have a proper log
        log(FAIL_MSG + " Exception: " + e, Project.MSG_ERR);
      }
    }
  }

  protected String[] getExmlcArgs() {
    List<String> args = new ArrayList<String>();
    Project project = getProject();
    String targetNamespaceArg = targetNamespace == null ? project.getName() : targetNamespace;
    args.add(targetNamespaceArg);
    String namespacePrefixArg = namespacePrefix == null ? project.getName() : namespacePrefix;
    args.add(namespacePrefixArg);
    args.add(xsdOutputFile.getAbsolutePath());
    args.add(src.toString());
    String destDirArg = destDir == null ? "." : destDir.getAbsolutePath();
    args.add(destDirArg);
    for (Path importedXsd : importedXsds) {
      Collections.addAll(args, importedXsd.list());
    }
    return args.toArray(new String[args.size()]);
  }
}
