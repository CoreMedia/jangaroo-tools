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
 * @author <a href="mailto:nicolas.deloof@gmail.com">nicolas De Loof</a>
 */
public class Type {

  public static final String CODE = "sencha-code";
  public static final String THEME = "sencha-theme";
  public static final String APP = "sencha-app";
  public static final String WORKSPACE = "pom";
  public static final String PACKAGE_EXTENSION = "pkg";
  public static final String ZIP_EXTENSION = "zip";

  public static boolean containsJangarooSources(MavenProject project) {
    String packagingType = project.getPackaging();
    return Type.APP.equals(packagingType) || Type.CODE.equals(packagingType) || Type.THEME.equals(packagingType);
  }


}
