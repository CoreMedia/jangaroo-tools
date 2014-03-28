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

"package Box2D.Dynamics.Joints",/*{
	
	
import Box2D.Common.Math.*
import Box2D.Dynamics.*

import Box2D.Common.b2internal
use namespace b2internal*/



/**
* Revolute joint definition. This requires defining an
* anchor point where the bodies are joined. The definition
* uses local anchor points so that the initial configuration
* can violate the constraint slightly. You also need to
* specify the initial relative angle for joint limits. This
* helps when saving and loading a game.
* The local anchor points are measured from the body's origin
* rather than the center of mass because:
* 1. you might not know where the center of mass will be.
* 2. if you add/remove shapes from a body and recompute the mass,
* the joints will be broken.
* @see b2RevoluteJoint
*/

"public class b2RevoluteJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

	"public function b2RevoluteJointDef",function b2RevoluteJointDef$()
	{this.super$2();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();
		this.type = Box2D.Dynamics.Joints.b2Joint.e_revoluteJoint;
		this.localAnchorA.Set(0.0, 0.0);
		this.localAnchorB.Set(0.0, 0.0);
		this.referenceAngle = 0.0;
		this.lowerAngle = 0.0;
		this.upperAngle = 0.0;
		this.maxMotorTorque = 0.0;
		this.motorSpeed = 0.0;
		this.enableLimit = false;
		this.enableMotor = false;
	},

	/**
	* Initialize the bodies, anchors, and reference angle using the world
	* anchor.
	*/
	"public function Initialize",function Initialize(bA/*:b2Body*/, bB/*:b2Body*/, anchor/*:b2Vec2*/)/* : void*/{
		this.bodyA = bA;
		this.bodyB = bB;
		this.localAnchorA = this.bodyA.GetLocalPoint(anchor);
		this.localAnchorB = this.bodyB.GetLocalPoint(anchor);
		this.referenceAngle = this.bodyB.GetAngle() - this.bodyA.GetAngle();
	},

	/**
	* The local anchor point relative to bodyA's origin.
	*/
	"public var",{ localAnchorA/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},

	/**
	* The local anchor point relative to bodyB's origin.
	*/
	"public var",{ localAnchorB/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},

	/**
	* The bodyB angle minus bodyA angle in the reference state (radians).
	*/
	"public var",{ referenceAngle/*:Number*/:NaN},

	/**
	* A flag to enable joint limits.
	*/
	"public var",{ enableLimit/*:Boolean*/:false},

	/**
	* The lower angle for the joint limit (radians).
	*/
	"public var",{ lowerAngle/*:Number*/:NaN},

	/**
	* The upper angle for the joint limit (radians).
	*/
	"public var",{ upperAngle/*:Number*/:NaN},

	/**
	* A flag to enable the joint motor.
	*/
	"public var",{ enableMotor/*:Boolean*/:false},

	/**
	* The desired motor speed. Usually in radians per second.
	*/
	"public var",{ motorSpeed/*:Number*/:NaN},

	/**
	* The maximum motor torque used to achieve the desired motor speed.
	* Usually in N-m.
	*/
	"public var",{ maxMotorTorque/*:Number*/:NaN},
	
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"

);