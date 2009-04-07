package net.jangaroo.jooc.config;

/**
 * Created by IntelliJ IDEA.
 * User: fwienber
 * Date: 07.04.2009
 * Time: 14:36:18
 * To change this template use File | Settings | File Templates.
 */
public interface JoocOptions {
  boolean isDebug();

  boolean isDebugLines();

  boolean isDebugSource();

  boolean isEnableAssertions();

  boolean isEnableGuessingMembers();

  boolean isEnableGuessingClasses();

  boolean isEnableGuessingTypeCasts();
}
