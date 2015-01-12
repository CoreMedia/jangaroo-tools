//noinspection ThisExpressionReferencesGlobalObjectJS
define("as3/joo/getQualifiedObject", [], function() {
  var theGlobalObject = this; // no strict mode!
  return function getQualifiedObject(qName) {
    "use strict";
    var parts = qName ? qName.split('.') : [];
    var current = theGlobalObject;
    for (var i = 0; current && i < parts.length; i++) {
      current = current[parts[i]];
    }
    return current;
  };
});