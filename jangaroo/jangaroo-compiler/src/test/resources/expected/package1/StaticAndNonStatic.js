define("as3/package1/StaticAndNonStatic",["module","exports","as3-rt/AS3"], function($module,$exports,AS3) { AS3.compilationUnit($module,$exports,function($primaryDeclaration){/*package package1 {

/**
 * Retest for JOO-64.
 * /
public class StaticAndNonStatic {

  public var StaticAndNonStatic:String;*/function static$0(){
  
  new StaticAndNonStatic();}/*
}*/function StaticAndNonStatic() {}/*
}

============================================== Jangaroo part ==============================================*/
    $primaryDeclaration(AS3.class_($module, {members: {
      StaticAndNonStatic: {
        value: null,
        writable: true
      },
      constructor: StaticAndNonStatic
    }}));
    static$0();
  });
});
