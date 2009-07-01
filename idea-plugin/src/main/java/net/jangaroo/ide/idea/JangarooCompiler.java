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

import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.module.Module;
import com.intellij.facet.FacetManager;
import com.intellij.compiler.make.MakeUtil;
import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
import org.jetbrains.annotations.NotNull;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.CompileLog;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.config.JoocConfiguration;

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
    return Jooc.AS_SUFFIX_NO_DOT.equals(file.getExtension());
  }

  private void compile(CompileContext context, Module module, final List<VirtualFile> files, List<OutputItem> outputItems, List<VirtualFile> filesToRecompile) {
    final VirtualFile outputDirectory = context.getModuleOutputDirectory(module);
    assert outputDirectory!=null;

    JoocConfiguration joocConfig = getJoocConfiguration(module, files);
    if (joocConfig!=null) {
      String outputDirectoryPath = joocConfig.getOutputDirectory().getPath();
      VirtualFile outputDirectoryVirtualFile = LocalFileSystem.getInstance().findFileByPath(outputDirectoryPath);
      if (outputDirectoryVirtualFile == null) {
        throw new IllegalStateException("Output directory does not exit: "+outputDirectoryPath);
      }
      outputDirectoryPath = outputDirectoryVirtualFile.getPath();
      String outputFileName = outputDirectoryPath + "/" + joocConfig.getOutputFileName();
      IdeaCompileLog ideaCompileLog = new IdeaCompileLog(context);
      new Jooc(ideaCompileLog).run(joocConfig);
      for (final VirtualFile file : files) {
        if (ideaCompileLog.hasErrors(file)) {
          filesToRecompile.add(file);
        } else {
          OutputItem outputItem = joocConfig.isMergeOutput()
            ? new OutputItemImpl(outputDirectoryPath, outputFileName, file)
            : createOutputItem(outputDirectoryPath, MakeUtil.getSourceRoot(context, module, file), file);
          outputItems.add(outputItem);
        }
      }
    }
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
    joocConfig.setMergeOutput(joocConfigurationBean.mergeOutput);
    if (joocConfigurationBean.mergeOutput) {
      File outputFile = new File(joocConfigurationBean.getOutputFileName());
      joocConfig.setOutputDirectory(outputFile.getParentFile());
      joocConfig.setOutputFileName(outputFile.getName());
    } else {
      joocConfig.setOutputDirectory(joocConfigurationBean.getOutputDirectory());
    }
    List<File> sourceFiles = new ArrayList<File>(virtualSourceFiles.size());
    for (VirtualFile virtualSourceFile : virtualSourceFiles) {
      sourceFiles.add(VfsUtil.virtualToIoFile(virtualSourceFile));
    }
    joocConfig.setSourceFiles(sourceFiles);
    return joocConfig;
  }

  private OutputItem createOutputItem(final String outputDirectory, VirtualFile sourceRoot, final VirtualFile file) {
    if (sourceRoot==null) {
      throw new IllegalStateException("File not under any source root: '" + file.getPath() + "'.");
    }
    String filePath = file.getPath();
    String relativePath = filePath.substring(sourceRoot.getPath().length(), filePath.lastIndexOf('.'));
    String outputFilePath = outputDirectory + relativePath + Jooc.OUTPUT_FILE_SUFFIX;
    return new OutputItemImpl(outputDirectory, outputFilePath, file);
  }

  public ExitStatus compile(CompileContext context, VirtualFile[] files) {
    List<OutputItem> outputItems = new ArrayList<OutputItem>(files.length);
    List<VirtualFile> filesToRecompile = new ArrayList<VirtualFile>(files.length);
    Map<Module,List<VirtualFile>> filesByModule = new HashMap<Module, List<VirtualFile>>(files.length);
    for (final VirtualFile file : files) {
      Module module = context.getModuleByFile(file);
      List<VirtualFile> filesOfModule = filesByModule.get(module);
      if (filesOfModule==null) {
        filesOfModule = new ArrayList<VirtualFile>(files.length);
        filesByModule.put(module, filesOfModule);
      }
      filesOfModule.add(file);
    }
    for (Map.Entry<Module, List<VirtualFile>> filesOfModuleEntry : filesByModule.entrySet()) {
      compile(context, filesOfModuleEntry.getKey(), filesOfModuleEntry.getValue(), outputItems, filesToRecompile);
    }
    final OutputItem[] outputItemArray = outputItems.toArray(new OutputItem[outputItems.size()]);
    final VirtualFile[] filesToRecompileArray = filesToRecompile.toArray(new VirtualFile[filesToRecompile.size()]);
    return new ExitStatus() {
      public OutputItem[] getSuccessfullyCompiled() {
        return outputItemArray;
      }
      public VirtualFile[] getFilesToRecompile() {
        return filesToRecompileArray;
      }
    };
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
    }

    public void error(String msg) {
      compileContext.addMessage(CompilerMessageCategory.ERROR, msg, null, -1, -1);
    }

    public void warning(JooSymbol sym, String msg) {
      addMessage(CompilerMessageCategory.WARNING, msg, sym);
    }

    public void warning(String msg) {
      compileContext.addMessage(CompilerMessageCategory.WARNING, msg, null, -1, -1);
    }

    public boolean hasErrors() {
      return compileContext.getMessageCount(CompilerMessageCategory.ERROR)>0;
    }

    public boolean hasErrors(VirtualFile file) {
      return filesWithErrors.contains(file);
    }
  }
}
