# java-taf-examples

## Getting started

The project contains working examples for a different combinations of technical stacks.

## Tests launch configuration

### Option 1
In order to launch existing tests on your local, you can just open it in any ide and select `Run as` option after context click at corresponding class.

### Option 2
This option covers case when surefire plugin is used for tests launch.

Taking into account that module-taf-examples repository contains tests for JUnit and TestNG, we need to take the control over the surefire test provider used by surefire plugin.
In order to run tests with required test framework use parameter `surefire.provider`:
- `surefire-testng`: in case you launch TestNG tests;
- `surefire-junit-platform`: for JUnit5;
- [more details](https://maven.apache.org/surefire/maven-surefire-plugin/examples/providers.html);

**Here is an example for testng:**
`mvn clean test -Dsurefire.suiteXmlFiles="src/test/resources/suites/smoke-allure.xml" -Dsurefire.provider="surefire-testng"`

Note:
* `-Dsurefire.suiteXmlFiles` - list of testng xml suites split by comma;
* `-Dsurefire.provider` - provider for correct launcher selection

**Examples:**
* [TestNG + Allure]: `mvn clean test -Dsurefire.suiteXmlFiles="src/test/resources/suites/demo-allure.xml" -Dsurefire.provider="surefire-testng"`
* [TestNG + Report Portal]: `mvn clean test -Dsurefire.suiteXmlFiles="src/test/resources/suites/demo-rp.xml" -Dsurefire.provider="surefire-testng"`
* [TestNG]: `mvn clean test -Dsurefire.suiteXmlFiles="src/test/resources/suites/demo-testng.xml” -Dsurefire.provider="surefire-testng"`
* [JUnit5 + Allure]: `mvn clean test -Dsurefire.includes="**/junit5/allure/suite/DemoSuite.java" -Dsurefire.provider="surefire-junit-platform"`
* [JUnit5 + Report Portal]: `mvn clean test -Dsurefire.includes="**/junit5/testng/rp/suite/DemoSuite.java" -Dsurefire.provider="surefire-junit-platform"`
* [JUnit5]: `mvn clean test -Dsurefire.includes="**/junit5/DemoSuite.java" -Dsurefire.provider="surefire-junit-platform"`
