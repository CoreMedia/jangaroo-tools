Ext.ns("joo");

joo.startTime = new Date().getTime();
if (typeof joo.debug !== "boolean") {
  joo.debug = typeof location === "object" &&
    typeof location.hash === "string" &&
    !!location.hash.match(/(^#|&)joo.debug(=true|&|$)/);
}

(function() {
  var scriptsToLoad = [];
  var scriptLoading = null;
  var loadNextScript = function() {
    if (!scriptLoading && scriptsToLoad.length > 0) {
      scriptLoading = scriptsToLoad.shift();
      Ext.Loader.loadScript({
        url: scriptLoading,
        onSuccess: function() {
          scriptLoading = null;
          loadNextScript();
        }
      });
    }
  };
  if (typeof joo._loadScript !== "function") {
    joo._loadScript = function _loadScript(src/*:String*/) {
      scriptsToLoad.push(src);
      loadNextScript();
    };
  }
})();

if (typeof joo.baseUrl !== "string") {
  joo.baseUrl = (function() {
    var baseUrl = "";
    var JANGAROO_SCRIPT_PATTERN = /^(.*\/)joo\/jangaroo-.*\.js$/;
    var scripts = window.document.getElementsByTagName("SCRIPT");
    for (var i=0; i<scripts.length; ++i) {
      var match = JANGAROO_SCRIPT_PATTERN.exec(scripts[i].src);
      if (match) {
        baseUrl = match[1];
        break;
      }
    }
    return baseUrl;
  })();
}
joo.resolveUrl = function resolveUrl(url/*:String*/) {
  return !joo.baseUrl || url.match(/^(https?:\/\/|\/)/) ? url : joo.baseUrl + url
};
joo.loadScript = function loadScript(standardSrc/*:String*/, debugSrc/*:String = undefined*/) {
  var url = arguments.length > 1 && joo.debug ? debugSrc : standardSrc;
  if (url) {
    joo._loadScript(joo.resolveUrl(url));
  }
};
joo.loadDebugScript = function loadDebugScript(debugSrc/*:String*/) {
  joo.loadScript(null, debugSrc);
};
if (typeof joo.loadScriptAsync !== "function") {
  joo.loadScriptAsync = function loadScriptAsync(url) {
    Ext.Loader.loadScript(joo.resolveUrl(url));
  };
}
joo.getRelativeClassUrl = function getRelativeClassUrl(fullClassName) {
  return "joo/classes/" + fullClassName.replace(/\./g,"/") + ".js";
};
joo.loadModule = function loadModule(groupId/*:String*/, artifactId/*:String*/) {
  // TODO: reactivate?
  // joo.loadScript("joo/" + groupId + "." + artifactId + ".classes.js", null);
};
/*@cc_on
(function() {
  var styleSheetUrls = [];
  joo.loadStyleSheet = function(href) {
    styleSheetUrls.push(joo.resolveUrl(href));
    if (styleSheetUrls.length >= 31) {
      joo.flushStyleSheets();
    }
  };
  joo.flushStyleSheets = function() {
    if (styleSheetUrls.length > 0) {
      document.writeln('<style type="text/css">');
      document.writeln('<!--');
      for (var i = 0; i < styleSheetUrls.length; i++) {
        document.writeln('@import url("' + styleSheetUrls[i] + '");');
      }
      document.writeln('-->');
      document.writeln('</style>');
      styleSheetUrls.length = 0;
    }
  }
})();
@*/
if (typeof joo.loadStyleSheet !== "function") {
  joo.loadStyleSheet = function(href) {
    document.writeln('<link rel="stylesheet" type="text/css" href="' + joo.resolveUrl(href) + '" />');
  };
  joo.flushStyleSheets = function() {};
}

joo.getQualifiedObject = (function(theGlobalObject) {
  var getQualified = function (parts) {
    var object = theGlobalObject;
    for (var i = 0; i < parts.length; ++i) {
      var subObject = object[parts[i]];
      if (!subObject) {
        return null;
      }
      object = subObject;
    }
    return object;
  };
  return function(name) {
    if (!name) {
      return theGlobalObject;
    }
    var parts = name.split(".");
    return getQualified(parts) || getQualified(["AS3"].concat(parts));
  };
})(this);
joo.getOrCreatePackage = function(name) {
  return Ext.ns("AS3." + name);
};
Ext.ns("joo.localization");

Ext.Loader.setPath({
  'AS3': 'joo/classes',
  'JooOverrides': 'joo/overrides'
});

Ext.Class.registerPreprocessor('__accessors__', function (Class, data) {
  if (data.__accessors__) {
    if (data.__accessors__.statics) {
      Object.defineProperties(Class, data.__accessors__.statics);
      delete data.__accessors__.statics;
    }
    Object.defineProperties(Class.prototype, data.__accessors__);
    delete data.__accessors__;
  }
});

Ext.ClassManager.registerPostprocessor('__factory__', function(className, cls, data) {
  if (data.__factory__) {
    var value = data.__factory__();
    this.set(className, value);
    this.triggerCreated(className);
    return false;
  }
  return true;
});

(function() {
  var wrapConstructor = function(Class) {
    return function() {
      // console.log("*** called constructor of " + Ext.getClassName(Class) + " for the first time.");
      Class.__doInit__();
      return Class.prototype.constructor.apply(this, arguments);
    };
  };
  var wrapStaticMember = function(staticMemberName) {
    return {
      get: function() {
        // console.log("*** read " + Ext.getClassName(this) + "." + staticMemberName + " for the first time.");
        this.__doInit__();
        return this[staticMemberName];
      },
      set: function(value) {
        // console.log("*** set " + Ext.getClassName(this) + "." + staticMemberName + " for the first time.");
        this.__doInit__();
        this[staticMemberName] = value;
      },
      enumerable: true,
      configurable: true
    };
  };
  Ext.Class.registerPreprocessor('statics', function (Class, data) {
    var statics = data.statics;
    if (statics) {
      var initStatics = statics.__initStatics__;
      if (!initStatics) {
        Class.addStatics(statics);
      } else {
        delete statics.__initStatics__;
        var originalConstructor = data.constructor;
        data.constructor = wrapConstructor(Class);
        for (var staticMemberName in statics) {
          Object.defineProperty(Class, staticMemberName, wrapStaticMember(staticMemberName));
        }
        Class.__doInit__ = function () {
          delete this.__doInit__; // self-destruct!
          // remove all initializing interceptors:
          this.prototype.constructor = data.constructor = originalConstructor;
          for (var staticMemberName in statics) {
            delete this[staticMemberName];
          }
          // now, define the real statics:
          this.addStatics(statics);
          initStatics();
        }
      }
      delete data.statics;
    }
  });
})();

joo.loadScript("joo/AS3.js");
Ext.require("AS3.joo.DynamicClassLoader", function() {
  new AS3.joo.DynamicClassLoader();
});
