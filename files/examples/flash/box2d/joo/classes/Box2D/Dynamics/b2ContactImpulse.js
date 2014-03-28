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

"package Box2D.Dynamics",/* 
{
	import Box2D.Common.b2Settings*/
	
	/**
	 * Contact impulses for reporting. Impulses are used instead of forces because
	 * sub-step forces may approach infinity for rigid body collisions. These
	 * match up one-to-one with the contact points in b2Manifold.
	 */
	"public class b2ContactImpulse",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);}, 
	
		"public var",{ normalImpulses/*:Array*//*Number*/ :function(){return( new Array/*Number*/(Box2D.Common.b2Settings.b2_maxManifoldPoints));}},
		"public var",{ tangentImpulses/*:Array*//*Number*/ :function(){return( new Array/*Number*/(Box2D.Common.b2Settings.b2_maxManifoldPoints));}},
		
	"public function b2ContactImpulse",function b2ContactImpulse$(){this.normalImpulses=this.normalImpulses();this.tangentImpulses=this.tangentImpulses();}];},[],["Array","Box2D.Common.b2Settings"], "0.8.0", "0.8.1"
	
);