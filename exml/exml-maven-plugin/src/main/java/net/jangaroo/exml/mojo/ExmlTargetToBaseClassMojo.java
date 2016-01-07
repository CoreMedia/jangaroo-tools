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
 * A Mojo to make all exml target class explicitly as baseClass.
 *
 */
public class ExmlTargetToBaseClassMojo extends AbstractExmlMojo {

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
        final String exmlName = exmlFile.getName().split("\\.")[0];
        //check if there is a as file in the same directory as the exml file
        File[] files = exmlFile.getParentFile().listFiles(new FilenameFilter() {
          String actionScriptFileName = exmlName + ".as";
          @Override
          public boolean accept(File dir, String name) {
            return actionScriptFileName.equals(name);
          }
        });

        if (files.length == 1) {
          File actionScriptFile = files[0];
          getLog().info("AS file with same name found as " + exmlFile.getAbsolutePath() + ", " +  actionScriptFile.getAbsolutePath());
          File baseClassFile = new File(actionScriptFile.getAbsolutePath().replace(".as", "Base.as"));
          if (actionScriptFile.renameTo(baseClassFile)) {
            getLog().info("Renamed to " + baseClassFile.getAbsolutePath());
            //fix now the class name in the base class file itself
            try {
              String baseClassContent = new String(Files.readAllBytes(baseClassFile.toPath()), StandardCharsets.UTF_8);

              //fix the class declaration
              String oldClassDeclarationPattern = "public\\s+class\\s+" +  exmlName;
              String newBaseClassName = baseClassFile.getName().split("\\.")[0];
              baseClassContent = baseClassContent.replaceAll(oldClassDeclarationPattern, "public class " + newBaseClassName);

              //fix the constructor
              String oldConstructorPattern = "public\\s+function\\s+" +  exmlName;
              baseClassContent = baseClassContent.replaceAll(oldConstructorPattern, "public function " + newBaseClassName);

              Files.write(baseClassFile.toPath(), baseClassContent.getBytes(StandardCharsets.UTF_8));
              getLog().info("Class name fixed: " + baseClassFile.getAbsolutePath());

              //the exml needs now the baseClass declaration in the exml
              String exmlContent = new String(Files.readAllBytes(exmlFile.toPath()), StandardCharsets.UTF_8);
              String exmlRootPattern = ".*(<exml[^>]*)(.*)";
              exmlContent = exmlContent.replaceFirst(exmlRootPattern, "$1\r\nbaseClass=\"" + newBaseClassName + "\"$2");

              Files.write(exmlFile.toPath(), exmlContent.getBytes(StandardCharsets.UTF_8));
              getLog().info("baseClass fixed: " + exmlFile.getAbsolutePath());
              fixedExmls++;
            } catch (IOException e) {
              getLog().error("Fixing of class name failed", e);
            }
          } else {
            getLog().error("Renaming failed to " + baseClassFile.getAbsolutePath());
          };
        } else  if (files.length > 1) {
          getLog().warn("There is more than one AS file with same name in the directory like " + exmlFile.getAbsolutePath());
        }
      }
      getLog().info("Number of fixed exml/AS pair: " + fixedExmls);
    }

  }

  public static void main(String[] args) {
    if (args.length > 0) {
      String sourceDirPath = args[0];
      File sourceDir = new File(sourceDirPath);
      new ExmlTargetToBaseClassMojo().execute(sourceDir);
    }
  }
}
