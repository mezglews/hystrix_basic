package com.hystrix.basic.services;

import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 * Date: 2017-08-19
 */
public class SlowService {
    private static final Logger LOG = Logger.getLogger(SlowService.class);

    public String getData(int sleepTime) {
        LOG.info("About to get data from slow service.");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("Returning data from slow service.");
        return "data";
    }
}
