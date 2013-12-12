define(["require", "as3-rt/getModuleName"], function(require, getModuleName) {
  "use strict";
  return function loadClasses(qNames, callback) {
    require(qNames.map(getModuleName), function() {
      callback.apply(null, Array.prototype.map.call(arguments, function(compilationUnit) {
        return compilationUnit._;
      }));
    });
  };
});
