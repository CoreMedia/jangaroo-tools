//noinspection ThisExpressionReferencesGlobalObjectJS
(function() {
  "use strict";
  // old *.module.js API compatibility:
  if (typeof this.joo !== "object") {
    this.joo = {};
  }
  var joo = this.joo;

  if (!joo.localization) {
    joo.localization = {};
  }

  joo.runtimeApiVersion = "3.0.0";
  joo.compilerVersion = "3.0.1";
  joo.startTime = new Date().getTime();

  if (typeof joo.debug !== "boolean") {
    joo.debug = typeof this.location === "object" &&
                typeof this.location.hash === "string" &&
                !!this.location.hash.match(/(^#|&)joo.debug(=true|&|$)/);
  }

  var document = this.document;
  if (typeof joo.baseUrl !== "string") {
    joo.baseUrl = (function() {
      if (document) {
        var REQUIREJS_SCRIPT_PATTERN = /^(.*\/)requirejs\/require.*\.js$/;
        var scripts = document.getElementsByTagName("SCRIPT");
        for (var i = 0; i < scripts.length; ++i) {
          var match = REQUIREJS_SCRIPT_PATTERN.exec(scripts[i].src);
          if (match) {
            return match[1];
          }
        }
      }
      return "";
    })();
  }
  joo.resolveUrl = function resolveUrl(url/*:String*/) {
    return !joo.baseUrl || url.match(/^(https?:\/\/|\/)/) ? url : joo.baseUrl + url
  };

  var scripts = [];
  joo.loadScript = function loadScript(standardSrc/*:String*/, debugSrc/*:String = undefined*/) {
    var url = arguments.length > 1 && joo.debug ? debugSrc : standardSrc;
    if (url) {
      scripts.push(joo.resolveUrl(url));
    }
  };
  joo.loadDebugScript = function loadDebugScript(debugSrc/*:String*/) {
    joo.loadScript(null, debugSrc);
  };
  joo.getScripts = function() {
    return scripts;
  };

  joo.loadModule = function() {
    // deprecated: do nothing, automatically loaded by requiring, loadModule() no longer needed.
  };

  var styleSheets = [];
  joo.loadStyleSheet = function(styleSheet) {
    styleSheets.push(joo.resolveUrl(styleSheet));
  };
  joo.flushStyleSheets = function() {
    if (document) {
      // to workaround IE9's style sheet limit, create style sheets as @imports in chunks of 31:
      var cssLines = styleSheets.map(function(styleSheetUrl) {
        return '@import url("' + styleSheetUrl + '");';
      });
      while (cssLines.length) {
        var css = cssLines.splice(0, 31).join("");
        var style = document.createElement('style');
        style.type = 'text/css';
        if (style.styleSheet) {
          style.styleSheet.cssText = css;
        } else {
          style.appendChild(document.createTextNode(css));
        }
        document.head.appendChild(style);
      }
    }
  };
}).call(this);
define("lib/net/jangaroo/jangaroo-runtime", ["lib!lib/net/jangaroo/jangaroo-runtime.lib"], function(rtMin) {
  require(["as3/trace", "as3/joo/ResourceBundleAwareClassLoader"], function(trace, ClassLoader) {
    joo.trace = trace;
    joo.classLoader = ClassLoader._.INSTANCE;
  });
  return rtMin;
});
