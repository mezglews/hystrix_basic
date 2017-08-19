package com.hystrix.basic.services._03_exceptions.business;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class SimpleCommand extends HystrixCommand<String> {
    private static final Logger LOG = Logger.getLogger(SimpleCommand.class);

    private final RemoteService remoteService;

    public SimpleCommand(RemoteService remoteService) {
        super(HystrixCommandGroupKey.Factory.asKey("HystrixCommandGroup"));
        this.remoteService = remoteService;
    }

    @Override
    protected String run() throws Exception {
        LOG.info("Executing RemoteService-getData().");
        try {
            String data = remoteService.getData();
            return data;
        } catch (Exception e) {
            throw new HystrixBadRequestException("Some business exception occurred. ", e);
        }
    }
}
