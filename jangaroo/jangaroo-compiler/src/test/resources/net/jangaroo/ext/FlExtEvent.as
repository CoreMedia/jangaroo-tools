package net.jangaroo.ext {

public class FlExtEvent {
  public function FlExtEvent(type:String, arguments:Array) {
    super();
  }

  /**
   * The type of event. The type is case-sensitive.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e55.html Event objects
   */
  public native function get type():String;

  /**
   * The options object passed to <a href="#!/api/Ext.util.Observable-method-addListener" rel="Ext.util.Observable-method-addListener" class="docClass">Ext.util.Observable.addListener</a>.
   */
  public native function get eOpts():Object;
}
}