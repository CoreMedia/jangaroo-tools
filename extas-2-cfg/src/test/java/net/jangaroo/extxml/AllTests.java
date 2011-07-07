/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.extxml;

import net.jangaroo.extxml.file.ExmlComponentSrcFileScannerTest;
import net.jangaroo.extxml.file.ExtComponentSrcFileScannerTest;
import net.jangaroo.extxml.json.JsonTest;
import net.jangaroo.extxml.model.ComponentClassTest;
import net.jangaroo.extxml.model.ConfigAttributeTest;
import net.jangaroo.extxml.xml.ExmlToJsonHandlerTest;
import net.jangaroo.extxml.xml.XsdScannerTest;
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
    XsdScannerTest.class,
    ExmlToJsonHandlerTest.class,
    JsonTest.class,
    ExmlComponentSrcFileScannerTest.class,
    ExtComponentSrcFileScannerTest.class,
    ExtXmlTest.class})
public class AllTests {
}
