define("as3/Class", [], function() {
  return {
    $class: {
      isInstance: function(o) {
        return o instanceof Function && !!o.$class;
      }
    }
  };
});
