define(["require", "runtime/getModuleName", "runtime/retrievePrimaryDeclaration"], function(require, getModuleName, retrievePrimaryDeclaration) {
  "use strict";
  return function loadClass(qName, callback) {
    if (!callback) {
      var compilationUnit = require(getModuleName(qName));
      return retrievePrimaryDeclaration(compilationUnit);
    }
    require([getModuleName(qName)], function(compilationUnit) {
      callback(retrievePrimaryDeclaration(compilationUnit));
    });
    return null;
  };
});
