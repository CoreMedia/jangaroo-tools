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
import Box2D.Collision.Shapes.*
import Box2D.Common.*
import Box2D.Common.Math.*
import Box2D.Common.b2internal

use namespace b2internal*/

	/**
	 * A distance proxy is used by the GJK algorithm.
 	 * It encapsulates any shape.
 	 */
	"public class b2DistanceProxy",1,function($$private){var as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Collision.Shapes.b2Shape);}, 
	
 		/**
 		 * Initialize the proxy using the given shape. The shape
 		 * must remain in scope while the proxy is in use.
 		 */
 		"public function Set",function Set(shape/*:b2Shape*/)/*:void*/
		{
			switch(shape.GetType())
			{
				case Box2D.Collision.Shapes.b2Shape.e_circleShape:
				{
					var circle/*:b2CircleShape*/ =as( shape,  Box2D.Collision.Shapes.b2CircleShape);
					this.m_vertices = new Array/*b2Vec2*/(1, true);
					this.m_vertices[0] = circle.m_p;
					this.m_count = 1;
					this.m_radius = circle.m_radius;
				}
				break;
				case Box2D.Collision.Shapes.b2Shape.e_polygonShape:
				{
					var polygon/*:b2PolygonShape*/ =as(  shape,  Box2D.Collision.Shapes.b2PolygonShape);
					this.m_vertices = polygon.m_vertices;
					this.m_count = polygon.m_vertexCount;
					this.m_radius = polygon.m_radius;
				}
				break;
				default:/*
				assert false;*/
			}
		},
		
 		/**
 		 * Get the supporting vertex index in the given direction.
 		 */
 		"public function GetSupport",function GetSupport(d/*:b2Vec2*/)/*:Number*/
		{
			var bestIndex/*:int*/ = 0;
			var bestValue/*:Number*/ = this.m_vertices[0].x * d.x + this.m_vertices[0].y * d.y;
			for (var i/*:int*/ = 1; i < this.m_count; ++i)
			{
				var value/*:Number*/ = this.m_vertices[i].x * d.x + this.m_vertices[i].y * d.y;
				if (value > bestValue)
				{
					bestIndex = i;
					bestValue = value;
				}
			}
			return bestIndex;
		},
		
 		/**
 		 * Get the supporting vertex in the given direction.
 		 */
 		"public function GetSupportVertex",function GetSupportVertex(d/*:b2Vec2*/)/*:b2Vec2*/
		{
			var bestIndex/*:int*/ = 0;
			var bestValue/*:Number*/ = this.m_vertices[0].x * d.x + this.m_vertices[0].y * d.y;
			for (var i/*:int*/ = 1; i < this.m_count; ++i)
			{
				var value/*:Number*/ = this.m_vertices[i].x * d.x + this.m_vertices[i].y * d.y;
				if (value > bestValue)
				{
					bestIndex = i;
					bestValue = value;
				}
			}
			return this.m_vertices[bestIndex];
		},
 		/**
 		 * Get the vertex count.
 		 */
 		"public function GetVertexCount",function GetVertexCount()/*:int*/
		{
			return this.m_count;
		},
		
 		/**
 		 * Get a vertex by index. Used by b2Distance.
 		 */
 		"public function GetVertex",function GetVertex(index/*:int*/)/*:b2Vec2*/
		{/*
			assert 0 <= index && index < this.m_count;*/
			return this.m_vertices[index];
		},
		
 		"public var",{ m_vertices/*:Array*/:null}/*b2Vec2*/,
 		"public var",{ m_count/*:int*/:0},
 		"public var",{ m_radius/*:Number*/:NaN},
	];},[],["Box2D.Collision.Shapes.b2Shape","Box2D.Collision.Shapes.b2CircleShape","Array","Box2D.Collision.Shapes.b2PolygonShape"], "0.8.0", "0.8.1"
	
);