package com.github.stevenkin.jim.gateway;

import com.github.stevenkin.jim.gateway.utils.KeyPairGenUtil;

import java.security.NoSuchAlgorithmException;

public class ClientPublicKeyTest {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenUtil.genKeyPair();
        System.out.println(KeyPairGenUtil.getPublicKey());
    }
}
