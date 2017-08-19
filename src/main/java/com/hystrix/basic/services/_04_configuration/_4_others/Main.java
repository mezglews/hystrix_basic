package com.hystrix.basic.services._04_configuration._4_others;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private static final int SLEEP_TIME = 3000;
    private static final int TIMEOUT = 1000;
    private static final boolean SHOULD_NOT_FAIL = false;

    public static void main(String[] args) {
        RemoteService remoteService = new RemoteService(SLEEP_TIME, SHOULD_NOT_FAIL);

        SimpleCommand command = new SimpleCommand(remoteService, TIMEOUT);

        LOG.info("About to execute command.");
        try {
            String result = command.execute();
            LOG.info("Got response: '" + result + "'");
        } catch (HystrixRuntimeException e) {
            LOG.error("Command execution failure. Reason: " + e.getFailureType());
        }
    }

}
