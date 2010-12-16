joo = {
  debug: typeof location === "object" &&
    typeof location.hash === "string" &&
    location.hash.match(/(^#|&)joo.debug(&|$)/),
  /**
   * (s) -> always load s
   * (s, ds) -> load s if standard; load ds if debug
   */
  loadScript: function loadScript(standardSrc/*:String*/, debugSrc/*:String = undefined*/) {
    var url = arguments.length > 1 && joo.debug ? debugSrc : standardSrc;
    if (url) {
      document.write('<script type="text/javascript" src="' + url + '"></script>');
    }
  },
  loadDebugScript: function loadDebugScript(debugSrc/*:String*/) {
    joo.loadScript(null, debugSrc);
  },
  scriptUrlPrefix: "scripts/",
  loadModule: function loadModule(moduleName/*:String*/) {
    joo.loadScript(joo.scriptUrlPrefix + moduleName + ".js", null);
  }
};
joo.loadScript("scripts/jangaroo-runtime.js", "scripts/jangaroo-runtime-debug-classes.js");