//noinspection ThisExpressionReferencesGlobalObjectJS
theGlobalObject = this;
define("as3/joo/getQualifiedObject", [], function() {
  return function getQualifiedObject(qName) {
    var parts = qName ? qName.split('.') : [];
    var current = theGlobalObject;
    for (var i = 0; current && i < parts.length; i++) {
      current = current[parts[i]];
    }
    return current;
  };
});