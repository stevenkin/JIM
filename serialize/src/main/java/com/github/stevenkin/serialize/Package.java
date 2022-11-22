package com.github.stevenkin.serialize;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class Package {
    private Header header;

    private String body;

    public Package(Header header, String body) {
        this.header = header;
        this.body = body;
    }

    public Package(String command, String channelId, long sequence, int flag, int status, String sender, String receiver, String body) {
        this.header = new Header(command, channelId, sequence, flag, status, sender, receiver);
        this.body = body;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Header {
        private String command;

        private String channelId;

        private long sequence;

        private int flag;

        private int status;

        private String sender;

        private String receiver;
    }
}
