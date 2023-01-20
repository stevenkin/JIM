package com.github.stevenkin.jim.gateway.router;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BusinessServerCluster {
    private List<Server> servers = new ArrayList<>();

    private Timer clear = new Timer();

    public BusinessServerCluster() {
        clear.schedule(new TimerTask() {
            @Override
            public void run() {
                List<Server> servers1 = new ArrayList<>();
                servers.forEach(s -> {
                    if (!s.isActive()) {
                        s.channel().close();
                    } else {
                        servers1.add(s);
                    }
                });
                servers = servers1;
            }
        }, 5000L);
    }

    public List<Server> getCluster() {
        return new ArrayList<>(servers);
    }

    public void serverOnline(Server server) {
        servers.add(server);
    }

    public void update(ServerInfo serverInfo) {
        Optional<Server> optionalServer = servers.stream().filter(s -> s.info().getAppName().equals(serverInfo.getAppName()) && s.info().getAppToken().equals(serverInfo.getAppToken()))
                .findFirst();
        if (optionalServer.isPresent()) {
            optionalServer.get().update(serverInfo);
        }
     }
}
