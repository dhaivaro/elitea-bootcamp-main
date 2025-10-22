package com.staf.common.encryption;

import com.staf.common.constant.ConfigConstant;
import com.staf.common.property.CryptoConfig;
import com.staf.common.property.PropertyUtil;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.crypto.AbstractEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Default encryption (by passphrase) Encrypt/Decrypt sensitive information */
@Slf4j
public final class DefaultEncryptor extends AbstractEncryptor {

    private static final CryptoConfig PROJECT_CONFIG = PropertyUtil.initConfig(CryptoConfig.class);

    private final AES256TextEncryptor encryptor = new AES256TextEncryptor();

    /**
     * Creates instance of data crypto using password
     *
     * @param password password
     */
    public DefaultEncryptor(final String password) {
        encryptor.setPassword(password);
    }

    /** Creates instance of data crypto using information from the ProjectConfig. */
    public DefaultEncryptor() {
        this(PROJECT_CONFIG.password());
    }

    @Override
    public String encrypt(final String decryptedString) {
        final String encryptedString = encryptor.encrypt(decryptedString);
        log.info("String '{}' has been encoded into '{}'", decryptedString, encryptedString);
        return encryptedString;
    }

    /**
     * Method allows to decrypt both an encrypted single-value string or a complex string with encrypted parts.
     * Each encrypted part inside of complex string must start with '&gt;' and end with '&lt;'.
     *
     * @param encryptedString - input with an encrypted single-value string or a complex string with encrypted parts
     * @return decrypted string
     */
    @Override
    public String decrypt(final String encryptedString) {
        if (encryptedString.matches(ConfigConstant.ENCRYPTED_STRING_PATTERN)) {
            return removePlaceholders(encryptedString);
        }
        return encryptor.decrypt(encryptedString);
    }

    private String removePlaceholders(final String partiallyEncryptedString) {
        final Pattern simplePattern = Pattern.compile(String.format("\\%s%s\\%s",
                                                                    ConfigConstant.ENCRYPTED_VALUE_SIMPLE_START_PLACEHOLDER,
                                                                    ".+?(>+)?",
                                                                    ConfigConstant.ENCRYPTED_VALUE_END_PLACEHOLDER));
        final Matcher simpleMatcher = simplePattern.matcher(partiallyEncryptedString);
        String decryptedString = partiallyEncryptedString;

        while (simpleMatcher.find()) {
            final String match = simpleMatcher.group();
            final String clearedMatch = match.startsWith(ConfigConstant.ENCRYPTED_VALUE_COMPLEX_START_PLACEHOLDER)
                                        ? match.replace(ConfigConstant.ENCRYPTED_VALUE_COMPLEX_START_PLACEHOLDER, "").replaceAll(">$", "")
                                        : match.substring(1, match.length() - 1);
            decryptedString = decryptedString.replace(match, decrypt(clearedMatch));
        }
        return decryptedString;
    }
}
