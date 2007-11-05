package com.coremedia.tools.jscdoc;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: htewis
 * Date: 20.07.2004
 * Time: 12:08:33
 * To change this template use File | Settings | File Templates.
 */
public class Context {
  ArrayList/*CompilationUnit*/ list;

  public Context(ArrayList/*CompilationUnit*/ list) {
    this.list = list;
  }

  public ArrayList getList() {
    return list;
  }
}
