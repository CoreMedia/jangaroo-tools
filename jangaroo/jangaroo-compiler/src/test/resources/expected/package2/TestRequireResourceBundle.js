Ext.define("package2.TestRequireResourceBundle", function(TestRequireResourceBundle) {/*package package2 {

[ResourceBundle("package2.ResourceBundle")]
public class TestRequireResourceBundle {
}*/function TestRequireResourceBundle$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: TestRequireResourceBundle$,
      requires: ["package2.ResourceBundle_properties"]
    };
});
