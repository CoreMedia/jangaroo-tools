package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CompilationUnitResolver;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.ZipEntryInputSource;
import net.jangaroo.jooc.input.ZipFileInputSource;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;

public class TypeScriptModuleResolver extends ModuleResolverBase {

  public TypeScriptModuleResolver(CompilationUnitResolver compilationUnitModelResolver) {
    super(compilationUnitModelResolver);
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

  private boolean isGeneratedSource(File sourceDir) {
    return sourceDir.getPath().replace(File.separatorChar, '/').endsWith("generated-sources/joo");
  }

  private boolean isTestSource(File sourceDir) {
    return sourceDir.getPath().replace(File.separatorChar, '/').endsWith("test/joo");
  }

  public String getRequireModuleName(CompilationUnit compilationUnit, IdeDeclaration declaration) {
    String moduleName = getRequireModulePath(declaration);
    if (moduleName == null) {
      return null;
    }
    InputSource importedInputSource = declaration.getCompilationUnit().getInputSource();
    if (importedInputSource instanceof FileInputSource) {
      FileInputSource currentFileInputSource = (FileInputSource) compilationUnit.getInputSource();
      FileInputSource importedFileInputSource = (FileInputSource) importedInputSource;
      boolean isModule = getRequireModulePath(compilationUnit.getPrimaryDeclaration()) != null;
      File currentSourceDir = currentFileInputSource.getSourceDir();
      File importedSourceDir = importedFileInputSource.getSourceDir();
      String prefix;
      // Only modules in the same source dir or non modules that are not in test source or generated source keep can
      // reference their target files using the normal relative path without prefix
      if (isModule && importedSourceDir.equals(currentSourceDir)
              || !isModule && !isGeneratedSource(importedSourceDir) && !isTestSource(importedSourceDir)) {
        prefix = "";
      } else {
        // otherwise a prefix is required based on the source root of the imported source
        prefix = isGeneratedSource(importedSourceDir) ? "../generated/src/" : "../src/";
      }
      File currentDir = isModule
              ? CompilerUtils.fileFromQName(compilationUnit.getPrimaryDeclaration().getExtNamespaceRelativeTargetQualifiedNameStr(),
              currentSourceDir, Jooc.TS_SUFFIX).getParentFile()
              : currentSourceDir;
      return computeRelativeModulePath(currentDir,
              new File(currentSourceDir, prefix + moduleName));
    }
    if (!(importedInputSource instanceof ZipEntryInputSource)) {
      throw new IllegalStateException("The input source for a compilation unit was not a file");
    }
    // compute target npm package name
    ZipFileInputSource inputSource = ((ZipEntryInputSource) importedInputSource).getZipFileInputSource();
    String npmPackageName = inputSource.getNpmPackageName();
    if (npmPackageName == null) {
      npmPackageName = inputSource.getSenchaPackageName();
    }
    if (npmPackageName == null) {
      return null;
    }
    if (npmPackageName.startsWith("net.jangaroo__")) {
      // well-known vendor prefix net.jangaroo -> @jangaroo
      npmPackageName = npmPackageName.replace("net.jangaroo__", "@jangaroo/");
      // very special case jangaroo-runtime -> runtime
      npmPackageName = npmPackageName.replace("/jangaroo-runtime", "/runtime");
      // another special case: 'ext-as' is replaced by 'ext-ts' for everything in namespace 'Ext' and
      // by 'joo' for everything else:
      if (npmPackageName.endsWith("ext-as")) {
        npmPackageName = npmPackageName.replace("/ext-as", moduleName.startsWith("joo/") ? "" : "/ext-ts");
      }
    }
    // prepend target npm package in front
    return npmPackageName + (moduleName.isEmpty() ? "" : "/" + moduleName);
  }

  private String computeRelativeModulePath(File currentDir, File importedFile) {
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
    if ("AS3.Error".equals(qualifiedName)) {
      return null;
    }
    return qualifiedName.replace('.', '/');
  }

  public static String getNonRequireNativeName(IdeDeclaration primaryDeclaration) {
    // special case: In TypeScript, native "Error" need not be imported:
    if ("Error".equals(primaryDeclaration.getQualifiedNameStr())) {
      return "Error";
    }
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
