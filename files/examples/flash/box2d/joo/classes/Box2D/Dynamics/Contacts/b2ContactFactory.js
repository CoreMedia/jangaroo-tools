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


import Box2D.Dynamics.*
import Box2D.Dynamics.Contacts.*
import Box2D.Collision.Shapes.*
import Box2D.Collision.*
import Box2D.Common.Math.*
import Box2D.Common.*

import Box2D.Common.b2internal
use namespace b2internal*/


//typedef b2Contact* b2ContactCreateFcn(b2Shape* shape1, b2Shape* shape2, b2BlockAllocator* allocator);
//typedef void b2ContactDestroyFcn(b2Contact* contact, b2BlockAllocator* allocator);



/**
 * This class manages creation and destruction of b2Contact objects.
 * @private
 */
"public class b2ContactFactory",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Collision.Shapes.b2Shape,Box2D.Dynamics.Contacts.b2PolygonContact,Box2D.Dynamics.Contacts.b2CircleContact,Box2D.Dynamics.Contacts.b2PolyAndCircleContact);},

	"function b2ContactFactory",function b2ContactFactory$(allocator/*:**/)
	{
		this.m_allocator$1 = allocator;
		this.InitializeRegisters();
	},
	
	"b2internal function AddType",function AddType(createFcn/*:Function*/, destroyFcn/*:Function*/, type1/*:int*/, type2/*:int*/)/* : void*/
	{
		//b2Settings.b2Assert(b2Shape.e_unknownShape < type1 && type1 < b2Shape.e_shapeTypeCount);
		//b2Settings.b2Assert(b2Shape.e_unknownShape < type2 && type2 < b2Shape.e_shapeTypeCount);
		
		this.m_registers$1[type1][type2].createFcn = createFcn;
		this.m_registers$1[type1][type2].destroyFcn = destroyFcn;
		this.m_registers$1[type1][type2].primary = true;
		
		if (type1 != type2)
		{
			this.m_registers$1[type2][type1].createFcn = createFcn;
			this.m_registers$1[type2][type1].destroyFcn = destroyFcn;
			this.m_registers$1[type2][type1].primary = false;
		}
	},
	"b2internal function InitializeRegisters",function InitializeRegisters()/* : void*/{
		this.m_registers$1 = new Array/*Vector.<b2ContactRegister> */(Box2D.Collision.Shapes.b2Shape.e_shapeTypeCount);
		for (var i/*:int*/ = 0; i < Box2D.Collision.Shapes.b2Shape.e_shapeTypeCount; i++){
			this.m_registers$1[i] = new Array/*b2ContactRegister*/(Box2D.Collision.Shapes.b2Shape.e_shapeTypeCount);
			for (var j/*:int*/ = 0; j < Box2D.Collision.Shapes.b2Shape.e_shapeTypeCount; j++){
				this.m_registers$1[i][j] = new Box2D.Dynamics.Contacts.b2ContactRegister();
			}
		}
		
		this.AddType(Box2D.Dynamics.Contacts.b2CircleContact.Create, Box2D.Dynamics.Contacts.b2CircleContact.Destroy, Box2D.Collision.Shapes.b2Shape.e_circleShape, Box2D.Collision.Shapes.b2Shape.e_circleShape);
		this.AddType(Box2D.Dynamics.Contacts.b2PolyAndCircleContact.Create, Box2D.Dynamics.Contacts.b2PolyAndCircleContact.Destroy, Box2D.Collision.Shapes.b2Shape.e_polygonShape, Box2D.Collision.Shapes.b2Shape.e_circleShape);
		this.AddType(Box2D.Dynamics.Contacts.b2PolygonContact.Create, Box2D.Dynamics.Contacts.b2PolygonContact.Destroy, Box2D.Collision.Shapes.b2Shape.e_polygonShape, Box2D.Collision.Shapes.b2Shape.e_polygonShape);
	},
	"public function Create",function Create(fixtureA/*:b2Fixture*/, fixtureB/*:b2Fixture*/)/*:b2Contact*/{
		var type1/*:int*/ = fixtureA.GetType();
		var type2/*:int*/ = fixtureB.GetType();
		
		//b2Settings.b2Assert(b2Shape.e_unknownShape < type1 && type1 < b2Shape.e_shapeTypeCount);
		//b2Settings.b2Assert(b2Shape.e_unknownShape < type2 && type2 < b2Shape.e_shapeTypeCount);
		
		var reg/*:b2ContactRegister*/ = this.m_registers$1[type1][type2];
		var poolReg/*:b2ContactRegister*/ = reg;
		if (!reg.primary) poolReg = this.m_registers$1[type2][type1];
		
		var c/*:b2Contact*/;
		if (poolReg.pool)
		{
			// Pop a contact off the pool
			c = poolReg.pool;
			poolReg.pool = c.m_next;
			poolReg.poolCount--;
		}else {
			var createFcn/*:Function*/ = reg.createFcn;
			if (createFcn == null) return null;
			c = createFcn(this.m_allocator$1);
		}
		
		if (reg.primary)
		{
			c.b2internal_Reset(fixtureA, fixtureB);
		}
		else
		{
			c.b2internal_Reset(fixtureB, fixtureA);
		}
		
		return c;
	},
	"public function Destroy",function Destroy(contact/*:b2Contact*/)/* : void*/{
		if (contact.m_manifold.m_pointCount > 0)
		{
			contact.m_fixtureA.m_body.SetAwake(true);
			contact.m_fixtureB.m_body.SetAwake(true);
		}
		
		var type1/*:int*/ = contact.m_fixtureA.GetType();
		var type2/*:int*/ = contact.m_fixtureB.GetType();
		
		//b2Settings.b2Assert(b2Shape.e_unknownShape < type1 && type1 < b2Shape.e_shapeTypeCount);
		//b2Settings.b2Assert(b2Shape.e_unknownShape < type2 && type2 < b2Shape.e_shapeTypeCount);
		
		var reg/*:b2ContactRegister*/ = this.m_registers$1[type1][type2];/*
		
		assert reg.primary;*/
		
		if (true)
		{
			reg.poolCount++;
			contact.m_next = reg.pool;
			reg.pool = contact;
		}
		
		var destroyFcn/*:Function*/ = reg.destroyFcn;
		destroyFcn(contact, this.m_allocator$1);
	},

	
	"private var",{ m_registers/*:Array*/:null}/*Vector.<b2ContactRegister> */,
	"private var",{ m_allocator/*:**/:undefined},
];},[],["Array","Box2D.Collision.Shapes.b2Shape","Box2D.Dynamics.Contacts.b2ContactRegister","Box2D.Dynamics.Contacts.b2CircleContact","Box2D.Dynamics.Contacts.b2PolyAndCircleContact","Box2D.Dynamics.Contacts.b2PolygonContact"], "0.8.0", "0.8.1"


);