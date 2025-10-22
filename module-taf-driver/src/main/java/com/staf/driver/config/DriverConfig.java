package com.staf.driver.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.staf.common.property.BaseConfig;
import com.staf.common.property.PropertyUtil;
import com.staf.driver.config.converter.BrowserToCapabilityLoaderConverter;
import com.staf.driver.config.converter.CapabilitiesNamespaceMapConverter;
import com.staf.driver.config.converter.ChromeOptionsArgumentsConverter;
import com.staf.driver.config.converter.DriverTypeConverter;
import com.staf.driver.config.converter.HolderTypeConverter;
import com.staf.driver.config.converter.KeyValueConverter;
import com.staf.driver.config.converter.PlatformToFactoryConverter;
import com.staf.driver.constant.DriverConstants;
import com.staf.driver.core.Type;
import com.staf.driver.factory.IDriverFactory;
import com.staf.driver.loader.ICapabilityLoader;
import com.staf.driver.type.DriverType;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

@LoadPolicy(LoadType.MERGE)
@Config.Sources({
    "system:env",
    "system:properties",
    "classpath:configuration.properties",
    "classpath:config/${env}/configuration.properties",
    "classpath:${driver.extra.config}"
})
public interface DriverConfig extends BaseConfig {

    // getting extra config path (required for loading additional configuration)
    String EXTRA_CONFIG = PropertyUtil.getConfig(DriverConfig.class).extraConfigurationPath();

    DriverConfig INSTANCE = Objects.nonNull(Resources.getResource(EXTRA_CONFIG))
            ? PropertyUtil.initConfig(DriverConfig.class, ImmutableMap.of(DriverConstants.DRIVER_EXTRA_CONFIG, EXTRA_CONFIG))
            : PropertyUtil.getConfig(DriverConfig.class);

    static DriverConfig get() {
        return INSTANCE;
    }

    @Config.Key("driver.type")
    @Config.DefaultValue("Chrome")
    @Config.ConverterClass(DriverTypeConverter.class)
    DriverType driverType();

    @Config.Key("driver.selenium.server")
    URL seleniumServer();

    @Config.Key("driver.type")
    @Config.ConverterClass(PlatformToFactoryConverter.class)
    IDriverFactory driverFactoryByPlatform();

    @Config.Key("driver.type")
    @Config.ConverterClass(BrowserToCapabilityLoaderConverter.class)
    ICapabilityLoader capabilityLoaderByBrowser();

    @Config.Key("driver.implicitlyWaitTimeout")
    @Config.DefaultValue("20000")
    long implicitlyWaitTimeout();

    @Config.Key("driver.startMaximized")
    @Config.DefaultValue("false")
    Boolean startMaximized();

    @Config.Key("driver.browserSize")
    @Config.DefaultValue("1920x1280")
    String browserSize();

    @Config.Key("driver.capabilities.*")
    @Config.ConverterClass(CapabilitiesNamespaceMapConverter.class)
    @DefaultValue("")
    DesiredCapabilities capabilities();

    @Config.Key("driver.options")
    @Config.ConverterClass(ChromeOptionsArgumentsConverter.class)
    ChromeOptions chromeOptions();

    @Config.Key("driver.experimentalOptions")
    @Config.ConverterClass(KeyValueConverter.class)
    @Config.DefaultValue("")
    Map<String, Object> chromeExperimentalOptions();

    @Config.Key("driver.prefs")
    @Config.ConverterClass(KeyValueConverter.class)
    @Config.DefaultValue("")
    Map<String, Object> chromePrefs();

    @Config.Key("driver.sauceOptions")
    @Config.ConverterClass(KeyValueConverter.class)
    @Config.DefaultValue("")
    Map<String, Object> sauceOptions();

    @Config.Key("driver.profile")
    @Config.ConverterClass(KeyValueConverter.class)
    @Config.DefaultValue("")
    Map<String, Object> firefoxProfile();

    @Config.Key("driver.extra.config")
    @Config.DefaultValue("")
    String extraConfigurationPath();

    @Config.Key("driver.applicationPath")
    @Config.DefaultValue("")
    String applicationPath();

    @Config.Key("driver.holder")
    @Config.DefaultValue("NOT_DEFINED")
    @Config.ConverterClass(HolderTypeConverter.class)
    Type holderType();
}
