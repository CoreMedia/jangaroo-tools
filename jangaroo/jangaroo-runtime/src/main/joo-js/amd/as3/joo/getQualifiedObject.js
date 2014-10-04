define("as3/joo/getQualifiedObject", ["require", "as3-rt/getModuleName"], function(require, getModuleName) {
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
    return getNative(qName) || require(getModuleName(qName))._;
  }
  return getQualifiedObject;
});