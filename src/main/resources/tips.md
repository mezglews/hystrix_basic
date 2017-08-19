# Hystrix

## 01. HystrixCommand
Log4j configuration for logging thread name and time
Every single call should be done in a new instance of hystrix command - they are not reusable.

##### 1.1 observe() vs execute() vs queue()
 **observe()**
`command.execute();`
Blocking execution in hystrix thread

**queue()**
`Future<Void> future = command.queue();
future.get();`
NonBlocking execution until we call get() on future

**toObservable() or observe()**
NonBlocking - asynchronous
Facade on HystrixCommand


##### 1.2 HystrixObservableCommand - observe(), toObservable()
`observe.subscribe(System.out::println);`
NonBlocking execution - notify subscriber once new element coming in
*Show that cannot execute() or queue() command*
*Add one more element to the SimpleObservableCommand() to show that ObservableCommand can return more than one element*
*Show which thread is used to run observable. observe() ignores hystrix threads and subscribeOn(). only toObservable() works*
`observe.subscribeOn(Schedulers.io()).subscribe(System.out::println);`
*FROM hystrix github: If you already have a non-blocking Observable, you don't need another thread*
*JavaDOC: Used to wrap code that will execute potentially risky functionality (typically meaning a service call over the network) with fault and latency tolerance, statistics and performance metrics capture, circuit breaker and bulkhead functionality. This command should be used for a purely non-blocking call pattern. The caller of this command will be subscribed to the Observable returned by the run() method.*

##### 1.3 Reusing command
*Show that hystrix command cannot be reused what it was executed*
`        try {
             command.execute();
             command.execute();
         } catch (HystrixRuntimeException e) {
             logger.error(e);
         }
         `

##### 1.4 Hystrix observeOn() and subsequent operations
*executing heavy operation - without observeOn everything is executed on hystrix thread*


## 2. Command configuration
Run Main and show whats logged etc

##### 2.1 Basic configuration
*andCommandKey()*
Sets the name of the command executed - by default is the class name

*andThreadPoolKey()*
Default ThreadPoolName is taken from HystrixCommandGroupKey
If we logically want these commands grouped together but want them isolated differently then we would use HystrixThreadPoolKey to give each of them a different thread-pool.

*andThreadPoolPropertiesDefaults - coreSize()*
Discussed in chapter 4 - settings for thread pool and command execution strategy

#####2.2 CB properties
https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/circuit-breaker-1280.png

CB properties

*withCircuitBreakerRequestVolumeThreshold()*
Minimum number of requests in the metricsRollingStatisticalWindowInMilliseconds() that must exist before the HystrixCircuitBreaker will trip.
This property sets the minimum number of requests in a rolling window that will trip the circuit.

For example, if the value is 20, then if only 19 requests are received in the rolling window (say a window of 10 seconds) the circuit will not trip open even if all 19 failed.

*.withCircuitBreakerErrorThresholdPercentage()*
Error percentage threshold (as whole number such as 50) at which point the circuit breaker will trip open and reject requests.

*withMetricsRollingStatisticalWindowInMilliseconds()*
Duration of statistical rolling window in milliseconds.

*withCircuitBreakerSleepWindowInMilliseconds()*
The time in milliseconds after a HystrixCircuitBreaker trips open that it should wait before trying requests again.

*.withCircuitBreakerForceOpen(true)* and *HystrixCircuitBreaker hystrixCircuitBreaker = HystrixCircuitBreaker.Factory.getInstance(HystrixCommandKey.Factory.asKey("CommandKey"));
                                                  System.out.println(hystrixCircuitBreaker.allowRequest());*
Show than we can query the state of the command (even future)

## 3. Fallback
Show exceptions when no fallback available and default response for command with fallback
Will back to fallback when we discuss exceptions in hystrix
Show how to get exception in fallback: `getFailedExecutionException()`

## 4. Hystrix thread pools
##### 4.1 own thread pools (coreSize, maximumSize with withAllowMaximumSizeToDivergeFromCoreSize, queueSize) rejections
Modify coreSize to show how many tasks were executed

`.withMaximumSize(3)
.withAllowMaximumSizeToDivergeFromCoreSize(true)`
Both properties has to be set together

`withKeepAliveTimeMinutes()`
If coreSize &lte; maximumSize then it sets the time when extra threads will go after they will be removed

`withMaxQueueSize()`
Adds the queue where command can be shelved for further execution

`withQueueSizeRejectionThreshold()`
Queue size rejection threshold is an artificial "max" size at which rejections will occur even if maxQueueSize has not been reached.
MaxQueueSize is a size of BlockingQueue and cannot be changed. But you can change artificial max by setting queuesizeRejection threshold.

Starting thread pool size is 1, core pool size is 5, max pool size is 10 and the queue is 100. As requests come in threads will be created up to 5, then tasks will be added to the queue until it reaches 100. When the queue is full new threads will be created up to maxPoolSize. Once all the threads are in use and the queue is full tasks will be rejected. As the queue reduces, so does the number of active threads.

#####4.2 isolation strategy - semaphore vs thread
THREAD: Execute the HystrixCommand.run() method on a separate thread and restrict concurrent executions using the thread-pool size.
SEMAPHORE: Execute the HystrixCommand.run() method on the calling thread and restrict concurrent executions using the semaphore permit count.

######4.2.1 - thread
Show thread names used to execute commands

`simpleCommand.queue();`
Show rejected command when core size is exceeded

`.withExecutionTimeoutInMilliseconds(200)`
Timeout for command execution - default 1 second

######4.2.1 - semaphore
Show thread names used to execute command (no thread pool used)
Show what will happen when provide different command key like using counter - use random
`withExecutionIsolationSemaphoreMaxConcurrentRequests(10)`

######4.2.3 - semaphore vs thread in terms of time out
Show difference between thread isolation  and semaphore.
For thread isolation a timeout will occur immediately after given time passed
For semaphore task will finish (even long running)  and after execution timeout exception will occur


#####4.3 ignoring rxjava observeOn() and subscribeOn() - executing observable in hystrixCommand, observeOn() execuites post hystrix in other thread
Run without any operator - Executed in hystrix thread pool

`.subscribeOn(Schedulers.computation())`
Executed (emission) in hystrix thread pool - hystrix ignores this

`.observeOn(Schedulers.computation())`
Executes (emission) in hystrix thread pool. ObserveOn just changes the thread for other operators - post hystrix -  here for printing



## 5. Hystrix error handling
##### 5.1 HystrixRuntimeException (failure types)
        - command exception,
        - timeout (add more than 1 sec),
        - shortcircuit (uncomment withCircuitBreakerForceOpen)
        - rejectedThreadExecution (ToManyCommandsMain)
##### 5.2 HystrixBadRequestException (fallback)
Show failing command
https://netflix.github.io/Hystrix/javadoc/com/netflix/hystrix/exception/HystrixBadRequestException.html
An exception representing an error with provided arguments or state rather than an execution failure.
Unlike all other exceptions thrown by a HystrixCommand this will not trigger fallback, not count against failure metrics and thus not trigger the circuit breaker.

NOTE: This should only be used when an error is due to user input such as IllegalArgumentException otherwise it defeats the purpose of fault-tolerance and fallback behavior.

Use FailingCommandWithFallback - fallback is useless here as exception type is HystrixBADRequestException

##### 5.3 unwrapHystrixException() - show code from UMG ?

## 6. Request caching

##### 6.1 Request caching
 - use HystrixRequestContext, show what happened when close context and reinit
Run it and show that cache does not work. Commands are executed every time

`HystrixRequestContext context = HystrixRequestContext.initializeContext();`
Since this depends on request context we must initialize the HystrixRequestContext.

Show that only 2 tasks were executed on hystrix thread pool. Third one was cached

##### 6.2 Invalidating cache - GET-SET-GET
If you are implementing a Get-Set-Get use case where the Get receives enough traffic that request caching is desired but sometimes a Set occurs on another command that should invalidate the cache within the same request, you can invalidate the cache by calling HystrixRequestCache.clear().


## 7. Request batching/collapsing
        - configure withTimerDelayInMilliseconds (delay), and withMaxRequestsInBatch, without HystrixRequestContext no exception!!

## 8. Issue with MDC
http://netflix.github.io/Hystrix/javadoc/com/netflix/hystrix/strategy/concurrency/HystrixConcurrencyStrategy.html#wrapCallable(java.util.concurrent.Callable)


## 9. Hystrix dashboard (open Web project, run Hystrix Dasbhoard from CMD, us ab.exe)

ab -n 20 -c 10 "http://localhost:8080/failing?probability=0.8&sleep=3100"