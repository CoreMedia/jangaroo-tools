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
      function loadAndNew() {
        req([name], function (value) {
          // don't use retrievePrimaryDeclaration here, as "new!" is used for bootstrap and we don't want to
          // load retrievePrimaryDeclaration.js as a separate request.
          load(new (value._ || value._$get())(config.config));
        });
      }
      if (config.isBuild) {
        loadAndNew();
      } else {
        switch (debug) {
          case "true":   loadAndNew(); break;
          case "linked": req(["application"], loadAndNew); break;
          case "false":  req(["application-min"], loadAndNew); break;
        }
      }
    }
  };
});
