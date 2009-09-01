package net.jangaroo.extxml;

import net.jangaroo.extxml.util.FileScanner;
import net.jangaroo.extxml.util.Rule;
import net.jangaroo.extxml.util.TidyComment;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A FileScanner to read Ext JS component classes (JavaScript or ActionScript) into a {@link ComponentSuite}.
 */
public class ExtComponentSrcFileScanner {

  public static void scan(ComponentSuite componentSuite, File srcFile) {
    State state = new State(componentSuite, srcFile);
    try {
      if (srcFile.getName().lastIndexOf(".as") != -1) {
        String className = FileUtils.removeExtension(srcFile.getName());
        state.addClass(className);
        EXT_COMPONENT_AS_FILE_SCANNER.scan(srcFile, state);
      } else {
        EXT_COMPONENT_SRC_FILE_SCANNER.scan(srcFile, state);
      }
      state.end();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static FileScanner<State> EXT_COMPONENT_AS_FILE_SCANNER = new FileScanner<State>()
      .add(new Rule<State>("package\\s+([a-zA-Z0-9$_.]+)") {
        public void matched(State state, List<String> groups) {
          state.cc.setFullClassName(groups.get(0)+"."+state.cc.getClassName());
        }
      })
      .add(new Rule<State>("import\\s+([a-zA-Z0-9$_.]+);") {
        public void matched(State state, List<String> groups) {
          state.addImport(groups.get(0));
        }
      })
      .add(new Rule<State>("@xtype\\s+([a-zA-Z.]+)") {
        public void matched(State state, List<String> groups) {
          state.setXtype(groups.get(0), state.cc.getFullClassName());
        }
      })
      .add(new Rule<State>("@cfg\\s+[{]?([a-zA-Z0-9$_./]+)[}]? ([a-zA-Z0-9$_]+)(.*)$") {
        public void matched(State state, List<String> groups) {
          // use List#remove(0) to skip optional type if missing:
          state.addCfg(groups.size() == 3 ? groups.remove(0) : "*", groups.remove(0), groups.remove(0));
        }
      })
      .add(new Rule<State>("extends\\s+([a-zA-Z0-9$_.]+)") {
        public void matched(State state, List<String> groups) {
          state.setExtends(groups.get(0));
        }
      });

  private static FileScanner<State> EXT_COMPONENT_SRC_FILE_SCANNER = new FileScanner<State>()
      .add(new Rule<State>("@class\\s+([a-zA-Z0-9$_.]+)") {
        public void matched(State state, List<String> groups) {
          state.addClass(groups.get(0));
        }
      })
      .add(new Rule<State>("@extends\\s+([a-zA-Z0-9$_.]+)") {
        public void matched(State state, List<String> groups) {
          state.setExtends(groups.get(0));
        }
      })
      .add(new Rule<State>("@xtype\\s+([a-zA-Z.]+)") {
        public void matched(State state, List<String> groups) {
          state.setXtype(groups.get(0), state.cc.getFullClassName());
        }
      })
      .add(new Rule<State>("\\bExt.reg\\('([a-zA-Z.]+)',\\s*([a-zA-Z0-9$_.]+)\\);") {
        public void matched(State state, List<String> groups) {
          // old-style xtype registration, still used e.g. in Ext.Component.js:
          state.setXtype(groups.get(0), groups.get(1));
        }
      })
      .add(new Rule<State>("@cfg\\s+[{]?([a-zA-Z0-9$_./]+)[}]? ([a-zA-Z0-9$_]+)(.*)$") {
        public void matched(State state, List<String> groups) {
          // use List#remove(0) to skip optional type if missing:
          state.addCfg(groups.size() == 3 ? groups.remove(0) : "*", groups.remove(0), groups.remove(0));
        }
      })
      .add(new Rule<State>("\\*/|@constructor") {
        public void matched(State state, List<String> groups) {
          state.endComment();
        }
      })
      .add(new Rule<State>("^\\s*\\*? ?(.*)$") {
        // inside comment: skip leading white space before first '*':
        public void matched(State state, List<String> groups) {
          state.addComment(groups.get(0));
        }
      });

  private static class State {
    State(ComponentSuite componentSuite, File srcFile) {
      this.componentSuite = componentSuite;
      this.srcFile = srcFile;
    }

    private void addClass(String className) {
      addIfHasXtype(cc);
      cc = new ComponentClass(srcFile);
      cc.setFullClassName(jsType2asType(className));
      setDescriptionHolder(cc);
    }

    private void addImport(String className) {
      cc.addImport(className);
    }

    private void setExtends(String superClassName) {
      if (isActionScript()) {
        String fullClassName = "";
        for (String imp : cc.getImports()) {
          if (imp.endsWith(superClassName))
            fullClassName = imp;
        }
        cc.setSuperClassName(fullClassName);
      } else {
        cc.setSuperClassName(jsType2asType(superClassName));
      }
    }

    public void setXtype(String xtype, String className) {
      if (cc.getXtype() == null && jsType2asType(className).equals(cc.getFullClassName())) {
        cc.setXtype(xtype);
      }
    }

    private void addCfg(String type, String name, String descriptionLine) {
      ConfigAttribute configAttribute = new ConfigAttribute(name, type);
      cc.addCfg(configAttribute);
      setDescriptionHolder(configAttribute);
      if (descriptionLine.length() > 0) { // suppress first empty line
        description.append(descriptionLine).append('\n');
      }
    }

    private void endComment() {
      setDescriptionHolder(null);
    }

    private void addComment(String comment) {
      if (descriptionHolder != null) {
        description.append(comment).append('\n');
      }
    }

    void end() {
      addIfHasXtype(cc);
    }

    private String jsType2asType(String jsType) {
      int lastDotPos = jsType.lastIndexOf('.');
      return lastDotPos == -1
          ? jsType
          : jsType.substring(0, lastDotPos).toLowerCase() + jsType.substring(lastDotPos);
    }

    private void addIfHasXtype(ComponentClass cc) {
      if (cc != null && cc.getXtype() != null) {
        componentSuite.addComponentClass(cc);
      }
    }

    private void setDescriptionHolder(DescriptionHolder nextDescriptionHolder) {
      if (nextDescriptionHolder != descriptionHolder) {
        if (descriptionHolder != null) {
          descriptionHolder.setDescription(TidyComment.tidy(description.toString()));
          description = new StringBuilder();
        }
        descriptionHolder = nextDescriptionHolder;
      }
    }

    private boolean isActionScript() {
      return srcFile.getName().lastIndexOf(".as") != -1;
    }

    private ComponentSuite componentSuite;
    private File srcFile;
    private ComponentClass cc;
    private StringBuilder description = new StringBuilder();
    private DescriptionHolder descriptionHolder;
  }
}
