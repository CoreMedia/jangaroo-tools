Ext.define("package1.StaticAndNonStatic", function(StaticAndNonStatic) {/*package package1 {

/**
 * Retest for JOO-64.
 * /
public class StaticAndNonStatic {

  public var StaticAndNonStatic:String;*/function static$0(){
  
  new StaticAndNonStatic();}/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      StaticAndNonStatic: null,
      statics: {__initStatics__: function() {
          static$0();
        }}
    };
});
