package package1 {

public class AuxVarConfusion {

  public function doSomething():void {
    for each (var i in {foo:true}) {
      new AuxVarConfusion();
    }
  }

}
}