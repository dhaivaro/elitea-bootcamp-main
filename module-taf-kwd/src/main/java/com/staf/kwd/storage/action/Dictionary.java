package com.staf.kwd.storage.action;

import com.staf.kwd.storage.ReservedKeys;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Dictionary {
    private static final String KEY_ERROR = "Reserved variable '%s' was not determined.";
    private static final Map<String, Action<String, String>> KEY_TO_ACTION;

    static {
        KEY_TO_ACTION = new HashMap<>();
        KEY_TO_ACTION.put(ReservedKeys.TEMP_VALUE.toString(), ActionsLibrary.GENERIC_RETURN_OBJECT);
        KEY_TO_ACTION.put(ReservedKeys.XLSX_ROW_CURRENT.toString(), ActionsLibrary.GENERIC_RETURN_OBJECT);
        KEY_TO_ACTION.put(ReservedKeys.XLSX_ID_CURRENT.toString(), ActionsLibrary.GENERIC_RETURN_OBJECT);
        KEY_TO_ACTION.put(ReservedKeys.XLSX_PATH_CURRENT.toString(), ActionsLibrary.GENERIC_RETURN_OBJECT);
        KEY_TO_ACTION.put(ReservedKeys.XLSX_SHEETNAME_CURRENT.toString(), ActionsLibrary.GENERIC_RETURN_OBJECT);
    }

    public static boolean contains(final String key) {
        return KEY_TO_ACTION.containsKey(key);
    }

    public static Action<String, String> getKey(final String key) {
        if (contains(key)) {
            return KEY_TO_ACTION.get(key);
        }
        throw new RuntimeException(String.format(KEY_ERROR, key));
    }

    @SuppressWarnings("unchecked")
    public static <V> V execute(final String key) {
        return (V) KEY_TO_ACTION.get(key).execute(key);
    }
}
