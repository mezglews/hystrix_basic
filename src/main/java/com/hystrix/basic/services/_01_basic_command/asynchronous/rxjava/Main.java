package com.hystrix.basic.services._01_basic_command.asynchronous.rxjava;

import com.hystrix.basic.services.RemoteService;
import com.hystrix.basic.services._01_basic_command.SimpleCommand;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutionException;

/**
 * User: Szymon Mezglewski
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private static final int SLEEP_TIME = 0;
    private static final boolean SHOULD_FAIL = false;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RemoteService remoteService = new RemoteService(SLEEP_TIME, SHOULD_FAIL);

        SimpleCommand command = new SimpleCommand(remoteService);

        command.toObservable()
                .subscribe(next -> LOG.info("Got response: '" + next + "'"));

        Thread.sleep(1000);
    }

}
