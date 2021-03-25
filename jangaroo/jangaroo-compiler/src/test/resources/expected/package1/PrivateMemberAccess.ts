
class PrivateMemberAccess {

  static readonly INSTANCE:PrivateMemberAccess = new PrivateMemberAccess();
  #secret:string = null;

  static doSomething():string {
    return PrivateMemberAccess.INSTANCE.#secret;
  }
}
export default PrivateMemberAccess;
