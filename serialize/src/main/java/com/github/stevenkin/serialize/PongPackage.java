package com.github.stevenkin.serialize;

import lombok.Getter;

@Getter
public class PongPackage extends Package{
    private int length;

    private byte[] body;

    public PongPackage(byte[] magic, int length, byte[] body) {
        super(magic);
        this.length = length;
        this.body = body;
    }

    @Override
    public PackageType getType() {
        return PackageType.PONG;
    }
}
