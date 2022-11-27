package com.github.stevenkin.serialize;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

public class PackageSerialization implements Serialization<Package> {
    @Override
    public ByteBuf serialize(Package pkg, ByteBuf byteBuf) {
        //write package header
        int n = 0;
        byte[] bytes = null;

        String command = pkg.getHeader().getCommand();
        if (StringUtils.isNotEmpty(command)) {
            bytes = command.getBytes(StandardCharsets.UTF_8);
            n = bytes.length;
        }
        byteBuf.writeInt(n);
        if (n > 0) {
            byteBuf.writeBytes(bytes);
        }

        n = 0;
        bytes = null;

        String channelId = pkg.getHeader().getChannelId();
        if (StringUtils.isNotEmpty(channelId)) {
            bytes = channelId.getBytes(StandardCharsets.UTF_8);
            n = bytes.length;
        }
        byteBuf.writeInt(n);
        if (n > 0) {
            byteBuf.writeBytes(bytes);
        }

        n = 0;
        bytes = null;

        String sessionId = pkg.getHeader().getSessionId();
        if (StringUtils.isNotEmpty(sessionId)) {
            bytes = channelId.getBytes(StandardCharsets.UTF_8);
            n = bytes.length;
        }
        byteBuf.writeInt(n);
        if (n > 0) {
            byteBuf.writeBytes(bytes);
        }

        n = 0;
        bytes = null;

        String receiver = pkg.getHeader().getReceiver();
        if (StringUtils.isNotEmpty(receiver)) {
            bytes = receiver.getBytes(StandardCharsets.UTF_8);
            n = bytes.length;
        }
        byteBuf.writeInt(n);
        if (n > 0) {
            byteBuf.writeBytes(bytes);
        }
        n = 0;
        bytes = null;

        String sender = pkg.getHeader().getSender();
        if (StringUtils.isNotEmpty(sender)) {
            bytes = sender.getBytes(StandardCharsets.UTF_8);
            n = bytes.length;
        }
        byteBuf.writeInt(n);
        if (n > 0) {
            byteBuf.writeBytes(bytes);
        }

        byteBuf.writeLong(pkg.getHeader().getSequence());
        byteBuf.writeInt(pkg.getHeader().getFlag());
        byteBuf.writeInt(pkg.getHeader().getStatus());

        //write package body
        n = 0;
        bytes = null;

        String body = pkg.getBody();
        if (StringUtils.isNotEmpty(body)) {
            bytes = body.getBytes(StandardCharsets.UTF_8);
            n = bytes.length;
        }
        byteBuf.writeInt(n);
        if (n > 0) {
            byteBuf.writeBytes(bytes);
        }

        return byteBuf;
    }

    @Override
    public Package deserialize(ByteBuf byteBuf) {
        Package pkg = new Package();
        Package.Header header = new Package.Header();
        pkg.setHeader(header);

        int n = byteBuf.readInt();
        if (n > 0) {
            ByteBuf buf = byteBuf.readBytes(n);
            String s = buf.toString(StandardCharsets.UTF_8);
            pkg.getHeader().setCommand(s);
        }
        n = 0;
        n = byteBuf.readInt();
        if (n > 0) {
            ByteBuf buf = byteBuf.readBytes(n);
            String s = buf.toString(StandardCharsets.UTF_8);
            pkg.getHeader().setChannelId(s);
        }
        n = 0;
        n = byteBuf.readInt();
        if (n > 0) {
            ByteBuf buf = byteBuf.readBytes(n);
            String s = buf.toString(StandardCharsets.UTF_8);
            pkg.getHeader().setSessionId(s);
        }
        n = 0;
        n = byteBuf.readInt();
        if (n > 0) {
            ByteBuf buf = byteBuf.readBytes(n);
            String s = buf.toString(StandardCharsets.UTF_8);
            pkg.getHeader().setReceiver(s);
        }
        n = 0;
        n = byteBuf.readInt();
        if (n > 0) {
            ByteBuf buf = byteBuf.readBytes(n);
            String s = buf.toString(StandardCharsets.UTF_8);
            pkg.getHeader().setSender(s);
        }
        pkg.getHeader().setSequence(byteBuf.readLong());
        pkg.getHeader().setFlag(byteBuf.readInt());
        pkg.getHeader().setStatus(byteBuf.readInt());
        n = 0;
        n = byteBuf.readInt();
        if (n > 0) {
            ByteBuf buf = byteBuf.readBytes(n);
            String s = buf.toString(StandardCharsets.UTF_8);
            pkg.setBody(s);
        }

        return pkg;
    }
}
