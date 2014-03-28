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
	
	
	
	"public class TestRaycast extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
		
		"public var",{ laser/*:b2Body*/:null},
		
		"public function TestRaycast",function TestRaycast$(){this.super$2();
			// Set Text field
			Main.m_aboutText.text = "Raycast";
			
			this.m_world.SetGravity(new Box2D.Common.Math.b2Vec2(0,0));
			
			var ground/*:b2Body*/ = this.m_world.GetGroundBody();
			
			var box/*:b2PolygonShape*/ = new Box2D.Collision.Shapes.b2PolygonShape();
			box.SetAsBox(30 / this.m_physScale, 4 / this.m_physScale);
			var fd/*:b2FixtureDef*/ = new Box2D.Dynamics.b2FixtureDef();
			fd.shape = box;
			fd.density = 4;
			fd.friction = 0.4;
			fd.restitution = 0.3;
			fd.userData="laser";
			var bd/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
			bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
			bd.position.Set(320 / this.m_physScale, 150 / this.m_physScale);
			bd.position.Set(40 / this.m_physScale, 150 / this.m_physScale);
			this.laser = this.m_world.CreateBody(bd);
			this.laser.CreateFixture(fd);
			this.laser.SetAngle(0.5);
			this.laser.SetAngle(Math.PI);
			
			var circle/*:b2CircleShape*/ = new Box2D.Collision.Shapes.b2CircleShape(30 / this.m_physScale);
			fd.shape = circle;
			fd.density = 4;
			fd.friction = 0.4;
			fd.restitution = 0.3;
			fd.userData="circle";
			bd.position.Set(100 / this.m_physScale, 100 / this.m_physScale);
			var body/*:b2Body*/ = this.m_world.CreateBody(bd);
			body.CreateFixture(fd);
		},
		
		
		//======================
		// Member Data 
		//======================
		
		"public override function Update",function Update()/*:void*/{
			this.Update$2();
			
			var p1/*:b2Vec2*/ = this.laser.GetWorldPoint(new Box2D.Common.Math.b2Vec2(30.1 / this.m_physScale, 0));
			var p2/*:b2Vec2*/ = this.laser.GetWorldPoint(new Box2D.Common.Math.b2Vec2(130.1 / this.m_physScale, 0));
			
			var f/*:b2Fixture*/ = this.m_world.RayCastOne(p1, p2);
			var lambda/*:Number*/ = 1;
			if (f)
			{
				var input/*:b2RayCastInput*/ = new Box2D.Collision.b2RayCastInput(p1, p2);
				var output/*:b2RayCastOutput*/ = new Box2D.Collision.b2RayCastOutput();
				f.RayCast(output, input);
				lambda = output.fraction;
			}
			this.m_sprite.graphics.lineStyle(1,0xff0000,1);
			this.m_sprite.graphics.moveTo(p1.x * this.m_physScale, p1.y * this.m_physScale);
			this.m_sprite.graphics.lineTo( 	(p2.x * lambda + (1 - lambda) * p1.x) * this.m_physScale,
										(p2.y * lambda + (1 - lambda) * p1.y) * this.m_physScale);

		},
	];},[],["TestBed.Test","Main","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2FixtureDef","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Math","Box2D.Collision.Shapes.b2CircleShape","Box2D.Collision.b2RayCastInput","Box2D.Collision.b2RayCastOutput"], "0.8.0", "0.8.1"
	
);