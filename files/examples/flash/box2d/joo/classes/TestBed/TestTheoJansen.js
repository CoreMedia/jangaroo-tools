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
	// Input
	import General.Input*/
	
	
	
	"public class TestTheoJansen extends TestBed.Test",2,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
		
		"public function TestTheoJansen",function TestTheoJansen$(){this.super$2();this.m_offset$2=this.m_offset$2();
			
			// Set Text field
			Main.m_aboutText.text = "Theo Jansen Walker";
			
			// scale walker by variable to easily change size
			this.tScale$2 = this.m_physScale * 2;
			
			// Set position in world space
			this.m_offset$2.Set(120.0/this.m_physScale, 250/this.m_physScale);
			this.m_motorSpeed$2 = -2.0;
			this.m_motorOn$2 = true;
			var pivot/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(0.0, -24.0/this.tScale$2);
			
			var pd/*:b2PolygonShape*/;
			var cd/*:b2CircleShape*/;
			var fd/*:b2FixtureDef*/;
			var bd/*:b2BodyDef*/;
			var body/*:b2Body*/;
			
			for (var i/*:int*/ = 0; i < 40; ++i)
			{
				cd = new Box2D.Collision.Shapes.b2CircleShape(7.5/this.tScale$2);
				
				bd = new Box2D.Dynamics.b2BodyDef();
				bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				// Position in world space
				bd.position.Set((Math.random() * 620 + 10)/this.m_physScale, 350/this.m_physScale);
				
				body = this.m_world.CreateBody(bd);
				body.CreateFixture2(cd, 1.0);
			}
			
			{
				pd = new Box2D.Collision.Shapes.b2PolygonShape();
				pd.SetAsBox(75 / this.tScale$2, 30 / this.tScale$2);
				fd = new Box2D.Dynamics.b2FixtureDef();
				fd.shape = pd;
				fd.density = 1.0;
				fd.filter.groupIndex = -1;
				bd = new Box2D.Dynamics.b2BodyDef();
				bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				//bd.position = pivot + m_offset;
				bd.position = Box2D.Common.Math.b2Math.AddVV(pivot, this.m_offset$2);
				this.m_chassis$2 = this.m_world.CreateBody(bd);
				this.m_chassis$2.CreateFixture(fd);
			}
			
			{
				cd = new Box2D.Collision.Shapes.b2CircleShape(48 / this.tScale$2);
				fd = new Box2D.Dynamics.b2FixtureDef();
				fd.shape = cd;
				fd.density = 1.0;
				fd.filter.groupIndex = -1;
				bd = new Box2D.Dynamics.b2BodyDef();
				bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
				//bd.position = pivot + m_offset;
				bd.position = Box2D.Common.Math.b2Math.AddVV(pivot, this.m_offset$2);
				this.m_wheel$2 = this.m_world.CreateBody(bd);
				this.m_wheel$2.CreateFixture(fd);
			}
			
			{
				var jd/*:b2RevoluteJointDef*/ = new Box2D.Dynamics.Joints.b2RevoluteJointDef();
				var po/*:b2Vec2*/ = pivot.Copy();
				po.Add(this.m_offset$2);
				jd.Initialize(this.m_wheel$2, this.m_chassis$2, po);
				jd.collideConnected = false;
				jd.motorSpeed = this.m_motorSpeed$2;
				jd.maxMotorTorque = 400.0;
				jd.enableMotor = this.m_motorOn$2;
				this.m_motorJoint$2 =as( this.m_world.CreateJoint(jd),  Box2D.Dynamics.Joints.b2RevoluteJoint);
			}
			
			var wheelAnchor/*:b2Vec2*/;
			
			//wheelAnchor = pivot + b2Vec2(0.0f, -0.8);
			wheelAnchor = new Box2D.Common.Math.b2Vec2(0.0, 24.0/this.tScale$2);
			wheelAnchor.Add(pivot);
			
			this.CreateLeg$2(-1.0, wheelAnchor);
			this.CreateLeg$2(1.0, wheelAnchor);
			
			this.m_wheel$2.SetPositionAndAngle(this.m_wheel$2.GetPosition(), 120.0 * Math.PI / 180.0);
			this.CreateLeg$2(-1.0, wheelAnchor);
			this.CreateLeg$2(1.0, wheelAnchor);
			
			this.m_wheel$2.SetPositionAndAngle(this.m_wheel$2.GetPosition(), -120.0 * Math.PI / 180.0);
			this.CreateLeg$2(-1.0, wheelAnchor);
			this.CreateLeg$2(1.0, wheelAnchor);
			
		},
		
		
		
		"private function CreateLeg",function CreateLeg(s/*:Number*/, wheelAnchor/*:b2Vec2*/)/*:void*/{
			
			var p1/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(162 * s/this.tScale$2, 183/this.tScale$2);
			var p2/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(216 * s/this.tScale$2, 36 /this.tScale$2);
			var p3/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(129 * s/this.tScale$2, 57 /this.tScale$2);
			var p4/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2( 93 * s/this.tScale$2, -24  /this.tScale$2);
			var p5/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(180 * s/this.tScale$2, -45  /this.tScale$2);
			var p6/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2( 75 * s/this.tScale$2, -111 /this.tScale$2);
			
			//b2PolygonDef sd1, sd2;
			var sd1/*:b2PolygonShape*/ = new Box2D.Collision.Shapes.b2PolygonShape();
			var sd2/*:b2PolygonShape*/ = new Box2D.Collision.Shapes.b2PolygonShape();
			var fd1/*:b2FixtureDef*/ = new Box2D.Dynamics.b2FixtureDef();
			var fd2/*:b2FixtureDef*/ = new Box2D.Dynamics.b2FixtureDef();
			fd1.shape = sd1;
			fd2.shape = sd2;
			fd1.filter.groupIndex = -1;
			fd2.filter.groupIndex = -1;
			fd1.density = 1.0;
			fd2.density = 1.0;
			
			if (s > 0.0)
			{
				sd1.SetAsArray([p3, p2, p1]);
				sd2.SetAsArray([
					Box2D.Common.Math.b2Math.SubtractVV(p6, p4),
					Box2D.Common.Math.b2Math.SubtractVV(p5, p4),
					new Box2D.Common.Math.b2Vec2()
					]);
			}
			else
			{
				sd1.SetAsArray([p2, p3, p1]);
				sd2.SetAsArray([
					Box2D.Common.Math.b2Math.SubtractVV(p5, p4),
					Box2D.Common.Math.b2Math.SubtractVV(p6, p4),
					new Box2D.Common.Math.b2Vec2()
					]);
			}
			
			//b2BodyDef bd1, bd2;
			var bd1/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
			bd1.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
			var bd2/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
			bd2.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
			bd1.position.SetV(this.m_offset$2);
			bd2.position = Box2D.Common.Math.b2Math.AddVV(p4, this.m_offset$2);
			
			bd1.angularDamping = 10.0;
			bd2.angularDamping = 10.0;
			
			var body1/*:b2Body*/ = this.m_world.CreateBody(bd1);
			var body2/*:b2Body*/ = this.m_world.CreateBody(bd2);
			
			body1.CreateFixture(fd1);
			body2.CreateFixture(fd2);
			
			var djd/*:b2DistanceJointDef*/ = new Box2D.Dynamics.Joints.b2DistanceJointDef();
			
			// Using a soft distance constraint can reduce some jitter.
			// It also makes the structure seem a bit more fluid by
			// acting like a suspension system.
			djd.dampingRatio = 0.5;
			djd.frequencyHz = 10.0;
			
			djd.Initialize(body1, body2, Box2D.Common.Math.b2Math.AddVV(p2, this.m_offset$2), Box2D.Common.Math.b2Math.AddVV(p5, this.m_offset$2));
			this.m_world.CreateJoint(djd);
			
			djd.Initialize(body1, body2, Box2D.Common.Math.b2Math.AddVV(p3, this.m_offset$2), Box2D.Common.Math.b2Math.AddVV(p4, this.m_offset$2));
			this.m_world.CreateJoint(djd);
			
			djd.Initialize(body1, this.m_wheel$2, Box2D.Common.Math.b2Math.AddVV(p3, this.m_offset$2), Box2D.Common.Math.b2Math.AddVV(wheelAnchor, this.m_offset$2));
			this.m_world.CreateJoint(djd);
			
			djd.Initialize(body2, this.m_wheel$2, Box2D.Common.Math.b2Math.AddVV(p6, this.m_offset$2), Box2D.Common.Math.b2Math.AddVV(wheelAnchor, this.m_offset$2));
			this.m_world.CreateJoint(djd);
			
			var rjd/*:b2RevoluteJointDef*/ = new Box2D.Dynamics.Joints.b2RevoluteJointDef();
			
			rjd.Initialize(body2, this.m_chassis$2, Box2D.Common.Math.b2Math.AddVV(p4, this.m_offset$2));
			this.m_world.CreateJoint(rjd);
			
		},
		
		
		
		"public override function Update",function Update()/*:void*/{
			
			//case 'a':
			if (General.Input.isKeyPressed(65)){ // A
				this.m_chassis$2.SetAwake(true);
				this.m_motorJoint$2.SetMotorSpeed(-this.m_motorSpeed$2);
			}
			//case 's':
			if (General.Input.isKeyPressed(83)){ // S
				this.m_chassis$2.SetAwake(true);
				this.m_motorJoint$2.SetMotorSpeed(0.0);
			}
			//case 'd':
			if (General.Input.isKeyPressed(68)){ // D
				this.m_chassis$2.SetAwake(true);
				this.m_motorJoint$2.SetMotorSpeed(this.m_motorSpeed$2);
			}
			//case 'm':
			if (General.Input.isKeyPressed(77)){ // M
				this.m_chassis$2.SetAwake(true);
				this.m_motorJoint$2.EnableMotor(!this.m_motorJoint$2.IsMotorEnabled());
			}
			
			// Finally update super class
			this.Update$2();
		},
		
		
		//======================
		// Member Data 
		//======================
		"private var",{ tScale/*:Number*/:NaN},
		
		"private var",{ m_offset/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
		"private var",{ m_chassis/*:b2Body*/:null},
		"private var",{ m_wheel/*:b2Body*/:null},
		"private var",{ m_motorJoint/*:b2RevoluteJoint*/:null},
		"private var",{ m_motorOn/*:Boolean*/ : true},
		"private var",{ m_motorSpeed/*:Number*/:NaN},
		
	];},[],["TestBed.Test","Main","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2CircleShape","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Math","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2FixtureDef","Box2D.Common.Math.b2Math","Box2D.Dynamics.Joints.b2RevoluteJointDef","Box2D.Dynamics.Joints.b2RevoluteJoint","Box2D.Dynamics.Joints.b2DistanceJointDef","General.Input"], "0.8.0", "0.8.1"
	
);