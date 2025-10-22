package com.staf.kwd.storage.action;

import com.staf.kwd.storage.ObjectsStorage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ActionsLibrary {
    public static final Action<String, String> GENERIC_RETURN_OBJECT = k -> ObjectsStorage.getEntity(k);
}
