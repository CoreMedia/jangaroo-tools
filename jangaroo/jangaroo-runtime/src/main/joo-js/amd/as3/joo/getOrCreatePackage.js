//noinspection ThisExpressionReferencesGlobalObjectJS
theGlobalObject = this;
define("as3/joo/getOrCreatePackage", [], function() {
  return function getOrCreatePackage(qName) {
    var parts = qName ? qName.split('.') : [];
    var current = theGlobalObject;
    for (var i = 0; i < parts.length; i++) {
      current = current[parts[i]] || (current[parts[i]] = {});
    }
    return current;
  };
});