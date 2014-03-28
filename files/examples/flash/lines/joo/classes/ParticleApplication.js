joo.classLoader.prepare("package",/*
{
	import flash.display.Bitmap
	import flash.display.BitmapData
	import flash.display.GradientType
	import flash.display.PixelSnapping
	import flash.display.Shape
	import flash.display.Sprite
	import flash.events.Event
	import flash.filters.BlurFilter
	import flash.geom.ColorTransform
	import flash.geom.Matrix
	import flash.geom.Point
	import flash.media.SoundChannel
	import flash.media.SoundTransform*/

	/**
	 * @author Andre Michelle, http://lab.andre-michelle.com/lines
	 * Adapted for Jangaroo by Frank Wienberg (FWI).
	 */
	"public class ParticleApplication extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.display.PixelSnapping,Math,flash.display.GradientType,flash.events.Event);},
	
		"static public const",{ WIDTH/*: int*/ : 384},
		"static public const",{ HEIGHT/*: int*/ : 384},
		
		"static private const",{ PARTICLE_NUM/*: int*/ : 100},
		
		"private var",{ bitmap/*: Bitmap*/:null},
		
		"private var",{ particles/*: Array*/:null},
		
		"private var",{ forceXPhase/*: Number*/:NaN},
		"private var",{ forceYPhase/*: Number*/:NaN},
		
		"public function ParticleApplication",function ParticleApplication$()
		{this.super$6();
			this.init$6();
		},
		
		"private function init",function init()/*: void*/
		{
			var m/*: Matrix*/ = new flash.geom.Matrix();
			m.createGradientBox( ParticleApplication.WIDTH, ParticleApplication.HEIGHT, Math.PI/2 );
			
			this.graphics.beginGradientFill( flash.display.GradientType.LINEAR, [ 0x212121, 0x404040, 0x0 ], [ 1, 1, 1 ], [ 0, 0x84, 0xff ], m );
			this.graphics.drawRect( 0, 0, ParticleApplication.WIDTH, ParticleApplication.HEIGHT );
			this.graphics.endFill();
			
			this.forceXPhase$6 = Math.random() * Math.PI;
			this.forceYPhase$6 = Math.random() * Math.PI;
			
			this.particles$6 = new Array();
			
			var particle/*: Particle*/;
			
			var a/*: Number*/;
			var r/*: Number*/;
			
			for( var i/*: int*/ = 0 ; i < $$private.PARTICLE_NUM ; i++ )
			{
				a = Math.PI * 2 / $$private.PARTICLE_NUM * i;
				r = ( 1 + i / $$private.PARTICLE_NUM * 4 ) * 1;
				
				particle = new Particle( Math.cos( a ) * 32, Math.sin( a ) * 32 );
				particle.vx = Math.sin( -a ) * r;
				particle.vy = -Math.cos( a ) * r;
				this.particles$6.push( particle );
			}
			
			this.bitmap$6 = new flash.display.Bitmap( new flash.display.BitmapData ( ParticleApplication.WIDTH, ParticleApplication.HEIGHT, true, 0 ), flash.display.PixelSnapping.AUTO, false );
			this.addChild( this.bitmap$6 );
			
			this.addEventListener( flash.events.Event.ENTER_FRAME, $$bound(this,"onEnterFrame$6") );
			
			this.addChild( new FPS() );
		},
		
		"private function onEnterFrame",function onEnterFrame( event/*: Event*/ )/*: void*/
		{
			this.render$6();
		},
		
		"private function render",function render()/*: void*/
		{
			var bitmapData/*: BitmapData*/ = this.bitmap$6.bitmapData;
			
			bitmapData.colorTransform( bitmapData.rect, new flash.geom.ColorTransform( 1, 1, 1, 1, 0, 0, 0, -1 ) );
			
			var p0/*: Particle*/;
			var p1/*: Particle*/;
			
			var dx/*: Number*/;
			var dy/*: Number*/;
			var dd/*: Number*/;
			
			var shape/*: Shape*/ = new flash.display.Shape();
			
			shape.graphics.clear();
			shape.graphics.lineStyle( 0, 0xffffff, 1 );
			
			this.forceXPhase$6 += .0025261;
			this.forceYPhase$6 += .000621;
			
			var forceX/*: Number*/ = 1000 + Math.sin( this.forceXPhase$6 ) * 500;
			var forceY/*: Number*/ = 1000 + Math.sin( this.forceYPhase$6 ) * 500;
			
			for/* each*/(var $1 in this.particles$6 )
			{ p0= this.particles$6[$1];
				shape.graphics.moveTo( p0.sx + (ParticleApplication.WIDTH >> 1), p0.sy + (ParticleApplication.HEIGHT >> 1));
//				shape.graphics.moveTo( p0.sx, p0.sy );
				
				p0.vx -= p0.sx / forceX;
				p0.vy -= p0.sy / forceY;
				
				p0.sx += p0.vx;
				p0.sy += p0.vy;
				
				shape.graphics.lineTo( p0.sx + (ParticleApplication.WIDTH >> 1), p0.sy + (ParticleApplication.HEIGHT >> 1));
//				shape.graphics.lineTo( p0.sx, p0.sy );
			}
			
			// FWI: transform on drawing, not on blitting into the bitmap:
//			bitmapData.draw( shape, new Matrix( 1, 0, 0, 1, WIDTH >> 1, HEIGHT >> 1 ) );
			bitmapData.draw( shape );
		},
	];},[],["flash.display.Sprite","flash.geom.Matrix","Math","flash.display.GradientType","Array","Particle","flash.display.Bitmap","flash.display.BitmapData","flash.display.PixelSnapping","flash.events.Event","FPS","flash.geom.ColorTransform","flash.display.Shape"], "0.8.0", "0.8.1"
);