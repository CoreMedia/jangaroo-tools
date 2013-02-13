define(["require", "as3-rt/getModuleName", "as3-rt/retrievePrimaryDeclaration"], function(require, getModuleName, retrievePrimaryDeclaration) {
  "use strict";
  return function loadClasses(qNames, callback) {
    require(qNames.map(getModuleName), function() {
      callback(Array.prototype.map.call(arguments, retrievePrimaryDeclaration));
    });
  };
});
