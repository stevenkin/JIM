package com.github.stevenkin.jim.gateway.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AES {
    public AES() {
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        CheckUtils.notEmpty(data, "data");
        CheckUtils.notEmpty(key, "key");
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        } else {
            try {
                SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(key);
                cipher.init(1, seckey, iv);
                byte[] result = cipher.doFinal(data);
                return result;
            } catch (Exception var8) {
                var8.printStackTrace();
                throw new RuntimeException("encrypt fail!", var8);
            }
        }
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        CheckUtils.notEmpty(data, "data");
        CheckUtils.notEmpty(key, "key");
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        } else {
            try {
                SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(key);
                cipher.init(2, seckey, iv);
                byte[] result = cipher.doFinal(data);
                return result;
            } catch (Exception var8) {
                var8.printStackTrace();
                throw new RuntimeException("decrypt fail!", var8);
            }
        }
    }

    public static String encryptToBase64(String data, String key) {
        try {
            byte[] valueByte = encrypt(data.getBytes("UTF-8"), Base64.getDecoder().decode(key));
            return new String(Base64.getEncoder().encode(valueByte));
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException("encrypt fail!", var3);
        }
    }

    public static String decryptFromBase64(String data, String key) {
        try {
            byte[] originalData = Base64.getDecoder().decode(data);
            byte[] valueByte = decrypt(originalData, Base64.getDecoder().decode(key));
            return new String(valueByte, "UTF-8");
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException("decrypt fail!", var4);
        }
    }

    public static String encryptWithKeyBase64(String data, String key) {
        try {
            byte[] valueByte = encrypt(data.getBytes("UTF-8"), Base64Sign.decode(key.getBytes()));
            return new String(Base64Sign.encode(valueByte));
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException("encrypt fail!", var3);
        }
    }

    public static String decryptWithKeyBase64(String data, String key) {
        try {
            byte[] originalData = Base64Sign.decode(data.getBytes());
            byte[] valueByte = decrypt(originalData, Base64Sign.decode(key.getBytes()));
            return new String(valueByte, "UTF-8");
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException("decrypt fail!", var4);
        }
    }

    public static byte[] genarateRandomKey() {
        KeyGenerator keygen = null;

        try {
            keygen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException var3) {
            throw new RuntimeException(" genarateRandomKey fail!", var3);
        }

        SecureRandom random = new SecureRandom();
        keygen.init(random);
        Key key = keygen.generateKey();
        return key.getEncoded();
    }

    public static String genarateRandomKeyWithBase64() {
        return new String(Base64.getEncoder().encode(genarateRandomKey()));
    }

}
