joo.classLoader.prepare("package org.flixel.data",/*
{
	import flash.display.DisplayObject
	import flash.display.LoaderInfo
	import flash.display.Loader
	import flash.display.Sprite
	import flash.net.URLRequest
	import flash.events.Event*/

	/**
	 * This class provides basic high scores and achievements via Kongregate's game API.
	 */
	"public class FlxKong extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.Event);},
	
		/**
		 * Stores the Kongregate API object.
		 * 
		 * @default null
		 */
		"public var",{ API/*:**/:undefined},
		
		/**
		 * Constructor.
		 */
		"public function FlxKong",function FlxKong$()
		{this.super$6();
			this.API = null;
		},
		
		/**
		 * Actually initializes the FlxKong object.  Highly recommend calling this
		 * inside your first game state's <code>update()</code> function to ensure
		 * that all the necessary Flash stage stuff is loaded.
		 */
		"public function init",function init()/*:void*/
		{
			var paramObj/*:Object*/ =/* flash.display.LoaderInfo*/(this.root.loaderInfo).parameters;
			var api_url/*:String*/ = paramObj.api_path || "http://www.kongregate.com/flash/API_AS3_Local.swf";
			
			//Load the API
			var request/*:URLRequest*/ = new flash.net.URLRequest(api_url);
			var loader/*:Loader*/ = new flash.display.Loader();
			loader.contentLoaderInfo.addEventListener(flash.events.Event.COMPLETE,$$bound(this,"APILoaded"));
			loader.load(request);
			this.addChild(loader);
		},
		
		/**
		 * Fired when the Kongregate API finishes loading into the API object.
		 */
		"protected function APILoaded",function APILoaded(event/*:Event*/)/*:void*/
		{
		    this.API = event.target.content;
		    this.API.services.connect();
		},
	];},[],["flash.display.Sprite","flash.display.LoaderInfo","flash.net.URLRequest","flash.display.Loader","flash.events.Event"], "0.8.0", "0.8.1"
);