package net.jangaroo.ide.idea.exml;

import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
import com.intellij.compiler.make.MakeUtil;
import com.intellij.facet.FacetManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import net.jangaroo.exml.ComponentSuiteRegistry;
import net.jangaroo.exml.file.ExmlComponentSrcFileScanner;
import net.jangaroo.exml.file.SrcFileScanner;
import net.jangaroo.exml.generation.JooClassGenerator;
import net.jangaroo.exml.generation.XsdGenerator;
import net.jangaroo.exml.model.ComponentSuite;
import net.jangaroo.exml.model.ComponentType;
import net.jangaroo.exml.xml.XsdScanner;
import net.jangaroo.ide.idea.JangarooFacetType;
import net.jangaroo.utils.log.Log;
import net.jangaroo.utils.log.LogHandler;
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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 */
public class ExmlCompiler implements TranslatingCompiler {

  private static final String JOO_SOURCES_PATH_REG_EXP = ".*\\bmain[/\\\\]joo\\b.*";
  private static final String GENERATED_SOURCES_PATH_REG_EXP = ".*\\bgenerated-sources\\b.*";

  @NotNull
  public String getDescription() {
    return "EXML Compiler";
  }

  public boolean validateConfiguration(CompileScope scope) {
    // TODO: what to check beforehand?
    return true;
  }

  public boolean isCompilableFile(VirtualFile file, CompileContext context) {
    if (ComponentType.from(file.getExtension()) != null) {
      Module module = context.getModuleByFile(file);
      if (module != null && FacetManager.getInstance(module).getFacetByType(JangarooFacetType.ID) != null) {
        return true;
      }
    }
    return false;
  }

  private static ExmlcConfigurationBean getExmlcConfiguration(Module module) {
    ExmlFacet exmlFacet = FacetManager.getInstance(module).getFacetByType(ExmlFacetType.ID);
    if (exmlFacet==null) {
      return null;
    }
    return exmlFacet.getConfiguration().getState();
  }

  static String getXsdFilename(Module module) {
    if (module != null) {
      ExmlcConfigurationBean exmlcConfig = getExmlcConfiguration(module);
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
    String sourceRootDir = findSourceRootDir(module, JOO_SOURCES_PATH_REG_EXP);
    String moduleName = module.getName();
    ComponentSuite suite = new ComponentSuite(moduleName, moduleName.substring(0, Math.max(1, moduleName.length())).toLowerCase(),
        new File(sourceRootDir), new File(findGeneratedAs3RootDir(module)));

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
      Writer out;
      File xsdFile;
      try {
        xsdFile = new File(xsdFilename);
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

  private void compile(final CompileContext context, Module module, final List<VirtualFile> files, List<OutputItem> outputItems, List<VirtualFile> filesToRecompile) {
    JooClassGenerator generator = null;
    ComponentSuite suite = null;
    String as3OutputDir = null;
    for (final VirtualFile file : files) {
      if (ComponentType.EXML.getExtension().equals(file.getExtension())) {
        if (suite == null) {
          suite = ComponentSuiteRegistry.getInstance().getComponentSuite(module.getName());
          if (suite == null) {
            context.addMessage(CompilerMessageCategory.ERROR, "No XML Schema (.xsd) found for component suite module " + module.getName(), null, -1, -1);
            return;
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
          OutputItem outputItem = new OutputItemImpl(as3OutputDir, outputFile.getPath().replace(File.separatorChar, '/'), file);
          context.addMessage(CompilerMessageCategory.INFORMATION, "exml->as (" + outputItem.getOutputPath() + ")", file.getUrl(), -1, -1);
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
  }

  private String findGeneratedAs3RootDir(Module module) {
    return findSourceRootDir(module, GENERATED_SOURCES_PATH_REG_EXP);
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
    final ComponentSuiteRegistry componentSuiteRegistry = ComponentSuiteRegistry.getInstance();
    componentSuiteRegistry.reset();

    Log.setLogHandler(new IdeaErrorHandler(context));
    List<OutputItem> outputItems = new ArrayList<OutputItem>(files.length);
    List<VirtualFile> filesToRecompile = new ArrayList<VirtualFile>(files.length);
    Map<Module, List<VirtualFile>> filesByModule = new HashMap<Module, List<VirtualFile>>(files.length);
    for (final VirtualFile file : files) {
      Module module = context.getModuleByFile(file);
      List<VirtualFile> filesOfModule = filesByModule.get(module);
      if (filesOfModule == null) {
        filesOfModule = new ArrayList<VirtualFile>(files.length);
        filesByModule.put(module, filesOfModule);
      }
      filesOfModule.add(file);
    }
    for (Map.Entry<Module, List<VirtualFile>> filesOfModuleEntry : filesByModule.entrySet()) {
      Module module = filesOfModuleEntry.getKey();
      addModuleDependenciesToComponentSuiteRegistry(module);
      ComponentSuite suite = scanSrcFiles(module);
      compile(context, module, filesOfModuleEntry.getValue(), outputItems, filesToRecompile);
      generateXsd(module, suite);
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

  private static class IdeaErrorHandler implements LogHandler {
    private final CompileContext context;
    private File currentFile;

    public IdeaErrorHandler(CompileContext context) {
      this.context = context;
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

    public void debug(String message) {
      //ignore debug messages for now
    }
  }

}
