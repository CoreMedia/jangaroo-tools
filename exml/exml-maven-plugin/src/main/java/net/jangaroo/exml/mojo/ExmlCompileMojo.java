/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.exml.mojo;

import net.jangaroo.exml.compiler.Exmlc;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * A Mojo to compile EXML sources to AS3 sources into target/generated-sources/joo in phase generate-sources.
 */
@Mojo(name = "exml",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true)
public class ExmlCompileMojo extends AbstractExmlCompileMojo {

  @Override
  protected void executeExmlc(Exmlc exmlc) {
    // Generate all config classes from EXML files:
    exmlc.generateAllConfigClasses();
    exmlc.generateAllComponentClasses();
  }

  protected List<File> getSourcePath() {
    return Collections.singletonList(getSourceDirectory());
  }
}
