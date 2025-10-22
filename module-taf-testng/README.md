# module-taf-testng

## General

Module **module-taf-testng** is a robust and flexible module that leverages TestNG's powerful test library for
seamless integration and execution of your test automation suite. With **module-taf-testng**, you can effortlessly create, manage,
and run test cases in parallel across various platforms, devices, and browsers—ultimately boosting efficiency, test
coverage, and reducing time-to-market.

**Features:**
- Easy configuration and setup of TestNG test suite
- Parallel execution of test cases for faster test cycles
- Flexible reporting with TestNG's default and custom reports
- Integration with popular tools and libraries for seamless end-to-end testing
- Extensible and customizable test execution approach


## Parallel execution
Use parameter -DthreadCount=${thread.count} to increase threads count to ${thread.count}

**Recommended** TestNG suite xml (achieved maximum threads consumption):
```xml

<suite name="Debug suite" verbose="1" thread-count="4" parallel="methods">
  <test name="Debug tests 1" parallel="methods">
    <classes>
      <class name="com.test.demo.ThreadTest"/>
      <class name="com.test.demo.Thread2Test"/>
      <class name="com.test.demo.Thread3Test"/>
    </classes>
  </test>
</suite>
```

**Notes:**

- Use parallel=“methods” in case you wish to parallel all the methods (marked with @Test) among required number of
  threads;
- thread-count defined on xml level will be overrided by -DthreadCount command line parameter;
- If each of classes (com.test.demo.ThreadTest, com.test.demo.Thread2Test, com.test.demo.Thread3Test)  has 4 methods,
  then 12 tests will be equally launched in 3 threads;

## Retry functionality

Actually, TestNG has built-in retry functionality based on retryAnalyzer parameter (please, review `5.12 - Rerunning failed
tests` [TestNG documentation](https://testng.org/doc/documentation-main.html#parallel-tests)).

At the same time STAF has an example of simple **RetryListener implementation** `com.staf.testng.listener.RetryListener` which can be used as well.
It takes into account `retry.count` parameter from configuration file and retries failed tests according to this
parameter. Also, it has merged logic of IRetryAnalyzer and IAnnotationTransformer interfaces under 1 class.
Please, follow the steps below to enable retry functionality:
1. Add `com.staf.testng.listener.RetryListener` to your test suite xml file:
```xml
<listeners>
        <listener class-name="com.staf.testng.listener.RetryListener"></listener>
</listeners>
```
2. Add `retry.count` parameter to your configuration.properties file:
```properties
retry.count=1
```

Please, refer to [TestNG documentation](https://testng.org/doc/documentation-main.html#parallel-tests) for more details.