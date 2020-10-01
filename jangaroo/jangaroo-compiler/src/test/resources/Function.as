package {
[Native]
public class Function {
    [Parameter("thisArg", required)]
    public native function call(thisArg:Object = null, ...args):*;
}
}