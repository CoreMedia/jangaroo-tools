/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */

import net.jangaroo.properties.compiler.PropertiesCompiler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PropertiesCompilerTest {
  
  @Rule
  public TemporaryFolder outputFolder = new TemporaryFolder();
  
  @Rule
  public TemporaryFolder apiFolder = new TemporaryFolder();

  @Test
  public void testMain() throws Exception {
    File root = new File(getClass().getResource("/").toURI());
    File out = outputFolder.getRoot();

    List<String> args = new ArrayList<String>();
    args.add("-d");
    args.add(outputFolder.getRoot().getAbsolutePath());
    args.add("-api");
    args.add(apiFolder.getRoot().getAbsolutePath());
    args.add("-sourcepath");
    args.add(root.getAbsolutePath());
    args.add("-defaultLocale");
    args.add("en_gb");
    args.add(getFile("/testPackage/subPackage/Proberties.properties").getAbsolutePath());
    args.add(getFile("/testPackage/PropertiesTest.properties").getAbsolutePath());
    args.add(getFile("/testPackage/PropertiesTest_de.properties").getAbsolutePath());
    args.add(getFile("/testPackage/PropertiesTest_es_ES.properties").getAbsolutePath());
    args.add(getFile("/testPackage/PropertiesTest_it_VA_WIN.properties").getAbsolutePath());

    PropertiesCompiler.main(args.toArray(new String[args.size()]));

    File api = new File(apiFolder.getRoot(), "testPackage/PropertiesTest_properties.as");
    assertTrue(api.getAbsolutePath() + " exists", api.exists());
    assertTrue(api.length() > 100);

    File defaultProp = new File(out,"en_gb/testPackage/PropertiesTest_properties.js");
    assertTrue(defaultProp.getAbsolutePath() + " exists", defaultProp.exists());
    assertTrue(defaultProp.length() > 100);

    File deProp = new File(out, "de/testPackage/PropertiesTest_properties.js");
    assertTrue(deProp.getAbsolutePath() + " exists", deProp.exists());
    assertTrue(deProp.length() > 100);

    File es_ESNProp = new File(out, "es_ES/testPackage/PropertiesTest_properties.js");
    assertTrue(es_ESNProp.getAbsolutePath() + " exists", es_ESNProp.exists());
    assertTrue(es_ESNProp.length() > 100);

    File it_VA_WINProp = new File(out, "it_VA_WIN/testPackage/PropertiesTest_properties.js");
    assertTrue(it_VA_WINProp.getAbsolutePath() + " exists", it_VA_WINProp.exists());
    assertTrue(it_VA_WINProp.length() > 100);

    File subPackageProp = new File(out, "en_gb/testPackage/subPackage/Proberties_properties.js");
    assertTrue(subPackageProp.getAbsolutePath() + " exists", subPackageProp.exists());
    assertTrue(subPackageProp.length() > 100);
  }

  private File getFile(String path) throws URISyntaxException {
    return new File(getClass().getResource(path).toURI());
  }
}
