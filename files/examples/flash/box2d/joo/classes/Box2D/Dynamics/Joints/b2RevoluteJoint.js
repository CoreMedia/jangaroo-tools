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
// C = p2 - p1
// Cdot = v2 - v1
//      = v2 + cross(w2, r2) - v1 - cross(w1, r1)
// J = [-I -r1_skew I r2_skew ]
// Identity used:
// w k % (rx i + ry j) = w * (-ry i + rx j)

// Motor constraint
// Cdot = w2 - w1
// J = [0 0 -1 0 0 1]
// K = invI1 + invI2

/**
* A revolute joint constrains to bodies to share a common point while they
* are free to rotate about the point. The relative rotation about the shared
* point is the joint angle. You can limit the relative rotation with
* a joint limit that specifies a lower and upper angle. You can use a motor
* to drive the relative rotation about the shared point. A maximum motor torque
* is provided so that infinite forces are not generated.
* @see b2RevoluteJointDef
*/
"public class b2RevoluteJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

	/** @inheritDoc */
	"public override function GetAnchorA",function GetAnchorA()/* :b2Vec2*/{
		return this.m_bodyA.GetWorldPoint(this.m_localAnchor1);
	},
	/** @inheritDoc */
	"public override function GetAnchorB",function GetAnchorB()/* :b2Vec2*/{
		return this.m_bodyB.GetWorldPoint(this.m_localAnchor2);
	},

	/** @inheritDoc */
	"public override function GetReactionForce",function GetReactionForce(inv_dt/*:Number*/)/* :b2Vec2*/{
		return new Box2D.Common.Math.b2Vec2(inv_dt * this.m_impulse$2.x, inv_dt * this.m_impulse$2.y);
	},
	/** @inheritDoc */
	"public override function GetReactionTorque",function GetReactionTorque(inv_dt/*:Number*/)/* :Number*/{
		return inv_dt * this.m_impulse$2.z;
	},

	/**
	* Get the current joint angle in radians.
	*/
	"public function GetJointAngle",function GetJointAngle()/* :Number*/{
		//b2Body* bA = m_bodyA;
		//b2Body* bB = m_bodyB;
		return this.m_bodyB.m_sweep.a - this.m_bodyA.m_sweep.a - this.m_referenceAngle$2;
	},

	/**
	* Get the current joint angle speed in radians per second.
	*/
	"public function GetJointSpeed",function GetJointSpeed()/* :Number*/{
		//b2Body* bA = m_bodyA;
		//b2Body* bB = m_bodyB;
		return this.m_bodyB.m_angularVelocity - this.m_bodyA.m_angularVelocity;
	},

	/**
	* Is the joint limit enabled?
	*/
	"public function IsLimitEnabled",function IsLimitEnabled()/* :Boolean*/{
		return this.m_enableLimit$2;
	},

	/**
	* Enable/disable the joint limit.
	*/
	"public function EnableLimit",function EnableLimit(flag/*:Boolean*/)/* :void*/{
		this.m_enableLimit$2 = flag;
	},

	/**
	* Get the lower joint limit in radians.
	*/
	"public function GetLowerLimit",function GetLowerLimit()/* :Number*/{
		return this.m_lowerAngle$2;
	},

	/**
	* Get the upper joint limit in radians.
	*/
	"public function GetUpperLimit",function GetUpperLimit()/* :Number*/{
		return this.m_upperAngle$2;
	},

	/**
	* Set the joint limits in radians.
	*/
	"public function SetLimits",function SetLimits(lower/*:Number*/, upper/*:Number*/)/* : void*/{
		//b2Settings.b2Assert(lower <= upper);
		this.m_lowerAngle$2 = lower;
		this.m_upperAngle$2 = upper;
	},

	/**
	* Is the joint motor enabled?
	*/
	"public function IsMotorEnabled",function IsMotorEnabled()/* :Boolean*/ {
		this.m_bodyA.SetAwake(true);
		this.m_bodyB.SetAwake(true);
		return this.m_enableMotor$2;
	},

	/**
	* Enable/disable the joint motor.
	*/
	"public function EnableMotor",function EnableMotor(flag/*:Boolean*/)/* :void*/{
		this.m_enableMotor$2 = flag;
	},

	/**
	* Set the motor speed in radians per second.
	*/
	"public function SetMotorSpeed",function SetMotorSpeed(speed/*:Number*/)/* : void*/ {
		this.m_bodyA.SetAwake(true);
		this.m_bodyB.SetAwake(true);
		this.m_motorSpeed$2 = speed;
	},

	/**
	* Get the motor speed in radians per second.
	*/
	"public function GetMotorSpeed",function GetMotorSpeed()/* :Number*/{
		return this.m_motorSpeed$2;
	},

	/**
	* Set the maximum motor torque, usually in N-m.
	*/
	"public function SetMaxMotorTorque",function SetMaxMotorTorque(torque/*:Number*/)/* : void*/{
		this.m_maxMotorTorque$2 = torque;
	},

	/**
	* Get the current motor torque, usually in N-m.
	*/
	"public function GetMotorTorque",function GetMotorTorque()/* :Number*/{
		return this.m_maxMotorTorque$2;
	},

	//--------------- Internals Below -------------------

	/** @private */
	"public function b2RevoluteJoint",function b2RevoluteJoint$(def/*:b2RevoluteJointDef*/){
		this.super$2(def);this.K$2=this.K$2();this.K1$2=this.K1$2();this.K2$2=this.K2$2();this.K3$2=this.K3$2();this.impulse3$2=this.impulse3$2();this.impulse2$2=this.impulse2$2();this.reduced$2=this.reduced$2();this.m_localAnchor1=this.m_localAnchor1();this.m_localAnchor2=this.m_localAnchor2();this.m_impulse$2=this.m_impulse$2();this.m_mass$2=this.m_mass$2();
		
		//m_localAnchor1 = def->localAnchorA;
		this.m_localAnchor1.SetV(def.localAnchorA);
		//m_localAnchor2 = def->localAnchorB;
		this.m_localAnchor2.SetV(def.localAnchorB);
		
		this.m_referenceAngle$2 = def.referenceAngle;
		
		this.m_impulse$2.SetZero();
		this.m_motorImpulse$2 = 0.0;
		
		this.m_lowerAngle$2 = def.lowerAngle;
		this.m_upperAngle$2 = def.upperAngle;
		this.m_maxMotorTorque$2 = def.maxMotorTorque;
		this.m_motorSpeed$2 = def.motorSpeed;
		this.m_enableLimit$2 = def.enableLimit;
		this.m_enableMotor$2 = def.enableMotor;
		this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
	},

	// internal vars
	"private var",{ K/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"private var",{ K1/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"private var",{ K2/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"private var",{ K3/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"b2internal override function InitVelocityConstraints",function InitVelocityConstraints(step/*:b2TimeStep*/)/* : void*/{
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;
		
		if (this.m_enableMotor$2 || this.m_enableLimit$2)
		{
			// You cannot create prismatic joint between bodies that
			// both have fixed rotation.
			//b2Settings.b2Assert(bA.m_invI > 0.0 || bB.m_invI > 0.0);
		}
		
		
		// Compute the effective mass matrix.
		
		//b2Vec2 r1 = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
		tMat = bA.m_xf.R;
		var r1X/*:Number*/ = this.m_localAnchor1.x - bA.m_sweep.localCenter.x;
		var r1Y/*:Number*/ = this.m_localAnchor1.y - bA.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
		r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
		r1X = tX;
		//b2Vec2 r2 = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
		tMat = bB.m_xf.R;
		var r2X/*:Number*/ = this.m_localAnchor2.x - bB.m_sweep.localCenter.x;
		var r2Y/*:Number*/ = this.m_localAnchor2.y - bB.m_sweep.localCenter.y;
		tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
		r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
		r2X = tX;
		
		// J = [-I -r1_skew I r2_skew] 
		// [ 0 -1 0 1]
		// r_skew = [-ry; rx] 
		
		// Matlab
		// K = [ m1+r1y^2*i1+m2+r2y^2*i2, -r1y*i1*r1x-r2y*i2*r2x, -r1y*i1-r2y*i2]
		//     [ -r1y*i1*r1x-r2y*i2*r2x, m1+r1x^2*i1+m2+r2x^2*i2, r1x*i1+r2x*i2] 
		//     [ -r1y*i1-r2y*i2, r1x*i1+r2x*i2, i1+i2] 
		
		var m1/*:Number*/ = bA.m_invMass;
		var m2/*:Number*/ = bB.m_invMass;
		var i1/*:Number*/ = bA.m_invI;
		var i2/*:Number*/ = bB.m_invI;
		
		this.m_mass$2.col1.x = m1 + m2 + r1Y * r1Y * i1 + r2Y * r2Y * i2;
		this.m_mass$2.col2.x = -r1Y * r1X * i1 - r2Y * r2X * i2;
		this.m_mass$2.col3.x = -r1Y * i1 - r2Y * i2;
		this.m_mass$2.col1.y = this.m_mass$2.col2.x;
		this.m_mass$2.col2.y = m1 + m2 + r1X * r1X * i1 + r2X * r2X * i2;
		this.m_mass$2.col3.y = r1X * i1 + r2X * i2;
		this.m_mass$2.col1.z = this.m_mass$2.col3.x;
		this.m_mass$2.col2.z = this.m_mass$2.col3.y;
		this.m_mass$2.col3.z = i1 + i2;
		
		
		this.m_motorMass$2 = i1 + i2;
		if (this.m_motorMass$2 > 0.0)
		{
			this.m_motorMass$2 = 1.0 / this.m_motorMass$2;
		}
		
		if (this.m_enableMotor$2 == false)
		{
			this.m_motorImpulse$2 = 0.0;
		}
		
		if (this.m_enableLimit$2)
		{
			//float32 jointAngle = bB->m_sweep.a - bA->m_sweep.a - m_referenceAngle;
			var jointAngle/*:Number*/ = bB.m_sweep.a - bA.m_sweep.a - this.m_referenceAngle$2;
			if (Box2D.Common.Math.b2Math.Abs(this.m_upperAngle$2 - this.m_lowerAngle$2) < 2.0 * Box2D.Common.b2Settings.b2_angularSlop)
			{
				this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_equalLimits;
			}
			else if (jointAngle <= this.m_lowerAngle$2)
			{
				if (this.m_limitState$2 != Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
				{
					this.m_impulse$2.z = 0.0;
				}
				this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit;
			}
			else if (jointAngle >= this.m_upperAngle$2)
			{
				if (this.m_limitState$2 != Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
				{
					this.m_impulse$2.z = 0.0;
				}
				this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit;
			}
			else
			{
				this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
				this.m_impulse$2.z = 0.0;
			}
		}
		else
		{
			this.m_limitState$2 = Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
		}
		
		// Warm starting.
		if (step.warmStarting)
		{
			//Scale impulses to support a variable time step
			this.m_impulse$2.x *= step.dtRatio;
			this.m_impulse$2.y *= step.dtRatio;
			this.m_motorImpulse$2 *= step.dtRatio;
			
			var PX/*:Number*/ = this.m_impulse$2.x;
			var PY/*:Number*/ = this.m_impulse$2.y;
			
			//bA->m_linearVelocity -= m1 * P;
			bA.m_linearVelocity.x -= m1 * PX;
			bA.m_linearVelocity.y -= m1 * PY;
			//bA->m_angularVelocity -= i1 * (b2Cross(r1, P) + m_motorImpulse + m_impulse.z);
			bA.m_angularVelocity -= i1 * ((r1X * PY - r1Y * PX) + this.m_motorImpulse$2 + this.m_impulse$2.z);
			
			//bB->m_linearVelocity += m2 * P;
			bB.m_linearVelocity.x += m2 * PX;
			bB.m_linearVelocity.y += m2 * PY;
			//bB->m_angularVelocity += i2 * (b2Cross(r2, P) + m_motorImpulse + m_impulse.z);
			bB.m_angularVelocity += i2 * ((r2X * PY - r2Y * PX) + this.m_motorImpulse$2 + this.m_impulse$2.z);
		}
		else
		{
			this.m_impulse$2.SetZero();
			this.m_motorImpulse$2 = 0.0;
		}
	},
	
	"private var",{ impulse3/*:b2Vec3*/ :function(){return( new Box2D.Common.Math.b2Vec3());}},
	"private var",{ impulse2/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ reduced/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"b2internal override function SolveVelocityConstraints",function SolveVelocityConstraints(step/*:b2TimeStep*/)/* : void*/ {
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var tMat/*:b2Mat22*/;
		var tX/*:Number*/;
		
		var newImpulse/*:Number*/;
		var r1X/*:Number*/;
		var r1Y/*:Number*/;
		var r2X/*:Number*/;
		var r2Y/*:Number*/;
		
		var v1/*:b2Vec2*/ = bA.m_linearVelocity;
		var w1/*:Number*/ = bA.m_angularVelocity;
		var v2/*:b2Vec2*/ = bB.m_linearVelocity;
		var w2/*:Number*/ = bB.m_angularVelocity;
		
		var m1/*:Number*/ = bA.m_invMass;
		var m2/*:Number*/ = bB.m_invMass;
		var i1/*:Number*/ = bA.m_invI;
		var i2/*:Number*/ = bB.m_invI;
		
		// Solve motor constraint.
		if (this.m_enableMotor$2 && this.m_limitState$2 != Box2D.Dynamics.Joints.b2Joint.e_equalLimits)
		{
			var Cdot/*:Number*/ = w2 - w1 - this.m_motorSpeed$2;
			var impulse/*:Number*/ = this.m_motorMass$2 * ( -Cdot);
			var oldImpulse/*:Number*/ = this.m_motorImpulse$2;
			var maxImpulse/*:Number*/ = step.dt * this.m_maxMotorTorque$2;
			
			this.m_motorImpulse$2 = Box2D.Common.Math.b2Math.Clamp(this.m_motorImpulse$2 + impulse, -maxImpulse, maxImpulse);
			impulse = this.m_motorImpulse$2 - oldImpulse;
			
			w1 -= i1 * impulse;
			w2 += i2 * impulse;
		}
		
		// Solve limit constraint.
		if (this.m_enableLimit$2 && this.m_limitState$2 != Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit)
		{
			//b2Vec2 r1 = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
			tMat = bA.m_xf.R;
			r1X = this.m_localAnchor1.x - bA.m_sweep.localCenter.x;
			r1Y = this.m_localAnchor1.y - bA.m_sweep.localCenter.y;
			tX =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
			r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
			r1X = tX;
			//b2Vec2 r2 = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
			tMat = bB.m_xf.R;
			r2X = this.m_localAnchor2.x - bB.m_sweep.localCenter.x;
			r2Y = this.m_localAnchor2.y - bB.m_sweep.localCenter.y;
			tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
			r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
			r2X = tX;
			
			// Solve point-to-point constraint
			//b2Vec2 Cdot1 = v2 + b2Cross(w2, r2) - v1 - b2Cross(w1, r1);
			var Cdot1X/*:Number*/ = v2.x + (-w2 * r2Y) - v1.x - (-w1 * r1Y);
			var Cdot1Y/*:Number*/ = v2.y + (w2 * r2X) - v1.y - (w1 * r1X);
			var Cdot2/*:Number*/  = w2 - w1;
			
			this.m_mass$2.Solve33(this.impulse3$2, -Cdot1X, -Cdot1Y, -Cdot2);
			
			if (this.m_limitState$2 == Box2D.Dynamics.Joints.b2Joint.e_equalLimits)
			{
				this.m_impulse$2.Add(this.impulse3$2);
			}
			else if (this.m_limitState$2 == Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
			{
				newImpulse = this.m_impulse$2.z + this.impulse3$2.z;
				if (newImpulse < 0.0)
				{
					this.m_mass$2.Solve22(this.reduced$2, -Cdot1X, -Cdot1Y);
					this.impulse3$2.x = this.reduced$2.x;
					this.impulse3$2.y = this.reduced$2.y;
					this.impulse3$2.z = -this.m_impulse$2.z;
					this.m_impulse$2.x += this.reduced$2.x;
					this.m_impulse$2.y += this.reduced$2.y;
					this.m_impulse$2.z = 0.0;
				}
			}
			else if (this.m_limitState$2 == Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
			{
				newImpulse = this.m_impulse$2.z + this.impulse3$2.z;
				if (newImpulse > 0.0)
				{
					this.m_mass$2.Solve22(this.reduced$2, -Cdot1X, -Cdot1Y);
					this.impulse3$2.x = this.reduced$2.x;
					this.impulse3$2.y = this.reduced$2.y;
					this.impulse3$2.z = -this.m_impulse$2.z;
					this.m_impulse$2.x += this.reduced$2.x;
					this.m_impulse$2.y += this.reduced$2.y;
					this.m_impulse$2.z = 0.0;
				}
			}
			
			v1.x -= m1 * this.impulse3$2.x;
			v1.y -= m1 * this.impulse3$2.y;
			w1 -= i1 * (r1X * this.impulse3$2.y - r1Y * this.impulse3$2.x + this.impulse3$2.z);
			
			v2.x += m2 * this.impulse3$2.x;
			v2.y += m2 * this.impulse3$2.y;
			w2 += i2 * (r2X * this.impulse3$2.y - r2Y * this.impulse3$2.x + this.impulse3$2.z);
		}
		else
		{
			//b2Vec2 r1 = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
			tMat = bA.m_xf.R;
			r1X = this.m_localAnchor1.x - bA.m_sweep.localCenter.x;
			r1Y = this.m_localAnchor1.y - bA.m_sweep.localCenter.y;
			tX =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
			r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
			r1X = tX;
			//b2Vec2 r2 = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
			tMat = bB.m_xf.R;
			r2X = this.m_localAnchor2.x - bB.m_sweep.localCenter.x;
			r2Y = this.m_localAnchor2.y - bB.m_sweep.localCenter.y;
			tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
			r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
			r2X = tX;
			
			//b2Vec2 Cdot = v2 + b2Cross(w2, r2) - v1 - b2Cross(w1, r1);
			var CdotX/*:Number*/ = v2.x + ( -w2 * r2Y) - v1.x - ( -w1 * r1Y);
			var CdotY/*:Number*/ = v2.y + (w2 * r2X) - v1.y - (w1 * r1X);
			
			this.m_mass$2.Solve22(this.impulse2$2, -CdotX, -CdotY);
			
			this.m_impulse$2.x += this.impulse2$2.x;
			this.m_impulse$2.y += this.impulse2$2.y;
			
			v1.x -= m1 * this.impulse2$2.x;
			v1.y -= m1 * this.impulse2$2.y;
			//w1 -= i1 * b2Cross(r1, impulse2); 
			w1 -= i1 * ( r1X * this.impulse2$2.y - r1Y * this.impulse2$2.x);
			
			v2.x += m2 * this.impulse2$2.x;
			v2.y += m2 * this.impulse2$2.y;
			//w2 += i2 * b2Cross(r2, impulse2); 
			w2 += i2 * ( r2X * this.impulse2$2.y - r2Y * this.impulse2$2.x);
		}
		
		bA.m_linearVelocity.SetV(v1);
		bA.m_angularVelocity = w1;
		bB.m_linearVelocity.SetV(v2);
		bB.m_angularVelocity = w2;
	},
	
	"private static var",{ tImpulse/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"b2internal override function SolvePositionConstraints",function SolvePositionConstraints(baumgarte/*:Number*/)/*:Boolean*/{
		
		// TODO_ERIN block solve with limit
		
		var oldLimitImpulse/*:Number*/;
		var C/*:Number*/;
		
		var tMat/*:b2Mat22*/;
		
		var bA/*:b2Body*/ = this.m_bodyA;
		var bB/*:b2Body*/ = this.m_bodyB;
		
		var angularError/*:Number*/ = 0.0;
		var positionError/*:Number*/ = 0.0;
		
		var tX/*:Number*/;
		
		var impulseX/*:Number*/;
		var impulseY/*:Number*/;
		
		// Solve angular limit constraint.
		if (this.m_enableLimit$2 && this.m_limitState$2 != Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit)
		{
			var angle/*:Number*/ = bB.m_sweep.a - bA.m_sweep.a - this.m_referenceAngle$2;
			var limitImpulse/*:Number*/ = 0.0;
			
			if (this.m_limitState$2 == Box2D.Dynamics.Joints.b2Joint.e_equalLimits)
			{
				// Prevent large angular corrections
				C = Box2D.Common.Math.b2Math.Clamp(angle - this.m_lowerAngle$2, -Box2D.Common.b2Settings.b2_maxAngularCorrection, Box2D.Common.b2Settings.b2_maxAngularCorrection);
				limitImpulse = -this.m_motorMass$2 * C;
				angularError = Box2D.Common.Math.b2Math.Abs(C);
			}
			else if (this.m_limitState$2 == Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
			{
				C = angle - this.m_lowerAngle$2;
				angularError = -C;
				
				// Prevent large angular corrections and allow some slop.
				C = Box2D.Common.Math.b2Math.Clamp(C + Box2D.Common.b2Settings.b2_angularSlop, -Box2D.Common.b2Settings.b2_maxAngularCorrection, 0.0);
				limitImpulse = -this.m_motorMass$2 * C;
			}
			else if (this.m_limitState$2 == Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
			{
				C = angle - this.m_upperAngle$2;
				angularError = C;
				
				// Prevent large angular corrections and allow some slop.
				C = Box2D.Common.Math.b2Math.Clamp(C - Box2D.Common.b2Settings.b2_angularSlop, 0.0, Box2D.Common.b2Settings.b2_maxAngularCorrection);
				limitImpulse = -this.m_motorMass$2 * C;
			}
			
			bA.m_sweep.a -= bA.m_invI * limitImpulse;
			bB.m_sweep.a += bB.m_invI * limitImpulse;
			
			bA.SynchronizeTransform();
			bB.SynchronizeTransform();
		}
		
		// Solve point-to-point constraint
		{
			//b2Vec2 r1 = b2Mul(bA->m_xf.R, m_localAnchor1 - bA->GetLocalCenter());
			tMat = bA.m_xf.R;
			var r1X/*:Number*/ = this.m_localAnchor1.x - bA.m_sweep.localCenter.x;
			var r1Y/*:Number*/ = this.m_localAnchor1.y - bA.m_sweep.localCenter.y;
			tX =  (tMat.col1.x * r1X + tMat.col2.x * r1Y);
			r1Y = (tMat.col1.y * r1X + tMat.col2.y * r1Y);
			r1X = tX;
			//b2Vec2 r2 = b2Mul(bB->m_xf.R, m_localAnchor2 - bB->GetLocalCenter());
			tMat = bB.m_xf.R;
			var r2X/*:Number*/ = this.m_localAnchor2.x - bB.m_sweep.localCenter.x;
			var r2Y/*:Number*/ = this.m_localAnchor2.y - bB.m_sweep.localCenter.y;
			tX =  (tMat.col1.x * r2X + tMat.col2.x * r2Y);
			r2Y = (tMat.col1.y * r2X + tMat.col2.y * r2Y);
			r2X = tX;
			
			//b2Vec2 C = bB->m_sweep.c + r2 - bA->m_sweep.c - r1;
			var CX/*:Number*/ = bB.m_sweep.c.x + r2X - bA.m_sweep.c.x - r1X;
			var CY/*:Number*/ = bB.m_sweep.c.y + r2Y - bA.m_sweep.c.y - r1Y;
			var CLengthSquared/*:Number*/ = CX * CX + CY * CY;
			var CLength/*:Number*/ = Math.sqrt(CLengthSquared);
			positionError = CLength;
			
			var invMass1/*:Number*/ = bA.m_invMass;
			var invMass2/*:Number*/ = bB.m_invMass;
			var invI1/*:Number*/ = bA.m_invI;
			var invI2/*:Number*/ = bB.m_invI;/*
			
			//Handle large detachment.
			const*/var k_allowedStretch/*:Number*/ = 10.0 * Box2D.Common.b2Settings.b2_linearSlop;
			if (CLengthSquared > k_allowedStretch * k_allowedStretch)
			{
				// Use a particle solution (no rotation)
				//b2Vec2 u = C; u.Normalize(); 
				var uX/*:Number*/ = CX / CLength;
				var uY/*:Number*/ = CY / CLength;
				var k/*:Number*/ = invMass1 + invMass2;
				//b2Settings.b2Assert(k>Number.MIN_VALUE)
				var m/*:Number*/ = 1.0 / k;
				impulseX = m * ( -CX);
				impulseY = m * ( -CY);/*
				const*/var k_beta/*:Number*/ = 0.5;
				bA.m_sweep.c.x -= k_beta * invMass1 * impulseX;
				bA.m_sweep.c.y -= k_beta * invMass1 * impulseY;
				bB.m_sweep.c.x += k_beta * invMass2 * impulseX;
				bB.m_sweep.c.y += k_beta * invMass2 * impulseY;
				
				//C = bB->m_sweep.c + r2 - bA->m_sweep.c - r1;
				CX = bB.m_sweep.c.x + r2X - bA.m_sweep.c.x - r1X;
				CY = bB.m_sweep.c.y + r2Y - bA.m_sweep.c.y - r1Y;
			}
			
			//b2Mat22 K1;
			this.K1$2.col1.x = invMass1 + invMass2;	this.K1$2.col2.x = 0.0;
			this.K1$2.col1.y = 0.0;					this.K1$2.col2.y = invMass1 + invMass2;
			
			//b2Mat22 K2;
			this.K2$2.col1.x =  invI1 * r1Y * r1Y;	this.K2$2.col2.x = -invI1 * r1X * r1Y;
			this.K2$2.col1.y = -invI1 * r1X * r1Y;	this.K2$2.col2.y =  invI1 * r1X * r1X;
			
			//b2Mat22 K3;
			this.K3$2.col1.x =  invI2 * r2Y * r2Y;		this.K3$2.col2.x = -invI2 * r2X * r2Y;
			this.K3$2.col1.y = -invI2 * r2X * r2Y;		this.K3$2.col2.y =  invI2 * r2X * r2X;
			
			//b2Mat22 K = K1 + K2 + K3;
			this.K$2.SetM(this.K1$2);
			this.K$2.AddM(this.K2$2);
			this.K$2.AddM(this.K3$2);
			//b2Vec2 impulse = K.Solve(-C);
			this.K$2.Solve($$private.tImpulse, -CX, -CY);
			impulseX = $$private.tImpulse.x;
			impulseY = $$private.tImpulse.y;
			
			//bA.m_sweep.c -= bA.m_invMass * impulse;
			bA.m_sweep.c.x -= bA.m_invMass * impulseX;
			bA.m_sweep.c.y -= bA.m_invMass * impulseY;
			//bA.m_sweep.a -= bA.m_invI * b2Cross(r1, impulse);
			bA.m_sweep.a -= bA.m_invI * (r1X * impulseY - r1Y * impulseX);
			
			//bB.m_sweep.c += bB.m_invMass * impulse;
			bB.m_sweep.c.x += bB.m_invMass * impulseX;
			bB.m_sweep.c.y += bB.m_invMass * impulseY;
			//bB.m_sweep.a += bB.m_invI * b2Cross(r2, impulse);
			bB.m_sweep.a += bB.m_invI * (r2X * impulseY - r2Y * impulseX);
			
			bA.SynchronizeTransform();
			bB.SynchronizeTransform();
		}
		
		return positionError <= Box2D.Common.b2Settings.b2_linearSlop && angularError <= Box2D.Common.b2Settings.b2_angularSlop;
	},

	"b2internal var",{ m_localAnchor1/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}}, // relative
	"b2internal var",{ m_localAnchor2/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"private var",{ m_impulse/*:b2Vec3*/ :function(){return( new Box2D.Common.Math.b2Vec3());}},
	"private var",{ m_motorImpulse/*:Number*/:NaN},

	"private var",{ m_mass/*:b2Mat33*/ :function(){return( new Box2D.Common.Math.b2Mat33());}},		// effective mass for point-to-point constraint.
	"private var",{ m_motorMass/*:Number*/:NaN},	// effective mass for motor/limit angular constraint.
	"private var",{ m_enableMotor/*:Boolean*/:false},
	"private var",{ m_maxMotorTorque/*:Number*/:NaN},
	"private var",{ m_motorSpeed/*:Number*/:NaN},

	"private var",{ m_enableLimit/*:Boolean*/:false},
	"private var",{ m_referenceAngle/*:Number*/:NaN},
	"private var",{ m_lowerAngle/*:Number*/:NaN},
	"private var",{ m_upperAngle/*:Number*/:NaN},
	"private var",{ m_limitState/*:int*/:0},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22","Box2D.Common.Math.b2Math","Box2D.Common.b2Settings","Box2D.Common.Math.b2Vec3","Math","Box2D.Common.Math.b2Mat33"], "0.8.0", "0.8.1"

);