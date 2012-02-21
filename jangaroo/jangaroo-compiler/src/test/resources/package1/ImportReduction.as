package package1 {

import package1.someOtherPackage.SomeOtherClass;
import package1.NoPrimitiveInit;
import package1.ParameterInitializers;

public class ImportReduction {
  public var initializers:ParameterInitializers;
  public var fun:Function;
  public var vec:Vector.<NoPrimitiveInit>;
  public function call(callback:Function = null):void {}
}
}