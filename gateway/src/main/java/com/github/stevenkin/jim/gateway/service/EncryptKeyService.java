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
    private String aesKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        KeyPairGenUtil.genKeyPair();
        this.aesKey = SecureRandomUtil.getRandom(16);
    }

    public void refresh() {
        try {
            KeyPairGenUtil.genKeyPair();
            this.aesKey = SecureRandomUtil.getRandom(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("refresh key error {}", e);
        }
    }

    public String getAesKey() {
        return this.aesKey;
    }

    public String getPublicKey() {
        return KeyPairGenUtil.getPublicKey();
    }

    public String encrypt(String data) throws Exception {
        return KeyPairGenUtil.encrypt(data);
    }

    public String decrypt(String data) throws Exception {
        return KeyPairGenUtil.decrypt(data);
    }
}
