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
	
	
	
	"public class TestRagdoll extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
		
		"public function TestRagdoll",function TestRagdoll$(){this.super$2();
			
			// Set Text field
			Main.m_aboutText.text = "Ragdolls";
			
			var circ/*:b2CircleShape*/; 
			var box/*:b2PolygonShape*/;
			var bd/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
			var jd/*:b2RevoluteJointDef*/ = new Box2D.Dynamics.Joints.b2RevoluteJointDef();
			var fixtureDef/*:b2FixtureDef*/ = new Box2D.Dynamics.b2FixtureDef();
			
			// Add 5 ragdolls along the top
			for (var i/*:int*/ = 0; i < 2; i++){
				var startX/*:Number*/ = 70 + Math.random() * 20 + 480 * i;
				var startY/*:Number*/ = 20 + Math.random() * 50;
				
				// BODIES
				// Set these to dynamic bodies
				bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				
				// Head
				circ = new Box2D.Collision.Shapes.b2CircleShape( 12.5 / this.m_physScale );
				fixtureDef.shape = circ;
				fixtureDef.density = 1.0;
				fixtureDef.friction = 0.4;
				fixtureDef.restitution = 0.3;
				bd.position.Set(startX / this.m_physScale, startY / this.m_physScale);
				var head/*:b2Body*/ = this.m_world.CreateBody(bd);
				head.CreateFixture(fixtureDef);
				//if (i == 0){
					head.ApplyLinearImpulse(new Box2D.Common.Math.b2Vec2(Math.random() * 100 - 50, Math.random() * 100 - 50), head.GetWorldCenter());
				//}
				
				// Torso1
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(15 / this.m_physScale, 10 / this.m_physScale);
				fixtureDef.shape = box;
				fixtureDef.density = 1.0;
				fixtureDef.friction = 0.4;
				fixtureDef.restitution = 0.1;
				bd.position.Set(startX / this.m_physScale, (startY + 28) / this.m_physScale);
				var torso1/*:b2Body*/ = this.m_world.CreateBody(bd);
				torso1.CreateFixture(fixtureDef);
				// Torso2
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(15 / this.m_physScale, 10 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set(startX / this.m_physScale, (startY + 43) / this.m_physScale);
				var torso2/*:b2Body*/ = this.m_world.CreateBody(bd);
				torso2.CreateFixture(fixtureDef);
				// Torso3
				box.SetAsBox(15 / this.m_physScale, 10 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set(startX / this.m_physScale, (startY + 58) / this.m_physScale);
				var torso3/*:b2Body*/ = this.m_world.CreateBody(bd);
				torso3.CreateFixture(fixtureDef);
				
				// UpperArm
				fixtureDef.density = 1.0;
				fixtureDef.friction = 0.4;
				fixtureDef.restitution = 0.1;
				// L
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(18 / this.m_physScale, 6.5 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((startX - 30) / this.m_physScale, (startY + 20) / this.m_physScale);
				var upperArmL/*:b2Body*/ = this.m_world.CreateBody(bd);
				upperArmL.CreateFixture(fixtureDef);
				// R
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(18 / this.m_physScale, 6.5 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((startX + 30) / this.m_physScale, (startY + 20) / this.m_physScale);
				var upperArmR/*:b2Body*/ = this.m_world.CreateBody(bd);
				upperArmR.CreateFixture(fixtureDef);
				
				// LowerArm
				fixtureDef.density = 1.0;
				fixtureDef.friction = 0.4;
				fixtureDef.restitution = 0.1;
				// L
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(17 / this.m_physScale, 6 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((startX - 57) / this.m_physScale, (startY + 20) / this.m_physScale);
				var lowerArmL/*:b2Body*/ = this.m_world.CreateBody(bd);
				lowerArmL.CreateFixture(fixtureDef);
				// R
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(17 / this.m_physScale, 6 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((startX + 57) / this.m_physScale, (startY + 20) / this.m_physScale);
				var lowerArmR/*:b2Body*/ = this.m_world.CreateBody(bd);
				lowerArmR.CreateFixture(fixtureDef);
				
				// UpperLeg
				fixtureDef.density = 1.0;
				fixtureDef.friction = 0.4;
				fixtureDef.restitution = 0.1;
				// L
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(7.5 / this.m_physScale, 22 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((startX - 8) / this.m_physScale, (startY + 85) / this.m_physScale);
				var upperLegL/*:b2Body*/ = this.m_world.CreateBody(bd);
				upperLegL.CreateFixture(fixtureDef);
				// R
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(7.5 / this.m_physScale, 22 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((startX + 8) / this.m_physScale, (startY + 85) / this.m_physScale);
				var upperLegR/*:b2Body*/ = this.m_world.CreateBody(bd);
				upperLegR.CreateFixture(fixtureDef);
				
				// LowerLeg
				fixtureDef.density = 1.0;
				fixtureDef.friction = 0.4;
				fixtureDef.restitution = 0.1;
				// L
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(6 / this.m_physScale, 20 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((startX - 8) / this.m_physScale, (startY + 120) / this.m_physScale);
				var lowerLegL/*:b2Body*/ = this.m_world.CreateBody(bd);
				lowerLegL.CreateFixture(fixtureDef);
				// R
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox(6 / this.m_physScale, 20 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((startX + 8) / this.m_physScale, (startY + 120) / this.m_physScale);
				var lowerLegR/*:b2Body*/ = this.m_world.CreateBody(bd);
				lowerLegR.CreateFixture(fixtureDef);
				
				
				// JOINTS
				jd.enableLimit = true;
				
				// Head to shoulders
				jd.lowerAngle = -40 / (180/Math.PI);
				jd.upperAngle = 40 / (180/Math.PI);
				jd.Initialize(torso1, head, new Box2D.Common.Math.b2Vec2(startX / this.m_physScale, (startY + 15) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				
				// Upper arm to shoulders
				// L
				jd.lowerAngle = -85 / (180/Math.PI);
				jd.upperAngle = 130 / (180/Math.PI);
				jd.Initialize(torso1, upperArmL, new Box2D.Common.Math.b2Vec2((startX - 18) / this.m_physScale, (startY + 20) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				// R
				jd.lowerAngle = -130 / (180/Math.PI);
				jd.upperAngle = 85 / (180/Math.PI);
				jd.Initialize(torso1, upperArmR, new Box2D.Common.Math.b2Vec2((startX + 18) / this.m_physScale, (startY + 20) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				
				// Lower arm to upper arm
				// L
				jd.lowerAngle = -130 / (180/Math.PI);
				jd.upperAngle = 10 / (180/Math.PI);
				jd.Initialize(upperArmL, lowerArmL, new Box2D.Common.Math.b2Vec2((startX - 45) / this.m_physScale, (startY + 20) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				// R
				jd.lowerAngle = -10 / (180/Math.PI);
				jd.upperAngle = 130 / (180/Math.PI);
				jd.Initialize(upperArmR, lowerArmR, new Box2D.Common.Math.b2Vec2((startX + 45) / this.m_physScale, (startY + 20) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				
				// Shoulders/stomach
				jd.lowerAngle = -15 / (180/Math.PI);
				jd.upperAngle = 15 / (180/Math.PI);
				jd.Initialize(torso1, torso2, new Box2D.Common.Math.b2Vec2(startX / this.m_physScale, (startY + 35) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				// Stomach/hips
				jd.Initialize(torso2, torso3, new Box2D.Common.Math.b2Vec2(startX / this.m_physScale, (startY + 50) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				
				// Torso to upper leg
				// L
				jd.lowerAngle = -25 / (180/Math.PI);
				jd.upperAngle = 45 / (180/Math.PI);
				jd.Initialize(torso3, upperLegL, new Box2D.Common.Math.b2Vec2((startX - 8) / this.m_physScale, (startY + 72) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				// R
				jd.lowerAngle = -45 / (180/Math.PI);
				jd.upperAngle = 25 / (180/Math.PI);
				jd.Initialize(torso3, upperLegR, new Box2D.Common.Math.b2Vec2((startX + 8) / this.m_physScale, (startY + 72) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				
				// Upper leg to lower leg
				// L
				jd.lowerAngle = -25 / (180/Math.PI);
				jd.upperAngle = 115 / (180/Math.PI);
				jd.Initialize(upperLegL, lowerLegL, new Box2D.Common.Math.b2Vec2((startX - 8) / this.m_physScale, (startY + 105) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				// R
				jd.lowerAngle = -115 / (180/Math.PI);
				jd.upperAngle = 25 / (180/Math.PI);
				jd.Initialize(upperLegR, lowerLegR, new Box2D.Common.Math.b2Vec2((startX + 8) / this.m_physScale, (startY + 105) / this.m_physScale));
				this.m_world.CreateJoint(jd);
				
			}
			
			
			// Add stairs on the left, these are static bodies so set the type accordingly
			bd.type = Box2D.Dynamics.b2Body.b2_staticBody;
			fixtureDef.density = 0.0;
			fixtureDef.friction = 0.4;
			fixtureDef.restitution = 0.3;
			for (var j/*:int*/ = 1; j <= 10; j++) {
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox((10*j) / this.m_physScale, 10 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((10*j) / this.m_physScale, (150 + 20*j) / this.m_physScale);
				head = this.m_world.CreateBody(bd);
				head.CreateFixture(fixtureDef);
			}
			
			// Add stairs on the right
			for (var k/*:int*/ = 1; k <= 10; k++){
				box = new Box2D.Collision.Shapes.b2PolygonShape();
				box.SetAsBox((10 * k) / this.m_physScale, 10 / this.m_physScale);
				fixtureDef.shape = box;
				bd.position.Set((640-10*k) / this.m_physScale, (150 + 20*k) / this.m_physScale);
				head = this.m_world.CreateBody(bd);
				head.CreateFixture(fixtureDef);
			}
			
			box = new Box2D.Collision.Shapes.b2PolygonShape();
			box.SetAsBox(30 / this.m_physScale, 40 / this.m_physScale);
			fixtureDef.shape = box;
			bd.position.Set(320 / this.m_physScale, 320 / this.m_physScale);
			head = this.m_world.CreateBody(bd);
			head.CreateFixture(fixtureDef);
			
			
		},
		
		
		//======================
		// Member Data 
		//======================
	];},[],["TestBed.Test","Main","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.Joints.b2RevoluteJointDef","Box2D.Dynamics.b2FixtureDef","Math","Box2D.Dynamics.b2Body","Box2D.Collision.Shapes.b2CircleShape","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2PolygonShape"], "0.8.0", "0.8.1"
	
);