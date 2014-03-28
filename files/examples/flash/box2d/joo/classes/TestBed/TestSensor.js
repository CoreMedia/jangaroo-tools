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
	import Box2D.Dynamics.Controllers.*
	import Box2D.Common.*
	import Box2D.Common.Math.**/
	
	
	
	"public class TestSensor extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Main,Box2D.Dynamics.b2Body);},
		
		"public function TestSensor",function TestSensor$(){this.super$2();
			
			var fd/*:b2FixtureDef*/ = new Box2D.Dynamics.b2FixtureDef();
			fd.shape = new Box2D.Collision.Shapes.b2CircleShape(.5);
			fd.density = 1.0;
			
			var bd/*:b2BodyDef*/ = new Box2D.Dynamics.b2BodyDef();
			bd.type = Box2D.Dynamics.b2Body.b2_dynamicBody;
			for (var i/*:int*/ = 0; i < 8; i++)
			{
				bd.position.Set(4 + i*1.5, 5);
				var body/*:b2Body*/ = this.m_world.CreateBody(bd);
				body.CreateFixture(fd);
			}
					
			// Make a large sensor in the center
			bd.type = Box2D.Dynamics.b2Body.b2_staticBody;
			bd.position.Set(10, 6);
			var sensorBody/*:b2Body*/ = this.m_world.CreateBody(bd);
			var fixture/*:b2Fixture*/ = sensorBody.CreateFixture2(new Box2D.Collision.Shapes.b2CircleShape(2), 0.0);
			fixture.SetSensor(true);
			
			// Fit a custom controller to it
			var controller/*:MyController*/ = new TestBed.TestSensor.MyController();
			controller.position.SetV(bd.position);
			controller.SetBodyIterable(new Box2D.Dynamics.Controllers.b2TouchingBodyIterable(sensorBody));
			this.m_world.AddController(controller);
			
			
			// Set Text field
			Main.m_aboutText.text = "Sensor";
			
		},
		
		
		
		//======================
		// Member Data 
		//======================
		
	/*

import Box2D.Dynamics.*
import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Dynamics.Joints.*
import Box2D.Dynamics.Contacts.*
import Box2D.Dynamics.Controllers.*
import Box2D.Common.*
import Box2D.Common.Math.**/
	

/**
 * Demonstration of implementing your own controller.
 * Controllers are just a convenience for grouping functionality - you can 
 * achieve the same effects directly with the API.
 * 
 * This is a very simple controller. See Box2D.Dynamics.Controllers for some better implemented ones
 * that provide debug drawing and work more efficiently.
 */
"internal class MyController extends Box2D.Dynamics.Controllers.b2Controller",2,function($$private){;return[

	"public var",{ position/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"public var",{ strength/*:Number*/ : 1.0},
	
	"override public function Step",function Step(step/*:b2TimeStep*/)/*:void*/ 
	{
		var iterator/*:IBodyIterator*/ = this.m_bodyIterable.GetIterator();
		while(iterator.HasNext())
		{
			var body/*:b2Body*/ = iterator.Next();
			var d/*:b2Vec2*/ = Box2D.Common.Math.b2Math.SubtractVV(this.position, body.GetPosition());
			d.Multiply(this.strength);
			body.ApplyForce(d, body.GetPosition());
		}
	},
"public function MyController",function MyController$(){this.super$2();this.position=this.position();}];},[],];},[],["TestBed.Test","Box2D.Dynamics.b2FixtureDef","Box2D.Collision.Shapes.b2CircleShape","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Box2D.Dynamics.Controllers.b2TouchingBodyIterable","Main","Box2D.Dynamics.Controllers.b2Controller","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"
	
);