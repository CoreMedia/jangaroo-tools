/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import net.jangaroo.extxml.json.JsonTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ComponentClassTest.class,
    ConfigAttributeTest.class,
    ExtComponentSrcFileScannerTest.class,
    XsdGeneratorTest.class,
    XsdScannerTest.class,
    XmlToJsonHandlerTest.class,
    JsonTest.class,
    JooClassGeneratorTest.class,
    ExmlComponentSrcFileScannerTest.class,
    ExtComponentSrcFileScannerTest.class,
    ExtXmlTest.class})
public class AllTests {
}
