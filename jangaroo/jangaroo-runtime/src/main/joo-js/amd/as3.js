define(function() {
  var debug = "false";
  if (typeof location === "object" && typeof location.hash === "string") {
    var match = location.hash.match(/(?:^#|&)joo.debug(?:=(true|false|linked)|&|$)/);
    if (match) {
      debug = match[1] || "true";
    }
  }
  "use strict";
  return {
    normalize: function (name, normalize) {
      return name.match(/^as3\//) ? name : "as3/" + name.replace(/\./g, "/");
    },

    load: function (name, req, load, config) {
      function loadAndInit() {
        req([name], function (value) {
          load(value._);
        });
      }
      if (config.isBuild) {
        loadAndInit();
      } else {
        switch (debug) {
          case "true":   loadAndInit(); break;
          case "linked": req(["application"], loadAndInit); break;
          case "false":  req(["application-min"], loadAndInit); break;
        }
      }
    }
  };
});
