package {

public final dynamic class XML extends Object {

  native public function XML(value:* = null):*;
  native public override function hasOwnProperty(propertyName:String):Boolean;
  native public function insertChildBefore(child1:*,child2:*):*;
  native public function replace(propertyName:*,value:*):XML;
  native public function setNotification(f:Function):*;

  native public function toXMLString():String;
  native public override function propertyIsEnumerable(propertyName:String):Boolean;
  native public function setChildren(value:*):XML;
  native public function name():Object;
  native public function normalize():XML;

  native public function inScopeNamespaces():Array;
  native public function setLocalName(name:*):void;
  native public function localName():Object;
  native public function attributes():XMLList;
  native public function processingInstructions(name:* = "*"):XMLList;

  native public function setNamespace(ns:*):void;
  native public function namespace(prefix:* = null):*;
  native public function child(propertyName:*):XMLList;
  native public function childIndex():int;
  native public function contains(value:*):Boolean;

  native public function appendChild(child:*):XML;
  native public function hasComplexContent():Boolean;
  native public function descendants(name:* = "*"):XMLList;
  native public function length():int;
  native public function valueOf():XML;

  native public function parent():*;
  native public function attribute(arg:*):XMLList;
  native public function toString():String;
  native public function hasSimpleContent():Boolean;
  native public function prependChild(value:*):XML;

  native public function setName(name:*):void;
  native public function notification():Function;
  native public function comments():XMLList;
  native public function copy():XML;
  native public function nodeKind():String;

  native public function elements(name:* = "*"):XMLList;
  native public function insertChildAfter(child1:*,child2:*):*;
  native public function addNamespace(ns:*):XML;
  native public function namespaceDeclarations():Array;
  native public function text():XMLList;

  native public function removeNamespace(ns:*):XML;
  native public function children():XMLList;
  static native public final function settings():Object;
  static native public final function set prettyIndent(newIndent:int):void;
  static native public final function setSettings(o:Object = null):void;

  static native public final function get ignoreComments():Boolean;
  static native public final function get prettyIndent():int;
  static native public final function get ignoreProcessingInstructions():Boolean;
  static native public final function get prettyPrinting():Boolean;

  static native public final function get ignoreWhitespace():Boolean;
  static native public final function set ignoreComments(newIgnore:Boolean):void;
  static native public final function set ignoreProcessingInstructions(newIgnore:Boolean):void;
  static native public final function set prettyPrinting(newPretty:Boolean):void;
  static native public final function defaultSettings():Object;

  static native public final function set ignoreWhitespace(newIgnore:Boolean):void;
}
}