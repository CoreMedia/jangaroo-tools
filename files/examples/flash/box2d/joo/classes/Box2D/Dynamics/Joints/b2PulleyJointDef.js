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
* Pulley joint definition. This requires two ground anchors,
* two dynamic body anchor points, max lengths for each side,
* and a pulley ratio.
* @see b2PulleyJoint
*/

"public class b2PulleyJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint,Box2D.Dynamics.Joints.b2PulleyJoint);},

	"public function b2PulleyJointDef",function b2PulleyJointDef$()
	{this.super$2();this.groundAnchorA=this.groundAnchorA();this.groundAnchorB=this.groundAnchorB();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();
		this.type = Box2D.Dynamics.Joints.b2Joint.e_pulleyJoint;
		this.groundAnchorA.Set(-1.0, 1.0);
		this.groundAnchorB.Set(1.0, 1.0);
		this.localAnchorA.Set(-1.0, 0.0);
		this.localAnchorB.Set(1.0, 0.0);
		this.lengthA = 0.0;
		this.maxLengthA = 0.0;
		this.lengthB = 0.0;
		this.maxLengthB = 0.0;
		this.ratio = 1.0;
		this.collideConnected = true;
	},
	
	"public function Initialize",function Initialize(bA/*:b2Body*/, bB/*:b2Body*/,
				gaA/*:b2Vec2*/, gaB/*:b2Vec2*/,
				anchorA/*:b2Vec2*/, anchorB/*:b2Vec2*/,
				r/*:Number*/)/* : void*/
	{
		this.bodyA = bA;
		this.bodyB = bB;
		this.groundAnchorA.SetV( gaA );
		this.groundAnchorB.SetV( gaB );
		this.localAnchorA = this.bodyA.GetLocalPoint(anchorA);
		this.localAnchorB = this.bodyB.GetLocalPoint(anchorB);
		//b2Vec2 d1 = anchorA - gaA;
		var d1X/*:Number*/ = anchorA.x - gaA.x;
		var d1Y/*:Number*/ = anchorA.y - gaA.y;
		//length1 = d1.Length();
		this.lengthA = Math.sqrt(d1X*d1X + d1Y*d1Y);
		
		//b2Vec2 d2 = anchor2 - ga2;
		var d2X/*:Number*/ = anchorB.x - gaB.x;
		var d2Y/*:Number*/ = anchorB.y - gaB.y;
		//length2 = d2.Length();
		this.lengthB = Math.sqrt(d2X*d2X + d2Y*d2Y);
		
		this.ratio = r;
		//b2Settings.b2Assert(ratio > Number.MIN_VALUE);
		var C/*:Number*/ = this.lengthA + this.ratio * this.lengthB;
		this.maxLengthA = C - this.ratio * Box2D.Dynamics.Joints.b2PulleyJoint.b2_minPulleyLength;
		this.maxLengthB = (C - Box2D.Dynamics.Joints.b2PulleyJoint.b2_minPulleyLength) / this.ratio;
	},

	/**
	* The first ground anchor in world coordinates. This point never moves.
	*/
	"public var",{ groundAnchorA/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	
	/**
	* The second ground anchor in world coordinates. This point never moves.
	*/
	"public var",{ groundAnchorB/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	
	/**
	* The local anchor point relative to bodyA's origin.
	*/
	"public var",{ localAnchorA/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	
	/**
	* The local anchor point relative to bodyB's origin.
	*/
	"public var",{ localAnchorB/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	
	/**
	* The a reference length for the segment attached to bodyA.
	*/
	"public var",{ lengthA/*:Number*/:NaN},
	
	/**
	* The maximum length of the segment attached to bodyA.
	*/
	"public var",{ maxLengthA/*:Number*/:NaN},
	
	/**
	* The a reference length for the segment attached to bodyB.
	*/
	"public var",{ lengthB/*:Number*/:NaN},
	
	/**
	* The maximum length of the segment attached to bodyB.
	*/
	"public var",{ maxLengthB/*:Number*/:NaN},
	
	/**
	* The pulley ratio, used to simulate a block-and-tackle.
	*/
	"public var",{ ratio/*:Number*/:NaN},
	
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Math","Box2D.Dynamics.Joints.b2PulleyJoint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"

);