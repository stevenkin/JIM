package com.github.stevenkin.jim.router;

import com.github.stevenkin.serialize.Frame;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KeepaliveFrameHandler implements RouteFrameHandler{
    @Autowired
    private BusinessServerCluster businessServerCluster;

    @Override
    public boolean isMatch(Frame frame) {
        return frame.getOpCode() == 0x6;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, Frame frame) throws Exception {
        Frame.Control control = frame.getControl();
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setAppName(control.getSourceAppName());
        serverInfo.setAppToken(control.getSourceAppToken());
        serverInfo.setAppWaitTaskNum(control.getAppWaitTaskNum());
        serverInfo.setAppThreadNum(control.getAppThreadNum());
        serverInfo.setClientIP(control.getSourceIP());
        serverInfo.setClientPort(control.getSourcePort());
        businessServerCluster.update(serverInfo);
    }
}
