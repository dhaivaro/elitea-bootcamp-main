package com.staf.api.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class URIUtil {

    public final String SLASH = "/";

    /**
     * Removes ending slash
     *
     * @param uri origin uri
     * @return formatted uri
     */
    public String removeEndingSlash(final String uri) {
        return uri.endsWith(SLASH) ? uri.substring(0, uri.length() - 1) : uri;
    }

    /**
     * Replace double slash with single slash
     *
     * @param uri origin uri
     * @return formatted uri
     */
    public String replaceDoubleSlash(final String uri) {
        return uri.replaceAll("(?<!(http:|https:))/+", SLASH);
    }
}
