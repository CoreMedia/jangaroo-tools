package js{

public class Window {

  public native function get top() : Window;



  public native function get parent() : Window;



  public native function get frameElement() : Element;



  public native function get status() : String;



  public native function set status(status : String) : void;



  public native function get document() : Document;



  public native function get navigator() : *;



  public native function get location() : Location;



  public native function get event() : Event;



  public native function open(name : String = null, mode: String = null, windowFeatures : String = null) : Window;



  public native function focus() : void;



  public native function addEventListener(eventType : String, handler : Function, capture : Boolean = false) : void;



  public native function setTimeout(handler : * /* Function or String */, millies : Number) : Object;



  public native function clearTimeout(timer : Object) : void;



  public native function setInterval(handler : Function, millies : Number) : Object;



  public native function clearInterval(interval : Object) : void;



  public native function alert(message : String) : void;



  public native function confirm(message : String) : Boolean;



  public native function prompt(message : String, defaultValue : String) : String;



  public native function get innerWidth() : Number;



  public native function get innerHeight() : Number;



  public native function eval(expr : String) : *;



  public native function getComputedStyle(elem : Element, pseudoElt : Element) : Style;



  public native function set location(location : String) : void;



  public native function get screen() : Screen;



  public native function moveTo(x:int, y:int) : void;



  public native function get closed() : Boolean;



  public native function close() : void;

}

}

