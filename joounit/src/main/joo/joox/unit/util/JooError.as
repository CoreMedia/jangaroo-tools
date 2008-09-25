package joox.unit.util {

public class JooError extends Error {

{
  // make Error#toString() cross-JS-engine-compatible:
  Error.prototype["toString"] = function() : String {
    return this.name + ": " + this.message;
  };
}

  public function JooError(name : String, message : String) {
    super(message);
    // super call to native class does not really work, because in some browsers, a new Error is returned
    // instead of modifying the "this" Object.
    this["message"] = message;
    this["name"] = name;
  }
}
}