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
* The base joint class. Joints are used to constraint two bodies together in
* various fashions. Some joints also feature limits and motors.
* @see b2JointDef
*/
"public class b2Joint",1,function($$private){var as=joo.as,assert=joo.assert;return[

	/**
	* Get the type of the concrete joint.
	*/
	"public function GetType",function GetType()/*:int*/{
		return this.m_type;
	},
	
	/**
	* Get the anchor point on bodyA in world coordinates.
	*/
	"public virtual function GetAnchorA",function GetAnchorA()/*:b2Vec2*/{return null;},
	/**
	* Get the anchor point on bodyB in world coordinates.
	*/
	"public virtual function GetAnchorB",function GetAnchorB()/*:b2Vec2*/{return null;},
	
	/**
	* Get the reaction force on body2 at the joint anchor in Newtons.
	*/
	"public virtual function GetReactionForce",function GetReactionForce(inv_dt/*:Number*/)/*:b2Vec2*/ {return null;},
	/**
	* Get the reaction torque on body2 in N*m.
	*/
	"public virtual function GetReactionTorque",function GetReactionTorque(inv_dt/*:Number*/)/*:Number*/ {return 0.0;},
	
	/**
	* Get the first body attached to this joint.
	*/
	"public function GetBodyA",function GetBodyA()/*:b2Body*/
	{
		return this.m_bodyA;
	},
	
	/**
	* Get the second body attached to this joint.
	*/
	"public function GetBodyB",function GetBodyB()/*:b2Body*/
	{
		return this.m_bodyB;
	},

	/**
	* Get the next joint the world joint list.
	*/
	"public function GetNext",function GetNext()/*:b2Joint*/{
		return this.m_next;
	},

	/**
	* Get the user data pointer.
	*/
	"public function GetUserData",function GetUserData()/*:**/{
		return this.m_userData$1;
	},

	/**
	* Set the user data pointer.
	*/
	"public function SetUserData",function SetUserData(data/*:**/)/*:void*/{
		this.m_userData$1 = data;
	},

	/**
	 * Short-cut function to determine if either body is inactive.
	 * @return
	 */
	"public function IsActive",function IsActive()/*:Boolean*/ {
		return this.m_bodyA.IsActive() && this.m_bodyB.IsActive();
	},
	
	//--------------- Internals Below -------------------

	"static b2internal function Create",function Create(def/*:b2JointDef*/, allocator/*:**/)/*:b2Joint*/{
		var joint/*:b2Joint*/ = null;
		
		switch (def.type)
		{
		case Box2D.Dynamics.Joints.b2Joint.e_distanceJoint:
			{
				//void* mem = allocator->Allocate(sizeof(b2DistanceJoint));
				joint = new Box2D.Dynamics.Joints.b2DistanceJoint(as(def,  Box2D.Dynamics.Joints.b2DistanceJointDef));
			}
			break;
		
		case Box2D.Dynamics.Joints.b2Joint.e_mouseJoint:
			{
				//void* mem = allocator->Allocate(sizeof(b2MouseJoint));
				joint = new Box2D.Dynamics.Joints.b2MouseJoint(as(def,  Box2D.Dynamics.Joints.b2MouseJointDef));
			}
			break;
		
		case Box2D.Dynamics.Joints.b2Joint.e_prismaticJoint:
			{
				//void* mem = allocator->Allocate(sizeof(b2PrismaticJoint));
				joint = new Box2D.Dynamics.Joints.b2PrismaticJoint(as(def,  Box2D.Dynamics.Joints.b2PrismaticJointDef));
			}
			break;
		
		case Box2D.Dynamics.Joints.b2Joint.e_revoluteJoint:
			{
				//void* mem = allocator->Allocate(sizeof(b2RevoluteJoint));
				joint = new Box2D.Dynamics.Joints.b2RevoluteJoint(as(def,  Box2D.Dynamics.Joints.b2RevoluteJointDef));
			}
			break;
		
		case Box2D.Dynamics.Joints.b2Joint.e_pulleyJoint:
			{
				//void* mem = allocator->Allocate(sizeof(b2PulleyJoint));
				joint = new Box2D.Dynamics.Joints.b2PulleyJoint(as(def,  Box2D.Dynamics.Joints.b2PulleyJointDef));
			}
			break;
		
		case Box2D.Dynamics.Joints.b2Joint.e_gearJoint:
			{
				//void* mem = allocator->Allocate(sizeof(b2GearJoint));
				joint = new Box2D.Dynamics.Joints.b2GearJoint(as(def,  Box2D.Dynamics.Joints.b2GearJointDef));
			}
			break;
		
		case Box2D.Dynamics.Joints.b2Joint.e_lineJoint:
			{
				//void* mem = allocator->Allocate(sizeof(b2LineJoint));
				joint = new Box2D.Dynamics.Joints.b2LineJoint(as(def,  Box2D.Dynamics.Joints.b2LineJointDef));
			}
			break;
			
		case Box2D.Dynamics.Joints.b2Joint.e_weldJoint:
			{
				//void* mem = allocator->Allocate(sizeof(b2WeldJoint));
				joint = new Box2D.Dynamics.Joints.b2WeldJoint(as(def,  Box2D.Dynamics.Joints.b2WeldJointDef));
			}
			break;
			
		case Box2D.Dynamics.Joints.b2Joint.e_frictionJoint:
			{
				//void* mem = allocator->Allocate(sizeof(b2FrictionJoint));
				joint = new Box2D.Dynamics.Joints.b2FrictionJoint(as(def,  Box2D.Dynamics.Joints.b2FrictionJointDef));
			}
			break;
			
		default:
			//b2Settings.b2Assert(false);
			break;
		}
		
		return joint;
	},
	
	"static b2internal function Destroy",function Destroy(joint/*:b2Joint*/, allocator/*:**/)/* : void*/{
		/*joint->~b2Joint();
		switch (joint.m_type)
		{
		case e_distanceJoint:
			allocator->Free(joint, sizeof(b2DistanceJoint));
			break;
		
		case e_mouseJoint:
			allocator->Free(joint, sizeof(b2MouseJoint));
			break;
		
		case e_prismaticJoint:
			allocator->Free(joint, sizeof(b2PrismaticJoint));
			break;
		
		case e_revoluteJoint:
			allocator->Free(joint, sizeof(b2RevoluteJoint));
			break;
		
		case e_pulleyJoint:
			allocator->Free(joint, sizeof(b2PulleyJoint));
			break;
		
		case e_gearJoint:
			allocator->Free(joint, sizeof(b2GearJoint));
			break;
		
		case e_lineJoint:
			allocator->Free(joint, sizeof(b2LineJoint));
			break;
			
		case e_weldJoint:
			allocator->Free(joint, sizeof(b2WeldJoint));
			break;
			
		case e_frictionJoint:
			allocator->Free(joint, sizeof(b2FrictionJoint));
			break;
		
		default:
			b2Assert(false);
			break;
		}*/
	},

	/** @private */
	"public function b2Joint",function b2Joint$(def/*:b2JointDef*/) {this.m_edgeA=this.m_edgeA();this.m_edgeB=this.m_edgeB();this.m_localCenterA=this.m_localCenterA();this.m_localCenterB=this.m_localCenterB();/*
		assert def.bodyA != def.bodyB;*/
		this.m_type = def.type;
		this.m_prev = null;
		this.m_next = null;
		this.m_bodyA = def.bodyA;
		this.m_bodyB = def.bodyB;
		this.m_collideConnected = def.collideConnected;
		this.m_islandFlag = false;
		this.m_userData$1 = def.userData;
	},
	//virtual ~b2Joint() {}

	"b2internal virtual function InitVelocityConstraints",function InitVelocityConstraints(step/*:b2TimeStep*/)/* : void*/{},
	"b2internal virtual function SolveVelocityConstraints",function SolveVelocityConstraints(step/*:b2TimeStep*/)/* : void*/ { },
	"b2internal virtual function FinalizeVelocityConstraints",function FinalizeVelocityConstraints()/* : void*/{},

	// This returns true if the position errors are within tolerance.
	"b2internal virtual function SolvePositionConstraints",function SolvePositionConstraints(baumgarte/*:Number*/)/*:Boolean*/ { return false; },

	"b2internal var",{ m_type/*:int*/:0},
	"b2internal var",{ m_prev/*:b2Joint*/:null},
	"b2internal var",{ m_next/*:b2Joint*/:null},
	"b2internal var",{ m_edgeA/*:b2JointEdge*/ :function(){return( new Box2D.Dynamics.Joints.b2JointEdge());}},
	"b2internal var",{ m_edgeB/*:b2JointEdge*/ :function(){return( new Box2D.Dynamics.Joints.b2JointEdge());}},
	"b2internal var",{ m_bodyA/*:b2Body*/:null},
	"b2internal var",{ m_bodyB/*:b2Body*/:null},

	"b2internal var",{ m_islandFlag/*:Boolean*/:false},
	"b2internal var",{ m_collideConnected/*:Boolean*/:false},

	"private var",{ m_userData/*:**/:undefined},
	
	// Cache here per time step to reduce cache misses.
	"b2internal var",{ m_localCenterA/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"b2internal var",{ m_localCenterB/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"b2internal var",{ m_invMassA/*:Number*/:NaN},
	"b2internal var",{ m_invMassB/*:Number*/:NaN},
	"b2internal var",{ m_invIA/*:Number*/:NaN},
	"b2internal var",{ m_invIB/*:Number*/:NaN},
	
	// ENUMS
	
	// enum b2JointType
	"static b2internal const",{ e_unknownJoint/*:int*/ : 0},
	"static b2internal const",{ e_revoluteJoint/*:int*/ : 1},
	"static b2internal const",{ e_prismaticJoint/*:int*/ : 2},
	"static b2internal const",{ e_distanceJoint/*:int*/ : 3},
	"static b2internal const",{ e_pulleyJoint/*:int*/ : 4},
	"static b2internal const",{ e_mouseJoint/*:int*/ : 5},
	"static b2internal const",{ e_gearJoint/*:int*/ : 6},
	"static b2internal const",{ e_lineJoint/*:int*/ : 7},
	"static b2internal const",{ e_weldJoint/*:int*/ : 8},
	"static b2internal const",{ e_frictionJoint/*:int*/ : 9},

	// enum b2LimitState
	"static b2internal const",{ e_inactiveLimit/*:int*/ : 0},
	"static b2internal const",{ e_atLowerLimit/*:int*/ : 1},
	"static b2internal const",{ e_atUpperLimit/*:int*/ : 2},
	"static b2internal const",{ e_equalLimits/*:int*/ : 3},
	
];},["Create","Destroy"],["Box2D.Dynamics.Joints.b2DistanceJoint","Box2D.Dynamics.Joints.b2DistanceJointDef","Box2D.Dynamics.Joints.b2MouseJoint","Box2D.Dynamics.Joints.b2MouseJointDef","Box2D.Dynamics.Joints.b2PrismaticJoint","Box2D.Dynamics.Joints.b2PrismaticJointDef","Box2D.Dynamics.Joints.b2RevoluteJoint","Box2D.Dynamics.Joints.b2RevoluteJointDef","Box2D.Dynamics.Joints.b2PulleyJoint","Box2D.Dynamics.Joints.b2PulleyJointDef","Box2D.Dynamics.Joints.b2GearJoint","Box2D.Dynamics.Joints.b2GearJointDef","Box2D.Dynamics.Joints.b2LineJoint","Box2D.Dynamics.Joints.b2LineJointDef","Box2D.Dynamics.Joints.b2WeldJoint","Box2D.Dynamics.Joints.b2WeldJointDef","Box2D.Dynamics.Joints.b2FrictionJoint","Box2D.Dynamics.Joints.b2FrictionJointDef","Box2D.Dynamics.Joints.b2JointEdge","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"



);