joo.classLoader.prepare(/*
* Copyright (c) 2009 Erin Catto http://www.gphysics.com
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

/**
 * Returns data on the collision between a ray and a shape.
 */
"package Box2D.Collision",/* 
{
	import Box2D.Common.Math.b2Vec2*/
	
	"public class b2RayCastOutput",1,function($$private){;return[ 
	
		"public function Set",function Set(other/*:b2RayCastOutput*/)/*:void*/
		{
			this.normal.SetV(other.normal);
			this.fraction = other.fraction;
		},
		
		/**
		 * The normal at the point of collision
		 */
		"public var",{ normal/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
		/**
		 * The fraction between p1 and p2 that the collision occurs at
		 */
		"public var",{ fraction/*:Number*/:NaN},
	"public function b2RayCastOutput",function b2RayCastOutput$(){this.normal=this.normal();}];},[],["Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
	
);