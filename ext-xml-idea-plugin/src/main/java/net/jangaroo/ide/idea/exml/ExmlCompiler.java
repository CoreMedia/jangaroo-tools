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
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import net.jangaroo.extxml.ComponentSuite;
import net.jangaroo.extxml.ComponentSuiteRegistry;
import net.jangaroo.extxml.ComponentSuiteResolver;
import net.jangaroo.extxml.ErrorHandler;
import net.jangaroo.extxml.ExtComponentSrcFileScanner;
import net.jangaroo.extxml.JooClassGenerator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.net.URL;

/**
 *
 */
public class ExmlCompiler implements TranslatingCompiler {

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
    ErrorHandler errorHandler = new IdeaErrorHandler(context);
    ComponentSuiteRegistry.getInstance().setErrorHandler(errorHandler);
    ComponentSuite suite = null;
    for (final VirtualFile file : files) {
      if (suite == null) {
        suite = ComponentSuiteRegistry.getInstance().getComponentSuite(module.getName());
        if (suite == null) {
          context.addMessage(CompilerMessageCategory.ERROR, "No XML Schema (.xsd) found for component suite module " + module.getName(), null, -1, -1);
          return;
        }
        String srcRootDir = MakeUtil.getSourceRoot(context, module, file).getPath();
        suite.setRootDir(new File(srcRootDir));
        suite.setAs3OutputDir(new File(findAs3RootDir(module)));
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

  private String findAs3RootDir(Module module) {
    VirtualFile[] srcRoots = ModuleRootManager.getInstance(module).getSourceRoots();
    for (VirtualFile srcRoot : srcRoots) {
      String path = srcRoot.getPath();
      if (path.matches(".*\\bgenerated-sources\\b.*")) {
        return path;
      }
    }
    return srcRoots[0].getPath();
  }

  public ExitStatus compile(CompileContext context, VirtualFile[] files) {
    // always re-create component suite registry, so that we get updates:
    ComponentSuiteRegistry componentSuiteRegistry = ComponentSuiteRegistry.getInstance();
    componentSuiteRegistry.reset();
    IdeaComponentSuiteResolver componentSuiteResolver = new IdeaComponentSuiteResolver();
    componentSuiteRegistry.setComponentSuiteResolver(componentSuiteResolver);
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
      componentSuiteResolver.setModule(filesOfModuleEntry.getKey());
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
      VirtualFile file = currentFile==null ? null : LocalFileSystem.getInstance().findFileByPath(currentFile.getAbsolutePath());
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

    private Module module;

    public Module getModule() {
      return module;
    }

    public void setModule(Module module) {
      this.module = module;
    }

    public InputStream resolveComponentSuite(String namespaceUri) throws IOException {
      String filename = ExternalResourceManager.getInstance().getResourceLocation(namespaceUri);
      if (namespaceUri.equals(filename)) { // not found: IDEA returns the namespaceUri itself!
        return findComponentSuiteInLibraries(namespaceUri);
      }
      if (filename.startsWith("jar:")) {
        filename = new URL(filename).getPath();
      }
      if (filename.startsWith("file:")) {
        filename = new URL(filename).getPath().substring(1); // also skip leading '/' or '\'
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

    private InputStream findComponentSuiteInLibraries(final String namespaceUri) throws FileNotFoundException {
      final String filename = findComponentSuiteFilename(namespaceUri);
      if (filename != null) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            ExternalResourceManager.getInstance().addResource(namespaceUri, filename);
          }
        });
        return new FileInputStream(filename);
      }
      return null;
    }

    private String findComponentSuiteFilename(String namespaceUri) {
      if (namespaceUri.equals(module.getName())) {
        return ModuleRootManager.getInstance(module).getContentRoots()[0].getPath() + "/target/generated-resources/" + module.getName() + ".xsd";
      }
      System.out.println("Scanning dependencies of " + module.getName());
      LibraryTable libraryTable = ProjectLibraryTable.getInstance(module.getProject());
      OrderEntry[] orderEntries = ModuleRootManager.getInstance(module).getOrderEntries();
      String namespaceUriPart = ":" + namespaceUri + ":";
      for (OrderEntry orderEntry: orderEntries) {
        Library library = libraryTable.getLibraryByName(orderEntry.getPresentableName());
        if (library != null && library.getName().indexOf(namespaceUriPart) != -1) {
          // found the right library!
          VirtualFile[] files = library.getFiles(OrderRootType.CLASSES);
          // check that library is not empty:
          if (files.length == 0) {
            files = library.getFiles(OrderRootType.SOURCES);
          }
          if (files.length > 0) {
            String filename = files[0].getPath();
            int lastDot = filename.lastIndexOf('.');
            filename = filename.substring(0, lastDot) + ".xsd";
            if (new File(filename).exists()) {
              return filename;
            }
          }
        }
      }
      return null;
    }
  }

}
