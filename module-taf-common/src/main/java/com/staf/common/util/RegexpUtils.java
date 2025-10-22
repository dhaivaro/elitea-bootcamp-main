package com.staf.common.util;

import com.staf.common.exception.AqaException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/** Utility class for interaction with Regexp */
@UtilityClass
public class RegexpUtils {
    /**
     * Get String by regexp and group from input string
     *
     * @param input initial string
     * @param regex regular expression
     * @param group group number
     * @return String extracted String
     */
    public String getStringByRegexp(final String input, final String regex, final int group) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return matcher.group(group);
        }
        throw new AqaException("Incorrect %s regexp for %s input string", regex, input);
    }

    /**
     * Get all group values by regexp
     *
     * @param input initial string
     * @param regex regular expression
     * @return String all extracted values from the groups
     */
    public String[] getAllGroupsByRegexp(final String input, final String regex) {
        int itterator = 0;
        final Matcher matcher = Pattern.compile(regex).matcher(input);
        final String[] args = new String[matcher.groupCount()];
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                args[itterator] = matcher.group(i);
                itterator++;
            }
        }
        return args;
    }
}
