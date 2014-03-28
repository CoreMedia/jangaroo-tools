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
// Input class
//===========================================================
"package General",/*{
	
	
	
	import flash.display.*
	import flash.events.**/
	
	
	"public class Input",1,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.KeyboardEvent,flash.events.MouseEvent,flash.events.Event);},
		
		
		//======================
		// constructor
		//======================
		"public function Input",function Input$(stageMc/*:Sprite*/){
			
			General.Input.m_stageMc = stageMc;
			
			// init ascii array
			General.Input.ascii = new Array(222);
			this.fillAscii$1();
			
			// init key state array
			$$private.keyState = new Array(222);
			$$private.keyArr = new Array();
			for (var i/*:int*/ = 0; i < 222; i++){
				$$private.keyState[i] = new int(0);
				if (General.Input.ascii[i] != undefined){
					$$private.keyArr.push(i);
				}
			}
			
			// buffer
			$$private.bufferSize = 5;
			$$private.keyBuffer = new Array($$private.bufferSize);
			for (var j/*:int*/ = 0; j < $$private.bufferSize; j++){
				$$private.keyBuffer[j] = new Array(0,0);
			}
			
			// add key listeners
			stageMc.stage.addEventListener(flash.events.KeyboardEvent.KEY_DOWN, $$bound(this,"keyPress"), false, 0, true);
			stageMc.stage.addEventListener(flash.events.KeyboardEvent.KEY_UP, $$bound(this,"keyRelease"), false, 0, true);		
			
			// mouse listeners
			stageMc.stage.addEventListener(flash.events.MouseEvent.MOUSE_DOWN, $$bound(this,"mousePress"), false, 0, true);
			stageMc.stage.addEventListener(flash.events.MouseEvent.CLICK, $$bound(this,"mouseRelease"), false, 0, true);
			stageMc.stage.addEventListener(flash.events.MouseEvent.MOUSE_MOVE, $$bound(this,"mouseMove"), false, 0, true);
			stageMc.stage.addEventListener(flash.events.Event.MOUSE_LEAVE, $$bound(this,"mouseLeave"), false, 0, true);
			
			General.Input.mouse.graphics.lineStyle(0.1, 0, 100);
			General.Input.mouse.graphics.moveTo(0,0);
			General.Input.mouse.graphics.lineTo(0,0.1);
			
		},
		
		
		
		//======================
		// update
		//======================
		"static public function update",function update()/*:void*/{
			
			// array of used keys
			/*var kArr:Array = new Array(
				Globals.keyP1Up,
				Globals.keyP1Down,
				Globals.keyP1Left,
				Globals.keyP1Right,
				Globals.keyP1Attack1,
				Globals.keyP1Attack2,
				Globals.keyP1Jump,
				Globals.keyP1Defend,
				Globals.keyResetGame,
				Globals.keyInvertBg,
				Globals.keyChangeBg,
				Globals.keyPauseGame);*/
				
			// update used keys
			for (var i/*:int*/ = 0; i < $$private.keyArr.length; i++){
				if ($$private.keyState[$$private.keyArr[i]] != 0){
					$$private.keyState[$$private.keyArr[i]]++;
				}
			}
			
			// update buffer
			for (var j/*:int*/ = 0; j < $$private.bufferSize; j++){
				$$private.keyBuffer[j][1]++;
			}
			
			// end mouse release
			General.Input.mouseReleased = false;
			General.Input.mousePressed = false;
			General.Input.mouseOver = false;
			
		},
		
		
		
		//======================
		// mousePress listener
		//======================
		"public function mousePress",function mousePress(e/*:MouseEvent*/)/*:void*/{
			General.Input.mousePressed = true;
			General.Input.mouseDown = true;
			General.Input.mouseDragX = 0;
			General.Input.mouseDragY = 0;
		},
		
		
		
		//======================
		// mousePress listener
		//======================
		"public function mouseRelease",function mouseRelease(e/*:MouseEvent*/)/*:void*/{
			General.Input.mouseDown = false;
			General.Input.mouseReleased = true;
		},
		
		
		
		//======================
		// mousePress listener
		//======================
		"public function mouseLeave",function mouseLeave(e/*:Event*/)/*:void*/{
			General.Input.mouseReleased = General.Input.mouseDown;
			General.Input.mouseDown = false;
		},
		
		
		
		//======================
		// mouseMove listener
		//======================
		"public function mouseMove",function mouseMove(e/*:MouseEvent*/)/*:void*/{
			
			// Fix mouse release not being registered from mouse going off stage
			if (General.Input.mouseDown != e.buttonDown){
				General.Input.mouseDown = e.buttonDown;
				General.Input.mouseReleased = !e.buttonDown;
				General.Input.mousePressed = e.buttonDown;
				General.Input.mouseDragX = 0;
				General.Input.mouseDragY = 0;
			}
			
			General.Input.mouseX = e.stageX - General.Input.m_stageMc.x;
			General.Input.mouseY = e.stageY - General.Input.m_stageMc.y;
			// Store offset
			General.Input.mouseOffsetX = General.Input.mouseX - General.Input.mouse.x;
			General.Input.mouseOffsetY = General.Input.mouseY - General.Input.mouse.y;
			// Update drag
			if (General.Input.mouseDown){
				General.Input.mouseDragX += General.Input.mouseOffsetX;
				General.Input.mouseDragY += General.Input.mouseOffsetY;
			}
			General.Input.mouse.x = General.Input.mouseX;
			General.Input.mouse.y = General.Input.mouseY;
		},
		
		
		
		//======================
		// getKeyHold
		//======================
		"static public function getKeyHold",function getKeyHold(k/*:int*/)/*:int*/{
			return Math.max(0, $$private.keyState[k]);
		},
		
		
		//======================
		// isKeyDown
		//======================
		"static public function isKeyDown",function isKeyDown(k/*:int*/)/*:Boolean*/{
			return ($$private.keyState[k] > 0);
		},
		
		
		
		//======================
		//  isKeyPressed
		//======================
		"static public function isKeyPressed",function isKeyPressed(k/*:int*/)/*:Boolean*/{
			General.Input.timeSinceLastKey = 0;
			return ($$private.keyState[k] == 1);
		},
		
		
		
		//======================
		//  isKeyReleased
		//======================
		"static public function isKeyReleased",function isKeyReleased(k/*:int*/)/*:Boolean*/{
			return ($$private.keyState[k] == -1);
		},
		
		
		
		//======================
		// isKeyInBuffer
		//======================
		"static public function isKeyInBuffer",function isKeyInBuffer(k/*:int*/, i/*:int*/, t/*:int*/)/*:Boolean*/{
			return ($$private.keyBuffer[i][0] == k && $$private.keyBuffer[i][1] <= t);
		},
		
		
		
		//======================
		// keyPress function
		//======================
		"public function keyPress",function keyPress(e/*:KeyboardEvent*/)/*:void*/{
			
			//strace ( e.keyCode + " : " + ascii[e.keyCode] );
			
			// set keyState
			$$private.keyState[e.keyCode] = Math.max($$private.keyState[e.keyCode], 1);
			
			// last key (for key config)
			General.Input.lastKey = e.keyCode;
			
		},
		
		//======================
		// keyRelease function
		//======================
		"public function keyRelease",function keyRelease(e/*:KeyboardEvent*/)/*:void*/{
			$$private.keyState[e.keyCode] = -1;
			
			// add to key buffer
			for (var i/*:int*/ = $$private.bufferSize-1; i > 0 ; i--){
				$$private.keyBuffer[i] = $$private.keyBuffer[i - 1];
			}
			$$private.keyBuffer[0] = [e.keyCode, 0];
		},
		
		
		
		//======================
		// get key string
		//======================
		"static public function getKeyString",function getKeyString(k/*:uint*/)/*:String*/{
			return General.Input.ascii[k];
		},
		
		
		//======================
		// set up ascii text
		//======================
		"private function fillAscii",function fillAscii()/*:void*/{
			General.Input.ascii[65] = "A";
			General.Input.ascii[66] = "B";
			General.Input.ascii[67] = "C";
			General.Input.ascii[68] = "D";
			General.Input.ascii[69] = "E";
			General.Input.ascii[70] = "F";
			General.Input.ascii[71] = "G";
			General.Input.ascii[72] = "H";
			General.Input.ascii[73] = "I";
			General.Input.ascii[74] = "J";
			General.Input.ascii[75] = "K";
			General.Input.ascii[76] = "L";
			General.Input.ascii[77] = "M";
			General.Input.ascii[78] = "N";
			General.Input.ascii[79] = "O";
			General.Input.ascii[80] = "P";
			General.Input.ascii[81] = "Q";
			General.Input.ascii[82] = "R";
			General.Input.ascii[83] = "S";
			General.Input.ascii[84] = "T";
			General.Input.ascii[85] = "U";
			General.Input.ascii[86] = "V";
			General.Input.ascii[87] = "W";
			General.Input.ascii[88] = "X";
			General.Input.ascii[89] = "Y";
			General.Input.ascii[90] = "Z";
			General.Input.ascii[48] = "0";
			General.Input.ascii[49] = "1";
			General.Input.ascii[50] = "2";
			General.Input.ascii[51] = "3";
			General.Input.ascii[52] = "4";
			General.Input.ascii[53] = "5";
			General.Input.ascii[54] = "6";
			General.Input.ascii[55] = "7";
			General.Input.ascii[56] = "8";
			General.Input.ascii[57] = "9";
			General.Input.ascii[32] = "Spacebar";
			General.Input.ascii[17] = "Ctrl";
			General.Input.ascii[16] = "Shift";
			General.Input.ascii[192] = "~";
			General.Input.ascii[38] = "up";
			General.Input.ascii[40] = "down";
			General.Input.ascii[37] = "left";
			General.Input.ascii[39] = "right";
			General.Input.ascii[96] = "Numpad 0";
			General.Input.ascii[97] = "Numpad 1";
			General.Input.ascii[98] = "Numpad 2";
			General.Input.ascii[99] = "Numpad 3";
			General.Input.ascii[100] = "Numpad 4";
			General.Input.ascii[101] = "Numpad 5";
			General.Input.ascii[102] = "Numpad 6";
			General.Input.ascii[103] = "Numpad 7";
			General.Input.ascii[104] = "Numpad 8";
			General.Input.ascii[105] = "Numpad 9";
			General.Input.ascii[111] = "Numpad /";
			General.Input.ascii[106] = "Numpad *";
			General.Input.ascii[109] = "Numpad -";
			General.Input.ascii[107] = "Numpad +";
			General.Input.ascii[110] = "Numpad .";
			General.Input.ascii[45] = "Insert";
			General.Input.ascii[46] = "Delete";
			General.Input.ascii[33] = "Page Up";
			General.Input.ascii[34] = "Page Down";
			General.Input.ascii[35] = "End";
			General.Input.ascii[36] = "Home";
			General.Input.ascii[112] = "F1";
			General.Input.ascii[113] = "F2";
			General.Input.ascii[114] = "F3";
			General.Input.ascii[115] = "F4";
			General.Input.ascii[116] = "F5";
			General.Input.ascii[117] = "F6";
			General.Input.ascii[118] = "F7";
			General.Input.ascii[119] = "F8";
			General.Input.ascii[188] = ",";
			General.Input.ascii[190] = ".";
			General.Input.ascii[186] = ";";
			General.Input.ascii[222] = "'";
			General.Input.ascii[219] = "[";
			General.Input.ascii[221] = "]";
			General.Input.ascii[189] = "-";
			General.Input.ascii[187] = "+";
			General.Input.ascii[220] = "\\";
			General.Input.ascii[191] = "/";
			General.Input.ascii[9] = "TAB";
			General.Input.ascii[8] = "Backspace";
			//ascii[27] = "ESC";
		},
		
		//======================
		// member data
		//======================
		// key text array
		"static public var",{ ascii/*:Array*/:null},
		"static private var",{ keyState/*:Array*/:null},
		"static private var",{ keyArr/*:Array*/:null},
		
		"static private var",{ keyBuffer/*:Array*/:null},
		"static private var",{ bufferSize/*:int*/:0},
		
		// last key pressed
		"static public var",{ lastKey/*:int*/ : 0},
		"static public var",{ timeSinceLastKey/*:Number*/ : 0},
		
		// mouse states
		"static public var",{ mouseDown/*:Boolean*/ : false},
		"static public var",{ mouseReleased/*:Boolean*/ : false},
		"static public var",{ mousePressed/*:Boolean*/ : false},
		"static public var",{ mouseOver/*:Boolean*/ : false},
		"static public var",{ mouseX/*:Number*/ : 0},
		"static public var",{ mouseY/*:Number*/ : 0},
		"static public var",{ mouseOffsetX/*:Number*/ : 0},
		"static public var",{ mouseOffsetY/*:Number*/ : 0},
		"static public var",{ mouseDragX/*:Number*/ : 0},
		"static public var",{ mouseDragY/*:Number*/ : 0},
		"static public var",{ mouse/*:Sprite*/ :function(){return( new flash.display.Sprite());}},
		
		// stage
		"static public var",{ m_stageMc/*:Sprite*/:null},
	];},["update","getKeyHold","isKeyDown","isKeyPressed","isKeyReleased","isKeyInBuffer","getKeyString"],["Array","int","flash.events.KeyboardEvent","flash.events.MouseEvent","flash.events.Event","Math","flash.display.Sprite"], "0.8.0", "0.8.1"
	
	
);