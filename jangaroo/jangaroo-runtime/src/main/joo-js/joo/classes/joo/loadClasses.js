define(["require", "runtime/getModuleName", "runtime/retrievePrimaryDeclaration"], function(require, getModuleName, retrievePrimaryDeclaration) {
  function loadClasses(qNames, callback) {
    require(qNames.map(getModuleName), function() {
      callback(Array.prototype.map.call(arguments, retrievePrimaryDeclaration));
    });
  }
});
