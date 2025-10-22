package com.staf.property;

import com.staf.common.property.Configuration;
import java.time.Duration;

import com.staf.common.property.TestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

@Slf4j
public class PropertyTest {
    @Test
    public void testFile() {
        log.info("Test property file mapping");
        SoftAssertions.assertSoftly(assertion -> {
            assertion
                    .assertThat(Configuration.get().pageWait())
                    .as("Incorrect page wait timeout was extracted from"
                            + " 'configuration.properties file'. Please, check its"
                            + " content or loading strategy.")
                    .isEqualTo(Duration.ofSeconds(30));
            assertion
                    .assertThat(Configuration.get().elementWait())
                    .as("Incorrect element wait timeout was extracted from"
                            + " 'configuration.properties file'. Please, check its"
                            + " content or loading strategy.")
                    .isEqualTo(Duration.ofSeconds(5));
        });
    }

    @Test(enabled = false)
    public void testEnvironmentOverride() {
        // Make sure launch configuration has environment variable thread.count=2.
        // Configuration file has 1 by default that should be override by env var.
        Assertions.assertThat(Configuration.get().threadCount())
                .as("Incorrect value of thread count in the file 'configuration.properties'")
                .isEqualTo(2);
    }

    @Test
    public void testCustomVariable() {
        // user.language is system property that is not declared neither at class level nor at
        // property file level
        Assertions.assertThat(TestConfiguration.get().getProperty("user.language"))
                .as("Unable to extract property")
                .isNotEmpty();
    }

    @Test
    public void testUndeclaredProperty() {
        // undeclared.property is not declared at Configuration class level
        Assertions.assertThat(TestConfiguration.get().getProperty("undeclared.property"))
                .as("Unable to extract property")
                .isEqualTo("test property value");
    }

    @Test
    public void testEncryptedProperty() {
        TestConfiguration configuration = TestConfiguration.get();
        // make sure passphrase 'testing encryption' is used in crypto.properties
        Assertions.assertThat(configuration.getDecryptedProperty("encrypted.property"))
                .as("Unable to extract encrypted property")
                .isEqualTo("Test string");
        Assertions.assertThat(configuration.encryptedProperty())
                .as("Unable to extract encrypted property")
                .isEqualTo("Test string");
        Assertions.assertThat(configuration.getDecryptedProperty("encrypted.undeclared.property"))
                .as("Unable to extract encrypted property")
                .isEqualTo("Test string");
    }
}
