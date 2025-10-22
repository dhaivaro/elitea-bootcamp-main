package com.staf.common.property;

import java.io.File;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env", "system:properties", "classpath:crypto.properties", "classpath:configuration.properties"})
public interface CryptoConfig extends Config {
    CryptoConfig INSTANCE = PropertyUtil.initConfig(CryptoConfig.class);

    static CryptoConfig get() {
        return INSTANCE;
    }

    @Key("crypto.algorithm")
    @DefaultValue("AES")
    String cryptoAlgorithm();

    @Key("crypto.mode")
    @DefaultValue("AES/ECB/PKCS5Padding")
    String cryptoMode();

    @Key("crypto.key.size")
    @DefaultValue("128")
    int keySize();

    @Key("crypto.key.path")
    File cryptoKeyPath();

    // Used only by StandardPBEStringEncryptor
    @Key("crypto.password")
    @DefaultValue("TBD")
    String password();
}
