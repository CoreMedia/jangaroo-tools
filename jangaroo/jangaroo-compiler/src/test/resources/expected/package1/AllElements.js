joo.classLoader.prepare("package package1",/* {
import ext.Panel;
import exmlparser.config.allElements;
import ext.config.button;
import ext.config.panel;
import ext.config.someMixin;
import ext.config.menuitem;
import ext.config.aplugin;*/

"public class AllElements extends ext.Panel",2,function($$private){var $3=ext;return[function(){joo.classLoader.init(exmlparser.config.allElements);}, /*
    /* add an extra import statement to the class * /
    import ext.MessageBox;*/

    /**
     * This is my <b>constant</b>
     */
    "public static const",{ SOME_CONSTANT/*:int*/ :function(){return( exmlparser.config.allElements.SOME_CONSTANT);}},
    /**
     * This is another <b>constant</b>
     */
    "public static const",{ ANOTHER_CONSTANT/*:String*/ :function(){return( exmlparser.config.allElements.ANOTHER_CONSTANT);}},
    "public static const",{ CODE_CONSTANT/*:int*/ :function(){return( exmlparser.config.allElements.CODE_CONSTANT);}},
    
    "var",{ mVar/*:String*/ :function(){return( this.config.myProperty + '_suffix');}},
    "var",{ myVar2/*:Object*/ :function(){return( {
      prop: this.config.myProperty
    });}},
    
  "public function AllElements",function AllElements($_1/*:Object = null*/) {if(arguments.length<=0)$_1=null;
    
    this.$bindings$2 = [];
    var $_2/*:Object*/ = {};
    this.config = new exmlparser.config.allElements();
    this.myVar3 = new ext.config.button();
    this.myVar3.text = "Foo";
    var $_3/*:ext.config.panel*/ = new ext.config.panel();
    $_3.layout = this.config.myLayout;
    $_3.title = "I am a panel";
    var $_4/*:Object*/ = {};
    var $_6/*:ext.config.button*/ = new ext.config.button();
    $_6.text = "click me!";
    $_4.someList = [$_6];
    $_3.mixins = [new ext.config.someMixin($_4)];
    $_3.defaults = "";
    var $_7/*:Object*/ = {};
    $_7.anchor = "test";
    $_7.bla = "blub";
    $_7.border = "solid";
    $_3.layoutConfig = $_7;
    var $_8/*:ext.config.button*/ = new ext.config.button();
    $_8.text = "Save";
    $_8.handler = function package1$AllElements$52_19()/*:void*/ {
          $3.MessageBox.alert('gotcha!');
        };
    var $_9/*:Object*/ = {};
    var $_10/*:Object*/ = {};
    $_3.items = [$_8, $_9, $_10];
    var $_11/*:ext.config.menuitem*/ = new ext.config.menuitem();
    $_11.text = "juhu1";
    var $_12/*:ext.config.menuitem*/ = new ext.config.menuitem();
    $_12.text = "juhu2";
    var $_13/*:ext.config.menuitem*/ = new ext.config.menuitem();
    $_13.text = "juhu3";
    $_3.menu = [$_11, $_12, $_13];
    this.gear = {};
    this.gear.handler = function package1$AllElements$66_20(x){return ''+x;};
    $_3.tools = [this.gear];
    var $_14/*:ext.config.aplugin*/ = new ext.config.aplugin();
    var $_15/*:ext.config.aplugin*/ = new ext.config.aplugin();
    $_3.plugins = [$_14, $_15];
    $_2.items = [this.config, this.myVar3, $_3];
    if ($_1) for (var $_16/*:String*/ in $_1) $_2[$_16] = $_1[$_16];
     $3.Panel.call(this,$_2);joo.initField(this, "mVar");joo.initField(this, "myVar2");;
    for/* each*/ (var $2=0;$2</* in*/ this.$bindings$2.length;++$2){var $binding= this.$bindings$2[$2]; $binding.execute();}
  },

  "private var",{ $bindings/*:Array*/:null},

  "public native function get config"/*():exmlparser.config.allElements*/,

  /**
   * @private
   */
  "public native function set config"/*(value:exmlparser.config.allElements):void*/,

  "public native function get myVar3"/*():ext.config.button*/,

  /**
   * @private
   */
  "public native function set myVar3"/*(value:ext.config.button):void*/,

  "public native function get gear"/*():Object*/,

  /**
   * @private
   */
  "public native function set gear"/*(value:Object):void*/,
undefined];},[],["ext.Panel","exmlparser.config.allElements","ext.config.button","ext.config.panel","ext.config.someMixin","ext.MessageBox","ext.config.menuitem","ext.config.aplugin"], "0.8.0", "2.0.15-SNAPSHOT"
);