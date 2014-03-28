var swfobject = {
  embedSWF: function(swfUrl, id, width, height, flashVersion, expressInstallUrl, parameters) {
    var div = document.getElementById(id);
    var iframe = document.createElement("IFRAME");
    iframe.id = id;
    var url = swfUrl.replace(/\.swf$/, ".html");
    var params = [];
    for (var p in parameters) {
      // value must already be URL-encoded!
      params.push(p + "=" + parameters[p]);
    }
    if (params.length) {
      url += "?" + params.join("&");
    }
    iframe.width = width;
    iframe.height = height;
    iframe.frameBorder = 0;
    div.parentNode.replaceChild(iframe, div);
    iframe.contentWindow.location.href = url;
  }
};
