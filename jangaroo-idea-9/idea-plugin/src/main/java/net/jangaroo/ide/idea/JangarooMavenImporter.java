package net.jangaroo.ide.idea;

import com.intellij.facet.FacetManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.LocalFileSystem;
import org.jetbrains.idea.maven.importing.MavenModifiableModelsProvider;
import org.jetbrains.idea.maven.importing.MavenRootModelAdapter;
import org.jetbrains.idea.maven.project.*;

import java.util.Map;
import java.util.List;
import java.io.File;

/**
 * A Facet-from-Maven Importer for the Jangaroo Facet type.
 */
public class JangarooMavenImporter extends org.jetbrains.idea.maven.importing.MavenImporter {
  private static final String JANGAROO_GROUP_ID = "net.jangaroo";
  private static final String JANGAROO_LIFECYCLE_MAVEN_PLUGIN_ARTIFACT_ID = "jangaroo-lifecycle";
  private static final String JANGAROO_MAVEN_PLUGIN_ARTIFACT_ID = "jangaroo-maven-plugin";
  private static final String JANGAROO_PACKAGING_TYPE = "jangaroo";
  private static final String DEFAULT_JANGAROO_FACET_NAME = "Jangaroo";

  public JangarooMavenImporter() {
    super();
  }

  @Override
  public boolean isApplicable(MavenProject mavenProjectModel) {
    return JANGAROO_PACKAGING_TYPE.equals(mavenProjectModel.getPackaging()) ||
      mavenProjectModel.findPlugin(JANGAROO_GROUP_ID, JANGAROO_LIFECYCLE_MAVEN_PLUGIN_ARTIFACT_ID) != null ||
      mavenProjectModel.findPlugin(JANGAROO_GROUP_ID, JANGAROO_MAVEN_PLUGIN_ARTIFACT_ID) != null;
  }

  @Override
  public boolean isSupportedDependency(MavenArtifact artifact) {
    return JANGAROO_PACKAGING_TYPE.equals(artifact.getType());
  }

  private boolean getBooleanConfigurationValue(MavenProject mavenProject, String configName, boolean defaultValue) {
    String value = findGoalConfigValue(mavenProject, "war-compile", configName);
    if (value == null) {
      value = findGoalConfigValue(mavenProject, "compile", configName);
    }
    if (value == null) {
      value = findConfigValue(mavenProject, configName);
    }
    return value != null ? Boolean.valueOf(value) : defaultValue;
  }

  private String findGoalConfigValue(MavenProject mavenProject, String goalName, String configName) {
    return mavenProject.findPluginGoalConfigurationValue(JANGAROO_GROUP_ID, JANGAROO_MAVEN_PLUGIN_ARTIFACT_ID, goalName, configName);
  }

  private String findConfigValue(MavenProject mavenProject, String configName) {
    return mavenProject.findPluginConfigurationValue(JANGAROO_GROUP_ID, JANGAROO_MAVEN_PLUGIN_ARTIFACT_ID, configName);
  }

  private void createModuleLibrary(ModifiableRootModel moduleRootModel, MavenArtifact artifact) {
    // add Maven artifact with classifier "-sources" as module library!
    LibraryTable table = moduleRootModel.getModuleLibraryTable();
    String libName = "Maven: " + artifact.getMavenId();
    Library library = table.getLibraryByName(libName);
    if (library == null) {
      library = table.createLibrary(libName);
      Library.ModifiableModel libraryModel = library.getModifiableModel();
      String jarPath = artifact.getPath();
      int lastDotPos = jarPath.lastIndexOf('.');
      jarPath = jarPath.substring(0, lastDotPos) + "-sources" + jarPath.substring(lastDotPos);
      VirtualFile jarVirtualFile = LocalFileSystem.getInstance().findFileByPath(jarPath);
      if (jarVirtualFile != null) {
        libraryModel.addRoot(jarVirtualFile, OrderRootType.CLASSES);
        libraryModel.commit();
      }
    }
  }

  @Override
  public void preProcess(Module module, MavenProject mavenProject, MavenProjectChanges mavenProjectChanges, MavenModifiableModelsProvider mavenModifiableModelsProvider) {
    // TODO: anything to do here?
  }

  @Override
  public void process(MavenModifiableModelsProvider mavenModifiableModelsProvider, Module module, MavenRootModelAdapter mavenRootModelAdapter, MavenProjectsTree mavenProjectsTree, MavenProject mavenProject, MavenProjectChanges mavenProjectChanges, Map<MavenProject, String> mavenProjectStringMap, List<MavenProjectsProcessorTask> postProjectConfigurationTasks) {
    FacetManager facetManager = FacetManager.getInstance(module);
    JangarooFacet jangarooFacet = facetManager.getFacetByType(JangarooFacetType.ID);
    if (jangarooFacet == null) {
      jangarooFacet = facetManager.addFacet(JangarooFacetType.INSTANCE, DEFAULT_JANGAROO_FACET_NAME, null);
    }
    JoocConfigurationBean jooConfig = jangarooFacet.getConfiguration().getState();
    jooConfig.allowDuplicateLocalVariables = getBooleanConfigurationValue(mavenProject, "allowDuplicateLocalVariables", jooConfig.allowDuplicateLocalVariables);
    jooConfig.verbose = getBooleanConfigurationValue(mavenProject, "verbose", false);
    jooConfig.enableAssertions = getBooleanConfigurationValue(mavenProject, "enableAssertions", false);
    jooConfig.enableGuessingClasses = getBooleanConfigurationValue(mavenProject, "enableGuessingClasses", true);
    jooConfig.enableGuessingMembers = getBooleanConfigurationValue(mavenProject, "enableGuessingMembers", true);
    jooConfig.enableGuessingTypeCasts = getBooleanConfigurationValue(mavenProject, "enableGuessingTypeCasts", false);
    // "debug" (boolean; true), "debuglevel" ("none", "lines", "source"; "source")
    jooConfig.outputDirectory = "war".equals(mavenProject.getPackaging())
      ? mavenProject.getBuildDirectory() + File.separator + mavenProject.getMavenModel().getBuild().getFinalName() + File.separator + "scripts" + File.separator + "classes"
      : mavenProject.getBuildDirectory() + File.separator + "joo" + File.separator + "classes";

//    ModifiableRootModel moduleRootModel = ModuleRootManager.getInstance(module).getModifiableModel();
//    for (MavenArtifact mavenArtifact : mavenProject.getDependencies()) {
//      if (JANGAROO_PACKAGING_TYPE.equals(mavenArtifact.getType())) {
//        System.out.println("Found 'jangaroo' dependency: " + mavenArtifact.getFile().getAbsolutePath());
//        createModuleLibrary(moduleRootModel, mavenArtifact);
//      }
//    }
//    moduleRootModel.commit();
  }

  /*
  public void collectSourceFolders(MavenProjectModel mavenProject, List<String> result) {
    collectSourceOrTestFolders(mavenProject, "compile", "src/main/joo", result);
  }

  public void collectTestFolders(MavenProjectModel mavenProject, List<String> result) {
    collectSourceOrTestFolders(mavenProject, "testCompile", "src/test/joo", result);
  }

  private void collectSourceOrTestFolders(MavenProjectModel mavenProject, String goal, String defaultDir, List<String> result) {
    Element sourcesElement = findGoalConfigNode(mavenProject, goal, "sources");
    if (sourcesElement == null) {
      result.add((new StringBuilder()).append(mavenProject.getDirectory()).append("/").append(defaultDir).toString());
      return;
    }
    for (Object each : sourcesElement.getChildren("fileset")) {
      String dir = findChildElementValue((Element)each, "directory", null);
      if (dir != null) {
        result.add(dir);
      }
    }
  }

  public void collectExcludedFolders(MavenProjectModel mavenProject, List<String> result) {
    String stubsDir = findGoalConfigValue(mavenProject, "generateStubs", "outputDirectory");
    String testStubsDir = findGoalConfigValue(mavenProject, "generateTestStubs", "outputDirectory");
    String defaultStubsDir = (new StringBuilder()).append(mavenProject.getGeneratedSourcesDirectory()).append("/joo").toString();
    result.add(stubsDir != null ? stubsDir : defaultStubsDir);
    result.add(testStubsDir != null ? testStubsDir : defaultStubsDir);
  }
  */
}
