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

    dh.setDescription("escape /* and @ please");
    assertEquals("escape &#47;&#42; and &#64; please", dh.getEscapedDescription());
  }
}
