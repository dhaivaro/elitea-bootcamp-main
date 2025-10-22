# Module TAF Report Portal

## Getting started

The project contains useful examples of the artifacts related to Report Portal:
- Listener for TestNG;
- Extensions for JUnit;
- etc.

## Usage

You may select any way of its integration with your tests supported by selected test framework.
[TestNG](https://testng.org/doc/documentation-main.html#testng-listeners) and [JUnit5](https://junit.org/junit5/docs/current/user-guide/#extensions) provides the ability to configure test launch per selected test framework.

Existing artifacts are not intended to be used as a standalone solution but may cover basic needs of the user.

Feel free to create your own listeners / extensions and share them with the community.

## Artifacts description
* CustomReportPortalListener - TestNG listener for Report Portal integration: covers integration characteristics and adds SLID attribute on the fly to follow possible RP-2-Sauce integration;
* OnTestFinishReportPortalExtension - JUnit5 extension for Report Portal integration: publishes test results to Report Portal on test finish, handles rerun cases;
* TBD.
