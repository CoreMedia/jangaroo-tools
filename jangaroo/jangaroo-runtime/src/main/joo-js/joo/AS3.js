Ext.define("AS3", {
  singleton: true,
  bind: function bind(object, boundMethodName) {
    var method = object[boundMethodName];
    if (object.hasOwnProperty(boundMethodName)) {
      return method;
    }
    var boundMethod = function () {
      return method.apply(object, arguments);
    };
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
//      if (type === Class) {
//        return !!object.$isClass;
//      }
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
        return Ext.isObject(object.mixins) && object.mixins[type.$className];
      } else if (object.xclass) {
        // TODO: inheritance!
        return object.xclass === type.$className;
      }
    }
    return false;
  },
  as: function(object, type) {
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
});

Ext.Class.registerPreprocessor('accessors', function (Class, data) {
  if (data.accessors) {
    Object.defineProperties(Class.prototype, data.accessors);
    delete data.accessors;
  }
});