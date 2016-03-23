package net.jangaroo.jooc.mvnplugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.project.MavenProject;

/**
 * Contains some basic constants concerning the different entities of a Jangaroo and a SenchaCmd workspace
 */
public final class Type {

  public static final String CODE = "code";
  public static final String THEME = "theme";
  public static final String APP = "app";
  public static final String WORKSPACE = "workspace";

  public static final String PACKAGE_EXTENSION = "pkg";
  public static final String ZIP_EXTENSION = "zip";

  public static final String JANGAROO_PKG_PACKAGING = "jangaroo-pkg";
  public static final String JANGAROO_APP_PACKAGING = "jangaroo-app";

  private Type() {
    // hide utility class constructor
  }

  public static boolean containsJangarooSources(MavenProject project) {
    String packagingType = project.getPackaging();
    return Type.JANGAROO_PKG_PACKAGING.equals(packagingType) || Type.JANGAROO_APP_PACKAGING.equals(packagingType);
  }

}