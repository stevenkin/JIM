package com.github.stevenkin.jim.gateway.router;

import com.github.stevenkin.serialize.Frame;
import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Slf4j
@Component
public class RouteDistributor {
    @Autowired
    private RouteStrategy routeStrategy;
    @Autowired
    private BusinessServerCluster businessServerCluster;

    public void distributor(Package pkg) {
        String command = pkg.getHeader().getCommand();
        List<Server> cluster = businessServerCluster.getCluster(command);
        List<Server> servers = routeStrategy.choose(cluster, pkg);
        for (Server server : servers) {
            server.write(pkg);
        }
    }
}
