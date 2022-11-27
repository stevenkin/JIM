package com.github.stevenkin.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.commons.lang3.StringUtils;


import java.nio.charset.StandardCharsets;

public class FrameSerialization implements Serialization<Frame> {
    private Serialization<Package> packageSerialization = new PackageSerialization();

    @Override
    public ByteBuf serialize(Frame msg, ByteBuf byteBuf) {
        int n = 0;
        byte[] bytes = null;
        int opCode = msg.getOpCode();
        byteBuf.writeInt(opCode);

        String clientIP = msg.getControl().getClientIP();
        if (StringUtils.isNotEmpty(clientIP)) {
            bytes = clientIP.getBytes(StandardCharsets.UTF_8);
            n = bytes.length;
        }
        byteBuf.writeInt(n);
        if (n > 0) {
            byteBuf.writeBytes(bytes);
        }
        n = 0;
        bytes = null;

        int clientPort = msg.getControl().getClientPort();
        byteBuf.writeInt(clientPort);

        String appName = msg.getControl().getAppName();
        if (StringUtils.isNotEmpty(appName)) {
            bytes = appName.getBytes(StandardCharsets.UTF_8);
            n = bytes.length;
        }
        byteBuf.writeInt(n);
        if (n > 0) {
            byteBuf.writeBytes(bytes);
        }
        n = 0;
        bytes = null;

        String appToken = msg.getControl().getAppToken();
        if (StringUtils.isNotEmpty(appToken)) {
            bytes = appToken.getBytes(StandardCharsets.UTF_8);
            n = bytes.length;
        }
        byteBuf.writeInt(n);
        if (n > 0) {
            byteBuf.writeBytes(bytes);
        }
        n = 0;
        bytes = null;

        long timestamp = msg.getControl().getTimestamp();
        byteBuf.writeLong(timestamp);

        int appWaitTaskNum = msg.getControl().getAppWaitTaskNum();
        byteBuf.writeInt(appWaitTaskNum);

        int appThreadNum = msg.getControl().getAppThreadNum();
        byteBuf.writeInt(appThreadNum);

        ByteBuf byteBuf1 = null;
        if (msg.getPayload() != null) {
            byteBuf1 = packageSerialization.serialize(msg.getPayload(), PooledByteBufAllocator.DEFAULT.buffer());
            byteBuf.writeInt(byteBuf1.readableBytes());
            byteBuf.writeBytes(byteBuf1);
        } else {
            byteBuf.writeInt(0);
        }

        return byteBuf;
    }

    @Override
    public Frame deserialize(ByteBuf byteBuf) {
        Frame frame = new Frame();
        frame.setControl(new Frame.Control());
        frame.setOpCode(byteBuf.readInt());

        int n = byteBuf.readInt();
        if (n > 0) {
            ByteBuf buf = byteBuf.readBytes(n);
            String s = buf.toString(StandardCharsets.UTF_8);
            frame.getControl().setClientIP(s);
        }
        n = 0;

        frame.getControl().setClientPort(byteBuf.readInt());

        if (n > 0) {
            ByteBuf buf = byteBuf.readBytes(n);
            String s = buf.toString(StandardCharsets.UTF_8);
            frame.getControl().setAppName(s);
        }
        n = 0;

        if (n > 0) {
            ByteBuf buf = byteBuf.readBytes(n);
            String s = buf.toString(StandardCharsets.UTF_8);
            frame.getControl().setAppToken(s);
        }
        n = 0;

        frame.getControl().setTimestamp(byteBuf.readLong());
        frame.getControl().setAppWaitTaskNum(byteBuf.readInt());
        frame.getControl().setAppThreadNum(byteBuf.readInt());

        int length = byteBuf.readInt();
        if (length > 0) {
            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer();
            buffer.writeBytes(byteBuf.readBytes(length));
            Package aPackage = packageSerialization.deserialize(buffer);
            frame.setPayload(aPackage);
        }
        return frame;
    }
}
