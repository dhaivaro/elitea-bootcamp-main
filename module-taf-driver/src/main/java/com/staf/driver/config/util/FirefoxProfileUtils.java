package com.staf.driver.config.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.firefox.FirefoxProfile;

/** Utility class for working with Firefox profile */
@UtilityClass
public final class FirefoxProfileUtils {
    public static void updateProfileByValue(final FirefoxProfile profile, final String key, final String toConvert) {
        if (NumberUtils.isParsable(toConvert)) {
            profile.setPreference(key, Integer.parseInt(toConvert));
        } else if (BooleanUtils.toBooleanObject(toConvert) != null) {
            profile.setPreference(key, Boolean.parseBoolean(toConvert));
        } else {
            profile.setPreference(key, toConvert);
        }
    }
}
