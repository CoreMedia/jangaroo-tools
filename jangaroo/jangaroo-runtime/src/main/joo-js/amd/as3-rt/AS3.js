define("as3-rt/AS3", ["as3/joo/getOrCreatePackage", "as3/joo/JooClassDeclaration", "as3/trace"], function(getOrCreatePackage, JooClassDeclaration, trace) {
  "use strict";
  function toString() {
    return "[Class " + this.$class.name + "]";
  }
  function convertShortcut(propertyDescriptor) {
    return propertyDescriptor !== null && typeof propertyDescriptor === "object" ? propertyDescriptor
      // anything *not* an object is a shortcut for a property descriptor with that value (non-enumerable, non-configurable, but (for the time being) writable):
            : { value: propertyDescriptor, writable: true }
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
  function amdIdToQName(amdName) {
    return amdName.split("/")
            .slice(1)  // ignore "as3" segments
            .join(".");
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
  function compilationUnit(module, exports, definingCode) {
    var qName = amdIdToQName(module.id);
    var lastDotPos = qName.lastIndexOf(".");
    Object.defineProperty(exports, "_", {
      configurable: true,
      get: function () {
        var result;
        definingCode(function(value) {
          result = convertShortcut(value);
          Object.defineProperty(exports, "_", result);
        });
        return handleMetadata(result.value);
      },
      set: function(value) {
        //noinspection BadExpressionStatementJS
        this._; // initialize, but ignore resulting value as it is overwritten immediately!
        this._ = value;
      }
    });
    // For Jangaroo 2 backwards-compatibility, make compilation unit accessible via package:
    var cuName = qName.substr(lastDotPos + 1);
    var package_ = getOrCreatePackage(qName.substr(0, lastDotPos));
    Object.defineProperty(package_, cuName, {
      get: function() {
        return exports._;
      },
      set: function(value) {
        exports._ = value;
      }
    });
  }

  function defineClass(module, config) {
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
                amdIdToQName(module.id),
                clazz,
                extends_,
                $implements,
                config.metadata
        )};
        // establish super class relation to satisfy Ext JS:
        staticMembers.superclass = { value: extends_.prototype };
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

  function defineInterface(module, config) {
    var qName = amdIdToQName(module.id);
    var interfaces = {};
    interfaces[qName] = true;
    config.extends_ && config.extends_.forEach(function (i) {
      i.addInterfaces(interfaces);
    });
    return Object.defineProperties(function(){}, convertShortcuts({
      $class: { value: new JooClassDeclaration(
              qName,
              undefined,
              undefined,
              interfaces,
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
      writable: true,    // TODO: for backwards-compatibility only
      value: boundMethod
    });
    return boundMethod;
  }

  var OBJECT_TO_STRING = Object.prototype.toString;
  var TYPE_OBJECT = "[object Object]";
  var JOO_JAVASCRIPT_OBJECT = "joo.JavaScriptObject";

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
        if (object instanceof type || object.constructor === type) {
          return true;
        }
        // Is it a simple Object?
        if (OBJECT_TO_STRING.call(object) === TYPE_OBJECT) {
         // check whether type extends joo.JavaScriptObject:
         var currentType = type.$class;
         while (currentType) {
           if (currentType.fullClassName === JOO_JAVASCRIPT_OBJECT) {
             return true;
           }
           currentType = currentType.superClassDeclaration;
         }
       }
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
    if (object && !type.interfaces && !isInstance(type, object)) {
      // Currently, we do not even warn when the type is an interface.
      // Like joo.JavaScriptObject, there should be a marker interface or annotation
      // that any Object can be cast to the given interface.
      // Later, the following warning will be replaced by throwing a TypeError,
      // but we do not dare change Jangaroo-2-semantics yet.
      trace("[WARN]", "'" + object + "' cannot be cast to " + type + ".");
    }
    return object;
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