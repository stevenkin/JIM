package com.github.stevenkin.jim.gateway.utils;

import java.security.SecureRandom;

public class SecureRandomUtil {
    public static SecureRandom random = new SecureRandom();

    public SecureRandomUtil() {
    }

    public static String getRandom(int length) {
        StringBuilder ret = new StringBuilder();

        for(int i = 0; i < length; ++i) {
            boolean isChar = random.nextInt(2) % 2 == 0;
            if (isChar) {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                ret.append((char)(choice + random.nextInt(26)));
            } else {
                ret.append(Integer.toString(random.nextInt(10)));
            }
        }

        return ret.toString();
    }

    public static String getRandomNum(int length) {
        StringBuilder ret = new StringBuilder();

        for(int i = 0; i < length; ++i) {
            ret.append(Integer.toString(random.nextInt(10)));
        }

        return ret.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRandom(5));
    }
}

