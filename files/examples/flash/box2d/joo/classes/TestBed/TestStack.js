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
	import Box2D.Common.Math.*
	
	//TODO_BORIS: Remove
	use namespace b2internal*/
	
	"public class TestStack extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main);},
		
		"public function TestStack",function TestStack$(){this.super$2();
			
			// Set Text field
			Main.m_aboutText.text = "Stacked Boxes";
			
			// Add bodies
			var fd/*:b2FixtureDef*/ = new Box2D.Dynamics.b2FixtureDef();
			var sd/*:b2PolygonShape*/ = new Box2D.Collision.Shapes.b2PolygonShape();
			var bd/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
			bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
			//bd.isBullet = true;
			var b/*:b2Body*/;
			fd.density = 1.0;
			fd.friction = 0.5;
			fd.restitution = 0.1;
			fd.shape = sd;
			var i/*:int*/;
			// Create 3 stacks
			for (i = 0; i < 10; i++){
				sd.SetAsBox((10) / this.m_physScale, (10) / this.m_physScale);
				//bd.position.Set((640/2+100+Math.random()*0.02 - 0.01) / m_physScale, (360-5-i*25) / m_physScale);
				bd.position.Set((640/2+100) / this.m_physScale, (360-5-i*25) / this.m_physScale);
				b = this.m_world.CreateBody(bd);
				b.CreateFixture(fd);
			}
			for (i = 0; i < 10; i++){
				sd.SetAsBox((10) / this.m_physScale, (10) / this.m_physScale);
				bd.position.Set((640/2-0+Math.random()*0.02 - 0.01) / this.m_physScale, (360-5-i*25) / this.m_physScale);
				b = this.m_world.CreateBody(bd);
				b.CreateFixture(fd);
			}
			for (i = 0; i < 10; i++){
				sd.SetAsBox((10) / this.m_physScale, (10) / this.m_physScale);
				bd.position.Set((640/2+200+Math.random()*0.02 - 0.01) / this.m_physScale, (360-5-i*25) / this.m_physScale);
				b = this.m_world.CreateBody(bd);
				b.CreateFixture(fd);
			}
			// Create ramp
			var vxs/*:Array*/ = [new Box2D.Common.Math.b2Vec2(0, 0),
				new Box2D.Common.Math.b2Vec2(0, -100 / this.m_physScale),
				new Box2D.Common.Math.b2Vec2(200 / this.m_physScale, 0)];
			sd.SetAsArray(vxs, vxs.length);
			fd.density = 0;
			bd.type = Box2D.Dynamics.b2Body.b2_staticBody;
			bd.userData = "ramp";
			bd.position.Set(0, 360 / this.m_physScale);
			b = this.m_world.CreateBody(bd);
			b.CreateFixture(fd);
			
			// Create ball
			var cd/*:b2CircleShape*/ = new Box2D.Collision.Shapes.b2CircleShape();
			cd.m_radius = 40/this.m_physScale;
			fd.density = 2;
			fd.restitution = 0.2;
			fd.friction = 0.5;
			fd.shape = cd;
			bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
			bd.userData = "ball";
			bd.position.Set(50/this.m_physScale, 100 / this.m_physScale);
			b = this.m_world.CreateBody(bd);
			b.CreateFixture(fd);
			
		},
		
		
		//======================
		// Member Data 
		//======================
	];},[],["TestBed.Test","Main","Box2D.Dynamics.b2FixtureDef","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Math","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2CircleShape"], "0.8.0", "0.8.1"
	
);