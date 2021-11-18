import uint from "../AS3/uint_";


class ReadonlyFieldAssignmentInConstructor extends Error {

  constructor(json:any) {
    super(json.message);
    this.method = json.method;
    this.errorCode = json.errorCode;
    this.errorName = json.errorName;
    this.status = json.status;
  }

  /**
   * The HTTP method of the request that raised this remote error.
   */
  readonly method:string;

  /**
   * The HTTP status code of the response that raised this remote error.
   * Defaults to 400.
   */
  readonly status:uint;

  /**
   * The name of the RemoteError.
   */
  readonly errorName:string;

  /**
   * The error code of the RemoteError.
   */
  readonly errorCode:string;
}
export default ReadonlyFieldAssignmentInConstructor;
