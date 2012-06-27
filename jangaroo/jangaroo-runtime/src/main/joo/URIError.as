package {

/**
 * A URIError exception is thrown when one of the global URI handling functions is used in a way that is incompatible with its definition. This exception is thrown when an invalid URI is specified to a function that expects a valid URI, such as the <code>Socket.connect()</code> method.
 * @see flash.net.Socket#connect()
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
 *
 */
[Native]
public dynamic class URIError extends Error {
  /**
   * Creates a new URIError object.
   * @param message Contains the message associated with the URIError object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7ecf.html Responding to error events and status
   *
   */
  public function URIError(message:String = "") {
    super(message);
  }
}
}
