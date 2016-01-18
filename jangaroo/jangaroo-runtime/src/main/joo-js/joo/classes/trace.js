// function trace(...msg) : void
Ext.define("AS3.trace", {
  singleton: true,
  constructor: function() {
    var console, defaultLogLevel;
    var lookup = function (consoleObject, traceFunctionName) {
      if (consoleObject && consoleObject[traceFunctionName]) {
        console = consoleObject;
        defaultLogLevel = traceFunctionName;
        return true;
      }
      return false;
    };
    try {
      lookup(window.console, "log") ||
      lookup(window.runtime, "trace") ||
      lookup(window, "trace") ||
      lookup(window.opera, "postError");
    } catch (e) {
      // ignore
    }
    var LOG_LEVEL_PATTERN = /^\[(LOG|DEBUG|TRACE|INFO|WARN|ERROR)]\s*(.*)$/;
    var trace = !console ? Ext.emptyFn : function () {
      // don't use Array.prototype.map, as it is not available in all browsers and has not yet been polyfilled:
      var params = [];
      for (var i = 0; i < arguments.length; i++) {
        params.push(String(arguments[i]));
      }
      var msg = params.join(" ");
      var logLevel = defaultLogLevel;
      var logLevelMatches = msg.match(LOG_LEVEL_PATTERN);
      if (logLevelMatches) {
        var specialLogLevel = logLevelMatches[1].toLowerCase();
        try {
          if (console[specialLogLevel]) {
            // special log level supported by this console:
            logLevel = specialLogLevel;
            msg = logLevelMatches[2];
          }
        } catch (e) {
          // ignore
        }
      }
      console[logLevel]("AS3: " + msg);
    };
    return trace;
  }
});
