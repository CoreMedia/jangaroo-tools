package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.Extends;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.mxml.MxmlParserHelper;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Helper class to create the MXML class declaration
 */
class ClassDeclarationBuilder {

  private final JangarooParser parser;
  private final MxmlParserHelper mxmlParserHelper;
  private final MxmlCompilationUnit unit;

  private final List<Directive> classBodyDirectives;

  ClassDeclarationBuilder(@Nonnull JangarooParser parser, @Nonnull MxmlParserHelper mxmlParserHelper, @Nonnull MxmlCompilationUnit unit) {
    this.parser = parser;
    this.mxmlParserHelper = mxmlParserHelper;
    this.unit = unit;
    classBodyDirectives = unit.getClassBodyDirectives();
  }

  ClassDeclaration build() {
    RootElementProcessor rootElementProcessor = unit.getRootElementProcessor();
    String classQName = unit.getClassQName();
    XmlElement rootNode = unit.getRootNode();

    // get super class name
    JooSymbol rootNodeSymbol = rootNode.getSymbol();

    // extends
    Extends ext = mxmlParserHelper.parseExtends(parser, rootNode, classQName);

    // implements
    Implements impl = null;
    JooSymbol implSymbol = rootElementProcessor.getImpl();
    if(null != implSymbol) {
      impl = mxmlParserHelper.parseImplements(implSymbol);
    }

    handleScripts(rootElementProcessor.getScripts());

    // assemble class declaration
    return MxmlAstUtils.createClassDeclaration(classQName, rootNodeSymbol, ext, impl, this.classBodyDirectives, unit.getInputSource());
  }

  private void handleScripts(@Nonnull List<JooSymbol> scripts) {
    for (JooSymbol jooSymbol : scripts) {
      ClassBody classBody = mxmlParserHelper.parseClassBody(jooSymbol);
      List<Directive> parsedClassBodyDirectives = classBody.getDirectives();
      if (null != parsedClassBodyDirectives) {
        classBodyDirectives.addAll(parsedClassBodyDirectives);
      } else {
        parser.getLog().warning(jooSymbol, "element is empty");
      }
    }
  }

}
