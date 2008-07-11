package net.jangaroo.joodoc;

import com.sun.javadoc.*;
import com.sun.javadoc.Type;
import net.jangaroo.jooc.*;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 11:18:10
 * To change this template use File | Settings | File Templates.
 */
public class FieldDocImpl extends MemberDocImpl implements FieldDoc {

  public FieldDocImpl(FieldDeclaration decl) {
    super (decl);
  }

  public boolean isTransient() {
    return false;
  }

  public boolean isVolatile() {
    return false;
  }

  public SerialFieldTag[] serialFieldTags() {
    return new SerialFieldTag[0];
  }

  public Type type() {
    return getType();
  }

  public Object constantValue() {
    return null;
  }

  public String constantValueExpression() {
    return null;
  }

  public boolean isField() {
    return true;
  }

  

}
