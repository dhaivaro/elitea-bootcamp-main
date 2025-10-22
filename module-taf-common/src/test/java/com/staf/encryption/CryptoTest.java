package com.staf.encryption;

import com.staf.common.encryption.DefaultEncryptor;
import com.staf.common.property.Configuration;
import com.staf.common.property.TestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Slf4j
public class CryptoTest {

    public static final String ENCRYPTED_REPLACEMENT_PATTERN = "start <%s> middle <%s> end";
    public static final String DECRYPTED_REPLACEMENT_PATTERN = "start %s middle %s end";

    @Test
    public void testEncryption() {
        final String decryptedString = "Test string";
        DefaultEncryptor encryptor = new DefaultEncryptor();
        final String encryptedString = encryptor.encrypt(decryptedString);
        log.info("Decrypted string: {}. Encrypted string: {}", decryptedString, encryptedString);
        SoftAssertions.assertSoftly(assertion -> {
            assertion.assertThat(encryptedString).isNotEqualTo(decryptedString);
            assertion.assertThat(encryptor.decrypt(encryptedString)).isEqualTo(decryptedString);
        });
    }

    @Test
    public void testPartialEncryption() {
        final String firstDecryptedString = "FIRST TEST STRING";
        final String secondDecryptedString = "SECOND TEST STRING";
        DefaultEncryptor encryptor = new DefaultEncryptor();
        String encrStr = String.format(ENCRYPTED_REPLACEMENT_PATTERN,
                                       encryptor.encrypt(firstDecryptedString),
                                       encryptor.encrypt(secondDecryptedString));
        String decryptedString = encryptor.decrypt(encrStr);
        Assertions.assertThat(decryptedString)
                  .as("Unable to extract encrypted complex string")
                  .isEqualTo(String.format(DECRYPTED_REPLACEMENT_PATTERN, firstDecryptedString, secondDecryptedString));
    }

    @Test
    public void testPartialEncryptionWithProperty() {
        final String firstEncryptedString = TestConfiguration.get().getProperty("test.encrypted.string.value1");
        final String secondEncryptedString = TestConfiguration.get().getProperty("test.encrypted.string.value2");
        DefaultEncryptor encryptor = new DefaultEncryptor();
        String encrStr = String.format(ENCRYPTED_REPLACEMENT_PATTERN,
                                       firstEncryptedString,
                                       secondEncryptedString);
        String decryptedString = encryptor.decrypt(encrStr);
        Assertions.assertThat(decryptedString)
                  .as("Unable to extract encrypted complex string")
                  .isEqualTo(String.format(DECRYPTED_REPLACEMENT_PATTERN, encryptor.decrypt(firstEncryptedString), encryptor.decrypt(secondEncryptedString)));
    }

    @Test
    public void testPartialEncryptionWithConfigurationProperty() {
        Assertions.assertThat(TestConfiguration.get().partiallyEncryptedProperty())
                  .as("Unable to extract encrypted complex string")
                  .isEqualTo(String.format(DECRYPTED_REPLACEMENT_PATTERN,
                                           TestConfiguration.get().getDecryptedProperty("test.encrypted.string.value1"),
                                           TestConfiguration.get().getDecryptedProperty("test.encrypted.string.value2")));
    }
}
