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

"package Box2D.Common.Math",/*{

	
import Box2D.Common.**/
	
	
/**
* This describes the motion of a body/shape for TOI computation.
* Shapes are defined with respect to the body origin, which may
* no coincide with the center of mass. However, to support dynamics
* we must interpolate the center of mass position.
*/
"public class b2Sweep",1,function($$private){;return[function(){joo.classLoader.init(Number);},

	"public function Set",function Set(other/*:b2Sweep*/)/*:void*/
	{
		this.localCenter.SetV(other.localCenter);
		this.c0.SetV(other.c0);
		this.c.SetV(other.c);
		this.a0 = other.a0;
		this.a = other.a;
		this.t0 = other.t0;
	},
	
	"public function Copy",function Copy()/*:b2Sweep*/
	{
		var copy/*:b2Sweep*/ = new Box2D.Common.Math.b2Sweep();
		copy.localCenter.SetV(this.localCenter);
		copy.c0.SetV(this.c0);
		copy.c.SetV(this.c);
		copy.a0 = this.a0;
		copy.a = this.a;
		copy.t0 = this.t0;
		return copy;
	},
	
	/**
	* Get the interpolated transform at a specific time.
	* @param alpha is a factor in [0,1], where 0 indicates t0.
	*/
	"public function GetTransform",function GetTransform(xf/*:b2Transform*/, alpha/*:Number*/)/*:void*/
	{
		xf.position.x = (1.0 - alpha) * this.c0.x + alpha * this.c.x;
		xf.position.y = (1.0 - alpha) * this.c0.y + alpha * this.c.y;
		var angle/*:Number*/ = (1.0 - alpha) * this.a0 + alpha * this.a;
		xf.R.Set(angle);
		
		// Shift to origin
		//xf->position -= b2Mul(xf->R, localCenter);
		var tMat/*:b2Mat22*/ = xf.R;
		xf.position.x -= (tMat.col1.x * this.localCenter.x + tMat.col2.x * this.localCenter.y);
		xf.position.y -= (tMat.col1.y * this.localCenter.x + tMat.col2.y * this.localCenter.y);
	},
	
	/**
	* Advance the sweep forward, yielding a new initial state.
	* @param t the new initial time.
	*/
	"public function Advance",function Advance(t/*:Number*/)/* : void*/{
		if (this.t0 < t && 1.0 - this.t0 > Number.MIN_VALUE)
		{
			var alpha/*:Number*/ = (t - this.t0) / (1.0 - this.t0);
			//c0 = (1.0f - alpha) * c0 + alpha * c;
			this.c0.x = (1.0 - alpha) * this.c0.x + alpha * this.c.x;
			this.c0.y = (1.0 - alpha) * this.c0.y + alpha * this.c.y;
			this.a0 = (1.0 - alpha) * this.a0 + alpha * this.a;
			this.t0 = t;
		}
	},

	/** Local center of mass position */
	"public var",{ localCenter/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	/** Center world position */
	"public var",{ c0/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2);}},
	/** Center world position */
	"public var",{ c/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	/** World angle */
	"public var",{ a0/*:Number*/:NaN},
	/** World angle */
	"public var",{ a/*:Number*/:NaN},
	/** Time interval = [t0,1], where t0 is in [0,1] */
	"public var",{ t0/*:Number*/:NaN},
"public function b2Sweep",function b2Sweep$(){this.localCenter=this.localCenter();this.c0=this.c0();this.c=this.c();}];},[],["Number","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"

);