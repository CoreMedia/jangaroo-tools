joo.classLoader.prepare("package flash.display",/* {*/


/**
 * AVM1Movie is a simple class that represents AVM1 movie clips, which use ActionScript 1.0 or 2.0. (AVM1 is the ActionScript virtual machine used to run ActionScript 1.0 and 2.0. AVM2 is the ActionScript virtual machine used to run ActionScript 3.0.) When a Flash Player 8, or older, SWF file is loaded by a Loader object, an AVM1Movie object is created. The AVM1Movie object can use methods and properties inherited from the DisplayObject class (such as <code>x</code>, <code>y</code>, <code>width</code>, and so on). However, no interoperability (such as calling methods or using parameters) between the AVM1Movie object and AVM2 objects is allowed.
 * <p>There are several restrictions on an AVM1 SWF file loaded by an AVM2 SWF file:</p>
 * <ul>
 * <li>The loaded AVM1Movie object operates as a psuedo-root object for the AVM1 SWF file and all AVM1 SWF files loaded by it (as if the ActionScript 1.0 <code>lockroot</code> property were set to <code>true</code>). The AVM1 movie is always the top of any ActionScript 1.0 or 2.0 code execution in any children. The <code>_root</code> property for loaded children is always this AVM1 SWF file, unless the <code>lockroot</code> property is set in a loaded AVM1 SWF file.</li>
 * <li>The AVM1 content cannot load files into levels. For example, it cannot load files by calling <code>loadMovieNum("url", levelNum)</code>.</li>
 * <li>The AVM1 SWF file that is loaded by an AVM2 SWF file cannot load another SWF file into <code>this</code>. That is, it cannot load another SWF file over itself. However, child Sprite objects, MovieClip objects, or other AVM1 SWF files loaded by this SWF file can load into <code>this</code>.</li></ul>
 * @see DisplayObject
 * @see Loader
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e3c.html Core display classes
 *
 */
"public class AVM1Movie extends flash.display.DisplayObject",3,function($$private){;return[ 
];},[],["flash.display.DisplayObject"], "0.8.0", "0.8.3"
);