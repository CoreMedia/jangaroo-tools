joo.classLoader.prepare("package org.flixel",/*
{
	import flash.display.BitmapData
	import flash.geom.Rectangle*/
	
	/**
	 * This is the basic "environment object" class, used to create simple walls and floors.
	 * It can be filled with a random selection of tiles to quickly add detail.
	 */
	"public class FlxTileblock extends org.flixel.FlxSprite",5,function($$private){;return[
			
		/**
		 * Creates a new <code>FlxBlock</code> object with the specified position and size.
		 * 
		 * @param	X			The X position of the block.
		 * @param	Y			The Y position of the block.
		 * @param	Width		The width of the block.
		 * @param	Height		The height of the block.
		 */
		"public function FlxTileblock",function FlxTileblock$(X/*:int*/,Y/*:int*/,Width/*:uint*/,Height/*:uint*/)
		{
			this.super$5(X,Y);
			this.createGraphic(Width,Height,0,true);		
			this.fixed = true;
		},
		
		/**
		 * Fills the block with a randomly arranged selection of graphics from the image provided.
		 * 
		 * @param	TileGraphic The graphic class that contains the tiles that should fill this block.
		 * @param	Empties		The number of "empty" tiles to add to the auto-fill algorithm (e.g. 8 tiles + 4 empties = 1/3 of block will be open holes).
		 */
		"public function loadTiles",function loadTiles(TileGraphic/*:Class*/,TileWidth/*:uint=0*/,TileHeight/*:uint=0*/,Empties/*:uint=0*/)/*:FlxTileblock*/
		{switch(arguments.length){case 0:case 1:TileWidth=0;case 2:TileHeight=0;case 3:Empties=0;}
			if(TileGraphic == null)
				return this;
			
			//First create a tile brush
			var s/*:FlxSprite*/ = new org.flixel.FlxSprite().loadGraphic(TileGraphic,true,false,TileWidth,TileHeight);
			var sw/*:uint*/ = $$uint(s.width);
			var sh/*:uint*/ = $$uint(s.height);
			var total/*:uint*/ = s.frames + Empties;
			
			//Then prep the "canvas" as it were (just doublechecking that the size is on tile boundaries)
			var regen/*:Boolean*/ = false;
			if(this.width % s.width != 0)
			{
				this.width = $$uint(this.width/sw+1)*sw;
				regen = true;
			}
			if(this.height % s.height != 0)
			{
				this.height = $$uint(this.height/sh+1)*sh;
				regen = true;
			}
			if(regen)
				this.createGraphic(this.width,this.height,0,true);
			else
				this.fill(0);
			
			//Stamp random tiles onto the canvas
			var r/*:uint*/ = 0;
			var c/*:uint*/;
			var ox/*:uint*/;
			var oy/*:uint*/ = 0;
			var widthInTiles/*:uint*/ = $$uint(this.width/sw);
			var heightInTiles/*:uint*/ = $$uint(this.height/sh);
			while(r < heightInTiles)
			{
				ox = 0;
				c = 0;
				while(c < widthInTiles)
				{
					if(org.flixel.FlxU.random()*total > Empties)
					{
						s.randomFrame();
						this.draw(s,ox,oy);
					}
					ox += sw;
					c++;
				}
				oy += sh;
				r++;
			}
			
			return this;
		},
		
		/**
		 * NOTE: MOST OF THE TIME YOU SHOULD BE USING LOADTILES(), NOT LOADGRAPHIC()!
		 * <code>LoadTiles()</code> has a lot more functionality, can load non-square tiles, etc.
		 * Load an image from an embedded graphic file and use it to auto-fill this block with tiles.
		 * 
		 * @param	Graphic		The image you want to use.
		 * @param	Animated	Ignored.
		 * @param	Reverse		Ignored.
		 * @param	Width		Ignored.
		 * @param	Height		Ignored.
		 * @param	Unique		Ignored.
		 * 
		 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
		 */
		"override public function loadGraphic",function loadGraphic(Graphic/*:Class*/,Animated/*:Boolean=false*/,Reverse/*:Boolean=false*/,Width/*:uint=0*/,Height/*:uint=0*/,Unique/*:Boolean=false*/)/*:FlxSprite*/
		{switch(arguments.length){case 0:case 1:Animated=false;case 2:Reverse=false;case 3:Width=0;case 4:Height=0;case 5:Unique=false;}
			this.loadTiles(Graphic);
			return this;
		},
	];},[],["org.flixel.FlxSprite","uint","org.flixel.FlxU"], "0.8.0", "0.8.2-SNAPSHOT"
);