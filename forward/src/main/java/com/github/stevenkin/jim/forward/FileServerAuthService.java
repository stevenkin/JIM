package com.github.stevenkin.jim.forward;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

public class FileServerAuthService implements ServerAuthService, InitializingBean {
    private Map<String, Map<String, String>> serverAuth;

    @Override
    public boolean auth(String appName, String appToken) {
        return serverAuth.getOrDefault(appName, new HashMap<>()).getOrDefault("appToken", "").equals(appToken);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("server-auth.yml");
        Yaml yaml = new Yaml();
        Map<String, Object> config = yaml.loadAs(classPathResource.getInputStream(), Map.class);
        Map<String, Map<String, String>> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            map.put(entry.getKey(), (Map<String, String>) entry.getValue());
        }
        serverAuth = map;
    }
}
