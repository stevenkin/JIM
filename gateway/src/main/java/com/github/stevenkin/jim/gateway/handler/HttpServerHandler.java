package com.github.stevenkin.jim.gateway.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.stevenkin.jim.gateway.config.GatewayProperties;
import com.github.stevenkin.jim.gateway.service.EncryptKeyService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Set;

import static com.github.stevenkin.serialize.Constant.CLIENT_PUBLIC_KEY;

@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final GatewayProperties config;
    private final EncryptKeyService encryptKeyService;
    private static ByteBuf faviconByteBuf = null;
    private static ByteBuf notFoundByteBuf = null;
    private static ByteBuf badRequestByteBuf = null;
    private static ByteBuf forbiddenByteBuf = null;
    private static ByteBuf internalServerErrorByteBuf = null;


    private static ByteBuf buildStaticRes(String resPath) {
        try {
            InputStream inputStream = HttpServerHandler.class.getResourceAsStream(resPath);
            if (inputStream != null) {
                int available = inputStream.available();
                if (available != 0) {
                    byte[] bytes = new byte[available];
                    inputStream.read(bytes);
                    return ByteBufAllocator.DEFAULT.buffer(bytes.length).writeBytes(bytes);
                }
            }
        } catch (Exception var4) {
        }

        return null;
    }

    public HttpServerHandler(GatewayProperties config, EncryptKeyService service) {
        this.config = config;
        this.encryptKeyService = service;
    }

    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        try {
            this.handleHttpRequest(ctx, msg);
        } catch (Exception var5) {
            DefaultFullHttpResponse res;
            if (internalServerErrorByteBuf != null) {
                res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, internalServerErrorByteBuf.retainedDuplicate());
            } else {
                res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }

            sendHttpResponse(ctx, msg, res);
            var5.printStackTrace();
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ChannelId id = ctx.channel().id();
        log.error("channel {} error", id.toString());
        cause.printStackTrace();
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelId id = ctx.channel().id();
        log.error("channel {} close", id.toString());
        super.channelInactive(ctx);
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        DefaultFullHttpResponse res;
        if (!req.decoderResult().isSuccess()) {
            if (badRequestByteBuf != null) {
                res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST, badRequestByteBuf.retainedDuplicate());
            } else {
                res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            }

            sendHttpResponse(ctx, req, res);
        } else if (req.method() != HttpMethod.GET) {
            if (forbiddenByteBuf != null) {
                res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, forbiddenByteBuf.retainedDuplicate());
            } else {
                res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
            }

            sendHttpResponse(ctx, req, res);
        } else {
            HttpHeaders headers = req.headers();
            String host = headers.get(HttpHeaderNames.HOST);
            if (StringUtils.isEmpty(host)) {
                if (forbiddenByteBuf != null) {
                    res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, forbiddenByteBuf.retainedDuplicate());
                } else {
                    res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
                }

                sendHttpResponse(ctx, req, res);
            } else if (!StringUtils.isEmpty(config.getHost()) && !config.getHost().equals("0.0.0.0") && !config.getHost().equals(host.split(":")[0])) {
                if (forbiddenByteBuf != null) {
                    res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, forbiddenByteBuf.retainedDuplicate());
                } else {
                    res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
                }

                sendHttpResponse(ctx, req, res);
            } else {
                String uri = req.uri();
                int index = uri.indexOf("?");
                String path = null;
                String originalParam = null;
                if (index == -1) {
                    path = uri;
                } else {
                    path = uri.substring(0, index);
                    originalParam = uri.substring(index + 1, uri.length());
                }

                final String param = originalParam;

                if ("/favicon.ico".equals(path)) {
                    if (faviconByteBuf != null) {
                        res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, faviconByteBuf.retainedDuplicate());
                    } else {
                        res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
                    }

                    sendHttpResponse(ctx, req, res);
                } else {
                    if (config.getPathSet() != null && !config.getPathSet().isEmpty() && !config.getPathSet().contains(path)) {
                        if (notFoundByteBuf != null) {
                            res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, notFoundByteBuf.retainedDuplicate());
                        } else {
                            res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
                        }

                        sendHttpResponse(ctx, req, res);
                    } else if (req.headers().contains(HttpHeaderNames.UPGRADE) && req.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_KEY) && req.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_VERSION)) {
                        Channel channel = ctx.channel();
                        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), (String)null, true, this.config.getmaxFramePayloadLength());
                        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
                        if (handshaker == null) {
                            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channel);
                        } else {
                            ChannelPipeline pipeline = ctx.pipeline();
                            pipeline.remove(ctx.name());
                            if (this.config.getReaderIdleTimeSeconds() != 0 || this.config.getWriterIdleTimeSeconds() != 0 || this.config.getAllIdleTimeSeconds() != 0) {
                                pipeline.addLast(new ChannelHandler[]{new IdleStateHandler(this.config.getReaderIdleTimeSeconds(), this.config.getWriterIdleTimeSeconds(), this.config.getAllIdleTimeSeconds())});
                            }

                            if (this.config.isUseCompressionHandler()) {
                                pipeline.addLast(new ChannelHandler[]{new WebSocketServerCompressionHandler()});
                            }

                            pipeline.addLast(new ChannelHandler[]{new WebSocketServerHandler()});
                            pipeline.addLast(new ChannelHandler[]{new DecryptFrameHandler(encryptKeyService)});
                            pipeline.addLast(new ChannelHandler[]{new EncryptFrameHandler(encryptKeyService)});
                            pipeline.addLast(new ChannelHandler[]{new GatewayServerHandler()});

                            HttpHeaders headers1 = new DefaultHttpHeaders().add("SERVER_PUBLIC_KEY", encryptKeyService.getPublicKey());
                            handshaker.handshake(channel, req, headers1, channel.newPromise()).addListener((future) -> {
                                if (future.isSuccess()) {
                                    ChannelId id = ctx.channel().id();
                                    log.info("channel {} handshake success", id.toString());
                                    ParameterMap parameterMap = null;
                                    if (StringUtils.isEmpty(param)) {
                                        log.error("build ws connection need client public key");
                                        handshaker.close(channel, new CloseWebSocketFrame());
                                    }
                                    parameterMap = new ParameterMap(param);
                                    if (StringUtils.isEmpty(parameterMap.getParameter(CLIENT_PUBLIC_KEY))) {
                                        log.error("build ws connection need client public key");
                                        handshaker.close(channel, new CloseWebSocketFrame());
                                    }
                                    String s = parameterMap.getParameter(CLIENT_PUBLIC_KEY);
                                    AttributeKey<String> clientPublicKey = AttributeKey.valueOf(CLIENT_PUBLIC_KEY);
                                    Attribute<String> attr = channel.attr(clientPublicKey);
                                    attr.set(s);

                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("SERVER_PUBLIC_KEY", encryptKeyService.getPublicKey());
                                    String jsonString = jsonObject.toJSONString();
                                    TextWebSocketFrame frame = new TextWebSocketFrame(jsonString);
                                    channel.writeAndFlush(frame);
                                } else {
                                    handshaker.close(channel, new CloseWebSocketFrame());
                                }

                            });
                        }

                    } else {
                        if (forbiddenByteBuf != null) {
                            res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, forbiddenByteBuf.retainedDuplicate());
                        } else {
                            res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
                        }

                        sendHttpResponse(ctx, req, res);
                    }
                }
            }
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        int statusCode = res.status().code();
        if (statusCode != HttpResponseStatus.OK.code() && res.content().readableBytes() == 0) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        HttpUtil.setContentLength(res, (long)res.content().readableBytes());
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || statusCode != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HttpHeaderNames.HOST) + req.uri();
        return "ws://" + location;
    }

    static {
        faviconByteBuf = buildStaticRes("/favicon.ico");
        notFoundByteBuf = buildStaticRes("/public/error/404.html");
        badRequestByteBuf = buildStaticRes("/public/error/400.html");
        forbiddenByteBuf = buildStaticRes("/public/error/403.html");
        internalServerErrorByteBuf = buildStaticRes("/public/error/500.html");
        if (notFoundByteBuf == null) {
            notFoundByteBuf = buildStaticRes("/public/error/4xx.html");
        }

        if (badRequestByteBuf == null) {
            badRequestByteBuf = buildStaticRes("/public/error/4xx.html");
        }

        if (forbiddenByteBuf == null) {
            forbiddenByteBuf = buildStaticRes("/public/error/4xx.html");
        }

        if (internalServerErrorByteBuf == null) {
            internalServerErrorByteBuf = buildStaticRes("/public/error/5xx.html");
        }

    }
}
