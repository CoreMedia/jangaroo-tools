joo.classLoader.prepare("package package1",/* {
import ext.Panel;
import exmlparser.config.allElements;
import ext.config.button;
import net.jangaroo.ext.Exml;*/

"public class AllElements extends ext.Panel",2,function($$private){var $1=ext;return[function(){joo.classLoader.init(exmlparser.config.allElements);}, /*
    /* add an extra import statement to the class * /
    import ext.MessageBox;*/

    /**
     * This is my <b>constant</b>
     */
    "public static const",{ SOME_CONSTANT/*:uint*/ :function(){return( exmlparser.config.allElements.SOME_CONSTANT);}},
    /**
     * This is another <b>constant</b>
     */
    "public static const",{ ANOTHER_CONSTANT/*:String*/ :function(){return( exmlparser.config.allElements.ANOTHER_CONSTANT);}},
    "public static const",{ CODE_CONSTANT/*:int*/ :function(){return( exmlparser.config.allElements.CODE_CONSTANT);}},
    
  "public function AllElements",function AllElements(config/*:exmlparser.config.allElements = null*/) {if(arguments.length<=0)config=null;
    
    var myVar/*:String*/ = config.myProperty + '_suffix';
    var myVar2/*:Object*/ = {
      prop: config.myProperty
    };
    var myVar3/*:ext.config.button*/ = {
      text: "Foo",
      scope: "constructor"
    };
     $1.Panel.call(this,/*exmlparser.config.allElements*/(net.jangaroo.ext.Exml.apply({
        layout: config.myLayout,
        title: "I am a panel",
        someList: [{
              xtype: "button",
              id: "myId",
              text: "click me!"
        }],
        defaults: {layout: "border"},
        layoutConfig: {
              anchor: "test",
              bla: "blub",
              border: "solid"
        },
        items: [
              {
                    xtype: "button",
                    text: "Save",
                    handler: function package1$AllElements$49_30()/*:void*/ {
          $1.MessageBox.alert('gotcha!');
        }
              },
              {xtype: "editortreepanel"}
        ],
        menu: [
              {
                    xtype: "menuitem",
                    text: "juhu1"
              },
              {
                    xtype: "menuitem",
                    text: "juhu2"
              },
              {
                    xtype: "menuitem",
                    text: "juhu3"
              }
        ],
        tools: [{
              handler: function package1$AllElements$70_24(x){return ''+x;},
              id: "gear"
        }],
        plugins: [
              {ptype: "aplugin"},
              {ptype: "aplugin"}
        ]
  }, config)));
  },
undefined];},[],["ext.Panel","exmlparser.config.allElements","net.jangaroo.ext.Exml","ext.MessageBox"], "0.8.0", "2.0.15-SNAPSHOT"
);