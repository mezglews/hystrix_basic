package com.hystrix.basic.services._04_configuration._3_circuit_breaker;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class SimpleCommand extends HystrixCommand<String> {
    private static final Logger LOG = Logger.getLogger(SimpleCommand.class);

    private final RemoteService remoteService;

    public SimpleCommand(RemoteService remoteService, int timeout) {

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("DefaultGroupName"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionTimeoutInMilliseconds(timeout)
                                .withCircuitBreakerEnabled(true) //default true
                                .withCircuitBreakerSleepWindowInMilliseconds(5000) //default 5000
                )
        );

        this.remoteService = remoteService;
    }

    @Override
    protected String run() throws Exception {
        LOG.info("Executing RemoteService-getData().");
        return remoteService.getData();
    }
}
