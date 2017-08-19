package com.hystrix.basic.services._02_command_reuse;

import com.hystrix.basic.services.RemoteService;
import com.hystrix.basic.services._01_basic_command.SimpleCommand;
import org.apache.log4j.Logger;

/**
 * User: Szymon Mezglewski
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private static final int SLEEP_TIME = 0;
    private static final boolean SHOULD_FAIL = false;

    public static void main(String[] args) {
        RemoteService remoteService = new RemoteService(SLEEP_TIME, SHOULD_FAIL);

        SimpleCommand command = new SimpleCommand(remoteService);

        String result = command.execute();
        LOG.info("Got response: '" + result + "'");

        String result2 = command.execute();
    }

}
