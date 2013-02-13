define(function() {
  return function getModuleName(qName) {
    return "as3/" + qName.replace(/\./g, "/");
  }
});
