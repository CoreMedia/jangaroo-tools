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
      return name.match(/^classes\//) ? name : "classes/" + name.replace(/\./g, "/");
    },

    load: function (name, req, load, config) {
      function loadAndNew() {
        req([name], function (value) {
          load(new value._(config.config));
        });
      }
      switch (debug) {
        case "true":   loadAndNew(); break;
        case "linked": req(["application"], loadAndNew); break;
        case "false":  req(["application-min"], loadAndNew); break;
      }
    }
  };
});
