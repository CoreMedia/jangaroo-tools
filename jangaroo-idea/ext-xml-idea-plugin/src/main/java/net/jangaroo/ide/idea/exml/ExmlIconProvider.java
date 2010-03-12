package net.jangaroo.ide.idea.exml;

import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Provides a custom icon for *.exml files.
 */
public class ExmlIconProvider extends IconProvider {

  public Icon getIcon(@NotNull PsiElement psiElement, int flags) {
    PsiFile containingFile = psiElement.getContainingFile();
    if (containingFile != null && containingFile.getName().endsWith(".exml")) {
      return ExmlFacetType.INSTANCE.getIcon();
    }
    return null;
  }

}
