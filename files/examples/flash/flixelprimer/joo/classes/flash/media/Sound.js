joo.classLoader.prepare("package flash.media",/* {

import flash.events.EventDispatcher
import flash.net.URLRequest

import js.Audio
import js.HTMLAudioElement*/

/**
 * Dispatched when data has loaded successfully.
 * @eventType flash.events.Event.COMPLETE
 */
{Event:{name:"complete", type:"flash.events.Event"}},
/**
 * Dispatched by a Sound object when ID3 data is available for an MP3 sound.
 * @eventType flash.events.Event.ID3
 */
{Event:{name:"id3", type:"flash.events.Event"}},
/**
 * Dispatched when an input/output error occurs that causes a load operation to fail.
 * @eventType flash.events.IOErrorEvent.IO_ERROR
 */
{Event:{name:"ioError", type:"flash.events.IOErrorEvent"}},
/**
 * Dispatched when a load operation starts.
 * @eventType flash.events.Event.OPEN
 */
{Event:{name:"open", type:"flash.events.Event"}},
/**
 * Dispatched when data is received as a load operation progresses.
 * @eventType flash.events.ProgressEvent.PROGRESS
 */
{Event:{name:"progress", type:"flash.events.ProgressEvent"}},

/**
 * The Sound class lets you work with sound in an application. The Sound class lets you create a Sound object, load and play an external MP3 file into that object, close the sound stream, and access data about the sound, such as information about the number of bytes in the stream and ID3 metadata. More detailed control of the sound is performed through the sound source � the SoundChannel or Microphone object for the sound � and through the properties in the SoundTransform class that control the output of the sound to the computer's speakers.
 * <p>In Flash Player 10 and later and AIR 1.5 and later, you can also use this class to work with sound that is generated dynamically. In this case, the Sound object uses the function you assign to a <code>sampleData</code> event handler to poll for sound data. The sound is played as it is retrieved from a ByteArray object that you populate with sound data. You can use <code>Sound.extract()</code> to extract sound data from a Sound object, after which you can manipulate it before writing it back to the stream for playback.</p>
 * <p>To control sounds that are embedded in a SWF file, use the properties in the SoundMixer class.</p>
 * <p><b>Note</b>: The ActionScript 3.0 Sound API differs from ActionScript 2.0. In ActionScript 3.0, you cannot take sound objects and arrange them in a hierarchy to control their properties.</p>
 * <p>When you use this class, consider the following security model:</p>
 * <ul>
 * <li>Loading and playing a sound is not allowed if the calling file is in a network sandbox and the sound file to be loaded is local.</li>
 * <li>By default, loading and playing a sound is not allowed if the calling file is local and tries to load and play a remote sound. A user must grant explicit permission to allow this type of access.</li>
 * <li>Certain operations dealing with sound are restricted. The data in a loaded sound cannot be accessed by a file in a different domain unless you implement a cross-domain policy file. Sound-related APIs that fall under this restriction are <code>Sound.id3</code>, <code>SoundMixer.computeSpectrum()</code>, <code>SoundMixer.bufferTime</code>, and the <code>SoundTransform</code> class.</li></ul>
 * <p>However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
 * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/media/Sound.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.net.NetStream
 * @see Microphone
 * @see SoundChannel
 * @see SoundMixer
 * @see SoundTransform
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d27.html Basics of working with sound
 *
 */
"public class Sound extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * Returns the currently available number of bytes in this sound object. This property is usually useful only for externally loaded files.
   */
  "public function get bytesLoaded",function bytesLoaded$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns the total number of bytes in this sound object.
   */
  "public function get bytesTotal",function bytesTotal$get()/*:int*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Provides access to the metadata that is part of an MP3 file.
   * <p>MP3 sound files can contain ID3 tags, which provide metadata about the file. If an MP3 sound that you load using the <code>Sound.load()</code> method contains ID3 tags, you can query these properties. Only ID3 tags that use the UTF-8 character set are supported.</p>
   * <p>Flash Player 9 and later and AIR support ID3 2.0 tags, specifically 2.3 and 2.4. The following tables list the standard ID3 2.0 tags and the type of content the tags represent. The <code>Sound.id3</code> property provides access to these tags through the format <code>my_sound.id3.COMM</code>, <code>my_sound.id3.TIME</code>, and so on. The first table describes tags that can be accessed either through the ID3 2.0 property name or the ActionScript property name. The second table describes ID3 tags that are supported but do not have predefined properties in ActionScript.</p>
   * <table>
   * <tr>
   * <td><b>ID3 2.0 tag</b></td>
   * <td><b>Corresponding Sound class property</b></td></tr>
   * <tr>
   * <td>COMM</td>
   * <td>Sound.id3.comment</td></tr>
   * <tr>
   * <td>TALB</td>
   * <td>Sound.id3.album</td></tr>
   * <tr>
   * <td>TCON</td>
   * <td>Sound.id3.genre</td></tr>
   * <tr>
   * <td>TIT2</td>
   * <td>Sound.id3.songName</td></tr>
   * <tr>
   * <td>TPE1</td>
   * <td>Sound.id3.artist</td></tr>
   * <tr>
   * <td>TRCK</td>
   * <td>Sound.id3.track</td></tr>
   * <tr>
   * <td>TYER</td>
   * <td>Sound.id3.year</td></tr></table>
   * <p>The following table describes ID3 tags that are supported but do not have predefined properties in the Sound class. You access them by calling <code>mySound.id3.TFLT</code>, <code>mySound.id3.TIME</code>, and so on. <b>NOTE:</b> None of these tags are supported in Flash Lite 4.</p>
   * <table>
   * <tr>
   * <td><b>Property</b></td>
   * <td><b>Description</b></td></tr>
   * <tr>
   * <td>TFLT</td>
   * <td>File type</td></tr>
   * <tr>
   * <td>TIME</td>
   * <td>Time</td></tr>
   * <tr>
   * <td>TIT1</td>
   * <td>Content group description</td></tr>
   * <tr>
   * <td>TIT2</td>
   * <td>Title/song name/content description</td></tr>
   * <tr>
   * <td>TIT3</td>
   * <td>Subtitle/description refinement</td></tr>
   * <tr>
   * <td>TKEY</td>
   * <td>Initial key</td></tr>
   * <tr>
   * <td>TLAN</td>
   * <td>Languages</td></tr>
   * <tr>
   * <td>TLEN</td>
   * <td>Length</td></tr>
   * <tr>
   * <td>TMED</td>
   * <td>Media type</td></tr>
   * <tr>
   * <td>TOAL</td>
   * <td>Original album/movie/show title</td></tr>
   * <tr>
   * <td>TOFN</td>
   * <td>Original filename</td></tr>
   * <tr>
   * <td>TOLY</td>
   * <td>Original lyricists/text writers</td></tr>
   * <tr>
   * <td>TOPE</td>
   * <td>Original artists/performers</td></tr>
   * <tr>
   * <td>TORY</td>
   * <td>Original release year</td></tr>
   * <tr>
   * <td>TOWN</td>
   * <td>File owner/licensee</td></tr>
   * <tr>
   * <td>TPE1</td>
   * <td>Lead performers/soloists</td></tr>
   * <tr>
   * <td>TPE2</td>
   * <td>Band/orchestra/accompaniment</td></tr>
   * <tr>
   * <td>TPE3</td>
   * <td>Conductor/performer refinement</td></tr>
   * <tr>
   * <td>TPE4</td>
   * <td>Interpreted, remixed, or otherwise modified by</td></tr>
   * <tr>
   * <td>TPOS</td>
   * <td>Part of a set</td></tr>
   * <tr>
   * <td>TPUB</td>
   * <td>Publisher</td></tr>
   * <tr>
   * <td>TRCK</td>
   * <td>Track number/position in set</td></tr>
   * <tr>
   * <td>TRDA</td>
   * <td>Recording dates</td></tr>
   * <tr>
   * <td>TRSN</td>
   * <td>Internet radio station name</td></tr>
   * <tr>
   * <td>TRSO</td>
   * <td>Internet radio station owner</td></tr>
   * <tr>
   * <td>TSIZ</td>
   * <td>Size</td></tr>
   * <tr>
   * <td>TSRC</td>
   * <td>ISRC (international standard recording code)</td></tr>
   * <tr>
   * <td>TSSE</td>
   * <td>Software/hardware and settings used for encoding</td></tr>
   * <tr>
   * <td>TYER</td>
   * <td>Year</td></tr>
   * <tr>
   * <td>WXXX</td>
   * <td>URL link frame</td></tr></table>
   * <p>When using this property, consider the Flash Player security model:</p>
   * <ul>
   * <li>The <code>id3</code> property of a Sound object is always permitted for SWF files that are in the same security sandbox as the sound file. For files in other sandboxes, there are security checks.</li>
   * <li>When you load the sound, using the <code>load()</code> method of the Sound class, you can specify a <code>context</code> parameter, which is a SoundLoaderContext object. If you set the <code>checkPolicyFile</code> property of the SoundLoaderContext object to <code>true</code>, Flash Player checks for a URL policy file on the server from which the sound is loaded. If a policy file exists and permits access from the domain of the loading SWF file, then the file is allowed to access the <code>id3</code> property of the Sound object; otherwise it is not.</li></ul>
   * <p>However, in Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
   * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * @see SoundLoaderContext#checkPolicyFile
   *
   * @example The following example reads the ID3 information from a sound file and displays it in a text field.
   * <p>In the constructor, the sound file is loaded but it is not set to play. Here, it is assumed that the file is in the SWF directory. The system must have permission in order to read the ID3 tags of a loaded sound file. If there is ID3 information in the file and the program is permitted to read it, an <code>Event.ID3</code> event will be fired and the <code>id3</code> property of the sound file will be populated. The <code>id3</code> property contains an <code>ID3Info</code> object with all of the ID3 information.</p>
   * <p>In the <code>id3Handler()</code> method, the file's ID3 tags are stored in <code>id3</code>, an ID3Info class object. A text field is instantiated to display the list of the ID3 tags. The for loop iterates through all the ID3 2.0 tags and appends the name and value to the content of the text field. Using ID3 info (<code>ID3Info</code>) properties, the artist, song name, and album are also appended. ActionScript 3.0 and Flash Player 9 and later support ID3 2.0 tags, specifically 2.3 and 2.4. If you iterate through properties like in the for loop, only ID3 2.0 tags will appear. However, the data from the earlier versions are also stored in the song's <code>id3</code> property and can be accessed using ID3 info class properties. The tags for the ID3 1.0 are at the end of the file while the ID3 2.0 tags are in the beginning of the file. (Sometimes, the files may have both earlier and later version tags in the same place.) If a file encoded with both version 1.0 and 2.0 tags at the beginning and the end of the file, the method <code>id3Handler()</code> will be invoked twice. It first reads the 2.0 version and then the version 1.0. If only ID3 1.0 tag is available, then the information is accessible via the ID3 info properties, like <code>id3.songname</code>. For ID3 2.0, <code>id3.TITS</code> property will retrieve the song name using the new tag (TITS).</p>
   * <p>Note that no error handling is written for this example and if the ID3 content is long, the result may go beyond the viewable area.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.media.Sound;
   *     import flash.net.URLRequest;
   *     import flash.media.ID3Info;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.events.Event;
   *
   *     public class Sound_id3Example extends Sprite {
   *         private var snd:Sound = new Sound();
   *         private var myTextField:TextField = new TextField();
   *
   *         public function Sound_id3Example() {
   *             snd.addEventListener(Event.ID3, id3Handler);
   *             snd.load(new URLRequest("mySound.mp3"));
   *         }
   *
   *         private function id3Handler(event:Event):void {
   *             var id3:ID3Info = snd.id3;
   *
   *             myTextField.autoSize = TextFieldAutoSize.LEFT;
   *             myTextField.border = true;
   *
   *             myTextField.appendText("Received ID3 Info: \n");
   *
   *             for (var propName:String in id3) {
   *                 myTextField.appendText(propName + " = " + id3[propName] + "\n");
   *             }
   *
   *             myTextField.appendText("\n" + "Artist: " + id3.artist + "\n");
   *             myTextField.appendText("Song name: " + id3.songName + "\n");
   *             myTextField.appendText("Album: " + id3.album + "\n\n");
   *
   *             this.addChild(myTextField);
   *         }
   *     }
   * }
   * </listing>
   */
  "public function get id3",function id3$get()/*:ID3Info*/ {
    return new flash.media.ID3Info();
  },

  /**
   * Returns the buffering state of external MP3 files. If the value is <code>true</code>, any playback is currently suspended while the object waits for more data.
   */
  "public function get isBuffering",function isBuffering$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The length of the current sound in milliseconds.
   */
  "public function get length",function length$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The URL from which this sound was loaded. This property is applicable only to Sound objects that were loaded using the <code>Sound.load()</code> method. For Sound objects that are associated with a sound asset from a SWF file's library, the value of the <code>url</code> property is <code>null</code>.
   * <p>When you first call <code>Sound.load()</code>, the <code>url</code> property initially has a value of <code>null</code>, because the final URL is not yet known. The <code>url</code> property will have a non-null value as soon as an <code>open</code> event is dispatched from the Sound object.</p>
   * <p>The <code>url</code> property contains the final, absolute URL from which a sound was loaded. The value of <code>url</code> is usually the same as the value passed to the <code>stream</code> parameter of <code>Sound.load()</code>. However, if you passed a relative URL to <code>Sound.load()</code> the value of the <code>url</code> property represents the absolute URL. Additionally, if the original URL request is redirected by an HTTP server, the value of the <code>url</code> property reflects the final URL from which the sound file was actually downloaded. This reporting of an absolute, final URL is equivalent to the behavior of <code>LoaderInfo.url</code>.</p>
   * <p>In some cases, the value of the <code>url</code> property is truncated; see the <code>isURLInaccessible</code> property for details.</p>
   * @see #load()
   * @see flash.display.LoaderInfo#url
   * @see #isURLInaccessible
   *
   */
  "public function get url",function url$get()/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a new Sound object. If you pass a valid URLRequest object to the Sound constructor, the constructor automatically calls the <code>load()</code> function for the Sound object. If you do not pass a valid URLRequest object to the Sound constructor, you must call the <code>load()</code> function for the Sound object yourself, or the stream will not load.
   * <p>Once <code>load()</code> is called on a Sound object, you can't later load a different sound file into that Sound object. To load a different sound file, create a new Sound object.</p>In Flash Player 10 and later and AIR 1.5 and later, instead of using <code>load()</code>, you can use the <code>sampleData</code> event handler to load sound dynamically into the Sound object.
   * @param stream The URL that points to an external MP3 file.
   * @param context An optional SoundLoader context object, which can define the buffer time (the minimum number of milliseconds of MP3 data to hold in the Sound object's buffer) and can specify whether the application should check for a cross-domain policy file prior to loading the sound.
   *
   */
  "public function Sound",function Sound$(stream/*:URLRequest = null*/, context/*:SoundLoaderContext = null*/) {if(arguments.length<2){if(arguments.length<1){stream = null;}context = null;}this.super$2();
    if (!this.audio) { // may have been set by subclass
      this.audio = new js.Audio();
      this.audio.addEventListener('error', function flash$media$Sound$290_39(e/*:**/)/*:void*/ {
        window.alert("error " + e);
      }, false);
      this.load(stream, context);
    }
  },

  /**
   * Closes the stream, causing any download of data to cease. No data may be read from the stream after the <code>close()</code> method is called.
   * @throws flash.errors.IOError The stream could not be closed, or the stream was not open.
   *
   * @example In the following example, when the user clicks on the Stop button, the <code>Sound.close()</code> method will be called and the sound will stop streaming.
   * <p>In the constructor, a text field is created for the Start and Stop button. When the user clicks on the text field, the <code>clickHandler()</code> method is invoked. It handles the starting and stopping of the sound file. Note that depending on the network connection or when the user clicks the Stop button, much of the file could already have been loaded and it may take a while for the sound file to stop playing. A <code>try...catch</code> block is used to catch any IO error that may occur while closing the stream. For example, if the sound is loaded from a local directory and not streamed, error 2029 is caught, stating, "This URLStream object does not have an open stream."</p>
   * <listing>
   *
   * package {
   *     import flash.display.Sprite;
   *     import flash.net.URLRequest;
   *     import flash.media.Sound;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.events.MouseEvent;
   *     import flash.errors.IOError;
   *     import flash.events.IOErrorEvent;
   *
   *     public class Sound_closeExample extends Sprite {
   *         private var snd:Sound = new Sound();
   *         private var button:TextField = new TextField();
   *         private var req:URLRequest = new URLRequest("http://av.adobe.com/podcast/csbu_dev_podcast_epi_2.mp3");
   *
   *         public function Sound_closeExample() {
   *             button.x = 10;
   *             button.y = 10;
   *             button.text = "START";
   *             button.border = true;
   *             button.background = true;
   *             button.selectable = false;
   *             button.autoSize = TextFieldAutoSize.LEFT;
   *
   *             button.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             this.addChild(button);
   *         }
   *
   *         private function clickHandler(e:MouseEvent):void {
   *
   *             if(button.text == "START") {
   *
   *                 snd.load(req);
   *                 snd.play();
   *
   *                 snd.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
   *
   *                 button.text = "STOP";
   *             }
   *             else if(button.text == "STOP") {
   *
   *                 try {
   *                     snd.close();
   *                     button.text = "Wait for loaded stream to finish.";
   *                 }
   *                 catch (error:IOError) {
   *                     button.text = "Couldn't close stream " + error.message;
   *                 }
   *             }
   *         }
   *
   *         private function errorHandler(event:IOErrorEvent):void {
   *                 button.text = "Couldn't load the file " + event.text;
   *         }
   *     }
   * }
   *
   *
   * </listing>
   */
  "public function close",function close()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Initiates loading of an external MP3 file from the specified URL. If you provide a valid URLRequest object to the Sound constructor, the constructor calls <code>Sound.load()</code> for you. You only need to call <code>Sound.load()</code> yourself if you don't pass a valid URLRequest object to the Sound constructor or you pass a <code>null</code> value.
   * <p>Once <code>load()</code> is called on a Sound object, you can't later load a different sound file into that Sound object. To load a different sound file, create a new Sound object.</p>
   * <p>When using this method, consider the following security model:</p>
   * <ul>
   * <li>Calling <code>Sound.load()</code> is not allowed if the calling file is in the local-with-file-system sandbox and the sound is in a network sandbox.</li>
   * <li>Access from the local-trusted or local-with-networking sandbox requires permission from a website through a URL policy file.</li>
   * <li>You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.</li>
   * <li>You can prevent a SWF file from using this method by setting the <code>allowNetworking</code> parameter of the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content.</li></ul>
   * <p>In Flash Player 10 and later, if you use a multipart Content-Type (for example "multipart/form-data") that contains an upload (indicated by a "filename" parameter in a "content-disposition" header within the POST body), the POST operation is subject to the security rules applied to uploads:</p>
   * <ul>
   * <li>The POST operation must be performed in response to a user-initiated action, such as a mouse click or key press.</li>
   * <li>If the POST operation is cross-domain (the POST target is not on the same server as the SWF file that is sending the POST request), the target server must provide a URL policy file that permits cross-domain access.</li></ul>
   * <p>Also, for any multipart Content-Type, the syntax must be valid (according to the RFC2046 standards). If the syntax appears to be invalid, the POST operation is subject to the security rules applied to uploads.</p>
   * <p>In Adobe AIR, content in the <code>application</code> security sandbox (content installed with the AIR application) are not restricted by these security limitations.</p>
   * <p>For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * @param stream A URL that points to an external MP3 file.
   * @param context An optional SoundLoader context object, which can define the buffer time (the minimum number of milliseconds of MP3 data to hold in the Sound object's buffer) and can specify whether the application should check for a cross-domain policy file prior to loading the sound.
   *
   * @throws flash.errors.IOError A network error caused the load to fail.
   * @throws SecurityError Local untrusted files may not communicate with the Internet. You can work around this by reclassifying this file as local-with-networking or trusted.
   * @throws SecurityError You cannot connect to commonly reserved ports. For a complete list of blocked ports, see "Restricting Networking APIs" in the <i>ActionScript 3.0 Developer's Guide</i>.
   * @throws flash.errors.IOError The <code>digest</code> property of the <code>stream</code> object is not <code>null</code>. You should only set the <code>digest</code> property of a URLRequest object when calling the <code>URLLoader.load()</code> method when loading a SWZ file (an Adobe platform component).
   *
   * @example The following example displays the loading progress of a sound file.
   * <p>In the constructor a <code>URLRequest</code> object is created to identify the location of the sound file, which is a podcast from Adobe. The file is loaded in a <code>try...catch</code> block in order to catch any error that may occur while loading the file. If an IO error occurred, the <code>errorHandler()</code> method also is invoked and the error message is written in the text field intended for the progress report. While a load operation is in progress, a <code>ProgressEvent.PROGRESS</code> event is dispatched and the <code>progressHandler()</code> method is called. Here, <code>ProgressEvent.PROGRESS</code> event is used as a timer for calculating the load progress.</p>
   * <p>The <code>progressHandler()</code> method divides the <code>bytesLoaded</code> value passed with the <code>ProgressEvent</code> object by the <code>bytesTotal</code> value to arrive at a percentage of the sound data that is being loaded. It then displays these values in the text field. (Note that if the file is small, cached, or the file is in the local directory, the progress may not be noticeable.)</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.net.URLRequest;
   *     import flash.media.Sound;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.events.ProgressEvent;
   *     import flash.events.IOErrorEvent;
   *
   *     public class Sound_loadExample extends Sprite {
   *         private var snd:Sound = new Sound();
   *         private var statusTextField:TextField  = new TextField();
   *
   *         public function Sound_loadExample(){
   *
   *             statusTextField.autoSize = TextFieldAutoSize.LEFT;
   *             var req:URLRequest = new URLRequest("http://av.adobe.com/podcast/csbu_dev_podcast_epi_2.mp3");
   *
   *             try {
   *             snd.load(req);
   *
   *             snd.play();
   *             }
   *             catch (err:Error) {
   *                 trace(err.message);
   *             }
   *
   *             snd.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
   *             snd.addEventListener(ProgressEvent.PROGRESS, progressHandler);
   *
   *             this.addChild(statusTextField);
   *         }
   *
   *         private function progressHandler(event:ProgressEvent):void {
   *             var loadTime:Number = event.bytesLoaded / event.bytesTotal;
   *             var LoadPercent:uint = Math.round(100 * loadTime);
   *
   *             statusTextField.text = "Sound file's size in bytes: " + event.bytesTotal + "\n"
   *                                  + "Bytes being loaded: " + event.bytesLoaded + "\n"
   *                                  + "Percentage of sound file that is loaded " + LoadPercent + "%.\n";
   *         }
   *
   *         private function errorHandler(errorEvent:IOErrorEvent):void {
   *             statusTextField.text = "The sound could not be loaded: " + errorEvent.text;
   *         }
   *     }
   * }
   * </listing>
   */
  "public function load",function load(stream/*:URLRequest*/, context/*:SoundLoaderContext = null*/)/*:void*/ {if(arguments.length<2){context = null;}
    if (stream && stream.url) {
      var url/*:String*/ = stream.url;
      var mp3ExtensionPos/*:int*/ = url.indexOf(".mp3");
      if (mp3ExtensionPos !== -1 && this.audio.canPlayType("audio/mp3")) {
        var newExtension/*:String*/ = this.audio.canPlayType("audio/ogg") ? ".ogg" : ".wav";
        url = url.substring(0, mp3ExtensionPos) + newExtension + url.substring(mp3ExtensionPos + 4);
      }
      this.audio.src = url;
      this.audio.load();
    }
  },

  /**
   * Generates a new SoundChannel object to play back the sound. This method returns a SoundChannel object, which you access to stop the sound and to monitor volume. (To control the volume, panning, and balance, access the SoundTransform object assigned to the sound channel.)
   * @param startTime The initial position in milliseconds at which playback should start.
   * @param loops Defines the number of times a sound loops back to the <code>startTime</code> value before the sound channel stops playback.
   * @param sndTransform The initial SoundTransform object assigned to the sound channel.
   *
   * @return A SoundChannel object, which you use to control the sound. This method returns <code>null</code> if you have no sound card or if you run out of available sound channels. The maximum number of sound channels available at once is 32.
   *
   * @see SoundChannel#stop()
   * @see SoundMixer#stopAll()
   *
   * @example In the following example, once the file is loaded, the user using a graphic bar can select the starting position (starting time) of the sound file.
   * <p>The constructor calls the <code>Sound.load()</code> method to start loading the sound data. Next it calls the <code>Sound.play()</code> method which will start playing the sound as soon as enough data has loaded. The <code>Sound.play()</code> method returns a SoundChannel object that can be used to control the playback of the sound. The text field displays the instructions. To make sure the content of where the user wants the sound to start, has already been loaded, the <code>bar</code> Sprite object is created and displayed after the file has finished loading. An <code>Event.COMPLETE</code> event is dispatched when the file is successfully loaded, which triggers the <code>completeHandler()</code> method. The <code>completeHandler()</code> method then creates the bar and adds it to the display list. (A sprite object is used instead of a shape object to support interactivity.) When the user clicks on the bar, the <code>clickHandler()</code> method is triggered.</p>
   * <p>In the <code>clickHandler()</code> method, the position of x coordinate of the user's click, <code>event.localX</code>, is used to determine where the user wants the file to start. Since the bar is 100 pixels and it starts at x coordinate 100 pixels, it is easy to determine the percentage of the position. Also, since the file is loaded, the <code>length</code> property of the sound file will have the length of the complete file in milliseconds. Using the length of the sound file and the position in the line, a starting position for the sound file is determined. After stopping the sound from playing, the sound file restarts at the selected starting position, which is past as the <code>startTime</code> parameter to the <code>play()</code> method.</p>
   * <listing>
   *
   * package {
   *     import flash.display.Sprite;
   *     import flash.display.Graphics;
   *     import flash.events.MouseEvent;
   *     import flash.media.Sound;;
   *     import flash.net.URLRequest;
   *     import flash.media.SoundChannel;
   *     import flash.events.ProgressEvent;
   *     import flash.events.Event;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.events.IOErrorEvent;
   *
   *     public class Sound_playExample1 extends Sprite {
   *         private var snd:Sound = new Sound();
   *         private var channel:SoundChannel = new SoundChannel();
   *         private var infoTextField:TextField = new TextField();
   *
   *         public function Sound_playExample1() {
   *
   *             var req:URLRequest = new URLRequest("MySound.mp3");
   *
   *             infoTextField.autoSize = TextFieldAutoSize.LEFT;
   *             infoTextField.text = "Please wait for the file to be loaded.\n"
   *                                 + "Then select from the bar to decide where the file should start.";
   *
   *             snd.load(req);
   *             channel = snd.play();
   *
   *             snd.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
   *             snd.addEventListener(Event.COMPLETE, completeHandler);
   *
   *
   *             this.addChild(infoTextField);
   *         }
   *
   *         private function completeHandler(event:Event):void {
   *             infoTextField.text = "File is ready.";
   *
   *             var bar:Sprite = new Sprite();
   *
   *             bar.graphics.lineStyle(5, 0xFF0000);
   *             bar.graphics.moveTo(100, 100);
   *             bar.graphics.lineTo(200, 100);
   *
   *             bar.addEventListener(MouseEvent.CLICK, clickHandler);
   *
   *             this.addChild(bar);
   *         }
   *
   *         private function clickHandler(event:MouseEvent):void {
   *
   *                 var position:uint = event.localX;
   *                 var percent:uint = Math.round(position) - 100;
   *                 var cue:uint = (percent / 100) * snd.length;
   *
   *                 channel.stop();
   *                 channel = snd.play(cue);
   *         }
   *
   *         private function errorHandler(errorEvent:IOErrorEvent):void {
   *             infoTextField.text = "The sound could not be loaded: " + errorEvent.text;
   *         }
   *     }
   * }
   * </listing>
   * <div>In the following example, depending on whether the user single or double clicks on a button the sound will play once or twice.
   * <p>In the constructor, the sound is loaded and a simple rectangle <code>button</code> sprite object is created. (A sprite object is used instead of a shape object to support interactivity.) Here, it is assumed that the sound file is in the same directory as the SWF file. (There is no error handling code for this example.)</p>
   * <p>Two event listeners are set up to respond to single mouse clicks and double clicks. If the user clicks once, the <code>clickHandler()</code> method is invoked, which plays the sound. If the user double clicks on the button, the <code>doubleClickHandler()</code> method is invoked, which will play the sound file twice. The second argument of the <code>play()</code> method is set to <code>1</code>, which means the sound will loop back once to the starting time of the sound and play again. The starting time, first argument, is set to <code>0</code>, meaning the file will play from the beginning.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.events.MouseEvent;
   *     import flash.media.Sound;
   *     import flash.net.URLRequest;
   *
   *     public class Sound_playExample2 extends Sprite {
   *         private var button:Sprite = new Sprite();
   *         private var snd:Sound = new Sound();
   *
   *         public function Sound_playExample2() {
   *
   *             var req:URLRequest = new URLRequest("click.mp3");
   *             snd.load(req);
   *
   *             button.graphics.beginFill(0x00FF00);
   *             button.graphics.drawRect(10, 10, 50, 30);
   *             button.graphics.endFill();
   *
   *             button.addEventListener(MouseEvent.CLICK, clickHandler);
   *             button.addEventListener(MouseEvent.DOUBLE_CLICK, doubleClickHandler);
   *
   *             this.addChild(button);
   *         }
   *
   *         private function clickHandler(event:MouseEvent):void {
   *             snd.play();
   *         }
   *
   *         private function doubleClickHandler(event:MouseEvent):void {
   *             snd.play(0, 2);
   *         }
   *     }
   * }
   * </listing></div>
   * <div>The following example displays the loading and playing progress of a sound file.
   * <p>In the constructor, the file is loaded in a <code>try...catch</code> block in order to catch any error that may occur while loading the file. A listener is added to the sound object that will respond to an <code>IOErrorEvent</code> event by calling the <code>errorHandler()</code> method. Another listener is added for the main application that will respond to an <code>Event.ENTER_FRAME</code> event, which is used as the timing mechanism for showing playback progress. Finally, a third listener is added for the sound channel that will respond to an <code>Event.SOUND_COMPLETE</code> event (when the sound has finished playing), by calling the <code>soundCompleteHandler()</code> method. The <code>soundCompleteHandler()</code> method also removes the event listener for the <code>Event.ENTER_FRAME</code> event.</p>
   * <p>The <code>enterFrameHandler()</code> method divides the <code>bytesLoaded</code> value passed with the <code>ProgressEvent</code> object by the <code>bytesTotal</code> value to arrive at a percentage of the sound data that is being loaded. The percentage of sound data that is being played could be determined by dividing the value of sound channel's <code>position</code> property by the length of the sound data. However, if the sound data is not fully loaded, the <code>length</code> property of the sound object shows only the size of the sound data that is currently loaded. An estimate of the eventual size of the full sound file is calculated by dividing the value of the current sound object's <code>length</code> by the value of the <code>bytesLoaded</code> property divided by the value of the <code>bytesTotal</code> property.</p>
   * <p>Note that if the file is small, cached, or the file is in the local directory, the load progress may not be noticeable. Also the lag time between when the sound data starts loading and the loaded data starts playing is determined by the value of the <code>SoundLoaderContext.buffertime</code> property, which is by default 1000 milliseconds and can be reset.</p>
   * <listing>
   * package {
   *     import flash.display.Sprite;
   *     import flash.net.URLRequest;
   *     import flash.media.Sound;
   *     import flash.media.SoundChannel;
   *     import flash.text.TextField;
   *     import flash.text.TextFieldAutoSize;
   *     import flash.events.Event;
   *     import flash.events.IOErrorEvent;
   *
   *     public class Sound_playExample3 extends Sprite {
   *         private var snd:Sound = new Sound();
   *         private var channel:SoundChannel;
   *         private var statusTextField:TextField  = new TextField();
   *
   *         public function Sound_playExample3(){
   *
   *             statusTextField.autoSize = TextFieldAutoSize.LEFT;
   *
   *            var req:URLRequest = new URLRequest("http://av.adobe.com/podcast/csbu_dev_podcast_epi_2.mp3");
   *
   *             try {
   *                 snd.load(req);
   *
   *                 channel = snd.play();
   *             }
   *             catch (err:Error) {
   *                 trace(err.message);
   *             }
   *
   *             snd.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
   *             addEventListener(Event.ENTER_FRAME, enterFrameHandler);
   *             channel.addEventListener(Event.SOUND_COMPLETE, soundCompleteHandler);
   *
   *             this.addChild(statusTextField);
   *         }
   *
   *         private function enterFrameHandler(event:Event):void {
   *             var loadTime:Number = snd.bytesLoaded / snd.bytesTotal;
   *             var loadPercent:uint = Math.round(100 * loadTime);
   *             var estimatedLength:int = Math.ceil(snd.length / (loadTime));
   *             var playbackPercent:uint = Math.round(100 * (channel.position / estimatedLength));
   *
   *             statusTextField.text = "Sound file's size is " + snd.bytesTotal + " bytes.\n"
   *                                    + "Bytes being loaded: " + snd.bytesLoaded + "\n"
   *                                    + "Percentage of sound file that is loaded " + loadPercent + "%.\n"
   *                                    + "Sound playback is " + playbackPercent + "% complete.";
   *         }
   *
   *         private function errorHandler(errorEvent:IOErrorEvent):void {
   *             statusTextField.text = "The sound could not be loaded: " + errorEvent.text;
   *         }
   *
   *         private function soundCompleteHandler(event:Event):void {
   *             statusTextField.text = "The sound has finished playing.";
   *             removeEventListener(Event.ENTER_FRAME, enterFrameHandler);
   *         }
   *     }
   * }
   * </listing></div>
   */
  "public function play",function play(startTime/*:Number = 0*/, loops/*:int = 0*/, sndTransform/*:SoundTransform = null*/)/*:SoundChannel*/ {if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){startTime = 0;}loops = 0;}sndTransform = null;}
    this.audio['play']();
    return null;
  },

  // ************************** Jangaroo part **************************

  /**
   * @private
   */
  "protected var",{ audio/*:HTMLAudioElement*/:null},
];},[],["flash.events.EventDispatcher","Error","flash.media.ID3Info","js.Audio"], "0.8.0", "0.8.3"
);