package com.staf.driver.type;

import com.staf.common.exception.AqaException;
import com.staf.driver.loader.ICapabilityLoader;
import com.staf.driver.loader.impl.AndroidChromeCapabilityLoader;
import com.staf.driver.loader.impl.ChromeCapabilityLoader;
import com.staf.driver.loader.impl.EdgeCapabilityLoader;
import com.staf.driver.loader.impl.FirefoxCapabilityLoader;
import com.staf.driver.loader.impl.IOSSafariCapabilityLoader;
import com.staf.driver.loader.impl.MobileNativeCapabilityLoader;
import com.staf.driver.loader.impl.SafariCapabilityLoader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum DriverType {
    CHROME("Chrome", new ChromeCapabilityLoader()),
    FIREFOX("Firefox", new FirefoxCapabilityLoader()),
    SAFARI("Safari", new SafariCapabilityLoader()),
    EDGE("Edge", new EdgeCapabilityLoader()),
    ANDROID_NATIVE("Android Native", new MobileNativeCapabilityLoader()),
    IOS_NATIVE("IOS Native", new MobileNativeCapabilityLoader()),
    ANDROID_CHROME("Android Chrome", new AndroidChromeCapabilityLoader()),
    IOS_SAFARI("IOS Safari", new IOSSafariCapabilityLoader());

    private final String driverType;
    private final ICapabilityLoader capabilityLoader;

    public static DriverType getDriverType(final String driverType) {
        for (DriverType type : values()) {
            if (type.getDriverType().equalsIgnoreCase(driverType)) {
                return type;
            }
        }
        throw new AqaException(
                "Current driver type %s isn't supported. List of supported platforms is: %s",
                driverType, Arrays.asList(values()).toString());
    }
}
