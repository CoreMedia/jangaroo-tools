package package1.someOtherPackage {
import ext.Base;

/**
 * This is not a native class, but used for the attempt to force a name-clash between a
 * native class (package1.SomeNativeClass) and a standard class (this class).
 */
public class SomeNativeClass extends Base {

}
}