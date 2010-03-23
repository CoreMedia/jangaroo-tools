/*
 * Copyright 2009 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */
package net.jangaroo.ide.idea.exml.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import net.jangaroo.ide.idea.exml.ExmlcConfigurationBean;

import javax.swing.*;

/**
 * Jangaroo configuration on its Facet Tab.
 */
public class ExmlFacetEditorTabUI {
  private JPanel rootComponent;
  private TextFieldWithBrowseButton sourceDirTextField;
  private TextFieldWithBrowseButton generatedSourcesDirTextField;
  private TextFieldWithBrowseButton generatedResourcesDirTextField;
  private JCheckBox showCompilerInfoMessages;

  private static final FileChooserDescriptor SOURCE_DIRECTORY_CHOOSER_DESCRIPTOR = FileChooserDescriptorFactory.createSingleFolderDescriptor();
  private static final FileChooserDescriptor GENERATED_SOURCE_DIRECTORY_CHOOSER_DESCRIPTOR = FileChooserDescriptorFactory.createSingleFolderDescriptor();
  private static final FileChooserDescriptor GENERATED_RESOURCE_DIRECTORY_CHOOSER_DESCRIPTOR = FileChooserDescriptorFactory.createSingleFolderDescriptor();

  static {
    SOURCE_DIRECTORY_CHOOSER_DESCRIPTOR.setTitle("Choose EXML Source Directory");
    SOURCE_DIRECTORY_CHOOSER_DESCRIPTOR.setDescription("Choose the directory where EXML should read *.exml files containing component descriptions.");
    GENERATED_SOURCE_DIRECTORY_CHOOSER_DESCRIPTOR.setTitle("Choose EXML Generated Sources Directory");
    GENERATED_SOURCE_DIRECTORY_CHOOSER_DESCRIPTOR.setDescription("Choose the directory where EXML should store the *.as files created from *.exml. "+
      "This should be a source directory, so that the Jangaroo Language plugin finds and compiles these classes.");
    GENERATED_RESOURCE_DIRECTORY_CHOOSER_DESCRIPTOR.setTitle("Choose EXML Schema Directory");
    GENERATED_RESOURCE_DIRECTORY_CHOOSER_DESCRIPTOR.setDescription("Choose the directory where EXML should store the geneated *.xsd file.");
  }

  public ExmlFacetEditorTabUI() {
    sourceDirTextField.addBrowseFolderListener(null,null, null, SOURCE_DIRECTORY_CHOOSER_DESCRIPTOR,
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
    generatedSourcesDirTextField.addBrowseFolderListener(null,null, null, SOURCE_DIRECTORY_CHOOSER_DESCRIPTOR,
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
    generatedResourcesDirTextField.addBrowseFolderListener(null,null, null, SOURCE_DIRECTORY_CHOOSER_DESCRIPTOR,
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
  }

  public JPanel getRootComponent() {
    return rootComponent;
  }

  public void setData(ExmlcConfigurationBean data) {
    sourceDirTextField.setText(data.getSourceDirectory());
    generatedSourcesDirTextField.setText(data.getGeneratedSourcesDirectory());
    generatedResourcesDirTextField.setText(data.getGeneratedResourcesDirectory());
    showCompilerInfoMessages.setSelected(data.isShowCompilerInfoMessages());
  }

  public ExmlcConfigurationBean getData(ExmlcConfigurationBean data) {
    data.setSourceDirectory(sourceDirTextField.getText());
    data.setGeneratedSourcesDirectory(generatedSourcesDirTextField.getText());
    data.setGeneratedResourcesDirectory(generatedResourcesDirTextField.getText());
    data.setShowCompilerInfoMessages(showCompilerInfoMessages.isSelected());
    return data;
  }

  public boolean isModified(ExmlcConfigurationBean data) {
    ExmlcConfigurationBean currentData = new ExmlcConfigurationBean();
    // TODO: namespace, namespacePrefix and xsd not yet used by UI, so copy these:
    currentData.setNamespace(data.getNamespace());
    currentData.setNamespacePrefix(data.getNamespacePrefix());
    currentData.setXsd(data.getXsd());
    return !getData(currentData).equals(data);
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here 
  }
}