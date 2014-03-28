joo.classLoader.prepare("package Box2D.Dynamics.Controllers",/* 
{
	import Box2D.Dynamics.b2Body*/

	/** @private */
	"internal class b2ManualBodyIterator implements Box2D.Dynamics.Controllers.IBodyIterator",1,function($$private){;return[
	
		"public var",{ position/*:int*/ : 0},
		"public var",{ bodyList/*:Array*/:null}/*b2Body*/,
		
		"public function HasNext",function HasNext()/*:Boolean*/
		{
			return this.position < this.bodyList.length;
		},
			
		"public function Next",function Next()/*:b2Body*/
		{
			return this.bodyList[this.position++];
		},
	];},[],["Box2D.Dynamics.Controllers.IBodyIterator"], "0.8.0", "0.8.1"
	
);