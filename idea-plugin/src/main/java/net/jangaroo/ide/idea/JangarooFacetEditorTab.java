package net.jangaroo.ide.idea;

import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.IconLoader;
import net.jangaroo.ide.idea.ui.JangarooFacetEditorTabUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Jangaroo Facet settings (Editor Tab).
 */
public class JangarooFacetEditorTab extends FacetEditorTab {
  private JoocConfigurationBean joocConfigurationBean;
  private JangarooFacetEditorTabUI form;


  public JangarooFacetEditorTab(JoocConfigurationBean joocConfigurationBean) {
    this.joocConfigurationBean = joocConfigurationBean;
  }

  @Nls
  public String getDisplayName() {
    return "Jangaroo !";
  }

  public Icon getIcon() {
    return IconLoader.getIcon(JangarooFacetType.JANGAROO_FACET_ICON_URL);
  }

  public String getHelpTopic() {
    return null;
  }

  public JComponent createComponent() {
    if (form==null) {
      form = new JangarooFacetEditorTabUI();
    }
    return form.getRootComponent();
  }

  public boolean isModified() {
    return form!=null && form.isModified(joocConfigurationBean);
  }

  public void apply() throws ConfigurationException {
    if (form!=null) {
      form.getData(joocConfigurationBean);
    }
  }

  public void reset() {
    if (form!=null) {
      form.setData(joocConfigurationBean);
    }
  }

  public void disposeUIResources() {
    //Disposer.dispose((Disposable)form);
    form = null;
  }

  public JoocConfigurationBean getJoocConfigurationBean() {
    return joocConfigurationBean;
  }
}
