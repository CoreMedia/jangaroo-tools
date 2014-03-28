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
{
	import Box2D.Common.*
	import Box2D.Common.Math.**/
	
	// A dynamic AABB tree broad-phase, inspired by Nathanael Presson's btDbvt.
	
	/**
	 * A dynamic tree arranges data in a binary tree to accelerate
	 * queries such as volume queries and ray casts. Leafs are proxies
	 * with an AABB. In the tree we expand the proxy AABB by b2_fatAABBFactor
	 * so that the proxy AABB is bigger than the client object. This allows the client
	 * object to move by small amounts without triggering a tree update.
	 * 
	 * Nodes are pooled.
	 */
	"public class b2DynamicTree",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);}, 
	
		/**
		 * Constructing the tree initializes the node pool.
		 */
		"public function b2DynamicTree",function b2DynamicTree$() 
		{
			this.m_root$1 = null;
			
			// TODO: Maybe allocate some free nodes?
			this.m_freeList$1 = null;
			this.m_path$1 = 0;
			
			this.m_insertionCount$1 = 0;
		},
		/*
		public function Dump(node:b2DynamicTreeNode=null, depth:int=0):void
		{
			if (!node)
			{
				node = m_root;
			}
			if (!node) return;
			for (var i:int = 0; i < depth; i++) s += " ";
			if (node.userData)
			{
				var ud:* = (node.userData as b2Fixture).GetBody().GetUserData();
				trace(s + ud);
			}else {
				trace(s + "-");
			}
			if (node.child1)
				Dump(node.child1, depth + 1);
			if (node.child2)
				Dump(node.child2, depth + 1);
		}
		*/
		
		/**
		 * Create a proxy. Provide a tight fitting AABB and a userData.
		 */
		"public function CreateProxy",function CreateProxy(aabb/*:b2AABB*/, userData/*:**/)/*:b2DynamicTreeNode*/
		{
			var node/*:b2DynamicTreeNode*/ = this.AllocateNode$1();
			
			// Fatten the aabb.
			var extendX/*:Number*/ = Box2D.Common.b2Settings.b2_aabbExtension;
			var extendY/*:Number*/ = Box2D.Common.b2Settings.b2_aabbExtension;
			node.aabb.lowerBound.x = aabb.lowerBound.x - extendX;
			node.aabb.lowerBound.y = aabb.lowerBound.y - extendY;
			node.aabb.upperBound.x = aabb.upperBound.x + extendX;
			node.aabb.upperBound.y = aabb.upperBound.y + extendY;
			
			node.userData = userData;
			
			this.InsertLeaf$1(node);
			
			// Rebalance if necessary
			// Actually, the tree has no max depth, but it would still be bad.
			var iterationCount/*:int*/ = this.m_nodeCount$1 >> 4;
			var tryCount/*:int*/ = 0;
			var height/*:int*/ = this.ComputeFullHeight$1();
			while (height > 64 && tryCount < 10)
			{
				this.Rebalance(iterationCount);
				height = this.ComputeFullHeight$1();
				++tryCount;
			}
			
			return node;
		},
		
		/**
		 * Destroy a proxy. This asserts if the id is invalid.
		 */
		"public function DestroyProxy",function DestroyProxy(proxy/*:b2DynamicTreeNode*/)/*:void*/
		{
			//b2Settings.b2Assert(proxy.IsLeaf());
			this.RemoveLeaf$1(proxy);
			this.FreeNode$1(proxy);
		},
		
		/**
		 * Move a proxy with a swept AABB. If the proxy has moved outside of its fattened AABB,
		 * then the proxy is removed from the tree and re-inserted. Otherwise
		 * the function returns immediately.
		 */
		"public function MoveProxy",function MoveProxy(proxy/*:b2DynamicTreeNode*/, aabb/*:b2AABB*/, displacement/*:b2Vec2*/)/*:Boolean*/
		{/*
			assert proxy.IsLeaf();*/
			
			if (proxy.aabb.Contains(aabb))
			{
				return false;
			}
			
			this.RemoveLeaf$1(proxy);
			
			// Extend AABB
			var extendX/*:Number*/ = Box2D.Common.b2Settings.b2_aabbExtension + Box2D.Common.b2Settings.b2_aabbMultiplier * (displacement.x > 0?displacement.x: -displacement.x);
			var extendY/*:Number*/ = Box2D.Common.b2Settings.b2_aabbExtension + Box2D.Common.b2Settings.b2_aabbMultiplier * (displacement.y > 0?displacement.y: -displacement.y);
			proxy.aabb.lowerBound.x = aabb.lowerBound.x - extendX;
			proxy.aabb.lowerBound.y = aabb.lowerBound.y - extendY;
			proxy.aabb.upperBound.x = aabb.upperBound.x + extendX;
			proxy.aabb.upperBound.y = aabb.upperBound.y + extendY;
			
			this.InsertLeaf$1(proxy);
			return true;
		},
		
		/**
		 * Perform some iterations to re-balance the tree.
		 */
		"public function Rebalance",function Rebalance(iterations/*:int*/)/*:void*/
		{
			if (this.m_root$1 == null)
				return;
				
			for (var i/*:int*/ = 0; i < iterations; i++)
			{
				var node/*:b2DynamicTreeNode*/ = this.m_root$1;
				var bit/*:uint*/ = 0;
				while (node.IsLeaf() == false)
				{
					node = (this.m_path$1 >> bit) & 1 ? node.child2 : node.child1;
					bit = (bit + 1) & 31; // 0-31 bits in a uint
				}
				++this.m_path$1;
				
				this.RemoveLeaf$1(node);
				this.InsertLeaf$1(node);
			}
		},
		
		"public function GetFatAABB",function GetFatAABB(proxy/*:b2DynamicTreeNode*/)/*:b2AABB*/
		{
			return proxy.aabb;
		},

		/**
		 * Get user data from a proxy. Returns null if the proxy is invalid.
		 */
		"public function GetUserData",function GetUserData(proxy/*:b2DynamicTreeNode*/)/*:**/
		{
			return proxy.userData;
		},
		
		/**
		 * Query an AABB for overlapping proxies. The callback
		 * is called for each proxy that overlaps the supplied AABB.
		 * The callback should match function signature
		 * <code>fuction callback(proxy:b2DynamicTreeNode):Boolean</code>
		 * and should return false to trigger premature termination.
		 */
		"public function Query",function Query(callback/*:Function*/, aabb/*:b2AABB*/)/*:void*/
		{
			this.QueryStack$1(callback, aabb, new Array/*b2DynamicTreeNode*/());
		},
		
		
		"private static var",{ s_stack/*:Array*//*b2DynamicTreeNode*/ :function(){return( new Array/*b2DynamicTreeNode*/());}},
		/**
		 * A non-allocation version of #Query(), providing you can guarantee that it
		 * won't be called recursive
		 */
		"public function QueryNonRecursive",function QueryNonRecursive(callback/*:Function*/, aabb/*:b2AABB*/)/*:void*/
		{
			this.QueryStack$1(callback, aabb, $$private.s_stack);
		},
		
		"private function QueryStack",function QueryStack(callback/*:Function*/, aabb/*:b2AABB*/, stack/*:Array*//*b2DynamicTreeNode*/)/*:void*/
		{
			if (this.m_root$1 == null)
				return;
				
			var count/*:int*/ = 0;
			stack[count++] = this.m_root$1;
			
			while (count > 0)
			{
				var node/*:b2DynamicTreeNode*/ = stack[--count];
				
				if (node.aabb.TestOverlap(aabb))
				{
					if (node.IsLeaf())
					{
						var proceed/*:Boolean*/ = callback(node);
						if (!proceed)
							return;
					}
					else
					{
						// No stack limit, so no assert
						stack[count++] = node.child1;
						stack[count++] = node.child2;
					}
				}
			}
		},
	
		/**
		 * Ray-cast against the proxies in the tree. This relies on the callback
		 * to perform a exact ray-cast in the case were the proxy contains a shape.
		 * The callback also performs the any collision filtering. This has performance
		 * roughly equal to k * log(n), where k is the number of collisions and n is the
		 * number of proxies in the tree.
		 * @param input the ray-cast input data. The ray extends from p1 to p1 + maxFraction * (p2 - p1).
		 * @param callback a callback class that is called for each proxy that is hit by the ray.
		 * It should be of signature:
		 * <code>function callback(input:b2RayCastInput, proxy:*):void</code>
		 */
		"public function RayCast",function RayCast(callback/*:Function*/, input/*:b2RayCastInput*/)/*:void*/
		{
			if (this.m_root$1 == null)
				return;
				
			var p1/*:b2Vec2*/ = input.p1;
			var p2/*:b2Vec2*/ = input.p2;
			var r/*:b2Vec2*/ = Box2D.Common.Math.b2Math.SubtractVV(p1, p2);
			//b2Settings.b2Assert(r.LengthSquared() > 0.0);
			r.Normalize();
			
			// v is perpendicular to the segment
			var v/*:b2Vec2*/ = Box2D.Common.Math.b2Math.CrossFV(1.0, r);
			var abs_v/*:b2Vec2*/ = Box2D.Common.Math.b2Math.AbsV(v);
			
			var maxFraction/*:Number*/ = input.maxFraction;
			
			// Build a bounding box for the segment
			var segmentAABB/*:b2AABB*/ = new Box2D.Collision.b2AABB();
			var tX/*:Number*/;
			var tY/*:Number*/;
			{
				tX = p1.x + maxFraction * (p2.x - p1.x);
				tY = p1.y + maxFraction * (p2.y - p1.y);
				segmentAABB.lowerBound.x = Math.min(p1.x, tX);
				segmentAABB.lowerBound.y = Math.min(p1.y, tY);
				segmentAABB.upperBound.x = Math.max(p1.x, tX);
				segmentAABB.upperBound.y = Math.max(p1.y, tY);
			}
			
			var stack/*:Array*//*b2DynamicTreeNode*/ = new Array/*b2DynamicTreeNode*/();
			
			var count/*:int*/ = 0;
			stack[count++] = this.m_root$1;
			
			while (count > 0)
			{
				var node/*:b2DynamicTreeNode*/ = stack[--count];
				
				if (node.aabb.TestOverlap(segmentAABB) == false)
				{
					continue;
				}
				
				// Separating axis for segment (Gino, p80)
				// |dot(v, p1 - c)| > dot(|v|,h)
				
				var c/*:b2Vec2*/ = node.aabb.GetCenter();
				var h/*:b2Vec2*/ = node.aabb.GetExtents();
				var separation/*:Number*/ = Math.abs(v.x * (p1.x - c.x) + v.y * (p1.y - c.y))
										- abs_v.x * h.x - abs_v.y * h.y;
				if (separation > 0.0)
					continue;
				
				if (node.IsLeaf())
				{
					var subInput/*:b2RayCastInput*/ = new Box2D.Collision.b2RayCastInput();
					subInput.p1 = input.p1;
					subInput.p2 = input.p2;
					subInput.maxFraction = input.maxFraction;
					
					var value/*:Number*/ = callback(subInput, node);
					
					if (value == 0.0)
					{
						// The client has terminated the ray cast.
						return;
					}
					
					if (value > 0.0)
					{
						maxFraction = value;
						//Update the segment bounding box
						{
							tX = p1.x + maxFraction * (p2.x - p1.x);
							tY = p1.y + maxFraction * (p2.y - p1.y);
							segmentAABB.lowerBound.x = Math.min(p1.x, tX);
							segmentAABB.lowerBound.y = Math.min(p1.y, tY);
							segmentAABB.upperBound.x = Math.max(p1.x, tX);
							segmentAABB.upperBound.y = Math.max(p1.y, tY);
						}
					}
				}
				else
				{
					// No stack limit, so no assert
					stack[count++] = node.child1;
					stack[count++] = node.child2;
				}
			}
		},
		
		
		"private function AllocateNode",function AllocateNode()/*:b2DynamicTreeNode*/
		{
			this.m_nodeCount$1++;
			// Peel a node off the free list
			if (this.m_freeList$1)
			{
				var node/*:b2DynamicTreeNode*/ = this.m_freeList$1;
				this.m_freeList$1 = node.parent;
				node.parent = null;
				node.child1 = null;
				node.child2 = null;
				return node;
			}
			
			// Ignore length pool expansion and relocation found in the C++
			// As we are using heap allocation
			return new Box2D.Collision.b2DynamicTreeNode();
		},
		
		"private function FreeNode",function FreeNode(node/*:b2DynamicTreeNode*/)/*:void*/
		{
			this.m_nodeCount$1--;
			node.parent = this.m_freeList$1;
			this.m_freeList$1 = node;
		},
		
		"private function InsertLeaf",function InsertLeaf(leaf/*:b2DynamicTreeNode*/)/*:void*/
		{
			++this.m_insertionCount$1;
			
			if (this.m_root$1 == null)
			{
				this.m_root$1 = leaf;
				this.m_root$1.parent = null;
				return;
			}
			
			var center/*:b2Vec2*/ = leaf.aabb.GetCenter();
			var sibling/*:b2DynamicTreeNode*/ = this.m_root$1;
			if (sibling.IsLeaf() == false)
			{
				do
				{
					var child1/*:b2DynamicTreeNode*/ = sibling.child1;
					var child2/*:b2DynamicTreeNode*/ = sibling.child2;
					
					//b2Vec2 delta1 = b2Abs(m_nodes[child1].aabb.GetCenter() - center);
					//b2Vec2 delta2 = b2Abs(m_nodes[child2].aabb.GetCenter() - center);
					//float32 norm1 = delta1.x + delta1.y;
					//float32 norm2 = delta2.x + delta2.y;
					
					var norm1/*:Number*/ = Math.abs((child1.aabb.lowerBound.x + child1.aabb.upperBound.x) / 2 - center.x)
									 + Math.abs((child1.aabb.lowerBound.y + child1.aabb.upperBound.y) / 2 - center.y);
					var norm2/*:Number*/ = Math.abs((child2.aabb.lowerBound.x + child2.aabb.upperBound.x) / 2 - center.x)
									 + Math.abs((child2.aabb.lowerBound.y + child2.aabb.upperBound.y) / 2 - center.y);
									 
					if (norm1 < norm2)
					{
						sibling = child1;
					}else {
						sibling = child2;
					}
				}
				while (sibling.IsLeaf() == false);
			}
			
			// Create a parent for the siblings
			var node1/*:b2DynamicTreeNode*/ = sibling.parent;
			var node2/*:b2DynamicTreeNode*/ = this.AllocateNode$1();
			node2.parent = node1;
			node2.userData = null;
			node2.aabb.Combine(leaf.aabb, sibling.aabb);
			if (node1)
			{
				if (sibling.parent.child1 == sibling)
				{
					node1.child1 = node2;
				}
				else
				{
					node1.child2 = node2;
				}
				
				node2.child1 = sibling;
				node2.child2 = leaf;
				sibling.parent = node2;
				leaf.parent = node2;
				do
				{
					if (node1.aabb.Contains(node2.aabb))
						break;
					
					node1.aabb.Combine(node1.child1.aabb, node1.child2.aabb);
					node2 = node1;
					node1 = node1.parent;
				}
				while (node1);
			}
			else
			{
				node2.child1 = sibling;
				node2.child2 = leaf;
				sibling.parent = node2;
				leaf.parent = node2;
				this.m_root$1 = node2;
			}
			
		},
		
		"private function RemoveLeaf",function RemoveLeaf(leaf/*:b2DynamicTreeNode*/)/*:void*/
		{
			if ( leaf == this.m_root$1)
			{
				this.m_root$1 = null;
				return;
			}
			
			var node2/*:b2DynamicTreeNode*/ = leaf.parent;
			var node1/*:b2DynamicTreeNode*/ = node2.parent;
			var sibling/*:b2DynamicTreeNode*/;
			if (node2.child1 == leaf)
			{
				sibling = node2.child2;
			}
			else
			{
				sibling = node2.child1;
			}
			
			if (node1)
			{
				// Destroy node2 and connect node1 to sibling
				if (node1.child1 == node2)
				{
					node1.child1 = sibling;
				}
				else
				{
					node1.child2 = sibling;
				}
				sibling.parent = node1;
				this.FreeNode$1(node2);
				
				// Adjust the ancestor bounds
				while (node1)
				{
					var oldAABB/*:b2AABB*/ = node1.aabb;
					node1.aabb = Box2D.Collision.b2AABB.Combine(node1.child1.aabb, node1.child2.aabb);
					
					if (oldAABB.Contains(node1.aabb))
						break;
						
					node1 = node1.parent;
				}
			}
			else
			{
				this.m_root$1 = sibling;
				sibling.parent = null;
				this.FreeNode$1(node2);
			}
		},
		
		"private function ComputeHeight",function ComputeHeight(node/*:b2DynamicTreeNode*/)/*:int*/
		{
			if (node == null)
				return 0;
			
			return 1 + Box2D.Common.Math.b2Math.Max(
				this.ComputeHeight$1(node.child1),
				this.ComputeHeight$1(node.child2));
		},
		
		"private function ComputeFullHeight",function ComputeFullHeight()/*:int*/
		{
			return this.ComputeHeight$1(this.m_root$1);
		},
		
		"private var",{ m_nodeCount/*:int*/ : 0},
		"private var",{ m_root/*:b2DynamicTreeNode*/:null},
		"private var",{ m_freeList/*:b2DynamicTreeNode*/:null},
		
		/** This is used for incrementally traverse the tree for rebalancing */
		"private var",{ m_path/*:uint*/:0},
		
		"private var",{ m_insertionCount/*:int*/:0},
	];},[],["Box2D.Common.b2Settings","Array","Box2D.Common.Math.b2Math","Box2D.Collision.b2AABB","Math","Box2D.Collision.b2RayCastInput","Box2D.Collision.b2DynamicTreeNode"], "0.8.0", "0.8.1"
	
);