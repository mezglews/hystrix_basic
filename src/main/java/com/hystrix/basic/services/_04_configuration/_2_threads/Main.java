package com.hystrix.basic.services._04_configuration._2_threads;

import com.hystrix.basic.services.RemoteService;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Szymon Mezglewski
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);
    private static final int TIMEOUT = 1000;

    private static final int SLEEP_TIME = 50;
    private static final boolean SHOULD_NOT_FAIL = false;
    private static final int TOTAL_THREADS = 15;

    public static void main(String[] args) throws InterruptedException {
        RemoteService remoteService = new RemoteService(SLEEP_TIME, SHOULD_NOT_FAIL);


        for(int i = 0; i< TOTAL_THREADS; i++) {
            final AtomicInteger counter = new AtomicInteger(i);

            Thread tomcatThread = new Thread(() -> {
                SimpleCommand command = new SimpleCommand(remoteService, TIMEOUT, TOTAL_THREADS);
                LOG.info("About to execute command #" + counter);
                try {
                    command.observe();
                } catch (HystrixRuntimeException e) {
                    LOG.error("Command execution failure. Reason: " + e.getFailureType());
                }
            });
            tomcatThread.start();

        }

        Thread.sleep(10_000);

    }

}
