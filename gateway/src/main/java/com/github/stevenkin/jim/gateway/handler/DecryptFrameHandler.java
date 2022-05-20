package com.github.stevenkin.jim.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.github.stevenkin.jim.gateway.encrypt.EncryptFrame;
import com.github.stevenkin.jim.gateway.service.EncryptKeyService;
import com.github.stevenkin.jim.gateway.utils.AES;
import com.github.stevenkin.jim.gateway.utils.ConvertUtils;
import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DecryptFrameHandler extends MessageToMessageDecoder<EncryptFrame> {
    private EncryptKeyService encryptKeyService;

    public DecryptFrameHandler(EncryptKeyService encryptKeyService) {
        this.encryptKeyService = encryptKeyService;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, EncryptFrame encryptFrame, List<Object> list) throws Exception {
        log.info("read encrypt frame {}", encryptFrame);
        String encryptData = encryptFrame.getEncryptData();
        String encryptAesKey = encryptFrame.getEncryptAesKey();

        String aesKey = encryptKeyService.decrypt(encryptAesKey, encryptKeyService.getPrivateKey());
        String data = AES.decryptFromBase64(encryptData, aesKey);
        log.info("data {}", data);
        Package pkg = JSON.parseObject(data, Package.class);
        list.add(pkg);
    }
}
