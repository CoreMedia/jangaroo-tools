package joox.unit.framework.test {

import joox.unit.framework.TestCase;
import joox.unit.framework.ComparisonFailure;
import joox.unit.framework.Assert;

public class ComparisonFailureTest extends TestCase {

public function ComparisonFailureTest( name )
{
	super( name );
}

public function testToString()
{
	var cf = new ComparisonFailure( "!", "a", "b", null );
	Assert.assertEquals( "ComparisonFailure: ! expected:<a>, but was:<b>", cf );
	cf = new ComparisonFailure( null, "a", "b", null );
	Assert.assertEquals( "ComparisonFailure: expected:<a>, but was:<b>", cf );
	cf = new ComparisonFailure( null, "ba", "bc", null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<...a>, but was:<...c>", cf );
	cf = new ComparisonFailure( null, "ab", "cb", null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<a...>, but was:<c...>", cf );
	cf = new ComparisonFailure( null, "ab", "ab", null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<ab>, but was:<ab>", cf );
	cf = new ComparisonFailure( null, "abc", "adc", null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<...b...>, but was:<...d...>", cf );
	cf = new ComparisonFailure( null, "ab", "abc", null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<...>, but was:<...c>", cf );
	cf = new ComparisonFailure( null, "bc", "abc", null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<...>, but was:<a...>", cf );
	cf = new ComparisonFailure( null, "abc", "abbc", null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<......>, but was:<...b...>", cf );
	cf = new ComparisonFailure( null, "abcdde", "abcde", null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<...d...>, but was:<......>", cf );
	cf = new ComparisonFailure( null, "a", null, null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<a>, but was:<null>", cf );
	cf = new ComparisonFailure( null, null, "a", null );
	Assert.assertEquals(
		"ComparisonFailure: expected:<null>, but was:<a>", cf );
}

}
}
