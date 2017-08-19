package com.hystrix.basic.services;

import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 * Date: 2017-08-19
 */
public class WorkingService {
    private static final Logger LOG = Logger.getLogger(WorkingService.class);

    public String getData() {
        LOG.info("About to call remote service to get data.");
        return "data";
    }
}
