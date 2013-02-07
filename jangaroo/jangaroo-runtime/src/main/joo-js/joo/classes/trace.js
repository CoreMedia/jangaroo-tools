// simulate AS3 trace()
define(["native!Array.prototype.map@runtime/es5-polyfills"], function(map) {
  var log = (function() {
    if (this.console && this.console.log) { // most browsers
      return this.console.log.bind(this.console); // take care to bind, esp. in Chrome!
    }
    if (typeof this.print === "function") { // Rhino
      return this.print;
    }
    return null;
  })();

  function trace() {
    var msg = map.call(arguments, String).join(" ");
    log(msg);
  }
  return log ? trace : function() { };
});
