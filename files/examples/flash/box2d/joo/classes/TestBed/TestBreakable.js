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


"package TestBed",/*{
	
	
	
	import Box2D.Dynamics.*
	import Box2D.Collision.*
	import Box2D.Collision.Shapes.*
	import Box2D.Dynamics.Joints.*
	import Box2D.Dynamics.Contacts.*
	import Box2D.Common.*
	import Box2D.Common.Math.**/
	
	
	
	"public class TestBreakable extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
		
		"public function TestBreakable",function TestBreakable$(){this.super$2();this.m_velocity=this.m_velocity();this.m_shape1=this.m_shape1();this.m_shape2=this.m_shape2();

			// Set Text field
			Main.m_aboutText.text = "Breakable";
			
			this.m_world.SetContactListener(new TestBed.TestBreakable.ContactListener(this));
			
			var ground/*:b2Body*/ = this.m_world.GetGroundBody();
			
			// Breakable Dynamic Body
			{
				var bd/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
				bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				bd.position.Set(5.0, 5.0);
				bd.angle = 0.25 * Math.PI;
				this.m_body1 = this.m_world.CreateBody(bd);
				
				this.m_shape1.SetAsOrientedBox(0.5, 0.5, new Box2D.Common.Math.b2Vec2( -0.5, 0.0));
				this.m_piece1 = this.m_body1.CreateFixture2(this.m_shape1, 1.0);
				
				this.m_shape2.SetAsOrientedBox(0.5, 0.5, new Box2D.Common.Math.b2Vec2( 0.5, 0.0));
				this.m_piece2 = this.m_body1.CreateFixture2(this.m_shape2, 1.0);
			}
			
			this.m_break = false;
			this.m_broke = false;
		},
		
		"public function Break",function Break()/*:void*/
		{var this$=this;
			// Apply cached velocity for more realistic break
			this.m_body1.SetLinearVelocity(this.m_velocity);
			this.m_body1.SetAngularVelocity(this.m_angularVelocity);
			
			// Split body into two pieces
			this.m_body1.Split(function TestBed$TestBreakable$71_18(fixture/*:b2Fixture*/)/*:Boolean*/ {
				return fixture != this$.m_piece1;
			});
		},
		
		"override public function Update",function Update()/*:void*/ 
		{
			this.Update$2();
			if (this.m_break)
			{
				this.Break();
				this.m_broke = true;
				this.m_break = false;
			}
			
			// Cache velocities to improve movement on breakage
			if (this.m_broke == false)
			{
				this.m_velocity = this.m_body1.GetLinearVelocity();
				this.m_angularVelocity = this.m_body1.GetAngularVelocity();
			}
		},
		
		//======================
		// Member Data 
		//======================
		
		"public var",{ m_body1/*:b2Body*/:null},
		"public var",{ m_velocity/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
		"public var",{ m_angularVelocity/*:Number*/:NaN},
		"public var",{ m_shape1/*:b2PolygonShape*/ :function(){return( new Box2D.Collision.Shapes.b2PolygonShape());}},
		"public var",{ m_shape2/*:b2PolygonShape*/ :function(){return( new Box2D.Collision.Shapes.b2PolygonShape());}},
		"public var",{ m_piece1/*:b2Fixture*/:null},
		"public var",{ m_piece2/*:b2Fixture*/:null},
		"public var",{ m_broke/*:Boolean*/:false},
		"public var",{ m_break/*:Boolean*/:false},
	/*

import Box2D.Dynamics.*
import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Dynamics.Joints.*
import Box2D.Dynamics.Contacts.*
import Box2D.Common.*
import Box2D.Common.Math.*
import TestBed.TestBreakable*/

"class ContactListener extends Box2D.Dynamics.b2EmptyContactListener",2,function($$private){;return[

	"private var",{ test/*:TestBreakable*/:null},
	"public function ContactListener",function ContactListener$(test/*:TestBreakable*/)
	{this.super$2();
		this.test$2 = test;
	},
	
	"override public function PostSolve",function PostSolve(contact/*:b2Contact*/, impulse/*:b2ContactImpulse*/)/*:void*/
	{
		if (this.test$2.m_broke)
		{
			// The body already broke
			return;
		}
		
		// Should the body break?
		var count/*:int*/ = contact.GetManifold().m_pointCount;
		
		var maxImpulse/*:Number*/ = 0.0;
		for (var i/*:int*/ = 0; i < count; i++)
		{
			maxImpulse = Box2D.Common.Math.b2Math.Max(maxImpulse, impulse.normalImpulses[i]);
		}
		
		if (maxImpulse > 50)
		{
			this.test$2.m_break = true;
		}
	},
];},[],];},[],["TestBed.Test","Main","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Math","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2EmptyContactListener","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"
	
);