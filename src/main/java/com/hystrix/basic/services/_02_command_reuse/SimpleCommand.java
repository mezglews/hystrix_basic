package com.hystrix.basic.services._02_command_reuse;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class SimpleCommand extends HystrixCommand<String> {
    private static final Logger LOG = Logger.getLogger(SimpleCommand.class);

    private final RemoteService remoteService;

    public SimpleCommand(RemoteService remoteService) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixCommandGroup")));
        this.remoteService = remoteService;
    }

    @Override
    protected String run() throws Exception {
        LOG.info("Executing RemoteService-getData().");
        return remoteService.getData();
    }
}
