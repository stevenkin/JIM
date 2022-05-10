package com.github.stevenkin.serialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Package {
    private Header header;

    private byte[] body;

    public Package(Header header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public Package(String command, String channelId, long sequence, int flag, int status, String sender, String receiver, int length, byte[] body) {
        this.header = new Header(command, channelId, sequence, flag, status, sender, receiver, length);
        this.body = body;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Header {
        private String command;

        private String channelId;

        private long sequence;

        private int flag;

        private int status;

        private String sender;

        private String receiver;

        private int length;
    }
}
