joo.classLoader.prepare("package flash.net",/* {*/


/**
 * The Responder class provides an object that is used in <code>NetConnection.call()</code> to handle return values from the server related to the success or failure of specific operations. When working with <code>NetConnection.call()</code>, you may encounter a network operation fault specific to the current operation or a fault related to the current connection status. Operation errors target the Responder object instead of the NetConnection object for easier error handling.
 * @see NetConnection#call()
 *
 */
"public class Responder",1,function($$private){;return[ 
  /**
   * Creates a new Responder object. You pass a Responder object to <code>NetConnection.call()</code> to handle return values from the server. You may pass <code>null</code> for either or both parameters.
   * @param result The function invoked if the call to the server succeeds and returns a result.
   * @param status The function invoked if the server returns an error.
   *
   */
  "public function Responder",function Responder$(result/*:Function*/, status/*:Function = null*/) {switch(arguments.length){case 0:case 1:status = null;}
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);