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

"package Box2D.Dynamics",/*{


import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Dynamics.Contacts.*
import Box2D.Dynamics.*
import Box2D.Common.Math.*
import Box2D.Common.*

import Box2D.Common.b2internal
use namespace b2internal*/


// Delegate of b2World.
/**
* @private
*/
"public class b2ContactManager",1,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(Box2D.Dynamics.b2ContactFilter,Box2D.Dynamics.Contacts.b2Contact);}, 

	"public function b2ContactManager",function b2ContactManager$(world/*:b2World*/) {
		this.m_world = world;
		this.m_contactCount = 0;
		this.m_contactFilter = Box2D.Dynamics.b2ContactFilter.b2_defaultFilter;
		this.m_contactListener = new Box2D.Dynamics.b2DefaultContactListener(world);
		this.m_contactFactory = new Box2D.Dynamics.Contacts.b2ContactFactory(this.m_allocator);
		this.m_broadPhase = new Box2D.Collision.b2DynamicTreeBroadPhase();
	},

	// This is a callback from the broadphase when two AABB proxies begin
	// to overlap. We create a b2Contact to manage the narrow phase.
	"public function AddPair",function AddPair(proxyUserDataA/*:**/, proxyUserDataB/*:**/)/*:void*/ {
		var fixtureA/*:b2Fixture*/ =as( proxyUserDataA,  Box2D.Dynamics.b2Fixture);
		var fixtureB/*:b2Fixture*/ =as( proxyUserDataB,  Box2D.Dynamics.b2Fixture);
		
		var bodyA/*:b2Body*/ = fixtureA.GetBody();
		var bodyB/*:b2Body*/ = fixtureB.GetBody();
		
		// Are the fixtures on the same body?
		if (bodyA == bodyB)
			return;
		
		// Does a contact already exist?
		var edge/*:b2ContactEdge*/ = bodyB.GetContactList();
		while (edge)
		{
			if (edge.other == bodyA)
			{
				var fA/*:b2Fixture*/ = edge.contact.GetFixtureA();
				var fB/*:b2Fixture*/ = edge.contact.GetFixtureB();
				if (fA == fixtureA && fB == fixtureB)
					return;
				if (fA == fixtureB && fB == fixtureA)
					return;
			}
			edge = edge.next;
		}
		
		//Does a joint override collision? Is at least one body dynamic?
		if (bodyB.ShouldCollide(bodyA) == false)
		{
			return;
		}
		
		// Check user filtering
		if (this.m_contactFilter && this.m_contactFilter.ShouldCollide(fixtureA, fixtureB) == false)
		{
			return;
		}
		
		// Call the factory.
		var c/*:b2Contact*/ = this.m_contactFactory.Create(fixtureA, fixtureB);
		
		// Contact creation may swap shapes.
		fixtureA = c.GetFixtureA();
		fixtureB = c.GetFixtureB();
		bodyA = fixtureA.m_body;
		bodyB = fixtureB.m_body;
		
		// Insert into the world.
		c.m_prev = null;
		c.m_next = this.m_world.m_contactList;
		if (this.m_world.m_contactList != null)
		{
			this.m_world.m_contactList.m_prev = c;
		}
		this.m_world.m_contactList = c;
		
		
		// Connect to island graph.
		
		// Connect to body A
		c.m_nodeA.contact = c;
		c.m_nodeA.other = bodyB;
		
		c.m_nodeA.prev = null;
		c.m_nodeA.next = bodyA.m_contactList;
		if (bodyA.m_contactList != null)
		{
			bodyA.m_contactList.prev = c.m_nodeA;
		}
		bodyA.m_contactList = c.m_nodeA;
		
		// Connect to body 2
		c.m_nodeB.contact = c;
		c.m_nodeB.other = bodyA;
		
		c.m_nodeB.prev = null;
		c.m_nodeB.next = bodyB.m_contactList;
		if (bodyB.m_contactList != null)
		{
			bodyB.m_contactList.prev = c.m_nodeB;
		}
		bodyB.m_contactList = c.m_nodeB;
		
		++this.m_contactCount;
		return;
		
	},

	"public function FindNewContacts",function FindNewContacts()/*:void*/
	{
		this.m_broadPhase.UpdatePairs($$bound(this,"AddPair"));
	},
	
	"static private const",{ s_evalCP/*:b2ContactPoint*/ :function(){return( new Box2D.Collision.b2ContactPoint());}},
	"public function Destroy",function Destroy(c/*:b2Contact*/)/* : void*/
	{
		
		var fixtureA/*:b2Fixture*/ = c.GetFixtureA();
		var fixtureB/*:b2Fixture*/ = c.GetFixtureB();
		var bodyA/*:b2Body*/ = fixtureA.GetBody();
		var bodyB/*:b2Body*/ = fixtureB.GetBody();
		
		if (this.m_contactListener && c.IsTouching())
		{
			this.m_contactListener.EndContact(c);
		}
		
		// Remove from the world.
		if (c.m_prev)
		{
			c.m_prev.m_next = c.m_next;
		}
		
		if (c.m_next)
		{
			c.m_next.m_prev = c.m_prev;
		}
		
		if (c == this.m_world.m_contactList)
		{
			this.m_world.m_contactList = c.m_next;
		}
		
		// Remove from body A
		if (c.m_nodeA.prev)
		{
			c.m_nodeA.prev.next = c.m_nodeA.next;
		}
		
		if (c.m_nodeA.next)
		{
			c.m_nodeA.next.prev = c.m_nodeA.prev;
		}
		
		if (c.m_nodeA == bodyA.m_contactList)
		{
			bodyA.m_contactList = c.m_nodeA.next;
		}
		
		// Remove from body 2
		if (c.m_nodeB.prev)
		{
			c.m_nodeB.prev.next = c.m_nodeB.next;
		}
		
		if (c.m_nodeB.next)
		{
			c.m_nodeB.next.prev = c.m_nodeB.prev;
		}
		
		if (c.m_nodeB == bodyB.m_contactList)
		{
			bodyB.m_contactList = c.m_nodeB.next;
		}
		
		// Call the factory.
		this.m_contactFactory.Destroy(c);
		--this.m_contactCount;
	},
	

	// This is the top level collision call for the time step. Here
	// all the narrow phase collision is processed for the world
	// contact list.
	"public function Collide",function Collide()/* : void*/
	{
		// Update awake contacts.
		var c/*:b2Contact*/ = this.m_world.m_contactList;
		while (c)
		{
			var fixtureA/*:b2Fixture*/ = c.GetFixtureA();
			var fixtureB/*:b2Fixture*/ = c.GetFixtureB();
			var bodyA/*:b2Body*/ = fixtureA.GetBody();
			var bodyB/*:b2Body*/ = fixtureB.GetBody();
			if (bodyA.IsAwake() == false && bodyB.IsAwake() == false)
			{
				c = c.GetNext();
				continue;
			}
			
			// Is this contact flagged for filtering?
			if (c.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_filterFlag)
			{
				// Should these bodies collide?
				if (bodyB.ShouldCollide(bodyA) == false)
				{
					var cNuke/*:b2Contact*/ = c;
					c = cNuke.GetNext();
					this.Destroy(cNuke);
					continue;
				}
				
				// Check user filtering.
				if (this.m_contactFilter && this.m_contactFilter.ShouldCollide(fixtureA, fixtureB) == false)
				{
					cNuke = c;
					c = cNuke.GetNext();
					this.Destroy(cNuke);
					continue;
				}
				
				// Clear the filtering flag
				c.m_flags &= ~Box2D.Dynamics.Contacts.b2Contact.e_filterFlag;
			}
			
			var proxyA/*:**/ = fixtureA.m_proxy;
			var proxyB/*:**/ = fixtureB.m_proxy;
			
			var overlap/*:Boolean*/ = this.m_broadPhase.TestOverlap(proxyA, proxyB);
			
			// Here we destroy contacts that cease to overlap in the broadphase
			if ( overlap == false)
			{
				cNuke = c;
				c = cNuke.GetNext();
				this.Destroy(cNuke);
				continue;
			}
			
			c.Update(this.m_contactListener);
			c = c.GetNext();
		}
	},

	
	"b2internal var",{ m_world/*:b2World*/:null},
	"b2internal var",{ m_broadPhase/*:IBroadPhase*/:null},
	
	"b2internal var",{ m_contactList/*:b2Contact*/:null},
	"b2internal var",{ m_contactCount/*:int*/:0},
	"b2internal var",{ m_contactFilter/*:b2ContactFilter*/:null},
	"b2internal var",{ m_contactListener/*:IContactListener*/:null},
	"b2internal var",{ m_contactFactory/*:b2ContactFactory*/:null},
	"b2internal var",{ m_allocator/*:**/:undefined},
];},[],["Box2D.Dynamics.b2ContactFilter","Box2D.Dynamics.b2DefaultContactListener","Box2D.Dynamics.Contacts.b2ContactFactory","Box2D.Collision.b2DynamicTreeBroadPhase","Box2D.Dynamics.b2Fixture","Box2D.Collision.b2ContactPoint","Box2D.Dynamics.Contacts.b2Contact"], "0.8.0", "0.8.1"

);