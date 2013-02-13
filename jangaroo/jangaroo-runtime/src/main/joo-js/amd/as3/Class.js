define(function() {
  return {
    $class: {
      isInstance: function(o) {
        return o instanceof Function && !!o.$class;
      }
    }
  };
});
