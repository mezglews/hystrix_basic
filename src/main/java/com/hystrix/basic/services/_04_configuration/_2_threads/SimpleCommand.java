package com.hystrix.basic.services._04_configuration._2_threads;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class SimpleCommand extends HystrixCommand<String> {
    private static final Logger LOG = Logger.getLogger(SimpleCommand.class);

    private final RemoteService remoteService;

    public SimpleCommand(RemoteService remoteService, int timeout, int totalThreads) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("DefaultGroupName"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(totalThreads))
        );

        this.remoteService = remoteService;
    }

    @Override
    protected String run() throws Exception {
        LOG.info("Executing RemoteService-getData().");
        return remoteService.getData();
    }
}
