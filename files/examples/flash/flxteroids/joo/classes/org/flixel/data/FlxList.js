joo.classLoader.prepare("package org.flixel.data",/*
{
	import org.flixel.**/
	
	/**
	 * The world's smallest linked list class.
	 * Useful for optimizing time-critical or highly repetitive tasks!
	 * See <code>FlxQuadTree</code> for how to use it, IF YOU DARE.
	 */
	"public class FlxList",1,function($$private){;return[
	
		/**
		 * Stores a reference to a <code>FlxObject</code>.
		 */
		"public var",{ object/*:FlxObject*/:null},
		/**
		 * Stores a reference to the next link in the list.
		 */
		"public var",{ next/*:FlxList*/:null},
		
		/**
		 * Creates a new link, and sets <code>object</code> and <code>next</code> to <code>null</null>.
		 */
		"public function FlxList",function FlxList$()
		{
			this.object = null;
			this.next = null;
		},
	];},[],[], "0.8.0", "0.8.1"
);