package com.github.stevenkin.jim.gateway.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class CheckUtils {
    public static final String COMMON_FIELD = "flowID,initiator,";

    public CheckUtils() {
    }

    public static void notEmpty(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message + " must be specified");
        } else if (obj instanceof String && obj.toString().trim().length() == 0) {
            throw new IllegalArgumentException(message + " must be specified");
        } else if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            throw new IllegalArgumentException(message + " must be specified");
        } else if (obj instanceof Collection && ((Collection)obj).isEmpty()) {
            throw new IllegalArgumentException(message + " must be specified");
        } else if (obj instanceof Map && ((Map)obj).isEmpty()) {
            throw new IllegalArgumentException(message + " must be specified");
        }
    }
}
