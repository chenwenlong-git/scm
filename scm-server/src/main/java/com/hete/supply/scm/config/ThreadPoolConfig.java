package com.hete.supply.scm.config;

import com.hete.supply.scm.common.constant.ScmConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author rockyHuas
 * @date 2023/05/15 10:22
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(ScmConstant.THREAD_POOL_NAME)
    public ThreadPoolTaskExecutor splitGetFuturePool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(0);
        executor.setKeepAliveSeconds(5);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("@SplitGetFuturePool-");
        executor.initialize();
        return executor;
    }
}