package com.staf.examples.demo.selenide;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.chrome.ChromeOptions;

public class Tester {
    public static void main(String[] args) {
        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 11");
        browserOptions.setCapability("platformName", "Windows 11");
        browserOptions.setBrowserVersion("latest");
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", "mikalai_biazruchka");
        sauceOptions.put("accessKey", "333312ec-8c6e-4f57-8aad-468e5001fb7e");
        sauceOptions.put("build", "selenium-build-GGJ73");
        sauceOptions.put("name", "<your test name>");
        browserOptions.setCapability("sauce:options", sauceOptions);
        System.out.println();
    }
}
