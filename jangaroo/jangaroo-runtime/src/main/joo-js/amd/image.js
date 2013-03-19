define(function () {

  function empty() { }

  return {
    load: function (name, req, load, config) {
      if (config.isBuild) {
        load(null);
      } else {
        var image = new Image();
        image.onerror = function (err) {
          load.error(err);
        };
        image.onload = function (evt) {
          load(image);
          try {
            delete image.onload;
          } catch (err) {
            image.onload = empty;
          }
        };
        // always interpret image URL as relative to baseUrl:
        image.src = req.toUrl(name);
      }
    }
  };

});
