package net.jangaroo.jooc.mxml;

import com.google.common.collect.Iterables;
import java_cup.runtime.Symbol;
import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassBody;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Extends;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.Implements;
import net.jangaroo.jooc.ast.ImportDirective;
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.SemicolonTerminatedStatement;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.mxml.ast.MxmlCompilationUnit;
import net.jangaroo.jooc.mxml.ast.XmlAttribute;
import net.jangaroo.jooc.mxml.ast.XmlElement;
import net.jangaroo.jooc.mxml.ast.XmlTag;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class MxmlParserHelper {

  private static final String TPL_CLASS_BODY = "package{class ___${\n%s\n}}";
  private static final String TPL_METHOD_BODY = "package{class ___${function __method__$(){\n%s\n}}}";
  private static final String TPL_EXPRESSION = "package{class ___${x= %s}}";
  private static final String TPL_IMPLEMENTS = "package{class ___$ implements %s\n{}}";
  private static final String TPL_IMPORT = "package{\nimport %s;\nclass ___$ {}}";
  private static final String TPL_METADATA = "package{\n%s\nclass ___$ {}}";
  private static final String TPL_EXTENDS = "package{class ___$ extends %s {}}";
  private static final String TPL_PACKAGE = "package %s {class ___$ {}}";

  private final JooParser parser;
  private InputSource inputSource;

  public MxmlParserHelper(JooParser parser) {
    this.parser = parser;
  }

  public XmlTag createXmlTag(JooSymbol lt, Ide tagName, List<XmlAttribute> attributes, JooSymbol gt) {
    return new XmlTag(lt, tagName, attributes, gt);
  }

  public XmlAttribute createXmlAttribute(Ide ide, JooSymbol eq, JooSymbol value){
    return new XmlAttribute(ide, eq, value);
  }

  public XmlElement createXmlElement(XmlTag openingMxmlTag, List children, XmlTag closingMxmlTag) {
    return new XmlElement(openingMxmlTag, children, closingMxmlTag);
  }

  public CompilationUnit createCompilationUnit(XmlElement root) throws Exception {
    return new MxmlCompilationUnit(getInputSource(), root, this);
  }

  InputSource getInputSource() {
    if(null == inputSource) {
      inputSource = parser.getScannerBase().getInputSource();
    }
    return inputSource;
  }

  @Nonnull
  public PackageDeclaration parsePackageDeclaration(String classQName) {
    String text = CompilerUtils.packageName(classQName);
    CompilationUnit unit = (CompilationUnit) parser.parseEmbedded(String.format(TPL_PACKAGE, text), 0, 0).value;
    return unit.getPackageDeclaration();
  }

  @Nonnull
  public ClassBody parseClassBody(@Nonnull JooSymbol symbol) {
    String text = symbol.getText();
    String template = TPL_CLASS_BODY;
    int[] position = position(symbol, template);
    CompilationUnit unit = (CompilationUnit) parser.parseEmbedded(String.format(template, text), position[0], position[1]).value;
    return ((ClassDeclaration)unit.getPrimaryDeclaration()).getBody();
  }

  @Nonnull
  public List<Directive> parseMethodBody(@Nonnull String text) {
    Symbol parsed = parser.parseEmbedded(String.format(TPL_METHOD_BODY, text), 0, 0);
    CompilationUnit unit = (CompilationUnit) parsed.value;
    List<Directive> directives = ((ClassDeclaration) unit.getPrimaryDeclaration()).getBody().getDirectives();
    if(null != directives) {
      Directive first = Iterables.getFirst(directives, null);
      if(first instanceof FunctionDeclaration) {
        return ((FunctionDeclaration)first).getBody().getDirectives();
      }
    }
    return Collections.emptyList();
  }

  @Nullable
  public Implements parseImplements(@Nonnull JooSymbol symbol) {
    String text = symbol.getText();
    String template = TPL_IMPLEMENTS;
    int[] position = position(symbol, template);
    CompilationUnit unit = (CompilationUnit) parser.parseEmbedded(String.format(template, text), position[0], position[1]).value;
    return ((ClassDeclaration)unit.getPrimaryDeclaration()).getOptImplements();
  }

  @Nonnull
  public Extends parseExtends(@Nonnull JangarooParser parser, @Nonnull XmlElement rootNode, @Nonnull String classQName) {
    JooSymbol rootNodeSymbol = rootNode.getSymbol();
    String superClassName = getClassNameForElement(parser, rootNode);
    if (superClassName.equals(classQName)) {
      throw JangarooParser.error(rootNodeSymbol, "Cyclic inheritance error: Super class and this component are the same.");
    }

    String template = TPL_EXTENDS;
    int[] position = position(rootNodeSymbol, template);
    CompilationUnit unit = (CompilationUnit) this.parser.parseEmbedded(String.format(template, superClassName), position[0], position[1]).value;
    return ((ClassDeclaration)unit.getPrimaryDeclaration()).getOptExtends();
  }

  @Nullable
  public ImportDirective parseImport(@Nonnull JooSymbol symbol) {
    String text = symbol.getText();
    String template = TPL_IMPORT;
    int[] position = position(symbol, template);
    try {
      Symbol parsed = parser.silent().parseEmbedded(String.format(template, text), position[0], position[1]);
      CompilationUnit unit = (CompilationUnit) parsed.value;
      return (ImportDirective) Iterables.getFirst(unit.getDirectives(), null);
    } catch (Exception ignored) {
    }
    return null;
  }

  @Nullable
  public List<Annotation> parseMetadata(@Nonnull JooSymbol symbol) {
    String text = symbol.getText();
    String template = TPL_METADATA;
    int[] position = position(symbol, template);
    try {
      Symbol parsed = parser.silent().parseEmbedded(String.format(template, text), position[0], position[1]);
      CompilationUnit unit = (CompilationUnit) parsed.value;
      return unit.getAnnotations();
    } catch (Exception ignored) {
    }
    return null;
  }

  @Nullable
  public ImportDirective parseImport(@Nonnull String text) {
    String template = TPL_IMPORT;
    Symbol parsed = parser.parseEmbedded(String.format(template, text), 0, 0);
    CompilationUnit unit = (CompilationUnit) parsed.value;
    return (ImportDirective) Iterables.getFirst(unit.getDirectives(), null);
  }

  public Ide parseIde(@Nonnull String text) {
    return parseImport(text).getIde();
  }

    @Nonnull
  static int[] position(@Nonnull JooSymbol symbol, @Nonnull String template) {
    String[] lines = template.split("\\n");
    int lineCount = 0;
    for(String line : lines) {
      lineCount ++;
      int index = line.indexOf("%s");
      if(-1 != index) {
        return new int[] {symbol.getLine() - lineCount, symbol.getColumn() - index};
      }
    }
    throw new IllegalStateException("cannot find %s in template string '" + template + "'");
  }

  @Nonnull
  public String getClassNameForElement(JangarooParser parser, XmlElement xmlElement){
    String name = xmlElement.getLocalName();
    String uri = xmlElement.getNamespaceURI();
    if (uri != null) {
      String packageName = parsePackageFromNamespace(uri);
      if (packageName != null) {
        String qName = CompilerUtils.qName(packageName, name);
        if (qName.equals(CompilerUtils.qNameFromRelativePath(getInputSource().getRelativePath()))) {
          return qName;
        }
        if (parser.isClass(qName)) {
          return qName;
        }
      } else {
        String className = parser.getMxmlComponentRegistry().getClassName(uri, name);
        if(null != className) {
          return className;
        }
      }
    }
    throw JangarooParser.error(xmlElement, "Could not resolve class from MXML node " + xmlElement);
  }

  static String parsePackageFromNamespace(String uri) {
    return uri.endsWith(".*") ? uri.substring(0, uri.length() -2)
            : uri.equals("*") || MxmlUtils.isMxmlNamespace(uri) ? "" : null;
  }

  @Nonnull
  public Expr parseExpression(@Nonnull JooSymbol symbol) {
    String text = symbol.getText();
    String template = TPL_EXPRESSION;
    int[] position = position(symbol, template);
    String input = String.format(template, text);
    CompilationUnit unit = (CompilationUnit) parser.parseEmbedded(input, position[0], position[1]).value;
    List<Directive> classBodyDirectives = ((ClassDeclaration) unit.getPrimaryDeclaration()).getBody().getDirectives();
    if(null != classBodyDirectives) {
      Directive directive = Iterables.getFirst(classBodyDirectives, null);
      if(directive instanceof SemicolonTerminatedStatement) {
        AstNode astNode = ((SemicolonTerminatedStatement) directive).getOptStatement();
        if(astNode instanceof AssignmentOpExpr) {
          return ((AssignmentOpExpr) astNode).getArg2();
        }
      }
    }
    throw new IllegalStateException("Unexpected AST obtained when parsing '" + input + "'");
  }
}
