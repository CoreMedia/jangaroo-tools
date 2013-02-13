define(function() {
  return function retrievePrimaryDeclaration(compilationUnit) {
    "use strict";
    return compilationUnit._ || compilationUnit._$get();
  }
});
