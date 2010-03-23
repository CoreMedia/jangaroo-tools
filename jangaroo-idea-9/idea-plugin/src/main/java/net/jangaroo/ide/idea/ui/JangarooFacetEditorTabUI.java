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
package net.jangaroo.ide.idea.ui;

import net.jangaroo.ide.idea.JoocConfigurationBean;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;

//import com.intellij.openapi.fileChooser.FileChooserFactory;
//import com.intellij.openapi.fileChooser.FileTextField;
//import com.intellij.openapi.Disposable;


/**
 * Jangaroo configuration on its Facet Tab.
 */
public class JangarooFacetEditorTabUI {
  private JCheckBox verboseCheckBox;
  private JPanel rootComponent;
  private JCheckBox enableAssertionsCheckBox;
  private JRadioButton keepDebugSourceRadioButton;
  private JRadioButton keepNewLinesOnlyRadioButton;
  private JRadioButton suppressWhiteSpaceRadioButton;
  private JCheckBox allowDuplicateVariableCheckBox;
  private JCheckBox guessInheritedMembersCheckBox;
  private JCheckBox guessInScopeClassesCheckBox;
  private JCheckBox guessTypeCastsCheckBox;
  private JCheckBox mergeOutputCheckBox;
  private TextFieldWithBrowseButton outputDirTextField;
  private TextFieldWithBrowseButton mergedOutputFileTextField;
  private JCheckBox showCompilerInfoMessages;
  private ButtonGroup whiteSpaceButtonGroup;

  private static final FileChooserDescriptor OUTPUT_DIRECTORY_CHOOSER_DESCRIPTOR = FileChooserDescriptorFactory.createSingleFolderDescriptor();
  private static final FileChooserDescriptor MERGED_OUTPUT_FILE_CHOOSER_DESCRIPTOR = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();

  static {
    OUTPUT_DIRECTORY_CHOOSER_DESCRIPTOR.setTitle("Choose Jangaroo Output Directory");
    OUTPUT_DIRECTORY_CHOOSER_DESCRIPTOR.setDescription("Choose the directory where Jangaroo should place JavaScript files containing compiled ActionScript classes.");
    MERGED_OUTPUT_FILE_CHOOSER_DESCRIPTOR.setTitle("Choose Jangaroo Merged Output File");
    MERGED_OUTPUT_FILE_CHOOSER_DESCRIPTOR.setDescription("Choose the file where Jangaroo should create the merged JavaScript file containing all compiled ActionScript classes.");
  }

  public JangarooFacetEditorTabUI() {
    outputDirTextField.addBrowseFolderListener(null,null, null, OUTPUT_DIRECTORY_CHOOSER_DESCRIPTOR,
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
    mergedOutputFileTextField.addBrowseFolderListener(null, null, null, MERGED_OUTPUT_FILE_CHOOSER_DESCRIPTOR,
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
    mergeOutputCheckBox.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        boolean selected = mergeOutputCheckBox.isSelected();
        outputDirTextField.setEnabled(!selected);
        mergedOutputFileTextField.setEnabled(selected);
      }
    });
  }

  public JPanel getRootComponent() {
    return rootComponent;
  }

/*
  // Reactivate when I found out how to deal with Disposables:
  private void createUIComponents() {
    FileTextField outputDirFileTextField = FileChooserFactory.getInstance().createFileTextField(OUTPUT_DIRECTORY_CHOOSER_DESCRIPTOR, new Disposable() {
      public void dispose() {
        // ignore
      }
    });
    //Disposer.register(this, (Disposable)outputDirFileTextField);
    outputDirTextField = new TextFieldWithBrowseButton(outputDirFileTextField.getField());
    outputDirTextField.addBrowseFolderListener(null,null, null, OUTPUT_DIRECTORY_CHOOSER_DESCRIPTOR,
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

    FileTextField mergedOutputFileFileTextField = FileChooserFactory.getInstance().createFileTextField(MERGED_OUTPUT_FILE_CHOOSER_DESCRIPTOR, new Disposable() {
      public void dispose() {
        // ignore
      }
    });
    mergedOutputFileTextField = new TextFieldWithBrowseButton(mergedOutputFileFileTextField.getField());
    mergedOutputFileTextField.addBrowseFolderListener(null, null, null, MERGED_OUTPUT_FILE_CHOOSER_DESCRIPTOR,
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
  }
  */

  public void setData(JoocConfigurationBean data) {
    verboseCheckBox.setSelected(data.verbose);
    enableAssertionsCheckBox.setSelected(data.enableAssertions);
    whiteSpaceButtonGroup.setSelected(
      (   data.isDebugSource() ? keepDebugSourceRadioButton
        : data.isDebugLines()  ? keepNewLinesOnlyRadioButton
        : suppressWhiteSpaceRadioButton).getModel(), true);
    allowDuplicateVariableCheckBox.setSelected(data.allowDuplicateLocalVariables);
    guessInheritedMembersCheckBox.setSelected(data.enableGuessingMembers);
    guessInScopeClassesCheckBox.setSelected(data.enableGuessingClasses);
    guessTypeCastsCheckBox.setSelected(data.enableGuessingTypeCasts);
    mergeOutputCheckBox.setSelected(data.mergeOutput);
    outputDirTextField.setEnabled(!data.mergeOutput);
    mergedOutputFileTextField.setEnabled(data.mergeOutput);
    outputDirTextField.setText(JoocConfigurationBean.getPath(data.outputDirectory));
    mergedOutputFileTextField.setText(JoocConfigurationBean.getPath(data.outputFileName));
    showCompilerInfoMessages.setSelected(data.showCompilerInfoMessages);
  }

  public JoocConfigurationBean getData(JoocConfigurationBean data) {
    data.verbose = verboseCheckBox.isSelected();
    data.enableAssertions = enableAssertionsCheckBox.isSelected();
    ButtonModel debugSelection = whiteSpaceButtonGroup.getSelection();
    data.debugLevel = 
        keepDebugSourceRadioButton. getModel().equals(debugSelection) ? JoocConfigurationBean.DEBUG_LEVEL_SOURCE :
        keepNewLinesOnlyRadioButton.getModel().equals(debugSelection) ? JoocConfigurationBean.DEBUG_LEVEL_LINES
                                                                      : JoocConfigurationBean.DEBUG_LEVEL_NONE;
    data.allowDuplicateLocalVariables = allowDuplicateVariableCheckBox.isSelected();
    data.enableGuessingMembers = guessInheritedMembersCheckBox.isSelected();
    data.enableGuessingClasses = guessInScopeClassesCheckBox.isSelected();
    data.enableGuessingTypeCasts = guessTypeCastsCheckBox.isSelected();
    data.mergeOutput = mergeOutputCheckBox.isSelected();
    data.outputDirectory = JoocConfigurationBean.getIdeaUrl(outputDirTextField.getText());
    data.outputFileName = JoocConfigurationBean.getIdeaUrl(mergedOutputFileTextField.getText());
    data.showCompilerInfoMessages = showCompilerInfoMessages.isSelected();
    return data;
  }

  public boolean isModified(JoocConfigurationBean data) {
    return !getData(new JoocConfigurationBean()).equals(data);
  }

}
