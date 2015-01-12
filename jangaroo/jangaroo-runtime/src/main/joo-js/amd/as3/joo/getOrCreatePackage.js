define("as3/joo/getOrCreatePackage", [], function() {
  var theGlobalObject = this; // no strict mode!
  return function getOrCreatePackage(qName) {
    "use strict";
    var parts = qName ? qName.split('.') : [];
    var current = theGlobalObject;
    for (var i = 0; i < parts.length; i++) {
      current = current[parts[i]] || (current[parts[i]] = {});
    }
    return current;
  };
});