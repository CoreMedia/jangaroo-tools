package package1 {

public class ManyConstructorParameters {
  public function ManyConstructorParameters(str:String, integer:int, num2:Number,
                                            bool:Boolean, obj:Object, undef:*, withDefault:String = null) {
  }

  public function isEmpty(str:String):Boolean {
    return str.length === 0;
  }
}
}