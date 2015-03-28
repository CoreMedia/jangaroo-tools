// simulate AS3 trace()
define("as3/trace", ["as3/joo/getQualifiedObject"], function(getQualifiedObject) {
  "use strict";
  var joo = getQualifiedObject("joo");
  if (joo && joo.trace) {
    return joo.trace;
  }

  var console = getQualifiedObject("console");

  if (!console || !console.log) {
    return function() {};
  }

  var LOG_LEVELS = ["DEBUG", "TRACE", "INFO", "WARN", "ERROR"];
  var LOG_LEVEL_PATTERN = new RegExp("^\\[(LOG|" + LOG_LEVELS.join("|") + ")\\]\\s*(.*)$");

  // polyfill any console.x() methods that are not present:
  LOG_LEVELS.forEach(function(logLevel) {
    var methodName = logLevel.toLowerCase();
    if (!console[methodName]) {
      console[methodName] = console.log;
    }
  });

  return function() {
    var logLevel = "log";
    var args = Array.prototype.slice.call(arguments);
    var logLevelMatches = typeof args[0] === "string" && args[0].match(LOG_LEVEL_PATTERN);
    if (logLevelMatches) {
      logLevel = logLevelMatches[1].toLowerCase();
      if (logLevelMatches[2]) {
        args[0] = logLevelMatches[2];
      } else {
        args.shift();
      }
    }
    // as console's methods are not "real" functions in all browsers, they have to be applied strangely:
    Function.prototype.apply.call(console[logLevel], console, args);
  };
});
