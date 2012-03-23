package net.jangaroo.exml.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class DescriptionHolderTest {

  @Test
  public void testGetEscapedDescription() throws Exception {
    DescriptionHolder dh = new DescriptionHolder();

    dh.setDescription("No escaping needed");
    assertEquals("No escaping needed", dh.getEscapedDescription());

    dh.setDescription("escape * please");
    assertEquals("escape &#42; please", dh.getEscapedDescription());
  }

  @Test
  public void testSplitAtAt() {
    DescriptionHolder dh = new DescriptionHolder();

    dh.setDescription("Some description containing an @see this.and.that");
    assertEquals("Some description containing an", dh.getEscapedDescription());
    assertEquals("@see this.and.that", dh.getEscapedDescriptionAts());
  }
}
