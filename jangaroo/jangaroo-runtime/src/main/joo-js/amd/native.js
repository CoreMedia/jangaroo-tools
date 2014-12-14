define(["as3/joo/getOrCreatePackage"], function(getOrCreatePackage) {
  return {
    load: function (name, req, load, config) {
      'use strict';
      var parts = name.split('@');
      var variable = parts[0];
      var module = parts[1];
      if (config.isBuild) {
        if (!module || config.linkNative === false) {
          load();
        } else {
          // as it may be needed, always load module and return:
          req([module], load);
        }
      } else {
        var value = getOrCreatePackage(variable);
        if (value !== undefined || !module) {
          load(value);
        } else {
          // load module and try again to resolve global variable:
          req([module], function () {
            load(getOrCreatePackage(variable));
          });
        }
      }
    },
    normalize: function (name, normalize) {
      'use strict';
      var parts = name.split('@');
      var variable = parts[0];
      var module = parts[1];
      var normalized = module ? variable + "@" + normalize(module) : variable;
      return normalized;
    }
  };
});