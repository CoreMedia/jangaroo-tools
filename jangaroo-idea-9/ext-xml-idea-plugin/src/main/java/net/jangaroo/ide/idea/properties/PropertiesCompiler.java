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
package net.jangaroo.ide.idea.properties;

import com.intellij.compiler.impl.CompilerUtil;
import com.intellij.compiler.make.MakeUtil;
import com.intellij.facet.FacetManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.IntermediateOutputCompiler;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Chunk;
import freemarker.template.TemplateException;
import net.jangaroo.ide.idea.exml.ExmlCompiler;
import net.jangaroo.ide.idea.exml.ExmlFacetType;
import net.jangaroo.ide.idea.util.OutputSinkItem;
import net.jangaroo.properties.PropertiesFileScanner;
import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.LocalizationSuite;
import org.apache.maven.shared.model.fileset.FileSet;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class PropertiesCompiler implements TranslatingCompiler, IntermediateOutputCompiler {

  @NotNull
  public String getDescription() {
    return "Jangaroo Properties Compiler";
  }

  public boolean validateConfiguration(CompileScope scope) {
    // TODO: what to check beforehand?
    return true;
  }

  public boolean isCompilableFile(VirtualFile file, CompileContext context) {
    // TODO: check source path!
    if ("properties".equals(file.getExtension())) {
      Module module = context.getModuleByFile(file);
      if (module != null) {
        if (FacetManager.getInstance(module).getFacetByType(ExmlFacetType.ID) != null  // must have EXML Facet!
          && !MakeUtil.getSourceRoot(context, module, file).getPath().endsWith("/webapp")) { // hack: skip all files under .../webapp
          return true;
        }
      }
    }
    return false;
  }

  public void compile(final CompileContext context, Chunk<Module> moduleChunk, final VirtualFile[] files, final OutputSink outputSink) {
    final Collection<OutputSinkItem> outputs = new ArrayList<OutputSinkItem>();
    final Map<Module, List<VirtualFile>> filesByModule = CompilerUtil.buildModuleToFilesMap(context, files);

    ApplicationManager.getApplication().runReadAction(new Runnable() {

      public void run() {
        for (Module module : filesByModule.keySet()) {
          String generatedAs3RootDir = ExmlCompiler.findGeneratedAs3RootDir(module);
          if (generatedAs3RootDir == null) {
            continue; // no valid output directory configuration found, ignore module.
          }
          boolean showCompilerInfoMessages = ExmlCompiler.getExmlConfig(module).isShowCompilerInfoMessages();
          try {
            OutputSinkItem outputSinkItem = new OutputSinkItem(generatedAs3RootDir);

            List<VirtualFile> filesOfModule = filesByModule.get(module);
            getLog().info(module.getName() + ": " + filesOfModule);

            Collection<FileSet> fileSets = computeSourceFileSets(context, module, filesOfModule);

            compileProperties(outputSinkItem.getOutputRoot(), fileSets);

            populateOutputSinkItem(context, module, outputSinkItem, showCompilerInfoMessages, filesOfModule);
            outputs.add(outputSinkItem);
          } catch (SecurityException e) {
            String message = "Output directory '" + generatedAs3RootDir + "' does not exist and could not be created: " + e.getMessage();
            context.addMessage(CompilerMessageCategory.ERROR, message, null, -1, -1);
            getLog().error(message);
          }

        }

      }
    });
    for (OutputSinkItem outputSinkItem : outputs) {
      outputSinkItem.addTo(outputSink);
    }
  }

  // prepare all handed-in virtual files as FileSets to give to properties compiler:
  private static Collection<FileSet> computeSourceFileSets(CompileContext context, Module module,
                                                           List<VirtualFile> filesOfModule) {
    Map<String,FileSet> fileSetBySourceRootDir = new HashMap<String, FileSet>();
    for (VirtualFile file : filesOfModule) {
      String sourceRootDir = getSourceRootPath(context, module, file);
      FileSet fileSet = fileSetBySourceRootDir.get(sourceRootDir);
      if (fileSet == null) {
        fileSet = new FileSet();
        fileSet.setDirectory(sourceRootDir);
        fileSetBySourceRootDir.put(sourceRootDir, fileSet);
      }
      String path = getRelativeSourcePath(sourceRootDir, file);
      fileSet.addInclude(path);
    }
    return fileSetBySourceRootDir.values();
  }

  private static void compileProperties(File outputDirectory, Collection<FileSet> fileSets) {
    for (FileSet fileSet : fileSets) {
      LocalizationSuite suite = new LocalizationSuite(fileSet, outputDirectory);
      PropertiesFileScanner scanner = new PropertiesFileScanner(suite);
      try {
        scanner.scan();
        PropertyClassGenerator generator = new PropertyClassGenerator(suite);
        try {
          generator.generate();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (TemplateException e) {
          e.printStackTrace();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static void populateOutputSinkItem(CompileContext context, Module module, OutputSinkItem outputSinkItem, boolean showCompilerInfoMessages, List<VirtualFile> filesOfModule) {
    String outputDirectoryPath = outputSinkItem.getOutputRootPath();
    for (VirtualFile sourceFile : filesOfModule) {
      String outputFilePath = computeOutputFilePath(getRelativeSourcePath(context, module, sourceFile), outputDirectoryPath);
      File outputFile = new File(outputFilePath);
      if (outputFile.exists()) {
        outputSinkItem.addOutputItem(sourceFile, outputFile);
        if (showCompilerInfoMessages) {
          context.addMessage(CompilerMessageCategory.INFORMATION, "properties->as (" + outputFilePath + ")", sourceFile.getUrl(), -1, -1);
        }
      } else {
        outputSinkItem.addFileToRecompile(sourceFile);
        context.addMessage(CompilerMessageCategory.WARNING, "failed: properties->as (" + outputFilePath + ")", sourceFile.getUrl(), -1, -1);
      }
    }
  }

  private static String getRelativeSourcePath(String sourceRootDir, VirtualFile file) {
    return file.getPath().substring(sourceRootDir.length() + 1);
  }

  private static String getSourceRootPath(CompileContext context, Module module, VirtualFile file) {
    return VfsUtil.virtualToIoFile(MakeUtil.getSourceRoot(context, module, file)).getPath();
  }

  private static String getRelativeSourcePath(CompileContext context, Module module, VirtualFile file) {
    return getRelativeSourcePath(getSourceRootPath(context, module, file), file);
  }

  // TODO: put the following logic as public API into Properties!
  private static String computeOutputFilePath(String relativeSourceFilePath, String outputDirectoryPath) {
    // cut off ".properties" extension:
    String outputFilePath = FileUtil.getNameWithoutExtension(relativeSourceFilePath);
    String suffix = "";
    // find locale suffix position:
    int firstUnderscoreInNamePos = outputFilePath.indexOf('_', outputFilePath.lastIndexOf('/'));
    if (firstUnderscoreInNamePos != -1) {
      // split into prefix and suffix:
      suffix = outputFilePath.substring(firstUnderscoreInNamePos, outputFilePath.length());
      outputFilePath = outputFilePath.substring(0, firstUnderscoreInNamePos);
    }
    outputFilePath = outputDirectoryPath + "/" + outputFilePath + "_properties" + suffix + ".as";
    return outputFilePath;
  }

  private static Logger getLog() {
    return Logger.getInstance("net.jangaroo.ide.idea.JangarooPropertiesCompiler");
  }

}