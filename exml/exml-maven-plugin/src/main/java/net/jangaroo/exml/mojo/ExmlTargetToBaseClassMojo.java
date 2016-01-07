package net.jangaroo.exml.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.apache.commons.io.FileUtils.listFiles;

/**
 * A Mojo to make all exml target class file with the same name as the exml file explicitly as baseClass of the exml class
 */
public class ExmlTargetToBaseClassMojo extends AbstractExmlMojo {

  public static void main(String[] args) {
    if (args.length > 0) {
      String sourceDirPath = args[0];
      File sourceDir = new File(sourceDirPath);
      new ExmlTargetToBaseClassMojo().execute(sourceDir);
    }
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!isExmlProject()) {
      getLog().info("not an EXML project, skipping base class conversion");
      return;
    }

    execute(getSourceDirectory());
  }

  private void execute(File sourceDir) {
    if (sourceDir != null && sourceDir.exists()) {
      int fixedExmls = 0;
      for (final File exmlFile : listFiles(sourceDir, new String[]{"exml"}, true)) {
        File targetFile = findTargetFileWithSameName(exmlFile);
        if (targetFile == null) {
          continue; //no target file with same name found
        }

        File baseFile = renameTargetToBase(targetFile);
        if (baseFile == null) {
          continue; //rename failed
        }

        try {
          fixBaseClassName(exmlFile, baseFile);
          addBaseClassDeclaration(exmlFile, baseFile);
          fixedExmls++;
        } catch (IOException e) {
          getLog().error("Fixing of class name failed", e);
        }
      }
      getLog().info("Number of fixed exml/AS pair: " + fixedExmls);
    }

  }

  //add the baseClass declaration in the exml
  private void addBaseClassDeclaration(File exmlFile, File baseFile) throws IOException {
    String exmlContent = new String(Files.readAllBytes(exmlFile.toPath()), StandardCharsets.UTF_8);
    String exmlRootPattern = ".*(<exml[^>]*)(.*)";
    exmlContent = exmlContent.replaceFirst(exmlRootPattern, "$1\r\nbaseClass=\"" + getName(baseFile) + "\"$2");

    Files.write(exmlFile.toPath(), exmlContent.getBytes(StandardCharsets.UTF_8));
    getLog().info("baseClass declaration added to: " + exmlFile.getAbsolutePath());
  }

  //fix the class name in the base class file itself
  private void fixBaseClassName(File exmlFile, File baseFile) throws IOException {
    String baseClassContent = new String(Files.readAllBytes(baseFile.toPath()), StandardCharsets.UTF_8);
    //fix the class declaration
    String oldClassDeclarationPattern = "public\\s+class\\s+" + getName(exmlFile);
    baseClassContent = baseClassContent.replaceAll(oldClassDeclarationPattern, "public class " + getName(baseFile));

    //fix the constructor
    String oldConstructorPattern = "public\\s+function\\s+" + getName(exmlFile);
    baseClassContent = baseClassContent.replaceAll(oldConstructorPattern, "public function " + getName(baseFile));

    Files.write(baseFile.toPath(), baseClassContent.getBytes(StandardCharsets.UTF_8));
    getLog().info("baseClass name fixed in : " + baseFile.getAbsolutePath());
  }

  private File findTargetFileWithSameName(final File exmlFile) {
    File[] files = exmlFile.getParentFile().listFiles(new FilenameFilter() {
      //check if there is a as file in the same directory as the exml file
      String targetFileName = getName(exmlFile) + ".as";

      @Override
      public boolean accept(File dir, String name) {
        return targetFileName.equals(name);
      }
    });

    if (files.length == 1) {
      getLog().info(exmlFile.getName() + " and " + files[0].getName() + " found in " + exmlFile.getParentFile().getAbsolutePath());
      return files[0];
    } else {
      if (files.length > 1) {
        getLog().warn("There is more than one target AS file with same name in the directory like " + exmlFile.getAbsolutePath());
      }
      return null;
    }
  }

  private File renameTargetToBase(File targetFile) {
    File baseClassFile = new File(targetFile.getAbsolutePath().replace(".as", "Base.as"));
    if (targetFile.renameTo(baseClassFile)) {
      getLog().info("Target file " + targetFile.getName() + " is renamed to " + baseClassFile.getName() + " in " +
      baseClassFile.getParent());
      return baseClassFile;
    } else {
      getLog().error("Renaming to " + baseClassFile.getAbsolutePath() + " failed");
      return null;
    }
  }

  private String getName(File file) {
    String[] nameAndExtension = file.getName().split("\\.");
    if (nameAndExtension.length != 2) {
      getLog().warn("Cannot compute the name of the file: " + file.getAbsolutePath());
      return null;
    }
    return nameAndExtension[0];
  }
}
