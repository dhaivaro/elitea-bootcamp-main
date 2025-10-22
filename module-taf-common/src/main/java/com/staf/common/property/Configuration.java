package com.staf.common.property;

import com.staf.common.encryption.DefaultEncryptor;
import java.time.Duration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.converters.DurationConverter;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
    "system:env",
    "system:properties",
    "classpath:configuration.properties",
    "classpath:config/${env}/configuration.properties"
})
@Config.DecryptorClass(DefaultEncryptor.class)
public interface Configuration extends BaseConfig {
    Configuration INSTANCE = PropertyUtil.getConfig(Configuration.class);

    static Configuration get() {
        return INSTANCE;
    }

    // Environment related properties
    @Key("env")
    @DefaultValue("demo")
    String env();

    @Key("${env}.baseUrl")
    @DefaultValue("https://www.google.com/")
    String baseUrl();

    // Timeout properties
    @Key("page.timeout")
    @DefaultValue("30 s")
    @ConverterClass(DurationConverter.class)
    Duration pageWait();

    @Key("element.timeout")
    @DefaultValue("3 s")
    // example of duration converter's usage: http://owner.aeonbits.org/docs/type-conversion/
    @ConverterClass(DurationConverter.class)
    Duration elementWait();

    @Key("element.poll.timeout")
    @DefaultValue("3 s")
    @ConverterClass(DurationConverter.class)
    Duration elementPollWait();

    // runner properties
    @Key("thread.count")
    @DefaultValue("1")
    int threadCount();

    @Key("retry.count")
    @DefaultValue("0")
    int retryCount();
}
