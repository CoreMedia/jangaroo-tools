joo.classLoader.prepare("package org.flixel",/*
{*/
	/**
	 * FlxMonitor is a simple class that aggregates and averages data.
	 * Flixel uses this to display the framerate and profiling data
	 * in the developer console.  It's nice for keeping track of
	 * things that might be changing too fast from frame to frame.
	 */
	"public class FlxMonitor",1,function($$private){;return[
	
		/**
		 * Stores the requested size of the monitor array.
		 */
		"protected var",{ _size/*:uint*/:0},
		/**
		 * Keeps track of where we are in the array.
		 */
		"protected var",{ _itr/*:uint*/:0},
		/**
		 * An array to hold all the data we are averaging.
		 */
		"protected var",{ _data/*:Array*/:null},
		
		/**
		 * Creates the monitor array and sets the size.
		 * 
		 * @param	Size	The desired size - more entries means a longer window of averaging.
		 * @param	Default	The default value of the entries in the array (0 by default).
		 */
		"public function FlxMonitor",function FlxMonitor$(Size/*:uint*/,Default/*:Number=0*/)
		{if(arguments.length<2){Default=0;}
			this._size = Size;
			if(this._size <= 0)
				this._size = 1;
			this._itr = 0;
			this._data = new Array(this._size);
			var i/*:uint*/ = 0;
			while(i < this._size)
				this._data[i++] = Default;
		},
		
		/**
		 * Adds an entry to the array of data.
		 * 
		 * @param	Data	The value you want to track and average.
		 */
		"public function add",function add(Data/*:Number*/)/*:void*/
		{
			this._data[this._itr++] = Data;
			if(this._itr >= this._size)
				this._itr = 0;
		},
		
		/**
		 * Averages the value of all the numbers in the monitor window.
		 * 
		 * @return	The average value of all the numbers in the monitor window.
		 */
		"public function average",function average()/*:Number*/
		{
			var sum/*:Number*/ = 0;
			var i/*:uint*/ = 0;
			while(i < this._size)
				sum += this._data[i++];
			return sum/this._size;
		},
	];},[],["Array"], "0.8.0", "0.8.3"
);