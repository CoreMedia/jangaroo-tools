// function trace(msg : String) : void
joo.trace = function joo$trace(msg) {
  msg = "AS3: " + msg;
  var console;
  if ((console = joo.getQualifiedObject("console")) && console.log) {
    console.log(msg);
  } else if ((console = joo.getQualifiedObject("runtime")) && console.trace) {
    console.trace(msg);
  } else if (console = joo.getQualifiedObject("trace")) {
    console(msg);
  } else if (console = joo.getQualifiedObject("opera")) {
    console.postError(msg);
  }
};
