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
    // constructor or instanceof may return false negatives:
    if (object instanceof type || object.constructor === type) {
      return true;
    }
    // special case int and uint:
//      if (type === $$int || type === $$uint) {
//        if (object instanceof Number || typeof object === 'number') {
//          // thanks http://stackoverflow.com/questions/3885817/how-to-check-if-a-number-is-float-or-integer
//          return (type === $$uint ? object >>> 0 : object >> 0) === object + 0; // "+ 0" converts Number to number!
//        }
//      } else
    if (typeof object === 'object' && type.$className) {
      if (object.isInstance) {
        var mixins = object.mixins;
        if (Ext.isObject(mixins)) {
          if (mixins[type.$className]) {
            return true;
          }
          if (type.prototype.mixinId && mixins[type.prototype.mixinId]) {
            return true;
          }
        }
        return false;
      } else if (object.xclass) {
        return Ext.ClassManager.get(object.xclass).prototype instanceof type;
      }
    }
    // special case for special observables, e.g. classes:
    if (object.isObservable && (type.$className === "Ext.mixin.Observable" || type.$className === "Ext.util.Observable")) {
      return true;
    }
    return false;
  },
  as: function (object, type) {
    return AS3.is(object, type) ? object : null;
  },
  cast: function (type, value) {
    if (value.isInstance || value.xclass) {
      if (!AS3.is(value, type)) {
        throw new TypeError();
      }
    } else if (type.$className) {
      value.xclass = type.$className;
    }
    return value;
  }
};

Ext.Loader.setPath('AS3', 'joo/classes');

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