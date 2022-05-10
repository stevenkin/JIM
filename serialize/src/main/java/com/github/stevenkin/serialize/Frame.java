package com.github.stevenkin.serialize;

import lombok.Data;

@Data
public class Frame {
    private int opCode;

    private int length;

    private byte[] payload;
}
