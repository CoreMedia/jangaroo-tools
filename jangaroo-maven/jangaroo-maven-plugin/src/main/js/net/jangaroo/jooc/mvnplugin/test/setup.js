Envjs({
  scriptTypes: {
    "": true,
    "text/javascript": true
  },
  beforeScriptLoad: {
    'sharethis': function (script) {
      script.src = '';
      return false;
    }
  },
  afterScriptLoad: {
    '.*': function (script) {
      console.log("loaded: ", script.src);
    },
    '.': function (script) {
      script.type = 'text/envjs';
    }
  }
});