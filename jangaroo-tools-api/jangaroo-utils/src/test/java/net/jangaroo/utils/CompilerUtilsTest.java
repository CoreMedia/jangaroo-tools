package net.jangaroo.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by IntelliJ IDEA. User: fwienber Date: 05.07.11 Time: 09:53 To change this template use File | Settings |
 * File Templates.
 */
public class CompilerUtilsTest {
  @Test
  public void testQName() throws Exception {
    Assert.assertEquals("irgend.wo.Was", CompilerUtils.qName("irgend.wo", "Was"));
    Assert.assertEquals("Was", CompilerUtils.qName("", "Was"));
  }

  @Test
  public void testPackageName() throws Exception {
    Assert.assertEquals("irgend.wo", CompilerUtils.packageName("irgend.wo.Was"));
    Assert.assertEquals("", CompilerUtils.packageName("Was"));
  }

  @Test
  public void testClassName() throws Exception {
    Assert.assertEquals("Was", CompilerUtils.className("irgend.wo.Was"));
    Assert.assertEquals("Was", CompilerUtils.className("Was"));
  }

  @Test
  public void testFileFromQName() throws Exception {
    Assert.assertEquals(new File("pfad/irgend/wo/Was.as"), CompilerUtils.fileFromQName("irgend.wo.Was", new File("pfad"), ".as"));
    Assert.assertEquals(new File("pfad/Was.as"), CompilerUtils.fileFromQName("Was", new File("pfad"), ".as"));
    Assert.assertEquals(new File("pfad/irgend/wo/Was.as"), CompilerUtils.fileFromQName("irgend.wo", "Was", new File("pfad"), ".as"));
  }

  @Test
  public void testFileNameFromQName() throws Exception {
    Assert.assertEquals("irgend/wo/Was.as", CompilerUtils.fileNameFromQName("irgend.wo.Was", '/', ".as"));
    Assert.assertEquals("Was.as", CompilerUtils.fileNameFromQName("Was", '/', ".as"));
  }

  @Test
  public void testQNameFromFile() throws Exception {
    File dir = new File("pfad");
    Assert.assertEquals("irgend.wo.Was", CompilerUtils.qNameFromFile(dir, new File(dir, "irgend/wo/Was.as")));
    Assert.assertEquals("Was", CompilerUtils.qNameFromFile(dir, new File(dir, "Was.as")));
  }

  @Test
  public void testDirname() {
    Assert.assertEquals(new File("this/is/my").getPath(),
      CompilerUtils.dirname(new File("this/is/my/file.bla").getPath()));
  }

  @Test
  public void testRemoveExtension() {
    Assert.assertEquals("this/is/my/file",CompilerUtils.removeExtension("this/is/my/file.bla"));
    Assert.assertEquals("file",CompilerUtils.removeExtension("file.bla"));
    Assert.assertEquals("file",CompilerUtils.removeExtension("file"));
  }

  @Test
  public void testUncapitalize() {
    Assert.assertEquals(null, CompilerUtils.uncapitalize(null));
    Assert.assertEquals("", CompilerUtils.uncapitalize(""));
    Assert.assertEquals("a", CompilerUtils.uncapitalize("a"));
    Assert.assertEquals("a", CompilerUtils.uncapitalize("A"));
    Assert.assertEquals("aB", CompilerUtils.uncapitalize("aB"));
    Assert.assertEquals("ab", CompilerUtils.uncapitalize("Ab"));
    Assert.assertEquals("ab", CompilerUtils.uncapitalize("ab"));
    Assert.assertEquals("ab", CompilerUtils.uncapitalize("AB"));
    Assert.assertEquals("aBc", CompilerUtils.uncapitalize("ABc"));
  }
}
