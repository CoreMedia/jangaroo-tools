joo.classLoader.prepare("package flash.system",/* {*/


/**
 * The ApplicationDomain class is a container for discrete groups of class definitions. Application domains are used to partition classes that are in the same security domain. They allow multiple definitions of the same class to exist and allow children to reuse parent definitions.
 * <p>Application domains are used when an external SWF file is loaded through the Loader class. All ActionScript 3.0 definitions in the loaded SWF file are stored in the application domain, which is specified by the <code>applicationDomain</code> property of the LoaderContext object that you pass as a <code>context</code> parameter of the Loader object's <code>load()</code> or <code>loadBytes()</code> method. The LoaderInfo object also contains an <code>applicationDomain</code> property, which is read-only.</p>
 * <p>All code in a SWF file is defined to exist in an application domain. The current application domain is where your main application runs. The system domain contains all application domains, including the current domain, which means that it contains all Flash Player classes.</p>
 * <p>Every application domain, except the system domain, has an associated parent domain. The parent domain of your main application's application domain is the system domain. Loaded classes are defined only when their parent doesn't already define them. You cannot override a loaded class definition with a newer definition.</p>
 * <p>For usage examples of application domains, see the <i>ActionScript 3.0 Developer's Guide</i>.</p>
 * <p>The <code>ApplicationDomain()</code> constructor function allows you to create an ApplicationDomain object.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/system/ApplicationDomain.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.display.Loader#load()
 * @see flash.display.Loader#loadBytes()
 * @see flash.display.LoaderInfo
 * @see flash.net.URLRequest
 * @see LoaderContext
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf619ab-7ffc.html About application domains
 * @see http://help.adobe.com/en_US/as3/dev/WSd75bf4610ec9e22f43855da312214da1d8f-8000.html Working with application domains
 *
 */
"public final class ApplicationDomain",1,function($$private){;return[ 
  /**
   * Gets the current application domain in which your code is executing.
   * @see http://help.adobe.com/en_US/as3/dev/WSd75bf4610ec9e22f43855da312214da1d8f-8000.html Working with application domains
   *
   */
  "public static function get currentDomain",function currentDomain$get()/*:ApplicationDomain*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Gets the parent domain of this application domain.
   * @see http://help.adobe.com/en_US/as3/dev/WSd75bf4610ec9e22f43855da312214da1d8f-8000.html Working with application domains
   *
   */
  "public function get parentDomain",function parentDomain$get()/*:ApplicationDomain*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new application domain.
   * @param parentDomain If no parent domain is passed in, this application domain takes the system domain as its parent.
   *
   */
  "public function ApplicationDomain",function ApplicationDomain$(parentDomain/*:ApplicationDomain = null*/) {switch(arguments.length){case 0:parentDomain = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Gets a public definition from the specified application domain. The definition can be that of a class, a namespace, or a function.
   * @param name The name of the definition.
   *
   * @return The object associated with the definition.
   *
   * @throws ReferenceError No public definition exists with the specified name.
   *
   */
  "public function getDefinition",function getDefinition(name/*:String*/)/*:Object*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Checks to see if a public definition exists within the specified application domain. The definition can be that of a class, a namespace, or a function.
   * @param name The name of the definition.
   *
   * @return A value of <code>true</code> if the specified definition exists; otherwise, <code>false</code>.
   *
   */
  "public function hasDefinition",function hasDefinition(name/*:String*/)/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},["currentDomain"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);