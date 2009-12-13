package net.jangaroo.ide.idea;

import com.intellij.facet.FacetManager;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.javaee.web.make.CustomWebBuildParticipant;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.make.BuildRecipe;
import com.intellij.openapi.deployment.DeploymentUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.compiler.impl.packagingCompiler.FileCopyInstructionImpl;

import java.io.File;
import java.util.Set;
import java.util.HashSet;

/**
 * A CustomWebBuildParticipant that copies the results of Jangaroo compilation from dependent modules to the exploded
 * Web directory of the Web module containing the dependencies.
 */
public class JangarooWebBuildParticipant extends CustomWebBuildParticipant {

  public void registerBuildInstructions(WebFacet webFacet, BuildRecipe buildRecipe, CompileContext compileContext) {
    Module webModule = webFacet.getModule();
    String explodedDir = webFacet.getBuildConfiguration().getBuildProperties().getExplodedPath() + "/";
    // TODO: compute relativePath from webModule's JangarooFacet, if it exists:
    String relativePath = DeploymentUtil.trimForwardSlashes("/scripts/classes");
    OrderEntry[] orderEntries = ModuleRootManager.getInstance(webModule).getOrderEntries();
    // determine all used modules first to avoid double copying on redundant dependencies:
    Set<Module> usedModules = new HashSet<Module>(orderEntries.length);
    for (OrderEntry orderEntry : orderEntries) {
      if (orderEntry instanceof ModuleOrderEntry) {
        usedModules.add(((ModuleOrderEntry)orderEntry).getModule());
      }
    }
    for (Module module : usedModules) {
      FacetManager facetManager = FacetManager.getInstance(module);
      JangarooFacet webJangarooFacet = facetManager.getFacetByType(JangarooFacetType.ID);
      if (webJangarooFacet != null) {
        File outputDirectory = webJangarooFacet.getConfiguration().getState().getOutputDirectory();
        if (outputDirectory.exists()) {
          compileContext.addMessage(CompilerMessageCategory.INFORMATION, "Jangaroo Web build: " + outputDirectory + " -> " + explodedDir + relativePath, null, -1, -1);
          FileCopyInstructionImpl instruction = new FileCopyInstructionImpl(outputDirectory, true, module, DeploymentUtil.trimForwardSlashes(relativePath), null);
          buildRecipe.addInstruction(instruction);
        }
      }
    }
  }

}
