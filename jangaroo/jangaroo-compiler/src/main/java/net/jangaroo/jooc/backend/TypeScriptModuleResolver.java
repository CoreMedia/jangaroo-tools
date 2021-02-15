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
import net.jangaroo.jooc.input.ZipFileInputSource;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class TypeScriptModuleResolver extends ModuleResolverBase {

  private List<SearchAndReplace> npmPackageNameReplacers;

  public TypeScriptModuleResolver(CompilationUnitResolver compilationUnitModelResolver, List<SearchAndReplace> npmPackageNameReplacers) {
    super(compilationUnitModelResolver);
    this.npmPackageNameReplacers = npmPackageNameReplacers;
  }

  public Set<DefaultImport> getDefaultImports(String sourceCompilationUnitId, Set<String> compilationUnitIds) {
    return getDefaultImports(sourceCompilationUnitId, compilationUnitIds, new HashSet<>());
  }

  public Set<DefaultImport> getDefaultImports(String sourceCompilationUnitId, Set<String> compilationUnitIds, Set<String> usedLocalNames) {
    CompilationUnit sourceCompilationUnit = compilationUnitModelResolver.resolveCompilationUnit(sourceCompilationUnitId);
    final Set<String> localNames = new HashSet<>(usedLocalNames);
    return compilationUnitIds.stream().map(compilationUnitId -> {
      CompilationUnit dependentCompilationUnitModel = compilationUnitModelResolver.resolveCompilationUnit(compilationUnitId);
      IdeDeclaration dependentPrimaryDeclaration = dependentCompilationUnitModel.getPrimaryDeclaration();
      String requireModuleName = getRequireModuleName(sourceCompilationUnit, dependentPrimaryDeclaration);
      String localName = getDefaultImportName(dependentPrimaryDeclaration);
      if (requireModuleName != null) {
        if (localNames.contains(localName)) {
          localName = toLocalName(dependentPrimaryDeclaration.getQualifiedName());
        }
        localNames.add(localName);
        return new DefaultImport(dependentCompilationUnitModel, localName, requireModuleName);
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.toSet());
  }

  public String getDefaultImportName(IdeDeclaration declaration) {
    String nativeName = getNonRequireNativeName(declaration);
    if (nativeName != null) {
      return nativeName;
    }
    Annotation renameAnnotation = declaration.getAnnotation(Jooc.RENAME_ANNOTATION_NAME);
    if (renameAnnotation != null) {
      String targetName = getNativeAnnotationValue(renameAnnotation);
      if (targetName != null && !targetName.isEmpty()) {
        return CompilerUtils.className(targetName);
      }
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
      File currentTargetFile = isModule
              ? CompilerUtils.fileFromQName(compilationUnit.getPrimaryDeclaration().getTargetQualifiedNameStr(),
              currentInputSource.getSourceDir(), Jooc.TS_SUFFIX)
              : new File(currentInputSource.getSourceDir(), "index.d.ts");
      // All source code from the same Maven module ends up in the same source directory, *but* test code:
      if (fileInputSource.getSourceDir().equals(currentInputSource.getSourceDir())
              || !currentInputSource.getSourceDir().getPath().replace(File.separatorChar, '/').endsWith("src/test/joo")) {
        // same input source or non-test-sources: relativize against current file
        return computeRelativeModulePath(currentTargetFile,
                new File(currentInputSource.getSourceDir(), moduleName));
      }
      // Only references from test code to non-test code must be rewritten.
      // We know that in the target TypeScript workspace, the relative path from the test source root
      // directory to the source root directory is "../src". This is achieved by creating
      // two absolute paths, one in the dummy root directory "/tests" (name is arbitrary)
      // and one which is just the root directory "/src". Then, the 'modulePath' is added.
      return computeRelativeModulePath(currentTargetFile,
              new File(currentInputSource.getSourceDir(), "../src/" + moduleName));
    }
    if (!(importedInputSource instanceof ZipEntryInputSource)) {
      throw new IllegalStateException("The input source for a compilation unit was not a file");
    }
    // compute target npm package name
    String npmPackageName = findSenchaPackageName((ZipEntryInputSource) importedInputSource);
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

  private String findSenchaPackageName(ZipEntryInputSource zipEntryInputSource) {
    ZipFileInputSource zipFileInputSource = zipEntryInputSource.getZipFileInputSource();
    InputSource groupIdDir = getFirstSubDirectory(zipFileInputSource.getChild("META-INF/maven"));
    InputSource artifactIdDir = getFirstSubDirectory(groupIdDir);
    if (groupIdDir != null && artifactIdDir != null) {
      return groupIdDir.getName() + "__" + artifactIdDir.getName();
    }
    return null;
  }

  private InputSource getFirstSubDirectory(InputSource directory) {
    if (directory == null) {
      return null;
    }
    List<? extends InputSource> list = directory.list();
    return list.size() >= 1 ? list.get(0) : null;
  }

  private String getRequireModulePath(IdeDeclaration declaration) {
    // exception: In TypeScript, "AS3.Error" is directly mapped to native "Error":
    String qualifiedName = declaration.getQualifiedNameStr();
    if ("Error".equals(qualifiedName)) {
      return null;
    }
    Annotation nativeAnnotation = declaration.getAnnotation(Jooc.NATIVE_ANNOTATION_NAME);
    if (nativeAnnotation == null) {
      Annotation renameAnnotation = declaration.getAnnotation(Jooc.RENAME_ANNOTATION_NAME);
      if (renameAnnotation != null) {
        qualifiedName = getNativeAnnotationValue(renameAnnotation);
      }
    } else {
      if (getNativeAnnotationRequireValue(nativeAnnotation) == null) {
        return null;
      }
      String nativeAnnotationValue = getNativeAnnotationValue(nativeAnnotation);
      if (nativeAnnotationValue != null) {
        qualifiedName = nativeAnnotationValue;
      }
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

  public static class DefaultImport {
    public final CompilationUnit compilationUnit;
    public final String localName;
    public final String source;

    public DefaultImport(CompilationUnit compilationUnit, String localName, String source) {
      this.compilationUnit = compilationUnit;
      this.localName = localName;
      this.source = source;
    }
  }
}
