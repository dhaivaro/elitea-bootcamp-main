package com.staf.common.encryption;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
@UtilityClass
public class SecretUtils {
    private static final String ERROR_MESSAGE_TEXT_INCORRECT_ALGORITHM =
            "Incorrect algorithm has been used for Secret Key generation";

    /**
     * Generates secret key
     *
     * @param algorithm cipher algorithm
     * @param size size
     * @return secret key instance
     */
    public static SecretKey createSecretKey(final String algorithm, final int size) {
        log.info("Secret key creation. Algorithm: {}. Size: {}", algorithm, size);
        final KeyGenerator keygen;
        try {
            keygen = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error during the Secret key creation process. Msg: {}", e.getMessage());
            throw new RuntimeException(ERROR_MESSAGE_TEXT_INCORRECT_ALGORITHM, e);
        }
        keygen.init(size);
        return keygen.generateKey();
    }

    /**
     * Saves secret key to the file system
     *
     * @param secretKey secret key instance
     * @param file file to save
     */
    public static void saveSecretKey(final Key secretKey, final File file) {
        try {
            log.info("Secret key will be saved to the file {}", file.getAbsolutePath());
            FileUtils.writeByteArrayToFile(file, Base64.getEncoder().encode(secretKey.getEncoded()));
        } catch (IOException e) {
            throw new RuntimeException(ERROR_MESSAGE_TEXT_INCORRECT_ALGORITHM, e);
        }
    }

    /**
     * Loads secret key from pre-defined file for encryption and decryption
     *
     * @param path path to secret key file
     * @param algorithm cipher algorithm
     * @return secret key instance
     */
    public static SecretKey loadSecretKey(final File path, final String algorithm) {
        try {
            log.info("Secret key will be loaded. Algorithm: {}. Path: {}", algorithm, path.getAbsolutePath());
            return new SecretKeySpec(Base64.getDecoder().decode(FileUtils.readFileToByteArray(path)), algorithm);
        } catch (IOException e) {
            log.error("Error during the attempt to read file to byte array: {}", e.getMessage());
            throw new RuntimeException("Error during the attempt to read file to byte array", e);
        }
    }
}
