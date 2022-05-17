package com.github.stevenkin.jim.gateway.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class KeyPairGenUtil {
    private static final int KEY_SIZE = 2048;
    private static Map<Integer, String> keyMap = new HashMap();

    public KeyPairGenUtil() {
    }

    public static Map<Integer, String> genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        String publicKeyString = java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded());
        keyMap.put(0, publicKeyString);
        keyMap.put(1, privateKeyString);
        return new HashMap<>(keyMap);
    }

    public static String encrypt(String str) throws Exception {
        return encrypt(str, keyMap.get(0));
    }

    public static String decrypt(String str) throws Exception {
        return decrypt(str, keyMap.get(1));
    }

    public static String encrypt(String str, String publicKey) throws Exception {
        byte[] decoded = java.util.Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, pubKey);
        String outStr = java.util.Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    public static String decrypt(String str, String privateKey) throws Exception {
        byte[] inputByte = java.util.Base64.getDecoder().decode(str);
        byte[] decoded = Base64.getDecoder().decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static void main(String[] args) throws Exception {
        long temp = System.currentTimeMillis();
        genKeyPair();
        System.out.println("公钥:" + (String)keyMap.get(0));
        System.out.println("私钥:" + (String)keyMap.get(1));
        System.out.println("生成密钥消耗时间:" + (double)(System.currentTimeMillis() - temp) / 1000.0D + "秒");
        String message = "RSA测试ABCD~!@#$";
        System.out.println("原文:" + message);
        temp = System.currentTimeMillis();
        String messageEn = encrypt(message, (String)keyMap.get(0));
        System.out.println("密文:" + messageEn);
        System.out.println("加密消耗时间:" + (double)(System.currentTimeMillis() - temp) / 1000.0D + "秒");
        temp = System.currentTimeMillis();
        String messageDe = decrypt(messageEn, (String)keyMap.get(1));
        System.out.println("解密:" + messageDe);
        System.out.println("解密消耗时间:" + (double)(System.currentTimeMillis() - temp) / 1000.0D + "秒");
    }

    public static String getPublicKey() {
        return keyMap.get(0);
    }
}

