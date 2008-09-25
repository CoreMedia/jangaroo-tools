package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.PackageDeclaration;
import net.jangaroo.jooc.ClassDeclaration;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: dhomann
 * Date: 23.09.2008
 * Time: 20:57:46
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCompilationUnitSinkFactory implements CompilationUnitSinkFactory {
  protected File outputDir = null;
  boolean debugKeepSource;
  boolean debugKeepLines;
  boolean enableAssertions;

  public AbstractCompilationUnitSinkFactory(boolean debugKeepLines, File outputDir, boolean enableAssertions, boolean debugKeepSource) {
    this.debugKeepLines = debugKeepLines;
    this.outputDir = outputDir;
    this.enableAssertions = enableAssertions;
    this.debugKeepSource = debugKeepSource;
  }

  public File getOutputDir() {
    return outputDir;
  }

  public boolean isDebugKeepSource() {
    return debugKeepSource;
  }

  public boolean isDebugKeepLines() {
    return debugKeepLines;
  }

  public boolean isEnableAssertions() {
    return enableAssertions;
  }

  protected void createOutputDirs(File outputFile) {
    File parentDir = outputFile.getParentFile();
    if (!parentDir.exists() && !parentDir.mkdirs()) {
      Jooc.error("cannot create directories '" + parentDir.getAbsolutePath() + "'");
    }
  }

  public abstract CompilationUnitSink createSink(PackageDeclaration packageDeclaration,
                               ClassDeclaration classDeclaration, File sourceFile,
                               boolean verbose);
}
