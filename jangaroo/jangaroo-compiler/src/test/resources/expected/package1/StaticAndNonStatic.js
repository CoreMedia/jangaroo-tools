Ext.define("package1.StaticAndNonStatic", function(StaticAndNonStatic) {/*package package1 {

/**
 * Retest for JOO-64.
 * /
public class StaticAndNonStatic {

  public var StaticAndNonStatic:String;*/function static$0(){
  
  new StaticAndNonStatic();}/*
}*/function StaticAndNonStatic$() {}/*
}

============================================== Jangaroo part ==============================================*/
    return {
      StaticAndNonStatic: null,
      constructor: StaticAndNonStatic$
    };
}, function() {
    static$0();
});
