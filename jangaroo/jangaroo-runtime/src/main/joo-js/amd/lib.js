define(function() {
  "use strict";
  var debug = "false";
  if (typeof location === "object" && typeof location.hash === "string") {
    var match = location.hash.match(/(?:^#|&)joo.debug(?:=(true|false)|&|$)/);
    if (match) {
      debug = match[1] || "true";
    }
  }

  return {
    normalize: function (name, normalize) {
      return name;
    },

    load: function (name, req, load, config) {
      if (!config.isBuild && debug === "false") {
        // TODO: allow different debug level settings per module name (pattern)!
        req([name], load);
      } else {
        load(null);
      }
    }
  };
});
