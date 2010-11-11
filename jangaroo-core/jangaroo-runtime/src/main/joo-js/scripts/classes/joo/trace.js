// function trace(msg : String) : void
joo.trace = function joo$trace(msg) {
  var logLevelMatches = String(msg).match(/^\[([A-Z]+)\]\s*(.*)$/);
  var logLevel = logLevelMatches ? logLevelMatches[1].toLowerCase() : null;
  msg = "AS3: " + msg;
  var console;
  if ((console = joo.getQualifiedObject("console")) && console.log) {
    // Firebug supports different log levels:
    if (!console[logLevel]) {
      logLevel = 'log';
    } else {
      msg = "AS3: " + logLevelMatches[2];
    }
    console[logLevel](msg);
  } else if ((console = joo.getQualifiedObject("runtime")) && console.trace) {
    console.trace(msg);
  } else if (console = joo.getQualifiedObject("trace")) {
    console(msg);
  } else if (console = joo.getQualifiedObject("opera")) {
    console.postError(msg);
  }
};
