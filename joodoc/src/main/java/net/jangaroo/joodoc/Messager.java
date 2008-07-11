package net.jangaroo.joodoc;

import com.sun.tools.javac.util.Context;

import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 19.07.2004
 * Time: 15:25:38
 * To change this template use File | Settings | File Templates.
 */
public class Messager extends com.sun.tools.javadoc.Messager {
  public Messager(Context context, String s) {
    super(context, s);
  }

  public Messager(Context context, String s, PrintWriter printwriter, PrintWriter printwriter1, PrintWriter printwriter2) {
    super(context, s, printwriter, printwriter1, printwriter2);
  }
}
