package com.staf.common.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigConstant {
    public static final String THREAD_COUNT_VAR = "thread.count";
    public static final String DYNAMIC_BEAN_CLASS = "javax.management.DynamicMBean";
    public static final String ENCRYPTED_STRING_PATTERN = "^.*<.+?>.*$";
    public static final String ENCRYPTED_VALUE_SIMPLE_START_PLACEHOLDER = "<";
    public static final String ENCRYPTED_VALUE_COMPLEX_START_PLACEHOLDER = "<ENCRYPTED:";
    public static final String ENCRYPTED_VALUE_END_PLACEHOLDER = ">";
}
