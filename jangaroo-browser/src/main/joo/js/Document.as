package js{

public class Document extends Node {

  public native function get compatMode() : String;



  public native function get designMode() : String;



  public native function set designMode(onOrOff : String) : void;



  public native function execCommand(commandName : String, showDefaultUI : Boolean = true, valueArgument : * = undefined) : void;



  public native function get defaultView() : Window;  // not in IE

  public native function get parentWindow() : Window; // IE only

  public native function get documentElement() : Element;



  public native function get body() : Element;



  public native function get title() : String;



  public native function set title(title : String) : void;



  public native function get forms() : Array;



  public native function get frames() : Array;



  public native function get cookie(): String;



  public native function set cookie(value : String) : void;



  public native function createElement(name : String) : Element;



  public native function createTextNode(text : String) : TextNode;



  public native function createComment(comment : String) : Node;



  public native function getElementById(id : String) : Element;



  public native function write(text : String) : void;



  public native function close() : void;



  public native function createEvent(eventType : String) : Event;



  public native function dispatchEvent(event:Event):void;



  public native function get location() : *;

}

}

