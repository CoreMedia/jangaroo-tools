// aliases the global object in broswers.
// Declared here to avoid dependency resp. nameclash with js.window in jangaroo-browser
package joo {

public native function get window() : *;

}