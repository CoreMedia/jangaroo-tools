define(["./es5-polyfills"], function() {
  "use strict";
  // alias for global package to avoid name-clashes between top-level classes and JavaScript global identifiers:
  window.js = window;
  // Jangaroo namespace
  window.joo = {
    // helper variable for flash.utils.getTimer():
    startTime: new Date().getTime(),
    // built-in Error constructor called as function unfortunately always creates a new Error object,
    // so we have to emulate it:
    Error: function(message/*String*/, id/*:int*/) {
      this.message = message || "";
      this.id = id >> 0;
    },
    assert: function assert(cond, file, line, column) {
      if (!cond)
        throw new Error(file+"("+line+":"+column+"): assertion failed");
    }
  };
  window.joo.Error.prototype = window.Error.prototype;

  // Vector = Array
  window.Vector$object = Array;

  function toString() {
    return "[Class " + this.name + "]";
  }
  function convertShortcuts(propertyDescriptors) {
    var result = {};
    if (propertyDescriptors) {
      for (var name in propertyDescriptors) {
        var propertyDescriptor = propertyDescriptors[name];
        result[name] = propertyDescriptor !== null && typeof propertyDescriptor === "object" ? propertyDescriptor
          // anything *not* an object is a shortcut for a property descriptor with that value (non-writable, non-enumerable, non-configurable):
                : { value: propertyDescriptor };
        if (propertyDescriptor.get) {
          result["get$" + name] = { value: propertyDescriptor.get };
        }
        if (propertyDescriptor.set) {
          result["set$" + name] = { value: propertyDescriptor.set };
        }
        if (name !== "constructor") {
          result[name].enumerable = true; // TODO: only for debugging, save describeType data elsewhere!
        }
      }
    }
    return result;
  }
  function defineClass(exports, definingCode) {
    Object.defineProperty(exports, "_", {
      configurable: true,
      get: function() {
        var config = definingCode();
        var members = convertShortcuts(config.members);
        var clazz = members.constructor.value;
        Object.defineProperty(this, "_", { value: clazz });
        var extends_ = config.extends_ || Object; // super class
        if (extends_ === Object || extends_ === joo.JavaScriptObject) {
          // do not set "constructor" property, or it will become enumerable in IE8!
          delete members.constructor;
        }
        var implements_ = config.implements_ ? typeof config.implements_ === "function" ? [config.implements_] : config.implements_ : [];
        // create set of all interfaces implemented by this class
        var $implements = extends_.$implements ? Object.create(extends_.$implements) : {};
        implements_.forEach(function(i) { i($implements); });
        var staticMembers = convertShortcuts(config.staticMembers);
        // add some meta information under reserved static field "$class":
        staticMembers.$class = { value: {
          metadata: config.metadata || {},
          implements_: $implements,
          fullClassName: config.package_ ? config.package_ + "." + config.class_ : config.class_,
          toString: function() { return this.fullClassName; }
        }};
        staticMembers.toString = { value: toString }; // add Class#toString()
        Object.defineProperties(clazz, staticMembers);   // add static members
        clazz.prototype = Object.create(extends_.prototype, members); // establish inheritance prototype chain and add instance members

        var staticCode = config.staticCode;
        // execute static initializers and code:
        staticCode && staticCode.call(clazz);
        return clazz;
      }
    });
  }

  function defineInterface(fullyQualifiedName, extends_) {
    function Interface($implements) {
      extends_.forEach(function(i) { i($implements); });
      $implements[fullyQualifiedName] = true;
      return $implements;
    }
    Interface.isInstance = function(object) {
      return object !== null && typeof object === "object" &&
              !!object.constructor.$class &&
              fullyQualifiedName in object.constructor.$class.implements_;
    };
    Interface.toString = function toString() {
      return "[Interface " + fullyQualifiedName + "]";
    };
    return Interface;
  }

  function defineGlobal(exports, definingCode) {
    Object.defineProperty(exports, "_", {
      configurable: true,
      // enumerable: true, // TODO: for debugging only
      get: function() {
        definingCode.call(this);
        return this._;
      }
    });
  }

  function bind(object, boundMethodName) {
    if (object.hasOwnProperty(boundMethodName)) {
      return object[boundMethodName];
    }
    var boundMethod = object[boundMethodName].bind(object);
    Object.defineProperty(object, boundMethodName, {
      // enumerable: true, // TODO: for debugging only
      value: boundMethod
    });
    return boundMethod;
  }

  function is(object, type) {
    return !!type && object !== undefined && object !== null &&
      // instanceof returns false negatives in some browsers, so check constructor property, too:
      (object instanceof type || object.constructor === type ||
      // "type" may be an interface:
      typeof type.isInstance === "function" && type.isInstance(object));
  }

  function as(object, type) {
    return is(object, type) ? object : null;
  }

  function cast(type, object) {
    if (object === undefined || object === null) {
      return null;
    }
    if (object instanceof type || object.constructor === type ||
      // "type" may be an interface:
      typeof type.isInstance === "function" && type.isInstance(object)) {
      return object;
    }
    throw new TypeError("'" + object + "' cannot be cast to " + type + ".");
  }

  return {
    class_: defineClass,
    interface_: defineInterface,
    global_: defineGlobal,
    as: as,
    cast: cast,
    is: is,
    bind: bind
  }
});
