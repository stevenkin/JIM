//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.github.stevenkin.jim.gateway.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
public class Digest {
    public static final String ENCODE = "UTF-8";

    public Digest() {
    }

    public static String signMD5(String aValue, String encoding) {
        try {
            byte[] input = aValue.getBytes(encoding);
            MessageDigest md = MessageDigest.getInstance("MD5");
            return ConvertUtils.toHex(md.digest(input));
        } catch (NoSuchAlgorithmException var4) {
            log.error(var4.getMessage(), var4);
            return null;
        } catch (UnsupportedEncodingException var5) {
            log.error(var5.getMessage(), var5);
            return null;
        }
    }

    public static String hmacSign(String aValue) {
        try {
            byte[] input = aValue.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            return ConvertUtils.toHex(md.digest(input));
        } catch (NoSuchAlgorithmException var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static String hmacSign(String aValue, String aKey) {
        return hmacSign(aValue, aKey, "UTF-8");
    }

    public static String hmacSign(String aValue, String aKey, String encoding) {
        byte[] k_ipad = new byte[64];
        byte[] k_opad = new byte[64];

        byte[] keyb;
        byte[] value;
        try {
            keyb = aKey.getBytes(encoding);
            value = aValue.getBytes(encoding);
        } catch (UnsupportedEncodingException var10) {
            keyb = aKey.getBytes();
            value = aValue.getBytes();
        }

        Arrays.fill(k_ipad, keyb.length, 64, (byte)54);
        Arrays.fill(k_opad, keyb.length, 64, (byte)92);

        for(int i = 0; i < keyb.length; ++i) {
            k_ipad[i] = (byte)(keyb[i] ^ 54);
            k_opad[i] = (byte)(keyb[i] ^ 92);
        }

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var9) {
            log.error(var9.getMessage(), var9);
            return null;
        }

        md.update(k_ipad);
        md.update(value);
        byte[] dg = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return ConvertUtils.toHex(dg);
    }

    public static String hmacSHASign(String aValue, String aKey, String encoding) {
        byte[] k_ipad = new byte[64];
        byte[] k_opad = new byte[64];

        byte[] keyb;
        byte[] value;
        try {
            keyb = aKey.getBytes(encoding);
            value = aValue.getBytes(encoding);
        } catch (UnsupportedEncodingException var10) {
            keyb = aKey.getBytes();
            value = aValue.getBytes();
        }

        Arrays.fill(k_ipad, keyb.length, 64, (byte)54);
        Arrays.fill(k_opad, keyb.length, 64, (byte)92);

        for(int i = 0; i < keyb.length; ++i) {
            k_ipad[i] = (byte)(keyb[i] ^ 54);
            k_opad[i] = (byte)(keyb[i] ^ 92);
        }

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException var9) {
            log.error(var9.getMessage(), var9);
            return null;
        }

        md.update(k_ipad);
        md.update(value);
        byte[] dg = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 20);
        dg = md.digest();
        return ConvertUtils.toHex(dg);
    }

    public static String digest(String aValue) {
        return digest(aValue, "UTF-8");
    }

    public static String digest(String aValue, String encoding) {
        aValue = aValue.trim();

        byte[] value;
        try {
            value = aValue.getBytes(encoding);
        } catch (UnsupportedEncodingException var6) {
            value = aValue.getBytes();
        }

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException var5) {
            log.error(var5.getMessage(), var5);
            return null;
        }

        return ConvertUtils.toHex(md.digest(value));
    }

    public static String digest(String aValue, String alg, String encoding) {
        aValue = aValue.trim();

        byte[] value;
        try {
            value = aValue.getBytes(encoding);
        } catch (UnsupportedEncodingException var7) {
            value = aValue.getBytes();
        }

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance(alg);
        } catch (NoSuchAlgorithmException var6) {
            log.error(var6.getMessage(), var6);
            return null;
        }

        return ConvertUtils.toHex(md.digest(value));
    }

    public static String udpSign(String aValue) {
        try {
            byte[] input = aValue.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return new String(Base64Sign.encode(md.digest(input)), "UTF-8");
        } catch (Exception var3) {
            return null;
        }
    }
}
