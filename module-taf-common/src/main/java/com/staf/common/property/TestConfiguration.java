package com.staf.common.property;

import com.staf.common.encryption.DefaultEncryptor;
import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
    "system:env",
    "system:properties",
    "classpath:test.properties"
})
@Config.DecryptorClass(DefaultEncryptor.class)
public interface TestConfiguration extends BaseConfig {

    TestConfiguration INSTANCE = PropertyUtil.getConfig(TestConfiguration.class);

    static TestConfiguration get() {
        return INSTANCE;
    }

    @Key("encrypted.property")
    @EncryptedValue
    String encryptedProperty();

    @Key("partially.encrypted.property")
    @EncryptedValue
    String partiallyEncryptedProperty();
}
