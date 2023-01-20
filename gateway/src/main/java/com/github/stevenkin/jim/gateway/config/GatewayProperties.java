package com.github.stevenkin.jim.gateway.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

@Component
public class GatewayProperties {
    private final String HOST;
    private final int PORT;
    private final Set<String> PATH_SET;
    private final int BOSS_LOOP_GROUP_THREADS;
    private final int WORKER_LOOP_GROUP_THREADS;
    private final boolean USE_COMPRESSION_HANDLER;
    private final int CONNECT_TIMEOUT_MILLIS;
    private final int SO_BACKLOG;
    private final int WRITE_SPIN_COUNT;
    private final int WRITE_BUFFER_HIGH_WATER_MARK;
    private final int WRITE_BUFFER_LOW_WATER_MARK;
    private final int SO_RCVBUF;
    private final int SO_SNDBUF;
    private final boolean TCP_NODELAY;
    private final boolean SO_KEEPALIVE;
    private final int SO_LINGER;
    private final boolean ALLOW_HALF_CLOSURE;
    private final int READER_IDLE_TIME_SECONDS;
    private final int WRITER_IDLE_TIME_SECONDS;
    private final int ALL_IDLE_TIME_SECONDS;
    private final int MAX_FRAME_PAYLOAD_LENGTH;
    private static Integer randomPort;

    public GatewayProperties(@Value("${gateway.host}")String host, @Value("${gateway.port}")int port, @Value("${gateway.forwardPort}")int forwardPort, @Value("${gateway.path}")String path, @Value("${gateway.bossLoopGroupThreads}")int bossLoopGroupThreads, @Value("${gateway.workerLoopGroupThreads}")int workerLoopGroupThreads, @Value("${gateway.useCompressionHandler}")boolean useCompressionHandler, @Value("${gateway.connectTimeoutMillis}")int connectTimeoutMillis, @Value("${gateway.soBacklog}")int soBacklog, @Value("${gateway.writeSpinCount}")int writeSpinCount, @Value("${gateway.writeBufferHighWaterMark}")int writeBufferHighWaterMark, @Value("${gateway.writeBufferLowWaterMark}")int writeBufferLowWaterMark, @Value("${gateway.soRcvbuf}")int soRcvbuf, @Value("${gateway.soSndbuf}")int soSndbuf, @Value("${gateway.tcpNodelay}")boolean tcpNodelay, @Value("${gateway.soKeepalive}")boolean soKeepalive, @Value("${gateway.soLinger}")int soLinger, @Value("${gateway.allowHalfClosure}")boolean allowHalfClosure, @Value("${gateway.readerIdleTimeSeconds}")int readerIdleTimeSeconds, @Value("${gateway.writerIdleTimeSeconds}")int writerIdleTimeSeconds, @Value("${gateway.allIdleTimeSeconds}")int allIdleTimeSeconds, @Value("${gateway.maxFramePayloadLength}")int maxFramePayloadLength) {
        if (!StringUtils.isEmpty(host) && !"0.0.0.0".equals(host) && !"0.0.0.0/0.0.0.0".equals(host)) {
            this.HOST = host;
        } else {
            this.HOST = "0.0.0.0";
        }

        this.PORT = this.getAvailablePort(port);
        this.PATH_SET = new HashSet();
        this.addPath(path);
        this.BOSS_LOOP_GROUP_THREADS = bossLoopGroupThreads;
        this.WORKER_LOOP_GROUP_THREADS = workerLoopGroupThreads;
        this.USE_COMPRESSION_HANDLER = useCompressionHandler;
        this.CONNECT_TIMEOUT_MILLIS = connectTimeoutMillis;
        this.SO_BACKLOG = soBacklog;
        this.WRITE_SPIN_COUNT = writeSpinCount;
        this.WRITE_BUFFER_HIGH_WATER_MARK = writeBufferHighWaterMark;
        this.WRITE_BUFFER_LOW_WATER_MARK = writeBufferLowWaterMark;
        this.SO_RCVBUF = soRcvbuf;
        this.SO_SNDBUF = soSndbuf;
        this.TCP_NODELAY = tcpNodelay;
        this.SO_KEEPALIVE = soKeepalive;
        this.SO_LINGER = soLinger;
        this.ALLOW_HALF_CLOSURE = allowHalfClosure;
        this.READER_IDLE_TIME_SECONDS = readerIdleTimeSeconds;
        this.WRITER_IDLE_TIME_SECONDS = writerIdleTimeSeconds;
        this.ALL_IDLE_TIME_SECONDS = allIdleTimeSeconds;
        this.MAX_FRAME_PAYLOAD_LENGTH = maxFramePayloadLength;
    }

    public String addPath(String path) {
        if (StringUtils.isEmpty(path)) {
            path = "/";
        }

        if (this.PATH_SET.contains(path)) {
            throw new RuntimeException("ServerEndpointConfig.addPath  path:" + path + " are repeat.");
        } else {
            this.PATH_SET.add(path);
            return path;
        }
    }

    private int getAvailablePort(int port) {
        if (port != 0) {
            return port;
        } else if (randomPort != null && randomPort != 0) {
            return randomPort;
        } else {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(0);
            Socket socket = new Socket();

            try {
                socket.bind(inetSocketAddress);
            } catch (IOException var7) {
                var7.printStackTrace();
            }

            int localPort = socket.getLocalPort();

            try {
                socket.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            randomPort = localPort;
            return localPort;
        }
    }

    public String getHost() {
        return this.HOST;
    }

    public int getPort() {
        return this.PORT;
    }

    public Set<String> getPathSet() {
        return this.PATH_SET;
    }

    public int getBossLoopGroupThreads() {
        return this.BOSS_LOOP_GROUP_THREADS;
    }

    public int getWorkerLoopGroupThreads() {
        return this.WORKER_LOOP_GROUP_THREADS;
    }

    public boolean isUseCompressionHandler() {
        return this.USE_COMPRESSION_HANDLER;
    }

    public int getConnectTimeoutMillis() {
        return this.CONNECT_TIMEOUT_MILLIS;
    }

    public int getSoBacklog() {
        return this.SO_BACKLOG;
    }

    public int getWriteSpinCount() {
        return this.WRITE_SPIN_COUNT;
    }

    public int getWriteBufferHighWaterMark() {
        return this.WRITE_BUFFER_HIGH_WATER_MARK;
    }

    public int getWriteBufferLowWaterMark() {
        return this.WRITE_BUFFER_LOW_WATER_MARK;
    }

    public int getSoRcvbuf() {
        return this.SO_RCVBUF;
    }

    public int getSoSndbuf() {
        return this.SO_SNDBUF;
    }

    public boolean isTcpNodelay() {
        return this.TCP_NODELAY;
    }

    public boolean isSoKeepalive() {
        return this.SO_KEEPALIVE;
    }

    public int getSoLinger() {
        return this.SO_LINGER;
    }

    public boolean isAllowHalfClosure() {
        return this.ALLOW_HALF_CLOSURE;
    }

    public static Integer getRandomPort() {
        return randomPort;
    }

    public int getReaderIdleTimeSeconds() {
        return this.READER_IDLE_TIME_SECONDS;
    }

    public int getWriterIdleTimeSeconds() {
        return this.WRITER_IDLE_TIME_SECONDS;
    }

    public int getAllIdleTimeSeconds() {
        return this.ALL_IDLE_TIME_SECONDS;
    }

    public int getmaxFramePayloadLength() {
        return this.MAX_FRAME_PAYLOAD_LENGTH;
    }
}
