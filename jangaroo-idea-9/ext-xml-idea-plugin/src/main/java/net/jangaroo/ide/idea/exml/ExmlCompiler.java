package net.jangaroo.ide.idea.exml;

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
import com.intellij.openapi.roots.ModuleOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Chunk;
import net.jangaroo.extxml.ComponentSuiteRegistry;
import net.jangaroo.extxml.file.ExmlComponentSrcFileScanner;
import net.jangaroo.extxml.file.SrcFileScanner;
import net.jangaroo.extxml.generation.JooClassGenerator;
import net.jangaroo.extxml.generation.XsdGenerator;
import net.jangaroo.utils.log.LogHandler;
import net.jangaroo.utils.log.Log;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.model.ComponentType;
import net.jangaroo.extxml.xml.XsdScanner;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 */
public class ExmlCompiler implements TranslatingCompiler, IntermediateOutputCompiler {

  @NotNull
  public String getDescription() {
    return "EXML Compiler";
  }

  public boolean validateConfiguration(CompileScope scope) {
    return true;
  }

  public boolean isCompilableFile(VirtualFile file, CompileContext context) {
    if (ComponentType.from(file.getExtension()) != null) {
      Module module = context.getModuleByFile(file);
      if (module != null && FacetManager.getInstance(module).getFacetByType(ExmlFacetType.ID) != null) {
        return true;
      }
    }
    return false;
  }

  public static ExmlcConfigurationBean getExmlConfig(Module module) {
    ExmlFacet exmlFacet = FacetManager.getInstance(module).getFacetByType(ExmlFacetType.ID);
    return exmlFacet == null ? null : exmlFacet.getConfiguration().getState();
  }

  static String getXsdFilename(Module module) {
    if (module != null) {
      ExmlcConfigurationBean exmlcConfig = getExmlConfig(module);
      if (exmlcConfig != null) {
        return exmlcConfig.getGeneratedResourcesDirectory() + "/" + exmlcConfig.getXsd();
      }
    }
    return null;
  }

  private void addModuleDependenciesToComponentSuiteRegistry(Module module) {
    // Add all dependent component suites to component suite registry, so they are found when looking for some xtype of fullClassName:
    //System.out.println("Scanning dependencies of " + moduleName + " for component suite XSDs...");
    OrderEntry[] orderEntries = ModuleRootManager.getInstance(module).getOrderEntries();
    XsdScanner scanner = new XsdScanner();
    for (OrderEntry orderEntry : orderEntries) {
      InputStream xsdInputStream = null;
      try {
        if (orderEntry instanceof ModuleOrderEntry) {
          String xsdFilename = getXsdFilename(((ModuleOrderEntry)orderEntry).getModule());
          if (xsdFilename != null) {
            xsdInputStream = new FileInputStream(xsdFilename);
          }
        } else {
          String zipFileName = findDependentModuleZipFileName(orderEntry);
          if (zipFileName != null) {
            ZipFile zipFile = new ZipFile(zipFileName);
            ZipEntry zipEntry = findXsdZipEntry(zipFile);
            if (zipEntry != null) {
              xsdInputStream = zipFile.getInputStream(zipEntry);
            }
          }
        }
      } catch (IOException e) {
        // ignore
      }
      if (xsdInputStream != null) {
        //System.out.println("  found XSD " + xsdInputStream + "...");
        try {
          scanner.scan(xsdInputStream); // adds scan result ComponentSuite to ComponentSuiteRegistry
        } catch (IOException e) {
          Log.e("Error while scanning XSD file " + xsdInputStream, e);
        }
      }
    }
  }

  static String findDependentModuleZipFileName(OrderEntry orderEntry) throws IOException {
    VirtualFile[] files = orderEntry.getFiles(OrderRootType.CLASSES);
    // check that library is not empty:
    for (VirtualFile file : files) {
      // TODO: make it work for classes, not only for jars!
      String filename = file.getPath();
      if (filename.endsWith("!/")) { // it is a jar:
        return filename.substring(0, filename.length() - "!/".length());
      }
    }
    return null;
  }

  static ZipEntry findXsdZipEntry(ZipFile zipFile) throws IOException {
    // find a *.xsd in jar's root folder:
    Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
    while (enumeration.hasMoreElements()) {
      ZipEntry zipEntry = enumeration.nextElement();
      if (!zipEntry.isDirectory() && zipEntry.getName().indexOf('/') == -1 && zipEntry.getName().endsWith(".xsd")) {
        return zipEntry;
      }
    }
    return null;
  }

  private ComponentSuite scanSrcFiles(Module module) {
    String sourceRootDir = findSourceRootDir(module);
    String generatedAs3RootDir = findGeneratedAs3RootDir(module);
    if (sourceRootDir == null || generatedAs3RootDir == null) {
      return null;
    }
    String moduleName = module.getName();
    ComponentSuite suite = new ComponentSuite(moduleName, moduleName.substring(0, Math.max(1, moduleName.length())).toLowerCase(),
        new File(sourceRootDir), new File(generatedAs3RootDir));

    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
      fileScanner.scan();
    } catch (IOException e) {
      Log.e("Error scanning component suite files.", e);
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    }
    return suite;
  }

  private void generateXsd(Module module, ComponentSuite suite) {
    if (!suite.getComponentClasses().isEmpty()) {
      String xsdFilename = getXsdFilename(module);
      if (xsdFilename != null) {
        Writer out;
        File xsdFile;
        try {
          xsdFile = new File(xsdFilename);
          //noinspection ResultOfMethodCallIgnored
          xsdFile.getParentFile().mkdirs();
          // (re-)generate the XSD for the given module.
          Log.setCurrentFile(xsdFile);
          out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xsdFile), "UTF-8"));
        } catch (Exception e) {
          Log.e("Cannot write component suite XSD file.", e);
          return;
        }
        try {
          new XsdGenerator(suite).generateXsd(out);
          LocalFileSystem.getInstance().refreshIoFiles(Arrays.asList(xsdFile));
        } catch (IOException e) {
          Log.e("Error while writing component suite XSD file.", e);
        }
      }
    }
  }

  private String compile(final CompileContext context, Module module, final List<VirtualFile> files, List<OutputItem> outputItems, List<VirtualFile> filesToRecompile) {
    JooClassGenerator generator = null;
    ComponentSuite suite = null;
    String as3OutputDir = null;
    boolean showCompilerInfoMessages = getExmlConfig(module).isShowCompilerInfoMessages();
    for (final VirtualFile file : files) {
      if (ComponentType.EXML.getExtension().equals(file.getExtension())) {
        if (suite == null) {
          suite = ComponentSuiteRegistry.getInstance().getComponentSuite(module.getName());
          if (suite == null) {
            context.addMessage(CompilerMessageCategory.ERROR, "No XML Schema (.xsd) found for component suite module " + module.getName(), null, -1, -1);
            return null;
          }
          String srcRootDir = MakeUtil.getSourceRoot(context, module, file).getPath();
          suite.setRootDir(new File(srcRootDir));
          as3OutputDir = findGeneratedAs3RootDir(module);
          suite.setAs3OutputDir(new File(as3OutputDir));
          generator = new JooClassGenerator(suite);
        }
        File outputFile = generator.generateClass(suite.getComponentClassByFullClassName(ExmlComponentSrcFileScanner.getComponentClassName(suite, new File(file.getPath()))));
        if (outputFile == null) { // TODO: or file has any compiler errors!
          //context.addMessage(CompilerMessageCategory.INFORMATION, "exml->as compilation failed.", file.getUrl(), -1, -1);
          filesToRecompile.add(file);
        } else {
          OutputItem outputItem = new OutputItemImpl(outputFile.getPath().replace(File.separatorChar, '/'), file);
          if (showCompilerInfoMessages) {
            context.addMessage(CompilerMessageCategory.INFORMATION, "exml->as (" + outputItem.getOutputPath() + ")", file.getUrl(), -1, -1);
          }
          getLog().info("exml->as: " + file.getUrl() + " -> " + outputItem.getOutputPath());
          LocalFileSystem.getInstance().refreshIoFiles(Arrays.asList(outputFile));
          outputItems.add(outputItem);
        }
      }
    }
    if (suite != null) {
      //Generate JSON out of the XML components, complete the data in those ComponentClasses
      generator.generateClasses();
      // TODO: let generateClasses() return a set of generated files and add these to outputItems!
    }
    return as3OutputDir;
  }

  public static String findGeneratedAs3RootDir(Module module) {
    ExmlcConfigurationBean exmlConfig = getExmlConfig(module);
    return exmlConfig == null ? null : getVFPath(exmlConfig.getGeneratedSourcesDirectory());
  }

  private String findSourceRootDir(Module module) {
    ExmlcConfigurationBean exmlConfig = getExmlConfig(module);
    return exmlConfig == null ? null : getVFPath(exmlConfig.getSourceDirectory());
  }

  private static String getVFPath(String path) {
    VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(path);
    return virtualFile == null ? null : virtualFile.getPath();
  }

  public void compile(CompileContext context, Chunk<Module> moduleChunk, VirtualFile[] files, OutputSink outputSink) {
    // always re-create component suite registry, so that we get updates:
    final ComponentSuiteRegistry componentSuiteRegistry = ComponentSuiteRegistry.getInstance();
    componentSuiteRegistry.reset();

    IdeaErrorHandler errorHandler = new IdeaErrorHandler(context);
    Log.setLogHandler(errorHandler);
    Map<Module, List<VirtualFile>> filesByModule = new HashMap<Module, List<VirtualFile>>(files.length);
    for (final VirtualFile file : files) {
      Module module = context.getModuleByFile(file);
      if (getExmlConfig(module) != null) {
        List<VirtualFile> filesOfModule = filesByModule.get(module);
        if (filesOfModule == null) {
          filesOfModule = new ArrayList<VirtualFile>(files.length);
          filesByModule.put(module, filesOfModule);
        }
        filesOfModule.add(file);
      }
    }
    for (Map.Entry<Module, List<VirtualFile>> filesOfModuleEntry : filesByModule.entrySet()) {
      Module module = filesOfModuleEntry.getKey();
      errorHandler.setShowCompilerInfoMessages(getExmlConfig(module).isShowCompilerInfoMessages());
      addModuleDependenciesToComponentSuiteRegistry(module);
      ComponentSuite suite = scanSrcFiles(module);
      if (suite != null) {
        List<OutputItem> outputItems = new ArrayList<OutputItem>(files.length);
        List<VirtualFile> filesToRecompile = new ArrayList<VirtualFile>(files.length);
        String outputRoot = compile(context, module, filesOfModuleEntry.getValue(), outputItems, filesToRecompile);
        if (outputRoot != null) {
          outputSink.add(outputRoot, outputItems, filesToRecompile.toArray(new VirtualFile[filesToRecompile.size()]));
          generateXsd(module, suite);
        }
      }
    }
  }

  static Logger getLog() {
    return Logger.getInstance("net.jangaroo.ide.idea.exml.ExmlCompiler");
  }

  private static class IdeaErrorHandler implements LogHandler {
    private final CompileContext context;
    private boolean showCompilerInfoMessages = true;
    private File currentFile;

    public IdeaErrorHandler(CompileContext context) {
      this.context = context;
    }

    public void setShowCompilerInfoMessages(boolean showCompilerInfoMessages) {
      this.showCompilerInfoMessages = showCompilerInfoMessages;
    }

    public void setCurrentFile(File file) {
      this.currentFile = file;
    }

    private VirtualFile addMessage(CompilerMessageCategory compilerMessageCategory, String msg, int lineNumber, int columnNumber) {
      VirtualFile file = currentFile == null ? null : LocalFileSystem.getInstance().findFileByPath(currentFile.getAbsolutePath());
      String fileUrl = file == null ? null : file.getUrl();
      context.addMessage(compilerMessageCategory, msg, fileUrl, lineNumber, columnNumber);
      return file;
    }

    public void error(String message, int lineNumber, int columnNumber) {
      addMessage(CompilerMessageCategory.ERROR, message, lineNumber, columnNumber);
      getLog().debug("EXML Compiler Error: " + message);
    }

    public void error(String message, Exception exception) {
      error(message + ": " + exception.getLocalizedMessage(), -1, -1);
    }

    public void error(String message) {
      error(message, -1, -1);
    }

    public void warning(String message) {
      warning(message, -1, -1);
    }

    public void warning(String message, int lineNumber, int columnNumber) {
      addMessage(CompilerMessageCategory.WARNING, message, lineNumber, columnNumber);
      getLog().debug("EXML Compiler Warning: " + message);
    }

    public void info(String message) {
      if (showCompilerInfoMessages) {
        addMessage(CompilerMessageCategory.INFORMATION, message, -1, -1);
      }
      getLog().debug("EXML Compiler Info: " + message);
    }

    public void debug(String message) {
      getLog().debug(message);
      //ignore debug messages for now
    }
  }

}
