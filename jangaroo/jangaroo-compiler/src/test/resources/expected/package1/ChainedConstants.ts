import SomeOtherClass from "./someOtherPackage/SomeOtherClass";
import int from "../AS3/int_";


class ChainedConstants {
  static readonly METHOD_TYPE_GET : string = "get";

  static readonly DEFAULT_METHOD_TYPE : string = ChainedConstants.METHOD_TYPE_GET;

  static readonly THE_METHOD_TYPE : string = ChainedConstants.METHOD_TYPE_GET;

  static readonly ANOTHER_METHOD_TYPE : string = ChainedConstants.METHOD_TYPE_GET.substr(0, 1);

  static readonly THE_BLA : int = SomeOtherClass.BLA;
}
export default ChainedConstants;
