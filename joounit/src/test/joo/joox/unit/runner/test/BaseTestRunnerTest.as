package joox.unit.runner.test {

import joox.unit.framework.TestCase;
import joox.unit.framework.test.MyTest;
import joox.unit.runner.BaseTestRunner;
import joox.unit.framework.Assert;

public class BaseTestRunnerTest extends TestCase {

  public function BaseTestRunnerTest(name : String) {
    super(name);
  }

  public override function setUp() : void {
    this.MyTest = MyTest;
    this.mRunner = new BaseTestRunner();
  }
  public override function tearDown() : void {
    delete this.mRunner;
  }
  public function testGetPreference() : void {
    Assert.assertTrue(BaseTestRunner.getPreference("filterStack"));
    var mPrefs = BaseTestRunner.getPreferences();
    BaseTestRunner.setPreferences(new Object());
    var value = BaseTestRunner.getPreference("key");
    Assert.assertUndefined(value);
    value = BaseTestRunner.getPreference("key", "default");
    Assert.assertEquals("default", value);
    BaseTestRunner.setPreference("key", "value");
    value = BaseTestRunner.getPreference("key", "default");
    Assert.assertEquals("value", value);
    BaseTestRunner.setPreferences(mPrefs);
  }
  public function testGetPreferences() : void {
    var mPrefs = BaseTestRunner.getPreferences();
    BaseTestRunner.setPreferences(new Object());
    BaseTestRunner.setPreference("key", "value");
    var newTestRunner = new BaseTestRunner();
    var prefs = BaseTestRunner.getPreferences();
    Assert.assertEquals("value", prefs["key"]);
    Assert.assertSame(BaseTestRunner.mPreferences, prefs);
    Assert.assertSame(BaseTestRunner.mPreferences,
      BaseTestRunner.getPreferences());
    BaseTestRunner.setPreferences(mPrefs);
  }
  public function getTest() : void {
    this.mRunner.runFailed = function() {
      this.mFailed = true;
    };
    var suite = this.mRunner.getTest("MyTest");
    Assert.assertNotNull(suite);
    Assert.assertEquals("MyTest", suite.getName());
    suite = this.mRunner.getTest("MyTestSuite");
    Assert.assertNotNull(suite);
    Assert.assertEquals("MyTestSuite's Name", suite.getName());
    Assert.assertNull(this.mRunner.getTest("none"));
    Assert.assertTrue(this.mRunner.mFailed);
    this.mRunner.mFailed = false;
    Assert.assertNull(this.mRunner.getTest("???"));
    Assert.assertTrue(this.mRunner.mFailed);
    Assert.assertNull(this.mRunner.getTest("MyTest.testMe"));
  }
  public function testSetPreference() : void {
    var mPrefs = BaseTestRunner.getPreferences();
    BaseTestRunner.setPreferences(new Object());
    var prefs = BaseTestRunner.mPreferences;
    Assert.assertUndefined(prefs.key);
    BaseTestRunner.setPreference("key", "value");
    Assert.assertEquals("value", prefs.key);
    BaseTestRunner.setPreferences(mPrefs);
  }
  public function testSetPreferences() : void {
    var mPrefs = BaseTestRunner.getPreferences();
    BaseTestRunner.setPreferences(new Object());
    var prefs = new Object();
    BaseTestRunner.setPreferences(prefs);
    Assert.assertSame(BaseTestRunner.mPreferences, prefs);
    BaseTestRunner.setPreferences(mPrefs);
  }
  public function testShowStackRaw() : void {
    var mPrefs = BaseTestRunner.getPreferences();
    var value = mPrefs.filterStack;
    BaseTestRunner.setPreference("filterStack", false);
    Assert.assertTrue("joox.unit.runner.BaseTestRunner.showStackRaw()");
    BaseTestRunner.setPreference("filterStack", true);
    Assert.assertFalse("joox.unit.runner.BaseTestRunner.showStackRaw()");
    BaseTestRunner.setPreferences(new Object());
    Assert.assertTrue("joox.unit.runner.BaseTestRunner.showStackRaw()");
    mPrefs.filterStack = value;
    BaseTestRunner.setPreferences(mPrefs);
  }
  public function testTruncate() : void {
    var oldMaxMessageLength =
      BaseTestRunner.getPreference("maxMessageLength");
    BaseTestRunner.setPreference("maxMessageLength", 10);
    Assert.assertEquals("0123456789...",
      BaseTestRunner.truncate("0123456789abcdef"));
    BaseTestRunner.setPreference("maxMessageLength", oldMaxMessageLength);
  }

}
}
