Ext.ns("joo");
// built-in as well as Ext Error constructor called as function unfortunately always create a new Error object,
// so we have to create a class that uses the original Error prototype, but also works as an Ext class:
joo.Error = function(message/*String*/, id/*:int*/) {
  this.message = message || "";
  this.id = id || 0;
};
joo.Error.$isClass = true;
joo.Error.$className = "joo.Error";
joo.Error.superclass = Error.prototype;
joo.Error.prototype = Object.create(Error.prototype);
joo.Error.prototype.self = joo.Error;
joo.Error.prototype.constructor = joo.Error;
joo.Error.prototype.callParent = Ext.Base.prototype.callParent;

Ext.ClassManager.triggerCreated("joo.Error");
