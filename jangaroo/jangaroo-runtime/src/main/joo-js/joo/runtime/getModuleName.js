define(function() {
  return function getModuleName(qName) {
    return "classes/" + qName.replace(/\./g, "/");
  }
});
