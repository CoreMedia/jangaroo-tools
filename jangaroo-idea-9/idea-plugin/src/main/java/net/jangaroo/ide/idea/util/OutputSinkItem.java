package net.jangaroo.ide.idea.util;

import com.intellij.compiler.impl.CompilerUtil;
import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Collects the parameters to {@link TranslatingCompiler.OutputSink#add}.
 */
public class OutputSinkItem {

  private final File outputRoot;
  private final Collection<TranslatingCompiler.OutputItem> outputItems = new ArrayList<TranslatingCompiler.OutputItem>();
  private final Collection<File> filesToRefresh = new ArrayList<File>();
  private final Collection<VirtualFile> filesToRecompile = new ArrayList<VirtualFile>();

  public OutputSinkItem(String outputRootPath) throws SecurityException {
    this.outputRoot = new File(outputRootPath);
    //noinspection ResultOfMethodCallIgnored
    outputRoot.mkdirs();
  }

  public File getOutputRoot() {
    return outputRoot;
  }

  public String getOutputRootPath() {
    return FileUtil.toSystemIndependentName(outputRoot.getPath());
  }

  public void addOutputItem(VirtualFile sourceFile, File outputFile) {
    outputItems.add(new OutputItemImpl(FileUtil.toSystemIndependentName(outputFile.getPath()), sourceFile));
    filesToRefresh.add(outputFile);
  }

  public void addFileToRecompile(VirtualFile file) {
    filesToRecompile.add(file);
  }

  public void addTo(TranslatingCompiler.OutputSink outputSink) {
    CompilerUtil.refreshIOFiles(filesToRefresh);
    outputSink.add(getOutputRootPath(), outputItems, filesToRecompile.toArray(new VirtualFile[filesToRecompile.size()]));
  }

}
