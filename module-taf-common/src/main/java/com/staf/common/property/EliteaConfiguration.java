package com.staf.common.property;

import com.staf.common.encryption.DefaultEncryptor;
import org.aeonbits.owner.Config;


@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:env",
        "system:properties",
        "classpath:elitea.properties"
})
@Config.DecryptorClass(DefaultEncryptor.class)
public interface EliteaConfiguration extends BaseConfig {
    EliteaConfiguration INSTANCE = PropertyUtil.getConfig(EliteaConfiguration.class);

    static EliteaConfiguration get() {
        return INSTANCE;
    }

    @Key("elitea.baseurl")
    @DefaultValue("https://nexus.elitea.ai")
    String baseUrl();
}