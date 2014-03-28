joo.classLoader.prepare("package org.flixel",/*
{
	import flash.net.SharedObject
	import flash.net.SharedObjectFlushStatus*/
	
	/**
	 * A class to help automate and simplify save game functionality.
	 */
	"public class FlxSave extends Object",1,function($$private){var is=joo.is;return[function(){joo.classLoader.init(flash.net.SharedObjectFlushStatus);},
	
		/**
		 * Allows you to directly access the data container in the local shared object.
		 * @default null
		 */
		"public var",{ data/*:Object*/:null},
		/**
		 * The name of the local shared object.
		 * @default null
		 */
		"public var",{ name/*:String*/:null},
		/**
		 * The local shared object itself.
		 * @default null
		 */
		"protected var",{ _so/*:SharedObject*/:null},
		
		/**
		 * Blanks out the containers.
		 */
		"public function FlxSave",function FlxSave$()
		{
			this.name = null;
			this._so = null;
			this.data = null;
		},
		
		/**
		 * Automatically creates or reconnects to locally saved data.
		 * 
		 * @param	Name	The name of the object (should be the same each time to access old data).
		 * 
		 * @return	Whether or not you successfully connected to the save data.
		 */
		"public function bind",function bind(Name/*:String*/)/*:Boolean*/
		{
			this.name = null;
			this._so = null;
			this.data = null;
			this.name = Name;
			try
			{
				this._so = flash.net.SharedObject.getLocal(this.name);
			}
			catch(e){if(is(e,Error))
			{
				org.flixel.FlxG.log("WARNING: There was a problem binding to\nthe shared object data from FlxSave.");
				this.name = null;
				this._so = null;
				this.data = null;
				return false;
			}else throw e;}
			this.data = this._so.data;
			return true;
		},
		
		/**
		 * If you don't like to access the data object directly, you can use this to write to it.
		 * 
		 * @param	FieldName		The name of the data field you want to create or overwrite.
		 * @param	FieldValue		The data you want to store.
		 * @param	MinFileSize		If you need X amount of space for your save, specify it here.
		 * 
		 * @return	Whether or not the write and flush were successful.
		 */
		"public function write",function write(FieldName/*:String*/,FieldValue/*:Object*/,MinFileSize/*:uint=0*/)/*:Boolean*/
		{if(arguments.length<3){MinFileSize=0;}
			if(this._so == null)
			{
				org.flixel.FlxG.log("WARNING: You must call FlxSave.bind()\nbefore calling FlxSave.write().");
				return false;
			}
			this.data[FieldName] = FieldValue;
			return this.forceSave(MinFileSize);
		},
		
		/**
		 * If you don't like to access the data object directly, you can use this to read from it.
		 * 
		 * @param	FieldName		The name of the data field you want to read
		 * 
		 * @return	The value of the data field you are reading (null if it doesn't exist).
		 */
		"public function read",function read(FieldName/*:String*/)/*:Object*/
		{
			if(this._so == null)
			{
				org.flixel.FlxG.log("WARNING: You must call FlxSave.bind()\nbefore calling FlxSave.read().");
				return null;
			}
			return this.data[FieldName];
		},
		
		/**
		 * Writes the local shared object to disk immediately.
		 *
		 * @param	MinFileSize		If you need X amount of space for your save, specify it here.
		 *
		 * @return	Whether or not the flush was successful.
		 */
		"public function forceSave",function forceSave(MinFileSize/*:uint=0*/)/*:Boolean*/
		{if(arguments.length<1){MinFileSize=0;}
			if(this._so == null)
			{
				org.flixel.FlxG.log("WARNING: You must call FlxSave.bind()\nbefore calling FlxSave.forceSave().");
				return false;
			}
			var status/*:Object*/ = null;
			try
			{
				status = this._so.flush(MinFileSize);
			}
			catch(e){if(is (e,Error))
			{
				org.flixel.FlxG.log("WARNING: There was a problem flushing\nthe shared object data from FlxSave.");
				return false;
			}else throw e;}
			return status == flash.net.SharedObjectFlushStatus.FLUSHED;
		},
		
		/**
		 * Erases everything stored in the local shared object.
		 * 
		 * @param	MinFileSize		If you need X amount of space for your save, specify it here.
		 * 
		 * @return	Whether or not the clear and flush was successful.
		 */
		"public function erase",function erase(MinFileSize/*:uint=0*/)/*:Boolean*/
		{if(arguments.length<1){MinFileSize=0;}
			if(this._so == null)
			{
				org.flixel.FlxG.log("WARNING: You must call FlxSave.bind()\nbefore calling FlxSave.erase().");
				return false;
			}
			this._so.clear();
			return this.forceSave(MinFileSize);
		},
	];},[],["Object","flash.net.SharedObject","Error","org.flixel.FlxG","flash.net.SharedObjectFlushStatus"], "0.8.0", "0.8.3"
);