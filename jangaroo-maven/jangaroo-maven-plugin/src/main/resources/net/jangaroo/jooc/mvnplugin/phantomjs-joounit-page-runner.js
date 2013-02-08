var system = require('system');
var fs = require("fs");
var page = require("webpage").create();
if (system.args.length !== 4) {
  console.info("USAGE: phantomjs-joounit-page-runner.js <page-url> <test-result-filename> <timeout>, instead found " + (system.args.length - 1) + " arguments.");
  phantom.exit(1);
}
page.onResourceRequested = function(request) {
  console.log("Request #" + request.id + ": " + request.method + " " + request.url);
};
page.onResourceReceived = function(response) {
  console.log("Response #" + response.id + " (" + response.stage + "): " + response.url + ": " + response.statusText);
};
page.onError = function(e, stack) {
  console.error("ERROR:" + e);
  console.debug(stack.join("\n"));
  phantom.exit(2);
};
page.onCallback = function(result) {
  console.log("RESULT:\n" + result);
  fs.write(system.args[2], result, "w");  
  phantom.exit(0);
};
page.open(system.args[1], function(status) {
  if (status !== "success") {
    phantom.exit(3);
  }
  window.setTimeout(function() {
    if (page.evaluate(function() {return typeof(window["result"]) === "undefined";})) {
       console.error("phantomjs> timeout, no result!");
       phantom.exit(4);
    }
  }, system.args[3]);
});
