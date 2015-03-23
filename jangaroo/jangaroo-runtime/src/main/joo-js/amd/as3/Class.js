define("as3/Class", [], function() {
  return {
    $class: {
      isInstance: function(o) {
        // There is no real chance to tell whether a function is actually a Class (constructor).
        return o instanceof Function;
      }
    }
  };
});
