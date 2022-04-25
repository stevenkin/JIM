package com.github.stevenkin.serialize;

import lombok.Getter;

@Getter
public abstract class Package {
    private byte[] magic;

    public Package(byte[] magic) {
        this.magic = magic;
    }

    public abstract PackageType getType();
}
