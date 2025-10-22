package com.staf.driver.type;

import com.staf.common.exception.AqaException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlatformTypes {
    DESKTOP("desktop"),
    MOBILE("mobile");

    private String platform;

    public static PlatformTypes getPlatform(final String platformString) {
        for (PlatformTypes platform : values()) {
            if (platform.name().equalsIgnoreCase(platformString)) {
                return platform;
            }
        }
        throw new AqaException(
                "Current platform %s isn't supported. List of supported platforms is: %s",
                platformString, Arrays.asList(values()).toString());
    }
}
