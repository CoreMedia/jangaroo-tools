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
import flash.events.IEventDispatcher

import Box2D.Common.b2internal
use namespace b2internal*/

/**
 * Default implementation of IContactListener, which automatically raises
 * events on the world, and related bodies.
 */
"public class b2DefaultContactListener implements Box2D.Dynamics.IContactListener",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2World);},

	"public function b2DefaultContactListener",function b2DefaultContactListener$(world/*:b2World*/)
	{this.m_beginContact$1=this.m_beginContact$1();this.m_endContact$1=this.m_endContact$1();this.m_preSolveContact$1=this.m_preSolveContact$1();this.m_postSolveContact$1=this.m_postSolveContact$1();
		this.m_world$1 = world;
	},
	
	"public function BeginContact",function BeginContact(contact/*:b2Contact*/)/*:void*/
	{
		this.m_beginContact$1.contact = contact;
		this.m_world$1.dispatchEvent(this.m_beginContact$1);
		var dispatcher/*:IEventDispatcher*/;
		dispatcher = contact.m_fixtureA.m_body.m_eventDispatcher;
		if (dispatcher) dispatcher.dispatchEvent(this.m_beginContact$1);
		dispatcher = contact.m_fixtureB.m_body.m_eventDispatcher;
		if (dispatcher) dispatcher.dispatchEvent(this.m_beginContact$1);
	},
	"public function EndContact",function EndContact(contact/*:b2Contact*/)/*:void*/ 
	{
		this.m_endContact$1.contact = contact;
		var dispatcher/*:IEventDispatcher*/;
		dispatcher = contact.m_fixtureA.m_body.m_eventDispatcher;
		if (dispatcher) dispatcher.dispatchEvent(this.m_endContact$1);
		dispatcher = contact.m_fixtureB.m_body.m_eventDispatcher;
		if (dispatcher) dispatcher.dispatchEvent(this.m_endContact$1);
		this.m_world$1.dispatchEvent(this.m_endContact$1);
	},
	"public function PreSolve",function PreSolve(contact/*:b2Contact*/, oldManifold/*:b2Manifold*/)/*:void*/
	{
		this.m_preSolveContact$1.contact = contact;
		this.m_preSolveContact$1.oldManifold = oldManifold;
		this.m_world$1.dispatchEvent(this.m_beginContact$1);
		var dispatcher/*:IEventDispatcher*/;
		dispatcher = contact.m_fixtureA.m_body.m_eventDispatcher;
		if (dispatcher) dispatcher.dispatchEvent(this.m_preSolveContact$1);
		dispatcher = contact.m_fixtureB.m_body.m_eventDispatcher;
		if (dispatcher) dispatcher.dispatchEvent(this.m_preSolveContact$1);
	},
	"public function PostSolve",function PostSolve(contact/*:b2Contact*/, impulse/*:b2ContactImpulse*/)/*:void*/
	{
		this.m_postSolveContact$1.contact = contact;
		this.m_postSolveContact$1.impulse = impulse;
		var dispatcher/*:IEventDispatcher*/;
		dispatcher = contact.m_fixtureA.m_body.m_eventDispatcher;
		if (dispatcher) dispatcher.dispatchEvent(this.m_postSolveContact$1);
		dispatcher = contact.m_fixtureB.m_body.m_eventDispatcher;
		if (dispatcher) dispatcher.dispatchEvent(this.m_postSolveContact$1);
		this.m_world$1.dispatchEvent(this.m_beginContact$1);
	},
	
	// Events for recycling
	"private var",{ m_beginContact/*:b2ContactEvent*/ :function(){return( new Box2D.Dynamics.b2ContactEvent(Box2D.Dynamics.b2World.BEGINCONTACT));}},
	"private var",{ m_endContact/*:b2ContactEvent*/ :function(){return( new Box2D.Dynamics.b2ContactEvent(Box2D.Dynamics.b2World.ENDCONTACT));}},
	"private var",{ m_preSolveContact/*:b2PreSolveEvent*/ :function(){return( new Box2D.Dynamics.b2PreSolveEvent(Box2D.Dynamics.b2World.PRESOLVE));}},
	"private var",{ m_postSolveContact/*:b2PostSolveEvent*/ :function(){return( new Box2D.Dynamics.b2PostSolveEvent(Box2D.Dynamics.b2World.POSTSOLVE));}},
	"private var",{ m_world/*:b2World*/:null},
];},[],["Box2D.Dynamics.IContactListener","Box2D.Dynamics.b2ContactEvent","Box2D.Dynamics.b2World","Box2D.Dynamics.b2PreSolveEvent","Box2D.Dynamics.b2PostSolveEvent"], "0.8.0", "0.8.1"
	
	
);