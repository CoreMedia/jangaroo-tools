joo.classLoader.prepare("package flash.net",/* {
import flash.events.EventDispatcher
import flash.media.Camera
import flash.media.Microphone
import flash.media.SoundTransform*/

/**
 * property AsyncErrorEvent.type =
 * @eventType flash.events.AsyncErrorEvent.ASYNC_ERROR
 */
{Event:{name:"asyncError", type:"flash.events.AsyncErrorEvent"}},
/**
 * property IOErrorEvent.type =
 * @eventType flash.events.IOErrorEvent.IO_ERROR
 */
{Event:{name:"ioError", type:"flash.events.IOErrorEvent"}},
/**
 * property NetStatusEvent.type =
 * @eventType flash.events.NetStatusEvent.NET_STATUS
 */
{Event:{name:"netStatus", type:"flash.events.NetStatusEvent"}},

/**
 * The NetStream class opens a one-way streaming channel over a NetConnection.
 * <p>Use the NetStream class to do the following:</p>
 * <ul>
 * <li>Call <code>NetStream.play()</code> to play a media file from a local disk, a web server, or Flash Media Server.</li>
 * <li>Call <code>NetStream.publish()</code> to publish a video, audio, and data stream to Flash Media Server.</li>
 * <li>Call <code>NetStream.send()</code> to send data messages to all subscribed clients.</li>
 * <li>Call <code>NetStream.send()</code> to add metadata to a live stream.</li>
 * <li>Call <code>NetStream.appendBytes()</code> to pass ByteArray data into the NetStream.</li></ul>
 * <p><b>Note:</b>You cannot play and publish a stream over the same NetStream object.</p>
 * <p>Adobe AIR and Flash Player 9.0.115.0 and later versions support files derived from the standard MPEG-4 container format. These files include F4V, MP4, M4A, MOV, MP4V, 3GP, and 3G2 if they contain H.264 video, HEAAC v2 encoded audio, or both. H.264 delivers higher quality video at lower bit rates when compared to the same encoding profile in Sorenson or On2. AAC is a standard audio format defined in the MPEG-4 video standard. HE-AAC v2 is an extension of AAC that uses Spectral Band Replication (SBR) and Parametric Stereo (PS) techniques to increase coding efficiency at low bit rates.</p>
 * <p>For information about supported codecs and file formats, see the following:</p>
 * <ul>
 * <li><a href="http://www.adobe.com/go/learn_fms_fileformats_en">Flash Media Server documentation</a></li>
 * <li><a href="http://www.adobe.com/go/hardware_scaling_en">Exploring Flash Player support for high-definition H.264 video and AAC audio</a></li>
 * <li><a href="http://www.adobe.com/go/video_file_format">FLV/F4V open specification documents</a></li></ul>
 * <p><b>Receiving data from a Flash Media Server stream, progressive F4V file, or progressive FLV file</b></p>
 * <p>Flash Media Server, F4V files, and FLV files can send event objects containing data at specific data points during streaming or playback. You can handle data from a stream or FLV file during playback in two ways:</p>
 * <ul>
 * <li>Associate a client property with an event handler to receive the data object. Use the <code>NetStream.client</code> property to assign an object to call specific data handling functions. The object assigned to the <code>NetStream.client</code> property can listen for the following data points: <code>onCuePoint()</code>, <code>onImageData()</code>, <code>onMetaData()</code>, <code>onPlayStatus()</code>, <code>onSeekPoint()</code>, <code>onTextData()</code>, and <code>onXMPData()</code>. Write procedures within those functions to handle the data object returned from the stream during playback. See the <code>NetStream.client</code> property for more information.</li>
 * <li>Associate a client property with a subclass of the NetStream class, then write an event handler to receive the data object. NetStream is a sealed class, which means that properties or methods cannot be added to a NetStream object at runtime. However, you can create a subclass of NetStream and define your event handler in the subclass. You can also make the subclass dynamic and add the event handler to an instance of the subclass.</li></ul>
 * <p>Wait to receive a <code>NetGroup.Neighbor.Connect</code> event before you use the object replication, direct routing, or posting APIs.</p>
 * <p><b>Note:</b> To send data through an audio file, like an mp3 file, use the Sound class to associate the audio file with a Sound object. Then, use the <code>Sound.id3</code> property to read metadata from the sound file.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/net/NetStream.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.media.Video
 * @see NetConnection
 * @see #appendBytes()
 * @see #play()
 * @see #publish()
 * @see #send()
 * @see #event:onImageData
 * @see #event:onMetaData
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7d50.html Basics of video
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118676a5be7-8000.html Using digital rights management
 *
 */
"public class NetStream extends flash.events.EventDispatcher",2,function($$private){;return[ 
  /**
   * The number of seconds of data currently in the buffer. You can use this property with the <code>bufferTime</code> property to estimate how close the buffer is to being full — for example, to display feedback to a user who is waiting for data to be loaded into the buffer.
   * @see #backBufferLength
   * @see #bufferTime
   * @see #bytesLoaded
   *
   */
  "public function get bufferLength",function bufferLength$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies how long to buffer messages before starting to display the stream.
   * <p>The default value is 0.1 (one-tenth of a second). To determine the number of seconds currently in the buffer, use the <code>bufferLength</code> property.</p>
   * <p>To play a server-side playlist, set <code>bufferTime</code> to at least 1 second. If you experience playback issues, increase the length of <code>bufferTime</code>.</p>
   * <p><b>Recorded content</b> To avoid distortion when streaming pre-recorded (not live) content, do not set the value of <code>Netstream.bufferTime</code> to 0. By default, the application uses an input buffer for pre-recorded content that queues the media data and plays the media properly. For pre-recorded content, use the default setting or increase the buffer time.</p>
   * <p><b>Live content</b> When streaming live content, set the <code>bufferTime</code> property to 0.</p>
   * <p>Starting with Flash Player 9.0.115.0, Flash Player no longer clears the buffer when <code>NetStream.pause()</code> is called. Before Flash Player 9.0.115.0, Flash Player waited for the buffer to fill up before resuming playback, which often caused a delay.</p>
   * <p>For a single pause, the <code>NetStream.bufferLength</code> property has a limit of either 60 seconds or twice the value of <code>NetStream.bufferTime</code>, whichever value is higher. For example, if <code>bufferTime</code> is 20 seconds, Flash Player buffers until <code>NetStream.bufferLength</code> is the higher value of either 20*2 (40), or 60. In this case it buffers until <code>bufferLength</code> is 60. If <code>bufferTime</code> is 40 seconds, Flash Player buffers until <code>bufferLength</code> is the higher value of 40*2 (80), or 60. In this case it buffers until <code>bufferLength</code> is 80 seconds.</p>
   * <p>The <code>bufferLength</code> property also has an absolute limit. If any call to <code>pause()</code> causes <code>bufferLength</code> to increase more than 600 seconds or the value of <code>bufferTime</code> * 2, whichever is higher, Flash Player flushes the buffer and resets <code>bufferLength</code> to 0. For example, if <code>bufferTime</code> is 120 seconds, Flash Player flushes the buffer if <code>bufferLength</code> reaches 600 seconds; if <code>bufferTime</code> is 360 seconds, Flash Player flushes the buffer if <code>bufferLength</code> reaches 720 seconds.</p>
   * <p><b>Tip</b>: You can use <code>NetStream.pause()</code> in code to buffer data while viewers are watching a commercial, for example, and then unpause when the main video starts.</p>
   * <p>For more information about the new pause behavior, see <a href="http://www.adobe.com/go/learn_fms_smartpause_en">http://www.adobe.com/go/learn_fms_smartpause_en</a>.</p>
   * <p><b>Flash Media Server</b>. The buffer behavior depends on whether the buffer time is set on a publishing stream or a subscribing stream. For a publishing stream, <code>bufferTime</code> specifies how long the outgoing buffer can grow before the application starts dropping frames. On a high-speed connection, buffer time is not a concern; data is sent almost as quickly as the application can buffer it. On a slow connection, however, there can be a significant difference between how fast the application buffers the data and how fast it is sent to the client.</p>
   * <p>For a subscribing stream, <code>bufferTime</code> specifies how long to buffer incoming data before starting to display the stream.</p>
   * <p>When a recorded stream is played, if <code>bufferTime</code> is 0, Flash sets it to a small value (approximately 10 milliseconds). If live streams are later played (for example, from a playlist), this buffer time persists. That is, <code>bufferTime</code> remains nonzero for the stream.</p>
   * @see #backBufferTime
   * @see #bufferLength
   * @see #bufferTimeMax
   * @see #time
   *
   */
  "public function get bufferTime",function bufferTime$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set bufferTime",function bufferTime$set(value/*:Number*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The number of bytes of data that have been loaded into the application. You can use this property with the <code>bytesTotal</code> property to estimate how close the buffer is to being full — for example, to display feedback to a user who is waiting for data to be loaded into the buffer.
   * @see #bytesTotal
   * @see #bufferLength
   *
   */
  "public function get bytesLoaded",function bytesLoaded$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The total size in bytes of the file being loaded into the application.
   * @see #bytesLoaded
   * @see #bufferTime
   *
   */
  "public function get bytesTotal",function bytesTotal$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies whether the application tries to download a cross-domain policy file from the loaded video file's server before beginning to load the video file. Use this property for progressive video download, and to load files that are outside the calling SWF file's own domain. This property is ignored when you are using RTMP.
   * <p>Set this property to <code>true</code> to call <code>BitmapData.draw()</code> on a video file loaded from a domain outside that of the calling SWF. The <code>BitmapData.draw()</code> method provides pixel-level access to the video. If you call <code>BitmapData.draw()</code> without setting the <code>checkPolicyFile</code> property to <code>true</code> at loading time, you can get a <code>SecurityError</code> exception because the required policy file was not downloaded.</p>
   * <p>Do not set this property to true unless you want pixel-level access to the video you are loading. Checking for a policy file consumes network bandwidth and can delay the start of your download.</p>
   * <p>When you call the <code>NetStream.play()</code> method with <code>checkPolicyFile</code> set to <code>true</code>, Flash Player or the AIR runtime must either successfully download a relevant cross-domain policy file or determine that no such policy file exists before it begins downloading. To verify the existence of a policy file, Flash Player or the AIR runtime performs the following actions, in this order:</p><ol>
   * <li>The application considers policy files that have already been downloaded.</li>
   * <li>The application tries to download any pending policy files specified in calls to the <code>Security.loadPolicyFile()</code> method.</li>
   * <li>The application tries to download a policy file from the default location that corresponds to the URL you passed to <code>NetStream.play()</code>, which is <code>/crossdomain.xml</code> on the same server as that URL.</li></ol>
   * <p>In all cases, Flash Player or Adobe AIR requires that an appropriate policy file exist on the video's server, that it provide access to the object at the URL you passed to <code>play()</code> based on the policy file's location, and that it allow the domain of the calling code's file to access the video, through one or more <code><allow-access-from></code> tags.</p>
   * <p>If you set <code>checkPolicyFile</code> to <code>true</code>, the application waits until the policy file is verified before downloading the video. Wait to perform any pixel-level operations on the video data, such as calling <code>BitmapData.draw()</code>, until you receive <code>onMetaData</code> or <code>NetStatus</code> events from your NetStream object.</p>
   * <p>If you set <code>checkPolicyFile</code> to <code>true</code> but no relevant policy file is found, you won't receive an error until you perform an operation that requires a policy file, and then the application throws a SecurityError exception.</p>
   * <p>Be careful with <code>checkPolicyFile</code> if you are downloading a file from a URL that uses server-side HTTP redirects. The application tries to retrieve policy files that correspond to the initial URL that you specify in <code>NetStream.play()</code>. If the final file comes from a different URL because of HTTP redirects, the initially downloaded policy files might not be applicable to the file's final URL, which is the URL that matters in security decisions.</p>
   * <p>For more information on policy files, see "Website controls (policy files)" in the <i>ActionScript 3.0 Developer's Guide</i> and the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * @see flash.display.BitmapData#draw()
   * @see flash.system.Security#loadPolicyFile()
   * @see #event:netStatus
   * @see #event:onMetaData
   * @see #play()
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e13.html Loading display content dynamically
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7de0.html Specifying loading context
   *
   */
  "public function get checkPolicyFile",function checkPolicyFile$get()/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set checkPolicyFile",function checkPolicyFile$set(value/*:Boolean*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Specifies the object on which callback methods are invoked to handle streaming or F4V/FLV file data. The default object is <code>this</code>, the NetStream object being created. If you set the <code>client</code> property to another object, callback methods are invoked on that other object. The <code>NetStream.client</code> object can call the following functions and receive an associated data object: <code>onCuePoint()</code>, <code>onImageData()</code>, <code>onMetaData()</code>, <code>onPlayStatus()</code>, <code>onSeekPoint()</code>, <code>onTextData()</code>, and <code>onXMPData()</code>.
   * <p>To associate the <code>client</code> property with an event handler:</p><ol>
   * <li>Create an object and assign it to the <code>client</code> property of the NetStream object:
   * <listing>
   *      var customClient:Object = new Object();
   *      my_netstream.client = customClient;
   *     </listing></li>
   * <li>Assign a handler function for the desired data event as a property of the client object:
   * <listing>
   *      customClient.onImageData = onImageDataHandler;
   *     </listing></li>
   * <li>Write the handler function to receive the data event object, such as:
   * <listing>
   *       public function onImageDataHandler(imageData:Object):void {
   *               trace("imageData length: " + imageData.data.length);
   *       }
   *     </listing></li></ol>
   * <p>When data is passed through the stream or during playback, the data event object (in this case the <code>imageData</code> object) is populated with the data. See the <code>onImageData</code> description, which includes a full example of an object assigned to the <code>client</code> property.</p>
   * <p>To associate the <code>client</code> property with a subclass:</p><ol>
   * <li>Create a subclass with a handler function to receive the data event object:
   * <listing>
   *      class CustomClient {
   *         public function onMetaData(info:Object):void {
   *             trace("metadata: duration=" + info.duration + " framerate=" + info.framerate);
   *      }
   *     </listing></li>
   * <li>Assign an instance of the subclass to the <code>client</code> property of the NetStream object:
   * <listing>
   *      my_netstream.client = new CustomClient();
   *     </listing></li></ol>
   * <p>When data is passed through the stream or during playback, the data event object (in this case the <code>info</code> object) is populated with the data. See the class example at the end of the NetStream class, which shows the assignment of a subclass instance to the <code>client</code> property.</p>
   * @throws TypeError The <code>client</code> property must be set to a non-null object.
   *
   * @see #event:onCuePoint
   * @see #event:onImageData
   * @see #event:onMetaData
   * @see #event:onPlayStatus
   * @see #event:onSeekPoint
   * @see #event:onTextData
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
   * The number of frames per second being displayed. If you are exporting video files to be played back on a number of systems, you can check this value during testing to help you determine how much compression to apply when exporting the file.
   */
  "public function get currentFPS",function currentFPS$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The number of seconds of data in the subscribing stream's buffer in live (unbuffered) mode. This property specifies the current network transmission delay (lag time).
   * <p>This property is intended primarily for use with a server such as Flash Media Server; for more information, see the class description.</p>
   * <p>You can get the value of this property to roughly gauge the transmission quality of the stream and communicate it to the user.</p>
   */
  "public function get liveDelay",function liveDelay$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The object encoding (AMF version) for this NetStream object. The NetStream object inherits its <code>objectEncoding</code> value from the associated NetConnection object. It's important to understand this property if your ActionScript 3.0 SWF file needs to communicate with servers released prior to Flash Player 9. For more information, see the <code>objectEncoding</code> property description in the NetConnection class.
   * <p>The value of this property depends on whether the stream is local or remote. Local streams, where <code>null</code> was passed to the <code>NetConnection.connect()</code> method, return the value of <code>NetConnection.defaultObjectEncoding</code>. Remote streams, where you are connecting to a server, return the object encoding of the connection to the server.</p>
   * <p>If you try to read this property when not connected, or if you try to change this property, the application throws an exception.</p>
   * @see NetConnection#objectEncoding
   *
   */
  "public function get objectEncoding",function objectEncoding$get()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Controls sound in this NetStream object. For more information, see the SoundTransform class.
   * @see flash.media.SoundTransform
   *
   */
  "public function get soundTransform",function soundTransform$get()/*:SoundTransform*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * @private
   */
  "public function set soundTransform",function soundTransform$set(value/*:SoundTransform*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * The position of the playhead, in seconds.
   * <p><b>Flash Media Server</b> For a subscribing stream, the number of seconds the stream has been playing. For a publishing stream, the number of seconds the stream has been publishing. This number is accurate to the thousandths decimal place; multiply by 1000 to get the number of milliseconds the stream has been playing.</p>
   * <p>For a subscribing stream, if the server stops sending data but the stream remains open, the value of the <code>time</code> property stops advancing. When the server begins sending data again, the value continues to advance from the point at which it stopped (when the server stopped sending data).</p>
   * <p>The value of <code>time</code> continues to advance when the stream switches from one playlist element to another. This property is set to 0 when <code>NetStream.play()</code> is called with <code>reset</code> set to <code>1</code> or <code>true</code>, or when <code>NetStream.close()</code> is called.</p>
   * @see #bufferLength
   * @see #bytesLoaded
   *
   */
  "public function get time",function time$get()/*:Number*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Creates a stream that you can use to play media files and send data over a NetConnection object.
   * @param connection A NetConnection object.
   * @param peerID This optional parameter is available in Flash Player 10 and later, for use with RTMFP connections. (If the value of the <code>NetConnection.protocol</code> property is not <code>"rtmfp"</code>, this parameter is ignored.) Use one of the following values:
   * <ul>
   * <li>To connect to Flash Media Server, specify <code>NetStream.CONNECT_TO_FMS</code>.</li>
   * <li>To publish directly to peers, specify <code>NetStream.DIRECT_CONNECTIONS</code>.</li>
   * <li>To play directly from a specific peer, specify that peer's identity (see <code>NetConnection.nearID</code> and <code>NetStream.farID</code>).</li>
   * <li>(Flash Player 10.1 or AIR 2 or later) To publish or play a stream in a peer-to-peer multicast group, specify a <code>groupspec</code> string (see the GroupSpecifier class).</li></ul>
   * <p>In most cases, a <code>groupspec</code> has the potential to use the network uplink on the local system. In this case, the user is asked for permission to use the computer's network resources. If the user allows this use, a <code>NetStream.Connect.Success</code> NetStatusEvent is sent to the NetConnection's event listener. If the user denies permission, a <code>NetStream.Connect.Rejected</code> event is sent. When specifying a <code>groupspec</code>, until a <code>NetStream.Connect.Success</code> event is received, it is an error to use any method of the NetStream object, and an exception is raised.</p>
   * <p>If you include this parameter in your constructor statement but pass a value of <code>null</code>, the value is set to <code>"connectToFMS"</code>.</p>
   *
   * @throws ArgumentError The NetConnection instance is not connected.
   *
   * @see #CONNECT_TO_FMS
   * @see #DIRECT_CONNECTIONS
   * @see #farID
   * @see flash.media.Video#attachCamera()
   * @see GroupSpecifier
   * @see GroupSpecifier#groupspecWithAuthorizations()
   * @see GroupSpecifier#groupspecWithoutAuthorizations()
   * @see GroupSpecifier#multicastEnabled
   * @see NetConnection
   * @see NetConnection#nearID
   * @see NetConnection#protocol
   * @see NetGroup
   * @see flash.events.NetStatusEvent#code_NetStream_Connect_Rejected
   * @see flash.events.NetStatusEvent#code_NetStream_Connect_Success
   *
   * @example The following code shows a connection to download and display, progressively, a video assigned to the variable <code>videoURL</code>:
   * <listing>
   *             var my_nc:NetConnection = new NetConnection();
   *             my_nc.connect(null);
   *             var my_ns:NetStream = new NetStream(my_nc);
   *             my_ns.play(videoURL);
   *             var my_video:Video = new Video();
   *             my_video.attachNetStream(my_ns);
   *             addChild(my_video);
   * </listing>
   * <div>The following code shows a connection to stream and display a video (assigned to the variable <code>videoURL</code>) on a remote Flash Media Server instance specified in the <code>connect()</code> command:
   * <listing>
   *             var my_nc:NetConnection = new NetConnection();
   *             my_nc.connect("rtmp://www.yourfmsserver.com/someappname");
   *             var my_ns:NetStream = new NetStream(my_nc, NetStream.CONNECT_TO_FMS);
   *             my_ns.play(videoURL);
   *             var my_video:Video = new Video();
   *             my_video.attachNetStream(my_ns);
   *             addChild(my_video);
   * </listing></div>
   */
  "public function NetStream",function NetStream$(connection/*:NetConnection*/, peerID/*:String = "connectToFMS"*/) {if(arguments.length<2){peerID = "connectToFMS";}this.super$2();
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Attaches an audio stream to a NetStream object from a Microphone object passed as the source. This method is available only to the publisher of the specified stream.
   * <p>Use this method with Flash Media Server to send live audio to the server. Call this method before or after you call the <code>publish()</code> method.</p>
   * <p>Set the <code>Microphone.rate</code> property to match the rate of the sound capture device. Call <code>setSilenceLevel()</code> to set the silence level threshold. To control the sound properties (volume and panning) of the audio stream, use the <code>Microphone.soundTransform</code> property.</p>
   * <pre>     var nc:NetConnection = new NetConnection();
   nc.connect("rtmp://server.domain.com/app");
   var ns:NetStream = new NetStream(nc);

   var live_mic:Microphone = Microphone.get();
   live_mic.rate = 8;
   live_mic.setSilenceLevel(20,200);

   var soundTrans:SoundTransform = new SoundTransform();
   soundTrans.volume = 6;
   live_mic.soundTransform = soundTrans;

   ns.attachAudio(live_mic);
   ns.publish("mic_stream","live")
   </pre>
   * <p>To hear the audio, call the <code>NetStream.play()</code> method and call <code>DisplayObjectContainer.addChild()</code> to route the audio to an object on the display list.</p>
   * @param microphone The source of the audio stream to be transmitted.
   *
   * @see #play()
   * @see flash.media.Microphone
   * @see flash.display.DisplayObjectContainer#addChild()
   *
   */
  "public function attachAudio",function attachAudio(microphone/*:Microphone*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Starts capturing video from a camera, or stops capturing if <code>theCamera</code> is set to <code>null</code>. This method is available only to the publisher of the specified stream.
   * <p>This method is intended for use with Flash Media Server; for more information, see the class description.</p>
   * <p>After attaching the video source, you must call <code>NetStream.publish()</code> to begin transmitting. Subscribers who want to display the video must call the <code>NetStream.play()</code> and <code>Video.attachCamera()</code> methods to display the video on the stage.</p>
   * <p>You can use <code>snapshotMilliseconds</code> to send a single snapshot (by providing a value of 0) or a series of snapshots — in effect, time-lapse footage — by providing a positive number that adds a trailer of the specified number of milliseconds to the video feed. The trailer extends the display time of the video message. By repeatedly calling <code>attachCamera()</code> with a positive value for <code>snapshotMilliseconds</code>, the sequence of alternating snapshots and trailers creates time-lapse footage. For example, you could capture one frame per day and append it to a video file. When a subscriber plays the file, each frame remains onscreen for the specified number of milliseconds and then the next frame is displayed.</p>
   * <p>The purpose of the <code>snapshotMilliseconds</code> parameter is different from the <code>fps</code> parameter you can set with <code>Camera.setMode()</code>. When you specify <code>snapshotMilliseconds</code>, you control how much time elapses between recorded frames. When you specify <code>fps</code> using <code>Camera.setMode()</code>, you are controlling how much time elapses during recording and playback.</p>
   * <p>For example, suppose you want to take a snapshot every 5 minutes for a total of 100 snapshots. You can do this in two ways:</p>
   * <ul>
   * <li>You can issue a <code>NetStream.attachCamera(myCamera, 500)</code> command 100 times, once every 5 minutes. This takes 500 minutes to record, but the resulting file will play back in 50 seconds (100 frames with 500 milliseconds between frames).</li>
   * <li>You can issue a <code>Camera.setMode()</code> command with an <code>fps</code> value of 1/300 (one per 300 seconds, or one every 5 minutes), and then issue a <code>NetStream.attachCamera(source)</code> command, letting the camera capture continuously for 500 minutes. The resulting file will play back in 500 minutes — the same length of time that it took to record — with each frame being displayed for 5 minutes.</li></ul>
   * <p>Both techniques capture the same 500 frames, and both approaches are useful; the approach to use depends primarily on your playback requirements. For example, in the second case, you could be recording audio the entire time. Also, both files would be approximately the same size.</p>
   * @param theCamera The source of the video transmission. Valid values are a Camera object (which starts capturing video) and <code>null</code>. If you pass <code>null</code>, the application stops capturing video, and any additional parameters you send are ignored.
   * @param snapshotMilliseconds Specifies whether the video stream is continuous, a single frame, or a series of single frames used to create time-lapse photography.
   * <ul>
   * <li>If you omit this parameter, the application captures all video until you pass a value of <code>null</code> to <code>attachCamera</code>.</li>
   * <li>If you pass 0, the application captures only a single video frame. Use this value to transmit "snapshots" within a preexisting stream. Flash Player or AIR interprets invalid, negative, or nonnumeric arguments as 0.</li>
   * <li>If you pass a positive number, the application captures a single video frame and then appends a pause of the specified length as a trailer on the snapshot. Use this value to create time-lapse photography effects.</li></ul>
   *
   */
  "public function attachCamera",function attachCamera(theCamera/*:Camera*/, snapshotMilliseconds/*:int = -1*/)/*:void*/ {if(arguments.length<2){snapshotMilliseconds = -1;}
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Stops playing all data on the stream, sets the <code>time</code> property to 0, and makes the stream available for another use. This method also deletes the local copy of a video file that was downloaded through HTTP. Although the application deletes the local copy of the file that it creates, a copy might persist in the cache directory. If you must completely prevent caching or local storage of the video file, use Flash Media Server.
   * <p>When using Flash Media Server, this method is invoked implicitly when you call <code>NetStream.play()</code> from a publishing stream or <code>NetStream.publish()</code> from a subscribing stream. Please note that:</p>
   * <ul>
   * <li>If <code>close()</code> is called from a publishing stream, the stream stops publishing and the publisher can now use the stream for another purpose. Subscribers no longer receive anything that was being published on the stream, because the stream has stopped publishing.</li>
   * <li>If <code>close()</code> is called from a subscribing stream, the stream stops playing for the subscriber, and the subscriber can use the stream for another purpose. Other subscribers are not affected.</li>
   * <li>You can stop a subscribing stream from playing, without closing the stream or changing the stream type by using <code>flash.net.NetStream.play(false)</code>.</li></ul>
   * @see #pause()
   * @see #play()
   * @see #publish()
   *
   */
  "public function close",function close()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Pauses playback of a video stream. Calling this method does nothing if the video is already paused. To resume play after pausing a video, call <code>resume()</code>. To toggle between pause and play (first pausing the video, then resuming), call <code>togglePause()</code>.
   * <p>Starting with Flash Player 9.0.115.0, Flash Player no longer clears the buffer when <code>NetStream.pause()</code> is called. This behavior is called "smart pause". Before Flash Player 9.0.115.0, Flash Player waited for the buffer to fill up before resuming playback, which often caused a delay.</p>
   * <p><b>Note:</b> For backwards compatibility, the <code>"NetStream.Buffer.Flush"</code> event (see the <code>NetStatusEvent.info</code> property) still fires, although the server does not flush the buffer.</p>
   * <p>For a single pause, the <code>NetStream.bufferLength</code> property has a limit of either 60 seconds or twice the value of <code>NetStream.bufferTime</code>, whichever value is higher. For example, if <code>bufferTime</code> is 20 seconds, Flash Player buffers until <code>NetStream.bufferLength</code> is the higher value of either 20*2 (40), or 60, so in this case it buffers until <code>bufferLength</code> is 60. If <code>bufferTime</code> is 40 seconds, Flash Player buffers until <code>bufferLength</code> is the higher value of 40*2 (80), or 60, so in this case it buffers until <code>bufferLength</code> is 80 seconds.</p>
   * <p>The <code>bufferLength</code> property also has an absolute limit. If any call to <code>pause()</code> causes <code>bufferLength</code> to increase more than 600 seconds or the value of <code>bufferTime</code> * 2, whichever is higher, Flash Player flushes the buffer and resets <code>bufferLength</code> to 0. For example, if <code>bufferTime</code> is 120 seconds, Flash Player flushes the buffer if <code>bufferLength</code> reaches 600 seconds; if <code>bufferTime</code> is 360 seconds, Flash Player flushes the buffer if <code>bufferLength</code> reaches 720 seconds.</p>
   * <p><b>Tip</b>: You can use <code>NetStream.pause()</code> in code to buffer data while viewers are watching a commercial, for example, and then unpause when the main video starts.</p>
   * @see #close()
   * @see #play()
   * @see #resume()
   * @see #togglePause()
   * @see #bufferLength
   * @see #bufferTime
   * @see flash.events.NetStatusEvent#info
   *
   */
  "public function pause",function pause()/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Plays a media file from a local directory or a web server; plays a media file or a live stream from Flash Media Server. Dispatches a <code>NetStatusEvent</code> object to report status and error messages.
   * <p>For information about supported codecs and file formats, see the following:</p>
   * <ul>
   * <li><a href="http://www.adobe.com/go/learn_fms_fileformats_en">Flash Media Server documentation</a></li>
   * <li><a href="http://www.adobe.com/go/hardware_scaling_en">Exploring Flash Player support for high-definition H.264 video and AAC audio</a></li>
   * <li><a href="http://www.adobe.com/go/video_file_format">FLV/F4V open specification documents</a></li></ul>
   * <p>Workflow for playing a file or live stream</p><ol>
   * <li>Create a NetConnection object and call <code>NetConnection.connect()</code>.
   * <p>To play a file from a local directory or web server, pass null.</p>
   * <p>To play a recorded file or live stream from Flash Media Server, pass the URI of a Flash Media Server application.</p></li>
   * <li>Call <code>NetConnection.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler)</code> to listen for NetStatusEvent events.</li>
   * <li>On <code>"NetConnection.Connect.Success"</code>, create a NetStream object and pass the NetConnection object to the constructor.</li>
   * <li>Create a Video object and call <code>Video.attachNetStream()</code> and pass the NetStream object.</li>
   * <li>Call <code>NetStream.play()</code>.
   * <p>To play a live stream, pass the stream name passed to the <code>NetStream.publish()</code> method.</p>
   * <p>To play a recorded file, pass the file name.</p></li>
   * <li>Call <code>addChild()</code> and pass the Video object to display the video.</li></ol>
   * <p><b>Note:</b>To see sample code, scroll to the example at the bottom of this page.</p>
   * <p>Enable Data Generation Mode</p>
   * <p>Call <code>play(null)</code> to enable "Data Generation Mode". In this mode, call the <code>appendBytes()</code> method to deliver data to the NetStream. Use Data Generation Mode to stream content over HTTP from the Adobe HTTP Dynamic Streaming Origin Module on an Apache HTTP Server. HTTP Dynamic Streaming lets clients seek quickly to any point in a file. The Open Source Media Framework (OSMF) supports HTTP Dynamic Streaming for vod and live streams. For examples of how to use NetStream Data Generation Mode, download the <a href="http://www.opensourcemediaframework.com">OSMF</a> source. For more information about HTTP Dynamic Streaming, see <a href="http://www.adobe.com/go/learn_fms_http_en">HTTP Dynamic Streaming</a>.</p>
   * <p>When you use this method without Flash Media Server, there are security considerations. A file in the local-trusted or local-with-networking sandbox can load and play a video file from the remote sandbox, but cannot access the remote file's data without explicit permission in the form of a URL policy file. Also, you can prevent a SWF file running in Flash Player from using this method by setting the <code>allowNetworking</code> parameter of the the <code>object</code> and <code>embed</code> tags in the HTML page that contains the SWF content. For more information related to security, see the Flash Player Developer Center Topic: <a href="http://www.adobe.com/go/devnet_security_en">Security</a>.</p>
   * @param rest
   * <p>Play a local file</p>
   * <p>The location of a media file. Argument can be a String, a <code>URLRequest.url</code> property, or a variable referencing either. In Flash Player and in AIR content outside the application security sandbox, you can play local video files that are stored in the same directory as the SWF file or in a subdirectory; however, you can't navigate to a higher-level directory.</p>
   * <p>Play a file from Flash Media Server</p>
   * <table>
   * <tr><th>Name</th><th>Required</th><th>Description</th></tr>
   * <tr>
   * <td><code>name:Object</code></td>
   * <td>Required</td>
   * <td>The name of a recorded file, an identifier for live data published by <code>NetStream.publish()</code>, or <code>false</code>. If <code>false</code>, the stream stops playing and any additional parameters are ignored. For more information on the filename syntax, see the file format table following this table.</td></tr>
   * <tr>
   * <td><code>start:Number</code></td>
   * <td>Optional</td>
   * <td>The start time, in seconds. Allowed values are -2, -1, 0, or a positive number. The default value is -2, which looks for a live stream, then a recorded stream, and if it finds neither, opens a live stream. If -1, plays only a live stream. If 0 or a positive number, plays a recorded stream, beginning <code>start</code> seconds in.</td></tr>
   * <tr>
   * <td><code>len:Number</code> </td>
   * <td>Optional if <code>start</code> is specified.</td>
   * <td>The duration of the playback, in seconds. Allowed values are -1, 0, or a positive number. The default value is -1, which plays a live or recorded stream until it ends. If 0, plays a single frame that is <code>start</code> seconds from the beginning of a recorded stream. If a positive number, plays a live or recorded stream for <code>len</code> seconds.</td></tr>
   * <tr>
   * <td><code>reset:Object</code> </td>
   * <td>Optional if <code>len</code> is specified.</td>
   * <td>Whether to clear a playlist. The default value is 1 or <code>true</code>, which clears any previous <code>play</code> calls and plays <code>name</code> immediately. If 0 or <code>false</code>, adds the stream to a playlist. If 2, maintains the playlist and returns all stream messages at once, rather than at intervals. If 3, clears the playlist and returns all stream messages at once.</td></tr></table>
   * <p>You can play back the file formats described in the following table. The syntax differs depending on the file format.</p>
   * <table>
   * <tr><th>File format</th><th>Syntax</th><th>Example</th></tr>
   * <tr>
   * <td>FLV</td>
   * <td>Specify the stream name (in the "samples" directory) as a string without a filename extension.</td>
   * <td><code>ns.play("samples/myflvstream");</code></td></tr>
   * <tr>
   * <td>mp3 or ID3</td>
   * <td>Specify the stream name (in the "samples" directory) as a string with the prefix <code>mp3:</code> or <code>id3:</code> without a filename extension.</td>
   * <td>
   * <p><code>ns.play("mp3:samples/mymp3stream");</code></p>
   * <p><code>ns.play("id3:samples/myid3data");</code></p></td></tr>
   * <tr>
   * <td>MPEG-4-based files (such as F4V and MP4)</td>
   * <td>Specify the stream name (in the "samples" directory) as a string with the prefix <code>mp4:</code> The prefix indicates to the server that the file contains H.264-encoded video and AAC-encoded audio within the MPEG-4 Part 14 container format. If the file on the server has a file extension, specify it.</td>
   * <td>
   * <p><code>ns.play("mp4:samples/myvideo.f4v");</code></p>
   * <p><code>ns.play("mp4:samples/myvideo.mp4");</code></p>
   * <p><code>ns.play("mp4:samples/myvideo");</code></p>
   * <p><code>ns.play("mp4:samples/myvideo.mov");</code></p></td></tr>
   * <tr>
   * <td>RAW</td>
   * <td>Specify the stream name (in the "samples" directory) as a string with the prefix <code>raw:</code></td>
   * <td><code>ns.play("raw:samples/myvideo");</code></td></tr></table>
   * <p>Enable Data Generation Mode</p>
   * <p>To enable "Data Generation Mode", pass the value <code>null</code> to a NetStream created on a NetConnection connected to <code>null</code>. In this mode, call <code>appendBytes()</code> to deliver data to the NetStream. (Passing <code>null</code> also resets the byte counter for the <code>onSeekPoint()</code> callback.)</p>
   * Events
   * <table>
   * <tr>
   * <td><code><b>status</b>:<a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/StatusEvent.html"><code>StatusEvent</code></a></code> — Dispatched when attempting to play content encrypted with digital rights management (DRM). The value of the <code>code</code> property is <code>"DRM.encryptedFLV"</code>.</td></tr></table>
   * @throws SecurityError Local untrusted SWF files cannot communicate with the Internet. You can work around this restriction by reclassifying this SWF file as local-with-networking or trusted.
   * @throws ArgumentError At least one parameter must be specified.
   * @throws Error The NetStream Object is invalid. This may be due to a failed NetConnection.
   *
   * @see flash.display.DisplayObjectContainer#addChild()
   * @see #checkPolicyFile
   * @see #appendBytes()
   *
   * @example <b>Flash Media Server</b> This example plays a recorded F4V file from the "samples" directory, starting at the beginning, for up to 100 seconds. With MPEG-4 files, if the file on the server has a filename extension the <code>play()</code> method must specify a filename extension.
   * <listing>
   *      ns.play("mp4:samples/record1.f4v", 0, 100, true);
   *     </listing>
   * <div><b>Flash Media Server</b> This example plays a live FLV stream published by a client, from beginning to end, starting immediately:
   * <listing>
   *      ns.play("livestream");
   *     </listing></div>
   * <div>The following example shows how to load an external FLV file:
   * <listing>
   * var MyVideo:Video = new Video();
   * addChild(MyVideo);
   *
   * var MyNC:NetConnection = new NetConnection();
   * MyNC.connect(null);
   *
   * var MyNS:NetStream = new NetStream(MyNC);
   * MyNS.play("http://www.helpexamples.com/flash/video/clouds.flv");
   *
   * MyVideo.attachNetStream(MyNS);
   *
   * //the clouds.flv video has metadata we're not using, so create
   * //an error handler to ignore the message generated by the runtime
   * //about the metadata
   * MyNS.addEventListener(AsyncErrorEvent.ASYNC_ERROR, asyncErrorHandler);
   *
   * function asyncErrorHandler(event:AsyncErrorEvent):void
   * {
   *    //ignore metadata error message
   * }
   * </listing></div>
   */
  "public function play",function play(/*...rest*/)/*:void*/ {var rest=arguments;
    throw new Error('not implemented'); // TODO: implement!
},
/**
 * Sends streaming audio, video, and data messages from a client to Flash Media Server, optionally recording the stream during transmission. This method dispatches a NetStatusEvent object with information about the stream. Before you call <code>NetStream.publish()</code>, capture the <code>"NetConnection.Connect.Success"</code> event to verify that the application has successfully connected to Flash Media Server.
 * <p>While publishing, you can record files in FLV or F4V format. If you record a file in F4V format, use a flattener tool to edit or play the file in another application. To download the tool, see <a href="http://www.adobe.com/go/fms_tools">www.adobe.com/go/fms_tools</a>.</p>
 * <p><b>Note:</b>Do not use this method to play a stream. To play a stream, call the <code>NetStream.play()</code> method.</p>
 * <p>Workflow for publishing a stream</p><ol>
 * <li>Create a NetConnection object and call <code>NetConnection.connect()</code>.</li>
 * <li>Call <code>NetConnection.addEventListener()</code> to listen for NetStatusEvent events.</li>
 * <li>On the <code>"NetConnection.Connect.Success"</code> event, create a NetStream object and pass the NetConnection object to the constructor.</li>
 * <li>To capture audio and video, call the <code>NetStream.attachAudio()</code>method and the <code>NetStream.attachCamera()</code> method.</li>
 * <li>To publish a stream, call the <code>NetStream.publish()</code> method. You can record the data as you publish it so that users can play it back later.</li></ol>
 * <p><b>Note:</b> A NetStream can either publish a stream or play a stream, it cannot do both. To publish a stream and view the playback from the server, create two NetStream objects. You can send multiple NetStream objects over one NetConnection object.</p>
 * <p>When Flash Media Server records a stream it creates a file. By default, the server creates a directory with the application instance name passed to <code>NetConnection.connect()</code> and stores the file in the directory. For example, the following code connects to the default instance of the "lectureseries" application and records a stream called "lecture". The file "lecture.flv" is recorded in the applications/lectureseries/streams/_definst_ directory:</p>
 * <listing>
 *      var nc:NetConnection = new NetConnection();
 *      nc.connect("rtmp://fms.example.com/lectureseries");
 *      nc.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler);
 *
 *      function netStatusHandler(event:NetStatusEvent):void{
 *        if (event.info.code == "NetConnection.Connect.Success"){
 *             var ns:NetStream = new NetStream(nc);
 *             ns.publish("lecture", "record");
 *        }
 *      }
 *     </listing>
 * <p>The following example connects to the "monday" instance of the same application. The file "lecture.flv" is recorded in the directory /applications/lectureseries/streams/monday:</p>
 * <listing>
 *      var nc:NetConnection = new NetConnection();
 *      nc.connect("rtmp://fms.example.com/lectureseries/monday");
 *      nc.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler);
 *
 *      function netStatusHandler(event:NetStatusEvent):void{
 *        if (event.info.code == "NetConnection.Connect.Success"){
 *             var ns:NetStream = new NetStream(nc);
 *             ns.publish("lecture", "record");
 *        }
 *      }
 *     </listing>
 * @param name A string that identifies the stream. Clients that subscribe to this stream pass this name when they call <code>NetStream.play()</code>. Don't follow the stream name with a "/". For example, don't use the stream name <code>"bolero/"</code>.
 * <p>You can record files in the formats described in the following table (you cannot use <code>publish()</code> for MP3 format files). The syntax differs depending on the file format.</p>
 * <table>
 * <tr><th>File format</th><th>Syntax</th><th>Example</th></tr>
 * <tr>
 * <td>FLV</td>
 * <td>Specify the stream name as a string without a filename extension.</td>
 * <td><code>ns.publish("myflvstream");</code></td></tr>
 * <tr>
 * <td>MPEG-4-based files (such as F4V or MP4)</td>
 * <td>Specify the stream name as a string with the prefix <code>mp4:</code> with or without the filename extension. Flash Player doesn't encode using H.264, but Flash Media Server can record any codec in the F4V container. Flash Media Live Encoder can encode using H.264.</td>
 * <td><code>ns.publish("mp4:myvideo.f4v")</code> <code>ns.publish("mp4:myvideo");</code></td></tr>
 * <tr>
 * <td>RAW</td>
 * <td>Specify the stream name as a string with the prefix <code>raw:</code></td>
 * <td><code>ns.publish("raw:myvideo");</code></td></tr></table>
 * @param type A string that specifies how to publish the stream. Valid values are "<code>record</code>", "<code>append</code>", "<code>appendWithGap</code>", and "<code>live</code>". The default value is "<code>live</code>".
 * <ul>
 * <li>If you pass "<code>record</code>", the server publishes and records live data, saving the recorded data to a new file with a name matching the value passed to the <code>name</code> parameter. If the file exists, it is overwritten.</li>
 * <li>If you pass "<code>append</code>", the server publishes and records live data, appending the recorded data to a file with a name that matches the value passed to the <code>name</code> parameter. If no file matching the <code>name</code> parameter is found, it is created.</li>
 * <li>If you pass "<code>appendWithGap</code>", additional information about time coordination is passed to help the server determine the correct transition point when dynamic streaming.</li>
 * <li>If you omit this parameter or pass "<code>live</code>", the server publishes live data without recording it. If a file with a name that matches the value passed to the <code>name</code> parameter exists, it is deleted.</li></ul>
 *
 * @see NetConnection#connect()
 * @see flash.events.NetStatusEvent#info
 *
 * @example The following example captures video from a camera and publishes it over a NetStream to Flash Media Server. The example displays the video as it's played back from Flash Media Server.
 * <p>To run this example, you need a camera attached to your computer. You also need to add a Button component and a Label component to the Library.</p>
 * <p>The application has a button that publishes a stream (sends it to Flash Media Server) only after the application has successfully connected to the server. The application plays back the stream from the server only after the stream has been successfully published. The <code>NetStatusEvent</code> returns an <code>info</code> object with a <code>code</code> property that specifies these cases. The <code>netStatusHandler</code> function handles these events for the NetConnection and NetStream classes.</p>
 * <listing>
 * package {
 *     import flash.display.Sprite;
 *     import flash.events.*;
 *     import flash.media.Video;
 *     import flash.media.Camera;
 *     import flash.net.NetConnection;
 *     import flash.net.NetStream;
 *     import fl.controls.Button;
 *     import fl.controls.Label;
 *
 *     public class NetStream_publish extends Sprite {
 *         private var connectionURL:String = "rtmp://localhost/live/";
 *         private var videoURL:String = "liveVideo";
 *         private var nc:NetConnection;
 *         private var ns_publish:NetStream;
 *         private var ns_playback:NetStream;
 *         private var video_publish:Video;
 *         private var video_playback:Video;
 *         private var cam:Camera;
 *         private var b:Button;
 *         private var l:Label;
 *
 *         public function NetStream_publish() {
 *             setUpUI();
 *
 *             nc = new NetConnection();
 *             nc.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler);
 *
 *             // Add bandwidth detection handlers on the NetConnection Client to
 *             // prevent Reference Errors at runtime when using the "live" and "vod" applications.
 *             var clientObj:Object = new Object();
 *             clientObj.onBWDone = onBWDone;
 *             clientObj.onBWCheck = onBWCheck;
 *             nc.client = clientObj;
 *
 *             // Connect to the "live" application on Flash Media Server.
 *             nc.connect(connectionURL);
 *         }
 *
 *         private function netStatusHandler(event:NetStatusEvent):void {
 *             trace(event.info.code + " | " + event.info.description);
 *             switch (event.info.code) {
 *                 case "NetConnection.Connect.Success":
 *                     // Enable the "Publish" button after the client connects to the server.
 *                     b.enabled = true;
 *                     break;
 *                 case "NetStream.Publish.Start":
 *                     playbackVideo();
 *                     break;
 *             }
 *         }
 *
 *         private function publishVideo(event:MouseEvent):void{
 *             // Disable the button so that you can only publish once.
 *             b.enabled = false;
 *             // Create a NetStream to send video to FMS.
 *             ns_publish = new NetStream(nc);
 *             ns_publish.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler);
 *             // Publish (send) the video to FMS.
 *             cam = Camera.getCamera();
 *             ns_publish.attachCamera(cam);
 *                ns_publish.publish(videoURL);
 *         }
 *
 *         private function playbackVideo():void {
 *             // Create a NetStream to receive the video from FMS.
 *             ns_playback = new NetStream(nc);
 *             ns_playback.addEventListener(NetStatusEvent.NET_STATUS, netStatusHandler);
 *             ns_playback.play(videoURL);
 *             // Display the video that was published to FMS.
 *             video_playback = new Video(cam.width, cam.height);
 *             video_playback.x = cam.width + 20;
 *             video_playback.y = 10;
 *             video_playback.attachNetStream(ns_playback);
 *             addChild(video_playback);
 *         }
 *
 *
 *         private function setUpUI():void { 
 *             b = new Button();
 *             b.addEventListener(MouseEvent.CLICK, publishVideo);
 *             b.width = 150;
 *             b.label = "Publish video to server";
 *             b.move(10, 150);
 *             b.enabled = false;
 *
 *             l = new Label();
 *             l.width = 150;
 *             l.text = "Playing back from server"
 *             l.move(190, 150);
 *
 *             addChild(b);
 *             addChild(l);
 *         }
 *
 *         // Handlers called by the Flash Media Server "live" and "vod" applications.
 *         public function onBWDone(... rest):Boolean {
 *             return true;
 *         }
 *
 *         public function onBWCheck(... rest):Number {
 *             return 0;
 *         }
 *     }
 * }
 * </listing>
 */
"public function publish",function publish(name/*:String = null*/, type/*:String = null*/)/*:void*/ {if(arguments.length<2){if(arguments.length<1){name = null;}type = null;}
  throw new Error('not implemented'); // TODO: implement!
},

/**
 * Specifies whether incoming audio plays on the stream. This method is available only to clients subscribed to the specified stream. It is not available to the publisher of the stream. Call this method before or after you call the <code>NetStream.play()</code> method. For example, attach this method to a button to allow users to mute and unmute the audio. Use this method only on unicast streams that are played back from Flash Media Server. This method doesn't work on RTMFP multicast streams or when using the <code>NetStream.appendBytes()</code> method.
 * @param flag Specifies whether incoming audio plays on the stream (<code>true</code>) or not (<code>false</code>). The default value is <code>true</code>. If the specified stream contains only audio data, <code>NetStream.time</code> stops incrementing when you pass <code>false</code>.
 *
 */
"public function receiveAudio",function receiveAudio(flag/*:Boolean*/)/*:void*/ {
  throw new Error('not implemented'); // TODO: implement!
},

/**
 * Specifies whether incoming video plays on the stream. This method is available only to clients subscribed to the specified stream. It is not available to the publisher of the stream. Call this method before or after you call the <code>NetStream.play()</code> method. For example, attach this method to a button to allow users to show and hide the video. Use this method only on unicast streams that are played back from Flash Media Server. This method doesn't work on RTMFP multicast streams or when using <code>the NetStream.appendBytes()</code> method.
 * @param flag Specifies whether incoming video plays on this stream (<code>true</code>) or not (<code>false</code>). The default value is <code>true</code>. If the specified stream contains only video data, <code>NetStream.time</code> stops incrementing when you pass <code>false</code>.
 *
 */
"public function receiveVideo",function receiveVideo(flag/*:Boolean*/)/*:void*/ {
  throw new Error('not implemented'); // TODO: implement!
},

/**
 * Specifies the frame rate for incoming video. This method is available only to clients subscribed to the specified stream. It is not available to the publisher of the stream. Call this method before or after you call the <code>NetStream.play()</code> method. For example, call this method to allow users to set the video frame rate. To determine the current frame rate, use <code>NetStream.currentFPS</code>. To stop receiving video, pass <code>0</code>.
 * <p>When you pass a value to the FPS parameter to limit the frame rate of the video, Flash Media Server attempts to reduce the frame rate while preserving the integrity of the video. Between every two keyframes, the server sends the minimum number of frames needed to satisfy the desired rate. Please note that I-frames (or intermediate frames) must be sent contiguously, otherwise the video is corrupted. Therefore, the desired number of frames is sent immediately and contiguously following a keyframe. Since the frames are not evenly distributed, the motion appears smooth in segments punctuated by stalls.</p>
 * <p>Use this method only on unicast streams that are played back from Flash Media Server. This method doesn't work on RTMFP multicast streams or when using the <code>NetStream.appendBytes()</code> method.</p>
 * @param FPS Specifies the frame rate per second at which the incoming video plays.
 *
 */
"public function receiveVideoFPS",function receiveVideoFPS(FPS/*:Number*/)/*:void*/ {
  throw new Error('not implemented'); // TODO: implement!
},

/**
 * Resumes playback of a video stream that is paused. If the video is already playing, calling this method does nothing.
 * @see #close()
 * @see #pause()
 * @see #play()
 * @see #togglePause()
 *
 */
"public function resume",function resume()/*:void*/ {
  throw new Error('not implemented'); // TODO: implement!
},

/**
 * Seeks the keyframe (also called an I-frame in the video industry) closest to the specified location. The keyframe is placed at an offset, in seconds, from the beginning of the stream.
 * <p>Video streams are usually encoded with two types of frames, keyframes (or I-frames) and P-frames. A keyframe contains an entire image, while a P-frame is an interim frame that provides additional video information between keyframes. A video stream typically has a keyframe every 10-50 frames.</p>
 * <p>Flash Media Server has several types of seek behavior: enhanced seeking and smart seeking.</p>
 * <p>Enhanced seeking</p>
 * <p>Enhanced seeking is enabled by default. To disable enhanced seeking, on Flash Media Server set the <code>EnhancedSeek</code> element in the <code>Application.xml</code> configuration file to <code>false</code>.</p>
 * <p>If enhanced seeking is enabled, the server generates a new keyframe at <code>offset</code> based on the previous keyframe and any intervening P-frames. However, generating keyframes creates a high processing load on the server and distortion might occur in the generated keyframe. If the video codec is On2, the keyframe before the seek point and any P-frames between the keyframe and the seek point are sent to the client.</p>
 * <p>If enhanced seeking is disabled, the server starts streaming from the nearest keyframe. For example, suppose a video has keyframes at 0 seconds and 10 seconds. A seek to 4 seconds causes playback to start at 4 seconds using the keyframe at 0 seconds. The video stays frozen until it reaches the next keyframe at 10 seconds. To get a better seeking experience, you need to reduce the keyframe interval. In normal seek mode, you cannot start the video at a point between the keyframes.</p>
 * <p>Smart seeking</p>
 * <p>To enable smart seeking, set <code>NetStream.inBufferSeek</code> to <code>true</code>.</p>
 * <p>Smart seeking allows Flash Player to seek within an existing back buffer and forward buffer. When smart seeking is disabled, each time <code>seek()</code> is called Flash Player flushes the buffer and requests data from the server. For more information, see <code>NetStream.inBufferSeek</code>.</p>
 * <p>Seeking in Data Generation Mode</p>
 * <p>When you call <code>seek()</code> on a NetStream in Data Generation Mode, all bytes passed to <code>appendBytes()</code> are discarded (not placed in the buffer, accumulated in the partial message FIFO, or parsed for seek points) until you call <code>appendBytesAction(NetStreamAppendBytesAction.RESET_BEGIN)</code> or <code>appendBytesAction(NetStreamAppendBytesAction.RESET_SEEK)</code> to reset the parser. For information about Data Generation Mode, see <code>NetStream.play()</code>.</p>
 * @param offset The approximate time value, in seconds, to move to in a video file. With Flash Media Server, if <code><EnhancedSeek></code> is set to <code>true</code> in the Application.xml configuration file (which it is by default), the server generates a keyframe at <code>offset</code>.
 * <ul>
 * <li>To return to the beginning of the stream, pass 0 for <code>offset</code>.</li>
 * <li>To seek forward from the beginning of the stream, pass the number of seconds to advance. For example, to position the playhead at 15 seconds from the beginning (or the keyframe before 15 seconds), use <code>myStream.seek(15)</code>.</li>
 * <li>To seek relative to the current position, pass <code>NetStream.time + n</code> or <code>NetStream.time - n</code> to seek <code>n</code> seconds forward or backward, respectively, from the current position. For example, to rewind 20 seconds from the current position, use <code>NetStream.seek(NetStream.time - 20).</code></li></ul>
 *
 * @see #inBufferSeek
 * @see #backBufferLength
 * @see #backBufferTime
 * @see #step()
 * @see #time
 * @see #play()
 *
 */
"public function seek",function seek(offset/*:Number*/)/*:void*/ {
  throw new Error('not implemented'); // TODO: implement!
},

/**
 * Sends a message on a published stream to all subscribing clients. This method is available only to the publisher of the specified stream. This method is available for use with Flash Media Server only. To process and respond to this message, create a handler on the <code>NetStream</code> object, for example, <code>ns.HandlerName</code>.
 * <p>Flash Player or AIR does not serialize methods or their data, object prototype variables, or non-enumerable variables. For display objects, Flash Player or AIR serializes the path but none of the data.</p>
 * <p>You can call the <code>send()</code> method to add data keyframes to a live stream published to Flash Media Server. A data keyframe is a message a publisher adds to a live stream. Data keyframes are typically used to add metadata to a live stream before data is captured for the stream from camera and microphone. A publisher can add a data keyframe at any time while the live stream is being published. The data keyframe is saved in the server's memory as long as the publisher is connected to the server.</p>
 * <p>Clients who are subscribed to the live stream before a data keyframe is added receive the keyframe as soon as it is added. Clients who subscribe to the live stream after the data keyframe is added receive the keyframe when they subscribe.</p>
 * <p>To add a keyframe of metadata to a live stream sent to Flash Media Server, use <code>&#64;setDataFrame</code> as the handler name, followed by two additional arguments, for example:</p>
 * <listing>
 *      var ns:NetStream = new NetStream(nc);
 *      ns.send("&#64;setDataFrame", "onMetaData", metaData);
 *     </listing>
 * <p>The <code>&#64;setDataFrame</code> argument refers to a special handler built in to Flash Media Server. The <code>onMetaData</code> argument is the name of a callback function in your client application that listens for the <code>onMetaData</code> event and retrieves the metadata. The third item, <code>metaData</code>, is an instance of <code>Object</code> or <code>Array</code> with properties that define the metadata values.</p>
 * <p>Use <code>&#64;clearDataFrame</code> to clear a keyframe of metadata that has already been set in the stream:</p>
 * <listing>
 *      ns.send("@clearDataFrame", "onMetaData");
 *     </listing>
 * @param handlerName The message to send; also the name of the ActionScript handler to receive the message. The handler name can be only one level deep (that is, it can't be of the form parent/child) and is relative to the stream object. Do not use a reserved term for a handler name. For example, using "<code>close</code>" as a handler name causes the method to fail. With Flash Media Server, use <code>&#64;setDataFrame</code> to add a keyframe of metadata to a live stream or <code>&#64;clearDataFrame</code> to remove a keyframe.
 * @param rest Optional arguments that can be of any type. They are serialized and sent over the connection, and the receiving handler receives them in the same order. If a parameter is a circular object (for example, a linked list that is circular), the serializer handles the references correctly. With Flash Media Server, if <code>&#64;setDataFrame</code> is the first argument, use <code>onMetaData</code> as the second argument; for the third argument, pass an instance of <code>Object</code> or <code>Array</code> that has the metadata set as properties. See the <a href="http://www.adobe.com/go/learn_fms_devguide_en">Flash Media Server Developer Guide</a> for a list of suggested property names. With <code>&#64;clearDataFrame</code> as the first argument, use <code>onMetaData</code> as the second argument and no third argument.
 *
 * @see #client
 * @see #dataReliable
 * @see #play()
 *
 * @example The following example creates two <code>NetStream</code> objects. One is used to publish a live stream to the server, while the other subscribes to the stream.
 * <listing>
 * package {
 *    import flash.display.Sprite;
 *    import flash.net.NetConnection;
 *    import flash.net.NetStream;
 *    import flash.events.NetStatusEvent;
 *    import flash.media.Video;
 *    import flash.utils.setTimeout;
 *
 *
 *    public class TestExample extends Sprite
 *    {
 *      var nc:NetConnection = new NetConnection();
 *      var ns1:NetStream;
 *      var ns2:NetStream;
 *      var vid:Video = new Video(300,300);
 *      var obj:Object = new Object();
 *
 *      public function TestExample() {
 *         nc.objectEncoding = 0;
 *         nc.addEventListener("netStatus", onNCStatus);
 *         nc.connect("rtmp://localhost/FlashVideoApp");
 *         addChild(vid);
 *      }
 *
 *      function onNCStatus(event:NetStatusEvent):void {
 *        switch (event.info.code) {
 *            case "NetConnection.Connect.Success":
 *                trace("You've connected successfully");
 *                ns1 = new NetStream(nc);
 *                ns2 = new NetStream(nc);
 *
 *                ns1.client = new CustomClient();
 *                ns1.publish("dummy", "live");
 *
 *                ns2.play("dummy");
 *                ns2.client = new CustomClient();
 *                vid.attachNetStream(ns2);
 *                setTimeout(sendHello, 3000);
 *                break;
 *
 *            case "NetStream.Publish.BadName":
 *                trace("Please check the name of the publishing stream" );
 *                break;
 *         }
 *      }
 *
 *      function sendHello():void {
 *          ns1.send("myFunction", "hello");
 *      }
 *    }
 *  }
 *
 *  class CustomClient {
 *     public function myFunction(event:String):void {
 *        trace(event);
 *     }
 *  }
 * </listing>
 * <div>The following example creates metadata and adds it to a live stream:
 * <listing>
 * private function netStatusHandler(event:NetStatusEvent):void {
 *      switch (event.info.code) {  
 *         case "NetStream.Publish.Start":
 *             var metaData:Object = new Object();
 *             metaData.title = "myStream";
 *             metaData.width = 400;
 *             metaData.height = 200;
 *             ns.send("&#64;setDataFrame", "onMetaData", metaData);
 *             ns.attachCamera( Camera.getCamera() );
 *             ns.attachAudio( Microphone.getMicrophone() );
 *     }
 * }
 * </listing></div>
 * <div>To respond to a data keyframe added to a video, the client needs to define an <code>onMetaData</code> event handler. The <code>onMetaData</code> event handler is not registered with <code>addEventListener()</code>, but instead is a callback function with the name <code>onMetaData</code>, for example:
 * <listing>
 *  public function onMetaData(info:Object):void {
 *     trace("width: " + info.width);
 *     trace("height: " + info.height);
 *  }
 * </listing></div>
 * <div>This example shows how to create a playlist on the server:
 * <listing>
 *  // Create a NetStream for playing
 *  var my_ns:NetStream = new NetStream(my_nc);
 *  my_video.attachNetStream(my_ns);
 *
 *  // Play the stream record1
 *  my_ns.play("record1", 0, -1, true);
 *
 *  // Switch to the stream live1 and play for 5 seconds.
 *  // Since reset is false, live1 will start to play after record1 is done.
 *  my_ns.play("live1", -1 , 5, false);
 * </listing></div>
 * <div>If the recorded video file contains only data messages, you can either play the video file at the speed at which it was originally recorded, or you can get the data messages all at once.
 * <listing>
 *  //To play at normal speed
 *  var my_ns:NetStream = new NetStream(my_nc);
 *  my_ns.play("log", 0, -1);
 *
 *  //To get the data messages all at once
 *  my_ns.play("log", 0, -1, 3);
 * </listing></div>
 */
"public function send",function send(handlerName/*:String, ...rest*/)/*:void*/ {var rest=Array.prototype.slice.call(arguments,1);
  throw new Error('not implemented'); // TODO: implement!
},
/**
 * Pauses or resumes playback of a stream. The first time you call this method, it pauses play; the next time, it resumes play. You could use this method to let users pause or resume playback by pressing a single button.
 * @see #close()
 * @see #play()
 * @see #pause()
 * @see #resume()
 *
 */
"public function togglePause",function togglePause()/*:void*/ {
  throw new Error('not implemented'); // TODO: implement!
},
/**
 * Establishes a listener to respond when an embedded cue point is reached while playing a video file. You can use the listener to trigger actions in your code when the video reaches a specific cue point, which lets you synchronize other actions in your application with video playback events. For information about video file formats supported by Flash Media Server, see the <a href="http://www.adobe.com/go/learn_fms_fileformats_en">www.adobe.com/go/learn_fms_fileformats_en</a>.
 * <p><code>onCuePoint</code> is actually a property of the <code>NetStream.client</code> object. IThe property is listed in the Events section because it responds to a data event, either when streaming media using Flash Media Server or during FLV file playback. For more information, see the NetStream class description. You cannot use the <code>addEventListener()</code> method, or any other EventDispatcher methods, to listen for, or process <code>onCuePoint</code> as an event. Define a callback function and attach it to one of the following objects:</p>
 * <ul>
 * <li>The object that the <code>client</code> property of a NetStream instance references.</li>
 * <li>An instance of a NetStream subclass. NetStream is a sealed class, which means that properties or methods cannot be added to a NetStream object at runtime. Create a subclass of NetStream and define your event handler in the subclass. You can also make the subclass dynamic and add the event handler function to an instance of the subclass.</li></ul>
 * <p>The associated event listener is triggered after a call to the <code>NetStream.play()</code> method, but before the video playhead has advanced.</p>
 * <p>You can embed the following types of cue points in a video file:</p>
 * <ul>
 * <li>A navigation cue point specifies a keyframe within the video file and the cue point's <code>time</code> property corresponds to that exact keyframe. Navigation cue points are often used as bookmarks or entry points to let users navigate through the video file.</li>
 * <li>An event cue point specifies a time. The time may or may not correspond to a specific keyframe. An event cue point usually represents a time in the video when something happens that could be used to trigger other application events.</li></ul>
 * <p>The <code>onCuePoint</code> event object has the following properties:</p>
 * <table>
 * <tr><th>Property</th><th>Description</th></tr>
 * <tr>
 * <td><code>name</code></td>
 * <td>The name given to the cue point when it was embedded in the video file.</td></tr>
 * <tr>
 * <td><code>parameters</code></td>
 * <td>An associative array of name and value pair strings specified for this cue point. Any valid string can be used for the parameter name or value.</td></tr>
 * <tr>
 * <td><code>time</code></td>
 * <td>The time in seconds at which the cue point occurred in the video file during playback.</td></tr>
 * <tr>
 * <td><code>type</code></td>
 * <td>The type of cue point that was reached, either navigation or event.</td></tr></table>
 * <p>You can define cue points in a video file when you first encode the file, or when you import a video clip in the Flash authoring tool by using the Video Import wizard.</p>
 * <p>The <code>onMetaData</code> event also retrieves information about the cue points in a video file. However the <code>onMetaData</code> event gets information about all of the cue points before the video begins playing. The <code>onCuePoint</code> event receives information about a single cue point at the time specified for that cue point during playback.</p>
 * <p>Generally, to have your code respond to a specific cue point at the time it occurs, use the <code>onCuePoint</code> event to trigger some action in your code.</p>
 * <p>You can use the list of cue points provided to the <code>onMetaData</code> event to let the user start playing the video at predefined points along the video stream. Pass the value of the cue point's <code>time</code> property to the <code>NetStream.seek()</code> method to play the video from that cue point.</p>
 * @example The following example shows how you can load external FLV files and respond to metadata and cue points. Example provided by <a href="http://actionscriptexamples.com/2008/02/26/loading-flv-files-in-actionscript-30-using-the-netconnection-and-netstream-classes/">ActionScriptExamples.com</a>.
 * <listing>
 * var video:Video = new Video();
 * addChild(video);
 *
 * var nc:NetConnection = new NetConnection();
 * nc.connect(null);
 *
 * var ns:NetStream = new NetStream(nc);
 * ns.client = {};
 * ns.client.onMetaData = ns_onMetaData;
 * ns.client.onCuePoint = ns_onCuePoint;
 * ns.play("http://www.helpexamples.com/flash/video/cuepoints.flv");
 *
 * video.attachNetStream(ns);
 *
 * function ns_onMetaData(item:Object):void {
 *     trace("metaData");
 *     // Resize video instance.
 *     video.width = item.width;
 *     video.height = item.height;
 *     // Center video instance on Stage.
 *     video.x = (stage.stageWidth - video.width) / 2;
 *     video.y = (stage.stageHeight - video.height) / 2;
 * }
 *
 * function ns_onCuePoint(item:Object):void {
 *     trace("cuePoint");
 *     trace(item.name + "\t" + item.time);
 * }
 * </listing>
 * @see #client
 * @see #event:onMetaData
 *
 */
/**
 * Establishes a listener to respond when Flash Player receives image data as a byte array embedded in a media file that is playing. The image data can produce either JPEG, PNG, or GIF content. Use the <code>flash.display.Loader.loadBytes()</code> method to load the byte array into a display object.
 * <p><code>onImageData</code> is actually a property of the <code>NetStream.client</code> object. The property is listed in the Events section because it responds to a data event, either when streaming media using Flash Media Server or during FLV file playback. For more information, see the NetStream class description. You cannot use the <code>addEventListener()</code> method, or any other EventDispatcher methods, to listen for, or process <code>onImageData</code> as an event. Define a single callback function and attach it to one of the following objects:</p>
 * <ul>
 * <li>The object that the <code>client</code> property of a NetStream instance references.</li>
 * <li>An instance of a NetStream subclass. NetStream is a sealed class, which means that properties or methods cannot be added to a NetStream object at runtime. Create a subclass of NetStream and define your event handler in the subclass. You can also make the subclass dynamic and add the event handler function to an instance of the subclass.</li></ul>
 * <p>The associated event listener is triggered after a call to the <code>NetStream.play()</code> method, but before the video playhead has advanced.</p>
 * <p>The onImageData event object contains the image data as a byte array sent through an AMF0 data channel.</p>
 * @example The code in this example uses the <code>Netstream.client</code> property to handle the callback functions for <code>onTextData</code> and <code>onImageData</code>. The <code>onImageDataHandler()</code> function uses the onImageData event object <code>imageData</code> to store the byte array. And, the <code>onTextDataHandler()</code> function uses the onTextData event object <code>textData</code> to store the pieces of text data (each piece of data is a property of the <code>textData</code> object).
 * <p>You need to substitute a real location to a media file with text or image metadata for the location <code>"yourURL"</code> in the code.</p>
 * <p>You can also handle image and text data using a custom class. See the article <a href="http://www.adobe.com/devnet/flash/quickstart">Handling metadata and cue points in Flash video</a> for more information and examples.</p>
 * <listing>
 * package {
 *     import flash.display.*;
 *     import flash.net.*;
 *     import flash.media.*;
 *     import flash.system.*;
 *     import flash.events.*;
 *
 *         public class OnTextDataExample extends Sprite {
 *
 *             public function OnTextDataExample():void {
 *
 *                 var customClient:Object = new Object();
 *                 customClient.onImageData = onImageDataHandler;
 *                 customClient.onTextData = onTextDataHandler;
 *
 *                 var my_nc:NetConnection = new NetConnection();
 *                 my_nc.connect(null);
 *                 var my_ns:NetStream = new NetStream(my_nc);
 *                 my_ns.play("yourURL");
 *                 my_ns.client = customClient;
 *
 *                 var my_video:Video = new Video();
 *                 my_video.attachNetStream(my_ns);
 *                 addChild(my_video);
 *
 *             }
 *
 *             public function onImageDataHandler(imageData:Object):void {
 *
 *                 trace("imageData length: " + imageData.data.length);
 *                 var imageloader:Loader = new Loader();
 *                 imageloader.loadBytes(imageData.data); // imageData.data is a ByteArray object.
 *                 addChild(imageloader);
 *             }
 *
 *
 *             public function onTextDataHandler(textData:Object):void {
 *
 *                 trace("--- textData properties ----");
 *                 var key:String;
 *
 *                 for (key in textData) {
 *                     trace(key + ": " + textData[key]);
 *                 }
 *             }
 *
 *         }
 *
 * }
 *
 * </listing>
 * @see NetConnection
 * @see flash.display.Loader#loadBytes()
 * @see #client
 * @see #event:asyncError
 * @see #play()
 * @see #event:onTextData
 *
 */
/**
 * Establishes a listener to respond when Flash Player receives descriptive information embedded in the video being played. For information about video file formats supported by Flash Media Server, see the <a href="http://www.adobe.com/go/learn_fms_fileformats_en">www.adobe.com/go/learn_fms_fileformats_en</a>.
 * <p><code>onMetaData</code> is actually a property of the <code>NetStream.client</code> object. The property is listed in the Events section because it responds to a data event, either when streaming media using Flash Media Server or during FLV file playback. For more information, see the NetStream class description and the <code>NetStream.client</code> property. You cannot use the <code>addEventListener()</code> method, or any other EventDispatcher methods, to listen for or process <code>onMetaData</code> as an event. Define a single callback function and attach it to one of the following objects:</p>
 * <ul>
 * <li>The object that the <code>client</code> property of a NetStream instance references.</li>
 * <li>An instance of a NetStream subclass. NetStream is a sealed class, which means that properties or methods cannot be added to a NetStream object at runtime. You can create a subclass of NetStream and define your event handler in the subclass. You can also make the subclass dynamic and add the event handler function to an instance of the subclass.</li></ul>
 * <p>The Flash Video Exporter utility (version 1.1 or later) embeds a video's duration, creation date, data rates, and other information into the video file itself. Different video encoders embed different sets of meta data.</p>
 * <p>The associated event listener is triggered after a call to the <code>NetStream.play()</code> method, but before the video playhead has advanced.</p>
 * <p>In many cases, the duration value embedded in stream metadata approximates the actual duration but is not exact. In other words, it does not always match the value of the <code>NetStream.time</code> property when the playhead is at the end of the video stream.</p>
 * <p>The event object passed to the onMetaData event handler contains one property for each piece of data.</p>
 * @example The following example shows how you can load external FLV files and respond to metadata and cue points. Example provided by <a href="http://actionscriptexamples.com/2008/02/26/loading-flv-files-in-actionscript-30-using-the-netconnection-and-netstream-classes/">ActionScriptExamples.com</a>.
 * <listing>
 * var video:Video = new Video();
 * addChild(video);
 *
 * var nc:NetConnection = new NetConnection();
 * nc.connect(null);
 *
 * var ns:NetStream = new NetStream(nc);
 * ns.client = {};
 * ns.client.onMetaData = ns_onMetaData;
 * ns.client.onCuePoint = ns_onCuePoint;
 * ns.play("http://www.helpexamples.com/flash/video/cuepoints.flv");
 *
 * video.attachNetStream(ns);
 *
 * function ns_onMetaData(item:Object):void {
 *     trace("metaData");
 *     // Resize video instance.
 *     video.width = item.width;
 *     video.height = item.height;
 *     // Center video instance on Stage.
 *     video.x = (stage.stageWidth - video.width) / 2;
 *     video.y = (stage.stageHeight - video.height) / 2;
 * }
 *
 * function ns_onCuePoint(item:Object):void {
 *     trace("cuePoint");
 *     trace(item.name + "\t" + item.time);
 * }
 * </listing>
 * @see NetConnection
 * @see #client
 * @see #event:asyncError
 * @see #event:onCuePoint
 * @see #play()
 * @see #time
 *
 */
/**
 * Establishes a listener to respond when a NetStream object has completely played a stream. The associated event object provides information in addition to what's returned by the <code>netStatus</code> event. You can use this property to trigger actions in your code when a NetStream object has switched from one stream to another stream in a playlist (as indicated by the information object <code>NetStream.Play.Switch</code>) or when a NetStream object has played to the end (as indicated by the information object <code>NetStream.Play.Complete</code>).
 * <p><code>onPlayStaus</code> is actually a property of the <code>NetStream.client</code> object. The property is listed in the Events section because it responds to a data event, either when streaming media using Flash Media Server or during FLV file playback. For more information, see the NetStream class description. You cannot use the <code>addEventListener()</code> method, or any other EventDispatcher methods, to listen for, or process <code>onPlayStatus</code> as an event. Define a callback function and attach it to one of the following objects:</p>
 * <ul>
 * <li>The object that the <code>client</code> property of a NetStream instance references.</li>
 * <li>An instance of a NetStream subclass. NetStream is a sealed class, which means that properties or methods cannot be added to a NetStream object at runtime. Create a subclass of NetStream and define your event handler in the subclass. You can also make the subclass dynamic and add the event handler function to an instance of the subclass.</li></ul>
 * <p>This event can return an information object with the following properties:</p>
 * <table>
 * <tr><th>Code property</th><th>Level property</th><th>Meaning</th></tr>
 * <tr>
 * <td><code>NetStream.Play.Switch</code></td>
 * <td><code>"status"</code></td>
 * <td>The subscriber is switching from one stream to another in a playlist.</td></tr>
 * <tr>
 * <td><code>NetStream.Play.Complete</code></td>
 * <td><code>"status"</code></td>
 * <td>Playback has completed.</td></tr>
 * <tr>
 * <td><code>NetStream.Play.TransitionComplete</code></td>
 * <td><code>"status"</code></td>
 * <td>The subscriber is switching to a new stream as a result of stream bit-rate switching</td></tr></table>
 * @see #client
 * @see flash.events.NetStatusEvent#NET_STATUS
 * @see #event:asyncError
 * @see #event:onMetaData
 * @see #event:onCuePoint
 *
 */
/**
 * Establishes a listener to respond when Flash Player receives text data embedded in a media file that is playing. The text data is in UTF-8 format and can contain information about formatting based on the 3GP timed text specification.
 * <p><code>onTextData</code> is actually a property of the <code>NetStream.client</code> object. The property is listed in the Events section because it responds to a data event, either when streaming media using Flash Media Server or during FLV file playback. For more information, see the NetStream class description. You cannot use the <code>addEventListener()</code> method, or any other EventDispatcher methods, to listen for, or process <code>onTextData</code> as an event. Define a callback function and attach it to one of the following objects:</p>
 * <ul>
 * <li>The object that the <code>client</code> property of a NetStream instance references.</li>
 * <li>An instance of a NetStream subclass. NetStream is a sealed class, which means that properties or methods cannot be added to a NetStream object at runtime. Create a subclass of NetStream and define your event handler in the subclass. You can also make the subclass dynamic and add the event handler function to an instance of the subclass.</li></ul>
 * <p>The associated event listener is triggered after a call to the <code>NetStream.play()</code> method, but before the video playhead has advanced.</p>
 * <p>The onTextData event object contains one property for each piece of text data.</p>
 * @example The code in this example uses the <code>Netstream.client</code> property to handle the callback functions for <code>onTextData</code> and <code>onImageData</code>. The <code>onImageDataHandler()</code> function uses the onImageData event object <code>imageData</code> to store the byte array. And, the <code>onTextDataHandler()</code> function uses the onTextData event object <code>textData</code> to store the pieces of text data (each piece of data is a property of the <code>textData</code> object).
 * <p>You need to substitute a real location to a media file with text or image metadata for the location <code>"yourURL"</code> in the code.</p>
 * <p>You can also handle image and text data using a custom class. See the article <a href="http://www.adobe.com/devnet/flash/quickstart">Handling metadata and cue points in Flash video</a> for more information and examples.</p>
 * <listing>
 * package {
 *     import flash.display.*;
 *     import flash.net.*;
 *     import flash.media.*;
 *     import flash.system.*;
 *     import flash.events.*;
 *
 *         public class OnTextDataExample extends Sprite {
 *
 *             public function OnTextDataExample():void {
 *
 *                 var customClient:Object = new Object();
 *                 customClient.onImageData = onImageDataHandler;
 *                 customClient.onTextData = onTextDataHandler;
 *
 *                 var my_nc:NetConnection = new NetConnection();
 *                 my_nc.connect(null);
 *                 var my_ns:NetStream = new NetStream(my_nc);
 *                 my_ns.play("yourURL");
 *                 my_ns.client = customClient;
 *
 *                 var my_video:Video = new Video();
 *                 my_video.attachNetStream(my_ns);
 *                 addChild(my_video);
 *
 *             }
 *
 *             public function onImageDataHandler(imageData:Object):void {
 *
 *                 trace("imageData length: " + imageData.data.length);
 *                 var imageloader:Loader = new Loader();
 *                 imageloader.loadBytes(imageData.data); // imageData.data is a ByteArray object.
 *                 addChild(imageloader);
 *             }
 *
 *
 *             public function onTextDataHandler(textData:Object):void {
 *
 *                 trace("--- textData properties ----");
 *                 var key:String;
 *
 *                 for (key in textData) {
 *                     trace(key + ": " + textData[key]);
 *                 }
 *             }
 *
 *         }
 *
 * }
 *
 * </listing>
 * @see NetConnection
 * @see #client
 * @see #event:asyncError
 * @see #play()
 * @see #event:onImageData
 *
 */
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.1"
);