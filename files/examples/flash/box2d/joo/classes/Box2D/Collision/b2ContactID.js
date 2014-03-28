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
	
import Box2D.Collision.Features

import Box2D.Common.b2internal
use namespace b2internal*/

// 
/**
* We use contact ids to facilitate warm starting.
*/
"public class b2ContactID",1,function($$private){;return[

	"public function b2ContactID",function b2ContactID$(){this.features=this.features();
		this.features._m_id = this;
		
	},
	"public function Set",function Set(id/*:b2ContactID*/)/* : void*/{
		this.key = id._key;
	},
	"public function Copy",function Copy()/*:b2ContactID*/{
		var id/*:b2ContactID*/ = new Box2D.Collision.b2ContactID();
		id.key = this.key;
		return id;
	},
	"public function get key",function key$get()/*:uint*/ {
		return this._key;
	},
	"public function set key",function key$set(value/*:uint*/)/* : void*/ {
		this._key = value;
		this.features._referenceEdge = this._key & 0x000000ff;
		this.features._incidentEdge = ((this._key & 0x0000ff00) >> 8) & 0x000000ff;
		this.features._incidentVertex = ((this._key & 0x00ff0000) >> 16) & 0x000000ff;
		this.features._flip = ((this._key & 0xff000000) >> 24) & 0x000000ff;
	},
	"public var",{ features/*:Features*/ :function(){return( new Box2D.Collision.Features());}},
	/** Used to quickly compare contact ids. */
	"b2internal var",{ _key/*:uint*/:0},
];},[],["Box2D.Collision.Features"], "0.8.0", "0.8.1"


);