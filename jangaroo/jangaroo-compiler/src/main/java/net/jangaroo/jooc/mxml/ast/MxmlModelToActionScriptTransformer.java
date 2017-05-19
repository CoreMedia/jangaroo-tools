package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Jooc;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.Directive;
import net.jangaroo.jooc.ast.Expr;
import net.jangaroo.jooc.ast.Ide;
import net.jangaroo.jooc.ast.TypeRelation;
import net.jangaroo.jooc.ast.TypedIdeDeclaration;
import net.jangaroo.jooc.ast.VariableDeclaration;
import net.jangaroo.jooc.mxml.MxmlParserHelper;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static net.jangaroo.jooc.mxml.ast.MxmlToModelParser.InstantiationMode.PLAIN;
import static net.jangaroo.jooc.mxml.ast.MxmlToModelParser.getTextContent;


final class MxmlModelToActionScriptTransformer {

  private static final String DELETE_OBJECT_PROPERTY_CODE = "\n    delete %s['%s'];";

  private final MxmlParserHelper mxmlParserHelper;
  private final MxmlCompilationUnit compilationUnit;

  private final Collection<Directive> constructorBodyDirectives = new LinkedList<>();
  private final Collection<Directive> classBodyDirectives = new LinkedList<>();
  String additionalDeclarations = "";

  MxmlModelToActionScriptTransformer(MxmlParserHelper mxmlParserHelper, MxmlCompilationUnit mxmlCompilationUnit) {
    this.mxmlParserHelper = mxmlParserHelper;
    this.compilationUnit = mxmlCompilationUnit;
  }

  private void renderConfigAuxVar(@Nonnull Ide ide, Ide type) {
    //constructorBodyDirectives.add(MxmlAstUtils.createVariableDeclaration(ide, type));
  }

  private void createPropertyAssignmentCodeWithBindings(Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig, @Nonnull JooSymbol value, @Nonnull MxmlToModelParser.MxmlPropertyModel propertyModel) {
    Ide variable = generatingConfig ? configVariable : targetVariable;
    // skip property assignment to target object if it was already contained in config object:
    if (generatingConfig || configVariable == null) {
      // default: create a normal property assignment:
      createPropertyAssignmentCode(variable, propertyModel, value, generatingConfig);
    }
  }

  void processAttributesAndChildNodes(MxmlToModelParser.MxmlObjectModel objectModel, Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig) {
    if (!objectModel.getMembers().isEmpty()) {
      processMembers(objectModel, configVariable, targetVariable, generatingConfig);
    }
  }

  private void processMembers(MxmlToModelParser.MxmlObjectModel objectModel, Ide configVariable, @Nonnull Ide targetVariable, boolean generatingConfig) {
    Ide variable = generatingConfig ? configVariable : targetVariable;
    for (MxmlToModelParser.MxmlMemberModel member : objectModel.getMembers()) {
      if (member instanceof MxmlToModelParser.MxmlEventHandlerModel) {
        MxmlToModelParser.MxmlEventHandlerModel eventHandlerModel = (MxmlToModelParser.MxmlEventHandlerModel) member;
        createAttachEventHandlerCode(variable, eventHandlerModel);
      } else if (member instanceof MxmlToModelParser.MxmlPropertyModel) {
        MxmlToModelParser.MxmlPropertyModel propertyModel = (MxmlToModelParser.MxmlPropertyModel) member;
        MxmlToModelParser.MxmlModel propertyValue = propertyModel.getValue();
        if (propertyValue instanceof MxmlToModelParser.MxmlValueModel) {
          createPropertyAssignmentCodeWithBindings(configVariable, targetVariable, generatingConfig, ((MxmlToModelParser.MxmlValueModel) propertyValue).getValue(), propertyModel);
        } else {
          createChildElementsPropertyAssignmentCode(propertyValue, variable, propertyModel, generatingConfig);
        }
      }
    }
  }

  private void createChildElementsPropertyAssignmentCode(MxmlToModelParser.MxmlModel childElements, Ide variable,
                                                         MxmlToModelParser.MxmlPropertyModel propertyModel, boolean generatingConfig) {
    String value = childElements instanceof MxmlToModelParser.MxmlArrayModel
            ? createArrayCodeFromChildElements(((MxmlToModelParser.MxmlArrayModel)childElements).getElements())
            : createValueCodeFromElement(null, childElements);

    if (childElements.getInstantiationMode() == PLAIN) {
      String constructorCode = String.format(DELETE_OBJECT_PROPERTY_CODE, value, "xtype")
              + String.format(DELETE_OBJECT_PROPERTY_CODE, value, "xclass");
      //constructorBodyDirectives.addAll(mxmlParserHelper.parseConstructorBody(constructorCode));
    }
    createPropertyAssignmentCode(variable, propertyModel, new JooSymbol(MxmlUtils.createBindingExpression(value)), generatingConfig);
  }

  private String createArrayCodeFromChildElements(List<MxmlToModelParser.MxmlModel> childElements) {
    List<String> arrayItems = new ArrayList<>();
    for (MxmlToModelParser.MxmlModel arrayItemNode : childElements) {
      String itemValue = createValueCodeFromElement(null, arrayItemNode);
      arrayItems.add(itemValue);
    }
    return "[" + join(arrayItems, ", ") + "]";
  }

  private static String join(List<String> array, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.size(); ++i) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(array.get(i));
    }
    return sb.toString();
  }

  @Nullable
  String createValueCodeFromElement(@Nullable Ide configVar, MxmlToModelParser.MxmlModel objectModel) {
    XmlElement objectElement = objectModel.getSourceElement();
    CompilationUnit type = objectModel.getType();
    final String className = type.getQualifiedNameStr();
    Ide typeIde = compilationUnit.addImport(className);
    String targetVariableName = null;   // name of the variable holding the object to build
    XmlAttribute idAttribute = objectElement.getAttribute(MxmlUtils.MXML_ID_ATTRIBUTE);
    String id = null;
    String additionalDeclaration = null;
    if (null != idAttribute) {
      JooSymbol idSymbol = idAttribute.getValue();
      id = (String) idSymbol.getJooValue();
      if (id.equals(compilationUnit.getConstructorParamName())) {
        return null;
      }

      Ide.verifyIdentifier(id, idSymbol);

      VariableDeclaration variableDeclaration = compilationUnit.getVariables().get(id);
      String qualifier = null != configVar ? configVar.getName() : "";
      if (null != variableDeclaration) {
        if (!variableDeclaration.isPublic()) {
          qualifier = ""; // corresponds to "this."
        }
      } else {
        String asDoc = MxmlUtils.toASDoc(objectElement.getSymbol().getWhitespace());
        int i = asDoc.lastIndexOf('\n');
        StringBuilder classBodyCode = new StringBuilder();
        classBodyCode
                .append(asDoc)
                .append('[').append(Jooc.BINDABLE_ANNOTATION_NAME).append(']')
                .append(i < 0 ? "\n" : asDoc.substring(i))
                .append("public var ").append(id).append(':').append(className).append(';');
        additionalDeclaration = classBodyCode.toString();
        classBodyDirectives.addAll(mxmlParserHelper.parseClassBody(new JooSymbol(additionalDeclaration)).getDirectives());
      }
      targetVariableName = CompilerUtils.qName(qualifier, id);
    }

    if (id != null && configVar != null // it is a declaration...
            && objectElement.getAttributes().size() == 1 // ...with only an id attribute...
            && objectElement.getChildren().isEmpty() && objectElement.getTextNodes().isEmpty()) {
      // prevent assigning a default value for such an empty declaration:
      additionalDeclarations += additionalDeclaration;
      return null;
    }

    Ide configVariable = null; // name of the variable holding the config object to use in the constructor

    if (objectModel.getInstantiationMode().isExt()) {
      // if class supports a config options parameter, create a config options object and assign properties to it:
      configVariable = createAuxVar(objectElement, id);
      renderConfigAuxVar(configVariable, typeIde != null ? typeIde : new Ide(className));
      if (targetVariableName == null) {
        targetVariableName = createAuxVar(objectElement).getName();
      }
      if (objectModel instanceof MxmlToModelParser.MxmlObjectModel) {
        // process attributes and children, using a forward reference to the object to build inside bindings:
        processAttributesAndChildNodes((MxmlToModelParser.MxmlObjectModel) objectModel, configVariable, new Ide(targetVariableName), true);
      } else {
        // TODO!
      }
    }

    String value = createValueCodeFromElement(objectModel, className, configVariable);

    StringBuilder constructorCode = new StringBuilder();
    if (null != id) {
      constructorCode.append("    ").append(targetVariableName);
    } else if (configVariable == null) {
      // no config object was built: create variable for object to build now:
      targetVariableName = createAuxVar(objectElement).getName();
      constructorCode.append("    ")
              .append("var ").append(targetVariableName).append(":").append(className);
    } else if (objectModel.getInstantiationMode().isConfig()) {
      return configVariable.getName();
    } else {
      return value; // no aux var necessary
    }
    constructorCode.append(" = ").append(value).append(";");
    //constructorBodyDirectives.addAll(mxmlParserHelper.parseConstructorBody(constructorCode.toString()));

    if (configVariable == null && !"Array".equals(className)) {
      // no config object was built or event listeners or bindings have to be added:
      // process attribute and children and assign properties directly on the target object
      Ide ide = null != targetVariableName ? new Ide(targetVariableName) : null;
      if(null == ide) {
        throw new IllegalStateException("potential NPE ahead!");
      }
      if (objectModel instanceof MxmlToModelParser.MxmlObjectModel) {
        processAttributesAndChildNodes((MxmlToModelParser.MxmlObjectModel) objectModel, null, ide, false);
      } else {
        // TODO!
      }
    }
    return targetVariableName;
  }

  private String createValueCodeFromElement(MxmlToModelParser.MxmlModel objectModel, String className, Ide configVariable) {
    String value;
    JooSymbol textContentSymbol = getTextContent(objectModel.getSourceElement());
    String textContent = ((String) textContentSymbol.getJooValue()).trim();
    if (MxmlUtils.isBindingExpression(textContent)) {
      return MxmlUtils.getBindingExpression(textContent);
    } else if ("String".equals(className)) {
      return CompilerUtils.quote(textContent);
    } else if ("int".equals(className) || "uint".equals(className) || "Number".equals(className) || "Boolean".equals(className)) {
      return textContent.isEmpty() ? null : textContent;
    }

    if (!textContent.isEmpty()) {
      throw Jooc.error(textContentSymbol, String.format("Unexpected text inside MXML element: '%s'.", textContent));
    }
    if ("Object".equals(className)) {
      value = "{}";
    } else if (objectModel instanceof MxmlToModelParser.MxmlArrayModel) {
      value = createArrayCodeFromChildElements(((MxmlToModelParser.MxmlArrayModel)objectModel).getElements());
    } else {
      StringBuilder valueBuilder = new StringBuilder();
      valueBuilder.append("new ").append(className).append("(");
      if (configVariable != null) {
        valueBuilder.append(configVariable);
      }
      valueBuilder.append(")");
      value = valueBuilder.toString();
    }
    return value;
  }

  private Ide createAuxVar(XmlElement element) {
    JooSymbol symbol = element.getSymbol();
    String prefix = element.getName();
    return createAuxVar(symbol, prefix);
  }

  @Nonnull
  private Ide createAuxVar(@Nonnull XmlElement element, @Nullable String idAttributeValue) {
    JooSymbol symbol = element.getSymbol();
    StringBuilder name = new StringBuilder();
    if (StringUtils.isEmpty(idAttributeValue)) {
      String prefix = element.getPrefix();
      if(null != prefix) {
        name.append(prefix).append('_');
      }
      name.append(element.getLocalName());
    } else {
      name.append(idAttributeValue);
      Ide.verifyIdentifier(idAttributeValue, symbol);
    }
    return createAuxVar(symbol, name.toString());
  }

  @Nonnull
  private Ide createAuxVar(@Nonnull JooSymbol symbol, @Nonnull String prefix) {
    String preferredName = CompilerUtils.uncapitalize(prefix.replaceAll("-", "\\$")) + '_' + symbol.getLine() + '_' + symbol.getColumn();
    return compilationUnit.createAuxVar(preferredName);
  }

  static String getEventHandlerName(@Nonnull MxmlToModelParser.MxmlEventHandlerModel event) {
    JooSymbol value = event.getHandlerCode();
    String eventName = event.getEventName();
    return "$on_" + eventName.replace('-', '_') + "_" + value.getLine() + "_" + value.getColumn();
  }

  private String createEventHandlerMethod(@Nonnull MxmlToModelParser.MxmlEventHandlerModel eventHandlerModel) {
    JooSymbol value = eventHandlerModel.getHandlerCode();
    String eventHandlerName = getEventHandlerName(eventHandlerModel);
    String eventTypeStr = eventHandlerModel.getEventTypeStr();
    compilationUnit.addImport(eventTypeStr);
    StringBuilder classBodyCode = new StringBuilder();
    classBodyCode
            .append("private function ").append(eventHandlerName)
            .append(" (").append("event").append(':').append(eventTypeStr).append(") :void {\n")
            .append("\n    ").append(value.getJooValue())
            .append('}');
    classBodyDirectives.addAll(mxmlParserHelper.parseClassBody(new JooSymbol(classBodyCode.toString())).getDirectives());
    return eventHandlerName;
  }

  private void createAttachEventHandlerCode(@Nonnull Ide ide, @Nonnull MxmlToModelParser.MxmlEventHandlerModel event) {
    String eventName = event.getEventName();
    String eventTypeStr = event.getEventTypeStr();
    String variable = ide.getName();
    String eventNameConstant = (eventName.substring(0, 1) + eventName.substring(1).replaceAll("([A-Z])", "_$1")).toUpperCase();
    String eventHandlerName = createEventHandlerMethod(event);
    StringBuilder constructorCode = new StringBuilder();
    constructorCode.append("    ").append(variable).append("." + MxmlUtils.ADD_EVENT_LISTENER_METHOD_NAME + "(").append(eventTypeStr)
            .append(".").append(eventNameConstant)
            .append(", ")
            .append(eventHandlerName)
            .append(");");
    constructorBodyDirectives.addAll(mxmlParserHelper.parseConstructorBody(constructorCode.toString()));
  }

  private void createPropertyAssignmentCode(@Nonnull Ide variable, @Nonnull MxmlToModelParser.MxmlPropertyModel propertyModel, @Nonnull JooSymbol value, boolean generatingConfig) {
    Directive propertyAssignment = createPropertyAssignment(variable, propertyModel, value, generatingConfig);
    //constructorBodyDirectives.add(propertyAssignment);
  }

  @Nonnull
  private Directive createPropertyAssignment(@Nonnull Ide variable, @Nonnull MxmlToModelParser.MxmlPropertyModel propertyModel, @Nonnull JooSymbol value, boolean generatingConfig) {
    String attributeValueAsString = getAttributeValueAsString(propertyModel.getPropertyDeclaration(), value);

    String propertyName = generatingConfig ? propertyModel.getConfigOptionName() : propertyModel.getPropertyDeclaration().getName();
    Expr rightHandSide = mxmlParserHelper.parseExpression(value.replacingSymAndTextAndJooValue(value.sym, attributeValueAsString, attributeValueAsString));
    return MxmlAstUtils.createPropertyAssignment(variable, rightHandSide, propertyName, true);
  }

  private static String getAttributeValueAsString(@Nonnull TypedIdeDeclaration propertyModel, @Nonnull JooSymbol value) {
    return MxmlUtils.valueToString(getAttributeValue(propertyModel, value));
  }

  private static Object getAttributeValue(@Nonnull TypedIdeDeclaration propertyModel, @Nonnull JooSymbol value) {
    TypeRelation typeRelation = propertyModel.getOptTypeRelation();
    String propertyType = typeRelation == null ? null : typeRelation.getType().getIde().getQualifiedNameStr();
    return MxmlUtils.getAttributeValue((String) value.getJooValue(), propertyType);
  }

  Collection<Directive> getConstructorBodyDirectives() {
    return constructorBodyDirectives;
  }

  Collection<Directive> getClassBodyDirectives() {
    return classBodyDirectives;
  }

}
