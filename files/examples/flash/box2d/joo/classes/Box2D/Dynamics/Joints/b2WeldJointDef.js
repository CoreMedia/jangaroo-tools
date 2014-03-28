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
import Box2D.Dynamics.Joints.*
import Box2D.Dynamics.*

import Box2D.Common.b2internal
use namespace b2internal*/


/**
 * Weld joint definition. You need to specify local anchor points
 * where they are attached and the relative body angle. The position
 * of the anchor points is important for computing the reaction torque.
 * @see b2WeldJoint
 */
"public class b2WeldJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

	"public function b2WeldJointDef",function b2WeldJointDef$()
	{this.super$2();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();
		this.type = Box2D.Dynamics.Joints.b2Joint.e_weldJoint;
		this.referenceAngle = 0.0;
	},
	
	/**
	 * Initialize the bodies, anchors, axis, and reference angle using the world
	 * anchor and world axis.
	 */
	"public function Initialize",function Initialize(bA/*:b2Body*/, bB/*:b2Body*/,
								anchor/*:b2Vec2*/)/* : void*/
	{
		this.bodyA = bA;
		this.bodyB = bB;
		this.localAnchorA.SetV( this.bodyA.GetLocalPoint(anchor));
		this.localAnchorB.SetV( this.bodyB.GetLocalPoint(anchor));
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
	 * The body2 angle minus body1 angle in the reference state (radians).
	 */
	"public var",{ referenceAngle/*:Number*/:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"

);