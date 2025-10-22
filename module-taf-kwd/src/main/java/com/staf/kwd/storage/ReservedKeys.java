package com.staf.kwd.storage;

import com.staf.kwd.storage.constants.StorageConstants;

public enum ReservedKeys {
    TEMP_VALUE,
    XLSX_ROW_CURRENT,
    XLSX_ID_CURRENT,
    XLSX_PATH_CURRENT,
    XLSX_SHEETNAME_CURRENT;

    @Override
    public String toString() {
        return String.format(StorageConstants.VARIABLE_PATTERN, name());
    }
}
