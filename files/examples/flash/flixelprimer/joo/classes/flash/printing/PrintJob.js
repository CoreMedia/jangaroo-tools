joo.classLoader.prepare("package flash.printing",/* {
import flash.display.Sprite
import flash.events.EventDispatcher
import flash.geom.Rectangle*/

/**
 * The PrintJob class lets you create content and print it to one or more pages. This class lets you render content that is visible, dynamic or offscreen to the user, prompt users with a single Print dialog box, and print an unscaled document with proportions that map to the proportions of the content. This capability is especially useful for rendering and printing dynamic content, such as database content and dynamic text.
 * <p><b>Mobile Browser Support:</b> This class is not supported in mobile browsers.</p>
 * <p><i>AIR profile support:</i> This feature is supported on all desktop operating systems, but it is not supported on mobile devices or AIR for TV devices. You can test for support at run time using the <code>PrintJob.isSupported</code> property. See <a href="http://help.adobe.com/en_US/air/build/WS144092a96ffef7cc16ddeea2126bb46b82f-8000.html">AIR Profile Support</a> for more information regarding API support across multiple profiles.</p>
 * <p>Use the <code>PrintJob()</code> constructor to create a print job.</p>
 * <p>Additionally, with the PrintJob class's properties, you can read your user's printer settings, such as page height, width, and image orientation, and you can configure your document to dynamically format Flash content that is appropriate for the printer settings.</p>
 * <p><b>Note:</b> ActionScript 3.0 does not restrict a PrintJob object to a single frame (as did previous versions of ActionScript). However, since the operating system displays print status information to the user after the user has clicked the OK button in the Print dialog box, you should call <code>PrintJob.addPage()</code> and <code>PrintJob.send()</code> as soon as possible to send pages to the spooler. A delay reaching the frame containing the <code>PrintJob.send()</code> call delays the printing process.</p>
 * <p>Additionally, a 15 second script timeout limit applies to the following intervals:</p>
 * <ul>
 * <li><code>PrintJob.start()</code> and the first <code>PrintJob.addPage()</code></li>
 * <li><code>PrintJob.addPage()</code> and the next <code>PrintJob.addPage()</code></li>
 * <li>The last <code>PrintJob.addPage()</code> and <code>PrintJob.send()</code></li></ul>
 * <p>If any of the above intervals span more than 15 seconds, the next call to <code>PrintJob.start()</code> on the PrintJob instance returns <code>false</code>, and the next <code>PrintJob.addPage()</code> on the PrintJob instance causes the Flash Player or Adobe AIR to throw a runtime exception.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/printing/PrintJob.html#includeExamplesSummary">View the examples</a></p>
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cba.html Basics of printing
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cc7.html Printing a page
 *
 */
"public class PrintJob extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * The image orientation for printing. The acceptable values are defined as constants in the PrintJobOrientation class.
   * <p><b>Note:</b> For AIR 2 or later, set this property before starting a print job to set the default orientation in the Page Setup and Print dialogs. Set the property while a print job is in progress (after calling <code>start()</code> or <code>start2()</code> to set the orientation for a range of pages within the job.</p>
   * @see PrintJobOrientation
   *
   */
  "public function get orientation",function orientation$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set orientation",function orientation$set(value/*:String*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The height of the largest area which can be centered in the actual printable area on the page, in points. Any user-set margins are ignored. This property is available only after a call to the <code>PrintJob.start()</code> method has been made.
   * <p><b>Note:</b> For AIR 2 or later, this property is deprecated. Use <code>printableArea</code> instead, which measures the printable area in fractional points and describes off-center printable areas accurately.</p>
   * @see #printableArea
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cc6.html Setting size, scale, and orientation
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cb3.html Responding to page height and width
   *
   */
  "public function get pageHeight",function pageHeight$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The width of the largest area which can be centered in the actual printable area on the page, in points. Any user-set margins are ignored. This property is available only after a call to the <code>PrintJob.start()</code> method has been made.
   * <p><b>Note:</b> For AIR 2 or later, this property is deprecated. Use <code>printableArea</code> instead, which measures the printable area in fractional points and describes off-center printable areas accurately.</p>
   * @see #printableArea
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cc6.html Setting size, scale, and orientation
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cb3.html Responding to page height and width
   *
   */
  "public function get pageWidth",function pageWidth$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The overall paper height, in points. This property is available only after a call to the <code>PrintJob.start()</code> method has been made.
   * <p><b>Note:</b> For AIR 2 or later, this property is deprecated. Use <code>paperArea</code> instead, which measures the paper dimensions in fractional points.</p>
   * @see #paperArea
   *
   */
  "public function get paperHeight",function paperHeight$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The overall paper width, in points. This property is available only after a call to the <code>PrintJob.start()</code> method has been made.
   * <p><b>Note:</b> For AIR 2 or later, this property is deprecated. Use <code>paperArea</code> instead, which measures the paper dimensions in fractional points.</p>
   * @see #paperArea
   *
   */
  "public function get paperWidth",function paperWidth$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a PrintJob object that you can use to print one or more pages. After you create a PrintJob object, you need to use (in the following sequence) the <code>PrintJob.start()</code>, <code>PrintJob.addPage()</code>, and then <code>PrintJob.send()</code> methods to send the print job to the printer.
   * <p>For example, you can replace the <code>[params]</code> placeholder text for the <code>myPrintJob.addPage()</code> method calls with custom parameters as shown in the following code:</p>
   * <pre> // create PrintJob object
   var myPrintJob:PrintJob = new PrintJob();

   // display Print dialog box, but only initiate the print job
   // if start returns successfully.
   if (myPrintJob.start()) {

   // add specified page to print job
   // repeat once for each page to be printed
   try {
   myPrintJob.addPage([params]);
   }
   catch(e:Error) {
   // handle error
   }
   try {
   myPrintJob.addPage([params]);
   }
   catch(e:Error) {
   // handle error
   }

   // send pages from the spooler to the printer, but only if one or more
   // calls to addPage() was successful. You should always check for successful
   // calls to start() and addPage() before calling send().
   myPrintJob.send();
   }
   </pre>
   * <p>In AIR 2 or later, you can create and use multiple PrintJob instances. Properties set on the PrintJob instance are retained after printing completes. This allows you to re-use a PrintJob instance and maintain a user's selected printing preferences, while offering different printing preferences for other content in your application. For content in Flash Player and in AIR prior to version 2, you cannot create a second PrintJob object while the first one is still active. If you create a second PrintJob object (by calling <code>new PrintJob()</code>) while the first PrintJob object is still active, the second PrintJob object will not be created. So, you may check for the <code>myPrintJob</code> value before creating a second PrintJob.</p>
   * @throws flash.errors.IllegalOperationError In Flash Player and AIR prior to AIR 2, throws an exception if another PrintJob object is still active.
   *
   * @see #addPage()
   * @see #send()
   * @see #start()
   *
   */
  "public function PrintJob",function PrintJob$() {this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sends the specified Sprite object as a single page to the print spooler. Before using this method, you must create a PrintJob object and then use <code>start()</code> or <code>start2()</code>. Then, after calling <code>addPage()</code> one or more times for a print job, use <code>send()</code> to send the spooled pages to the printer. In other words, after you create a PrintJob object, use (in the following sequence) <code>start()</code> or <code>start2()</code>, <code>addPage()</code>, and then <code>send()</code> to send the print job to the printer. You can call <code>addPage()</code> multiple times after a single call to <code>start()</code> to print several pages in a print job.
   * <p>If <code>addPage()</code> causes Flash Player to throw an exception (for example, if you haven't called <code>start()</code> or the user cancels the print job), any subsequent calls to <code>addPage()</code> fail. However, if previous calls to <code>addPage()</code> are successful, the concluding <code>send()</code> command sends the successfully spooled pages to the printer.</p>
   * <p>If the print job takes more than 15 seconds to complete an <code>addPage()</code> operation, Flash Player throws an exception on the next <code>addPage()</code> call.</p>
   * <p>If you pass a value for the <code>printArea</code> parameter, the <code><i>x</i></code> and <code><i>y</i></code> coordinates of the <code>printArea</code> Rectangle map to the upper-left corner (0, 0 coordinates) of the printable area on the page. The read-only properties <code>pageHeight</code> and <code>pageWidth</code> describe the printable area set by <code>start()</code>. Because the printout aligns with the upper-left corner of the printable area on the page, when the area defined in <code>printArea</code> is bigger than the printable area on the page, the printout is cropped to the right or bottom (or both) of the area defined by <code>printArea</code>. In Flash Professional, if you don't pass a value for <code>printArea</code> and the Stage is larger than the printable area, the same type of clipping occurs. In Flex or Flash Builder, if you don't pass a value for <code>printArea</code> and the screen is larger than the printable area, the same type of clipping takes place.</p>
   * <p>If you want to scale a Sprite object before you print it, set scale properties (see <code>flash.display.DisplayObject.scaleX</code> and <code>flash.display.DisplayObject.scaleY</code>) before calling this method, and set them back to their original values after printing. The scale of a Sprite object has no relation to <code>printArea</code>. That is, if you specify a print area that is 50 x 50 pixels, 2500 pixels are printed. If you scale the Sprite object, the same 2500 pixels are printed, but the Sprite object is printed at the scaled size.</p>
   * <p>The Flash Player printing feature supports PostScript and non-PostScript printers. Non-PostScript printers convert vectors to bitmaps.</p>
   * @param sprite The Sprite containing the content to print.
   * @param printArea A Rectangle object that specifies the area to print.
   * <p>A rectangle's width and height are pixel values. A printer uses points as print units of measurement. Points are a fixed physical size (1/72 inch), but the size of a pixel, onscreen, depends on the resolution of the particular screen. So, the conversion rate between pixels and points depends on the printer settings and whether the sprite is scaled. An unscaled sprite that is 72 pixels wide prints out one inch wide, with one point equal to one pixel, independent of screen resolution.</p>
   * <p>You can use the following equivalencies to convert inches or centimeters to twips or points (a twip is 1/20 of a point):</p>
   * <ul>
   * <li>1 point = 1/72 inch = 20 twips</li>
   * <li>1 inch = 72 points = 1440 twips</li>
   * <li>1 cm = 567 twips</li></ul>
   * <p>If you omit the <code>printArea</code> parameter, or if it is passed incorrectly, the full area of the <code>sprite</code> parameter is printed.</p>
   * <p>If you don't want to specify a value for <code>printArea</code> but want to specify a value for <code>options</code> or <code>frameNum</code>, pass <code>null</code> for <code>printArea</code>.</p>
   * @param options An optional parameter that specifies whether to print as vector or bitmap. The default value is <code>null</code>, which represents a request for vector printing. To print <code>sprite</code> as a bitmap, set the <code>printAsBitmap</code> property of the PrintJobOptions object to <code>true</code>. Remember the following suggestions when determining whether to set <code>printAsBitmap</code> to <code>true</code>:
   * <ul>
   * <li>If the content you're printing includes a bitmap image, set <code>printAsBitmap</code> to <code>true</code> to include any alpha transparency and color effects.</li>
   * <li>If the content does not include bitmap images, omit this parameter to print the content in higher quality vector format.</li></ul>
   * <p>If <code>options</code> is omitted or is passed incorrectly, vector printing is used. If you don't want to specify a value for <code>options</code> but want to specify a value for <code>frameNumber</code>, pass <code>null</code> for <code>options</code>.</p>
   * @param frameNum An optional number that lets you specify which frame of a MovieClip object to print. Passing a <code>frameNum</code> does not invoke ActionScript on that frame. If you omit this parameter and the <code>sprite</code> parameter is a MovieClip object, the current frame in <code>sprite</code> is printed.
   *
   * @throws Error Throws an exception if you haven't called <code>start()</code> or the user cancels the print job
   *
   * @see #send()
   * @see #start()
   * @see flash.display.DisplayObject
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cc7.html Printing a page
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cc6.html Setting size, scale, and orientation
   *
   */
  "public function addPage",function addPage(sprite/*:Sprite*/, printArea/*:Rectangle = null*/, options/*:PrintJobOptions = null*/, frameNum/*:int = 0*/)/*:void*/ {if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){printArea = null;}options = null;}frameNum = 0;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Sends spooled pages to the printer after successful calls to the <code>start()</code> or <code>start2()</code> and <code>addPage()</code> methods.
   * <p>This method does not succeed if the call to the <code>start()</code> or <code>start2()</code> method fails, or if a call to the <code>addPage()</code> method throws an exception. To avoid an error, check that the <code>start()</code> or <code>start2()</code> method returns <code>true</code> and catch any <code>addPage()</code> exceptions before calling this method. The following example demonstrates how to properly check for errors before calling this method:</p>
   * <listing>
   *      var myPrintJob:PrintJob = new PrintJob();
   *      if (myPrintJob.start()) {
   *        try {
   *          myPrintJob.addPage([params]);
   *        }
   *        catch(e:Error) {
   *           // handle error
   *        }
   *
   *        myPrintJob.send();
   *      }
   *     </listing>
   * @see #addPage()
   * @see #start()
   * @see #start2()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cc7.html Printing a page
   *
   */
  "public function send",function send()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Displays the operating system's Print dialog box and starts spooling. The Print dialog box lets the user change print settings. When the <code>PrintJob.start()</code> method returns successfully (the user clicks OK in the Print dialog box), the following properties are populated, representing the user's chosen print settings:
   * <table>
   * <tr><th>Property</th><th>Type</th><th>Units</th><th>Notes</th></tr>
   * <tr>
   * <td><code>PrintJob.paperHeight</code></td>
   * <td>Number</td>
   * <td>Points</td>
   * <td>Overall paper height.</td></tr>
   * <tr>
   * <td><code>PrintJob.paperWidth</code></td>
   * <td>Number</td>
   * <td>Points</td>
   * <td>Overall paper width.</td></tr>
   * <tr>
   * <td><code>PrintJob.pageHeight</code></td>
   * <td>Number</td>
   * <td>Points</td>
   * <td>Height of actual printable area on the page; any user-set margins are ignored.</td></tr>
   * <tr>
   * <td><code>PrintJob.pageWidth</code></td>
   * <td>Number</td>
   * <td>Points</td>
   * <td>Width of actual printable area on the page; any user-set margins are ignored.</td></tr>
   * <tr>
   * <td><code>PrintJob.orientation</code></td>
   * <td>String</td>
   * <td><code>"portrait"</code> (<code>flash.printing.PrintJobOrientation.PORTRAIT</code>) or <code>"landscape"</code> (<code>flash.printing.PrintJobOrientation.LANDSCAPE</code>).</td></tr></table>
   * <p><b>Note:</b> If the user cancels the Print dialog box, the properties are not populated.</p>
   * <p>After the user clicks OK in the Print dialog box, the player begins spooling a print job to the operating system. Because the operating system then begins displaying information to the user about the printing progress, you should call the <code>PrintJob.addPage()</code> and <code>PrintJob.send()</code> calls as soon as possible to send pages to the spooler. You can use the read-only height, width, and orientation properties this method populates to format the printout.</p>
   * <p>Test to see if this method returns <code>true</code> (when the user clicks OK in the operating system's Print dialog box) before any subsequent calls to <code>PrintJob.addPage()</code> and <code>PrintJob.send()</code>:</p>
   * <listing>
   *      var myPrintJob:PrintJob = new PrintJob();
   *         if(myPrintJob.start()) {
   *           // addPage() and send() statements here
   *         }
   *     </listing>
   * <p>For the given print job instance, if any of the following intervals last more than 15 seconds the next call to <code>PrintJob.start()</code> will return <code>false</code>:</p>
   * <ul>
   * <li><code>PrintJob.start()</code> and the first <code>PrintJob.addPage()</code></li>
   * <li>One <code>PrintJob.addPage()</code> and the next <code>PrintJob.addPage()</code></li>
   * <li>The last <code>PrintJob.addPage()</code> and <code>PrintJob.send()</code></li></ul>
   * @return A value of <code>true</code> if the user clicks OK when the Print dialog box appears; <code>false</code> if the user clicks Cancel or if an error occurs.
   *
   * @throws flash.errors.IllegalOperationError in AIR 2 or later, if another PrintJob is currently active
   *
   * @see #addPage()
   * @see #send()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cc7.html Printing a page
   *
   */
  "public function start",function start()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.3"
);