var system = require('system');
var fs = require("fs");
var page = require("webpage").create();

// extract command line args
if (system.args.length !== 4) {
  console.info("USAGE: phantomjs-joounit-page-runner.js <page-url> <test-result-filename> <timeout>, instead found " + (system.args.length - 1) + " arguments.");
  phantom.exit(1);
}

var testUrl = system.args[1];
var resultFile = system.args[2];
/*
 http://phantomjs.org/api/webpage/property/settings.html
 (in milli-secs) defines the timeout after which any resource requested will stop trying and proceed with other parts of the page. onResourceTimeout callback will be called on timeout.
 */
page.settings.resourceTimeout = args[3];

page.onResourceRequested = function(request) {
  console.log("Request #" + request.id + ": " + request.method + " " + request.url);
};
page.onResourceReceived = function(response) {
  console.log("Response #" + response.id + " (" + response.stage + "): " + response.url + ": " + response.statusText);
};
page.onResourceTimeout = function(request) {
  console.error('Response timeout (#' + request.id + '): ' + JSON.stringify(request));
  page.close();
  phantom.exit(42);
};
page.onInitialized = function() {
  page.evaluate(function() {
    function wrapLogMethod(logMethodName) {
      var originalLogMethod = window.console[logMethodName];
      if (typeof originalLogMethod === "function") {
        window.console[logMethodName] = function() {
          var newArgs = Array.prototype.slice.call(arguments);
          newArgs.splice(0, 0, "[" + logMethodName.toUpperCase() + "]");
          return originalLogMethod.apply(window.console, newArgs);
        };
      }
    }
    for (var member in window.console) {
      wrapLogMethod(member);
    }
    window.joo = window.joo || {};
    window.joo.trace = function trace() {
      var args = Array.prototype.slice.call(arguments);
      var logMethod;
      if (typeof args[0] === "string") {
        var match = args[0].match(/^\[([A-Z]+)\]$/);
        if (match) {
          logMethod = match[1].toLowerCase();
          args.splice(0, 1);
        }
      }
      if (!logMethod || !window.console[logMethod]) {
        logMethod = "log";
      }
      window.console[logMethod].apply(window.console, arguments);
    }
  });
};
page.onConsoleMessage = function(msg, line, source) {
  var output = msg;
  if (source) {
    output += " " + source;
  }
  if (line) {
    output += ":" + line;
  }
  console.log(output);
};
page.onError = function(e, stack) {
  console.error("[ERROR]", e, "(" + stack.length + " stack frames)");
  stack.forEach(function(item, i) {
    console.error("[ERROR]", (stack.length - i) + ".", item.file, ':', item.line, item.function ? ' (in function "' + item.function + '")' : '');
  });
};
page.onCallback = function(result) {
  console.log("RESULT:\n" + result);
  fs.write(resultFile, result, "w");
  page.close();
  phantom.exit(0);
};

console.log("opening " + testUrl);
page.open(testUrl, {}, function(status) {
  console.log("Test url status: " + status);
  if (status !== "success") {
    page.close();
    phantom.exit(3);
  }
});
