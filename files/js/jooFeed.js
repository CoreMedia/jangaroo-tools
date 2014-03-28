var monthNames = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    ];
 
function removeAllChildren(element) {
  while (element.hasChildNodes()) {
    element.removeChild(element.firstChild);
  }
}

function jooDisplayFeed(containerId, feedUri, maxEntries) {
   google.load("feeds", "1");
   google.setOnLoadCallback(function() { 
     var feed = new google.feeds.Feed(feedUri);
     feed.load(function(result) {
       if (!result.error) {
         var feedContainer = document.getElementById(containerId);
         removeAllChildren(feedContainer);
         for (var i = 0; i < result.feed.entries.length && i < maxEntries; i++) {
           var entryElement = document.createElement("div");
           entryElement.className = "sk_teaser";
           var entry = result.feed.entries[i];
           var date = new Date(entry.publishedDate);
           var dateElement = document.createElement("h4");
           var dateStr = monthNames[date.getMonth()] + ' ' + date.getDate() + ', ' + date.getFullYear();
           dateElement .appendChild(document.createTextNode(dateStr));
           entryElement.appendChild(dateElement );
           var titleAnchor = document.createElement("a");
           titleAnchor.appendChild(document.createTextNode(entry.title));
           titleAnchor.href = entry.link;
           var titleElement = document.createElement("h3");
           titleElement.appendChild(titleAnchor);
           entryElement.appendChild(titleElement);
           var contentElement = document.createElement("p");
           contentElement.appendChild(document.createTextNode(entry.contentSnippet));
           entryElement.appendChild(contentElement);
           feedContainer.appendChild(entryElement);
         }
         var clearElement = document.createElement("div");
         clearElement.className = "sk_clear";
         feedContainer.appendChild(clearElement);
         feedContainer.appendChild(document.createElement("p"));
       }
     });
   });
}
