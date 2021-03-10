package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.config.SearchAndReplace;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.ZipEntryInputSource;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;

public class TypeScriptModuleResolver extends ModuleResolverBase {

  private final List<SearchAndReplace> npmPackageNameReplacers;

  public TypeScriptModuleResolver(CompilationUnitResolver compilationUnitModelResolver, List<SearchAndReplace> npmPackageNameReplacers) {
    super(compilationUnitModelResolver);
    this.npmPackageNameReplacers = npmPackageNameReplacers;
  }

  public String getDefaultImportName(IdeDeclaration declaration) {
    Annotation renameAnnotation = declaration.getAnnotation(Jooc.RENAME_ANNOTATION_NAME);
    if (renameAnnotation != null) {
      String targetName = getNativeAnnotationValue(renameAnnotation);
      if (targetName != null && !targetName.isEmpty()) {
        return CompilerUtils.className(targetName);
      }
    }
    String nativeName = getNonRequireNativeName(declaration);
    if (nativeName != null) {
      return nativeName;
    }
    return declaration.getName();
  }

  public String getRequireModuleName(CompilationUnit compilationUnit, IdeDeclaration declaration) {
    String moduleName = getRequireModulePath(declaration);
    if (moduleName == null) {
      return null;
    }
    InputSource importedInputSource = declaration.getCompilationUnit().getInputSource();
    FileInputSource currentInputSource = (FileInputSource) compilationUnit.getInputSource();
    if (importedInputSource instanceof FileInputSource) {
      FileInputSource fileInputSource = (FileInputSource) importedInputSource;
      boolean isModule = getRequireModulePath(compilationUnit.getPrimaryDeclaration()) != null;
      File sourceDir = currentInputSource.getSourceDir();
      File currentTargetFile = isModule
              ? CompilerUtils.fileFromQName(compilationUnit.getPrimaryDeclaration().getExtNamespaceRelativeTargetQualifiedNameStr(),
              sourceDir, Jooc.TS_SUFFIX)
              : new File(sourceDir, "index.d.ts");
      // All source code from the same Maven module ends up in the same source directory, *but* test code:
      if (fileInputSource.getSourceDir().equals(sourceDir)
              || !sourceDir.getPath().replace(File.separatorChar, '/').endsWith("src/test/joo")) {
        // same input source or non-test-sources: relativize against current file
        return computeRelativeModulePath(currentTargetFile,
                new File(sourceDir, moduleName));
      }
      // Only references from test code to non-test code must be rewritten.
      // We know that in the target TypeScript workspace, the relative path from the test source root
      // directory to the source root directory is "../src".
      return computeRelativeModulePath(currentTargetFile,
              new File(sourceDir, "../src/" + moduleName));
    }
    if (!(importedInputSource instanceof ZipEntryInputSource)) {
      throw new IllegalStateException("The input source for a compilation unit was not a file");
    }
    // compute target npm package name
    String npmPackageName = ((ZipEntryInputSource) importedInputSource).getZipFileInputSource().getSenchaPackageName();
    if (npmPackageName == null) {
      return null;
    }
    if (npmPackageName.startsWith("net.jangaroo__")) {
      // well-known vendor prefix net.jangaroo -> @jangaroo
      npmPackageName = npmPackageName.replace("net.jangaroo__", "@jangaroo/");
      // very special case jangaroo-runtime -> joo
      npmPackageName = npmPackageName.replace("/jangaroo-runtime", "/joo");
      // another special case: 'ext-as' is replaced by 'ext-ts' for everything in namespace 'Ext' and
      // by 'joo' for everything else:
      if (npmPackageName.endsWith("ext-as")) {
        npmPackageName = npmPackageName.replace("/ext-as", moduleName.startsWith("Ext") ? "/ext-ts" : "/joo");
      }
    } else {
      for (SearchAndReplace searchAndReplace : npmPackageNameReplacers) {
        Matcher matcher = searchAndReplace.search.matcher(npmPackageName);
        if (matcher.matches()) {
          npmPackageName = matcher.replaceAll(searchAndReplace.replace);
          break;
        }
      }
    }
    // prepend target npm package in front
    return npmPackageName + "/" + moduleName;
  }

  private String computeRelativeModulePath(File currentFile, File importedFile) {
    File currentDir = currentFile.getParentFile();
    String relativeModulePath = CompilerUtils.getRelativePath(currentDir,
            importedFile, false);
    relativeModulePath = relativeModulePath.replace(File.separatorChar, '/');
    if (!relativeModulePath.startsWith(".")) {
      relativeModulePath = "./" + relativeModulePath;
    }
    return relativeModulePath;
  }

  private String getRequireModulePath(IdeDeclaration declaration) {
    Annotation nativeAnnotation = declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeAnnotation != null && getNativeAnnotationRequireValue(nativeAnnotation) == null) {
      return null;
    }
    String qualifiedName = declaration.getExtNamespaceRelativeTargetQualifiedNameStr();
    // special case: In TypeScript, "AS3.Error" is directly mapped to native "Error":
    if ("Error".equals(qualifiedName)) {
      return null;
    }
    return qualifiedName.replace('.', '/');
  }

  public static String getNonRequireNativeName(IdeDeclaration primaryDeclaration) {
    Annotation nativeAnnotation = primaryDeclaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeAnnotation != null && getAnnotationParameterValue(nativeAnnotation, Jooc.NATIVE_ANNOTATION_REQUIRE_PROPERTY, "") == null) {
      String nativeName = getNativeAnnotationValue(nativeAnnotation);
      if (nativeName == null) {
        nativeName = primaryDeclaration.getQualifiedNameStr();
      }
      if (!"Ext.Base".equals(nativeName)) {
        return nativeName;
      }
    }
    return null;
  }

  @Override
  protected String getNativeAnnotationRequireValue(Annotation nativeAnnotation) {
    // exception: Ext.Base does not need to be "required", but for TypeScript, it needs to be imported!
    if ("Ext.Base".equals(getNativeAnnotationValue(nativeAnnotation))) {
      return "";
    }
    return super.getNativeAnnotationRequireValue(nativeAnnotation);
  }

  public static String toLocalName(String[] qualifiedName) {
    return String.join("_", qualifiedName);
  }
}
