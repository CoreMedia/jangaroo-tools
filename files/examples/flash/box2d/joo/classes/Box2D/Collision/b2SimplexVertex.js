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

"package Box2D.Collision",/* 
{
	
import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Common.*
import Box2D.Common.Math.**/

"internal class b2SimplexVertex",1,function($$private){;return[

	"public function Set",function Set(other/*:b2SimplexVertex*/)/*:void*/
	{
		this.wA.SetV(other.wA);
		this.wB.SetV(other.wB);
		this.w.SetV(other.w);
		this.a = other.a;
		this.indexA = other.indexA;
		this.indexB = other.indexB;
	},
	
	"public var",{ wA/*:b2Vec2*/:null},		// support point in proxyA
	"public var",{ wB/*:b2Vec2*/:null},		// support point in proxyB
	"public var",{ w/*:b2Vec2*/:null},		// wB - wA
	"public var",{ a/*:Number*/:NaN},		// barycentric coordinate for closest point
	"public var",{ indexA/*:int*/:0},	// wA index
	"public var",{ indexB/*:int*/:0},	// wB index
];},[],[], "0.8.0", "0.8.1"
	
);