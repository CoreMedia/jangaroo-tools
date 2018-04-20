package package2 {
public class TestExtPrivate {

  public function canOverride():void {
    // This method can be overridden in subclasses.
  }

  [ExtPrivate]
  public function canOnlyOverrideIfAnnotated(foo:String):Boolean {
    // This method can only be overridden in subclasses if annotated with [ExtPrivate].
  }
}
}