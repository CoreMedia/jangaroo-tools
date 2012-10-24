//
// Minimal window emulation for rhino test execution
//


setTimeout = function (fn,delay) {
  return mojoTimer.schedule(new JavaAdapter(java.lang.Runnable,{run: fn}), delay, false);
};

setInterval = function (fn,delay) {
  return mojoTimer.schedule(new JavaAdapter(java.lang.Runnable,{run: fn}), delay, true);
};

clearTimeout = function (id) {
  mojoTimer.cancel(id);
};

clearInterval = function (id) {
  mojoTimer.cancel(id);
};

// ----- simulation of window
window =  {};
window.setTimeout = function(fn, delay) {return setTimeout(fn, delay);};
window.setInterval = function(fn, delay) {return setInterval(fn, delay);};
window.clearTimeout = function(id) {clearTimeout(id);};
window.clearInterval = function(id) {clearInterval(id);};