// function trace(msg : String) : void
joo.trace = function joo$trace(msg) {
  if (window["console"]) {
    window["console"].log("AS3: "+msg);
  }
};
