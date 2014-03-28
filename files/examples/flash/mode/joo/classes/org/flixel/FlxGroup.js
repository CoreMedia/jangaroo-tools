joo.classLoader.prepare("package org.flixel",/*
{*/
	/**
	 * This is an organizational class that can update and render a bunch of <code>FlxObject</code>s.
	 * NOTE: Although <code>FlxGroup</code> extends <code>FlxObject</code>, it will not automatically
	 * add itself to the global collisions quad tree, it will only add its members.
	 */
	"public class FlxGroup extends org.flixel.FlxObject",4,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[
	
		"static public const",{ ASCENDING/*:int*/ : -1},
		"static public const",{ DESCENDING/*:int*/ : 1},
		
		/**
		 * Array of all the <code>FlxObject</code>s that exist in this layer.
		 */
		"public var",{ members/*:Array*/:null},
		/**
		 * Helpers for moving/updating group members.
		 */
		"protected var",{ _last/*:FlxPoint*/:null},
		"protected var",{ _first/*:Boolean*/:false},
		/**
		 * Helpers for sorting members.
		 */
		"protected var",{ _sortIndex/*:String*/:null},
		"protected var",{ _sortOrder/*:int*/:0},

		/**
		 * Constructor
		 */
		"public function FlxGroup",function FlxGroup$()
		{
			this.super$4();
			this._group = true;
			this.solid = false;
			this.members = new Array();
			this._last = new org.flixel.FlxPoint();
			this._first = true;
		},
		
		/**
		 * Adds a new <code>FlxObject</code> subclass (FlxSprite, FlxBlock, etc) to the list of children
		 *
		 * @param	Object			The object you want to add
		 * @param	ShareScroll		Whether or not this FlxObject should sync up with this layer's scrollFactor
		 *
		 * @return	The same <code>FlxObject</code> object that was passed in.
		 */
		"public function add",function add(Object/*:FlxObject*/,ShareScroll/*:Boolean=false*/)/*:FlxObject*/
		{switch(arguments.length){case 0:case 1:ShareScroll=false;}
			if (this.members.indexOf(Object) < 0)
				this.members[this.members.length] = Object;
			if(ShareScroll)
				Object.scrollFactor = this.scrollFactor;
			return Object;
		},
		
		/**
		 * Replaces an existing <code>FlxObject</code> with a new one.
		 * 
		 * @param	OldObject	The object you want to replace.
		 * @param	NewObject	The new object you want to use instead.
		 * 
		 * @return	The new object.
		 */
		"public function replace",function replace(OldObject/*:FlxObject*/,NewObject/*:FlxObject*/)/*:FlxObject*/
		{
			var index/*:int*/ = this.members.indexOf(OldObject);
			if((index < 0) || (index >= this.members.length))
				return null;
			this.members[index] = NewObject;
			return NewObject;
		},
		
		/**
		 * Removes an object from the group.
		 * 
		 * @param	Object	The <code>FlxObject</code> you want to remove.
		 * @param	Splice	Whether the object should be cut from the array entirely or not.
		 * 
		 * @return	The removed object.
		 */
		"public function remove",function remove(Object/*:FlxObject*/,Splice/*:Boolean=false*/)/*:FlxObject*/
		{switch(arguments.length){case 0:case 1:Splice=false;}
			var index/*:int*/ = this.members.indexOf(Object);
			if((index < 0) || (index >= this.members.length))
				return null;
			if(Splice)
				this.members.splice(index,1);
			else
				this.members[index] = null;
			return Object;
		},
		
		/**
		 * Call this function to sort the group according to a particular value and order.
		 * For example, to sort game objects for Zelda-style overlaps you might call
		 * <code>myGroup.sort("y",ASCENDING)</code> at the bottom of your
		 * <code>FlxState.update()</code> override.  To sort all existing objects after
		 * a big explosion or bomb attack, you might call <code>myGroup.sort("exists",DESCENDING)</code>.
		 * 
		 * @param	Index	The <code>String</code> name of the member variable you want to sort on.  Default value is "y".
		 * @param	Order	A <code>FlxGroup</code> constant that defines the sort order.  Possible values are <code>ASCENDING</code> and <code>DESCENDING</code>.  Default value is <code>ASCENDING</code>.  
		 */
		"public function sort",function sort(Index/*:String="y"*/,Order/*:int=org.flixel.FlxGroup.ASCENDING*/)/*:void*/
		{switch(arguments.length){case 0:Index="y";case 1:Order=org.flixel.FlxGroup.ASCENDING;}
			this._sortIndex = Index;
			this._sortOrder = Order;
			this.members.sort($$bound(this,"sortHandler"));
		},
		
		/**
		 * Call this function to retrieve the first object with exists == false in the group.
		 * This is handy for recycling in general, e.g. respawning enemies.
		 * 
		 * @return	A <code>FlxObject</code> currently flagged as not existing.
		 */
		"public function getFirstAvail",function getFirstAvail()/*:FlxObject*/
		{
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if((o != null) && !o.exists)
					return o;
			}
			return null;
		},
		
		/**
		 * Call this function to retrieve the first index set to 'null'.
		 * Returns -1 if no index stores a null object.
		 * 
		 * @return	An <code>int</code> indicating the first null slot in the group.
		 */
		"public function getFirstNull",function getFirstNull()/*:int*/
		{
			var i/*:uint*/ = 0;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				if(this.members[i] == null)
					return i;
				else
					i++;
			}
			return -1;
		},
		
		/**
		 * Finds the first object with exists == false and calls reset on it.
		 * 
		 * @param	X	The new X position of this object.
		 * @param	Y	The new Y position of this object.
		 * 
		 * @return	Whether a suitable <code>FlxObject</code> was found and reset.
		 */
		"public function resetFirstAvail",function resetFirstAvail(X/*:Number=0*/, Y/*:Number=0*/)/*:Boolean*/
		{switch(arguments.length){case 0:X=0;case 1:Y=0;}
			var o/*:FlxObject*/ = this.getFirstAvail();
			if(o == null)
				return false;
			o.reset(X,Y);
			return true;
		},
		
		/**
		 * Call this function to retrieve the first object with exists == true in the group.
		 * This is handy for checking if everything's wiped out, or choosing a squad leader, etc.
		 * 
		 * @return	A <code>FlxObject</code> currently flagged as existing.
		 */
		"public function getFirstExtant",function getFirstExtant()/*:FlxObject*/
		{
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if((o != null) && o.exists)
					return o;
			}
			return null;
		},
		
		/**
		 * Call this function to retrieve the first object with dead == false in the group.
		 * This is handy for checking if everything's wiped out, or choosing a squad leader, etc.
		 * 
		 * @return	A <code>FlxObject</code> currently flagged as not dead.
		 */
		"public function getFirstAlive",function getFirstAlive()/*:FlxObject*/
		{
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if((o != null) && o.exists && !o.dead)
					return o;
			}
			return null;
		},
		
		/**
		 * Call this function to retrieve the first object with dead == true in the group.
		 * This is handy for checking if everything's wiped out, or choosing a squad leader, etc.
		 * 
		 * @return	A <code>FlxObject</code> currently flagged as dead.
		 */
		"public function getFirstDead",function getFirstDead()/*:FlxObject*/
		{
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if((o != null) && o.dead)
					return o;
			}
			return null;
		},
		
		/**
		 * Call this function to find out how many members of the group are not dead.
		 * 
		 * @return	The number of <code>FlxObject</code>s flagged as not dead.  Returns -1 if group is empty.
		 */
		"public function countLiving",function countLiving()/*:int*/
		{
			var count/*:int*/ = -1;
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if(o != null)
				{
					if(count < 0)
						count = 0;
					if(o.exists && !o.dead)
						count++;
				}
			}
			return count;
		},
		
		/**
		 * Call this function to find out how many members of the group are dead.
		 * 
		 * @return	The number of <code>FlxObject</code>s flagged as dead.  Returns -1 if group is empty.
		 */
		"public function countDead",function countDead()/*:int*/
		{
			var count/*:int*/ = -1;
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if(o != null)
				{
					if(count < 0)
						count = 0;
					if(o.dead)
						count++;
				}
			}
			return count;
		},
		
		/**
		 * Returns a count of how many objects in this group are on-screen right now.
		 * 
		 * @return	The number of <code>FlxObject</code>s that are on screen.  Returns -1 if group is empty.
		 */
		"public function countOnScreen",function countOnScreen()/*:int*/
		{
			var count/*:int*/ = -1;
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if(o != null)
				{
					if(count < 0)
						count = 0;
					if(o.onScreen())
						count++;
				}
			}
			return count;
		},		
		
		/**
		 * Returns a member at random from the group.
		 * 
		 * @return	A <code>FlxObject</code> from the members list.
		 */
		"public function getRandom",function getRandom()/*:FlxObject*/
		{
			var c/*:uint*/ = 0;
			var o/*:FlxObject*/ = null;
			var l/*:uint*/ = this.members.length;
			var i/*:uint*/ = $$uint(org.flixel.FlxU.random()*l);
			while((o == null) && (c < this.members.length))
			{
				o =as( this.members[(++i)%l],  org.flixel.FlxObject);
				c++;
			}
			return o;
		},
		
		/**
		 * Internal function, helps with the moving/updating of group members.
		 */
		"protected function saveOldPosition",function saveOldPosition()/*:void*/
		{
			if(this._first)
			{
				this._first = false;
				this._last.x = 0;
				this._last.y = 0;
				return;
			}
			this._last.x = this.x;
			this._last.y = this.y;
		},
		
		/**
		 * Internal function that actually goes through and updates all the group members.
		 * Depends on <code>saveOldPosition()</code> to set up the correct values in <code>_last</code> in order to work properly.
		 */
		"protected function updateMembers",function updateMembers()/*:void*/
		{
			var mx/*:Number*/;
			var my/*:Number*/;
			var moved/*:Boolean*/ = false;
			if((this.x != this._last.x) || (this.y != this._last.y))
			{
				moved = true;
				mx = this.x - this._last.x;
				my = this.y - this._last.y;
			}
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if((o != null) && o.exists)
				{
					if(moved)
					{
						if(o._group)
							o.reset(o.x+mx,o.y+my);
						else
						{
							o.x += mx;
							o.y += my;
						}
					}
					if(o.active)
						o.update();
					if(moved && o.solid)
					{
						o.colHullX.width += ((mx>0)?mx:-mx);
						if(mx < 0)
							o.colHullX.x += mx;
						o.colHullY.x = this.x;
						o.colHullY.height += ((my>0)?my:-my);
						if(my < 0)
							o.colHullY.y += my;
						o.colVector.x += mx;
						o.colVector.y += my;
					}
				}
			}
		},
		
		/**
		 * Automatically goes through and calls update on everything you added,
		 * override this function to handle custom input and perform collisions.
		 */
		"override public function update",function update()/*:void*/
		{
			this.saveOldPosition();
			this.updateMotion();
			this.updateMembers();
			this.updateFlickering();
		},
		
		/**
		 * Internal function that actually loops through and renders all the group members.
		 */
		"protected function renderMembers",function renderMembers()/*:void*/
		{
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if((o != null) && o.exists && o.visible)
					o.render();
			}
		},
		
		/**
		 * Automatically goes through and calls render on everything you added,
		 * override this loop to control render order manually.
		 */
		"override public function render",function render()/*:void*/
		{
			this.renderMembers();
		},
		
		/**
		 * Internal function that calls kill on all members.
		 */
		"protected function killMembers",function killMembers()/*:void*/
		{
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if(o != null)
					o.kill();
			}
		},
		
		/**
		 * Calls kill on the group and all its members.
		 */
		"override public function kill",function kill()/*:void*/
		{
			this.killMembers();
			this.kill$4();
		},
		
		/**
		 * Internal function that actually loops through and destroys each member.
		 */
		"protected function destroyMembers",function destroyMembers()/*:void*/
		{
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if(o != null)
					o.destroy();
			}
			this.members.length = 0;
		},
		
		/**
		 * Override this function to handle any deleting or "shutdown" type operations you might need,
		 * such as removing traditional Flash children like Sprite objects.
		 */
		"override public function destroy",function destroy()/*:void*/
		{
			this.destroyMembers();
			this.destroy$4();
		},
		
		/**
		 * If the group's position is reset, we want to reset all its members too.
		 * 
		 * @param	X	The new X position of this object.
		 * @param	Y	The new Y position of this object.
		 */
		"override public function reset",function reset(X/*:Number*/,Y/*:Number*/)/*:void*/
		{
			this.saveOldPosition();
			this.reset$4(X,Y);
			var mx/*:Number*/;
			var my/*:Number*/;
			var moved/*:Boolean*/ = false;
			if((this.x != this._last.x) || (this.y != this._last.y))
			{
				moved = true;
				mx = this.x - this._last.x;
				my = this.y - this._last.y;
			}
			var i/*:uint*/ = 0;
			var o/*:FlxObject*/;
			var ml/*:uint*/ = this.members.length;
			while(i < ml)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if((o != null) && o.exists)
				{
					if(moved)
					{
						if(o._group)
							o.reset(o.x+mx,o.y+my);
						else
						{
							o.x += mx;
							o.y += my;
							if(this.solid)
							{
								o.colHullX.width += ((mx>0)?mx:-mx);
								if(mx < 0)
									o.colHullX.x += mx;
								o.colHullY.x = this.x;
								o.colHullY.height += ((my>0)?my:-my);
								if(my < 0)
									o.colHullY.y += my;
								o.colVector.x += mx;
								o.colVector.y += my;
							}
						}
					}
				}
			}
		},
		
		/**
		 * Helper function for the sort process.
		 * 
		 * @param 	Obj1	The first object being sorted.
		 * @param	Obj2	The second object being sorted.
		 * 
		 * @return	An integer value: -1 (Obj1 before Obj2), 0 (same), or 1 (Obj1 after Obj2).
		 */
		"protected function sortHandler",function sortHandler(Obj1/*:FlxObject*/,Obj2/*:FlxObject*/)/*:int*/
		{
			if(Obj1[this._sortIndex] < Obj2[this._sortIndex])
				return this._sortOrder;
			else if(Obj1[this._sortIndex] > Obj2[this._sortIndex])
				return -this._sortOrder;
			return 0;
		},
	];},[],["org.flixel.FlxObject","Array","org.flixel.FlxPoint","uint","org.flixel.FlxU"], "0.8.0", "0.8.2-SNAPSHOT"
);