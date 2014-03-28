joo.classLoader.prepare("package js",/* {*/

"public class Attr extends js.Node",2,function($$private){;return[ 

  /**
   * True if this attribute was explicitly given a value in the instance document, false otherwise.
   * If the application changed the value of this attribute node (even if it ends up having the same value as the
   * default value) then it is set to true. The implementation may handle attributes with default values from other
   * schemas similarly but applications should use Document.normalizeDocument() to guarantee this information is
   * up-to-date.
   */
  "public native function get specified"/*() : Boolean;*/,

  /**
   * Returns the name of this attribute. If Node.localName is different from null, this attribute is a qualified name.
   */
  "public native function get name"/*() : String;*/,

  /**
   * On retrieval, the value of the attribute is returned as a string. Character and general entity references are
   * replaced with their values. See also the method getAttribute on the Element interface.
   * On setting, this creates a Text node with the unparsed contents of the string, i.e. any characters that an XML
   * processor would recognize as markup are instead treated as literal text. See also the method Element.setAttribute().
   */
  "public native function get value"/*(): Object;*/,

  /**
   * The Element node this attribute is attached to or null if this attribute is not in use.
   * @since DOM Level 2
   */
  "public native function get ownerElement"/*() : Element;*/,

];},[],["js.Node"], "0.8.0", "0.8.1"

);