package com.hystrix.basic.services;

import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class SlowFailingService {
    private static final Logger LOG = Logger.getLogger(SlowFailingService.class);

    public String getData() {
        return getData(0);
    }

    public String getData(int sleepTime) {
        LOG.info("About to get data from slow and failing service.");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Unable to get data!");
    }
}
