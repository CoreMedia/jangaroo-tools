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
 * Line joint definition. This requires defining a line of
 * motion using an axis and an anchor point. The definition uses local
 * anchor points and a local axis so that the initial configuration
 * can violate the constraint slightly. The joint translation is zero
 * when the local anchor points coincide in world space. Using local
 * anchors and a local axis helps when saving and loading a game.
 * @see b2LineJoint
 */
"public class b2LineJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

	"public function b2LineJointDef",function b2LineJointDef$()
	{this.super$2();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();this.localAxisA=this.localAxisA();
		this.type = Box2D.Dynamics.Joints.b2Joint.e_lineJoint;
		//localAnchor1.SetZero();
		//localAnchor2.SetZero();
		this.localAxisA.Set(1.0, 0.0);
		this.enableLimit = false;
		this.lowerTranslation = 0.0;
		this.upperTranslation = 0.0;
		this.enableMotor = false;
		this.maxMotorForce = 0.0;
		this.motorSpeed = 0.0;
	},
	
	"public function Initialize",function Initialize(bA/*:b2Body*/, bB/*:b2Body*/, anchor/*:b2Vec2*/, axis/*:b2Vec2*/)/* : void*/
	{
		this.bodyA = bA;
		this.bodyB = bB;
		this.localAnchorA = this.bodyA.GetLocalPoint(anchor);
		this.localAnchorB = this.bodyB.GetLocalPoint(anchor);
		this.localAxisA = this.bodyA.GetLocalVector(axis);
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
	* The local translation axis in bodyA.
	*/
	"public var",{ localAxisA/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},

	/**
	* Enable/disable the joint limit.
	*/
	"public var",{ enableLimit/*:Boolean*/:false},

	/**
	* The lower translation limit, usually in meters.
	*/
	"public var",{ lowerTranslation/*:Number*/:NaN},

	/**
	* The upper translation limit, usually in meters.
	*/
	"public var",{ upperTranslation/*:Number*/:NaN},

	/**
	* Enable/disable the joint motor.
	*/
	"public var",{ enableMotor/*:Boolean*/:false},

	/**
	* The maximum motor torque, usually in N-m.
	*/
	"public var",{ maxMotorForce/*:Number*/:NaN},

	/**
	* The desired motor speed in radians per second.
	*/
	"public var",{ motorSpeed/*:Number*/:NaN},

	
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
	
);