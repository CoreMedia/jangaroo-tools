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


// p = attached point, m = mouse point
// C = p - m
// Cdot = v
//      = v + cross(w, r)
// J = [I r_skew]
// Identity used:
// w k % (rx i + ry j) = w * (-ry i + rx j)

/**
* A mouse joint is used to make a point on a body track a
* specified world point. This a soft constraint with a maximum
* force. This allows the constraint to stretch and without
* applying huge forces.
* Note: this joint is not fully documented as it is intended primarily
* for the testbed. See that for more instructions.
* @see b2MouseJointDef
*/

"public class b2MouseJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Math);},

	/** @inheritDoc */
	"public override function GetAnchorA",function GetAnchorA()/*:b2Vec2*/{
		return this.m_target$2;
	},
	/** @inheritDoc */
	"public override function GetAnchorB",function GetAnchorB()/*:b2Vec2*/{
		return this.m_bodyB.GetWorldPoint(this.m_localAnchor$2);
	},
	/** @inheritDoc */
	"public override function GetReactionForce",function GetReactionForce(inv_dt/*:Number*/)/*:b2Vec2*/
	{
		return new Box2D.Common.Math.b2Vec2(inv_dt * this.m_impulse$2.x, inv_dt * this.m_impulse$2.y);
	},
	/** @inheritDoc */
	"public override function GetReactionTorque",function GetReactionTorque(inv_dt/*:Number*/)/*:Number*/
	{
		return 0.0;
	},
	
	"public function GetTarget",function GetTarget()/*:b2Vec2*/
	{
		return this.m_target$2;
	},
	
	/**
	 * Use this to update the target point.
	 */
	"public function SetTarget",function SetTarget(target/*:b2Vec2*/)/* : void*/{
		if (this.m_bodyB.IsAwake() == false){
			this.m_bodyB.SetAwake(true);
		}
		this.m_target$2 = target;
	},

	/// Get the maximum force in Newtons.
	"public function GetMaxForce",function GetMaxForce()/*:Number*/
	{
		return this.m_maxForce$2;
	},
	
	/// Set the maximum force in Newtons.
	"public function SetMaxForce",function SetMaxForce(maxForce/*:Number*/)/*:void*/
	{
		this.m_maxForce$2 = maxForce;
	},
	
	/// Get frequency in Hz
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
	"public function b2MouseJoint",function b2MouseJoint$(def/*:b2MouseJointDef*/){
		this.super$2(def);this.K$2=this.K$2();this.K1$2=this.K1$2();this.K2$2=this.K2$2();this.m_localAnchor$2=this.m_localAnchor$2();this.m_target$2=this.m_target$2();this.m_impulse$2=this.m_impulse$2();this.m_mass$2=this.m_mass$2();this.m_C$2=this.m_C$2();
		
		//b2Settings.b2Assert(def.target.IsValid());
		//b2Settings.b2Assert(b2Math.b2IsValid(def.maxForce) && def.maxForce > 0.0);
		//b2Settings.b2Assert(b2Math.b2IsValid(def.frequencyHz) && def.frequencyHz > 0.0);
		//b2Settings.b2Assert(b2Math.b2IsValid(def.dampingRatio) && def.dampingRatio > 0.0);
		
		this.m_target$2.SetV(def.target);
		//m_localAnchor = b2MulT(m_bodyB.m_xf, m_target);
		var tX/*:Number*/ = this.m_target$2.x - this.m_bodyB.m_xf.position.x;
		var tY/*:Number*/ = this.m_target$2.y - this.m_bodyB.m_xf.position.y;
		var tMat/*:b2Mat22*/ = this.m_bodyB.m_xf.R;
		this.m_localAnchor$2.x = (tX * tMat.col1.x + tY * tMat.col1.y);
		this.m_localAnchor$2.y = (tX * tMat.col2.x + tY * tMat.col2.y);
		
		this.m_maxForce$2 = def.maxForce;
		this.m_impulse$2.SetZero();
		
		this.m_frequencyHz$2 = def.frequencyHz;
		this.m_dampingRatio$2 = def.dampingRatio;
		
		this.m_beta$2 = 0.0;
		this.m_gamma$2 = 0.0;
	},

	// Presolve vars
	"private var",{ K/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"private var",{ K1/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"private var",{ K2/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"b2internal override function InitVelocityConstraints",function InitVelocityConstraints(step/*:b2TimeStep*/)/*: void*/{
		var b/*:b2Body*/ = this.m_bodyB;
		
		var mass/*:Number*/ = b.GetMass();
		
		// Frequency
		var omega/*:Number*/ = 2.0 * Math.PI * this.m_frequencyHz$2;
		
		// Damping co-efficient
		var d/*:Number*/ = 2.0 * mass * this.m_dampingRatio$2 * omega;
		
		// Spring stiffness
		var k/*:Number*/ = mass * omega * omega;
		
		// magic formulas
		// gamma has units of inverse mass
		// beta hs units of inverse time
		//b2Settings.b2Assert(d + step.dt * k > Number.MIN_VALUE)
		this.m_gamma$2 = step.dt * (d + step.dt * k);
		this.m_gamma$2 = this.m_gamma$2 != 0 ? 1 / this.m_gamma$2:0.0;
		this.m_beta$2 = step.dt * k * this.m_gamma$2;
		
		var tMat/*:b2Mat22*/;
		
		// Compute the effective mass matrix.
		//b2Vec2 r = b2Mul(b->m_xf.R, m_localAnchor - b->GetLocalCenter());
		tMat = b.m_xf.R;
		var rX/*:Number*/ = this.m_localAnchor$2.x - b.m_sweep.localCenter.x;
		var rY/*:Number*/ = this.m_localAnchor$2.y - b.m_sweep.localCenter.y;
		var tX/*:Number*/ = (tMat.col1.x * rX + tMat.col2.x * rY);
		rY = (tMat.col1.y * rX + tMat.col2.y * rY);
		rX = tX;
		
		// K    = [(1/m1 + 1/m2) * eye(2) - skew(r1) * invI1 * skew(r1) - skew(r2) * invI2 * skew(r2)]
		//      = [1/m1+1/m2     0    ] + invI1 * [r1.y*r1.y -r1.x*r1.y] + invI2 * [r1.y*r1.y -r1.x*r1.y]
		//        [    0     1/m1+1/m2]           [-r1.x*r1.y r1.x*r1.x]           [-r1.x*r1.y r1.x*r1.x]
		var invMass/*:Number*/ = b.m_invMass;
		var invI/*:Number*/ = b.m_invI;
		
		//b2Mat22 K1;
		this.K1$2.col1.x = invMass;	this.K1$2.col2.x = 0.0;
		this.K1$2.col1.y = 0.0;		this.K1$2.col2.y = invMass;
		
		//b2Mat22 K2;
		this.K2$2.col1.x =  invI * rY * rY;	this.K2$2.col2.x = -invI * rX * rY;
		this.K2$2.col1.y = -invI * rX * rY;	this.K2$2.col2.y =  invI * rX * rX;
		
		//b2Mat22 K = K1 + K2;
		this.K$2.SetM(this.K1$2);
		this.K$2.AddM(this.K2$2);
		this.K$2.col1.x += this.m_gamma$2;
		this.K$2.col2.y += this.m_gamma$2;
		
		//m_ptpMass = K.GetInverse();
		this.K$2.GetInverse(this.m_mass$2);
		
		//m_C = b.m_position + r - m_target;
		this.m_C$2.x = b.m_sweep.c.x + rX - this.m_target$2.x;
		this.m_C$2.y = b.m_sweep.c.y + rY - this.m_target$2.y;
		
		// Cheat with some damping
		b.m_angularVelocity *= 0.98;
		
		// Warm starting.
		this.m_impulse$2.x *= step.dtRatio;
		this.m_impulse$2.y *= step.dtRatio;
		//b.m_linearVelocity += invMass * m_impulse;
		b.m_linearVelocity.x += invMass * this.m_impulse$2.x;
		b.m_linearVelocity.y += invMass * this.m_impulse$2.y;
		//b.m_angularVelocity += invI * b2Cross(r, m_impulse);
		b.m_angularVelocity += invI * (rX * this.m_impulse$2.y - rY * this.m_impulse$2.x);
	},
	
	"b2internal override function SolveVelocityConstraints",function SolveVelocityConstraints(step/*:b2TimeStep*/)/* : void*/{
		var b/*:b2Body*/ = this.m_bodyB;
		
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;
		var tY/*:Number*/;
		
		// Compute the effective mass matrix.
		//b2Vec2 r = b2Mul(b->m_xf.R, m_localAnchor - b->GetLocalCenter());
		tMat = b.m_xf.R;
		var rX/*:Number*/ = this.m_localAnchor$2.x - b.m_sweep.localCenter.x;
		var rY/*:Number*/ = this.m_localAnchor$2.y - b.m_sweep.localCenter.y;
		tX = (tMat.col1.x * rX + tMat.col2.x * rY);
		rY = (tMat.col1.y * rX + tMat.col2.y * rY);
		rX = tX;
		
		// Cdot = v + cross(w, r)
		//b2Vec2 Cdot = b->m_linearVelocity + b2Cross(b->m_angularVelocity, r);
		var CdotX/*:Number*/ = b.m_linearVelocity.x + (-b.m_angularVelocity * rY);
		var CdotY/*:Number*/ = b.m_linearVelocity.y + (b.m_angularVelocity * rX);
		//b2Vec2 impulse = - b2Mul(m_mass, Cdot + m_beta * m_C + m_gamma * m_impulse);
		tMat = this.m_mass$2;
		tX = CdotX + this.m_beta$2 * this.m_C$2.x + this.m_gamma$2 * this.m_impulse$2.x;
		tY = CdotY + this.m_beta$2 * this.m_C$2.y + this.m_gamma$2 * this.m_impulse$2.y;
		var impulseX/*:Number*/ = -(tMat.col1.x * tX + tMat.col2.x * tY);
		var impulseY/*:Number*/ = -(tMat.col1.y * tX + tMat.col2.y * tY);
		
		var oldImpulseX/*:Number*/ = this.m_impulse$2.x;
		var oldImpulseY/*:Number*/ = this.m_impulse$2.y;
		//m_impulse += impulse;
		this.m_impulse$2.x += impulseX;
		this.m_impulse$2.y += impulseY;
		var maxImpulse/*:Number*/ = step.dt * this.m_maxForce$2;
		if (this.m_impulse$2.LengthSquared() > maxImpulse*maxImpulse)
		{
			//m_impulse *= m_maxImpulse / m_impulse.Length();
			this.m_impulse$2.Multiply(maxImpulse / this.m_impulse$2.Length());
		}
		//impulse = m_impulse - oldImpulse;
		impulseX = this.m_impulse$2.x - oldImpulseX;
		impulseY = this.m_impulse$2.y - oldImpulseY;
		
		//b->m_linearVelocity += b->m_invMass * impulse;
		b.m_linearVelocity.x += b.m_invMass * impulseX;
		b.m_linearVelocity.y += b.m_invMass * impulseY;
		//b->m_angularVelocity += b->m_invI * b2Cross(r, P);
		b.m_angularVelocity += b.m_invI * (rX * impulseY - rY * impulseX);
	},

	"b2internal override function SolvePositionConstraints",function SolvePositionConstraints(baumgarte/*:Number*/)/*:Boolean*/ { 
		//B2_NOT_USED(baumgarte);
		return true; 
	},

	"private var",{ m_localAnchor/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_target/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_impulse/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},

	"private var",{ m_mass/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},	// effective mass for point-to-point constraint.
	"private var",{ m_C/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},			// position error
	"private var",{ m_maxForce/*:Number*/:NaN},
	"private var",{ m_frequencyHz/*:Number*/:NaN},
	"private var",{ m_dampingRatio/*:Number*/:NaN},
	"private var",{ m_beta/*:Number*/:NaN},						// bias factor
	"private var",{ m_gamma/*:Number*/:NaN},						// softness
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22","Math"], "0.8.0", "0.8.1"

);