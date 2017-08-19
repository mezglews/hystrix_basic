package com.hystrix.basic.services._03_exceptions.business;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private static final int SLEEP_TIME = 0;
    private static final boolean SHOULD_FAIL = true;

    public static void main(String[] args) {
        RemoteService remoteService = new RemoteService(SLEEP_TIME, SHOULD_FAIL);

        SimpleCommand command = new SimpleCommand(remoteService);

        try {
            String result = command.execute();
            LOG.info("Got response: '" + result + "'");
        } catch (HystrixBadRequestException e) {
            LOG.error("Command execution failure because of client bad request", e.getCause());
        }
    }

}
