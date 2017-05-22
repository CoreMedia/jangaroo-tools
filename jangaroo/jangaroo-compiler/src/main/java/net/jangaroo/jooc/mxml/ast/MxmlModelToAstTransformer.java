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


final class MxmlModelToAstTransformer {

  private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");

  private final MxmlParserHelper mxmlParserHelper;
  private MxmlCompilationUnit mxmlCompilationUnit;

  MxmlModelToAstTransformer(MxmlCompilationUnit mxmlCompilationUnit, MxmlParserHelper mxmlParserHelper) {
    this.mxmlCompilationUnit = mxmlCompilationUnit;
    this.mxmlParserHelper = mxmlParserHelper;
  }

  private Expr modelToAst(@Nonnull MxmlToModelParser.MxmlModel mxmlModel) {
    Expr expr = modelToAstNoId(mxmlModel);
    String id = mxmlModel.getId();
    if (id != null) {
      JooSymbol jooSymbol = moveWhitespace(new JooSymbol(sym.IDE, id), expr.getSymbol());
      expr = new AssignmentOpExpr(new IdeExpr(new Ide(jooSymbol)), MxmlAstUtils.SYM_EQ, expr);
    }
    return expr;
  }

  private Expr modelToAstNoId(@Nonnull MxmlToModelParser.MxmlModel mxmlModel) {
    Expr expr;
    CompilationUnit type = mxmlModel.getType();
    if (mxmlModel instanceof MxmlToModelParser.MxmlArrayModel) {
      expr = arrayModelToArrayLiteral((MxmlToModelParser.MxmlArrayModel) mxmlModel);
    } else if (mxmlModel instanceof MxmlToModelParser.MxmlObjectModel) {
      String defaultValue = getDefaultValue(type == null ? null : type.getQualifiedNameStr());
      if (defaultValue != null) {
        expr = mxmlParserHelper.parseExpression(new JooSymbol(defaultValue));
      } else {
        expr = objectModelToObject((MxmlToModelParser.MxmlObjectModel) mxmlModel);
      }
    } else if (mxmlModel instanceof MxmlToModelParser.MxmlValueModel){
      MxmlToModelParser.MxmlValueModel valueModel = (MxmlToModelParser.MxmlValueModel) mxmlModel;
      expr = mxmlParserHelper.parseExpression(getValue(valueModel, type));
    } else {
      throw new IllegalStateException("Unknown MxmlModel subclass " + mxmlModel.getClass());
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

  private JooSymbol getValue(MxmlToModelParser.MxmlValueModel valueModel, CompilationUnit type) {
    JooSymbol valueSymbol = valueModel.getValue();
    String code = MxmlUtils.valueToString(MxmlUtils.getAttributeValue((String) valueSymbol.getJooValue(), type == null ? null : type.getQualifiedNameStr()));
    valueSymbol = valueSymbol.replacingSymAndTextAndJooValue(valueSymbol.sym, code, code);
    return valueSymbol;
  }

  private static JooSymbol replace(@Nullable JooSymbol original, JooSymbol replacementSymAndText) {
    return original == null ? replacementSymAndText
            : replace(original, replacementSymAndText.sym, replacementSymAndText.getText());
  }
  private static JooSymbol replace(@Nullable JooSymbol original, int replacementSym, String replacementText) {
    return original == null ? new JooSymbol(replacementSym, replacementText)
            : new JooSymbol(replacementSym, original.getFileName(), original.getLine(), original.getColumn(), MxmlUtils.toASDoc(original.getWhitespace()), replacementText, replacementText);
  }

  private ArrayLiteral arrayModelToArrayLiteral(MxmlToModelParser.MxmlArrayModel objectNode) {
    CommaSeparatedList<Expr> elementList = MxmlAstUtils.createCommaSeparatedList(objectNode.getElements().stream()
            .map(this::modelToAst).collect(toList()));
    JooSymbol lBracket = MxmlAstUtils.SYM_LBRACK;
    JooSymbol rBracket = replace(objectNode.getSourceElement().getClosingSymbol(), MxmlAstUtils.SYM_RBRACK);
    return new ArrayLiteral(lBracket, elementList, rBracket);
  }

  ObjectLiteral rootModelToObjectLiteral(MxmlToModelParser.MxmlRootModel objectModel) {
    return (ObjectLiteral) objectModelToObject(objectModel);
  }

  private Expr objectModelToObject(MxmlToModelParser.MxmlObjectModel objectModel) {
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
        return createNewOfObjectListeralExpr(objectModel, objectLiteral);
    }
    throw new IllegalStateException("Cannot happen: No case for " + objectModel.getInstantiationMode());
  }

  private Expr createNewOfObjectListeralExpr(MxmlToModelParser.MxmlObjectModel objectModel, ObjectLiteral objectLiteral) {
    JooSymbol symNew = moveWhitespace(MxmlAstUtils.SYM_NEW, objectLiteral.getSymbol());
    return new NewExpr(symNew, createApplyTypeToObjectLiteralExpr(objectModel, objectLiteral));
  }

  private ApplyExpr createApplyTypeToObjectLiteralExpr(MxmlToModelParser.MxmlObjectModel objectModel, ObjectLiteral objectLiteral) {
    Ide typeIde = mxmlParserHelper.parseIde(objectLiteral.getSymbol().getWhitespace() + objectModel.getType().getQualifiedNameStr());
    objectLiteral.getSymbol().setWhitespace("");
    return MxmlAstUtils.createApplyExpr(new IdeExpr(typeIde), objectLiteral);
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
    XmlNode sourceNode = eventHandlerModel.getSourceNode();
    Ide eventNameIde = createIde(sourceNode.getSymbol(), eventHandlerModel.getExtEventName());
    JooSymbol symColon = replace(sourceNode instanceof XmlAttribute ? ((XmlAttribute)sourceNode).getEq() : null, MxmlAstUtils.SYM_COLON);
    String eventTypeStr = eventHandlerModel.getEventTypeStr();
    JooSymbol handlerCode = eventHandlerModel.getHandlerCode();
    mxmlCompilationUnit.addImport(eventTypeStr);
    String eventHandlerExpr = String.format("{scope:this,fn:function():* {var event:%s=new %s(\"%s\",arguments);%s%s}}",
            eventTypeStr, eventTypeStr, eventHandlerModel.getFlexEventName(),
            handlerCode.getWhitespace(), handlerCode.getJooValue());
    return new ObjectField(eventNameIde, symColon, mxmlParserHelper.parseExpression(new JooSymbol(eventHandlerExpr)));
    //TODO: construct JooSymbol from handler code symbol so that error line numbers are correct!
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

  private ObjectField declarationToObjectField(MxmlToModelParser.MxmlModel mxmlModel) {
    if (mxmlModel.getId() == null ||
            mxmlModel instanceof MxmlToModelParser.MxmlObjectModel &&
                    ((MxmlToModelParser.MxmlObjectModel) mxmlModel).getMembers().isEmpty() ||
            mxmlModel instanceof MxmlToModelParser.MxmlArrayModel &&
                    ((MxmlToModelParser.MxmlArrayModel) mxmlModel).getElements().isEmpty()) {
      return null;
    }
    Ide fieldName = new Ide(new JooSymbol(mxmlModel.getId()).withWhitespace(MxmlUtils.toASDoc(mxmlModel.getSourceElement().getSymbol().getWhitespace())));
    return new ObjectField(fieldName, MxmlAstUtils.SYM_COLON, modelToAstNoId(mxmlModel));
  }

  ObjectLiteral getDefaults(MxmlToModelParser.MxmlRootModel mxmlRootModel) {
    return createObjectLiteral(mxmlRootModel.getDeclarations().getSourceElement(),
            mxmlRootModel.getDeclarations().getElements().stream()
                    .map(this::declarationToObjectField)
                    .filter(Objects::nonNull)
                    .collect(toList()));
  }

  private static JooSymbol moveWhitespace(JooSymbol targetSymbol, JooSymbol sourceSymbol) {
    String whitespace = sourceSymbol.getWhitespace();
    sourceSymbol.setWhitespace("");
    return targetSymbol.withWhitespace(whitespace);
  }

}
