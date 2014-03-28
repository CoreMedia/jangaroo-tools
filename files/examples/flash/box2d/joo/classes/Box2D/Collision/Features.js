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

"package Box2D.Collision",/* {
	
import Box2D.Common.b2internal
use namespace b2internal*/

/**
* We use contact ids to facilitate warm starting.
*/
"public class Features",1,function($$private){;return[

	/**
	* The edge that defines the outward contact normal.
	*/
	"public function get referenceEdge",function referenceEdge$get()/*:int*/{
		return this._referenceEdge;
	},
	"public function set referenceEdge",function referenceEdge$set(value/*:int*/)/* : void*/{
		this._referenceEdge = value;
		this._m_id._key = (this._m_id._key & 0xffffff00) | (this._referenceEdge & 0x000000ff);
	},
	"b2internal var",{ _referenceEdge/*:int*/:0},
	
	/**
	* The edge most anti-parallel to the reference edge.
	*/
	"public function get incidentEdge",function incidentEdge$get()/*:int*/{
		return this._incidentEdge;
	},
	"public function set incidentEdge",function incidentEdge$set(value/*:int*/)/* : void*/{
		this._incidentEdge = value;
		this._m_id._key = (this._m_id._key & 0xffff00ff) | ((this._incidentEdge << 8) & 0x0000ff00);
	},
	"b2internal var",{ _incidentEdge/*:int*/:0},
	
	/**
	* The vertex (0 or 1) on the incident edge that was clipped.
	*/
	"public function get incidentVertex",function incidentVertex$get()/*:int*/{
		return this._incidentVertex;
	},
	"public function set incidentVertex",function incidentVertex$set(value/*:int*/)/* : void*/{
		this._incidentVertex = value;
		this._m_id._key = (this._m_id._key & 0xff00ffff) | ((this._incidentVertex << 16) & 0x00ff0000);
	},
	"b2internal var",{ _incidentVertex/*:int*/:0},
	
	/**
	* A value of 1 indicates that the reference edge is on shape2.
	*/
	"public function get flip",function flip$get()/*:int*/{
		return this._flip;
	},
	"public function set flip",function flip$set(value/*:int*/)/* : void*/{
		this._flip = value;
		this._m_id._key = (this._m_id._key & 0x00ffffff) | ((this._flip << 24) & 0xff000000);
	},
	"b2internal var",{ _flip/*:int*/:0},
	
	
	"b2internal var",{ _m_id/*:b2ContactID*/:null},
];},[],[], "0.8.0", "0.8.1"


);