package com.github.stevenkin.serialize;

import lombok.Getter;

import static com.github.stevenkin.serialize.Constant.*;

public enum PackageType {
    LOGIN(LOGIN_MAGIC),

    PING(PING_MAGIC),

    PONG(PONG_MAGIC);

    @Getter
    private final byte[] magic;

    PackageType(byte[] magic) {
        this.magic = magic;
    }
}
