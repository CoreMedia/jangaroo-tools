package net.jangaroo.extxml.file;

import net.jangaroo.extxml.model.ComponentClass;
import net.jangaroo.extxml.model.ComponentSuite;
import net.jangaroo.extxml.model.ComponentType;
import net.jangaroo.extxml.model.ConfigAttribute;
import net.jangaroo.extxml.model.DescriptionHolder;
import net.jangaroo.utils.log.Log;
import net.jangaroo.extxml.util.FileScanner;
import net.jangaroo.extxml.util.Rule;
import net.jangaroo.extxml.util.TidyComment;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A FileScanner to read Ext JS component classes (JavaScript or ActionScript) into a {@link net.jangaroo.extxml.model.ComponentSuite}.
 */
public final class ExtComponentSrcFileScanner {

  private ExtComponentSrcFileScanner() {
    //hide the constructor  
  }

  public static void scan(ComponentSuite componentSuite, File srcFile, ComponentType type) throws IOException {
    State state = new State(componentSuite, srcFile);
    if (ComponentType.ActionScript.equals(type)) {
      String className = FileUtils.removeExtension(srcFile.getName());
      state.addClass(className);
      state.cc.setType(ComponentType.ActionScript);
      Log.d(String.format("Parsing AS3 class '%s'", className));
      EXT_COMPONENT_AS_FILE_SCANNER.scan(srcFile, state);
    } else if (ComponentType.JavaScript.equals(type)) {
      EXT_COMPONENT_SRC_FILE_SCANNER.scan(srcFile, state);
      if (state.cc != null) {
        state.cc.setType(ComponentType.JavaScript);
      }
    }
    state.end();

  }

  // Rules used in both scanners:

  private static final Rule<State> TYPE_RULE = new Rule<State>("@[apx]type\\s+([\\p{Alpha}$_.][\\p{Alnum}$_.]*)") {
    public void matched(State state, List<String> groups) {
      state.setXtype(groups.get(0), state.cc.getFullClassName());
    }
  };
  private static final Rule<State> CFG_RULE = new Rule<State>("@cfg\\s+[{]?([\\p{Alnum}$_./|]+)[}]?\\s+([\\p{Alnum}$_]+)(.*)$") {
    public void matched(State state, List<String> groups) {
      // use List#remove(0) to skip optional type if missing:
      state.addCfg(groups.size() == 3 ? groups.remove(0) : "*", groups.remove(0), groups.remove(0));
    }
  };
  private static final Rule<State> COMMENT_START_RULE = new Rule<State>("^\\s*/\\*\\* ?(.*)$") {
    public void matched(State state, List<String> groups) {
      state.startComment(groups.get(0));
    }
  };
  private static final Rule<State> COMMENT_RULE = new Rule<State>("^\\s*\\*? ?(.*)$") {
    // inside comment: skip leading white space before first '*':
    public void matched(State state, List<String> groups) {
      state.addComment(groups.get(0));
    }
  };
  private static final Rule<State> COMMENT_END_RULE = new Rule<State>("\\*/|@constructor") {
    public void matched(State state, List<String> groups) {
      state.endComment();
    }
  };

  private final static FileScanner<State> EXT_COMPONENT_AS_FILE_SCANNER = new FileScanner<State>()
      .add(new Rule<State>("^\\s*package\\s+([\\p{Alnum}$_.]+)") {
        public void matched(State state, List<String> groups) {
          state.cc.setFullClassName(groups.get(0) + "." + state.cc.getClassName());
          // Next comment following the package declaration is the class comment:
          state.setDescriptionHolder(state.cc);
        }
      })
      .add(new Rule<State>("^\\s*import\\s+([\\p{Alnum}$_.]+);") {
        public void matched(State state, List<String> groups) {
          state.addImport(groups.get(0));
        }
      })
      .add(new Rule<State>("\\bextends\\s+([\\p{Alnum}$_.]+)") {
        public void matched(State state, List<String> groups) {
          state.setExtends(groups.get(0));
        }
      })
      .add(new Rule<State>("(?:public\\s+static|static\\s+public)\\s+const\\s+[apx]type\\s*:\\s*String\\s*=\\s*['\"]([^'\"]+)['\"]") {
        public void matched(State state, List<String> groups) {
          state.setXtype(groups.get(0), state.cc.getFullClassName());
        }
      })
      .add(CFG_RULE)
      .add(COMMENT_END_RULE)
      .add(new Rule<State>("^?\\s*/\\*\\*?(.*)$") {
        public void matched(State state, List<String> groups) {
          state.startComment(groups.get(0));
        }
      })
      .add(COMMENT_RULE);

  private final static FileScanner<State> EXT_COMPONENT_SRC_FILE_SCANNER = new FileScanner<State>()
      .add(new Rule<State>("@class\\s+([\\p{Alnum}$_.]+)") {
        public void matched(State state, List<String> groups) {
          state.addClass(groups.get(0));
        }
      })
      .add(new Rule<State>("@extends\\s+([\\p{Alnum}$_.]+)") {
        public void matched(State state, List<String> groups) {
          state.setExtends(groups.get(0));
        }
      })
      .add(new Rule<State>("\\bExt\\.reg\\('([\\p{Alnum}$_.]+)',\\s*([\\p{Alnum}$_.]+)\\);") {
        public void matched(State state, List<String> groups) {
          // old-style xtype registration, still used e.g. in Ext.Component.js:
          state.setXtype(groups.get(0), groups.get(1));
        }
      })
      .add(new Rule<State>("\\bExt\\.Container\\.LAYOUTS\\['([\\p{Alnum}$_.]+)'\\]\\s*=\\s*([\\p{Alnum}$_.]+);") {
        public void matched(State state, List<String> groups) {
          // layout type registration using LAYOUTS['type']:
          state.setXtype(groups.get(0) + "layout", groups.get(1));
        }
      })
      .add(new Rule<State>("\\bExt\\.Container\\.LAYOUTS\\.([\\p{Alnum}$_.]+)\\s*=\\s*([\\p{Alnum}$_.]+);") {
        public void matched(State state, List<String> groups) {
          // layout type registration using LAYOUTS.type:
          state.setXtype(groups.get(0) + "layout", groups.get(1));
        }
      })
      .add(TYPE_RULE)
      .add(CFG_RULE)
      .add(COMMENT_END_RULE)
      .add(COMMENT_START_RULE)
      .add(COMMENT_RULE);

  private static class State {

    private final static String[] SPECIAL_EXT_CLASS_TO_XTYPE = {
      "Ext.grid.Column",         "gridcolumn",
      "Ext.grid.BooleanColumn",  "booleancolumn",
      "Ext.grid.NumberColumn",   "numbercolumn",
      "Ext.grid.DateColumn",     "datecolumn",
      "Ext.grid.TemplateColumn", "templatecolumn",
      "Ext.util.Observable",     "observable",
      "Ext.data.Field",          "datafield",
      // "abstract" layouts:
      "Ext.layout.ContainerLayout", "containerlayout",
      "Ext.layout.BoxLayout",       "boxlayout",
      // Ext.layout.Container.LAYOUTS setting comes too late for this class, so we have to assign it manually:
      "Ext.layout.BorderLayout",    "borderlayout",
      // Ext's built-in Action class:
      "Ext.Action",                 "action"
    };

    private ComponentSuite componentSuite;
    private File srcFile;
    private ComponentClass cc;
    private boolean insideComment;
    private StringBuilder description = new StringBuilder();
    private DescriptionHolder descriptionHolder;

    State(ComponentSuite componentSuite, File srcFile) {
      this.componentSuite = componentSuite;
      this.srcFile = srcFile;
    }

    private void addClass(String className) {
      addIfHasXtype(cc);
      cc = new ComponentClass(srcFile);
      cc.setFullClassName(jsType2asType(className));
      setDescriptionHolder(cc);
      // special cases: xtypes for certain Ext classes that have implicit xtypes
      // or provide config attributes for subclasses, or are added as EXML elements:
      for (int i = 0; i < SPECIAL_EXT_CLASS_TO_XTYPE.length; i+=2) {
        if (SPECIAL_EXT_CLASS_TO_XTYPE[i].equals(className)) {
          cc.setXtype(SPECIAL_EXT_CLASS_TO_XTYPE[i+1]);
          break;
        }
      }
    }

    private void addImport(String className) {
      cc.addImport(className);
    }

    private void setExtends(String superClassName) {
      if (isActionScript()) {
        String fullClassName = "";
        for (String imp : cc.getImports()) {
          if (imp.endsWith(superClassName)) {
            fullClassName = imp;
          }
        }
        cc.setSuperClassName(fullClassName);
      } else if (cc != null) {
        cc.setSuperClassName(jsType2asType(superClassName));
      }
    }

    public void setXtype(String xtype, String className) {
      if (cc != null && cc.getXtype() == null && jsType2asType(className).equals(cc.getFullClassName())) {
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

    private void startComment(String comment) {
      if (!insideComment) {
        insideComment = true;
        addComment(comment);
      }
    }

    private void endComment() {
      if (insideComment) {
        setDescriptionHolder(null);
        insideComment = false;
      }
    }

    private void addComment(String comment) {
      if (insideComment) {
        description.append(comment).append('\n');
      }
    }

    void validateComponentClass(ComponentClass cc) {
      if (cc != null) {
        if (cc.getImports().isEmpty()) {
          Log.w("No imports in Compontent class");
        }
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
      if (cc != null) {
        if (cc.getXtype() != null) {
          validateComponentClass(cc);
          componentSuite.addComponentClass(cc);
          Log.i(String.format("Component class '%s' with xtype '%s' parsed.", cc.getFullClassName(), cc.getXtype()));
        } else {
          Log.d(String.format("Class '%s' has no xtype - skipped.", cc.getFullClassName()));
        }
      }
    }

    private void setDescriptionHolder(DescriptionHolder nextDescriptionHolder) {
      if (nextDescriptionHolder != descriptionHolder) {
        String cleanedDescription = TidyComment.tidy(description.toString()).trim();
        if (descriptionHolder != null && cleanedDescription.length() > 0) {
          descriptionHolder.setDescription(cleanedDescription);
        }
        description = new StringBuilder();
        descriptionHolder = nextDescriptionHolder;
      }
    }

    private boolean isActionScript() {
      return srcFile.getName().lastIndexOf(".as") != -1;
    }
  }
}
