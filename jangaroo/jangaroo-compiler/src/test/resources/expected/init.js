/*package {
import package1.someOtherPackage.SomeEvent;
import package1.someOtherPackage.SomeOtherClass;*/

Ext.define("init", function(init) {/*public*/ function init()/*:void*/ {
   new package1.someOtherPackage.SomeOtherClass().addEventListener(package1.someOtherPackage.SomeEvent.CLICK_CLACK, function (event/*: SomeEvent*/)/*:void*/ {/*
     const*/var title/*: String*/ = event.source.title;
   });
}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      __factory__: function() {
        return init;
      },
      requires: [
        "package1.someOtherPackage.SomeEvent",
        "package1.someOtherPackage.SomeOtherClass"
      ]
    };
});
