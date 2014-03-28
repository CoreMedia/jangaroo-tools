joo.classLoader.prepare("package js",/* {*/

/**
 * The XMLHttpRequest object can be used by scripts to programmatically connect to their originating server via HTTP. 
 */
"public class XMLHttpRequest",1,function($$private){;return[ 

  "public native function XMLHttpRequest"/*();*/,

  /**
   * When constructed, the XMLHttpRequest object is be in the UNSENT state.
   */
  "public native static function get UNSENT"/*() : int;*/,

  /**
   * The OPENED state is the state of the object when the open() method has been successfully invoked. During this
   * state request headers can be set using setRequestHeader() and the request can be made using send().
   */
  "public native static function get OPENED"/*() : int;*/,

  /**
   * The HEADERS_RECEIVED state is the state of the object when all response headers have been received.
   */
  "public native static function get HEADERS_RECEIVED"/*() : int;*/,

  /**
   * The LOADING state is the state of the object when the response entity body is being received.
   */
  "public native static function get LOADING"/*() : int;*/,

  /**
   * The DONE state is the state of the object when either the data transfer has been completed or something went
   * wrong during the transfer (infinite redirects for instance).
   */
  "public native static function get DONE"/*() : int;*/,

  /**
   * The state of the request.
   * The XMLHttpRequest object can be in five states: UNSENT, OPENED, HEADERS_RECEIVED, LOADING and DONE.
   * The current state is exposed through the readyState attribute.
   */
  "public native function get readyState"/*() : int;*/,

  /**
   * The response to the request as text, or null if the request was unsucessful or has not yet been sent.
   */
  "public native function get responseText"/*() : String;*/,

  /**
   * The response to the request as a DOM Document object, or null if the request was unsuccessful, has not yet been
   * sent, or cannot be parsed as XML.
   * The response is parsed as if it were a text/xml stream.
   * <p><b>Note:</b> If the server doesn't apply the text/xml Content-Type header, you can use overrideMimeType() to
   * force XMLHttpRequest to parse it as XML anyway.</p>
   * @return the response to the request as a DOM Document object or null.
   */
  "public native function get responseXML"/*() : Document;*/,

  /**
   * The status of the response to the request.
   * This is the HTTP result code (for example, status is 200 for a successful request).
   */
  "public native function get status"/*() : uint;*/,

  /**
   * The response string returned by the HTTP server.
   * Unlike status, this includes the entire text of the response message ("200 OK", for example).
   * @return the response string returned by the HTTP server.
   */
  "public native function get statusText"/*() : String;*/,

  /**
   * Sets the value of an HTTP request header.
   * <p><b>Note:</b> You must call open() before using this method.</p>
   * @param header The name of the header whose value is to be set.
   * @param value The value to set as the body of the header.
   */
  "public native function setRequestHeader"/*(header : String, value : String) : void;*/,

  /**
   * Overrides the MIME type returned by the server.
   * <p><b>Note:</b> This method must be called before send().</p>
   * @param mimetype The type that should be used instead of the one returned by the server, if any.
   */
  "public native function overrideMimeType"/*(mimetype : String) : void;*/,

  /**
   * Initializes a request.
   * <p><b>Note:</b> Calling this method an already active request (one for which open() or openRequest() has already
   * been called) is the equivalent of calling abort().</p>
   * @param method The HTTP method to use; either "POST" or "GET".  Ignored for non-HTTP URLs.
   * @param url The URL to which to send the request.
   * @param async An optional boolean parameter, defaulting to true, indicating whether or not to perform the operation
   *  asynchronously.  If this value is false, the send() method does not return until the response is received.
   * If true, notification of a completed transaction is provided using event listeners. This must be true if the
   * multipart attribute is true, or an exception will be thrown.
   * @param user The optional user name to use for authentication purposes; by default, this is an empty string.
   * @param password The optional password to use for authentication purposes; by default, this is an empty string.
   */
  "public native function open"/*(method : String, url : String, async : Boolean = true,
                              user : String = undefined, password : String = undefined) : void;*/,

  "public var",{ onreadystatechange/* : Function*/:null},

  "public native function addEventListener"/*(eventType : String, handler : Function, capture : Boolean) : void;*/,

  "public native function removeEventListener"/*(eventType : String, handler : Function, capture : Boolean) : void;*/,
  
  /**
   * Sends the request.
   * If the request is asynchronous (which is the default), this method returns as soon as the request is sent.
   * If the request is synchronous, this method doesn't return until the response has arrived.
   * <p><b>Note:</b> Any event listeners you wish to set must be set before calling send().</p>
   * @param body This may be a Document or a String that is used to populate the body of a POST request.
   *  If the body is an Document, it is serialized before being sent.
   */
  "public native function send"/*(body : * = undefined) : void;*/,

  /**
   * Aborts the request if it has already been sent.
   */
  "public native function abort"/*() : void;*/,

  /**
   * Returns the text of a specified header.
   * @param header The name of the header to retrieve.
   * @return A string containing the text of the specified header, or null if either the response has not yet been
   *  received or the header doesn't exist in the response.
   */
  "public native function getResponseHeader"/*(header : String) : String;*/,

  /**
   * Returns all the response headers as a string.
   * <p><b>Note:</b> For multipart requests, this returns the headers from the current part of the request, not from
   *  the original channel.</p>
   * @return The text of all response headers, or null if no response has been received.
   */
  "public native function getAllResponseHeaders"/*() : String;*/,
];},[],[], "0.8.0", "0.8.1"
);