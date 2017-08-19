package com.hystrix.basic.services;

import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class RemoteService {
    private static final Logger LOG = Logger.getLogger(RemoteService.class);

    private final int sleepTime;
    private final boolean shouldFail;

    public RemoteService(int sleepTime, boolean shouldFail) {
        this.sleepTime = sleepTime;
        this.shouldFail = shouldFail;
    }

    public String getData() {
        LOG.info("About to call service over network do get data.");

        if(sleepTime > 0) {
            LOG.info("Simulating slow response. Sleep = " + sleepTime);
            sleep(sleepTime);
        }

        if(shouldFail) {
            throw new RuntimeException("Unable to get data!");
        }
        return "data";
    }

    private void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }
}
