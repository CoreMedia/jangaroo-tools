import Interface from './Interface';

class TestResolveMembers {

  constructor() {
    // resolve Object methods on something typed by an interface:
    var someInterface:Interface;
    var str = someInterface.toString();
  }
}
export default TestResolveMembers;
