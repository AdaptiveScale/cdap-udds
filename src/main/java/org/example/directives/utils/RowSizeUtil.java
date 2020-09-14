package org.example.directives.utils;

public class RowSizeUtil {
    public static int getStringSize(String str) {
        int strLen = str.length() * 2 + 38;
        int mod = strLen % 8;
        if(mod != 0)
            strLen += 8 - mod;
        return strLen;
    }
}
