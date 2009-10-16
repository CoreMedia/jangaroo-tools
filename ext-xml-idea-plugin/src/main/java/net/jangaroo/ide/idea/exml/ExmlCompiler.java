package net.jangaroo.ide.idea.exml;

import com.intellij.compiler.make.MakeUtil;
import com.intellij.javaee.ExternalResourceManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import net.jangaroo.extxml.ComponentSuite;
import net.jangaroo.extxml.ComponentSuiteRegistry;
import net.jangaroo.extxml.ComponentSuiteResolver;
import net.jangaroo.extxml.ErrorHandler;
import net.jangaroo.extxml.ExtComponentSrcFileScanner;
import net.jangaroo.extxml.JooClassGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 
 */
public class ExmlCompiler implements TranslatingCompiler {

  private ComponentSuiteRegistry componentSuiteRegistry = new ComponentSuiteRegistry(new IdeaComponentSuiteResolver());

  @NotNull
  public String getDescription() {
    return "EXML Compiler";
  }

  public boolean validateConfiguration(CompileScope scope) {
    // TODO: what to check beforehand?
    return true;
  }

  public boolean isCompilableFile(VirtualFile file, CompileContext context) {
    return "exml".equals(file.getExtension());
    // TODO: how can we also receive *.as and *.js files to scan for annotations?
  }

  private void compile(final CompileContext context, Module module, final List<VirtualFile> files, List<OutputItem> outputItems, List<VirtualFile> filesToRecompile) {
    final VirtualFile outputDirectory = context.getModuleOutputDirectory(module);
    assert outputDirectory!=null;

    ErrorHandler errorHandler = new IdeaErrorHandler(context);
    componentSuiteRegistry.setErrorHandler(errorHandler);
    ComponentSuite suite = null;
    for (final VirtualFile file : files) {
      if (suite == null) {
        suite = new ComponentSuite(componentSuiteRegistry, module.getName(), module.getName(), new File(MakeUtil.getSourceRoot(context, module, file).getPath()), new File(outputDirectory.getPath()));
      }
      context.addMessage(CompilerMessageCategory.INFORMATION, "exml->as", file.getUrl(), -1, -1);
      try {
        ExtComponentSrcFileScanner.scan(suite, new File(file.getPath()));
      } catch (IOException e) {
        context.addMessage(CompilerMessageCategory.ERROR, "Exception "+e, file.getUrl(), -1, -1);
        filesToRecompile.add(file);
      }
    }
    //Generate JSON out of the xml compontents, complete the data in those ComponentClasses
    JooClassGenerator generator = new JooClassGenerator(suite, errorHandler);
    generator.generateClasses();
    // TODO: let generateClasses() return a set of generated files and add these to outputItems!
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

  private static class IdeaErrorHandler implements ErrorHandler {
    private final CompileContext context;
    private File currentFile;

    public IdeaErrorHandler(CompileContext context) {
      this.context = context;
    }

    public void setCurrentFile(File file) {
      this.currentFile = file;
    }

    private VirtualFile addMessage(CompilerMessageCategory compilerMessageCategory, String msg, int lineNumber, int columnNumber) {
      VirtualFile file = LocalFileSystem.getInstance().findFileByPath(currentFile.getAbsolutePath());
      String fileUrl = file==null ? null : file.getUrl();
      context.addMessage(compilerMessageCategory, msg, fileUrl, lineNumber, columnNumber);
      return file;
    }

    public void error(String message, int lineNumber, int columnNumber) {
      addMessage(CompilerMessageCategory.ERROR, message, lineNumber, columnNumber);
    }

    public void error(String message, Exception exception) {
      addMessage(CompilerMessageCategory.ERROR, message + ": " + exception.getLocalizedMessage(), -1, -1);
    }

    public void error(String message) {
      addMessage(CompilerMessageCategory.ERROR, message, -1, -1);
    }

    public void warning(String message) {
      addMessage(CompilerMessageCategory.WARNING, message, -1, -1);
    }

    public void warning(String message, int lineNumber, int columnNumber) {
      addMessage(CompilerMessageCategory.WARNING, message, lineNumber, columnNumber);
    }

    public void info(String message) {
      addMessage(CompilerMessageCategory.INFORMATION, message, -1, -1);
    }
  }

  private static class IdeaComponentSuiteResolver implements ComponentSuiteResolver {

    public InputStream resolveComponentSuite(String namespaceUri) throws IOException {
      String filename = ExternalResourceManager.getInstance().getResourceLocation(namespaceUri);
      if (filename == null) {
        return null;
      }
      int zipSeparator = filename.indexOf('!');
      if (zipSeparator != -1) {
        ZipFile zipFile = new ZipFile(filename.substring(0,zipSeparator));
        ZipEntry zipEntry = zipFile.getEntry(filename.substring(zipSeparator + 2));
        // TODO: +2 skips the '!' as well as the leading slash. Probably does not work for paths!
        return zipFile.getInputStream(zipEntry);
      }
      return new FileInputStream(filename);
    }
  }
  
}
