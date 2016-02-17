package net.jangaroo.jooc.backend;

import net.jangaroo.jooc.CompilationUnitRegistry;
import net.jangaroo.jooc.CompilerError;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

class EmbeddedAssetResolver extends AstVisitorBase {
  private static final Collection<String> IMAGE_EXTENSIONS = Arrays.asList("png", "gif", "bmp", "jpg", "jpeg");
  private final CompilationUnit unit;
  private final CompilationUnitRegistry compilationUnitRegistry;

  EmbeddedAssetResolver(@Nonnull CompilationUnit unit, CompilationUnitRegistry compilationUnitRegistry) {
    this.unit = unit;
    this.compilationUnitRegistry = compilationUnitRegistry;
  }

  static String guessAssetType(String path) {
    String extension = CompilerUtils.extension(path);
    String assetType = "text"; // default asset type: text
    if (IMAGE_EXTENSIONS.contains(extension)) {
      assetType = "image";
    }
    return assetType;
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    AstNode value = annotationParameter.getValue();
    if (value != null) {
      Annotation parentAnnotation = annotationParameter.getParentAnnotation();
      String metaName = parentAnnotation.getMetaName();
      if (Jooc.EMBED_ANNOTATION_NAME.equals(metaName) && annotationParameter.getOptName() != null &&
              Jooc.EMBED_ANNOTATION_SOURCE_PROPERTY.equals(annotationParameter.getOptName().getName())) {
        JooSymbol valueSymbol = value.getSymbol();
        if (valueSymbol.sym != sym.STRING_LITERAL) {
          throw new CompilerError(valueSymbol, "The source parameter of an [Embed] annotation must be a string literal");
        }

        String text = valueSymbol.getText();
        String quote = text.substring(0, 1);
        String source = (String) valueSymbol.getJooValue();
        String path = source.startsWith("/") || source.startsWith("\\")
                ? source
                : new File(unit.getInputSource().getParent().getRelativePath(), source).getPath().replace('\\', '/');
        if (path.startsWith("/")) {
          path = path.substring(1);
        }
        String assetType = guessAssetType(path);
        unit.getResourceDependencies().add(assetType + "!" + path);
        if ("image".equals(assetType)) {
          unit.addDependency(compilationUnitRegistry.getCompilationUnit("flash.display.Bitmap"), false);
        }
        String absoluteSource = path;
        annotationParameter.setValue(new LiteralExpr(new JooSymbol(valueSymbol.sym, valueSymbol.getFileName(),
                valueSymbol.getLine(), valueSymbol.getColumn(), valueSymbol.getWhitespace(),
                quote + absoluteSource + quote,
                absoluteSource)));
      }
    }
  }
}
