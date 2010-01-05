package net.jangaroo.ide.idea.ui;

import net.jangaroo.ide.idea.JoocApplicationComponent;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.*;

import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.GridConstraints;

/**
 * 
 */
public class JangarooToolConfiguration implements Disposable {
  private JLabel jooHomeLabel;
  private JPanel rootComponent;
  private TextFieldWithBrowseButton jooHomeTextField;
  private FileTextField fileTextField;
  private static final FileChooserDescriptor SINGLE_FOLDER_CHOOSER_DESCRIPTOR = FileChooserDescriptorFactory.createSingleFolderDescriptor();

  public JangarooToolConfiguration() {
  }

  public JComponent getRootComponent() {
    return rootComponent;
  }

  public void setData(JoocApplicationComponent data) {
    jooHomeTextField.setText(data.getJangarooHomeDir());
  }

  public void getData(JoocApplicationComponent data) {
    data.setJangarooHomeDir(jooHomeTextField.getText());
    GridConstraints constraints = ((GridLayoutManager)rootComponent.getLayout()).getConstraintsForComponent(fileTextField.getField());
    System.out.println("constraints: "+constraints);
  }

  public boolean isModified(JoocApplicationComponent data) {
    return jooHomeTextField.getText() != null ? !jooHomeTextField.getText().equals(data.getJangarooHomeDir()) : data.getJangarooHomeDir() != null;
  }

  private void createUIComponents() {
    fileTextField = FileChooserFactory.getInstance().createFileTextField(SINGLE_FOLDER_CHOOSER_DESCRIPTOR, this);
    //Disposer.register(this, (Disposable)fileTextField);
    jooHomeTextField = new TextFieldWithBrowseButton(fileTextField.getField());
    jooHomeTextField.addBrowseFolderListener("Choose Jangaroo Home Directory", null, null,
      SINGLE_FOLDER_CHOOSER_DESCRIPTOR, TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
  }

  public void dispose() {
    //Disposer.dispose((Disposable)fileTextField);
    rootComponent = null;
    jooHomeLabel = null;
    fileTextField = null;
    jooHomeTextField = null;
  }

}
