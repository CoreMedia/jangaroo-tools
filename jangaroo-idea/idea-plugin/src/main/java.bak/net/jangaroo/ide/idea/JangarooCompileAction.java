package net.jangaroo.ide.idea;

import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.filters.RegexpFilter;
import com.intellij.execution.process.DefaultJavaProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.compiler.make.BuildConfiguration;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.EmptyRunnable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.ui.Messages;
import com.intellij.facet.FacetManager;
import com.intellij.javaee.web.facet.WebFacet;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;


/**
 * Action to invoke Jangaroo Compilation.
 */
public class JangarooCompileAction extends AnAction {
  public void actionPerformed(AnActionEvent e) {
    Project project = DataKeys.PROJECT.getData(e.getDataContext());
    if (project!=null) {
      Pair<Module,VirtualFile[]> moduleAndSourceFiles = getModuleAndSourceFiles(e.getDataContext());
      Module module = moduleAndSourceFiles.first;
      VirtualFile[] files = moduleAndSourceFiles.second;
      if (module != null) {
        String compilerOutputPath = getCompilerOutputPath(module);
        if (compilerOutputPath!=null) {
          Application application = ApplicationManager.getApplication();
          JoocApplicationComponent joocAppComponent = application.getComponent(JoocApplicationComponent.class);
          String jangarooHomeDirPath = joocAppComponent.getJangarooHomeDir();
          File jangarooHomeDir = jangarooHomeDirPath==null ? null : new File(jangarooHomeDirPath);
          // show error if Jangaroo Home Dir is not set:
          if (jangarooHomeDir==null || !jangarooHomeDir.exists()) {
            Messages.showErrorDialog(project, "Please configure correct location of Jangaroo Home Directory.\n"
              +"Currently configured path is "+jangarooHomeDirPath, "Jangaroo");
            return;
          }        
          // first, trigger save:
          ActionManager.getInstance().getAction("SaveAll").actionPerformed(e);
          compile(project, jangarooHomeDir, compilerOutputPath, files);
          return;
        }
      }
    }
    // show info that there was nothing to compile:
    Messages.showInfoMessage(project, "Please select ActionScript 3 files under some source root and try again.",
      "Jangaroo");
    
  }

  private static String getCompilerOutputPath(Module module) {
    String compilerOutputPath = getCompilerOutputPathFromWebFacet(module);
    if (compilerOutputPath==null) {
      compilerOutputPath = getCompilerOutputPathFromCompilerSettings(module);
    }
    return compilerOutputPath;
  }

  private static String getCompilerOutputPathFromCompilerSettings(Module module) {
    CompilerModuleExtension compilerModuleExtension = CompilerModuleExtension.getInstance(module);
    if (compilerModuleExtension!=null) {
      VirtualFile compilerOutputVF = compilerModuleExtension.getCompilerOutputPath();
      if (compilerOutputVF!=null) {
        return compilerOutputVF.getPath();
      }
    }
    return null;
  }

  private static String getCompilerOutputPathFromWebFacet(Module module) {
    FacetManager facetManager = FacetManager.getInstance(module);
    Collection<WebFacet> webFacets = facetManager.getFacetsByType(WebFacet.ID);
    if (!webFacets.isEmpty()) {
      WebFacet webFacet = webFacets.iterator().next();
      BuildConfiguration buildProperties = webFacet.getBuildConfiguration().getBuildProperties();
      if (buildProperties.isExplodedEnabled()) {
        return buildProperties.getExplodedPath(); // TODO: plus configurable prefix
      }
    }
    return null;
  }

  @Override
  public void update(AnActionEvent e) {
    super.update(e);    //To change body of overridden methods use File | Settings | File Templates.
    Pair<Module,VirtualFile[]> moduleAndSourceFiles = getModuleAndSourceFiles(e.getDataContext());
    // Visibility
    //e.getPresentation().setVisible(visible);
    // Enable or disable
    e.getPresentation().setEnabled(moduleAndSourceFiles.first!=null);
  }

  private Pair<Module,VirtualFile[]> getModuleAndSourceFiles(DataContext dataContext) {
    Project project = DataKeys.PROJECT.getData(dataContext);
    Module module = null;
    List<VirtualFile> sourceFiles = new ArrayList<VirtualFile>();
    if (project!=null) {
      VirtualFile[] sourceRoots = null;
      VirtualFile[] files = DataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
      if (files!=null) {
        for (VirtualFile file : files) {
          if (file.exists() && "as".equals(file.getExtension())) {
            Module sourceModule = ModuleUtil.findModuleForFile(file, project);
            if (sourceModule!=null) {
              if (module==null) {
                module = sourceModule;
                sourceRoots = ModuleRootManager.getInstance(module).getSourceRoots();
              }
              if (module==sourceModule) { // do not add sources from different module => inconsistent settings!
                for (VirtualFile sourceRoot : sourceRoots) { // only use files under some source root!
                  if (file.getPath().startsWith(sourceRoot.getPath())) {
                    sourceFiles.add(file);
                    break;
                  }
                }
              }
            }
          }
        }
      }
    }
    return new Pair<Module,VirtualFile[]>(module,sourceFiles.toArray(new VirtualFile[sourceFiles.size()]));
  }

  private static String ID = "Jangaroo Console";
  private static String TITLE = "Jangaroo Compiler Output";
  private static ConsoleView view = null;
  static String JANGAROO_ICON_URL = "/net/jangaroo/jooley-16x16.png";
  private static String JANGAROO_LIB_SUBDIR = "lib";

  public static void compile(Project project, File jangarooHomeDir, String outputDir, VirtualFile[] sources) {
    try {
      if (project != null) {
        ToolWindowManager manager = ToolWindowManager.getInstance(project);

        synchronized (JangarooCompileAction.class) {
          if (view == null) {
            TextConsoleBuilderFactory factory = TextConsoleBuilderFactory.getInstance();
            TextConsoleBuilder builder = factory.createBuilder(project);
            view = builder.getConsole();
          }
        }

        JavaParameters parameters = new JavaParameters();
        parameters.setJdk(ProjectRootManager.getInstance(project).getProjectJdk());
        File libDir = new File(jangarooHomeDir.getPath() + File.separator + JANGAROO_LIB_SUBDIR);
        File[] jars = libDir.listFiles(new FilenameFilter() {
          public boolean accept(File dir, String name) {
            return name.endsWith(".jar");
          }
        });
        for (File jar : jars) {
          parameters.getClassPath().add(jar.getPath());
        }
        parameters.setMainClass("net.jangaroo.jooc.Jooc");
        parameters.getProgramParametersList().add("-v");
        for (VirtualFile source : sources) {
          parameters.getProgramParametersList().add(source.getPath());
        }
        parameters.getProgramParametersList().add("-d");
        parameters.getProgramParametersList().add(outputDir);

        ToolWindow window = manager.getToolWindow(ID);

        if (window == null) {
          window = manager.registerToolWindow(ID, view.getComponent(), ToolWindowAnchor.BOTTOM);
          window.setTitle(TITLE);
          window.setIcon(IconLoader.getIcon(JANGAROO_ICON_URL));
          window.setToHideOnEmptyContent(true);
        }
        window.show(EmptyRunnable.getInstance());
        DefaultJavaProcessHandler handler = new DefaultJavaProcessHandler(parameters);
        view.addMessageFilter(new RegexpFilter(project, "$FILE_PATH$\\($LINE$\\): [EW][a-z]*: in column $COLUMN$:.*"));
        view.attachToProcess(handler);
        view.clear();
        handler.startNotify();
        int exitVal = handler.getProcess().waitFor();
        view.print("Compilation completed " + (exitVal==0 ? "successfully." : "with errors."),
          exitVal==0 ? ConsoleViewContentType.NORMAL_OUTPUT : ConsoleViewContentType.ERROR_OUTPUT);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
