joo.classLoader.prepare("package flash.events",/* {*/






/**
 * A NetConnection, NetStream, or SharedObject object dispatches NetStatusEvent objects when a it reports its status. There is only one type of status event: <code>NetStatusEvent.NET_STATUS</code>.
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/NetStatusEvent.html#includeExamplesSummary">View the examples</a></p>
 * @see flash.net.NetConnection
 * @see flash.net.NetStream
 * @see flash.net.SharedObject
 * @see #NET_STATUS
 *
 */
"public class NetStatusEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * An object with properties that describe the object's status or error condition.
   * <p>The information object could have a <code>code</code> property containing a string that represents a specific event or a <code>level</code> property containing a string that is either <code>"status"</code> or <code>"error"</code>.</p>
   * <p>The information object could also be something different. The <code>code</code> and <code>level</code> properties might not work for some implementations and some servers might send different objects.</p>
   * <p>P2P connections send messages to a <code>NetConnection</code> with a <code>stream</code> parameter in the information object that indicates which <code>NetStream</code> the message pertains to.</p>
   * <p>For example, Flex Data Services sends Message objects that cause coercion errors if you try to access the <code>code</code> or <code>level</code> property.</p>
   * <p>The following table describes the possible string values of the <code>code</code> and <code>level</code> properties.</p>
   * <table>
   * <tr><th>Code property</th><th>Level property</th><th>Meaning</th></tr>
   * <tr>
   * <td><code>"NetConnection.Call.BadVersion"</code></td>
   * <td><code>"error"</code></td>
   * <td>Packet encoded in an unidentified format.</td></tr>
   * <tr>
   * <td><code>"NetConnection.Call.Failed"</code></td>
   * <td><code>"error"</code></td>
   * <td>The <code>NetConnection.call()</code> method was not able to invoke the server-side method or command.</td></tr>
   * <tr>
   * <td><code>"NetConnection.Call.Prohibited"</code></td>
   * <td><code>"error"</code></td>
   * <td>An Action Message Format (AMF) operation is prevented for security reasons. Either the AMF URL is not in the same domain as the file containing the code calling the <code>NetConnection.call()</code> method, or the AMF server does not have a policy file that trusts the domain of the the file containing the code calling the <code>NetConnection.call()</code> method.</td></tr>
   * <tr>
   * <td><code>"NetConnection.Connect.AppShutdown"</code></td>
   * <td><code>"error"</code></td>
   * <td>The server-side application is shutting down.</td></tr>
   * <tr>
   * <td><code>"NetConnection.Connect.Closed"</code></td>
   * <td><code>"status"</code></td>
   * <td>The connection was closed successfully.</td></tr>
   * <tr>
   * <td><code>"NetConnection.Connect.Failed"</code></td>
   * <td><code>"error"</code></td>
   * <td>The connection attempt failed.</td></tr>
   * <tr>
   * <td><code>"NetConnection.Connect.IdleTimeout"</code></td>
   * <td><code>"status"</code></td>
   * <td>Flash Media Server disconnected the client because the client was idle longer than the configured value for <code><MaxIdleTime></code>. On Flash Media Server, <code><AutoCloseIdleClients></code> is disabled by default. When enabled, the default timeout value is 3600 seconds (1 hour). For more information, see <a href="http://help.adobe.com/en_US/flashmediaserver/configadmin/WS5b3ccc516d4fbf351e63e3d119f2925e64-7ff0.html#WS5b3ccc516d4fbf351e63e3d119f2925e64-7fe9">Close idle connections</a>.</td></tr>
   * <tr>
   * <td><code>"NetConnection.Connect.InvalidApp"</code></td>
   * <td><code>"error"</code></td>
   * <td>The application name specified in the call to <code>NetConnection.connect()</code> is invalid.</td></tr>
   * <tr>
   * <td><code>"NetConnection.Connect.NetworkChange"</code></td>
   * <td><code>"status"</code></td>
   * <td>
   * <p>Flash Player has detected a network change, for example, a dropped wireless connection, a successful wireless connection,or a network cable loss.</p>
   * <p>Use this event to check for a network interface change. Don't use this event to implement your NetConnection reconnect logic. Use <code>"NetConnection.Connect.Closed"</code> to implement your NetConnection reconnect logic.</p></td></tr>
   * <tr>
   * <td><code>"NetConnection.Connect.Rejected"</code></td>
   * <td><code>"error"</code></td>
   * <td>The connection attempt did not have permission to access the application.</td></tr>
   * <tr>
   * <td><code>"NetConnection.Connect.Success"</code></td>
   * <td><code>"status"</code></td>
   * <td>The connection attempt succeeded.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Connect.Failed"</code></a></td>
   * <td><code>"error"</code></td>
   * <td>The NetGroup connection attempt failed. The <code>info.group</code> property indicates which NetGroup failed.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Connect.Rejected"</code></a></td>
   * <td><code>"error"</code></td>
   * <td>The NetGroup is not authorized to function. The <code>info.group</code> property indicates which NetGroup was denied.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Connect.Succcess"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>The NetGroup is successfully constructed and authorized to function. The <code>info.group</code> property indicates which NetGroup has succeeded.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.LocalCoverage.Notify"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a portion of the group address space for which this node is responsible changes.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.MulticastStream.PublishNotify"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a new named stream is detected in NetGroup's Group. The <code>info.name:String</code> property is the name of the detected stream.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.MulticastStream.UnpublishNotify"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a named stream is no longer available in the Group. The <code>info.name:String</code> property is name of the stream which has disappeared.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Neighbor.Connect"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a neighbor connects to this node. The <code>info.neighbor:String</code> property is the group address of the neighbor. The <code>info.peerID:String</code> property is the peer ID of the neighbor.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Neighbor.Disconnect"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a neighbor disconnects from this node. The <code>info.neighbor:String</code> property is the group address of the neighbor. The <code>info.peerID:String</code> property is the peer ID of the neighbor.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Posting.Notify"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a new Group Posting is received. The <code>info.message:Object</code> property is the message. The <code>info.messageID:String</code> property is this message's messageID.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Replication.Fetch.Failed"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a fetch request for an object (previously announced with NetGroup.Replication.Fetch.SendNotify) fails or is denied. A new attempt for the object will be made if it is still wanted. The <code>info.index:Number</code> property is the index of the object that had been requested.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Replication.Fetch.Result"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a fetch request was satisfied by a neighbor. The <code>info.index:Number</code> property is the object index of this result. The <code>info.object:Object</code> property is the value of this object. This index will automatically be removed from the Want set. If the object is invalid, this index can be re-added to the Want set with <code>NetGroup.addWantObjects()</code>.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Replication.Fetch.SendNotify"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when the Object Replication system is about to send a request for an object to a neighbor.The <code>info.index:Number</code> property is the index of the object that is being requested.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.Replication.Request"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a neighbor has requested an object that this node has announced with <code>NetGroup.addHaveObjects()</code>. This request <b>must</b> eventually be answered with either <code>NetGroup.writeRequestedObject()</code> or <code>NetGroup.denyRequestedObject()</code>. Note that the answer may be asynchronous. The <code>info.index:Number</code> property is the index of the object that has been requested. The <code>info.requestID:int</code> property is the ID of this request, to be used by <code>NetGroup.writeRequestedObject()</code> or <code>NetGroup.denyRequestedObject()</code>.</td></tr>
   * <tr>
   * <td><a><code>"NetGroup.SendTo.Notify"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>Sent when a message directed to this node is received. The <code>info.message:Object</code> property is the message. The <code>info.from:String</code> property is the groupAddress from which the message was received. The <code>info.fromLocal:Boolean</code> property is <code>TRUE</code> if the message was sent by this node (meaning the local node is the nearest to the destination group address), and <code>FALSE</code> if the message was received from a different node. To implement recursive routing, the message must be resent with <code>NetGroup.sendToNearest()</code> if <code>info.fromLocal</code> is <code>FALSE</code>.</td></tr>
   * <tr>
   * <td><code>"NetStream.Buffer.Empty"</code></td>
   * <td><code>"status"</code></td>
   * <td>Flash Player is not receiving data quickly enough to fill the buffer. Data flow is interrupted until the buffer refills, at which time a <code>NetStream.Buffer.Full</code> message is sent and the stream begins playing again.</td></tr>
   * <tr>
   * <td><code>"NetStream.Buffer.Flush"</code></td>
   * <td><code>"status"</code></td>
   * <td>Data has finished streaming, and the remaining buffer is emptied.</td></tr>
   * <tr>
   * <td><code>"NetStream.Buffer.Full"</code></td>
   * <td><code>"status"</code></td>
   * <td>The buffer is full and the stream begins playing.</td></tr>
   * <tr>
   * <td><code>"NetStream.Connect.Closed"</code></td>
   * <td><code>"status"</code></td>
   * <td>The P2P connection was closed successfully. The <code>info.stream</code> property indicates which stream has closed.</td></tr>
   * <tr>
   * <td><code>"NetStream.Connect.Failed"</code></td>
   * <td><code>"error"</code></td>
   * <td>The P2P connection attempt failed. The <code>info.stream</code> property indicates which stream has failed.</td></tr>
   * <tr>
   * <td><a><code>"NetStream.Connect.Rejected"</code></a></td>
   * <td><code>"error"</code></td>
   * <td>The P2P connection attempt did not have permission to access the other peer. The <code>info.stream</code> property indicates which stream was rejected.</td></tr>
   * <tr>
   * <td><a><code>"NetStream.Connect.Success"</code></a></td>
   * <td><code>"status"</code></td>
   * <td>The P2P connection attempt succeeded. The <code>info.stream</code> property indicates which stream has succeeded.</td></tr>
   * <tr>
   * <td><code>"NetStream.DRM.UpdateNeeded"</code></td>
   * <td><code>"status"</code></td>
   * <td>A NetStream object is attempting to play protected content, but the required Flash Access module is either not present, not permitted by the effective content policy, or not compatible with the current player. To update the module or player, use the <code>update()</code> method of flash.system.SystemUpdater.</td></tr>
   * <tr>
   * <td><code>"NetStream.Failed"</code></td>
   * <td><code>"error"</code></td>
   * <td>(Flash Media Server) An error has occurred for a reason other than those listed in other event codes.</td></tr>
   * <tr>
   * <td><code>"NetStream.MulticastStream.Reset"</code></td>
   * <td><code>"status"</code></td>
   * <td>A multicast subscription has changed focus to a different stream published with the same name in the same group. Local overrides of multicast stream parameters are lost. Reapply the local overrides or the new stream's default parameters will be used.</td></tr>
   * <tr>
   * <td><code>"NetStream.Pause.Notify"</code></td>
   * <td><code>"status"</code></td>
   * <td>The stream is paused.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.Failed"</code></td>
   * <td><code>"error"</code></td>
   * <td>An error has occurred in playback for a reason other than those listed elsewhere in this table, such as the subscriber not having read access.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.FileStructureInvalid"</code></td>
   * <td><code>"error"</code></td>
   * <td>(AIR and Flash Player 9.0.115.0) The application detects an invalid file structure and will not try to play this type of file.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.InsufficientBW"</code></td>
   * <td><code>"warning"</code></td>
   * <td>(Flash Media Server) The client does not have sufficient bandwidth to play the data at normal speed.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.NoSupportedTrackFound"</code></td>
   * <td><code>"error"</code></td>
   * <td>(AIR and Flash Player 9.0.115.0) The application does not detect any supported tracks (video, audio or data) and will not try to play the file.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.PublishNotify"</code></td>
   * <td><code>"status"</code></td>
   * <td>The initial publish to a stream is sent to all subscribers.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.Reset"</code></td>
   * <td><code>"status"</code></td>
   * <td>Caused by a play list reset.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.Start"</code></td>
   * <td><code>"status"</code></td>
   * <td>Playback has started.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.Stop"</code></td>
   * <td><code>"status"</code></td>
   * <td>Playback has stopped.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.StreamNotFound"</code></td>
   * <td><code>"error"</code></td>
   * <td>The file passed to the <code>NetStream.play()</code> method can't be found.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.Transition"</code></td>
   * <td><code>"status"</code></td>
   * <td>(Flash Media Server 3.5) The server received the command to transition to another stream as a result of bitrate stream switching. This code indicates a success status event for the <code>NetStream.play2()</code> call to initiate a stream switch. If the switch does not succeed, the server sends a <code>NetStream.Play.Failed</code> event instead. When the stream switch occurs, an <code>onPlayStatus</code> event with a code of "NetStream.Play.TransitionComplete" is dispatched. For Flash Player 10 and later.</td></tr>
   * <tr>
   * <td><code>"NetStream.Play.UnpublishNotify"</code></td>
   * <td><code>"status"</code></td>
   * <td>An unpublish from a stream is sent to all subscribers.</td></tr>
   * <tr>
   * <td><code>"NetStream.Publish.BadName"</code></td>
   * <td><code>"error"</code></td>
   * <td>Attempt to publish a stream which is already being published by someone else.</td></tr>
   * <tr>
   * <td><code>"NetStream.Publish.Idle"</code></td>
   * <td><code>"status"</code></td>
   * <td>The publisher of the stream is idle and not transmitting data.</td></tr>
   * <tr>
   * <td><code>"NetStream.Publish.Start"</code></td>
   * <td><code>"status"</code></td>
   * <td>Publish was successful.</td></tr>
   * <tr>
   * <td><code>"NetStream.Record.AlreadyExists"</code></td>
   * <td><code>"status"</code></td>
   * <td>The stream being recorded maps to a file that is already being recorded to by another stream. This can happen due to misconfigured virtual directories.</td></tr>
   * <tr>
   * <td><code>"NetStream.Record.Failed"</code></td>
   * <td><code>"error"</code></td>
   * <td>An attempt to record a stream failed.</td></tr>
   * <tr>
   * <td><code>"NetStream.Record.NoAccess"</code></td>
   * <td><code>"error"</code></td>
   * <td>Attempt to record a stream that is still playing or the client has no access right.</td></tr>
   * <tr>
   * <td><code>"NetStream.Record.Start"</code></td>
   * <td><code>"status"</code></td>
   * <td>Recording has started.</td></tr>
   * <tr>
   * <td><code>"NetStream.Record.Stop"</code></td>
   * <td><code>"status"</code></td>
   * <td>Recording stopped.</td></tr>
   * <tr>
   * <td><code>"NetStream.Seek.Failed"</code></td>
   * <td><code>"error"</code></td>
   * <td>The seek fails, which happens if the stream is not seekable.</td></tr>
   * <tr>
   * <td><code>"NetStream.Seek.InvalidTime"</code></td>
   * <td><code>"error"</code></td>
   * <td>For video downloaded progressively, the user has tried to seek or play past the end of the video data that has downloaded thus far, or past the end of the video once the entire file has downloaded. The <code>info.details</code> property of the event object contains a time code that indicates the last valid position to which the user can seek.</td></tr>
   * <tr>
   * <td><code>"NetStream.Seek.Notify"</code></td>
   * <td><code>"status"</code></td>
   * <td>
   * <p>The seek operation is complete.</p>
   * <p>Sent when <code>NetStream.seek()</code> is called on a stream in AS3 NetStream Data Generation Mode. The info object is extended to include <code>info.seekPoint</code> which is the same value passed to <code>NetStream.seek()</code>.</p></td></tr>
   * <tr>
   * <td><code>"NetStream.Step.Notify"</code></td>
   * <td><code>"status"</code></td>
   * <td>The step operation is complete.</td></tr>
   * <tr>
   * <td><code>"NetStream.Unpause.Notify"</code></td>
   * <td><code>"status"</code></td>
   * <td>The stream is resumed.</td></tr>
   * <tr>
   * <td><code>"NetStream.Unpublish.Success"</code></td>
   * <td><code>"status"</code></td>
   * <td>The unpublish operation was successfuul.</td></tr>
   * <tr>
   * <td><code>"SharedObject.BadPersistence"</code></td>
   * <td><code>"error"</code></td>
   * <td>A request was made for a shared object with persistence flags, but the request cannot be granted because the object has already been created with different flags.</td></tr>
   * <tr>
   * <td><code>"SharedObject.Flush.Failed"</code></td>
   * <td><code>"error"</code></td>
   * <td>The "pending" status is resolved, but the <code>SharedObject.flush()</code> failed.</td></tr>
   * <tr>
   * <td><code>"SharedObject.Flush.Success"</code></td>
   * <td><code>"status"</code></td>
   * <td>The "pending" status is resolved and the <code>SharedObject.flush()</code> call succeeded.</td></tr>
   * <tr>
   * <td><code>"SharedObject.UriMismatch"</code></td>
   * <td><code>"error"</code></td>
   * <td>An attempt was made to connect to a NetConnection object that has a different URI (URL) than the shared object.</td></tr></table>
   * <p>If you consistently see errors regarding the buffer, try changing the buffer using the <code>NetStream.bufferTime</code> property.</p>
   * @see flash.net.NetConnection
   * @see flash.net.NetStream
   * @see flash.net.NetGroup
   *
   * @example The following example shows an event handler function that tests for the <code>"NetStream.Seek.InvalidTime"</code> error. The <code>"NetStream.Seek.InvalidTime"</code> error happens when the user attempts to seek beyond the end of the downloaded stream. The example tests the value of the event object's <code>info.code</code> property. In case the error occurs, the <code>eventObj.info.details</code> property is assigned to a variable to use as a parameter for the stream's <code>seek()</code> method. The <code>eventObj.info.details</code> contains the last valid position available to handle the error. So, the user goes to a valid location at the end of the downloaded stream.
   * <listing>
   * function videoStatus(eventObj:NetStatusEvent):Void
   * {
   *     switch(eventObj.info.code)
   *     {
   *         case "NetStream.Seek.InvalidTime":
   *         {
   *             var validSeekTime:Number = eventObj.info.details;
   *             nStream.seek(validSeekTime);
   *             break;
   *         }
   *     }
   * }
   * </listing>
   */
  "public native function get info"/*():Object;*/,

  /**
   * @private
   */

  "public native function set info"/*(value:Object):void;*/,

  /**
   * Creates an Event object that contains information about <code>netStatus</code> events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one type of status event: <code>NetStatusEvent.NET_STATUS</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param info An object containing properties that describe the object's status. Event listeners can access this object through the <code>info</code> property.
   *
   * @see #NET_STATUS
   *
   */
  "public function NetStatusEvent",function NetStatusEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, info/*:Object = null*/) {if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}info = null;}
    this.super$2(type, bubbles, cancelable);
    this.info = info;
  },

  /**
   * Creates a copy of the NetStatusEvent object and sets the value of each property to match that of the original.
   * @return A new NetStatusEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.NetStatusEvent(this.type, this.bubbles, this.cancelable, this.info);
  },

  /**
   * Returns a string that contains all the properties of the NetStatusEvent object. The string is in the following format:
   * <p><code>[NetStatusEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> info=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the NetStatusEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("NetStatusEvent", "type", "bubbles", "cancelable", "info");
  },

  /**
   * Defines the value of the <code>type</code> property of a <code>netStatus</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>info</code></td>
   * <td>An object with properties that describe the object's status or error condition.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The NetConnection or NetStream object reporting its status.</td></tr></table>
   * @see #info
   * @see flash.net.NetConnection#event:netStatus
   * @see flash.net.NetStream#event:netStatus
   * @see flash.net.SharedObject#event:netStatus
   *
   */
  "public static const",{ NET_STATUS/*:String*/ : "netStatus"},

];},[],["flash.events.Event"], "0.8.0", "0.8.3"
);