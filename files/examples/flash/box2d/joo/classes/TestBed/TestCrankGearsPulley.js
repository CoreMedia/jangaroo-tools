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
	
	
	
	"public class TestCrankGearsPulley extends TestBed.Test",2,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
		
		"public function TestCrankGearsPulley",function TestCrankGearsPulley$(){this.super$2();
			
			// Set Text field
			Main.m_aboutText.text = "Joints";
			
			var ground/*:b2Body*/ = this.m_world.GetGroundBody();
			
			var body/*:b2Body*/;
			var circleBody/*:b2Body*/;
			var sd/*:b2PolygonShape*/;
			var bd/*:b2BodyDef*/;
			var fixtureDef/*:b2FixtureDef*/ = new Box2D.Dynamics.b2FixtureDef();
			
			//
			// CRANK
			//
			{
				// Define crank.
				sd = new Box2D.Collision.Shapes.b2PolygonShape();
				sd.SetAsBox(7.5 / this.m_physScale, 30.0 / this.m_physScale);
				fixtureDef.shape = sd;
				fixtureDef.density = 1.0;
				
				var rjd/*:b2RevoluteJointDef*/ = new Box2D.Dynamics.Joints.b2RevoluteJointDef();
				
				var prevBody/*:b2Body*/ = ground;
				
				bd = new Box2D.Dynamics.b2BodyDef();
				bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				bd.position.Set(100.0/this.m_physScale, (360.0-105.0)/this.m_physScale);
				body = this.m_world.CreateBody(bd);
				body.CreateFixture(fixtureDef);
				
				rjd.Initialize(prevBody, body, new Box2D.Common.Math.b2Vec2(100.0/this.m_physScale, (360.0-75.0)/this.m_physScale));
				rjd.motorSpeed = 1.0 * -Math.PI;
				rjd.maxMotorTorque = 5000.0;
				rjd.enableMotor = true;
				this.m_joint1$2 =as( this.m_world.CreateJoint(rjd),  Box2D.Dynamics.Joints.b2RevoluteJoint);
				
				prevBody = body;
				
				// Define follower.
				sd = new Box2D.Collision.Shapes.b2PolygonShape;
				sd.SetAsBox(7.5 / this.m_physScale, 60.0 / this.m_physScale);
				fixtureDef.shape = sd;
				bd.position.Set(100.0/this.m_physScale, (360.0-195.0)/this.m_physScale);
				body = this.m_world.CreateBody(bd);
				body.CreateFixture(fixtureDef);
				
				rjd.Initialize(prevBody, body, new Box2D.Common.Math.b2Vec2(100.0/this.m_physScale, (360.0-135.0)/this.m_physScale));
				rjd.enableMotor = false;
				this.m_world.CreateJoint(rjd);
				
				prevBody = body;
				
				// Define piston
				sd = new Box2D.Collision.Shapes.b2PolygonShape();
				sd.SetAsBox(22.5 / this.m_physScale, 22.5 / this.m_physScale);
				fixtureDef.shape = sd;
				bd.position.Set(100.0/this.m_physScale, (360.0-255.0)/this.m_physScale);
				body = this.m_world.CreateBody(bd);
				body.CreateFixture(fixtureDef);
				
				rjd.Initialize(prevBody, body, new Box2D.Common.Math.b2Vec2(100.0/this.m_physScale, (360.0-255.0)/this.m_physScale));
				this.m_world.CreateJoint(rjd);
				
				var pjd/*:b2PrismaticJointDef*/ = new Box2D.Dynamics.Joints.b2PrismaticJointDef();
				pjd.Initialize(ground, body, new Box2D.Common.Math.b2Vec2(100.0/this.m_physScale, (360.0-255.0)/this.m_physScale), new Box2D.Common.Math.b2Vec2(0.0, 1.0));
				
				pjd.maxMotorForce = 500.0;
				pjd.enableMotor = true;
				
				this.m_joint2$2 =as( this.m_world.CreateJoint(pjd),  Box2D.Dynamics.Joints.b2PrismaticJoint);
				
				// Create a payload
				sd = new Box2D.Collision.Shapes.b2PolygonShape();
				sd.SetAsBox(22.5 / this.m_physScale, 22.5 / this.m_physScale);
				fixtureDef.shape = sd;
				fixtureDef.density = 2.0;
				bd.position.Set(100.0/this.m_physScale, (360.0-345.0)/this.m_physScale);
				body = this.m_world.CreateBody(bd);
				body.CreateFixture(fixtureDef);
			}
			
			
			// 
			// GEARS
			//
			//{
				var circle1/*:b2CircleShape*/ = new Box2D.Collision.Shapes.b2CircleShape(25 / this.m_physScale);
				fixtureDef.shape = circle1;
				fixtureDef.density = 5.0;
				
				var bd1/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
				bd1.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				bd1.position.Set(200 / this.m_physScale, 360/2 / this.m_physScale);
				var body1/*:b2Body*/ = this.m_world.CreateBody(bd1);
				body1.CreateFixture(fixtureDef);
				
				var jd1/*:b2RevoluteJointDef*/ = new Box2D.Dynamics.Joints.b2RevoluteJointDef();
				jd1.Initialize(ground, body1, bd1.position);
				this.m_gJoint1 =as( this.m_world.CreateJoint(jd1),  Box2D.Dynamics.Joints.b2RevoluteJoint);
				
				var circle2/*:b2CircleShape*/ = new Box2D.Collision.Shapes.b2CircleShape(50 / this.m_physScale);
				fixtureDef.shape = circle2;
				fixtureDef.density = 5.0;
				
				var bd2/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
				bd2.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				bd2.position.Set(275 / this.m_physScale, 360/2 / this.m_physScale);
				var body2/*:b2Body*/ = this.m_world.CreateBody(bd2);
				body2.CreateFixture(fixtureDef);
				
				var jd2/*:b2RevoluteJointDef*/ = new Box2D.Dynamics.Joints.b2RevoluteJointDef();
				jd2.Initialize(ground, body2, bd2.position);
				this.m_gJoint2 =as( this.m_world.CreateJoint(jd2),  Box2D.Dynamics.Joints.b2RevoluteJoint);
				
				var box/*:b2PolygonShape*/ = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(10 / this.m_physScale, 100 / this.m_physScale);
				fixtureDef.shape = box;
				fixtureDef.density = 5.0;
				
				var bd3/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
				bd3.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				bd3.position.Set(335 / this.m_physScale, 360/2 / this.m_physScale);
				var body3/*:b2Body*/ = this.m_world.CreateBody(bd3);
				body3.CreateFixture(fixtureDef);
				
				var jd3/*:b2PrismaticJointDef*/ = new Box2D.Dynamics.Joints.b2PrismaticJointDef();
				jd3.Initialize(ground, body3, bd3.position, new Box2D.Common.Math.b2Vec2(0,1));
				jd3.lowerTranslation = -25.0 / this.m_physScale;
				jd3.upperTranslation = 100.0 / this.m_physScale;
				jd3.enableLimit = true;
				
				this.m_gJoint3 =as( this.m_world.CreateJoint(jd3),  Box2D.Dynamics.Joints.b2PrismaticJoint);
				
				var jd4/*:b2GearJointDef*/ = new Box2D.Dynamics.Joints.b2GearJointDef();
				jd4.bodyA = body1;
				jd4.bodyB = body2;
				jd4.joint1 = this.m_gJoint1;
				jd4.joint2 = this.m_gJoint2;
				jd4.ratio = circle2.GetRadius() / circle1.GetRadius();
				this.m_gJoint4 =as( this.m_world.CreateJoint(jd4),  Box2D.Dynamics.Joints.b2GearJoint);
				
				var jd5/*:b2GearJointDef*/ = new Box2D.Dynamics.Joints.b2GearJointDef();
				jd5.bodyA = body2;
				jd5.bodyB = body3;
				jd5.joint1 = this.m_gJoint2;
				jd5.joint2 = this.m_gJoint3;
				jd5.ratio = -1.0 / circle2.GetRadius();
				this.m_gJoint5 =as( this.m_world.CreateJoint(jd5),  Box2D.Dynamics.Joints.b2GearJoint);
			//}
			
			
			
			//
			// PULLEY
			//
			//{
				sd = new Box2D.Collision.Shapes.b2PolygonShape();
				sd.SetAsBox(50 / this.m_physScale, 20 / this.m_physScale);
				fixtureDef.shape = sd;
				fixtureDef.density = 5.0;
				
				bd = new Box2D.Dynamics.b2BodyDef();
				bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				
				bd.position.Set(480 / this.m_physScale, 200 / this.m_physScale);
				body2 = this.m_world.CreateBody(bd);
				body2.CreateFixture(fixtureDef);
				
				var pulleyDef/*:b2PulleyJointDef*/ = new Box2D.Dynamics.Joints.b2PulleyJointDef();
				
				var anchor1/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(335 / this.m_physScale, 180 / this.m_physScale);
				var anchor2/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(480 / this.m_physScale, 180 / this.m_physScale);
				var groundAnchor1/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(335 / this.m_physScale, 50 / this.m_physScale);
				var groundAnchor2/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(480 / this.m_physScale, 50 / this.m_physScale);
				pulleyDef.Initialize(body3, body2, groundAnchor1, groundAnchor2, anchor1, anchor2, 2.0);
				
				pulleyDef.maxLengthA = 200 / this.m_physScale;
				pulleyDef.maxLengthB = 150 / this.m_physScale;as(
				
				//m_joint1 = m_world.CreateJoint(pulleyDef) as b2PulleyJoint;
				this.m_world.CreateJoint(pulleyDef),  Box2D.Dynamics.Joints.b2PulleyJoint);
				
				
				// Add a circle to weigh down the pulley
				var circ/*:b2CircleShape*/ = new Box2D.Collision.Shapes.b2CircleShape(40 / this.m_physScale);
				fixtureDef.shape = circ;
				fixtureDef.friction = 0.3;
				fixtureDef.restitution = 0.3;
				fixtureDef.density = 5.0;
				bd.position.Set(485 / this.m_physScale, 100 / this.m_physScale);
				body1 = circleBody = this.m_world.CreateBody(bd);
				body1.CreateFixture(fixtureDef);
			//}
			
			//
			// LINE JOINT
			//
			{
				sd = new Box2D.Collision.Shapes.b2PolygonShape();
				sd.SetAsBox(7.5 / this.m_physScale, 30.0 / this.m_physScale);
				fixtureDef.shape = sd;
				fixtureDef.density = 1.0;
				
				bd = new Box2D.Dynamics.b2BodyDef();
				bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				bd.position.Set(500 / this.m_physScale, 500/2 / this.m_physScale);
				body = this.m_world.CreateBody(bd);
				body.CreateFixture(fixtureDef);
				
				var ljd/*:b2LineJointDef*/ = new Box2D.Dynamics.Joints.b2LineJointDef();
				ljd.Initialize(ground, body, body.GetPosition(), new Box2D.Common.Math.b2Vec2(0.4, 0.6));
				
				ljd.lowerTranslation = -1;
				ljd.upperTranslation = 1;
				ljd.enableLimit = true;
				
				ljd.maxMotorForce = 1;
				ljd.motorSpeed = 0;
				ljd.enableMotor = true;
				
				this.m_world.CreateJoint(ljd);
			}
			
			//
			// FRICTION JOINT
			//
			{
				var fjd/*:b2FrictionJointDef*/ = new Box2D.Dynamics.Joints.b2FrictionJointDef();
				fjd.Initialize(circleBody, this.m_world.GetGroundBody(), circleBody.GetPosition());
				fjd.collideConnected = true;
				fjd.maxForce = 200;
				this.m_world.CreateJoint(fjd);
			}
			
			//
			// WELD JOINT
			//
			// Not enabled as Weld joints are not encouraged compared with merging two bodies
			if(false)
			{
				var wjd/*:b2WeldJointDef*/ = new Box2D.Dynamics.Joints.b2WeldJointDef();
				wjd.Initialize(circleBody, body, circleBody.GetPosition());
				this.m_world.CreateJoint(wjd);
			}
		},
		
		
		//======================
		// Member Data 
		//======================
		"private var",{ m_joint1/*:b2RevoluteJoint*/:null},
		"private var",{ m_joint2/*:b2PrismaticJoint*/:null},
		
		"public var",{ m_gJoint1/*:b2RevoluteJoint*/:null},
		"public var",{ m_gJoint2/*:b2RevoluteJoint*/:null},
		"public var",{ m_gJoint3/*:b2PrismaticJoint*/:null},
		"public var",{ m_gJoint4/*:b2GearJoint*/:null},
		"public var",{ m_gJoint5/*:b2GearJoint*/:null},
		
	];},[],["TestBed.Test","Main","Box2D.Dynamics.b2FixtureDef","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.Joints.b2RevoluteJointDef","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Box2D.Common.Math.b2Vec2","Math","Box2D.Dynamics.Joints.b2RevoluteJoint","Box2D.Dynamics.Joints.b2PrismaticJointDef","Box2D.Dynamics.Joints.b2PrismaticJoint","Box2D.Collision.Shapes.b2CircleShape","Box2D.Dynamics.Joints.b2GearJointDef","Box2D.Dynamics.Joints.b2GearJoint","Box2D.Dynamics.Joints.b2PulleyJointDef","Box2D.Dynamics.Joints.b2PulleyJoint","Box2D.Dynamics.Joints.b2LineJointDef","Box2D.Dynamics.Joints.b2FrictionJointDef","Box2D.Dynamics.Joints.b2WeldJointDef"], "0.8.0", "0.8.1"
	
);