(function(theGlobalObject) {
  var MAX_VALUE =  4294967295;
  theGlobalObject['uint'] = theGlobalObject['$$uint'] = function $uint(num) {
    num = Number(num);
    if (num > 0) {
      return Math.floor(num);
    }
    if (num <= -1) {
      return MAX_VALUE + Math.ceil(num) + 1;
    }
    // also handles NaN
    return 0;
  };
  theGlobalObject['uint'].MAX_VALUE =  MAX_VALUE;
  theGlobalObject['uint'].MIN_VALUE =  0;
})(this);
