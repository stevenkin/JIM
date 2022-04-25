package com.github.stevenkin.serialize;

import lombok.Getter;

@Getter
public class PingPackage extends Package{
    private int length;

    private byte[] body;

    public PingPackage(byte[] magic, int length, byte[] body) {
        super(magic);
        this.length = length;
        this.body = body;
    }

    @Override
    public PackageType getType() {
        return PackageType.PING;
    }
}
