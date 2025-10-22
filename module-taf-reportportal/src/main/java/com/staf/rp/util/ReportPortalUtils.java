package com.staf.rp.util;

import java.io.File;
import java.util.Base64;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class ReportPortalUtils {
    private static final String FILE_MESSAGE = "RP_MESSAGE#FILE#{}#{}";
    private static final String BASE_64_MESSAGE = "RP_MESSAGE#BASE64#{}#{}";

    public static void log(final File file, final String message) {
        log.info(FILE_MESSAGE, file.getAbsolutePath(), message);
    }

    public static void log(final byte[] bytes, final String message) {
        log.info(BASE_64_MESSAGE, Base64.getEncoder().encodeToString(bytes), message);
    }

    public static void logBase64(final String base64, final String message) {
        log.info(BASE_64_MESSAGE, base64, message);
    }
}
