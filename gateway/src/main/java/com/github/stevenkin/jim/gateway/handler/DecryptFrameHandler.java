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

import java.util.List;

public class DecryptFrameHandler extends MessageToMessageDecoder<EncryptFrame> {
    private EncryptKeyService encryptKeyService;

    public DecryptFrameHandler(EncryptKeyService encryptKeyService) {
        this.encryptKeyService = encryptKeyService;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, EncryptFrame encryptFrame, List<Object> list) throws Exception {
        String encryptData = encryptFrame.getEncryptData();
        String encryptAesKey = encryptFrame.getEncryptAesKey();

        String aesKey = encryptKeyService.decrypt(encryptAesKey, encryptKeyService.getPrivateKey());
        String data = ConvertUtils.hexStringToString(AES.decryptFromBase64(encryptData, aesKey));
        Package pkg = JSON.parseObject(data, Package.class);
        list.add(pkg);
    }
}
