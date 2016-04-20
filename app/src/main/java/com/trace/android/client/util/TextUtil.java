package com.trace.android.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TextUtil {

    public static boolean isBlank(String content) {
        return (content == null || content.trim().length() == 0);
    }

    public static boolean isEmpty(CharSequence content) {
        return (content == null || content.length() == 0);
    }

    public static boolean isEquals(String actual, String expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    public static int length(CharSequence content) {
        return content == null ? 0 : content.length();
    }

    /**
     * 将一个对象用String形式表示
     *
     * @param obj
     * @return
     */
    public static String nullStrToEmpty(Object obj) {
        return (obj == null ? "" : (obj instanceof String ? (String) obj : obj.toString()));
    }

    /**
     * 将首字母大写
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param content
     * @return
     */
    public static String capitalizeFirstLetter(String content) {
        if (isEmpty(content)) {
            return content;
        }

        char c = content.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c) ? content : new StringBuilder(content.length()).append(Character.toUpperCase(c)).append(content.substring(1)).toString());
    }

    public static String utf8Encode(String content) {
        if (!isEmpty(content) && content.getBytes().length != content.length()) {
            try {
                return URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred.", e);
            }
        }
        return content;
    }

    public static String utf8Encode(String content, String defultReturn) {
        if (!isEmpty(content) && content.getBytes().length != content.length()) {
            try {
                return URLEncoder.encode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return content;
    }
}