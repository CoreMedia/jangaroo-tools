joo.classLoader.prepare("package com.chipacabra.Jumper",/* 
{
	import org.flixel.FlxSprite*/
	
	/**
	 * ...
	 * @author David Bell
	 */
	"public class EnemyTemplate extends org.flixel.FlxSprite",5,function($$private){;return[ 
	
		
		"protected var",{ _player/*:Player*/:null},
		"protected var",{ _startx/*:Number*/:NaN},
		"protected var",{ _starty/*:Number*/:NaN},
		
		"public function EnemyTemplate",function EnemyTemplate$(X/*:Number*/, Y/*:Number*/, ThePlayer/*:Player*/) 
		{
			this.super$5(X, Y);
			this._startx = X;
			this._starty = Y;
			this._player = ThePlayer;
		},
		
	];},[],["org.flixel.FlxSprite"], "0.8.0", "0.8.2-SNAPSHOT"

);