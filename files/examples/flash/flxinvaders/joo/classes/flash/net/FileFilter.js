joo.classLoader.prepare("package flash.net",/* {*/


/**
 * The FileFilter class is used to indicate what files on the user's system are shown in the file-browsing dialog box that is displayed when the <code>FileReference.browse()</code> method, the <code>FileReferenceList.browse()</code> method is called or a browse method of a File, FileReference, or FileReferenceList object is called. FileFilter instances are passed as a value for the optional <code>typeFilter</code> parameter to the method. If you use a FileFilter instance, extensions and file types that aren't specified in the FileFilter instance are filtered out; that is, they are not available to the user for selection. If no FileFilter object is passed to the method, all files are shown in the dialog box.
 * <p>You can use FileFilter instances in one of two ways:</p>
 * <ul>
 * <li>A description with file extensions only</li>
 * <li>A description with file extensions and Macintosh file types</li></ul>
 * <p>The two formats are not interchangeable within a single call to the browse method. You must use one or the other.</p>
 * <p>You can pass one or more FileFilter instances to the browse method, as shown in the following:</p>
 * <listing>
 *  var imagesFilter:FileFilter = new FileFilter("Images", "*.jpg;*.gif;*.png");
 *  var docFilter:FileFilter = new FileFilter("Documents", "*.pdf;*.doc;*.txt");
 *  var myFileReference:FileReference = new FileReference();
 *  myFileReference.browse([imagesFilter, docFilter]);
 * </listing>
 * <p>Or in an AIR application:</p>
 * <listing>
 *  var imagesFilter:FileFilter = new FileFilter("Images", "*.jpg;*.gif;*.png");
 *  var docFilter:FileFilter = new FileFilter("Documents", "*.pdf;*.doc;*.txt");
 *  var myFile:File = new File();
 *  myFile.browseForOpen("Open", [imagesFilter, docFilter]);
 * </listing>
 * <p>The list of extensions in the <code>FileFilter.extension</code> property is used to filter the files shown in the file browsing dialog. The list is not actually displayed in the dialog box; to display the file types for users, you must list the file types in the description string as well as in the extension list. The description string is displayed in the dialog box in Windows and Linux. (It is not used on the Macintosh<sup>®</sup>.) On the Macintosh, if you supply a list of Macintosh file types, that list is used to filter the files. If not, the list of file extensions is used.</p>
 */
"public final class FileFilter",1,function($$private){;return[ 
  /**
   * The description string for the filter. The description is visible to the user in the dialog box that opens when <code>FileReference.browse()</code> or <code>FileReferenceList.browse()</code> is called. The description string contains a string, such as <code>"Images (*.gif, *.jpg, *.png)"</code>, that can help instruct the user on what file types can be uploaded or downloaded. Note that the actual file types that are supported by this FileReference object are stored in the <code>extension</code> property.
   */
  "public function get description",function description$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set description",function description$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A list of file extensions. This list indicates the types of files that you want to show in the file-browsing dialog box. (The list is not visible to the user; the user sees only the value of the <code>description</code> property.) The <code>extension</code> property contains a semicolon-delimited list of file extensions, with a wildcard (*) preceding each extension, as shown in the following string: <code>"*.jpg;*.gif;*.png"</code>.
   */
  "public function get extension",function extension$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set extension",function extension$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * A list of Macintosh file types. This list indicates the types of files that you want to show in the file-browsing dialog box. (This list itself is not visible to the user; the user sees only the value of the <code>description</code> property.) The <code>macType</code> property contains a semicolon-delimited list of Macintosh file types, as shown in the following string: <code>"JPEG;jp2_;GIFF"</code>.
   */
  "public function get macType",function macType$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set macType",function macType$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new FileFilter instance.
   * @param description The description string that is visible to users when they select files for uploading.
   * @param extension A list of file extensions that indicate which file formats are visible to users when they select files for uploading.
   * @param macType A list of Macintosh file types that indicate which file types are visible to users when they select files for uploading. If no value is passed, this parameter is set to <code>null</code>.
   *
   * @example The following example uploads an image from your local file system to the root display object (in this case, the stage). Example provided by <a href="http://www.andrevenancio.com/blog/">Andre Venancio</a>.
   * <listing>
   * var buttonShape:Shape = new Shape();
   * buttonShape.graphics.beginFill(0x336699);
   * buttonShape.graphics.drawCircle(50, 50, 25);
   * var button = new SimpleButton(buttonShape, buttonShape, buttonShape, buttonShape);
   * addChild(button);
   *
   * var fileRef:FileReference= new FileReference();
   * button.addEventListener(MouseEvent.CLICK, onButtonClick);
   *
   * function onButtonClick(e:MouseEvent):void {
   * fileRef.browse([new FileFilter("Images", "*.jpg;*.gif;*.png")]);
   * fileRef.addEventListener(Event.SELECT, onFileSelected);
   * }
   *
   * function onFileSelected(e:Event):void {
   * fileRef.addEventListener(Event.COMPLETE, onFileLoaded);
   * fileRef.load();
   * }
   *
   * function onFileLoaded(e:Event):void {
   * var loader:Loader = new Loader();
   * loader.loadBytes(e.target.data);
   * addChild(loader);
   * }
   * </listing>
   */
  "public function FileFilter",function FileFilter$(description/*:String*/, extension/*:String*/, macType/*:String = null*/) {if(arguments.length<3){macType = null;}
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.1"
);