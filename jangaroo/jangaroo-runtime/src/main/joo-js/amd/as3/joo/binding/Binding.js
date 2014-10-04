define("as3/joo/binding/Binding", ["as3-rt/AS3", "as3/trace", "as3/joo/addEventListener"], function (AS3, trace, addEventListener) {
  return AS3.class_({
    package_: "joo.binding",
    class_: "Binding",
    members: {
      constructor: function Binding(source, destination) {
        this.source$1 = source;
        this.destination$1 = destination;
        // bind once:
        AS3.bind(this, "execute");
      },
      dependOn: function dependOn(observable, event) {
        if (event.substr(0, 2) === "on") {
          event = event.substr(2);
        }
        var dependencies = this.dependencies$1;
        var dependenciesForEvent = dependencies[event];
        if (!dependencies[event]) {
          dependenciesForEvent = dependencies[event] = [];
        }
        if (dependenciesForEvent.indexOf(observable) !== -1) {
          // already listening:
          return;
        }
        //trace("[DEBUG]", "Recording dependency on " + observable + "#" + event);
        addEventListener(observable, event, this.execute);
        dependenciesForEvent.push(observable);
      },
      execute: function execute() {
        var dependencies = this.dependencies$1;
        if (dependencies) {
          //trace("Invalidating dependency " + this);
          // remove all event listeners:
          for (var event in dependencies) {
            var eventDependencies = dependencies[event];
            for (var i = 0; i < eventDependencies.length; ++i) {
              eventDependencies[i].removeListener(event, this.execute);
            }
          }
        }
        //trace("Evaluating " + this);
        this.dependencies$1 = {};
        AS3.executeBinding(this);
      },
      _execute: function _execute() {
        try {
          var result = this.source$1.call(null);
          this.destination$1.call(null, result);
        } catch (e) {
          // ignore
          trace("[WARN]", "While evaluating " + this + ":", e);
        }
      },
      toString: function toString() {
        return "{" + this.source$1.toString().match(/function\s*[^(]*\(\)[^{]*\{\s*([^}]*)\}\s*/)[1].match(/^\s*(.*)\s*$/)[1] + "}";
      }
    }
  });
});
