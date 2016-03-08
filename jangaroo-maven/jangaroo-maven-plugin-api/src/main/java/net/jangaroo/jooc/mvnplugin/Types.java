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

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">nicolas De Loof</a>
 */
public interface Types {

  /**
   * packaging type handled by the plugin
   */
  String JANGAROO_TYPE = "jangaroo"; // TODO legacy

  String SENCHA_CODE = "sencha-code";
  String SENCHA_THEME = "sencha-theme";
  String SENCHA_APP = "sencha-app";

  List<String> SENCHA_TYPES = Arrays.asList(JANGAROO_TYPE, SENCHA_CODE, SENCHA_APP, SENCHA_THEME);

  /**
   * extension for packaging handled by the plugin
   */
  String JAVASCRIPT_EXTENSION = "jar";

}
