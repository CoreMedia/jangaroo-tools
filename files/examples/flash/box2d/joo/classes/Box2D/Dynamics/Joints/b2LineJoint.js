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


import Box2D.Common.*
import Box2D.Common.Math.*
import Box2D.Dynamics.*

import Box2D.Common.b2internal
use namespace b2internal*/

// Linear constraint (point-to-line)
// d = p2 - p1 = x2 + r2 - x1 - r1
// C = dot(perp, d)
// Cdot = dot(d, cross(w1, perp)) + dot(perp, v2 + cross(w2, r2) - v1 - cross(w1, r1))
//      = -dot(perp, v1) - dot(cross(d + r1, perp), w1) + dot(perp, v2) + dot(cross(r2, perp), v2)
// J = [-perp, -cross(d + r1, perp), perp, cross(r2,perp)]
//
// K = J * invM * JT
//
// J = [-a -s1 a s2]
// a = perp
// s1 = cross(d + r1, a) = cross(p2 - x1, a)
// s2 = cross(r2, a) = cross(p2 - x2, a)


// Motor/Limit linear constraint
// C = dot(ax1, d)
// Cdot = = -dot(ax1, v1) - dot(cross(d + r1, ax1), w1) + dot(ax1, v2) + dot(cross(r2, ax1), v2)
// J = [-ax1 -cross(d+r1,ax1) ax1 cross(r2,ax1)]

// Block Solver
// We develop a block solver that includes the joint limit. This makes the limit stiff (inelastic) even
// when the mass has poor distribution (leading to large torques about the joint anchor points).
//
// The Jacobian has 3 rows:
// J = [-uT -s1 uT s2] // linear
//     [-vT -a1 vT a2] // limit
//
// u = perp
// v = axis
// s1 = cross(d + r1, u), s2 = cross(r2, u)
// a1 = cross(d + r1, v), a2 = cross(r2, v)

// M * (v2 - v1) = JT * df
// J * v2 = bias
//
// v2 = v1 + invM * JT * df
// J * (v1 + invM * JT * df) = bias
// K * df = bias - J * v1 = -Cdot
// K = J * invM * JT
// Cdot = J * v1 - bias
//
// Now solve for f2.
// df = f2 - f1
// K * (f2 - f1) = -Cdot
// f2 = invK * (-Cdot) + f1
//
// Clamp accumulated limit impulse.
// lower: f2(2) = max(f2(2), 0)
// upper: f2(2) = min(f2(2), 0)
//
// Solve for correct f2(1)
// K(1,1) * f2(1) = -Cdot(1) - K(1,2) * f2(2) + K(1,1:2) * f1
//                = -Cdot(1) - K(1,2) * f2(2) + K(1,1) * f1(1) + K(1,2) * f1(2)
// K(1,1) * f2(1) = -Cdot(1) - K(1,2) * (f2(2) - f1(2)) + K(1,1) * f1(1)
// f2(1) = invK(1,1) * (-Cdot(1) - K(1,2) * (f2(2) - f1(2))) + f1(1)
//
// Now compute impulse to be applied:
// df = f2 - f1

/**
 * A line joint. This joint provides two degrees of freedom: translation
 * along an axis fixed in body1 and rotation in the plane. You can use a 
 * joint limit to restrict the range of motion and a joint motor to drive
 * the motion or to model joint friction.
 * @see b2LineJointDef
 */
"public class b2LineJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Number,Box2D.Common.b2Settings);},

	/** @inheritDoc */
	"public override function GetAnchorA",function GetAnchorA()/*:b2Vec2*/{
		return this.m_bodyA.GetWorldPoint(this.m_localAnchor1);
	},
	/** @inheritDoc */
	"public override function GetAnchorB",function GetAnchorB()/*:b2Vec2*/{
		return this.m_bodyB.GetWorldPoint(this.m_localAnchor2);
	},
	/** @inheritDoc */
	"public override function GetReactionForce",function GetReactionForce(inv_dt/*:Number*/)/* : b2Vec2*/
	{
		//return inv_dt * (m_impulse.x * m_perp + (m_motorImpulse + m_impulse.y) * m_axis);
		return new Box2D.Common.Math.b2Vec2(	inv_dt * (this.m_impulse$2.x * this.m_perp$2.x + (this.m_motorImpulse$2 + this.m_impulse$2.y) * this.m_axis$2.x),
							inv_dt * (this.m_impulse$2.x * this.m_perp$2.y + (this.m_motorImpulse$2 + this.m_impulse$2.y) * this.m_axis$2.y));
	},

	/** @inheritDoc */
	"public override function GetReactionTorque",function GetReactionTorque(inv_dt/*:Number*/)/* : Number*/
	{
		return inv_dt * this.m_impulse$2.y;
	},
	
	/**
	* Get the current joint translation, usually in meters.
	*/
	"public function GetJointTranslation",function GetJointTranslation()/*:Number*/{
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var tMat/*:b2Mat22*/;
		
		var p1/*:b2Vec2*/ = bA.GetWorldPoint(this.m_localAnchor1);
		var p2/*:b2Vec2*/ = bB.GetWorldPoint(this.m_localAnchor2);
		//var d:b2Vec2 = b2Math.SubtractVV(p2, p1);
		var dX/*:Number*/ = p2.x - p1.x;
		var dY/*:Number*/ = p2.y - p1.y;
		//b2Vec2 axis = bA->GetWorldVector(m_localXAxis1);
		var axis/*:b2Vec2*/ = bA.GetWorldVector(this.m_localXAxis1);
		
		//float32 translation = b2Dot(d, axis);
		var translation/*:Number*/ = axis.x*dX + axis.y*dY;
		return translation;
	},
	
	/**
	* Get the current joint translation speed, usually in meters per second.
	*/
	"public function GetJointSpeed",function GetJointSpeed()/*:Number*/{
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var tMat/*:b2Mat22*/;
		
		//b2Vec2 r1 = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
		tMat = bA.m_xf.R;
		var r1X/*:Number*/ = this.m_localAnchor1.x - bA.m_sweep.localCenter.x;
		var r1Y/*:Number*/ = this.m_localAnchor1.y - bA.m_sweep.localCenter.y;
		var tX/*:Number*/ =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
		r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
		r1X = tX;
		//b2Vec2 r2 = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
		tMat = bB.m_xf.R;
		var r2X/*:Number*/ = this.m_localAnchor2.x - bB.m_sweep.localCenter.x;
		var r2Y/*:Number*/ = this.m_localAnchor2.y - bB.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
		r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
		r2X = tX;
		
		//b2Vec2 p1 = bA->m_sweep.c + r1;
		var p1X/*:Number*/ = bA.m_sweep.c.x + r1X;
		var p1Y/*:Number*/ = bA.m_sweep.c.y + r1Y;
		//b2Vec2 p2 = bB->m_sweep.c + r2;
		var p2X/*:Number*/ = bB.m_sweep.c.x + r2X;
		var p2Y/*:Number*/ = bB.m_sweep.c.y + r2Y;
		//var d:b2Vec2 = b2Math.SubtractVV(p2, p1);
		var dX/*:Number*/ = p2X - p1X;
		var dY/*:Number*/ = p2Y - p1Y;
		//b2Vec2 axis = bA->GetWorldVector(m_localXAxis1);
		var axis/*:b2Vec2*/ = bA.GetWorldVector(this.m_localXAxis1);
		
		var v1/*:b2Vec2*/ = bA.m_linearVelocity;
		var v2/*:b2Vec2*/ = bB.m_linearVelocity;
		var w1/*:Number*/ = bA.m_angularVelocity;
		var w2/*:Number*/ = bB.m_angularVelocity;
		
		//var speed:Number = b2Math.b2Dot(d, b2Math.b2CrossFV(w1, ax1)) + b2Math.b2Dot(ax1, b2Math.SubtractVV( b2Math.SubtractVV( b2Math.AddVV( v2 , b2Math.b2CrossFV(w2, r2)) , v1) , b2Math.b2CrossFV(w1, r1)));
		//var b2D:Number = (dX*(-w1 * ax1Y) + dY*(w1 * ax1X));
		//var b2D2:Number = (ax1X * ((( v2.x + (-w2 * r2Y)) - v1.x) - (-w1 * r1Y)) + ax1Y * ((( v2.y + (w2 * r2X)) - v1.y) - (w1 * r1X)));
		var speed/*:Number*/ = (dX*(-w1 * axis.y) + dY*(w1 * axis.x)) + (axis.x * ((( v2.x + (-w2 * r2Y)) - v1.x) - (-w1 * r1Y)) + axis.y * ((( v2.y + (w2 * r2X)) - v1.y) - (w1 * r1X)));
		
		return speed;
	},
	
	/**
	* Is the joint limit enabled?
	*/
	"public function IsLimitEnabled",function IsLimitEnabled()/* : Boolean*/
	{
		return this.m_enableLimit$2;
	},
	/**
	* Enable/disable the joint limit.
	*/
	"public function EnableLimit",function EnableLimit(flag/*:Boolean*/)/* : void*/
	{
		this.m_bodyA.SetAwake(true);
		this.m_bodyB.SetAwake(true);
		this.m_enableLimit$2 = flag;
	},
	/**
	* Get the lower joint limit, usually in meters.
	*/
	"public function GetLowerLimit",function GetLowerLimit()/* : Number*/
	{
		return this.m_lowerTranslation$2;
	},
	/**
	* Get the upper joint limit, usually in meters.
	*/
	"public function GetUpperLimit",function GetUpperLimit()/* : Number*/
	{
		return this.m_upperTranslation$2;
	},
	/**
	* Set the joint limits, usually in meters.
	*/
	"public function SetLimits",function SetLimits(lower/*:Number*/, upper/*:Number*/)/* : void*/
	{
		//b2Settings.b2Assert(lower <= upper);
		this.m_bodyA.SetAwake(true);
		this.m_bodyB.SetAwake(true);
		this.m_lowerTranslation$2 = lower;
		this.m_upperTranslation$2 = upper;
	},
	/**
	* Is the joint motor enabled?
	*/
	"public function IsMotorEnabled",function IsMotorEnabled()/* : Boolean*/
	{
		return this.m_enableMotor$2;
	},
	/**
	* Enable/disable the joint motor.
	*/
	"public function EnableMotor",function EnableMotor(flag/*:Boolean*/)/* : void*/
	{
		this.m_bodyA.SetAwake(true);
		this.m_bodyB.SetAwake(true);
		this.m_enableMotor$2 = flag;
	},
	/**
	* Set the motor speed, usually in meters per second.
	*/
	"public function SetMotorSpeed",function SetMotorSpeed(speed/*:Number*/)/* : void*/
	{
		this.m_bodyA.SetAwake(true);
		this.m_bodyB.SetAwake(true);
		this.m_motorSpeed$2 = speed;
	},
	/**
	* Get the motor speed, usually in meters per second.
	*/
	"public function GetMotorSpeed",function GetMotorSpeed()/* :Number*/
	{
		return this.m_motorSpeed$2;
	},
	
	/**
	 * Set the maximum motor force, usually in N.
	 */
	"public function SetMaxMotorForce",function SetMaxMotorForce(force/*:Number*/)/* : void*/
	{
		this.m_bodyA.SetAwake(true);
		this.m_bodyB.SetAwake(true);
		this.m_maxMotorForce$2 = force;
	},
	
	/**
	 * Get the maximum motor force, usually in N.
	 */
	"public function GetMaxMotorForce",function GetMaxMotorForce()/*:Number*/
	{
		return this.m_maxMotorForce$2;
	},
	
	/**
	* Get the current motor force, usually in N.
	*/
	"public function GetMotorForce",function GetMotorForce()/* : Number*/
	{
		return this.m_motorImpulse$2;
	},
	

	//--------------- Internals Below -------------------

	/** @private */
	"public function b2LineJoint",function b2LineJoint$(def/*:b2LineJointDef*/){
		this.super$2(def);this.m_localAnchor1=this.m_localAnchor1();this.m_localAnchor2=this.m_localAnchor2();this.m_localXAxis1=this.m_localXAxis1();this.m_localYAxis1$2=this.m_localYAxis1$2();this.m_axis$2=this.m_axis$2();this.m_perp$2=this.m_perp$2();this.m_K$2=this.m_K$2();this.m_impulse$2=this.m_impulse$2();
		
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;
		var tY/*:Number*/;
		
		this.m_localAnchor1.SetV(def.localAnchorA);
		this.m_localAnchor2.SetV(def.localAnchorB);
		this.m_localXAxis1.SetV(def.localAxisA);
		
		//m_localYAxis1 = b2Cross(1.0f, m_localXAxis1);
		this.m_localYAxis1$2.x = -this.m_localXAxis1.y;
		this.m_localYAxis1$2.y = this.m_localXAxis1.x;
		
		this.m_impulse$2.SetZero();
		this.m_motorMass$2 = 0.0;
		this.m_motorImpulse$2 = 0.0;
		
		this.m_lowerTranslation$2 = def.lowerTranslation;
		this.m_upperTranslation$2 = def.upperTranslation;
		this.m_maxMotorForce$2 = def.maxMotorForce;
		this.m_motorSpeed$2 = def.motorSpeed;
		this.m_enableLimit$2 = def.enableLimit;
		this.m_enableMotor$2 = def.enableMotor;
		this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
		
		this.m_axis$2.SetZero();
		this.m_perp$2.SetZero();
	},

	"b2internal override function InitVelocityConstraints",function InitVelocityConstraints(step/*:b2TimeStep*/)/* : void*/{
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;
		
		this.m_localCenterA.SetV(bA.GetLocalCenter());
		this.m_localCenterB.SetV(bB.GetLocalCenter());
		
		var xf1/*:b2Transform*/ = bA.GetTransform();
		var xf2/*:b2Transform*/ = bB.GetTransform();
		
		// Compute the effective masses.
		//b2Vec2 r1 = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
		tMat = bA.m_xf.R;
		var r1X/*:Number*/ = this.m_localAnchor1.x - this.m_localCenterA.x;
		var r1Y/*:Number*/ = this.m_localAnchor1.y - this.m_localCenterA.y;
		tX =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
		r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
		r1X = tX;
		//b2Vec2 r2 = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
		tMat = bB.m_xf.R;
		var r2X/*:Number*/ = this.m_localAnchor2.x - this.m_localCenterB.x;
		var r2Y/*:Number*/ = this.m_localAnchor2.y - this.m_localCenterB.y;
		tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
		r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
		r2X = tX;
		
		//b2Vec2 d = bB->m_sweep.c + r2 - bA->m_sweep.c - r1;
		var dX/*:Number*/ = bB.m_sweep.c.x + r2X - bA.m_sweep.c.x - r1X;
		var dY/*:Number*/ = bB.m_sweep.c.y + r2Y - bA.m_sweep.c.y - r1Y;
		
		this.m_invMassA = bA.m_invMass;
		this.m_invMassB = bB.m_invMass;
		this.m_invIA = bA.m_invI;
		this.m_invIB = bB.m_invI;
		
		// Compute motor Jacobian and effective mass.
		{
			this.m_axis$2.SetV(Box2D.Common.Math.b2Math.MulMV(xf1.R, this.m_localXAxis1));
			//m_a1 = b2Math.b2Cross(d + r1, m_axis);
			this.m_a1$2 = (dX + r1X) * this.m_axis$2.y - (dY + r1Y) * this.m_axis$2.x;
			//m_a2 = b2Math.b2Cross(r2, m_axis);
			this.m_a2$2 = r2X * this.m_axis$2.y - r2Y * this.m_axis$2.x;
			
			this.m_motorMass$2 = this.m_invMassA + this.m_invMassB + this.m_invIA * this.m_a1$2 * this.m_a1$2 + this.m_invIB * this.m_a2$2 * this.m_a2$2; 
			this.m_motorMass$2 = this.m_motorMass$2 > Number.MIN_VALUE?1.0 / this.m_motorMass$2:0.0;
		}
		
		// Prismatic constraint.
		{
			this.m_perp$2.SetV(Box2D.Common.Math.b2Math.MulMV(xf1.R, this.m_localYAxis1$2));
			//m_s1 = b2Math.b2Cross(d + r1, m_perp);
			this.m_s1$2 = (dX + r1X) * this.m_perp$2.y - (dY + r1Y) * this.m_perp$2.x;
			//m_s2 = b2Math.b2Cross(r2, m_perp);
			this.m_s2$2 = r2X * this.m_perp$2.y - r2Y * this.m_perp$2.x;
			
			var m1/*:Number*/ = this.m_invMassA;
			var m2/*:Number*/ = this.m_invMassB;
			var i1/*:Number*/ = this.m_invIA;
			var i2/*:Number*/ = this.m_invIB;
			
			this.m_K$2.col1.x = m1 + m2 + i1 * this.m_s1$2 * this.m_s1$2 + i2 * this.m_s2$2 * this.m_s2$2;
 	  	  	this.m_K$2.col1.y = i1 * this.m_s1$2 * this.m_a1$2 + i2 * this.m_s2$2 * this.m_a2$2;
			this.m_K$2.col2.x = this.m_K$2.col1.y;
 	  	  	this.m_K$2.col2.y = m1 + m2 + i1 * this.m_a1$2 * this.m_a1$2 + i2 * this.m_a2$2 * this.m_a2$2; 
		}
		
		// Compute motor and limit terms
		if (this.m_enableLimit$2)
		{
			//float32 jointTranslation = b2Dot(m_axis, d); 
			var jointTransition/*:Number*/ = this.m_axis$2.x * dX + this.m_axis$2.y * dY;
			if (Box2D.Common.Math.b2Math.Abs(this.m_upperTranslation$2 - this.m_lowerTranslation$2) < 2.0 * Box2D.Common.b2Settings.b2_linearSlop)
			{
				this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_equalLimits;
			}
			else if (jointTransition <= this.m_lowerTranslation$2)
			{
				if (this.m_limitState$2 != Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
				{
					this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit;
					this.m_impulse$2.y = 0.0;
				}
			}
			else if (jointTransition >= this.m_upperTranslation$2)
			{
				if (this.m_limitState$2 != Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
				{
					this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit;
					this.m_impulse$2.y = 0.0;
				}
			}
			else
			{
				this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
				this.m_impulse$2.y = 0.0;
			}
		}
		else
		{
			this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
		}
		
		if (this.m_enableMotor$2 == false)
		{
			this.m_motorImpulse$2 = 0.0;
		}
		
		if (step.warmStarting)
		{
			// Account for variable time step.
			this.m_impulse$2.x *= step.dtRatio;
			this.m_impulse$2.y *= step.dtRatio;
			this.m_motorImpulse$2 *= step.dtRatio; 
			
			//b2Vec2 P = m_impulse.x * m_perp + (m_motorImpulse + m_impulse.z) * m_axis;
			var PX/*:Number*/ = this.m_impulse$2.x * this.m_perp$2.x + (this.m_motorImpulse$2 + this.m_impulse$2.y) * this.m_axis$2.x;
			var PY/*:Number*/ = this.m_impulse$2.x * this.m_perp$2.y + (this.m_motorImpulse$2 + this.m_impulse$2.y) * this.m_axis$2.y;
			var L1/*:Number*/ = this.m_impulse$2.x * this.m_s1$2     + (this.m_motorImpulse$2 + this.m_impulse$2.y) * this.m_a1$2;
			var L2/*:Number*/ = this.m_impulse$2.x * this.m_s2$2     + (this.m_motorImpulse$2 + this.m_impulse$2.y) * this.m_a2$2; 

			//bA->m_linearVelocity -= m_invMassA * P;
			bA.m_linearVelocity.x -= this.m_invMassA * PX;
			bA.m_linearVelocity.y -= this.m_invMassA * PY;
			//bA->m_angularVelocity -= m_invIA * L1;
			bA.m_angularVelocity -= this.m_invIA * L1;
			
			//bB->m_linearVelocity += m_invMassB * P;
			bB.m_linearVelocity.x += this.m_invMassB * PX;
			bB.m_linearVelocity.y += this.m_invMassB * PY;
			//bB->m_angularVelocity += m_invIB * L2;
			bB.m_angularVelocity += this.m_invIB * L2;
		}
		else
		{
			this.m_impulse$2.SetZero();
			this.m_motorImpulse$2 = 0.0;
		}
	},
	
	"b2internal override function SolveVelocityConstraints",function SolveVelocityConstraints(step/*:b2TimeStep*/)/* : void*/{
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var v1/*:b2Vec2*/ = bA.m_linearVelocity;
		var w1/*:Number*/ = bA.m_angularVelocity;
		var v2/*:b2Vec2*/ = bB.m_linearVelocity;
		var w2/*:Number*/ = bB.m_angularVelocity;
		
		var PX/*:Number*/;
		var PY/*:Number*/;
		var L1/*:Number*/;
		var L2/*:Number*/;
		
		// Solve linear motor constraint
		if (this.m_enableMotor$2 && this.m_limitState$2 != Box2D.Dynamics.Joints.b2Joint.e_equalLimits)
		{
			//float32 Cdot = b2Dot(m_axis, v2 - v1) + m_a2 * w2 - m_a1 * w1; 
			var Cdot/*:Number*/ = this.m_axis$2.x * (v2.x -v1.x) + this.m_axis$2.y * (v2.y - v1.y) + this.m_a2$2 * w2 - this.m_a1$2 * w1;
			var impulse/*:Number*/ = this.m_motorMass$2 * (this.m_motorSpeed$2 - Cdot);
			var oldImpulse/*:Number*/ = this.m_motorImpulse$2;
			var maxImpulse/*:Number*/ = step.dt * this.m_maxMotorForce$2;
			this.m_motorImpulse$2 = Box2D.Common.Math.b2Math.Clamp(this.m_motorImpulse$2 + impulse, -maxImpulse, maxImpulse);
			impulse = this.m_motorImpulse$2 - oldImpulse;
			
			PX = impulse * this.m_axis$2.x;
			PY = impulse * this.m_axis$2.y;
			L1 = impulse * this.m_a1$2;
			L2 = impulse * this.m_a2$2;
			
			v1.x -= this.m_invMassA * PX;
			v1.y -= this.m_invMassA * PY;
			w1 -= this.m_invIA * L1;
			
			v2.x += this.m_invMassB * PX;
			v2.y += this.m_invMassB * PY;
			w2 += this.m_invIB * L2;
		}
		
		//Cdot1 = b2Dot(m_perp, v2 - v1) + m_s2 * w2 - m_s1 * w1; 
		var Cdot1/*:Number*/ = this.m_perp$2.x * (v2.x - v1.x) + this.m_perp$2.y * (v2.y - v1.y) + this.m_s2$2 * w2 - this.m_s1$2 * w1; 
		
		if (this.m_enableLimit$2 && this.m_limitState$2 != Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit)
		{
			// Solve prismatic and limit constraint in block form
			//Cdot2 = b2Dot(m_axis, v2 - v1) + m_a2 * w2 - m_a1 * w1; 
			var Cdot2/*:Number*/ = this.m_axis$2.x * (v2.x - v1.x) + this.m_axis$2.y * (v2.y - v1.y) + this.m_a2$2 * w2 - this.m_a1$2 * w1; 
			
			var f1/*:b2Vec2*/ = this.m_impulse$2.Copy();
			var df/*:b2Vec2*/ = this.m_K$2.Solve(new Box2D.Common.Math.b2Vec2(), -Cdot1, -Cdot2);
			
			this.m_impulse$2.Add(df);
			
			if (this.m_limitState$2 == Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
			{
				this.m_impulse$2.y = Box2D.Common.Math.b2Math.Max(this.m_impulse$2.y, 0.0);
			}
			else if (this.m_limitState$2 == Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
			{
				this.m_impulse$2.y = Box2D.Common.Math.b2Math.Min(this.m_impulse$2.y, 0.0);
			}
			
			// f2(1) = invK(1,1) * (-Cdot(1) - K(1,3) * (f2(2) - f1(2))) + f1(1) 
			var b/*:Number*/ = -Cdot1 - (this.m_impulse$2.y - f1.y) * this.m_K$2.col2.x;
			var f2r/*:Number*/;
			if (this.m_K$2.col1.x != 0.0)
			{
				f2r = b / this.m_K$2.col1.x + f1.x;
			}else {
				f2r = f1.x;
			}
			this.m_impulse$2.x = f2r;
			
			df.x = this.m_impulse$2.x - f1.x;
			df.y = this.m_impulse$2.y - f1.y;
			
			PX = df.x * this.m_perp$2.x + df.y * this.m_axis$2.x;
			PY = df.x * this.m_perp$2.y + df.y * this.m_axis$2.y;
			L1 = df.x * this.m_s1$2 + df.y * this.m_a1$2;
			L2 = df.x * this.m_s2$2 + df.y * this.m_a2$2;
			
			v1.x -= this.m_invMassA * PX;
			v1.y -= this.m_invMassA * PY;
			w1 -= this.m_invIA * L1;
			
			v2.x += this.m_invMassB * PX;
			v2.y += this.m_invMassB * PY;
			w2 += this.m_invIB * L2;
		}
		else
		{
			// Limit is inactive, just solve the prismatic constraint in block form. 
			var df2/*:Number*/;
			if (this.m_K$2.col1.x != 0.0)
			{
				df2 = ( -Cdot1) / this.m_K$2.col1.x;
			}else {
				df2 = 0.0;
			}
			this.m_impulse$2.x += df2;
			
			PX = df2 * this.m_perp$2.x;
			PY = df2 * this.m_perp$2.y;
			L1 = df2 * this.m_s1$2;
			L2 = df2 * this.m_s2$2;
			
			v1.x -= this.m_invMassA * PX;
			v1.y -= this.m_invMassA * PY;
			w1 -= this.m_invIA * L1;
			
			v2.x += this.m_invMassB * PX;
			v2.y += this.m_invMassB * PY;
			w2 += this.m_invIB * L2;
		}
		
		bA.m_linearVelocity.SetV(v1);
		bA.m_angularVelocity = w1;
		bB.m_linearVelocity.SetV(v2);
		bB.m_angularVelocity = w2;
	},
	
	"b2internal override function SolvePositionConstraints",function SolvePositionConstraints(baumgarte/*:Number*/ )/*:Boolean*/
	{
		//B2_NOT_USED(baumgarte);
		
		
		var limitC/*:Number*/;
		var oldLimitImpulse/*:Number*/;
		
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var c1/*:b2Vec2*/ = bA.m_sweep.c;
		var a1/*:Number*/ = bA.m_sweep.a;
		
		var c2/*:b2Vec2*/ = bB.m_sweep.c;
		var a2/*:Number*/ = bB.m_sweep.a;
		
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;
		
		var m1/*:Number*/;
		var m2/*:Number*/;
		var i1/*:Number*/;
		var i2/*:Number*/;
		
		// Solve linear limit constraint
		var linearError/*:Number*/ = 0.0;
		var angularError/*:Number*/ = 0.0;
		var active/*:Boolean*/ = false;
		var C2/*:Number*/ = 0.0;
		
		var R1/*:b2Mat22*/ = Box2D.Common.Math.b2Mat22.FromAngle(a1);
		var R2/*:b2Mat22*/ = Box2D.Common.Math.b2Mat22.FromAngle(a2);
		
		//b2Vec2 r1 = b2Mul(R1, m_localAnchor1 - m_localCenter1);
		tMat = R1;
		var r1X/*:Number*/ = this.m_localAnchor1.x - this.m_localCenterA.x;
		var r1Y/*:Number*/ = this.m_localAnchor1.y - this.m_localCenterA.y;
		tX =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
		r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
		r1X = tX;
		//b2Vec2 r2 = b2Mul(R2, m_localAnchor2 - m_localCenter2);
		tMat = R2;
		var r2X/*:Number*/ = this.m_localAnchor2.x - this.m_localCenterB.x;
		var r2Y/*:Number*/ = this.m_localAnchor2.y - this.m_localCenterB.y;
		tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
		r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
		r2X = tX;
		
		var dX/*:Number*/ = c2.x + r2X - c1.x - r1X;
		var dY/*:Number*/ = c2.y + r2Y - c1.y - r1Y;
		
		if (this.m_enableLimit$2)
		{
			this.m_axis$2 = Box2D.Common.Math.b2Math.MulMV(R1, this.m_localXAxis1);
			
			//m_a1 = b2Math.b2Cross(d + r1, m_axis);
			this.m_a1$2 = (dX + r1X) * this.m_axis$2.y - (dY + r1Y) * this.m_axis$2.x;
			//m_a2 = b2Math.b2Cross(r2, m_axis);
			this.m_a2$2 = r2X * this.m_axis$2.y - r2Y * this.m_axis$2.x;
			
			var translation/*:Number*/ = this.m_axis$2.x * dX + this.m_axis$2.y * dY;
			if (Box2D.Common.Math.b2Math.Abs(this.m_upperTranslation$2 - this.m_lowerTranslation$2) < 2.0 * Box2D.Common.b2Settings.b2_linearSlop)
			{
				// Prevent large angular corrections.
				C2 = Box2D.Common.Math.b2Math.Clamp(translation, -Box2D.Common.b2Settings.b2_maxLinearCorrection, Box2D.Common.b2Settings.b2_maxLinearCorrection);
				linearError = Box2D.Common.Math.b2Math.Abs(translation);
				active = true;
			}
			else if (translation <= this.m_lowerTranslation$2)
			{
				// Prevent large angular corrections and allow some slop.
				C2 = Box2D.Common.Math.b2Math.Clamp(translation - this.m_lowerTranslation$2 + Box2D.Common.b2Settings.b2_linearSlop, -Box2D.Common.b2Settings.b2_maxLinearCorrection, 0.0);
				linearError = this.m_lowerTranslation$2 - translation;
				active = true;
			}
			else if (translation >= this.m_upperTranslation$2)
			{
				// Prevent large angular corrections and allow some slop.
				C2 = Box2D.Common.Math.b2Math.Clamp(translation - this.m_upperTranslation$2 + Box2D.Common.b2Settings.b2_linearSlop, 0.0, Box2D.Common.b2Settings.b2_maxLinearCorrection);
				linearError = translation - this.m_upperTranslation$2;
				active = true;
			}
		}
		
		this.m_perp$2 = Box2D.Common.Math.b2Math.MulMV(R1, this.m_localYAxis1$2);
		
		//m_s1 = b2Cross(d + r1, m_perp); 
		this.m_s1$2 = (dX + r1X) * this.m_perp$2.y - (dY + r1Y) * this.m_perp$2.x;
		//m_s2 = b2Cross(r2, m_perp); 
		this.m_s2$2 = r2X * this.m_perp$2.y - r2Y * this.m_perp$2.x;
		
		var impulse/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2();
		var C1/*:Number*/ = this.m_perp$2.x * dX + this.m_perp$2.y * dY;
		
		linearError = Box2D.Common.Math.b2Math.Max(linearError, Box2D.Common.Math.b2Math.Abs(C1));
		angularError = 0.0;
		
		if (active)
		{
			m1 = this.m_invMassA;
			m2 = this.m_invMassB;
			i1 = this.m_invIA;
			i2 = this.m_invIB;
			
			this.m_K$2.col1.x = m1 + m2 + i1 * this.m_s1$2 * this.m_s1$2 + i2 * this.m_s2$2 * this.m_s2$2;
 	  	  	this.m_K$2.col1.y = i1 * this.m_s1$2 * this.m_a1$2 + i2 * this.m_s2$2 * this.m_a2$2;
			this.m_K$2.col2.x = this.m_K$2.col1.y;
 	  	  	this.m_K$2.col2.y = m1 + m2 + i1 * this.m_a1$2 * this.m_a1$2 + i2 * this.m_a2$2 * this.m_a2$2;
			
			this.m_K$2.Solve(impulse, -C1, -C2);
		}
		else
		{
			m1 = this.m_invMassA;
			m2 = this.m_invMassB;
			i1 = this.m_invIA;
			i2 = this.m_invIB;
			
			var k11/*:Number*/  = m1 + m2 + i1 * this.m_s1$2 * this.m_s1$2 + i2 * this.m_s2$2 * this.m_s2$2;
			
			var impulse1/*:Number*/;
			if (k11 != 0.0)
			{
				impulse1 = ( -C1) / k11;
			}else {
				impulse1 = 0.0;
			}
			impulse.x = impulse1;
			impulse.y = 0.0;
		}
		
		var PX/*:Number*/ = impulse.x * this.m_perp$2.x + impulse.y * this.m_axis$2.x;
		var PY/*:Number*/ = impulse.x * this.m_perp$2.y + impulse.y * this.m_axis$2.y;
		var L1/*:Number*/ = impulse.x * this.m_s1$2 + impulse.y * this.m_a1$2;
		var L2/*:Number*/ = impulse.x * this.m_s2$2 + impulse.y * this.m_a2$2;
		
		c1.x -= this.m_invMassA * PX;
		c1.y -= this.m_invMassA * PY;
		a1 -= this.m_invIA * L1;
		
		c2.x += this.m_invMassB * PX;
		c2.y += this.m_invMassB * PY;
		a2 += this.m_invIB * L2;
		
		// TODO_ERIN remove need for this
		//bA.m_sweep.c = c1;	//Already done by reference
		bA.m_sweep.a = a1;
		//bB.m_sweep.c = c2;	//Already done by reference
		bB.m_sweep.a = a2;
		bA.SynchronizeTransform();
		bB.SynchronizeTransform(); 
		
		return linearError <= Box2D.Common.b2Settings.b2_linearSlop && angularError <= Box2D.Common.b2Settings.b2_angularSlop;
		
	},

	"b2internal var",{ m_localAnchor1/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"b2internal var",{ m_localAnchor2/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"b2internal var",{ m_localXAxis1/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_localYAxis1/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},

	"private var",{ m_axis/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_perp/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_s1/*:Number*/:NaN},
	"private var",{ m_s2/*:Number*/:NaN},
	"private var",{ m_a1/*:Number*/:NaN},
	"private var",{ m_a2/*:Number*/:NaN},
	
	"private var",{ m_K/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"private var",{ m_impulse/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},

	"private var",{ m_motorMass/*:Number*/:NaN},			// effective mass for motor/limit translational constraint.
	"private var",{ m_motorImpulse/*:Number*/:NaN},

	"private var",{ m_lowerTranslation/*:Number*/:NaN},
	"private var",{ m_upperTranslation/*:Number*/:NaN},
	"private var",{ m_maxMotorForce/*:Number*/:NaN},
	"private var",{ m_motorSpeed/*:Number*/:NaN},
	
	"private var",{ m_enableLimit/*:Boolean*/:false},
	"private var",{ m_enableMotor/*:Boolean*/:false},
	"private var",{ m_limitState/*:int*/:0},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Math","Number","Box2D.Common.b2Settings","Box2D.Common.Math.b2Mat22"], "0.8.0", "0.8.1"

);