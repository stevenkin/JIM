package com.github.stevenkin.jim.business.server.config;

import com.github.stevenkin.jim.business.server.utils.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

/**
 * 线程池配置
 *
 * @author ruoyi
 **/
@Configuration
public class ThreadPoolConfig
{
    // 核心线程池大小
    private int corePoolSize = 50;

    // 最大可创建的线程数
    private int maxPoolSize = 200;

    // 队列最大长度
    private int queueCapacity = 1000;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 300;

    @Bean(name = "executorService")
    public ExecutorService executorService() {
        return Executors.newExecutor(corePoolSize, maxPoolSize, queueCapacity, "business-");
    }
}
