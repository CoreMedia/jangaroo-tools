joo.classLoader.prepare("package js",/* {*/

"public class Node",1,function($$private){;return[ 

  "public static const",{ ELEMENT_NODE/* : int*/ : 1},

  "public static const",{ ATTRIBUTE_NODE/* : int*/ : 2},

  "public static const",{ TEXT_NODE/* : int*/ : 3},

  "public static const",{ CDATA_SECTION_NODE/* : int*/ : 4},

  "public static const",{ ENTITY_REFERENCE_NODE/* : int*/ : 5},

  "public static const",{ ENTITY_NODE/* : int*/ : 6},

  "public static const",{ PROCESSING_INSTRUCTION_NODE/* : int*/ : 7},

  "public static const",{ COMMENT_NODE/* : int*/ : 8},

  "public static const",{ DOCUMENT_NODE/* : int*/ : 9},

  "public static const",{ DOCUMENT_TYPE_NODE/* : int*/ : 10},

  "public static const",{ DOCUMENT_FRAGMENT_NODE/* : int*/ : 11},

  "public static const",{ NOTATION_NODE/* : int*/ : 12},

  "public static const",{ DOCUMENT_POSITION_DISCONNECTED/* : int*/ : 1},

  "public static const",{ DOCUMENT_POSITION_PRECEDING/* : int*/ : 2},

  "public static const",{ DOCUMENT_POSITION_FOLLOWING/* : int*/ : 4},

  "public static const",{ DOCUMENT_POSITION_CONTAINS/* : int*/ : 8},

  "public static const",{ DOCUMENT_POSITION_CONTAINED_BY/* : int*/ : 16},

  "public static const",{ DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC/* : int*/ : 32},



  "public native function get nodeName"/*() : String;*/,



  "public native function get localName"/*() : String;*/,



  "public native function get namespaceURI"/*() : String;*/,



  "public native function get nodeType"/*() : Number;*/,



  "public native function get nodeValue"/*() : String;*/,



  "public native function get parentNode"/*() : Element;*/,



  "public native function get childNodes"/*() : Collection;*/,



  "public native function get firstChild"/*() : Node;*/,



  "public native function get lastChild"/*() : Node;*/,



  "public native function get nextSibling"/*() : Node;*/,



  "public native function get previousSibling"/*() : Node;*/,



  "public native function setAttribute"/*(name : String, value : Object) : void;*/,



  "public native function removeChild"/*(child : Node) : Node;*/,



  "public native function appendChild"/*(child : Node) : Node;*/,



  "public native function insertBefore"/*(newNode : Node, refNode : Node) : Node;*/,



  "public native function replaceChild"/*(newChild : Node, oldChild : Node) : Node;*/,



  "public native function cloneNode"/*(recursive : Boolean) : Node;*/,



  "public native function getElementsByTagName"/*(name : String) : Collection;*/,



  "public native function getElementsByTagNameNS"/*(ns : String, name : String) : Collection;*/,



  "public native function addEventListener"/*(eventType : String, handler : Function, capture : Boolean) : void;*/,



  "public native function removeEventListener"/*(eventType : String, handler : Function, capture : Boolean) : void;*/,



  "public native function attachEvent"/*(eventType : String, handler : Function) : Boolean;*/,



  "public native function detachEvent"/*(eventType : String, handler : Function) : void;*/,

];},[],[], "0.8.0", "0.8.3"

);