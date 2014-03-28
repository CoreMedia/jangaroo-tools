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
* A 2D column vector.
*/

"public class b2Vec2",1,function($$private){;return[function(){joo.classLoader.init(Number);},

	"public function b2Vec2",function b2Vec2$(x_/*:Number=0*/, y_/*:Number=0*/)/* : void*/ {if(arguments.length<2){if(arguments.length<1){x_=0;}y_=0;}this.x=x_; this.y=y_;},

	"public function SetZero",function SetZero()/* : void*/ { this.x = 0.0; this.y = 0.0; },
	"public function Set",function Set(x_/*:Number=0*/, y_/*:Number=0*/)/* : void*/ {if(arguments.length<2){if(arguments.length<1){x_=0;}y_=0;}this.x=x_; this.y=y_;},
	"public function SetV",function SetV(v/*:b2Vec2*/)/* : void*/ {this.x=v.x; this.y=v.y;},

	"public function GetNegative",function GetNegative()/*:b2Vec2*/ { return new Box2D.Common.Math.b2Vec2(-this.x, -this.y); },
	"public function NegativeSelf",function NegativeSelf()/*:void*/ { this.x = -this.x; this.y = -this.y; },
	
	"static public function Make",function Make(x_/*:Number*/, y_/*:Number*/)/*:b2Vec2*/
	{
		return new Box2D.Common.Math.b2Vec2(x_, y_);
	},
	
	"public function Copy",function Copy()/*:b2Vec2*/{
		return new Box2D.Common.Math.b2Vec2(this.x,this.y);
	},
	
	"public function Add",function Add(v/*:b2Vec2*/)/* : void*/
	{
		this.x += v.x; this.y += v.y;
	},
	
	"public function Subtract",function Subtract(v/*:b2Vec2*/)/* : void*/
	{
		this.x -= v.x; this.y -= v.y;
	},

	"public function Multiply",function Multiply(a/*:Number*/)/* : void*/
	{
		this.x *= a; this.y *= a;
	},
	
	"public function MulM",function MulM(A/*:b2Mat22*/)/* : void*/
	{
		var tX/*:Number*/ = this.x;
		this.x = A.col1.x * tX + A.col2.x * this.y;
		this.y = A.col1.y * tX + A.col2.y * this.y;
	},
	
	"public function MulTM",function MulTM(A/*:b2Mat22*/)/* : void*/
	{
		var tX/*:Number*/ = Box2D.Common.Math.b2Math.Dot(this, A.col1);
		this.y = Box2D.Common.Math.b2Math.Dot(this, A.col2);
		this.x = tX;
	},
	
	"public function CrossVF",function CrossVF(s/*:Number*/)/* : void*/
	{
		var tX/*:Number*/ = this.x;
		this.x = s * this.y;
		this.y = -s * tX;
	},
	
	"public function CrossFV",function CrossFV(s/*:Number*/)/* : void*/
	{
		var tX/*:Number*/ = this.x;
		this.x = -s * this.y;
		this.y = s * tX;
	},
	
	"public function MinV",function MinV(b/*:b2Vec2*/)/* : void*/
	{
		this.x = this.x < b.x ? this.x : b.x;
		this.y = this.y < b.y ? this.y : b.y;
	},
	
	"public function MaxV",function MaxV(b/*:b2Vec2*/)/* : void*/
	{
		this.x = this.x > b.x ? this.x : b.x;
		this.y = this.y > b.y ? this.y : b.y;
	},
	
	"public function Abs",function Abs()/* : void*/
	{
		if (this.x < 0) this.x = -this.x;
		if (this.y < 0) this.y = -this.y;
	},

	"public function Length",function Length()/*:Number*/
	{
		return Math.sqrt(this.x * this.x + this.y * this.y);
	},
	
	"public function LengthSquared",function LengthSquared()/*:Number*/
	{
		return (this.x * this.x + this.y * this.y);
	},

	"public function Normalize",function Normalize()/*:Number*/
	{
		var length/*:Number*/ = Math.sqrt(this.x * this.x + this.y * this.y);
		if (length < Number.MIN_VALUE)
		{
			return 0.0;
		}
		var invLength/*:Number*/ = 1.0 / length;
		this.x *= invLength;
		this.y *= invLength;
		
		return length;
	},

	"public function IsValid",function IsValid()/*:Boolean*/
	{
		return Box2D.Common.Math.b2Math.IsValid(this.x) && Box2D.Common.Math.b2Math.IsValid(this.y);
	},

	"public var",{ x/*:Number*/:NaN},
	"public var",{ y/*:Number*/:NaN},
];},["Make"],["Box2D.Common.Math.b2Math","Math","Number"], "0.8.0", "0.8.1"

);