joo.classLoader.prepare("package Box2D.Collision",/* 
{
import Box2D.Common.Math.**/
	
/**
 * The broad-phase is used for computing pairs and performing volume queries and ray casts.
 * This broad-phase does not persist pairs. Instead, this reports potentially new pairs.
 * It is up to the client to consume the new pairs and to track subsequent overlap.
 */
"public class b2DynamicTreeBroadPhase implements Box2D.Collision.IBroadPhase",1,function($$private){;return[

	/**
	 * Create a proxy with an initial AABB. Pairs are not reported until
	 * UpdatePairs is called.
	 */
	"public function CreateProxy",function CreateProxy(aabb/*:b2AABB*/, userData/*:**/)/*:**/
	{
		var proxy/*:b2DynamicTreeNode*/ = this.m_tree$1.CreateProxy(aabb, userData);
		++this.m_proxyCount$1;
		this.BufferMove$1(proxy);
		return proxy;
	},
	
	/**
	 * Destroy a proxy. It is up to the client to remove any pairs.
	 */
	"public function DestroyProxy",function DestroyProxy(proxy/*:**/)/*:void*/
	{
		this.UnBufferMove$1(proxy);
		--this.m_proxyCount$1;
		this.m_tree$1.DestroyProxy(proxy);
	},
	
	/**
	 * Call MoveProxy as many times as you like, then when you are done
	 * call UpdatePairs to finalized the proxy pairs (for your time step).
	 */
	"public function MoveProxy",function MoveProxy(proxy/*:**/, aabb/*:b2AABB*/, displacement/*:b2Vec2*/)/*:void*/
	{
		var buffer/*:Boolean*/ = this.m_tree$1.MoveProxy(proxy, aabb, displacement);
		if (buffer)
		{
			this.BufferMove$1(proxy);
		}
	},
	
	"public function TestOverlap",function TestOverlap(proxyA/*:**/, proxyB/*:**/)/*:Boolean*/
	{
		var aabbA/*:b2AABB*/ = this.m_tree$1.GetFatAABB(proxyA);
		var aabbB/*:b2AABB*/ = this.m_tree$1.GetFatAABB(proxyB);
		return aabbA.TestOverlap(aabbB);
	},
	
	/**
	 * Get user data from a proxy. Returns null if the proxy is invalid.
	 */
	"public function GetUserData",function GetUserData(proxy/*:**/)/*:**/
	{
		return this.m_tree$1.GetUserData(proxy);
	},
	
	/**
	 * Get the AABB for a proxy.
	 */
	"public function GetFatAABB",function GetFatAABB(proxy/*:**/)/*:b2AABB*/
	{
		return this.m_tree$1.GetFatAABB(proxy);
	},
	
	/**
	 * Get the number of proxies.
	 */
	"public function GetProxyCount",function GetProxyCount()/*:int*/
	{
		return this.m_proxyCount$1;
	},
	
	/**
	 * Update the pairs. This results in pair callbacks. This can only add pairs.
	 */
	"public function UpdatePairs",function UpdatePairs(callback/*:Function*/)/*:void*/
	{var this$=this;
		this.m_pairCount$1 = 0;
		// Perform tree queries for all moving queries
		for/* each*/(var $1 in this.m_moveBuffer$1)
		{var queryProxy/*:b2DynamicTreeNode*/ = this.m_moveBuffer$1[$1];
			function QueryCallback(proxy/*:b2DynamicTreeNode*/)/*:Boolean*/
			{
				// A proxy cannot form a pair with itself.
				if (proxy == queryProxy)
					return true;
					
				// Grow the pair buffer as needed
				if (this$.m_pairCount$1 == this$.m_pairBuffer$1.length)
				{
					this$.m_pairBuffer$1[this$.m_pairCount$1] = new Box2D.Collision.b2DynamicTreePair();
				}
				
				var pair/*:b2DynamicTreePair*/ = this$.m_pairBuffer$1[this$.m_pairCount$1];
				pair.proxyA = proxy < queryProxy?proxy:queryProxy;
				pair.proxyB = proxy >= queryProxy?proxy:queryProxy;
				++this$.m_pairCount$1;
				
				return true;
			}
			// We have to query the tree with the fat AABB so that
			// we don't fail to create a pair that may touch later.
			var fatAABB/*:b2AABB*/ = this.m_tree$1.GetFatAABB(queryProxy);
			this.m_tree$1.QueryNonRecursive(QueryCallback, fatAABB);
		}
		
		// Reset move buffer
		this.m_moveBuffer$1.length = 0;
		
		// Sort the pair buffer to expose duplicates.
		// TODO: Something more sensible
		//m_pairBuffer.sort(ComparePairs);
		
		// Send the pair buffer
		for (var i/*:int*/ = 0; i < this.m_pairCount$1; )
		{
			var primaryPair/*:b2DynamicTreePair*/ = this.m_pairBuffer$1[i];
			var userDataA/*:**/ = this.m_tree$1.GetUserData(primaryPair.proxyA);
			var userDataB/*:**/ = this.m_tree$1.GetUserData(primaryPair.proxyB);
			callback(userDataA, userDataB);
			++i;
			
			// Skip any duplicate pairs
			while (i < this.m_pairCount$1)
			{
				var pair/*:b2DynamicTreePair*/ = this.m_pairBuffer$1[i];
				if (pair.proxyA != primaryPair.proxyA || pair.proxyB != primaryPair.proxyB)
				{
					break;
				}
				++i;
			}
		}
		
		// Try to keep the tree balanced
		this.m_tree$1.Rebalance(4);
	},
	
	/**
	 * @inheritDoc
	 */
	"public function Query",function Query(callback/*:Function*/, aabb/*:b2AABB*/)/*:void*/
	{
		this.m_tree$1.Query(callback, aabb);
	},
	
	/**
	 * @inheritDoc
	 */
	"public function RayCast",function RayCast(callback/*:Function*/, input/*:b2RayCastInput*/)/*:void*/
	{
		this.m_tree$1.RayCast(callback, input);
	},
	
	
	"public function Validate",function Validate()/*:void*/
	{
		//TODO_BORIS
	},
	
	"public function Rebalance",function Rebalance(iterations/*:int*/)/*:void*/
	{
		this.m_tree$1.Rebalance(iterations);
	},
	
	
	// Private ///////////////
	
	"private function BufferMove",function BufferMove(proxy/*:b2DynamicTreeNode*/)/*:void*/
	{
		this.m_moveBuffer$1[this.m_moveBuffer$1.length] = proxy;
	},
	
	"private function UnBufferMove",function UnBufferMove(proxy/*:b2DynamicTreeNode*/)/*:void*/
	{
		var i/*:int*/ = this.m_moveBuffer$1.indexOf(proxy);
		this.m_moveBuffer$1.splice(i, 1);
	},
	
	"private function ComparePairs",function ComparePairs(pair1/*:b2DynamicTreePair*/, pair2/*:b2DynamicTreePair*/)/*:int*/
	{
		//TODO_BORIS:
		// We cannot consistently sort objects easily in AS3
		// The caller of this needs replacing with a different method.
		return 0;
	},
	"private var",{ m_tree/*:b2DynamicTree*/ :function(){return( new Box2D.Collision.b2DynamicTree());}},
	"private var",{ m_proxyCount/*:int*/:0},
	"private var",{ m_moveBuffer/*:Array*//*b2DynamicTreeNode*/ :function(){return( new Array/*b2DynamicTreeNode*/());}},
	
	"private var",{ m_pairBuffer/*:Array*//*b2DynamicTreePair*/ :function(){return( new Array/*b2DynamicTreePair*/());}},
	"private var",{ m_pairCount/*:int*/ : 0},
"public function b2DynamicTreeBroadPhase",function b2DynamicTreeBroadPhase$(){this.m_tree$1=this.m_tree$1();this.m_moveBuffer$1=this.m_moveBuffer$1();this.m_pairBuffer$1=this.m_pairBuffer$1();}];},[],["Box2D.Collision.IBroadPhase","Box2D.Collision.b2DynamicTreePair","Box2D.Collision.b2DynamicTree","Array"], "0.8.0", "0.8.1"
	
);