joo.classLoader.prepare("package org.flixel.data",/*
{
	import org.flixel.*
	import flash.ui.Mouse*/

	/**
	 * This is a little built-in support visor that developers can optionally display.
	 * It has built in support for syndicating your game to StumbleUpon, Digg,
	 * Reddit, Del.icio.us, and Twitter.  It also has a PayPal donate button.
	 * This panel is automatically created by <code>FlxGame</code> and you
	 * can toggle the visibility via <code>FlxG</code>.
	 */
	"public class FlxPanel extends org.flixel.FlxObject",4,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"org/flixel/data/donate.png"}}, "private var",{ ImgDonate/*:Class*/:null},
		{Embed:{source:"org/flixel/data/stumble.png"}}, "private var",{ ImgStumble/*:Class*/:null},
		{Embed:{source:"org/flixel/data/digg.png"}}, "private var",{ ImgDigg/*:Class*/:null},
		{Embed:{source:"org/flixel/data/reddit.png"}}, "private var",{ ImgReddit/*:Class*/:null},
		{Embed:{source:"org/flixel/data/delicious.png"}}, "private var",{ ImgDelicious/*:Class*/:null},
		{Embed:{source:"org/flixel/data/twitter.png"}}, "private var",{ ImgTwitter/*:Class*/:null},
		{Embed:{source:"org/flixel/data/close.png"}}, "private var",{ ImgClose/*:Class*/:null},

		/**
		 * @private
		 */
		"protected var",{ _topBar/*:FlxSprite*/:null},
		/**
		 * @private
		 */
		"protected var",{ _mainBar/*:FlxSprite*/:null},
		/**
		 * @private
		 */
		"protected var",{ _bottomBar/*:FlxSprite*/:null},
		/**
		 * @private
		 */
		"protected var",{ _donate/*:FlxButton*/:null},
		/**
		 * @private
		 */
		"protected var",{ _stumble/*:FlxButton*/:null},
		/**
		 * @private
		 */
		"protected var",{ _digg/*:FlxButton*/:null},
		/**
		 * @private
		 */
		"protected var",{ _reddit/*:FlxButton*/:null},
		/**
		 * @private
		 */
		"protected var",{ _delicious/*:FlxButton*/:null},
		/**
		 * @private
		 */
		"protected var",{ _twitter/*:FlxButton*/:null},
		/**
		 * @private
		 */
		"protected var",{ _close/*:FlxButton*/:null},
		/**
		 * @private
		 */
		"protected var",{ _caption/*:FlxText*/:null},
		/**
		 * @private
		 */
		"protected var",{ _payPalID/*:String*/:null},
		/**
		 * @private
		 */
		"protected var",{ _payPalAmount/*:Number*/:NaN},
		/**
		 * @private
		 */
		"protected var",{ _gameTitle/*:String*/:null},
		/**
		 * @private
		 */
		"protected var",{ _gameURL/*:String*/:null},
		
		/**
		 * @private
		 */
		"protected var",{ _initialized/*:Boolean*/:false},
		/**
		 * @private
		 */
		"protected var",{ _closed/*:Boolean*/:false},
		
		/**
		 * @private
		 */
		"protected var",{ _ty/*:Number*/:NaN},
		/**
		 * @private
		 */
		"protected var",{ _s/*:Number*/:NaN},
		
		/**
		 * Constructor.
		 */
		"public function FlxPanel",function FlxPanel$()
		{
			this.super$4();
			this.y = -21;
			this._ty = this.y;
			this._closed = false;
			this._initialized = false;
			this._topBar = new org.flixel.FlxSprite();
			this._topBar.createGraphic(org.flixel.FlxG.width,1,0x7fffffff);
			this._topBar.scrollFactor.x = 0;
			this._topBar.scrollFactor.y = 0;
			this._mainBar = new org.flixel.FlxSprite();
			this._mainBar.createGraphic(org.flixel.FlxG.width,19,0x7f000000);
			this._mainBar.scrollFactor.x = 0;
			this._mainBar.scrollFactor.y = 0;
			this._bottomBar = new org.flixel.FlxSprite();
			this._bottomBar.createGraphic(org.flixel.FlxG.width,1,0x7fffffff);
			this._bottomBar.scrollFactor.x = 0;
			this._bottomBar.scrollFactor.y = 0;
			this._donate = new org.flixel.FlxButton(3,0,$$bound(this,"onDonate"));
			this._donate.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgDonate$4));
			this._donate.scrollFactor.x = 0;
			this._donate.scrollFactor.y = 0;
			this._stumble = new org.flixel.FlxButton(org.flixel.FlxG.width/2-6-13-6-13-6,0,$$bound(this,"onStumble"));
			this._stumble.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgStumble$4));
			this._stumble.scrollFactor.x = 0;
			this._stumble.scrollFactor.y = 0;
			this._digg = new org.flixel.FlxButton(org.flixel.FlxG.width/2-6-13-6,0,$$bound(this,"onDigg"));
			this._digg.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgDigg$4));
			this._digg.scrollFactor.x = 0;
			this._digg.scrollFactor.y = 0;
			this._reddit = new org.flixel.FlxButton(org.flixel.FlxG.width/2-6,0,$$bound(this,"onReddit"));
			this._reddit.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgReddit$4));
			this._reddit.scrollFactor.x = 0;
			this._reddit.scrollFactor.y = 0;
			this._delicious = new org.flixel.FlxButton(org.flixel.FlxG.width/2+7+6,0,$$bound(this,"onDelicious"));
			this._delicious.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgDelicious$4));
			this._delicious.scrollFactor.x = 0;
			this._delicious.scrollFactor.y = 0;
			this._twitter = new org.flixel.FlxButton(org.flixel.FlxG.width/2+7+6+12+6,0,$$bound(this,"onTwitter"));
			this._twitter.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgTwitter$4));
			this._twitter.scrollFactor.x = 0;
			this._twitter.scrollFactor.y = 0;
			this._caption = new org.flixel.FlxText(org.flixel.FlxG.width/2,0,org.flixel.FlxG.width/2-19,"");
			this._caption.alignment = "right";
			this._caption.scrollFactor.x = 0;
			this._caption.scrollFactor.y = 0;
			this._close = new org.flixel.FlxButton(org.flixel.FlxG.width-16,0,$$bound(this,"onClose"));
			this._close.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgClose$4));
			this._close.scrollFactor.x = 0;
			this._close.scrollFactor.y = 0;
			this.hide();
			this.visible = false;
			this._s = 50;
		},
		
		/**
		 * Set up the support panel with donation and aggregation info.
		 * Like <code>show()</code> and <code>hide()</code> this function is usually
		 * called through <code>FlxGame</code> or <code>FlxG</code>, not directly.
		 * 
		 * @param	PayPalID		Your paypal username, usually your email address (leave it blank to disable donations).
		 * @param	PayPalAmount	The default amount of the donation.
		 * @param	GameTitle		The text that you would like to appear in the aggregation services (usually just the name of your game).
		 * @param	GameURL			The URL you would like people to use when trying to find your game.
		 */
		"public function setup",function setup(PayPalID/*:String*/,PayPalAmount/*:Number*/,GameTitle/*:String*/,GameURL/*:String*/,Caption/*:String*/)/*:void*/
		{
			this._payPalID = PayPalID;
			if(this._payPalID.length <= 0) this._donate.visible = false;
			this._payPalAmount = PayPalAmount;
			this._gameTitle = GameTitle;
			this._gameURL = GameURL;
			this._caption.text = Caption;
			this._initialized = true;
		},
		
		/**
		 * Updates and animates the panel.
		 */
		"override public function update",function update()/*:void*/
		{
			if(!this._initialized) return;
			if(this._ty != this.y)
			{
				if(this.y < this._ty)
				{
					this.y += org.flixel.FlxG.elapsed*this._s;
					if(this.y > this._ty) this.y = this._ty;
				}
				else
				{
					this.y -= org.flixel.FlxG.elapsed*this._s;
					if(this.y < this._ty) this.y = this._ty;
				}
				
				this._topBar.y = this.y;
				this._mainBar.y = this.y+1;
				this._bottomBar.y = this.y+20;
				this._donate.reset(this._donate.x,this.y+4);
				this._stumble.reset(this._stumble.x,this.y+4);
				this._digg.reset(this._digg.x,this.y+4);
				this._reddit.reset(this._reddit.x,this.y+4);
				this._delicious.reset(this._delicious.x,this.y+5);
				this._twitter.reset(this._twitter.x,this.y+4);
				this._caption.reset(this._caption.x,this.y+4);
				this._close.reset(this._close.x,this.y+4);
			}
			if((this.y <= -21) || (this.y >= org.flixel.FlxG.height))
				this.visible = false;
			else
				this.visible = true;
			
			if(this.visible)
			{
				if(this._donate.active) this._donate.update();
				if(this._stumble.active) this._stumble.update();
				if(this._digg.active) this._digg.update();
				if(this._reddit.active) this._reddit.update();
				if(this._delicious.active) this._delicious.update();
				if(this._twitter.active) this._twitter.update();
				if(this._caption.active) this._caption.update();
				if(this._close.active) this._close.update();
			}
		},
		
		/**
		 * Actually draws the bar to the screen.
		 */
		"override public function render",function render()/*:void*/
		{
			if(!this._initialized) return;
			if(this._topBar.visible) this._topBar.render();
			if(this._mainBar.visible) this._mainBar.render();
			if(this._bottomBar.visible) this._bottomBar.render();
			if(this._donate.visible) this._donate.render();
			if(this._stumble.visible) this._stumble.render();
			if(this._digg.visible) this._digg.render();
			if(this._reddit.visible) this._reddit.render();
			if(this._delicious.visible) this._delicious.render();
			if(this._twitter.visible) this._twitter.render();
			if(this._caption.visible) this._caption.render();
			if(this._close.visible) this._close.render();
		},
		
		/**
		 * Show the support panel.
		 * 
		 * @param	Top		Whether the visor should appear at the top or bottom of the screen.
		 */
		"public function show",function show(Top/*:Boolean=true*/)/*:void*/
		{switch(arguments.length){case 0:Top=true;}
			if(this._closed) return;
			if(!this._initialized)
			{
				org.flixel.FlxG.log("SUPPORT PANEL ERROR: Uninitialized.\nYou forgot to call FlxGame.setupSupportPanel()\nfrom your game constructor.");
				return;
			}
			
			if(Top)
			{
				this.y = -21;
				this._ty = -1;
			}
			else
			{
				this.y = org.flixel.FlxG.height;
				this._ty = org.flixel.FlxG.height-20;
			}
			this._donate.reset(this._donate.x,this.y+4);
			this._stumble.reset(this._stumble.x,this.y+4);
			this._digg.reset(this._digg.x,this.y+4);
			this._reddit.reset(this._reddit.x,this.y+4);
			this._delicious.reset(this._delicious.x,this.y+5);
			this._twitter.reset(this._twitter.x,this.y+4);
			this._caption.reset(this._caption.x,this.y+4);
			this._close.reset(this._close.x,this.y+4);
			if(!org.flixel.FlxG.mouse.cursor.visible)
				flash.ui.Mouse.show();
			this.visible = true;
		},
		
		/**
		 * Hide the support panel.
		 */
		"public function hide",function hide()/*:void*/
		{
			if(this.y < 0) this._ty = -21;
			else this._ty = org.flixel.FlxG.height;
			flash.ui.Mouse.hide();
		},
		
		/**
		 * Called when the player presses the Donate button.
		 */
		"public function onDonate",function onDonate()/*:void*/
		{
			org.flixel.FlxU.openURL("https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business="+encodeURIComponent(this._payPalID)+"&item_name="+encodeURIComponent(this._gameTitle+" Contribution ("+this._gameURL)+")&currency_code=USD&amount="+this._payPalAmount);
		},
		
		/**
		 * Called when the player presses the StumbleUpon button.
		 */
		"public function onStumble",function onStumble()/*:void*/
		{
			org.flixel.FlxU.openURL("http://www.stumbleupon.com/submit?url="+encodeURIComponent(this._gameURL));
		},
		
		/**
		 * Called when the player presses the Digg button.
		 */
		"public function onDigg",function onDigg()/*:void*/
		{
			org.flixel.FlxU.openURL("http://digg.com/submit?url="+encodeURIComponent(this._gameURL)+"&title="+encodeURIComponent(this._gameTitle));
		},
		
		/**
		 * Called when the player presses the Reddit button.
		 */
		"public function onReddit",function onReddit()/*:void*/
		{
			org.flixel.FlxU.openURL("http://www.reddit.com/submit?url="+encodeURIComponent(this._gameURL));
		},
		
		/**
		 * Called when the player presses the del.icio.us button.
		 */
		"public function onDelicious",function onDelicious()/*:void*/
		{
			org.flixel.FlxU.openURL("http://delicious.com/save?v=5&amp;noui&amp;jump=close&amp;url="+encodeURIComponent(this._gameURL)+"&amp;title="+encodeURIComponent(this._gameTitle));
		},
		
		/**
		 * Called when the player presses the Twitter button.
		 */
		"public function onTwitter",function onTwitter()/*:void*/
		{
			org.flixel.FlxU.openURL("http://twitter.com/home?status=Playing"+encodeURIComponent(" "+this._gameTitle+" - "+this._gameURL));
		},
		
		/**
		 * Called when the player presses the Close button.
		 */
		"public function onClose",function onClose()/*:void*/
		{
			this._closed = true;
			this.hide();
		},
	];},[],["org.flixel.FlxObject","resource:org/flixel/data/donate.png","resource:org/flixel/data/stumble.png","resource:org/flixel/data/digg.png","resource:org/flixel/data/reddit.png","resource:org/flixel/data/delicious.png","resource:org/flixel/data/twitter.png","resource:org/flixel/data/close.png","org.flixel.FlxSprite","org.flixel.FlxG","org.flixel.FlxButton","org.flixel.FlxText","flash.ui.Mouse","org.flixel.FlxU"], "0.8.0", "0.8.2-SNAPSHOT"
);