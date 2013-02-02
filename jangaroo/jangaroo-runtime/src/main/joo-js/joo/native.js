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
      if (config.isBuild) {
        load();
      } else {
        var parts = name.split('@');
        var variable = parts[0];
        var value = global(variable);
        if (value !== undefined || parts.length === 1) {
          load(value);
        } else {
          // load module and try again to resolve global variable:
          req([parts[1]], function () {
            load(global(variable));
          });
        }
      }
    }
  };
});