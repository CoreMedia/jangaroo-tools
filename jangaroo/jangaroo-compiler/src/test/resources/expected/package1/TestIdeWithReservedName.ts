
 class TestIdeWithReservedName {
   parameterWithReservedName(char:string):string {
    char = char + "!";
    return char;
  }

   localVarWithReservedName():string {
    var char = "x";
    char = char.substr(0,0) + "u";
    return char;
  }
}
export default TestIdeWithReservedName;
