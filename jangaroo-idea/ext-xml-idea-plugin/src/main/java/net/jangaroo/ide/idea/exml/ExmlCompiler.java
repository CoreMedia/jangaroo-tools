package net.jangaroo.ide.idea.exml;

import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
import com.intellij.compiler.make.MakeUtil;
import com.intellij.javaee.ExternalResourceManager;
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
import com.intellij.facet.FacetManager;
import net.jangaroo.extxml.ComponentSuite;
import net.jangaroo.extxml.ComponentSuiteRegistry;
import net.jangaroo.extxml.ErrorHandler;
import net.jangaroo.extxml.Log;
import net.jangaroo.extxml.JooClassGenerator;
import net.jangaroo.extxml.XsdGenerator;
import net.jangaroo.extxml.XsdScanner;
import net.jangaroo.extxml.ComponentType;
import net.jangaroo.extxml.file.SrcFileScanner;
import net.jangaroo.extxml.file.ExmlComponentSrcFileScanner;
import net.jangaroo.ide.idea.JangarooFacetType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
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
import java.util.LinkedHashMap;
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

  private static String getXsdFilename(Module module) {   
    VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
    return contentRoots.length > 0 ? contentRoots[0].getPath() + "/target/generated-resources/" + module.getName() + ".xsd" : null;
  }

  private void addModuleDependenciesToComponentSuiteRegistry(Module module, Map<String, String> resourceMap) {
    // Add all dependent component suites to component suite registry, so they are found when looking for some xtype of fullClassName:
    //System.out.println("Scanning dependencies of " + moduleName + " for component suite XSDs...");
    OrderEntry[] orderEntries = ModuleRootManager.getInstance(module).getOrderEntries();
    XsdScanner scanner = new XsdScanner();
    for (OrderEntry orderEntry: orderEntries) {
      String filename = null;
      InputStream xsdInputStream = null;
      try {
        if (orderEntry instanceof ModuleOrderEntry) {
          Module usedModule = ((ModuleOrderEntry)orderEntry).getModule();
          if (usedModule != null) {
            filename = getXsdFilename(usedModule);
            xsdInputStream = new FileInputStream(new File(filename));
          }
        } else {
          VirtualFile[] files = orderEntry.getFiles(OrderRootType.CLASSES);
          // check that library is not empty:
          for (VirtualFile file : files) {
            // TODO: make it work for classes, not only for jars!
            filename = file.getPath();
            if (filename.endsWith("!/")) { // it is a jar:
              ZipFile zipFile = new ZipFile(filename.substring(0, filename.length() - "!/".length()));
              // find a *.xsd in jar's root folder:
              Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
              while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                if (!zipEntry.isDirectory() && zipEntry.getName().indexOf('/') == -1 && zipEntry.getName().endsWith(".xsd")) {
                  filename = filename + zipEntry.getName();
                  xsdInputStream = zipFile.getInputStream(zipEntry);
                  break;
                }
              }
            }
          }
        }
      } catch (IOException e) {
        // ignore
      }
      if (xsdInputStream != null) {
        //System.out.println("  found XSD " + xsdInputStream + "...");
        try {
          ComponentSuite componentSuite = scanner.scan(xsdInputStream);
          if (componentSuite != null) {
            resourceMap.put(componentSuite.getNamespace(), filename);
          }
        } catch (IOException e) {
          Log.getErrorHandler().error("Error while scanning XSD file "+xsdInputStream, e);
        }
      }
    }
    String xsdFilename = getXsdFilename(module);
    if (xsdFilename != null) {
      resourceMap.put(module.getName(), xsdFilename);
    }
  }

  private void generateXsd(Module module) {
    String sourceRootDir = findSourceRootDir(module, JOO_SOURCES_PATH_REG_EXP);
    String moduleName = module.getName();
    ComponentSuite suite = new ComponentSuite(moduleName, moduleName.substring(0,Math.max(1, moduleName.length())).toLowerCase(),
      new File(sourceRootDir), new File(findGeneratedAs3RootDir(module)));
    
    SrcFileScanner fileScanner = new SrcFileScanner(suite);
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
      fileScanner.scan();
    } catch (IOException e) {
      Log.getErrorHandler().error("Error scanning component suite files.", e);
      return;
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    }
    if (!suite.getComponentClasses().isEmpty()) {
      String xsdFilename = getXsdFilename(module);
      File xsdFile = new File(xsdFilename);
      // (re-)generate the XSD for the given module.
      Writer out;
      Log.getErrorHandler().setCurrentFile(xsdFile);
      try {
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xsdFile), "UTF-8"));
      } catch (Exception e) {
        Log.getErrorHandler().error("Cannot write component suite XSD file.", e);
        return;
      }
      try {
        new XsdGenerator(suite).generateXsd(out);
      } catch (IOException e) {
        Log.getErrorHandler().error("Error while writing component suite XSD file.", e);
      }
    }
  }

  private void compile(final CompileContext context, Module module, final List<VirtualFile> files, List<OutputItem> outputItems, List<VirtualFile> filesToRecompile) {
    ComponentSuite suite = null;
    String srcRootDir = null;
    for (final VirtualFile file : files) {
      if (ComponentType.EXML.getExtension().equals(file.getExtension())) {
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
          ExmlComponentSrcFileScanner.scan(suite, new File(file.getPath()), ComponentType.EXML);
          outputItems.add(outputItem);
          filesToRecompile.add(LocalFileSystem.getInstance().findFileByPath(outputItem.getOutputPath()));
        } catch (IOException e) {
          context.addMessage(CompilerMessageCategory.ERROR, "Exception "+e, file.getUrl(), -1, -1);
          filesToRecompile.add(file);
        }
      }
    }
    if (suite != null) {
      //Generate JSON out of the XML components, complete the data in those ComponentClasses
      JooClassGenerator generator = new JooClassGenerator(suite);
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
    String outputFilePath = outputDirectory + relativePath + ComponentType.EXML.getExtension();
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
    final ComponentSuiteRegistry componentSuiteRegistry = ComponentSuiteRegistry.getInstance();
    componentSuiteRegistry.reset();

    Log.setErrorHandler(new IdeaErrorHandler(context));
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
    final Map<String,String> resourceMap = new LinkedHashMap<String,String>();
    for (Map.Entry<Module, List<VirtualFile>> filesOfModuleEntry : filesByModule.entrySet()) {
      Module module = filesOfModuleEntry.getKey();
      addModuleDependenciesToComponentSuiteRegistry(module, resourceMap);
      generateXsd(module);
      compile(context, module, filesOfModuleEntry.getValue(), outputItems, filesToRecompile);
    }
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        ExternalResourceManager externalResourceManager = ExternalResourceManager.getInstance();
        for (Map.Entry<String, String> uri2filename : resourceMap.entrySet()) {
          externalResourceManager.addResource(uri2filename.getKey(), uri2filename.getValue());
        }
      }
    });
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

}
