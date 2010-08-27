this['int'] = function $int(num) {
  var result = new Number(Math.floor(num));
  result.constructor = $int;
  return result;
};
this['int'].MAX_VALUE =  2147483647;
this['int'].MIN_VALUE = -2147483648;
