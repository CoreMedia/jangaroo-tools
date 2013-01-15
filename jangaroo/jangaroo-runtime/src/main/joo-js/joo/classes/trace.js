// simulate AS3 trace()
define(function() {
  var log = (function() {
    if (this.console && this.console.log) { // most browsers
      return this.console.log.bind(this.console); // take care to bind, esp. in Chrome!
    }
    if (this.print) { // Rhino
      return this.print;
    }
    return null;
  })();

  function trace() {
    var msg = Array.prototype.map.call(arguments, String).join(" ");
    log(msg);
  }
  return { _: log ? trace : function() { } };
});
