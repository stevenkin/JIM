package com.github.stevenkin.jim.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.github.stevenkin.jim.gateway.encrypt.EncryptFrame;
import com.github.stevenkin.jim.gateway.service.EncryptKeyService;
import com.github.stevenkin.jim.gateway.utils.AES;
import com.github.stevenkin.jim.gateway.utils.ConvertUtils;
import com.github.stevenkin.jim.gateway.utils.KeyPairGenUtil;
import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.github.stevenkin.serialize.Constant.CLIENT_PUBLIC_KEY;

@Slf4j
public class EncryptFrameHandler extends ChannelOutboundHandlerAdapter {
    private EncryptKeyService encryptKeyService;

    public EncryptFrameHandler(EncryptKeyService encryptKeyService) {
        this.encryptKeyService = encryptKeyService;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof Package)) {
            super.write(ctx, msg, promise);
            return;
        }
        Package pkg = (Package) msg;
        log.info("write package {}", pkg);
        String aesKey = AES.genarateRandomKeyWithBase64();;
        Attribute<Object> attr = ctx.channel().attr(AttributeKey.valueOf(CLIENT_PUBLIC_KEY));
        String clientPublicKey = (String) attr.get();

        String data = JSON.toJSONString(pkg);

        String s = AES.encryptToBase64(data, aesKey);
        String encrypt = KeyPairGenUtil.encrypt(aesKey, clientPublicKey);

        EncryptFrame encryptFrame = new EncryptFrame();
        encryptFrame.setEncryptData(s);
        encryptFrame.setEncryptAesKey(encrypt);
        String json = JSON.toJSONString(encryptFrame);

        TextWebSocketFrame frame = new TextWebSocketFrame(json);
        ctx.writeAndFlush(frame);
    }
}
