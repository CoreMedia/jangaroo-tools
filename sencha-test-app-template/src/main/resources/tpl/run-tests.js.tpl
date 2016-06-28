Ext['USE_NATIVE_JSON'] = true;
Ext.application({
  name: '{moduleName}',

  requires: [
    'net.jangaroo.joounit.runner.BrowserRunner',
    '{testSuite}'
  ],

  launch: function() {
    net.jangaroo.joounit.runner.BrowserRunner.main(
            {testSuite}
    );
  }
});