package net.jangaroo.mxmlc.ast;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.AstVisitor;
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
import net.jangaroo.jooc.ast.PackageDeclaration;
import net.jangaroo.jooc.ast.QualifiedIde;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * AST node for an MXML compilation unit, represented by its root node.
 */
public class MxmlCompilationUnit {

  private static final String IMPLEMENTS = "implements";

  private static final JooSymbol[] SYM_MODIFIERS = new JooSymbol[]{};

  private static final JooSymbol SYM_CLASS = new JooSymbol(sym.CLASS, "class");
  private static final JooSymbol SYM_COMMA = new JooSymbol(sym.COMMA, ",");
  private static final JooSymbol SYM_DOT = new JooSymbol(sym.DOT, ".");
  private static final JooSymbol SYM_EXTENDS = new JooSymbol(sym.EXTENDS, "extends");
  private static final JooSymbol SYM_IMPLEMENTS = new JooSymbol(sym.IMPLEMENTS, IMPLEMENTS);
  private static final JooSymbol SYM_IMPORT = new JooSymbol(sym.IMPORT, null, -1, -1, "\n", "import");
  private static final JooSymbol SYM_LBRACE = new JooSymbol(sym.LBRACE, "{");
  private static final JooSymbol SYM_PACKAGE = new JooSymbol(sym.PACKAGE, "package");
  private static final JooSymbol SYM_RBRACE = new JooSymbol(sym.RBRACE, "}");
  private static final JooSymbol SYM_SEMICOLON = new JooSymbol(sym.SEMICOLON, ";");
  private static final Pattern PACKAGE_PATTERN = Pattern.compile("[a-zA-Z_][\\.\\w]*[\\*]");

  private final InputSource source;
  private final XmlHeader optXmlHeader;
  private final XmlElement rootNode;

  public MxmlCompilationUnit(@Nonnull InputSource source, XmlHeader optXmlHeader, XmlElement rootNode) {
    this.source = source;
    this.optXmlHeader = optXmlHeader;
    this.rootNode = rootNode;
  }

  public JooSymbol getSymbol() {
    return rootNode.getSymbol();
  }

  public List<? extends AstNode> getChildren() {
    return Collections.emptyList();
  }

  public void scope(Scope scope) {

  }

  public void analyze(AstNode parentNode) {

  }

  public AstNode getParentNode() {
    return null;
  }

  public void visit(AstVisitor visitor) throws IOException {
  }

  public CompilationUnit convert() {
    String qName = CompilerUtils.qNameFromRelativPath(source.getRelativePath());

    // TODO extract imports
    LinkedList<AstNode> imports = new LinkedList<AstNode>();
    List<XmlAttribute> rootNodeAttributes = rootNode.getAttributes();
    for (XmlAttribute rootNodeAttribute : rootNodeAttributes) {
      String jooValue = ((String) rootNodeAttribute.getValue().getJooValue()).trim();
      if(PACKAGE_PATTERN.matcher(jooValue).matches()) {
        imports.add(new ImportDirective(SYM_IMPORT, createQualifiedIde(jooValue), SYM_SEMICOLON));
      }
    }

    Extends ext = new Extends(SYM_EXTENDS, new Ide(rootNode.getName()));
    Implements impl = null;
    XmlAttribute implementsAttribute = rootNode.getAttribute(IMPLEMENTS);
    if(null != implementsAttribute) {
      String interfaces = (String) implementsAttribute.getValue().getJooValue();
      List<Ide> packageIdes = Lists.transform(asList(interfaces.split(",")), new Function<String, Ide>() {

        @Nullable
        @Override
        public Ide apply(@Nullable String input) {
          return null != input ? createQualifiedIde(input.trim()) : null;
        }
      });
      for (Ide packageIde : Iterables.filter(packageIdes, Predicates.notNull())) {
        imports.add(new ImportDirective(SYM_IMPORT, packageIde, SYM_SEMICOLON));
      }

      CommaSeparatedList<Ide> superTypes = null;
      for (Ide packageIde : Lists.reverse(packageIdes)) {
        if(null == superTypes) {
          superTypes = new CommaSeparatedList<Ide>(packageIde);
        } else {
          superTypes = new CommaSeparatedList<Ide>(packageIde, SYM_COMMA, superTypes);
        }
      }

      impl = new Implements(SYM_IMPLEMENTS, superTypes);
    }
      // TODO class body directives?
    List<Directive> directives = Collections.emptyList();
    ClassBody classBody = new ClassBody(SYM_LBRACE, directives, SYM_RBRACE);
    ClassDeclaration classDeclaration = new ClassDeclaration(SYM_MODIFIERS, SYM_CLASS, new Ide(CompilerUtils.className(qName)), ext, impl, classBody);

    // no secondary declarations: https://issues.apache.org/jira/browse/FLEX-21373
    List<IdeDeclaration> secondaryDeclarations = Collections.emptyList();
    return new CompilationUnit(createPackageDeclaration(qName), SYM_LBRACE, imports, classDeclaration, SYM_RBRACE, secondaryDeclarations);
  }

  static PackageDeclaration createPackageDeclaration(String qName) {
    String packageName = CompilerUtils.packageName(qName);
    Ide packageIde = createQualifiedIde(packageName);
    return new PackageDeclaration(SYM_PACKAGE, packageIde);
  }

  private static Ide createQualifiedIde(String fullyQualifiedName) {
    String[] parts = fullyQualifiedName.split("\\.");
    Ide ide = null;
    for (String part : parts) {
      if (null == ide) {
        ide = new Ide(part);
      } else {
        ide = new QualifiedIde(ide, SYM_DOT, new JooSymbol(part));
      }
    }
    return ide;
  }
}
