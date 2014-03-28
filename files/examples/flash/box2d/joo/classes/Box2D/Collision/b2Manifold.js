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
import Box2D.Common.Math.*
import Box2D.Common.*

import Box2D.Common.b2internal
use namespace b2internal*/

/**
 * A manifold for two touching convex shapes.
 * Box2D supports multiple types of contact:
 * - clip point versus plane with radius
 * - point versus point with radius (circles)
 * The local point usage depends on the manifold type:
 * -e_circles: the local center of circleA
 * -e_faceA: the center of faceA
 * -e_faceB: the center of faceB
 * Similarly the local normal usage:
 * -e_circles: not used
 * -e_faceA: the normal on polygonA
 * -e_faceB: the normal on polygonB
 * We store contacts in this way so that position correction can
 * account for movement, which is critical for continuous physics.
 * All contact scenarios must be expressed in one of these types.
 * This structure is stored across time steps, so we keep it small.
 */
"public class b2Manifold",1,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

	"public function b2Manifold",function b2Manifold$(){
		this.m_points = new Array/*b2ManifoldPoint*/(Box2D.Common.b2Settings.b2_maxManifoldPoints);
		for (var i/*:int*/ = 0; i < Box2D.Common.b2Settings.b2_maxManifoldPoints; i++){
			this.m_points[i] = new Box2D.Collision.b2ManifoldPoint();
		}
		this.m_localPlaneNormal = new Box2D.Common.Math.b2Vec2();
		this.m_localPoint = new Box2D.Common.Math.b2Vec2();
	},
	"public function Reset",function Reset()/* : void*/{
		for (var i/*:int*/ = 0; i < Box2D.Common.b2Settings.b2_maxManifoldPoints; i++){
			(as(this.m_points[i],  Box2D.Collision.b2ManifoldPoint)).Reset();
		}
		this.m_localPlaneNormal.SetZero();
		this.m_localPoint.SetZero();
		this.m_type = 0;
		this.m_pointCount = 0;
	},
	"public function Set",function Set(m/*:b2Manifold*/)/* : void*/{
		this.m_pointCount = m.m_pointCount;
		for (var i/*:int*/ = 0; i < Box2D.Common.b2Settings.b2_maxManifoldPoints; i++){
			(as(this.m_points[i],  Box2D.Collision.b2ManifoldPoint)).Set(m.m_points[i]);
		}
		this.m_localPlaneNormal.SetV(m.m_localPlaneNormal);
		this.m_localPoint.SetV(m.m_localPoint);
		this.m_type = m.m_type;
	},
	"public function Copy",function Copy()/*:b2Manifold*/
	{
		var copy/*:b2Manifold*/ = new Box2D.Collision.b2Manifold();
		copy.Set(this);
		return copy;
	},
	/** The points of contact */	
	"public var",{ m_points/*:Array*/:null}/*b2ManifoldPoint*/,	
	/** Not used for Type e_points*/	
	"public var",{ m_localPlaneNormal/*:b2Vec2*/:null},	
	/** Usage depends on manifold type */	
	"public var",{ m_localPoint/*:b2Vec2*/:null},	
	"public var",{ m_type/*:int*/:0},
	/** The number of manifold points */	
	"public var",{ m_pointCount/*:int*/ : 0},
	
	//enum Type
	"public static const",{ e_circles/*:int*/ : 0x0001},
	"public static const",{ e_faceA/*:int*/ : 0x0002},
	"public static const",{ e_faceB/*:int*/ : 0x0004},
];},[],["Array","Box2D.Common.b2Settings","Box2D.Collision.b2ManifoldPoint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"


);