//define(["./es5-polyfills"], function() {
define(function() {
  //"use strict"; // sorry, we need context-agnostic access to the global object!
  var window = (function(){return this})();
  // alias for global package to avoid name-clashes between top-level classes and JavaScript global identifiers:
  window.js = window;

  // built-ins:
  window.Class = Function;
  var int_ = window["int"] = function int_(num) {
    return num >> 0;
  };
  int_.MAX_VALUE =  2147483647;
  int_.MIN_VALUE = -2147483648;
  var uint_ = window["uint"] = function uint_(num) {
    return num >>> 0;
  };
  uint_.MAX_VALUE = 4294967295;
  uint_.MIN_VALUE = 0;

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
    // add event listener using Ext JS config object style:
    addEventListener: function addEventListener(config, eventName, eventType, callback) {
      if (!config.listeners) {
        config.listeners = {};
      }
      config.listeners[eventName] = function() {
        callback(new eventType(arguments));
      }
    }
  };
  window.joo.Error.prototype = window.Error.prototype;
  function assert(cond, file, line, column) {
    if (!cond)
      throw new Error(file+"("+line+":"+column+"): assertion failed");
  }

  // Vector = Array
  window.Vector$object = Array;

  function metaToString() {
    return this.qName;
  }
  function toString() {
    return "[Class " + this.$class.name + "]";
  }
  function convertShortcut(propertyDescriptor) {
    return propertyDescriptor !== null && typeof propertyDescriptor === "object" ? propertyDescriptor
      // anything *not* an object is a shortcut for a property descriptor with that value (non-writable, non-enumerable, non-configurable):
            : { value: propertyDescriptor }
  }
  function convertShortcuts(propertyDescriptors) {
    var result = {};
    if (propertyDescriptors) {
      for (var name in propertyDescriptors) {
        var propertyDescriptor = propertyDescriptors[name];
        result[name] = convertShortcut(propertyDescriptor);
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
  var metadataHandlers = {};
  function registerMetadataHandler(handler) {
    metadataHandlers[handler.metadata] = handler;
  }
  function handleMetadata(value) {
    if (value.$class) {
      var metadata = value.$class.metadata;
      if (metadata) {
        for (var key in metadata) {
          var metadataHandler = metadataHandlers[key];
          if (metadataHandler) {
            metadataHandler(value, metadata[key]);
          }
        }
      }
    }
    return value;
  }
  function getModuleName(qName) {
    return "classes/" + qName.replace(/\./g, "/");
  }
  function compilationUnit(exports, definingCode) {
    Object.defineProperty(exports, "_", {
      configurable: true,
      get: function() {
        var result;
        definingCode(function(value) {
          result = convertShortcut(value);
          Object.defineProperty(exports, "_", result);
        });
        return handleMetadata(result.value);
      }
    });
  }

  function defineClass(config) {
        var members = convertShortcuts(config.members);
        var clazz = members.constructor.value;
        var extends_ = config.extends_ || Object; // super class
        if (extends_ === joo.JavaScriptObject) {
          // do not set "constructor" property, or it will become enumerable in IE8!
          delete members.constructor;
        }
        var implements_ = config.implements_ || [];
        // create set of all interfaces implemented by this class
        var $implements = extends_.$class && extends_.$class.implements_ ? Object.create(extends_.$class.implements_ ) : {};
        implements_.forEach(function(i) { i.addInterfaces($implements); });
        var staticMembers = convertShortcuts(config.staticMembers);
        // add some meta information under reserved static field "$class":
        var qName = config.package_ ? config.package_ + "." + config.class_ : config.class_;
        staticMembers.$class = { value: {
          metadata: config.metadata || {},
          extends_: extends_,
          implements_: $implements,
          name: config.class_,
          qName: qName,
          toString: metaToString
        }};
        staticMembers.toString = { value: toString }; // add Class#toString()
        Object.defineProperties(clazz, staticMembers);   // add static members
        clazz.prototype = Object.create(extends_.prototype, members); // establish inheritance prototype chain and add instance members
        return clazz;
  }

  function addInterfaces($implements) {
    for (var interface_ in this.interfaces) {
      $implements[interface_] = true;
    }
    return $implements;
  }

  function defineInterface(exports, config) {
    var qName = config.package_ ? config.package_ + "." + config.interface_ : config.interface_;
    var interfaces = {};
    interfaces[qName] = true;
    config.extends_ && config.extends_.forEach(function (i) {
      i.addInterfaces(interfaces);
    });
    Object.defineProperties(exports, convertShortcuts({
      $class: { value: Object.defineProperties({}, convertShortcuts({
        name: config.interface_,
        qName: qName,
        toString: metaToString
      }))},
      interfaces: { value: interfaces },
      addInterfaces: addInterfaces,
      toString: toString
    }));
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

  /**
   * Internal utility to check whether the non-null/-undefined "object" is an instance of the given type.
   * Note that ActionScript, in contrast to JavaScript, coerces primitives to objects before doing the check!
   */
  function isInstance(type, object) {
    //noinspection FallthroughInSwitchStatementJS
    switch (typeof object) {
      case "boolean":
      case "number":
      case "string":
        object = Object(object);
        // fall through!
      case "object":
      case "function":
        // "type" may be an interface:
        if (typeof type === "object") {
          return !!object.constructor.$class &&
                  type.$class.qName in object.constructor.$class.implements_;
        }
        if (type === int_ || type === uint_) {
          return object instanceof Number && type(object) === object.valueOf();
        }
        // type is a Class: instanceof returns false negatives in some browsers, so check constructor property, too:
        return object instanceof type || object.constructor === type;
    }
    return false;
  }

  function is(object, type) {
    return object !== undefined && object !== null && isInstance(type, object);
  }

  function as(object, type) {
    return is(object, type) ? object : null;
  }

  function cast(type, object) {
    if (object === undefined || object === null) {
      return null;
    }
    if (isInstance(type, object)) {
      return object;
    }
    throw new TypeError("'" + object + "' cannot be cast to " + type + ".");
  }

  return {
    compilationUnit: compilationUnit,
    class_: defineClass,
    interface_: defineInterface,
    as: as,
    cast: cast,
    is: is,
    bind: bind,
    assert: assert,
    registerMetadataHandler: registerMetadataHandler,
    getModuleName: getModuleName
  }
});
