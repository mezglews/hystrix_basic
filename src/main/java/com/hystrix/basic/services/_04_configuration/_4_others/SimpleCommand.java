package com.hystrix.basic.services._04_configuration._4_others;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.*;
import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class SimpleCommand extends HystrixCommand<String> {
    private static final Logger LOG = Logger.getLogger(SimpleCommand.class);

    private final RemoteService remoteService;

    public SimpleCommand(RemoteService remoteService) {

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("DefaultGroupName"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("CommandName"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ThreadPoolName"))
        );
        this.remoteService = remoteService;
    }

    @Override
    protected String run() throws Exception {
        LOG.info("Executing RemoteService-getData().");
        return remoteService.getData();
    }
}
