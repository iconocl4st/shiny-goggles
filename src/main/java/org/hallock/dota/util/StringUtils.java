package org.hallock.dota.util;

public class StringUtils {
    public static String padRight(String str, char c, int num) {
        StringBuilder builder = new StringBuilder(num);
        builder.append(str);
        while (builder.length() < num) {
            builder.append(c);
        }
        return builder.toString();
    }
}
