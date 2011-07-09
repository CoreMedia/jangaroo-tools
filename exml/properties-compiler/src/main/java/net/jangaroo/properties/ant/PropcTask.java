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

import freemarker.template.TemplateException;
import net.jangaroo.properties.PropertiesFileScanner;
import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.LocalizationSuite;
import net.jangaroo.utils.log.AntLogHandler;
import net.jangaroo.utils.log.Log;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Frank Wienberg
 */
public class PropcTask extends MatchingTask {

  private static final String FAIL_MSG
    = "Compile failed; see the compiler error output for details.";

  private File srcDir;
  private File destDir;
  private boolean failOnError = true;

  /**
   * Indicates whether the build will continue even if there are compilation errors; defaults to true.
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
   * Set the source directory to find the source properties files.
   */
  public void setSrcdir(File srcDir) {
    this.srcDir = srcDir;
  }

  /**
   * Set the destination directory where to create the resulting AS3 files.
   */
  public void setDestDir(File destDir) {
    this.destDir = destDir;
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
   */
  protected void checkParameters() throws BuildException {
    if (srcDir == null || !srcDir.isDirectory()) {
      throw new BuildException("source directory \""
        + srcDir
        + "\" does not exist "
        + "or is not a directory", getLocation());
    }
    if (destDir == null || !destDir.isDirectory()) {
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
    log("Compiling *.properties from " + srcDir +
      " to *.as "
      + (destDir != null ? " at " + destDir : ""));

    Log.setLogHandler(new AntLogHandler(getProject()));
    FileSet sourceFiles = new FileSet();
    sourceFiles.setDirectory(srcDir.getPath());
    sourceFiles.setIncludes(Collections.singletonList("**/*.properties"));
    LocalizationSuite suite = new LocalizationSuite(sourceFiles, destDir);

    PropertiesFileScanner scanner = new PropertiesFileScanner(suite);
    try {
      scanner.scan();
    } catch (IOException e) {
      throw new BuildException("Scan failure", e);
    }

    PropertyClassGenerator generator = new PropertyClassGenerator(suite);
    try {
      generator.generate();
    } catch (IOException e) {
      handleError(e);
    } catch (TemplateException e) {
      handleError(e);
    }
  }

  private void handleError(Exception e) {
    if (failOnError) {
      throw new BuildException(FAIL_MSG, e, getLocation());
    } else {
      log(FAIL_MSG, Project.MSG_ERR);
    }
  }
}
