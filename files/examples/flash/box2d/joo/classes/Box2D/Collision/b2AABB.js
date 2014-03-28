joo.classLoader.prepare(/*
* Copyright (c) 2006-2007 Erin Catto http://www.gphysics.com
*
* This software is provided 'as-is', without any express or implied
* warranty.  In no event will the authors be held liable for any damages
* arising from the use of this software.
* Permission is granted to anyone to use this software for any purpose,
* including commercial applications, and to alter it and redistribute it
* freely, subject to the following restrictions:
* 1. The origin of this software must not be misrepresented; you must not
* claim that you wrote the original software. If you use this software
* in a product, an acknowledgment in the product documentation would be
* appreciated but is not required.
* 2. Altered source versions must be plainly marked as such, and must not be
* misrepresented as being the original software.
* 3. This notice may not be removed or altered from any source distribution.
*/

"package Box2D.Collision",/*{
	
import Box2D.Collision.*
import Box2D.Collision.Shapes.b2Shape
import Box2D.Common.b2Settings
import Box2D.Common.Math.*

import Box2D.Common.b2internal
use namespace b2internal*/

/**
* An axis aligned bounding box.
*/
"public class b2AABB",1,function($$private){;return[function(){joo.classLoader.init(Number);},

	"public function Copy",function Copy()/*:b2AABB*/
	{
		var aabb/*:b2AABB*/ = new Box2D.Collision.b2AABB();
		aabb.Set(this);
		return aabb;
	},
	
	"public function Set",function Set(other/*:b2AABB*/)/*:void*/
	{
		this.lowerBound.SetV(other.lowerBound);
		this.upperBound.SetV(other.upperBound);
	},
	
	/**
	* Verify that the bounds are sorted.
	*/
	"public function IsValid",function IsValid()/*:Boolean*/{
		//b2Vec2 d = upperBound - lowerBound;;
		var dX/*:Number*/ = this.upperBound.x - this.lowerBound.x;
		var dY/*:Number*/ = this.upperBound.y - this.lowerBound.y;
		var valid/*:Boolean*/ = dX >= 0.0 && dY >= 0.0;
		valid = valid && this.lowerBound.IsValid() && this.upperBound.IsValid();
		return valid;
	},
	
	/** Get the center of the AABB. */
	"public function GetCenter",function GetCenter()/*:b2Vec2*/
	{
		return new Box2D.Common.Math.b2Vec2( (this.lowerBound.x + this.upperBound.x) / 2,
		                   (this.lowerBound.y + this.upperBound.y) / 2);
	},
	
	/** Get the extents of the AABB (half-widths). */
	"public function GetExtents",function GetExtents()/*:b2Vec2*/
	{
		return new Box2D.Common.Math.b2Vec2( (this.upperBound.x - this.lowerBound.x) / 2,
		                   (this.upperBound.y - this.lowerBound.y) / 2);
	},
	
	/**
	 * Is an AABB contained within this one.
	 */
	"public function Contains",function Contains(aabb/*:b2AABB*/)/*:Boolean*/
	{
		var result/*:Boolean*/ = true;
		result =
result&&( this.lowerBound.x <= aabb.lowerBound.x);
		result =
result&&( this.lowerBound.y <= aabb.lowerBound.y);
		result =
result&&( aabb.upperBound.x <= this.upperBound.x);
		result =
result&&( aabb.upperBound.y <= this.upperBound.y);
		return result;
	},
	
	// From Real-time Collision Detection, p179.
	/**
	 * Perform a precise raycast against the AABB.
	 */
	"public function RayCast",function RayCast(output/*:b2RayCastOutput*/, input/*:b2RayCastInput*/)/*:Boolean*/
	{
		var tmin/*:Number*/ = -Number.MAX_VALUE;
		var tmax/*:Number*/ = Number.MAX_VALUE;
		
		var pX/*:Number*/ = input.p1.x;
		var pY/*:Number*/ = input.p1.y;
		var dX/*:Number*/ = input.p2.x - input.p1.x;
		var dY/*:Number*/ = input.p2.y - input.p1.y;
		var absDX/*:Number*/ = Math.abs(dX);
		var absDY/*:Number*/ = Math.abs(dY);
		
		var normal/*:b2Vec2*/ = output.normal;
		
		var inv_d/*:Number*/;
		var t1/*:Number*/;
		var t2/*:Number*/;
		var t3/*:Number*/;
		var s/*:Number*/;
		
		//x
		{
			if (absDX < Number.MIN_VALUE)
			{
				// Parallel.
				if (pX < this.lowerBound.x || this.upperBound.x < pX)
					return false;
			}
			else
			{
				inv_d = 1.0 / dX;
				t1 = (this.lowerBound.x - pX) * inv_d;
				t2 = (this.upperBound.x - pX) * inv_d;
				
				// Sign of the normal vector
				s = -1.0;
				
				if (t1 > t2)
				{
					t3 = t1;
					t1 = t2;
					t2 = t3;
					s = 1.0;
				}
				
				// Push the min up
				if (t1 > tmin)
				{
					normal.x = s;
					normal.y = 0;
					tmin = t1;
				}
				
				// Pull the max down
				tmax = Math.min(tmax, t2);
				
				if (tmin > tmax)
					return false;
			}
		}
		//y
		{
			if (absDY < Number.MIN_VALUE)
			{
				// Parallel.
				if (pY < this.lowerBound.y || this.upperBound.y < pY)
					return false;
			}
			else
			{
				inv_d = 1.0 / dY;
				t1 = (this.lowerBound.y - pY) * inv_d;
				t2 = (this.upperBound.y - pY) * inv_d;
				
				// Sign of the normal vector
				s = -1.0;
				
				if (t1 > t2)
				{
					t3 = t1;
					t1 = t2;
					t2 = t3;
					s = 1.0;
				}
				
				// Push the min up
				if (t1 > tmin)
				{
					normal.y = s;
					normal.x = 0;
					tmin = t1;
				}
				
				// Pull the max down
				tmax = Math.min(tmax, t2);
				
				if (tmin > tmax)
					return false;
			}
		}
		
		output.fraction = tmin;
		return true;
	},
	
	/**
	 * Tests if another AABB overlaps this one.
	 */
	"public function TestOverlap",function TestOverlap(other/*:b2AABB*/)/*:Boolean*/
	{
		var d1X/*:Number*/ = other.lowerBound.x - this.upperBound.x;
		var d1Y/*:Number*/ = other.lowerBound.y - this.upperBound.y;
		var d2X/*:Number*/ = this.lowerBound.x - other.upperBound.x;
		var d2Y/*:Number*/ = this.lowerBound.y - other.upperBound.y;

		if (d1X > 0.0 || d1Y > 0.0)
			return false;

		if (d2X > 0.0 || d2Y > 0.0)
			return false;

		return true;
	},
	
	/** Combine two AABBs into one. */
	"public static function Combine",function Combine(aabb1/*:b2AABB*/, aabb2/*:b2AABB*/)/*:b2AABB*/
	{
		var aabb/*:b2AABB*/ = new Box2D.Collision.b2AABB();
		aabb.Combine(aabb1, aabb2);
		return aabb;
	},
	
	/** Combine two AABBs into one. */
	"public function Combine",function Combine(aabb1/*:b2AABB*/, aabb2/*:b2AABB*/)/*:void*/
	{
		this.lowerBound.x = Math.min(aabb1.lowerBound.x, aabb2.lowerBound.x);
		this.lowerBound.y = Math.min(aabb1.lowerBound.y, aabb2.lowerBound.y);
		this.upperBound.x = Math.max(aabb1.upperBound.x, aabb2.upperBound.x);
		this.upperBound.y = Math.max(aabb1.upperBound.y, aabb2.upperBound.y);
	},

	/** The lower vertex */
	"public var",{ lowerBound/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	/** The upper vertex */
	"public var",{ upperBound/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
"public function b2AABB",function b2AABB$(){this.lowerBound=this.lowerBound();this.upperBound=this.upperBound();}];},["Combine"],["Box2D.Common.Math.b2Vec2","Number","Math"], "0.8.0", "0.8.1"


);