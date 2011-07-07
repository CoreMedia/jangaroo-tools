package net.jangaroo.extxml;

import java.io.IOException;

/**
 * Generate config classes for components defined in ActionScript.
 * To run the tool, you need a UI workspace based on Jangaroo 0.8.4
 * in the directory git/ui in your home directory and a UI workspace
 * based on Jangaroo 0.8.5-SNAPSHOT in the directory git/ui2.
 */
public class GenerateAll {
  public static void main(String[] args) throws IOException {
    String home = System.getProperty("user.home");
    ExtXml.main(
            home + "/git/ui/ui-toolkit/ui-components/src/main/joo",
            home + "/git/ui2/ui-toolkit/ui-components/src/main/joo",
            "com.coremedia.ui.config",
            home + "/git/ui/ui-toolkit/ui-components/target/jangaroo-output-test/ext3.xsd", "ext.config"
    );
    ExtXml.main(
            home + "/git/ui/editor-sdk/editor-components/src/main/joo",
            home + "/git/ui2/editor-sdk/editor-components/src/main/joo",
            "com.coremedia.cms.editor.sdk.config",
            home + "/git/ui/ui-toolkit/ui-components/target/jangaroo-output-test/ext3.xsd", "ext.config",
            home + "/git/ui/ui-toolkit/ui-components/target/jangaroo-output/ui-components.xsd", "com.coremedia.ui.config"
    );
    ExtXml.main(
            home + "/git/ui/editor-sdk/components-test-webapp/src/main/joo",
            home + "/git/ui2/editor-sdk/components-test-webapp/src/main/joo",
            "com.coremedia.cms.editor.sdk.test.config",
            home + "/git/ui/ui-toolkit/ui-components/target/jangaroo-output-test/ext3.xsd", "ext.config",
            home + "/git/ui/ui-toolkit/ui-components/target/jangaroo-output/ui-components.xsd", "com.coremedia.ui.config",
            home + "/git/ui/editor-sdk/editor-components/target/jangaroo-output/editor-components.xsd", "com.coremedia.cms.editor.sdk.config"
    );
  }
}
