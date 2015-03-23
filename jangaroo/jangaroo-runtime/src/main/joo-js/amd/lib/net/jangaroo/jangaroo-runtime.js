//noinspection ThisExpressionReferencesGlobalObjectJS
(function() {
  // old *.module.js API compatibility:
  var scripts = [];
  var styleSheets = [];
  var joo = this.joo = this.joo || {};
  joo.debug = false;
  if (typeof this.location === "object" && typeof this.location.hash === "string") {
    var match = this.location.hash.match(/(?:^#|&)joo.debug(?:=(true|false)|&|$)/);
    if (match) {
      joo.debug = !match[1] || match[1] === "true";
    }
  }
  joo.runtimeApiVersion = "3.0.0";
  joo.compilerVersion = "3.0.0";
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
}).call(this);
define("lib/net/jangaroo/jangaroo-runtime", ["lib!lib/net/jangaroo/jangaroo-runtime.lib"], function(rtMin) {
  require(["as3/joo/ResourceBundleAwareClassLoader"], function(ClassLoader) {
    joo.classLoader = new ClassLoader._();
  });
  return rtMin;
});
