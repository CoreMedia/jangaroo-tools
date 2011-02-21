package net.jangaroo.ide.idea.exml;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.javaee.ExternalResourceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.ModuleOrderEntry;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.xml.XsdScanner;
import org.jetbrains.idea.maven.importing.FacetImporter;
import org.jetbrains.idea.maven.importing.MavenModifiableModelsProvider;
import org.jetbrains.idea.maven.importing.MavenRootModelAdapter;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectChanges;
import org.jetbrains.idea.maven.project.MavenProjectsProcessorTask;
import org.jetbrains.idea.maven.project.MavenProjectsTree;

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
public class ExmlFacetImporter extends FacetImporter<ExmlFacet, ExmlFacetConfiguration, ExmlFacetType> {
  // TODO: share these constants with Jangaroo Language plugin:
  private static final String JANGAROO_GROUP_ID = "net.jangaroo";
  private static final String JANGAROO_PACKAGING_TYPE = "jangaroo";
  private static final String EXML_MAVEN_PLUGIN_ARTIFACT_ID = "ext-xml-maven-plugin";
  private static final String DEFAULT_EXML_FACET_NAME = "EXML";

  public ExmlFacetImporter() {
    super(JANGAROO_GROUP_ID, EXML_MAVEN_PLUGIN_ARTIFACT_ID, ExmlFacetType.INSTANCE, DEFAULT_EXML_FACET_NAME);
  }

  public boolean isApplicable(MavenProject mavenProjectModel) {
    return JANGAROO_PACKAGING_TYPE.equals(mavenProjectModel.getPackaging()) ||
      mavenProjectModel.findPlugin(JANGAROO_GROUP_ID, EXML_MAVEN_PLUGIN_ARTIFACT_ID) != null;
  }

  protected void setupFacet(ExmlFacet exmlFacet, MavenProject mavenProjectModel) {
    //System.out.println("setupFacet called!");
  }

  @Override
  protected void reimportFacet(MavenModifiableModelsProvider modelsProvider, Module module,
                               MavenRootModelAdapter rootModel, ExmlFacet exmlFacet, MavenProjectsTree mavenTree,
                               MavenProject mavenProjectModel, MavenProjectChanges changes,
                               Map<MavenProject, String> mavenProjectToModuleName, List<MavenProjectsProcessorTask> postTasks) {
    //System.out.println("reimportFacet called!");
    ExmlFacetConfiguration exmlFacetConfiguration = exmlFacet.getConfiguration();
    ExmlcConfigurationBean exmlConfig = exmlFacetConfiguration.getState();
    exmlConfig.setSourceDirectory(mavenProjectModel.getSources().get(0));
    exmlConfig.setGeneratedSourcesDirectory(mavenProjectModel.getGeneratedSourcesDirectory(false) + "/joo");
    exmlConfig.setGeneratedResourcesDirectory(getTargetOutputPath(mavenProjectModel,  "generated-resources"));
    String artifactId = mavenProjectModel.getMavenId().getArtifactId();
    exmlConfig.setNamespace(artifactId);
    exmlConfig.setNamespacePrefix(artifactId);
    exmlConfig.setXsd(artifactId + ".xsd");

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

  public void collectSourceFolders(MavenProject mavenProject, List<String> result) {
    // TODO: peek into Maven config of ext-xml goal!
    result.add("target/generated-sources/joo");
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
    if (xsdFilename != null && new File(xsdFilename).exists()) {
      try {
        addResource(resourceMap, scanner, new FileInputStream(xsdFilename), xsdFilename);
      } catch (FileNotFoundException e) {
        Logger.getInstance("exml").warn("Error while scanning XSD file " + xsdFilename, e);
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
      ExmlCompiler.getLog().warn("Error while scanning XSD file " + filename, e);
    }
  }

}