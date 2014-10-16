define("as3-rt/AS3", ["as3/joo/JooClassDeclaration"], function(JooClassDeclaration) {
  "use strict";
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
    if (value && value.$class) {
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
  function compilationUnit(exports, definingCode) {
    function getter() {
      var result;
      definingCode(function(value) {
        result = convertShortcut(value);
        Object.defineProperty(exports, "_", result);
      });
      return handleMetadata(result.value);
    }
    Object.defineProperties(exports, convertShortcuts({
      "_": {
        configurable: true,
        get: getter,
        set: function(value) {
          getter(); // initialize, but ignore resulting value as it is overwritten immediately!
          exports._ = value;
        }
      }
    }));
  }

  function defineClass(config) {
        var members = convertShortcuts(config.members);
        var clazz = members.constructor.value;
        var extends_ = config.extends_ || Object; // super class
        var implements_ = config.implements_ || [];
        // create set of all interfaces implemented by this class
        var $implements = extends_.$class && extends_.$class.implements_ ? Object.create(extends_.$class.implements_ ) : {};
        implements_.forEach(function(i) { i.addInterfaces($implements); });
        var staticMembers = convertShortcuts(config.staticMembers);
        // add some meta information under reserved static field "$class":
        staticMembers.$class = { value: new JooClassDeclaration(
                config.package_,
                config.class_,
                clazz,
                extends_,
                $implements,
                config.metadata
        )};
        staticMembers.toString = { value: toString }; // add Class#toString()
        Object.defineProperties(clazz, staticMembers);   // add static members
        // for classes extending joo.JavaScriptObject, remove the constructor, as it would be enumerable in IE8:
        var current = clazz;
        while (current.$class) {
          if (current.$class.qName === "joo.JavaScriptObject") {
            delete members.constructor;
            break;
          }
          current = current.$class.extends_;
        }
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
      $class: { value: new JooClassDeclaration(
              config.package_,
              config.interface_,
              undefined,
              undefined,
              config.metadata
      )},
      interfaces: { value: interfaces },
      addInterfaces: addInterfaces,
      toString: toString
    }));
  }

  function bind(object, boundMethodName) {
    var method = object[boundMethodName];
    if (object.hasOwnProperty(boundMethodName)) {
      return method;
    }
    var boundMethod = function() {
      return method.apply(object, arguments);
    };
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
        // is it an AS3 class or interface?
        if (type.$class) {
          // "type" may be an interface:
          if (type.interfaces) {
            return !!object.constructor.$class &&
                    type.$class.qName in object.constructor.$class.implements_;
          }
          if (typeof type.$class.isInstance === "function") {
            return type.$class.isInstance(object);
          }
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

  var bindingStack = [];

  function getBindable(object, getter, event) {
    var result;
    if (object) { // allow undefined / null objects; simply return undefined
      result = object[getter]();
      if (bindingStack.length) {
        bindingStack[bindingStack.length - 1].dependOn(object, event);
      }
    }
    return result;
  }

  function executeBinding(binding) {
    bindingStack.push(binding);
    try {
      binding._execute();
    } finally {
      bindingStack.pop();
    }
  }

  return {
    compilationUnit: compilationUnit,
    class_: defineClass,
    interface_: defineInterface,
    as: as,
    cast: cast,
    is: is,
    bind: bind,
    registerMetadataHandler: registerMetadataHandler,
    bindingStack: bindingStack,
    getBindable: getBindable,
    executeBinding: executeBinding
  }
});
