package com.github.stevenkin.jim.gateway.router;

import com.github.stevenkin.jim.forward.NetworkUtil;
import com.github.stevenkin.jim.forward.ServerAuthService;
import com.github.stevenkin.serialize.Frame;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;

@Component
@Slf4j
public class BusinessAuthHandler implements RouteFrameHandler {
    @Autowired
    private BusinessServerCluster businessServerCluster;
    @Autowired
    private ServerAuthService serverAuthService;
    @Autowired
    private ScheduledExecutorService scheduledExecutorService;
    @Value("${jim.appName}")
    private String appName;
    @Value("${jim.appToken}")
    private String appToken;
    @Value("${router.port}")
    private Integer port;

    @Override
    public boolean isMatch(Frame frame) {
        return frame.getOpCode() == 0x1;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Frame frame) throws Exception {
        String appName = frame.getControl().getDestAppName();
        String appToken = frame.getControl().getDestAppToken();
        boolean auth = serverAuthService.auth(appName, appToken);
        Frame frame1 = new Frame();
        Frame.Control control = new Frame.Control();
        control.setDestIP(frame.getControl().getSourceIP());
        control.setDestPort(frame.getControl().getSourcePort());
        control.setSourceIP(frame.getControl().getDestIP());
        control.setSourcePort(frame.getControl().getDestPort());
        control.setTimestamp(System.currentTimeMillis());
        frame1.setControl(control);
        if (auth) {
            frame1.setOpCode(0x21);
            ServerInfo info = new ServerInfo();
            info.setAppName(control.getSourceAppName());
            info.setAppToken(control.getSourceAppToken());
            info.setServerIP(control.getSourceIP());
            info.setServerPort(control.getSourcePort());
            info.setAppThreadNum(control.getAppThreadNum());
            info.setAppWaitTaskNum(control.getAppWaitTaskNum());
            Server server = new BusinessServer(ctx.channel(), scheduledExecutorService, this.appName, this.appToken, info, NetworkUtil.getLocalHost(), port);
            businessServerCluster.serverOnline(server);
        } else {
            log.error("app {}, token {} BusinessAuthHandler fail, frame {}", appName, appToken, frame);
            frame1.setOpCode(0x20);
        }
        ctx.writeAndFlush(frame1).addListener(ChannelFutureListener.CLOSE);
    }
}
