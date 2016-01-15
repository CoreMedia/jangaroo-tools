// built-in as well as Ext Error constructor called as function unfortunately always create a new Error object,
// so we have to create a class that uses the original Error prototype, but also works as an Ext class:
AS3.Error = function(message/*String*/, id/*:int*/) {
  this.message = message || "";
  this.id = id || 0;
};
AS3.Error.$isClass = true;
AS3.Error.$className = "AS3.Error";
AS3.Error.__isInstance__ = function(object) {
  // use built-in Error class for instanceof check:
  return object instanceof Error;
};
AS3.Error.superclass = Error.prototype;
AS3.Error.prototype = Object.create(Error.prototype);
AS3.Error.prototype.self = AS3.Error;
AS3.Error.prototype.constructor = AS3.Error;
AS3.Error.prototype.callParent = Ext.Base.prototype.callParent;

Ext.ClassManager.triggerCreated("AS3.Error");
