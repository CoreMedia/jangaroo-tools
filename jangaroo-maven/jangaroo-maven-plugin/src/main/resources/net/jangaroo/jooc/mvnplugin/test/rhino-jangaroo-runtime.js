//
// Rhino patch for jangaroo-runtime
//


joo = {};
joo.debug = false;
joo.baseUrl = '';
joo._loadScript = function _loadScript(src/*:String*/) {mojoScriptImporter.importScript(src, false);};
joo.loadScriptAsync = function _loadScript(src/*:String*/) {mojoScriptImporter.importScript(src, true);};
joo.trace = function(m1,m2,m3,m4) {

  if( m4 !== undefined ) {
    mojoConsole.log(m1,m2,m3,m4);
  }
  else if( m3 !== undefined ) {
    mojoConsole.log(m1,m2,m3);
  }
  else if( m2 !== undefined ) {
    mojoConsole.log(m1,m2);
  }
  else {
    mojoConsole.log(m1);
  }
};

