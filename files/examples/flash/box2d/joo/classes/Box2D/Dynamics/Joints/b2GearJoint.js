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
import Box2D.Common.*
import Box2D.Dynamics.*

import Box2D.Common.b2internal
use namespace b2internal*/




/**
* A gear joint is used to connect two joints together. Either joint
* can be a revolute or prismatic joint. You specify a gear ratio
* to bind the motions together:
* coordinate1 + ratio * coordinate2 = constant
* The ratio can be negative or positive. If one joint is a revolute joint
* and the other joint is a prismatic joint, then the ratio will have units
* of length or units of 1/length.
* @warning The revolute and prismatic joints must be attached to
* fixed bodies (which must be body1 on those joints).
* @see b2GearJointDef
*/

"public class b2GearJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint,Box2D.Common.b2Settings);},

	/** @inheritDoc */
	"public override function GetAnchorA",function GetAnchorA()/*:b2Vec2*/{
		//return m_bodyA->GetWorldPoint(m_localAnchor1);
		return this.m_bodyA.GetWorldPoint(this.m_localAnchor1$2);
	},
	/** @inheritDoc */
	"public override function GetAnchorB",function GetAnchorB()/*:b2Vec2*/{
		//return m_bodyB->GetWorldPoint(m_localAnchor2);
		return this.m_bodyB.GetWorldPoint(this.m_localAnchor2$2);
	},
	/** @inheritDoc */
	"public override function GetReactionForce",function GetReactionForce(inv_dt/*:Number*/)/*:b2Vec2*/{
		// TODO_ERIN not tested
		// b2Vec2 P = m_impulse * m_J.linear2;
		//return inv_dt * P;
		return new Box2D.Common.Math.b2Vec2(inv_dt * this.m_impulse$2 * this.m_J$2.linearB.x, inv_dt * this.m_impulse$2 * this.m_J$2.linearB.y);
	},
	/** @inheritDoc */
	"public override function GetReactionTorque",function GetReactionTorque(inv_dt/*:Number*/)/*:Number*/{
		// TODO_ERIN not tested
		//b2Vec2 r = b2Mul(m_bodyB->m_xf.R, m_localAnchor2 - m_bodyB->GetLocalCenter());
		var tMat/*:b2Mat22*/ = this.m_bodyB.m_xf.R;
		var rX/*:Number*/ = this.m_localAnchor1$2.x - this.m_bodyB.m_sweep.localCenter.x;
		var rY/*:Number*/ = this.m_localAnchor1$2.y - this.m_bodyB.m_sweep.localCenter.y;
		var tX/*:Number*/ = tMat.col1.x * rX + tMat.col2.x * rY;
		rY = tMat.col1.y * rX + tMat.col2.y * rY;
		rX = tX;
		//b2Vec2 P = m_impulse * m_J.linearB;
		var PX/*:Number*/ = this.m_impulse$2 * this.m_J$2.linearB.x;
		var PY/*:Number*/ = this.m_impulse$2 * this.m_J$2.linearB.y;
		//float32 L = m_impulse * m_J.angularB - b2Cross(r, P);
		//return inv_dt * L;
		return inv_dt * (this.m_impulse$2 * this.m_J$2.angularB - rX * PY + rY * PX);
	},

	/**
	 * Get the gear ratio.
	 */
	"public function GetRatio",function GetRatio()/*:Number*/{
		return this.m_ratio$2;
	},
	
	/**
	 * Set the gear ratio.
	 */
	"public function SetRatio",function SetRatio(ratio/*:Number*/)/*:void*/ {
		//b2Settings.b2Assert(b2Math.b2IsValid(ratio));
		this.m_ratio$2 = ratio;
	},

	//--------------- Internals Below -------------------

	/** @private */
	"public function b2GearJoint",function b2GearJoint$(def/*:b2GearJointDef*/){
		// parent constructor
		this.super$2(def);this.m_groundAnchor1$2=this.m_groundAnchor1$2();this.m_groundAnchor2$2=this.m_groundAnchor2$2();this.m_localAnchor1$2=this.m_localAnchor1$2();this.m_localAnchor2$2=this.m_localAnchor2$2();this.m_J$2=this.m_J$2();
		
		var type1/*:int*/ = def.joint1.m_type;
		var type2/*:int*/ = def.joint2.m_type;
		
		//b2Settings.b2Assert(type1 == b2Joint.e_revoluteJoint || type1 == b2Joint.e_prismaticJoint);
		//b2Settings.b2Assert(type2 == b2Joint.e_revoluteJoint || type2 == b2Joint.e_prismaticJoint);
		//b2Settings.b2Assert(def.joint1.GetBodyA().GetType() == b2Body.b2_staticBody);
		//b2Settings.b2Assert(def.joint2.GetBodyA().GetType() == b2Body.b2_staticBody);
		
		this.m_revolute1$2 = null;
		this.m_prismatic1$2 = null;
		this.m_revolute2$2 = null;
		this.m_prismatic2$2 = null;
		
		var coordinate1/*:Number*/;
		var coordinate2/*:Number*/;
		
		this.m_ground1$2 = def.joint1.GetBodyA();
		this.m_bodyA = def.joint1.GetBodyB();
		if (type1 == Box2D.Dynamics.Joints.b2Joint.e_revoluteJoint)
		{
			this.m_revolute1$2 =as( def.joint1,  Box2D.Dynamics.Joints.b2RevoluteJoint);
			this.m_groundAnchor1$2.SetV( this.m_revolute1$2.m_localAnchor1 );
			this.m_localAnchor1$2.SetV( this.m_revolute1$2.m_localAnchor2 );
			coordinate1 = this.m_revolute1$2.GetJointAngle();
		}
		else
		{
			this.m_prismatic1$2 =as( def.joint1,  Box2D.Dynamics.Joints.b2PrismaticJoint);
			this.m_groundAnchor1$2.SetV( this.m_prismatic1$2.m_localAnchor1 );
			this.m_localAnchor1$2.SetV( this.m_prismatic1$2.m_localAnchor2 );
			coordinate1 = this.m_prismatic1$2.GetJointTranslation();
		}
		
		this.m_ground2$2 = def.joint2.GetBodyA();
		this.m_bodyB = def.joint2.GetBodyB();
		if (type2 == Box2D.Dynamics.Joints.b2Joint.e_revoluteJoint)
		{
			this.m_revolute2$2 =as( def.joint2,  Box2D.Dynamics.Joints.b2RevoluteJoint);
			this.m_groundAnchor2$2.SetV( this.m_revolute2$2.m_localAnchor1 );
			this.m_localAnchor2$2.SetV( this.m_revolute2$2.m_localAnchor2 );
			coordinate2 = this.m_revolute2$2.GetJointAngle();
		}
		else
		{
			this.m_prismatic2$2 =as( def.joint2,  Box2D.Dynamics.Joints.b2PrismaticJoint);
			this.m_groundAnchor2$2.SetV( this.m_prismatic2$2.m_localAnchor1 );
			this.m_localAnchor2$2.SetV( this.m_prismatic2$2.m_localAnchor2 );
			coordinate2 = this.m_prismatic2$2.GetJointTranslation();
		}
		
		this.m_ratio$2 = def.ratio;
		
		this.m_constant$2 = coordinate1 + this.m_ratio$2 * coordinate2;
		
		this.m_impulse$2 = 0.0;
		
	},

	"b2internal override function InitVelocityConstraints",function InitVelocityConstraints(step/*:b2TimeStep*/)/* : void*/{
		var g1/*:b2Body*/ = this.m_ground1$2;
		var g2/*:b2Body*/ = this.m_ground2$2;
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		// temp vars
		var ugX/*:Number*/;
		var ugY/*:Number*/;
		var rX/*:Number*/;
		var rY/*:Number*/;
		var tMat/*:b2Mat22*/;
		var tVec/*:b2Vec2*/;
		var crug/*:Number*/;
		var tX/*:Number*/;
		
		var K/*:Number*/ = 0.0;
		this.m_J$2.SetZero();
		
		if (this.m_revolute1$2)
		{
			this.m_J$2.angularA = -1.0;
			K += bA.m_invI;
		}
		else
		{
			//b2Vec2 ug = b2MulMV(g1->m_xf.R, m_prismatic1->m_localXAxis1);
			tMat = g1.m_xf.R;
			tVec = this.m_prismatic1$2.m_localXAxis1;
			ugX = tMat.col1.x * tVec.x + tMat.col2.x * tVec.y;
			ugY = tMat.col1.y * tVec.x + tMat.col2.y * tVec.y;
			//b2Vec2 r = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
			tMat = bA.m_xf.R;
			rX = this.m_localAnchor1$2.x - bA.m_sweep.localCenter.x;
			rY = this.m_localAnchor1$2.y - bA.m_sweep.localCenter.y;
			tX = tMat.col1.x * rX + tMat.col2.x * rY;
			rY = tMat.col1.y * rX + tMat.col2.y * rY;
			rX = tX;
			
			//var crug:Number = b2Cross(r, ug);
			crug = rX * ugY - rY * ugX;
			//m_J.linearA = -ug;
			this.m_J$2.linearA.Set(-ugX, -ugY);
			this.m_J$2.angularA = -crug;
			K += bA.m_invMass + bA.m_invI * crug * crug;
		}
		
		if (this.m_revolute2$2)
		{
			this.m_J$2.angularB = -this.m_ratio$2;
			K += this.m_ratio$2 * this.m_ratio$2 * bB.m_invI;
		}
		else
		{
			//b2Vec2 ug = b2Mul(g2->m_xf.R, m_prismatic2->m_localXAxis1);
			tMat = g2.m_xf.R;
			tVec = this.m_prismatic2$2.m_localXAxis1;
			ugX = tMat.col1.x * tVec.x + tMat.col2.x * tVec.y;
			ugY = tMat.col1.y * tVec.x + tMat.col2.y * tVec.y;
			//b2Vec2 r = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
			tMat = bB.m_xf.R;
			rX = this.m_localAnchor2$2.x - bB.m_sweep.localCenter.x;
			rY = this.m_localAnchor2$2.y - bB.m_sweep.localCenter.y;
			tX = tMat.col1.x * rX + tMat.col2.x * rY;
			rY = tMat.col1.y * rX + tMat.col2.y * rY;
			rX = tX;
			
			//float32 crug = b2Cross(r, ug);
			crug = rX * ugY - rY * ugX;
			//m_J.linearB = -m_ratio * ug;
			this.m_J$2.linearB.Set(-this.m_ratio$2*ugX, -this.m_ratio$2*ugY);
			this.m_J$2.angularB = -this.m_ratio$2 * crug;
			K += this.m_ratio$2 * this.m_ratio$2 * (bB.m_invMass + bB.m_invI * crug * crug);
		}
		
		// Compute effective mass.
		this.m_mass$2 = K > 0.0?1.0 / K:0.0;
		
		if (step.warmStarting)
		{
			// Warm starting.
			//bA.m_linearVelocity += bA.m_invMass * m_impulse * m_J.linearA;
			bA.m_linearVelocity.x += bA.m_invMass * this.m_impulse$2 * this.m_J$2.linearA.x;
			bA.m_linearVelocity.y += bA.m_invMass * this.m_impulse$2 * this.m_J$2.linearA.y;
			bA.m_angularVelocity += bA.m_invI * this.m_impulse$2 * this.m_J$2.angularA;
			//bB.m_linearVelocity += bB.m_invMass * m_impulse * m_J.linearB;
			bB.m_linearVelocity.x += bB.m_invMass * this.m_impulse$2 * this.m_J$2.linearB.x;
			bB.m_linearVelocity.y += bB.m_invMass * this.m_impulse$2 * this.m_J$2.linearB.y;
			bB.m_angularVelocity += bB.m_invI * this.m_impulse$2 * this.m_J$2.angularB;
		}
		else
		{
			this.m_impulse$2 = 0.0;
		}
	},
	
	"b2internal override function SolveVelocityConstraints",function SolveVelocityConstraints(step/*:b2TimeStep*/)/*: void*/
	{
		//B2_NOT_USED(step);
		
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var Cdot/*:Number*/ = this.m_J$2.Compute(	bA.m_linearVelocity, bA.m_angularVelocity,
										bB.m_linearVelocity, bB.m_angularVelocity);
		
		var impulse/*:Number*/ = - this.m_mass$2 * Cdot;
		this.m_impulse$2 += impulse;
		
		bA.m_linearVelocity.x += bA.m_invMass * impulse * this.m_J$2.linearA.x;
		bA.m_linearVelocity.y += bA.m_invMass * impulse * this.m_J$2.linearA.y;
		bA.m_angularVelocity  += bA.m_invI * impulse * this.m_J$2.angularA;
		bB.m_linearVelocity.x += bB.m_invMass * impulse * this.m_J$2.linearB.x;
		bB.m_linearVelocity.y += bB.m_invMass * impulse * this.m_J$2.linearB.y;
		bB.m_angularVelocity  += bB.m_invI * impulse * this.m_J$2.angularB;
	},
	
	"b2internal override function SolvePositionConstraints",function SolvePositionConstraints(baumgarte/*:Number*/)/*:Boolean*/
	{
		//B2_NOT_USED(baumgarte);
		
		var linearError/*:Number*/ = 0.0;
		
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var coordinate1/*:Number*/;
		var coordinate2/*:Number*/;
		if (this.m_revolute1$2)
		{
			coordinate1 = this.m_revolute1$2.GetJointAngle();
		}
		else
		{
			coordinate1 = this.m_prismatic1$2.GetJointTranslation();
		}
		
		if (this.m_revolute2$2)
		{
			coordinate2 = this.m_revolute2$2.GetJointAngle();
		}
		else
		{
			coordinate2 = this.m_prismatic2$2.GetJointTranslation();
		}
		
		var C/*:Number*/ = this.m_constant$2 - (coordinate1 + this.m_ratio$2 * coordinate2);
		
		var impulse/*:Number*/ = -this.m_mass$2 * C;
		
		bA.m_sweep.c.x += bA.m_invMass * impulse * this.m_J$2.linearA.x;
		bA.m_sweep.c.y += bA.m_invMass * impulse * this.m_J$2.linearA.y;
		bA.m_sweep.a += bA.m_invI * impulse * this.m_J$2.angularA;
		bB.m_sweep.c.x += bB.m_invMass * impulse * this.m_J$2.linearB.x;
		bB.m_sweep.c.y += bB.m_invMass * impulse * this.m_J$2.linearB.y;
		bB.m_sweep.a += bB.m_invI * impulse * this.m_J$2.angularB;
		
		bA.SynchronizeTransform();
		bB.SynchronizeTransform();
		
		// TODO_ERIN not implemented
		return linearError < Box2D.Common.b2Settings.b2_linearSlop;
	},

	"private var",{ m_ground1/*:b2Body*/:null},
	"private var",{ m_ground2/*:b2Body*/:null},

	// One of these is NULL.
	"private var",{ m_revolute1/*:b2RevoluteJoint*/:null},
	"private var",{ m_prismatic1/*:b2PrismaticJoint*/:null},

	// One of these is NULL.
	"private var",{ m_revolute2/*:b2RevoluteJoint*/:null},
	"private var",{ m_prismatic2/*:b2PrismaticJoint*/:null},

	"private var",{ m_groundAnchor1/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_groundAnchor2/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},

	"private var",{ m_localAnchor1/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_localAnchor2/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},

	"private var",{ m_J/*:b2Jacobian*/ :function(){return( new Box2D.Dynamics.Joints.b2Jacobian());}},

	"private var",{ m_constant/*:Number*/:NaN},
	"private var",{ m_ratio/*:Number*/:NaN},

	// Effective mass
	"private var",{ m_mass/*:Number*/:NaN},

	// Impulse for accumulation/warm starting.
	"private var",{ m_impulse/*:Number*/:NaN},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Dynamics.Joints.b2RevoluteJoint","Box2D.Dynamics.Joints.b2PrismaticJoint","Box2D.Common.b2Settings","Box2D.Dynamics.Joints.b2Jacobian"], "0.8.0", "0.8.1"


);