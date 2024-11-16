package com.seven.delivr.util;

public final class Utilities {
    public static String clean(String value, String regex){
        if(value == null) return value;
        return value.strip().replaceAll(regex, "");
    }
    public static String escape(String value){
        if(value == null) return value;
        return value.strip().replaceAll("(\\(\\-\\$\\!\\)\\+\\.\\,\\'\\:)", "\\\\$1");
    }

    public static boolean isEmpty(String value){
        return value== null || value.isBlank() || value.isEmpty();
    }
}
