define(["require", "runtime/getModuleName", "runtime/retrievePrimaryDeclaration"], function(require, getModuleName, retrievePrimaryDeclaration) {
  function loadClass(qName, callback) {
    require([getModuleName(qName)], function(clazz) {
      callback(retrievePrimaryDeclaration(clazz));
    });
  }
});
