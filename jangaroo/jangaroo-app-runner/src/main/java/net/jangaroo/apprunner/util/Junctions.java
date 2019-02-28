package net.jangaroo.apprunner.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * This class contains static methods to test whether a file ({@link Path}) is a Windows directory junction.
 *
 * <p> A Windows directory junction is a Windows-specific type of symbolic directory link.
 * Unfortunately, {@link Files#isSymbolicLink(Path)} returns <code>false</code> for junctions.
 *
 * <p> Use {@link #isSymbolicLink(Path)} as a replacement of {@link Files#isSymbolicLink(Path)}} if you want to
 * treat junctions as symbolic links.
 */
public class Junctions {

  private Junctions() {
  }

  /**
   * Tests whether a file is a Windows directory junction.
   *
   * <p> A Windows directory junction is a Windows-specific type of symbolic directory link.
   * Unfortunately, {@link Files#isSymbolicLink(Path)} returns <code>false</code> for junctions.
   *
   * <p> To tell a junction from a normal directory, this implementation uses {@link BasicFileAttributes#isOther()}.
   *
   * @param   path  The path to the file
   *
   * @return  {@code true} if the file is a junction; {@code false} if
   *          the file does not exist, is not a junction, or it cannot
   *          be determined if the file is a junction or not.
   *
   * @throws  SecurityException
   *          In the case of the default provider, and a security manager is
   *          installed, its {@link SecurityManager#checkRead(String) checkRead}
   *          method denies read access to the file.
   */
  public static boolean isJunction(Path path) {
    try {
      BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
      return fileAttributes.isDirectory() && fileAttributes.isOther();
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Tests whether the file is a symbolic link or a Windows directory junction.
   *
   * <p> A Windows directory junction is a Windows-specific type of symbolic directory link.
   * Unfortunately, {@link Files#isSymbolicLink(Path)} returns <code>false</code> for junctions.
   * 
   * <p> Use this method as a replacement of {@link Files#isSymbolicLink(Path)}} if you want to treat junctions
   * as symbolic links.
   *
   * @param   path  The path to the file
   *
   * @return  {@code true} if the file is a symbolic link (including junctions); {@code false} if
   *          the file does not exist, is not a symbolic link, or it cannot
   *          be determined if the file is a symbolic link or not.
   *
   * @throws  SecurityException
   *          In the case of the default provider, and a security manager is
   *          installed, its {@link SecurityManager#checkRead(String) checkRead}
   *          method denies read access to the file.
   */
  public static boolean isSymbolicLink(Path path) {
    return Files.isSymbolicLink(path) || isJunction(path);
  }
}
