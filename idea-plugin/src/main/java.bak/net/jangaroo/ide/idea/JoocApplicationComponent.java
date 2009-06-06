package net.jangaroo.ide.idea;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.*;
import net.jangaroo.ide.idea.ui.JangarooToolConfiguration;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * An application component for the Jangaroo Compiler "jooc".
 */
//@State(name="Jangaroo IDEA Plugin", storages={ @Storage(id="other", file = "$APP_CONFIG$" )}) //"$OPTIONS$"
public class JoocApplicationComponent implements ApplicationComponent, Configurable, JDOMExternalizable { // PersistentStateComponent<JoocApplicationComponent> {

  private String jangarooHomeDir;
  private transient JangarooToolConfiguration form;
  private static final String JANGAROO_LARGE_ICON_URL = "/net/jangaroo/jooley-48x48.png";

  public JoocApplicationComponent() {
  }

  public void initComponent() {
    // TODO: insert component initialization logic here
  }

  public void disposeComponent() {
    // TODO: insert component disposal logic here
  }

  @NotNull
  public String getComponentName() {
    return "JoocApplicationComponent";
  }

  @Nls
  public String getDisplayName() {
    return "Jangaroo Settings";
  }

  public Icon getIcon() {
    return IconLoader.getIcon(JANGAROO_LARGE_ICON_URL);
  }

  public String getHelpTopic() {
    return null;
  }

  public JComponent createComponent() {
    if (form==null) {
      form = new JangarooToolConfiguration();
    }
    return form.getRootComponent();
  }

  public boolean isModified() {
    return form!=null && form.isModified(this);
  }

  public void apply() throws ConfigurationException {
    if (form!=null) {
      form.getData(this);
    }
  }

  public void reset() {
    if (form!=null) {
      form.setData(this);
    }
  }

  public void disposeUIResources() {
    Disposer.dispose(form);
    form = null;
  }

  public String getJangarooHomeDir() {
    return jangarooHomeDir;
  }

  public void setJangarooHomeDir(String jangarooHomeDir) {
    this.jangarooHomeDir = jangarooHomeDir;
  }

  /*
  public JoocApplicationComponent getState() {
    return this;
  }

  public void loadState(JoocApplicationComponent state) {
    form = null;
    jangarooHomeDir = state.jangarooHomeDir;
  }
  */
  public void readExternal(Element element) throws InvalidDataException {
    //JDOMClassExternalizer.readExternal(this, element);
  }

  public void writeExternal(Element element) throws WriteExternalException {
    //JDOMClassExternalizer.writeExternal(this, element);
  }
}
