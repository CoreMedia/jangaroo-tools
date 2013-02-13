// simulate AS3 trace()
define(["native!Array.prototype.map@as3-rt/es5-polyfills"], function(map) {
  var log = (function() {
    if (this.console && this.console.log) { // most browsers
      if (this.console.log.bind) {
        // take care to bind if possible, esp. in Chrome!
        return this.console.log.bind(this.console);
      } else {
        // in IE, console.log() is not a "real" function and thus cannot be bound:
        return this.console.log;
      }
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
