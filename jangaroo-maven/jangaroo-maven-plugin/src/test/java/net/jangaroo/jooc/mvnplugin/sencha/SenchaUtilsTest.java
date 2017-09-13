package net.jangaroo.jooc.mvnplugin.sencha;

import org.junit.Test;

import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaPackageName;
import static net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils.getSenchaVersionForMavenVersion;
import static org.junit.Assert.*;

public class SenchaUtilsTest {
  @Test
  public void testGetSenchaVersionForMavenVersion() throws Exception {
    assertEquals("9.1.3", getSenchaVersionForMavenVersion("foobar-9.1.3"));
    assertEquals("9.1.3", getSenchaVersionForMavenVersion("9.1.3-SNAPSHOT"));
    assertEquals("9.1.3", getSenchaVersionForMavenVersion("foobar-9.1-3"));
    assertEquals("9.1.3", getSenchaVersionForMavenVersion("9.1-3-SNAPSHOT"));
    assertEquals("9.1.3", getSenchaVersionForMavenVersion("foo-9.1-bar-3-baz"));

    assertEquals("9.1", getSenchaVersionForMavenVersion("foo-9.1-bar"));
    assertEquals("9.1", getSenchaVersionForMavenVersion("foo9-1-bar"));

    assertEquals("9.1.3.1", getSenchaVersionForMavenVersion("foobar-9.1.3.1-patch1022"));
    assertEquals("9.1.3.1022", getSenchaVersionForMavenVersion("foobar-9-bar1.3-1022p"));
    assertEquals(null, getSenchaVersionForMavenVersion("foobar-baz-SNAPSHOT"));
  }

}