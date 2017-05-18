package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static net.jangaroo.jooc.mxml.ast.MxmlModelToActionScriptTransformer.getEventHandlerName;


final class MxmlModelToAstTransformer {

  private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");

  private final MxmlParserHelper mxmlParserHelper;

  MxmlModelToAstTransformer(MxmlParserHelper mxmlParserHelper) {
    this.mxmlParserHelper = mxmlParserHelper;
  }

  private Expr modelToAst(@Nonnull MxmlToModelParser.MxmlModel mxmlModel) {
    Expr expr;
    CompilationUnit type = mxmlModel.getType();
    String typeName = type == null ? "*" : type.getQualifiedNameStr();
    if (mxmlModel instanceof MxmlToModelParser.MxmlArrayModel) {
      expr = arrayModelToArrayLiteral((MxmlToModelParser.MxmlArrayModel) mxmlModel);
    } else if (mxmlModel instanceof MxmlToModelParser.MxmlObjectModel) {
      Expr objectModel = objectModelToObject((MxmlToModelParser.MxmlObjectModel) mxmlModel);
      String defaultValue = getDefaultValue(typeName);
      if (defaultValue != null) {
        expr = mxmlParserHelper.parseExpression(new JooSymbol(defaultValue));
      } else {
        expr = objectModel;
      }
    } else if (mxmlModel instanceof MxmlToModelParser.MxmlValueModel){
      MxmlToModelParser.MxmlValueModel valueModel = (MxmlToModelParser.MxmlValueModel) mxmlModel;
      expr = mxmlParserHelper.parseExpression(getValue(valueModel));
    } else {
      throw new IllegalStateException("Unknown MxmlModel subclass " + mxmlModel.getClass());
    }
    String id = mxmlModel.getId();
    if (id != null) {
      expr = new AssignmentOpExpr(new IdeExpr(new Ide(id)), MxmlAstUtils.SYM_EQ, expr);
    }
    return expr;
  }

  private String getDefaultValue(String typeName) {
    AS3Type as3Type = AS3Type.typeByName(typeName);
    if (as3Type != null) {
      switch (as3Type) {
        case INT:
        case UINT:
        case NUMBER:
          return "0";
        case BOOLEAN:
          return "false";
        case STRING:
          return "\"\"";
      }
    }
    return null;
  }

  private JooSymbol getValue(MxmlToModelParser.MxmlValueModel valueModel) {
    JooSymbol valueSymbol = valueModel.getValue();
    Object value = valueSymbol.getJooValue();
    if (value instanceof String && MxmlUtils.isBindingExpression((String) value)) {
      String code = MxmlUtils.getBindingExpression((String) value);
      valueSymbol = valueSymbol.replacingSymAndTextAndJooValue(valueSymbol.sym, code, code);
    }
    return valueSymbol;
  }

  private static JooSymbol replace(@Nullable JooSymbol original, JooSymbol replacementSymAndText) {
    return original == null ? replacementSymAndText
            : replace(original, replacementSymAndText.sym, replacementSymAndText.getText());
  }
  private static JooSymbol replace(@Nullable JooSymbol original, int replacementSym, String replacementText) {
    return original == null ? new JooSymbol(replacementSym, replacementText)
            : new JooSymbol(replacementSym, original.getFileName(), original.getLine(), original.getColumn(), original.getWhitespace().replaceAll("<!--", "/*").replaceAll("-->", "*/"), replacementText, replacementText);
  }

  private ArrayLiteral arrayModelToArrayLiteral(MxmlToModelParser.MxmlArrayModel objectNode) {
    CommaSeparatedList<Expr> elementList = MxmlAstUtils.createCommaSeparatedList(objectNode.getElements().stream()
            .map(this::modelToAst).collect(toList()));
    XmlElement sourceElement = objectNode.getSourceElement();
    JooSymbol lBracket = replace(sourceElement.getSymbol(), MxmlAstUtils.SYM_LBRACK);
    JooSymbol rBracket = replace(sourceElement.getClosingSymbol(), MxmlAstUtils.SYM_RBRACK);
    return new ArrayLiteral(lBracket, elementList, rBracket);
  }

  Expr objectModelToObject(MxmlToModelParser.MxmlObjectModel objectModel) {
    List<ObjectField> eventHandlerFields = objectModel.getEventHandlers()
            .map(this::eventHandlerModelToObjectField).collect(toList());
    Stream<ObjectField> propertyFieldStream = objectModel.getMembers().stream()
            .map(memberModel -> propertyModelToObjectField(memberModel, eventHandlerFields))
            .filter(Objects::nonNull);
    ObjectLiteral objectLiteral = createObjectLiteral(objectModel.getSourceElement(), propertyFieldStream.collect(toList()));
    switch (objectModel.getInstantiationMode()) {
      case PLAIN:
        return objectLiteral;
      case EXT_CONFIG:
        // wrap in type cast:
        return createApplyTypeToObjectLiteralExpr(objectModel, objectLiteral);
      case MXML:
        return objectLiteral; // TODO
      case EXT_CREATE:
        return new NewExpr(MxmlAstUtils.SYM_NEW, createApplyTypeToObjectLiteralExpr(objectModel, objectLiteral));
    }
    throw new IllegalStateException("Cannot happen: No case for " + objectModel.getInstantiationMode());
  }

  private ApplyExpr createApplyTypeToObjectLiteralExpr(MxmlToModelParser.MxmlObjectModel objectModel, ObjectLiteral objectLiteral) {
    return MxmlAstUtils.createApplyExpr(new IdeExpr(mxmlParserHelper.parseIde(objectModel.getType().getQualifiedNameStr())), objectLiteral);
  }

  private ObjectLiteral createObjectLiteral(@Nullable XmlElement sourceElement, List<ObjectField> propertyFields) {
    JooSymbol lBrace = MxmlAstUtils.SYM_LBRACE;
    JooSymbol rBrace = MxmlAstUtils.SYM_RBRACE;
    if (sourceElement != null) {
      lBrace = replace(sourceElement.getSymbol(), lBrace);
      rBrace = replace(sourceElement.getClosingSymbol(), rBrace);
    }
    return new ObjectLiteral(lBrace, MxmlAstUtils.createCommaSeparatedList(propertyFields), null, rBrace);
  }

  private ObjectField eventHandlerModelToObjectField(MxmlToModelParser.MxmlEventHandlerModel eventHandlerModel) {
    String eventHandlerMethodName = getEventHandlerName(eventHandlerModel);
    XmlNode sourceNode = eventHandlerModel.getSourceNode();
    Ide eventNameIde = createIde(sourceNode.getSymbol(), eventHandlerModel.getEventName());
    JooSymbol symColon = replace(sourceNode instanceof XmlAttribute ? ((XmlAttribute)sourceNode).getEq() : null, MxmlAstUtils.SYM_COLON);
    return new ObjectField(eventNameIde, symColon, new IdeExpr(new Ide(eventHandlerMethodName)));
  }

  private Ide createIde(JooSymbol symbol, String ideName) {
    return new Ide(replace(symbol, sym.IDE, ideName));
  }

  private ObjectField propertyModelToObjectField(MxmlToModelParser.MxmlMemberModel memberModel, List<ObjectField> eventHandlerFields) {
    if (memberModel instanceof MxmlToModelParser.MxmlEventHandlerModel) {
      if (eventHandlerFields.isEmpty()) {
        return null;
      }
      // When encountering the first event handler, insert all (this is the best we can do to keep source code order):
      ObjectLiteral listenersValue = createObjectLiteral(null, Collections.unmodifiableList(eventHandlerFields));
      eventHandlerFields.clear();
      return new ObjectField(new Ide("listeners"), MxmlAstUtils.SYM_COLON, listenersValue);
    } else {
      MxmlToModelParser.MxmlPropertyModel propertyModel = (MxmlToModelParser.MxmlPropertyModel) memberModel;
      String configOptionName = propertyModel.getConfigOptionName();
      MxmlToModelParser.MxmlModel propertyValueModel = propertyModel.getValue();
      Expr configOptionValue = modelToAst(propertyValueModel);
      XmlNode sourceNode = propertyModel.getSourceNode();
      JooSymbol sourceSymbol = sourceNode != null ? sourceNode.getSymbol() : null;
      AstNode configOptionNameIde = IDENTIFIER_PATTERN.matcher(configOptionName).matches()
              ? createIde(sourceSymbol, configOptionName)
              : new LiteralExpr(replace(sourceSymbol, sym.STRING_LITERAL, CompilerUtils.quote(configOptionName)));
      JooSymbol symColon = replace(sourceNode instanceof XmlAttribute ? ((XmlAttribute) sourceNode).getEq() : null,
              MxmlAstUtils.SYM_COLON);
      return new ObjectField(configOptionNameIde, symColon, configOptionValue);
    }
  }

}
