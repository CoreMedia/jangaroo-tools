(function() {
function isGetterOrSetter(object, propertyName) {
  if (typeof Object.prototype.__lookupGetter__!="function")
    return false;
  return object.__lookupGetter__(propertyName) || object.__lookupSetter__(propertyName);
}
var flash_utils = joo.getOrCreatePackage("flash.utils");
flash_utils.getQualifiedClassName = function getQualifiedClassName(value) {
      var type = typeof value=="function" ? value : value.constructor;
      return typeof type["$class"]=="object" ? type["$class"]["fullClassName"].replace(/\.([^\.]+$)/,"::$1") : String(type);
    };
flash_utils.getQualifiedSuperclassName = function getQualifiedSuperclassName(value) {
      var type = typeof value=="function" ? value : value.constructor;
      return typeof type["$class"]=="object" ? type["$class"]["superClassDeclaration"]["fullClassName"].replace(/\.([^\.]+$)/,"::$1") : String(type);
    };
flash_utils.describeType = function(value) {
      var type = typeof value=="function" ? value : value.constructor;
      // fake collection:
      var len = 0;
      var methods = {
        length: function() {
          return len;
        }
      };
      if (type && type.prototype) {
        for (var p in type.prototype) {
          if (p.match(/^[a-zA-Z_]/) && !isGetterOrSetter(type.prototype,p)
              && typeof type.prototype[p]=="function") {
            methods[len++] = p;
          }
        }
      }
      return {
        attribute: function(attr) { if(attr=="name") return flash.utils.getQualifiedClassName(value); },
        method: {
          "@name": methods
        }
      };
    };
flash_utils.getDefinitionByName = function(name) {
  var clazz = joo.getQualifiedObject(name);
  if (typeof clazz !== 'function') {
    throw new ReferenceError(name);
  }
  return clazz;
};
var startTime = new Date().getTime();
flash_utils.getTimer = function() {
      return new Date().getTime()-startTime;
};
function applyWithRestParameters(closure, parameters) {
  if (parameters.length > 2) {
    var rest = Array.prototype.slice.call(parameters, 2);
    return function() {
      closure.apply(null, rest);
    };
  } else {
    return closure;
  }
}
flash_utils.setTimeout = function(closure, delay) {
  return setTimeout(applyWithRestParameters(closure, arguments), delay);
};
flash_utils.clearTimeout = clearTimeout;
flash_utils.setInterval = function(closure, delay) {
  return setInterval(applyWithRestParameters(closure, arguments), delay);
};
flash_utils.clearInterval = clearInterval;
flash_utils.escapeMultiByte = function() {
  throw new Error("not implemented");
};
flash_utils.unescapeMultiByte = function() {
  throw new Error("not implemented");
};
var flash_net = joo.getOrCreatePackage("flash.net");
flash_net.navigateToURL = function(request, windowName) {
  window.open(request.url, windowName || "_blank");
};
flash_net.sendToURL = function(request) {
  var xhr = new XMLHttpRequest();
  xhr.open(request.method, request.url);
  xhr.setRequestHeader("Content-Type", request.contentType);
  var requestHeaders = request.requestHeaders;
  for (var i = 0; i < requestHeaders.length; i++) {
    var requestHeader = requestHeaders[i];
    xhr.setRequestHeader(requestHeader.name, requestHeader.value);
  }
  xhr.send(request.method === "GET" ? undefined : request.data);
};
var classAliasRegistry = {};
flash_net.registerClassAlias = function(aliasName, classObject) {
  classAliasRegistry[aliasName] = classObject;
};
flash_net.getClassByAlias = function(aliasName) {
  return classAliasRegistry[aliasName];
};

joo.getOrCreatePackage("flash.system").fscommand = function() {
  throw new Error('not implemented');
};
joo.getOrCreatePackage("flash.profiler").showRedrawRegions = function() {
  throw new Error('not implemented');
};
joo.classLoader.import_("joo.flash.Meta");
})();
