joo.classLoader.prepare("package flash.net",/* {*/


/**
 * The URLRequestMethod class provides values that specify whether the URLRequest object should use the <code>POST</code> method or the <code>GET</code> method when sending data to a server.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/URLRequestMethod.html#includeExamplesSummary">View the examples</a></p>
 * @see URLRequest
 * @see URLVariables
 *
 */
"public final class URLRequestMethod",1,function($$private){;return[ 
  /**
   * Specifies that the URLRequest object is a <code>GET</code>.
   */
  "public static const",{ GET/*:String*/ : "GET"},
  /**
   * Specifies that the URLRequest object is a <code>POST</code>.
   * <p><i>Note:</i> For content running in Adobe AIR, when using the <code>navigateToURL()</code> function, the runtime treats a URLRequest that uses the POST method (one that has its <code>method</code> property set to <code>URLRequestMethod.POST</code>) as using the GET method.</p>
   */
  "public static const",{ POST/*:String*/ : "POST"},
];},[],[], "0.8.0", "0.8.1"
);