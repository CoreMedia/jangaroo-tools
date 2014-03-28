joo.classLoader.prepare("package js",/* {*/
"public interface Location",1,function($$private){;return[ /*

  /**
   * The part of the URL that follows the # symbol, including the # symbol.
   * /
  function hash()     :String;*/,/*
  /**
   * The part of the URL that follows the # symbol, including the # symbol.
   * /
  function hash(value:String):void;*/,/*
  /**
   * The host name and port number.
   * /
  function host()     :String;*/,/*
  /**
   * The host name and port number.
   * /
  function host(value:String):void;*/,/*
  /**
   * The host name (without the port number or square brackets).
   * /
  function hostname() :String;*/,/*
  /**
   * The host name (without the port number or square brackets).
   * /
  function hostname(value:String):void;*/,/*
  /**
   * The entire URL.
   * /
  function href()     :String;*/,/*
  /**
   * The entire URL.
   * /
  function href(value:String):void;*/,/*
  /**
   * The path (relative to the host).
   * /
  function pathname() :String;*/,/*
  /**
   * The path (relative to the host).
   * /
  function pathname(value:String):void;*/,/*
  /**
   * The port number of the URL.
   * /
  function port()     :String;*/,/*
  /**
   * The port number of the URL.
   * /
  function port(value:String):void;*/,/*
  /**
   * The protocol of the URL.
   * /
  function protocol() :String;*/,/*
  /**
   * The protocol of the URL.
   * /
  function protocol(value:String):void;*/,/*
  /**
   * The part of the URL that follows the ? symbol, including the ? symbol.
   * /
  function search()   :String;*/,/*
  /**
   * The part of the URL that follows the ? symbol, including the ? symbol.
   * /
  function search(value:String):void;*/,/*

  /**
   * Load the document at the provided URL.
   * /
  function assign(url:String):void;*/,/*

  /**
   * Reload the document from the current URL.
   * @param forceGet when true, causes the page to always be reloaded from the server. If false or not specified, the
   *   browser may reload the page from its cache.
   * /
  function reload(forceGet:Boolean = false):void;*/,/*

  /**
   * Replace the current document with the one at the provided URL. The difference from the assign() method is that
   * after using replace() the current page will not be saved in session history, meaning the user won't be able to use
   * the Back button to navigate to it.
   * @param url the new URL to load the document from.
   * /
  function replace(url:String):void;*/,/*

  /**
   * Returns the string representation of the Location object's URL. See the JavaScript reference for details.
   * /
  function toString():String;*/,
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);