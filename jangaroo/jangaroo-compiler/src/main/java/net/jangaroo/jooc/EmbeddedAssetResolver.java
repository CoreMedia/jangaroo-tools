package net.jangaroo.jooc;

import net.jangaroo.jooc.ast.AnnotationParameter;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitorBase;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.LiteralExpr;

import javax.annotation.Nonnull;
import java.io.IOException;

class EmbeddedAssetResolver extends AstVisitorBase {
  private final CompilationUnit unit;

  EmbeddedAssetResolver(@Nonnull CompilationUnit unit) {
    this.unit = unit;
  }

  @Override
  public void visitAnnotationParameter(AnnotationParameter annotationParameter) throws IOException {
    super.visitAnnotationParameter(annotationParameter);
    AstNode value = annotationParameter.getValue();
    if (value != null) {
      String metaName = annotationParameter.getParentAnnotation().getMetaName();
      if ("Embed".equals(metaName) && annotationParameter.getOptName() != null && "source".equals(annotationParameter.getOptName().getName())) {
        JooSymbol valueSymbol = value.getSymbol();
        if (valueSymbol.sym != sym.STRING_LITERAL) {
          throw new CompilerError(valueSymbol, "The source parameter of an [Embed] annotation must be a string literal");
        }

        String text = valueSymbol.getText();
        String quote = text.substring(0, 1);
        String source = (String) valueSymbol.getJooValue();
        String absoluteSource = unit.addResourceDependency(source);
        annotationParameter.setValue(new LiteralExpr(new JooSymbol(valueSymbol.sym, valueSymbol.getFileName(),
                valueSymbol.getLine(), valueSymbol.getColumn(), valueSymbol.getWhitespace(),
                quote + absoluteSource + quote,
                absoluteSource)));
      }
    }
  }
}
