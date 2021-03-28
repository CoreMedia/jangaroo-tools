
class TestExtPrivate {

  canOverride():void {
    // This method can be overridden in subclasses.
  }

  canOnlyOverrideIfAnnotated(foo:string):boolean {
    // This method can only be overridden in subclasses if annotated with [ExtPrivate].
  }
}
export default TestExtPrivate;
