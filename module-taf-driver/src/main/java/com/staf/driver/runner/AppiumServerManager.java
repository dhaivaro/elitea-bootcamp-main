package com.staf.driver.runner;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class AppiumServerManager {
    private CopyOnWriteArrayList<AppiumDriverLocalService> appiumLocalServiceList = new CopyOnWriteArrayList<>();

    public AppiumDriverLocalService startAppiumServer() {
        final AppiumDriverLocalService appiumDriverLocalService = startAppium();
        appiumLocalServiceList.add(appiumDriverLocalService);
        return appiumDriverLocalService;
    }

    public void stopAppiumServers() {
        appiumLocalServiceList.forEach(AppiumDriverLocalService::stop);
    }

    private AppiumDriverLocalService startAppium() {
        final AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        serviceBuilder.usingAnyFreePort();

        final AppiumDriverLocalService server = AppiumDriverLocalService.buildService(serviceBuilder);
        server.start();

        return server;
    }
}
