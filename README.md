# java-module-taf

## Quick start guide
Build the project:
```bash
./mvnw clean install -DskipTests -Dpmd.skip=true -Dcheckstyle.skip=true
```
Run KWD tests against EliteA:
```bash
./mvnw test -pl module-taf-examples -Dpmd.skip=true -Dcheckstyle.skip=true -Dtest=com.staf.examples.demo.kwd.runner.KwdRunner -Dcucumber.features=src/test/java/com/staf/examples/demo/kwd/features/smoke.feature
```

## Code style

### Code format

Currently, we are using Spotless [Maven Plugin](https://github.com/diffplug/spotless/tree/main/plugin-maven#java) to maintain code style
Before commit your changes make sure that files are formatted
```bash
./mvnw clean spotless:check
```
To format your code please use following command
```bash
./mvnw clean spotless:apply
```

### Code quality

**PMD is used for code analysis.**
It should be executed after `compile` phase. Only aggregate can be invoked as is because it invokes
separate lifecyle `test-compile`.
Before commit your changes make sure that pmd detected no violations (in case of any build will be failed):
```bash
./mvnw clean compile pmd:check
```
If you'd like to build individual report per module run this command from project root for all modules or in module
to receive report for a particular module (report(s) will appear in the module(s) target/site/pmd.html):
```bash
./mvnw clean compile jxr:jxr pmd:pmd
```
If you'd like to build aggregated report for the whole project (will appear in project-root target/site/pmd.html):
```bash
./mvnw clean jxr:aggregate pmd:aggregate-pmd
```

The same applies for cpd (copy-paste detector) tool.
```bash
./mvnw clean compile pmd:cpd-check
```
```bash
./mvnw clean compile jxr:jxr pmd:cpd
```
```bash
./mvnw clean jxr:aggregate pmd:aggregate-cpd
```

**Checkstyle is used for code style analysis.**
Violations check:
```bash
./mvnw clean checkstyle:check
```
Individual report per module:
```bash
./mvnw clean checkstyle:checkstyle
```
Aggregated report:
```bash
./mvnw clean checkstyle:checkstyle-aggregate
```
# Description

## Selenide-based approach
The project has selenide module for interaction with selenium web driver. It has all the features granted by [Selenide](https://selenide.org/) itself and provides clear vision on tests implementation flow using selected technical stack.
As an end user, you may select any desired approach for page object definitions and tests design, but we highly recommend following the way described in paragraphs:
* **[Page Object (Selenide)](#page-object-selenide)**;
* **TBD**

### Page object (Selenide)

Modern technologies (huge amount of custom components, entities, etc.) and general Page Object concept involves principles of page parts re-usability and its integration into complex entities.
To follow these key features, the following essences were introduced:
- a [custom element](#custom-element): a representation of any custom element that has its own logical features (named input, custom select, table, etc.);
- a [fragment](#fragment): a custom component consists of multiple custom elements and has its own logic of interaction with them (form that has 2+ named inputs, etc.). As a result, some independent custom elements can be united into the fragment for further reuse;
- a [page](#page): entity that represents page or screen view and incapsulates fragments, custom elements and other selenide elements if any.

**NOTE**: it is possible to use default SelenideElement with a newly introduced elements (see the list above)

![General concept](./readme/page%20object.png)

#### Advantages:
* re-usable code base;
* easy to refactor / to update;
* flexibility: custom API for element;
* well-structured: elements have references to the pages that owns it;
* speeds up tests implementation;
* increases code readability via support of step-by-step chain implementation;
* encapsulate complex logic into component related entity;
* no more complex logic of page inheritance to support same functionality.

#### Examples

#### Custom element:

**General concept:**

this type of entity is dependent on element's context and page where it is located.
It means that user can easily interact with the page instance if needed that grants possibility to use chain during the tests implementation phase.

```java
public class NamedInput<Page extends AbstractPage> extends AbstractElement<Page> {

    public NamedInput(final SelenideElement elementBlock, final String name, final Page pageContext) {
        super(elementBlock, pageContext);
        this.element = elementBlock.$(String.format("[placeholder='%s']", name));
    }

    public NamedInput(final SelenideElement elementBlock, final Page pageContext) {
        super(elementBlock, pageContext);
    }

    public String getName() {
        return element.getAttribute("Placeholder");
    }

    public String getValue() {
        return element.getText();
    }

    public Page setValue(final String value) {
        log.info("Attempt to type '{}' into the input '{}'", value, getName());
        element.setValue(value);
        return pageContext;
    }

}
```
Custom methods:
- setValue;
- getValue.

#### Fragment:

**General concept:**

this type of entity encapsulates 2+ custom elements united together (table, form, etc).
It has almost the same structure as custom element and grants the same advantages.

```java
public class Form<Page extends AbstractPage> extends AbstractElement<Page> {

    private final ElementList<NamedInput<Page>, Page> formInputs = new ElementList(elementBlock.$$(".form_group input"), pageContext, NamedInput.class);

    private final SelenideElement submitButton = elementBlock.$("[type='submit']");

    public Form(final SelenideElement elementBlock, final Page page) {
        super(elementBlock, page);
    }

    public Page submitForm(final Map<String, String> inputsData) {
        formInputs.getElements().forEach(input -> {
            final String inputName = input.getName();
            final String inputData = inputsData.getOrDefault(input.getName(), String.format("input data is undefined for field '%s'", inputName));
            // add logging in case inputData is incorrect, empty, etc.
            input.setValue(inputData);
        });
        submitButton.click();
        return pageContext;
    }

    public List<String> getInputs() {
        return formInputs.getElements().stream().map(NamedInput::getName).collect(Collectors.toList());
    }

}
```
Custom methods:
- getInputs: returns a list of all inputs that are currently on the page and located within the form;
- submitForm: fill in the named inputs based on passed Map<String, String> (input_name: input_value) and clicks at submit button.

NOTE: code above has an example of interaction with a list of custom elements: _new ElementList(...)_

#### Page:

**General concept:**

Page contains all the element related to the page without any limitations: SelenideElement, CustomElements (NamedInput), Fragments (Form).
The idea is to split the page into re-usable across the application / website parts and create appropriate custom components where possible (custom elementst or fragments). Then other pages may use the same pieces without any complex logic of pages inheritance, etc.

```java
public class LoginDemoPage extends AbstractPage {

    // option #1 (default SelenideElement)
    private final SelenideElement loginForm = $("form");
    private final SelenideElement submitBtn = $("#login-button");

    // Option #1.1 (custom element logic via static init)
    @Getter
    private final NamedInput<LoginDemoPage> usernameInput = NamedInput.init(loginForm.$("[placeholder='Username']"), this, NamedInput.class);

    // Option #1.2 (custom element logic - initialization via constructor)
    @Getter
    private final NamedInput<LoginDemoPage> passwordInput = new NamedInput<>(loginForm, "Password", this);

    // Option #2 (fragment-based approach to avoid declaration of huge number of inputs)
    @Getter
    private final Form<LoginDemoPage> loginFormFragment = new Form<>(loginForm, this);

    public LoginDemoPage clickSubmitButton() {
        submitBtn.click();
        return this;
    }

    // allure step
    @io.qameta.allure.Step("{method} login as user {username}")
    // RP step
    @Step("{method} login as user {username}")
    public LoginDemoPage login(final String username, final String password) {
        return usernameInput.setValue(username).passwordInput.setValue(password).clickSubmitButton();
    }

}
```


## Test implementation

### General testing
By default, there are examples for
- [Report Portal + TestNG](#customreportportallistener);
- [Report Portal + JUnit5](#ontestfinishreportportalextension);
- [Allure + TestNG](#alluretestnglistener);
- [Allure + JUnit5](#allurereportextension);

### [TestNG](https://testng.org/)
Project ```NAME OF THE MODULE``` has main specifics related to TestNG usage.
It contains the following listeners:
- [CustomReportPortalListener](#customreportportallistener)
- [AllureTestNgListener](#alluretestnglistener)

### CustomReportPortalListener
This listener adds integration with [Report Portal](#report-portal) and has following key features:
- supports the logic of screen shooting on test failure;
- TBD

### AllureTestNgListener
It provides a capability to send logs and screenshots to allure report.
From the design perspective, it was built based on TestLifecycleListener.
In order to make it work, you need to create a file in resources folder (META-INF/services/io.qameta.allure.listener.TestLifecycleListener) with the full path to this listener. Example of file's content: ```com.staf.testng.listener.AllureTestNgListener```.

### General recommendations

**TBD**: add data on threads, parallel mode (method, class, etc), visibility scopes for shared objects while using different modes, etc.

### TestNG example

It is strongly advised to introduce intermediate level to keep common (shared stuff) during the test design.
You could use extension of AbstractTest class where shared resources (steps holder, initial page, common methods, etc.) may be stored.

```java
@Slf4j
public class LoginTest extends AbstractTest {

    private final String USERNAME = "standard_user";
    private final String PASSWORD = "secret_sauce";

    @Test
    public void rpTest() {
        log.info("Test RP");
    }

    @Test
    public void loginTest() {
        open(Configuration.baseUrl);
        LoginDemoPage loginDemoPage = page(LoginDemoPage.class);
        loginDemoPage
                .getUsernameInput().setValue(USERNAME)
                .getPasswordInput().setValue(PASSWORD)
                .clickSubmitButton();
    }

    @Test
    public void loginFragmentTest() {
        open(Configuration.baseUrl);
        LoginDemoPage loginDemoPage = page(LoginDemoPage.class);
        log.info("Login form inputs: {}", loginDemoPage.getLoginFormFragment().getInputs());
        loginDemoPage
                .getLoginFormFragment()
                .submitForm(Map.of("Username", USERNAME, "Password", PASSWORD));
    }

    @Test
    public void loginStepTest() {
        open(Configuration.baseUrl);
        page(LoginDemoPage.class).login(USERNAME, PASSWORD);
    }

    @Test
    public void failedTest() {
        open(Configuration.baseUrl);
        Assertions.fail("Demo failure");
    }
}
```

### [JUnit5](https://junit.org/junit5/docs/current/user-guide/)
Project ```NAME OF THE MODULE``` includes functionality related to the usage of JUnit5.
To ensure that reporting functionality works fine with [Report Portal](#report-portal) and [Allure](#allure), the following extensions were introduced:
It contains the following extensions:
- [OnTestFinishReportPortalExtension](#ontestfinishreportportalextension)
- [AllureReportExtension](#allurereportextension)

In order to support multi-threading you can add custom parallelisation strategy```com.staf.junit.config.ParallelExecutionStrategy```.
You need to create file ```junit-platform.properties``` in resources folder, please, refer to the example below:
```properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=same_thread
junit.jupiter.execution.parallel.mode.classes.default=concurrent
junit.jupiter.execution.parallel.config.strategy=custom
junit.jupiter.execution.parallel.config.custom.class=com.staf.junit.config.ParallelExecutionStrategy
```

### OnTestFinishReportPortalExtension
It is responsible for integration between Report Portal and JUnit5.
This extension has the following functionality:
- screen shooting at failure;
- re-run mechanism;
- TBD;

### AllureReportExtension

It provides a capability to integrate JUnit5 tests with an Allure reporting and supports the following features:
- screenshots attachment to the report;
- TBD;

## How To?
### How to create a CustomElement?
Pre-requisites:
- extend class AbstractElement: ```public class NamedInput<Page extends AbstractPage> extends AbstractElement<Page>```
- declare the constructor that matches constructor from AbstractElement class
- add you custom logic (feel free to return page's or custom element's instances to support chaining):
```java
    public Page setValue(final String value) {
        log.info("Attempt to type '{}' into the input '{}'", value, getName());
        element.setValue(value);
        return pageContext;
    }
```

NOTE: in order to have possibility to initialize custom elements via static _init_ method you need to declare constructor with parameters (final SelenideElement element, final Page pageContext)

[Custom element example](#custom-element)

### How to create a Fragment?
The logic of a fragment creation is similar to CustomElement except of one fact:
the fragment already has element (fragment element) that refers to the whole block, then you need to define inner elements based on this fragment element (similar to the search from element's perspective inside the html tree):
```java
    // element is predefined in constructor
    private final ElementList<NamedInput<Page>, Page> formInputs = new ElementList(element.$$(".form_group input"), pageContext, NamedInput.class);

    private final SelenideElement submitButton = element.$("[type='submit']");
```

[Fragment example](#fragment)
### How to create a Page?
In order to create new page you need to extend your class from abstract class ```com.staf.ui.selenide.core.AbstractPage``` and combine different types of elements including default SelenideElement if needed.
```java
public class LoginDemoPage extends AbstractPage {
    //...
}
```
[Page example](#page)


### How to Encrypt/Decrypt sensitive data?

Any string value could be encrypted/decrypted from-the-box with ```DefaultEncryptor``` that uses AES256 standard. 
#### Encrypt:
```java
String encryptedValue = new DefaultEncryptor().encrypt("someString");
```
#### Decrypt:
```java
String decryptedValue = new DefaultEncryptor().decrypt(encryptedValue);
```

AES256 uses password for data encryption/decryption. Encryption password could be set both through ```crypto.properties``` file or environment variables using ```crypto.password``` key.

Available inputs for ```decrypt(...)``` method:

- simple encrypted value: ```3RQdBRPRi+fgLla4bM7TvfIEVHryb6ghYtEWpCNVjJz8ScAWblwhc31zZMJmctH+f34Y80I7R43NVRtiLu30iA==```
- simple encrypted value with placeholders: ```<someEncryptedValue>``` or ```<ENCRYPTED:someEncryptedValue>```
- complex string with few encrypted values. Important: in this case usage of placeholders is obligatory: ```https://<someEncryptedValue>/<ENCRYPTED:someEncryptedValue>.com```

#### Supported placeholders: 

    ```<...>```

    ```<ENCRYPTED:...>```

TODO:do we want to keep methods (service methods) within the page? or we plan to introduce steps for these goals?
