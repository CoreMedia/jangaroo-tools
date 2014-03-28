joo.classLoader.prepare("package flash.system",/* {*/



/**
 * The SecurityDomain class represents the current security "sandbox," also known as a security domain. By passing an instance of this class to <code>Loader.load()</code>, you can request that loaded media be placed in a particular sandbox.
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf619ab-7ff2.html About security domains
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7f03.html Specifying a LoaderContext
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7f0d.html Developing sandboxed applications
 *
 */
"public class SecurityDomain",1,function($$private){;return[ 
  /**
   * Gets the current security domain.
   * @see flash.display.Loader#load()
   * @see flash.display.Loader#loadBytes()
   * @see LoaderContext
   *
   */
  "public static function get currentDomain",function currentDomain$get()/*:SecurityDomain*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},["currentDomain"],["Error"], "0.8.0", "0.8.3"
);