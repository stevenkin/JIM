package com.github.stevenkin.jim.business.server.client;


import com.github.stevenkin.jim.forward.FrameDecoder;
import com.github.stevenkin.jim.forward.FrameEncoder;
import com.github.stevenkin.jim.forward.PackageDecoder;
import com.github.stevenkin.jim.forward.PackageEncoder;
import com.github.stevenkin.serialize.Package;
import com.github.stevenkin.serialize.Serialization;
import com.github.stevenkin.serialize.PackageSerialization;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class ClientV0 {

    public static void main(String[] args) throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.option(NioChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);

        NioEventLoopGroup group = new NioEventLoopGroup();

        Serialization serialization = new PackageSerialization();
        try {

            bootstrap.group(group);

            LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);

            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new FrameDecoder());
                    pipeline.addLast(new FrameEncoder());
                    pipeline.addLast(new PackageDecoder(serialization));
                    pipeline.addLast(new PackageEncoder(serialization));
                    pipeline.addLast(loggingHandler);

                }
            });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);

            channelFuture.sync();

            Package pkg = new Package();

            Package.Header header = new Package.Header();

            header.setStatus(0);
            header.setFlag(0);
            header.setSequence(1000);
            header.setSender("sender");
            header.setReceiver("receiver");
            header.setCommand("echo");
            header.setChannelId("111");
            pkg.setHeader(header);

            pkg.setBody("data111");

            channelFuture.channel().writeAndFlush(pkg);

            channelFuture.channel().closeFuture().sync();

        } finally{
            group.shutdownGracefully();
        }

    }

}
