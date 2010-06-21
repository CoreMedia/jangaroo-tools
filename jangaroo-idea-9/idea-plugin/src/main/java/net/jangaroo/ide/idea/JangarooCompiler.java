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

import com.intellij.compiler.impl.CompilerUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.facet.FacetManager;
import com.intellij.compiler.make.MakeUtil;
import com.intellij.util.Chunk;
import net.jangaroo.ide.idea.util.OutputSinkItem;
import org.jetbrains.annotations.NotNull;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.CompileLog;
import net.jangaroo.jooc.JooSymbol;

import java.util.*;
import java.io.File;

/**
 *
 */
public class JangarooCompiler implements TranslatingCompiler {

  @NotNull
  public String getDescription() {
    return "Jangaroo Compiler";
  }

  public boolean validateConfiguration(CompileScope scope) {
    // TODO: what to check beforehand?
    return true;
  }

  public boolean isCompilableFile(VirtualFile file, CompileContext context) {
    // Does not work due to ClassLoader problems:
    // return JavaScriptSupportLoader.ECMA_SCRIPT_L4.equals(JavaScriptSupportLoader.getLanguageDialect(file));
    return Jooc.AS_SUFFIX_NO_DOT.equals(file.getExtension())
      && (file.getParent() == null || !file.getParent().getPath().endsWith("/joo-api")); // hack: skip all files under .../joo-api
  }

  private OutputSinkItem compile(CompileContext context, Module module, final List<VirtualFile> files) {
    JoocConfiguration joocConfig = getJoocConfiguration(module, files);
    OutputSinkItem outputSinkItem = null;
    if (joocConfig!=null) {
      ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
      VirtualFile[] allSourceRoots = moduleRootManager.getSourceRoots();
      // TODO: to filter out test sources, we could use moduleRootManager.getFileIndex().isInTestSourceContent(<virtualFile>)
      List<File> sourcePath = virtualToIoFiles(Arrays.asList(allSourceRoots));
      joocConfig.setSourcePath(sourcePath);
      String outputDirectoryPath = joocConfig.getOutputDirectory().getPath();
      try {
        outputSinkItem = new OutputSinkItem(outputDirectoryPath);
        IdeaCompileLog ideaCompileLog = new IdeaCompileLog(context);
        new Jooc(ideaCompileLog).run(joocConfig);
        for (final VirtualFile file : files) {
          if (ideaCompileLog.hasErrors(file)) {
            outputSinkItem.addFileToRecompile(file);
          } else {
            File outputFile = computeOutputFile(context, module, outputDirectoryPath, file);
            outputSinkItem.addOutputItem(file, outputFile);
            String fileUrl = file.getUrl();
            if (joocConfig.showCompilerInfoMessages) {
              context.addMessage(CompilerMessageCategory.INFORMATION, "as->js (" + outputFile.getPath() + ")", fileUrl, -1, -1);
            }
            getLog().info("as->js: " + fileUrl + " -> " + outputFile.getPath());
          }
        }
      } catch (SecurityException e) {
        String message = "Output directory " + outputDirectoryPath + " does not exist and could not be created: " + e.getMessage();
        context.addMessage(CompilerMessageCategory.ERROR, message, null, -1, -1);
        getLog().warn(message);
      }
    }
    return outputSinkItem;
  }

  // a little hack so we don't need another wrapper:
  private static class JoocConfiguration extends net.jangaroo.jooc.config.JoocConfiguration {
    public boolean showCompilerInfoMessages;
  }

  private JoocConfiguration getJoocConfiguration(Module module, List<VirtualFile> virtualSourceFiles) {
    JangarooFacet jangarooFacet = FacetManager.getInstance(module).getFacetByType(JangarooFacetType.ID);
    if (jangarooFacet==null) {
      return null;
    }
    JoocConfigurationBean joocConfigurationBean = jangarooFacet.getConfiguration().getState();
    JoocConfiguration joocConfig = new JoocConfiguration();
    joocConfig.setVerbose(joocConfigurationBean.verbose);
    joocConfig.setDebug(joocConfigurationBean.isDebug());
    joocConfig.setDebugLines(joocConfigurationBean.isDebugLines());
    joocConfig.setDebugSource(joocConfigurationBean.isDebugSource());
    joocConfig.setAllowDuplicateLocalVariables(joocConfigurationBean.allowDuplicateLocalVariables);
    joocConfig.setEnableAssertions(joocConfigurationBean.enableAssertions);
    joocConfig.setEnableGuessingClasses(joocConfigurationBean.enableGuessingClasses);
    joocConfig.setEnableGuessingMembers(joocConfigurationBean.enableGuessingMembers);
    joocConfig.setMergeOutput(false); // no longer supported: joocConfigurationBean.mergeOutput;
    if (joocConfigurationBean.mergeOutput) {
      File outputFile = new File(joocConfigurationBean.getOutputFileName());
      joocConfig.setOutputDirectory(outputFile.getParentFile());
      joocConfig.setOutputFileName(outputFile.getName());
    } else {
      joocConfig.setOutputDirectory(joocConfigurationBean.getOutputDirectory());
    }
    List<File> sourceFiles = virtualToIoFiles(virtualSourceFiles);
    joocConfig.setSourceFiles(sourceFiles);
    joocConfig.showCompilerInfoMessages = joocConfigurationBean.showCompilerInfoMessages;
    return joocConfig;
  }

  private static List<File> virtualToIoFiles(List<VirtualFile> virtualFiles) {
    List<File> ioFiles = new ArrayList<File>(virtualFiles.size());
    for (VirtualFile virtualSourceFile : virtualFiles) {
      ioFiles.add(VfsUtil.virtualToIoFile(virtualSourceFile));
    }
    return ioFiles;
  }

  private @NotNull File computeOutputFile(CompileContext context, Module module, final String outputDirectory, final VirtualFile file) {
    VirtualFile sourceRoot = MakeUtil.getSourceRoot(context, module, file);
    if (sourceRoot==null) {
      throw new IllegalStateException("File not under any source root: '" + file.getPath() + "'.");
    }
    String filePath = file.getPath();
    String relativePath = filePath.substring(sourceRoot.getPath().length(), filePath.lastIndexOf('.'));
    String outputFilePath = outputDirectory + relativePath + Jooc.OUTPUT_FILE_SUFFIX;
    return new File(outputFilePath);
  }

  public void compile(final CompileContext context, Chunk<Module> moduleChunk, final VirtualFile[] files, final OutputSink outputSink) {
    final Collection<OutputSinkItem> outputs = new ArrayList<OutputSinkItem>();
    final Map<Module, List<VirtualFile>> filesByModule = CompilerUtil.buildModuleToFilesMap(context, files);

    ApplicationManager.getApplication().runReadAction(new Runnable() {

      public void run() {
        for (Map.Entry<Module, List<VirtualFile>> filesOfModuleEntry : filesByModule.entrySet()) {
          OutputSinkItem outputSinkItem = compile(context, filesOfModuleEntry.getKey(), filesOfModuleEntry.getValue());
          if (outputSinkItem != null) {
            outputs.add(outputSinkItem);
          }
        }
      }

    });
    for (OutputSinkItem outputSinkItem : outputs) {
      outputSinkItem.addTo(outputSink);
    }
  }

  private static Logger getLog() {
    return Logger.getInstance("net.jangaroo.ide.idea.JangarooCompiler");
  }

  private static class IdeaCompileLog implements CompileLog {
    private CompileContext compileContext;
    private Set<VirtualFile> filesWithErrors;

    private IdeaCompileLog(CompileContext compileContext) {
      this.compileContext = compileContext;
      filesWithErrors = new HashSet<VirtualFile>();
    }

    private VirtualFile addMessage(CompilerMessageCategory compilerMessageCategory, String msg, JooSymbol sym) {
      VirtualFile file = LocalFileSystem.getInstance().findFileByPath(sym.getFileName());
      String fileUrl = file==null ? null : file.getUrl();
      compileContext.addMessage(compilerMessageCategory, msg, fileUrl, sym.getLine(), sym.getColumn()-1);
      return file;
    }

    public void error(JooSymbol sym, String msg) {
      filesWithErrors.add(addMessage(CompilerMessageCategory.ERROR, msg, sym));
      getLog().debug(sym.getFileName() + ": Jangaroo Compile Error: " + msg);
    }

    public void error(String msg) {
      compileContext.addMessage(CompilerMessageCategory.ERROR, msg, null, -1, -1);
      getLog().debug("Jangaroo Compile Error: " + msg);
    }

    public void warning(JooSymbol sym, String msg) {
      addMessage(CompilerMessageCategory.WARNING, msg, sym);
      getLog().debug(sym.getFileName() + ": Jangaroo Compile Warning: " + msg);
    }

    public void warning(String msg) {
      compileContext.addMessage(CompilerMessageCategory.WARNING, msg, null, -1, -1);
      getLog().debug("Jangaroo Compile Warning: " + msg);
    }

    public boolean hasErrors() {
      return compileContext.getMessageCount(CompilerMessageCategory.ERROR)>0;
    }

    public boolean hasErrors(VirtualFile file) {
      return filesWithErrors.contains(file);
    }
  }
}
