package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.Annotation;
import net.jangaroo.jooc.ast.ApplyExpr;
import net.jangaroo.jooc.ast.ArrayLiteral;
import net.jangaroo.jooc.ast.AssignmentOpExpr;
import net.jangaroo.jooc.ast.AstNode;
import net.jangaroo.jooc.ast.ClassDeclaration;
import net.jangaroo.jooc.ast.CommaSeparatedList;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.IdeExpr;
import net.jangaroo.jooc.ast.LiteralExpr;
import net.jangaroo.jooc.ast.NewExpr;
import net.jangaroo.jooc.ast.ObjectField;
import net.jangaroo.jooc.ast.ObjectLiteral;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.jooc.sym;
import net.jangaroo.utils.AS3Type;
import net.jangaroo.utils.CompilerUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


final class MxmlNodeToAstTransformer {

  private static final String CONFIG_MODE_AT_SUFFIX = "$at";
  private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");

  private final MxmlParserHelper mxmlParserHelper;
  private MxmlCompilationUnit mxmlCompilationUnit;

  MxmlNodeToAstTransformer(MxmlCompilationUnit mxmlCompilationUnit, MxmlParserHelper mxmlParserHelper) {
    this.mxmlCompilationUnit = mxmlCompilationUnit;
    this.mxmlParserHelper = mxmlParserHelper;
  }

  private static String getEventName(@Nonnull Annotation event) {
    Object eventNameModel = event.getPropertiesByName().get("name");
    return (String) (eventNameModel != null ? eventNameModel : event.getPropertiesByName().get(null));
  }

  private static String getEventTypeStr(@Nonnull Annotation event) {
    Object eventType = event.getPropertiesByName().get("type");
    String eventTypeStr;
    if (eventType instanceof String) {
      eventTypeStr = (String) eventType;
    } else {
      eventTypeStr = "Object";
    }
    return eventTypeStr;
  }

  private Expr modelToAst(@Nonnull XmlElement mxmlModel) {
    Expr expr = modelToAstNoId(mxmlModel);
    String id = mxmlModel.getId();
    if (id != null) {
      JooSymbol jooSymbol = moveWhitespace(new JooSymbol(sym.IDE, id), expr.getSymbol());
      expr = new AssignmentOpExpr(new IdeExpr(new Ide(jooSymbol)), MxmlAstUtils.SYM_EQ, expr);
    }
    return expr;
  }

  private Expr modelToAstNoId(@Nonnull XmlElement mxmlModel) {
    Expr expr;
    ClassDeclaration type = mxmlModel.getType();
    JooSymbol textContent = mxmlModel.getTextContent();
    if (textContent != null) {
      if (!mxmlModel.getMembers().isEmpty() ||
              !mxmlModel.getElements().isEmpty()) {
        throw Jooc.error(textContent, String.format("Unexpected text inside MXML element: '%s'.", textContent.getText()));
      }
      return propertyValueToExpr(mxmlModel, textContent);
    }
    if (mxmlModel.isArray()) {
      expr = arrayModelToArrayLiteral(mxmlModel.getElements(), mxmlModel.getClosingSymbol());
    } else {
      String defaultValue = null;
      if (false) {
        String typeName = type == null ? "*" : type.getQualifiedNameStr();
        mxmlCompilationUnit.addImport(typeName);
        defaultValue = getDefaultValue(typeName);
      }
      if (defaultValue != null) {
        expr = mxmlParserHelper.parseExpression(new JooSymbol(defaultValue));
      } else {
        expr = objectModelToObject(mxmlModel);
      }
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

  private JooSymbol getValue(JooSymbol valueSymbol, String type) {
    String code = MxmlUtils.valueToString(MxmlUtils.getAttributeValue((String) valueSymbol.getJooValue(), type));
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

  private ArrayLiteral arrayModelToArrayLiteral(List<XmlElement> elements, JooSymbol closingSymbol) {
    CommaSeparatedList<Expr> elementList = MxmlAstUtils.createCommaSeparatedList(elements.stream()
            .map(this::modelToAst).collect(toList()));
    JooSymbol lBracket = MxmlAstUtils.SYM_LBRACK;
    JooSymbol rBracket = replace(closingSymbol, MxmlAstUtils.SYM_RBRACK);
    return new ArrayLiteral(lBracket, elementList, rBracket);
  }

  Expr objectModelToObject(XmlElement objectModel) {
    List<ObjectField> objectFields = createObjectFields(objectModel);
    ObjectLiteral objectLiteral = createObjectLiteral(objectModel, objectFields);
    switch (objectModel.getInstantiationMode()) {
      case PLAIN:
        // { ... }
        return objectLiteral;
      case EXT_CONFIG:
        // wrap in type cast: <type>({ ... })
        return createApplyTypeToObjectLiteralExpr(objectModel, objectLiteral);
      case MXML:
        // Exml.apply(new <type>, { ... })
        return createApplyObjectLiteralOnNewExpr(objectModel, objectLiteral);
      case EXT_CREATE:
        // new <type>({ ... })
        return createNewOfObjectListeralExpr(objectModel, objectLiteral);
    }
    throw new IllegalStateException("Cannot happen: No case for " + objectModel.getInstantiationMode());
  }

  private Expr createApplyObjectLiteralOnNewExpr(XmlElement objectModel, ObjectLiteral objectLiteral) {
    String whitespace = objectLiteral.getSymbol().getWhitespace();
    objectLiteral.getSymbol().setWhitespace("");
    return mxmlCompilationUnit.createExmlApplyExpr(whitespace, new NewExpr(MxmlAstUtils.SYM_NEW, new IdeExpr(mxmlParserHelper.parseIde(objectModel.getType().getQualifiedNameStr()))), objectLiteral);
  }

  private List<ObjectField> createObjectFields(XmlElement objectModel) {
    List<ObjectField> eventHandlerFields = objectModel.getEventHandlers()
            .map(this::eventHandlerModelToObjectField).collect(toList());
    Stream<ObjectField> propertyFieldStream = objectModel.getMembers().stream()
            .map(memberModel -> propertyModelToObjectField(memberModel, eventHandlerFields))
            .flatMap(Collection::stream);
    List<XmlElement> defaultPropertyValues = objectModel.getDefaultPropertyValues();
    if (!defaultPropertyValues.isEmpty()) {
      Expr defaultValueExpr = objectModel.isArrayDefaultPropertyValue()
              ? arrayModelToArrayLiteral(defaultPropertyValues, null)
              : objectModelToObject(objectModel.getSingleDefaultPropertyValue());
      ObjectField defaultPropertyField = new ObjectField(new Ide(objectModel.getDefaultPropertyName()),
              MxmlAstUtils.SYM_COLON, defaultValueExpr);
      propertyFieldStream = Stream.concat(propertyFieldStream, Stream.of(defaultPropertyField));
    }
    return propertyFieldStream.collect(toList());
  }

  private Expr createNewOfObjectListeralExpr(XmlElement objectModel, ObjectLiteral objectLiteral) {
    JooSymbol symNew = moveWhitespace(MxmlAstUtils.SYM_NEW, objectLiteral.getSymbol());
    return new NewExpr(symNew, createApplyTypeToObjectLiteralExpr(objectModel, objectLiteral));
  }

  private ApplyExpr createApplyTypeToObjectLiteralExpr(XmlElement objectModel, ObjectLiteral objectLiteral) {
    Ide typeIde = mxmlParserHelper.parseIde(objectLiteral.getSymbol().getWhitespace() + objectModel.getType().getQualifiedNameStr());
    objectLiteral.getSymbol().setWhitespace("");
    return MxmlAstUtils.createApplyExpr(new IdeExpr(typeIde), objectLiteral);
  }

  private ObjectLiteral createObjectLiteral(@Nullable XmlElement sourceElement, List<ObjectField> propertyFields) {
    JooSymbol lBrace = MxmlAstUtils.SYM_LBRACE;
    JooSymbol rBrace = MxmlAstUtils.SYM_RBRACE;
    if (sourceElement != null && sourceElement.parent != null) {
      lBrace = replace(sourceElement.getSymbol(), lBrace);
      rBrace = replace(sourceElement.getClosingSymbol(), rBrace);
    }
    return new ObjectLiteral(lBrace, MxmlAstUtils.createCommaSeparatedList(propertyFields), null, rBrace);
  }

  private static String getExtEventName(String eventName) {
    if (eventName.startsWith("on")) {
      return eventName.substring(2).toLowerCase();
    }
    return eventName.toLowerCase();
  }

  private ObjectField eventHandlerModelToObjectField(XmlNode sourceNode) {
    String eventName = getEventName(sourceNode.getEvent());
    Ide eventNameIde = createIde(sourceNode.getSymbol(), getExtEventName(eventName));
    JooSymbol symColon = replace(sourceNode instanceof XmlAttribute ? ((XmlAttribute)sourceNode).getEq() : null, MxmlAstUtils.SYM_COLON);
    String eventTypeStr = getEventTypeStr(sourceNode.getEvent());
    JooSymbol handlerCode = (JooSymbol) sourceNode.getPropertyValue();
    mxmlCompilationUnit.addImport(eventTypeStr);
    String eventHandlerExpr = String.format("{scope:this,fn:function():* {var event:%s=new %s(\"%s\",arguments);%s%s}}",
            eventTypeStr, eventTypeStr, eventName,
            handlerCode.getWhitespace(), handlerCode.getJooValue());
    return new ObjectField(eventNameIde, symColon, mxmlParserHelper.parseExpression(new JooSymbol(eventHandlerExpr)));
    //TODO: construct JooSymbol from handler code symbol so that error line numbers are correct!
  }

  private static Ide createIde(JooSymbol symbol, String ideName) {
    return new Ide(replace(symbol, sym.IDE, ideName));
  }

  private List<ObjectField> propertyModelToObjectField(XmlNode memberModel, List<ObjectField> eventHandlerFields) {
    List<ObjectField> result = new ArrayList<>();
    if (memberModel.isEvent()) {
      if (eventHandlerFields.isEmpty()) {
        return Collections.emptyList();
      }
      // When encountering the first event handler, insert all (this is the best we can do to keep source code order):
      ObjectLiteral listenersValue = createObjectLiteral(null, Collections.unmodifiableList(eventHandlerFields));
      eventHandlerFields.clear();
      result.add(new ObjectField(new Ide("listeners"), MxmlAstUtils.SYM_COLON, listenersValue));
    } else {
      assert memberModel.isProperty();
      String configOptionName = memberModel.getConfigOptionName();
      if (MxmlUtils.EXML_MIXINS_PROPERTY_NAME.equals(configOptionName)) {
        createMixinsObjectFields(memberModel, result);
      } else {
        Expr configOptionValue = propertyValueToExpr(memberModel, memberModel.getPropertyValue());
        JooSymbol sourceSymbol = memberModel.getSymbol();
        AstNode configOptionNameIde = IDENTIFIER_PATTERN.matcher(configOptionName).matches()
                ? createIde(sourceSymbol, configOptionName)
                : new LiteralExpr(replace(sourceSymbol, sym.STRING_LITERAL, CompilerUtils.quote(configOptionName)));
        JooSymbol symColon = replace(memberModel instanceof XmlAttribute ? ((XmlAttribute) memberModel).getEq() : null,
                MxmlAstUtils.SYM_COLON);
        createArrayAtObjectField(memberModel, result);
        createExtractedXTypePropertyModel(memberModel, result);
        result.add(new ObjectField(configOptionNameIde, symColon, configOptionValue));
      }
    }
    return result;
  }

  private void createArrayAtObjectField(XmlNode element, List<ObjectField> result) {
    String configMode = element.getConfigMode();
    if (!configMode.isEmpty()) {
      String atValue = MxmlCompilationUnit.NET_JANGAROO_EXT_EXML + "." + configMode.toUpperCase();
      JooSymbol atPropertyName = new JooSymbol(element.getConfigOptionName() + CONFIG_MODE_AT_SUFFIX)
              .withWhitespace(element.getSymbol().getWhitespace());
      Ide label = new Ide(atPropertyName);
      result.add(new ObjectField(label, MxmlAstUtils.SYM_COLON, mxmlParserHelper.parseExpression(new JooSymbol(atValue))));
    }
  }

  private void createExtractedXTypePropertyModel(XmlNode propertyModel, List<ObjectField> result) {
    String extractXTypePropertyName = propertyModel.getExtractXTypePropertyName();
    if (extractXTypePropertyName != null && !extractXTypePropertyName.isEmpty()) {
      Object propertyValueModel = propertyModel.getPropertyValue();
      ClassDeclaration classDeclaration = propertyModel.getPropertyDeclaration().getClassDeclaration();
      TypedIdeDeclaration extractXTypePropertyModel = classDeclaration.getMemberDeclaration(extractXTypePropertyName);
      if (extractXTypePropertyModel == null) {
        throw Jooc.error(String.format("Annotation [ExtConfig(extractXType=\"%s\")] in class %s refers to a non-existing property.", extractXTypePropertyName, classDeclaration.getQualifiedNameStr()));
      } else {
        if (propertyValueModel instanceof XmlElement) {
          String extractedXTypeCode = ((XmlElement) propertyValueModel).getType().getQualifiedNameStr() + "['prototype'].xtype";
          Expr extractedXTypeValue = mxmlParserHelper.parseExpression(new JooSymbol(extractedXTypeCode));
          result.add(new ObjectField(new Ide(new JooSymbol(extractXTypePropertyName).withWhitespace(propertyModel.getSymbol().getWhitespace())),
                  MxmlAstUtils.SYM_COLON, extractedXTypeValue));
        }
      }
    }
  }

  private void createMixinsObjectFields(XmlNode memberModel, List<ObjectField> result) {
    Object propertyValueModel = memberModel.getPropertyValue();
    if (propertyValueModel instanceof List) {
      for (XmlElement mixin : (List<XmlElement>) propertyValueModel) {
        if (mixin.getType() != null) {
          result.addAll(createObjectFields(mixin));
        } else {
          throw Jooc.error(mixin.getSymbol(),
                  "MXML mixins property must only contain sub-elements.");
        }
      }
    } else {
      throw Jooc.error(memberModel.getSymbol(),
              "MXML mixins property must contain a list of sub-elements.");
    }
  }

  private Expr propertyValueToExpr(XmlNode mxmlModel, Object propertyValue) {
    if (propertyValue == null) {
      return new LiteralExpr(mxmlModel.getSymbol().replacingSymAndTextAndJooValue(sym.NULL_LITERAL, "null", null).withWhitespace(" "));
    }
    if (propertyValue instanceof JooSymbol) {
      String type = mxmlModel.isProperty()
              ? mxmlModel.getPropertyTypeName()
              : ((XmlElement) mxmlModel).getType().getQualifiedNameStr();
      return mxmlParserHelper.parseExpression(getValue((JooSymbol) propertyValue, type));
    }
    if (propertyValue instanceof XmlElement) {
      return objectModelToObject((XmlElement) propertyValue);
    }
    //noinspection unchecked
    return arrayModelToArrayLiteral((List<XmlElement>) propertyValue, ((XmlElement) mxmlModel).getClosingSymbol());
  }

  private ObjectField declarationToObjectField(XmlElement mxmlModel) {
    if (mxmlModel.getId() == null ||
            mxmlModel.getTextContent() == null &&
                    (!mxmlModel.isArray() && mxmlModel.getMembers().isEmpty() ||
                            mxmlModel.isArray() && mxmlModel.getElements().isEmpty())) {
      return null;
    }
    Ide fieldName = new Ide(new JooSymbol(mxmlModel.getId()).withWhitespace(MxmlUtils.toASDoc(mxmlModel.getSymbol().getWhitespace())));
    return new ObjectField(fieldName, MxmlAstUtils.SYM_COLON, modelToAstNoId(mxmlModel));
  }

  ObjectLiteral getDefaults(RootElementProcessor rootElementProcessor) {
    return createObjectLiteral(rootElementProcessor.getDeclarationsElement(),
            rootElementProcessor.getDeclarations().stream()
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
