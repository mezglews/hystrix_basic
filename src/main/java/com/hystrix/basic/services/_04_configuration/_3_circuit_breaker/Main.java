package com.hystrix.basic.services._04_configuration._3_circuit_breaker;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private static final int SLEEP_TIME = 10;
    private static final int TIMEOUT = 1000;
    private static final boolean SHOULD_FAIL = true;
    private static final int SLEEP_BETWEEN_REQUESTS = 100;

    public static void main(String[] args) throws InterruptedException {
        RemoteService remoteService = new RemoteService(SLEEP_TIME, SHOULD_FAIL);

        for(int i = 0; i<100; i++) {
            SimpleCommand command = new SimpleCommand(remoteService, TIMEOUT);
            try {
                LOG.info("About to execute command.");
                String result = command.execute();
                LOG.info("Got response: '" + result + "'");
            } catch (HystrixRuntimeException e) {
                LOG.error("Command execution failure. Reason: " + e.getFailureType());
            }
            Thread.sleep(SLEEP_BETWEEN_REQUESTS);
        }
    }

}
