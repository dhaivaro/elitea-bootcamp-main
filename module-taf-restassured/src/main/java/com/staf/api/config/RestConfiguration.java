package com.staf.api.config;

import com.staf.common.encryption.DefaultEncryptor;
import com.staf.common.property.BaseConfig;
import com.staf.common.property.PropertyUtil;
import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
    "system:env",
    "system:properties",
    "classpath:configuration.properties",
    "classpath:config/${env}/configuration.properties"
})
@Config.DecryptorClass(DefaultEncryptor.class)
public interface RestConfiguration extends BaseConfig {

    RestConfiguration INSTANCE = PropertyUtil.getConfig(RestConfiguration.class);

    static RestConfiguration get() {
        return INSTANCE;
    }

    @Key("restassured.baseurl")
    @DefaultValue("https://www.google.com/")
    String baseUrl();

    @Key("restassured.logging")
    @DefaultValue("false")
    boolean isLoggingEnabled();
}
