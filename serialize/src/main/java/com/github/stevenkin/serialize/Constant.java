package com.github.stevenkin.serialize;

public interface Constant {
    byte[] LOGIN_MAGIC = {(byte) 0xc3, (byte) 0x11, (byte) 0xa3, (byte) 0x65};

    byte[] PING_MAGIC = {(byte) 0xc3, (byte) 0x15, (byte) 0xa7, (byte) 0x65};

    byte[] PONG_MAGIC = {(byte) 0xc3, (byte) 0x17, (byte) 0xab, (byte) 0x65};

}
