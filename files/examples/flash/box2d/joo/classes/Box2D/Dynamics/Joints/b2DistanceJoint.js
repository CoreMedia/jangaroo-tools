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

// 1-D constrained system
// m (v2 - v1) = lambda
// v2 + (beta/h) * x1 + gamma * lambda = 0, gamma has units of inverse mass.
// x2 = x1 + h * v2

// 1-D mass-damper-spring system
// m (v2 - v1) + h * d * v2 + h * k * 

// C = norm(p2 - p1) - L
// u = (p2 - p1) / norm(p2 - p1)
// Cdot = dot(u, v2 + cross(w2, r2) - v1 - cross(w1, r1))
// J = [-u -cross(r1, u) u cross(r2, u)]
// K = J * invM * JT
//   = invMass1 + invI1 * cross(r1, u)^2 + invMass2 + invI2 * cross(r2, u)^2

/**
* A distance joint constrains two points on two bodies
* to remain at a fixed distance from each other. You can view
* this as a massless, rigid rod.
* @see b2DistanceJointDef
*/
"public class b2DistanceJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Math,Box2D.Common.b2Settings);},

	/** @inheritDoc */
	"public override function GetAnchorA",function GetAnchorA()/*:b2Vec2*/{
		return this.m_bodyA.GetWorldPoint(this.m_localAnchor1$2);
	},
	/** @inheritDoc */
	"public override function GetAnchorB",function GetAnchorB()/*:b2Vec2*/{
		return this.m_bodyB.GetWorldPoint(this.m_localAnchor2$2);
	},
	
	/** @inheritDoc */
	"public override function GetReactionForce",function GetReactionForce(inv_dt/*:Number*/)/*:b2Vec2*/
	{
		//b2Vec2 F = (m_inv_dt * m_impulse) * m_u;
		//return F;
		return new Box2D.Common.Math.b2Vec2(inv_dt * this.m_impulse$2 * this.m_u$2.x, inv_dt * this.m_impulse$2 * this.m_u$2.y);
	},

	/** @inheritDoc */
	"public override function GetReactionTorque",function GetReactionTorque(inv_dt/*:Number*/)/*:Number*/
	{
		//B2_NOT_USED(inv_dt);
		return 0.0;
	},
	
	/// Set the natural length
	"public function GetLength",function GetLength()/*:Number*/
	{
		return this.m_length$2;
	},
	
	/// Get the natural length
	/// Manipulating the length can lead to non-physical behavior when the frequency is zero.
	"public function SetLength",function SetLength(length/*:Number*/)/*:void*/
	{
		this.m_length$2 = length;
	},
	
	/// Get the frequency in Hz
	"public function GetFrequency",function GetFrequency()/*:Number*/
	{
		return this.m_frequencyHz$2;
	},
	
	/// Set the frequency in Hz
	"public function SetFrequency",function SetFrequency(hz/*:Number*/)/*:void*/
	{
		this.m_frequencyHz$2 = hz;
	},
	
	/// Get damping ratio
	"public function GetDampingRatio",function GetDampingRatio()/*:Number*/
	{
		return this.m_dampingRatio$2;
	},
	
	/// Set damping ratio
	"public function SetDampingRatio",function SetDampingRatio(ratio/*:Number*/)/*:void*/
	{
		this.m_dampingRatio$2 = ratio;
	},
	
	//--------------- Internals Below -------------------

	/** @private */
	"public function b2DistanceJoint",function b2DistanceJoint$(def/*:b2DistanceJointDef*/){
		this.super$2(def);this.m_localAnchor1$2=this.m_localAnchor1$2();this.m_localAnchor2$2=this.m_localAnchor2$2();this.m_u$2=this.m_u$2();
		
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;
		var tY/*:Number*/;
		this.m_localAnchor1$2.SetV(def.localAnchorA);
		this.m_localAnchor2$2.SetV(def.localAnchorB);
		
		this.m_length$2 = def.length;
		this.m_frequencyHz$2 = def.frequencyHz;
		this.m_dampingRatio$2 = def.dampingRatio;
		this.m_impulse$2 = 0.0;
		this.m_gamma$2 = 0.0;
		this.m_bias$2 = 0.0;
	},

	"b2internal override function InitVelocityConstraints",function InitVelocityConstraints(step/*:b2TimeStep*/)/* : void*/{
		
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;
		
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		// Compute the effective mass matrix.
		//b2Vec2 r1 = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
		tMat = bA.m_xf.R;
		var r1X/*:Number*/ = this.m_localAnchor1$2.x - bA.m_sweep.localCenter.x;
		var r1Y/*:Number*/ = this.m_localAnchor1$2.y - bA.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
		r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
		r1X = tX;
		//b2Vec2 r2 = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
		tMat = bB.m_xf.R;
		var r2X/*:Number*/ = this.m_localAnchor2$2.x - bB.m_sweep.localCenter.x;
		var r2Y/*:Number*/ = this.m_localAnchor2$2.y - bB.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
		r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
		r2X = tX;
		
		//m_u = bB->m_sweep.c + r2 - bA->m_sweep.c - r1;
		this.m_u$2.x = bB.m_sweep.c.x + r2X - bA.m_sweep.c.x - r1X;
		this.m_u$2.y = bB.m_sweep.c.y + r2Y - bA.m_sweep.c.y - r1Y;
		
		// Handle singularity.
		//float32 length = m_u.Length();
		var length/*:Number*/ = Math.sqrt(this.m_u$2.x*this.m_u$2.x + this.m_u$2.y*this.m_u$2.y);
		if (length > Box2D.Common.b2Settings.b2_linearSlop)
		{
			//m_u *= 1.0 / length;
			this.m_u$2.Multiply( 1.0 / length );
		}
		else
		{
			this.m_u$2.SetZero();
		}
		
		//float32 cr1u = b2Cross(r1, m_u);
		var cr1u/*:Number*/ = (r1X * this.m_u$2.y - r1Y * this.m_u$2.x);
		//float32 cr2u = b2Cross(r2, m_u);
		var cr2u/*:Number*/ = (r2X * this.m_u$2.y - r2Y * this.m_u$2.x);
		//m_mass = bA->m_invMass + bA->m_invI * cr1u * cr1u + bB->m_invMass + bB->m_invI * cr2u * cr2u;
		var invMass/*:Number*/ = bA.m_invMass + bA.m_invI * cr1u * cr1u + bB.m_invMass + bB.m_invI * cr2u * cr2u;
		this.m_mass$2 = invMass != 0.0 ? 1.0 / invMass : 0.0;
		
		if (this.m_frequencyHz$2 > 0.0)
		{
			var C/*:Number*/ = length - this.m_length$2;
	
			// Frequency
			var omega/*:Number*/ = 2.0 * Math.PI * this.m_frequencyHz$2;
	
			// Damping coefficient
			var d/*:Number*/ = 2.0 * this.m_mass$2 * this.m_dampingRatio$2 * omega;
	
			// Spring stiffness
			var k/*:Number*/ = this.m_mass$2 * omega * omega;
	
			// magic formulas
			this.m_gamma$2 = step.dt * (d + step.dt * k);
			this.m_gamma$2 = this.m_gamma$2 != 0.0?1 / this.m_gamma$2:0.0;
			this.m_bias$2 = C * step.dt * k * this.m_gamma$2;
	
			this.m_mass$2 = invMass + this.m_gamma$2;
			this.m_mass$2 = this.m_mass$2 != 0.0 ? 1.0 / this.m_mass$2 : 0.0;
		}
		
		if (step.warmStarting)
		{
			// Scale the impulse to support a variable time step
			this.m_impulse$2 *= step.dtRatio;
			
			//b2Vec2 P = m_impulse * m_u;
			var PX/*:Number*/ = this.m_impulse$2 * this.m_u$2.x;
			var PY/*:Number*/ = this.m_impulse$2 * this.m_u$2.y;
			//bA->m_linearVelocity -= bA->m_invMass * P;
			bA.m_linearVelocity.x -= bA.m_invMass * PX;
			bA.m_linearVelocity.y -= bA.m_invMass * PY;
			//bA->m_angularVelocity -= bA->m_invI * b2Cross(r1, P);
			bA.m_angularVelocity -= bA.m_invI * (r1X * PY - r1Y * PX);
			//bB->m_linearVelocity += bB->m_invMass * P;
			bB.m_linearVelocity.x += bB.m_invMass * PX;
			bB.m_linearVelocity.y += bB.m_invMass * PY;
			//bB->m_angularVelocity += bB->m_invI * b2Cross(r2, P);
			bB.m_angularVelocity += bB.m_invI * (r2X * PY - r2Y * PX);
		}
		else
		{
			this.m_impulse$2 = 0.0;
		}
	},
	
	
	
	"b2internal override function SolveVelocityConstraints",function SolveVelocityConstraints(step/*:b2TimeStep*/)/*: void*/{
		
		var tMat/*:b2Mat22*/;
		
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		//b2Vec2 r1 = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
		tMat = bA.m_xf.R;
		var r1X/*:Number*/ = this.m_localAnchor1$2.x - bA.m_sweep.localCenter.x;
		var r1Y/*:Number*/ = this.m_localAnchor1$2.y - bA.m_sweep.localCenter.y;
		var tX/*:Number*/ =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
		r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
		r1X = tX;
		//b2Vec2 r2 = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
		tMat = bB.m_xf.R;
		var r2X/*:Number*/ = this.m_localAnchor2$2.x - bB.m_sweep.localCenter.x;
		var r2Y/*:Number*/ = this.m_localAnchor2$2.y - bB.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
		r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
		r2X = tX;
		
		// Cdot = dot(u, v + cross(w, r))
		//b2Vec2 v1 = bA->m_linearVelocity + b2Cross(bA->m_angularVelocity, r1);
		var v1X/*:Number*/ = bA.m_linearVelocity.x + (-bA.m_angularVelocity * r1Y);
		var v1Y/*:Number*/ = bA.m_linearVelocity.y + (bA.m_angularVelocity * r1X);
		//b2Vec2 v2 = bB->m_linearVelocity + b2Cross(bB->m_angularVelocity, r2);
		var v2X/*:Number*/ = bB.m_linearVelocity.x + (-bB.m_angularVelocity * r2Y);
		var v2Y/*:Number*/ = bB.m_linearVelocity.y + (bB.m_angularVelocity * r2X);
		//float32 Cdot = b2Dot(m_u, v2 - v1);
		var Cdot/*:Number*/ = (this.m_u$2.x * (v2X - v1X) + this.m_u$2.y * (v2Y - v1Y));
		
		var impulse/*:Number*/ = -this.m_mass$2 * (Cdot + this.m_bias$2 + this.m_gamma$2 * this.m_impulse$2);
		this.m_impulse$2 += impulse;
		
		//b2Vec2 P = impulse * m_u;
		var PX/*:Number*/ = impulse * this.m_u$2.x;
		var PY/*:Number*/ = impulse * this.m_u$2.y;
		//bA->m_linearVelocity -= bA->m_invMass * P;
		bA.m_linearVelocity.x -= bA.m_invMass * PX;
		bA.m_linearVelocity.y -= bA.m_invMass * PY;
		//bA->m_angularVelocity -= bA->m_invI * b2Cross(r1, P);
		bA.m_angularVelocity -= bA.m_invI * (r1X * PY - r1Y * PX);
		//bB->m_linearVelocity += bB->m_invMass * P;
		bB.m_linearVelocity.x += bB.m_invMass * PX;
		bB.m_linearVelocity.y += bB.m_invMass * PY;
		//bB->m_angularVelocity += bB->m_invI * b2Cross(r2, P);
		bB.m_angularVelocity += bB.m_invI * (r2X * PY - r2Y * PX);
	},
	
	"b2internal override function SolvePositionConstraints",function SolvePositionConstraints(baumgarte/*:Number*/)/*:Boolean*/
	{
		//B2_NOT_USED(baumgarte);
		
		var tMat/*:b2Mat22*/;
		
		if (this.m_frequencyHz$2 > 0.0)
		{
			// There is no position correction for soft distance constraints
			return true;
		}
		
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		//b2Vec2 r1 = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
		tMat = bA.m_xf.R;
		var r1X/*:Number*/ = this.m_localAnchor1$2.x - bA.m_sweep.localCenter.x;
		var r1Y/*:Number*/ = this.m_localAnchor1$2.y - bA.m_sweep.localCenter.y;
		var tX/*:Number*/ =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
		r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
		r1X = tX;
		//b2Vec2 r2 = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
		tMat = bB.m_xf.R;
		var r2X/*:Number*/ = this.m_localAnchor2$2.x - bB.m_sweep.localCenter.x;
		var r2Y/*:Number*/ = this.m_localAnchor2$2.y - bB.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
		r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
		r2X = tX;
		
		//b2Vec2 d = bB->m_sweep.c + r2 - bA->m_sweep.c - r1;
		var dX/*:Number*/ = bB.m_sweep.c.x + r2X - bA.m_sweep.c.x - r1X;
		var dY/*:Number*/ = bB.m_sweep.c.y + r2Y - bA.m_sweep.c.y - r1Y;
		
		//float32 length = d.Normalize();
		var length/*:Number*/ = Math.sqrt(dX*dX + dY*dY);
		dX /= length;
		dY /= length;
		//float32 C = length - m_length;
		var C/*:Number*/ = length - this.m_length$2;
		C = Box2D.Common.Math.b2Math.Clamp(C, -Box2D.Common.b2Settings.b2_maxLinearCorrection, Box2D.Common.b2Settings.b2_maxLinearCorrection);
		
		var impulse/*:Number*/ = -this.m_mass$2 * C;
		//m_u = d;
		this.m_u$2.Set(dX, dY);
		//b2Vec2 P = impulse * m_u;
		var PX/*:Number*/ = impulse * this.m_u$2.x;
		var PY/*:Number*/ = impulse * this.m_u$2.y;
		
		//bA->m_sweep.c -= bA->m_invMass * P;
		bA.m_sweep.c.x -= bA.m_invMass * PX;
		bA.m_sweep.c.y -= bA.m_invMass * PY;
		//bA->m_sweep.a -= bA->m_invI * b2Cross(r1, P);
		bA.m_sweep.a -= bA.m_invI * (r1X * PY - r1Y * PX);
		//bB->m_sweep.c += bB->m_invMass * P;
		bB.m_sweep.c.x += bB.m_invMass * PX;
		bB.m_sweep.c.y += bB.m_invMass * PY;
		//bB->m_sweep.a -= bB->m_invI * b2Cross(r2, P);
		bB.m_sweep.a += bB.m_invI * (r2X * PY - r2Y * PX);
		
		bA.SynchronizeTransform();
		bB.SynchronizeTransform();
		
		return Box2D.Common.Math.b2Math.Abs(C) < Box2D.Common.b2Settings.b2_linearSlop;
		
	},

	"private var",{ m_localAnchor1/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_localAnchor2/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_u/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_frequencyHz/*:Number*/:NaN},
	"private var",{ m_dampingRatio/*:Number*/:NaN},
	"private var",{ m_gamma/*:Number*/:NaN},
	"private var",{ m_bias/*:Number*/:NaN},
	"private var",{ m_impulse/*:Number*/:NaN},
	"private var",{ m_mass/*:Number*/:NaN},	// effective mass for the constraint.
	"private var",{ m_length/*:Number*/:NaN},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Math","Box2D.Common.b2Settings","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"

);