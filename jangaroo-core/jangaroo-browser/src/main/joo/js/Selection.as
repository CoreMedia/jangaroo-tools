package js {

/**
 * Interface for manipulating and querying the current selected range
 * of nodes within the document.
 */
public interface Selection {

  /**
     * Returns the node in which the selection begins.
     */
    function get anchorNode() : Node;

    /**
     * The offset within the (text) node where the selection begins.
     */
    function get anchorOffset() : int;

    /**
     * Returns the node in which the selection ends.
     */
    function get focusNode() : Node;

    /**
     * The offset within the (text) node where the selection ends.
     */
    function get focusOffset() : int;

    /**
     * Indicates if the selection is collapsed or not.
     */
    function get isCollapsed() : Boolean;

    /**
     * Returns the number of ranges in the selection.
     */
    function get rangeCount() : int;

    /**
     * Returns the range at the specified index.
     */
    function getRangeAt(index : int) : Range;

    /**
     * Collapses the selection to a single point, at the specified offset
     * in the given DOM node. When the selection is collapsed, and the content
     * is focused and editable, the caret will blink there.
     * @param parentNode      The given dom node where the selection will be set
     * @param offset          Where in given dom node to place the selection (the offset into the given node)
     */
    function collapse(parentNode : Node, offset : int) : void;

 
     /**
      * Extends the selection by moving the selection end to the specified node and offset,
      * preserving the selection begin position. The new selection end result will always
      * be from the anchorNode to the new focusNode, regardless of direction.
      * @param parentNode      The node where the selection will be extended to
      * @param offset          Where in node to place the offset in the new selection end
      */
     function extend(parentNode : Node, offset : int) : void;
 
     /**
      * Collapses the whole selection to a single point at the start
      * of the current selection (irrespective of direction).  If content
      * is focused and editable, the caret will blink there.
      */
     function collapseToStart() : void;
 
     /**
      * Collapses the whole selection to a single point at the end
      * of the current selection (irrespective of direction).  If content
      * is focused and editable, the caret will blink there.
      */
     function collapseToEnd() : void;
 
     /**
      * Indicates whether the node is part of the selection. If partlyContained 
      * is set to PR_TRUE, the function returns true when some part of the node 
      * is part of the selection. If partlyContained is set to PR_FALSE, the
      * function only returns true when the entire node is part of the selection.
      */
     function containsNode(node : Node, partlyContained : Boolean) : Boolean;
 
     /**
      * Adds all children of the specified node to the selection.
      * @param parentNode  the parent of the children to be added to the selection.
      */
     function selectAllChildren(parentNode : Node) : void;
 
     /**
      * Adds a range to the current selection.
      */
     function addRange(range : Range) : void;
  
     /**
      * Removes a range from the current selection.
      */
     function removeRange(range : Range) : void;
 
     /**
      * Removes all ranges from the current selection.
      */
     function removeAllRanges() : void;
 
     /**
      * Deletes this selection from document the nodes belong to.
      */
     function deleteFromDocument() : void;
 
     /**
      * Modifies the cursor Bidi level after a change in keyboard direction
      * @param langRTL is PR_TRUE if the new language is right-to-left or
      *                PR_FALSE if the new language is left-to-right.
      */
     function selectionLanguageChange(langRTL : Boolean) : void;
 
     /**
      * Returns the whole selection into a plain text string.
      */
     function toString() : String;


}
}