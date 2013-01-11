define({
  _: function(config) {
    if (config) {
      for (var m in config) {
        this[m] = config[m];
      }
    }
  }
});
