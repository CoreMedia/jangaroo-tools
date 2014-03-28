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

import Box2D.Common.Math.*
import Box2D.Common.*
import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Dynamics.*
import Box2D.Dynamics.Contacts.*
import Box2D.Dynamics.Controllers.b2Controller
import Box2D.Dynamics.Joints.*
import flash.events.Event
import flash.events.EventDispatcher

import Box2D.Common.b2internal
use namespace b2internal*/


/**
* The world class manages all physics entities, dynamic simulation,
* and asynchronous queries. 
*/
"public class b2World extends flash.events.EventDispatcher",2,function($$private){var as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint,Box2D.Dynamics.b2Body,Box2D.Collision.Shapes.b2Shape,Number,Box2D.Dynamics.Contacts.b2Contact,Box2D.Common.b2Settings,Box2D.Dynamics.b2DebugDraw);},

	// Step events
	{Event:{name:"PreStep", type:"flash.events.Event"}},
	"public static var",{ PRESTEP/*:String*/ : "PreStep"},
	{Event:{name:"PostStep", type:"flash.events.Event"}},
	"public static var",{ POSTSTEP/*:String*/ : "PostStep"},
	
	// Contact based events
	{Event:{name:"BeginContact",type:"Box2D.Dynamics.b2ContactEvent"}},
	"public static var",{ BEGINCONTACT/*:String*/ : "BeginContact"},
	{Event:{name:"EndContact", type:"Box2D.Dynamics.b2ContactEvent"}},
	"public static var",{ ENDCONTACT/*:String*/ : "EndContact"},
	{Event:{name:"PreSolve", type:"Box2D.Dynamics.b2PreSolveEvent"}},
	"public static var",{ PRESOLVE/*:String*/ : "PreSolve"},
	{Event:{name:"PostSolve", type:"Box2D.Dynamics.b2PostSolveEvent"}},
	"public static var",{ POSTSOLVE/*:String*/ : "PostSolve"},
	
	// Add/Remove based events
	{Event:{name:"AddBody", type:"Box2D.Dynamics.b2BodyEvent"}},
	"public static var",{ ADDBODY/*:String*/ : "AddBody"},
	{Event:{name:"RemoveBody", type:"Box2D.Dynamics.b2BodyEvent"}},
	"public static var",{ REMOVEBODY/*:String*/ : "RemoveBody"},
	{Event:{name:"AddFixture", type:"Box2D.Dynamics.b2FixtureEvent"}},
	"public static var",{ ADDFIXTURE/*:String*/ : "AddFixture"},
	{Event:{name:"RemoveFixture", type:"Box2D.Dynamics.b2FixtureEvent"}},
	"public static var",{ REMOVEFIXTURE/*:String*/ : "RemoveJoint"},
	{Event:{name:"AddJoint", type:"Box2D.Dynamics.b2JointEvent"}},
	"public static var",{ ADDJOINT/*:String*/ : "AddJoint"},
	{Event:{name:"RemoveJoint", type:"Box2D.Dynamics.b2JointEvent"}},
	"public static var",{ REMOVEJOINT/*:String*/ : "RemoveJoint"},
	
	// Construct a world object.
	/**
	* @param gravity the world gravity vector.
	* @param doSleep improve performance by not simulating inactive bodies.
	*/
	"public function b2World",function b2World$(gravity/*:b2Vec2*/, doSleep/*:Boolean*/){this.super$2();this.s_stack$2=this.s_stack$2();this.m_contactSolver$2=this.m_contactSolver$2();this.m_island$2=this.m_island$2();this.m_preStepEvent$2=this.m_preStepEvent$2();this.m_postStepEvent$2=this.m_postStepEvent$2();this.m_addBodyEvent$2=this.m_addBodyEvent$2();this.m_removeBodyEvent$2=this.m_removeBodyEvent$2();this.m_addFixtureEvent=this.m_addFixtureEvent();this.m_removeFixtureEvent=this.m_removeFixtureEvent();this.m_addJointEvent$2=this.m_addJointEvent$2();this.m_removeJointEvent$2=this.m_removeJointEvent$2();
		
		this.m_destructionListener$2 = null;
		this.m_debugDraw$2 = null;
		
		this.m_bodyList = null;
		this.m_contactList = null;
		this.m_jointList$2 = null;
		this.m_controllerList$2 = null;
		
		this.m_bodyCount$2 = 0;
		this.m_jointCount$2 = 0;
		this.m_controllerCount$2 = 0;
		
		$$private.m_warmStarting = true;
		$$private.m_continuousPhysics = true;
		
		this.m_allowSleep$2 = doSleep;
		this.m_gravity$2 = gravity.Copy();
		
		this.m_flags = Box2D.Dynamics.b2World.e_clearForces;
		
		this.m_inv_dt0$2 = 0.0;
		
		this.m_contactManager = new Box2D.Dynamics.b2ContactManager(this);
		
		var bd/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
		this.m_groundBody = this.CreateBody(bd);
	},

	/**
	* Destruct the world. All physics entities are destroyed and all heap memory is released.
	*/
	//~b2World();

	/**
	* Register a destruction listener.
	*/
	"public function SetDestructionListener",function SetDestructionListener(listener/*:IDestructionListener*/)/* : void*/{
		this.m_destructionListener$2 = listener;
	},

	/**
	* Register a contact filter to provide specific control over collision.
	* Otherwise the default filter is used (b2_defaultFilter).
	*/
	"public function SetContactFilter",function SetContactFilter(filter/*:b2ContactFilter*/)/* : void*/{
		this.m_contactManager.m_contactFilter = filter;
	},

	/**
	* Register a contact event listener
	*/
	"public function SetContactListener",function SetContactListener(listener/*:IContactListener*/)/* : void*/{
		this.m_contactManager.m_contactListener = listener;
	},

	/**
	* Register a routine for debug drawing. The debug draw functions are called
	* inside the b2World::Step method, so make sure your renderer is ready to
	* consume draw commands when you call Step().
	*/
	"public function SetDebugDraw",function SetDebugDraw(debugDraw/*:b2DebugDraw*/)/* : void*/{
		this.m_debugDraw$2 = debugDraw;
	},
	
	/**
	 * Use the given object as a broadphase.
	 * The old broadphase will not be cleanly emptied.
	 * @warning It is not recommended you call this except immediately after constructing the world.
	 * @warning This function is locked during callbacks.
	 */
	"public function SetBroadPhase",function SetBroadPhase(broadPhase/*:IBroadPhase*/)/* : void*/ {
		var oldBroadPhase/*:IBroadPhase*/ = this.m_contactManager.m_broadPhase;
		this.m_contactManager.m_broadPhase = broadPhase;
		for (var b/*:b2Body*/ = this.m_bodyList; b; b = b.m_next)
		{
			for (var f/*:b2Fixture*/ = b.m_fixtureList; f; f = f.m_next)
			{
				f.m_proxy = broadPhase.CreateProxy(oldBroadPhase.GetFatAABB(f.m_proxy), f);
			}
		}
	},
	
	/**
	* Perform validation of internal data structures.
	*/
	"public function Validate",function Validate()/* : void*/
	{
		this.m_contactManager.m_broadPhase.Validate();
	},
	
	/**
	* Get the number of broad-phase proxies.
	*/
	"public function GetProxyCount",function GetProxyCount()/* : int*/
	{
		return this.m_contactManager.m_broadPhase.GetProxyCount();
	},
	
	/**
	* Create a rigid body given a definition. No reference to the definition
	* is retained.
	* @warning This function is locked during callbacks.
	*/
	"public function CreateBody",function CreateBody(def/*:b2BodyDef*/)/* : b2Body*/{
		
		this.CheckUnlocked();
		
		//void* mem = m_blockAllocator.Allocate(sizeof(b2Body));
		var b/*:b2Body*/ = new Box2D.Dynamics.b2Body(def, this);
		
		// Add to world doubly linked list.
		b.m_prev = null;
		b.m_next = this.m_bodyList;
		if (this.m_bodyList)
		{
			this.m_bodyList.m_prev = b;
		}
		this.m_bodyList = b;
		++this.m_bodyCount$2;
		
		// Events
		this.m_addBodyEvent$2.body = b;
		this.dispatchEvent(this.m_addBodyEvent$2);
		if (b.m_eventDispatcher) b.m_eventDispatcher.dispatchEvent(this.m_addBodyEvent$2);
		
		return b;
		
	},

	/**
	* Destroy a rigid body given a definition. No reference to the definition
	* is retained. This function is locked during callbacks.
	* @warning This automatically deletes all associated shapes and joints.
	* @warning This function is locked during callbacks.
	*/
	"public function DestroyBody",function DestroyBody(b/*:b2Body*/)/* : void*/{
		
		//b2Settings.b2Assert(m_bodyCount > 0);
		this.CheckUnlocked();
		
		if (!b.m_world)
			throw new Error("You cannot delete a body twice");
		b.m_world = null;
		
		// Events
		this.m_removeBodyEvent$2.body = b;
		if (b.m_eventDispatcher) b.m_eventDispatcher.dispatchEvent(this.m_removeBodyEvent$2);
		this.dispatchEvent(this.m_removeBodyEvent$2);
		
		// Delete the attached joints.
		var jn/*:b2JointEdge*/ = b.m_jointList;
		while (jn)
		{
			var jn0/*:b2JointEdge*/ = jn;
			jn = jn.next;
			
			if (this.m_destructionListener$2)
			{
				this.m_destructionListener$2.SayGoodbyeJoint(jn0.joint);
			}
			
			this.DestroyJoint(jn0.joint);
		}
		
		// Delete the attached contacts.
		var ce/*:b2ContactEdge*/ = b.m_contactList;
		while (ce)
		{
			var ce0/*:b2ContactEdge*/ = ce;
			ce = ce.next;
			this.m_contactManager.Destroy(ce0.contact);
		}
		b.m_contactList = null;
		
		// Delete the attached fixtures. This destroys broad-phase
		// proxies.
		var f/*:b2Fixture*/ = b.m_fixtureList;
		while (f)
		{
			var f0/*:b2Fixture*/ = f;
			f = f.m_next;
			
			if (this.m_destructionListener$2)
			{
				this.m_destructionListener$2.SayGoodbyeFixture(f0);
			}
			
			// Events
			this.m_removeFixtureEvent.fixture = f;
			if (b.m_eventDispatcher) b.m_eventDispatcher.dispatchEvent(this.m_removeFixtureEvent);
			this.dispatchEvent(this.m_removeFixtureEvent);
			
			f0.DestroyProxy(this.m_contactManager.m_broadPhase);
			f0.Destroy();
			//f0->~b2Fixture();
			//m_blockAllocator.Free(f0, sizeof(b2Fixture));
			
		}
		b.m_fixtureList = null;
		b.m_fixtureCount = 0;
		
		// Remove world body list.
		if (b.m_prev)
		{
			b.m_prev.m_next = b.m_next;
		}
		
		if (b.m_next)
		{
			b.m_next.m_prev = b.m_prev;
		}
		
		if (b == this.m_bodyList)
		{
			this.m_bodyList = b.m_next;
		}
		
		--this.m_bodyCount$2;
		//b->~b2Body();
		//m_blockAllocator.Free(b, sizeof(b2Body));
		
	},

	/**
	* Create a joint to constrain bodies together. No reference to the definition
	* is retained. This may cause the connected bodies to cease colliding.
	* @warning This function is locked during callbacks.
	*/
	"public function CreateJoint",function CreateJoint(def/*:b2JointDef*/)/* : b2Joint*/{
		
		//b2Settings.b2Assert(m_lock == false);
		
		var j/*:b2Joint*/ = Box2D.Dynamics.Joints.b2Joint.Create(def, null);
		
		// Connect to the world list.
		j.m_prev = null;
		j.m_next = this.m_jointList$2;
		if (this.m_jointList$2)
		{
			this.m_jointList$2.m_prev = j;
		}
		this.m_jointList$2 = j;
		++this.m_jointCount$2;
		
		// Connect to the bodies' doubly linked lists.
		j.m_edgeA.joint = j;
		j.m_edgeA.other = j.m_bodyB;
		j.m_edgeA.prev = null;
		j.m_edgeA.next = j.m_bodyA.m_jointList;
		if (j.m_bodyA.m_jointList) j.m_bodyA.m_jointList.prev = j.m_edgeA;
		j.m_bodyA.m_jointList = j.m_edgeA;
		
		j.m_edgeB.joint = j;
		j.m_edgeB.other = j.m_bodyA;
		j.m_edgeB.prev = null;
		j.m_edgeB.next = j.m_bodyB.m_jointList;
		if (j.m_bodyB.m_jointList) j.m_bodyB.m_jointList.prev = j.m_edgeB;
		j.m_bodyB.m_jointList = j.m_edgeB;
		
		var bodyA/*:b2Body*/ = def.bodyA;
		var bodyB/*:b2Body*/ = def.bodyB;
		
		// If the joint prevents collisions, then flag any contacts for filtering.
		if (def.collideConnected == false )
		{
			var edge/*:b2ContactEdge*/ = bodyB.GetContactList();
			while (edge)
			{
				if (edge.other == bodyA)
				{
					// Flag the contact for filtering at the next time step (where either
					// body is awake).
					edge.contact.FlagForFiltering();
				}

				edge = edge.next;
			}
		}
		
		// Events
		this.m_addJointEvent$2.joint = j;
		this.dispatchEvent(this.m_addJointEvent$2);
		if (bodyA.m_eventDispatcher) bodyA.m_eventDispatcher.dispatchEvent(this.m_addJointEvent$2);
		if (bodyB.m_eventDispatcher) bodyB.m_eventDispatcher.dispatchEvent(this.m_addJointEvent$2);
		
		// Note: creating a joint doesn't wake the bodies.
		
		return j;
		
	},

	/**
	* Destroy a joint. This may cause the connected bodies to begin colliding.
	* @warning This function is locked during callbacks.
	*/
	"public function DestroyJoint",function DestroyJoint(j/*:b2Joint*/)/* : void*/{
		
		//b2Settings.b2Assert(m_lock == false);
		this.CheckUnlocked();
		
		var bodyA/*:b2Body*/ = j.m_bodyA;
		var bodyB/*:b2Body*/ = j.m_bodyB;
		
		if (!j.m_bodyA)
			throw new Error("You cannot delete a joint twice.");
		j.m_bodyA = null;
		j.m_bodyB = null;
		
		// Events
		this.m_removeJointEvent$2.joint = j;
		if (bodyB.m_eventDispatcher) bodyB.m_eventDispatcher.dispatchEvent(this.m_removeJointEvent$2);
		if (bodyA.m_eventDispatcher) bodyA.m_eventDispatcher.dispatchEvent(this.m_removeJointEvent$2);
		this.dispatchEvent(this.m_removeJointEvent$2);
		
		var collideConnected/*:Boolean*/ = j.m_collideConnected;
		
		// Remove from the doubly linked list.
		if (j.m_prev)
		{
			j.m_prev.m_next = j.m_next;
		}
		
		if (j.m_next)
		{
			j.m_next.m_prev = j.m_prev;
		}
		
		if (j == this.m_jointList$2)
		{
			this.m_jointList$2 = j.m_next;
		}
		
		// Disconnect from island graph.
		
		// Wake up connected bodies.
		bodyA.SetAwake(true);
		bodyB.SetAwake(true);
		
		// Remove from body 1.
		if (j.m_edgeA.prev)
		{
			j.m_edgeA.prev.next = j.m_edgeA.next;
		}
		
		if (j.m_edgeA.next)
		{
			j.m_edgeA.next.prev = j.m_edgeA.prev;
		}
		
		if (j.m_edgeA == bodyA.m_jointList)
		{
			bodyA.m_jointList = j.m_edgeA.next;
		}
		
		j.m_edgeA.prev = null;
		j.m_edgeA.next = null;
		
		// Remove from body 2
		if (j.m_edgeB.prev)
		{
			j.m_edgeB.prev.next = j.m_edgeB.next;
		}
		
		if (j.m_edgeB.next)
		{
			j.m_edgeB.next.prev = j.m_edgeB.prev;
		}
		
		if (j.m_edgeB == bodyB.m_jointList)
		{
			bodyB.m_jointList = j.m_edgeB.next;
		}
		
		j.m_edgeB.prev = null;
		j.m_edgeB.next = null;
		
		Box2D.Dynamics.Joints.b2Joint.Destroy(j, null);
		
		//b2Settings.b2Assert(m_jointCount > 0);
		--this.m_jointCount$2;
		
		// If the joint prevents collisions, then flag any contacts for filtering.
		if (collideConnected == false)
		{
			var edge/*:b2ContactEdge*/ = bodyB.GetContactList();
			while (edge)
			{
				if (edge.other == bodyA)
				{
					// Flag the contact for filtering at the next time step (where either
					// body is awake).
					edge.contact.FlagForFiltering();
				}

				edge = edge.next;
			}
		}
		
	},
	
	/**
	 * Add a controller to the world list
	 */
	"public function AddController",function AddController(c/*:b2Controller*/)/* : b2Controller*/
	{
		c.m_next = this.m_controllerList$2;
		c.m_prev = null;
		this.m_controllerList$2 = c;
		
		c.m_world = this;
		
		this.m_controllerCount$2++;
		
		return c;
	},
	
	"public function RemoveController",function RemoveController(c/*:b2Controller*/)/* : void*/
	{
		//TODO: Remove bodies from controller
		if (c.m_prev)
			c.m_prev.m_next = c.m_next;
		if (c.m_next)
			c.m_next.m_prev = c.m_prev;
		if (this.m_controllerList$2 == c)
			this.m_controllerList$2 = c.m_next;
			
		this.m_controllerCount$2--;
	},
	
	/**
	* Enable/disable warm starting. For testing.
	*/
	"public function SetWarmStarting",function SetWarmStarting(flag/*: Boolean*/)/* : void*/ { $$private.m_warmStarting = flag; },

	/**
	* Enable/disable continuous physics. For testing.
	*/
	"public function SetContinuousPhysics",function SetContinuousPhysics(flag/*: Boolean*/)/* : void*/ { $$private.m_continuousPhysics = flag; },
	
	/**
	* Get the number of bodies.
	*/
	"public function GetBodyCount",function GetBodyCount()/* : int*/
	{
		return this.m_bodyCount$2;
	},
	
	/**
	* Get the number of joints.
	*/
	"public function GetJointCount",function GetJointCount()/* : int*/
	{
		return this.m_jointCount$2;
	},
	
	/**
	* Get the number of contacts (each may have 0 or more contact points).
	*/
	"public function GetContactCount",function GetContactCount()/* : int*/
	{
		return this.m_contactManager.m_contactCount;
	},
	
	/**
	* Change the global gravity vector.
	*/
	"public function SetGravity",function SetGravity(gravity/*: b2Vec2*/)/*: void*/
	{
		this.m_gravity$2 = gravity;
	},

	/**
	* Get the global gravity vector.
	*/
	"public function GetGravity",function GetGravity()/*:b2Vec2*/{
		return this.m_gravity$2;
	},

	/**
	* The world provides a single static ground body with no collision shapes.
	* You can use this to simplify the creation of joints and static shapes.
	*/
	"public function GetGroundBody",function GetGroundBody()/* : b2Body*/{
		return this.m_groundBody;
	},

	"private static var",{ s_timestep2/*:b2TimeStep*/ :function(){return( new Box2D.Dynamics.b2TimeStep());}},
	/**
	* Take a time step. This performs collision detection, integration,
	* and constraint solution.
	* @param timeStep the amount of time to simulate, this should not vary.
	* @param velocityIterations for the velocity constraint solver.
	* @param positionIterations for the position constraint solver.
	*/
	"public function Step",function Step(dt/*:Number*/, velocityIterations/*:int*/, positionIterations/*:int*/)/* : void*/ {
		
		this.dispatchEvent(this.m_preStepEvent$2);
		
		if (this.m_flags & Box2D.Dynamics.b2World.e_newFixture)
		{
			this.m_contactManager.FindNewContacts();
			this.m_flags &= ~Box2D.Dynamics.b2World.e_newFixture;
		}
		
		this.m_flags |= Box2D.Dynamics.b2World.e_locked;
		
		var step/*:b2TimeStep*/ = $$private.s_timestep2;
		step.dt = dt;
		step.velocityIterations = velocityIterations;
		step.positionIterations = positionIterations;
		if (dt > 0.0)
		{
			step.inv_dt = 1.0 / dt;
		}
		else
		{
			step.inv_dt = 0.0;
		}
		
		step.dtRatio = this.m_inv_dt0$2 * dt;
		
		step.warmStarting = $$private.m_warmStarting;
		
		// Update contacts.
		this.m_contactManager.Collide();
		
		// Integrate velocities, solve velocity constraints, and integrate positions.
		if (step.dt > 0.0)
		{
			this.Solve(step);
		}
		
		// Handle TOI events.
		if ($$private.m_continuousPhysics && step.dt > 0.0)
		{
			this.SolveTOI(step);
		}
		
		if (step.dt > 0.0)
		{
			this.m_inv_dt0$2 = step.inv_dt;
		}
		if (this.m_flags & Box2D.Dynamics.b2World.e_clearForces)
		{
			this.ClearForces();
		}
		this.m_flags &= ~Box2D.Dynamics.b2World.e_locked;
		
		this.dispatchEvent(this.m_postStepEvent$2);
	},
	
	/**
	 * Call this after you are done with time steps to clear the forces. You normally
	 * call this after each call to Step, unless you are performing sub-steps. By default,
	 * forces will be automatically cleared, so you don't need to call this function.
	 * @see SetAutoClearForces
	 */
	"public function ClearForces",function ClearForces()/* : void*/
	{
		for (var body/*:b2Body*/ = this.m_bodyList; body; body = body.m_next)
		{
			body.m_force.SetZero();
			body.m_torque = 0.0;
		}
	},
	
	"static private var",{ s_xf/*:b2Transform*/ :function(){return( new Box2D.Common.Math.b2Transform());}},
	/**
	 * Call this to draw shapes and other debug draw data.
	 */
	"public function DrawDebugData",function DrawDebugData()/* : void*/{
		
		if (this.m_debugDraw$2 == null)
		{
			return;
		}
		
		this.m_debugDraw$2.m_sprite.graphics.clear();
		
		var flags/*:uint*/ = this.m_debugDraw$2.GetFlags();
		
		var i/*:int*/;
		var b/*:b2Body*/;
		var f/*:b2Fixture*/;
		var s/*:b2Shape*/;
		var j/*:b2Joint*/;
		var bp/*:IBroadPhase*/;
		var invQ/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2;
		var x1/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2;
		var x2/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2;
		var xf/*:b2Transform*/;
		var b1/*:b2AABB*/ = new Box2D.Collision.b2AABB();
		var b2/*:b2AABB*/ = new Box2D.Collision.b2AABB();
		var vs/*:Array*/ = [new Box2D.Common.Math.b2Vec2(), new Box2D.Common.Math.b2Vec2(), new Box2D.Common.Math.b2Vec2(), new Box2D.Common.Math.b2Vec2()];
		
		// Store color here and reuse, to reduce allocations
		var color/*:b2Color*/ = new Box2D.Common.b2Color(0, 0, 0);
			
		if (flags & Box2D.Dynamics.b2DebugDraw.e_shapeBit)
		{
			for (b = this.m_bodyList; b; b = b.m_next)
			{
				xf = b.m_xf;
				for (f = b.GetFixtureList(); f; f = f.m_next)
				{
					s = f.GetShape();
					if (b.IsActive() == false)
					{
						color.Set(0.5, 0.5, 0.3);
						this.DrawShape(s, xf, color);
					}
					else if (b.GetType() == Box2D.Dynamics.b2Body.b2_staticBody)
					{
						color.Set(0.5, 0.9, 0.5);
						this.DrawShape(s, xf, color);
					}
					else if (b.GetType() == Box2D.Dynamics.b2Body.b2_kinematicBody)
					{
						color.Set(0.5, 0.5, 0.9);
						this.DrawShape(s, xf, color);
					}
					else if (b.IsAwake() == false)
					{
						color.Set(0.6, 0.6, 0.6);
						this.DrawShape(s, xf, color);
					}
					else
					{
						color.Set(0.9, 0.7, 0.7);
						this.DrawShape(s, xf, color);
					}
				}
			}
		}
		
		if (flags & Box2D.Dynamics.b2DebugDraw.e_jointBit)
		{
			for (j = this.m_jointList$2; j; j = j.m_next)
			{
				this.DrawJoint(j);
			}
		}
		
		if (flags & Box2D.Dynamics.b2DebugDraw.e_controllerBit)
		{
			for (var c/*:b2Controller*/ = this.m_controllerList$2; c; c = c.m_next)
			{
				c.Draw(this.m_debugDraw$2);
			}
		}
		
		if (flags & Box2D.Dynamics.b2DebugDraw.e_pairBit)
		{
			color.Set(0.3, 0.9, 0.9);
			for (var contact/*:b2Contact*/ = this.m_contactManager.m_contactList; contact; contact = contact.GetNext())
			{
				var fixtureA/*:b2Fixture*/ = contact.GetFixtureA();
				var fixtureB/*:b2Fixture*/ = contact.GetFixtureB();

				var cA/*:b2Vec2*/ = fixtureA.GetAABB().GetCenter();
				var cB/*:b2Vec2*/ = fixtureB.GetAABB().GetCenter();

				this.m_debugDraw$2.DrawSegment(cA, cB, color);
			}
		}
		
		if (flags & Box2D.Dynamics.b2DebugDraw.e_aabbBit)
		{
			bp = this.m_contactManager.m_broadPhase;
			
			vs = [new Box2D.Common.Math.b2Vec2(),new Box2D.Common.Math.b2Vec2(),new Box2D.Common.Math.b2Vec2(),new Box2D.Common.Math.b2Vec2()];
			
			for (b= this.m_bodyList; b; b = b.GetNext())
			{
				if (b.IsActive() == false)
				{
					continue;
				}
				for (f = b.GetFixtureList(); f; f = f.GetNext())
				{
					var aabb/*:b2AABB*/ = bp.GetFatAABB(f.m_proxy);
					vs[0].Set(aabb.lowerBound.x, aabb.lowerBound.y);
					vs[1].Set(aabb.upperBound.x, aabb.lowerBound.y);
					vs[2].Set(aabb.upperBound.x, aabb.upperBound.y);
					vs[3].Set(aabb.lowerBound.x, aabb.upperBound.y);

					this.m_debugDraw$2.DrawPolygon(vs, 4, color);
				}
			}
		}
		
		if (flags & Box2D.Dynamics.b2DebugDraw.e_centerOfMassBit)
		{
			for (b = this.m_bodyList; b; b = b.m_next)
			{
				xf = $$private.s_xf;
				xf.R = b.m_xf.R;
				xf.position = b.GetWorldCenter();
				this.m_debugDraw$2.DrawTransform(xf);
			}
		}
	},

	/**
	 * Query the world for all fixtures that potentially overlap the
	 * provided AABB.
	 * @param callback a user implemented callback class. It should match signature
	 * <code>function Callback(fixture:b2Fixture):Boolean</code>
	 * Return true to continue to the next fixture.
	 * @param aabb the query box.
	 */
	"public function QueryAABB",function QueryAABB(callback/*:Function*/, aabb/*:b2AABB*/)/*:void*/
	{
		var broadPhase/*:IBroadPhase*/ = this.m_contactManager.m_broadPhase;
		function WorldQueryWrapper(proxy/*:**/)/*:Boolean*/
		{
			return callback(broadPhase.GetUserData(proxy));
		}
		broadPhase.Query(WorldQueryWrapper, aabb);
	},
	/**
	 * Query the world for all fixtures that precisely overlap the
	 * provided transformed shape.
	 * @param callback a user implemented callback class. It should match signature
	 * <code>function Callback(fixture:b2Fixture):Boolean</code>
	 * Return true to continue to the next fixture.
	 * @asonly
	 */
	"public function QueryShape",function QueryShape(callback/*:Function*/, shape/*:b2Shape*/, transform/*:b2Transform = null*/)/*:void*/
	{if(arguments.length<3){transform = null;}
		if (transform == null)
		{
			transform = new Box2D.Common.Math.b2Transform();
			transform.SetIdentity();
		}
		var broadPhase/*:IBroadPhase*/ = this.m_contactManager.m_broadPhase;
		function WorldQueryWrapper(proxy/*:**/)/*:Boolean*/
		{
			var fixture/*:b2Fixture*/ =as( broadPhase.GetUserData(proxy),  Box2D.Dynamics.b2Fixture);
			if(Box2D.Collision.Shapes.b2Shape.TestOverlap(shape, transform, fixture.GetShape(), fixture.GetBody().GetTransform()))
				return callback(fixture);
			return true;
		}
		var aabb/*:b2AABB*/ = new Box2D.Collision.b2AABB();
		shape.ComputeAABB(aabb, transform);
		broadPhase.Query(WorldQueryWrapper, aabb);
	},
	
	/**
	 * Query the world for all fixtures that contain a point.
	 * @param callback a user implemented callback class. It should match signature
	 * <code>function Callback(fixture:b2Fixture):Boolean</code>
	 * Return true to continue to the next fixture.
	 * @asonly
	 */
	"public function QueryPoint",function QueryPoint(callback/*:Function*/, p/*:b2Vec2*/)/*:void*/
	{
		var broadPhase/*:IBroadPhase*/ = this.m_contactManager.m_broadPhase;
		function WorldQueryWrapper(proxy/*:**/)/*:Boolean*/
		{
			var fixture/*:b2Fixture*/ =as( broadPhase.GetUserData(proxy),  Box2D.Dynamics.b2Fixture);
			if(fixture.TestPoint(p))
				return callback(fixture);
			return true;
		}
		// Make a small box.
		var aabb/*:b2AABB*/ = new Box2D.Collision.b2AABB();
		aabb.lowerBound.Set(p.x - Box2D.Common.b2Settings.b2_linearSlop, p.y - Box2D.Common.b2Settings.b2_linearSlop);
		aabb.upperBound.Set(p.x + Box2D.Common.b2Settings.b2_linearSlop, p.y + Box2D.Common.b2Settings.b2_linearSlop);
		broadPhase.Query(WorldQueryWrapper, aabb);
	},
	
	/**
	 * Ray-cast the world for all fixtures in the path of the ray. Your callback
	 * Controls whether you get the closest point, any point, or n-points
	 * The ray-cast ignores shapes that contain the starting point
	 * @param callback A callback function which must be of signature:
	 * <code>function Callback(fixture:b2Fixture,    // The fixture hit by the ray
	 * point:b2Vec2,         // The point of initial intersection
	 * normal:b2Vec2,        // The normal vector at the point of intersection
	 * fraction:Number       // The fractional length along the ray of the intersection
	 * ):Number
	 * </code>
	 * Callback should return the new length of the ray as a fraction of the original length.
	 * By returning -1, you ignore this fixture and continue
	 * By returning 0, you immediately terminate.
	 * By returning 1, you continue wiht the original ray.
	 * By returning the current fraction, you proceed to find the closest point.
	 * @param point1 the ray starting point
	 * @param point2 the ray ending point
	 * @return -1 to filter, 0 to terminate, fraction to clip, 1 to continue.
	 */
	"public function RayCast",function RayCast(callback/*:Function*/, point1/*:b2Vec2*/, point2/*:b2Vec2*/)/*:void*/
	{
		var broadPhase/*:IBroadPhase*/ = this.m_contactManager.m_broadPhase;
		var output/*:b2RayCastOutput*/ = new Box2D.Collision.b2RayCastOutput;
		function RayCastWrapper(input/*:b2RayCastInput*/, proxy/*:**/)/*:Number*/
		{
			var userData/*:**/ = broadPhase.GetUserData(proxy);
			var fixture/*:b2Fixture*/ =as( userData,  Box2D.Dynamics.b2Fixture);
			var hit/*:Boolean*/ = fixture.RayCast(output, input);
			if (hit)
			{
				var fraction/*:Number*/ = output.fraction;
				var point/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(
					(1.0 - fraction) * point1.x + fraction * point2.x,
					(1.0 - fraction) * point1.y + fraction * point2.y);
				return callback(fixture, point, output.normal, fraction);
			}
			return input.maxFraction;
		}
		var input/*:b2RayCastInput*/ = new Box2D.Collision.b2RayCastInput(point1, point2);
		broadPhase.RayCast(RayCastWrapper, input);
	},
	
	"public function RayCastOne",function RayCastOne(point1/*:b2Vec2*/, point2/*:b2Vec2*/)/*:b2Fixture*/
	{
		var result/*:b2Fixture*/;
		function RayCastOneWrapper(fixture/*:b2Fixture*/, point/*:b2Vec2*/, normal/*:b2Vec2*/, fraction/*:Number*/)/*:Number*/
		{
			result = fixture;
			return fraction;
		}
		this.RayCast(RayCastOneWrapper, point1, point2);
		return result;
	},
	
	"public function RayCastAll",function RayCastAll(point1/*:b2Vec2*/, point2/*:b2Vec2*/)/*:Array*//*b2Fixture*/
	{
		var result/*:Array*//*b2Fixture*/ = new Array/*b2Fixture*/();
		function RayCastAllWrapper(fixture/*:b2Fixture*/, point/*:b2Vec2*/, normal/*:b2Vec2*/, fraction/*:Number*/)/*:Number*/
		{
			result[result.length] = fixture;
			return 1;
		}
		this.RayCast(RayCastAllWrapper, point1, point2);
		return result;
	},

	/**
	* Get the world body list. With the returned body, use b2Body::GetNext to get
	* the next body in the world list. A NULL body indicates the end of the list.
	* @return the head of the world body list.
	*/
	"public function GetBodyList",function GetBodyList()/* : b2Body*/{
		return this.m_bodyList;
	},

	/**
	* Get the world joint list. With the returned joint, use b2Joint::GetNext to get
	* the next joint in the world list. A NULL joint indicates the end of the list.
	* @return the head of the world joint list.
	*/
	"public function GetJointList",function GetJointList()/* : b2Joint*/{
		return this.m_jointList$2;
	},

	/**
	 * Get the world contact list. With the returned contact, use b2Contact::GetNext to get
	 * the next contact in the world list. A NULL contact indicates the end of the list.
	 * @return the head of the world contact list.
	 * @warning contacts are 
	 */
	"public function GetContactList",function GetContactList()/*:b2Contact*/
	{
		return this.m_contactList;
	},
	
	/**
	 * Is the world locked (in the middle of a time step).
	 */
	"public function IsLocked",function IsLocked()/*:Boolean*/
	{
		return (this.m_flags & Box2D.Dynamics.b2World.e_locked) > 0;
	},
	
	/**
	 * Throws if the world is locked
	 */
	"b2internal function CheckUnlocked",function CheckUnlocked()/*:void*/
	{
		if (this.IsLocked())
			throw new Error("You cannot call this method while the world is locked");
	},
	
	/**
	 * Set flag to control automatic clearing of forces after each time step.
	 */
	"public function SetAutoClearForces",function SetAutoClearForces(flag/*:Boolean*/)/*:void*/
	{
		if (flag)
		{
			this.m_flags |= Box2D.Dynamics.b2World.e_clearForces;
		}else {
			this.m_flags &= ~Box2D.Dynamics.b2World.e_clearForces;
		}
	},
	
	/**
	 * Get the flag that controls automatic clearing of forces after each time step.
	 */
	"public function GetAutoClearForces",function GetAutoClearForces()/*:Boolean*/
	{
		return (this.m_flags & Box2D.Dynamics.b2World.e_clearForces) == Box2D.Dynamics.b2World.e_clearForces;
	},

	//--------------- Internals Below -------------------
	// Internal yet public to make life easier.

	// Find islands, integrate and solve constraints, solve position constraints
	"private var",{ s_stack/*:Array*//*b2Body*/ :function(){return( new Array/*b2Body*/());}},
	"b2internal function Solve",function Solve(step/*:b2TimeStep*/)/* : void*/{
		var b/*:b2Body*/;
		
		// Step all controllers
		for(var controller/*:b2Controller*/ = this.m_controllerList$2;controller;controller=controller.m_next)
		{
			controller.Step(step);
		}
		
		// Size the island for the worst case.
		var island/*:b2Island*/ = this.m_island$2;
		island.Initialize(this.m_bodyCount$2, this.m_contactManager.m_contactCount, this.m_jointCount$2, null, this.m_contactManager.m_contactListener, this.m_contactSolver$2);
		
		// Clear all the island flags.
		for (b = this.m_bodyList; b; b = b.m_next)
		{
			b.m_flags &= ~Box2D.Dynamics.b2Body.e_islandFlag;
		}
		for (var c/*:b2Contact*/ = this.m_contactList; c; c = c.m_next)
		{
			c.m_flags &= ~Box2D.Dynamics.Contacts.b2Contact.e_islandFlag;
		}
		for (var j/*:b2Joint*/ = this.m_jointList$2; j; j = j.m_next)
		{
			j.m_islandFlag = false;
		}
		
		// Build and simulate all awake islands.
		var stackSize/*:int*/ = this.m_bodyCount$2;
		//b2Body** stack = (b2Body**)m_stackAllocator.Allocate(stackSize * sizeof(b2Body*));
		var stack/*:Array*//*b2Body*/ = this.s_stack$2;
		for (var seed/*:b2Body*/ = this.m_bodyList; seed; seed = seed.m_next)
		{
			if (seed.m_flags & Box2D.Dynamics.b2Body.e_islandFlag )
			{
				continue;
			}
			
			if (seed.IsAwake() == false || seed.IsActive() == false)
			{
				continue;
			}
			
			// The seed can be dynamic or kinematic.
			if (seed.GetType() == Box2D.Dynamics.b2Body.b2_staticBody)
			{
				continue;
			}
			
			// Reset island and stack.
			island.Clear();
			var stackCount/*:int*/ = 0;
			stack[stackCount++] = seed;
			seed.m_flags |= Box2D.Dynamics.b2Body.e_islandFlag;
			
			// Perform a depth first search (DFS) on the constraint graph.
			while (stackCount > 0)
			{
				// Grab the next body off the stack and add it to the island.
				b = stack[--stackCount];
				//b2Assert(b.IsActive() == true);
				island.AddBody(b);
				
				// Make sure the body is awake.
				b.SetAwake(true);
				
				// To keep islands as small as possible, we don't
				// propagate islands across static bodies.
				if (b.GetType() == Box2D.Dynamics.b2Body.b2_staticBody)
				{
					continue;
				}
				
				var other/*:b2Body*/;
				// Search all contacts connected to this body.
				for (var ce/*:b2ContactEdge*/ = b.m_contactList; ce; ce = ce.next)
				{
					var contact/*:b2Contact*/ = ce.contact;
					// Has this contact already been added to an island?
					if (contact.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_islandFlag)
					{
						continue;
					}
					
					// Is this contact solid and touching?
					if (contact.IsSensor() == true ||
						contact.IsEnabled() == false ||
						contact.IsTouching() == false)
					{
						continue;
					}
					
					island.AddContact(contact);
					contact.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_islandFlag;
					
					//var other:b2Body = ce.other;
					other = ce.other;
					
					// Was the other body already added to this island?
					if (other.m_flags & Box2D.Dynamics.b2Body.e_islandFlag)
					{
						continue;
					}
					
					//b2Settings.b2Assert(stackCount < stackSize);
					stack[stackCount++] = other;
					other.m_flags |= Box2D.Dynamics.b2Body.e_islandFlag;
				}
				
				// Search all joints connect to this body.
				for (var jn/*:b2JointEdge*/ = b.m_jointList; jn; jn = jn.next)
				{
					var joint/*:b2Joint*/ = jn.joint;
					if (joint.m_islandFlag == true)
					{
						continue;
					}
					
					other = jn.other;
					
					// Don't simulate joints connected to inactive bodies.
					if (other.IsActive() == false)
					{
						continue;
					}
					
					island.AddJoint(joint);
					joint.m_islandFlag = true;
					
					if (other.m_flags & Box2D.Dynamics.b2Body.e_islandFlag)
					{
						continue;
					}
					
					//b2Settings.b2Assert(stackCount < stackSize);
					stack[stackCount++] = other;
					other.m_flags |= Box2D.Dynamics.b2Body.e_islandFlag;
				}
			}
			island.Solve(step, this.m_gravity$2, this.m_allowSleep$2);
			
			// Post solve cleanup.
			for (var i/*:int*/ = 0; i < island.m_bodyCount; ++i)
			{
				// Allow static bodies to participate in other islands.
				b = island.m_bodies[i];
				if (b.GetType() == Box2D.Dynamics.b2Body.b2_staticBody)
				{
					b.m_flags &= ~Box2D.Dynamics.b2Body.e_islandFlag;
				}
			}
		}
		
		//m_stackAllocator.Free(stack);
		for (i = 0; i < stack.length;++i)
		{
			if (!stack[i]) break;
			stack[i] = null;
		}
		
		// Synchronize fixutres, check for out of range bodies.
		for (b = this.m_bodyList; b; b = b.m_next)
		{
			// If a body was not in an island then it did not move.
			if ((b.m_flags & Box2D.Dynamics.b2Body.e_islandFlag) == 0)
			{
				continue;
			}
			
			if (b.GetType() == Box2D.Dynamics.b2Body.b2_staticBody)
			{
				continue;
			}
			
			// Update fixtures (for broad-phase).
			b.SynchronizeFixtures();
		}
		
		// Look for new contacts.
		this.m_contactManager.FindNewContacts();
		
	},
	
	"private static var",{ s_backupA/*:b2Sweep*/ :function(){return( new Box2D.Common.Math.b2Sweep());}},
	"private static var",{ s_backupB/*:b2Sweep*/ :function(){return( new Box2D.Common.Math.b2Sweep());}},
	"private static var",{ s_timestep/*:b2TimeStep*/ :function(){return( new Box2D.Dynamics.b2TimeStep());}},
	"private static var",{ s_queue/*:Array*//*b2Body*/ :function(){return( new Array/*b2Body*/());}},
	// Find TOI contacts and solve them.
	"b2internal function SolveTOI",function SolveTOI(step/*:b2TimeStep*/)/* : void*/{
		
		var b/*:b2Body*/;
		var fA/*:b2Fixture*/;
		var fB/*:b2Fixture*/;
		var bA/*:b2Body*/;
		var bB/*:b2Body*/;
		var cEdge/*:b2ContactEdge*/;
		var j/*:b2Joint*/;
		
		// Reserve an island and a queue for TOI island solution.
		var island/*:b2Island*/ = this.m_island$2;
		island.Initialize(this.m_bodyCount$2, Box2D.Common.b2Settings.b2_maxTOIContacts, Box2D.Common.b2Settings.b2_maxTOIJoints, null, this.m_contactManager.m_contactListener, this.m_contactSolver$2);
		
		//Simple one pass queue
		//Relies on the fact that we're only making one pass
		//through and each body can only be pushed/popped one.
		//To push:
		//  queue[queueStart+queueSize++] = newElement;
		//To pop:
		//  poppedElement = queue[queueStart++];
		//  --queueSize;
		
		var queue/*:Array*//*b2Body*/ = $$private.s_queue;
		
		for (b = this.m_bodyList; b; b = b.m_next)
		{
			b.m_flags &= ~Box2D.Dynamics.b2Body.e_islandFlag;
			b.m_sweep.t0 = 0.0;
		}
		
		var c/*:b2Contact*/;
		for (c = this.m_contactList; c; c = c.m_next)
		{
			// Invalidate TOI
			c.m_flags &= ~(Box2D.Dynamics.Contacts.b2Contact.e_toiFlag | Box2D.Dynamics.Contacts.b2Contact.e_islandFlag);
		}
		
		for (j = this.m_jointList$2; j; j = j.m_next)
		{
			j.m_islandFlag = false;
		}
		
		// Find TOI events and solve them.
		for (;;)
		{
			// Find the first TOI.
			var minContact/*:b2Contact*/ = null;
			var minTOI/*:Number*/ = 1.0;
			
			for (c = this.m_contactList; c; c = c.m_next)
			{
				// Can this contact generate a solid TOI contact?
 				if (c.IsSensor() == true ||
					c.IsEnabled() == false ||
					c.IsContinuous() == false)
				{
					continue;
				}
				
				// TODO_ERIN keep a counter on the contact, only respond to M TOIs per contact.
				
				var toi/*:Number*/ = 1.0;
				if (c.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_toiFlag)
				{
					// This contact has a valid cached TOI.
					toi = c.m_toi;
				}
				else
				{
					// Compute the TOI for this contact.
					fA = c.m_fixtureA;
					fB = c.m_fixtureB;
					bA = fA.m_body;
					bB = fB.m_body;
					
					if ((bA.GetType() != Box2D.Dynamics.b2Body.b2_dynamicBody || bA.IsAwake() == false) &&
						(bB.GetType() != Box2D.Dynamics.b2Body.b2_dynamicBody || bB.IsAwake() == false))
					{
						continue;
					}
					
					// Put the sweeps onto the same time interval.
					var t0/*:Number*/ = bA.m_sweep.t0;
					
					if (bA.m_sweep.t0 < bB.m_sweep.t0)
					{
						t0 = bB.m_sweep.t0;
						bA.m_sweep.Advance(t0);
					}
					else if (bB.m_sweep.t0 < bA.m_sweep.t0)
					{
						t0 = bA.m_sweep.t0;
						bB.m_sweep.Advance(t0);
					}
					
					//b2Settings.b2Assert(t0 < 1.0f);
					
					// Compute the time of impact.
					toi = c.ComputeTOI(bA.m_sweep, bB.m_sweep);/*
					assert 0.0 <= toi && toi <= 1.0;*/
					
					// If the TOI is in range ...
					if (toi > 0.0 && toi < 1.0)
					{
						// Interpolate on the actual range.
						//toi = Math.min((1.0 - toi) * t0 + toi, 1.0);
						toi = (1.0 - toi) * t0 + toi;
						if (toi > 1) toi = 1;
					}
					
					
					c.m_toi = toi;
					c.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_toiFlag;
				}
				
				if (Number.MIN_VALUE < toi && toi < minTOI)
				{
					// This is the minimum TOI found so far.
					minContact = c;
					minTOI = toi;
				}
			}
			
			if (minContact == null || 1.0 - 100.0 * Number.MIN_VALUE < minTOI)
			{
				// No more TOI events. Done!
				break;
			}
			
			// Advance the bodies to the TOI.
			fA = minContact.m_fixtureA;
			fB = minContact.m_fixtureB;
			bA = fA.m_body;
			bB = fB.m_body;
			$$private.s_backupA.Set(bA.m_sweep);
			$$private.s_backupB.Set(bB.m_sweep);
			bA.Advance(minTOI);
			bB.Advance(minTOI);
			
			// The TOI contact likely has some new contact points.
			minContact.Update(this.m_contactManager.m_contactListener);
			minContact.m_flags &= ~Box2D.Dynamics.Contacts.b2Contact.e_toiFlag;
			
			// Is the contact solid?
			if (minContact.IsSensor() == true ||
				minContact.IsEnabled() == false)
			{
				// Restore the sweeps
				bA.m_sweep.Set($$private.s_backupA);
				bB.m_sweep.Set($$private.s_backupB);
				bA.SynchronizeTransform();
				bB.SynchronizeTransform();
				continue;
			}
			
			// Did numerical issues prevent;,ontact pointjrom being generated
			if (minContact.IsTouching() == false)
			{
				// Give up on this TOI
				continue;
			}
			
			// Build the TOI island. We need a dynamic seed.
			var seed/*:b2Body*/ = bA;
			if (seed.GetType() != Box2D.Dynamics.b2Body.b2_dynamicBody)
			{
				seed = bB;
			}
			
			// Reset island and queue.
			island.Clear();
			var queueStart/*:int*/ = 0;	//start index for queue
			var queueSize/*:int*/ = 0;	//elements in queue
			queue[queueStart + queueSize++] = seed;
			seed.m_flags |= Box2D.Dynamics.b2Body.e_islandFlag;
			
			// Perform a breadth first search (BFS) on the contact graph.
			while (queueSize > 0)
			{
				// Grab the next body off the stack and add it to the island.
				b = queue[queueStart++];
				--queueSize;
				
				island.AddBody(b);
				
				// Make sure the body is awake.
				if (b.IsAwake() == false)
				{
					b.SetAwake(true);
				}
				
				// To keep islands as small as possible, we don't
				// propagate islands across static or kinematic bodies.
				if (b.GetType() != Box2D.Dynamics.b2Body.b2_dynamicBody)
				{
					continue;
				}
				
				// Search all contacts connected to this body.
				for (cEdge = b.m_contactList; cEdge; cEdge = cEdge.next)
				{
					// Does the TOI island still have space for contacts?
					if (island.m_contactCount == island.m_contactCapacity)
					{
						break;
					}
					
					// Has this contact already been added to an island?
					if (cEdge.contact.m_flags & Box2D.Dynamics.Contacts.b2Contact.e_islandFlag)
					{
						continue;
					}
					
					// Skip sperate, sensor, or disabled contacts.
					if (cEdge.contact.IsSensor() == true ||
						cEdge.contact.IsEnabled() == false ||
						cEdge.contact.IsTouching() == false)
					{
						continue;
					}
					
					island.AddContact(cEdge.contact);
					cEdge.contact.m_flags |= Box2D.Dynamics.Contacts.b2Contact.e_islandFlag;
					
					// Update other body.
					var other/*:b2Body*/ = cEdge.other;
					
					// Was the other body already added to this island?
					if (other.m_flags & Box2D.Dynamics.b2Body.e_islandFlag)
					{
						continue;
					}
					
					// Synchronize the connected body.
					if (other.GetType() != Box2D.Dynamics.b2Body.b2_staticBody)
					{
						other.Advance(minTOI);
						other.SetAwake(true);
					}
					
					//b2Settings.b2Assert(queueStart + queueSize < queueCapacity);
					queue[queueStart + queueSize] = other;
					++queueSize;
					other.m_flags |= Box2D.Dynamics.b2Body.e_islandFlag;
				}
				
				for (var jEdge/*:b2JointEdge*/ = b.m_jointList; jEdge; jEdge = jEdge.next) 
				{
					if (island.m_jointCount == island.m_jointCapacity) 
						continue;
					
					if (jEdge.joint.m_islandFlag == true)
						continue;
					
					other = jEdge.other;
					if (other.IsActive() == false)
					{
						continue;
					}
					
					island.AddJoint(jEdge.joint);
					jEdge.joint.m_islandFlag = true;
					
					if (other.m_flags & Box2D.Dynamics.b2Body.e_islandFlag)
						continue;
						
					// Synchronize the connected body.
					if (other.GetType() != Box2D.Dynamics.b2Body.b2_staticBody)
					{
						other.Advance(minTOI);
						other.SetAwake(true);
					}
					
					//b2Settings.b2Assert(queueStart + queueSize < queueCapacity);
					queue[queueStart + queueSize] = other;
					++queueSize;
					other.m_flags |= Box2D.Dynamics.b2Body.e_islandFlag;
				}
			}
			
			var subStep/*:b2TimeStep*/ = $$private.s_timestep;
			subStep.warmStarting = false;
			subStep.dt = (1.0 - minTOI) * step.dt;
			subStep.inv_dt = 1.0 / subStep.dt;
			subStep.dtRatio = 0.0;
			subStep.velocityIterations = step.velocityIterations;
			subStep.positionIterations = step.positionIterations;
			
			island.SolveTOI(subStep);
			
			var i/*:int*/;
			// Post solve cleanup.
			for (i = 0; i < island.m_bodyCount; ++i)
			{
				// Allow bodies to participate in future TOI islands.
				b = island.m_bodies[i];
				b.m_flags &= ~Box2D.Dynamics.b2Body.e_islandFlag;
				
				if (b.IsAwake() == false)
				{
					continue;
				}
				
				if (b.GetType() != Box2D.Dynamics.b2Body.b2_dynamicBody)
				{
					continue;
				}
				
				// Update fixtures (for broad-phase).
				b.SynchronizeFixtures();
				
				// Invalidate all contact TOIs associated with this body. Some of these
				// may not be in the island because they were not touching.
				for (cEdge = b.m_contactList; cEdge; cEdge = cEdge.next)
				{
					cEdge.contact.m_flags &= ~Box2D.Dynamics.Contacts.b2Contact.e_toiFlag;
				}
			}
			
			for (i = 0; i < island.m_contactCount; ++i)
			{
				// Allow contacts to participate in future TOI islands.
				c = island.m_contacts[i];
				c.m_flags &= ~(Box2D.Dynamics.Contacts.b2Contact.e_toiFlag | Box2D.Dynamics.Contacts.b2Contact.e_islandFlag);
			}
			
			for (i = 0; i < island.m_jointCount;++i)
			{
				// Allow joints to participate in future TOI islands
				j = island.m_joints[i];
				j.m_islandFlag = false;
			}
			
			// Commit fixture proxy movements to the broad-phase so that new contacts are created.
			// Also, some contacts can be destroyed.
			this.m_contactManager.FindNewContacts();
		}
		
		//m_stackAllocator.Free(queue);
	},
	
	"static private var",{ s_jointColor/*:b2Color*/ :function(){return( new Box2D.Common.b2Color(0.5, 0.8, 0.8));}},
	//
	"b2internal function DrawJoint",function DrawJoint(joint/*:b2Joint*/)/* : void*/{
		
		var b1/*:b2Body*/ = joint.GetBodyA();
		var b2/*:b2Body*/ = joint.GetBodyB();
		var xf1/*:b2Transform*/ = b1.m_xf;
		var xf2/*:b2Transform*/ = b2.m_xf;
		var x1/*:b2Vec2*/ = xf1.position;
		var x2/*:b2Vec2*/ = xf2.position;
		var p1/*:b2Vec2*/ = joint.GetAnchorA();
		var p2/*:b2Vec2*/ = joint.GetAnchorB();
		
		//b2Color color(0.5f, 0.8f, 0.8f);
		var color/*:b2Color*/ = $$private.s_jointColor;
		
		switch (joint.m_type)
		{
		case Box2D.Dynamics.Joints.b2Joint.e_distanceJoint:
			this.m_debugDraw$2.DrawSegment(p1, p2, color);
			break;
		
		case Box2D.Dynamics.Joints.b2Joint.e_pulleyJoint:
			{
				var pulley/*:b2PulleyJoint*/ = (as(joint,  Box2D.Dynamics.Joints.b2PulleyJoint));
				var s1/*:b2Vec2*/ = pulley.GetGroundAnchorA();
				var s2/*:b2Vec2*/ = pulley.GetGroundAnchorB();
				this.m_debugDraw$2.DrawSegment(s1, p1, color);
				this.m_debugDraw$2.DrawSegment(s2, p2, color);
				this.m_debugDraw$2.DrawSegment(s1, s2, color);
			}
			break;
		
		case Box2D.Dynamics.Joints.b2Joint.e_mouseJoint:
			this.m_debugDraw$2.DrawSegment(p1, p2, color);
			break;
		
		default:
			if (b1 != this.m_groundBody)
				this.m_debugDraw$2.DrawSegment(x1, p1, color);
			this.m_debugDraw$2.DrawSegment(p1, p2, color);
			if (b2 != this.m_groundBody)
				this.m_debugDraw$2.DrawSegment(x2, p2, color);
		}
	},
	
	"b2internal function DrawShape",function DrawShape(shape/*:b2Shape*/, xf/*:b2Transform*/, color/*:b2Color*/)/* : void*/{
		
		switch (shape.m_type)
		{
		case Box2D.Collision.Shapes.b2Shape.e_circleShape:
			{
				var circle/*:b2CircleShape*/ = (as(shape,  Box2D.Collision.Shapes.b2CircleShape));
				
				var center/*:b2Vec2*/ = Box2D.Common.Math.b2Math.MulX(xf, circle.m_p);
				var radius/*:Number*/ = circle.m_radius;
				var axis/*:b2Vec2*/ = xf.R.col1;
				
				this.m_debugDraw$2.DrawSolidCircle(center, radius, axis, color);
			}
			break;
		
		case Box2D.Collision.Shapes.b2Shape.e_polygonShape:
			{
				var i/*:int*/;
				var poly/*:b2PolygonShape*/ = (as(shape,  Box2D.Collision.Shapes.b2PolygonShape));
				var vertexCount/*:int*/ = poly.GetVertexCount();
				var localVertices/*:Array*//*b2Vec2*/ = poly.GetVertices();
				
				var vertices/*:Array*//*b2Vec2*/ = new Array/*b2Vec2*/(vertexCount);
				
				for (i = 0; i < vertexCount; ++i)
				{
					vertices[i] = Box2D.Common.Math.b2Math.MulX(xf, localVertices[i]);
				}
				
				this.m_debugDraw$2.DrawSolidPolygon(vertices, vertexCount, color);
			}
			break;
		}
	},
	
	"b2internal var",{ m_flags/*:int*/:0},

	"b2internal var",{ m_contactManager/*:b2ContactManager*/:null},
	
	// These two are stored purely for efficiency purposes, they don't maintain
	// any data outside of a call to Step
	"private var",{ m_contactSolver/*:b2ContactSolver*/ :function(){return( new Box2D.Dynamics.Contacts.b2ContactSolver());}},
	"private var",{ m_island/*:b2Island*/ :function(){return( new Box2D.Dynamics.b2Island());}},

	"b2internal var",{ m_bodyList/*:b2Body*/:null},
	"private var",{ m_jointList/*:b2Joint*/:null},

	"b2internal var",{ m_contactList/*:b2Contact*/:null},

	"private var",{ m_bodyCount/*:int*/:0},
	"private var",{ m_jointCount/*:int*/:0},
	"private var",{ m_controllerList/*:b2Controller*/:null},
	"private var",{ m_controllerCount/*:int*/:0},

	"private var",{ m_gravity/*:b2Vec2*/:null},
	"private var",{ m_allowSleep/*:Boolean*/:false},

	"b2internal var",{ m_groundBody/*:b2Body*/:null},

	"private var",{ m_destructionListener/*:IDestructionListener*/:null},
	"private var",{ m_debugDraw/*:b2DebugDraw*/:null},

	// This is used to compute the time step ratio to support a variable time step.
	"private var",{ m_inv_dt0/*:Number*/:NaN},

	// This is for debugging the solver.
	"static private var",{ m_warmStarting/*:Boolean*/:false},

	// This is for debugging the solver.
	"static private var",{ m_continuousPhysics/*:Boolean*/:false},
	
	// m_flags
	"public static const",{ e_newFixture/*:int*/ : 0x0001},
	"public static const",{ e_locked/*:int*/ : 0x0002},
	"public static const",{ e_clearForces/*:int*/ : 0x0004},
	
	"private var",{ m_preStepEvent/*:Event*/ :function(){return( new flash.events.Event(Box2D.Dynamics.b2World.PRESTEP));}},
	"private var",{ m_postStepEvent/*:Event*/ :function(){return( new flash.events.Event(Box2D.Dynamics.b2World.POSTSTEP));}},
	"private var",{ m_addBodyEvent/*:b2BodyEvent*/ :function(){return( new Box2D.Dynamics.b2BodyEvent(Box2D.Dynamics.b2World.ADDBODY));}},
	"private var",{ m_removeBodyEvent/*:b2BodyEvent*/ :function(){return( new Box2D.Dynamics.b2BodyEvent(Box2D.Dynamics.b2World.REMOVEBODY));}},
	"b2internal var",{ m_addFixtureEvent/*:b2FixtureEvent*/ :function(){return( new Box2D.Dynamics.b2FixtureEvent(Box2D.Dynamics.b2World.ADDFIXTURE));}},
	"b2internal var",{ m_removeFixtureEvent/*:b2FixtureEvent*/ :function(){return( new Box2D.Dynamics.b2FixtureEvent(Box2D.Dynamics.b2World.REMOVEFIXTURE));}},
	"private var",{ m_addJointEvent/*:b2JointEvent*/ :function(){return( new Box2D.Dynamics.b2JointEvent(Box2D.Dynamics.b2World.ADDJOINT));}},
	"private var",{ m_removeJointEvent/*:b2JointEvent*/ :function(){return( new Box2D.Dynamics.b2JointEvent(Box2D.Dynamics.b2World.REMOVEJOINT));}},
];},[],["flash.events.EventDispatcher","Box2D.Dynamics.b2ContactManager","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Error","Box2D.Dynamics.Joints.b2Joint","Box2D.Dynamics.b2TimeStep","Box2D.Common.Math.b2Transform","Box2D.Common.Math.b2Vec2","Box2D.Collision.b2AABB","Box2D.Common.b2Color","Box2D.Dynamics.b2DebugDraw","Box2D.Dynamics.b2Fixture","Box2D.Collision.Shapes.b2Shape","Box2D.Common.b2Settings","Box2D.Collision.b2RayCastOutput","Box2D.Collision.b2RayCastInput","Array","Box2D.Dynamics.Contacts.b2Contact","Box2D.Common.Math.b2Sweep","Number","Box2D.Dynamics.Joints.b2PulleyJoint","Box2D.Collision.Shapes.b2CircleShape","Box2D.Common.Math.b2Math","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.Contacts.b2ContactSolver","Box2D.Dynamics.b2Island","flash.events.Event","Box2D.Dynamics.b2BodyEvent","Box2D.Dynamics.b2FixtureEvent","Box2D.Dynamics.b2JointEvent"], "0.8.0", "0.8.1"



);