package net.jangaroo.jooc.mxml.ast;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.Extends;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeDeclaration;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlToModelParser;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * AST node for an MXML compilation unit, represented by its root node.
 */
public class MxmlCompilationUnit extends CompilationUnit {

  private static final String IMPLEMENTS = "implements";
  private static final String CLASS = "class";

  private static final JooSymbol[] SYM_MODIFIERS = new JooSymbol[]{};

  private static final JooSymbol SYM_IMPORT = new JooSymbol(sym.IMPORT, null, -1, -1, "\n", "import");
  private static final JooSymbol SYM_LBRACE = new JooSymbol(sym.LBRACE, "{");
  private static final JooSymbol SYM_RBRACE = new JooSymbol(sym.RBRACE, "}");
  private static final JooSymbol SYM_SEMICOLON = new JooSymbol(sym.SEMICOLON, ";");

  private final InputSource source;
  private final XmlElement rootNode;
  private final MxmlParserHelper mxmlParserHelper;

  private final List<Directive> classBodyDirectives = new LinkedList<Directive>();

  public MxmlCompilationUnit(@Nonnull InputSource source, @Nonnull XmlElement rootNode, @Nonnull MxmlParserHelper mxmlParserHelper) {
    // no secondary declarations: https://issues.apache.org/jira/browse/FLEX-21373
    super(null, SYM_LBRACE, new LinkedList<AstNode>(), null, SYM_RBRACE, Collections.<IdeDeclaration>emptyList());
    this.source = source;
    this.rootNode = rootNode;
    this.mxmlParserHelper = mxmlParserHelper;
  }

  @Override
  public void scope(Scope scope) {
    JangarooParser parser = scope.getCompiler();

    List<AstNode> importsAndAnnotations = getDirectives();

    List<XmlAttribute> rootNodeAttributes = rootNode.getAttributes();
    Iterator<XmlAttribute> iterator = rootNodeAttributes.iterator();
    while (iterator.hasNext()) {
      XmlAttribute rootNodeAttribute = iterator.next();
      if(rootNodeAttribute.isNamespaceDefinition()) {
        iterator.remove();
        JooSymbol value = rootNodeAttribute.getValue();
        ImportDirective importDirective = mxmlParserHelper.parseImport(value);
        if(null != importDirective) {
          importsAndAnnotations.add(importDirective);
        }
      }

    }
    packageDeclaration = mxmlParserHelper.parsePackageDeclaration();
    initializeClassDeclaration(parser);


    // TODO traverse XML nodes and insert
    // 1. imports
    List<Directive> imports = new LinkedList<Directive>();
    // 2. class level annotations
    List<Directive> annotations = new LinkedList<Directive>();
    // 3. script code (without imports)
    List<Directive> noImports = new LinkedList<Directive>();
    // 4. declarations
    // 5. ordinary members from nested elements

    for (AstNode child: Iterables.filter(rootNode.getChildren(), Predicates.instanceOf(XmlElement.class))) {
      XmlElement element = (XmlElement) child;
      if(element.isBuiltInTag()) {
        String name = element.getName();
        if(MxmlToModelParser.MXML_DECLARATIONS.equals(name)) {


        } else {
          List<JooSymbol> textNodes = element.getTextNodes();
          JooSymbol first = Iterables.getFirst(textNodes, null);
          if (null != first) {
            ClassBody embedded = mxmlParserHelper.parseClassBody(first);
            if (MxmlToModelParser.MXML_METADATA.equals(name)) {
              annotations.addAll(embedded.getDirectives());
            } else if (MxmlToModelParser.MXML_SCRIPT.equals(name)) {
              List<Directive> embeddedDirectives = embedded.getDirectives();
              for (Directive embeddedDirective : embeddedDirectives) {
                if (embeddedDirective instanceof ImportDirective) {
                  imports.add(embeddedDirective);
                } else {
                  noImports.add(embeddedDirective);
                }
              }
            }
          }
        }
      }
    }

    importsAndAnnotations.addAll(imports);
    importsAndAnnotations.addAll(annotations);
    classBodyDirectives.addAll(noImports);

    super.scope(scope);
  }

  void initializeClassDeclaration(JangarooParser parser) {
    List<AstNode> importsAndAnnotations = getDirectives();

    // get class name
    String classQName = CompilerUtils.qNameFromRelativPath(source.getRelativePath());

    // get super class name
    JooSymbol rootNodeSymbol = rootNode.getSymbol();

    // extends
    Extends ext = mxmlParserHelper.parseExtends(parser, rootNode, classQName);
    importsAndAnnotations.add(new ImportDirective(SYM_IMPORT, ext.getSuperClass(), SYM_SEMICOLON));

    // implements
    XmlAttribute implementsAttribute = rootNode.getAttribute(IMPLEMENTS);
    Implements impl = null;
    if (null != implementsAttribute) {
      impl = mxmlParserHelper.parseImplements(implementsAttribute.getValue());
      CommaSeparatedList<Ide> superTypes = impl.getSuperTypes();
      for (AstNode superType : superTypes.getChildren()) {
        if(superType instanceof Ide) {
          importsAndAnnotations.add(new ImportDirective(SYM_IMPORT, (Ide)superType, SYM_SEMICOLON));
        }
      }
    }

    // assemble class declaration
    ClassBody classBody = new ClassBody(SYM_LBRACE, classBodyDirectives, SYM_RBRACE);
    // TODO care for class comment
    JooSymbol symClass = new JooSymbol(sym.CLASS, source.getPath(), rootNodeSymbol.getLine(), rootNodeSymbol.getColumn(), rootNodeSymbol.getWhitespace(), CLASS);

    primaryDeclaration = new ClassDeclaration(SYM_MODIFIERS, symClass, new Ide(CompilerUtils.className(classQName)), ext, impl, classBody);
  }

  @Override
  public void analyze(AstNode parentNode) {
    // perform the ordinary compilation unit analysis
    super.analyze(parentNode);
  }

}
