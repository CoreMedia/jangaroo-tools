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

"package Box2D.Dynamics.Contacts",/*{


import Box2D.Common.Math.*
import Box2D.Collision.*
import Box2D.Dynamics.*
import Box2D.Common.*

import Box2D.Common.b2internal
use namespace b2internal*/


/**
* @private
*/
"public class b2ContactConstraint",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

	"public function b2ContactConstraint",function b2ContactConstraint$(){this.localPlaneNormal=this.localPlaneNormal();this.localPoint=this.localPoint();this.normal=this.normal();this.normalMass=this.normalMass();this.K=this.K();
		this.points = new Array/*b2ContactConstraintPoint*/(Box2D.Common.b2Settings.b2_maxManifoldPoints);
		for (var i/*:int*/ = 0; i < Box2D.Common.b2Settings.b2_maxManifoldPoints; i++){
			this.points[i] = new Box2D.Dynamics.Contacts.b2ContactConstraintPoint();
		}
		
		
	},
	"public var",{ points/*:Array*/:null}/*b2ContactConstraintPoint*/,
	"public var",{ localPlaneNormal/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"public var",{ localPoint/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"public var",{ normal/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"public var",{ normalMass/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"public var",{ K/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	"public var",{ bodyA/*:b2Body*/:null},
	"public var",{ bodyB/*:b2Body*/:null},
	"public var",{ type/*:int*/:0},//b2Manifold::Type
	"public var",{ radius/*:Number*/:NaN},
	"public var",{ friction/*:Number*/:NaN},
	"public var",{ restitution/*:Number*/:NaN},
	"public var",{ pointCount/*:int*/:0},
	"public var",{ manifold/*:b2Manifold*/:null},
];},[],["Array","Box2D.Common.b2Settings","Box2D.Dynamics.Contacts.b2ContactConstraintPoint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22"], "0.8.0", "0.8.1"


);