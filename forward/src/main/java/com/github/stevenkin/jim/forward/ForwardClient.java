package com.github.stevenkin.jim.forward;

import com.github.stevenkin.serialize.Frame;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ForwardClient {
    private String host;

    private int port;

    private Bootstrap clientBootstrap;

    private NioEventLoopGroup clientGroup;

    public ForwardClient() {
        clientBootstrap = new Bootstrap();
        clientGroup = new NioEventLoopGroup();
    }

    public void connect(String ip, int port) throws Exception {
        clientBootstrap.group(clientGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    //初始化时将handler设置到ChannelPipeline
                    @Override
                    public void initChannel(SocketChannel ch) {
                        //ch.pipeline().addLast("logs", new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(10 * 3, 15 * 3, 20 * 3));
                        ch.pipeline().addLast(new ProxyMessageDecoder(2 * 1024 * 1024, 0, 4));
                        ch.pipeline().addLast(new ProxyMessageEncoder());
                        ch.pipeline().addLast(new LoginAuthReqHandler());
                        ch.pipeline().addLast(new HeartBeatReqHandler());
                        ch.pipeline().addLast(new ClientHandler(realServerBootstrap));
                        ch.pipeline().addLast(new MyHttpRequestDecoder());
                        ch.pipeline().addLast(new MyHttpObjectAggregator(maxContentLength));
                        ch.pipeline().addLast(new HttpReceiveHandler());
                    }
                });
    }

    public void send(Frame frame) throws Exception {

    }

    public void disconnect() throws Exception {

    }
}
