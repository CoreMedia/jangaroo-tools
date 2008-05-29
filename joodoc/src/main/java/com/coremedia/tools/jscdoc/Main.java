package com.coremedia.tools.jscdoc;

/**
 * Starting point for creation of jsc javadoc.
 * User: htewis
 * Date: 19.07.2004
 * Time: 14:27:12
 * To change this template use File | Settings | File Templates.
 */
public class Main {

  public static void main(String[] args) {
        Start start = new Start();
        System.out.println("Result code:"+start.begin(args));
  }
}