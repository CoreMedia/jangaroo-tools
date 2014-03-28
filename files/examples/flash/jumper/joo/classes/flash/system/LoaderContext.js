joo.classLoader.prepare("package flash.system",/* {*/





/**
 * The LoaderContext class provides options for loading SWF files and other media by using the Loader class. The LoaderContext class is used as the <code>context</code> parameter in the <code>load()</code> and <code>loadBytes()</code> methods of the Loader class.
 * <p>When loading SWF files with the <code>Loader.load()</code> method, you have two decisions to make: into which security domain the loaded SWF file should be placed, and into which application domain within that security domain? For more details on these choices, see the <code>applicationDomain</code> and <code>securityDomain</code> properties.</p>
 * <p>When loading a SWF file with the <code>Loader.loadBytes()</code> method, you have the same application domain choice to make as for <code>Loader.load()</code>, but it's not necessary to specify a security domain, because <code>Loader.loadBytes()</code> always places its loaded SWF file into the security domain of the loading SWF file.</p>
 * <p>When loading images (JPEG, GIF, or PNG) instead of SWF files, there is no need to specify a SecurityDomain or an application domain, because those concepts are meaningful only for SWF files. Instead, you have only one decision to make: do you need programmatic access to the pixels of the loaded image? If so, see the <code>checkPolicyFile</code> property. If you want to apply deblocking when loading an image, use the JPEGLoaderContext class instead of the LoaderContext class.</p>
 * @see flash.display.Loader#load()
 * @see flash.display.Loader#loadBytes()
 * @see ApplicationDomain
 * @see JPEGLoaderContext
 * @see #applicationDomain
 * @see #checkPolicyFile
 * @see #securityDomain
 * @see SecurityDomain
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7f03.html Specifying a LoaderContext
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf619ab-7fe3.html Creating class instances from loaded applications
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e13.html Loading display content dynamically
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7de0.html Specifying loading context
 *
 */
"public class LoaderContext",1,function($$private){;return[ 
  /**
   * Specifies the application domain to use for the <code>Loader.load()</code> or <code>Loader.loadBytes()</code> method. Use this property only when loading a SWF file written in ActionScript 3.0 (not an image or a SWF file written in ActionScript 1.0 or ActionScript 2.0).
   * <p>Every security domain is divided into one or more application domains, represented by ApplicationDomain objects. Application domains are not for security purposes; they are for managing cooperating units of ActionScript code. If you are loading a SWF file from another domain, and allowing it to be placed in a separate security domain, then you cannot control the choice of application domain into which the loaded SWF file is placed; and if you have specified a choice of application domain, it will be ignored. However, if you are loading a SWF file into your own security domain � either because the SWF file comes from your own domain, or because you are importing it into your security domain � then you can control the choice of application domain for the loaded SWF file.</p>
   * <p>You can pass an application domain only from your own security domain in <code>LoaderContext.applicationDomain</code>. Attempting to pass an application domain from any other security domain results in a <code>SecurityError</code> exception.</p>
   * <p>You have four choices for what kind of <code>ApplicationDomain</code> property to use:</p>
   * <ul>
   * <li><b>Child of loader's ApplicationDomain.</b> The default. You can explicitly represent this choice with the syntax <code>new ApplicationDomain(ApplicationDomain.currentDomain)</code>. This allows the loaded SWF file to use the parent's classes directly, for example by writing <code>new MyClassDefinedInParent()</code>. The parent, however, cannot use this syntax; if the parent wishes to use the child's classes, it must call <code>ApplicationDomain.getDefinition()</code> to retrieve them. The advantage of this choice is that, if the child defines a class with the same name as a class already defined by the parent, no error results; the child simply inherits the parent's definition of that class, and the child's conflicting definition goes unused unless either child or parent calls the <code>ApplicationDomain.getDefinition()</code> method to retrieve it.</li>
   * <li><b>Loader's own ApplicationDomain.</b> You use this application domain when using <code>ApplicationDomain.currentDomain</code>. When the load is complete, parent and child can use each other's classes directly. If the child attempts to define a class with the same name as a class already defined by the parent, the parent class is used and the child class is ignored.</li>
   * <li><b>Child of the system ApplicationDomain.</b> You use this application domain when using <code>new ApplicationDomain(null)</code>. This separates loader and loadee entirely, allowing them to define separate versions of classes with the same name without conflict or overshadowing. The only way either side sees the other's classes is by calling the <code>ApplicationDomain.getDefinition()</code> method.</li>
   * <li><b>Child of some other ApplicationDomain.</b> Occasionally you may have a more complex ApplicationDomain hierarchy. You can load a SWF file into any ApplicationDomain from your own SecurityDomain. For example, <code>new ApplicationDomain(ApplicationDomain.currentDomain.parentDomain.parentDomain)</code> loads a SWF file into a new child of the current domain's parent's parent.</li></ul>
   * <p>When a load is complete, either side (loading or loaded) may need to find its own ApplicationDomain, or the other side's ApplicationDomain, for the purpose of calling <code>ApplicationDomain.getDefinition()</code>. Either side can retrieve a reference to its own application domain by using <code>ApplicationDomain.currentDomain</code>. The loading SWF file can retrieve a reference to the loaded SWF file's ApplicationDomain via <code>Loader.contentLoaderInfo.applicationDomain</code>. If the loaded SWF file knows how it was loaded, it can find its way to the loading SWF file's ApplicationDomain object. For example, if the child was loaded in the default way, it can find the loading SWF file's application domain by using <code>ApplicationDomain.currentDomain.parentDomain</code>.</p>
   * <p>For more information, see the "ApplicationDomain class" section of the "Client System Environment" chapter of the <i>ActionScript 3.0 Developer's Guide</i>.</p>
   * @see flash.display.Loader#load()
   * @see flash.display.Loader#loadBytes()
   * @see ApplicationDomain
   *
   */
  "public var",{ applicationDomain/*:ApplicationDomain*/ : null},
  /**
   * Specifies whether the application should attempt to download a URL policy file from the loaded object's server before beginning to load the object itself. This flag is applicable to the <code>Loader.load()</code> method, but not to the <code>Loader.loadBytes()</code> method.
   * <p>Set this flag to <code>true</code> when you are loading an image (JPEG, GIF, or PNG) from outside the calling SWF file's own domain, and you expect to need access to the content of that image from ActionScript. Examples of accessing image content include referencing the <code>Loader.content</code> property to obtain a Bitmap object, and calling the <code>BitmapData.draw()</code> method to obtain a copy of the loaded image's pixels. If you attempt one of these operations without having specified <code>checkPolicyFile</code> at loading time, you may get a <code>SecurityError</code> exception because the needed policy file has not been downloaded yet.</p>
   * <p>When you call the <code>Loader.load()</code> method with <code>LoaderContext.checkPolicyFile</code> set to <code>true</code>, the application does not begin downloading the specified object in <code>URLRequest.url</code> until it has either successfully downloaded a relevant URL policy file or discovered that no such policy file exists. Flash Player or AIR first considers policy files that have already been downloaded, then attempts to download any pending policy files specified in calls to the <code>Security.loadPolicyFile()</code> method, then attempts to download a policy file from the default location that corresponds to <code>URLRequest.url</code>, which is <code>/crossdomain.xml</code> on the same server as <code>URLRequest.url</code>. In all cases, the given policy file is required to exist at <code>URLRequest.url</code> by virtue of the policy file's location, and the file must permit access by virtue of one or more <code><allow-access-from></code> tags.</p>
   * <p>If you set <code>checkPolicyFile</code> to <code>true</code>, the main download that specified in the <code>Loader.load()</code> method does not load until the policy file has been completely processed. Therefore, as long as the policy file that you need exists, as soon as you have received any <code>ProgressEvent.PROGRESS</code> or <code>Event.COMPLETE</code> events from the <code>contentLoaderInfo</code> property of your Loader object, the policy file download is complete, and you can safely begin performing operations that require the policy file.</p>
   * <p>If you set <code>checkPolicyFile</code> to <code>true</code>, and no relevant policy file is found, you will not receive any error indication until you attempt an operation that throws a <code>SecurityError</code> exception. However, once the LoaderInfo object dispatches a <code>ProgressEvent.PROGRESS</code> or <code>Event.COMPLETE</code> event, you can test whether a relevant policy file was found by checking the value of the <code>LoaderInfo.childAllowsParent</code> property.</p>
   * <p>If you will not need pixel-level access to the image that you are loading, you should not set the <code>checkPolicyFile</code> property to <code>true</code>. Checking for a policy file in this case is wasteful, because it may delay the start of your download, and it may consume network bandwidth unnecessarily.</p>
   * <p>Also try to avoid setting <code>checkPolicyFile</code> to <code>true</code> if you are using the <code>Loader.load()</code> method to download a SWF file. This is because SWF-to-SWF permissions are not controlled by policy files, but rather by the <code>Security.allowDomain()</code> method, and thus <code>checkPolicyFile</code> has no effect when you load a SWF file. Checking for a policy file in this case is wasteful, because it may delay the download of the SWF file, and it may consume network bandwidth unnecessarily. (Flash Player or AIR cannot tell whether your main download will be a SWF file or an image, because the policy file download occurs before the main download.)</p>
   * <p>Be careful with <code>checkPolicyFile</code> if you are downloading an object from a URL that may use server-side HTTP redirects. Policy files are always retrieved from the corresponding initial URL that you specify in <code>URLRequest.url</code>. If the final object comes from a different URL because of HTTP redirects, then the initially downloaded policy files might not be applicable to the object's final URL, which is the URL that matters in security decisions. If you find yourself in this situation, you can examine the value of <code>LoaderInfo.url</code> after you have received a <code>ProgressEvent.PROGRESS</code> or <code>Event.COMPLETE</code> event, which tells you the object's final URL. Then call the <code>Security.loadPolicyFile()</code> method with a policy file URL based on the object's final URL. Then poll the value of <code>LoaderInfo.childAllowsParent</code> until it becomes <code>true</code>.</p>
   * <p>You do not need to set this property for AIR content running in the application sandbox. Content in the AIR application sandbox can call the <code>BitmapData.draw()</code> method using any loaded image content as the source.</p>
   * @see flash.display.BitmapData#draw()
   * @see flash.display.Loader#content
   * @see flash.display.Loader#load()
   * @see flash.display.LoaderInfo#childAllowsParent
   * @see flash.display.LoaderInfo#url
   * @see Security#allowDomain()
   * @see Security#loadPolicyFile()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e13.html Loading display content dynamically
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7de0.html Specifying loading context
   *
   */
  "public var",{ checkPolicyFile/*:Boolean*/ : false},
  /**
   * Specifies the security domain to use for a <code>Loader.load()</code> operation. Use this property only when loading a SWF file (not an image).
   * <p>The choice of security domain is meaningful only if you are loading a SWF file that might come from a different domain (a different server) than the loading SWF file. When you load a SWF file from your own domain, it is always placed into your security domain. But when you load a SWF file from a different domain, you have two options. You can allow the loaded SWF file to be placed in its "natural" security domain, which is different from that of the loading SWF file; this is the default. The other option is to specify that you want to place the loaded SWF file placed into the same security domain as the loading SWF file, by setting <code>myLoaderContext.securityDomain</code> to be equal to <code>SecurityDomain.currentDomain</code>. This is called <i>import loading</i>, and it is equivalent, for security purposes, to copying the loaded SWF file to your own server and loading it from there. In order for import loading to succeed, the loaded SWF file's server must have a policy file trusting the domain of the loading SWF file.</p>
   * <p>You can pass your own security domain only in <code>LoaderContext.securityDomain</code>. Attempting to pass any other security domain results in a <code>SecurityError</code> exception.</p>
   * <p>Content in the AIR application security sandbox cannot load content from other sandboxes into its SecurityDomain.</p>
   * <p>For more information, see the "Security" chapter in the <i>ActionScript 3.0 Developer's Guide</i>.</p>
   * @see flash.display.Loader#load()
   * @see SecurityDomain
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e13.html Loading display content dynamically
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7de0.html Specifying loading context
   *
   */
  "public var",{ securityDomain/*:SecurityDomain*/ : null},

  /**
   * Creates a new LoaderContext object, with the specified settings. For complete details on these settings, see the descriptions of the properties of this class.
   * @param checkPolicyFile Specifies whether a check should be made for the existence of a URL policy file before loading the object.
   * @param applicationDomain Specifies the ApplicationDomain object to use for a Loader object.
   * @param securityDomain Specifies the SecurityDomain object to use for a Loader object.
   * <p><i>Note:</i> Content in the air application security sandbox cannot load content from other sandboxes into its SecurityDomain.</p>
   *
   * @see flash.display.Loader#load()
   * @see flash.display.Loader#loadBytes()
   * @see ApplicationDomain
   * @see SecurityDomain
   *
   */
  "public function LoaderContext",function LoaderContext$(checkPolicyFile/*:Boolean = false*/, applicationDomain/*:ApplicationDomain = null*/, securityDomain/*:SecurityDomain = null*/) {switch(arguments.length){case 0:checkPolicyFile = false;case 1:applicationDomain = null;case 2:securityDomain = null;}
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);