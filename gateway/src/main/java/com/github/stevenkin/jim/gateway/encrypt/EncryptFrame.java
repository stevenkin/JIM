package com.github.stevenkin.jim.gateway.encrypt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptFrame {
    private String encryptAesKey;

    private String encryptData;
}
