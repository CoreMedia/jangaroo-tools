joo.classLoader.prepare("package flash.net",/* {
import flash.events.EventDispatcher*/

/**
 * Dispatched when the user dismisses the file-browsing dialog box. (This dialog box opens when you call the <code>FileReferenceList.browse()</code>, <code>FileReference.browse()</code>, or <code>FileReference.download()</code> methods.)
 * @eventType flash.events.Event.CANCEL
 */
{Event:{name:"cancel", type:"flash.events.Event"}},
/**
 * Dispatched when the user selects one or more files to upload from the file-browsing dialog box. (This dialog box opens when you call the <code>FileReferenceList.browse()</code>, <code>FileReference.browse()</code>, or <code>FileReference.download()</code> methods.) When the user selects a file and confirms the operation (for example, by clicking Save), the <code>FileReferenceList</code> object is populated with FileReference objects that represent the files that the user selects.
 * @eventType flash.events.Event.SELECT
 */
{Event:{name:"select", type:"flash.events.Event"}},

/**
 * The FileReferenceList class provides a means to let users select one or more files for uploading. A FileReferenceList object represents a group of one or more local files on the user's disk as an array of FileReference objects. For detailed information and important considerations about FileReference objects and the FileReference class, which you use with FileReferenceList, see the FileReference class.
 * <p>To work with the FileReferenceList class:</p>
 * <ul>
 * <li>Instantiate the class: <code>var myFileRef = new FileReferenceList();</code></li>
 * <li>Call the <code>FileReferenceList.browse()</code> method, which opens a dialog box that lets the user select one or more files for upload: <code>myFileRef.browse();</code></li>
 * <li>After the <code>browse()</code> method is called successfully, the <code>fileList</code> property of the FileReferenceList object is populated with an array of FileReference objects.</li>
 * <li>Call <code>FileReference.upload()</code> on each element in the <code>fileList</code> array.</li></ul>
 * <p>The FileReferenceList class includes a <code>browse()</code> method and a <code>fileList</code> property for working with multiple files. While a call to <code>FileReferenceList.browse()</code> is executing, SWF file playback pauses in stand-alone and external versions of Flash Player and in AIR for Linux and Mac OS X 10.1 and earlier.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/FileReferenceList.html#includeExamplesSummary">View the examples</a></p>
 * @see FileReference
 *
 */
"public class FileReferenceList extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * An array of <code>FileReference</code> objects.
   * <p>When the <code>FileReferenceList.browse()</code> method is called and the user has selected one or more files from the dialog box that the <code>browse()</code> method opens, this property is populated with an array of FileReference objects, each of which represents the files the user selected. You can then use this array to upload each file with the <code>FileReference.upload()</code>method. You must upload one file at a time.</p>
   * <p>The <code>fileList</code> property is populated anew each time browse() is called on that FileReferenceList object.</p>
   * <p>The properties of <code>FileReference</code> objects are described in the FileReference class documentation.</p>
   * @see FileReference
   * @see FileReference#upload()
   * @see #browse()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cf8.html Using the FileReference class
   *
   */
  "public function get fileList",function fileList$get()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new FileReferenceList object. A FileReferenceList object contains nothing until you call the <code>browse()</code> method on it and the user selects one or more files. When you call <code>browse()</code> on the FileReference object, the <code>fileList</code> property of the object is populated with an array of <code>FileReference</code> objects.
   * @see FileReference
   * @see #browse()
   *
   */
  "public function FileReferenceList",function FileReferenceList$() {this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Displays a file-browsing dialog box that lets the user select one or more local files to upload. The dialog box is native to the user's operating system.
   * <p>In Flash Player 10 and later, you can call this method successfully only in response to a user event (for example, in an event handler for a mouse click or keypress event). Otherwise, calling this method results in Flash Player throwing an Error.</p>
   * <p>When you call this method and the user successfully selects files, the <code>fileList</code> property of this FileReferenceList object is populated with an array of FileReference objects, one for each file that the user selects. Each subsequent time that the FileReferenceList.browse() method is called, the <code>FileReferenceList.fileList</code> property is reset to the file(s) that the user selects in the dialog box.</p>
   * <p>Using the <code>typeFilter</code> parameter, you can determine which files the dialog box displays.</p>
   * <p>Only one <code>FileReference.browse()</code>, <code>FileReference.download()</code>, or <code>FileReferenceList.browse()</code> session can be performed at a time on a FileReferenceList object (because only one dialog box can be opened at a time).</p>
   * @param typeFilter An array of FileFilter instances used to filter the files that are displayed in the dialog box. If you omit this parameter, all files are displayed. For more information, see the <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/FileFilter.html">FileFilter</a> class.
   *
   * @return Returns <code>true</code> if the parameters are valid and the file-browsing dialog box opens.
   * Events
   * <table>
   * <tr>
   * <td><code><b>select</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Invoked when the user has successfully selected an item for upload from the dialog box.</td></tr>
   * <tr>
   * <td> </td></tr>
   * <tr>
   * <td><code><b>cancel</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html"><code>Event</code></a></code> — Invoked when the user dismisses the dialog box by clicking Cancel or by closing it.</td></tr></table>
   * @throws flash.errors.IllegalOperationError Thrown for the following reasons: 1) Another FileReference or FileReferenceList browse session is in progress; only one file browsing session may be performed at a time. 2) A setting in the user's mms.cfg file prohibits this operation.
   * @throws ArgumentError If the <code>typeFilter</code> array does not contain correctly formatted FileFilter objects, an exception is thrown. For details on correct filter formatting, see the FileFilter documentation.
   * @throws Error If the method is not called in response to a user action, such as a mouse event or keypress event.
   *
   * @see FileReference#browse()
   * @see FileReference
   * @see FileFilter
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cf8.html Using the FileReference class
   *
   */
  "public function browse",function browse(typeFilter/*:Array = null*/)/*:Boolean*/ {if(arguments.length<1){typeFilter = null;}
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.1"
);