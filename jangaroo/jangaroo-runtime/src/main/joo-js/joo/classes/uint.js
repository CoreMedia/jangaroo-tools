(function(theGlobalObject) {
  theGlobalObject['uint'] = theGlobalObject['$$uint'] = function $$uint(num) {
    return num >>> 0;
  };
  theGlobalObject['uint'].MAX_VALUE = 4294967295;
  theGlobalObject['uint'].MIN_VALUE = 0;
})(AS3);
