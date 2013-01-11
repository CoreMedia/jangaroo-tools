// simulate AS3 trace()
define(function() {
  "use strict";
  
  function trace() {
    var msg = Array.prototype.map.call(arguments, String).join(" ");
    //var logWindow = document.createElement("div");
    //logWindow.appendChild(document.createTextNode(msg));
    //document.body.appendChild(logWindow);
    console.log(msg);
  }
  return { _: trace };
});
