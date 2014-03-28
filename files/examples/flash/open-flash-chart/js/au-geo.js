(function(option) {
  var select = document.getElementsByTagName("SELECT")[2];
  var options = select.options;
  for (var i=0; i < options.length; ++i) {
    if (options[i].firstChild.data.indexOf(option) !== -1) {
      select.value = options[i].value;
      select.onchange();
      return;
    }
  }
  alert("Not found: " + option);
}
)(prompt("Enter option"));