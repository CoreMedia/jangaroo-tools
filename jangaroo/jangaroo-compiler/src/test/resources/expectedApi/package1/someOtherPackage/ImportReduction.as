package package1.someOtherPackage {
import package1.NoPrimitiveInit;
import package1.ParameterInitializers;

[Uses("Object")]
[Uses("package1.IncludedClass")]
public class ImportReduction {
  public var initializers:package1.ParameterInitializers;

  public var fun:Function;

  public var vec:Vector.<NoPrimitiveInit>;

  public native function call(callback:Function = null):void;
}
}