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
* The class manages contact between two shapes. A contact exists for each overlapping
* AABB in the broad-phase (except if filtered). Therefore a contact object may exist
* that has no contact points.
*/
"public class b2Contact",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Box2D.Common.b2Settings);},

	/**
	 * Get the contact manifold. Do not modify the manifold unless you understand the
	 * internals of Box2D
	 */
	"public function GetManifold",function GetManifold()/*:b2Manifold*/
	{
		return this.m_manifold;
	},
	
	/**
	 * Get the world manifold
	 */
	"public function GetWorldManifold",function GetWorldManifold(worldManifold/*:b2WorldManifold*/)/*:void*/
	{
		var bodyA/*:b2Body*/ = this.m_fixtureA.GetBody();
		var bodyB/*:b2Body*/ = this.m_fixtureB.GetBody();
		var shapeA/*:b2Shape*/ = this.m_fixtureA.GetShape();
		var shapeB/*:b2Shape*/ = this.m_fixtureB.GetShape();
		
		worldManifold.Initialize(this.m_manifold, bodyA.GetTransform(), shapeA.m_radius, bodyB.GetTransform(), shapeB.m_radius);
	},
	
	/**
	 * Is this contact touching.
	 */
	"public function IsTouching",function IsTouching()/*:Boolean*/
	{
		return (this.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag) == Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag; 
	},
	
	/**
	 * Does this contact generate TOI events for continuous simulation
	 */
	"public function IsContinuous",function IsContinuous()/*:Boolean*/
	{
		return (this.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag) == Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag; 
	},
	
	/**
	 * Change this to be a sensor or-non-sensor contact.
	 */
	"public function SetSensor",function SetSensor(sensor/*:Boolean*/)/*:void*/{
		if (sensor)
		{
			this.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag;
		}
		else
		{
			this.m_flags &= ~Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag;
		}
	},
	
	/**
	 * Is this contact a sensor?
	 */
	"public function IsSensor",function IsSensor()/*:Boolean*/{
		return (this.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag) == Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag;
	},
	
	/**
	 * Enable/disable this contact. This can be used inside the pre-solve
	 * contact listener. The contact is only disabled for the current
	 * time step (or sub-step in continuous collision).
	 */
	"public function SetEnabled",function SetEnabled(flag/*:Boolean*/)/*:void*/{
		if (flag)
		{
			this.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
		}
		else
		{
			this.m_flags &= ~Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
		}
	},
	
	/**
	 * Has this contact been disabled?
	 * @return
	 */
	"public function IsEnabled",function IsEnabled()/*:Boolean*/ {
		return (this.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag) == Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
	},
	
	/**
	* Get the next contact in the world's contact list.
	*/
	"public function GetNext",function GetNext()/*:b2Contact*/{
		return this.m_next;
	},
	
	/**
	* Get the first fixture in this contact.
	*/
	"public function GetFixtureA",function GetFixtureA()/*:b2Fixture*/
	{
		return this.m_fixtureA;
	},
	
	/**
	* Get the second fixture in this contact.
	*/
	"public function GetFixtureB",function GetFixtureB()/*:b2Fixture*/
	{
		return this.m_fixtureB;
	},
	
	/**
	 * Flag this contact for filtering. Filtering will occur the next time step.
	 */
	"public function FlagForFiltering",function FlagForFiltering()/*:void*/
	{
		this.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_filterFlag;
	},

	//--------------- Internals Below -------------------
	
	// m_flags
	// enum
	// This contact should not participate in Solve
	// The contact equivalent of sensors
	"static b2internal var",{ e_sensorFlag/*:uint*/		: 0x0001},
	// Generate TOI events.
	"static b2internal var",{ e_continuousFlag/*:uint*/	: 0x0002},
	// Used when crawling contact graph when forming islands.
	"static b2internal var",{ e_islandFlag/*:uint*/		: 0x0004},
	// Used in SolveTOI to indicate the cached toi value is still valid.
	"static b2internal var",{ e_toiFlag/*:uint*/		: 0x0008},
	// Set when shapes are touching
	"static b2internal var",{ e_touchingFlag/*:uint*/	: 0x0010},
	// This contact can be disabled (by user)
	"static b2internal var",{ e_enabledFlag/*:uint*/	: 0x0020},
	// This contact needs filtering because a fixture filter was changed.
	"static b2internal var",{ e_filterFlag/*:uint*/		: 0x0040},

	"public function b2Contact",function b2Contact$()
	{this.m_nodeA=this.m_nodeA();this.m_nodeB=this.m_nodeB();this.m_manifold=this.m_manifold();this.m_oldManifold=this.m_oldManifold();
		// Real work is done in Reset
	},
	
	/** @private */
	"b2internal function b2internal_Reset",function b2internal_Reset(fixtureA/*:b2Fixture = null*/, fixtureB/*:b2Fixture = null*/)/*:void*/
	{if(arguments.length<2){if(arguments.length<1){fixtureA = null;}fixtureB = null;}
		this.m_flags = Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
		
		if (!fixtureA || !fixtureB){
			this.m_fixtureA = null;
			this.m_fixtureB = null;
			return;
		}
		
		if (fixtureA.IsSensor() || fixtureB.IsSensor())
		{
			this.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag;
		}
		
		var bodyA/*:b2Body*/ = fixtureA.GetBody();
		var bodyB/*:b2Body*/ = fixtureB.GetBody();
		
		if (bodyA.GetType() != Box2D.Dynamics.b2Body.b2_dynamicBody || bodyA.IsBullet() || bodyB.GetType() != Box2D.Dynamics.b2Body.b2_dynamicBody || bodyB.IsBullet())
		{
			this.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag;
		}
		
		this.m_fixtureA = fixtureA;
		this.m_fixtureB = fixtureB;
		
		this.m_manifold.m_pointCount = 0;
		
		this.m_prev = null;
		this.m_next = null;
		
		this.m_nodeA.contact = null;
		this.m_nodeA.prev = null;
		this.m_nodeA.next = null;
		this.m_nodeA.other = null;
		
		this.m_nodeB.contact = null;
		this.m_nodeB.prev = null;
		this.m_nodeB.next = null;
		this.m_nodeB.other = null;
	},
	
	"b2internal function Update",function Update(listener/*:IContactListener*/)/* : void*/
	{
		// Swap old & new manifold
		var tManifold/*:b2Manifold*/ = this.m_oldManifold;
		this.m_oldManifold = this.m_manifold;
		this.m_manifold = tManifold;
		
		// Re-enable this contact
		this.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
		
		var touching/*:Boolean*/ = false;
		var wasTouching/*:Boolean*/ = (this.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag) == Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag;
		
		var bodyA/*:b2Body*/ = this.m_fixtureA.m_body;
		var bodyB/*:b2Body*/ = this.m_fixtureB.m_body;
		
		var aabbOverlap/*:Boolean*/ = this.m_fixtureA.m_aabb.TestOverlap(this.m_fixtureB.m_aabb);
		
		// Is this contat a sensor?
		if (this.m_flags  & Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag)
		{
			if (aabbOverlap)
			{
				var shapeA/*:b2Shape*/ = this.m_fixtureA.GetShape();
				var shapeB/*:b2Shape*/ = this.m_fixtureB.GetShape();
				var xfA/*:b2Transform*/ = bodyA.GetTransform();
				var xfB/*:b2Transform*/ = bodyB.GetTransform();
				touching = Box2D.Collision.Shapes.b2Shape.TestOverlap(shapeA, xfA, shapeB, xfB);
			}
			
			// Sensors don't generate manifolds
			this.m_manifold.m_pointCount = 0;
		}
		else
		{
			// Slow contacts don't generate TOI events.
			if (bodyA.GetType() != Box2D.Dynamics.b2Body.b2_dynamicBody || bodyA.IsBullet() || bodyB.GetType() != Box2D.Dynamics.b2Body.b2_dynamicBody || bodyB.IsBullet())
			{
				this.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag;
			}
			else
			{
				this.m_flags &= ~Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag;
			}
			
			if (aabbOverlap)
			{
				this.Evaluate();
				
				touching = this.m_manifold.m_pointCount > 0;
				
				// Match old contact ids to new contact ids and copy the
				// stored impulses to warm start the solver.
				for (var i/*:int*/ = 0; i < this.m_manifold.m_pointCount; ++i)
				{
					var mp2/*:b2ManifoldPoint*/ = this.m_manifold.m_points[i];
					mp2.m_normalImpulse = 0.0;
					mp2.m_tangentImpulse = 0.0;
					var id2/*:b2ContactID*/ = mp2.m_id;

					for (var j/*:int*/ = 0; j < this.m_oldManifold.m_pointCount; ++j)
					{
						var mp1/*:b2ManifoldPoint*/ = this.m_oldManifold.m_points[j];

						if (mp1.m_id.key == id2.key)
						{
							mp2.m_normalImpulse = mp1.m_normalImpulse;
							mp2.m_tangentImpulse = mp1.m_tangentImpulse;
							break;
						}
					}
				}

			}
			else
			{
				this.m_manifold.m_pointCount = 0;
			}
			if (touching != wasTouching)
			{
				bodyA.SetAwake(true);
				bodyB.SetAwake(true);
			}
		}
				
		if (touching)
		{
			this.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag;
		}
		else
		{
			this.m_flags &= ~Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag;
		}

		if (!wasTouching && touching && listener)
		{
			listener.BeginContact(this);
		}

		if (wasTouching && !touching  && listener)
		{
			listener.EndContact(this);
		}

		if ((this.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag) == 0 && touching && listener)
		{
			listener.PreSolve(this, this.m_oldManifold);
		}
	},

	//virtual ~b2Contact() {}

	"b2internal virtual function Evaluate",function Evaluate()/* : void*/{},
	
	"private static var",{ s_input/*:b2TOIInput*/ :function(){return( new Box2D.Collision.b2TOIInput());}},
	"b2internal function ComputeTOI",function ComputeTOI(sweepA/*:b2Sweep*/, sweepB/*:b2Sweep*/)/*:Number*/
	{
		$$private.s_input.proxyA.Set(this.m_fixtureA.GetShape());
		$$private.s_input.proxyB.Set(this.m_fixtureB.GetShape());
		$$private.s_input.sweepA = sweepA;
		$$private.s_input.sweepB = sweepB;
		$$private.s_input.tolerance = Box2D.Common.b2Settings.b2_linearSlop;
		return Box2D.Collision.b2TimeOfImpact.TimeOfImpact($$private.s_input);
	},
	
	"b2internal var",{ m_flags/*:uint*/:0},

	// World pool and list pointers.
	"b2internal var",{ m_prev/*:b2Contact*/:null},
	"b2internal var",{ m_next/*:b2Contact*/:null},

	// Nodes for connecting bodies.
	"b2internal var",{ m_nodeA/*:b2ContactEdge*/ :function(){return( new Box2D.Dynamics.Contacts.b2ContactEdge());}},
	"b2internal var",{ m_nodeB/*:b2ContactEdge*/ :function(){return( new Box2D.Dynamics.Contacts.b2ContactEdge());}},

	"b2internal var",{ m_fixtureA/*:b2Fixture*/:null},
	"b2internal var",{ m_fixtureB/*:b2Fixture*/:null},

	"b2internal var",{ m_manifold/*:b2Manifold*/ :function(){return( new Box2D.Collision.b2Manifold());}},
	"b2internal var",{ m_oldManifold/*:b2Manifold*/ :function(){return( new Box2D.Collision.b2Manifold());}},
	
	"b2internal var",{ m_toi/*:Number*/:NaN},
];},[],["Box2D.Dynamics.b2Body","Box2D.Collision.Shapes.b2Shape","Box2D.Collision.b2TOIInput","Box2D.Common.b2Settings","Box2D.Collision.b2TimeOfImpact","Box2D.Dynamics.Contacts.b2ContactEdge","Box2D.Collision.b2Manifold"], "0.8.0", "0.8.1"


);