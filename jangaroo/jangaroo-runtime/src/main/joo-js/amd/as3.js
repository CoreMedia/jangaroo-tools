define(function() {
  "use strict";
  return {
    normalize: function (name, normalize) {
      return name.match(/^as3\//) ? name : "as3/" + name.replace(/\./g, "/");
    },

    load: function (name, req, load, config) {
      req([name], function (value) {
        load(value._);
      });
    }
  };
});
