package net.jangaroo.ide.idea;

import com.intellij.facet.FacetManager;
import com.intellij.javaee.facet.JavaeeFacet;
import com.intellij.javaee.ui.packaging.ExplodedWarArtifactType;
import com.intellij.javaee.ui.packaging.JavaeeFacetResourcesPackagingElement;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.JavadocOrderRootType;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactManager;
import com.intellij.packaging.artifacts.ModifiableArtifact;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import com.intellij.packaging.elements.CompositePackagingElement;
import com.intellij.packaging.elements.PackagingElement;
import com.intellij.packaging.elements.PackagingElementResolvingContext;
import com.intellij.packaging.impl.elements.ArchivePackagingElement;
import com.intellij.packaging.impl.elements.ArtifactPackagingElement;
import com.intellij.packaging.impl.elements.DirectoryPackagingElement;
import com.intellij.packaging.impl.elements.LibraryPackagingElement;
import com.intellij.packaging.impl.elements.ModuleOutputPackagingElement;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.embedder.MavenConsole;
import org.jetbrains.idea.maven.importing.FacetImporter;
import org.jetbrains.idea.maven.importing.MavenModifiableModelsProvider;
import org.jetbrains.idea.maven.importing.MavenRootModelAdapter;
import org.jetbrains.idea.maven.project.MavenArtifact;
import org.jetbrains.idea.maven.project.MavenEmbeddersManager;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectChanges;
import org.jetbrains.idea.maven.project.MavenProjectsProcessorTask;
import org.jetbrains.idea.maven.project.MavenProjectsTree;
import org.jetbrains.idea.maven.utils.MavenConstants;
import org.jetbrains.idea.maven.utils.MavenJDOMUtil;
import org.jetbrains.idea.maven.utils.MavenProcessCanceledException;
import org.jetbrains.idea.maven.utils.MavenProgressIndicator;

import java.util.*;
import java.io.File;

import static org.jetbrains.idea.maven.importing.MavenExtraArtifactType.SOURCES;

/**
 * A Facet-from-Maven Importer for the Jangaroo Facet type.
 */
public class JangarooFacetImporter extends FacetImporter<JangarooFacet, JangarooFacetConfiguration, JangarooFacetType> {
  private static final String JANGAROO_GROUP_ID = "net.jangaroo";
  private static final String JANGAROO_MAVEN_PLUGIN_ARTIFACT_ID = "jangaroo-maven-plugin";
  private static final String JANGAROO_PACKAGING_TYPE = "jangaroo";
  private static final String DEFAULT_JANGAROO_FACET_NAME = "Jangaroo";

  public JangarooFacetImporter() {
    super(JANGAROO_GROUP_ID, JANGAROO_MAVEN_PLUGIN_ARTIFACT_ID, JangarooFacetType.INSTANCE, DEFAULT_JANGAROO_FACET_NAME);
  }

  public boolean isApplicable(MavenProject mavenProjectModel) {
    return JANGAROO_PACKAGING_TYPE.equals(mavenProjectModel.getPackaging()) ||
      mavenProjectModel.findPlugin(JANGAROO_GROUP_ID, JANGAROO_MAVEN_PLUGIN_ARTIFACT_ID) != null;
  }

  @Override
  public boolean isSupportedDependency(MavenArtifact artifact) {
    return JANGAROO_PACKAGING_TYPE.equals(artifact.getType());
  }

  @Override
  protected void setupFacet(JangarooFacet f, MavenProject mavenProject) {
    //System.out.println("setupFacet called!");
  }

  private boolean getBooleanConfigurationValue(MavenProject mavenProjectModel, String configName, boolean defaultValue) {
    String value = findGoalConfigValue(mavenProjectModel, "compile", configName);
    if (value == null) {
      value = findConfigValue(mavenProjectModel, configName);
    }
    return value != null ? Boolean.valueOf(value) : defaultValue;
  }

  @Override
  protected void reimportFacet(MavenModifiableModelsProvider modelsProvider, Module module,
                               MavenRootModelAdapter rootModel, JangarooFacet jangarooFacet,
                               MavenProjectsTree mavenTree, MavenProject mavenProjectModel,
                               MavenProjectChanges changes, Map<MavenProject, String> mavenProjectToModuleName,
                               List<MavenProjectsProcessorTask> postTasks) {
    //System.out.println("reimportFacet called!");
    JangarooFacetConfiguration jangarooFacetConfiguration = jangarooFacet.getConfiguration();
    JoocConfigurationBean jooConfig = jangarooFacetConfiguration.getState();
    jooConfig.allowDuplicateLocalVariables = getBooleanConfigurationValue(mavenProjectModel, "allowDuplicateLocalVariables", jooConfig.allowDuplicateLocalVariables);
    jooConfig.verbose = getBooleanConfigurationValue(mavenProjectModel, "verbose", false);
    jooConfig.enableAssertions = getBooleanConfigurationValue(mavenProjectModel, "enableAssertions", false);
    jooConfig.enableGuessingClasses = getBooleanConfigurationValue(mavenProjectModel, "enableGuessingClasses", true);
    jooConfig.enableGuessingMembers = getBooleanConfigurationValue(mavenProjectModel, "enableGuessingMembers", true);
    jooConfig.enableGuessingTypeCasts = getBooleanConfigurationValue(mavenProjectModel, "enableGuessingTypeCasts", false);
    // "debug" (boolean; true), "debuglevel" ("none", "lines", "source"; "source")
    jooConfig.outputDirectory = mavenProjectModel.getBuildDirectory() + File.separator + "joo" + File.separator + "scripts" + File.separator + "classes";

    ModifiableRootModel moduleRootModel = ModuleRootManager.getInstance(module).getModifiableModel();
    for (MavenArtifact mavenArtifact : mavenProjectModel.getDependencies()) {
      if (JANGAROO_PACKAGING_TYPE.equals(mavenArtifact.getType())) {
        System.out.println("Found 'jangaroo' dependency: " + mavenArtifact.getFile().getAbsolutePath());
        postTasks.add(new PatchJangarooLibraryTask(mavenArtifact));
      }
    }
    moduleRootModel.commit();
    if ("war".equals(mavenProjectModel.getPackaging())) {
      postTasks.add(new AddJangarooPackagingOutputToExplodedWebArtifactsTask(jangarooFacet));
    }
  }

  public void collectSourceFolders(MavenProject mavenProject, List<String> result) {
    collectSourceOrTestFolders(mavenProject, "compile", "src/main/joo", result);
    result.add("src/main/joo-api"); // must be a source folder in IDEA for references to API-only classes to work
  }

  public void collectTestFolders(MavenProject mavenProject, List<String> result) {
    collectSourceOrTestFolders(mavenProject, "testCompile", "src/test/joo", result);
  }

  private void collectSourceOrTestFolders(MavenProject mavenProject, String goal, String defaultDir, List<String> sourceDirs) {
    Element goalConfiguration = getGoalConfig(mavenProject, goal);
    if (goalConfiguration != null) {
      List<String> mvnSrcDirs = MavenJDOMUtil.findChildrenValuesByPath(goalConfiguration, "sources", "directory");
      if (!mvnSrcDirs.isEmpty()) {
        sourceDirs.addAll(mvnSrcDirs);
        return;
      }
    }
    sourceDirs.add(defaultDir);
  }

  private static class PatchJangarooLibraryTask implements MavenProjectsProcessorTask {
    private final MavenArtifact artifact;

    public PatchJangarooLibraryTask(MavenArtifact mavenArtifact) {
      this.artifact = mavenArtifact;
    }

    public void perform(Project project, MavenEmbeddersManager embeddersManager, MavenConsole console, MavenProgressIndicator indicator) throws MavenProcessCanceledException {
      // add Maven artifact with classifier "-sources" as module library!
      LibraryTable table = LibraryTablesRegistrar.getInstance().getLibraryTableByLevel(LibraryTablesRegistrar.PROJECT_LEVEL, project);
      if (table == null) {
        System.out.println("  Project level libraries could not be retrieved to patch 'jangaroo' dependency " + artifact.getFile().getAbsolutePath());
        return;
      }
      String libName = "Maven: " + artifact.getDisplayStringForLibraryName();
      final Library library = table.getLibraryByName(libName);
      if (library == null) {
        System.out.println("  Project level library '"+libName+"' not found, cannot patch 'jangaroo' dependency " + artifact.getFile().getAbsolutePath());
        return;
      }
      ApplicationManager.getApplication().invokeLater(new Runnable() {
        public void run() {
          ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
              Library.ModifiableModel libraryModel = library.getModifiableModel();
              // Jangaroo specialty: add a CLASSES root for the SOURCES artifact!
              String newUrl = artifact.getUrlForExtraArtifact(SOURCES.getDefaultClassifier(), SOURCES.getDefaultExtension());
              for (String url : libraryModel.getUrls(OrderRootType.CLASSES)) {
                if (newUrl != null && newUrl.equals(url)) {
                  newUrl = null; // do not add again!
                  break;
                }
                if (MavenConstants.SCOPE_SYSTEM.equals(artifact.getScope()) || isRepositoryUrl(artifact, url, SOURCES.getDefaultClassifier())) {
                  libraryModel.removeRoot(url, OrderRootType.CLASSES);
                }
              }
              if (newUrl != null) {
                libraryModel.addRoot(newUrl, OrderRootType.CLASSES);
              }
              // Jangaroo specialty: there is no javadoc!
              OrderRootType javadocOrderRootType = JavadocOrderRootType.getInstance();
              for (String javadocUrl : libraryModel.getUrls(javadocOrderRootType)) {
                libraryModel.removeRoot(javadocUrl, javadocOrderRootType);
              }
              libraryModel.commit();
            }
          });
        }
      });
    }

    private boolean isRepositoryUrl(MavenArtifact artifact, String url, String classifier) {
      return url.endsWith(artifact.getRelativePathForExtraArtifact(classifier, SOURCES.getDefaultExtension()) + JarFileSystem.JAR_SEPARATOR);
    }


  }

  private static class AddJangarooPackagingOutputToExplodedWebArtifactsTask implements MavenProjectsProcessorTask {
    private final JangarooFacet jangarooFacet;

    private AddJangarooPackagingOutputToExplodedWebArtifactsTask(JangarooFacet jangarooFacet) {
      this.jangarooFacet = jangarooFacet;
    }

    public void perform(final Project project, MavenEmbeddersManager embeddersManager, MavenConsole console, MavenProgressIndicator indicator) throws MavenProcessCanceledException {
      ApplicationManager.getApplication().runReadAction(new Runnable() {
        public void run() {
          Module webModule = jangarooFacet.getModule();
          // for this Jangaroo-enabled Web app, add all Jangaroo-dependent modules' Jangaroo compiler output.

          // find the IDEA exploded Web artifact for this Jangaroo-enabled Web app module:
          final Artifact artifact = getExplodedWebArtifact(webModule);
          if (artifact != null) {
            // instruct IDEA to build the Web app on make:
            final ArtifactManager artifactManager = ArtifactManager.getInstance(project);
            if (!artifact.isBuildOnMake()) {
              ApplicationManager.getApplication().invokeLater(new Runnable() {
                public void run() {
                  ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                      ModifiableArtifactModel modifiableArtifactModel = artifactManager.createModifiableModel();
                      final ModifiableArtifact modifiableArtifact = modifiableArtifactModel.getOrCreateModifiableArtifact(artifact);
                      modifiableArtifact.setBuildOnMake(true);
                      modifiableArtifactModel.commit();
                    }
                  });
                }
              });
            }

            // get all Jangaroo Facets used by this Web app:
            Set<JangarooFacet> dependencies = getTransitiveJangarooDependencies(jangarooFacet);
            // remove all Jangaroo Facets already contained in some overlay:
            Set<JangarooFacet> overlays = findTransitiveJangarooOverlays(artifactManager, artifact);
            for (JangarooFacet overlay : overlays) {
              dependencies.remove(overlay);
              dependencies.removeAll(getTransitiveJangarooDependencies(overlay));
            }
            // add the remaining modules' Jangaroo packaging output to the Web app's root directory:
            CompositePackagingElement<?> rootDirectory = artifact.getRootElement();
            removeJangarooJarsFromWebInfLib(project, rootDirectory);
            for (JangarooFacet dependency : dependencies) {
              rootDirectory.addOrFindChild(new JangarooPackagingOutputElement(project, dependency));
            }
          }
        }
      });
    }

    private static void removeJangarooJarsFromWebInfLib(Project project, CompositePackagingElement<?> rootDirectory) {
      DirectoryPackagingElement libDir = rootDirectory.addOrFindChild(new DirectoryPackagingElement("WEB-INF")).addOrFindChild(new DirectoryPackagingElement("lib"));
      PackagingElementResolvingContext packagingElementResolvingContext = ArtifactManager.getInstance(project).getResolvingContext();
      ModuleManager moduleManager = ModuleManager.getInstance(project);
      Collection<PackagingElement<?>> toBeRemovedLibraries = new ArrayList<PackagingElement<?>>();
      for (PackagingElement packagingElement : libDir.getChildren()) {
        if (packagingElement instanceof LibraryPackagingElement) {
          Library library = ((LibraryPackagingElement)packagingElement).findLibrary(packagingElementResolvingContext);
          if (library.getName().indexOf(":jangaroo:") != -1) {
            toBeRemovedLibraries.add(packagingElement);
          }
        } else if (packagingElement instanceof ArchivePackagingElement) {
          List<PackagingElement<?>> archiveChildren = ((ArchivePackagingElement)packagingElement).getChildren();
          if (!archiveChildren.isEmpty()) {
            PackagingElement<?> modulePackagingElement = archiveChildren.get(0);
            if (modulePackagingElement instanceof ModuleOutputPackagingElement) {
              Module module = moduleManager.findModuleByName(((ModuleOutputPackagingElement)modulePackagingElement).getModuleName());
              if (FacetManager.getInstance(module).getFacetsByType(JangarooFacetType.ID) != null) {
                toBeRemovedLibraries.add(packagingElement);
              }
            }
          }
        }
      }
      libDir.removeChildren(toBeRemovedLibraries);
    }

    private static Artifact getExplodedWebArtifact(Module module) {
      ArtifactManager artifactManager = ArtifactManager.getInstance(module.getProject());
      for (Artifact artifact : artifactManager.getArtifactsByType(ExplodedWarArtifactType.getInstance())) {
        Module artifactModule = findModule(artifactManager, artifact);
        if (module.equals(artifactModule)) {
          return artifact;
        }
      }
      return null;
    }

    private static @Nullable Module findModule(@NotNull ArtifactManager artifactManager, @NotNull Artifact artifact) {
      PackagingElementResolvingContext packagingElementResolvingContext = artifactManager.getResolvingContext();
      for (PackagingElement<?> packagingElement : artifact.getRootElement().getChildren()) {
        if (packagingElement instanceof JavaeeFacetResourcesPackagingElement) {
          JavaeeFacet facet = ((JavaeeFacetResourcesPackagingElement) packagingElement).findFacet(packagingElementResolvingContext);
          if (facet != null) {
            return facet.getModule();
          }
        }
      }
      return null;
    }

    private static Set<JangarooFacet> findTransitiveJangarooOverlays(ArtifactManager artifactManager, Artifact artifact) {
      Set<JangarooFacet> overlays = new LinkedHashSet<JangarooFacet>();
      for (PackagingElement<?> packagingElement : artifact.getRootElement().getChildren()) {
        if (packagingElement instanceof ArtifactPackagingElement) {
          String artifactName = ((ArtifactPackagingElement) packagingElement).getArtifactName();
          if (artifactName != null) {
            Artifact overlayArtifact = artifactManager.findArtifact(artifactName);
            if (overlayArtifact != null) {
              Module overlayModule = findModule(artifactManager, overlayArtifact);
              if (overlayModule != null) {
                JangarooFacet overlayJangarooFacet = findJangarooFacet(overlayModule);
                if (overlayJangarooFacet != null) {
                  overlays.add(overlayJangarooFacet);
                }
                overlays.addAll(findTransitiveJangarooOverlays(artifactManager, overlayArtifact));
              }
            }
          }
        }
      }
      return overlays;
    }

    private static Set<JangarooFacet> getTransitiveJangarooDependencies(JangarooFacet jangarooFacet) {
      return collectTransitiveJangarooDependencies(jangarooFacet, new LinkedHashSet<JangarooFacet>());
    }

    private static Set<JangarooFacet> collectTransitiveJangarooDependencies(JangarooFacet jangarooFacet, Set<JangarooFacet> dependencies) {
      dependencies.add(jangarooFacet);
      for (Module directDependency : ModuleRootManager.getInstance(jangarooFacet.getModule()).getDependencies()) {
        JangarooFacet dependentModuleJangarooFacet = findJangarooFacet(directDependency);
        if (dependentModuleJangarooFacet != null) {
          collectTransitiveJangarooDependencies(dependentModuleJangarooFacet, dependencies);
        }
      }
      return dependencies;
    }

    private static JangarooFacet findJangarooFacet(Module module) {
      return FacetManager.getInstance(module).findFacet(JangarooFacetType.ID, DEFAULT_JANGAROO_FACET_NAME);
    }
  }
}
