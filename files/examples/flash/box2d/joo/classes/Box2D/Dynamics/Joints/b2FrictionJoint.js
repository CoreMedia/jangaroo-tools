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

// Point-to-point constraint
// Cdot = v2 - v1
//      = v2 + cross(w2, r2) - v1 - cross(w1, r1)
// J = [-I -r1_skew I r2_skew ]
// Identity used:
// w k % (rx i + ry j) = w * (-ry i + rx j)

// Angle constraint
// Cdot = w2 - w1
// J = [0 0 -1 0 0 1]
// K = invI1 + invI2

/**
 * Friction joint. This is used for top-down friction.
 * It provides 2D translational friction and angular friction.
 * @see b2FrictionJointDef
 */
"public class b2FrictionJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[

	/** @inheritDoc */
	"public override function GetAnchorA",function GetAnchorA()/*:b2Vec2*/{
		return this.m_bodyA.GetWorldPoint(this.m_localAnchorA$2);
	},
	/** @inheritDoc */
	"public override function GetAnchorB",function GetAnchorB()/*:b2Vec2*/{
		return this.m_bodyB.GetWorldPoint(this.m_localAnchorB$2);
	},
	
	/** @inheritDoc */
	"public override function GetReactionForce",function GetReactionForce(inv_dt/*:Number*/)/*:b2Vec2*/
	{
		return new Box2D.Common.Math.b2Vec2(inv_dt * this.m_linearImpulse$2.x, inv_dt * this.m_linearImpulse$2.y);
	},

	/** @inheritDoc */
	"public override function GetReactionTorque",function GetReactionTorque(inv_dt/*:Number*/)/*:Number*/
	{
		//B2_NOT_USED(inv_dt);
		return inv_dt * this.m_angularImpulse$2;
	},
	
	"public function SetMaxForce",function SetMaxForce(force/*:Number*/)/*:void*/
	{
		this.m_maxForce$2 = force;
	},
	
	"public function GetMaxForce",function GetMaxForce()/*:Number*/
	{
		return this.m_maxForce$2;
	},
	
	"public function SetMaxTorque",function SetMaxTorque(torque/*:Number*/)/*:void*/
	{
		this.m_maxTorque$2 = torque;
	},
	
	"public function GetMaxTorque",function GetMaxTorque()/*:Number*/
	{
		return this.m_maxTorque$2;
	},
	
	//--------------- Internals Below -------------------

	/** @private */
	"public function b2FrictionJoint",function b2FrictionJoint$(def/*:b2FrictionJointDef*/){
		this.super$2(def);this.m_localAnchorA$2=this.m_localAnchorA$2();this.m_localAnchorB$2=this.m_localAnchorB$2();this.m_linearMass=this.m_linearMass();this.m_linearImpulse$2=this.m_linearImpulse$2();
		
		this.m_localAnchorA$2.SetV(def.localAnchorA);
		this.m_localAnchorB$2.SetV(def.localAnchorB);
		
		this.m_linearMass.SetZero();
		this.m_angularMass = 0.0;
		
		this.m_linearImpulse$2.SetZero();
		this.m_angularImpulse$2 = 0.0;
		
		this.m_maxForce$2 = def.maxForce;
		this.m_maxTorque$2 = def.maxTorque;
	},

	"b2internal override function InitVelocityConstraints",function InitVelocityConstraints(step/*:b2TimeStep*/)/* : void*/ {
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;
		
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;

		// Compute the effective mass matrix.
		//b2Vec2 rA = b2Mul(bA->m_xf.R, m_localAnchorA - bA->GetLocalCenter());
		tMat = bA.m_xf.R;
		var rAX/*:Number*/ = this.m_localAnchorA$2.x - bA.m_sweep.localCenter.x;
		var rAY/*:Number*/ = this.m_localAnchorA$2.y - bA.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * rAX + tMat.col2.x * rAY);
		rAY = (tMat.col1.y * rAX + tMat.col2.y * rAY);
		rAX = tX;
		//b2Vec2 rB = b2Mul(bB->m_xf.R, m_localAnchorB - bB->GetLocalCenter());
		tMat = bB.m_xf.R;
		var rBX/*:Number*/ = this.m_localAnchorB$2.x - bB.m_sweep.localCenter.x;
		var rBY/*:Number*/ = this.m_localAnchorB$2.y - bB.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * rBX + tMat.col2.x * rBY);
		rBY = (tMat.col1.y * rBX + tMat.col2.y * rBY);
		rBX = tX;

		// J = [-I -r1_skew I r2_skew]
		//     [ 0       -1 0       1]
		// r_skew = [-ry; rx]

		// Matlab
		// K = [ mA+r1y^2*iA+mB+r2y^2*iB,  -r1y*iA*r1x-r2y*iB*r2x,          -r1y*iA-r2y*iB]
		//     [  -r1y*iA*r1x-r2y*iB*r2x, mA+r1x^2*iA+mB+r2x^2*iB,           r1x*iA+r2x*iB]
		//     [          -r1y*iA-r2y*iB,           r1x*iA+r2x*iB,                   iA+iB]

		var mA/*:Number*/ = bA.m_invMass;
		var mB/*:Number*/ = bB.m_invMass;
		var iA/*:Number*/ = bA.m_invI;
		var iB/*:Number*/ = bB.m_invI;

		var K/*:b2Mat22*/ = new Box2D.Common.Math.b2Mat22();
		K.col1.x = mA + mB;	K.col2.x = 0.0;
		K.col1.y = 0.0;		K.col2.y = mA + mB;

		K.col1.x+=  iA * rAY * rAY;	K.col2.x+= -iA * rAX * rAY;
		K.col1.y+= -iA * rAX * rAY;	K.col2.y+=  iA * rAX * rAX;

		K.col1.x+=  iB * rBY * rBY;	K.col2.x+= -iB * rBX * rBY;
		K.col1.y+= -iB * rBX * rBY;	K.col2.y+=  iB * rBX * rBX;

		K.GetInverse(this.m_linearMass);

		this.m_angularMass = iA + iB;
		if (this.m_angularMass > 0.0)
		{
			this.m_angularMass = 1.0 / this.m_angularMass;
		}

		if (step.warmStarting)
		{
			// Scale impulses to support a variable time step.
			this.m_linearImpulse$2.x *= step.dtRatio;
			this.m_linearImpulse$2.y *= step.dtRatio;
			this.m_angularImpulse$2 *= step.dtRatio;

			var P/*:b2Vec2*/ = this.m_linearImpulse$2;

			bA.m_linearVelocity.x -= mA * P.x;
			bA.m_linearVelocity.y -= mA * P.y;
			bA.m_angularVelocity -= iA * (rAX * P.y - rAY * P.x + this.m_angularImpulse$2);

			bB.m_linearVelocity.x += mB * P.x;
			bB.m_linearVelocity.y += mB * P.y;
			bB.m_angularVelocity += iB * (rBX * P.y - rBY * P.x + this.m_angularImpulse$2);
		}
		else
		{
			this.m_linearImpulse$2.SetZero();
			this.m_angularImpulse$2 = 0.0;
		}

	},
	
	
	
	"b2internal override function SolveVelocityConstraints",function SolveVelocityConstraints(step/*:b2TimeStep*/)/*: void*/{
		//B2_NOT_USED(step);
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;

		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;

		var vA/*:b2Vec2*/ = bA.m_linearVelocity;
		var wA/*:Number*/ = bA.m_angularVelocity;
		var vB/*:b2Vec2*/ = bB.m_linearVelocity;
		var wB/*:Number*/ = bB.m_angularVelocity;

		var mA/*:Number*/ = bA.m_invMass;
		var mB/*:Number*/ = bB.m_invMass;
		var iA/*:Number*/ = bA.m_invI;
		var iB/*:Number*/ = bB.m_invI;

		//b2Vec2 rA = b2Mul(bA->m_xf.R, m_localAnchorA - bA->GetLocalCenter());
		tMat = bA.m_xf.R;
		var rAX/*:Number*/ = this.m_localAnchorA$2.x - bA.m_sweep.localCenter.x;
		var rAY/*:Number*/ = this.m_localAnchorA$2.y - bA.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * rAX + tMat.col2.x * rAY);
		rAY = (tMat.col1.y * rAX + tMat.col2.y * rAY);
		rAX = tX;
		//b2Vec2 rB = b2Mul(bB->m_xf.R, m_localAnchorB - bB->GetLocalCenter());
		tMat = bB.m_xf.R;
		var rBX/*:Number*/ = this.m_localAnchorB$2.x - bB.m_sweep.localCenter.x;
		var rBY/*:Number*/ = this.m_localAnchorB$2.y - bB.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * rBX + tMat.col2.x * rBY);
		rBY = (tMat.col1.y * rBX + tMat.col2.y * rBY);
		rBX = tX;
		
		var maxImpulse/*:Number*/;

		// Solve angular friction
		{
			var Cdot/*:Number*/ = wB - wA;
			var impulse/*:Number*/ = -this.m_angularMass * Cdot;

			var oldImpulse/*:Number*/ = this.m_angularImpulse$2;
			maxImpulse = step.dt * this.m_maxTorque$2;
			this.m_angularImpulse$2 = Box2D.Common.Math.b2Math.Clamp(this.m_angularImpulse$2 + impulse, -maxImpulse, maxImpulse);
			impulse = this.m_angularImpulse$2 - oldImpulse;

			wA -= iA * impulse;
			wB += iB * impulse;
		}

		// Solve linear friction
		{
			//b2Vec2 Cdot = vB + b2Cross(wB, rB) - vA - b2Cross(wA, rA);
			var CdotX/*:Number*/ = vB.x - wB * rBY - vA.x + wA * rAY;
			var CdotY/*:Number*/ = vB.y + wB * rBX - vA.y - wA * rAX;

			var impulseV/*:b2Vec2*/ = Box2D.Common.Math.b2Math.MulMV(this.m_linearMass, new Box2D.Common.Math.b2Vec2(-CdotX, -CdotY));
			var oldImpulseV/*:b2Vec2*/ = this.m_linearImpulse$2.Copy();
			
			this.m_linearImpulse$2.Add(impulseV);

			maxImpulse = step.dt * this.m_maxForce$2;

			if (this.m_linearImpulse$2.LengthSquared() > maxImpulse * maxImpulse)
			{
				this.m_linearImpulse$2.Normalize();
				this.m_linearImpulse$2.Multiply(maxImpulse);
			}

			impulseV = Box2D.Common.Math.b2Math.SubtractVV(this.m_linearImpulse$2, oldImpulseV);

			vA.x -= mA * impulseV.x;
			vA.y -= mA * impulseV.y;
			wA -= iA * (rAX * impulseV.y - rAY * impulseV.x);

			vB.x += mB * impulseV.x;
			vB.y += mB * impulseV.y;
			wB += iB * (rBX * impulseV.y - rBY * impulseV.x);
		}

		// References has made some sets unnecessary
		//bA->m_linearVelocity = vA;
		bA.m_angularVelocity = wA;
		//bB->m_linearVelocity = vB;
		bB.m_angularVelocity = wB;

	},
	
	"b2internal override function SolvePositionConstraints",function SolvePositionConstraints(baumgarte/*:Number*/)/*:Boolean*/
	{
		//B2_NOT_USED(baumgarte);
		
		return true;
		
	},

	"private var",{ m_localAnchorA/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_localAnchorB/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	
	"public var",{ m_linearMass/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"public var",{ m_angularMass/*:Number*/:NaN},
	
	"private var",{ m_linearImpulse/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_angularImpulse/*:Number*/:NaN},
	
	"private var",{ m_maxForce/*:Number*/:NaN},
	"private var",{ m_maxTorque/*:Number*/:NaN},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"

);