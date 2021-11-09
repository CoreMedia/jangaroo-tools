module.exports = {
  "env": {
    "browser": true,
    "es2021": true
  },
  "parser": "@typescript-eslint/parser",
  "parserOptions": {
    "ecmaVersion": 2021,
    "sourceType": "module"
  },
  "plugins": [
    "@typescript-eslint",
    "import",
    "jsdoc",
    "unused-imports",
  ],
  "rules": {
    "@typescript-eslint/brace-style": "error",
    "@typescript-eslint/comma-dangle": [
      "error",
      "always-multiline"
    ],
    "@typescript-eslint/comma-spacing": "error",
    "@typescript-eslint/object-curly-spacing": ["error", "always"],
    "@typescript-eslint/func-call-spacing": "error",
    "@typescript-eslint/keyword-spacing": "error",
    "@typescript-eslint/lines-between-class-members": "error",
    "@typescript-eslint/quotes": [
      "error",
      "double"
    ],
    "@typescript-eslint/space-before-function-paren": ["error", {
      "anonymous": "never",
      "named": "never",
      "asyncArrow": "always",
    }],
    "@typescript-eslint/space-infix-ops": "error",
    "@typescript-eslint/type-annotation-spacing": "error",
    "space-before-blocks": "error",
    "array-bracket-newline": ["error", "consistent"],
    "arrow-parens": ["error", "as-needed"],
    "prefer-const": "error",
    "import/no-duplicates": "error",
    "import/newline-after-import": [
      "error", {"count": 1}
    ],
    "import/order": ["error", {
      "alphabetize": {
        "order": "asc"
      },
    }],
    "indent": ["error", 2],
    "jsdoc/check-alignment": [
      "error"
    ],
    "key-spacing": ["error", {
      "mode": "strict",
    }],
    "linebreak-style": [
      "error",
      "unix"
    ],
    "no-mixed-spaces-and-tabs": "error",
    "no-multiple-empty-lines": ["error", {
      "max": 1
    }],
    "no-multi-spaces": "error",
    "no-trailing-spaces": "error",
    "no-var": "error",
    "no-whitespace-before-property": "error",
    "object-curly-newline": ["error", {
      "multiline": true
    }],
    "object-property-newline": "error",
    "padding-line-between-statements": "off",
    "@typescript-eslint/padding-line-between-statements": [
      "error",
      {
        "blankLine": "always",
        "prev": ["class", "interface", "const"],
        "next": ["class", "interface"],
      },
      {
        "blankLine": "always",
        "prev": "*",
        "next": "export",
      }
    ],
    "semi": [
      "error",
      "always"
    ],
    "space-in-parens": "error",
    "unused-imports/no-unused-imports": "error",
  }
};
