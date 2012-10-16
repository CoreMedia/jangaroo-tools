//
// Invokes phantomjs-page.html using PhantomJS and extracts the test result that is provided by this page.
//

var fs = require('fs');

var page = require('webpage').create();
var pageUrl = "file:///"+ fs.workingDirectory+ "/phantomjs-page.html";

// handler for capturing console events that occur in the page.
// Since this might cause lots of (jangaroo) output, this is enabled for "verbose" mode only
if( _phantom_verbose ) {
  page.onConsoleMessage = function(msg, lineNum, sourceId) {
    console.log("phantomjs page>: " + msg+")");
  };
}


// open the page
console.log('phantomjs> Opening '+pageUrl+' ...');
page.open('phantomjs-page.html', function (status) {

  // ---- check whether the page can be opened or not
  if (status !== 'success') {
    console.log('phantomjs> Unable to open '+pageUrl+'');
    phantom.exit();
  }
  else{
    console.log("phantomjs> Successfully opened "+pageUrl);
  }

  // ---- extract test result xml from page output
  // this is done by polling the availability of a certain object in the DOM
  var interval = 1000;
  var timeOut = _phantom_timeout;
  var begin = new Date().getTime();
  var pollResult = function() {

    console.log("phantomjs> Running ...");
    if(page.evaluate(function() {return typeof(window["result"]) !== "undefined";})) {

      // the result is available
      try{
        // Retrieve the result of the test suite
        var resultXml = page.evaluate(function(){return window["result"];});

        // print the result. enclose it in <jangaroo-maven-plugin>test result</jangaroo-maven-plugin> so that the
        // the maven plugin is able to extract the result.
        console.info("phantomjs> Result: \n<jangaroo-maven-plugin>"+resultXml+"\n</jangaroo-maven-plugin>");

        // ---- done
        console.log("phantomjs> Done");
        phantom.exit();
      }
      catch(e){
        console.info("phantomjs> Error while extracting result: "+e);
        phantom.exit();
      }
    }
    else if (begin + timeOut > new Date().getTime()){
      // do another recursion as long as timeout isn't reached
      window.setTimeout(pollResult, interval);
    }
    else {
      // timeout has been exceeded. leaving without a result
      console.log("phantomjs> Timeout has been exceeded. No test result is available.");
      phantom.exit();
    }
  };
  window.setTimeout(pollResult, interval);

});