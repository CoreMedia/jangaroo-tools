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

"package Box2D.Dynamics.Contacts",/*{


import Box2D.Collision.Shapes.*
import Box2D.Collision.*
import Box2D.Dynamics.*
import Box2D.Common.*
import Box2D.Common.Math.*

import Box2D.Common.b2internal
use namespace b2internal*/


/**
* @private
*/
"public class b2PolyAndCircleContact extends Box2D.Dynamics.Contacts.b2Contact",2,function($$private){var as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Collision.Shapes.b2Shape);},
	
	"static public function Create",function Create(allocator/*:**/)/*:b2Contact*/{
		return new Box2D.Dynamics.Contacts.b2PolyAndCircleContact();
	},
	"static public function Destroy",function Destroy(contact/*:b2Contact*/, allocator/*:**/)/*: void*/{
	},

	"public function Reset",function Reset(fixtureA/*:b2Fixture*/, fixtureB/*:b2Fixture*/)/*:void*/{
		this.b2internal_Reset$2(fixtureA, fixtureB);/*
		assert fixtureA.GetType() == Box2D.Collision.Shapes.b2Shape.e_polygonShape;
		assert fixtureB.GetType() == Box2D.Collision.Shapes.b2Shape.e_circleShape;*/
	},
	//~b2PolyAndCircleContact() {}

	"b2internal override function Evaluate",function Evaluate()/*: void*/{
		var bA/*:b2Body*/ = this.m_fixtureA.m_body;
		var bB/*:b2Body*/ = this.m_fixtureB.m_body;
		
		Box2D.Collision.b2Collision.CollidePolygonAndCircle(this.m_manifold,as( 
					this.m_fixtureA.GetShape(),  Box2D.Collision.Shapes.b2PolygonShape), bA.m_xf,as( 
					this.m_fixtureB.GetShape(),  Box2D.Collision.Shapes.b2CircleShape), bB.m_xf);
	},
];},["Create","Destroy"],["Box2D.Dynamics.Contacts.b2Contact","Box2D.Collision.Shapes.b2Shape","Box2D.Collision.b2Collision","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Collision.Shapes.b2CircleShape"], "0.8.0", "0.8.1"

);