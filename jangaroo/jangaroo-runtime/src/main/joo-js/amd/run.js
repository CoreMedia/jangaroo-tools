define(function() {
  "use strict";
  return {
    normalize: function (name, normalize) {
      return name;
    },

    load: function (name, req, load, config) {
      if (config.isBuild) {
        // TODO: what?
      } else {
        var parts = name.split("!");
        var bootstrapAmd = "lib/" + parts[0].replace(/[.:]/g, "/");
        var mainClassAmd = "as3/" + parts[1].replace(/[.]/g, "/");
        req([bootstrapAmd], function() {
          req([mainClassAmd], function(mainClass) {
            load(mainClass._.main(config));
          });
        });
      }
    }
  };
});
