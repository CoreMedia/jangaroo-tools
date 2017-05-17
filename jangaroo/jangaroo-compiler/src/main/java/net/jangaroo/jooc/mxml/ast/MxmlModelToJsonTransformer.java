package net.jangaroo.jooc.mxml.ast;

import net.jangaroo.jooc.JangarooParser;
import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.json.Json;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.jooc.json.JsonValue;
import net.jangaroo.jooc.mxml.MxmlUtils;
import net.jangaroo.utils.AS3Type;

import javax.annotation.Nonnull;

import static net.jangaroo.jooc.mxml.ast.MxmlModelToActionScriptTransformer.getEventHandlerName;


final class MxmlModelToJsonTransformer {

  private final JangarooParser jangarooParser;

  MxmlModelToJsonTransformer(JangarooParser jangarooParser) {
    this.jangarooParser = jangarooParser;
  }

  Json modelToJson(@Nonnull MxmlToModelParser.MxmlModel mxmlModel) {
    Json json;
    CompilationUnit type = mxmlModel.getType();
    String typeName = type == null ? "*" : type.getQualifiedNameStr();
    if (mxmlModel instanceof MxmlToModelParser.MxmlArrayModel) {
      json = arrayModelToJsonArray((MxmlToModelParser.MxmlArrayModel) mxmlModel);
    } else if (mxmlModel instanceof MxmlToModelParser.MxmlObjectModel) {
      JsonObject objectModel = objectModelToJsonObject((MxmlToModelParser.MxmlObjectModel) mxmlModel);
      Object defaultValue = getDefaultValue(typeName);
      if (defaultValue != null) {
        json = new JsonValue(defaultValue);
      } else {
        if (!"Object".equals(typeName) && !mxmlModel.isUsePlainObjects()) {
          objectModel.settingWrapperClass(typeName);
          objectModel.setInstantiationMode(
                  (mxmlModel.isUseConfigObjects() != null ? mxmlModel.isUseConfigObjects() : false)
                          ? JsonObject.InstantiationMode.EXT_CONFIG
                          : CompilationUnitUtils.constructorSupportsConfigOptionsParameter(typeName, jangarooParser)
                          ? JsonObject.InstantiationMode.EXT_CREATE
                          : JsonObject.InstantiationMode.MXML
          );
        }
        json = objectModel;
      }
    } else if (mxmlModel instanceof MxmlToModelParser.MxmlValueModel){
      MxmlToModelParser.MxmlValueModel valueModel = (MxmlToModelParser.MxmlValueModel) mxmlModel;
      json = new JsonValue(getValue(typeName, valueModel));
    } else {
      throw new IllegalStateException("Unknown MxmlModel subclass " + mxmlModel.getClass());
    }
    json.setId(mxmlModel.getId());
    return json;
  }

  private Object getDefaultValue(String typeName) {
    AS3Type as3Type = AS3Type.typeByName(typeName);
    if (as3Type != null) {
      switch (as3Type) {
        case INT:
        case UINT:
        case NUMBER:
          return 0;
        case BOOLEAN:
          return false;
        case STRING:
          return "";
      }
    }
    return null;
  }

  private Object getValue(String typeName, MxmlToModelParser.MxmlValueModel valueModel) {
    JooSymbol valueSymbol = valueModel.getValue();
    Object value = MxmlUtils.getAttributeValue((String) valueSymbol.getJooValue(), typeName);
    if (value instanceof String && MxmlUtils.isBindingExpression((String) value)) {
      value = JsonObject.code(MxmlUtils.getBindingExpression((String) value));
    }
    return value;
  }

  private JsonArray arrayModelToJsonArray(MxmlToModelParser.MxmlArrayModel objectNode) {
    return new JsonArray(objectNode.getElements().stream().map(this::modelToJson).toArray());
  }

  JsonObject objectModelToJsonObject(MxmlToModelParser.MxmlObjectModel objectModel) {
    return objectModelToJsonObject(new JsonObject(), objectModel);
  }

  private JsonObject objectModelToJsonObject(JsonObject model, MxmlToModelParser.MxmlObjectModel objectModel) {
    for (MxmlToModelParser.MxmlMemberModel member : objectModel.getMembers()) {
      if (member instanceof MxmlToModelParser.MxmlPropertyModel) {
        MxmlToModelParser.MxmlPropertyModel propertyModel = (MxmlToModelParser.MxmlPropertyModel) member;
        String configOptionName = propertyModel.getConfigOptionName();
        MxmlToModelParser.MxmlModel propertyValueModel = propertyModel.getValue();
        Json configOptionValue = modelToJson(propertyValueModel);
        model.set(configOptionName, configOptionValue);
      } else if (member instanceof MxmlToModelParser.MxmlEventHandlerModel) {
        MxmlToModelParser.MxmlEventHandlerModel eventHandlerModel = (MxmlToModelParser.MxmlEventHandlerModel) member;
        JsonObject listeners = (JsonObject) model.get("listeners");
        if (listeners == null) {
          listeners = new JsonObject();
          model.set("listeners", listeners);
        }
        String eventHandlerMethodName = getEventHandlerName(eventHandlerModel);
        listeners.set(eventHandlerModel.getEventName(), JsonObject.code(eventHandlerMethodName));
      }
    }
    return model;
  }

}
