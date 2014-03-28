joo.classLoader.prepare(//===========================================================
//=========================================================//
//						-=ANTHEM=-
//	file: .as
//
//	copyright: Matthew Bush 2007
//
//	notes:
//
//=========================================================//
//===========================================================



//===========================================================
// FPS COUNTER CLASS
//===========================================================
"package General",/*{
	
	import flash.display.Sprite
	import flash.text.*
	import flash.utils.getTimer
	import flash.events.*
	import flash.system.System*/
	
	"public class FpsCounter extends flash.display.Sprite",6,function($$private){;return[function(){joo.classLoader.init(flash.system.System);},
		
		//======================
		// constructor
		//======================
		"public function FpsCounter",function FpsCounter$(){this.super$6();
			
			// create text field
			this.textBox$6 = new flash.text.TextField();
			this.textBox$6.text = "...";
			this.textBox$6.textColor = 0xaa1144;
			this.textBox$6.selectable = false;
			
			this.textBox2$6 = new flash.text.TextField();
			this.textBox2$6.text = "...";
			this.textBox2$6.width = 150;
			this.textBox2$6.textColor = 0xaa1144;
			this.textBox2$6.selectable = false;
			this.textBox2$6.y = 15;
			
			this.textBox3$6 = new flash.text.TextField();
			this.textBox3$6.text = "...";
			this.textBox3$6.textColor = 0xaa1144;
			this.textBox3$6.selectable = false;
			this.textBox3$6.y = 30;
			
			// set initial lastTime
			this.oldT$6 = flash.utils.getTimer();
			
			this.addChild(this.textBox$6);
			this.addChild(this.textBox2$6);
			this.addChild(this.textBox3$6);
		},
		
		//======================
		// update function
		//======================
		"public function update",function update()/*:void*/{
			var newT/*:uint*/ = flash.utils.getTimer();
			var f1/*:uint*/ = newT-this.oldT$6;
			this.mfpsCount$6 += f1;
			if (this.avgCount$6 < 1){
				this.textBox$6.text = String(Math.round(1000/(this.mfpsCount$6/30))+" fps average");
				this.avgCount$6 = 30;
				this.mfpsCount$6 = 0;
			}
			this.avgCount$6--;
			this.oldT$6 = flash.utils.getTimer();
			
			this.textBox3$6.text = Math.round(flash.system.System.totalMemory/(1024*1024)) + " MB used";
			
		},
		
		
		"public function updatePhys",function updatePhys(oldT2/*:uint*/)/*:void*/{
			var newT/*:uint*/ = flash.utils.getTimer();
			var f1/*:uint*/ = newT-oldT2;
			this.mfpsCount2$6 += f1;
			if (this.avgCount2$6 < 1){
				this.textBox2$6.text = String("Physics step: "+Math.round(this.mfpsCount2$6/30)+" ms (" +Math.round(1000/(this.mfpsCount2$6/30))+" fps)");
				this.avgCount2$6 = 30;
				this.mfpsCount2$6 = 0;
			}
			this.avgCount2$6--;
		},
		
		
		//======================
		// updateend function
		//======================
		"public function updateEnd",function updateEnd()/*:void*/{
			// wrong
			/*var newT:uint = getTimer();
			var f1:uint = newT-oldT;
			mfpsCount2 += f1;
			if (avgCount2 < 1){
				textBox2.text = String(Math.round(1000/(mfpsCount2/30))+" fps uncapped");
				avgCount2 = 30;
				mfpsCount2 = 0;
			}
			avgCount2--;*/
			
		},
		
		
		//======================
		// private variables
		//======================
		"private var",{ textBox/*:TextField*/:null},
		"private var",{ textBox2/*:TextField*/:null},
		"private var",{ textBox3/*:TextField*/:null},
		"private var",{ mfpsCount/*:int*/ : 0},
		"private var",{ mfpsCount2/*:int*/ : 0},
		"private var",{ avgCount/*:int*/ : 30},
		"private var",{ avgCount2/*:int*/ : 30},
		"private var",{ oldT/*:uint*/:0},
	];},[],["flash.display.Sprite","flash.text.TextField","String","Math","flash.system.System"], "0.8.0", "0.8.1"
);