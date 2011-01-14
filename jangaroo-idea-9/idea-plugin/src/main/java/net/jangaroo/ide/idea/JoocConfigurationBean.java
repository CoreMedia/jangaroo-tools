/*
 * Copyright 2009 CoreMedia AG
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
package net.jangaroo.ide.idea;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.LocalFileSystem;

import java.io.File;
import java.net.URI;

/**
 * IDEA serialization adapter of JoocConfiguration. 
 */
public class JoocConfigurationBean {
  public static final int DEBUG_LEVEL_NONE = 0;
  public static final int DEBUG_LEVEL_LINES = 50;
  public static final int DEBUG_LEVEL_SOURCE = 100;

  public int debugLevel = DEBUG_LEVEL_SOURCE;
  public boolean verbose = false;
  public boolean enableAssertions = true;
  public boolean allowDuplicateLocalVariables = false;
  public String outputPrefix;
  public String outputDirectory = "target/jangaroo-output/joo/classes";
  public boolean showCompilerInfoMessages = false;
  public static final String IDEA_URL_PREFIX = "file://";

  public JoocConfigurationBean() {
  }

  public boolean isDebug() {
    return debugLevel > DEBUG_LEVEL_NONE;
  }

  public boolean isDebugLines() {
    return debugLevel >= DEBUG_LEVEL_LINES;
  }

  public boolean isDebugSource() {
    return debugLevel >= DEBUG_LEVEL_SOURCE;
  }

  public File getOutputDirectory() {
    File outputDir = new File(getPath(outputDirectory));
    if (!outputDir.isAbsolute() && outputPrefix != null && outputPrefix.length() > 0) {
      outputDir = new File(outputPrefix + outputDir.getPath());
    }
    return outputDir;
  }

  public File getApiOutputDirectory() {
    // TODO: make configurable
    return new File(getOutputDirectory(), "../../META-INF/joo-api");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    JoocConfigurationBean that = (JoocConfigurationBean)o;

    boolean[] flags = getFlags();
    boolean[] thatFlags = that.getFlags();
    for (int i = 0; i < flags.length; i++) {
      if (flags[i] != thatFlags[i])
        return false;
    }
    //noinspection StringEquality
    return debugLevel==that.debugLevel
      && (outputPrefix==null ? that.outputPrefix==null : outputPrefix.equals(that.outputPrefix))
      && outputDirectory.equals(that.outputDirectory);
  }

  @Override
  public int hashCode() {
    int result = 0;
    for (boolean flag : getFlags()) {
      result = 31 * result + (flag ? 1 : 0);
    }
    result = 31 * result + debugLevel;
    result = 31 * result + outputDirectory.hashCode();
    return result;
  }

  private boolean[] getFlags() {
    return new boolean[]{verbose, enableAssertions,
      allowDuplicateLocalVariables, showCompilerInfoMessages};
  }

  public static String getPath(String ideaUrl) {
    if (ideaUrl.startsWith(IDEA_URL_PREFIX)) {
      try {
        return new File(new URI(VfsUtil.fixIDEAUrl(ideaUrl))).getPath();
      } catch (Exception e) {
        ideaUrl = ideaUrl.substring(IDEA_URL_PREFIX.length());
      }
    }
    return ideaUrl.replace('/', File.separatorChar);
  }

  public static String getIdeaUrl(String path) {
    path = path.trim();
    if (path.length()==0) {
      return path;
    }
    VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(path);
    return virtualFile==null ? IDEA_URL_PREFIX + path.replace(File.separatorChar, '/') : virtualFile.getUrl();
  }
}
