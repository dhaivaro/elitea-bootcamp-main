# module-taf-selenide

# Description
This module provides the capabilities for UI testing: driver control, UI basics: custom elements, ways of its creation. It allows interacting with both: desktop and mobile.
In order to communicate with a driver session and with UI templating (PageObjects) you have the following options (will be extended later):
- [Selenide](#selenide-based-approach);
- [DriverFactory](#general-driver-factory-concept);
- TBD

## General Driver Factory concept
General concept describes interaction with a driver via usage of a single entry configuration file (examples have `configuration.properties` file).
It has implemented to interact with a pre-defined driver specific properties.
Required driver instance will be instantiated based on selected [driver type](#possible-driver-types) (property `driver.type`).
Driver type is responsible for a proper selection of required driver factory (mobile or desktop) and [capabilities loader](#capabilities-loaders).

All driver-related properties have `driver` namespace.

There are two possible ways of driver configuration using **EXISTING** (user can define its own factory for driver if needed) loaders:
1. via _configuration.properties_ (see what parameters can be added to [here](#thats-it-what-about-other-configurations))
2. via separate property file passed as a value to the property `driver.extra.config` in _configuration.properties_:
```properties
driver.extra.config=driver/driver_config.properties
```
Example above point at _driver_config.properties_ file with a pre-defined driver's capabilities and options.

**Common** capabilities (applicable for any type of the driver):
- `driver.type` (**required**): [the preferable driver for automation](#possible-driver-types);
- `driver.capabilities.capabilityName`: [capability to pass into the driver](#passing-capabilities-into-the-factory);
- `driver.selenium.server`: defines URL for selenium server (grid);

Example:
```properties
# make sure you have started appium server at port 4723
driver.selenium.server=http://localhost:4723/wd/hub
driver.type=Android Chrome
# capability orientation is valid for emulators only
driver.capabilities.orientation=LANDSCAPE
```
Notes:
_the properties above describe the configuration for starting of chrome on Android device or emulator_

### Passing capabilities into the factory
You can pass required capabilities into the factory by adding them to _configuration.properties_ (or your own config.properties) file using the namespace `driver.capabilities`.
Example:
```properties
driver.capabilities.platform=LINUX
```

That's it? What about other configurations?
-
[How to start desktop chrome?](#start-chrome-desktop)

[How to start desktop firefox?](#start-firefox-desktop)

[How to start Android chrome?](#start-android-chrome)

TBD
[How to start native mobile?](#start-native-mobile)
[How to start ios safari?](#start-native-mobile)
[How to start desktop edge?](#start-native-mobile)
[How to start desktop ie?](#start-native-mobile)



### Start Chrome desktop
There are some **Chrome specific** capabilities supported by the framework that should customize the process of driver's instance creation:
  - `driver.options` (_optional_) - contains list of required chrome options (divided by `;;` symbol)
  - `driver.experementalOptions` (_optional_) - list of experimental chrome options (divided by `;;` symbol)
  - `driver.prefs` (_optional_) - list of prefs for expiremental chrome options (divided by `;;` symbol)

Example of driver configuration block in the configuration.properties or your own configuration file:
```properties
driver.selenium.server=http://localhost:4444/wd/hub
driver.type=Chrome

driver.options=--no-sandbox;;--disable-browser-side-navigation;;--test-type;;--disable-infobars;;--ignore-certificate-errors;;--disable-popup-blocking;;--window-size=1920,1080
driver.experimentalOptions=useAutomationExtension:false
driver.prefs=profile.default_content_settings.popups:0;;safebrowsing.enabled
```
**Assumptions:**
- [_chromedriver_](https://chromedriver.chromium.org/getting-started) that supports required version of Chrome browser has been installed
- _selenium server_ has been started at port 4444 (configurable);

The example above starts chrome browser with pre-defined options and preferences.

### Start Firefox desktop
There is a **Firefox specific** capability supported by the framework to customize the process of driver's instance creation:
-  `driver.profile` (_optional_) - provides a possibility to configure the Firefox driver with required FirefoxProfile

Example of driver configuration block in the configuration.properties
```properties
driver.selenium.server=http://localhost:4444/wd/hub
driver.type=Firefox
# properties below can be moved to separate configuration file declared as driver.extra.config=driver/driver_config.properties
driver.profile=xpinstall.signatures.required:false;;pdfjs.disabled:true;;browser.download.folderList:12
```
**Assumptions:**
- [_geckodriver_](https://github.com/mozilla/geckodriver/releases) that supports required version of Firefox browser has been installed;
- _selenium server_ has been started at port 4444 (configurable);

### Start Android Chrome
In order to start Android Chrome, we should make sure following conditions are in place:
- _driver.type=Android Chrome_ is added to configuration.properties;
- _appium server_ has been started at specific port and added as `driver.selenium.server` property.
- the configuration of desired capabilities can be done per [instructions](#passing-capabilities-into-the-factory)

NOTE: if you have 2+ devices connected to PC then you need to declare capability udid in order to start tests on concrete mobile device:
```properties
driver.capabilities.udid=emulator-5554
```

Example of driver configuration block in the configuration.properties
```properties
driver.selenium.server=http://localhost:4723/wd/hub
driver.type=Android Chrome
```

### Start native mobile
There are some **Mobile specific** capabilities supported by the framework that should customize the process of native driver's instance creation:
- `driver.applicationPath` - path to native mobile application.

```properties
TBD

```

### Possible driver types
1. Chrome: the type for desktop Chrome browser automation;
2. Firefox: the type for desktop Firefox browser automation;
3. Safari: the type for desktop Safari browser automation (TBD: check if it is actual);
4. Internet Explorer: the type for desktop Internet Explorer browser automation (TBD: check if it is actual);
5. Edge: the type for desktop Edge browser automation (TBD: check if it is actual);
6. Mobile Native: the type for automation of mobile native applications;
7. Android Native: the type for automation of Android native applications;
8. IOS Native: the type for automation of iOS native applications;
9. Android Chrome: the type for Chrome browser automation on Android;
10. IOS Safari: the type for Safari browser automation on iOS;

### Capabilities Loaders
The main idea of capabilities loaders is to provide flexible and user controllable mechanism of driver configuration.
It means that each separate loader has its own specifics. In addition to it, we try to accumulate some default capabilities that are related ONLY to specified type of driver. User can override them by passing it from _configuration.properties_ file if any.

For example, _ChromeCapabilityLoader_ takes responsibility for parsing of chrome preferences, experimental options, etc. Such functionality is senseless for firefox. At the same time FirefoxCapabilityLoader incapsulates logic of interaction with firefox profiles that has no sense for other driver types.
As of now, there are following loaders:
- _AndroidChromeCapabilityLoader_;
- _ChromeCapabilityLoader_;
- _EdgeCapabilityLoader_;
- _InternetExplorerCapabilityLoader_;
- _IOSSafariCapabilityLoader_;
- _MobileNativeCapabilityLoader_;

The mechanism of proper selection of the capability loader based on `driver.type` used in _configuration.properties_. Each type of driver has mapping to corresponding capability loader.

## Selenide-based approach
The project has selenide module for interaction with selenium web driver. It has all the features granted by [Selenide](https://selenide.org/) itself and provides clear vision on tests implementation flow using selected technical stack.
As an end user, you may select any desired approach for page object definitions and tests design, but we highly recommend following the way described in paragraphs:
* **[Page Object (Selenide)](#page-object-selenide)**;
* **[Driver instantiation for Selenide](#selenide-driver-instantiation)**

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

## Selenide driver instantiation

Current solution fully supports default Selenide driver's instantiation logic.
It means that you can use _selenide.properties_ as a configuration file for your driver instance.
At the same time, there is a flexible driver's configuration logic based on custom driver's factory.
It is implemented via Selenide WebDriverProvider instantiation flow. The only change you need to do to turn this flow on is to declare path to custom WebDriverProvider in _selenide.properties_:

```properties
selenide.browser=com.staf.driver.DriverProvider
```
From this point, all the configurations will be done per [driver's factory flow](#general-driver-factory-concept)

**IMPORTANT**:
1. using WebDriverProvider flow you become responsible for driver's lifecycle: it means that you need to decide on when and how to close driver. It can be done via Listeners (for TestNG) or Extensions (for JUnit5), or any other preferable ways.

Example (as a part of CustomReportPortalListener.class):
```java
public class CustomReportPortalListener extends ReportPortalTestNGListener {

    @Override
    public void onTestFailure(ITestResult testResult) {
        log.info("test failure");
        log(Selenide.screenshot(OutputType.BYTES), "test failure");
        onTestFinish();
        super.onTestFailure(testResult);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        onTestFinish();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        onTestFinish();
    }

    private void onTestFinish() {
        Selenide.closeWebDriver();
    }

}
```
Example above demonstrates approach to close driver after each test despite its status (passed / failed / skipped).
The final choice is on user, but we highly recommend to use restart driver for each new test since it may simplify the process of tests scalability and improve the stability.


#### When to use standard Selenide flow?
It is highly recommended using this flow in case you run your tests on your local and you don't need to customize your driver a lot.

#### When to use WebDriverProvider?
In order to achieve a flexible driver's configuration, it is better to use [driver's factory flow](#general-driver-factory-concept).
Using this flow you will be able to customize your driver with required specifics with less effort.

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
In order to create new page you need to extend your class from abstract class ```com.epam.ui.selenide.core.AbstractPage``` and combine different types of elements including default SelenideElement if needed.
```java
public class LoginDemoPage extends AbstractPage {
    //...
}
```
[Page example](#page)

TODO:do we want to keep methods (service methods) within the page? or we plan to introduce steps for these goals?
