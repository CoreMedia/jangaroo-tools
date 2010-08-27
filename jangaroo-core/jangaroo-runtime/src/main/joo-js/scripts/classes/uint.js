this['uint'] = function $uint(num) {
  var result = new Number(Math.abs(Math.floor(num)));
  result.constructor = $uint;
  return result;
};
