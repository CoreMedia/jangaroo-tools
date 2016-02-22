package net.jangaroo.exml.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.io.FileUtils.listFiles;

/**
 * A Mojo to make all exml target class file with the same name as the exml file explicitly as baseClass of the exml class
 */
@Mojo(name = "exml-target-to-base",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true)
public class ExmlTargetToBaseClassMojo extends AbstractExmlMojo {

  public static final String PRIVATE_PREFIX = "private";
  public static final String PROTECTED_PREFIX = "protected";
  public static final String INTERNAL_PREFIX = "internal";
  public static final String PUBLIC_PREFIX = "public";
  public static final String STATIC_PREFIX = "static";
  public static final String FUNCTION_LOWER_CASE = "function";
  public static final String CONST = "const";
  public static final String VAR = "var";
  public static final String FUNCTION_UPPER_CASE = "Function";

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
    execute(getTestSourceDirectory());
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
          logTargetClassesWithStatics(exmlFile, baseFile);
          fixedExmls++;
        } catch (IOException e) {
          getLog().error("Fixing of class name failed", e);
        }
      }
      getLog().info("Number of fixed exml/AS pair: " + fixedExmls);
    }

  }

  //fix the class name in the base class file itself
  private void fixBaseClassName(File exmlFile, File baseFile) throws IOException {
    String baseClassContent = new String(Files.readAllBytes(baseFile.toPath()), StandardCharsets.UTF_8);
    //fix the class declaration
    String oldClassDeclarationPattern = "class\\s+" + getName(exmlFile);
    baseClassContent = baseClassContent.replaceAll(oldClassDeclarationPattern, "class " + getName(baseFile));

    //fix the constructor
    String oldConstructorPattern = "public\\s+function\\s+" + getName(exmlFile);
    baseClassContent = baseClassContent.replaceAll(oldConstructorPattern, "public function " + getName(baseFile));

    Files.write(baseFile.toPath(), baseClassContent.getBytes(StandardCharsets.UTF_8));
    getLog().info("baseClass name fixed in : " + baseFile.getAbsolutePath());
  }


  //add the baseClass declaration in the exml
  private void addBaseClassDeclaration(File exmlFile, File baseFile) throws IOException {
    String exmlContent = new String(Files.readAllBytes(exmlFile.toPath()), StandardCharsets.UTF_8);
    int i = exmlContent.indexOf("<exml:");
    int j = exmlContent.indexOf(">", i);

    String exmlBefore = exmlContent.substring(0, i);

    String exmlDeclaration = exmlContent.substring(i, j + 1);
    //remove the baseClass declaration (unnecessary but there are such codes, e.g. PremularBase.exml)
    if (exmlDeclaration.indexOf("baseClass") > 0) {
      exmlDeclaration = exmlDeclaration.replaceAll("baseClass[\\s\\S]*?\"[\\s\\S]*?\"[\r\n\\s]*", "");
    }

    String exmlAfter = exmlContent.substring(j + 1);

    if (exmlDeclaration.endsWith("/>")) {
      exmlDeclaration = exmlDeclaration.substring(0, exmlDeclaration.length() - 2);
      exmlDeclaration += "\r\nbaseClass=\"" + getName(baseFile) + "\"/>";
    } else {
      exmlDeclaration = exmlDeclaration.substring(0, exmlDeclaration.length() - 1);
      exmlDeclaration += "\r\nbaseClass=\"" + getName(baseFile) + "\">";
    }

    exmlContent = exmlBefore + exmlDeclaration + exmlAfter;
    Files.write(exmlFile.toPath(), exmlContent.getBytes(StandardCharsets.UTF_8));
    getLog().info("baseClass declaration added to: " + exmlFile.getAbsolutePath());
  }


  private void logTargetClassesWithStatics(File exmlFile, File baseFile) throws IOException {
    List<String> baseClassLines = Files.readAllLines(baseFile.toPath(), StandardCharsets.UTF_8);
    List<String> staticList = new ArrayList<String>();
    for (String line : baseClassLines) {
      String staticName = null;
      String staticType = null;
      List<String> tokens = getRelevantTokens(line);
      if (tokens.isEmpty()) {
        continue;
      }
      if (tokens.get(0).equals(FUNCTION_LOWER_CASE)) {
        staticName = tokens.get(1).split("\\(")[0];
        staticType = FUNCTION_UPPER_CASE;
      } else if (tokens.get(0).equals(CONST) || tokens.get(0).equals(VAR)) {
        String[] constNameAndTypeTokens = tokens.get(1).split(":");
        staticName = constNameAndTypeTokens[0];
        staticType = constNameAndTypeTokens.length > 1 ? constNameAndTypeTokens[1] : "Object";
        //handle "public static const bla:blub=" (without space between type and "=")
        staticType = staticType.split("=")[0];
        //handle "public static var bla:blub; (without value)
        staticType = staticType.split(";")[0];
      }
      if (staticName != null) {
        staticList.add(staticType + " " + staticName);
      }

    }

    if (!staticList.isEmpty()) {
      getLog().info("The renamed target file " + baseFile.getAbsolutePath() + " has static members: " + staticList);
    }
  }

  /**
   * get all strings after "function" or "const" including "function" or "const"
   * empty list if the visibility is "private"
   *
   * @param line the given line
   * @return empty list if the visibility is "private"
   */
  private List<String> getRelevantTokens(String line) {
    String cleanLine = line.trim().replaceAll("\\s+", " ");
    //what if "public static const bla : blub" (space before ":" and after)?
    cleanLine = cleanLine.replaceAll(" :", ":");
    cleanLine = cleanLine.replaceAll(" :", ":");

    List<String> lineTokens = new LinkedList<String>(Arrays.asList(cleanLine.split(" ")));
    if (lineTokens.contains(PRIVATE_PREFIX) || !lineTokens.contains(STATIC_PREFIX)) {
      return Collections.emptyList();
    }
    lineTokens.remove(PUBLIC_PREFIX);
    lineTokens.remove(PROTECTED_PREFIX);
    lineTokens.remove(INTERNAL_PREFIX);
    lineTokens.remove(STATIC_PREFIX);
    return lineTokens;
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
