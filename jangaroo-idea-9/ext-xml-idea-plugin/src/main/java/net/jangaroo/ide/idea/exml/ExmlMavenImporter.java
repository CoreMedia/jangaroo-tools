package net.jangaroo.ide.idea.exml;

import com.intellij.facet.FacetManager;
import com.intellij.javaee.ExternalResourceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.ModuleOrderEntry;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.xml.XsdScanner;
import net.jangaroo.utils.log.Log;
import org.jetbrains.idea.maven.importing.MavenModifiableModelsProvider;
import org.jetbrains.idea.maven.importing.MavenRootModelAdapter;
import org.jetbrains.idea.maven.project.*;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A Facet-from-Maven Importer for the EXML Facet type.
 */
public class ExmlMavenImporter extends org.jetbrains.idea.maven.importing.MavenImporter {
  // TODO: share these constants with Jangaroo Language plugin:
  private static final String JANGAROO_GROUP_ID = "net.jangaroo";
  private static final String JANGAROO_LIFECYCLE_MAVEN_PLUGIN_ARTIFACT_ID = "jangaroo-lifecycle";
  private static final String EXML_MAVEN_PLUGIN_ARTIFACT_ID = "ext-xml-maven-plugin";
  private static final String DEFAULT_EXML_FACET_NAME = "EXML";

  public ExmlMavenImporter() {
    super();
  }

  @Override
  public boolean isSupportedDependency(MavenArtifact artifact) {
    return false;
  }

  @Override
  public boolean isApplicable(MavenProject mavenProjectModel) {
    return mavenProjectModel.findPlugin(JANGAROO_GROUP_ID, JANGAROO_LIFECYCLE_MAVEN_PLUGIN_ARTIFACT_ID) != null ||
      mavenProjectModel.findPlugin(JANGAROO_GROUP_ID, EXML_MAVEN_PLUGIN_ARTIFACT_ID) != null;
  }

  @Override
  public void preProcess(Module module, MavenProject mavenProject, MavenProjectChanges mavenProjectChanges, MavenModifiableModelsProvider mavenModifiableModelsProvider) {
    // TODO: anything to do here?
  }

  @Override
  public void process(MavenModifiableModelsProvider modifiableModelsProvider, Module module,
                      MavenRootModelAdapter rootModel, MavenProjectsTree mavenModel, MavenProject mavenProjectModel,
                      MavenProjectChanges changes, Map<MavenProject, String> mavenProjectToModuleName,
                      List<MavenProjectsProcessorTask> postTasks) {
    FacetManager facetManager = FacetManager.getInstance(module);
    ExmlFacet exmlFacet = facetManager.getFacetByType(ExmlFacetType.ID);
    if (exmlFacet == null) {
      exmlFacet = facetManager.addFacet(ExmlFacetType.INSTANCE, DEFAULT_EXML_FACET_NAME, null);
    }
    ExmlcConfigurationBean exmlConfig = exmlFacet.getConfiguration().getState();
    exmlConfig.setSourceDirectory(mavenProjectModel.getSources().get(0));
    exmlConfig.setGeneratedSourcesDirectory(mavenProjectModel.getGeneratedSourcesDirectory() + "/joo");
    exmlConfig.setGeneratedResourcesDirectory(mavenProjectModel.getBuildDirectory() + "/generated-resources");
    exmlConfig.setNamespace(mavenProjectModel.getMavenModel().getArtifactId());
    exmlConfig.setNamespacePrefix(mavenProjectModel.getMavenModel().getArtifactId());
    exmlConfig.setXsd(mavenProjectModel.getMavenModel().getArtifactId() + ".xsd");

    final Map<String, String> resourceMap = getXsdResourcesOfModule(module);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        ExternalResourceManager externalResourceManager = ExternalResourceManager.getInstance();
        for (Map.Entry<String, String> uri2filename : resourceMap.entrySet()) {
          externalResourceManager.removeResource(uri2filename.getKey());
          externalResourceManager.addResource(uri2filename.getKey(), uri2filename.getValue());
        }
      }
    });
  }

  private Map<String, String> getXsdResourcesOfModule(Module module) {
    // Collect the XSD resource mappings of this modules and all its dependent component suites.
    //System.out.println("Scanning dependencies of " + moduleName + " for component suite XSDs...");
    final Map<String, String> resourceMap = new LinkedHashMap<String, String>();
    OrderEntry[] orderEntries = ModuleRootManager.getInstance(module).getOrderEntries();
    XsdScanner scanner = new XsdScanner();
    for (OrderEntry orderEntry : orderEntries) {
      try {
        if (orderEntry instanceof ModuleOrderEntry) {
          String xsdFilename = ExmlCompiler.getXsdFilename(((ModuleOrderEntry)orderEntry).getModule());
          if (xsdFilename != null) {
            InputStream xsdInputStream = new FileInputStream(new File(xsdFilename));
            addResource(resourceMap, scanner, xsdInputStream, xsdFilename);
          }
        } else {
          String zipFileName = ExmlCompiler.findDependentModuleZipFileName(orderEntry);
          if (zipFileName != null) {
            ZipFile zipFile = new ZipFile(zipFileName);
            ZipEntry xsdZipEntry = ExmlCompiler.findXsdZipEntry(zipFile);
            if (xsdZipEntry != null) {
              String filename = zipFileName + "!/" + xsdZipEntry.getName();
              InputStream xsdInputStream = zipFile.getInputStream(xsdZipEntry);
              addResource(resourceMap, scanner, xsdInputStream, filename);
            }
          }
        }
      } catch (IOException e) {
        // ignore
      }
    }
    String xsdFilename = ExmlCompiler.getXsdFilename(module);
    if (xsdFilename != null) {
      try {
        addResource(resourceMap, scanner, new FileInputStream(xsdFilename), xsdFilename);
      } catch (FileNotFoundException e) {
        Log.e("Error while scanning XSD file " + xsdFilename, e);
      }
    }
    return resourceMap;
  }

  private void addResource(Map<String, String> resourceMap, XsdScanner scanner, InputStream xsdInputStream, String filename) {
    //System.out.println("  found XSD " + xsdInputStream + "...");
    try {
      ComponentSuite componentSuite = scanner.scan(xsdInputStream);
      if (componentSuite != null) {
        resourceMap.put(componentSuite.getNamespace(), filename);
      }
    } catch (IOException e) {
      Log.e("Error while scanning XSD file " + filename, e);
    }
  }

}