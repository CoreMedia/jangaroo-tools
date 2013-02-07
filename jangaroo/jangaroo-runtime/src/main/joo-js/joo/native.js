define(function() {
  function global(name) {
    var ref = this; // global object
    var props = name.split('.');
    var pl = props.length;
    try {
      for (var i = 0; ref && i < pl; i++) {
        ref = ref[props[i]];
      }
      return ref;
    } catch(e) {
      // ignore
    }
    return undefined;
  }
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
        var value = global(variable);
        if (value !== undefined || !module) {
          load(value);
        } else {
          // load module and try again to resolve global variable:
          req([module], function () {
            load(global(variable));
          });
        }
      }
    }
  };
});