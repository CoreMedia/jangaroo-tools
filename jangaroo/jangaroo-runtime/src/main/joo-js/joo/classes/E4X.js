// hack: provide empty objects for E4X classes for browsers not supporting E4X (all but Firefox)
// so that at least the runtime does not try to load them:
if (typeof XML === "undefined") {
  XML = {};
}
if (typeof XMLList === "undefined") {
  XMLList = {};
}
