package {

public final dynamic class XMLList extends Object {
  native public function XMLList(value:* = null):*;

  native public override function hasOwnProperty(propertyName:String):Boolean;
  native public function insertChildBefore(child1:*,child2:*):*;
  native public function replace(propertyName:*,value:*):XML;
  native public override function propertyIsEnumerable(propertyName:String):Boolean;
  native public function setChildren(value:*):XML;

  native public function name():Object;
  native public function normalize():XMLList;
  native public function inScopeNamespaces():Array;
  native public function toXMLString():String;
  native public function descendants(name:* = "*"):XMLList;

  native public function attributes():XMLList;
  native public function processingInstructions(name:* = "*"):XMLList;
  native public function setNamespace(ns:*):void;
  native public function setLocalName(name:*):void;
  native public function namespace(prefix:* = null):*;

  native public function attribute(arg:*):XMLList;
  native public function childIndex():int;
  native public function contains(value:*):Boolean;
  native public function appendChild(child:*):XML;
  native public function hasComplexContent():Boolean;

  native public function localName():Object;
  native public function length():int;
  native public function valueOf():XMLList;
  native public function parent():*;
  native public function child(propertyName:*):XMLList;

  native public function toString():String;
  native public function hasSimpleContent():Boolean;
  native public function prependChild(value:*):XML;
  native public function setName(name:*):void;
  native public function comments():XMLList;

  native public function copy():XMLList;
  native public function nodeKind():String;
  native public function elements(name:* = "*"):XMLList;
  native public function insertChildAfter(child1:*,child2:*):*;
  native public function addNamespace(ns:*):XML;

  native public function namespaceDeclarations():Array;
  native public function text():XMLList;
  native public function removeNamespace(ns:*):XML;
  native public function children():XMLList;
}
}