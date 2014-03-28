joo.classLoader.prepare("package js",/*{*/
"public class Range",1,function($$private){;return[ 

  /**
   * Returns a boolean indicating whether the range's start and end points are at the same position.
   */
  "public native function get collapsed"/*() : Boolean;*/,

  /**
   * Returns the deepest Node that contains the startContainer and endContainer Nodes.
   */
  "public native function get commonAncestorContainer"/*() : Node;*/,

  /**
   * Returns the Node within which the Range ends.
   */
  "public native function get endContainer"/*() : Node;*/,

  /**
   * Returns a number representing where in the endContainer the Range ends.
   */
  "public native function get endOffset"/*() : Number;*/,

  /**
   * Returns the Node within which the Range starts.
   */
  "public native function get startContainer"/*() : Node;*/,

  /**
   * Returns a number representing where in the startContainer the Range starts.
   */
  "public native function get startOffset"/*() : Number;*/,

  // Methods

  // Positioning Methods
  // These methods set the start and end points of a range.

  /**
   * Sets the start position of a Range.
   * <p>If the startNode is a Node of type Text, Comment, or CDATASection, then startOffset is the number of characters
   * from the start of startNode. For other Node types, startOffset is the number of child nodes between the start of
   * the startNode.</p>
   * <p>Setting the start point below (further down in the document) than the end point will throw an
   *  NS_ERROR_ILLEGAL_VALUE exception.</p>
   * @param startNode The Node to start the Range
   * @param startOffset An integer greater than or equal to zero representing the offset for the start
   *  of the Range from the start of startNode.
   */
  "public native function setStart"/*(startNode : Node, startOffset : Number) : void;*/,

  /**
   * Sets the end position of a Range.
   * <p>If the endNode is a Node of type Text, Comment, or CDATASection, then endOffset is the number of characters
   *  from the start of endNode. For other Node types, endOffset is the number of child nodes between the start of
   *  the endNode.</p>
   * <p>Setting the end point above (higher in the document) the start point will throw an
   *  NS_ERROR_ILLEGAL_VALUE exception.</p>
   * @param endNode The Node to end the Range
   * @param endOffset An integer greater than or equal to zero representing the offset for the end of the Range
   *  from the start of endNode.
   */
  "public native function setEnd"/*(endNode : Node, endOffset : Number) : void;*/,

  /**
   * Sets the start position of a Range relative to another Node.
   * <p>The parent Node of the start of the Range will be the same as that for the referenceNode.</p>
   * @param referenceNode The Node to start the Range before
   */
  "public native function setStartBefore"/*(referenceNode : Node) : void;*/,

  /**
   * Sets the start position of a Range relative to another Node.
   * <p>The parent Node of the start of the Range will be the same as that for the referenceNode.</p>
   * @param referenceNode The Node to start the Range after
   */
  "public native function setStartAfter"/*(referenceNode : Node) : void;*/,

  /**
   * Sets the end position of a Range relative to another Node.
   * <p>The parent Node of end of the Range will be the same as that for the referenceNode.</p>
   * @param referenceNode The Node to end the Range before
   */
  "public native function setEndBefore"/*(referenceNode : Node) : void;*/,

  /**
   * Sets the end position of a Range relative to another Node.
   * <p>The parent Node of end of the Range will be the same as that for the referenceNode.</p>
   * @param referenceNode The Node to end the Range after
   */
  "public native function setEndAfter"/*(referenceNode : Node) : void;*/,

  /**
   * Sets the Range to contain the node and its contents.
   * <p>The parent Node of the start and end of the Range will be the same as the parent of the referenceNode.</p>
   * @param referenceNode The Node to select within a Range
   */
  "public native function selectNode"/*(referenceNode : Node) : void;*/,

  /**
   * Sets the Range to contain the contents of a Node.
   * <p>The parent Node of the start and end of the Range will be the referenceNode. The startOffset is 0, and the
   *  endOffset is the number of child Nodes or number of characters contained in the reference node.</p>
   * @param referenceNode The Node whose contents will be selected within a Range
   */
  "public native function selectNodeContents"/*(referenceNode : Node) : void;*/,

  /**
   * Collapses the Range to one of its boundary points.
   * <p>A collapsed Range is empty, containing no content, specifying a single-point in a DOM tree. To determine if a
   *  Range is already collapsed, see the collapsed property.</p>
   * @param toStart A boolean, true collapses the Range to its start, false to its end.
   */
  "public native function collapse"/*(toStart : Boolean) : void;*/,


  // Editing Methods

  // These methods retrieve Nodes from a range and modify the contents of a range.

  /**
   * Returns a document fragment copying the nodes of a Range.
   * <p>Event Listeners added using DOM Events are not copied during cloning. HTML attribute events are duplicated as
   *  they are for the DOM Core cloneNode method. HTML id attributes are also cloned, which can lead to an invalid
   *  document through cloning.</p>
   * <p>Partially selected nodes include the parent tags necessary to make the document fragment valid.</p>
   */
  "public native function cloneContents"/*() : Object /*DocumentFragment* /;*/,


  /**
   * Removes the contents of a Range from the document.
   * <p>Unlike extractContents, this method does not return a documentFragment containing the deleted content.</p>
   */
  "public native function deleteContents"/*() : void;*/,

  /**
   * Moves contents of a Range from the document tree into a document fragment.
   * <p>Event Listeners added using DOM Events are not retained during extraction. HTML attribute events are retained
   *  or duplicated as they are for the DOM Core cloneNode method. HTML id attributes are also cloned, which can lead
   *  to an invalid document if a partially-selected node is extracted and appened to the document.</p>
   * <p>Partially selected nodes are cloned to include the parent tags necessary to make the document fragment valid.</p>
   */
  "public native function extractContents"/*() : void;*/,

  /**
   * Insert a node at the start of a Range.
   * <p>newNode is inserted at the start boundary point of the Range. If the newNodes is to be added to a text Node,
   *  that Node is split at the insertion point, and the insertion occurs between the two text Nodes.</p>
   * <p>If newNode is a document fragment, the children of the document fragment are inserted instead.</p>
   * @param newNode is a Node.
   */
  "public native function insertNode"/*(newNode : Node) : void;*/,

  /**
   * Moves content of a Range into a new node.
   * <p><code>surroundContents</code> is equivalent to
   * <code>newNode.appendChild(range.extractContents()); range.insertNode(newNode)</code>.
   * After surrounding, the boundary points of the range include newNode.</p>
   * @param newNode a Node
   */
  "public native function surroundContents"/*(newNode : Node) : void;*/,

  // Other Methods

  /**
   *  Use as <code>how</code> paramter in compareBoundaryPoints() to compare the end boundary-point of sourceRange to
   *  the end boundary-point of range.
   */
  "public static native function get END_TO_END"/*() : Number;*/,

  /**
   *  Use as <code>how</code> paramter in compareBoundaryPoints() to compare the end boundary-point of sourceRange to
   *  the start boundary-point of range.
   */
  "public static native function get END_TO_START"/*() : Number;*/,

  /**
   *  Use as <code>how</code> paramter in compareBoundaryPoints() to compare the start boundary-point of sourceRange to
   *  the end boundary-point of range.
   */
  "public static native function get START_TO_END"/*() : Number;*/,

  /**
   *  Use as <code>how</code> paramter in compareBoundaryPoints() to compare the start boundary-point of sourceRange to
   *  the start boundary-point of range.
   */
  "public static native function get START_TO_START"/*() : Number;*/,

  /**
   * Compares the boundary points of two Ranges.
   * <p>Any of the following constants can be passed as the value of how parameter:</p>
   * <ul>
   *  <li>Range.END_TO_END compares the end boundary-point of sourceRange to the end boundary-point of range.</li>
   *  <li>Range.END_TO_START compares the end boundary-point of sourceRange to the start boundary-point of range.</li>
   *  <li>Range.START_TO_END compares the start boundary-point of sourceRange to the end boundary-point of range.</li>
   *  <li>Range.START_TO_START compares the start boundary-point of sourceRange to the start boundary-point of range.</li>
   * </ul>
   * @return A number, -1, 0, or 1, indicating whether the corresponding boundary-point of range is respectively
   *  before, equal to, or after the corresponding boundary-point of sourceRange.
   * @param how A constant describing the comparison method, one of values described below.
   * @param sourceRange A Range to compare boundary points with range
   */
  "public native function compareBoundaryPoints"/*(how : Number, sourceRange : Range) : Number;*/,

  /**
   * Returns a Range object with boundary points identical to the cloned Range.
   * <p>clone is copied by value, not reference, so a change in either Range does not effect the other.</p>
   */
  "public native function cloneRange"/*() : Range;*/,

  /**
   * Releases Range from use to improve performance.
   * <p>Allows the user agent to relinquish resources associated with this Range. Subsequent attempts to use the
   *  detached range will result in a DOMException being thrown with an error code of INVALID_STATE_ERR.</p>
   */
  "public native function detach"/*() : void;*/,

  // Gecko Methods

  // This section describes Range methods that are particular to Mozilla and not part of the W3C DOM specifications.

  /**
   * Returns -1, 0, or 1 indicating whether the point occurs before, inside, or after the range.
   * <p>If the referenceNode is a Node of type Text, Comment, or CDATASection, then offset is the number of characters
   *  from the start of referenceNode. For other Node types, offset is the number of child nodes between the start of
   *  the referenceNode.</p>
   * @param referenceNode The Node to compare with the Range.
   * @param offset An integer greater than or equal to zero representing the offset inside the referenceNode.
   * @return -1, 0, or 1 indicating whether the point occurs before, inside, or after the range.
   */
  "public native function comparePoint"/*(referenceNode : Node, offset : Number) : Number;*/,

  /**
   * Returns a document fragment created from a given string of code.
   * <p>This method takes a string and uses Mozilla's parser to convert it to a DOM tree.</p>
   * @param tagString Text that contains text and tags to be converted to a document fragment.
   * @return a document fragment created from a given string of code.
   */
  "public native function createContextualFragment"/*(tagString : String) : Object /*DocumentFragment* /;*/,

  /**
   * Returns a boolean indicating whether the given point is in the range.
   * @param referenceNode The Node to compare with the Range.
   * @param offset The offset into Node of the point to compare with the Range.
   * @return true if the point (cursor position) at offset within ReferenceNode is within this range.
   */
  "public native function isPointInRange"/*(referenceNode : Node, offset : Number) : Boolean;*/,


];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);