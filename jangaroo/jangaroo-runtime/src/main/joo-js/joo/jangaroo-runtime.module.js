if (typeof joo !== "object") {
  joo = {};
}
if (typeof joo.debug !== "boolean") {
  joo.debug = typeof location === "object" &&
    typeof location.hash === "string" &&
    location.hash.match(/(^#|&)joo.debug(=true|&|$)/);
}
if (typeof joo._loadScript !== "function") {
  joo._loadScript = function _loadScript(src/*:String*/) {
    document.write('<script type="text/javascript" src="' + src + '"></script>');
  };
}
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
joo.loadScript = function loadScript(standardSrc/*:String*/, debugSrc/*:String = undefined*/) {
  var url = arguments.length > 1 && joo.debug ? debugSrc : standardSrc;
  if (url) {
    joo._loadScript(joo.baseUrl + url);
  }
};
joo.loadDebugScript = function loadDebugScript(debugSrc/*:String*/) {
  joo.loadScript(null, debugSrc);
};
if (typeof joo.loadScriptAsync !== "function") {
  joo.loadScriptAsync = function loadScriptAsync(url) {
    var script = document.createElement("script");
    script.type = "text/javascript";
    document.getElementsByTagName("HEAD")[0].appendChild(script);
    script.src = joo.baseUrl + url;
    return script;
  };
}
joo.getRelativeClassUrl = function getRelativeClassUrl(fullClassName) {
  return "joo/classes/" + fullClassName.replace(/\./g,"/") + ".js";
};
joo.loadModule = function loadModule(groupId/*:String*/, artifactId/*:String*/) {
  joo.loadScript("joo/" + groupId + "." + artifactId + ".classes.js", null);
};
joo.loadStyleSheet = function(href) {
  document.write('<link rel="stylesheet" type="text/css" href="' + joo.baseUrl + href + '" />');
};
if (!joo.debug) {
  joo.loadModule("net.jangaroo", "jangaroo-runtime");
} else {
  (function() {
    var SYSTEM_CLASSES = [
      "int",
      "uint",
      "joo.BootstrapClassLoader",
      "joo.assert",
      "joo.Class",
      "joo.trace",
      "joo.MemberDeclaration",
      "joo.NativeClassDeclaration",
      "joo.JooClassDeclaration",
      "joo.SystemClassLoader",
      "ArgumentError",
      "DefinitionError",
      "SecurityError",
      "Array",
      "Vector",
      "Date",
      "joo.StandardClassLoader",
      "joo.DynamicClassLoader",
      "joo.ResourceBundleAwareClassLoader",
      "joo._createClassLoader"
    ];
    for (var c=0; c<SYSTEM_CLASSES.length; ++c) {
      joo.loadScript(joo.getRelativeClassUrl(SYSTEM_CLASSES[c]));
    }
  })();
}