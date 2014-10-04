define("as3-rt/getModuleName", [], function() {
  return function getModuleName(qName) {
    return "as3/" + qName.replace(/\./g, "/");
  }
});
