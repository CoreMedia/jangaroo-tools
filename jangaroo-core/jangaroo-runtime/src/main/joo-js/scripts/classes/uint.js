(function(theGlobalObject) {
  theGlobalObject['uint'] = theGlobalObject['$$uint'] = function $uint(num) {
    var ui = Math.abs(Math.floor(num));
    if (this===theGlobalObject) {
      return ui;
    }
    var result = new Number(ui);
    result.constructor = $uint;
    return result;
  };
  theGlobalObject['uint'].MAX_VALUE =  4294967295;
  theGlobalObject['uint'].MIN_VALUE =  0;
})(this);
