joo.classLoader.prepare("package flash.printing",/* {*/


/**
 * The PrintJobOptions class contains properties to use with the <code>options</code> parameter of the <code>PrintJob.addPage()</code> method. For more information about <code>addPage()</code>, see the PrintJob class.
 * @see PrintJob
 * @see PrintJob#addPage()
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cc2.html Flash runtime tasks and system printing
 *
 */
"public class PrintJobOptions",1,function($$private){;return[ 
  /**
   * Specifies whether the content in the print job is printed as a bitmap or as a vector. The default value is <code>false</code>, for vector printing.
   * <p>If the content that you're printing includes a bitmap image, set <code>printAsBitmap</code> to <code>true</code> to include any alpha transparency and color effects. If the content does not include bitmap images, print the content in higher quality vector format (the default option).</p>
   * <p>For example, to print your content as a bitmap, use the following syntax:</p>
   * <listing>
   *          var options:PrintJobOptions = new PrintJobOptions();
   *          options.printAsBitmap = true;
   *          myPrintJob.addPage(mySprite, null, options);
   *         </listing>
   * <p><i>Note:</i>Adobe AIR does not support vector printing on Mac OS.</p>
   * @see #printMethod
   *
   * @example The following example first loads a picture and puts it in a rectangle frame, then print the picture as a bitmap. <ol>
   * <li>The constructor loads the picture (<code>image.jpg</code>) using the <code>Loader</code> and <code>URLRequest</code> objects. It also checks if an error occurred during loading. Here the file is assumed to be in the same directory as the SWF file. The SWF file needs to be compiled with Local Playback Secuirty set to Access Local Files Only.</li>
   * <li>When the picture is loaded (the event is complete), the <code>completeHandler()</code> method is called.</li>
   * <li>The <code>completeHandler()</code> method, creates a <code>BitmapData</code> object, and loads the picture (bitmap) in it. A rectangle is drawn in the <code>Sprite</code> object (<code>frame</code>) and the <code>beginBitmapFill()</code> method is used to fill the rectangle with the picture (a <code>BitmapData</code> object). A <code>Matrix</code> object also is used to scale the image to fit the rectangle. (Note that this will distort the image. It is used in this example to make sure the image fits.) Once the image is filled, the <code>printPage()</code> method is called.</li>
   * <li>The <code>printPage()</code> method creates a new instance of the print job and starts the printing process, which invokes the print dialog box for the user, and populates the properties of the print job. The <code>addPage()</code> method contains the details about the print job. Here, the frame with the picture (a Sprite object) is set to print as a bitmap and not as a vector. <code>options</code> is an instance of <code>PrintJobOptions</code> class and its property <code>printAsBitmap</code> is set to <code>true</code> in order to print as a bitmap (default setting is false).</li></ol>
   * <p>Note: There is very limited error handling defined for this example.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.Loader;
   *     import flash.display.Bitmap;
   *     import flash.display.BitmapData;
   *     import flash.printing.PrintJob;
   *     import flash.printing.PrintJobOptions;
   *     import flash.events.Event;
   *     import flash.events.IOErrorEvent;
   *     import flash.net.URLRequest;
   *     import flash.geom.Matrix;
   *
   *     public class printAsBitmapExample extends Sprite {
   *
   *         private var frame:Sprite = new Sprite();
   *         private var url:String = "image.jpg";
   *         private var loader:Loader = new Loader();
   *
   *         public function printAsBitmapExample() {
   *
   *            var request:URLRequest = new URLRequest(url);
   *
   *            loader.load(request);
   *            loader.contentLoaderInfo.addEventListener(Event.COMPLETE, completeHandler);
   *            loader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
   *         }
   *
   *         private function completeHandler(event:Event):void {
   *
   *             var picture:Bitmap = Bitmap(loader.content);
   *             var bitmap:BitmapData = picture.bitmapData;
   *
   *             var matrix:Matrix = new Matrix();
   *
   *             matrix.scale((200 / bitmap.width), (200 / bitmap.height));
   *
   *             frame.graphics.lineStyle(10);
   *             frame.graphics.beginBitmapFill(bitmap, matrix, true);
   *             frame.graphics.drawRect(0, 0, 200, 200);
   *             frame.graphics.endFill();
   *
   *             addChild(frame);
   *
   *             printPage();
   *         }
   *
   *         private function ioErrorHandler(event:IOErrorEvent):void {
   *             trace("Unable to load the image: " + url);
   *         }
   *
   *         private function printPage ():void {
   *             var myPrintJob:PrintJob = new PrintJob();
   *             var options:PrintJobOptions = new PrintJobOptions();
   *             options.printAsBitmap = true;
   *
   *             myPrintJob.start();
   *
   *             try {
   *                 myPrintJob.addPage(frame, null, options);
   *             }
   *             catch(e:Error) {
   *                 trace ("Had problem adding the page to print job: " + e);
   *             }
   *
   *             try {
   *             myPrintJob.send();
   *             }
   *             catch (e:Error) {
   *                 trace ("Had problem printing: " + e);
   *             }
   *         }
   *     }
   * }
   *
   * </listing>
   */
  "public var",{ printAsBitmap/*:Boolean*/ : false},

  /**
   * Creates a new PrintJobOptions object. Pass this object to the <code>options</code> parameter of the <code>PrintJob.addPage()</code> method.
   * @param printAsBitmap If <code>true</code>, this object is printed as a bitmap. If <code>false</code>, this object is printed as a vector.
   * <p>If the content that you're printing includes a bitmap image, set the <code>printAsBitmap</code> property to <code>true</code> to include any alpha transparency and color effects. If the content does not include bitmap images, omit this parameter to print the content in higher quality vector format (the default option).</p>
   * <p><i>Note:</i>Adobe AIR does not support vector printing on Mac OS.</p>
   *
   * @see PrintJob#addPage()
   *
   */
  "public function PrintJobOptions",function PrintJobOptions$(printAsBitmap/*:Boolean = false*/) {if(arguments.length<1){printAsBitmap = false;}
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.3"
);