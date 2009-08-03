package js {
public class Element extends DocumentOrElement {
  public native function get id() : String;

  public native function set id(id : String) : void;

  public native function get ownerDocument() : Document;

  public native function get contentWindow() : Window;

  public native function get innerHTML() : String;

  public native function set innerHTML(s : String) : void;

  public native function get outerHTML() : String;

  public native function set outerHTML(s : String) : void;

  public native function get style() : Style;

  public native function get filters() : Collection; // IE only
  public native function get offsetLeft() : Number;

  public native function get offsetTop() : Number;

  public native function get offsetWidth() : Number;

  public native function get offsetHeight() : Number;

  public native function get offsetParent() : Element;

  public native function get clientWidth() : Number;

  public native function get clientHeight() : Number;

  public native function get className() : String;

  public native function set className(s : String) : void;

  public native function get scrollLeft() : Number;

  public native function set scrollLeft(value : Number) : void;

  public native function get scrollTop()  : Number;

  public native function set scrollTop(value : Number) : void;

  public native function focus() : void;

  public native function select() : void;

  public native function get attributes() : Collection;

  public native function mergeAttributes(withElem : Element, copyId : Boolean = true) : void;

  public native function get htmlFor() : String;

  public native function set htmlFor(htmlFor : String) : void;

  public native function get scopeName() : String;   // IE only
  public native function getAttribute(name : String) : Object;

  public native function removeAttribute(name : String) : void;

  public native function get type() : String;

  public native function get title() : String;

  public native function get defaultChecked() : String;

  public native function set defaultChecked(checked : String) : void;

  public native function get tBodies() : Array;

  public native function get rows() : Array;

  public native function get cells() : Array;

  public native function get name() : String;

  public native function set name(name : String) : void;

  public native function get value() : String;

  public native function set value(value : String) : void;

  public native function get defaultValue() : String;

  public native function set defaultValue(value : String) : void;

  public native function get checked() : Boolean;

  public native function set checked(checked : Boolean) : void;

  public native function get disabled() : Boolean;

  public native function get selectedIndex() : Number; // <select>
  public native function set selectedIndex(value : Number) : void; // <select>
  public native function get src() : String; // <script>, <img>
  public native function set src(url : String) : void; // <script>, <img>
  public native function get href() : String; // <a>, <link>
  public native function set href(href : String) : void; // <a>, <link>
  public native function get target() : String; // <a>
  public native function set target(target : String) : void; // <a>
  public native function get colSpan() : Number; // <th>, <td>
  public native function set colSpan(span : Number) : void; // <th>, <td>
  public native function get rowSpan() : Number; // <th>, <td>
  public native function set rowSpan(span : Number) : void; // <th>, <td>
  public native function scrollIntoView(alignWithTop : Boolean = true) : void;

}
}
