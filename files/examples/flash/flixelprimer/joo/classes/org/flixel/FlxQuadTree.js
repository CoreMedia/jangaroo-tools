joo.classLoader.prepare("package org.flixel",/*
{
	import org.flixel.data.FlxList*/

	/**
	 * A fairly generic quad tree structure for rapid overlap checks.
	 * FlxQuadTree is also configured for single or dual list operation.
	 * You can add items either to its A list or its B list.
	 * When you do an overlap check, you can compare the A list to itself,
	 * or the A list against the B list.  Handy for different things!
	 */
	"public class FlxQuadTree extends org.flixel.FlxRect",3,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxU);},
	
		/**
		 * Set this to null to force it to refresh on the next collide.
		 */
		"static public var",{ quadTree/*:FlxQuadTree*/:null},
		/**
		 * This variable stores the dimensions of the root of the quad tree.
		 * This is the eligible game collision space.
		 */
		"static public var",{ bounds/*:FlxRect*/:null},
		/**
		 * Controls the granularity of the quad tree.  Default is 3 (decent performance on large and small worlds).
		 */
		"static public var",{ divisions/*:uint*/:0},
		/**
		 * Flag for specifying that you want to add an object to the A list.
		 */
		"static public const",{ A_LIST/*:uint*/ : 0},
		/**
		 * Flag for specifying that you want to add an object to the B list.
		 */
		"static public const",{ B_LIST/*:uint*/ : 1},
		
		/**
		 * Whether this branch of the tree can be subdivided or not.
		 */
		"protected var",{ _canSubdivide/*:Boolean*/:false},
		
		/**
		 * These variables refer to the internal A and B linked lists,
		 * which are used to store objects in the leaves.
		 */
		"protected var",{ _headA/*:FlxList*/:null},
		"protected var",{ _tailA/*:FlxList*/:null},
		"protected var",{ _headB/*:FlxList*/:null},
		"protected var",{ _tailB/*:FlxList*/:null},

		/**
		 * These variables refer to the potential child quadrants for this node.
		 */
		"static protected var",{ _min/*:uint*/:0},
		"protected var",{ _nw/*:FlxQuadTree*/:null},
		"protected var",{ _ne/*:FlxQuadTree*/:null},
		"protected var",{ _se/*:FlxQuadTree*/:null},
		"protected var",{ _sw/*:FlxQuadTree*/:null},		
		"protected var",{ _l/*:Number*/:NaN},
		"protected var",{ _r/*:Number*/:NaN},
		"protected var",{ _t/*:Number*/:NaN},
		"protected var",{ _b/*:Number*/:NaN},
		"protected var",{ _hw/*:Number*/:NaN},
		"protected var",{ _hh/*:Number*/:NaN},
		"protected var",{ _mx/*:Number*/:NaN},
		"protected var",{ _my/*:Number*/:NaN},
		
		/**
		 * These objects are used to reduce recursive parameters internally.
		 */
		"static protected var",{ _o/*:FlxObject*/:null},
		"static protected var",{ _ol/*:Number*/:NaN},
		"static protected var",{ _ot/*:Number*/:NaN},
		"static protected var",{ _or/*:Number*/:NaN},
		"static protected var",{ _ob/*:Number*/:NaN},
		"static protected var",{ _oa/*:uint*/:0},
		"static protected var",{ _oc/*:Function*/:null},
		
		/**
		 * Instantiate a new Quad Tree node.
		 * 
		 * @param	X			The X-coordinate of the point in space.
		 * @param	Y			The Y-coordinate of the point in space.
		 * @param	Width		Desired width of this node.
		 * @param	Height		Desired height of this node.
		 * @param	Parent		The parent branch or node.  Pass null to create a root.
		 */
		"public function FlxQuadTree",function FlxQuadTree$(X/*:Number*/, Y/*:Number*/, Width/*:Number*/, Height/*:Number*/, Parent/*:FlxQuadTree=null*/)
		{if(arguments.length<5){Parent=null;}
			this.super$3(X,Y,Width,Height);
			this._headA = this._tailA = new org.flixel.data.FlxList();
			this._headB = this._tailB = new org.flixel.data.FlxList();
			
			/*DEBUG: draw a randomly colored rectangle indicating this quadrant (may induce seizures)
			var brush:FlxSprite = new FlxSprite().createGraphic(Width,Height,0xffffffff*FlxU.random());
			FlxState.screen.draw(brush,X+FlxG.scroll.x,Y+FlxG.scroll.y);//*/
			
			//Copy the parent's children (if there are any)
			if(Parent != null)
			{
				var itr/*:FlxList*/;
				var ot/*:FlxList*/;
				if(Parent._headA.object != null)
				{
					itr = Parent._headA;
					while(itr != null)
					{
						if(this._tailA.object != null)
						{
							ot = this._tailA;
							this._tailA = new org.flixel.data.FlxList();
							ot.next = this._tailA;
						}
						this._tailA.object = itr.object;
						itr = itr.next;
					}
				}
				if(Parent._headB.object != null)
				{
					itr = Parent._headB;
					while(itr != null)
					{
						if(this._tailB.object != null)
						{
							ot = this._tailB;
							this._tailB = new org.flixel.data.FlxList();
							ot.next = this._tailB;
						}
						this._tailB.object = itr.object;
						itr = itr.next;
					}
				}
			}
			else
				org.flixel.FlxQuadTree._min = $$uint((this.width + this.height)/(2*org.flixel.FlxQuadTree.divisions));
			this._canSubdivide = (this.width > org.flixel.FlxQuadTree._min) || (this.height > org.flixel.FlxQuadTree._min);
			
			//Set up comparison/sort helpers
			this._nw = null;
			this._ne = null;
			this._se = null;
			this._sw = null;
			this._l = this.x;
			this._r = this.x+this.width;
			this._hw = this.width/2;
			this._mx = this._l+this._hw;
			this._t = this.y;
			this._b = this.y+this.height;
			this._hh = this.height/2;
			this._my = this._t+this._hh;
		},
		
		/**
		 * Call this function to add an object to the root of the tree.
		 * This function will recursively add all group members, but
		 * not the groups themselves.
		 * 
		 * @param	Object		The <code>FlxObject</code> you want to add.  <code>FlxGroup</code> objects will be recursed and their applicable members added automatically.
		 * @param	List		A <code>uint</code> flag indicating the list to which you want to add the objects.  Options are <code>A_LIST</code> and <code>B_LIST</code>.
		 */
		"public function add",function add(Object/*:FlxObject*/, List/*:uint*/)/*:void*/
		{
			org.flixel.FlxQuadTree._oa = List;
			if(Object._group)
			{
				var i/*:uint*/ = 0;
				var m/*:FlxObject*/;
				var members/*:Array*/ = (as(Object,  org.flixel.FlxGroup)).members;
				var l/*:uint*/ = members.length;
				while(i < l)
				{
					m =as( members[i++],  org.flixel.FlxObject);
					if((m != null) && m.exists)
					{
						if(m._group)
							this.add(m,List);
						else if(m.solid)
						{
							org.flixel.FlxQuadTree._o = m;
							org.flixel.FlxQuadTree._ol = org.flixel.FlxQuadTree._o.x;
							org.flixel.FlxQuadTree._ot = org.flixel.FlxQuadTree._o.y;
							org.flixel.FlxQuadTree._or = org.flixel.FlxQuadTree._o.x + org.flixel.FlxQuadTree._o.width;
							org.flixel.FlxQuadTree._ob = org.flixel.FlxQuadTree._o.y + org.flixel.FlxQuadTree._o.height;
							this.addObject();
						}
					}
				}
			}
			if(Object.solid)
			{
				org.flixel.FlxQuadTree._o = Object;
				org.flixel.FlxQuadTree._ol = org.flixel.FlxQuadTree._o.x;
				org.flixel.FlxQuadTree._ot = org.flixel.FlxQuadTree._o.y;
				org.flixel.FlxQuadTree._or = org.flixel.FlxQuadTree._o.x + org.flixel.FlxQuadTree._o.width;
				org.flixel.FlxQuadTree._ob = org.flixel.FlxQuadTree._o.y + org.flixel.FlxQuadTree._o.height;
				this.addObject();
			}
		},
		
		/**
		 * Internal function for recursively navigating and creating the tree
		 * while adding objects to the appropriate nodes.
		 */
		"protected function addObject",function addObject()/*:void*/
		{
			//If this quad (not its children) lies entirely inside this object, add it here
			if(!this._canSubdivide || ((this._l >= org.flixel.FlxQuadTree._ol) && (this._r <= org.flixel.FlxQuadTree._or) && (this._t >= org.flixel.FlxQuadTree._ot) && (this._b <= org.flixel.FlxQuadTree._ob)))
			{
				this.addToList();
				return;
			}
			
			//See if the selected object fits completely inside any of the quadrants
			if((org.flixel.FlxQuadTree._ol > this._l) && (org.flixel.FlxQuadTree._or < this._mx))
			{
				if((org.flixel.FlxQuadTree._ot > this._t) && (org.flixel.FlxQuadTree._ob < this._my))
				{
					if(this._nw == null)
						this._nw = new org.flixel.FlxQuadTree(this._l,this._t,this._hw,this._hh,this);
					this._nw.addObject();
					return;
				}
				if((org.flixel.FlxQuadTree._ot > this._my) && (org.flixel.FlxQuadTree._ob < this._b))
				{
					if(this._sw == null)
						this._sw = new org.flixel.FlxQuadTree(this._l,this._my,this._hw,this._hh,this);
					this._sw.addObject();
					return;
				}
			}
			if((org.flixel.FlxQuadTree._ol > this._mx) && (org.flixel.FlxQuadTree._or < this._r))
			{
				if((org.flixel.FlxQuadTree._ot > this._t) && (org.flixel.FlxQuadTree._ob < this._my))
				{
					if(this._ne == null)
						this._ne = new org.flixel.FlxQuadTree(this._mx,this._t,this._hw,this._hh,this);
					this._ne.addObject();
					return;
				}
				if((org.flixel.FlxQuadTree._ot > this._my) && (org.flixel.FlxQuadTree._ob < this._b))
				{
					if(this._se == null)
						this._se = new org.flixel.FlxQuadTree(this._mx,this._my,this._hw,this._hh,this);
					this._se.addObject();
					return;
				}
			}
			
			//If it wasn't completely contained we have to check out the partial overlaps
			if((org.flixel.FlxQuadTree._or > this._l) && (org.flixel.FlxQuadTree._ol < this._mx) && (org.flixel.FlxQuadTree._ob > this._t) && (org.flixel.FlxQuadTree._ot < this._my))
			{
				if(this._nw == null)
					this._nw = new org.flixel.FlxQuadTree(this._l,this._t,this._hw,this._hh,this);
				this._nw.addObject();
			}
			if((org.flixel.FlxQuadTree._or > this._mx) && (org.flixel.FlxQuadTree._ol < this._r) && (org.flixel.FlxQuadTree._ob > this._t) && (org.flixel.FlxQuadTree._ot < this._my))
			{
				if(this._ne == null)
					this._ne = new org.flixel.FlxQuadTree(this._mx,this._t,this._hw,this._hh,this);
				this._ne.addObject();
			}
			if((org.flixel.FlxQuadTree._or > this._mx) && (org.flixel.FlxQuadTree._ol < this._r) && (org.flixel.FlxQuadTree._ob > this._my) && (org.flixel.FlxQuadTree._ot < this._b))
			{
				if(this._se == null)
					this._se = new org.flixel.FlxQuadTree(this._mx,this._my,this._hw,this._hh,this);
				this._se.addObject();
			}
			if((org.flixel.FlxQuadTree._or > this._l) && (org.flixel.FlxQuadTree._ol < this._mx) && (org.flixel.FlxQuadTree._ob > this._my) && (org.flixel.FlxQuadTree._ot < this._b))
			{
				if(this._sw == null)
					this._sw = new org.flixel.FlxQuadTree(this._l,this._my,this._hw,this._hh,this);
				this._sw.addObject();
			}
		},
		
		/**
		 * Internal function for recursively adding objects to leaf lists.
		 */
		"protected function addToList",function addToList()/*:void*/
		{
			var ot/*:FlxList*/;
			if(org.flixel.FlxQuadTree._oa == org.flixel.FlxQuadTree.A_LIST)
			{
				if(this._tailA.object != null)
				{
					ot = this._tailA;
					this._tailA = new org.flixel.data.FlxList();
					ot.next = this._tailA;
				}
				this._tailA.object = org.flixel.FlxQuadTree._o;
			}
			else
			{
				if(this._tailB.object != null)
				{
					ot = this._tailB;
					this._tailB = new org.flixel.data.FlxList();
					ot.next = this._tailB;
				}
				this._tailB.object = org.flixel.FlxQuadTree._o;
			}
			if(!this._canSubdivide)
				return;
			if(this._nw != null)
				this._nw.addToList();
			if(this._ne != null)
				this._ne.addToList();
			if(this._se != null)
				this._se.addToList();
			if(this._sw != null)
				this._sw.addToList();
		},
		
		/**
		 * <code>FlxQuadTree</code>'s other main function.  Call this after adding objects
		 * using <code>FlxQuadTree.add()</code> to compare the objects that you loaded.
		 * 
		 * @param	BothLists	Whether you are doing an A-B list comparison, or comparing A against itself.
		 * @param	Callback	A function with two <code>FlxObject</code> parameters - e.g. <code>myOverlapFunction(Object1:FlxObject,Object2:FlxObject);</code>  If no function is provided, <code>FlxQuadTree</code> will call <code>kill()</code> on both objects.
		 *
		 * @return	Whether or not any overlaps were found.
		 */
		"public function overlap",function overlap(BothLists/*:Boolean=true*/,Callback/*:Function=null*/)/*:Boolean*/
		{if(arguments.length<2){if(arguments.length<1){BothLists=true;}Callback=null;}
			org.flixel.FlxQuadTree._oc = Callback;
			var c/*:Boolean*/ = false;
			var itr/*:FlxList*/;
			if(BothLists)
			{
				//An A-B list comparison
				org.flixel.FlxQuadTree._oa = org.flixel.FlxQuadTree.B_LIST;
				if(this._headA.object != null)
				{
					itr = this._headA;
					while(itr != null)
					{
						org.flixel.FlxQuadTree._o = itr.object;
						if(org.flixel.FlxQuadTree._o.exists && org.flixel.FlxQuadTree._o.solid && this.overlapNode())
							c = true;
						itr = itr.next;
					}
				}
				org.flixel.FlxQuadTree._oa = org.flixel.FlxQuadTree.A_LIST;
				if(this._headB.object != null)
				{
					itr = this._headB;
					while(itr != null)
					{
						org.flixel.FlxQuadTree._o = itr.object;
						if(org.flixel.FlxQuadTree._o.exists && org.flixel.FlxQuadTree._o.solid)
						{
							if((this._nw != null) && this._nw.overlapNode())
								c = true;
							if((this._ne != null) && this._ne.overlapNode())
								c = true;
							if((this._se != null) && this._se.overlapNode())
								c = true;
							if((this._sw != null) && this._sw.overlapNode())
								c = true;
						}
						itr = itr.next;
					}
				}
			}
			else
			{
				//Just checking the A list against itself
				if(this._headA.object != null)
				{
					itr = this._headA;
					while(itr != null)
					{
						org.flixel.FlxQuadTree._o = itr.object;
						if(org.flixel.FlxQuadTree._o.exists && org.flixel.FlxQuadTree._o.solid && this.overlapNode(itr.next))
							c = true;
						itr = itr.next;
					}
				}
			}
			
			//Advance through the tree by calling overlap on each child
			if((this._nw != null) && this._nw.overlap(BothLists,org.flixel.FlxQuadTree._oc))
				c = true;
			if((this._ne != null) && this._ne.overlap(BothLists,org.flixel.FlxQuadTree._oc))
				c = true;
			if((this._se != null) && this._se.overlap(BothLists,org.flixel.FlxQuadTree._oc))
				c = true;
			if((this._sw != null) && this._sw.overlap(BothLists,org.flixel.FlxQuadTree._oc))
				c = true;
			
			return c;
		},
		
		/**
		 * An internal function for comparing an object against the contents of a node.
		 * 
		 * @param	Iterator	An optional pointer to a linked list entry (for comparing A against itself).
		 * 
		 * @return	Whether or not any overlaps were found.
		 */
		"protected function overlapNode",function overlapNode(Iterator/*:FlxList=null*/)/*:Boolean*/
		{if(arguments.length<1){Iterator=null;}
			//member list setup
			var c/*:Boolean*/ = false;
			var co/*:FlxObject*/;
			var itr/*:FlxList*/ = Iterator;
			if(itr == null)
			{
				if(org.flixel.FlxQuadTree._oa == org.flixel.FlxQuadTree.A_LIST)
					itr = this._headA;
				else
					itr = this._headB;
			}
			
			//Make sure this is a valid list to walk first!
			if(itr.object != null)
			{
				//Walk the list and check for overlaps
				while(itr != null)
				{
					co = itr.object;
					if( (org.flixel.FlxQuadTree._o === co) || !co.exists || !org.flixel.FlxQuadTree._o.exists || !co.solid || !org.flixel.FlxQuadTree._o.solid ||
						(org.flixel.FlxQuadTree._o.x + org.flixel.FlxQuadTree._o.width  < co.x + org.flixel.FlxU.roundingError) ||
						(org.flixel.FlxQuadTree._o.x + org.flixel.FlxU.roundingError > co.x + co.width) ||
						(org.flixel.FlxQuadTree._o.y + org.flixel.FlxQuadTree._o.height < co.y + org.flixel.FlxU.roundingError) ||
						(org.flixel.FlxQuadTree._o.y + org.flixel.FlxU.roundingError > co.y + co.height) )
					{
						itr = itr.next;
						continue;
					}
					if(org.flixel.FlxQuadTree._oc == null)
					{
						org.flixel.FlxQuadTree._o.kill();
						co.kill();
						c = true;
					}
					else if(org.flixel.FlxQuadTree._oc(org.flixel.FlxQuadTree._o,co))
						c = true;
					itr = itr.next;
				}
			}
			
			return c;
		},
	];},[],["org.flixel.FlxRect","org.flixel.data.FlxList","uint","org.flixel.FlxGroup","org.flixel.FlxObject","org.flixel.FlxU"], "0.8.0", "0.8.3"
);