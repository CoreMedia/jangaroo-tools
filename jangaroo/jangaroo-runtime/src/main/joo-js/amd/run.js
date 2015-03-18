define(["as3-rt/getModuleName", "lib/net/jangaroo/jangaroo-runtime"], function(getModuleName) {
  "use strict";

  // load non-AMD scripts added through joo.loadScript() sequentially, then call callback:
  function loadScripts(req, callback) {
    var scripts = joo.getScripts();
    loadScript();
    function loadScript() {
      if (scripts.length) {
        req([scripts.shift()], loadScript);
      } else {
        callback();
      }
    }
  }
  return {
    normalize: function (name, normalize) {
      return name;
    },

    load: function (name, req, load, config) {
      if (config.isBuild) {
        // TODO: what?
      } else {
        var parts = name.split("!");
        var bootstrapAmd = "jangaroo-application";
        if (parts.length > 1) {
          bootstrapAmd = parts.shift();
          if (bootstrapAmd.indexOf(":") !== -1) {
            bootstrapAmd = "lib/" + bootstrapAmd.replace(/[.:]/g, "/");
          }
        }
        var mainClassName = parts.shift();
        var mainArgs = config.args;
        req([bootstrapAmd, "domReady!"], function() {
          joo.addStyleSheets(); // link CSS added through joo.loadStyleSheet()
          loadScripts(req, function() {
            if (mainClassName) {
              req([getModuleName(mainClassName)], function(mainClass) {
                load(mainClass._.main(mainArgs));
              });
            } else {
              load(null);
            }
          });
        });
      }
    }
  };
});
