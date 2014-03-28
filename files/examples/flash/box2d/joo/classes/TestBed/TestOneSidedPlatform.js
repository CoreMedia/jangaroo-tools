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
	
	"public class TestOneSidedPlatform extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main);}, 
		

		
		"public function TestOneSidedPlatform",function TestOneSidedPlatform$(){this.super$2();
			
			// Set Text field
			Main.m_aboutText.text = "One Sided Platform\n" +
				"Press: (c) create a shape, (d) destroy a shape.";
				
			var bd/*:b2BodyDef*/;
			var body/*:b2Body*/;
			
			// Platform
			{
				bd = new Box2D.Dynamics.b2BodyDef();
				bd.position.Set(10.0, 10.0);
				body = this.m_world.CreateBody(bd);
				
				var polygon/*:b2PolygonShape*/ = Box2D.Collision.Shapes.b2PolygonShape.AsBox(3.0, 0.5);
				this.m_platform = body.CreateFixture2(polygon, 0.0);
				
				this.m_bottom = bd.position.y + 0.5;
				this.m_top = bd.position.y - 0.5;
				
			}
			
			// Actor
			{
				bd = new Box2D.Dynamics.b2BodyDef();
				bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				bd.position.Set(10.0, 12.0);
				body = this.m_world.CreateBody(bd);
				
				this.m_radius = 0.5;
				var circle/*:b2CircleShape*/ = new Box2D.Collision.Shapes.b2CircleShape(this.m_radius);
				this.m_character = body.CreateFixture2(circle, 1.0);
				
				this.m_state = $$private.e_unknown;
			}
			
			this.m_world.SetContactListener(new TestBed.TestOneSidedPlatform.ContactListener(this));
		},
		
		//======================
		// Member Data 
		//======================
		
		"static private var",{ e_unknown/*:int*/ : 0},
		"static private var",{ e_above/*:int*/ : 1},
		"static private var",{ e_below/*:int*/ : 2},
		
		"public var",{ m_radius/*:Number*/:NaN},
		"public var",{ m_top/*:Number*/:NaN},
		"public var",{ m_bottom/*:Number*/:NaN},
		"public var",{ m_state/*:int*/:0},
		"public var",{ m_platform/*:b2Fixture*/:null},
		"public var",{ m_character/*:b2Fixture*/:null},
		
	/*
import Box2D.Dynamics.*
import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Dynamics.Joints.*
import Box2D.Dynamics.Contacts.*
import Box2D.Common.*
import Box2D.Common.Math.*
import TestBed.TestOneSidedPlatform*/

"class ContactListener extends Box2D.Dynamics.b2EmptyContactListener",2,function($$private){;return[

	"private var",{ test/*:TestOneSidedPlatform*/:null},
	"public function ContactListener",function ContactListener$(test/*:TestOneSidedPlatform*/)
	{this.super$2();
		this.test$2 = test;
	},
	"override public function PreSolve",function PreSolve(contact/*:b2Contact*/, oldManifold/*:b2Manifold*/)/*:void*/ 
	{
		var fixtureA/*:b2Fixture*/ = contact.GetFixtureA();
		var fixtureB/*:b2Fixture*/ = contact.GetFixtureB();
		if (fixtureA != this.test$2.m_platform && fixtureA != this.test$2.m_character)
			return;
		if (fixtureB != this.test$2.m_platform && fixtureB != this.test$2.m_character)
			return;
			
		var position/*:b2Vec2*/ = this.test$2.m_character.GetBody().GetPosition();
		if (position.y > this.test$2.m_top)
			contact.SetEnabled(false);
	},
];},[],];},[],["TestBed.Test","Main","Box2D.Dynamics.b2BodyDef","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2Body","Box2D.Collision.Shapes.b2CircleShape","Box2D.Dynamics.b2EmptyContactListener"], "0.8.0", "0.8.1"
);