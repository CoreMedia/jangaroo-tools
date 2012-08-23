(function(theGlobalObject) {
  theGlobalObject['uint'] = theGlobalObject['$$uint'] = function $$uint(num) {
    return Number(num) >>> 0;
  };
  theGlobalObject['uint'].MAX_VALUE = Math.pow(2, 32);
  theGlobalObject['uint'].MIN_VALUE = 0;
})(this);
