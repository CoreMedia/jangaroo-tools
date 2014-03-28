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

"package Box2D.Dynamics",/*{


import Box2D.Dynamics.*
import Box2D.Dynamics.Joints.*
import Box2D.Dynamics.Contacts.*
import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Common.b2Settings
import Box2D.Common.Math.*

import Box2D.Common.b2internal
use namespace b2internal*/


/**
* A body definition holds all the data needed to construct a rigid body.
* You can safely re-use body definitions.
*/
"public class b2BodyDef",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body);},

	/**
	* This constructor sets the body definition default values.
	*/
	"public function b2BodyDef",function b2BodyDef$()
	{this.position=this.position();this.linearVelocity=this.linearVelocity();
		this.userData = null;
		this.position.Set(0.0, 0.0);
		this.angle = 0.0;
		this.linearVelocity.Set(0, 0);
		this.angularVelocity = 0.0;
		this.linearDamping = 0.0;
		this.angularDamping = 0.0;
		this.allowSleep = true;
		this.awake = true;
		this.fixedRotation = false;
		this.bullet = false;
		this.type = Box2D.Dynamics.b2Body.b2_staticBody;
		this.active = true;
		this.inertiaScale = 1.0;
	},

	/** The body type: static, kinematic, or dynamic. A member of the b2BodyType class
	 * Note: if a dynamic body would have zero mass, the mass is set to one.
	 * @see b2Body#b2_staticBody
	 * @see b2Body#b2_dynamicBody
	 * @see b2Body#b2_kinematicBody
	 */
	"public var",{ type/*:uint*/:0},

	/**
	 * The world position of the body. Avoid creating bodies at the origin
	 * since this can lead to many overlapping shapes.
	 */
	"public var",{ position/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},

	/**
	 * The world angle of the body in radians.
	 */
	"public var",{ angle/*:Number*/:NaN},
	
	/**
	 * The linear velocity of the body's origin in world co-ordinates.
	 */
	"public var",{ linearVelocity/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	
	/**
	 * The angular velocity of the body.
	 */
	"public var",{ angularVelocity/*:Number*/:NaN},

	/**
	 * Linear damping is use to reduce the linear velocity. The damping parameter
	 * can be larger than 1.0f but the damping effect becomes sensitive to the
	 * time step when the damping parameter is large.
	 */
	"public var",{ linearDamping/*:Number*/:NaN},

	/**
	 * Angular damping is use to reduce the angular velocity. The damping parameter
	 * can be larger than 1.0f but the damping effect becomes sensitive to the
	 * time step when the damping parameter is large.
	 */
	"public var",{ angularDamping/*:Number*/:NaN},

	/**
	 * Set this flag to false if this body should never fall asleep. Note that
	 * this increases CPU usage.
	 */
	"public var",{ allowSleep/*:Boolean*/:false},

	/**
	 * Is this body initially awake or sleeping?
	 */
	"public var",{ awake/*:Boolean*/:false},

	/**
	 * Should this body be prevented from rotating? Useful for characters.
	 */
	"public var",{ fixedRotation/*:Boolean*/:false},

	/**
	 * Is this a fast moving body that should be prevented from tunneling through
	 * other moving bodies? Note that all bodies are prevented from tunneling through
	 * static bodies.
	 * @warning You should use this flag sparingly since it increases processing time.
	 */
	"public var",{ bullet/*:Boolean*/:false},
	
	/**
	 * Does this body start out active?
	 */ 
	"public var",{ active/*:Boolean*/:false},
	
	/**
	 * Use this to store application specific body data.
	 */
	"public var",{ userData/*:**/:undefined},
	
	/**
	 * Scales the inertia tensor.
	 * @warning Experimental
	 */
	"public var",{ inertiaScale/*:Number*/:NaN},
];},[],["Box2D.Dynamics.b2Body","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"


);