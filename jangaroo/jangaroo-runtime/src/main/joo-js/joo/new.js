define(function() {
  var debug = typeof location === "object" &&
          typeof location.hash === "string" &&
          !!location.hash.match(/(^#|&)joo.debug(=true|&|$)/);
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
      if (debug) {
        loadAndNew();
      } else {
        req(["application"], loadAndNew);
      }
    }
  };
});
