package net.jangaroo.ide.idea.exml;

import com.intellij.compiler.make.MakeUtil;
import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
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
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.ModuleOrderEntry;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import net.jangaroo.extxml.ComponentSuite;
import net.jangaroo.extxml.ComponentSuiteRegistry;
import net.jangaroo.extxml.ComponentSuiteResolver;
import net.jangaroo.extxml.ErrorHandler;
import net.jangaroo.extxml.ExtComponentSrcFileScanner;
import net.jangaroo.extxml.JooClassGenerator;
import net.jangaroo.extxml.XsdGenerator;
import net.jangaroo.extxml.SrcFileScanner;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
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

  private static final String EXT3_XSD_URI = "http://extjs.com/ext3";
  private static final String JOO_SOURCES_PATH_REG_EXP = ".*\\bmain[/\\\\]joo\\b.*";
  private static final String GENERATED_SOURCES_PATH_REG_EXP = ".*\\bgenerated-sources\\b.*";
  private static final String EXML_SUFFIX_NO_DOT = "exml";

  @NotNull
  public String getDescription() {
    return "EXML Compiler";
  }

  public boolean validateConfiguration(CompileScope scope) {
    // TODO: what to check beforehand?
    return true;
  }

  public boolean isCompilableFile(VirtualFile file, CompileContext context) {
    String extension = file.getExtension();
    return EXML_SUFFIX_NO_DOT.equals(extension) || "as".equals(extension) || "js".equals(extension);
  }

  private static String getXsdFilename(Module module) {
    return ModuleRootManager.getInstance(module).getContentRoots()[0].getPath() + "/target/generated-resources/" + module.getName() + ".xsd";
  }

  private void generateXsd(CompileContext context, Module module, List<OutputItem> outputItems, List<VirtualFile> filesToRecompile, ErrorHandler errorHandler) {
    String moduleName = module.getName();
    String xsdFilename = getXsdFilename(module);
    // Generate the XSD for the given module.
    Writer out;
    try {
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(xsdFilename)), "UTF-8"));
      context.addMessage(CompilerMessageCategory.INFORMATION, "Generating XSD for module "+moduleName, LocalFileSystem.getInstance().findFileByPath(xsdFilename).getUrl(), -1, -1);
    } catch (Exception e) {
      errorHandler.error("Cannot write component suite XSD file.", e);
      return;
    }
    String sourceRootDir = findSourceRootDir(module, JOO_SOURCES_PATH_REG_EXP);
    ComponentSuite suite = new ComponentSuite(moduleName, moduleName.substring(0,Math.max(1, moduleName.length())).toLowerCase(),
      new File(sourceRootDir), new File(findGeneratedAs3RootDir(module)));
    
    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
      fileScanner.scan();
    } catch (IOException e) {
      errorHandler.error("Error scanning component suite files.", e);
      return;
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    }
    try {
      new XsdGenerator(suite, errorHandler).generateXsd(out);
    } catch (IOException e) {
      errorHandler.error("Error while writing component suite XSD file.", e);
    }
  }

  private void compile(final CompileContext context, Module module, final List<VirtualFile> files, List<OutputItem> outputItems, List<VirtualFile> filesToRecompile, ErrorHandler errorHandler) {
    ComponentSuite suite = null;
    String srcRootDir = null;
    for (final VirtualFile file : files) {
      if (EXML_SUFFIX_NO_DOT.equals(file.getExtension())) {
        if (suite == null) {
          suite = ComponentSuiteRegistry.getInstance().getComponentSuite(module.getName());
          if (suite == null) {
            context.addMessage(CompilerMessageCategory.ERROR, "No XML Schema (.xsd) found for component suite module " + module.getName(), null, -1, -1);
            return;
          }
          srcRootDir = MakeUtil.getSourceRoot(context, module, file).getPath();
          suite.setRootDir(new File(srcRootDir));
          suite.setAs3OutputDir(new File(findGeneratedAs3RootDir(module)));
        }
        OutputItem outputItem = createOutputItem(srcRootDir, MakeUtil.getSourceRoot(context, module, file), file);
        context.addMessage(CompilerMessageCategory.INFORMATION, "exml->as ("+outputItem.getOutputPath()+")", file.getUrl(), -1, -1);
        try {
          ExtComponentSrcFileScanner.scan(suite, new File(file.getPath()));
          outputItems.add(outputItem);
          filesToRecompile.add(LocalFileSystem.getInstance().findFileByPath(outputItem.getOutputPath()));
        } catch (IOException e) {
          context.addMessage(CompilerMessageCategory.ERROR, "Exception "+e, file.getUrl(), -1, -1);
          filesToRecompile.add(file);
        }
      }
    }
    if (suite != null) {
      //Generate JSON out of the xml compontents, complete the data in those ComponentClasses
      JooClassGenerator generator = new JooClassGenerator(suite, errorHandler);
      generator.generateClasses();
      // TODO: let generateClasses() return a set of generated files and add these to outputItems!
    }
  }

  private String findGeneratedAs3RootDir(Module module) {
    return findSourceRootDir(module, GENERATED_SOURCES_PATH_REG_EXP);
  }

  // TODO: very similar to a helper method in Jangaroo compiler IDEA plugin. Refactor?
  private OutputItem createOutputItem(final String outputDirectory, VirtualFile sourceRoot, final VirtualFile file) {
    if (sourceRoot==null) {
      throw new IllegalStateException("File not under any source root: '" + file.getPath() + "'.");
    }
    String filePath = file.getPath();
    String relativePath = filePath.substring(sourceRoot.getPath().length(), filePath.lastIndexOf('.')+1);
    String outputFilePath = outputDirectory + relativePath + EXML_SUFFIX_NO_DOT;
    return new OutputItemImpl(outputDirectory, outputFilePath, file);
  }
  
  private String findSourceRootDir(Module module, String GENERATED_SOURCES_PATH_REG_EXP) {
    VirtualFile[] srcRoots = ModuleRootManager.getInstance(module).getSourceRoots();
    for (VirtualFile srcRoot : srcRoots) {
      String path = srcRoot.getPath();
      if (path.matches(GENERATED_SOURCES_PATH_REG_EXP)) {
        return path;
      }
    }
    return srcRoots[0].getPath();
  }

  public ExitStatus compile(CompileContext context, VirtualFile[] files) {
    // always re-create component suite registry, so that we get updates:
    ComponentSuiteRegistry componentSuiteRegistry = ComponentSuiteRegistry.getInstance();
    componentSuiteRegistry.reset();
    ErrorHandler errorHandler = new IdeaErrorHandler(context);
    componentSuiteRegistry.setErrorHandler(errorHandler);
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
    for (Module module : filesByModule.keySet()) {
      componentSuiteResolver.setModule(module);
      generateXsd(context, module, outputItems, filesToRecompile, errorHandler);
    }
    for (Map.Entry<Module, List<VirtualFile>> filesOfModuleEntry : filesByModule.entrySet()) {
      componentSuiteResolver.setModule(filesOfModuleEntry.getKey());
      compile(context, filesOfModuleEntry.getKey(), filesOfModuleEntry.getValue(), outputItems, filesToRecompile, errorHandler);
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
        filename = findComponentSuiteInLibraries(namespaceUri);
        if (filename == null) {
          return null;
        }
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

    private String findComponentSuiteInLibraries(final String namespaceUri) throws FileNotFoundException {
      final String filename = findComponentSuiteFilename(namespaceUri);
      if (filename != null) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            ExternalResourceManager.getInstance().addResource(namespaceUri, filename);
          }
        });
        return filename;
      }
      return null;
    }

    private String findComponentSuiteFilename(String namespaceUri) {
      if (namespaceUri.equals(module.getName())) {
        return getXsdFilename(module);
      }
      System.out.println("Scanning dependencies of " + module.getName() + " for XSD with URI "+namespaceUri+"...");
      LibraryTable libraryTable = ProjectLibraryTable.getInstance(module.getProject());
      OrderEntry[] orderEntries = ModuleRootManager.getInstance(module).getOrderEntries();
      String namespaceUriPart;
      String xsdBasename;
      if (namespaceUri.equals(EXT3_XSD_URI)) {
        namespaceUriPart = "net.jangaroo:ext-as:jangaroo:";
        xsdBasename = "ext3";
      } else {
        namespaceUriPart = ":" + namespaceUri + ":";
        xsdBasename = namespaceUri;
      }
      for (OrderEntry orderEntry: orderEntries) {
        String orderEntryName = orderEntry.getPresentableName();
        if (orderEntry instanceof LibraryOrderEntry) {
          Library library = libraryTable.getLibraryByName(orderEntryName);
          if (library != null && library.getName().indexOf(namespaceUriPart) != -1) {
            // found the right library!
            VirtualFile[] files = library.getFiles(OrderRootType.CLASSES);
            // check that library is not empty:
            if (files.length == 0) {
              files = library.getFiles(OrderRootType.SOURCES);
            }
            if (files.length > 0) {
              return files[0].getPath() + xsdBasename + ".xsd";
            }
          }
        } else if (orderEntry instanceof ModuleOrderEntry) {
          if (namespaceUri.equals(orderEntryName)) {
            return getXsdFilename(((ModuleOrderEntry)orderEntry).getModule());
          }
        }
      }
      return null;
    }
  }

}
