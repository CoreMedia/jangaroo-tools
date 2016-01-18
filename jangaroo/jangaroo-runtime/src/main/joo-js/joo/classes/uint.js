(function(theGlobalObject) {
  theGlobalObject['uint'] = theGlobalObject['$$uint'] = function $$uint(num) {
    return num >>> 0;
  };
  theGlobalObject['uint'].MAX_VALUE = 4294967295;
  theGlobalObject['uint'].MIN_VALUE = 0;
  theGlobalObject['uint'].$className = "AS3.uint";
  theGlobalObject['uint'].__isInstance__ = function(object) {
    return (object instanceof Number || typeof object === 'number') &&
            object >>> 0 === object + 0; // "+ 0" converts Number to number!
  };
})(AS3);
