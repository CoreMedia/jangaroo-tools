joo.classLoader.prepare("package js",/* {*/

/**
 * Represents the active selection, which is a highlighted block of text or other elements in the document that a user
 * or a script can carry out some action on.
 * <code>selection</code> is a child object of the <code>document</code> object.
 */
"public interface IESelection",1,function($$private){;return[ /*

  /**
   * Retrieves the type of selection.
   *
   * @return String that receives one of the following values.
   * <ul>
   * <li><code>None</code> - No selection/insertion point.
   * <li><code>Text</code> - Specifies a text selection.
   * <li><code>Control</code> - Specifies a control selection, which enables dimension controls allowing the selected
   *   object to be resized.
   * /
  function type():String;*/,/*

  /**
   * Clears the contents of the selection.
   * <p>This method is defined in <a href="http://www.w3.org/tr/2000/wd-dom-level-1-20000929/" target="_top">World Wide Web Consortium (W3C) Document Object Model (DOM) Level 1</a>.</p>
   * /
  function clear():void;*/,/*

  /**
   * Creates a TextRange object from the current text selection, or a controlRange collection from a control selection.
   *
   * @return TextRange the created TextRange object.
   * /
  function createRange():TextRange;*/,/*

  /**
   * Creates a TextRange object collection from the current selection.
   * <p>The default implementation of this method returns a collection consisting of a single TextRange object.</p>
   * /
  function createRangeCollection():Collection;*/,/*

  /**
   * Cancels the current selection, sets the selection type to none, and sets the item property to null.
   * /
  function empty():void;*/,

];},[],[], "0.8.0", "0.8.3"
);