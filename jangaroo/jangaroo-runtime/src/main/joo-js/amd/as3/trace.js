// simulate AS3 trace()
define("as3/trace", ["native!"], function(global) {
  "use strict";
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
  }).call(global);

  function trace() {
    var msg = Array.prototype.map.call(arguments, String).join(" ");
    log(msg);
  }
  return log ? trace : function() { };
});
