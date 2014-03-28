joo.classLoader.prepare(/*
* Copyright (c) 2009 Erin Catto http://www.gphysics.com
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

"package Box2D.Collision",/* 
{*/
	
	/**
	 * A node in the dynamic tree. The client does not interact with this directly.
	 * @private
	 */
	"public class b2DynamicTreeNode",1,function($$private){;return[ 
	
		"public function IsLeaf",function IsLeaf()/*:Boolean*/
		{
			return this.child1 == null;
		},
		
		"public var",{ userData/*:**/:undefined},
		"public var",{ aabb/*:b2AABB*/ :function(){return( new Box2D.Collision.b2AABB());}},
		"public var",{ parent/*:b2DynamicTreeNode*/:null},
		"public var",{ child1/*:b2DynamicTreeNode*/:null},
		"public var",{ child2/*:b2DynamicTreeNode*/:null},
	"public function b2DynamicTreeNode",function b2DynamicTreeNode$(){this.aabb=this.aabb();}];},[],["Box2D.Collision.b2AABB"], "0.8.0", "0.8.1"
	
);