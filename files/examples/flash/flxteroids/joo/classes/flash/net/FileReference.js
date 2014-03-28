joo.classLoader.prepare("package flash.net",/* {
import flash.events.EventDispatcher*/

/**
 * Dispatched when a file upload or download is canceled through the file-browsing dialog box by the user. Flash Player does not dispatch this event if the user cancels an upload or download through other means (closing the browser or stopping the current application).
 * @eventType flash.events.Event.CANCEL
 */
{Event:{name:"cancel", type:"flash.events.Event"}},
/**
 * Dispatched when download is complete or when upload generates an HTTP status code of 200. For file download, this event is dispatched when Flash Player or Adobe AIR finishes downloading the entire file to disk. For file upload, this event is dispatched after the Flash Player or Adobe AIR receives an HTTP status code of 200 from the server receiving the transmission.
 * @eventType flash.events.Event.COMPLETE
 */
{Event:{name:"complete", type:"flash.events.Event"}},
/**
 * property HTTPStatusEvent.type =
 * @eventType flash.events.HTTPStatusEvent.HTTP_STATUS
 */
{Event:{name:"httpStatus", type:"flash.events.HTTPStatusEvent"}},
/**
 * property IOErrorEvent.type =
 * @eventType flash.events.IOErrorEvent.IO_ERROR
 */
{Event:{name:"ioError", type:"flash.events.IOErrorEvent"}},
/**
 * Dispatched when an upload or download operation starts.
 * @eventType flash.events.Event.OPEN
 */
{Event:{name:"open", type:"flash.events.Event"}},
/**
 * property ProgressEvent.type =
 * @eventType flash.events.ProgressEvent.PROGRESS
 */
{Event:{name:"progress", type:"flash.events.ProgressEvent"}},
/**
 * property SecurityErrorEvent.type =
 * @eventType flash.events.SecurityErrorEvent.SECURITY_ERROR
 */
{Event:{name:"securityError", type:"flash.events.SecurityErrorEvent"}},
/**
 * Dispatched when the user selects a file for upload or download from the file-browsing dialog box. (This dialog box opens when you call the <code>FileReference.browse()</code>, <code>FileReferenceList.browse()</code>, or <code>FileReference.download()</code> method.) When the user selects a file and confirms the operation (for example, by clicking OK), the properties of the FileReference object are populated.
 * <p>For content running in Flash Player or outside of the application security sandbox in the Adobe AIR runtime, the <code>select</code> event acts slightly differently depending on what method invokes it. When the <code>select</code> event is dispatched after a <code>browse()</code> call, Flash Player or the AIR application can read all the FileReference object's properties, because the file selected by the user is on the local file system. When the <code>select</code> event occurs after a <code>download()</code> call, Flash Player or the AIR application can read only the <code>name</code> property, because the file hasn't yet been downloaded to the local file system at the moment the <code>select</code> event is dispatched. When the file is downloaded and the <code>complete</code> event dispatched, Flash Player or the AIR application can read all other properties of the FileReference object.</p>
 * @eventType flash.events.Event.SELECT
 */
{Event:{name:"select", type:"flash.events.Event"}},
/**
 * property DataEvent.type =
 * @eventType flash.events.DataEvent.UPLOAD_COMPLETE_DATA
 */
{Event:{name:"uploadCompleteData", type:"flash.events.DataEvent"}},

/**
 * The FileReference class provides a means to upload and download files between a user's computer and a server. An operating-system dialog box prompts the user to select a file to upload or a location for download. Each FileReference object refers to a single file on the user's disk and has properties that contain information about the file's size, type, name, creation date, modification date, and creator type (Macintosh only).
 * <p><b>Note:</b> In Adobe AIR, the File class, which extends the FileReference class, provides more capabilities and has less security restrictions than the FileReference class.</p>
 * <p>FileReference instances are created in the following ways:</p>
 * <ul>
 * <li>When you use the <code>new</code> operator with the FileReference constructor: <code>var myFileReference = new FileReference();</code></li>
 * <li>When you call the <code>FileReferenceList.browse()</code> method, which creates an array of FileReference objects.</li></ul>
 * <p>During an upload operation, all the properties of a FileReference object are populated by calls to the <code>FileReference.browse()</code> or <code>FileReferenceList.browse()</code> methods. During a download operation, the <code>name</code> property is populated when the <code>select</code> event is dispatched; all other properties are populated when the <code>complete</code> event is dispatched.</p>
 * <p>The <code>browse()</code> method opens an operating-system dialog box that prompts the user to select a file for upload. The <code>FileReference.browse()</code> method lets the user select a single file; the <code>FileReferenceList.browse()</code> method lets the user select multiple files. After a successful call to the <code>browse()</code> method, call the <code>FileReference.upload()</code> method to upload one file at a time. The <code>FileReference.download()</code> method prompts the user for a location to save the file and initiates downloading from a remote URL.</p>
 * <p>The FileReference and FileReferenceList classes do not let you set the default file location for the dialog box that the <code>browse()</code> or <code>download()</code> methods generate. The default location shown in the dialog box is the most recently browsed folder, if that location can be determined, or the desktop. The classes do not allow you to read from or write to the transferred file. They do not allow the SWF file that initiated the upload or download to access the uploaded or downloaded file or the file's location on the user's disk.</p>
 * <p>The FileReference and FileReferenceList classes also do not provide methods for authentication. With servers that require authentication, you can download files with the Flash<sup>®</sup> Player browser plug-in, but uploading (on all players) and downloading (on the stand-alone or external player) fails. Listen for FileReference events to determine whether operations complete successfully and to handle errors.</p>
 * <p>For content running in Flash Player or for content running in Adobe AIR outside of the application security sandbox, uploading and downloading operations can access files only within its own domain and within any domains that a URL policy file specifies. Put a policy file on the file server if the content initiating the upload or download doesn't come from the same domain as the file server.</p>
 * <p>Note that because of new functionality added to the Flash Player, when publishing to Flash Player 10, you can have only one of the following operations active at one time: <code>FileReference.browse()</code>, <code>FileReference.upload()</code>, <code>FileReference.download()</code>, <code>FileReference.load()</code>, <code>FileReference.save()</code>. Otherwise, Flash Player throws a runtime error (code 2174). Use <code>FileReference.cancel()</code> to stop an operation in progress. This restriction applies only to Flash Player 10. Previous versions of Flash Player are unaffected by this restriction on simultaneous multiple operations.</p>
 * <p>While calls to the <code>FileReference.browse()</code>, <code>FileReferenceList.browse()</code>, or <code>FileReference.download()</code> methods are executing, SWF file playback pauses in stand-alone and external versions of Flash Player and in AIR for Linux and Mac OS X 10.1 and earlier</p>
 * <p>The following sample HTTP <code>POST</code> request is sent from Flash Player to a server-side script if no parameters are specified:</p>
 * <pre>  POST /handler.cfm HTTP/1.1
 Accept: text/*
 Content-Type: multipart/form-data;
 boundary=----------Ij5ae0ae0KM7GI3KM7
 User-Agent: Shockwave Flash
 Host: www.example.com
 Content-Length: 421
 Connection: Keep-Alive
 Cache-Control: no-cache

 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7
 Content-Disposition: form-data; name="Filename"

 MyFile.jpg
 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7
 Content-Disposition: form-data; name="Filedata"; filename="MyFile.jpg"
 Content-Type: application/octet-stream

 FileDataHere
 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7
 Content-Disposition: form-data; name="Upload"

 Submit Query
 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7--
 </pre>
 * <p>Flash Player sends the following HTTP <code>POST</code> request if the user specifies the parameters <code>"api_sig"</code>, <code>"api_key"</code>, and <code>"auth_token"</code>:</p>
 * <pre>  POST /handler.cfm HTTP/1.1
 Accept: text/*
 Content-Type: multipart/form-data;
 boundary=----------Ij5ae0ae0KM7GI3KM7
 User-Agent: Shockwave Flash
 Host: www.example.com
 Content-Length: 421
 Connection: Keep-Alive
 Cache-Control: no-cache

 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7
 Content-Disposition: form-data; name="Filename"

 MyFile.jpg
 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7
 Content-Disposition: form-data; name="api_sig"

 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7
 Content-Disposition: form-data; name="api_key"

 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7
 Content-Disposition: form-data; name="auth_token"

 XXXXXXXXXXXXXXXXXXXXXX
 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7
 Content-Disposition: form-data; name="Filedata"; filename="MyFile.jpg"
 Content-Type: application/octet-stream

 FileDataHere
 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7
 Content-Disposition: form-data; name="Upload"

 Submit Query
 ------------Ij5GI3GI3ei4GI3ei4KM7GI3KM7KM7--
 </pre>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/FileReference.html#includeExamplesSummary">View the examples</a></p>
 * @see FileReferenceList
 * @see flash.filesystem.File
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cf8.html Using the FileReference class
 *
 */
"public class FileReference extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * The creation date of the file on the local disk. If the object is was not populated, a call to get the value of this property returns <code>null</code>.
   * @throws flash.errors.IllegalOperationError If the <code>FileReference.browse()</code>, <code>FileReferenceList.browse()</code>, or <code>FileReference.download()</code> method was not called successfully, an exception is thrown with a message indicating that functions were called in the incorrect sequence or an earlier call was unsuccessful. In this case, the value of the <code>creationDate</code> property is <code>null</code>.
   * @throws flash.errors.IOError If the file information cannot be accessed, an exception is thrown with a message indicating a file I/O error.
   *
   * @see #browse()
   *
   */
  "public function get creationDate",function creationDate$get()/*:Date*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The Macintosh creator type of the file, which is only used in Mac OS versions prior to Mac OS X. In Windows or Linux, this property is <code>null</code>. If the FileReference object was not populated, a call to get the value of this property returns <code>null</code>.
   * @throws flash.errors.IllegalOperationError On Macintosh, if the <code>FileReference.browse()</code>, <code>FileReferenceList.browse()</code>, or <code>FileReference.download()</code> method was not called successfully, an exception is thrown with a message indicating that functions were called in the incorrect sequence or an earlier call was unsuccessful. In this case, the value of the <code>creator</code> property is <code>null</code>.
   *
   * @see #browse()
   * @see #extension
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cf8.html Using the FileReference class
   *
   */
  "public function get creator",function creator$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The date that the file on the local disk was last modified. If the FileReference object was not populated, a call to get the value of this property returns <code>null</code>.
   * @throws flash.errors.IllegalOperationError If the <code>FileReference.browse()</code>, <code>FileReferenceList.browse()</code>, or <code>FileReference.download()</code> method was not called successfully, an exception is thrown with a message indicating that functions were called in the incorrect sequence or an earlier call was unsuccessful. In this case, the value of the <code>modificationDate</code> property is <code>null</code>.
   * @throws flash.errors.IOError If the file information cannot be accessed, an exception is thrown with a message indicating a file I/O error.
   *
   * @see #browse()
   *
   */
  "public function get modificationDate",function modificationDate$get()/*:Date*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The name of the file on the local disk. If the FileReference object was not populated (by a valid call to <code>FileReference.download()</code> or <code>FileReference.browse()</code>), Flash Player throws an error when you try to get the value of this property.
   * <p>All the properties of a FileReference object are populated by calling the <code>browse()</code> method. Unlike other FileReference properties, if you call the <code>download()</code> method, the <code>name</code> property is populated when the <code>select</code> event is dispatched.</p>
   * @throws flash.errors.IllegalOperationError If the <code>FileReference.browse()</code>, <code>FileReferenceList.browse()</code>, or <code>FileReference.download()</code> method was not called successfully, an exception is thrown with a message indicating that functions were called in the incorrect sequence or an earlier call was unsuccessful.
   *
   * @see #browse()
   *
   */
  "public function get name",function name$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The size of the file on the local disk in bytes. If <code>size</code> is 0, an exception is thrown.
   * <p><i>Note:</i> In the initial version of ActionScript 3.0, the <code>size</code> property was defined as a uint object, which supported files with sizes up to about 4 GB. It is now implimented as a Number object to support larger files.</p>
   * @throws flash.errors.IllegalOperationError If the <code>FileReference.browse()</code>, <code>FileReferenceList.browse()</code>, or <code>FileReference.download()</code> method was not called successfully, an exception is thrown with a message indicating that functions were called in the incorrect sequence or an earlier call was unsuccessful.
   * @throws flash.errors.IOError If the file cannot be opened or read, or if a similar error is encountered in accessing the file, an exception is thrown with a message indicating a file I/O error.
   *
   * @see #browse()
   *
   */
  "public function get size",function size$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The file type.
   * <p>In Windows or Linux, this property is the file extension. On the Macintosh, this property is the four-character file type, which is only used in Mac OS versions prior to Mac OS X. If the FileReference object was not populated, a call to get the value of this property returns <code>null</code>.</p>
   * <p>For Windows, Linux, and Mac OS X, the file extension — the portion of the <code>name</code> property that follows the last occurrence of the dot (.) character — identifies the file type.</p>
   * @throws flash.errors.IllegalOperationError If the <code>FileReference.browse()</code>, <code>FileReferenceList.browse()</code>, or <code>FileReference.download()</code> method was not called successfully, an exception is thrown with a message indicating that functions were called in the incorrect sequence or an earlier call was unsuccessful. In this case, the value of the <code>type</code> property is <code>null</code>.
   *
   * @see #extension
   *
   */
  "public function get type",function type$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new FileReference object. When populated, a FileReference object represents a file on the user's local disk.
   * @see #browse()
   *
   */
  "public function FileReference",function FileReference$() {this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Displays a file-browsing dialog box that lets the user select a file to upload. The dialog box is native to the user's operating system. The user can select a file on the local computer or from other systems, for example, through a UNC path on Windows.
   * <p><b>Note:</b> The File class, available in Adobe AIR, includes methods for accessing more specific system file selection dialog boxes. These methods are <code>File.browseForDirectory()</code>, <code>File.browseForOpen()</code>, <code>File.browseForOpenMultiple()</code>, and <code>File.browseForSave()</code>.</p>
   * <p>When you call this method and the user successfully selects a file, the properties of this FileReference object are populated with the properties of that file. Each subsequent time that the <code>FileReference.browse()</code> method is called, the FileReference object's properties are reset to the file that the user selects in the dialog box. Only one <code>browse()</code> or <code>download()</code> session can be performed at a time (because only one dialog box can be invoked at a time).</p>
   * <p>Using the <code>typeFilter</code> parameter, you can determine which files the dialog box displays.</p>
   * <p>In Flash Player 10 and Flash Player 9 Update 5, you can only call this method successfully in response to a user event (for example, in an event handler for a mouse click or keypress event). Otherwise, calling this method results in Flash Player throwing an Error exception.</p>
   * <p>Note that because of new functionality added to the Flash Player, when publishing to Flash Player 10, you can have only one of the following operations active at one time: <code>FileReference.browse()</code>, <code>FileReference.upload()</code>, <code>FileReference.download()</code>, <code>FileReference.load()</code>, <code>FileReference.save()</code>. Otherwise, Flash Player throws a runtime error (code 2174). Use <code>FileReference.cancel()</code> to stop an operation in progress. This restriction applies only to Flash Player 10. Previous versions of Flash Player are unaffected by this restriction on simultaneous multiple operations.</p>
   * @param typeFilter An array of FileFilter instances used to filter the files that are displayed in the dialog box. If you omit this parameter, all files are displayed. For more information, see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/FileFilter.html">FileFilter</a> class.
   *
   * @return Returns <code>true</code> if the parameters are valid and the file-browsing dialog box opens.
   * Events
   * <table>
   * <tr>
   * <td><code><b>select</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when the user successfully selects an item from the Browse file chooser.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>cancel</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when the user cancels the file upload Browse window.</td></tr></table>
   * @throws flash.errors.IllegalOperationError Thrown in the following situations: 1) Another FileReference or FileReferenceList browse session is in progress; only one file browsing session may be performed at a time. 2) A setting in the user's mms.cfg file prohibits this operation.
   * @throws ArgumentError If the <code>typeFilter</code> array contains FileFilter objects that are incorrectly formatted, an exception is thrown. For information on the correct format for FileFilter objects, see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/FileFilter.html">FileFilter</a> class.
   * @throws Error If the method is not called in response to a user action, such as a mouse event or keypress event.
   *
   * @see FileReferenceList#event:select
   * @see #event:cancel
   * @see #download()
   * @see FileReferenceList#browse()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cf8.html Using the FileReference class
   *
   */
  "public function browse",function browse(typeFilter/*:Array = null*/)/*:Boolean*/ {if(arguments.length<1){typeFilter = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Cancels any ongoing upload or download operation on this FileReference object. Calling this method does not dispatch the <code>cancel</code> event; that event is dispatched only when the user cancels the operation by dismissing the file upload or download dialog box.
   */
  "public function cancel",function cancel()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Opens a dialog box that lets the user download a file from a remote server. Although Flash Player has no restriction on the size of files you can upload or download, the player officially supports uploads or downloads of up to 100 MB.
   * <p>The <code>download()</code> method first opens an operating-system dialog box that asks the user to enter a filename and select a location on the local computer to save the file. When the user selects a location and confirms the download operation (for example, by clicking Save), the download from the remote server begins. Listeners receive events to indicate the progress, success, or failure of the download. To ascertain the status of the dialog box and the download operation after calling <code>download()</code>, your code must listen for events such as <code>cancel</code>, <code>open</code>, <code>progress</code>, and <code>complete</code>.</p>
   * <p>The <code>FileReference.upload()</code> and <code>FileReference.download()</code> functions are nonblocking. These functions return after they are called, before the file transmission is complete. In addition, if the FileReference object goes out of scope, any upload or download that is not yet completed on that object is canceled upon leaving the scope. Be sure that your FileReference object remains in scope for as long as the upload or download is expected to continue.</p>
   * <p>When the file is downloaded successfully, the properties of the FileReference object are populated with the properties of the local file. The <code>complete</code> event is dispatched if the download is successful.</p>
   * <p>Only one <code>browse()</code> or <code>download()</code> session can be performed at a time (because only one dialog box can be invoked at a time).</p>
   * <p>This method supports downloading of any file type, with either HTTP or HTTPS.</p>
   * <p>You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.</p>
   * <p><b>Note</b>: If your server requires user authentication, only SWF files running in a browser — that is, using the browser plug-in or ActiveX control — can provide a dialog box to prompt the user for a user name and password for authentication, and only for downloads. For uploads using the plug-in or ActiveX control, or for uploads and downloads using the stand-alone or external player, the file transfer fails.</p>
   * <p>When you use this method , consider the Flash Player security model:</p>
   * <ul>
   * <li>Loading operations are not allowed if the calling SWF file is in an untrusted local sandbox.</li>
   * <li>The default behavior is to deny access between sandboxes. A website can enable access to a resource by adding a URL policy file.</li>
   * <li>You can prevent a SWF file from using this method by setting the <code>allowNetworking</code> parameter of the the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content.</li>
   * <li>In Flash Player 10 and Flash Player 9 Update 5, you can only call this method successfully in response to a user event (for example, in an event handler for a mouse click or keypress event). Otherwise, calling this method results in Flash Player throwing an Error exception.</li></ul>
   * <p>However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) is not restricted by these security limitations.</p>
   * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * <p>When you download a file using this method, it is flagged as downloaded on operating systems that flag downloaded files:</p>
   * <ul>
   * <li>Windows XP service pack 2 and later, and on Windows Vista</li>
   * <li>Mac OS 10.5 and later</li></ul>
   * <p>Some operating systems, such as Linux, do not flag downloaded files.</p>
   * <p>Note that because of new functionality added to the Flash Player, when publishing to Flash Player 10, you can have only one of the following operations active at one time: <code>FileReference.browse()</code>, <code>FileReference.upload()</code>, <code>FileReference.download()</code>, <code>FileReference.load()</code>, <code>FileReference.save()</code>. Otherwise, Flash Player throws a runtime error (code 2174). Use <code>FileReference.cancel()</code> to stop an operation in progress. This restriction applies only to Flash Player 10. Previous versions of Flash Player are unaffected by this restriction on simultaneous multiple operations.</p>
   * @param request The URLRequest object. The <code>url</code> property of the URLRequest object should contain the URL of the file to download to the local computer. If this parameter is <code>null</code>, an exception is thrown. The <code>requestHeaders</code> property of the URLRequest object is ignored; custom HTTP request headers are not supported in uploads or downloads. To send <code>POST</code> or GET parameters to the server, set the value of <code>URLRequest.data</code> to your parameters, and set <code>URLRequest.method</code> to either <code>URLRequestMethod.POST</code> or <code>URLRequestMethod.GET</code>.
   * <p>On some browsers, URL strings are limited in length. Lengths greater than 256 characters may fail on some browsers or servers.</p>
   * @param defaultFileName The default filename displayed in the dialog box for the file to be downloaded. This string must not contain the following characters: / \ : * ? " < > | %
   * <p>If you omit this parameter, the filename of the remote URL is parsed and used as the default.</p>
   * Events
   * <table>
   * <tr>
   * <td><code><b>open</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when a download operation starts.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>progress</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/ProgressEvent.html"><code>ProgressEvent</code></a></code> — Dispatched periodically during the file download operation.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>complete</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when the file download operation successfully completes.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>cancel</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when the user dismisses the dialog box.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>select</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when the user selects a file for download from the dialog box.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — Dispatched when a download fails because of a security error.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>ioError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/IOErrorEvent.html"><code>IOErrorEvent</code></a></code> — Dispatched for any of the following reasons:
   * <ul>
   * <li>An input/output error occurs while the file is being read or transmitted.</li>
   * <li>SWF content running in the stand-alone or external versions of Flash Player tries to download a file from a server that requires authentication. During download, the standalone and external players do not provide a means for users to enter passwords. If a SWF file in these players tries to download a file from a server that requires authentication, the download fails. File download can succeed only in the ActiveX control and browser plug-in players.</li></ul></td></tr></table>
   * @throws flash.errors.IllegalOperationError Thrown in the following situations: 1) Another browse session is in progress; only one file browsing session can be performed at a time. 2) The value passed to <code>request</code> does not contain a valid path or protocol. 3) The filename to download contains prohibited characters. 4) A setting in the user's mms.cfg file prohibits this operation.
   * @throws SecurityError Local untrusted content may not communicate with the Internet. To avoid this situation, reclassify this SWF file as local-with-networking or trusted. This exception is thrown with a message indicating the filename and the URL that may not be accessed because of local file security restrictions.
   * @throws SecurityError You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.
   * @throws ArgumentError If <code>url.data</code> is of type ByteArray, an exception is thrown. For use with the <code>FileReference.upload()</code> and <code>FileReference.download()</code> methods, <code>url.data</code> can only be of type URLVariables or String.
   * @throws flash.errors.MemoryError This error can occur for the following reasons: 1) Flash Player cannot convert the <code>URLRequest.data</code> parameter from UTF8 to MBCS. This error is applicable if the URLRequest object passed to the <code>FileReference.download()</code> method is set to perform a GET operation and if <code>System.useCodePage</code> is set to <code>true</code>. 2) Flash Player cannot allocate memory for the <code>POST</code> data. This error is applicable if the URLRequest object passed to the <code>FileReference.download()</code> method is set to perform a <code>POST</code> operation.
   * @throws Error If the method is not called in response to a user action, such as a mouse event or keypress event.
   *
   * @see flash.filesystem.File#downloaded
   * @see #browse()
   * @see FileReferenceList#browse()
   * @see #upload()
   * @see #save()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cf8.html Using the FileReference class
   *
   * @example The following example shows usage of the <code>download</code> event object. To run this example, change the <code>downloadURL.url</code> property to point to an actual domain and file, rather than the fictional http://www.[yourDomain].com/SomeFile.pdf. You might also need to compile the SWF file with Local playback security set to Access network only or to update Flash Player security settings to allow this file network access.
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.*;
   *     import flash.net.FileReference;
   *     import flash.net.URLRequest;
   *     import flash.net.FileFilter;
   *
   *     public class FileReference_download extends Sprite {
   *         private var downloadURL:URLRequest;
   *         private var fileName:String = "SomeFile.pdf";
   *         private var file:FileReference;
   *
   *         public function FileReference_download() {
   *             downloadURL = new URLRequest();
   *             downloadURL.url = "http://www.[yourDomain].com/SomeFile.pdf";
   *             file = new FileReference();
   *             configureListeners(file);
   *             file.download(downloadURL, fileName);
   *         }
   *
   *         private function configureListeners(dispatcher:IEventDispatcher):void {
   *             dispatcher.addEventListener(Event.CANCEL, cancelHandler);
   *             dispatcher.addEventListener(Event.COMPLETE, completeHandler);
   *             dispatcher.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
   *             dispatcher.addEventListener(Event.OPEN, openHandler);
   *             dispatcher.addEventListener(ProgressEvent.PROGRESS, progressHandler);
   *             dispatcher.addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
   *             dispatcher.addEventListener(Event.SELECT, selectHandler);
   *         }
   *
   *         private function cancelHandler(event:Event):void {
   *             trace("cancelHandler: " + event);
   *         }
   *
   *         private function completeHandler(event:Event):void {
   *             trace("completeHandler: " + event);
   *         }
   *
   *         private function ioErrorHandler(event:IOErrorEvent):void {
   *             trace("ioErrorHandler: " + event);
   *         }
   *
   *         private function openHandler(event:Event):void {
   *             trace("openHandler: " + event);
   *         }
   *
   *         private function progressHandler(event:ProgressEvent):void {
   *             var file:FileReference = FileReference(event.target);
   *             trace("progressHandler name=" + file.name + " bytesLoaded=" + event.bytesLoaded + " bytesTotal=" + event.bytesTotal);
   *         }
   *
   *         private function securityErrorHandler(event:SecurityErrorEvent):void {
   *             trace("securityErrorHandler: " + event);
   *         }
   *
   *         private function selectHandler(event:Event):void {
   *             var file:FileReference = FileReference(event.target);
   *             trace("selectHandler: name=" + file.name + " URL=" + downloadURL.url);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function download",function download(request/*:URLRequest*/, defaultFileName/*:String = null*/)/*:void*/ {if(arguments.length<2){defaultFileName = null;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Starts the upload of the file to a remote server. Although Flash Player has no restriction on the size of files you can upload or download, the player officially supports uploads or downloads of up to 100 MB. You must call the <code>FileReference.browse()</code> or <code>FileReferenceList.browse()</code> method before you call this method.
   * <p>For the Adobe AIR File class, which extends the FileReference class, you can use the <code>upload()</code> method to upload any file. For the FileReference class (used in Flash Player), the user must first select a file.</p>
   * <p>Listeners receive events to indicate the progress, success, or failure of the upload. Although you can use the FileReferenceList object to let users select multiple files for upload, you must upload the files one by one; to do so, iterate through the <code>FileReferenceList.fileList</code> array of FileReference objects.</p>
   * <p>The <code>FileReference.upload()</code> and <code>FileReference.download()</code> functions are nonblocking. These functions return after they are called, before the file transmission is complete. In addition, if the FileReference object goes out of scope, any upload or download that is not yet completed on that object is canceled upon leaving the scope. Be sure that your FileReference object remains in scope for as long as the upload or download is expected to continue.</p>
   * <p>The file is uploaded to the URL passed in the <code>url</code> parameter. The URL must be a server script configured to accept uploads. Flash Player uploads files by using the HTTP <code>POST</code> method. The server script that handles the upload should expect a <code>POST</code> request with the following elements:</p>
   * <ul>
   * <li><code>Content-Type</code> of <code>multipart/form-data</code></li>
   * <li><code>Content-Disposition</code> with a <code>name</code> attribute set to <code>"Filedata"</code> by default and a <code>filename</code> attribute set to the name of the original file</li>
   * <li>The binary contents of the file</li></ul>
   * <p>You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.</p>
   * <p>For a sample <code>POST</code> request, see the description of the <code>uploadDataFieldName</code> parameter. You can send <code>POST</code> or <code>GET</code> parameters to the server with the <code>upload()</code> method; see the description of the <code>request</code> parameter.</p>
   * <p>If the <code>testUpload</code> parameter is <code>true</code>, and the file to be uploaded is bigger than approximately 10 KB, Flash Player on Windows first sends a test upload <code>POST</code> operation with zero content before uploading the actual file, to verify that the transmission is likely to succeed. Flash Player then sends a second <code>POST</code> operation that contains the actual file content. For files smaller than 10 KB, Flash Player performs a single upload <code>POST</code> with the actual file content to be uploaded. Flash Player on Macintosh does not perform test upload <code>POST</code> operations.</p>
   * <p><b>Note</b>: If your server requires user authentication, only SWF files running in a browser — that is, using the browser plug-in or ActiveX control — can provide a dialog box to prompt the user for a username and password for authentication, and only for downloads. For uploads using the plug-in or ActiveX control, or for uploads and downloads using the stand-alone or external player, the file transfer fails.</p>
   * <p>When you use this method , consider the Flash Player security model:</p>
   * <ul>
   * <li>Loading operations are not allowed if the calling SWF file is in an untrusted local sandbox.</li>
   * <li>The default behavior is to deny access between sandboxes. A website can enable access to a resource by adding a URL policy file.</li>
   * <li>You can prevent a SWF file from using this method by setting the <code>allowNetworking</code> parameter of the the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content.</li></ul>
   * <p>However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
   * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * <p>Note that because of new functionality added to the Flash Player, when publishing to Flash Player 10, you can have only one of the following operations active at one time: <code>FileReference.browse()</code>, <code>FileReference.upload()</code>, <code>FileReference.download()</code>, <code>FileReference.load()</code>, <code>FileReference.save()</code>. Otherwise, Flash Player throws a runtime error (code 2174). Use <code>FileReference.cancel()</code> to stop an operation in progress. This restriction applies only to Flash Player 10. Previous versions of Flash Player are unaffected by this restriction on simultaneous multiple operations.</p>
   * @param request The URLRequest object; the <code>url</code> property of the URLRequest object should contain the URL of the server script configured to handle upload through HTTP <code>POST</code> calls. On some browsers, URL strings are limited in length. Lengths greater than 256 characters may fail on some browsers or servers. If this parameter is <code>null</code>, an exception is thrown. The <code>requestHeaders</code> property of the URLRequest object is ignored; custom HTTP request headers are not supported in uploads or downloads.
   * <p>The URL can be HTTP or, for secure uploads, HTTPS. To use HTTPS, use an HTTPS url in the <code>url</code> parameter. If you do not specify a port number in the <code>url</code> parameter, port 80 is used for HTTP and port 443 us used for HTTPS, by default.</p>
   * <p>To send <code>POST</code> or <code>GET</code> parameters to the server, set the <code>data</code> property of the URLRequest object to your parameters, and set the <code>method</code> property to either <code>URLRequestMethod.POST</code> or <code>URLRequestMethod.GET</code>.</p>
   * @param uploadDataFieldName The field name that precedes the file data in the upload <code>POST</code> operation. The <code>uploadDataFieldName</code> value must be non-null and a non-empty String. By default, the value of <code>uploadDataFieldName</code> is <code>"Filedata"</code>, as shown in the following sample <code>POST</code> request:
   * <pre>    Content-Type: multipart/form-data; boundary=AaB03x
   --AaB03x
   Content-Disposition: form-data; name="Filedata"; filename="example.jpg"
   Content-Type: application/octet-stream
   ... contents of example.jpg ...
   --AaB03x--
   </pre>
   * @param testUpload A setting to request a test file upload. If <code>testUpload</code> is <code>true</code>, for files larger than 10 KB, Flash Player attempts a test file upload <code>POST</code> with a Content-Length of 0. The test upload checks whether the actual file upload will be successful and that server authentication, if required, will succeed. A test upload is only available for Windows players.
   * Events
   * <table>
   * <tr>
   * <td><code><b>open</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when an upload operation starts.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>progress</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/ProgressEvent.html"><code>ProgressEvent</code></a></code> — Dispatched periodically during the file upload operation.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>complete</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Dispatched when the file upload operation completes successfully.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>uploadCompleteData</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/DataEvent.html"><code>DataEvent</code></a></code> — Dispatched when data has been received from the server after a successful file upload.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>securityError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/SecurityErrorEvent.html"><code>SecurityErrorEvent</code></a></code> — Dispatched when an upload fails because of a security violation.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>httpStatus</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/HTTPStatusEvent.html"><code>HTTPStatusEvent</code></a></code> — Dispatched when an upload fails because of an HTTP error.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>httpResponseStatus</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/HTTPStatusEvent.html"><code>HTTPStatusEvent</code></a></code> — The upload operation completes successfully and the server returns a response URL and response headers.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>ioError</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/IOErrorEvent.html"><code>IOErrorEvent</code></a></code> — Invoked in any of the following situations:
   * <ul>
   * <li>The upload fails because of an input/output error while Flash Player or Adobe AIR is reading, writing, or transmitting the file.</li>
   * <li>The upload fails because an attempt to upload a file to a server that requires authentication (such as a user name and password). During upload, no mean is provided for users to enter passwords.</li>
   * <li>The upload fails because the <code>url</code> parameter contains an invalid protocol. <code>FileReference.upload()</code> must use HTTP or HTTPS.</li></ul></td></tr></table>
   * @throws SecurityError Local untrusted SWF files may not communicate with the Internet. To avoid this situation, reclassify this SWF file as local-with-networking or trusted. This exception is thrown with a message indicating the name of the local file and the URL that may not be accessed.
   * @throws SecurityError You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.
   * @throws flash.errors.IllegalOperationError Thrown in the following situations: 1) Another FileReference or FileReferenceList browse session is in progress; only one file browsing session may be performed at a time. 2) The URL parameter is not a valid path or protocol. File upload must use HTTP, and file download must use FTP or HTTP. 3) The <code>uploadDataFieldName</code> parameter is set to <code>null</code>. 4) A setting in the user's mms.cfg file prohibits this operation.
   * @throws ArgumentError Thrown in the following situations: 1) The <code>uploadDataFieldName</code> parameter is an empty string. 2) <code>url.data</code> is of type ByteArray. For use with the <code>FileReference.upload()</code> and <code>FileReference.download()</code> methods, <code>url.data</code> may only be of type URLVariables or String. 3) In the AIR runtime (in the application security sandbox), the method of the URLRequest is not GET or POST (use <code>uploadEncoded()</code> instead).
   * @throws flash.errors.MemoryError This error can occur for the following reasons: 1) Flash Player cannot convert the <code>URLRequest.data</code> parameter from UTF8 to MBCS. This error is applicable if the URLRequest object passed to <code>FileReference.upload()</code> is set to perform a GET operation and if <code>System.useCodePage</code> is set to <code>true</code>. 2) Flash Player cannot allocate memory for the <code>POST</code> data. This error is applicable if the URLRequest object passed to <code>FileReference.upload()</code> is set to perform a <code>POST</code> operation.
   *
   * @see #browse()
   * @see FileReferenceList#browse()
   * @see #download()
   * @see FileReferenceList#fileList
   * @see #load()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cf8.html Using the FileReference class
   *
   */
  "public function upload",function upload(request/*:URLRequest*/, uploadDataFieldName/*:String = "Filedata"*/, testUpload/*:Boolean = false*/)/*:void*/ {if(arguments.length<3){if(arguments.length<2){uploadDataFieldName = "Filedata";}testUpload = false;}
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.1"
);