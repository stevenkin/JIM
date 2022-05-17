package com.github.stevenkin.jim.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.github.stevenkin.jim.gateway.encrypt.EncryptFrame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        ByteBuf content = webSocketFrame.content();
        String s = content.toString(CharsetUtil.UTF_8);
        log.info("read websocket frame {}", s);
        handleWebSocketFrame(channelHandlerContext, webSocketFrame);
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof PingWebSocketFrame) {
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
        } else if (frame instanceof CloseWebSocketFrame) {
            ctx.writeAndFlush(frame.retainedDuplicate()).addListener(ChannelFutureListener.CLOSE);
        } else if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) frame;
            String content = textWebSocketFrame.text();
            EncryptFrame encryptFrame = JSON.parseObject(content, EncryptFrame.class);
            ctx.fireChannelRead(encryptFrame);
        } else if (frame instanceof BinaryWebSocketFrame) {
            log.info("ignore BinaryWebSocketFrame {}", frame);
        }
    }
}
