package net.jangaroo.ide.idea;

import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.LocalFileSystem;
import org.jetbrains.idea.maven.project.MavenProjectModel;
import org.jetbrains.idea.maven.project.MavenRootModelAdapter;
import org.jetbrains.idea.maven.project.MavenProjectsTree;
import org.jetbrains.idea.maven.project.PostProjectConfigurationTask;
import org.jetbrains.idea.maven.project.MavenArtifact;
import org.jdom.Element;

import java.util.Map;
import java.util.List;
import java.io.File;

/**
 * A Facet-from-Maven Importer for the Jangaroo Facet type.
 */
public class JangarooFacetImporter extends org.jetbrains.idea.maven.facets.FacetImporter<JangarooFacet, JangarooFacetConfiguration, JangarooFacetType> {
  private static final String JANGAROO_GROUP_ID = "net.jangaroo";
  private static final String JANGAROO_MAVEN_PLUGIN_ARTIFACT_ID = "jangaroo-maven-plugin";
  private static final String JANGAROO_PACKAGING_TYPE = "jangaroo";
  private static final String DEFAULT_JANGAROO_FACET_NAME = "Jangaroo";

  public JangarooFacetImporter() {
    super(JANGAROO_GROUP_ID, JANGAROO_MAVEN_PLUGIN_ARTIFACT_ID, JangarooFacetType.INSTANCE, DEFAULT_JANGAROO_FACET_NAME);
  }

  public boolean isApplicable(MavenProjectModel mavenProjectModel) {
    return mavenProjectModel.findPlugin(myPluginGroupID, myPluginArtifactID) != null;
  }

  @Override
  public boolean isSupportedDependency(MavenArtifact artifact) {
    return JANGAROO_PACKAGING_TYPE.equals(artifact.getType());
  }

  protected void setupFacet(JangarooFacet jangarooFacet, MavenProjectModel mavenProjectModel) {
    //System.out.println("setupFacet called!");
  }

  private boolean getBooleanConfigurationValue(MavenProjectModel mavenProjectModel, String configName, boolean defaultValue) {
    String value = findGoalConfigValue(mavenProjectModel, "war-compile", configName);
    if (value == null) {
      value = findGoalConfigValue(mavenProjectModel, "compile", configName);
    }
    if (value == null) {
      value = findConfigValue(mavenProjectModel, configName);
    }
    return value != null ? Boolean.valueOf(value) : defaultValue;
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
      libraryModel.addRoot(jarVirtualFile, OrderRootType.CLASSES);
      libraryModel.commit();
    }
  }
  
  protected void reimportFacet(ModifiableModuleModel modifiableModuleModel, Module module, MavenRootModelAdapter mavenRootModelAdapter, JangarooFacet jangarooFacet, MavenProjectsTree mavenProjectsTree, MavenProjectModel mavenProjectModel, Map<MavenProjectModel, String> mavenProjectModelStringMap, List<PostProjectConfigurationTask> postProjectConfigurationTasks) {
    //System.out.println("reimportFacet called!");
    JoocConfigurationBean jooConfig = jangarooFacet.getConfiguration().getState();
    jooConfig.allowDuplicateLocalVariables = getBooleanConfigurationValue(mavenProjectModel, "allowDuplicateLocalVariables", jooConfig.allowDuplicateLocalVariables);
    jooConfig.verbose = getBooleanConfigurationValue(mavenProjectModel, "verbose", false);
    jooConfig.enableAssertions = getBooleanConfigurationValue(mavenProjectModel, "enableAssertions", false);
    jooConfig.enableGuessingClasses = getBooleanConfigurationValue(mavenProjectModel, "enableGuessingClasses", true);
    jooConfig.enableGuessingMembers = getBooleanConfigurationValue(mavenProjectModel, "enableGuessingMembers", true);
    jooConfig.enableGuessingTypeCasts = getBooleanConfigurationValue(mavenProjectModel, "enableGuessingTypeCasts", false);
    // "debug" (boolean; true), "debuglevel" ("none", "lines", "source"; "source")
    jooConfig.outputDirectory = "war".equals(mavenProjectModel.getPackaging())
      ? mavenProjectModel.getBuildDirectory() + File.separator + mavenProjectModel.getMavenModel().getBuild().getFinalName() + File.separator + "scripts" + File.separator + "classes"
      : mavenProjectModel.getOutputDirectory() + File.separator + "joo" + File.separator + "classes";

//    ModifiableRootModel moduleRootModel = ModuleRootManager.getInstance(module).getModifiableModel();
//    for (MavenArtifact mavenArtifact : mavenProjectModel.getDependencies()) {
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
