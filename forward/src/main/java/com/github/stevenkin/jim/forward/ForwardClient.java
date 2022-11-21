package com.github.stevenkin.jim.forward;

import com.github.stevenkin.serialize.Frame;
import com.github.stevenkin.serialize.Package;
import com.github.stevenkin.serialize.Serialization;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ForwardClient {
    private String host;

    private int port;

    private Bootstrap clientBootstrap;

    private NioEventLoopGroup clientGroup;

    private Serialization serialization;

    private Channel channel;

    private volatile boolean success = false;

    public ForwardClient(Serialization serialization) {
        this.clientBootstrap = new Bootstrap();
        this.clientGroup = new NioEventLoopGroup();
        this.serialization = serialization;
    }

    public void connect(String ip, int port) throws Exception {
        clientBootstrap.group(clientGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    //初始化时将handler设置到ChannelPipeline
                    @Override
                    public void initChannel(SocketChannel ch) {
                        //ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(10 * 3, 15 * 3, 20 * 3));
                        ch.pipeline().addLast(new PackageDecoder(serialization));
                        ch.pipeline().addLast(new PackageEncoder(serialization));
                        ch.pipeline().addLast(new FrameDecoder());
                        ch.pipeline().addLast(new FrameEncoder());
                        ch.pipeline().addLast("logs", new LoggingHandler(LogLevel.DEBUG));
                    }
                });
        /**
         * 最多尝试5次和服务端连接
         */
        this.channel = doConnect(5);
        this.success = true;
    }

    private Channel doConnect(int retry) throws InterruptedException {
        ChannelFuture future = null;
        for (int i = retry; i > 0; i++) {
            future = clientBootstrap.connect(host, port).sync();
            if (future.isSuccess()) {
                return future.channel();
            }
            Thread.sleep(1000);
        }
        throw new RuntimeException("connect business server fail, client " + NetworkUtil.getLocalHost() + ", server " + host + ":" + port);
    }

    public void send(Package pkg) throws Exception {
        if (!success) {
            throw new RuntimeException("no connect!");
        }
        channel.writeAndFlush(pkg);
    }

    public void disconnect() throws Exception {
        if (!success) {
            throw new RuntimeException("no connect!");
        }
        channel.close().sync();
    }
}
