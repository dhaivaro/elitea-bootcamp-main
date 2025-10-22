package com.staf.common.encryption;

import com.staf.common.property.CryptoConfig;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.crypto.AbstractEncryptor;

/** Encrypt/Decrypt sensitive information */
@Slf4j
public final class Encryptor extends AbstractEncryptor {
    private static final CryptoConfig PROJECT_CONFIG = CryptoConfig.get();

    private final String algorithmMode;
    private final Key key;
    private Cipher cipher;

    public Encryptor(final String cryptoAlgorithm, final Key key) {
        this.algorithmMode = cryptoAlgorithm;
        this.key = key;
        try {
            this.cipher = Cipher.getInstance(algorithmMode);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Creates instance of data crypto using information from the ProjectConfig and manually passed
     * crypto key path
     *
     * @param cryptoKeyPath path to crypto key
     */
    public Encryptor(final File cryptoKeyPath) {
        this(PROJECT_CONFIG.cryptoMode(), PROJECT_CONFIG.cryptoAlgorithm(), cryptoKeyPath);
    }

    /**
     * Creates instance of data crypto using information from the ProjectConfig: algorithm, mode and
     * path to key
     *
     * @param cryptoAlgorithmMode algorithm mode
     * @param cryptoAlgorithm algorithm
     * @param cryptoKeyPath path to crypto key
     */
    public Encryptor(final String cryptoAlgorithmMode, final String cryptoAlgorithm, final File cryptoKeyPath) {
        this.algorithmMode = cryptoAlgorithmMode;
        this.key = SecretUtils.loadSecretKey(cryptoKeyPath, cryptoAlgorithm);
        try {
            this.cipher = Cipher.getInstance(cryptoAlgorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /** Creates instance of data crypto using information from the ProjectConfig. */
    public Encryptor() {
        this(PROJECT_CONFIG.cryptoMode(), PROJECT_CONFIG.cryptoAlgorithm(), PROJECT_CONFIG.cryptoKeyPath());
    }

    @Override
    public String encrypt(final String decryptedString) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            final String encryptedString =
                    Base64.getEncoder().encodeToString(cipher.doFinal(decryptedString.getBytes()));
            log.info("String '{}' has been encoded into '{}'", decryptedString, encryptedString);
            return encryptedString;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Error while encrypting, check your crypto key! " + e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(final String encryptedString) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedString.getBytes())));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Error while decrypting, check your crypto key! " + e.getMessage(), e);
        }
    }
}
