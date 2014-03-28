joo.classLoader.prepare("package flash.net",/* {
import flash.events.EventDispatcher*/

/**
 * property AsyncErrorEvent.type =
 * @eventType flash.events.AsyncErrorEvent.ASYNC_ERROR
 */
{Event:{name:"asyncError", type:"flash.events.AsyncErrorEvent"}},
/**
 * property SecurityErrorEvent.type =
 * @eventType flash.events.SecurityErrorEvent.SECURITY_ERROR
 */
{Event:{name:"securityError", type:"flash.events.SecurityErrorEvent"}},
/**
 * property StatusEvent.type =
 * @eventType flash.events.StatusEvent.STATUS
 */
{Event:{name:"status", type:"flash.events.StatusEvent"}},

/**
 * The LocalConnection class lets you create a LocalConnection object that can invoke a method in another LocalConnection object. The communication can be:
 * <ul>
 * <li>Within a single SWF file</li>
 * <li>Between multiple SWF files</li>
 * <li>Between content (SWF-based or HTML-based) in AIR applications</li>
 * <li>Between content (SWF-based or HTML-based) in an AIR application and SWF content running in a browser</li></ul>
 * <p><i>AIR profile support:</i> This feature is supported on all desktop operating systems and on all AIR for TV devices, but is not supported on mobile devices. You can test for support at run time using the <code>LocalConnection.isSupported</code> property. See <a href="http://help.adobe.com/en_US/air/build/WS144092a96ffef7cc16ddeea2126bb46b82f-8000.html">AIR Profile Support</a> for more information regarding API support across multiple profiles.</p>
 * <p><b>Note:</b> AIR for TV devices support communication only between SWF-based content in AIR applications.</p>
 * <p>Local connections enable this kind of communication between SWF files without the use of <code>fscommand()</code> or JavaScript. LocalConnection objects can communicate only among files that are running on the same client computer, but they can be running in different applications — for example, a file running in a browser and a SWF file running in Adobe AIR.</p>
 * <p>LocalConnection objects created in ActionScript 3.0 can communicate with LocalConnection objects created in ActionScript 1.0 or 2.0. The reverse is also true: LocalConnection objects created in ActionScript 1.0 or 2.0 can communicate with LocalConnection objects created in ActionScript 3.0. Flash Player handles this communication between LocalConnection objects of different versions automatically.</p>
 * <p>There are three ways to add callback methods to a LocalConnection object:</p>
 * <ul>
 * <li>Subclass the LocalConnection class and add methods.</li>
 * <li>Set the <code>LocalConnection.client</code> property to an object that implements the methods.</li>
 * <li>Create a dynamic class that extends LocalConnection and dynamically attach methods.</li></ul>
 * <p>To understand how to use LocalConnection objects to implement communication between two files, it is helpful to identify the commands used in each file. One file is called the <i>receiving</i> file; it is the file that contains the method to be invoked. The receiving file must contain a LocalConnection object and a call to the <code>connect()</code> method. The other file is called the <i>sending</i> file; it is the file that invokes the method. The sending file must contain another LocalConnection object and a call to the <code>send()</code> method.</p>
 * <p>Your use of <code>send()</code> and <code>connect()</code> differs depending on whether the files are in the same domain, in different domains with predictable domain names, or in different domains with unpredictable or dynamic domain names. The following paragraphs explain the three different situations, with code samples for each.</p>
 * <p><b>Same domain</b>. This is the simplest way to use a LocalConnection object, to allow communication only between LocalConnection objects that are located in the same domain, because same-domain communication is permitted by default. When two files from the same domain communicate, you do not need to implement any special security measures, and you simply pass the same value for the <code>connectionName</code> parameter to both the <code>connect()</code> and <code>send()</code> methods:</p>
 * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/localconnection_samedomains.gif" /></p>
 * <listing>
 * // receivingLC is in http://www.domain.com/receiving.swf
 * receivingLC.connect('myConnection');
 *
 * // sendingLC is in http://www.domain.com/sending.swf
 * // myMethod() is defined in sending.swf
 * sendingLC.send('myConnection', 'myMethod');
 * </listing>
 * <p><b>Different domains with predictable domain names</b>. When two SWF files from different domains communicate, you need to allow communication between the two domains by calling the <code>allowDomain()</code> method. You also need to qualify the connection name in the <code>send()</code> method with the receiving LocalConnection object's domain name:</p>
 * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/localconnection_differentdomains.gif" /></p>
 * <listing>
 * // receivingLC is in http://www.domain.com/receiving.swf
 * receivingLC.allowDomain('www.anotherdomain.com');
 * receivingLC.connect('myConnection');
 *
 * // sendingLC is in http://www.anotherdomain.com/sending.swf
 * sendingLC.send('www.domain.com:myConnection', 'myMethod');
 * </listing>
 * <p><b>Different domains with unpredictable domain names</b>. Sometimes, you might want to make the file with the receiving LocalConnection object more portable between domains. To avoid specifying the domain name in the <code>send()</code> method, but to indicate that the receiving and sending LocalConnection objects are not in the same domain, precede the connection name with an underscore (_), in both the <code>connect()</code> and <code>send()</code> calls. To allow communication between the two domains, call the <code>allowDomain()</code> method and pass the domains from which you want to allow LocalConnection calls. Alternatively, pass the wildcard (*) argument to allow calls from all domains:</p>
 * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/localconnection_unknowndomains.gif" /></p>
 * <listing>
 * // receivingLC is in http://www.domain.com/receiving.swf
 * receivingLC.allowDomain('*');
 * receivingLC.connect('_myConnection');
 *
 * // sendingLC is in http://www.anotherdomain.com/sending.swf
 * sendingLC.send('_myConnection', 'myMethod');
 * </listing>
 * <p><b>From Flash Player to an AIR application</b>. A LocalConnection object created in the AIR application sandbox uses a special string as it's connection prefix instead of a domain name. This string has the form: <code>app#appID.pubID</code> where appID is the application ID and pubID is the publisher ID of the application. (Only include the publisher ID if the AIR application uses a publisher ID.) For example, if an AIR application has an application ID of, "com.example", and no publisher ID, you could use: <code>app#com.example:myConnection</code> as the local connection string. The AIR application also must call the <code>allowDomain()</code> method, passing in the calling SWF file's domain of origin:</p>
 * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/localconnection_flash2AIR.gif" /></p>
 * <listing>
 * // receivingLC is an AIR application with app ID = com.example (and no publisher ID)
 * receivingLC.allowDomain('www.domain.com');
 * receivingLC.connect('myConnection');
 *
 * // sendingLC is in http://www.domain.com/sending.swf
 * sendingLC.send('app#com.example:myConnection', 'myMethod');
 * </listing>
 * <p><b>Note:</b> If an AIR application loads a SWF outside the AIR application sandbox, then the rules for establishing a local connection with that SWF are the same as the rules for establishing a connection with a SWF running in Flash Player.</p>
 * <p><b>From an AIR application to Flash Player</b>. When an AIR application communicates with a SWF running in the Flash Player runtime, you need to allow communication between the two by calling the <code>allowDomain()</code> method and passing in the AIR application's connection prefix. For example, if an AIR application has an application ID of, "com.example", and no publisher ID, you could pass the string: <code>app#com.example</code> to the <code>allowDomain()</code> method. You also need to qualify the connection name in the <code>send()</code> method with the receiving LocalConnection object's domain name (use "localhost" as the domain for SWF files loaded from the local file system):</p>
 * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/localconnection_AIR2flash.gif" /></p>
 * <listing>
 * // receivingLC is in http://www.domain.com/receiving.swf
 * receivingLC.allowDomain('app#com.example');
 * receivingLC.connect('myConnection');
 *
 * // sendingLC is an AIR application with app ID = com.example (and no publisher ID)
 * sendingLC.send('www.domain.com:myConnection', 'myMethod');
 * </listing>
 * <p><b>From an AIR application to another AIR application</b>. To communicate between two AIR applications, you need to allow communication between the two by calling the <code>allowDomain()</code> method and passing in the sending AIR application's connection prefix. For example, if the sending application has an application ID of, "com.example", and no publisher ID, you could pass the string: <code>app#com.example</code> to the <code>allowDomain()</code> method in the receiving application. You also need to qualify the connection name in the <code>send()</code> method with the receiving LocalConnection object's connection prefix:</p>
 * <p><img src="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/images/localconnection_AIR2AIR.gif" /></p>
 * <listing>
 * // receivingLC is an AIR application with app ID = com.sample (and no publisher ID)
 * receivingLC.allowDomain('app#com.example');
 * receivingLC.connect('myConnection');
 *
 * // sendingLC is an AIR application with app ID = com.example (and no publisher ID)
 * sendingLC.send('app#com.sample:myConnection', 'myMethod');
 * </listing>
 * <p>You can use LocalConnection objects to send and receive data within a single file, but this is not a typical implementation.</p>
 * <p>For more information about the <code>send()</code> and <code>connect()</code> methods, see the discussion of the <code>connectionName</code> parameter in the <code>LocalConnection.send()</code> and <code>LocalConnection.connect()</code>entries. Also, see the <code>allowDomain()</code> and <code>domain</code> entries.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/LocalConnection.html#includeExamplesSummary">View the examples</a></p>
 * @see #send()
 * @see #allowDomain()
 * @see #domain
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118666ade46-7c7e.html Communicating with other Flash Player and AIR instances
 *
 */
"public class LocalConnection extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * Indicates the object on which callback methods are invoked. The default object is <code>this</code>, the local connection being created. You can set the <code>client</code> property to another object, and callback methods are invoked on that other object.
   * @throws TypeError The <code>client</code> property must be set to a non-null object.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118666ade46-7c7e.html Communicating with other Flash Player and AIR instances
   *
   */
  "public function get client",function client$get()/*:Object*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set client",function client$set(value/*:Object*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A string representing the domain of the location of the current file.
   * <p>In content running in the <code>application</code> security sandbox in Adobe AIR (content installed with the AIR application), the runtime uses the string <code>app#</code> followed by the application ID for the AIR application (defined in the application descriptor file) in place of the superdomain. For example a <code>connectionName</code> for an application with the application ID <code>com.example.air.MyApp</code> <code>connectionName</code> resolves to <code>"app#com.example.air.MyApp:connectionName"</code>.</p>
   * <p>In SWF files published for Flash Player 9 or later, the returned string is the exact domain of the file, including subdomains. For example, if the file is located at www.adobe.com, this command returns <code>"www.adobe.com"</code>.</p>
   * <p>If the current file is a local file residing on the client computer running in Flash Player, this command returns <code>"localhost"</code>.</p>
   * <p>The most common ways to use this property are to include the domain name of the sending LocalConnection object as a parameter to the method you plan to invoke in the receiving LocalConnection object, or to use it with <code>LocalConnection.allowDomain()</code> to accept commands from a specified domain. If you are enabling communication only between LocalConnection objects that are located in the same domain, you probably don't need to use this property.</p>
   * @see #allowDomain()
   * @see #connect()
   *
   */
  "public function get domain",function domain$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a LocalConnection object. You can use LocalConnection objects to enable communication between different files that are running on the same client computer.
   * @see #connect()
   * @see #send()
   *
   */
  "public function LocalConnection",function LocalConnection$() {this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies one or more domains that can send LocalConnection calls to this LocalConnection instance.
   * <p>You cannot use this method to let files hosted using a secure protocol (HTTPS) allow access from files hosted in nonsecure protocols; you must use the <code>allowInsecureDomain()</code> method instead.</p>
   * <p>You may want to use this method so that a child file from a different domain can make LocalConnection calls to the parent file, without knowing the final domain from which the child file will come. This can happen, for example, when you use load-balancing redirects or third-party servers. In this situation, you can use the <code>url</code> property of the LoaderInfo object used with the load, to get the domain to use with the <code>allowDomain()</code> method. For example, if you use a Loader object to load a child file, once the file is loaded, you can check the <code>contentLoaderInfo.url</code> property of the Loader object, and parse the domain out of the full URL string. If you do this, make sure that you wait until the file is loaded, because the <code>contentLoaderInfo.url</code> property will not have its final, correct value until the file is completely loaded.</p>
   * <p>The opposite situation can also occur: you might create a child file that wants to accept LocalConnection calls from its parent but doesn't know the domain of its parent. In this situation, implement this method by checking whether the domain argument matches the domain of the <code>loaderInfo.url</code> property in the loaded file. Again, you must parse the domain out of the full URL from <code>loaderInfo.url</code>. In this situation, you don't have to wait for the parent file to load; the parent will already be loaded by the time the child loads.</p>
   * <p>When using this method, consider the Flash Player security model. By default, a LocalConnection object is associated with the sandbox of the file that created it, and cross-domain calls to LocalConnection objects are not allowed unless you call the <code>LocalConnection.allowDomain()</code> method in the receiving file. However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
   * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * <p><b>Note</b>: The <code>allowDomain()</code> method has changed from the form it had in ActionScript 1.0 and 2.0. In those earlier versions, <code>allowDomain</code> was a callback method that you implemented. In ActionScript 3.0, <code>allowDomain()</code> is a built-in method of LocalConnection that you call. With this change, <code>allowDomain()</code> works in much the same way as <code>flash.system.Security.allowDomain()</code>.</p>
   * @param domains One or more strings that name the domains from which you want to allow LocalConnection calls. This parameter has two special cases:
   * <ul>
   * <li>You can specify a wildcard character "*" to allow calls from all domains.</li>
   * <li>You can specify the string "<code>localhost</code>" to allow calls to this file from files that are installed locally. Flash Player 8 introduced security restrictions on local files. By default, a SWF file running in Flash Player that is allowed to access the Internet cannot also have access to the local file system. In Flash Player, if you specify "<code>localhost</code>", any local SWF file can access this SWF file.</li></ul>
   *
   * @throws ArgumentError All parameters specified must be non-null strings.
   *
   * @see #allowInsecureDomain()
   * @see flash.display.LoaderInfo#url
   * @see flash.system.Security#allowDomain()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118666ade46-7c7e.html Communicating with other Flash Player and AIR instances
   *
   */
  "public function allowDomain",function allowDomain(/*...domains*/)/*:void*/ {var domains=arguments;
    throw new Error('not implemented'); // TODO: implement!
},
/**
 * Specifies one or more domains that can send LocalConnection calls to this LocalConnection object.
 * <p>The <code>allowInsecureDomain()</code> method works just like the <code>allowDomain()</code> method, except that the <code>allowInsecureDomain()</code> method additionally permits SWF files from non-HTTPS origins to send LocalConnection calls to files from HTTPS origins. This difference is meaningful only if you call the <code>allowInsecureDomain()</code> method from a file that was loaded using HTTPS. You must call the <code>allowInsecureDomain()</code> method even if you are crossing a non-HTTPS/HTTPS boundary within the same domain; by default, LocalConnection calls are never permitted from non-HTTPS files to HTTPS files, even within the same domain.</p>
 * <p>Calling <code>allowInsecureDomain()</code> is not recommended, because it can compromise the security offered by HTTPS. When you load a file over HTTPS, you can be reasonably sure that the file will not be tampered with during delivery over the network. If you then permit a non-HTTPS file to make LocalConnection calls to the HTTPS file, you are accepting calls from a file that may in fact have been tampered with during delivery. This generally requires extra vigilance because you cannot trust the authenticity of LocalConnection calls arriving at your HTTPS file.</p>
 * <p>By default, files hosted using the HTTPS protocol can be accessed only by other files hosted using the HTTPS protocol. This implementation maintains the integrity provided by the HTTPS protocol.</p>
 * <p>Using this method to override the default behavior is not recommended, because it compromises HTTPS security. However, you might need to do so, for example, if you need to permit access to HTTPS SWF files published for Flash Player 9 or later from HTTP files SWF published for Flash Player 6 or earlier.</p>
 * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
 * @param domains One or more strings that name the domains from which you want to allow LocalConnection calls. There are two special cases for this parameter:
 * <ul>
 * <li>You can specify the wildcard character "*" to allow calls from all domains. Specifying "*" does not include local hosts.</li>
 * <li>You can specify the string "<code>localhost</code>" to allow calls to this SWF file from SWF files that are installed locally. Flash Player 8 introduced security restrictions on local SWF files. A SWF file that is allowed to access the Internet cannot also have access to the local file system. If you specify "<code>localhost</code>", any local SWF file can access this SWF file. Remember that you must also designate the calling SWF file as a local-with-networking SWF file at authoring time.</li></ul>
 *
 * @throws ArgumentError All parameters specified must be non-null strings.
 *
 * @see #allowDomain()
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118666ade46-7c7e.html Communicating with other Flash Player and AIR instances
 *
 */
"public function allowInsecureDomain",function allowInsecureDomain(/*...domains*/)/*:void*/ {var domains=arguments;
  throw new Error('not implemented'); // TODO: implement!
},
/**
 * Closes (disconnects) a LocalConnection object. Issue this command when you no longer want the object to accept commands — for example, when you want to issue a <code>connect()</code> command using the same <code>connectionName</code> parameter in another SWF file.
 * @throws ArgumentError The LocalConnection instance is not connected, so it cannot be closed.
 *
 * @see #connect()
 *
 */
"public function close",function close()/*:void*/ {
  throw new Error('not implemented'); // TODO: implement!
},
/**
 * Prepares a LocalConnection object to receive commands that are sent from a <code>send()</code> command (from the <i>sending LocalConnection object</i>). The object used with the <code>connect()</code> method is called the <i>receiving LocalConnection object</i>. The receiving and sending objects must be running on the same client computer.
 * <p>To avoid a race condition, define the methods attached to the receiving LocalConnection object before calling this method, as shown in the LocalConnection class example.</p>
 * <p>By default, the <code>connectionName</code> argument is resolved into a value of <code>"<i>superdomain</i>:connectionName"</code>, where <code><i>superdomain</i></code> is the superdomain of the file that contains the <code>connect()</code> command. For example, if the file that contains the receiving LocalConnection object is located at www.someDomain.com, <code>connectionName</code> resolves to <code>"someDomain.com:connectionName"</code>. (If a file running in Flash Player is located on the client computer, the value assigned to <code>superdomain</code> is <code>"localhost"</code>.)</p>
 * <p>In content running in the <code>application</code> security sandbox in Adobe AIR (content installed with the AIR application), the runtime uses the string <code>app#</code> followed by the application ID for the AIR application (defined in the application descriptor file) in place of the superdomain. For example a <code>connectionName</code> for an application with the application ID <code>com.example.air.MyApp</code> <code>connectionName</code> resolves to <code>"app#com.example.air.MyApp:connectionName"</code>.</p>
 * <p>Also by default, Flash Player lets the receiving LocalConnection object accept commands only from sending LocalConnection objects whose connection name also resolves into a value of <code>"</code><code><i>superdomain</i></code><code>:connectionName"</code>. In this way, Flash Player makes it simple for files that are located in the same domain to communicate with each other.</p>
 * <p>If you are implementing communication only between files in the same domain, specify a string for <code>connectionName</code> that does not begin with an underscore (_) and that does not specify a domain name (for example, <code>"myDomain:connectionName"</code>). Use the same string in the <code>connect(connectionName)</code> method.</p>
 * <p>If you are implementing communication between files in different domains, specifying a string for <code>connectionName</code> that begins with an underscore (_) makes the file with the receiving LocalConnection object more portable between domains. Here are the two possible cases:</p>
 * <ul>
 * <li>If the string for <code>connectionName</code>does not begin with an underscore (_), a prefix is added with the superdomain and a colon (for example, <code>"myDomain:connectionName"</code>). Although this ensures that your connection does not conflict with connections of the same name from other domains, any sending LocalConnection objects must specify this superdomain (for example, <code>"myDomain:connectionName"</code>). If the file with the receiving LocalConnection object is moved to another domain, the player changes the prefix to reflect the new superdomain (for example, <code>"anotherDomain:connectionName"</code>). All sending LocalConnection objects would have to be manually edited to point to the new superdomain.</li>
 * <li>If the string for <code>connectionName</code>begins with an underscore (for example, <code>"_connectionName"</code>), a prefix is not added to the string. This means that the receiving and sending LocalConnection objects use identical strings for <code>connectionName</code>. If the receiving object uses <code>allowDomain()</code> to specify that connections from any domain will be accepted, the file with the receiving LocalConnection object can be moved to another domain without altering any sending LocalConnection objects.</li></ul>
 * <p>For more information, see the discussion in the class overview and the discussion of <code>connectionName</code> in <code>send()</code>, and also the <code>allowDomain()</code> and <code>domain</code> entries.</p>
 * <p><b>Note:</b> Colons are used as special characters to separate the superdomain from the <code>connectionName</code> string. A string for <code>connectionName</code> that contains a colon is not valid.</p>
 * <p>When you use this method , consider the Flash Player security model. By default, a LocalConnection object is associated with the sandbox of the file that created it, and cross-domain calls to LocalConnection objects are not allowed unless you call the <code>LocalConnection.allowDomain()</code> method in the receiving file. You can prevent a file from using this method by setting the <code>allowNetworking</code> parameter of the the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content. However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
 * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
 * @param connectionName A string that corresponds to the connection name specified in the <code>send()</code> command that wants to communicate with the receiving LocalConnection object.
 *
 * @throws TypeError The value passed to the <code>connectionName</code> parameter must be non-null.
 * @throws ArgumentError This error can occur for three reasons: 1) The string value passed to the <code>connectionName</code> parameter was null. Pass a non-null value. 2) The value passed to the <code>connectionName</code> parameter contained a colon (:). Colons are used as special characters to separate the superdomain from the <code>connectionName</code> string in the <code>send()</code> method, not the <code>connect()</code>method. 3) The LocalConnection instance is already connected.
 *
 * @see #send()
 * @see #allowDomain()
 * @see #domain
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118666ade46-7c7e.html Communicating with other Flash Player and AIR instances
 *
 */
"public function connect",function connect(connectionName/*:String*/)/*:void*/ {
  throw new Error('not implemented'); // TODO: implement!
},
/**
 * Invokes the method named <code>methodName</code> on a connection that was opened with the <code>connect(connectionName)</code> method (in the <i>receiving LocalConnection object</i>). The object used with the <code>send()</code> method is called the <i>sending LocalConnection object</i>. The SWF files that contain the sending and receiving objects must be running on the same client computer.
 * <p>There is a 40 kilobyte limit to the amount of data you can pass as parameters to this command. If <code>send()</code> throws an <code>ArgumentError</code> but your syntax is correct, try dividing the <code>send()</code> requests into multiple commands, each with less than 40K of data.</p>
 * <p>As discussed in the <code>connect()</code> entry, the current superdomain in added to <code>connectionName</code> by default. If you are implementing communication between different domains, you need to define <code>connectionName</code> in both the sending and receiving LocalConnection objects in such a way that the current superdomain is not added to <code>connectionName</code>. You can do this in one of the following two ways:</p>
 * <ul>
 * <li>Use an underscore (_) at the beginning of <code>connectionName</code> in both the sending and receiving LocalConnection objects. In the file that contains the receiving object, use <code>LocalConnection.allowDomain()</code> to specify that connections from any domain will be accepted. This implementation lets you store your sending and receiving files in any domain.</li>
 * <li>Include the superdomain in <code>connectionName</code> in the sending LocalConnection object — for example, <code>myDomain.com:myConnectionName</code>. In the receiving object, use <code>LocalConnection.allowDomain()</code> to specify that connections from the specified superdomain will be accepted (in this case, myDomain.com) or that connections from any domain will be accepted.</li></ul>
 * <p><b>Note:</b> You cannot specify a superdomain in <code>connectionName</code> in the receiving LocalConnection object — you can do this in only the sending LocalConnection object.</p>
 * <p>When you use this method , consider the Flash Player security model. By default, a LocalConnection object is associated with the sandbox of the file that created it, and cross-domain calls to LocalConnection objects are not allowed unless you call the <code>LocalConnection.allowDomain()</code> method in the receiving file. For SWF content running in the browser, ou can prevent a file from using this method by setting the <code>allowNetworking</code> parameter of the the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content. However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
 * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
 * @param connectionName Corresponds to the connection name specified in the <code>connect()</code> command that wants to communicate with the sending LocalConnection object.
 * @param methodName The name of the method to be invoked in the receiving LocalConnection object. The following method names cause the command to fail: <code>send</code>, <code>connect</code>, <code>close</code>, <code>allowDomain</code>, <code>allowInsecureDomain</code>, <code>client</code>, and <code>domain</code>.
 * @param rest Additional optional parameters to be passed to the specified method.
 * Events
 * <table>
 * <tr>
 * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — <code>LocalConnection.send()</code> attempted to communicate with a SWF file from a security sandbox to which the calling code does not have access. You can work around this in the receiver's implementation of <code>LocalConnection.allowDomain()</code>.</td></tr>
 * <tr>
 * <td> </td></tr>
 * <tr>
 * <td><code><b>status</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/StatusEvent.html"><code>StatusEvent</code></a></code> — If the value of the <code>level</code> property is <code>"status"</code>, the call was successful; if the value is <code>"error"</code>, the call failed. The call can fail if the receiving SWF file refuses the connection.</td></tr></table>
 * @throws TypeError The value of either <code>connectionName</code> or <code>methodName</code> is null. Pass non-null values for these parameters.
 * @throws ArgumentError This error can occur for one of the following reasons: 1) The value of either <code>connectionName</code> or <code>methodName</code> is an empty string. Pass valid strings for these parameters. 2) The method specified in <code>methodName</code> is restricted. 3) The serialized message that is being sent is too large (larger than 40K).
 *
 * @see #allowDomain()
 * @see #connect()
 * @see #domain
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118666ade46-7c7e.html Communicating with other Flash Player and AIR instances
 *
 */
"public function send",function send(connectionName/*:String*/, methodName/*:String, ...rest*/)/*:void*/ {var rest=Array.prototype.slice.call(arguments,2);
  throw new Error('not implemented'); // TODO: implement!
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.1"
);