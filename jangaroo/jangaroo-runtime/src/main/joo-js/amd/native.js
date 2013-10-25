define(function() {
  var theGlobalObject = this;
  function global(names) {
    'use strict';
    for (var j = 0; j < names.length; ++j) {
      var name = names[j];
      var ref = theGlobalObject;
      var props = name ? name.split('.') : [];
      var pl = props.length;
      try {
        for (var i = 0; ref && i < pl; i++) {
          ref = ref[props[i]];
        }
        if (ref !== undefined) {
          return ref;
        }
      } catch(e) {
        // ignore
      }
    }
    return undefined;
  }
  return {
    load: function (name, req, load, config) {
      'use strict';
      var parts = name.split('@');
      var variables = parts[0].split('|');
      var module = parts[1];
      if (config.isBuild) {
        if (!module || config.linkNative === false) {
          load();
        } else {
          // as it may be needed, always load module and return:
          req([module], load);
        }
      } else {
        var value = global(variables);
        if (value !== undefined || !module) {
          load(value);
        } else {
          // load module and try again to resolve global variable:
          req([module], function () {
            load(global(variables));
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