package com.github.stevenkin.jim.business.server;

import com.github.stevenkin.jim.business.server.business.BusinessContext;
import com.github.stevenkin.jim.business.server.business.BusinessHandler;
import com.github.stevenkin.jim.business.server.utils.ExecutorUtil;
import com.github.stevenkin.serialize.Package;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Slf4j
public class BusinessDistributor implements Runnable {
    private ApplicationContext applicationContext;

    private ExecutorService executorService;

    private Collection<BusinessHandler> businessHandlers;

    private Map<String, BusinessContext> businessContextMap;

    public BusinessDistributor( ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        Map<String, BusinessHandler> beansOfType = applicationContext.getBeansOfType(BusinessHandler.class);
        Collection<BusinessHandler> values = beansOfType.values();
        this.businessHandlers = values;
        this.executorService = ExecutorUtil.newExecutor(100, 10000, "businessProcess-");
        this.businessContextMap = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        for (; ;) {
            try {
                Package aPackage = null;//TODO 需要重新实现
                executorService.submit(() -> {
                    String sessionId = aPackage.getHeader().getSessionId();
                    BusinessContext ctx = businessContextMap.computeIfAbsent(sessionId, (key) -> new BusinessContext());
                    if (businessHandlers != null && !businessHandlers.isEmpty()) {
                        businessHandlers.forEach(b -> {
                            if (b.isSupport(aPackage)) {
                                b.process(ctx, aPackage);
                            }
                        });
                    }
                });
            } catch (Exception e) {
                log.error("mqConsumer poll error", e);
            }
        }
    }
}
