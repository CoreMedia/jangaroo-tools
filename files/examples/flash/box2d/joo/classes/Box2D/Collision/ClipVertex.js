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
	
	
import Box2D.Common.Math.*
import Box2D.Collision.*

import Box2D.Common.b2internal
use namespace b2internal*/
	
	
/**
* @private
*/
"public class ClipVertex",1,function($$private){;return[

	"public function Set",function Set(other/*:ClipVertex*/)/*:void*/
	{
		this.v.SetV(other.v);
		this.id.Set(other.id);
	},
	
	"public var",{ v/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"public var",{ id/*:b2ContactID*/ :function(){return( new Box2D.Collision.b2ContactID());}},
"public function ClipVertex",function ClipVertex$(){this.v=this.v();this.id=this.id();}];},[],["Box2D.Common.Math.b2Vec2","Box2D.Collision.b2ContactID"], "0.8.0", "0.8.1"
	
	
);