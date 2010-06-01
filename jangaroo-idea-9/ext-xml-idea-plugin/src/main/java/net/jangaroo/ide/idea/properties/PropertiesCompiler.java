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

import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
import com.intellij.compiler.make.MakeUtil;
import com.intellij.facet.FacetManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.IntermediateOutputCompiler;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Chunk;
import freemarker.template.TemplateException;
import net.jangaroo.ide.idea.JangarooFacetType;
import net.jangaroo.ide.idea.exml.ExmlCompiler;
import net.jangaroo.ide.idea.exml.ExmlFacetType;
import net.jangaroo.properties.PropertiesFileScanner;
import net.jangaroo.properties.PropertyClassGenerator;
import net.jangaroo.properties.model.LocalizationSuite;
import org.apache.maven.shared.model.fileset.FileSet;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
      if (module != null && FacetManager.getInstance(module).getFacetByType(JangarooFacetType.ID) != null) {
        return true;
      }
    }
    return false;
  }

  public void compile(CompileContext context, Chunk<Module> moduleChunk, VirtualFile[] files, OutputSink outputSink) {
    Map<Module,List<VirtualFile>> filesByModule = new HashMap<Module, List<VirtualFile>>(files.length);
    for (final VirtualFile file : files) {
      Module module = context.getModuleByFile(file);
      // ignore modules without EXML facet:
      if (FacetManager.getInstance(module).getFacetByType(ExmlFacetType.ID) == null) {
        continue;
      }
      // hack: skip all files under .../webapp:
      VirtualFile sourceRoot = MakeUtil.getSourceRoot(context, module, file);
      if (sourceRoot.getPath().endsWith("/webapp")) {
        continue;
      }
      List<VirtualFile> filesOfModule = filesByModule.get(module);
      if (filesOfModule==null) {
        filesOfModule = new ArrayList<VirtualFile>(files.length);
        filesByModule.put(module, filesOfModule);
      }
      filesOfModule.add(file);
    }
    for (Map.Entry<Module, List<VirtualFile>> filesOfModuleEntry : filesByModule.entrySet()) {
      Module module = filesOfModuleEntry.getKey();
      String generatedAs3RootDir = ExmlCompiler.findGeneratedAs3RootDir(module);
      if (generatedAs3RootDir == null) {
        continue; // no valid output directory configuration found, ignore module.
      }
      File outputDirectory = new File(generatedAs3RootDir);
      boolean showCompilerInfoMessages = ExmlCompiler.getExmlConfig(module).isShowCompilerInfoMessages();
      List<OutputItem> outputItems = new ArrayList<OutputItem>(files.length);
      List<VirtualFile> filesToRecompile = new ArrayList<VirtualFile>(files.length);
      getLog().info(module.getName() + ": " + filesOfModuleEntry.getValue());
      FileSet fileSet = new FileSet();
      VirtualFile outputDirectoryVirtualFile = outputDirectory.mkdirs()
        ? LocalFileSystem.getInstance().refreshAndFindFileByIoFile(outputDirectory)
        : LocalFileSystem.getInstance().findFileByIoFile(outputDirectory);
      if (outputDirectoryVirtualFile == null) {
        String message = "Output directory does not exist and could not be created: " + outputDirectory.getPath();
        context.addMessage(CompilerMessageCategory.ERROR, message, null, -1, -1);
        getLog().warn(message);
        return;
      }
      String outputDirectoryPath = outputDirectoryVirtualFile.getPath();
      String sourceRootDir = null;
      for (VirtualFile file : filesOfModuleEntry.getValue()) {
        // TODO: we assume that all properties files are under the same source root. This must not be true!
        if (sourceRootDir == null) {
          fileSet = new FileSet();
          sourceRootDir = VfsUtil.virtualToIoFile(MakeUtil.getSourceRoot(context, module, file)).getPath();
          getLog().info("-in " + sourceRootDir);
          fileSet.setDirectory(sourceRootDir);
        }
        String path = file.getPath().substring(sourceRootDir.length() + 1);
        fileSet.addInclude(path);
        getLog().info(" ..." + path);
        String outputFilePath = computeOutputFilePath(path, outputDirectoryPath);
        outputItems.add(new OutputItemImpl(outputFilePath, file));
      }

      LocalizationSuite suite = new LocalizationSuite(fileSet, outputDirectory);
      PropertiesFileScanner scanner = new PropertiesFileScanner(suite);
      try {
        scanner.scan();
        PropertyClassGenerator generator = new PropertyClassGenerator(suite);
        try {
          generator.generate();
        } catch (IOException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TemplateException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      } catch (IOException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }

      for (Iterator<OutputItem> iterator = outputItems.iterator(); iterator.hasNext();) {
        OutputItem outputItem = iterator.next();
        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(outputItem.getOutputPath());
        VirtualFile sourceFile = outputItem.getSourceFile();
        if (virtualFile == null) {
          context.addMessage(CompilerMessageCategory.WARNING, "failed: properties->as (" + outputItem.getOutputPath() + ")", sourceFile.getUrl(), -1, -1);
          iterator.remove();
          filesToRecompile.add(sourceFile);
        } else if (showCompilerInfoMessages) {
          context.addMessage(CompilerMessageCategory.INFORMATION, "properties->as (" + outputItem.getOutputPath() + ")", sourceFile.getUrl(), -1, -1);
        }
      }
      outputSink.add(outputDirectoryPath, outputItems, filesToRecompile.toArray(new VirtualFile[filesToRecompile.size()]));
    }
  }

  // TODO: put the following logic as public API into Properties!
  private static String computeOutputFilePath(String sourceFilePath, String outputDirectoryPath) {
    // cut off ".properties" extension:
    String outputFilePath = sourceFilePath.substring(0, sourceFilePath.length() - ".properties".length());
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