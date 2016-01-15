Class = {
  $isClass: true,
  $className: "Class",
  __isInstance__: function(object) {
    return typeof object === "function" && !!object.$isClass;
  }
};
Vector$object = Array;

AS3 = {
  bind: function bind(object, boundMethodName) {
    var method = object[boundMethodName];
    if (object.hasOwnProperty(boundMethodName)) {
      return method;
    }
    var boundMethod = method.bind(object);
    Object.defineProperty(object, boundMethodName, {
      writable: true,    // TODO: for backwards-compatibility only
      value: boundMethod
    });
    return boundMethod;
  },
  is: function (object, type) {
    if (!type || object === undefined || object === null) {
      return false;
    }
    if (Ext.isFunction(type.__isInstance__)) {
      return type.__isInstance__(object);
    }
    if (!Ext.isFunction(type)) {
      return false;
    }
    // constructor or instanceof may return false negatives:
    if (object.constructor === type || object instanceof type) {
      return true;
    }
    // special case for special observables, e.g. classes:
    if (object.isObservable && (type.$className === "Ext.mixin.Observable" || type.$className === "Ext.util.Observable")) {
      return true;
    }

    if (typeof object === 'object') {
      var objectType;
      if (object.isInstance) {
        objectType = object.self;
      } else {
        var typeName = object.xclass || object.xtype && Ext.ClassManager.getNameByAlias("widget." + object.xtype);
        if (typeName) {
          objectType = Ext.ClassManager.get(typeName);
        }
      }
      if (AS3.isAssignableFrom(type, objectType)) {
        return true;
      }
    }
    return false;
  },
  isAssignableFrom: function(type, clazz) {
    if (!type || !clazz) {
      return false;
    }
    if (type === clazz) {
      return true;
    }
    if (clazz.prototype instanceof type) {
      return true;
    }
    if (type.$className) {
      var mixins = clazz.prototype.mixins;
      if (Ext.isObject(mixins)) {
        if (mixins[type.$className]) {
          return true;
        }
        if (type.prototype.mixinId && mixins[type.prototype.mixinId]) {
          return true;
        }
      }
    }
    return false;
  },
  as: function (object, type) {
    return AS3.is(object, type) ? object : null;
  },
  cast: function (type, value) {
    if (value === undefined || value === null) {
      return value;
    }
    if (type.$className && type.prototype &&
            typeof value === "object" && !value.isInstance && !value.xclass && !value.xtype) {
      if (type.prototype.hasOwnProperty("xtype")) {
        value.xtype = type.prototype.xtype;
      } else {
        value.xclass = type.$className;
      }
    } else if (!AS3.is(value, type)) {
      throw new TypeError("Value cannot be cast to " + Ext.getClassName(type) + ": " + value);
    }
    return value;
  },
  getBindable: function (object, property) {
    if (object.isInstance) {
      return object.getConfig(property);
    } else {
      return object[property];
    }
  },
  setBindable: function (object, property, value) {
    if (object.isInstance) {
      object.setConfig(property, value);
    } else {
      object[property] = value;
    }
  }
};

var joo = Ext.ns("joo");
joo.getQualifiedObject = function(name) {
  return eval(name);
};
joo.getOrCreatePackage = function(name) {
  return Ext.ns("AS3." + name);
};
Ext.ns("joo.localization");

Ext.Loader.setPath({
  'AS3': 'joo/classes',
  'JooOverrides': 'joo/overrides'
});

Ext.Class.registerPreprocessor('accessors', function (Class, data) {
  if (data.accessors) {
    if (data.accessors.statics) {
      Object.defineProperties(Class, data.accessors.statics);
      delete data.accessors.statics;
    }
    Object.defineProperties(Class.prototype, data.accessors);
    delete data.accessors;
  }
});
