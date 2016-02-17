Ext.define('JooOverrides.Ext.ClassManager', {
  override: 'Ext.ClassManager'
}, function() {
  Ext.ClassManager.registerPostprocessor('__factory__', function(className, cls, data) {
    if (data.__factory__) {
      var value = data.__factory__();
      this.set(className, value);
      this.triggerCreated(className);
      return false;
    }
    return true;
  });
});
