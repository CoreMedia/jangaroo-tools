theGlobalObject = this;
define("lib/net/jangaroo/jangaroo-runtime", ["require", "as3-rt/getModuleName", "lib!lib/net/jangaroo/jangaroo-runtime.lib"], function(require, getModuleName, rtMin) {
  // old *.module.js API compatibility:
  var scripts = [];
  var styleSheets = [];
  var joo = theGlobalObject.joo = theGlobalObject.joo || {};
  joo.debug = false;
  if (typeof theGlobalObject.location === "object" && typeof theGlobalObject.location.hash === "string") {
    var match = theGlobalObject.location.hash.match(/(?:^#|&)joo.debug(?:=(true|false)|&|$)/);
    if (match) {
      joo.debug = !match[1] || match[1] === "true";
    }
  }
  joo.loadModule = function() {
    console.log("deprecated: loadModule() no longer needed.");
  };
  joo.loadScript = function(script, debugScript) {
    if (this.debug && debugScript) {
      scripts.push(debugScript);
    } else if (script) {
      scripts.push(script);
    }
  };
  joo.loadDebugScript = function(script) {
    if (this.debug && script) {
      scripts.push(script);
    }
  };
  joo.loadStyleSheet = function(styleSheet) {
    styleSheets.push(styleSheet);
  };
  joo.classLoader = {
    import_: function() {
      require(Array.prototype.map.call(arguments, getModuleName));
    },
    getClassDeclaration: function(qName) {
      return require(getModuleName(qName))._.$class;
    }
  };
  joo.addStyleSheets = function() {
    for (var i = 0; i < styleSheets.length; i++) {
      var link = document.createElement("link");
      link.setAttribute("rel", "stylesheet");
      link.setAttribute("type", "text/css");
      link.setAttribute("href", styleSheets[i]);
      document.head.appendChild(link);
    }
  };
  joo.getScripts = function() {
    return scripts;
  };
  return rtMin;
});
