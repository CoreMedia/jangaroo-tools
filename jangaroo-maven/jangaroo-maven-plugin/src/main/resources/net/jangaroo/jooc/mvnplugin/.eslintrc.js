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
    "padding"
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
    "array-bracket-newline": ["error", "consistent"],
    "arrow-parens": ["error", "as-needed"],
    "import/newline-after-import": [
      "error", {"count": 1}
    ],
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
    "no-whitespace-before-property": "error",
    "object-curly-newline": ["error", {
      "multiline": true
    }],
    "object-property-newline": ["error", {"allowAllPropertiesOnSameLine": true}],
    "padding/spacing": [
      "error",
      {
        "blankLine": "always",
        "prev": ["abstract class", "class", "interface", "declare namespace", "const"],
        "next": ["abstract class", "class", "interface", "declare namespace", "export default"],
      },
    ],
    "semi": [
      "error",
      "always"
    ],
    "space-in-parens": "error",
  }
};