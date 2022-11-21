package com.github.stevenkin.jim.forward;

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
    private Bootstrap clientBootstrap;

    private NioEventLoopGroup clientGroup;

    private Serialization serialization;

    private Channel channel;

    private volatile boolean success = false;

    public ForwardClient(Serialization serialization) {
        this.clientBootstrap = new Bootstrap();
        this.clientGroup = new NioEventLoopGroup();
        this.serialization = serialization;

        clientBootstrap.group(clientGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
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
    }

    public void connect(String ip, int port) throws Exception {
        /**
         * 最多尝试5次和服务端连接
         */
        this.channel = doConnect(ip, port, 5);
        this.success = true;
    }

    private Channel doConnect(String ip, int port, int retry) throws InterruptedException {
        ChannelFuture future = null;
        for (int i = retry; i > 0; i--) {
            try {
                future = clientBootstrap.connect(ip, port).sync();
            } catch (InterruptedException e) {
                log.debug("debug:connect business server fail, client " + NetworkUtil.getLocalHost() + ", server " + ip + ":" + port);
            }
            if (future.isSuccess()) {
                return future.channel();
            }
            Thread.sleep(5000);
        }
        throw new RuntimeException("connect business server fail, client " + NetworkUtil.getLocalHost() + ", server " + ip + ":" + port);
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
