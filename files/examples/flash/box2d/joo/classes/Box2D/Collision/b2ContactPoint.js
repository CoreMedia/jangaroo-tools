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

"package Box2D.Collision",/*{
	
import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Common.Math.*

import Box2D.Common.b2internal
use namespace b2internal*/

/**
* This structure is used to report contact points.
*/
"public class b2ContactPoint",1,function($$private){;return[

	/** The first shape */
	"public var",{ shape1/*:b2Shape*/:null},
	/** The second shape */
	"public var",{ shape2/*:b2Shape*/:null},
	/** Position in world coordinates */
	"public var",{ position/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	/** Velocity of point on body2 relative to point on body1 (pre-solver) */
	"public var",{ velocity/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	/** Points from shape1 to shape2 */
	"public var",{ normal/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	/** The separation is negative when shapes are touching */
	"public var",{ separation/*:Number*/:NaN},
	/** The combined friction coefficient */
	"public var",{ friction/*:Number*/:NaN},
	/** The combined restitution coefficient */
	"public var",{ restitution/*:Number*/:NaN},
	/** The contact id identifies the features in contact */
	"public var",{ id/*:b2ContactID*/ :function(){return( new Box2D.Collision.b2ContactID());}},
"public function b2ContactPoint",function b2ContactPoint$(){this.position=this.position();this.velocity=this.velocity();this.normal=this.normal();this.id=this.id();}];},[],["Box2D.Common.Math.b2Vec2","Box2D.Collision.b2ContactID"], "0.8.0", "0.8.1"


);