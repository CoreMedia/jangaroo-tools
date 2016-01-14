Class = {
  $isClass: true,
  $className: "Class"
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
    // special case meta-class Class:
    if (type === Class) {
      return !!object.$isClass;
    }
    if (!Ext.isFunction(type)) {
      return false;
    }
    var $className = type.$className;
    if ($className === "AS3.Error") {
      // use built-in Error class for instanceof check:
      type = Error;
    }
    // constructor or instanceof may return false negatives:
    if (object instanceof type || object.constructor === type) {
      return true;
    }
    // special case int and uint:
    if (type === AS3.int || type === AS3.uint) {
      // thanks http://stackoverflow.com/questions/3885817/how-to-check-if-a-number-is-float-or-integer
      return (object instanceof Number || typeof object === 'number') &&
              (type === AS3.uint ? object >>> 0 : object >> 0) === object + 0; // "+ 0" converts Number to number!
    }
    if (typeof object === 'object' && $className) {
      if (object.isInstance) {
        var mixins = object.mixins;
        if (Ext.isObject(mixins)) {
          if (mixins[$className]) {
            return true;
          }
          if (type.prototype.mixinId && mixins[type.prototype.mixinId]) {
            return true;
          }
        }
        return false;
      } else if (object.xclass) {
        var prototype = Ext.ClassManager.get(object.xclass).prototype;
        return prototype === type.prototype || prototype instanceof type;
      }
    }
    // special case for special observables, e.g. classes:
    if (object.isObservable && ($className === "Ext.mixin.Observable" || $className === "Ext.util.Observable")) {
      return true;
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
    if (value.isInstance || value.xclass) {
      if (!AS3.is(value, type)) {
        throw new TypeError();
      }
    } else if (type.$className) {
      value.xclass = type.$className;
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
