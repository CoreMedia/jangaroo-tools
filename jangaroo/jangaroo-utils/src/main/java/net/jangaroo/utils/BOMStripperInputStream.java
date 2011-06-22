package net.jangaroo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * Thanks sabre150, http://forums.sun.com/thread.jspa?messageID=10324562#10324562
 */
public class BOMStripperInputStream extends PushbackInputStream {

  public static final int[][] BOMS = {
    {0x00, 0x00, 0xFE, 0xFF},
    {0xFF, 0xFE, 0x00, 0x00},
    {0x2B, 0x2F, 0x76, 0x38},
    {0x2B, 0x2F, 0x76, 0x39},
    {0x2B, 0x2F, 0x76, 0x2B},
    {0x2B, 0x2F, 0x76, 0x2F},
    {0xDD, 0x73, 0x66, 0x73},
    {0xEF, 0xBB, 0xBF},
    {0x0E, 0xFE, 0xFF},
    {0xFB, 0xEE, 0x28},
    {0xFE, 0xFF},
    {0xFF, 0xFE}
  };

  static private int testForBOM(int[] bom, int[] bytes) {
    for (int index = 0; index < bom.length; index++) {
      if (bom[index] != bytes[index])
        return 0;
    }
    return bom.length;
  }

  public BOMStripperInputStream(InputStream is) throws IOException {
    super(is, 4);

    final int[] bytes =
      {
        read(), read(), read(), read()
      };
    int count = 0;
    for (int[] bom : BOMS) {
      count = testForBOM(bom, bytes);
      if (count != 0)
        break;
    }
    for (int index = bytes.length - 1; index >= count; index--) {
      if (bytes[index] != -1)
        unread(bytes[index]);
    }
  }
}
