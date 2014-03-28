joo.classLoader.prepare("package org.flixel",/*
{
	import flash.display.Bitmap
	import flash.display.BitmapData
	import flash.geom.Matrix
	import flash.geom.Rectangle*/
	
	/**
	 * This is a traditional tilemap display and collision class.
	 * It takes a string of comma-separated numbers and then associates
	 * those values with tiles from the sheet you pass in.
	 * It also includes some handy static parsers that can convert
	 * arrays or PNG files into strings that can be successfully loaded.
	 */
	"public class FlxTilemap extends org.flixel.FlxObject",4,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"org/flixel/data/autotiles.png"}}, "static public var",{ ImgAuto/*:Class*/:null},
		{Embed:{source:"org/flixel/data/autotiles_alt.png"}}, "static public var",{ ImgAutoAlt/*:Class*/:null},
		
		/**
		 * No auto-tiling.
		 */
		"static public const",{ OFF/*:uint*/ : 0},
		/**
		 * Platformer-friendly auto-tiling.
		 */
		"static public const",{ AUTO/*:uint*/ : 1},
		/**
		 * Top-down auto-tiling.
		 */
		"static public const",{ ALT/*:uint*/ : 2},
		
		/**
		 * What tile index will you start colliding with (default: 1).
		 */
		"public var",{ collideIndex/*:uint*/:0},
		/**
		 * The first index of your tile sheet (default: 0) If you want to change it, do so before calling loadMap().
		 */
		"public var",{ startingIndex/*:uint*/:0},
		/**
		 * What tile index will you start drawing with (default: 1)  NOTE: should always be >= startingIndex.
		 * If you want to change it, do so before calling loadMap().
		 */
		"public var",{ drawIndex/*:uint*/:0},
		/**
		 * Set this flag to use one of the 16-tile binary auto-tile algorithms (OFF, AUTO, or ALT).
		 */
		"public var",{ auto/*:uint*/:0},
		/**
		 * Set this flag to true to force the tilemap buffer to refresh on the next render frame.
		 */
		"public var",{ refresh/*:Boolean*/:false},
		
		/**
		 * Read-only variable, do NOT recommend changing after the map is loaded!
		 */
		"public var",{ widthInTiles/*:uint*/:0},
		/**
		 * Read-only variable, do NOT recommend changing after the map is loaded!
		 */
		"public var",{ heightInTiles/*:uint*/:0},
		/**
		 * Read-only variable, do NOT recommend changing after the map is loaded!
		 */
		"public var",{ totalTiles/*:uint*/:0},
		/**
		 * Rendering helper.
		 */
		"protected var",{ _flashRect/*:Rectangle*/:null},
		"protected var",{ _flashRect2/*:Rectangle*/:null},
		
		"protected var",{ _pixels/*:BitmapData*/:null},
		"protected var",{ _bbPixels/*:BitmapData*/:null},
		"protected var",{ _buffer/*:BitmapData*/:null},
		"protected var",{ _bufferLoc/*:FlxPoint*/:null},
		"protected var",{ _bbKey/*:String*/:null},
		"protected var",{ _data/*:Array*/:null},
		"protected var",{ _rects/*:Array*/:null},
		"protected var",{ _tileWidth/*:uint*/:0},
		"protected var",{ _tileHeight/*:uint*/:0},
		"protected var",{ _block/*:FlxObject*/:null},
		"protected var",{ _callbacks/*:Array*/:null},
		"protected var",{ _screenRows/*:uint*/:0},
		"protected var",{ _screenCols/*:uint*/:0},
		"protected var",{ _boundsVisible/*:Boolean*/:false},
		
		/**
		 * The tilemap constructor just initializes some basic variables.
		 */
		"public function FlxTilemap",function FlxTilemap$()
		{
			this.super$4();
			this.auto = org.flixel.FlxTilemap.OFF;
			this.collideIndex = 1;
			this.startingIndex = 0;
			this.drawIndex = 1;
			this.widthInTiles = 0;
			this.heightInTiles = 0;
			this.totalTiles = 0;
			this._buffer = null;
			this._bufferLoc = new org.flixel.FlxPoint();
			this._flashRect2 = new flash.geom.Rectangle();
			this._flashRect = this._flashRect2;
			this._data = null;
			this._tileWidth = 0;
			this._tileHeight = 0;
			this._rects = null;
			this._pixels = null;
			this._block = new org.flixel.FlxObject();
			this._block.width = this._block.height = 0;
			this._block.fixed = true;
			this._callbacks = new Array();
			this.fixed = true;
		},
		
		/**
		 * Load the tilemap with string data and a tile graphic.
		 * 
		 * @param	MapData			A string of comma and line-return delineated indices indicating what order the tiles should go in.
		 * @param	TileGraphic		All the tiles you want to use, arranged in a strip corresponding to the numbers in MapData.
		 * @param	TileWidth		The width of your tiles (e.g. 8) - defaults to height of the tile graphic if unspecified.
		 * @param	TileHeight		The height of your tiles (e.g. 8) - defaults to width if unspecified.
		 * 
		 * @return	A pointer this instance of FlxTilemap, for chaining as usual :)
		 */
		"public function loadMap",function loadMap(MapData/*:String*/, TileGraphic/*:Class*/, TileWidth/*:uint=0*/, TileHeight/*:uint=0*/)/*:FlxTilemap*/
		{if(arguments.length<4){if(arguments.length<3){TileWidth=0;}TileHeight=0;}
			this.refresh = true;
			
			//Figure out the map dimensions based on the data string
			var cols/*:Array*/;
			var rows/*:Array*/ = MapData.split("\n");
			this.heightInTiles = rows.length;
			this._data = new Array();
			var r/*:uint*/ = 0;
			var c/*:uint*/;
			while(r < this.heightInTiles)
			{
				cols = rows[r++].split(",");
				if(cols.length <= 1)
				{
					this.heightInTiles = this.heightInTiles - 1;
					continue;
				}
				if(this.widthInTiles == 0)
					this.widthInTiles = cols.length;
				c = 0;
				while(c < this.widthInTiles)
					this._data.push($$uint(cols[c++]));
			}
			
			//Pre-process the map data if it's auto-tiled
			var i/*:uint*/;
			this.totalTiles = this.widthInTiles*this.heightInTiles;
			if(this.auto > org.flixel.FlxTilemap.OFF)
			{
				this.collideIndex = this.startingIndex = this.drawIndex = 1;
				i = 0;
				while(i < this.totalTiles)
					this.autoTile(i++);
			}
			
			//Figure out the size of the tiles
			this._pixels = org.flixel.FlxG.addBitmap(TileGraphic);
			this._tileWidth = TileWidth;
			if(this._tileWidth == 0)
				this._tileWidth = this._pixels.height;
			this._tileHeight = TileHeight;
			if(this._tileHeight == 0)
				this._tileHeight = this._tileWidth;
			this._block.width = this._tileWidth;
			this._block.height = this._tileHeight;
			
			//Then go through and create the actual map
			this.width = this.widthInTiles*this._tileWidth;
			this.height = this.heightInTiles*this._tileHeight;
			this._rects = new Array(this.totalTiles);
			i = 0;
			while(i < this.totalTiles)
				this.updateTile(i++);
			
			//Also need to allocate a buffer to hold the rendered tiles
			var bw/*:uint*/ = (org.flixel.FlxU.ceil(org.flixel.FlxG.width / this._tileWidth) + 1)*this._tileWidth;
			var bh/*:uint*/ = (org.flixel.FlxU.ceil(org.flixel.FlxG.height / this._tileHeight) + 1)*this._tileHeight;
			this._buffer = new flash.display.BitmapData(bw,bh,true,0);
			
			//Pre-set some helper variables for later
			this._screenRows = Math.ceil(org.flixel.FlxG.height/this._tileHeight)+1;
			if(this._screenRows > this.heightInTiles)
				this._screenRows = this.heightInTiles;
			this._screenCols = Math.ceil(org.flixel.FlxG.width/this._tileWidth)+1;
			if(this._screenCols > this.widthInTiles)
				this._screenCols = this.widthInTiles;
			
			this._bbKey = String(TileGraphic);
			this.generateBoundingTiles();
			this.refreshHulls();
			
			this._flashRect.x = 0;
			this._flashRect.y = 0;
			this._flashRect.width = this._buffer.width;
			this._flashRect.height = this._buffer.height;
			
			return this;
		},
		
		/**
		 * Generates a bounding box version of the tiles, flixel should call this automatically when necessary.
		 */
		"protected function generateBoundingTiles",function generateBoundingTiles()/*:void*/
		{
			this.refresh = true;
			
			if((this._bbKey == null) || (this._bbKey.length <= 0))
				return;
			
			//Check for an existing version of this bounding boxes tilemap
			var bbc/*:uint*/ = this.getBoundingColor();
			var key/*:String*/ = this._bbKey + ":BBTILES" + bbc;
			var skipGen/*:Boolean*/ = org.flixel.FlxG.checkBitmapCache(key);
			this._bbPixels = org.flixel.FlxG.createBitmap(this._pixels.width, this._pixels.height, 0, true, key);
			if(!skipGen)
			{
				//Generate a bounding boxes tilemap for this color
				this._flashRect.width = this._pixels.width;
				this._flashRect.height = this._pixels.height;
				this._flashPoint.x = 0;
				this._flashPoint.y = 0;
				
				this._bbPixels.copyPixels(this._pixels,this._flashRect,this._flashPoint);
				this._flashRect.width = this._tileWidth;
				this._flashRect.height = this._tileHeight;
				
				//Check for an existing non-collide bounding box stamp
				var ov/*:Boolean*/ = this._solid;
				this._solid = false;
				bbc = this.getBoundingColor();
				key = "BBTILESTAMP"+this._tileWidth+"X"+this._tileHeight+bbc;
				skipGen = org.flixel.FlxG.checkBitmapCache(key);
				var stamp1/*:BitmapData*/ = org.flixel.FlxG.createBitmap(this._tileWidth, this._tileHeight, 0, true, key);
				if(!skipGen)
				{
					//Generate a bounding boxes stamp for this color
					stamp1.fillRect(this._flashRect,bbc);
					this._flashRect.x = this._flashRect.y = 1;
					this._flashRect.width = this._flashRect.width - 2;
					this._flashRect.height = this._flashRect.height - 2;
					stamp1.fillRect(this._flashRect,0);
					this._flashRect.x = this._flashRect.y = 0;
					this._flashRect.width = this._tileWidth;
					this._flashRect.height = this._tileHeight;
				}
				this._solid = ov;
				
				//Check for an existing collide bounding box
				bbc = this.getBoundingColor();
				key = "BBTILESTAMP"+this._tileWidth+"X"+this._tileHeight+bbc;
				skipGen = org.flixel.FlxG.checkBitmapCache(key);
				var stamp2/*:BitmapData*/ = org.flixel.FlxG.createBitmap(this._tileWidth, this._tileHeight, 0, true, key);
				if(!skipGen)
				{
					//Generate a bounding boxes stamp for this color
					stamp2.fillRect(this._flashRect,bbc);
					this._flashRect.x = this._flashRect.y = 1;
					this._flashRect.width = this._flashRect.width - 2;
					this._flashRect.height = this._flashRect.height - 2;
					stamp2.fillRect(this._flashRect,0);
					this._flashRect.x = this._flashRect.y = 0;
					this._flashRect.width = this._tileWidth;
					this._flashRect.height = this._tileHeight;
				}
				
				//Stamp the new tile bitmap with the bounding box border
				var r/*:uint*/ = 0;
				var c/*:uint*/;
				var i/*:uint*/ = 0;
				while(r < this._bbPixels.height)
				{
					c = 0;
					while(c < this._bbPixels.width)
					{
						this._flashPoint.x = c;
						this._flashPoint.y = r;
						if(i++ < this.collideIndex)
							this._bbPixels.copyPixels(stamp1,this._flashRect,this._flashPoint,null,null,true);
						else
							this._bbPixels.copyPixels(stamp2,this._flashRect,this._flashPoint,null,null,true);
						c += this._tileWidth;
					}
					r += this._tileHeight;
				}
				
				this._flashRect.x = 0;
				this._flashRect.y = 0;
				this._flashRect.width = this._buffer.width;
				this._flashRect.height = this._buffer.height;
			}
		},
		
		/**
		 * Internal function that actually renders the tilemap to the tilemap buffer.  Called by render().
		 */
		"protected function renderTilemap",function renderTilemap()/*:void*/
		{
			this._buffer.fillRect(this._flashRect,0);
			
			//Bounding box display options
			var tileBitmap/*:BitmapData*/;
			if(org.flixel.FlxG.showBounds)
			{
				tileBitmap = this._bbPixels;
				this._boundsVisible = true;
			}
			else
			{
				tileBitmap = this._pixels;
				this._boundsVisible = false;
			}
			
			//Copy tile images into the tile buffer
			this.getScreenXY(this._point);
			this._flashPoint.x = this._point.x;
			this._flashPoint.y = this._point.y;
			var tx/*:int*/ = Math.floor(-this._flashPoint.x/this._tileWidth);
			var ty/*:int*/ = Math.floor(-this._flashPoint.y/this._tileHeight);
			if(tx < 0) tx = 0;
			if(tx > this.widthInTiles-this._screenCols) tx = this.widthInTiles-this._screenCols;
			if(ty < 0) ty = 0;
			if(ty > this.heightInTiles-this._screenRows) ty = this.heightInTiles-this._screenRows;
			var ri/*:int*/ = ty*this.widthInTiles+tx;
			this._flashPoint.y = 0;
			var r/*:uint*/ = 0;
			var c/*:uint*/;
			var cri/*:uint*/;
			while(r < this._screenRows)
			{
				cri = ri;
				c = 0;
				this._flashPoint.x = 0;
				while(c < this._screenCols)
				{
					this._flashRect =as( this._rects[cri++],  flash.geom.Rectangle);
					if(this._flashRect != null)
						this._buffer.copyPixels(tileBitmap,this._flashRect,this._flashPoint,null,null,true);
					this._flashPoint.x += this._tileWidth;
					c++;
				}
				ri += this.widthInTiles;
				this._flashPoint.y += this._tileHeight;
				r++;
			}
			this._flashRect = this._flashRect2;
			this._bufferLoc.x = tx*this._tileWidth;
			this._bufferLoc.y = ty*this._tileHeight;
		},
		
		/**
		 * Checks to see if the tilemap needs to be refreshed or not.
		 */
		"override public function update",function update()/*:void*/
		{
			this.update$4();
			this.getScreenXY(this._point);
			this._point.x += this._bufferLoc.x;
			this._point.y += this._bufferLoc.y;
			if((this._point.x > 0) || (this._point.y > 0) || (this._point.x + this._buffer.width < org.flixel.FlxG.width) || (this._point.y + this._buffer.height < org.flixel.FlxG.height))
				this.refresh = true;
		},
		
		/**
		 * Draws the tilemap.
		 */
		"override public function render",function render()/*:void*/
		{
			if(org.flixel.FlxG.showBounds != this._boundsVisible)
				this.refresh = true;
			
			//Redraw the tilemap buffer if necessary
			if(this.refresh)
			{
				this.renderTilemap();
				this.refresh = false;
			}
			
			//Render the buffer no matter what
			this.getScreenXY(this._point);
			this._flashPoint.x = this._point.x + this._bufferLoc.x;
			this._flashPoint.y = this._point.y + this._bufferLoc.y;
			org.flixel.FlxG.buffer.copyPixels(this._buffer,this._flashRect,this._flashPoint,null,null,true);
		},
		
		/**
		 * @private
		 */
		"override public function set solid",function solid$set(Solid/*:Boolean*/)/*:void*/
		{
			var os/*:Boolean*/ = this._solid;
			this._solid = Solid;
			if(os != this._solid)
				this.generateBoundingTiles();
		},
		
		/**
		 * @private
		 */
		"override public function set fixed",function fixed$set(Fixed/*:Boolean*/)/*:void*/
		{
			var of/*:Boolean*/ = this._fixed;
			this._fixed = Fixed;
			if(of != this._fixed)
				this.generateBoundingTiles();
		},
		
		/**
		 * Checks for overlaps between the provided object and any tiles above the collision index.
		 * 
		 * @param	Core		The <code>FlxCore</code> you want to check against.
		 */
		"override public function overlaps",function overlaps(Core/*:FlxObject*/)/*:Boolean*/
		{
			var d/*:uint*/;
			
			var dd/*:uint*/;
			var blocks/*:Array*/ = new Array();
			
			//First make a list of all the blocks we'll use for collision
			var ix/*:uint*/ = Math.floor((Core.x - this.x)/this._tileWidth);
			var iy/*:uint*/ = Math.floor((Core.y - this.y)/this._tileHeight);
			var iw/*:uint*/ = Math.ceil(Core.width/this._tileWidth)+1;
			var ih/*:uint*/ = Math.ceil(Core.height/this._tileHeight)+1;
			var r/*:uint*/ = 0;
			var c/*:uint*/;
			while(r < ih)
			{
				if(r >= this.heightInTiles) break;
				d = (iy+r)*this.widthInTiles+ix;
				c = 0;
				while(c < iw)
				{
					if(c >= this.widthInTiles) break;
					dd = this._data[d+c];
					if(dd >= this.collideIndex)
						blocks.push({x:this.x+(ix+c)*this._tileWidth,y:this.y+(iy+r)*this._tileHeight,data:dd});
					c++;
				}
				r++;
			}
			
			//Then check for overlaps
			var bl/*:uint*/ = blocks.length;
			var hx/*:Boolean*/ = false;
			var i/*:uint*/ = 0;
			while(i < bl)
			{
				this._block.x = blocks[i].x;
				this._block.y = blocks[i++].y;
				if(this._block.overlaps(Core))
					return true;
			}
			return false;
		},
		
		/**
		 * Checks to see if a point in 2D space overlaps a solid tile.
		 * 
		 * @param	X			The X coordinate of the point.
		 * @param	Y			The Y coordinate of the point.
		 * @param	PerPixel	Not available in <code>FlxTilemap</code>, ignored.
		 * 
		 * @return	Whether or not the point overlaps this object.
		 */
		"override public function overlapsPoint",function overlapsPoint(X/*:Number*/,Y/*:Number*/,PerPixel/*:Boolean = false*/)/*:Boolean*/
		{if(arguments.length<3){PerPixel = false;}
			return this.getTile($$uint((X-this.x)/this._tileWidth),$$uint((Y-this.y)/this._tileHeight)) >= this.collideIndex;
		},
		
		/**
		 * Called by <code>FlxObject.updateMotion()</code> and some constructors to
		 * rebuild the basic collision data for this object.
		 */
		"override public function refreshHulls",function refreshHulls()/*:void*/
		{
			this.colHullX.x = 0;
			this.colHullX.y = 0;
			this.colHullX.width = this._tileWidth;
			this.colHullX.height = this._tileHeight;
			this.colHullY.x = 0;
			this.colHullY.y = 0;
			this.colHullY.width = this._tileWidth;
			this.colHullY.height = this._tileHeight;
		},
		
		/**
		 * <code>FlxU.collide()</code> (and thus <code>FlxObject.collide()</code>) call
		 * this function each time two objects are compared to see if they collide.
		 * It doesn't necessarily mean these objects WILL collide, however.
		 * 
		 * @param	Object	The <code>FlxObject</code> you're about to run into.
		 */
		"override public function preCollide",function preCollide(Object/*:FlxObject*/)/*:void*/
		{
			//Collision fix, in case updateMotion() is called
			this.colHullX.x = 0;
			this.colHullX.y = 0;
			this.colHullY.x = 0;
			this.colHullY.y = 0;
			
			var r/*:uint*/;
			var c/*:uint*/;
			var rs/*:uint*/;
			var col/*:uint*/ = 0;
			var ix/*:int*/ = org.flixel.FlxU.floor((Object.x - this.x)/this._tileWidth);
			var iy/*:int*/ = org.flixel.FlxU.floor((Object.y - this.y)/this._tileHeight);
			var iw/*:uint*/ = ix + org.flixel.FlxU.ceil(Object.width/this._tileWidth)+1;
			var ih/*:uint*/ = iy + org.flixel.FlxU.ceil(Object.height/this._tileHeight)+1;
			if(ix < 0)
				ix = 0;
			if(iy < 0)
				iy = 0;
			if(iw > this.widthInTiles)
				iw = this.widthInTiles;
			if(ih > this.heightInTiles)
				ih = this.heightInTiles;
			rs = iy*this.widthInTiles;
			r = iy;
			while(r < ih)
			{
				c = ix;
				while(c < iw)
				{
					if((this._data[rs+c]) >= this.collideIndex)
						this.colOffsets[col++] = new org.flixel.FlxPoint(this.x+c*this._tileWidth, this.y+r*this._tileHeight);
					c++;
				}
				rs += this.widthInTiles;
				r++;
			}
			if(this.colOffsets.length != col)
				this.colOffsets.length = col;
		},
		
		/**
		 * Check the value of a particular tile.
		 * 
		 * @param	X		The X coordinate of the tile (in tiles, not pixels).
		 * @param	Y		The Y coordinate of the tile (in tiles, not pixels).
		 * 
		 * @return	A uint containing the value of the tile at this spot in the array.
		 */
		"public function getTile",function getTile(X/*:uint*/,Y/*:uint*/)/*:uint*/
		{
			return this.getTileByIndex(Y * this.widthInTiles + X);
		},
		
		/**
		 * Get the value of a tile in the tilemap by index.
		 * 
		 * @param	Index	The slot in the data array (Y * widthInTiles + X) where this tile is stored.
		 * 
		 * @return	A uint containing the value of the tile at this spot in the array.
		 */
		"public function getTileByIndex",function getTileByIndex(Index/*:uint*/)/*:uint*/
		{
			return this._data[Index];
		},
		
		/**
		 * Change the data and graphic of a tile in the tilemap.
		 * 
		 * @param	X				The X coordinate of the tile (in tiles, not pixels).
		 * @param	Y				The Y coordinate of the tile (in tiles, not pixels).
		 * @param	Tile			The new integer data you wish to inject.
		 * @param	UpdateGraphics	Whether the graphical representation of this tile should change.
		 * 
		 * @return	Whether or not the tile was actually changed.
		 */ 
		"public function setTile",function setTile(X/*:uint*/,Y/*:uint*/,Tile/*:uint*/,UpdateGraphics/*:Boolean=true*/)/*:Boolean*/
		{if(arguments.length<4){UpdateGraphics=true;}
			if((X >= this.widthInTiles) || (Y >= this.heightInTiles))
				return false;
			return this.setTileByIndex(Y * this.widthInTiles + X,Tile,UpdateGraphics);
		},
		
		/**
		 * Change the data and graphic of a tile in the tilemap.
		 * 
		 * @param	Index			The slot in the data array (Y * widthInTiles + X) where this tile is stored.
		 * @param	Tile			The new integer data you wish to inject.
		 * @param	UpdateGraphics	Whether the graphical representation of this tile should change.
		 * 
		 * @return	Whether or not the tile was actually changed.
		 */
		"public function setTileByIndex",function setTileByIndex(Index/*:uint*/,Tile/*:uint*/,UpdateGraphics/*:Boolean=true*/)/*:Boolean*/
		{if(arguments.length<3){UpdateGraphics=true;}
			if(Index >= this._data.length)
				return false;
			
			var ok/*:Boolean*/ = true;
			this._data[Index] = Tile;
			
			if(!UpdateGraphics)
				return ok;
			
			this.refresh = true;
			
			if(this.auto == org.flixel.FlxTilemap.OFF)
			{
				this.updateTile(Index);
				return ok;
			}
			
			//If this map is autotiled and it changes, locally update the arrangement
			var i/*:uint*/;
			var r/*:int*/ = $$int(Index/this.widthInTiles) - 1;
			var rl/*:int*/ = r + 3;
			var c/*:int*/ = Index%this.widthInTiles - 1;
			var cl/*:int*/ = c + 3;
			while(r < rl)
			{
				c = cl - 3;
				while(c < cl)
				{
					if((r >= 0) && (r < this.heightInTiles) && (c >= 0) && (c < this.widthInTiles))
					{
						i = r*this.widthInTiles+c;
						this.autoTile(i);
						this.updateTile(i);
					}
					c++;
				}
				r++;
			}
			
			return ok;
		},
		
		/**
		 * Bind a function Callback(Core:FlxCore,X:uint,Y:uint,Tile:uint) to a range of tiles.
		 * 
		 * @param	Tile		The tile to trigger the callback.
		 * @param	Callback	The function to trigger.  Parameters should be <code>(Core:FlxCore,X:uint,Y:uint,Tile:uint)</code>.
		 * @param	Range		If you want this callback to work for a bunch of different tiles, input the range here.  Default value is 1.
		 */
		"public function setCallback",function setCallback(Tile/*:uint*/,Callback/*:Function*/,Range/*:uint=1*/)/*:void*/
		{if(arguments.length<3){Range=1;}
			org.flixel.FlxG.log("WARNING: FlxTilemap.setCallback()\nhas been temporarily deprecated.");
			//if(Range <= 0) return;
			//for(var i:uint = Tile; i < Tile+Range; i++)
			//	_callbacks[i] = Callback;
		},
		
		/**
		 * Call this function to lock the automatic camera to the map's edges.
		 * 
		 * @param	Border		Adjusts the camera follow boundary by whatever number of tiles you specify here.  Handy for blocking off deadends that are offscreen, etc.  Use a negative number to add padding instead of hiding the edges.
		 */
		"public function follow",function follow(Border/*:int=0*/)/*:void*/
		{if(arguments.length<1){Border=0;}
			org.flixel.FlxG.followBounds(this.x+Border*this._tileWidth,this.y+Border*this._tileHeight,this.width-Border*this._tileWidth,this.height-Border*this._tileHeight);
		},
		
		/**
		 * Shoots a ray from the start point to the end point.
		 * If/when it passes through a tile, it stores and returns that point.
		 * 
		 * @param	StartX		The X component of the ray's start.
		 * @param	StartY		The Y component of the ray's start.
		 * @param	EndX		The X component of the ray's end.
		 * @param	EndY		The Y component of the ray's end.
		 * @param	Result		A <code>Point</code> object containing the first wall impact.
		 * @param	Resolution	Defaults to 1, meaning check every tile or so.  Higher means more checks!
		 * @return	Whether or not there was a collision between the ray and a colliding tile.
		 */
		"public function ray",function ray(StartX/*:Number*/, StartY/*:Number*/, EndX/*:Number*/, EndY/*:Number*/, Result/*:FlxPoint*/, Resolution/*:Number=1*/)/*:Boolean*/
		{if(arguments.length<6){Resolution=1;}
			var step/*:Number*/ = this._tileWidth;
			if(this._tileHeight < this._tileWidth)
				step = this._tileHeight;
			step /= Resolution;
			var dx/*:Number*/ = EndX - StartX;
			var dy/*:Number*/ = EndY - StartY;
			var distance/*:Number*/ = Math.sqrt(dx*dx + dy*dy);
			var steps/*:uint*/ = Math.ceil(distance/step);
			var stepX/*:Number*/ = dx/steps;
			var stepY/*:Number*/ = dy/steps;
			var curX/*:Number*/ = StartX - stepX;
			var curY/*:Number*/ = StartY - stepY;
			var tx/*:uint*/;
			var ty/*:uint*/;
			var i/*:uint*/ = 0;
			while(i < steps)
			{
				curX += stepX;
				curY += stepY;
				
				if((curX < 0) || (curX > this.width) || (curY < 0) || (curY > this.height))
				{
					i++;
					continue;
				}
				
				tx = curX/this._tileWidth;
				ty = curY/this._tileHeight;
				if((this._data[ty*this.widthInTiles+tx]) >= this.collideIndex)
				{
					//Some basic helper stuff
					tx *= this._tileWidth;
					ty *= this._tileHeight;
					var rx/*:Number*/ = 0;
					var ry/*:Number*/ = 0;
					var q/*:Number*/;
					var lx/*:Number*/ = curX-stepX;
					var ly/*:Number*/ = curY-stepY;
					
					//Figure out if it crosses the X boundary
					q = tx;
					if(dx < 0)
						q += this._tileWidth;
					rx = q;
					ry = ly + stepY*((q-lx)/stepX);
					if((ry > ty) && (ry < ty + this._tileHeight))
					{
						if(Result == null)
							Result = new org.flixel.FlxPoint();
						Result.x = rx;
						Result.y = ry;
						return true;
					}
					
					//Else, figure out if it crosses the Y boundary
					q = ty;
					if(dy < 0)
						q += this._tileHeight;
					rx = lx + stepX*((q-ly)/stepY);
					ry = q;
					if((rx > tx) && (rx < tx + this._tileWidth))
					{
						if(Result == null)
							Result = new org.flixel.FlxPoint();
						Result.x = rx;
						Result.y = ry;
						return true;
					}
					return false;
				}
				i++;
			}
			return false;
		},
		
		/**
		 * Converts a one-dimensional array of tile data to a comma-separated string.
		 * 
		 * @param	Data		An array full of integer tile references.
		 * @param	Width		The number of tiles in each row.
		 * 
		 * @return	A comma-separated string containing the level data in a <code>FlxTilemap</code>-friendly format.
		 */
		"static public function arrayToCSV",function arrayToCSV(Data/*:Array*/,Width/*:int*/)/*:String*/
		{
			var r/*:uint*/ = 0;
			var c/*:uint*/;
			var csv/*:String*/;
			var Height/*:int*/ = Data.length / Width;
			while(r < Height)
			{
				c = 0;
				while(c < Width)
				{
					if(c == 0)
					{
						if(r == 0)
							csv += Data[0];
						else
							csv += "\n"+Data[r*Width];
					}
					else
						csv += ", "+Data[r*Width+c];
					c++;
				}
				r++;
			}
			return csv;
		},
		
		/**
		 * Converts a <code>BitmapData</code> object to a comma-separated string.
		 * Black pixels are flagged as 'solid' by default,
		 * non-black pixels are set as non-colliding.
		 * Black pixels must be PURE BLACK.
		 * 
		 * @param	PNGFile		An embedded graphic, preferably black and white.
		 * @param	Invert		Load white pixels as solid instead.
		 * 
		 * @return	A comma-separated string containing the level data in a <code>FlxTilemap</code>-friendly format.
		 */
		"static public function bitmapToCSV",function bitmapToCSV(bitmapData/*:BitmapData*/,Invert/*:Boolean=false*/,Scale/*:uint=1*/)/*:String*/
		{if(arguments.length<3){if(arguments.length<2){Invert=false;}Scale=1;}
			//Import and scale image if necessary
			if(Scale > 1)
			{
				var bd/*:BitmapData*/ = bitmapData;
				bitmapData = new flash.display.BitmapData(bitmapData.width*Scale,bitmapData.height*Scale);
				var mtx/*:Matrix*/ = new flash.geom.Matrix();
				mtx.scale(Scale,Scale);
				bitmapData.draw(bd,mtx);
			}
			
			//Walk image and export pixel values
			var r/*:uint*/ = 0;
			var c/*:uint*/;
			var p/*:uint*/;
			var csv/*:String*/;
			var w/*:uint*/ = bitmapData.width;
			var h/*:uint*/ = bitmapData.height;
			while(r < h)
			{
				c = 0;
				while(c < w)
				{
					//Decide if this pixel/tile is solid (1) or not (0)
					p = bitmapData.getPixel(c,r);
					if((Invert && (p > 0)) || (!Invert && (p == 0)))
						p = 1;
					else
						p = 0;
					
					//Write the result to the string
					if(c == 0)
					{
						if(r == 0)
							csv += p;
						else
							csv += "\n"+p;
					}
					else
						csv += ", "+p;
					c++;
				}
				r++;
			}
			return csv;
		},
		
		/**
		 * Converts a resource image file to a comma-separated string.
		 * Black pixels are flagged as 'solid' by default,
		 * non-black pixels are set as non-colliding.
		 * Black pixels must be PURE BLACK.
		 * 
		 * @param	PNGFile		An embedded graphic, preferably black and white.
		 * @param	Invert		Load white pixels as solid instead.
		 * 
		 * @return	A comma-separated string containing the level data in a <code>FlxTilemap</code>-friendly format.
		 */
		"static public function imageToCSV",function imageToCSV(ImageFile/*:Class*/,Invert/*:Boolean=false*/,Scale/*:uint=1*/)/*:String*/
		{if(arguments.length<3){if(arguments.length<2){Invert=false;}Scale=1;}
			return org.flixel.FlxTilemap.bitmapToCSV((new ImageFile).bitmapData,Invert,Scale);
		},
		
		/**
		 * An internal function used by the binary auto-tilers.
		 * 
		 * @param	Index		The index of the tile you want to analyze.
		 */
		"protected function autoTile",function autoTile(Index/*:uint*/)/*:void*/
		{
			if(this._data[Index] == 0) return;
			this._data[Index] = 0;
			if((Index-this.widthInTiles < 0) || (this._data[Index-this.widthInTiles] > 0)) 		//UP
				this._data[Index] += 1;
			if((Index%this.widthInTiles >= this.widthInTiles-1) || (this._data[Index+1] > 0)) 		//RIGHT
				this._data[Index] += 2;
			if((Index+this.widthInTiles >= this.totalTiles) || (this._data[Index+this.widthInTiles] > 0)) //DOWN
				this._data[Index] += 4;
			if((Index%this.widthInTiles <= 0) || (this._data[Index-1] > 0)) 					//LEFT
				this._data[Index] += 8;
			if((this.auto == org.flixel.FlxTilemap.ALT) && (this._data[Index] == 15))	//The alternate algo checks for interior corners
			{
				if((Index%this.widthInTiles > 0) && (Index+this.widthInTiles < this.totalTiles) && (this._data[Index+this.widthInTiles-1] <= 0))
					this._data[Index] = 1;		//BOTTOM LEFT OPEN
				if((Index%this.widthInTiles > 0) && (Index-this.widthInTiles >= 0) && (this._data[Index-this.widthInTiles-1] <= 0))
					this._data[Index] = 2;		//TOP LEFT OPEN
				if((Index%this.widthInTiles < this.widthInTiles-1) && (Index-this.widthInTiles >= 0) && (this._data[Index-this.widthInTiles+1] <= 0))
					this._data[Index] = 4;		//TOP RIGHT OPEN
				if((Index%this.widthInTiles < this.widthInTiles-1) && (Index+this.widthInTiles < this.totalTiles) && (this._data[Index+this.widthInTiles+1] <= 0))
					this._data[Index] = 8; 		//BOTTOM RIGHT OPEN
			}
			this._data[Index] += 1;
		},
		
		/**
		 * Internal function used in setTileByIndex() and the constructor to update the map.
		 * 
		 * @param	Index		The index of the tile you want to update.
		 */
		"protected function updateTile",function updateTile(Index/*:uint*/)/*:void*/
		{
			if(this._data[Index] < this.drawIndex)
			{
				this._rects[Index] = null;
				return;
			}
			var rx/*:uint*/ = (this._data[Index]-this.startingIndex)*this._tileWidth;
			var ry/*:uint*/ = 0;
			if(rx >= this._pixels.width)
			{
				ry = $$uint(rx/this._pixels.width)*this._tileHeight;
				rx %= this._pixels.width;
			}
			this._rects[Index] = (new flash.geom.Rectangle(rx,ry,this._tileWidth,this._tileHeight));
		},
	];},["arrayToCSV","bitmapToCSV","imageToCSV"],["org.flixel.FlxObject","resource:org/flixel/data/autotiles.png","resource:org/flixel/data/autotiles_alt.png","org.flixel.FlxPoint","flash.geom.Rectangle","Array","uint","org.flixel.FlxG","org.flixel.FlxU","flash.display.BitmapData","Math","String","int","flash.geom.Matrix"], "0.8.0", "0.8.3"
);