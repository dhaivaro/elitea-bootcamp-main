package com.staf.kwd.storage.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StorageConstants {
    public static final String VARIABLE_REGEX = "\\$\\{(.*)\\}";
    public static final String VARIABLE_ARRAY_REGEX = "(\\$\\{.*\\})\\((\\d)\\)";
    public static final String VARIABLE_PATTERN = "${%s}";
    public static final String VARIABLE_XLS_PATTERN = "${XLS:%s}";

    // SPLITTERS
    public static final String REGEXP_SPLITTER = ",|;|\\|";
}
