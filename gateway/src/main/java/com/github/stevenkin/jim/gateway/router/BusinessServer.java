package com.github.stevenkin.jim.gateway.router;

import com.github.stevenkin.serialize.Frame;
import io.netty.channel.Channel;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BusinessServer implements Server{
    private Channel channel;

    private ScheduledExecutorService keepaliveService;

    private String businessIP;

    private Integer businessPort;

    private String routeIP;

    private Integer routePort;

    private String appName;

    private String appToken;

    private String selfAppName;

    private String selfAppToken;

    private AtomicReference<ServerInfo> serverInfo;

    private long lastRecentUpdateTime;

    public BusinessServer(Channel channel, ScheduledExecutorService keepaliveService, String selfAppName, String selfAppToken, ServerInfo info, String routeIP, Integer routePort) {
        this.channel = channel;
        this.keepaliveService = keepaliveService;
        this.businessIP = info.getClientIP();
        this.businessPort = info.getClientPort();
        this.routeIP = routeIP;
        this.routePort = routePort;
        this.appName = info.getAppName();
        this.appToken = info.getAppToken();
        this.selfAppName = selfAppName;
        this.selfAppToken = selfAppToken;

        this.serverInfo = new AtomicReference<>(info);
        this.lastRecentUpdateTime = System.currentTimeMillis();

        this.keepaliveService.schedule(() -> {
            Frame keepaliveFrame = keepaliveFrame();
            channel.writeAndFlush(keepaliveFrame);
        }, 5, TimeUnit.SECONDS);
    }

    @Override
    public void update(ServerInfo info) {
        serverInfo.set(info);
        lastRecentUpdateTime = System.currentTimeMillis();
    }

    @Override
    public boolean isActive() {
        return (System.currentTimeMillis() - lastRecentUpdateTime) / 1000 > 15;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public ServerInfo info() {
        return serverInfo.get();
    }

    private Frame keepaliveFrame() {
        Frame frame = new Frame();
        Frame.Control control = new Frame.Control();
        control.setSourceIP(routeIP);
        control.setSourcePort(routePort);
        control.setDestIP(businessIP);
        control.setDestPort(businessPort);
        control.setSourceAppName(selfAppName);
        control.setSourceAppToken(selfAppToken);
        control.setDestAppName(appName);
        control.setDestAppToken(appToken);
        frame.setControl(control);
        frame.setOpCode(0x5);
        frame.setPayload(null);
        return frame;
    }
}
