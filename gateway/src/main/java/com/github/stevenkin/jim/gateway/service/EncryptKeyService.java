package com.github.stevenkin.jim.gateway.service;

import com.github.stevenkin.jim.gateway.utils.KeyPairGenUtil;
import com.github.stevenkin.jim.gateway.utils.SecureRandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class EncryptKeyService implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        KeyPairGenUtil.genKeyPair();
    }

    public void refresh() {
        try {
            KeyPairGenUtil.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("refresh key error {}", e);
        }
    }

    public String genAesKey() {
        return SecureRandomUtil.getRandom(16);
    }

    public String getPublicKey() {
        return KeyPairGenUtil.getPublicKey();
    }

    public String getPrivateKey() {
        return KeyPairGenUtil.getPrivateKey();
    }

    public String encrypt(String data, String key) throws Exception {
        return KeyPairGenUtil.encrypt(data, key);
    }

    public String decrypt(String data, String key) throws Exception {
        return KeyPairGenUtil.decrypt(data, key);
    }
}
