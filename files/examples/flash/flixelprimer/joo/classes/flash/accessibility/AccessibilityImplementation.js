joo.classLoader.prepare("package flash.accessibility",/* {
import flash.geom.Rectangle*/

/**
 * The AccessibilityImplementation class is the base class in Flash Player that allows for the implementation of accessibility in components. This class enables communication between a component and a screen reader. Screen readers are used to translate screen content into synthesized speech or braille for visually impaired users.
 * <p>The AccessibilityImplementation class provides a set of methods that allow a component developer to make information about system roles, object based events, and states available to assistive technology.</p>
 * <p>Adobe Flash Player uses Microsoft Active Accessibility (MSAA), which provides a descriptive and standardized way for applications and screen readers to communicate. For more information on how the Flash Player works with MSAA, see the accessibility chapter in <i>Using Flex SDK</i>.</p>
 * <p>The methods of the AccessibilityImplementation class are a subset of the <a href="http://msdn.microsoft.com/en-us/library/ms696097(VS.85).aspx">IAccessible</a> interface for a component instance.</p>
 * <p>The way that an AccessibilityImplementation implements the IAccessible interface, and the events that it sends, depend on the kind of component being implemented.</p>
 * <p>Do not directly instantiate AccessibilityImplementation by calling its constructor. Instead, create new accessibility implementations by extending the AccImpl class for each new component. In Flash, see the fl.accessibility package. In Flex, see the mx.accessibility package and the accessibility chapter in <i>Using Flex SDK</i>.</p>
 * <p><b>Note:</b> The AccessibilityImplementation class is not supported in AIR runtime versions before AIR 2. The class is available for compilation in AIR versions before AIR 2, but is not supported in the runtime until AIR 2.</p>
 */
"public class AccessibilityImplementation",1,function($$private){;return[ 
  /**
   * Indicates an error code. Errors are indicated out-of-band, rather than in return values. To indicate an error, set the <code>errno</code> property to one of the error codes documented in the AccessibilityImplementation Constants appendix. This causes your return value to be ignored. The <code>errno</code> property of your AccessibilityImplementation is always cleared (set to zero) by the player before any AccessibilityImplementation method is called.
   * @see accessibilityImplementationConstants
   *
   */
  "public var",{ errno/*:uint*/:0},
  /**
   * Used to create a component accessibility stub. If a component is released without an ActionScript accessibility implementation, Adobe recommends that you add a component accessibility stub. This stub causes Flash Player, for accessibility purposes, to treat the component as a simple graphic rather than exposing the internal structure of buttons, textfields, and so on, within the component.
   * <p>To create a component accessibility stub, subclass the relevant AccImpl class, overriding the property stub with a value of <code>true</code>.</p>
   *
   */
  "public var",{ stub/*:Boolean*/:false},

  /**
   * Static constructor. Do not directly instantiate AccessibilityImplementation by calling its constructor. Instead, create new accessibility implementations by extending the mx.accessibility.AccImpl class for each new component.
   * @see mx.accessibility.AccImpl
   *
   */
  "public function AccessibilityImplementation",function AccessibilityImplementation$() {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * An IAccessible method that performs the default action associated with the component that this AccessibilityImplementation represents or of one of its child elements.
   * <p>Implement this method only if the AccessibilityImplementation represents a UI element that has a default action in the MSAA model.</p>
   * <p>If you are implementing <code>accDoDefaultAction()</code> only for the AccessibilityImplementation itself, or only for its child elements, you will need in some cases to indicate that there is no default action for the particular childID that was passed. Do this by setting the <code>errno</code> property to <code>E_MEMBERNOTFOUND</code>.</p>
   * @param childID An unsigned integer corresponding to one of the component's child elements, as defined by <code>getChildIDArray()</code>.
   *
   * @example Following is an example showing how this method is implemented to perform the appropriate default action in the Flex mx.accessibility.ListBaseAccImpl class, the ListBase Accessibility Implementation. For the ListBase and classes that inherit from it, performing the default action "Double Click" for one of its child list item elements selects that element.
   * <listing>
   *      override public function accDoDefaultAction(childID:uint):void
   *      {
   *          if (childID > 0)
   *              ListBase(master).selectedIndex = childID - 1;
   *      }</listing>
   */
  "public function accDoDefaultAction",function accDoDefaultAction(childID/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * MSAA method for returning a <code>DisplayObject</code> or <code>Rectangle</code> specifying the bounding box of a child element in the AccessibilityImplementation.
   * <p>This method is never called with a <code>childID</code> of zero. If your AccessibilityImplementation will never contain child elements, you should not implement this method. If your AccessibilityImplementation can contain child elements, this method is mandatory.</p>
   * <p>You can usually satisfy the requirements of this method by returning an object that represents the child element itself. This works as long as the child element is a <code>DisplayObject</code>. In these cases, simply return the <code>DisplayObject</code> that corresponds to the instance name associated with the relevant visual object in display list.</p>
   * <p>If a child element does not qualify for the technique described above, you may do the bounding-box math yourself and return a <code>Rectangle</code> with: <code>x</code>, <code>y</code>, <code>width</code>, and <code>height</code> properties. The <code>x</code> and <code>y</code> members specify the upper-left corner of the bounding box, and the <code>width</code> and <code>height</code> members specify its size. All four members should be in units of Stage pixels, and relative to the origin of the component that the AccessibilityImplementation represents. The <code>x</code> and <code>y</code> properties may have negative values, since the origin of a <code>DisplayObject</code> is not necessarily in its upper-left corner.</p>
   * <p>If the child element specified by <code>childID</code> is not visible (that is, <code>get_accState</code> for that child would return a value including <code>STATE_SYSTEM_INVISIBLE</code>), you may return <code>null</code> from <code>accLocation</code>. You can also return a <code>Rectangle</code> representing the coordinates where the child element would appear if it were visible.</p>
   * @param childID An unsigned integer corresponding to one of the component's child elements as defined by <code>getChildIDArray()</code>.
   *
   * @return <code>DisplayObject</code> or <code>Rectangle</code> specifying the bounding box of the child element specified by <code>childID</code> parameter.
   *
   * @see flash.display.DisplayObject
   * @see flash.geom.Rectangle
   * @see #getChildIDArray()
   * @see http://msdn.microsoft.com/en-us/library/ms696118(VS.85).aspx Microsoft Accessibility Developer Center: IAccessible::accLocation
   *
   * @example The following example shows how this method is implemented to return the location of a given child element in the Flex mx.accessibility.ListBaseAccImpl class, the ListBase accessibility implementation.
   * <listing>
   *      override public function accLocation(childID:uint):*
   *
   *      {
   *          var listBase:ListBase = ListBase(master);
   *
   *          var index:uint = childID - 1;
   *
   *          if (index < listBase.verticalScrollPosition ||
   *              index >= listBase.verticalScrollPosition + listBase.rowCount)
   *          {
   *              return null;
   *          }
   *          var item:Object = getItemAt(index);
   *
   *          return listBase.itemToItemRenderer(item);
   *      }
   *     </listing>
   */
  "public function accLocation",function accLocation(childID/*:uint*/)/*:**/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * IAccessible method for altering the selection in the component that this AccessibilityImplementation represents.
   * <p>The <code>childID</code> parameter will always be nonzero. This method always applies to a child element rather than the overall component; Flash Player manages the selection of the overall component itself.</p>
   * <p>The <code>selFlag</code> parameter is a bitfield consisting of one or more selection flag constants that allows an MSAA client to indicate how the item referenced by the <code>childID</code> should be selected or take focus. What follows are descriptions of the selection flag constants and what they communicate to the accessibility implementation. As a practical matter, most implementations of this method in accessibility implementations that inherit from the Flex mx.accessibility.ListBaseAccImpl class ignore the <code>selFlag</code> constant and instead rely on the component's keyboard selection behavior to handle multi-selection.</p>
   * <p>The <code>selFlag</code> parameter may or may not contain the <code>SELFLAG_TAKEFOCUS</code> flag. If it does, you should set the child focus to the specified <code>childID</code>, and, unless <code>SELFLAG_EXTENDSELECTION</code> is also present, make that child element the selection anchor. Otherwise, the child focus and selection anchor should remain unmodified, despite the fact that additional flags described below may modify the selection.</p>
   * <p>The <code>selFlag</code> argument will always contain one of the following four flags, which indicate what kind of selection modification is desired:</p>
   * <ul>
   * <li>
   * <p><code>SELFLAG_TAKESELECTION</code>: Clear any existing selection, and set the selection to the specified <code>childID</code>.</p></li>
   * <li>
   * <p><code>SELFLAG_EXTENDSELECTION</code>: Calculate the range of child elements between and including the selection anchor and the specified <code>childID</code>. If <code>SELFLAG_ADDSELECTION</code> is present, add all of these child elements to the selection. If <code>SELFLAG_REMOVESELECTION</code> is present, remove all of these child elements from the selection. If neither <code>SELFLAG_ADDSELECTION</code> nor <code>SELFLAG_REMOVESELECTION</code> is present, all of these child elements should take on the selection anchor's selection state: if the selection anchor is selected, add these child elements to the selection; otherwise remove them from the selection.</p></li>
   * <li>
   * <p><code>SELFLAG_ADDSELECTION</code> (without <code>SELFLAG_EXTENDSELECTION</code>): Add the specified <code>childID</code> to the selection.</p></li>
   * <li>
   * <p><code>SELFLAG_REMOVESELECTION</code> (without <code>SELFLAG_EXTENDSELECTION</code>): Remove the specified <code>childID</code> from the selection.</p></li></ul>
   * <p>Note that for a non-multi-selectable component, the only valid <code>selFlag</code> parameter values are <code>SELFLAG_TAKEFOCUS</code> and <code>SELFLAG_TAKESELECTION</code>. You could in theory also choose to support <code>SELFLAG_REMOVESELECTION</code> for a non-multi-selectable component that allowed the user to force a <code>null</code> selection, but in practice most non-multi-selectable components do not work this way, and MSAA clients may not attempt this type of operation.</p>
   * <p>If you encounter flags that seem invalid, set <code>errno</code> to <code>E_INVALIDARG</code>.</p>
   * <p>Finally, note that when <code>accSelect</code> is called, Flash Player ensures that it has <i>host focus</i> (the window focus of its container application), and that your component has focus within Flash Player.</p>
   * @param operation A bitfield consisting of one or more selection flag constants to indicate how the item is selected or takes focus.
   * @param childID An unsigned integer corresponding to one of the component's child elements as defined by <code>getChildIDArray()</code>.
   *
   * @see #getChildIDArray()
   * @see http://msdn.microsoft.com/en-us/library/ms697291(VS.85).aspx Microsoft Accessibility Developer Center: IAccessible::accSelect
   *
   * @example The following example shows how this method is implemented to select a child item in the Flex mx.accessibility.ListBaseAccImpl class, the ListBase accessibility implementation.
   * <listing>
   * override public function accSelect(selFlag:uint, childID:uint):void
   *      {
   *
   *          var listBase:ListBase = ListBase(master);
   *
   *          var index:uint = childID - 1;
   *
   *          if (index >= 0 && index < listBase.dataProvider.length)
   *              listBase.selectedIndex = index;
   *      }
   *     </listing>
   */
  "public function accSelect",function accSelect(operation/*:uint*/, childID/*:uint*/)/*:void*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * MSAA method for returning the default action of the component that this AccessibilityImplementation represents or of one of its child elements.
   * <p>Implement this method only if the AccessibilityImplementation represents a UI element that has a default action in the MSAA model; be sure to return the exact string that the MSAA model specifies. For example, the default action string for a Button component is "Press."</p>
   * <p>If you are implementing <code>get_accDefaultAction</code> only for the AccessibilityImplementation itself, or only for its child elements, you will need in some cases to indicate that there is no default action for the particular <code>childID</code> that was passed. Do this by simply returning <code>null</code>.</p>
   * @param childID An unsigned integer corresponding to one of the component's child elements, as defined by <code>getChildIDArray()</code>.
   *
   * @return The default action string specified in the MSAA model for the AccessibilityImplementation or for one of its child elements.
   *
   * @see #getChildIDArray()
   * @see http://msdn.microsoft.com/en-us/library/ms696144(VS.85).aspx Microsoft Accessibility Developer Center: IAccessible::get_accDefaultAction
   *
   * @example The following example shows how this method is implemented to return the appropriate default actions in the Flex mx.accessibility.ListBaseAccImpl class, the ListBase accessibility implementation.
   * <listing>
   * override public function get_accDefaultAction(childID:uint):String
   *      {
   *          if (childID == 0)
   *              return null;
   *
   *          return "Double Click";
   *      }</listing>
   */
  "public function get_accDefaultAction",function get_accDefaultAction(childID/*:uint*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * MSAA method for returning the unsigned integer ID of the child element, if any, that has child focus within the component. If no child has child focus, the method returns zero.
   * @return The unsigned integer ID of the child element, if any, that has child focus within the component.
   *
   * @see #getChildIDArray()
   * @see http://msdn.microsoft.com/en-us/library/ms696150(VS.85).aspx Microsoft Accessibility Developer Center: IAccessible::get_accFocus
   *
   * @example The following example shows how this method is implemented to return the focused childID in the Flex mx.accessibility.ListBaseAccImpl class, the ListBase accessibility implementation.
   * <listing>
   * override public function get_accFocus():uint
   *      {
   *      var index:uint = ListBase(master).selectedIndex;
   *
   *      return index >= 0 ? index + 1 : 0;
   *      }</listing>
   */
  "public function get_accFocus",function get_accFocus()/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * MSAA method for returning the name for the component that this AccessibilityImplementation represents or for one of its child elements.
   * <p>In the case of the AccessibilityImplementation itself (<code>childID == 0</code>), if this method is not implemented, or does not return a value, Flash Player uses the <code>AccessibilityProperties.name</code> property value, if it is present.</p>
   * <p>For AccessibilityImplementations that can have child elements, this method must be implemented, and must return a string value when <code>childID</code> is nonzero.</p>
   * <p>Depending on the type of user interface element, names in MSAA mean one of two different things: an author-assigned name, or the actual text content of the element. Usually, an AccessibilityImplementation itself will fall into the former category. Its <code>name</code> property is an author-assigned name. Child elements always fall into the second category. Their names indicate their text content.</p>
   * <p>When the <code>name</code> property of an AccessibilityImplementation has the meaning of an author-assigned name, there are two ways in which components can acquire names from authors. The first entails names present within the component itself; for example, a checkbox component might include a text label that serves as its name. The second—a fallback from the first—entails names specified in the UI and ending up in <code>AccessibilityProperties.name</code>. This fallback option allows users to specify names just as they would for any other Sprite or MovieClip.</p>
   * <p>This leaves three possibilities for the AccessibilityImplementation itself (<code>childID == zero</code>):</p>
   * <ul>
   * <li>
   * <p><b>Author-assigned name within component.</b> The <code>get_accName</code> method should be implemented and should return a string value that contains the AccessibilityImplementation's name when <code>childID</code> is zero. If <code>childID</code> is zero but the AccessibilityImplementation has no name, <code>get_accName</code> should return an empty string to prevent the player from falling back to the <code>AccessibilityProperties.name</code> property.</p></li>
   * <li>
   * <p><b>Author-assigned name from UI.</b> If the AccessibilityImplementation can have child elements, the <code>get_accName</code> method should be implemented but should not return a value when <code>childID</code> is zero. If the AccessibilityImplementation will never have child elements, <code>get_accName</code> should not be implemented.</p></li>
   * <li>
   * <p><b>Name signifying content.</b> The <code>get_accName</code> method should be implemented and should return an appropriate string value when <code>childID</code> is zero. If <code>childId</code> is zero but the AccessibilityImplementation has no content, <code>get_accName</code> should return an empty string to prevent the player from falling back to the <code>AccessibilityProperties.name</code> property.</p></li></ul>
   * <p>Note that for child elements (if the AccessibilityImplementation can have them), the third case always applies. The <code>get_accName</code> method should be implemented and should return an appropriate string value when <code>childID</code> is nonzero.</p>
   * @param childID An unsigned integer corresponding to one of the component's child elements as defined by <code>getChildIDArray()</code>.
   *
   * @return Name of the component or one of its child elements.
   *
   * @see #getChildIDArray()
   * @see AccessibilityProperties
   * @see AccessibilityProperties#name
   * @see mx.accessibility.AccImpl#get_accName()
   * @see mx.accessibility.AccImpl#getName()
   * @see mx.accessibility.AccImpl#getStatusName()
   * @see http://msdn.microsoft.com/en-us/library/ms696177(VS.85).aspx Microsoft Accessibility Developer Center: IAccessible::get_accName
   *
   * @example The following example shows how this method is implemented in the Flex mx.accessibility.AccImpl class, the base accessibility implementation in Flex.
   * <listing>
   * override public function get_accName(childID:uint):String
   *      {
   *          // Start with the name of the component's parent form
   *           // if the component is contained within a form
   *          var accName:String = UIComponentAccImpl.getFormName(master);
   *
   *          // If the element requested is the component itself,
   *          // append the value of any assigned accessibilityProperties.name
   *          if (childID == 0 && master.accessibilityProperties
   *              && master.accessibilityProperties.name
   *                  && master.accessibilityProperties.name != "")
   *              accName += master.accessibilityProperties.name + " ";
   *
   *          // Append the value of the childIDs name
   *      // returned by the component-specific override
   *      // of the mx.accessibility.AccImpl.getName() utility function,
   *          // and append the component's status returned by the
   *      // component-specific override of the
   *      // mx.accessibility.AccImpl.getStatusName() utility function
   *          accName += getName(childID) + getStatusName();
   *
   *      // Return the assembled String if it is neither empty nor null,
   *      // otherwise return null
   *          return (accName != null && accName != "") ? accName : null;
   *      }</listing>
   */
  "public function get_accName",function get_accName(childID/*:uint*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * MSAA method for returning the system role for the component that this AccessibilityImplementation represents or for one of its child elements. System roles are predefined for all the components in MSAA.
   * @param childID An unsigned integer corresponding to one of the component's child elements as defined by <code>getChildIDArray()</code>.
   *
   * @return System role associated with the component.
   *
   * @throws Error Error code <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/runtimeErrors.html#2143">2143</a>, AccessibilityImplementation.get_accRole() must be overridden from its default.
   *
   * @see #getChildIDArray()
   * @see accessibilityImplementationConstants
   * @see http://msdn.microsoft.com/en-us/library/ms696113(VS.85).aspx Microsoft Accessibility Developer Center: IAccessible::get_accRole
   *
   */
  "public function get_accRole",function get_accRole(childID/*:uint*/)/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * MSAA method for returning an array containing the IDs of all child elements that are selected. The returned array may contain zero, one, or more IDs, all unsigned integers.
   * @return An array of the IDs of all child elements that are selected.
   *
   * @see #getChildIDArray()
   * @see http://msdn.microsoft.com/en-us/library/ms696179(VS.85).aspx Microsoft Accessibility Developer Center: IAccessible::get_accSelection
   *
   * @example The following example shows how this method is implemented to return the selected childIDs in the Flex mx.accessibility.ListBaseAccImpl class, the ListBase accessibility implementation.
   * <listing>
   *      override public function get_accSelection():Array
   *      {
   *          var accSelection:Array = [];
   *
   *          var selectedIndices:Array = ListBase(master).selectedIndices;
   *
   *          var n:int = selectedIndices.length;
   *          for (var i:int = 0; i < n; i++)
   *          {
   *              accSelection[i] = selectedIndices[i] + 1;
   *          }
   *
   *          return accSelection;
   *      }
   *     </listing>
   */
  "public function get_accSelection",function get_accSelection()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * IAccessible method for returning the current runtime state of the component that this AccessibilityImplementation represents or of one of its child elements.
   * <p>This method must return a combination of zero, one, or more of the predefined <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/accessibilityImplementationConstants.html">object state constants</a> for components in MSAA. When more than one state applies, the state constants should be combined into a bitfield using <code>|</code>, the bitwise OR operator.</p>
   * <p>To indicate that none of the state constants currently applies, this method should return zero.</p>
   * <p>You should not need to track or report the STATE_SYSTEM_FOCUSABLE or STATE_SYSTEM_FOCUSED states. Flash Player handles these states automatically.</p>
   * @param childID An unsigned integer corresponding to one of the component's child elements as defined by <code>getChildIDArray()</code>.
   *
   * @return A combination of zero, one, or more of the system state constants. Multiple constants are assembled into a bitfield using <code>|</code>, the bitwise OR operator.
   *
   * @throws Error Error code <a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/runtimeErrors.html#2144">2144</a>, AccessibilityImplementation.get_accState() must be overridden from its default.
   *
   * @see #getChildIDArray()
   * @see accessibilityImplementationConstants
   * @see http://msdn.microsoft.com/en-us/library/ms696191(VS.85).aspx Microsoft Accessibility Developer Center: IAccessible::get_accState
   *
   * @example The following example shows how this method is implemented to combine more than one state constant in mx.accessibility.ListBaseAccImpl, the Flex ListBase Accessibility Implementation.
   * <listing>
   *      override public function get_accState(childID:uint):uint
   *      {
   *          var accState:uint = getState(childID);
   *
   *          if (childID > 0)
   *          {
   *              var listBase:ListBase = ListBase(master);
   *
   *              var index:uint = childID - 1;
   *
   *              // For returning states (OffScreen and Invisible)
   *              // when the list Item is not in the displayed rows.
   *              if (index < listBase.verticalScrollPosition ||
   *                  index >= listBase.verticalScrollPosition + listBase.rowCount)
   *              {
   *                  accState |= (STATE_SYSTEM_OFFSCREEN |
   *                               STATE_SYSTEM_INVISIBLE);
   *              }
   *              else
   *              {
   *                  accState |= STATE_SYSTEM_SELECTABLE;
   *
   *                  var item:Object = getItemAt(index);
   *
   *                  var renderer:IListItemRenderer =
   *                      listBase.itemToItemRenderer(item);
   *
   *                  if (renderer != null && listBase.isItemSelected(renderer.data))
   *                      accState |= STATE_SYSTEM_SELECTED | STATE_SYSTEM_FOCUSED;
   *              }
   *          }
   *
   *          return accState;
   *      }</listing>
   */
  "public function get_accState",function get_accState(childID/*:uint*/)/*:uint*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * MSAA method for returning the runtime value of the component that this AccessibilityImplementation represents or of one of its child elements.
   * <p>Implement this method only if your AccessibilityImplementation represents a UI element that has a value in the MSAA model. Be aware that some UI elements that have an apparent 'value' actually expose this value by different means, such as <code>get_accName</code> (text, for example), <code>get_accState</code> (check boxes, for example), or <code>get_accSelection</code> (list boxes, for example).</p>
   * <p>If you are implementing <code>get_accValue</code> only for the AccessibilityImplementation itself, or only for its child elements, you will need in some cases to indicate that there is no concept of value for the particular <code>childID</code> that was passed. Do this by simply returning <code>null</code>.</p>
   * @param childID An unsigned integer corresponding to one of the component's child elements as defined by <code>getChildIDArray()</code>.
   *
   * @return A string representing the runtime value of the component of of one of its child elements.
   *
   * @see #getChildIDArray()
   * @see http://msdn.microsoft.com/en-us/library/ms697312(VS.85).aspx Microsoft Accessibility Developer Center: IAccessible::get_accValue
   *
   * @example The following example shows how this method is implemented to return the appropriate value based on the component's <code>selectedIndex</code> value in the Flex mx.accessibility.ListBaseAccImpl class, the ListBase accessibility implementation.
   * <listing>
   * override public function get_accValue(childID:uint):String
   *      {
   *          var accValue:String;
   *
   *          var listBase:ListBase = ListBase(master);
   *
   *          var index:int = listBase.selectedIndex;
   *          if (childID == 0)
   *          {
   *              if (index > -1)
   *              {
   *                  var item:Object = getItemAt(index);
   *
   *                  if (item is String)
   *                  {
   *                      accValue = item + " " + (index + 1) + " of " + listBase.dataProvider.length;
   *                  }
   *                  else
   *                  {
   *                      accValue = listBase.itemToLabel(item) + " " + (index + 1) +
   *                                 " of " + listBase.dataProvider.length;
   *                  }
   *              }
   *          }
   *
   *          return accValue;
   *      }</listing>
   */
  "public function get_accValue",function get_accValue(childID/*:uint*/)/*:String*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns an array containing the unsigned integer IDs of all child elements in the AccessibilityImplementation.
   * <p>The length of the array may be zero. The IDs in the array should appear in the same logical order as the child elements they represent. If your AccessibilityImplementation can contain child elements, this method is mandatory; otherwise, do not implement it.</p>
   * <p>In assigning child IDs to your child elements, use any scheme that preserves uniqueness within each instance of your AccessibilityImplementation. Child IDs need not be contiguous, and their ordering need not match the logical ordering of the child elements. You should arrange so as to <i>not</i> reuse child IDs; if a child element is deleted, its ID should never be used again for the lifetime of that AccessibilityImplementation instance. Be aware that, due to implementation choices in the Flash player code, undesirable behavior can result if you use child IDs that exceed one million.</p>
   * @return Array containing the unsigned integer IDs of all child elements in the AccessibilityImplementation.
   *
   * @example The following example shows how this method is implemented to return an array of childIDs in the Flex mx.accessibility.ListBaseAccImpl class, the ListBase Accessibility Implementation.
   * <listing>
   *      override public function getChildIDArray():Array
   *      {
   *          var childIDs:Array = [];
   *
   *          if (ListBase(master).dataProvider)
   *          {
   *              var n:uint = ListBase(master).dataProvider.length;
   *              for (var i:int = 0; i < n; i++)
   *              {
   *                  childIDs[i] = i + 1;
   *              }
   *          }
   *          return childIDs;
   *      }
   *     </listing>
   */
  "public function getChildIDArray",function getChildIDArray()/*:Array*/ {
    throw new Error('not implemented'); // TODO: implement!
  },

  /**
   * Returns <code>true</code> or <code>false</code> to indicate whether a text object having a bounding box specified by a <code>x</code>, <code>y</code>, <code>width</code>, and <code>height</code> should be considered a label for the component that this AccessibilityImplementation represents.
   * <p>The <code>x</code> and <code>y</code> coordinates are relative to the upper-left corner of the component to which the AccessibilityImplementation applies, and may be negative. All coordinates are in units of Stage pixels.</p>
   * <p>This method allows accessible components to fit into the Flash Player's search for automatic labeling relationships, which allow text external to an object to supply the object's name. This method is provided because it is expected that the criteria for recognizing labels will differ from component to component. If you implement this method, you should aim to use geometric criteria similar to those in use inside the player code for buttons and textfields. Those criteria are as follows:</p>
   * <ul>
   * <li>For buttons, any text falling entirely inside the button is considered a label.</li>
   * <li>For textfields, any text appearing nearby above and left-aligned, or nearby to the left, is considered a label.</li></ul>
   * <p>If the component that the AccessibilityImplementation represents should never participate in automatic labeling relationships, do not implement <code>isLabeledBy</code>. This is equivalent to always returning <code>false</code>. One case in which <code>isLabeledBy</code> should not be implemented is when the AccessibilityImplementation falls into the "author-assigned name within component" case described under <code>get_accName</code> above.</p>
   * <p>Note that this method is not based on any <b>IAccessible</b> method; it is specific to Flash.</p>
   * @param labelBounds A Rectangle representing the bounding box of a text object.
   *
   * @return <code>true</code> or <code>false</code> to indicate whether a text object having the given label bounds should be considered a label for the component that this AccessibilityImplementation represents.
   *
   */
  "public function isLabeledBy",function isLabeledBy(labelBounds/*:Rectangle*/)/*:Boolean*/ {
    throw new Error('not implemented'); // TODO: implement!
  },
];},[],["Error"], "0.8.0", "0.8.3"
);