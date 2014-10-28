theGlobalObject = this;
define("lib/net/jangaroo/jangaroo-runtime", ["require", "as3-rt/getModuleName", "lib!lib/net/jangaroo/jangaroo-runtime.lib"], function(require, getModuleName, rtMin) {
  // old *.module.js API compatibility:
  var scripts = [];
  var styleSheets = [];
  var joo = theGlobalObject.joo = theGlobalObject.joo || {};
  joo.loadModule = function() {
    console.log("deprecated: loadModule() no longer needed.");
  };
  joo.loadScript = function(script) {
    if (script) {
      scripts.push(script);
    }
  };
  joo.loadDebugScript = function() {
    // TODO: implement debug scripts!
  };
  joo.loadStyleSheet = function(styleSheet) {
    styleSheets.push(styleSheet);
  };
  joo.classLoader = {
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
