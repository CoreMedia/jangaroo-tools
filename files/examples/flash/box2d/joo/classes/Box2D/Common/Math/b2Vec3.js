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
* A 2D column vector with 3 elements.
*/

"public class b2Vec3",1,function($$private){;return[

	/**
	 * Construct using co-ordinates
	 */
	"public function b2Vec3",function b2Vec3$(x/*:Number = 0*/, y/*:Number = 0*/, z/*:Number = 0*/)
	{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){x = 0;}y = 0;}z = 0;}
		this.x = x;
		this.y = y;
		this.z = z;
	},
	
	/**
	 * Sets this vector to all zeros
	 */
	"public function SetZero",function SetZero()/*:void*/
	{
		this.x = this.y = this.z = 0.0;
	},
	
	/**
	 * Set this vector to some specified coordinates.
	 */
	"public function Set",function Set(x/*:Number*/, y/*:Number*/, z/*:Number*/)/*:void*/
	{
		this.x = x;
		this.y = y;
		this.z = z;
	},
	
	"public function SetV",function SetV(v/*:b2Vec3*/)/*:void*/
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	},
	
	/**
	 * Negate this vector
	 */
	"public function GetNegative",function GetNegative()/*:b2Vec3*/ { return new Box2D.Common.Math.b2Vec3( -this.x, -this.y, -this.z); },
	
	"public function NegativeSelf",function NegativeSelf()/*:void*/ { this.x = -this.x; this.y = -this.y; this.z = -this.z; },
	
	"public function Copy",function Copy()/*:b2Vec3*/{
		return new Box2D.Common.Math.b2Vec3(this.x,this.y,this.z);
	},
	
	"public function Add",function Add(v/*:b2Vec3*/)/* : void*/
	{
		this.x += v.x; this.y += v.y; this.z += v.z;
	},
	
	"public function Subtract",function Subtract(v/*:b2Vec3*/)/* : void*/
	{
		this.x -= v.x; this.y -= v.y; this.z -= v.z;
	},

	"public function Multiply",function Multiply(a/*:Number*/)/* : void*/
	{
		this.x *= a; this.y *= a; this.z *= a;
	},
	
	"public var",{ x/*:Number*/:NaN},
	"public var",{ y/*:Number*/:NaN},
	"public var",{ z/*:Number*/:NaN},
	
];},[],[], "0.8.0", "0.8.1"
);