package net.jangaroo.exml.test;

import net.jangaroo.exml.model.ConfigClassRegistry;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.Assert.assertEquals;

public class ExmlToMxmlTest extends AbstractExmlTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Override
    public ConfigClassRegistry getConfigClassRegistry() {
        ConfigClassRegistry configClassRegistry = super.getConfigClassRegistry();
        configClassRegistry.getConfig().setKeepExmlFiles(true);
        return configClassRegistry;
    }

    @Test
    public void testConvertAllExmlToMxml() throws Exception {
        setUp("testNamespace.config", "/test-module/testPackage", "/ext-as");
        getConfigClassRegistry().getConfig().setOutputDirectory(temporaryFolder.getRoot());

        File[] mxmlFiles = getExmlc().convertAllExmlToMxml();

        for (File mxmlFile : mxmlFiles) {
            File expected = new File(getClass().getResource("/expected/" + mxmlFile.getName()).toURI());
            assertEquals(mxmlFile.getName() + " as expected", readFileToString(expected), readFileToString(mxmlFile));
        }
    }
}
