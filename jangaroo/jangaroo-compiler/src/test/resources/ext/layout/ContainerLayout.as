/**
 * Created by fwienber on 25.07.2016.
 */
package ext.layout {
import ext.Panel;

[Native("Ext.layout.ContainerLayout", require)]
public class ContainerLayout {

  public native function getOwner():Panel;

}
}