/*package package1 {*/

/**
 * Retest for JOO-64.
 */
Ext.define("package1.StaticAndNonStatic", function(StaticAndNonStatic) {/*public class StaticAndNonStatic {*/function static$0(){

  new StaticAndNonStatic();}/*

  public var StaticAndNonStatic:String;*/function static$1(){
  
  new StaticAndNonStatic();}/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      StaticAndNonStatic: null,
      statics: {__initStatics__: function() {
          static$0();
          static$1();
        }}
    };
});
