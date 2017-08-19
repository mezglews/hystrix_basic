package com.hystrix.basic.services._03_exceptions;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.exception.HystrixRuntimeException;
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
        } catch (HystrixRuntimeException e) {
            LOG.error("Command execution failure. Failure type="+ e.getFailureType(), e.getCause());
            /*
            BAD_REQUEST_EXCEPTION,
            COMMAND_EXCEPTION,
            TIMEOUT,
            SHORTCIRCUIT,
            REJECTED_THREAD_EXECUTION,
            REJECTED_SEMAPHORE_EXECUTION,
            REJECTED_SEMAPHORE_FALLBACK;
             */
        }
    }

}
