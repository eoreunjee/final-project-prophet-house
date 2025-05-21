package com.ssafy.home.util;

public class KMPUtil {

    // 접두사 배열 생성
    public static int[] buildLps(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                lps[i++] = ++len;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
        }
        return lps;
    }

    // KMP 문자열 포함 여부 확인
    public static boolean kmpContains(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) return true;

        int[] lps = buildLps(pattern);
        int i = 0, j = 0;

        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++; j++;
                if (j == pattern.length()) return true;
            } else {
                if (j != 0) j = lps[j - 1];
                else i++;
            }
        }

        return false;
    }
}
