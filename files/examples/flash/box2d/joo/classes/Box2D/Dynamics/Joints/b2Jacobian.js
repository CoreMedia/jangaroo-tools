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

"package Box2D.Dynamics.Joints",/*{


import Box2D.Common.Math.*

import Box2D.Common.b2internal
use namespace b2internal*/


/**
* @private
*/
"public class b2Jacobian",1,function($$private){;return[

	"public var",{ linearA/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"public var",{ angularA/*:Number*/:NaN},
	"public var",{ linearB/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"public var",{ angularB/*:Number*/:NaN},

	"public function SetZero",function SetZero()/* : void*/{
		this.linearA.SetZero(); this.angularA = 0.0;
		this.linearB.SetZero(); this.angularB = 0.0;
	},
	"public function Set",function Set(x1/*:b2Vec2*/, a1/*:Number*/, x2/*:b2Vec2*/, a2/*:Number*/)/* : void*/{
		this.linearA.SetV(x1); this.angularA = a1;
		this.linearB.SetV(x2); this.angularB = a2;
	},
	"public function Compute",function Compute(x1/*:b2Vec2*/, a1/*:Number*/, x2/*:b2Vec2*/, a2/*:Number*/)/*:Number*/{
		
		//return b2Math.b2Dot(linearA, x1) + angularA * a1 + b2Math.b2Dot(linearV, x2) + angularV * a2;
		return (this.linearA.x*x1.x + this.linearA.y*x1.y) + this.angularA * a1 + (this.linearB.x*x2.x + this.linearB.y*x2.y) + this.angularB * a2;
	},
"public function b2Jacobian",function b2Jacobian$(){this.linearA=this.linearA();this.linearB=this.linearB();}];},[],["Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"


);