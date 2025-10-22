# module-taf-driver

# Description
The module is related to driver wrapper declaration and allocation of common API [SelenideDriverHolder, TBD]

This need is due to usage of undefined driver implementation within the modules: module-taf-junit, module-taf-testng, module-taf-reportportal.

These modules operate with a DriverManager that is responsible for provisioning of required driver holder based of parameter `driver.holder` used in _configuration.properties_.
It allows to make these modules **independent of used driver type**.

## Key concepts
1. It is expected that all core functionality related to driver interaction will be on core libraries: Selenide, Serenity or PlainDriver solutions are fully responsible for manipulations with a driver (thread-safety, core API like getDriver(), etc.);
2. DriverManager supports ONLY limited number of methods required to cover needs of a separate modules (in order to unweave them with each of the implementations): the rest can be added by end user if any (at the same time it is optional since user knows what core driver should be used within the solution);
3. Reduction of a code duplicates for selection of correct driver holder

## Required properties
It is necessary to declare property `driver.holder` to identify core lib used by user.
This parameter is **NOT REQUIRED** if API **ONLY** module is used.
At the moment, it is required to define if DriverFactory from `module-taf-driver` is used for driver's instantiation.



## Available driver holders
1. Selenide: `driver.holder=selenide`;
2. TBD

## How to add new driver holder?
If you need to add new holder, please, follow these steps:
1. Create holder class that implements interface IDriver;
2. Put @HolderType annotation;
3. Extend existing enum Type with new holder;
4. Add implementation to holder's methods.

Example for SelenideHolder:
```java
@HolderType(type = Type.SELENIDE)
public class SelenideDriverHolder implements IDriver {
    ...
}
```

## Does usage of DriverManager is required?
DriverManager usage is optional it can be easily replaced required core lib.
Example for Selenide:
`DriverManager.getInstance().getCurrentDriver().screenshot(OutputType.BYTES)`
can be replaced with
`Selenide.screenshot(OutputType.BYTES)`.

The main purpose of this is to make some taf modules independent of core lib usage.


## Sauce Labs support
You can use sauce labs from the Staf by adding additional capabilities to configuration file:
```
driver.sauceOptions=username:{username};;accessKey:{access-key};;build:{selenium-build};;name:{test-name}
```

```;;``` - is used as separator for sauce options

Values for 'username' and 'accessKey' fields can be found in your Sauce Labs profile. 
