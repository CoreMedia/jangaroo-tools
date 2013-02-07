define(["runtime/getModuleName", "runtime/retrievePrimaryDeclaration"], function(getModuleName, retrievePrimaryDeclaration) {
  function getNative(qName) {
    var parts = qName.split(".");
    var current = window;
    for (var i = 0; current && i < parts.length; i++) {
      current = current[parts[i]];
    }
    return current;
  }
  function getQualifiedObject(qName) {
    // try native first, then require corresponding module synchronously:
    return getNative(qName) || retrievePrimaryDeclaration(require(getModuleName(qName)));
  }
  return getQualifiedObject;
});