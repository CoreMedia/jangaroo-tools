define(["require", "as3-rt/getModuleName"], function(require, getModuleName) {
  "use strict";
  return function loadClass(qName, callback) {
    if (!callback) {
      var compilationUnit = require(getModuleName(qName));
      return compilationUnit._;
    }
    require([getModuleName(qName)], function(compilationUnit) {
      callback(compilationUnit._);
    });
    return null;
  };
});
